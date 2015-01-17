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

import com.taskadapter.redmineapi.bean.Issue;

public class Issue2EVTotalBean {

    /**
     * 引数のオブジェクトのプロパティからデータをコピーして戻り値のオブジェクトを生成して返すメソッド。
     * 
     * @param source
     * @return
     */
    public static EVTotalBean convert(Issue source) {
        EVTotalBean dest = new EVTotalBean();

        // 必要に応じて特殊な載せ替え処理 開始
        ((EVTotalBean) dest).setTaskId(Integer.toString(source.getId()));
        ((EVTotalBean) dest).setProgressRate(toProgressRate(source));

        // 特殊な載せ替え処理 終了

        return dest;
    }

    /**
     * 第一引数から第二引数へプロパティをコピーするメソッド。
     * 
     * @param source
     * @param dest
     */
    public static void convert(Issue source, EVTotalBean dest) {
        // 必要に応じて特殊な載せ替え処理 開始
        ((EVTotalBean) dest).setTaskId(Integer.toString(source.getId()));
        ((EVTotalBean) dest).setProgressRate(toProgressRate(source));

        // 特殊な載せ替え処理 終了

    }

    public static double toProgressRate(Issue source) {
        return source.getDoneRatio() != null ? source.getDoneRatio() / 100.0d
                : Double.NaN;
    }

}
