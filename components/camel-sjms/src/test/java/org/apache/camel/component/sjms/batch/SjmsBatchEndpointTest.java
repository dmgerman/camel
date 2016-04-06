begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.batch
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|batch
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|ActiveMQConnectionFactory
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
name|FailedToCreateRouteException
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
name|sjms
operator|.
name|SjmsComponent
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
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|SimpleRegistry
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|toolbox
operator|.
name|AggregationStrategies
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
DECL|class|SjmsBatchEndpointTest
specifier|public
class|class
name|SjmsBatchEndpointTest
extends|extends
name|CamelTestSupport
block|{
comment|// Create one embedded broker instance for the entire test, as we aren't actually
comment|// going to send any messages to it; we just need it so that the ConnectionFactory
comment|// has something local to connect to.
DECL|field|broker
specifier|public
specifier|static
name|EmbeddedActiveMQBroker
name|broker
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|setupBroker ()
specifier|public
specifier|static
name|void
name|setupBroker
parameter_list|()
block|{
name|broker
operator|=
operator|new
name|EmbeddedActiveMQBroker
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
try|try
block|{
name|broker
operator|.
name|before
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|t
argument_list|)
throw|;
block|}
block|}
annotation|@
name|AfterClass
DECL|method|shutDownBroker ()
specifier|public
specifier|static
name|void
name|shutDownBroker
parameter_list|()
block|{
name|broker
operator|.
name|after
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"aggStrategy"
argument_list|,
name|AggregationStrategies
operator|.
name|groupedExchange
argument_list|()
argument_list|)
expr_stmt|;
name|ActiveMQConnectionFactory
name|connectionFactory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|()
decl_stmt|;
name|connectionFactory
operator|.
name|setBrokerURL
argument_list|(
name|broker
operator|.
name|getTcpConnectorUri
argument_list|()
argument_list|)
expr_stmt|;
name|SjmsComponent
name|sjmsComponent
init|=
operator|new
name|SjmsComponent
argument_list|()
decl_stmt|;
name|sjmsComponent
operator|.
name|setConnectionFactory
argument_list|(
name|connectionFactory
argument_list|)
expr_stmt|;
name|SjmsBatchComponent
name|sjmsBatchComponent
init|=
operator|new
name|SjmsBatchComponent
argument_list|()
decl_stmt|;
name|sjmsBatchComponent
operator|.
name|setConnectionFactory
argument_list|(
name|connectionFactory
argument_list|)
expr_stmt|;
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
decl_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"sjms-batch"
argument_list|,
name|sjmsBatchComponent
argument_list|)
expr_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"sjms"
argument_list|,
name|sjmsComponent
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Override
DECL|method|isUseAdviceWith ()
specifier|public
name|boolean
name|isUseAdviceWith
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|FailedToCreateRouteException
operator|.
name|class
argument_list|)
DECL|method|testProducerFailure ()
specifier|public
name|void
name|testProducerFailure
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:in"
argument_list|)
operator|.
name|to
argument_list|(
literal|"sjms-batch:testQueue?aggregationStrategy=#unknown"
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
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|FailedToCreateRouteException
operator|.
name|class
argument_list|)
DECL|method|testConsumerNegativePollDuration ()
specifier|public
name|void
name|testConsumerNegativePollDuration
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
name|from
argument_list|(
literal|"sjms-batch:in?aggregationStrategy=#aggStrategy&pollDuration=-1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:out"
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
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|FailedToCreateRouteException
operator|.
name|class
argument_list|)
DECL|method|testConsumerNegativeConsumerCount ()
specifier|public
name|void
name|testConsumerNegativeConsumerCount
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
name|from
argument_list|(
literal|"sjms-batch:in?aggregationStrategy=#aggStrategy&consumerCount=-1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:out"
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
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|FailedToCreateRouteException
operator|.
name|class
argument_list|)
DECL|method|testConsumerTopic ()
specifier|public
name|void
name|testConsumerTopic
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
name|from
argument_list|(
literal|"sjms-batch:topic:in?aggregationStrategy=#aggStrategy"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:out"
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
block|}
block|}
end_class

end_unit

