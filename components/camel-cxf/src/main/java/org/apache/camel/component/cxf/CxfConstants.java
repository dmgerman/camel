begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
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
package|;
end_package

begin_comment
comment|/**  * Constants used in this module  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|CxfConstants
specifier|public
interface|interface
name|CxfConstants
block|{
DECL|field|METHOD
name|String
name|METHOD
init|=
literal|"method"
decl_stmt|;
DECL|field|SERVICE_CLASS
name|String
name|SERVICE_CLASS
init|=
literal|"serviceClass"
decl_stmt|;
comment|// CamelCXFDataFormat is used as exchange property key
DECL|field|DATA_FORMAT_PROPERTY
name|String
name|DATA_FORMAT_PROPERTY
init|=
literal|"CamelCXFDataFormat"
decl_stmt|;
DECL|field|SET_DEFAULT_BUS
name|String
name|SET_DEFAULT_BUS
init|=
literal|"setDefaultBus"
decl_stmt|;
DECL|field|WSDL_URL
name|String
name|WSDL_URL
init|=
literal|"wsdlURL"
decl_stmt|;
DECL|field|ADDRESS
name|String
name|ADDRESS
init|=
literal|"address"
decl_stmt|;
DECL|field|SERVICE_NAME
name|String
name|SERVICE_NAME
init|=
literal|"serviceName"
decl_stmt|;
DECL|field|PORT_NAME
name|String
name|PORT_NAME
init|=
literal|"portName"
decl_stmt|;
DECL|field|SERVICE_LOCALNAME
name|String
name|SERVICE_LOCALNAME
init|=
literal|"serviceLocalName"
decl_stmt|;
DECL|field|SERVICE_NAMESPACE
name|String
name|SERVICE_NAMESPACE
init|=
literal|"serviceNamespace"
decl_stmt|;
DECL|field|PORT_LOCALNAME
name|String
name|PORT_LOCALNAME
init|=
literal|"endpointLocalName"
decl_stmt|;
DECL|field|PORT_NAMESPACE
name|String
name|PORT_NAMESPACE
init|=
literal|"endpointNamespace"
decl_stmt|;
DECL|field|PROTOCOL_NAME_RES
name|String
name|PROTOCOL_NAME_RES
init|=
literal|"res"
decl_stmt|;
DECL|field|OPERATION_NAME
name|String
name|OPERATION_NAME
init|=
literal|"operationName"
decl_stmt|;
DECL|field|OPERATION_NAMESPACE
name|String
name|OPERATION_NAMESPACE
init|=
literal|"operationNameSpace"
decl_stmt|;
DECL|field|SPRING_CONTEXT_ENDPOINT
name|String
name|SPRING_CONTEXT_ENDPOINT
init|=
literal|"bean:"
decl_stmt|;
DECL|field|CAMEL_TRANSPORT_PREFIX
name|String
name|CAMEL_TRANSPORT_PREFIX
init|=
literal|"camel:"
decl_stmt|;
DECL|field|CXF_EXCHANGE
name|String
name|CXF_EXCHANGE
init|=
literal|"org.apache.cxf.message.exchange"
decl_stmt|;
DECL|field|CAMEL_EXCHANGE
name|String
name|CAMEL_EXCHANGE
init|=
literal|"org.apache.camel.exchange"
decl_stmt|;
DECL|field|CAMEL_CXF_RS_USING_HTTP_API
name|String
name|CAMEL_CXF_RS_USING_HTTP_API
init|=
literal|"CamelCxfRsUsingHttpAPI"
decl_stmt|;
DECL|field|CAMEL_CXF_RS_VAR_VALUES
name|String
name|CAMEL_CXF_RS_VAR_VALUES
init|=
literal|"CamelCxfRsVarValues"
decl_stmt|;
DECL|field|CAMEL_CXF_RS_RESPONSE_CLASS
name|String
name|CAMEL_CXF_RS_RESPONSE_CLASS
init|=
literal|"CamelCxfRsResponseClass"
decl_stmt|;
block|}
end_interface

end_unit

