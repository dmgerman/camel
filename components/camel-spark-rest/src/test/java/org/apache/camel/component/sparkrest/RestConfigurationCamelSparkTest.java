begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sparkrest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sparkrest
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|RestConfigurationCamelSparkTest
specifier|public
class|class
name|RestConfigurationCamelSparkTest
extends|extends
name|BaseSparkTest
block|{
annotation|@
name|Test
DECL|method|testSparkHello ()
specifier|public
name|void
name|testSparkHello
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"http://localhost:"
operator|+
name|port
operator|+
literal|"/spark/hello"
argument_list|,
literal|null
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
block|}
annotation|@
name|Test
DECL|method|testSparkBye ()
specifier|public
name|void
name|testSparkBye
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"http://localhost:"
operator|+
name|port
operator|+
literal|"/spark/bye"
argument_list|,
literal|null
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
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSparkPost ()
specifier|public
name|void
name|testSparkPost
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:update"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"I did this"
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
literal|"http://localhost:"
operator|+
name|port
operator|+
literal|"/spark/bye"
argument_list|,
literal|"I did this"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
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
comment|// configure port on rest configuration which spark-rest will pickup and use
name|restConfiguration
argument_list|()
operator|.
name|component
argument_list|(
literal|"spark-rest"
argument_list|)
operator|.
name|port
argument_list|(
name|port
argument_list|)
expr_stmt|;
comment|// will automatic find the spark component to use, as we setup that component in the BaseSparkTest
name|rest
argument_list|(
literal|"/spark/hello"
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:hello"
argument_list|)
expr_stmt|;
name|rest
argument_list|(
literal|"/spark/bye"
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:bye"
argument_list|)
operator|.
name|post
argument_list|()
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

