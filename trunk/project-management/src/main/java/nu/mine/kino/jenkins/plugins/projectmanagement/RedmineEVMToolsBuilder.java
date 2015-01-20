package nu.mine.kino.jenkins.plugins.projectmanagement;

import hudson.Extension;
import hudson.FilePath;
import hudson.FilePath.FileCallable;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.remoting.VirtualChannel;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import nu.mine.kino.projects.RedmineProjectCreator;
import nu.mine.kino.projects.RedmineProjectCreator2;
import nu.mine.kino.projects.utils.Utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;

/**
 * Sample {@link Builder}.
 * 
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked and a new
 * {@link RedmineEVMToolsBuilder} is created. The created instance is persisted
 * to the project configuration XML by using XStream, so this allows you to use
 * instance fields (like {@link #url}) to remember the configuration.
 * 
 * <p>
 * When a build is performed, the
 * {@link #perform(AbstractBuild, Launcher, BuildListener)} method will be
 * invoked.
 * 
 * @author Masatomi KINO.
 */
public class RedmineEVMToolsBuilder extends Builder {

    private final String url;

    private static final String[] PREFIX_ARRAY = new String[] { "base_",
            "base1_", "base2_" };

    private final String userid;

    private final String password;

    private final String projectId;

    private final String queryId;

    private final String apiKey;

    private final String addresses;

    // Fields in config.jelly must match the parameter names in the
    // "DataBoundConstructor"
    @DataBoundConstructor
    public RedmineEVMToolsBuilder(String url, String userid, String password,
            String projectId, String queryId, String apiKey, String addresses) {
        this.url = url;
        this.userid = userid;
        this.password = password;
        this.projectId = projectId;
        this.queryId = queryId;
        this.apiKey = apiKey;
        this.addresses = addresses;
    }

    public String getUrl() {
        return url;
    }

    public String getUserid() {
        return userid;
    }

