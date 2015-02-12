package nu.mine.kino.jenkins.plugins.projectmanagement;

import hudson.AbortException;
import hudson.Extension;
import hudson.FilePath;
import hudson.FilePath.FileCallable;
import hudson.Launcher;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.remoting.VirtualChannel;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;
import nu.mine.kino.entity.Project;
import nu.mine.kino.jenkins.plugins.projectmanagement.utils.PMUtils;
import nu.mine.kino.projects.ACCreator;
import nu.mine.kino.projects.EVCreator;
import nu.mine.kino.projects.JSONProjectCreator;
import nu.mine.kino.projects.PVCreator;
import nu.mine.kino.projects.ProjectException;
import nu.mine.kino.projects.ProjectWriter;
import nu.mine.kino.projects.utils.ProjectUtils;
import nu.mine.kino.projects.utils.ReadUtils;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Sample {@link Builder}.
 * 
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked and a new
 * {@link EVMToolsBuilder} is created. The created instance is persisted to the
 * project configuration XML by using XStream, so this allows you to use
 * instance fields (like {@link #name}) to remember the configuration.
 * 
 * <p>
 * When a build is performed, the
 * {@link #perform(AbstractBuild, Launcher, BuildListener)} method will be
 * invoked.
 * 
 * @author Masatomi KINO.
 */
public class EVMToolsBuilder extends Builder {

    private final String name;

    private static final String[] PREFIX_ARRAY = new String[] { "base_",
            "base1_", "base2_" };

    private final String addresses;

    private final boolean sendAll;

    private final boolean higawari;

    // Fields in config.jelly must match the parameter names in the
    // "DataBoundConstructor"
    @DataBoundConstructor
    public EVMToolsBuilder(String name, String addresses, boolean sendAll,
            boolean higawari) {
        this.name = name;
        this.addresses = addresses;
        this.sendAll = sendAll;
        this.higawari = higawari;
    }

    /**
     * We'll use this from the <tt>config.jelly</tt>.
     */
    public String getName() {
        return name;
    }

    public String getAddresses() {
        return addresses;
    }

    public boolean getSendAll() {
        return sendAll;
    }

    public boolean getHigawari() {
        return higawari;
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher,
            BuildListener listener) throws InterruptedException, IOException {

        listener.getLogger().println("[EVM Tools] 集計対象: " + name);
        FilePath root = build.getModuleRoot(); // ワークスペースのルート

        // 順番的に、日替わりチェックは、JSONファイルを作ってから行うようにした。
        FilePath buildRoot = new FilePath(build.getRootDir()); // このビルドのルート
        listener.getLogger().println("[EVM Tools] JSONファイル作成開始");
        FilePath pmJSON = executeAndCopies(root, buildRoot,
                new ProjectWriterExecutor(name, !higawari));
        listener.getLogger().println("[EVM Tools] 作成完了。ファイル名: " + pmJSON);

        // 日替わり運用をちゃんと行うのであれば。
        boolean higawariOKFlag = false;
        if (higawari) {
            listener.getLogger()
                    .println("[EVM Tools] Jenkins日替わり管理バージョンで稼働します");
            higawariOKFlag = checkHigawari(root, name, listener);
            if (!higawariOKFlag) {
                throw new AbortException("日替わりチェックでエラーとなったため、ビルドを停止します。");
            }
        }

        listener.getLogger().println("[EVM Tools] PVファイル作成開始");
        executeAndCopy(root, buildRoot, new PVCreatorExecutor(name));
        listener.getLogger().println("[EVM Tools] ACファイル作成開始");
        executeAndCopies(root, buildRoot, new ACCreatorExecutor(name));
        listener.getLogger().println("[EVM Tools] EVファイル作成開始");
        executeAndCopies(root, buildRoot, new EVCreatorExecutor(name));

        if (higawari && higawariOKFlag) { // 日替わり管理していて、かつ日替わりしていいよと言うことなので
            listener.getLogger().println(
                    "[EVM Tools] 集計も完了したので、前回取り込んだファイルを上書き保存します");
            // FilePath targetFile = new FilePath(root, name);
            // FilePath previousNewestFile = new FilePath(root,
            // targetFile.getName() + ".tmp"); // 前回取り込んだ最新ファイルへの参照
            // targetFile.copyTo(previousNewestFile); // 上書き。

            FilePath previousNewestJsonFile = new FilePath(root,
                    pmJSON.getName() + ".tmp"); // 前回取り込んだ最新ファイルへの参照
            pmJSON.copyTo(previousNewestJsonFile);
        }

        ProjectSummaryAction action = PMUtils.getProjectSummaryAction(build);
        action.setFileName(pmJSON.getName());

        File json = new File(build.getRootDir(), pmJSON.getName());
        // listener.getLogger().println(
        // "[EVM Tools] Project :" + json.getAbsolutePath());

        Project project = null;
        try {
            project = new JSONProjectCreator(json).createProject();
        } catch (ProjectException e) {
            listener.getLogger().println(e);
            throw new AbortException(e.getMessage());
        }

        PMUtils.checkProjectAndMail(project, addresses, build, listener,
                sendAll);

        // testMethod(build, listener);
        return true;
    }

    /**
     * @param root
     *            ワークスペースのルート
     * @param fileName
     *            集計対象Excelファイル
     * @param listener
     * @throws IOException
     * @throws InterruptedException
     */
    private boolean checkHigawari(FilePath root, String fileName,
            BuildListener listener) throws IOException, InterruptedException {
        // 元々あったファイルと、date.datの日付を見比べる必要あり。
        PrintStream logger = listener.getLogger();

        FilePath targetFile = new FilePath(root,
                ProjectUtils.findJSONFileName(fileName));
        FilePath previousNewestFile = new FilePath(root, targetFile.getName()
                + ".tmp"); // 前回取り込んだ最新ファイル(のJSONファイル)への参照

        String shimeFileName = PMConstants.DATE_DAT_FILENAME;
        FilePath shimeFile = new FilePath(root, shimeFileName); // 基準日ファイル

        logger.println("[EVM Tools] 前回取り込んだ最新ファイル(JSONファイル): "
                + previousNewestFile.getName());
        if (!previousNewestFile.exists()) {
            logger.println("[EVM Tools] 前回取り込んだファイルが存在しないのでこのまま集計処理を実施します。");
            return true;
        }

        logger.println("[EVM Tools] 日替わり基準日ファイル: " + shimeFile.getName());
        if (!shimeFile.exists()) {
            logger.println("[EVM Tools] 日替わり基準日ファイルが存在しないのでこのまま集計処理を実施します。");
            return true;
        }

        logger.println("[EVM Tools] 前回取り込んだファイルも日替わり基準日ファイルも存在します。");
        logger.println("[EVM Tools] 集計が正常終了したら前回ファイルは上書きしてしまうので、日付をチェックして上書きしてよいかを確認してから、集計処理を実施します。");

        Date targetDate = root
                .act(new DateGetter(targetFile.getName(), "json"));// 集計対象の日付
        Date newestDate = root.act(new DateGetter(previousNewestFile.getName(),
                "json"));// 今まで取り込んだ基準日
        Date shimeDate = root.act(new DateGetter(shimeFileName, "txt"));// 直近、シメた基準日

        logger.println("[EVM Tools] 対象ファイルの基準日:"
                + DateFormatUtils.format(targetDate, "yyyyMMdd") + " : "
                + targetFile.getName());
        logger.println("[EVM Tools] 前回取り込んだファイル基準日:"
                + DateFormatUtils.format(newestDate, "yyyyMMdd") + " : "
                + previousNewestFile.getName());
        logger.println("[EVM Tools] 日替わり基準日:"
                + DateFormatUtils.format(shimeDate, "yyyyMMdd") + " : "
                + shimeFileName);

        if (targetDate.getTime() == newestDate.getTime()) {// 同じ基準日のデータの取込なので、OK
            logger.println("[EVM Tools] 同じ基準日のデータの取込なので問題ない");
            return true;
        }

        if (newestDate.getTime() == shimeDate.getTime()) {// シメ直後など。次はどんな基準日が来るか分からないので、OK。
            logger.println("[EVM Tools] 前回基準日と日替わり基準日が同じなので、対象ファイルは様々な基準日があり得るので問題ない");
            return true;
        } else {
            if (targetDate.getTime() > newestDate.getTime()) {
                logger.println("[EVM Tools] 前回基準日と日替わり基準日が異なるが、対象ファイルの基準日が、前回基準日より大きい。日替わりが行われていない可能性が高い");
                return false;
            } else {
                logger.println("[EVM Tools] 前回基準日と日替わり基準日が異なるが、対象ファイルの基準日が、前回基準日より以下なので問題ない");
                return true;
            }
        }
    }

    private static class DateGetter implements FileCallable<Date> {
        private final String fileName;

        private final String format;

        public DateGetter(String name, String format) {
            this.fileName = name;
            this.format = format;
        }

        @Override
        public Date invoke(File f, VirtualChannel channel) throws IOException,
                InterruptedException {
            File target = new File(f, fileName);
            if ("excel".equals(format)) {
                return PMUtils.getBaseDateFromExcel(target);
            } else if ("json".equals(format)) {
                return PMUtils.getBaseDateFromJSON(target);
            } else {
                String string = ReadUtils.readFile(target);
                try {
                    Date parseDate = DateUtils.parseDate(string,
                            new String[] { "yyyyMMdd" });
                    return parseDate;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    /**
     * rootに対してcallableな処理を実行し、結果ファイルをbuildRootの下に配置する。
     * FilePath[]のデータを取得しローカルにコピー。 先頭のポインタ(FilePath)だけ後続の処理で使うので、呼び元に返却。
     * 
     * @param root
     * @param buildRoot
     * @param callable
     * @throws IOException
     * @throws InterruptedException
     */
    private FilePath executeAndCopies(FilePath root, FilePath buildRoot,
            FileCallable<FilePath[]> callable) throws IOException,
            InterruptedException {
        FilePath[] resultPaths = root.act(callable);

        for (FilePath resultPath : resultPaths) {
            if (resultPath != null) {
                FilePath targetPath = new FilePath(buildRoot,
                        resultPath.getName());
                resultPath.copyTo(targetPath); // remoteファイルを、ローカルにコピー。。
            }
        }
        return resultPaths[0];
    }

    private void executeAndCopy(FilePath root, FilePath buildRoot,
            FileCallable<FilePath> callable) throws IOException,
            InterruptedException {
        FilePath resultPath = root.act(callable);

        // FilePath returnPath = null;
        // for (FilePath resultPath : resultPaths) {
        FilePath targetPath = new FilePath(buildRoot, resultPath.getName());
        resultPath.copyTo(targetPath); // remoteファイルを、ローカルにコピー。。
        // if (returnPath == null) {
        // returnPath = targetPath;
        // }
        // }
        // return returnPath;
    }

    private static class ProjectWriterExecutor implements
            FileCallable<FilePath[]> {
        private final String fileName;

        private final boolean createJsonFlag;

        public ProjectWriterExecutor(String fileName, boolean createJsonFlag) {
            this.fileName = fileName;
            this.createJsonFlag = createJsonFlag;
        }

        public FilePath[] invoke(File f, VirtualChannel channel)
                throws IOException, InterruptedException {
            File target = new File(f, fileName);
            try {
                List<FilePath> returnList = new ArrayList<FilePath>();
                File result = ProjectWriter.write(target);
                returnList.add(new FilePath(result));
                for (String base_prefix : PREFIX_ARRAY) {
                    File base = new File(target.getParentFile(), base_prefix
                            + target.getName());
                    if (base.exists() && createJsonFlag) { // 日替わりモードでないときは(baseがあるばあいは)昨日のファイルのJSON化を行う
                        FilePath result_base = new FilePath(
                                ProjectWriter.write(base));
                        returnList.add(result_base);
                    }
                    if (!createJsonFlag) { // 日替わりモードの場合は、base_xx.jsonを(ある場合は)そのまま使う
                        FilePath result_base = new FilePath(new File(
                                base.getParentFile(),
                                ProjectUtils.findJSONFileName(base.getName())));
                        if (result_base.exists()) {
                            returnList.add(result_base);
                        }
                    }
                }
                return returnList.toArray(new FilePath[returnList.size()]);
            } catch (ProjectException e) {
                throw new IOException(e);
            }
        }

    }

    private static class PVCreatorExecutor implements FileCallable<FilePath> {
        private final String fileName;

        public PVCreatorExecutor(String fileName) {
            this.fileName = fileName;
        }

        public FilePath invoke(File f, VirtualChannel channel)
                throws IOException, InterruptedException {
            File target = new File(f, fileName);
            // File base = new File(target.getParentFile(), "base_"
            // + target.getName()); // file名にPrefix: base_をつけた
            try {
                File jsonFile = new File(target.getParentFile(),
                        ProjectUtils.findJSONFileName(target.getName()));
                if (jsonFile.exists()) {
                    File result = PVCreator.createFromJSON(jsonFile);
                    return new FilePath(result);
                }
                File result = PVCreator.create(target);
                return new FilePath(result);

            } catch (ProjectException e) {
                throw new IOException(e);
            }
        }
    }

    private static class ACCreatorExecutor implements FileCallable<FilePath[]> {

        private final String fileName;

        public ACCreatorExecutor(String fileName) {
            this.fileName = fileName;
        }

        public FilePath[] invoke(File f, VirtualChannel channel)
                throws IOException, InterruptedException {
            File target = new File(f, fileName);
            try {
                FilePath[] results = executes(target, PREFIX_ARRAY);
                return results;
            } catch (ProjectException e) {
                throw new IOException(e);
            }
        }

        private FilePath[] executes(File target, String[] prefixArray)
                throws ProjectException {
            List<FilePath> returnList = new ArrayList<FilePath>();
            for (String base_prefix : prefixArray) {
                FilePath result = execute(target, base_prefix);
                returnList.add(result);
            }
            return returnList.toArray(new FilePath[returnList.size()]);
        }

        private FilePath execute(File target, String base_prefix)
                throws ProjectException {

            File jsonFile = new File(target.getParentFile(), ProjectUtils.findJSONFileName(target.getName())
                    );
            File jsonFile_base = new File(target.getParentFile(),ProjectUtils.findJSONFileName( base_prefix
                    + target.getName()) );
            if (jsonFile_base.exists()) {
                File result = ACCreator.createFromJSON(jsonFile, jsonFile_base,
                        base_prefix);
                return new FilePath(result);
            }

            File base = new File(target.getParentFile(), base_prefix
                    + target.getName()); // file名にPrefix: base_をつけた

            if (base.exists()) {
                File result = ACCreator.create(target, base, base_prefix);
                return new FilePath(result);
            }
            return null;
        }
    }

    private static class EVCreatorExecutor implements FileCallable<FilePath[]> {
        private final String fileName;

        public EVCreatorExecutor(String fileName) {
            this.fileName = fileName;
        }

        public FilePath[] invoke(File f, VirtualChannel channel)
                throws IOException, InterruptedException {
            File target = new File(f, fileName);
            try {
                FilePath[] results = executes(target, PREFIX_ARRAY);
                return results;
                // return execute(target, base_prefix);
            } catch (ProjectException e) {
                throw new IOException(e);
            }
        }

        private FilePath[] executes(File target, String[] prefixArray)
                throws ProjectException {
            List<FilePath> returnList = new ArrayList<FilePath>();
            for (String base_prefix : prefixArray) {
                FilePath result = execute(target, base_prefix);
                returnList.add(result);
            }
            return returnList.toArray(new FilePath[returnList.size()]);
        }

        private FilePath execute(File target, String base_prefix)
                throws ProjectException {

            File jsonFile = new File(target.getParentFile(), ProjectUtils.findJSONFileName(target.getName())
                    );
            File jsonFile_base = new File(target.getParentFile(), ProjectUtils.findJSONFileName(base_prefix
                    + target.getName()));
            if (jsonFile_base.exists()) {
                File result = EVCreator.createFromJSON(jsonFile, jsonFile_base,
                        base_prefix);
                return new FilePath(result);
            }

            File base = new File(target.getParentFile(), base_prefix
                    + target.getName()); // file名にPrefix: base_をつけた
            if (base.exists()) {
                File result = EVCreator.create(target, base, base_prefix);
                return new FilePath(result);
            }
            return null;
        }
    }

    @Override
    public Action getProjectAction(AbstractProject<?, ?> project) {
        return new ProjectSummaryProjectAction(project);
    }

    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    /**
     * Descriptor for {@link EVMToolsBuilder}. Used as a singleton. The class is
     * marked as public so that it can be accessed from views.
     * 
     * <p>
     * See
     * <tt>src/main/resources/hudson/plugins/hello_world/HelloWorldBuilder/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension
    // This indicates to Jenkins that this is an implementation of an extension
    // point.
    public static final class DescriptorImpl extends
            BuildStepDescriptor<Builder> {
        private String prefixs;

        private String addresses;

        // /**
        // * To persist global configuration information, simply store it in a
        // * field and call save().
        // *
        // * <p>
        // * If you don't want fields to be persisted, use <tt>transient</tt>.
        // */
        // private boolean useFrench;

        /**
         * In order to load the persisted global configuration, you have to call
         * load() in the constructor.
         */
        public DescriptorImpl() {
            load();
        }

        /**
         * Performs on-the-fly validation of the form field 'name'.
         * 
         * @param value
         *            This parameter receives the value that the user has typed.
         * @return Indicates the outcome of the validation. This is sent to the
         *         browser.
         *         <p>
         *         Note that returning {@link FormValidation#error(String)} does
         *         not prevent the form from being saved. It just means that a
         *         message will be displayed to the user.
         */
        public FormValidation doCheckName(@QueryParameter
        String value) throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("Please set a Project File Name");
            if (value.length() < 4)
                return FormValidation.warning("Isn't the name too short?");
            return FormValidation.ok();
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project
            // types
            return true;
        }

        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "EVM集計ツール";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData)
                throws FormException {
            // To persist global configuration information,
            // set that to properties and call save().
            // useFrench = formData.getBoolean("useFrench");
            // ^Can also use req.bindJSON(this, formData);
            // (easier when there are many fields; need set* methods for this,
            // like setUseFrench)

            prefixs = formData.getString("prefixs");
            if (formData.has("useMail")) {
                JSONObject useMail = formData.getJSONObject("useMail");
                addresses = useMail.getString("addresses");
            } else {
                addresses = null;
            }
            save();
            return super.configure(req, formData);
        }

        // /**
        // * This method returns true if the global configuration says we should
        // * speak French.
        // *
        // * The method name is bit awkward because global.jelly calls this
        // method
        // * to determine the initial state of the checkbox by the naming
        // * convention.
        // */
        // public boolean getUseFrench() {
        // return useFrench;
        // }

        // Getterがあれば、保存されるぽい。
        public String getPrefixs() {
            return prefixs;
        }

        // Getterがあれば、保存されるぽい。
        public String getAddresses() {
            return addresses;
        }
    }

    // private static class DateChecker implements FileCallable<Boolean> {
    // private final String dotTmpFileName;
    //
    // public DateChecker(String name) {
    // dotTmpFileName = name;
    // }
    //
    // @Override
    // public Boolean invoke(File f, VirtualChannel channel)
    // throws IOException, InterruptedException {
    // try {
    // Date baseDate = new ExcelProjectCreator(new File(f,
    // dotTmpFileName)).createProject().getBaseDate();
    // String format = DateFormatUtils.format(baseDate, "yyyyMMdd");
    // File dateFile = new File(f, "date.dat");
    // if (dateFile.exists()) {
    // System.out.println("ファイルが存在します。");
    // }
    //
    // String string = ReadUtils.readFile(dateFile);
    // System.out.println(string);
    // System.out.println(format);
    // return string.equals(format);
    //
    // } catch (ProjectException e) {
    // // TODO 自動生成された catch ブロック
    // e.printStackTrace();
    // }
    // return Boolean.FALSE;
    // }
    //
    // }

    // private void testMethod(AbstractBuild build, BuildListener listener)
    // throws IOException, InterruptedException {
    // try {
    // ExtensionList<TokenMacro> all = TokenMacro.all();
    // for (TokenMacro tokenMacro : all) {
    // listener.getLogger().println(tokenMacro.toString());
    // }
    //
    // List<TokenMacro> privateMacros = new ArrayList<TokenMacro>();
    // ClassLoader cl = Jenkins.getInstance().pluginManager.uberClassLoader;
    // for (final IndexItem<EmailToken, TokenMacro> item : Index.load(
    // EmailToken.class, TokenMacro.class, cl)) {
    // try {
    // privateMacros.add(item.instance());
    // } catch (Exception e) {
    // // ignore errors loading tokens
    // }
    // }
    // String expand = TokenMacro.expandAll(build, listener,
    // "$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!",
    // false, null);
    // listener.getLogger().println(expand);
    // expand = TokenMacro.expandAll(build, listener,
    // "Check console output at $BUILD_URL to view the results.",
    // false, null);
    // listener.getLogger().println(expand);
    // String BUILD_URL = (new StringBuilder())
    // .append(Hudson.getInstance().getRootUrl())
    // .append(build.getUrl()).toString();
    // String PROJECT_NAME = build.getProject().getName();
    // String BUILD_NUMBER = String.valueOf(build.getNumber());
    //
    // listener.getLogger()
    // .println(
    // String.format("%s - Build # %s", PROJECT_NAME,
    // BUILD_NUMBER));
    // listener.getLogger().println(
    // String.format(
    // "Check console output at %s to view the results.",
    // BUILD_URL));
    // } catch (MacroEvaluationException e1) {
    // // TODO 自動生成された catch ブロック
    // e1.printStackTrace();
    // }
    // }

    // This is where you 'build' the project.
    // Since this is a dummy, we just say 'hello world' and call that a
    // build.

    // This also shows how you can consult the global configuration of the
    // builder
    // if (getDescriptor().getUseFrench())
    // listener.getLogger().println("Bonjour, " + name + "!");
    // else
    // listener.getLogger().println("Hello, " + name + "!");
    //
    // Collection<User> all = User.getAll();

}
