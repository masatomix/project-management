package nu.mine.kino.plugin.jenkins.heartbeat;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

//import org.jsoup.nodes.Element;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(ApplicationWorkbenchWindowAdvisor.class);

    private TrayItem trayItem;

    // private Image smsNotExistImage;

    private Image smsExistImage;

    // private Image smsNewImage;

    private Image errorImage;

    private IWorkbenchWindow window;

    private boolean showDialog;

    private ScheduledExecutorService scheduler;

    private ScheduledFuture<?> future;

    // private String previousSMSId;

    public ApplicationWorkbenchWindowAdvisor(
            IWorkbenchWindowConfigurer configurer) {
        super(configurer);

        // smsNotExistImage = HeartbeatPlugin.getDefault().getImageRegistry()
        // .getDescriptor(HeartbeatPlugin.IMG_NOT_EXIST).createImage();
        smsExistImage = HeartbeatPlugin.getDefault().getImageRegistry()
                .getDescriptor(HeartbeatPlugin.IMG_EXIST).createImage();
        // smsNewImage = HeartbeatPlugin.getDefault().getImageRegistry()
        // .getDescriptor(HeartbeatPlugin.IMG_NEW).createImage();
        errorImage = HeartbeatPlugin.getDefault().getImageRegistry()
                .getDescriptor(HeartbeatPlugin.IMG_ERROR).createImage();
    }

    public ActionBarAdvisor createActionBarAdvisor(
            IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }

    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(10, 10));
        configurer.setShowCoolBar(false);
        configurer.setShowStatusLine(false);
        configurer.setShellStyle(SWT.NONE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#postWindowOpen()
     */
    @Override
    public void postWindowOpen() {
        logger.debug("postWindowOpen() - Start");
        super.postWindowOpen();
        window = getWindowConfigurer().getWindow();

        Shell shell = window.getShell();
        shell.setMinimized(true);
        shell.setVisible(false);

        initTrayItem(errorImage, "Retrieving information ...");

        final Runnable task = new Runnable() {
            public void run() {
                logger.debug("run() - start");
                executeMethod();
                logger.debug("run() - end");
            }
        };

        String periodStr = HeartbeatPlugin.getDefault().getPreferenceStore()
                .getString(Constants.PERIOD);
        int period = Integer.parseInt(periodStr);
        scheduler = Executors.newSingleThreadScheduledExecutor();
        future = scheduler.scheduleAtFixedRate(task, 1000, period * 1000,
                TimeUnit.MILLISECONDS);
        logger.debug("postWindowOpen() - End");
    }

    private void executeMethod() {
        if (!window.getShell().getDisplay().isDisposed()) {
            window.getShell().getDisplay().asyncExec(new Runnable() {
                @Override
                public void run() {
                    logger.debug("run() - start");

                    try {
                        final int status = HeartbeatPlugin.getDefault().check();
                        boolean checkFlag = false;
                        if (status == 200) {
                            checkFlag = true;
                        }
                        if (checkFlag) { // あり
                            // logger.debug("未読: " + smsCounter + "件");

                            // Element nextShortMail = HeartbeatPlugin
                            // .getDefault().getNexeShortMail(username,
                            // password);
                            // String currentSMSId = nextShortMail
                            // .getElementsByTag("smjsid").text();
                            //
                            // if (previousSMSId != null
                            // && previousSMSId.equals(currentSMSId)) {
                            // logger.debug("前回取得したのと同じSMSオブジェクトだ");
                            // String message = "未読のメッセージが " + counter
                            // + " 件あります。";
                            // trayItem.setToolTipText(message);
                            // trayItem.setImage(smsExistImage);
                            //
                            // } else {
                            // logger.debug("前回取得したのと違うSMSオブジェクトだ");
                            // previousSMSId = currentSMSId;
                            String message = "Jenkinsサーバは正常稼働中。";
                            trayItem.setToolTipText(message);
                            trayItem.setImage(smsExistImage); // こっちはNewに変更
                            showDialog(message);
                            // }
                        } else {
                            // logger.debug("未読: " + smsCounter + "件");
                            // previousSMSId = null;
                            trayItem.setImage(errorImage);
                            trayItem.setToolTipText("Jenkinsサーバとの接続ができませんでした.Statusコード:"
                                    + status);
                        }
                    } catch (IOException e) {
                        logger.error("run()", e);
                        // previousSMSId = null;
                        // logger.debug("GroupSessionのショートメッセージ未読件数の取得に失敗しました.\nタスクトレイのアイコンを右クリックして設定画面を表示して、設定を見なおしてみてください。");
                        trayItem.setImage(errorImage);
                        trayItem.setToolTipText("Jenkinsサーバとの接続ができませんでした。\nタスクトレイのアイコンを右クリックして設定画面を表示して、設定を見なおしてみてください。");
                    }

                    logger.debug("run() - end");
                }

                private void showDialog(String message) {
                    if (showDialog) {
                        MessageBox box = new MessageBox(window.getShell(),
                                SWT.OK | SWT.ICON_INFORMATION);
                        box.setMessage(message);
                        box.open();
                    }
                }
            });
        }
    }

    private void initTrayItem(Image image, String toolTip) {
        if (trayItem != null) {
            trayItem.dispose();
        }

        trayItem = initTaskItem(window, image, toolTip);

        final Shell shell = window.getShell();

        final MenuManager trayMenu = new MenuManager();

        final Menu menu = trayMenu.createContextMenu(shell);
        trayMenu.add(new Action() {
            {
                setText("今すぐ確認");
            }

            @Override
            public void run() {
                logger.debug("run() - start");
                // previousSMSId = null;
                executeMethod();
                logger.debug("run() - end");
            }

        });

        NotifyAction notifyAction = new NotifyAction(trayMenu);
        HeartbeatPlugin.getDefault().getPreferenceStore()
                .addPropertyChangeListener(notifyAction);
        trayMenu.add(notifyAction);

        trayMenu.add(ActionFactory.PREFERENCES.create(window));
        final IWorkbenchAction quitAction = ActionFactory.QUIT.create(window);
        // trayMenu.add(quitAction);

        trayMenu.add(new Action() {
            {
                setText("Quit");
            }

            @Override
            public void run() {
                logger.debug("run() - start");

                if (future != null) {
                    future.cancel(true);
                }
                scheduler.shutdownNow();
                shell.setVisible(true);
                shell.setMinimized(false);
                quitAction.run();

                logger.debug("run() - end");
            }

        });

        trayItem.addMenuDetectListener(new MenuDetectListener() {

            public void menuDetected(MenuDetectEvent e) {
                menu.setVisible(true);
            }

        });

        trayItem.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                String baseURL = HeartbeatPlugin.getDefault()
                        .getPreferenceStore().getString(Constants.BASE_URL);
                // String smsWebURL = baseURL
                // + "/gsession/common/cmn002.do?url=../smail/sml010.do";
                if (baseURL != null) {
                    Program program = Program.findProgram(".html");
                    if (program != null) {
                        program.launch(baseURL);
                    }
                }
            }

        });
    }

    private TrayItem initTaskItem(IWorkbenchWindow window, Image image,
            String toolTip) {

        final Tray tray = window.getShell().getDisplay().getSystemTray();

        TrayItem trayItem = new TrayItem(tray, SWT.NONE);

        trayItem.setImage(image);

        trayItem.setToolTipText(toolTip);

        return trayItem;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#dispose()
     */
    @Override
    public void dispose() {
        logger.debug("dispose() - start");
        trayItem.dispose();
        super.dispose();

        logger.debug("dispose() - end");
    }

    class NotifyAction extends Action implements IPropertyChangeListener {
        private final MenuManager trayMenu;

        public NotifyAction(MenuManager trayMenu) {
            this.trayMenu = trayMenu;
            setText("ダイアログで通知する");
            showDialog = HeartbeatPlugin.getDefault().getPreferenceStore()
                    .getBoolean(Constants.SHOW_DIALOG);
            setChecked(showDialog);
        }

        @Override
        public void run() {
            if (showDialog) {
                showDialog = false;
            } else {
                showDialog = true;
            }
            setChecked(showDialog);
            trayMenu.markDirty();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org
         * .eclipse.jface.util.PropertyChangeEvent)
         */
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if (event.getProperty().equals(Constants.SHOW_DIALOG)) {
                showDialog = HeartbeatPlugin.getDefault().getPreferenceStore()
                        .getBoolean(Constants.SHOW_DIALOG);
                setChecked(showDialog);
                trayMenu.markDirty();
            }
        }
    }

}
