package nu.mine.kino.jenkins.plugins.projectmanagement;

import hudson.model.Action;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nu.mine.kino.entity.EVMViewBean;
import nu.mine.kino.entity.Holiday;
import nu.mine.kino.entity.Project;
import nu.mine.kino.jenkins.plugins.projectmanagement.utils.PMUtils;
import nu.mine.kino.projects.ProjectException;
import nu.mine.kino.projects.utils.ProjectUtils;
import nu.mine.kino.projects.utils.ReadUtils;

/**
 * プロジェクトのトップページに表示するべきデータを集計するアクション。 あまりビルドに依存しない時系列の情報を整理して表示する。
 * 
 * @author Masatomi KINO
 * @version $Revision$
 */
public class ProjectSummaryProjectAction implements Action {

    private static final String seriesFileNameSuffix = PMConstants.SERIES_DAT_FILENAME;

    private final AbstractProject<?, ?> project;

    private FORMAT format;// リスト表示か、グラフ表示かのフラグ

    public ProjectSummaryProjectAction(AbstractProject<?, ?> project) {
        this.project = project;
    }

    public AbstractProject<?, ?> getProject() {
        return project;
    }

    public String getIconFileName() {
        String ret = null;
        switch (format) {
        case LIST:
            ret = "/plugin/project-management/images/24x24/application_view_detail.png";
            break;
        case GRAPH:
            ret = "/plugin/project-management/images/24x24/chart_line.png";
            break;
        default:
            break;
        }
        return ret;
    }

    public String getDisplayName() {
        return String.format("プロジェクトサマリー(%s)", format.getName());
    }

    public String getUrlName() {
        return "project-summary" + "-" + format.getUrlSuffix();
    }

    public void setFormat(FORMAT format) {
        this.format = format;
    }

    public ProjectSummaryAction[] getSeriesActions() throws IOException {
        // List<ProjectSummaryAction> actions = new
        // ArrayList<ProjectSummaryAction>();
        // String file = PMConstants.BASE + "_" + seriesFileNameSuffix;
        // AbstractBuild<?, ?> build = PMUtils.findBuild(project, file);
        // if (build == null) {
        // return new ProjectSummaryAction[0];
        // }
        // String data = ReadUtils.readFile(new File(build.getRootDir(), file));
        // BufferedReader reader = new BufferedReader(new StringReader(data));
        // String line;
        // while ((line = reader.readLine()) != null) {
        // String[] split = line.split("\t");
        // String buildNumber = split[1];
        // AbstractBuild<?, ?> record = project.getBuildByNumber(Integer
        // .parseInt(buildNumber));
        // actions.add(record.getAction(ProjectSummaryAction.class));
        // }
        // return actions.toArray(new ProjectSummaryAction[actions.size()]);
        return this.getSeriesActionsWithPrefix(PMConstants.BASE);
    }

    public EVMViewBean[] getEVMViewBeans() throws IOException {
        return this.getGraphSeriesActionsWithPrefix(PMConstants.BASE);
    }

    public ProjectSummaryAction[] getSeriesActionsBase() throws IOException {
        return getSeriesActions();
    }

    public ProjectSummaryAction[] getSeriesActionsBase1() throws IOException {
        return this.getSeriesActionsWithPrefix(PMConstants.BASE + "1");
    }

    public ProjectSummaryAction[] getSeriesActionsBase2() throws IOException {
        return this.getSeriesActionsWithPrefix(PMConstants.BASE + "2");
    }

    public ProjectSummaryAction[] getSeriesActionsWithPrefix(String prefix)
            throws IOException {
        List<ProjectSummaryAction> actions = new ArrayList<ProjectSummaryAction>();
        String file = prefix + "_" + seriesFileNameSuffix;
        AbstractBuild<?, ?> build = PMUtils.findBuild(project, file);
        if (build == null) {
            return new ProjectSummaryAction[0];
        }
        String data = ReadUtils.readFile(new File(build.getRootDir(), file));
        BufferedReader reader = new BufferedReader(new StringReader(data));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] split = line.split("\t");
            String buildNumber = split[1];
            AbstractBuild<?, ?> record = project.getBuildByNumber(Integer
                    .parseInt(buildNumber));

