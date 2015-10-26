package nu.mine.kino.jenkins.plugins.projectmanagement;

import hudson.AbortException;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.ServletException;

import net.sf.json.JSONObject;
import nu.mine.kino.jenkins.plugins.projectmanagement.utils.PMUtils;
import nu.mine.kino.projects.utils.Utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.jenkinsci.plugins.tokenmacro.MacroEvaluationException;
import org.jenkinsci.plugins.tokenmacro.TokenMacro;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

/**
 * {@link EVMToolsBuilder} がセットされたジョブを探し、現在の日替わり基準日を探してくる。
 * <p>
 * When a build is performed, the
 * {@link #perform(AbstractBuild, Launcher, BuildListener)} method will be
 * invoked.
 * 
 * @author Masatomi KINO.
 */
public class HigawariCheckBuilder extends Builder {

    private static final String default_subject = "${PROJECT_NAME} からのメール(#${BUILD_NUMBER})";

    private static final String default_message = "${HIGAWARI_CHECK_RESULTS}";

    // ネストしたテキストボックスを作成するときの定石。
    private String targetProjects;

    public static class EnableTextBlock {
        private String targetProjects;

        @DataBoundConstructor
        public EnableTextBlock(String targetProjects) {
            this.targetProjects = targetProjects;
        }
    }

    private final EnableTextBlock useFilter;

    public EnableTextBlock getUseFilter() {
        return useFilter;
    }

    private final String mailSubject;

    private final String mailBody;

    public String getMailSubject() {
        return mailSubject;
    }

    public String getMailBody() {
        return mailBody;
    }

    // Fields in config.jelly must match the parameter names in the
    // "DataBoundConstructor"
    @DataBoundConstructor
    public HigawariCheckBuilder(EnableTextBlock useFilter,
            EnableUseMailTextBlock useMail, String mailSubject, String mailBody) {

        this.useFilter = useFilter;
        this.useMail = useMail;
        this.mailSubject = mailSubject;
        this.mailBody = mailBody;
        if (useFilter != null) { // targetProjectsは、ココを通らなければ初期値に戻る。
            this.targetProjects = useFilter.targetProjects;
        }
        if (useMail != null) { // targetProjectsは、ココを通らなければ初期値に戻る。
            this.addresses = useMail.addresses;
        }
    }

    public String getTargetProjects() {
        return targetProjects;
    }

    // ネストしたテキストボックスを作成するときの定石。

    // ネストしたテキストボックスを作成するときの定石。
    private String addresses;

    public static class EnableUseMailTextBlock {
        private String addresses;

        @DataBoundConstructor
        public EnableUseMailTextBlock(String addresses) {
            this.addresses = addresses;
        }
    }

    private final EnableUseMailTextBlock useMail;

    public String getAddresses() {
        return addresses;
    }

    // ネストしたテキストボックスを作成するときの定石。

    public String getSamples() {
        return getDescriptor().defaultSamples();
    }

    /**
     */
    @Override
    public boolean perform(AbstractBuild build, Launcher launcher,
            BuildListener listener) throws InterruptedException, IOException {

        String header = createHeader();
        listener.getLogger().println(convertText(build, listener, header));
        listener.getLogger().println(
                convertText(build, listener, default_message));

        String subject = createSubject(build, listener);
        String message = createMessage(build, listener);

        System.out.printf("[EVM Tools] 宛先: %s\n", addresses);
        System.out.printf("[EVM Tools] サブジェクト: %s\n", subject);
        System.out.printf("[EVM Tools] 本文:\n%s\n", message);

        if (useMail != null) {
            listener.getLogger().println("[EVM Tools] 宛先: " + addresses);
            listener.getLogger().println("[EVM Tools] サブジェクト: " + subject);
            sendMail(listener, subject, message);
        }
        return true;
    }