    public String getPassword() {
        return password;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getQueryId() {
        return queryId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getAddresses() {
        return addresses;
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher,
            BuildListener listener) throws InterruptedException, IOException {

        Integer queryIdInt = null;
        if (!StringUtils.isEmpty(queryId)) {
            queryIdInt = Integer.valueOf(queryId);
        }
        RedmineManager mgr = null;
        if (StringUtils.isEmpty(apiKey)) {
            mgr = RedmineManagerFactory.createWithUserAuth(url, userid,
                    password);
        } else {
            mgr = RedmineManagerFactory.createWithApiKey(url, apiKey);
        }
        listener.getLogger().println("[Redmine EVM Tools] url:" + url);
        listener.getLogger().println(
                "[Redmine EVM Tools] projectId:" + projectId);
        listener.getLogger().println("[Redmine EVM Tools] queryId:" + queryId);

        String fileName = projectId;
        try {
            Project project = createProject(mgr, queryIdInt);
            System.out.println(project);
            StringBuffer buf = new StringBuffer();
            buf.append(fileName);
            if (!StringUtils.isEmpty(queryId)) {
                buf.append("_");
                buf.append(queryId);
            }
            buf.append(".json");
            fileName = new String(buf);

            // ビルドのディレクトリに作成し、ワークスペースへコピー。
            File outputJSON = new File(build.getRootDir(), fileName);
            ProjectWriter.write(project, outputJSON);

            copyTo(build.getModuleRoot(), outputJSON);
            // new FilePath(outputJSON).copyTo(new
            // FilePath(build.getModuleRoot(),
            // "base_" + outputJSON.getName()));// workspaceへbase_

            // FileUtils.copyFile(outputJSON, new File(build.getRootDir(),
            // "base_"
            // + outputJSON.getName()));

            listener.getLogger().println(
                    "[Redmine EVM Tools] JSON File:"
                            + outputJSON.getAbsolutePath());

            File outputTsv = new File(build.getRootDir(), fileName + ".tsv");
            ProjectWriter.writeText(project, outputTsv);
            copyTo(build.getModuleRoot(), outputTsv);
            listener.getLogger().println(
                    "[Redmine EVM Tools] TSV File:"
                            + outputTsv.getAbsolutePath());

            ProjectSummaryAction action = PMUtils
                    .getProjectSummaryAction(build);
            action.setRedmineFileName(fileName);

            PMUtils.checkProjectAndMail(project, addresses, listener);

        } catch (ProjectException e) {
            throw new IOException(e);
        }

        return true;
    }

    private void copyTo(FilePath path, File source) throws IOException,
            InterruptedException {
        new FilePath(source).copyTo(new FilePath(path, source.getName()));// workspaceへコピー
    }

    private Project createProject(RedmineManager mgr, Integer queryIdInt)
            throws ProjectException {
        RedmineProjectCreator creator = new RedmineProjectCreator(mgr);
        Project project = null;
        try {
            project = creator.createProject(projectId, queryIdInt);
        } catch (ProjectException e) {
            creator = new RedmineProjectCreator2(mgr, url, apiKey);
            project = creator.createProject(projectId, queryIdInt);
        }
        return project;
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

        public ProjectWriterExecutor(String fileName) {
            this.fileName = fileName;
        }

        public FilePath[] invoke(File f, VirtualChannel channel)
                throws IOException, InterruptedException {
            File target = new File(f, fileName);
            // String prefix = "base_";
            // File base = new File(target.getParentFile(), prefix
            // + target.getName()); // file名にPrefix: base_をつけた

            // File targetOutputFile = extracted(target);
            // File baseOutputFile = extracted(base);

            // //// Add ////
            // すでにJSONファイルがあったら、作らない、という処理。
            // if (targetOutputFile.exists() && baseOutputFile.exists()) {
            // return new FilePath[] { new FilePath(targetOutputFile),
            // new FilePath(baseOutputFile) };
            // }
            // //// Add ////
            try {
                List<FilePath> returnList = new ArrayList<FilePath>();
                File result = ProjectWriter.write(target);
                returnList.add(new FilePath(result));
                for (String base_prefix : PREFIX_ARRAY) {
                    File base = new File(target.getParentFile(), base_prefix
                            + target.getName());
                    if (base.exists()) {
                        FilePath result_base = new FilePath(
                                ProjectWriter.write(base));
                        returnList.add(result_base);
                    }
                }
                return returnList.toArray(new FilePath[returnList.size()]);
            } catch (ProjectException e) {
                throw new IOException(e);
            }
        }

        private File extracted(File target) {
            File baseDir = target.getParentFile();
            String output = target.getName() + "." + "json";
            File file = new File(baseDir, output);
            return file;
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
                        target.getName() + ".json");
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

            File jsonFile = new File(target.getParentFile(), target.getName()
                    + ".json");
            File jsonFile_base = new File(target.getParentFile(), base_prefix
                    + target.getName() + ".json");
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

            File jsonFile = new File(target.getParentFile(), target.getName()
                    + ".json");
            File jsonFile_base = new File(target.getParentFile(), base_prefix
                    + target.getName() + ".json");
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

    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public RedmineEVMDescriptor getDescriptor() {
        return (RedmineEVMDescriptor) super.getDescriptor();
    }

    /**
     * Descriptor for {@link RedmineEVMToolsBuilder}. Used as a singleton. The
     * class is marked as public so that it can be accessed from views.
     * 
     * <p>
     * See
     * <tt>src/main/resources/hudson/plugins/hello_world/HelloWorldBuilder/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension
    // This indicates to Jenkins that this is an implementation of an extension
    // point.
    public static final class RedmineEVMDescriptor extends
            BuildStepDescriptor<Builder> {
        private String prefixs;

        /**
         * In order to load the persisted global configuration, you have to call
         * load() in the constructor.
         */
        public RedmineEVMDescriptor() {
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
            return "RedmineEVM集計ツール";
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
    }

}
