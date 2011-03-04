/* 
 * Copyright 2011 Kazuhiro Sera
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language 
 * governing permissions and limitations under the License. 
 */
package com.github.seratch.signedrequest4j;

/**
 * Exception thrown by this client library
 *
 * @author <a href="mailto:seratch@gmail.com">Kazuhiro Sera</a>
 */
public class SignedRequestClientException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String detailMessage;

    public SignedRequestClientException(String message) {
        this.detailMessage = message;
    }

    public SignedRequestClientException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getLocalizedMessage() {
        return detailMessage;
    }

    @Override
    public String getMessage() {
        return detailMessage;
    }

}