    private String createHeader() {
        StringBuffer buf = new StringBuffer();
        buf.append("${BUILD_DATE}時点の日回し確認の結果報告です。\n\n");
        buf.append("【凡例】\n");
        buf.append("○･･･前営業日分の日替処理が行われている\n");
        buf.append("×･･･前営業日分の日替処理が行われていない\n");
        buf.append("\n------------------------------------------------------------");
        String target = new String(buf);
        return target;
    }

    private String createSubject(AbstractBuild build, BuildListener listener)
            throws AbortException, IOException, InterruptedException {
        String target = null;
        if (StringUtils.isEmpty(mailSubject)) {
            target = createDefaultSubject(build, listener);
        } else {
            target = mailSubject;
        }
        return convertText(build, listener, target);
    }

    private String createMessage(AbstractBuild build, BuildListener listener)
            throws IOException, InterruptedException, AbortException {
        String target = null;
        if (StringUtils.isEmpty(mailBody)) {
            target = createDefaultMessage(build, listener);
        } else {
            target = mailBody;
        }
        return convertText(build, listener, target);

    }

    private String convertText(AbstractBuild build, BuildListener listener,
            String target) throws IOException, InterruptedException,
            AbortException {

        try {
            String result = TokenMacro.expandAll(build, listener, target);
            return result;
        } catch (MacroEvaluationException e) {
            listener.getLogger().println("[EVM Tools] " + e.getMessage());
            listener.getLogger().println("[EVM Tools] 設定された文字:\n");
            listener.getLogger().println(target);
            throw new AbortException(e.getMessage());
        }
    }

    private String createDefaultSubject(AbstractBuild build,
            BuildListener listener) {
        return default_subject;
    }

    private String createDefaultMessage(AbstractBuild build,
            BuildListener listener) throws IOException, InterruptedException,
            AbortException {
        String header = "以下、${PROJECT_NAME} からのメールです。\n\n";
        header += createHeader();
        String footer = "\n\nCheck console output at ${BUILD_URL} to view the results.";

        StringBuffer msgBuf = new StringBuffer();
        msgBuf.append(header);
        msgBuf.append(default_message);
        msgBuf.append(footer);
        String message = new String(msgBuf);

        return message;
    }

    private void sendMail(BuildListener listener, String subject, String message)
            throws UnsupportedEncodingException, AbortException {
        StopWatch watch = new StopWatch();
        watch.start();

        if (!StringUtils.isEmpty(addresses)) {
            String[] addressesArray = Utils.parseCommna(addresses);
            for (String to : addressesArray) {
                System.out.printf("宛先: [%s]\n", to);
            }
            try {
                if (addressesArray.length > 0) {
                    PMUtils.sendMail(addressesArray, subject, message);
                } else {
                    String errorMsg = "メール送信に失敗しました。宛先の設定がされていません";
                    listener.getLogger().println("[EVM Tools] " + errorMsg);
                    throw new AbortException(errorMsg);
                }
            } catch (MessagingException e) {
                String errorMsg = "メール送信に失敗しました。「システムの設定」で E-mail 通知 の設定や宛先などを見直してください";
                listener.getLogger().println("[EVM Tools] " + errorMsg);
                throw new AbortException(errorMsg);
            }
        }
        watch.stop();
        System.out.printf("メール送信時間:[%d] ms\n", watch.getTime());
        watch.reset();
        watch = null;
    }

    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    /**
     * Descriptor for {@link HigawariCheckBuilder}. Used as a singleton. The
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
            return "日替わりチェックツール";
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

        // https://wiki.jenkins-ci.org/display/JENKINS/Basic+guide+to+Jelly+usage+in+Jenkins
        // config.jellyから呼び出される、デフォルト値をセットするメソッド。
        public String defaultSamples() {
            StringBuffer buf = new StringBuffer();
            List<AbstractProject<?, ?>> projects = PMUtils
                    .findProjectsWithEVMToolsBuilder();
            for (int i = 0; i < projects.size(); i++) {
                buf.append(projects.get(i).getName());
                if (i < projects.size() - 1) {
                    buf.append("\n");
                }
            }
            return new String(buf);
        }
    }

}
