begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ahc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ahc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpHeaders
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|HttpResponseStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|Request
import|;
end_import

begin_comment
comment|/**  * Binding from Camel to/from {@link com.ning.http.client.AsyncHttpClient}  */
end_comment

begin_interface
DECL|interface|AhcBinding
specifier|public
interface|interface
name|AhcBinding
block|{
comment|/**      * Prepares the AHC {@link Request} to be send.      *      * @param endpoint the endpoint      * @param exchange the exchange      * @return the request to send using the {@link com.ning.http.client.AsyncHttpClient}      * @throws Exception is thrown if error occurred preparing the request      */
DECL|method|prepareRequest (AhcEndpoint endpoint, Exchange exchange)
name|Request
name|prepareRequest
parameter_list|(
name|AhcEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Callback from the {@link com.ning.http.client.AsyncHttpClient} when an exception occurred sending the request.      *      * @param endpoint the endpoint      * @param exchange the exchange      * @param t        the thrown exception      * @throws Exception is thrown if error occurred in the callback      */
DECL|method|onThrowable (AhcEndpoint endpoint, Exchange exchange, Throwable t)
name|void
name|onThrowable
parameter_list|(
name|AhcEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|t
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Callback from the {@link com.ning.http.client.AsyncHttpClient} when the HTTP response status was received      *      * @param endpoint       the endpoint      * @param exchange       the exchange      * @param responseStatus the HTTP response status      * @throws Exception is thrown if error occurred in the callback      */
DECL|method|onStatusReceived (AhcEndpoint endpoint, Exchange exchange, HttpResponseStatus responseStatus)
name|void
name|onStatusReceived
parameter_list|(
name|AhcEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|HttpResponseStatus
name|responseStatus
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Callback from the {@link com.ning.http.client.AsyncHttpClient} when the HTTP headers was received      *      * @param endpoint the endpoint      * @param exchange the exchange      * @param headers  the HTTP headers      * @throws Exception is thrown if error occurred in the callback      */
DECL|method|onHeadersReceived (AhcEndpoint endpoint, Exchange exchange, HttpHeaders headers)
name|void
name|onHeadersReceived
parameter_list|(
name|AhcEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|HttpHeaders
name|headers
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Callback from the {@link com.ning.http.client.AsyncHttpClient} when complete and all the response has been received.      *      *      * @param endpoint      the endpoint      * @param exchange      the exchange      * @param url           the url requested      * @param os            output stream with the HTTP response body      * @param contentLength length of the response body      * @param statusCode    the http response code      * @param statusText    the http status text      * @throws Exception is thrown if error occurred in the callback      */
DECL|method|onComplete (AhcEndpoint endpoint, Exchange exchange, String url, ByteArrayOutputStream os, int contentLength, int statusCode, String statusText)
name|void
name|onComplete
parameter_list|(
name|AhcEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|String
name|url
parameter_list|,
name|ByteArrayOutputStream
name|os
parameter_list|,
name|int
name|contentLength
parameter_list|,
name|int
name|statusCode
parameter_list|,
name|String
name|statusText
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

