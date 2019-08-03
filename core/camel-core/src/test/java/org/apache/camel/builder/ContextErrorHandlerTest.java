begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Channel
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
name|ExtendedCamelContext
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
name|Route
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
name|engine
operator|.
name|EventDrivenConsumerRoute
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
name|processor
operator|.
name|SendProcessor
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
name|processor
operator|.
name|errorhandler
operator|.
name|DeadLetterChannel
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
name|processor
operator|.
name|errorhandler
operator|.
name|RedeliveryPolicy
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
DECL|class|ContextErrorHandlerTest
specifier|public
class|class
name|ContextErrorHandlerTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
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
name|setUseRouteBuilder
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|RedeliveryPolicy
name|redeliveryPolicy
init|=
operator|new
name|RedeliveryPolicy
argument_list|()
decl_stmt|;
name|redeliveryPolicy
operator|.
name|maximumRedeliveries
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|redeliveryPolicy
operator|.
name|setUseExponentialBackOff
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DeadLetterChannelBuilder
name|deadLetterChannelBuilder
init|=
operator|new
name|DeadLetterChannelBuilder
argument_list|(
literal|"mock:error"
argument_list|)
decl_stmt|;
name|deadLetterChannelBuilder
operator|.
name|setRedeliveryPolicy
argument_list|(
name|redeliveryPolicy
argument_list|)
expr_stmt|;
name|context
operator|.
name|adapt
argument_list|(
name|ExtendedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|setErrorHandlerFactory
argument_list|(
name|deadLetterChannelBuilder
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|startCamelContext ()
specifier|protected
name|void
name|startCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
comment|// do nothing here
block|}
annotation|@
name|Override
DECL|method|stopCamelContext ()
specifier|protected
name|void
name|stopCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
comment|// do nothing here
block|}
DECL|method|getRouteListWithCurrentContext (RouteBuilder builder)
specifier|protected
name|List
argument_list|<
name|Route
argument_list|>
name|getRouteListWithCurrentContext
parameter_list|(
name|RouteBuilder
name|builder
parameter_list|)
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Route
argument_list|>
name|answer
init|=
name|context
operator|.
name|getRoutes
argument_list|()
decl_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Test
DECL|method|testOverloadingTheDefaultErrorHandler ()
specifier|public
name|void
name|testOverloadingTheDefaultErrorHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteBuilder
name|builder
init|=
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"log:FOO.BAR"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:b"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|List
argument_list|<
name|Route
argument_list|>
name|list
init|=
name|getRouteListWithCurrentContext
argument_list|(
name|builder
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Number routes created"
operator|+
name|list
argument_list|,
literal|1
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Route
name|route
range|:
name|list
control|)
block|{
name|Endpoint
name|key
init|=
name|route
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"From endpoint"
argument_list|,
literal|"seda://a"
argument_list|,
name|key
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|EventDrivenConsumerRoute
name|consumerRoute
init|=
name|assertIsInstanceOf
argument_list|(
name|EventDrivenConsumerRoute
operator|.
name|class
argument_list|,
name|route
argument_list|)
decl_stmt|;
name|Processor
name|processor
init|=
name|consumerRoute
operator|.
name|getProcessor
argument_list|()
decl_stmt|;
name|Channel
name|channel
init|=
name|unwrapChannel
argument_list|(
name|processor
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|DeadLetterChannel
operator|.
name|class
argument_list|,
name|channel
operator|.
name|getErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
name|SendProcessor
name|sendProcessor
init|=
name|assertIsInstanceOf
argument_list|(
name|SendProcessor
operator|.
name|class
argument_list|,
name|channel
operator|.
name|getNextProcessor
argument_list|()
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Found sendProcessor: "
operator|+
name|sendProcessor
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testGetTheDefaultErrorHandlerFromContext ()
specifier|public
name|void
name|testGetTheDefaultErrorHandlerFromContext
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteBuilder
name|builder
init|=
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"seda:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:b"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:c"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:d"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|List
argument_list|<
name|Route
argument_list|>
name|list
init|=
name|getRouteListWithCurrentContext
argument_list|(
name|builder
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Number routes created"
operator|+
name|list
argument_list|,
literal|2
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Route
name|route
range|:
name|list
control|)
block|{
name|EventDrivenConsumerRoute
name|consumerRoute
init|=
name|assertIsInstanceOf
argument_list|(
name|EventDrivenConsumerRoute
operator|.
name|class
argument_list|,
name|route
argument_list|)
decl_stmt|;
name|Processor
name|processor
init|=
name|consumerRoute
operator|.
name|getProcessor
argument_list|()
decl_stmt|;
name|Channel
name|channel
init|=
name|unwrapChannel
argument_list|(
name|processor
argument_list|)
decl_stmt|;
name|DeadLetterChannel
name|deadLetterChannel
init|=
name|assertIsInstanceOf
argument_list|(
name|DeadLetterChannel
operator|.
name|class
argument_list|,
name|channel
operator|.
name|getErrorHandler
argument_list|()
argument_list|)
decl_stmt|;
name|RedeliveryPolicy
name|redeliveryPolicy
init|=
name|deadLetterChannel
operator|.
name|getRedeliveryPolicy
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getMaximumRedeliveries()"
argument_list|,
literal|1
argument_list|,
name|redeliveryPolicy
operator|.
name|getMaximumRedeliveries
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"isUseExponentialBackOff()"
argument_list|,
literal|true
argument_list|,
name|redeliveryPolicy
operator|.
name|isUseExponentialBackOff
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

