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

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|BindingProvider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Endpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|Processor
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
name|converter
operator|.
name|jaxp
operator|.
name|XmlConverter
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
name|pizza
operator|.
name|Pizza
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
name|pizza
operator|.
name|PizzaService
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
name|pizza
operator|.
name|types
operator|.
name|CallerIDHeaderType
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
name|pizza
operator|.
name|types
operator|.
name|OrderPizzaResponseType
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
name|pizza
operator|.
name|types
operator|.
name|OrderPizzaType
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
name|pizza
operator|.
name|types
operator|.
name|ToppingsListType
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
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|soap
operator|.
name|SoapHeader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_class
DECL|class|CxfPayLoadSoapHeaderTest
specifier|public
class|class
name|CxfPayLoadSoapHeaderTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|port1
specifier|static
name|int
name|port1
init|=
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
decl_stmt|;
DECL|field|port2
specifier|static
name|int
name|port2
init|=
name|CXFTestSupport
operator|.
name|getPort2
argument_list|()
decl_stmt|;
DECL|field|serviceName
specifier|private
specifier|final
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://camel.apache.org/pizza"
argument_list|,
literal|"PizzaService"
argument_list|)
decl_stmt|;
DECL|method|getRouterEndpointURI ()
specifier|protected
name|String
name|getRouterEndpointURI
parameter_list|()
block|{
return|return
literal|"cxf:http://localhost:"
operator|+
name|port1
operator|+
literal|"/"
operator|+
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"/pizza_service/services/PizzaService?wsdlURL=classpath:pizza_service.wsdl&dataFormat=PAYLOAD"
return|;
block|}
DECL|method|getServiceEndpointURI ()
specifier|protected
name|String
name|getServiceEndpointURI
parameter_list|()
block|{
return|return
literal|"cxf:http://localhost:"
operator|+
name|port2
operator|+
literal|"/"
operator|+
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"/new_pizza_service/services/PizzaService?wsdlURL=classpath:pizza_service.wsdl&dataFormat=PAYLOAD"
return|;
block|}
annotation|@
name|Override
DECL|method|isCreateCamelContextPerClass ()
specifier|public
name|boolean
name|isCreateCamelContextPerClass
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
comment|// START SNIPPET: payload
name|from
argument_list|(
name|getRouterEndpointURI
argument_list|()
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|CxfPayload
argument_list|<
name|SoapHeader
argument_list|>
name|payload
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|CxfPayload
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Source
argument_list|>
name|elements
init|=
name|payload
operator|.
name|getBodySources
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"We should get the elements here"
argument_list|,
name|elements
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong elements size"
argument_list|,
literal|1
argument_list|,
name|elements
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Element
name|el
init|=
operator|new
name|XmlConverter
argument_list|()
operator|.
name|toDOMElement
argument_list|(
name|elements
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|elements
operator|.
name|set
argument_list|(
literal|0
argument_list|,
operator|new
name|DOMSource
argument_list|(
name|el
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong namespace URI"
argument_list|,
literal|"http://camel.apache.org/pizza/types"
argument_list|,
name|el
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|SoapHeader
argument_list|>
name|headers
init|=
name|payload
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"We should get the headers here"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong headers size"
argument_list|,
name|headers
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong namespace URI"
argument_list|,
operator|(
call|(
name|Element
call|)
argument_list|(
name|headers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getObject
argument_list|()
argument_list|)
operator|)
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
literal|"http://camel.apache.org/pizza/types"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
name|getServiceEndpointURI
argument_list|()
argument_list|)
expr_stmt|;
comment|// END SNIPPET: payload
block|}
block|}
return|;
block|}
DECL|method|start (String n)
specifier|protected
name|void
name|start
parameter_list|(
name|String
name|n
parameter_list|)
block|{
name|Object
name|implementor
init|=
operator|new
name|PizzaImpl
argument_list|()
decl_stmt|;
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|port2
operator|+
literal|"/"
operator|+
name|n
operator|+
literal|"/new_pizza_service/services/PizzaService"
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
DECL|method|startService ()
specifier|public
name|void
name|startService
parameter_list|()
block|{
name|start
argument_list|(
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPizzaService ()
specifier|public
name|void
name|testPizzaService
parameter_list|()
block|{
name|Pizza
name|port
init|=
name|getPort
argument_list|()
decl_stmt|;
name|OrderPizzaType
name|req
init|=
operator|new
name|OrderPizzaType
argument_list|()
decl_stmt|;
name|ToppingsListType
name|t
init|=
operator|new
name|ToppingsListType
argument_list|()
decl_stmt|;
name|t
operator|.
name|getTopping
argument_list|()
operator|.
name|add
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|req
operator|.
name|setToppings
argument_list|(
name|t
argument_list|)
expr_stmt|;
name|CallerIDHeaderType
name|header
init|=
operator|new
name|CallerIDHeaderType
argument_list|()
decl_stmt|;
name|header
operator|.
name|setName
argument_list|(
literal|"Willem"
argument_list|)
expr_stmt|;
name|header
operator|.
name|setPhoneNumber
argument_list|(
literal|"108"
argument_list|)
expr_stmt|;
name|OrderPizzaResponseType
name|res
init|=
name|port
operator|.
name|orderPizza
argument_list|(
name|req
argument_list|,
name|header
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|208
argument_list|,
name|res
operator|.
name|getMinutesUntilReady
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|private
name|Pizza
name|getPort
parameter_list|()
block|{
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/pizza_service.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"WSDL is null"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|PizzaService
name|service
init|=
operator|new
name|PizzaService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Service is null "
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|Pizza
name|pizza
init|=
name|service
operator|.
name|getPizzaPort
argument_list|()
decl_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|pizza
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
literal|"http://localhost:"
operator|+
name|port1
operator|+
literal|"/"
operator|+
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"/pizza_service/services/PizzaService"
argument_list|)
expr_stmt|;
return|return
name|pizza
return|;
block|}
block|}
end_class

end_unit

