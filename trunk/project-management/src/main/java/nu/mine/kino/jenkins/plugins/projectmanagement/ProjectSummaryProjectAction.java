package nu.mine.kino.jenkins.plugins.projectmanagement;

import hudson.model.Action;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;

import java.io.IOException;
import java.util.Date;

import nu.mine.kino.entity.PVACEVViewBean;
import nu.mine.kino.jenkins.plugins.projectmanagement.utils.PMUtils;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

public class ProjectSummaryProjectAction implements Action {

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
        return "プロジェクトサマリー(直近)";
        // return Messages.SampleProjectAction_DisplayName();
    }

    public String getUrlName() {
        return "project-summary";
    }

    // public String getData() {
    // return "Sample Project Action!!";
    // }

    public PVACEVViewBean getCurrentPVACEV() {
        final AbstractBuild<?, ?> tb = project.getLastSuccessfulBuild();
        AbstractBuild<?, ?> b = project.getLastBuild();
        while (b != null) {
            ProjectSummaryAction a = PMUtils.getProjectSummaryAction(b);
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
            ProjectSummaryAction a = PMUtils.getProjectSummaryAction(b);
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

    public void doIndex(final StaplerRequest request,
            final StaplerResponse response) throws IOException {
        AbstractBuild<?, ?> build = project.getLastSuccessfulBuild();

        String path = String.format("/job/%s/%d/%s", build.getProject()
                .getName(), build.getNumber(), "project-summary");
        System.out.println(path);
        System.out.println(request.getContextPath());
        response.sendRedirect2(request.getContextPath() + path);
    }
}
