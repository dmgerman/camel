begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rest
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
name|impl
operator|.
name|JndiRegistry
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
name|ToDefinition
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
name|rest
operator|.
name|RestDefinition
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
DECL|class|FromRestGetEmbeddedRouteTest
specifier|public
class|class
name|FromRestGetEmbeddedRouteTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"dummy-test"
argument_list|,
operator|new
name|DummyRestConsumerFactory
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
DECL|method|getExpectedNumberOfRoutes ()
specifier|protected
name|int
name|getExpectedNumberOfRoutes
parameter_list|()
block|{
return|return
literal|3
return|;
block|}
annotation|@
name|Test
DECL|method|testFromRestModel ()
specifier|public
name|void
name|testFromRestModel
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|getExpectedNumberOfRoutes
argument_list|()
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
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|context
operator|.
name|getRestDefinitions
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|RestDefinition
name|rest
init|=
name|context
operator|.
name|getRestDefinitions
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|rest
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/say/hello"
argument_list|,
name|rest
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|rest
operator|.
name|getVerbs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ToDefinition
name|to
init|=
name|assertIsInstanceOf
argument_list|(
name|ToDefinition
operator|.
name|class
argument_list|,
name|rest
operator|.
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getRoute
argument_list|()
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"mock:hello"
argument_list|,
name|to
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|rest
operator|=
name|context
operator|.
name|getRestDefinitions
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|rest
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/say/bye"
argument_list|,
name|rest
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|rest
operator|.
name|getVerbs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application/json"
argument_list|,
name|rest
operator|.
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getConsumes
argument_list|()
argument_list|)
expr_stmt|;
name|to
operator|=
name|assertIsInstanceOf
argument_list|(
name|ToDefinition
operator|.
name|class
argument_list|,
name|rest
operator|.
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getRoute
argument_list|()
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mock:bye"
argument_list|,
name|to
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
comment|// the rest becomes routes and the input is a seda endpoint created by
comment|// the DummyRestConsumerFactory
name|getMockEndpoint
argument_list|(
literal|"mock:update"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:post-say-bye"
argument_list|,
literal|"I was here"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"seda:get-say-hello"
argument_list|,
literal|"Me"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|String
name|out2
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"seda:get-say-bye"
argument_list|,
literal|"Me"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out2
argument_list|)
expr_stmt|;
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|restConfiguration
argument_list|()
operator|.
name|host
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|rest
argument_list|(
literal|"/say/hello"
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|route
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:hello"
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Hello World"
argument_list|)
argument_list|)
expr_stmt|;
name|rest
argument_list|(
literal|"/say/bye"
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|consumes
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|route
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:bye"
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
argument_list|)
operator|.
name|endRest
argument_list|()
operator|.
name|post
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:update"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

