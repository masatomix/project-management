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
//作成日: 2009/05/08
package nu.mine.kino.dbunit.ant;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITableIterator;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.operation.DatabaseOperation;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class ExcelImportTask extends Task {
    private String driver;

    private String url;

    private String userId;

    private String password;

    private String schema;

    private Import importt;

    public Import createImport() {
        if (importt == null) {
            importt = new Import();
        }
        return importt;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.tools.ant.Task#execute()
     */
    @Override
    public void execute() throws BuildException {
        IDatabaseConnection con = null;
        try {
            con = getConnection();
            IDataSet dataset = new XlsDataSet(new File(importt.getSrc()));
            ITableIterator iterator = dataset.iterator();
            System.out.println("-------------------");
            while (iterator.next()) {
                System.out.println(iterator.getTableMetaData().getTableName());
            }
            System.out.println("-------------------");
            DatabaseOperation.CLEAN_INSERT.execute(con, dataset);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BuildException(e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private IDatabaseConnection getConnection() throws Exception {
        System.out.println("driver   : " + driver);
        System.out.println("url      : " + url);
        System.out.println("userId   : " + userId);
        System.out.println("password : " + password);
        System.out.println("schema   : " + schema);
        System.out.println("import   : "
                + new File(importt.getSrc()).getAbsolutePath());
        Class driverClass = Class.forName(driver);
        Connection connection = DriverManager.getConnection(url, userId,
                password);
        return new DatabaseConnection(connection, schema);
    }

    /**
     * @param driver
     *            driver を設定。
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }

    /**
     * @param importt
     *            export を設定。
     */
    public void setImport(Import importt) {
        this.importt = importt;
    }

    /**
     * @param password
     *            password を設定。
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param schema
     *            schema を設定。
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * @param url
     *            url を設定。
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @param userId
     *            userId を設定。
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
