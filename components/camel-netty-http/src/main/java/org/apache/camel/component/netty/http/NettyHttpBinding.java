begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
operator|.
name|http
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|FullHttpRequest
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
name|FullHttpResponse
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
name|HttpRequest
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
name|HttpResponse
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
name|apache
operator|.
name|camel
operator|.
name|Message
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
name|spi
operator|.
name|HeaderFilterStrategy
import|;
end_import

begin_comment
comment|/**  * To bind Netty http codec with the Camel {@link org.apache.camel.Message} api.  */
end_comment

begin_interface
DECL|interface|NettyHttpBinding
specifier|public
interface|interface
name|NettyHttpBinding
block|{
comment|/**      * Binds from Netty {@link HttpRequest} to Camel {@link Message}.      *<p/>      * Will use the<tt>populateCamelHeaders</tt> method for populating the headers.      *      * @param request       the netty http request      * @param exchange      the exchange that should contain the returned message.      * @param configuration the endpoint configuration      * @return the message to store on the given exchange      * @throws Exception is thrown if error during binding      */
DECL|method|toCamelMessage (FullHttpRequest request, Exchange exchange, NettyHttpConfiguration configuration)
name|Message
name|toCamelMessage
parameter_list|(
name|FullHttpRequest
name|request
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|NettyHttpConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Binds from Netty {@link InboundStreamHttpRequest} to Camel {@link Message}.      *<p/>      *      * @param request       the netty http request      * @param exchange      the exchange that should contain the returned message.      * @param configuration the endpoint configuration      * @return the message to store on the given exchange      * @throws Exception is thrown if error during binding      */
DECL|method|toCamelMessage (InboundStreamHttpRequest request, Exchange exchange, NettyHttpConfiguration configuration)
name|Message
name|toCamelMessage
parameter_list|(
name|InboundStreamHttpRequest
name|request
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|NettyHttpConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Binds from Netty {@link HttpRequest} to Camel headers as a {@link Map}.      * Will use the<tt>populateCamelHeaders</tt> method for populating the headers.      *      * @param request       the netty http request      * @param headers       the Camel headers that should be populated      * @param exchange      the exchange that should contain the returned message.      * @param configuration the endpoint configuration      * @throws Exception is thrown if error during binding      */
DECL|method|populateCamelHeaders (HttpRequest request, Map<String, Object> headers, Exchange exchange, NettyHttpConfiguration configuration)
name|void
name|populateCamelHeaders
parameter_list|(
name|HttpRequest
name|request
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|NettyHttpConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Binds from Netty {@link HttpResponse} to Camel {@link Message}.      *<p/>      * Will use the<tt>populateCamelHeaders</tt> method for populating the headers.      *      * @param response      the netty http response      * @param exchange      the exchange that should contain the returned message.      * @param configuration the endpoint configuration      * @return the message to store on the given exchange      * @throws Exception is thrown if error during binding      */
DECL|method|toCamelMessage (FullHttpResponse response, Exchange exchange, NettyHttpConfiguration configuration)
name|Message
name|toCamelMessage
parameter_list|(
name|FullHttpResponse
name|response
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|NettyHttpConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Binds from Netty {@link InboundStreamHttpResponse} to Camel {@link Message}.      *<p/>      * Will use the<tt>populateCamelHeaders</tt> method for populating the headers.      *      * @param response      the netty http response      * @param exchange      the exchange that should contain the returned message.      * @param configuration the endpoint configuration      * @return the message to store on the given exchange      * @throws Exception is thrown if error during binding      */
DECL|method|toCamelMessage (InboundStreamHttpResponse response, Exchange exchange, NettyHttpConfiguration configuration)
name|Message
name|toCamelMessage
parameter_list|(
name|InboundStreamHttpResponse
name|response
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|NettyHttpConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Binds from Netty {@link HttpResponse} to Camel headers as a {@link Map}.      *      * @param response      the netty http response      * @param headers       the Camel headers that should be populated      * @param exchange      the exchange that should contain the returned message.      * @param configuration the endpoint configuration      * @throws Exception is thrown if error during binding      */
DECL|method|populateCamelHeaders (HttpResponse response, Map<String, Object> headers, Exchange exchange, NettyHttpConfiguration configuration)
name|void
name|populateCamelHeaders
parameter_list|(
name|HttpResponse
name|response
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|NettyHttpConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Binds from Camel {@link Message} to Netty {@link HttpResponse}.      *      * @param message       the Camel message      * @param configuration the endpoint configuration      * @return the http response      * @throws Exception is thrown if error during binding      */
DECL|method|toNettyResponse (Message message, NettyHttpConfiguration configuration)
name|HttpResponse
name|toNettyResponse
parameter_list|(
name|Message
name|message
parameter_list|,
name|NettyHttpConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Binds from Camel {@link Message} to Netty {@link io.netty.handler.codec.http.HttpRequest}.      *      * @param message       the Camel message      * @param uri           the uri which is the intended uri to call, though the message may override the uri      * @param configuration the endpoint configuration      * @return the http request      * @throws Exception is thrown if error during binding      */
DECL|method|toNettyRequest (Message message, String uri, NettyHttpConfiguration configuration)
name|HttpRequest
name|toNettyRequest
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|uri
parameter_list|,
name|NettyHttpConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Gets the header filter strategy      *      * @return the strategy      */
DECL|method|getHeaderFilterStrategy ()
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
function_decl|;
comment|/**      * Sets the header filter strategy to use.      *      * @param headerFilterStrategy the custom strategy      */
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy headerFilterStrategy)
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

