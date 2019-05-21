begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Unmarshaller
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
name|ContextTestSupport
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
name|ExtendedCamelContext
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
name|model
operator|.
name|ProcessorDefinition
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
name|model
operator|.
name|RouteDefinition
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
DECL|class|CamelContextAddRouteDefinitionsFromXmlTest
specifier|public
class|class
name|CamelContextAddRouteDefinitionsFromXmlTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|jaxbContext
specifier|protected
name|JAXBContext
name|jaxbContext
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|jaxbContext
operator|=
name|context
operator|.
name|adapt
argument_list|(
name|ExtendedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getModelJAXBContextFactory
argument_list|()
operator|.
name|newJAXBContext
argument_list|()
expr_stmt|;
block|}
DECL|method|parseUri (String uri)
specifier|protected
name|Object
name|parseUri
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|JAXBException
block|{
name|Unmarshaller
name|unmarshaller
init|=
name|jaxbContext
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|URL
name|resource
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Cannot find resource on the classpath: "
operator|+
name|uri
argument_list|,
name|resource
argument_list|)
expr_stmt|;
name|Object
name|value
init|=
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|resource
argument_list|)
decl_stmt|;
return|return
name|value
return|;
block|}
DECL|method|loadRoute (String uri)
specifier|protected
name|RouteDefinition
name|loadRoute
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|route
init|=
name|parseUri
argument_list|(
name|uri
argument_list|)
decl_stmt|;
return|return
name|assertIsInstanceOf
argument_list|(
name|RouteDefinition
operator|.
name|class
argument_list|,
name|route
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testAddRouteDefinitionsFromXml ()
specifier|public
name|void
name|testAddRouteDefinitionsFromXml
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteDefinition
name|route
init|=
name|loadRoute
argument_list|(
literal|"route1.xml"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRouteDefinition
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Route should be started"
argument_list|,
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveRouteDefinitionsFromXml ()
specifier|public
name|void
name|testRemoveRouteDefinitionsFromXml
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteDefinition
name|route
init|=
name|loadRoute
argument_list|(
literal|"route1.xml"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRouteDefinition
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getRouteDefinitions
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Route should be started"
argument_list|,
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|removeRouteDefinition
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getRouteDefinitions
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAddRouteDefinitionsFromXml2 ()
specifier|public
name|void
name|testAddRouteDefinitionsFromXml2
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteDefinition
name|route
init|=
name|loadRoute
argument_list|(
literal|"route2.xml"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRouteDefinition
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Route should be stopped"
argument_list|,
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Route should be started"
argument_list|,
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAddRouteDefinitionsFromXmlIsPrepared ()
specifier|public
name|void
name|testAddRouteDefinitionsFromXmlIsPrepared
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteDefinition
name|route
init|=
name|loadRoute
argument_list|(
literal|"route1.xml"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRouteDefinition
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Route should be started"
argument_list|,
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
comment|// should be prepared, check parents has been set
name|assertNotNull
argument_list|(
literal|"Parent should be set on outputs"
argument_list|)
expr_stmt|;
name|route
operator|=
name|context
operator|.
name|getRouteDefinition
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
for|for
control|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|output
range|:
name|route
operator|.
name|getOutputs
argument_list|()
control|)
block|{
name|assertNotNull
argument_list|(
literal|"Parent should be set on output"
argument_list|,
name|output
operator|.
name|getParent
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|route
argument_list|,
name|output
operator|.
name|getParent
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testAddRouteDefinitionsFromXml3 ()
specifier|public
name|void
name|testAddRouteDefinitionsFromXml3
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteDefinition
name|route
init|=
name|loadRoute
argument_list|(
literal|"route3.xml"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRouteDefinition
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Route should be started"
argument_list|,
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|whenExchangeReceived
argument_list|(
literal|2
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
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
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:handled"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAddRouteDefinitionsAfterExceptionFromXml ()
specifier|public
name|void
name|testAddRouteDefinitionsAfterExceptionFromXml
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteDefinition
name|route
init|=
name|loadRoute
argument_list|(
literal|"route4_error.xml"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|context
operator|.
name|addRouteDefinition
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// catch this is error to simulate test case!!!!
block|}
comment|// load route with same id
name|route
operator|=
name|loadRoute
argument_list|(
literal|"route4_ok.xml"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRouteDefinition
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

