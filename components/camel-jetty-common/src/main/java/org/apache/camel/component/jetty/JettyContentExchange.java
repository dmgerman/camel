begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|AsyncCallback
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
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|HttpClient
import|;
end_import

begin_interface
DECL|interface|JettyContentExchange
specifier|public
interface|interface
name|JettyContentExchange
block|{
DECL|method|init (Exchange exchange, JettyHttpBinding jettyBinding, HttpClient client, AsyncCallback callback)
name|void
name|init
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|JettyHttpBinding
name|jettyBinding
parameter_list|,
name|HttpClient
name|client
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
function_decl|;
comment|// Methods to prepare the request
DECL|method|setRequestContentType (String contentType)
name|void
name|setRequestContentType
parameter_list|(
name|String
name|contentType
parameter_list|)
function_decl|;
DECL|method|setMethod (String method)
name|void
name|setMethod
parameter_list|(
name|String
name|method
parameter_list|)
function_decl|;
DECL|method|setTimeout (long timeout)
name|void
name|setTimeout
parameter_list|(
name|long
name|timeout
parameter_list|)
function_decl|;
DECL|method|setURL (String url)
name|void
name|setURL
parameter_list|(
name|String
name|url
parameter_list|)
function_decl|;
DECL|method|setRequestContent (byte[] byteArray)
name|void
name|setRequestContent
parameter_list|(
name|byte
index|[]
name|byteArray
parameter_list|)
function_decl|;
DECL|method|setRequestContent (String data, String charset)
name|void
name|setRequestContent
parameter_list|(
name|String
name|data
parameter_list|,
name|String
name|charset
parameter_list|)
throws|throws
name|UnsupportedEncodingException
function_decl|;
DECL|method|setRequestContent (InputStream ins)
name|void
name|setRequestContent
parameter_list|(
name|InputStream
name|ins
parameter_list|)
function_decl|;
DECL|method|setRequestContent (InputStream ins, int contentLength)
name|void
name|setRequestContent
parameter_list|(
name|InputStream
name|ins
parameter_list|,
name|int
name|contentLength
parameter_list|)
function_decl|;
DECL|method|addRequestHeader (String key, String s)
name|void
name|addRequestHeader
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|s
parameter_list|)
function_decl|;
DECL|method|setSupportRedirect (boolean supportRedirect)
name|void
name|setSupportRedirect
parameter_list|(
name|boolean
name|supportRedirect
parameter_list|)
function_decl|;
comment|/*      * Send using jetty HttpClient and return. The callback will be called when the response arrives      */
DECL|method|send (HttpClient client)
name|void
name|send
parameter_list|(
name|HttpClient
name|client
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|// Methods to retrieve the response
DECL|method|getBody ()
name|byte
index|[]
name|getBody
parameter_list|()
function_decl|;
DECL|method|getUrl ()
name|String
name|getUrl
parameter_list|()
function_decl|;
DECL|method|getResponseStatus ()
name|int
name|getResponseStatus
parameter_list|()
function_decl|;
DECL|method|getResponseContentBytes ()
name|byte
index|[]
name|getResponseContentBytes
parameter_list|()
function_decl|;
DECL|method|getResponseHeaders ()
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|String
argument_list|>
argument_list|>
name|getResponseHeaders
parameter_list|()
function_decl|;
DECL|method|getRequestHeaders ()
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|String
argument_list|>
argument_list|>
name|getRequestHeaders
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

