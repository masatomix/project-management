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

public class EVBean2EVViewBean {

    /**
     * 引数のオブジェクトのプロパティからデータをコピーして戻り値のオブジェクトを生成して返すメソッド。
     * 
     * @param source
     * @return
     */
    public static EVViewBean convert(EVBean source) {
        EVViewBean dest = new EVViewBean();

        // 必要に応じて特殊な載せ替え処理 開始
        ((EVViewBean) dest).setId(source.getId());
        ((EVViewBean) dest).setTaskId(source.getTaskId());
        ((EVViewBean) dest).setProgressRate(source.getProgressRate());
        ((EVViewBean) dest).setEarnedValue(source.getEarnedValue());
        ((EVViewBean) dest).setBaseDate(source.getBaseDate());

        // 特殊な載せ替え処理 終了

        return dest;
    }

    /**
     * 第一引数から第二引数へプロパティをコピーするメソッド。
     * 
     * @param source
     * @param dest
     */
    public static void convert(EVBean source, EVViewBean dest) {
        // 必要に応じて特殊な載せ替え処理 開始
        ((EVViewBean) dest).setId(source.getId());
        ((EVViewBean) dest).setTaskId(source.getTaskId());
        ((EVViewBean) dest).setProgressRate(source.getProgressRate());
        ((EVViewBean) dest).setEarnedValue(source.getEarnedValue());
        ((EVViewBean) dest).setBaseDate(source.getBaseDate());

        // 特殊な載せ替え処理 終了

    }

}
