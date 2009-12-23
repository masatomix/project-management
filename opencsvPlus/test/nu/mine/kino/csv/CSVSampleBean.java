/******************************************************************************
 * Copyright (c) 2008-2009 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/

package nu.mine.kino.csv;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * JavaBeans Generatorの為のサンプルJavaBeansです。
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 * @version $Revision$
 */
public class CSVSampleBean implements Serializable {

    /**
     * 姓 です
     */
    private String last_name;

    /**
     * 名 です
     */
    private String first_name;

    /**
     * 年齢 です
     */
    private String age;

    /**
     * 姓をセットする。
     * 
     * @param last_name
     *            姓
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * 名をセットする。
     * 
     * @param first_name
     *            名
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * 年齢をセットする。
     * 
     * @param age
     *            年齢
     */
    public void setAge(String age) {
        this.age = age;
    }

    /**
     * 姓を取得する。
     * 
     * @return 姓
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * 名を取得する。
     * 
     * @return 名
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * 年齢を取得する。
     * 
     * @return 年齢
     */
    public String getAge() {
        return age;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("姓", last_name).append("名",
                first_name).append("年齢", age).toString();
    }
}