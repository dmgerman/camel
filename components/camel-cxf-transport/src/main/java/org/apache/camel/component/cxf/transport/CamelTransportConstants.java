begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.transport
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|transport
package|;
end_package

begin_class
DECL|class|CamelTransportConstants
specifier|public
specifier|final
class|class
name|CamelTransportConstants
block|{
DECL|field|TEXT_MESSAGE_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|TEXT_MESSAGE_TYPE
init|=
literal|"text"
decl_stmt|;
DECL|field|BINARY_MESSAGE_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|BINARY_MESSAGE_TYPE
init|=
literal|"binary"
decl_stmt|;
DECL|field|CAMEL_TARGET_ENDPOINT_URI
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_TARGET_ENDPOINT_URI
init|=
literal|"org.apache.cxf.camel.target.endpoint.uri"
decl_stmt|;
DECL|field|CAMEL_SERVER_REQUEST_HEADERS
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_SERVER_REQUEST_HEADERS
init|=
literal|"org.apache.cxf.camel.server.request.headers"
decl_stmt|;
DECL|field|CAMEL_SERVER_RESPONSE_HEADERS
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_SERVER_RESPONSE_HEADERS
init|=
literal|"org.apache.cxf.camel.server.response.headers"
decl_stmt|;
DECL|field|CAMEL_REQUEST_MESSAGE
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_REQUEST_MESSAGE
init|=
literal|"org.apache.cxf.camel.request.message"
decl_stmt|;
DECL|field|CAMEL_RESPONSE_MESSAGE
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_RESPONSE_MESSAGE
init|=
literal|"org.apache.cxf.camel.reponse.message"
decl_stmt|;
DECL|field|CAMEL_CLIENT_REQUEST_HEADERS
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_CLIENT_REQUEST_HEADERS
init|=
literal|"org.apache.cxf.camel.template.request.headers"
decl_stmt|;
DECL|field|CAMEL_CLIENT_RESPONSE_HEADERS
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_CLIENT_RESPONSE_HEADERS
init|=
literal|"org.apache.cxf.camel.template.response.headers"
decl_stmt|;
DECL|field|CAMEL_CLIENT_RECEIVE_TIMEOUT
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_CLIENT_RECEIVE_TIMEOUT
init|=
literal|"org.apache.cxf.camel.template.timeout"
decl_stmt|;
DECL|field|CAMEL_SERVER_CONFIGURATION_URI
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_SERVER_CONFIGURATION_URI
init|=
literal|"http://cxf.apache.org/configuration/transport/camel-server"
decl_stmt|;
DECL|field|CAMEL_CLIENT_CONFIGURATION_URI
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_CLIENT_CONFIGURATION_URI
init|=
literal|"http://cxf.apache.org/configuration/transport/camel-template"
decl_stmt|;
DECL|field|ENDPOINT_CONFIGURATION_URI
specifier|public
specifier|static
specifier|final
name|String
name|ENDPOINT_CONFIGURATION_URI
init|=
literal|"http://cxf.apache.org/jaxws/endpoint-config"
decl_stmt|;
DECL|field|SERVICE_CONFIGURATION_URI
specifier|public
specifier|static
specifier|final
name|String
name|SERVICE_CONFIGURATION_URI
init|=
literal|"http://cxf.apache.org/jaxws/service-config"
decl_stmt|;
DECL|field|PORT_CONFIGURATION_URI
specifier|public
specifier|static
specifier|final
name|String
name|PORT_CONFIGURATION_URI
init|=
literal|"http://cxf.apache.org/jaxws/port-config"
decl_stmt|;
DECL|field|CAMEL_CLIENT_CONFIG_ID
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_CLIENT_CONFIG_ID
init|=
literal|"camel-template"
decl_stmt|;
DECL|field|CAMEL_SERVER_CONFIG_ID
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_SERVER_CONFIG_ID
init|=
literal|"camel-server"
decl_stmt|;
DECL|field|CAMEL_REBASED_REPLY_TO
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_REBASED_REPLY_TO
init|=
literal|"org.apache.cxf.camel.server.replyto"
decl_stmt|;
DECL|field|CAMEL_CORRELATION_ID
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_CORRELATION_ID
init|=
literal|"org.apache.cxf.camel.correlationId"
decl_stmt|;
DECL|field|CXF_EXCHANGE
specifier|public
specifier|static
specifier|final
name|String
name|CXF_EXCHANGE
init|=
literal|"org.apache.cxf.message.exchange"
decl_stmt|;
DECL|field|CAMEL_TRANSPORT_PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_TRANSPORT_PREFIX
init|=
literal|"camel:"
decl_stmt|;
DECL|field|CAMEL_EXCHANGE
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_EXCHANGE
init|=
literal|"org.apache.camel.exchange"
decl_stmt|;
DECL|method|CamelTransportConstants ()
specifier|private
name|CamelTransportConstants
parameter_list|()
block|{
comment|// Utility class
block|}
block|}
end_class

end_unit

