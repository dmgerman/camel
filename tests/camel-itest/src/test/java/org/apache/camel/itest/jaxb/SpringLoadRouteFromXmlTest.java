begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.jaxb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|jaxb
package|;
end_package

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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|Constants
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
name|RoutesDefinition
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
name|CamelSpringTestSupport
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|ClassPathResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|Resource
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|SpringLoadRouteFromXmlTest
specifier|public
class|class
name|SpringLoadRouteFromXmlTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/itest/jaxb/SpringLoadRouteFromXmlTest.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testLoadRouteFromXml ()
specifier|public
name|void
name|testLoadRouteFromXml
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
literal|"Existing foo route should be there"
argument_list|,
name|context
operator|.
name|getRoute
argument_list|(
literal|"foo"
argument_list|)
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
comment|// test that existing route works
name|MockEndpoint
name|foo
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
decl_stmt|;
name|foo
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
literal|"direct:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|foo
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// load bar route from classpath using JAXB
name|JAXBContext
name|jaxb
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|Constants
operator|.
name|JAXB_CONTEXT_PACKAGES
argument_list|)
decl_stmt|;
name|Unmarshaller
name|unmarshaller
init|=
name|jaxb
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|Resource
name|rs
init|=
operator|new
name|ClassPathResource
argument_list|(
literal|"org/apache/camel/itest/jaxb/BarRoute.xml"
argument_list|)
decl_stmt|;
name|Object
name|value
init|=
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|rs
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
comment|// it should be a RoutesDefinition (we can have multiple routes in the same XML file)
name|RoutesDefinition
name|routes
init|=
operator|(
name|RoutesDefinition
operator|)
name|value
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should load routes from XML"
argument_list|,
name|routes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|routes
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// add the routes to existing CamelContext
name|context
operator|.
name|addRouteDefinitions
argument_list|(
name|routes
operator|.
name|getRoutes
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Loaded bar route should be there"
argument_list|,
name|context
operator|.
name|getRoute
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
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
comment|// test that loaded route works
name|MockEndpoint
name|bar
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
decl_stmt|;
name|bar
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
literal|"direct:bar"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|bar
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

