package nu.mine.kino.jenkins.plugins.projectmanagement;

import hudson.AbortException;
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
import hudson.util.ListBoxModel;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;
import nu.mine.kino.jenkins.plugins.projectmanagement.utils.PMUtils;
import nu.mine.kino.projects.utils.ProjectUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.jenkinsci.remoting.RoleChecker;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Sample {@link Builder}.
 * 
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked and a new
 * {@link HigawariBuilder} is created. The created instance is persisted to the
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
public class HigawariBuilder extends Builder {
    private static final String seriesFileNameSuffix = PMConstants.SERIES_DAT_FILENAME;

    private String projectName;

    private String prefix;

    // Fields in config.jelly must match the parameter names in the
    // "DataBoundConstructor"
    @DataBoundConstructor
    public HigawariBuilder(String projectName, String prefix) {
        this.projectName = projectName;
        this.prefix = prefix;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getPrefix() {
        return prefix;
    }

    /**
     * パラメータ付きビルドで、
     * 
     * @see hudson.tasks.BuildStepCompatibilityLayer#perform(hudson.model.AbstractBuild,
     *      hudson.Launcher, hudson.model.BuildListener)
     */
    @Override
    public boolean perform(AbstractBuild build, Launcher launcher,
            BuildListener listener) throws InterruptedException, IOException {
        PrintStream stdout = listener.getLogger();
        PrintStream stderr = listener.getLogger();

        Map<String, String> buildVariables = build.getBuildVariables();
        String paramProjectName = buildVariables.get("project");
        String paramPrefix = buildVariables.get("prefix");

        if (!StringUtils.isEmpty(paramProjectName)) {
            projectName = paramProjectName;
        }
        if (!StringUtils.isEmpty(paramPrefix)) {
            prefix = paramPrefix;
        }

        AbstractProject<?, ?> job = PMUtils.findProject(projectName);
        if (job == null) {
            throw new AbortException(projectName
                    + " というフリースタイル・プロジェクトは存在しないようです。");
        }

        String originalExcelFileName = PMUtils.findProjectFileName(job);
        if (originalExcelFileName == null) {
            throw new AbortException("スケジュールファイルを見つけることができませんでした。");
        }
        stdout.println("EVMファイル名: " + originalExcelFileName);

        // 相対的に指定されたファイルについて、ワークスペースルートにファイルコピーします。
        FilePath someWorkspace = job.getSomeWorkspace();
        FilePath excelFilePath = new FilePath(someWorkspace,
                originalExcelFileName);
        String previousJsonFileName = PMUtils
                .getPreviousJsonFileName(originalExcelFileName);
        FilePath jsonSource = new FilePath(someWorkspace, previousJsonFileName);
        stdout.println(excelFilePath);
        stdout.println("このファイルの日替わり処理を行います。");
        if (jsonSource.exists()) { //
            String tmpPrefix = prefix;
            if (StringUtils.isEmpty(prefix)) {
                tmpPrefix = PMConstants.BASE;
            }

            String destFileName = tmpPrefix
                    + "_"
                    + ProjectUtils.findJSONFileName((new FilePath(
                            someWorkspace, originalExcelFileName).getName()));
            FilePath dest = new FilePath(someWorkspace, destFileName);
            jsonSource.copyTo(dest);
            stdout.println(jsonSource.getParent() + " 内 でコピー");
            stdout.println("[" + jsonSource.getName() + "] -> ["
                    + dest.getName() + "] コピー完了");

            final AbstractBuild<?, ?> shimeBuild = job.getLastSuccessfulBuild();

            String baseDateStr = jsonSource.act(new DateFileExecutor());
            PMUtils.writeBaseDateFile(baseDateStr, shimeBuild, stdout);

            stdout.println("基準日: " + baseDateStr + " を締めました。日替わり処理が正常終了しました。");

            String seriesFileName = tmpPrefix + "_" + seriesFileNameSuffix;
            PMUtils.writeSeriesFile(job, baseDateStr, seriesFileName,
                    shimeBuild, stdout, stderr);
        } else {
            stderr.println("---- エラーが発生したため日替わり処理を停止します。------");
            if (!jsonSource.exists()) {
                stderr.println("バックアップファイル(基準日も取得する):");
                stderr.println(jsonSource);
                stderr.println("が存在しないため日替わり処理を停止します。");
            }
            stderr.println("------------------------------------------------");
            return false;
        }
        return true;
    }

    private static class DateFileExecutor implements FileCallable<String> {

        @Override
        public String invoke(File jsonFile, VirtualChannel channel)
                throws IOException, InterruptedException {
            Date baseDate = PMUtils.getBaseDateFromJSON(jsonFile);
            if (baseDate != null) {
                String baseDateStr = DateFormatUtils.format(baseDate,
                        "yyyyMMdd");
                return baseDateStr;
            }
            return null;
        }

        @Override
        public void checkRoles(RoleChecker checker) throws SecurityException {
            // TODO 自動生成されたメソッド・スタブ

        }

    }

    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    /**
     * Descriptor for {@link HigawariBuilder}. Used as a singleton. The class is
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
            return "日替がわりビルド";
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
            save();
            return super.configure(req, formData);
        }

        /**
         * これが動的にプルダウンを作るやり方
         * 
         * @return
         */
        public ListBoxModel doFillProjectNameItems() {
            ListBoxModel items = new ListBoxModel();
            List<AbstractProject<?, ?>> projects = PMUtils
                    .findProjectsWithEVMToolsBuilder();
            for (AbstractProject<?, ?> project : projects) {
                items.add(project.getName(), project.getName());
            }
            return items;
        }

    }

}
