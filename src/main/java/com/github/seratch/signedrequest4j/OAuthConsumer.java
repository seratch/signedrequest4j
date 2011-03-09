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
 * OAuth Consumer
 *
 * @author <a href="mailto:seratch@gmail.com">Kazuhiro Sera</a>
 * @see <a href="http://oauth.net/core/1.0/">OAuth Core 1.0</a>
 */
public class OAuthConsumer implements NotString {

    /**
     * OAuth consumer key
     */
    private final String consumerKey;

    /**
     * OAuth consumer secret
     */
    private final String consumerSecret;

    public OAuthConsumer(String consumerKey, String consumerSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

}
