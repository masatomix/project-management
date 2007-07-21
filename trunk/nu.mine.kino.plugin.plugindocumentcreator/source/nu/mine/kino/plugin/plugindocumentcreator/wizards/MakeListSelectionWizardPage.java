/*******************************************************************************
 * Copyright (c) 2007 Masatomi KINO.
 * All rights reserved. 
 * $Id$
 *******************************************************************************/
//作成日: 2006/10/22
package nu.mine.kino.plugin.plugindocumentcreator.wizards;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author Masatomi KINO
 * @version $Revision: 1.4 $
 */
public class MakeListSelectionWizardPage extends WizardPage {
    private Text pluginIdText;

    private Text alert;

    private Text descriptionText;

    private Text implementationClassText;

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(MakeListSelectionWizardPage.class);

    private Combo extensionPointIdCombo;

    
    private Text outputFile;

    /**
     * Create the wizard
     */
    public MakeListSelectionWizardPage() {
        super("wizardPage");
        setTitle("ドキュメント生成ウィザード");
        setDescription("ドキュメントを生成したい拡張ポイントを指定してください。");
    }

    /**
     * Create contents of the wizard
     * @param parent
     */
    public void createControl(Composite parent) {
        logger.debug("createControl(Composite) - start");

        Composite container = new Composite(parent, SWT.NULL);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        container.setLayout(gridLayout);
        //
        setControl(container);

        final Group group = new Group(container, SWT.NONE);
        group.setText("拡張ポイントの選択");
        group
                .setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false,
                        3, 1));
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 2;
        group.setLayout(gridLayout_1);

        final Label pluginidLabel = new Label(group, SWT.NONE);
        pluginidLabel.setLayoutData(new GridData());
        pluginidLabel.setText("拡張ポイント");

        extensionPointIdCombo = new Combo(group, SWT.READ_ONLY);
        extensionPointIdCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
                true, false));
        SelectionAdapter adapter = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                logger.debug("widgetSelected(SelectionEvent) - start");

                int selectionIndex = extensionPointIdCombo.getSelectionIndex();
                String clazzName = (String) extensionPointIdCombo
                        .getData("class_" + Integer.toString(selectionIndex));
                clazzName = clazzName == null ? "" : clazzName;
                String descriptionName = (String) extensionPointIdCombo
                        .getData("description_"
                                + Integer.toString(selectionIndex));
                descriptionName = descriptionName == null ? ""
                        : descriptionName;

                String pluginId = (String) extensionPointIdCombo
                        .getData("plugin_" + Integer.toString(selectionIndex));
                pluginId = pluginId == null ? "" : pluginId;

                logger.debug("選択された拡張ポイント: " + extensionPointIdCombo.getText());
                logger.debug("実装クラス: " + clazzName);
                logger.debug("説明: " + descriptionName);
                logger.debug("記述したプラグイン: " + pluginId);

                implementationClassText.setText(clazzName);
                descriptionText.setText(descriptionName);
                pluginIdText.setText(pluginId);

                logger.debug("widgetSelected(SelectionEvent) - end");
            }
        };
        extensionPointIdCombo.addSelectionListener(adapter);

        final Label label = new Label(group, SWT.NONE);
        label.setText("実装クラス");

        implementationClassText = new Text(group, SWT.READ_ONLY);
        implementationClassText.setLayoutData(new GridData(SWT.FILL,
                SWT.CENTER, false, false));

        final Label label_2 = new Label(group, SWT.NONE);
        label_2.setText("説明");

        descriptionText = new Text(group, SWT.READ_ONLY);
        descriptionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
                false));
        new Label(group, SWT.NONE);

        alert = new Text(group, SWT.READ_ONLY);
        alert.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        initCombo();

        final Label label_1 = new Label(container, SWT.NONE);
        label_1.setLayoutData(new GridData());
        label_1.setText("出力先");

        outputFile = new Text(container, SWT.BORDER);
        outputFile.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                logger.debug("modifyText(ModifyEvent) - start");
                validate();
                logger.debug("modifyText(ModifyEvent) - end");
            }

        });
        outputFile
                .setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        final Button button = new Button(container, SWT.NONE);
        button.setLayoutData(new GridData());
        button.addSelectionListener(new SelectionAdapter() {
            // private String[] Executable_Filters = new String[] { "csv" };

            public void widgetSelected(final SelectionEvent e) {
                logger.debug("widgetSelected(SelectionEvent) - start");

                FileDialog dialog = new FileDialog(getShell(), SWT.OPEN
                        | SWT.PRIMARY_MODAL);
                if (outputFile != null) {
                    dialog.setFileName(outputFile.getText());
                }
                dialog.setFileName(extensionPointIdCombo.getText() + ".csv");
                // dialog.setFilterExtensions(Executable_Filters);
                String path = dialog.open();
                outputFile.setText(path);

                logger.debug("widgetSelected(SelectionEvent) - end");
            }
        });
        button.setText("参照(&R)...");

        extensionPointIdCombo.select(0);

        final Label label_4 = new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);

        final Composite composite = new Composite(container, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false,
                3, 1));
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.numColumns = 2;
        composite.setLayout(gridLayout_2);

        final Label label_3 = new Label(composite, SWT.NONE);
        label_3.setText("上の設定を記述したプラグイン：");

        pluginIdText = new Text(composite, SWT.READ_ONLY);
        pluginIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false));

        adapter.widgetSelected(null);
        validate();

        logger.debug("createControl(Composite) - end");
    }

    private void initCombo() {
        logger.debug("initCombo() - start");

        // プラグインのレジストリ取得
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        // レジストリから、拡張ポイント名で拡張ポイントを取得
        IExtensionPoint point = registry
                .getExtensionPoint("nu.mine.kino.plugin.plugindocumentcreator.formatters");
        // この拡張ポイントを使っているプラグイン一覧を取得。
        IConfigurationElement[] configurationElements = point
                .getConfigurationElements();
        int count = 0;
        for (int i = 0; i < configurationElements.length; i++) {
            IConfigurationElement element = configurationElements[i];
            String id = element.getAttribute("extension-point-id");
            String[] items = extensionPointIdCombo.getItems();
            if (contain(id, items)) {
                extensionPointIdCombo.add(id);
                String clazz = element.getAttribute("class");
                String description = element.getAttribute("description");
                extensionPointIdCombo.setData("class_"
                        + Integer.toString(count), clazz);
                extensionPointIdCombo.setData("description_"
                        + Integer.toString(count), description);

                extensionPointIdCombo.setData("plugin_"
                        + Integer.toString(count), element
                        .getDeclaringExtension().getNamespaceIdentifier());
                count++;
            } else {
                alert
                        .setText("※一つの拡張ポイントに複数のフォーマッターが設定されています。先に読み込まれたものが設定されています。");
            }

        }

        logger.debug("initCombo() - end");
    }

    private void validate() {
        logger.debug("validate() - start");
        logger.debug(outputFile.getText());
        setPageComplete(outputFile.getText() != null
                && !outputFile.getText().equals(""));
        logger.debug("validate() - end");
    }

    /**
     * @param id
     * @param items
     * @return
     */
    private boolean contain(String id, String[] items) {
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            if (item.equals(id)) {
                return false;
            }

        }
        return true;
    }

    /**
     * 
     */
    public String getPath() {
        return outputFile.getText();
    }

    public String getExtensionPointId() {
        return extensionPointIdCombo.getText();
    }

}
