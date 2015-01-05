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

public class Issue2Task {

    /**
     * 引数のオブジェクトのプロパティからデータをコピーして戻り値のオブジェクトを生成して返すメソッド。
     * 
     * @param source
     * @return
     */
    public static Task convert(Issue source) {
        Task dest = new Task();

        // 必要に応じて特殊な載せ替え処理 開始
        ((Task) dest).setTaskId(Integer.toString(source.getId()));
        ((Task) dest).setTaskName(source.getSubject());
        ((Task) dest).setPersonInCharge(source.getAssignee().getFullName());
        ((Task) dest).setScheduledStartDate(source.getStartDate());
        ((Task) dest).setScheduledEndDate(source.getDueDate());
        ((Task) dest).setNumberOfManDays(toNumberOfManDays(source));

        // 特殊な載せ替え処理 終了

        return dest;
    }

    /**
     * 第一引数から第二引数へプロパティをコピーするメソッド。
     * 
     * @param source
     * @param dest
     */
    public static void convert(Issue source, Task dest) {
        // 必要に応じて特殊な載せ替え処理 開始
        ((Task) dest).setTaskId(Integer.toString(source.getId()));
        ((Task) dest).setTaskName(source.getSubject());
        ((Task) dest).setPersonInCharge(source.getAssignee().getFullName());
        ((Task) dest).setScheduledStartDate(source.getStartDate());
        ((Task) dest).setScheduledEndDate(source.getDueDate());
        ((Task) dest).setNumberOfManDays(toNumberOfManDays(source));

        // 特殊な載せ替え処理 終了

    }

    public static double toNumberOfManDays(Issue source) {
        return source.getEstimatedHours() != null ? source.getEstimatedHours() / 8.0d
                : Double.NaN;
    }

}
