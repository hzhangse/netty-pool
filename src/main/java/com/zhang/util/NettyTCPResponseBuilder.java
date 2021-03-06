/*
 * Copyright 2014 The LightNettyClient Project
 *
 * The Light netty client Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.zhang.util;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.zhang.client.NettyTCPResponse;

/**
 * @author xianwu.zhang
 */
public class NettyTCPResponseBuilder {

    

    private volatile List<ByteBuf>     pendingContents;

    private volatile Throwable         cause;

    private volatile NettyTCPResponse content;

    private AtomicBoolean              isBuild = new AtomicBoolean(false);

    private volatile boolean           success = false;

    public NettyTCPResponse build() {
        if (isBuild.getAndSet(true)) {
            return content;
        }
        NettyTCPResponse response = new NettyTCPResponse();
        content = response;

        if (success) {
            response.setSuccess(true);
          
            response.setContents(pendingContents);
        } else {
            response.setCause(cause);
        }
        return content;
    }

    public void addContent(ByteBuf byteBuf) {
        if (null == pendingContents) {
            pendingContents = new ArrayList<ByteBuf>();
        }
        pendingContents.add(byteBuf);
    }

    /**
     * @return the contents
     */
    public List<ByteBuf> getContents() {
        return pendingContents;
    }

 

    /**
     * Getter method for property <tt>pendingContents</tt>.
     * 
     * @return property value of pendingContents
     */
    public List<ByteBuf> getPendingContents() {
        return pendingContents;
    }

    /**
     * Setter method for property <tt>pendingContents</tt>.
     * 
     * @param pendingContents value to be assigned to property pendingContents
     */
    public void setPendingContents(List<ByteBuf> pendingContents) {
        this.pendingContents = pendingContents;
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
}
