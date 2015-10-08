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
import java.util.Map;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

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
 * {@link TestBuilder} is created. The created instance is persisted to the
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
public class TestBuilder extends Builder {

    // Fields in config.jelly must match the parameter names in the
    // "DataBoundConstructor"
    @DataBoundConstructor
    public TestBuilder() {
    }

    /**
     * ワークスペース上の該当ファイルから、JSONファイルを作成。また存在するなら該当ファイル(base,base1,base2)
     * ExcelからJSONファイルを作成。 またPV/AC/EVのtsvファイルも作成。
     * 作成された諸々のファイル群はビルドディレクトリ(中央にある)にコピーされ、Actionから参照可能となる。
     * 
     * ただし、tsvファイル群はダウンロード時使用するのでコピー必須だが、
     * jsonファイルはProject情報としてシリアライズされていれば不要かもしれない。 どちらが速いか要確認であるが。
     * 
     * @see hudson.tasks.BuildStepCompatibilityLayer#perform(hudson.model.AbstractBuild,
     *      hudson.Launcher, hudson.model.BuildListener)
     */
    @Override
    public boolean perform(AbstractBuild build, Launcher launcher,
            BuildListener listener) throws InterruptedException, IOException {
        Map<String, String> buildVariables = build.getBuildVariables();
        System.out.println(buildVariables);

        System.out.println(buildVariables.get("param1"));

        System.out.println(build);
        FilePath root = build.getModuleRoot(); // ワークスペースのルート.スレーブでビルドが動くと、他サーバのディレクトリだったりする。
        root.act(new Hoge(listener));

        FilePath moduleRoot = build.getModuleRoot();
        File rootDir = build.getRootDir();
        FilePath rootDirPath = new FilePath(rootDir);
        FilePath workspace = build.getWorkspace();
        File artifactsDir = build.getArtifactsDir();
        FilePath artifactsDirPath = new FilePath(artifactsDir);

        listener.getLogger().println(moduleRoot);
        listener.getLogger().println(rootDir);
        listener.getLogger().println(rootDirPath);
        listener.getLogger().println(workspace);
        listener.getLogger().println(artifactsDir);
        listener.getLogger().println(artifactsDirPath);

        try {
            // Fileを返すメソッドなので、マスターのディレクトリと予測
            File file = new File(build.getRootDir(), "buildDir.txt");
            listener.getLogger().println(file.getAbsolutePath());
            file.createNewFile();

            // おなじくFileを操作しているので、マスターのディレクトリと予測
            File file2 = new File("/tmp/fullpath.txt");
            listener.getLogger().println(file2.getAbsolutePath());
            file2.createNewFile();

            moduleRoot = build.getModuleRoot();
            String[] result = moduleRoot.act(new F());
            listener.getLogger().println(result[0]);
            listener.getLogger().println(result[1]);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }

    private static class F implements FileCallable<String[]> {
        private static final long serialVersionUID = 1L;

        public String[] invoke(File f, VirtualChannel channel)
                throws IOException, InterruptedException {
            File file = new File(f, "root.txt"); // f
                                                 // はFilePathから生成されるので場合によってスレーブのパスとなると予測
            file.createNewFile();

            File file2 = new File("/tmp/fullpath2.txt"); // このメソッド内はスレーブのパスになると予測
            file2.createNewFile();

            return new String[] { file.getAbsolutePath(),
                    file2.getAbsolutePath() };
        }

        @Override
        public void checkRoles(RoleChecker checker) throws SecurityException {
            // TODO 自動生成されたメソッド・スタブ

        }
    }

    private static class Hoge implements FileCallable<Void> {

        /**
         * <code>serialVersionUID</code> のコメント
         */
        private static final long serialVersionUID = 8984513401663215528L;

        private final BuildListener listener;

        public Hoge(BuildListener listener) {
            this.listener = listener;
        }

        @Override
        public void checkRoles(RoleChecker checker) throws SecurityException {
            // TODO 自動生成されたメソッド・スタブ

        }

        @Override
        public Void invoke(File f, VirtualChannel channel) throws IOException,
                InterruptedException {
            listener.getLogger().print(f.toString());
            System.out.println(f);
            return null;
        }
    };

    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    /**
     * Descriptor for {@link TestBuilder}. Used as a singleton. The class is
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
            return "Testツール";
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
    //
    // Collection<User> all = User.getAll();

}