            ProjectSummaryAction a = PMUtils.findActionByUrlEndsWith(record,
                    ProjectSummaryAction.class, PMConstants.BASE);
            if (a != null) {
                actions.add(a);
            }
        }
        return actions.toArray(new ProjectSummaryAction[actions.size()]);
    }

    /**
     * EVMチャート用の時系列データを返す。期間は、プロジェクトのファイルの開始日から終了日。
     * 時系列データのうち、EV/ACについてはプロジェクト上の実データ(締め時点データ)、
     * PVについては実データがある日付はそのデータ、実データがない場合は計算から算出されるデータ BACについては現状では、
     * 実データがある日付はそのデータ、ない場合は直近のBACを補完している。
     * 
     * @param prefix
     * @return
     * @throws IOException
     */
    public EVMViewBean[] getGraphSeriesActionsWithPrefix(String prefix)
            throws IOException {
        Map<Date, EVMViewBean> actionsMap = new HashMap<Date, EVMViewBean>();

        String file = prefix + "_" + seriesFileNameSuffix;
        AbstractBuild<?, ?> build = PMUtils.findBuild(project, file);
        if (build == null) {
            return new EVMViewBean[0];
        }

        // まずは、実データを取得して、リスト(Map)へセット。
        String data = ReadUtils.readFile(new File(build.getRootDir(), file));
        BufferedReader reader = new BufferedReader(new StringReader(data));
        Double bac = Double.NaN;
        String line;
        while ((line = reader.readLine()) != null) {
            String[] split = line.split("\t");
            String buildNumber = split[1];
            AbstractBuild<?, ?> record = project.getBuildByNumber(Integer
                    .parseInt(buildNumber));

            ProjectSummaryAction a = PMUtils.findActionByUrlEndsWith(record,
                    ProjectSummaryAction.class, PMConstants.BASE);
            if (a != null) {
                EVMViewBean currentPVACEV = a.getCurrentPVACEV();
                actionsMap.put(currentPVACEV.getBaseDate(), currentPVACEV);

                if (Double.isNaN(bac)) {
                    bac = currentPVACEV.getBac();
                }

            }
        }
        // まずは、実データを取得して、リスト(Map)へセット。 以上

        // つづいて、PVとBACなどの計算値を、リスト(Map)へセット。
        Map<Date, Double> pvMap = calculateTotalPVOfProject(project,
                PMConstants.BASE);

        // つづいて、PVとBACなどの計算値を、リスト(Map)へセット。
        Map<Date, Double> bacMap = new HashMap<Date, Double>();
        bacMap = calculateBACOfProject(project, actionsMap, PMConstants.BASE);

        Set<Date> keySet = pvMap.keySet();
        for (Date date : keySet) {
            // なんちゃってBeanつくる。
            EVMViewBean evmViewBean = createEVMViewBean(pvMap.get(date),
                    bacMap.get(date), date);
            // すでにある基準日のデータは上書きしない。
            if (!actionsMap.containsKey(evmViewBean.getBaseDate())) {
                // かつ、休日でデータなしの場合も追加しない。
                if (!ProjectUtils.isHoliday(
                        getHolidays(project, PMConstants.BASE),
                        evmViewBean.getBaseDate())) {
                    actionsMap.put(evmViewBean.getBaseDate(), evmViewBean);
                }
            }
        }

        List<EVMViewBean> actions = new ArrayList<EVMViewBean>(
                actionsMap.values());
        // sort
        Collections.sort(actions, new Comparator<EVMViewBean>() {
            /**
             * 日付を過去 → 未来になるようにソート。。
             * 
             * @param arg0
             * @param arg1
             * @return
             */
            @Override
            public int compare(EVMViewBean arg0, EVMViewBean arg1) {
                Date baseDate0 = arg0.getBaseDate();
                Date baseDate1 = arg1.getBaseDate();
                return baseDate0.compareTo(baseDate1);
            }
        });
        return actions.toArray(new EVMViewBean[actions.size()]);
    }

    private EVMViewBean createEVMViewBean(Double pv, Double bac, Date date) {
        EVMViewBean evmViewBean = new EVMViewBean();
        evmViewBean.setBaseDate(date);
        evmViewBean.setPlannedValue(pv);
        evmViewBean.setBac(bac);
        evmViewBean.setActualCost(Double.NaN);
        evmViewBean.setEarnedValue(Double.NaN);
        evmViewBean.setCpi(Double.NaN);
        evmViewBean.setSpi(Double.NaN);
        return evmViewBean;
    }

    private Map<Date, Double> calculateTotalPVOfProject(
            AbstractProject<?, ?> jenkinsProject, String suffix)
            throws IOException {
        Project targetProject = getProject(jenkinsProject, suffix);
        Map<Date, Double> pvMap = ProjectUtils
                .calculateTotalPVOfProject(targetProject);
        return pvMap;
    }

    private Map<Date, Double> calculateBACOfProject(
            AbstractProject<?, ?> jenkinsProject,
            Map<Date, EVMViewBean> actionsMap, String suffix)
            throws IOException {
        Project targetProject = getProject(jenkinsProject, suffix);

        Map<Date, Double> bacMap = ProjectUtils.calculateBACOfProject(
                targetProject, actionsMap);
        return bacMap;
    }

    private Holiday[] getHolidays(AbstractProject<?, ?> jenkinsProject,
            String suffix) throws IOException {
        AbstractBuild<?, ?> record = jenkinsProject
                .getBuildByNumber(getBuildNumber());
        ProjectSummaryAction a = PMUtils.findActionByUrlEndsWith(record,
                ProjectSummaryAction.class, suffix);
        return a.getHolidays();
    }

    private Project getProject(AbstractProject<?, ?> jenkinsProject,
            String suffix) throws IOException {
        AbstractBuild<?, ?> record = jenkinsProject
                .getBuildByNumber(getBuildNumber());
        ProjectSummaryAction a = PMUtils.findActionByUrlEndsWith(record,
                ProjectSummaryAction.class, suffix);
        String fileName = a.getFileName();
        try {
            Project targetProject = a.getProject(fileName);
            return targetProject;
        } catch (ProjectException e) {
            throw new IOException(e);
        }
    }

    public EVMViewBean getCurrentPVACEV() {
        final AbstractBuild<?, ?> tb = project.getLastSuccessfulBuild();
        AbstractBuild<?, ?> b = project.getLastBuild();
        while (b != null) {
            ProjectSummaryAction a = PMUtils.findActionByUrlEndsWith(b,
                    ProjectSummaryAction.class, PMConstants.BASE);
            if (a != null)
                return a.getCurrentPVACEV();
            if (b == tb)
                // if even the last successful build didn't produce the test
                // result,
                // that means we just don't have any tests configured.
                return null;
            b = b.getPreviousBuild();
        }
        return null;
    }

    public Date getBaseDate() {
        final AbstractBuild<?, ?> tb = project.getLastSuccessfulBuild();
        AbstractBuild<?, ?> b = project.getLastBuild();
        while (b != null) {
            ProjectSummaryAction a = null;
            List<ProjectSummaryAction> actions = b
                    .getActions(ProjectSummaryAction.class);
            for (ProjectSummaryAction tmpA : actions) {
                if (tmpA.getUrlName().endsWith(PMConstants.BASE)) {
                    a = tmpA;
                }
            }
            if (a != null)
                return a.getBaseDate();
            if (b == tb)
                // if even the last successful build didn't produce the test
                // result,
                // that means we just don't have any tests configured.
                return null;
            b = b.getPreviousBuild();
        }
        return null;
    }

    public int getBuildNumber() {
        ProjectSummaryAction action = PMUtils.getMostRecentSummaryAction(
                project, PMConstants.BASE);
        if (action != null) {
            return action.getBuildNumber();
        }
        return 0;
    }

    // /**
    // * 左メニューのリンクを無理矢理下記の画面へ遷移させている。本来はindex.jellyに行く。
    // *
    // * @param request
    // * @param response
    // * @throws IOException
    // */
    // public void doIndex(final StaplerRequest request,
    // final StaplerResponse response) throws IOException {
    // AbstractBuild<?, ?> build = project.getLastSuccessfulBuild();
    //
    // String path = String.format("/job/%s/%d/%s", build.getProject()
    // .getName(), build.getNumber(), "project-summary");
    // // System.out.println(path);
    // // System.out.println(request.getContextPath());
    // response.sendRedirect2(request.getContextPath() + path);
    // }
}
