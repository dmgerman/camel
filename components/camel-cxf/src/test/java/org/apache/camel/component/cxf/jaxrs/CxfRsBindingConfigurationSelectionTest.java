begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs
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
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|Entity
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
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
name|BindToRegistry
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
name|builder
operator|.
name|RouteBuilder
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
name|component
operator|.
name|cxf
operator|.
name|CXFTestSupport
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Tests different binding configuration options of the CXFRS consumer.   */
end_comment

begin_class
DECL|class|CxfRsBindingConfigurationSelectionTest
specifier|public
class|class
name|CxfRsBindingConfigurationSelectionTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|RESOURCE_CLASS
specifier|private
specifier|static
specifier|final
name|String
name|RESOURCE_CLASS
init|=
literal|"resourceClasses=org.apache.camel.component.cxf.jaxrs.simplebinding.testbean.CustomerServiceResource"
decl_stmt|;
DECL|field|CXF_RS_ENDPOINT_URI_CUSTOM
specifier|private
specifier|static
specifier|final
name|String
name|CXF_RS_ENDPOINT_URI_CUSTOM
init|=
name|String
operator|.
name|format
argument_list|(
literal|"cxfrs://http://localhost:%s/CxfRsConsumerTest/rest?bindingStyle=Custom&"
argument_list|,
name|CXFTestSupport
operator|.
name|getPort2
argument_list|()
argument_list|)
operator|+
name|RESOURCE_CLASS
operator|+
literal|"&binding=#binding"
decl_stmt|;
DECL|field|CXF_RS_ENDPOINT_URI_SIMPLE
specifier|private
specifier|static
specifier|final
name|String
name|CXF_RS_ENDPOINT_URI_SIMPLE
init|=
name|String
operator|.
name|format
argument_list|(
literal|"cxfrs://http://localhost:%s/CxfRsConsumerTest/rest?bindingStyle=SimpleConsumer&"
argument_list|,
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
argument_list|)
operator|+
name|RESOURCE_CLASS
decl_stmt|;
DECL|field|CXF_RS_ENDPOINT_URI_DEFAULT
specifier|private
specifier|static
specifier|final
name|String
name|CXF_RS_ENDPOINT_URI_DEFAULT
init|=
name|String
operator|.
name|format
argument_list|(
literal|"cxfrs://http://localhost:%s/CxfRsConsumerTest/rest?bindingStyle=Default&"
argument_list|,
name|CXFTestSupport
operator|.
name|getPort3
argument_list|()
argument_list|)
operator|+
name|RESOURCE_CLASS
decl_stmt|;
DECL|field|CXF_RS_ENDPOINT_URI_NONE
specifier|private
specifier|static
specifier|final
name|String
name|CXF_RS_ENDPOINT_URI_NONE
init|=
name|String
operator|.
name|format
argument_list|(
literal|"cxfrs://http://localhost:%s/CxfRsConsumerTest/rest?"
argument_list|,
name|CXFTestSupport
operator|.
name|getPort4
argument_list|()
argument_list|)
operator|+
name|RESOURCE_CLASS
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"binding"
argument_list|)
DECL|field|dummyCxfRsBindingImplementation
specifier|private
name|DummyCxfRsBindingImplementation
name|dummyCxfRsBindingImplementation
init|=
operator|new
name|DummyCxfRsBindingImplementation
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testCxfRsBindingConfiguration ()
specifier|public
name|void
name|testCxfRsBindingConfiguration
parameter_list|()
block|{
comment|// check binding styles
name|assertEquals
argument_list|(
name|BindingStyle
operator|.
name|Custom
argument_list|,
name|endpointForRouteId
argument_list|(
literal|"custom"
argument_list|)
operator|.
name|getBindingStyle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|BindingStyle
operator|.
name|SimpleConsumer
argument_list|,
name|endpointForRouteId
argument_list|(
literal|"simple"
argument_list|)
operator|.
name|getBindingStyle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|BindingStyle
operator|.
name|Default
argument_list|,
name|endpointForRouteId
argument_list|(
literal|"default"
argument_list|)
operator|.
name|getBindingStyle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|BindingStyle
operator|.
name|Default
argument_list|,
name|endpointForRouteId
argument_list|(
literal|"none"
argument_list|)
operator|.
name|getBindingStyle
argument_list|()
argument_list|)
expr_stmt|;
comment|// check binding implementations
name|assertEquals
argument_list|(
name|DummyCxfRsBindingImplementation
operator|.
name|class
argument_list|,
name|endpointForRouteId
argument_list|(
literal|"custom"
argument_list|)
operator|.
name|getBinding
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SimpleCxfRsBinding
operator|.
name|class
argument_list|,
name|endpointForRouteId
argument_list|(
literal|"simple"
argument_list|)
operator|.
name|getBinding
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DefaultCxfRsBinding
operator|.
name|class
argument_list|,
name|endpointForRouteId
argument_list|(
literal|"default"
argument_list|)
operator|.
name|getBinding
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DefaultCxfRsBinding
operator|.
name|class
argument_list|,
name|endpointForRouteId
argument_list|(
literal|"default"
argument_list|)
operator|.
name|getBinding
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|endpointForRouteId (String routeId)
specifier|private
name|CxfRsEndpoint
name|endpointForRouteId
parameter_list|(
name|String
name|routeId
parameter_list|)
block|{
return|return
operator|(
name|CxfRsEndpoint
operator|)
name|context
operator|.
name|getRoute
argument_list|(
name|routeId
argument_list|)
operator|.
name|getConsumer
argument_list|()
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
name|CXF_RS_ENDPOINT_URI_CUSTOM
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"custom"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|CXF_RS_ENDPOINT_URI_SIMPLE
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"simple"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|CXF_RS_ENDPOINT_URI_DEFAULT
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"default"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|CXF_RS_ENDPOINT_URI_NONE
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"none"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|DummyCxfRsBindingImplementation
specifier|private
specifier|final
class|class
name|DummyCxfRsBindingImplementation
implements|implements
name|CxfRsBinding
block|{
annotation|@
name|Override
DECL|method|populateExchangeFromCxfRsRequest (org.apache.cxf.message.Exchange cxfExchange, Exchange camelExchange, Method method, Object[] paramArray)
specifier|public
name|void
name|populateExchangeFromCxfRsRequest
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Exchange
name|cxfExchange
parameter_list|,
name|Exchange
name|camelExchange
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|paramArray
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|populateCxfRsResponseFromExchange (Exchange camelExchange, org.apache.cxf.message.Exchange cxfExchange)
specifier|public
name|Object
name|populateCxfRsResponseFromExchange
parameter_list|(
name|Exchange
name|camelExchange
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Exchange
name|cxfExchange
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|bindResponseToCamelBody (Object response, Exchange camelExchange)
specifier|public
name|Object
name|bindResponseToCamelBody
parameter_list|(
name|Object
name|response
parameter_list|,
name|Exchange
name|camelExchange
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|bindResponseHeadersToCamelHeaders (Object response, Exchange camelExchange)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|bindResponseHeadersToCamelHeaders
parameter_list|(
name|Object
name|response
parameter_list|,
name|Exchange
name|camelExchange
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|bindCamelMessageToRequestEntity (Object body, Message camelMessage, Exchange camelExchange)
specifier|public
name|Entity
argument_list|<
name|Object
argument_list|>
name|bindCamelMessageToRequestEntity
parameter_list|(
name|Object
name|body
parameter_list|,
name|Message
name|camelMessage
parameter_list|,
name|Exchange
name|camelExchange
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|bindCamelMessageBodyToRequestBody (Message camelMessage, Exchange camelExchange)
specifier|public
name|Object
name|bindCamelMessageBodyToRequestBody
parameter_list|(
name|Message
name|camelMessage
parameter_list|,
name|Exchange
name|camelExchange
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|bindCamelHeadersToRequestHeaders (Map<String, Object> camelHeaders, Exchange camelExchange)
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|bindCamelHeadersToRequestHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|camelHeaders
parameter_list|,
name|Exchange
name|camelExchange
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

