package nu.mine.kino.jenkins.plugins.projectmanagement;

import hudson.model.Action;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nu.mine.kino.entity.EVMViewBean;
import nu.mine.kino.jenkins.plugins.projectmanagement.utils.PMUtils;
import nu.mine.kino.projects.utils.ReadUtils;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

public class ProjectSummaryProjectAction implements Action {

    private static final String seriesFileNameSuffix = PMConstants.SERIES_DAT_FILENAME;

    private final AbstractProject<?, ?> project;

    public ProjectSummaryProjectAction(AbstractProject<?, ?> project) {
        this.project = project;
    }

    public AbstractProject<?, ?> getProject() {
        return project;
    }

    public String getIconFileName() {
        return "/plugin/project-management/images/24x24/user_suit.png";
        // return "graph.gif";
    }

    public String getDisplayName() {
        return "プロジェクトサマリー(時系列詳細)";
        // return Messages.SampleProjectAction_DisplayName();
    }

    public String getUrlName() {
        return "project-summary";
    }

    // public String getData() {
    // return "Sample Project Action!!";
    // }

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
            actions.add(record.getAction(ProjectSummaryAction.class));
        }
        return actions.toArray(new ProjectSummaryAction[actions.size()]);
    }

    public EVMViewBean getCurrentPVACEV() {
        ProjectSummaryAction action = getMostRecentSummaryAction();
        if (action != null) {
            return action.getCurrentPVACEV();
        }
        return null;

        // final AbstractBuild<?, ?> tb = project.getLastSuccessfulBuild();
        // AbstractBuild<?, ?> b = project.getLastBuild();
        // while (b != null) {
        // ProjectSummaryAction a = PMUtils.getProjectSummaryAction(b);
        // if (a != null)
        // return a.getCurrentPVACEV();
        // if (b == tb)
        // // if even the last successful build didn't produce the test
        // // result,
        // // that means we just don't have any tests configured.
        // return null;
        // b = b.getPreviousBuild();
        // }
        // return null;
    }

    public Date getBaseDate() {
        ProjectSummaryAction action = getMostRecentSummaryAction();
        if (action != null) {
            return action.getBaseDate();
        }
        return null;
        // final AbstractBuild<?, ?> tb = project.getLastSuccessfulBuild();
        // AbstractBuild<?, ?> b = project.getLastBuild();
        // while (b != null) {
        // ProjectSummaryAction a = PMUtils.getProjectSummaryAction(b);
        // if (a != null)
        // return a.getBaseDate();
        // if (b == tb)
        // // if even the last successful build didn't produce the test
        // // result,
        // // that means we just don't have any tests configured.
        // return null;
        // b = b.getPreviousBuild();
        // }
        // return null;
    }

    public int getBuildNumber() {
        ProjectSummaryAction action = getMostRecentSummaryAction();
        if (action != null) {
            return action.getBuildNumber();
        }
        return 0;
    }

    public ProjectSummaryAction getMostRecentSummaryAction() {
        final AbstractBuild<?, ?> tb = project.getLastSuccessfulBuild();
        AbstractBuild<?, ?> b = project.getLastBuild();
        while (b != null) {
            ProjectSummaryAction a = PMUtils.getProjectSummaryAction(b);
            if (a != null)
                return a;
            if (b == tb)
                // if even the last successful build didn't produce the test
                // result,
                // that means we just don't have any tests configured.
                return null;
            b = b.getPreviousBuild();
        }
        return null;
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
