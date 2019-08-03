begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.coap
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|coap
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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
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
name|support
operator|.
name|DefaultProducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|core
operator|.
name|CoapClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|core
operator|.
name|CoapResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|core
operator|.
name|coap
operator|.
name|MediaTypeRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|core
operator|.
name|network
operator|.
name|CoapEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|core
operator|.
name|network
operator|.
name|config
operator|.
name|NetworkConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|elements
operator|.
name|tcp
operator|.
name|TcpClientConnector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|elements
operator|.
name|tcp
operator|.
name|TlsClientConnector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|scandium
operator|.
name|DTLSConnector
import|;
end_import

begin_comment
comment|/**  * The CoAP producer.  */
end_comment

begin_class
DECL|class|CoAPProducer
specifier|public
class|class
name|CoAPProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|CoAPEndpoint
name|endpoint
decl_stmt|;
DECL|field|client
specifier|private
name|CoapClient
name|client
decl_stmt|;
DECL|method|CoAPProducer (CoAPEndpoint endpoint)
specifier|public
name|CoAPProducer
parameter_list|(
name|CoAPEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|CoapClient
name|client
init|=
name|getClient
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|String
name|ct
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ct
operator|==
literal|null
condition|)
block|{
comment|//?default?
name|ct
operator|=
literal|"application/octet-stream"
expr_stmt|;
block|}
name|String
name|method
init|=
name|CoAPHelper
operator|.
name|getDefaultMethod
argument_list|(
name|exchange
argument_list|,
name|client
argument_list|)
decl_stmt|;
name|int
name|mediaType
init|=
name|MediaTypeRegistry
operator|.
name|parse
argument_list|(
name|ct
argument_list|)
decl_stmt|;
name|CoapResponse
name|response
init|=
literal|null
decl_stmt|;
name|boolean
name|pingResponse
init|=
literal|false
decl_stmt|;
switch|switch
condition|(
name|method
condition|)
block|{
case|case
name|CoAPConstants
operator|.
name|METHOD_GET
case|:
name|response
operator|=
name|client
operator|.
name|get
argument_list|()
expr_stmt|;
break|break;
case|case
name|CoAPConstants
operator|.
name|METHOD_DELETE
case|:
name|response
operator|=
name|client
operator|.
name|delete
argument_list|()
expr_stmt|;
break|break;
case|case
name|CoAPConstants
operator|.
name|METHOD_POST
case|:
name|byte
index|[]
name|bodyPost
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|response
operator|=
name|client
operator|.
name|post
argument_list|(
name|bodyPost
argument_list|,
name|mediaType
argument_list|)
expr_stmt|;
break|break;
case|case
name|CoAPConstants
operator|.
name|METHOD_PUT
case|:
name|byte
index|[]
name|bodyPut
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|response
operator|=
name|client
operator|.
name|put
argument_list|(
name|bodyPut
argument_list|,
name|mediaType
argument_list|)
expr_stmt|;
break|break;
case|case
name|CoAPConstants
operator|.
name|METHOD_PING
case|:
name|pingResponse
operator|=
name|client
operator|.
name|ping
argument_list|()
expr_stmt|;
break|break;
default|default:
break|break;
block|}
if|if
condition|(
name|response
operator|!=
literal|null
condition|)
block|{
name|Message
name|resp
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|String
name|mt
init|=
name|MediaTypeRegistry
operator|.
name|toString
argument_list|(
name|response
operator|.
name|getOptions
argument_list|()
operator|.
name|getContentFormat
argument_list|()
argument_list|)
decl_stmt|;
name|resp
operator|.
name|setHeader
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|mt
argument_list|)
expr_stmt|;
name|resp
operator|.
name|setHeader
argument_list|(
name|CoAPConstants
operator|.
name|COAP_RESPONSE_CODE
argument_list|,
name|response
operator|.
name|getCode
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|resp
operator|.
name|setBody
argument_list|(
name|response
operator|.
name|getPayload
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|method
operator|.
name|equalsIgnoreCase
argument_list|(
name|CoAPConstants
operator|.
name|METHOD_PING
argument_list|)
condition|)
block|{
name|Message
name|resp
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|resp
operator|.
name|setBody
argument_list|(
name|pingResponse
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getClient (Exchange exchange)
specifier|private
specifier|synchronized
name|CoapClient
name|getClient
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
throws|,
name|GeneralSecurityException
block|{
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|URI
name|uri
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CoAPConstants
operator|.
name|COAP_URI
argument_list|,
name|URI
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|==
literal|null
condition|)
block|{
name|uri
operator|=
name|endpoint
operator|.
name|getUri
argument_list|()
expr_stmt|;
block|}
name|client
operator|=
operator|new
name|CoapClient
argument_list|(
name|uri
argument_list|)
expr_stmt|;
comment|// Configure TLS and / or TCP
if|if
condition|(
name|CoAPEndpoint
operator|.
name|enableDTLS
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|DTLSConnector
name|connector
init|=
name|endpoint
operator|.
name|createDTLSConnector
argument_list|(
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|CoapEndpoint
operator|.
name|Builder
name|coapBuilder
init|=
operator|new
name|CoapEndpoint
operator|.
name|Builder
argument_list|()
decl_stmt|;
name|coapBuilder
operator|.
name|setConnector
argument_list|(
name|connector
argument_list|)
expr_stmt|;
name|client
operator|.
name|setEndpoint
argument_list|(
name|coapBuilder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|CoAPEndpoint
operator|.
name|enableTCP
argument_list|(
name|endpoint
operator|.
name|getUri
argument_list|()
argument_list|)
condition|)
block|{
name|NetworkConfig
name|config
init|=
name|NetworkConfig
operator|.
name|createStandardWithoutFile
argument_list|()
decl_stmt|;
name|int
name|tcpThreads
init|=
name|config
operator|.
name|getInt
argument_list|(
name|NetworkConfig
operator|.
name|Keys
operator|.
name|TCP_WORKER_THREADS
argument_list|)
decl_stmt|;
name|int
name|tcpConnectTimeout
init|=
name|config
operator|.
name|getInt
argument_list|(
name|NetworkConfig
operator|.
name|Keys
operator|.
name|TCP_CONNECT_TIMEOUT
argument_list|)
decl_stmt|;
name|int
name|tcpIdleTimeout
init|=
name|config
operator|.
name|getInt
argument_list|(
name|NetworkConfig
operator|.
name|Keys
operator|.
name|TCP_CONNECTION_IDLE_TIMEOUT
argument_list|)
decl_stmt|;
name|TcpClientConnector
name|tcpConnector
init|=
literal|null
decl_stmt|;
comment|// TLS + TCP
if|if
condition|(
name|endpoint
operator|.
name|getUri
argument_list|()
operator|.
name|getScheme
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"coaps"
argument_list|)
condition|)
block|{
name|SSLContext
name|sslContext
init|=
name|endpoint
operator|.
name|getSslContextParameters
argument_list|()
operator|.
name|createSSLContext
argument_list|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|tcpConnector
operator|=
operator|new
name|TlsClientConnector
argument_list|(
name|sslContext
argument_list|,
name|tcpThreads
argument_list|,
name|tcpConnectTimeout
argument_list|,
name|tcpIdleTimeout
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|tcpConnector
operator|=
operator|new
name|TcpClientConnector
argument_list|(
name|tcpThreads
argument_list|,
name|tcpConnectTimeout
argument_list|,
name|tcpIdleTimeout
argument_list|)
expr_stmt|;
block|}
name|CoapEndpoint
operator|.
name|Builder
name|tcpBuilder
init|=
operator|new
name|CoapEndpoint
operator|.
name|Builder
argument_list|()
decl_stmt|;
name|tcpBuilder
operator|.
name|setConnector
argument_list|(
name|tcpConnector
argument_list|)
expr_stmt|;
name|client
operator|.
name|setEndpoint
argument_list|(
name|tcpBuilder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|client
return|;
block|}
block|}
end_class

end_unit

