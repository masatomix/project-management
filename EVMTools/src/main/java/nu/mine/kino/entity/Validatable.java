/******************************************************************************
 * Copyright (c) 2014 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//作成日: 2014/11/14

package nu.mine.kino.entity;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public interface Validatable {

    /**
     * オブジェクトが有効かどうかを判定する。条件が,長さが 0の配列であったり nullの場合は付帯条件は評価に影響しない
     * 
     * @param conditions
     *            付帯条件
     * @return
     */
    public boolean isValid(Object... conditions);

}
