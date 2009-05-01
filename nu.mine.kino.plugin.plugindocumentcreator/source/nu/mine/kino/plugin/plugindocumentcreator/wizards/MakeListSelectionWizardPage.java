/******************************************************************************
 * Copyright (c) 2009 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//作成日: 2006/10/22
package nu.mine.kino.plugin.plugindocumentcreator.wizards;

import java.util.List;

import nu.mine.kino.plugin.plugindocumentcreator.Utils;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class MakeListSelectionWizardPage extends WizardPage {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(MakeListSelectionWizardPage.class);

    private static final String ID = "id_";

    private static final String PLUGIN = "plugin_";

    private static final String DESCRIPTION = "description_";

    private static final String CLASS = "class_";

    private static final String EXTENSION_POINT_ID = "extension_point_id_";

    private static final String NAME = "name_";

    private Text extensionPointIdText;

    private Combo idCombo;

    private Text pluginIdText;

    private Text alert;

    private Text descriptionText;

    private Text implementationClassText;

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
     * 
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
        group.setBackground(Display.getCurrent().getSystemColor(
                SWT.COLOR_WIDGET_BACKGROUND));
        group.setText("拡張ポイントの選択");
        group
                .setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false,
                        3, 1));
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 2;
        group.setLayout(gridLayout_1);

        final Label idLabel = new Label(group, SWT.NONE);
        idLabel.setLayoutData(new GridData());
        idLabel.setText("ID");

        idCombo = new Combo(group, SWT.READ_ONLY);
        final GridData gd_idCombo = new GridData(SWT.FILL, SWT.CENTER, true,
                false);
        idCombo.setLayoutData(gd_idCombo);
        idCombo.addSelectionListener(idComboListener);

        idCombo.select(0);

        final Label pluginidLabel = new Label(group, SWT.NONE);
        pluginidLabel.setLayoutData(new GridData());
        pluginidLabel.setText("拡張ポイント");

        extensionPointIdText = new Text(group, SWT.READ_ONLY);
        extensionPointIdText.setBackground(Display.getCurrent().getSystemColor(
                SWT.COLOR_WIDGET_BACKGROUND));
        final GridData gd_extensionPointIdText = new GridData(SWT.FILL,
                SWT.CENTER, true, false);
        extensionPointIdText.setLayoutData(gd_extensionPointIdText);

        final Label label = new Label(group, SWT.NONE);
        label.setLayoutData(new GridData());
        label.setText("実装クラス");

        implementationClassText = new Text(group, SWT.READ_ONLY);
        implementationClassText.setBackground(Display.getCurrent()
                .getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        implementationClassText.setLayoutData(new GridData(SWT.FILL,
                SWT.CENTER, false, false));

        final Label label_2 = new Label(group, SWT.NONE);
        label_2.setLayoutData(new GridData());
        label_2.setText("説明");

        descriptionText = new Text(group, SWT.READ_ONLY);
        descriptionText.setBackground(Display.getCurrent().getSystemColor(
                SWT.COLOR_WIDGET_BACKGROUND));
        descriptionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
                false));
        new Label(group, SWT.NONE);

        alert = new Text(group, SWT.READ_ONLY);
        alert.setBackground(Display.getCurrent().getSystemColor(
                SWT.COLOR_WIDGET_BACKGROUND));
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
        button.addSelectionListener(buttonListener);
        button.setText("参照...");
        // button.setText("参照(&R)...");
        idCombo.select(0);

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
        pluginIdText.setBackground(Display.getCurrent().getSystemColor(
                SWT.COLOR_WIDGET_BACKGROUND));
        pluginIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false));

        idComboListener.widgetSelected(null);
        validate();

        logger.debug("createControl(Composite) - end");
    }

    private void initCombo() {
        logger.debug("initCombo() - start");

        List<IConfigurationElement> list = Utils
                .getConfigurationElements("nu.mine.kino.plugin.plugindocumentcreator.formatters");
        int count = 0;
        for (IConfigurationElement element : list) {
            String id = element.getAttribute("id");
            String[] items = idCombo.getItems();
            // idが重複してなかったら、追加する。
            if (notContains(id, items)) {
                // まずはxmlから値を取得
                String extension_point_id = element
                        .getAttribute("extension-point-id");
                String clazz = element.getAttribute("class");
                String description = element.getAttribute("description");
                String name = element.getAttribute("name");

                // 次にComboにセットするkey値を生成。
                String id_key = ID + Integer.toString(count);
                String extension_point_id_key = EXTENSION_POINT_ID
                        + Integer.toString(count);
                String class_key = CLASS + Integer.toString(count);
                String description_key = DESCRIPTION + Integer.toString(count);
                String name_key = NAME + Integer.toString(count);
                String plugin_key = PLUGIN + Integer.toString(count);

                // 表示する文字列をセット。
                idCombo.add(name);

                // 最後に、Comboに値をセット。
                idCombo.setData(id_key, id);
                idCombo.setData(extension_point_id_key, extension_point_id);
                idCombo.setData(class_key, clazz);
                idCombo.setData(description_key, description);
                idCombo.setData(name_key, name);
                idCombo.setData(plugin_key, element.getDeclaringExtension()
                        .getNamespaceIdentifier());// そのElementを書いてるプラグイン自体のID

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
    private boolean notContains(String id, String[] items) {
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

    public String getExtensionPointConfigKey() {
        String index = Integer.toString(idCombo.getSelectionIndex());
        String id = getDataFromCombo(ID, index);
        return id;
    }

    // プルダウン選択を変更したときの動き。
    SelectionAdapter idComboListener = new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
            logger.debug("widgetSelected(SelectionEvent) - start");

            String index = Integer.toString(idCombo.getSelectionIndex());

            String id = getDataFromCombo(ID, index);
            String extension_point_id = getDataFromCombo(EXTENSION_POINT_ID,
                    index);
            String clazzName = getDataFromCombo(CLASS, index);
            String descriptionName = getDataFromCombo(DESCRIPTION, index);
            String name = getDataFromCombo(NAME, index);
            String pluginId = getDataFromCombo(PLUGIN, index);

            logger.debug("選択されたID: " + id);
            logger.debug("名前: " + name);
            logger.debug("選択された拡張ポイント: " + extension_point_id);
            logger.debug("実装クラス: " + clazzName);
            logger.debug("説明: " + descriptionName);
            logger.debug("この設定が記述されているプラグイン: " + pluginId);

            extensionPointIdText.setText(extension_point_id);
            implementationClassText.setText(clazzName);
            descriptionText.setText(descriptionName);
            pluginIdText.setText(pluginId);

            logger.debug("widgetSelected(SelectionEvent) - end");
        }

    };

    SelectionAdapter buttonListener = new SelectionAdapter() {
        // private String[] Executable_Filters = new String[] { "csv" };

        public void widgetSelected(final SelectionEvent e) {
            logger.debug("widgetSelected(SelectionEvent) - start");

            FileDialog dialog = new FileDialog(getShell(), SWT.SAVE
                    | SWT.PRIMARY_MODAL);
            if (outputFile != null) {
                dialog.setFileName(outputFile.getText());
            }
            dialog.setFileName(extensionPointIdText.getText() + ".csv");
            // dialog.setFilterExtensions(Executable_Filters);
            String path = dialog.open();
            outputFile.setText(path);

            logger.debug("widgetSelected(SelectionEvent) - end");
        }
    };

    private String getDataFromCombo(String prefix, String index) {
        String data = (String) idCombo.getData(prefix + index);
        return null2(data);
    }

    private String null2(String value) {
        return value == null ? "" : value;
    }
}
