
package com.zhang.client;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;


public class NettyTCPResponse {
    private volatile boolean            success = false;
   
    private volatile List<ByteBuf>      contents;
    private volatile Throwable          cause;

    public NettyTCPResponse() {
        super();
    }

    public String getResponseBody() {
        return getResponseBody(Charset.forName("GBK"));
    }

    public String getResponseBody(Charset charset) {
        if (null == contents || 0 == contents.size()) {
            return null;
        }
        StringBuilder responseBody = new StringBuilder();
        for (ByteBuf content : contents) {
            responseBody.append(content.toString(charset));
        }

        return responseBody.toString();
    }

    public void addContent(ByteBuf byteBuf) {
        if (null == contents) {
            contents = new ArrayList<ByteBuf>();
        }
        contents.add(byteBuf);
    }


    /**
     * @return the contents
     */
    public List<ByteBuf> getContents() {
        return contents;
    }

    /**
     * @param contents the contents to set
     */
    public void setContents(List<ByteBuf> contents) {
        this.contents = contents;
    }

   


    /**
     * Getter method for property <tt>success</tt>.
     * 
     * @return property value of success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Setter method for property <tt>success</tt>.
     * 
     * @param success value to be assigned to property success
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Getter method for property <tt>cause</tt>.
     * 
     * @return property value of cause
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * Setter method for property <tt>cause</tt>.
     * 
     * @param cause value to be assigned to property cause
     */
    public void setCause(Throwable cause) {
        this.cause = cause;
    }

}
