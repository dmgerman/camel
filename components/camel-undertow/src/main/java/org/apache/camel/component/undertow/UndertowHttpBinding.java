begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.undertow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|undertow
operator|.
name|client
operator|.
name|ClientExchange
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|client
operator|.
name|ClientRequest
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|client
operator|.
name|ClientResponse
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|server
operator|.
name|HttpServerExchange
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
comment|/**  * Interface to define custom binding for the component  */
end_comment

begin_interface
DECL|interface|UndertowHttpBinding
specifier|public
interface|interface
name|UndertowHttpBinding
block|{
DECL|method|toCamelMessage (HttpServerExchange httpExchange, Exchange exchange)
name|Message
name|toCamelMessage
parameter_list|(
name|HttpServerExchange
name|httpExchange
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|toCamelMessage (ClientExchange clientExchange, Exchange exchange)
name|Message
name|toCamelMessage
parameter_list|(
name|ClientExchange
name|clientExchange
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|populateCamelHeaders (HttpServerExchange httpExchange, Map<String, Object> headerMap, Exchange exchange)
name|void
name|populateCamelHeaders
parameter_list|(
name|HttpServerExchange
name|httpExchange
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headerMap
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|populateCamelHeaders (ClientResponse response, Map<String, Object> headerMap, Exchange exchange)
name|void
name|populateCamelHeaders
parameter_list|(
name|ClientResponse
name|response
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headerMap
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|toHttpResponse (HttpServerExchange httpExchange, Message message)
name|Object
name|toHttpResponse
parameter_list|(
name|HttpServerExchange
name|httpExchange
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|IOException
function_decl|;
DECL|method|toHttpRequest (ClientRequest clientRequest, Message message)
name|Object
name|toHttpRequest
parameter_list|(
name|ClientRequest
name|clientRequest
parameter_list|,
name|Message
name|message
parameter_list|)
function_decl|;
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy headerFilterStrategy)
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
function_decl|;
DECL|method|setTransferException (Boolean transferException)
name|void
name|setTransferException
parameter_list|(
name|Boolean
name|transferException
parameter_list|)
function_decl|;
DECL|method|setMuteException (Boolean muteException)
name|void
name|setMuteException
parameter_list|(
name|Boolean
name|muteException
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

