begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|DefaultConsumerBridgeErrorHandlerRedeliveryTest
specifier|public
class|class
name|DefaultConsumerBridgeErrorHandlerRedeliveryTest
extends|extends
name|DefaultConsumerBridgeErrorHandlerTest
block|{
DECL|field|redeliverCounter
specifier|protected
specifier|final
name|AtomicInteger
name|redeliverCounter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testDefaultConsumerBridgeErrorHandler ()
specifier|public
name|void
name|testDefaultConsumerBridgeErrorHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:dead"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Cannot process"
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// should not attempt redelivery as we must be exhausted when bridging the error handler
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|redeliverCounter
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|Exception
name|cause
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:dead"
argument_list|)
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cause
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Simulated"
argument_list|,
name|cause
operator|.
name|getMessage
argument_list|()
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
comment|// register our custom component
name|getContext
argument_list|()
operator|.
name|addComponent
argument_list|(
literal|"my"
argument_list|,
operator|new
name|MyComponent
argument_list|()
argument_list|)
expr_stmt|;
comment|// configure exception clause
name|onException
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
operator|.
name|onRedelivery
argument_list|(
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
name|redeliverCounter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
comment|// setting delay to zero is just to make unit testing faster
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:dead"
argument_list|)
expr_stmt|;
comment|// configure the consumer to bridge with the Camel error handler,
comment|// so the above error handler will trigger if exceptions also
comment|// occurs inside the consumer
name|from
argument_list|(
literal|"my:foo?consumer.bridgeErrorHandler=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo"
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
