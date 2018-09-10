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
name|Consumer
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
name|Producer
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
name|DefaultConsumer
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
name|DefaultEndpoint
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
name|DefaultProducer
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|Endpoint2MustBeStartedBeforeSendProcessorTest
specifier|public
class|class
name|Endpoint2MustBeStartedBeforeSendProcessorTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|myendpoint
specifier|private
name|MyEndpoint
name|myendpoint
decl_stmt|;
DECL|field|order
specifier|private
specifier|volatile
name|String
name|order
init|=
literal|""
decl_stmt|;
annotation|@
name|Test
DECL|method|testEndpointMustBeStartedBeforeProducer ()
specifier|public
name|void
name|testEndpointMustBeStartedBeforeProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|context
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
name|myendpoint
operator|=
operator|new
name|MyEndpoint
argument_list|(
literal|"myendpoint"
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
name|myendpoint
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"EndpointProducer"
argument_list|,
name|order
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEndpointMustBeStartedBeforeConsumer ()
specifier|public
name|void
name|testEndpointMustBeStartedBeforeConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|context
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
name|myendpoint
operator|=
operator|new
name|MyEndpoint
argument_list|(
literal|"myendpoint"
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|myendpoint
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"EndpointConsumer"
argument_list|,
name|order
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEndpointMustBeStartedBeforeConsumerAndProducer ()
specifier|public
name|void
name|testEndpointMustBeStartedBeforeConsumerAndProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|context
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
name|myendpoint
operator|=
operator|new
name|MyEndpoint
argument_list|(
literal|"myendpoint"
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|myendpoint
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|to
argument_list|(
name|myendpoint
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"EndpointProducerConsumer"
argument_list|,
name|order
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEndpointStartedOnceAndOnlyStoppedOnShutdown ()
specifier|public
name|void
name|testEndpointStartedOnceAndOnlyStoppedOnShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|context
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
name|myendpoint
operator|=
operator|new
name|MyEndpoint
argument_list|(
literal|"myendpoint"
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|myendpoint
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|to
argument_list|(
name|myendpoint
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"EndpointProducerConsumer"
argument_list|,
name|order
argument_list|)
expr_stmt|;
name|order
operator|=
literal|""
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"StopConsumerStopProducer"
argument_list|,
name|order
argument_list|)
expr_stmt|;
name|order
operator|=
literal|""
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ProducerConsumer"
argument_list|,
name|order
argument_list|)
expr_stmt|;
name|order
operator|=
literal|""
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"StopConsumerStopProducerStopEndpoint"
argument_list|,
name|order
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|class|MyEndpoint
specifier|private
specifier|final
class|class
name|MyEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|method|MyEndpoint (String endpointUri, CamelContext camelContext)
specifier|private
name|MyEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|setEndpointUri
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|MyProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|MyConsumer
argument_list|(
name|this
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|// in this test we use start/stop to implement logic
comment|// this is however discouraged, as you should prefer to use doStart/doStop
annotation|@
name|Override
DECL|method|doStart ()
specifier|public
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|order
operator|+=
literal|"Endpoint"
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|public
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|order
operator|+=
literal|"StopEndpoint"
expr_stmt|;
block|}
block|}
DECL|class|MyProducer
specifier|private
class|class
name|MyProducer
extends|extends
name|DefaultProducer
block|{
DECL|method|MyProducer (Endpoint endpoint)
name|MyProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
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
comment|// noop
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|order
operator|+=
literal|"Producer"
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|order
operator|+=
literal|"StopProducer"
expr_stmt|;
block|}
block|}
DECL|class|MyConsumer
specifier|private
class|class
name|MyConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|method|MyConsumer (Endpoint endpoint, Processor processor)
name|MyConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|order
operator|+=
literal|"Consumer"
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|order
operator|+=
literal|"StopConsumer"
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

