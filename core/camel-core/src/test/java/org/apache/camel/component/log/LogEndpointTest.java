begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.log
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|log
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
name|AsyncCallback
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
name|support
operator|.
name|processor
operator|.
name|CamelLogProcessor
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
DECL|class|LogEndpointTest
specifier|public
class|class
name|LogEndpointTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|logged
specifier|private
specifier|static
name|Exchange
name|logged
decl_stmt|;
DECL|class|MyLogger
specifier|private
specifier|static
class|class
name|MyLogger
extends|extends
name|CamelLogProcessor
block|{
annotation|@
name|Override
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|logged
operator|=
name|exchange
expr_stmt|;
return|return
name|super
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"myLogger"
return|;
block|}
block|}
annotation|@
name|Test
DECL|method|testLogEndpoint ()
specifier|public
name|void
name|testLogEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
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
literal|"direct:start1"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|logged
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLogEndpointGroupSize ()
specifier|public
name|void
name|testLogEndpointGroupSize
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
name|out
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|int
name|expectedCount
init|=
literal|50
decl_stmt|;
name|out
operator|.
name|expectedMessageCount
argument_list|(
name|expectedCount
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|expectedCount
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start2"
argument_list|,
literal|"blub"
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|assertIsSatisfied
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
name|LogEndpoint
name|end
init|=
operator|new
name|LogEndpoint
argument_list|()
decl_stmt|;
name|end
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|end
operator|.
name|setLogger
argument_list|(
operator|new
name|MyLogger
argument_list|()
argument_list|)
expr_stmt|;
name|LogEndpoint
name|endpoint
init|=
operator|new
name|LogEndpoint
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|setLoggerName
argument_list|(
literal|"loggerSetter"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setGroupSize
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"log:myLogger"
argument_list|,
name|end
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start1"
argument_list|)
operator|.
name|to
argument_list|(
name|end
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start2"
argument_list|)
operator|.
name|to
argument_list|(
name|endpoint
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

