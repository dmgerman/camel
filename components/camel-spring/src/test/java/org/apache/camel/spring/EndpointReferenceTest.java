begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
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
name|Endpoint
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
name|NoSuchEndpointException
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
name|impl
operator|.
name|DefaultRouteContext
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
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|RouteContext
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
name|spring
operator|.
name|example
operator|.
name|DummyBean
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
name|AbstractXmlApplicationContext
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

begin_class
DECL|class|EndpointReferenceTest
specifier|public
class|class
name|EndpointReferenceTest
extends|extends
name|SpringTestSupport
block|{
DECL|field|body
specifier|protected
specifier|static
name|Object
name|body
init|=
literal|"<hello>world!</hello>"
decl_stmt|;
annotation|@
name|Test
DECL|method|testContextToString ()
specifier|public
name|void
name|testContextToString
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
name|context
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEndpointConfiguration ()
specifier|public
name|void
name|testEndpointConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|getMandatoryBean
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
literal|"endpoint1"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"endpoint URI"
argument_list|,
literal|"direct://start"
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|DummyBean
name|dummyBean
init|=
name|getMandatoryBean
argument_list|(
name|DummyBean
operator|.
name|class
argument_list|,
literal|"mybean"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The bean should have an endpoint injected"
argument_list|,
name|dummyBean
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"endpoint URI"
argument_list|,
literal|"direct://start"
argument_list|,
name|dummyBean
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Found dummy bean: "
operator|+
name|dummyBean
argument_list|)
expr_stmt|;
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
comment|// now lets send a message
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|createCamelContext ()
specifier|protected
name|SpringCamelContext
name|createCamelContext
parameter_list|()
block|{
return|return
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"camel"
argument_list|,
name|SpringCamelContext
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testEndpointConfigurationAfterEnsuringThatTheStatementRouteBuilderWasCreated ()
specifier|public
name|void
name|testEndpointConfigurationAfterEnsuringThatTheStatementRouteBuilderWasCreated
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|names
init|=
name|applicationContext
operator|.
name|getBeanDefinitionNames
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Found bean name: "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
name|testEndpointConfiguration
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReferenceEndpointFromOtherCamelContext ()
specifier|public
name|void
name|testReferenceEndpointFromOtherCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"camel2"
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|RouteContext
name|routeContext
init|=
operator|new
name|DefaultRouteContext
argument_list|(
name|context
argument_list|,
operator|new
name|RouteDefinition
argument_list|(
literal|"temporary"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|routeContext
operator|.
name|resolveEndpoint
argument_list|(
literal|null
argument_list|,
literal|"endpoint1"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchEndpointException
name|exception
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Get a wrong exception message"
argument_list|,
name|exception
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"make sure the endpoint has the same camel context as the route does"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/endpointReference.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

