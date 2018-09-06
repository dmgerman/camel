begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|TestSupport
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
name|FilterProcessor
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
name|LoggingErrorHandler
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
name|RedeliveryPolicy
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|ErrorHandlerTest
specifier|public
class|class
name|ErrorHandlerTest
extends|extends
name|TestSupport
block|{
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
comment|// make SEDA testing faster
name|System
operator|.
name|setProperty
argument_list|(
literal|"CamelSedaPollTimeout"
argument_list|,
literal|"10"
argument_list|)
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
name|System
operator|.
name|clearProperty
argument_list|(
literal|"CamelSedaPollTimeout"
argument_list|)
expr_stmt|;
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
comment|// START SNIPPET: e1
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
comment|// use logging error handler
name|errorHandler
argument_list|(
name|loggingErrorHandler
argument_list|(
literal|"com.mycompany.foo"
argument_list|)
argument_list|)
expr_stmt|;
comment|// here is our regular route
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
comment|// END SNIPPET: e1
name|List
argument_list|<
name|Route
argument_list|>
name|list
init|=
name|getRouteList
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
name|Channel
name|channel
init|=
name|unwrapChannel
argument_list|(
name|consumerRoute
operator|.
name|getProcessor
argument_list|()
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|LoggingErrorHandler
operator|.
name|class
argument_list|,
name|channel
operator|.
name|getErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
name|Processor
name|processor
init|=
name|unwrap
argument_list|(
name|channel
operator|.
name|getNextProcessor
argument_list|()
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|SendProcessor
operator|.
name|class
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testOverloadingTheHandlerOnASingleRoute ()
specifier|public
name|void
name|testOverloadingTheHandlerOnASingleRoute
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e2
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
comment|// this route is using a nested logging error handler
name|from
argument_list|(
literal|"seda:a"
argument_list|)
comment|// here we configure the logging error handler
operator|.
name|errorHandler
argument_list|(
name|loggingErrorHandler
argument_list|(
literal|"com.mycompany.foo"
argument_list|)
argument_list|)
comment|// and we continue with the routing here
operator|.
name|to
argument_list|(
literal|"seda:b"
argument_list|)
expr_stmt|;
comment|// this route will use the default error handler (DeadLetterChannel)
name|from
argument_list|(
literal|"seda:b"
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:c"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
comment|// END SNIPPET: e2
name|List
argument_list|<
name|Route
argument_list|>
name|list
init|=
name|getRouteList
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
block|}
annotation|@
name|Test
DECL|method|testConfigureDeadLetterChannel ()
specifier|public
name|void
name|testConfigureDeadLetterChannel
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e3
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
comment|// using dead letter channel with a seda queue for errors
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"seda:errors"
argument_list|)
argument_list|)
expr_stmt|;
comment|// here is our route
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
comment|// END SNIPPET: e3
name|List
argument_list|<
name|Route
argument_list|>
name|list
init|=
name|getRouteList
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
name|Channel
name|channel
init|=
name|unwrapChannel
argument_list|(
name|consumerRoute
operator|.
name|getProcessor
argument_list|()
argument_list|)
decl_stmt|;
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
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testConfigureDeadLetterChannelWithCustomRedeliveryPolicy ()
specifier|public
name|void
name|testConfigureDeadLetterChannelWithCustomRedeliveryPolicy
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e4
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
comment|// configures dead letter channel to use seda queue for errors and use at most 2 redelveries
comment|// and exponential backoff
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"seda:errors"
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|2
argument_list|)
operator|.
name|useExponentialBackOff
argument_list|()
argument_list|)
expr_stmt|;
comment|// here is our route
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
comment|// END SNIPPET: e4
name|List
argument_list|<
name|Route
argument_list|>
name|list
init|=
name|getRouteList
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
literal|2
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
annotation|@
name|Test
DECL|method|testLoggingErrorHandler ()
specifier|public
name|void
name|testLoggingErrorHandler
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e5
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
name|errorHandler
argument_list|(
name|loggingErrorHandler
argument_list|(
literal|"FOO.BAR"
argument_list|)
argument_list|)
operator|.
name|filter
argument_list|(
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|)
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
comment|// END SNIPPET: e5
name|List
argument_list|<
name|Route
argument_list|>
name|routes
init|=
name|getRouteList
argument_list|(
name|builder
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Number routes created"
argument_list|,
literal|1
argument_list|,
name|routes
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
name|routes
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
name|Channel
name|channel
init|=
name|unwrapChannel
argument_list|(
name|consumerRoute
operator|.
name|getProcessor
argument_list|()
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|LoggingErrorHandler
operator|.
name|class
argument_list|,
name|channel
operator|.
name|getErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|FilterProcessor
operator|.
name|class
argument_list|,
name|channel
operator|.
name|getNextProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

