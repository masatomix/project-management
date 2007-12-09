/*******************************************************************************
 * Copyright (c) 2006 Masatomi KINO.
 * All rights reserved. 
 * $Id$
 *******************************************************************************/
//çÏê¨ì˙: 2007/12/08
package nu.mine.kino.utils.db;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class Main {
    public static void main(String[] args) {
        FileSystemXmlApplicationContext context1 = new FileSystemXmlApplicationContext(
                new String[] { "beans_db.xml" });
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { "beans.xml", "beans_auto.xml" }, context1);

        // ClassPathXmlApplicationContext context = new
        // ClassPathXmlApplicationContext(
        // new String[] { "beans.xml", "beans_auto.xml","beans_db.xml" } );
        FKEnabler enabler = (FKEnabler) context
                .getBean("nu.mine.kino.utils.db.FKEnabler");
        enabler.enableAll();
        enabler.disableAll();

        FKChecker checker = (FKChecker) context
                .getBean("nu.mine.kino.utils.db.FKChecker");
        // List<Map<String, String>> name = checker.findAll();
        // for (Map<String, String> map : name) {
        // String tableName = map.get("TABLE_NAME");
        // String fkName = map.get("CONSTRAINT_NAME");
        // String status = map.get("STATUS");
        // System.out.println(tableName + ":" + fkName + ":" + status);
        // }

        List<String> fks = checker.findFKNameByTableName("CUSTOMER_ATTR");
        for (String fk : fks) {
            System.out.println("FK: " + fk);
        }

        boolean b = checker.exist("CUSTOMER_ATTR", "FK_CUSTOMER");
        System.out.println(b);
        List<String> name2 = checker.findFKNameByTableName(null);
        for (String string : name2) {
            System.out.println(string);
        }
    }
}
