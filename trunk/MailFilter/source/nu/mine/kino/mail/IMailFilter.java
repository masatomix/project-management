/*******************************************************************************
 * Copyright (c) 2008 Masatomi KINO. All rights reserved. $Id$
 ******************************************************************************/
// 作成日: 2008/05/19
package nu.mine.kino.mail;

/**
 * メールをフィルタするインタフェースです。
 * 
 * @author Masatomi KINO
 * @version $Revision$
 */
public interface IMailFilter {
    /**
     * 引数にメールデータが渡ってくるので、内容を見てフィルターしてください。 基本的には、
     * <ul>
     * <li>ＯＫなら、そのままもらったデータを戻す</li>
     * <li>ＮＧで処理を進めさせたくないなら、例外をスローする</li>
     * </ul>
     * とすればよいと思います。場合によっては内容を編集して戻してもよいと思います。
     * 
     * @param mailData
     * @return 次のフィルタに渡すためのメールデータ
     * @throws FilterException
     */
    public String doFilter(String mailData) throws FilterException;
}
