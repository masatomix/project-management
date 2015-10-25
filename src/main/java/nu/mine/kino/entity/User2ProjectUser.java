/******************************************************************************
 * Copyright (c) 2008-2014 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 ******************************************************************************/

package nu.mine.kino.entity;

import hudson.model.User;

public class User2ProjectUser {

    /**
     * 引数のオブジェクトのプロパティからデータをコピーして戻り値のオブジェクトを生成して返すメソッド。
     * 
     * @param source
     * @return
     */
    public static ProjectUser convert(User source) {
        ProjectUser dest = new ProjectUser();

        // 必要に応じて特殊な載せ替え処理 開始
        ((ProjectUser) dest).setId(source.getId());
        ((ProjectUser) dest).setFullName(source.getFullName());
        ((ProjectUser) dest).setDescription(source.getDescription());
        ((ProjectUser) dest).setEmailAddress(getEmail(source));
        ((ProjectUser) dest).setUrl(source.getUrl());

        // 特殊な載せ替え処理 終了

        return dest;
    }

    /**
     * 第一引数から第二引数へプロパティをコピーするメソッド。
     * 
     * @param source
     * @param dest
     */
    public static void convert(User source, ProjectUser dest) {
        // 必要に応じて特殊な載せ替え処理 開始
        ((ProjectUser) dest).setId(source.getId());
        ((ProjectUser) dest).setFullName(source.getFullName());
        ((ProjectUser) dest).setDescription(source.getDescription());
        ((ProjectUser) dest).setEmailAddress(getEmail(source));
        ((ProjectUser) dest).setUrl(source.getUrl());

        // 特殊な載せ替え処理 終了

    }

    private static String getEmail(User source) {
        hudson.tasks.Mailer.UserProperty property = source
                .getProperty(hudson.tasks.Mailer.UserProperty.class);
        // System.out.println("pro: " + property.getAddress());
        return property.getAddress();
    }

}
