begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ironmq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ironmq
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|io
operator|.
name|iron
operator|.
name|ironmq
operator|.
name|EmptyQueueException
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
name|BindToRegistry
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
name|EndpointInject
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|FromQueueToQueueTest
specifier|public
class|class
name|FromQueueToQueueTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:result"
argument_list|)
DECL|field|result
specifier|private
name|MockEndpoint
name|result
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"ironmq:testqueue?client=#ironMock"
argument_list|)
DECL|field|queue1
specifier|private
name|IronMQEndpoint
name|queue1
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"ironmq:testqueue2?client=#ironMock"
argument_list|)
DECL|field|queue2
specifier|private
name|IronMQEndpoint
name|queue2
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"ironMock"
argument_list|)
DECL|field|mock
specifier|private
name|IronMQClientMock
name|mock
init|=
operator|new
name|IronMQClientMock
argument_list|(
literal|"dummy"
argument_list|,
literal|"dummy"
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|shouldDeleteMessageFromQueue1 ()
specifier|public
name|void
name|shouldDeleteMessageFromQueue1
parameter_list|()
throws|throws
name|Exception
block|{
name|result
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"This is my message text."
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
try|try
block|{
name|queue1
operator|.
name|getClient
argument_list|()
operator|.
name|queue
argument_list|(
literal|"testqueue"
argument_list|)
operator|.
name|reserve
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Message was in the first queue!"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|e
operator|instanceof
name|EmptyQueueException
operator|)
condition|)
block|{
comment|// Unexpected exception.
throw|throw
name|e
throw|;
block|}
block|}
try|try
block|{
name|queue2
operator|.
name|getClient
argument_list|()
operator|.
name|queue
argument_list|(
literal|"testqueue1"
argument_list|)
operator|.
name|reserve
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Message remained in second queue!"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|e
operator|instanceof
name|EmptyQueueException
operator|)
condition|)
block|{
comment|// Unexpected exception.
throw|throw
name|e
throw|;
block|}
block|}
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
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"ironmq:testqueue?client=#ironMock"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"ironmq:testqueue?client=#ironMock"
argument_list|)
operator|.
name|to
argument_list|(
literal|"ironmq:testqueue2?client=#ironMock"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"ironmq:testqueue2?client=#ironMock"
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

