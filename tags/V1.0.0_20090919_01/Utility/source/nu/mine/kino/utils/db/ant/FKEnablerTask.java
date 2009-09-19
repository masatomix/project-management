package nu.mine.kino.utils.db.ant;

import java.util.ArrayList;
import java.util.List;

import nu.mine.kino.utils.common.ApplicationContextManager;
import nu.mine.kino.utils.db.FKEnabler;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.springframework.context.ApplicationContext;

public class FKEnablerTask extends Task {
    private String method;

    private List<Table> tables = new ArrayList<Table>();

    public Table createTable() {
        Table table = new Table();
        tables.add(table);
        return table;
    }

    public void execute() throws BuildException {

        // コマンドラインからのAntでコケちゃった。
        // 結局以下(のさらに下)のコードを書けばOKのようです。
        // code to handle cnf issues with taskdef classloader
        // AntClassLoader2 antClassLoader = null;
        // Object obj = this.getClass().getClassLoader();
        // if (obj instanceof AntClassLoader2) {
        // antClassLoader = (AntClassLoader2) obj;
        // antClassLoader.setThreadContextLoader();
        // }
        // end code to handle classnotfound issue

        // これが正しい？？
        // http://forum.springframework.org/showthread.php?t=14635
        // http://blog.xole.net/article.php?id=573
        // AntClassLoader antClassLoader = null;
        // ClassLoader obj = this.getClass().getClassLoader();
        // if (AntClassLoader.class.isAssignableFrom(obj.getClass())) {
        // antClassLoader = (AntClassLoader) obj;
        // antClassLoader.setThreadContextLoader();
        // }

        if (method == null || "".equals(method)) {
            throw new BuildException("method属性は必須項目です。");
        }

        // FileSystemXmlApplicationContext context1 = new
        // FileSystemXmlApplicationContext(
        // new String[] { "beans_db.xml" });
        // ClassPathXmlApplicationContext context = new
        // ClassPathXmlApplicationContext(
        // new String[] { "beans.xml", "beans_auto.xml" }, context1);

        Thread currentThread = Thread.currentThread();
        ClassLoader oldClassLoader = currentThread.getContextClassLoader();

        try {

            currentThread.setContextClassLoader(this.getClass()
                    .getClassLoader());

            ApplicationContext context = ApplicationContextManager
                    .getInstance().getContext();
            FKEnabler enabler = (FKEnabler) context
                    .getBean("nu.mine.kino.utils.db.FKEnabler");
            if (method.equals("enableAll")) {
                enabler.enableAll();
            } else if (method.equals("disableAll")) {
                enabler.disableAll();
            } else if (method.equals("enable")) {
                if (tables == null || tables.size() <= 0) {
                    throw new BuildException(
                            "method属性にenableを指定した場合は子要素tableでテーブル名とFK名を指定する必要があります。");
                }
                for (Table table : tables) {
                    enabler.enable(table.getName(), table.getFkname());
                }
            } else if (method.equals("disable")) {
                if (tables == null || tables.size() <= 0) {
                    throw new BuildException(
                            "method属性にdisableを指定した場合は子要素tableでテーブル名とFK名を指定する必要があります。");
                }
                for (Table table : tables) {
                    enabler.disable(table.getName(), table.getFkname());
                }
            } else {
                throw new BuildException(
                        "method属性の値に不正な値が記述されています。現在サポートしている値はenableAll,disableAll,enable,disableのみです。");
            }
        } finally {
            currentThread.setContextClassLoader(oldClassLoader);
        }

    }

    // /**
    // * @return method
    // */
    // public String getMethod() {
    // return method;
    // }

    /**
     * @param method
     *            method を設定。
     */
    public void setMethod(String method) {
        this.method = method;
    }

}