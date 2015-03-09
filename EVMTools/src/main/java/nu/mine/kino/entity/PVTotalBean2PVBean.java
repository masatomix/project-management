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

public class PVTotalBean2PVBean {

    /**
     * 引数のオブジェクトのプロパティからデータをコピーして戻り値のオブジェクトを生成して返すメソッド。
     * 
     * @param source
     * @return
     */
    public static PVBean convert(PVTotalBean source) {
        PVBean dest = new PVBean();

        // 必要に応じて特殊な載せ替え処理 開始
        ((PVBean) dest).setId(source.getId());
        ((PVBean) dest).setTaskId(source.getTaskId());
        ((PVBean) dest).setBaseDate(source.getBaseDate());
        ((PVBean) dest).setPlannedValue(source.getPlannedValue());

        // 特殊な載せ替え処理 終了

        return dest;
    }

    /**
     * 第一引数から第二引数へプロパティをコピーするメソッド。
     * 
     * @param source
     * @param dest
     */
    public static void convert(PVTotalBean source, PVBean dest) {
        // 必要に応じて特殊な載せ替え処理 開始
        ((PVBean) dest).setId(source.getId());
        ((PVBean) dest).setTaskId(source.getTaskId());
        ((PVBean) dest).setBaseDate(source.getBaseDate());
        ((PVBean) dest).setPlannedValue(source.getPlannedValue());

        // 特殊な載せ替え処理 終了

    }

}
