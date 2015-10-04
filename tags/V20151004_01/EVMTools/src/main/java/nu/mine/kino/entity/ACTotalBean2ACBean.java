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

public class ACTotalBean2ACBean {

    /**
     * 引数のオブジェクトのプロパティからデータをコピーして戻り値のオブジェクトを生成して返すメソッド。
     * 
     * @param source
     * @return
     */
    public static ACBean convert(ACTotalBean source) {
        ACBean dest = new ACBean();

        // 必要に応じて特殊な載せ替え処理 開始
        ((ACBean) dest).setId(source.getId());
        ((ACBean) dest).setTaskId(source.getTaskId());
        ((ACBean) dest).setBaseDate(source.getBaseDate());
        ((ACBean) dest).setActualCost(source.getActualCost());

        // 特殊な載せ替え処理 終了

        return dest;
    }

    /**
     * 第一引数から第二引数へプロパティをコピーするメソッド。
     * 
     * @param source
     * @param dest
     */
    public static void convert(ACTotalBean source, ACBean dest) {
        // 必要に応じて特殊な載せ替え処理 開始
        ((ACBean) dest).setId(source.getId());
        ((ACBean) dest).setTaskId(source.getTaskId());
        ((ACBean) dest).setBaseDate(source.getBaseDate());
        ((ACBean) dest).setActualCost(source.getActualCost());

        // 特殊な載せ替え処理 終了

    }

}
