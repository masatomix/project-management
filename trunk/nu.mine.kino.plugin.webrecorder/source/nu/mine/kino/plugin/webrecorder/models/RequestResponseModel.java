/******************************************************************************
 * Copyright (c) 2012 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//�쐬��: 2012/07/07

package nu.mine.kino.plugin.webrecorder.models;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class RequestResponseModel {

    private Integer status;

    private String host;

    private String url;

    private String method;

    private String reqContentType;

    private String reqContentLength;

    private String requestBody;

    private String queryString;

    private String resContentType;

    private String resContentLength;

    private Date resDate;

    private String rawRequest;

    private String rawResponse;

    public String getHost() {
        return host;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public String getReqContentType() {
        return reqContentType;
    }

    public String getReqContentLength() {
        return reqContentLength;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setReqContentType(String reqContentType) {
        this.reqContentType = reqContentType;
    }

    public void setReqContentLength(String reqContentLength) {
        this.reqContentLength = reqContentLength;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getResContentType() {
        return resContentType;
    }

    public String getResContentLength() {
        return resContentLength;
    }

    public Date getResDate() {
        return resDate;
    }

    public void setResContentType(String resContentType) {
        this.resContentType = resContentType;
    }

    public void setResContentLength(String resContentLength) {
        this.resContentLength = resContentLength;
    }

    public void setResDate(Date resDate) {
        this.resDate = resDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRawRequest() {
        return rawRequest;
    }

    public String getRawResponse() {
        return rawResponse;
    }

    public void setRawRequest(String rawRequest) {
        this.rawRequest = rawRequest;
    }

    public void setRawResponse(String rawResponse) {
        this.rawResponse = rawResponse;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("status", status)
                .append("host", host).append("url", url)
                .append("method", method)
                .append("reqContentType", reqContentType)
                .append("reqContentLength", reqContentLength)
                .append("requestBody", requestBody)
                .append("queryString", queryString)
                .append("resContentType", resContentType)
                .append("resContentLength", resContentLength)
                .append("resDate", resDate).append("rawRequest", rawRequest)
                .append("rawResponse", rawResponse).toString();
    }

}
