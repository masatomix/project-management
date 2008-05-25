/*******************************************************************************
 * Copyright (c) 2008 Masatomi KINO. All rights reserved. $Id$
 ******************************************************************************/
// çÏê¨ì˙: 2008/05/19
package nu.mine.kino.mail;

public class FilterSystemException extends RuntimeException {

    /**
     * <code>serialVersionUID</code> ÇÃÉRÉÅÉìÉg
     */
    private static final long serialVersionUID = -6778850343077538308L;

    public FilterSystemException() {
        super();
    }

    public FilterSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public FilterSystemException(String message) {
        super(message);
    }

    public FilterSystemException(Throwable cause) {
        super(cause);
    }

    // private final String mailData;

    // public FilterSystemException(String s, String mailData) {
    // super(s);
    // this.mailData = mailData;
    // }
    //
    // public String getMailData() {
    // return mailData;
    // }

}
