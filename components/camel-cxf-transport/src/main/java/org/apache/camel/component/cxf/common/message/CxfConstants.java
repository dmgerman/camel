begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.common.message
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
name|common
operator|.
name|message
package|;
end_package

begin_comment
comment|/**  * Constants used in this module  *  * @version   */
end_comment

begin_class
DECL|class|CxfConstants
specifier|public
specifier|final
class|class
name|CxfConstants
block|{
DECL|field|METHOD
specifier|public
specifier|static
specifier|final
name|String
name|METHOD
init|=
literal|"method"
decl_stmt|;
DECL|field|SERVICE_CLASS
specifier|public
specifier|static
specifier|final
name|String
name|SERVICE_CLASS
init|=
literal|"serviceClass"
decl_stmt|;
comment|// CamelCXFDataFormat is used as exchange property key
DECL|field|DATA_FORMAT_PROPERTY
specifier|public
specifier|static
specifier|final
name|String
name|DATA_FORMAT_PROPERTY
init|=
literal|"CamelCXFDataFormat"
decl_stmt|;
DECL|field|SET_DEFAULT_BUS
specifier|public
specifier|static
specifier|final
name|String
name|SET_DEFAULT_BUS
init|=
literal|"setDefaultBus"
decl_stmt|;
DECL|field|WSDL_URL
specifier|public
specifier|static
specifier|final
name|String
name|WSDL_URL
init|=
literal|"wsdlURL"
decl_stmt|;
DECL|field|ADDRESS
specifier|public
specifier|static
specifier|final
name|String
name|ADDRESS
init|=
literal|"address"
decl_stmt|;
DECL|field|SERVICE_NAME
specifier|public
specifier|static
specifier|final
name|String
name|SERVICE_NAME
init|=
literal|"serviceName"
decl_stmt|;
DECL|field|PORT_NAME
specifier|public
specifier|static
specifier|final
name|String
name|PORT_NAME
init|=
literal|"portName"
decl_stmt|;
DECL|field|SERVICE_LOCALNAME
specifier|public
specifier|static
specifier|final
name|String
name|SERVICE_LOCALNAME
init|=
literal|"serviceLocalName"
decl_stmt|;
DECL|field|SERVICE_NAMESPACE
specifier|public
specifier|static
specifier|final
name|String
name|SERVICE_NAMESPACE
init|=
literal|"serviceNamespace"
decl_stmt|;
DECL|field|PORT_LOCALNAME
specifier|public
specifier|static
specifier|final
name|String
name|PORT_LOCALNAME
init|=
literal|"endpointLocalName"
decl_stmt|;
DECL|field|PORT_NAMESPACE
specifier|public
specifier|static
specifier|final
name|String
name|PORT_NAMESPACE
init|=
literal|"endpointNamespace"
decl_stmt|;
DECL|field|PROTOCOL_NAME_RES
specifier|public
specifier|static
specifier|final
name|String
name|PROTOCOL_NAME_RES
init|=
literal|"res"
decl_stmt|;
DECL|field|OPERATION_NAME
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_NAME
init|=
literal|"operationName"
decl_stmt|;
DECL|field|OPERATION_NAMESPACE
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_NAMESPACE
init|=
literal|"operationNamespace"
decl_stmt|;
DECL|field|SPRING_CONTEXT_ENDPOINT
specifier|public
specifier|static
specifier|final
name|String
name|SPRING_CONTEXT_ENDPOINT
init|=
literal|"bean:"
decl_stmt|;
DECL|field|JAXWS_CONTEXT
specifier|public
specifier|static
specifier|final
name|String
name|JAXWS_CONTEXT
init|=
literal|"jaxwsContext"
decl_stmt|;
DECL|field|DISPATCH_NAMESPACE
specifier|public
specifier|static
specifier|final
name|String
name|DISPATCH_NAMESPACE
init|=
literal|"http://camel.apache.org/cxf/jaxws/dispatch"
decl_stmt|;
DECL|field|DISPATCH_DEFAULT_OPERATION_NAMESPACE
specifier|public
specifier|static
specifier|final
name|String
name|DISPATCH_DEFAULT_OPERATION_NAMESPACE
init|=
literal|"Invoke"
decl_stmt|;
DECL|field|CAMEL_CXF_MESSAGE
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_CXF_MESSAGE
init|=
literal|"CamelCxfMessage"
decl_stmt|;
DECL|field|CAMEL_CXF_RS_USING_HTTP_API
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_CXF_RS_USING_HTTP_API
init|=
literal|"CamelCxfRsUsingHttpAPI"
decl_stmt|;
DECL|field|CAMEL_CXF_RS_VAR_VALUES
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_CXF_RS_VAR_VALUES
init|=
literal|"CamelCxfRsVarValues"
decl_stmt|;
DECL|field|CAMEL_CXF_RS_RESPONSE_CLASS
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_CXF_RS_RESPONSE_CLASS
init|=
literal|"CamelCxfRsResponseClass"
decl_stmt|;
DECL|field|CAMEL_CXF_RS_RESPONSE_GENERIC_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_CXF_RS_RESPONSE_GENERIC_TYPE
init|=
literal|"CamelCxfRsResponseGenericType"
decl_stmt|;
DECL|field|CAMEL_CXF_RS_QUERY_MAP
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_CXF_RS_QUERY_MAP
init|=
literal|"CamelCxfRsQueryMap"
decl_stmt|;
DECL|field|CAMEL_CXF_RS_EXTRACT_ENTITY
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_CXF_RS_EXTRACT_ENTITY
init|=
literal|"CamelCxfRsExtractEntity"
decl_stmt|;
DECL|field|CAMEL_CXF_RS_OPERATION_RESOURCE_INFO_STACK
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_CXF_RS_OPERATION_RESOURCE_INFO_STACK
init|=
literal|"CamelCxfRsOperationResourceInfoStack"
decl_stmt|;
DECL|field|CAMEL_CXF_ATTACHMENTS
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_CXF_ATTACHMENTS
init|=
literal|"CamelAttachments"
decl_stmt|;
DECL|field|CAMEL_CXF_RS_THROW_EXCEPTION_ON_FAILURE
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_CXF_RS_THROW_EXCEPTION_ON_FAILURE
init|=
literal|"CamelCxfRsThrowExceptionOnFailure"
decl_stmt|;
DECL|method|CxfConstants ()
specifier|private
name|CxfConstants
parameter_list|()
block|{
comment|// Utility class
block|}
block|}
end_class

end_unit

