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
DECL|class|FromRestUriPrefixTest
specifier|public
class|class
name|FromRestUriPrefixTest
extends|extends
name|FromRestGetTest
block|{
annotation|@
name|Override
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
literal|1
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
literal|"/say/"
argument_list|,
name|rest
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
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
literal|"/hello"
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
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/bye"
argument_list|,
name|rest
operator|.
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/hi"
argument_list|,
name|rest
operator|.
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getUri
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
name|getTo
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"direct:hello"
argument_list|,
name|to
operator|.
name|getUri
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
literal|1
argument_list|)
operator|.
name|getTo
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"direct:bye"
argument_list|,
name|to
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
comment|// the rest becomes routes and the input is a seda endpoint created by the DummyRestConsumerFactory
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
literal|"seda:post-say-hi"
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
comment|// we have logic to cleanup those paths so there is only one / between the paths
name|rest
argument_list|(
literal|"/say/"
argument_list|)
operator|.
name|get
argument_list|(
literal|"/hello"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:hello"
argument_list|)
operator|.
name|get
argument_list|(
literal|"/bye"
argument_list|)
operator|.
name|consumes
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:bye"
argument_list|)
operator|.
name|post
argument_list|(
literal|"/hi"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:update"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:hello"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:bye"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

