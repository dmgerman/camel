begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.disruptor.vm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|disruptor
operator|.
name|vm
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
name|ExchangePattern
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|DisruptorVmMultipleConsumersIssueTest
specifier|public
class|class
name|DisruptorVmMultipleConsumersIssueTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testDisruptorVmMultipleConsumersIssue ()
specifier|public
name|void
name|testDisruptorVmMultipleConsumersIssue
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:d"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:e"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:done"
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
literal|"direct:inbox"
argument_list|,
literal|"Hello World"
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
name|from
argument_list|(
literal|"direct:inbox"
argument_list|)
operator|.
name|to
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|,
literal|"disruptor-vm:foo?timeout=5000"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:done"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"disruptor-vm:foo?multipleConsumers=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"disruptor-vm:foo?multipleConsumers=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:b"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"disruptor-vm:foo?multipleConsumers=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:c"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:c"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"disruptor-vm:foo?multipleConsumers=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:d"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:d"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"disruptor-vm:foo?multipleConsumers=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:e"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:e"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

