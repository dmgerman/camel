begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.direct
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|direct
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
name|DefaultCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
DECL|class|TwoCamelContextDirectEndpointTest
specifier|public
class|class
name|TwoCamelContextDirectEndpointTest
extends|extends
name|Assert
block|{
DECL|field|camel1
specifier|private
name|DefaultCamelContext
name|camel1
decl_stmt|;
DECL|field|camel2
specifier|private
name|DefaultCamelContext
name|camel2
decl_stmt|;
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
name|camel1
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|camel1
operator|.
name|addRoutes
argument_list|(
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|camel1
operator|.
name|start
argument_list|()
expr_stmt|;
name|camel2
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|camel2
operator|.
name|addRoutes
argument_list|(
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|camel2
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|camel1
operator|.
name|stop
argument_list|()
expr_stmt|;
name|camel2
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTwoCamelContextDirectEndpoint ()
specifier|public
name|void
name|testTwoCamelContextDirectEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|start1
init|=
name|camel1
operator|.
name|getEndpoint
argument_list|(
literal|"direct:start"
argument_list|)
decl_stmt|;
name|Endpoint
name|start2
init|=
name|camel2
operator|.
name|getEndpoint
argument_list|(
literal|"direct:start"
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|start1
argument_list|,
name|start2
argument_list|)
expr_stmt|;
name|Endpoint
name|foo1
init|=
name|camel1
operator|.
name|getEndpoint
argument_list|(
literal|"direct:foo"
argument_list|)
decl_stmt|;
name|Endpoint
name|foo2
init|=
name|camel2
operator|.
name|getEndpoint
argument_list|(
literal|"direct:foo"
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|foo1
argument_list|,
name|foo2
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock1
init|=
name|camel1
operator|.
name|getEndpoint
argument_list|(
literal|"mock:a"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mock1
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock2
init|=
name|camel2
operator|.
name|getEndpoint
argument_list|(
literal|"mock:b"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mock2
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|camel1
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|camel2
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|mock1
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|mock2
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

