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
name|management
operator|.
name|InstrumentationProcessor
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
name|management
operator|.
name|JmxSystemPropertyKeys
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
name|DelegateAsyncProcessor
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|ErrorHandlerTest
specifier|public
class|class
name|ErrorHandlerTest
extends|extends
name|TestSupport
block|{
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
name|errorHandler
argument_list|(
name|loggingErrorHandler
argument_list|(
literal|"FOO.BAR"
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
literal|"seda:a"
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
name|processor
operator|=
name|unwrap
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|LoggingErrorHandler
name|loggingProcessor
init|=
name|assertIsInstanceOf
argument_list|(
name|LoggingErrorHandler
operator|.
name|class
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|processor
operator|=
name|unwrap
argument_list|(
name|loggingProcessor
operator|.
name|getOutput
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
name|processor
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
name|to
argument_list|(
literal|"seda:b"
argument_list|)
expr_stmt|;
comment|// this route will use the default error handler,
comment|// DeadLetterChannel
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
name|String
name|endpointUri
init|=
name|key
operator|.
name|getEndpointUri
argument_list|()
decl_stmt|;
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
name|unwrap
argument_list|(
name|consumerRoute
operator|.
name|getProcessor
argument_list|()
argument_list|)
decl_stmt|;
name|SendProcessor
name|sendProcessor
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|endpointUri
operator|.
name|equals
argument_list|(
literal|"seda:a"
argument_list|)
condition|)
block|{
name|LoggingErrorHandler
name|loggingProcessor
init|=
name|assertIsInstanceOf
argument_list|(
name|LoggingErrorHandler
operator|.
name|class
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|Processor
name|outputProcessor
init|=
name|loggingProcessor
operator|.
name|getOutput
argument_list|()
decl_stmt|;
if|if
condition|(
name|Boolean
operator|.
name|getBoolean
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|DISABLED
argument_list|)
condition|)
block|{
name|sendProcessor
operator|=
name|assertIsInstanceOf
argument_list|(
name|SendProcessor
operator|.
name|class
argument_list|,
name|outputProcessor
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|InstrumentationProcessor
name|interceptor
init|=
name|assertIsInstanceOf
argument_list|(
name|InstrumentationProcessor
operator|.
name|class
argument_list|,
name|outputProcessor
argument_list|)
decl_stmt|;
name|sendProcessor
operator|=
name|assertIsInstanceOf
argument_list|(
name|SendProcessor
operator|.
name|class
argument_list|,
name|interceptor
operator|.
name|getProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|assertEquals
argument_list|(
literal|"From endpoint"
argument_list|,
literal|"seda:b"
argument_list|,
name|endpointUri
argument_list|)
expr_stmt|;
name|DeadLetterChannel
name|deadLetterChannel
init|=
name|assertIsInstanceOf
argument_list|(
name|DeadLetterChannel
operator|.
name|class
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|Processor
name|outputProcessor
init|=
name|deadLetterChannel
operator|.
name|getOutput
argument_list|()
decl_stmt|;
if|if
condition|(
name|Boolean
operator|.
name|getBoolean
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|DISABLED
argument_list|)
condition|)
block|{
name|sendProcessor
operator|=
name|assertIsInstanceOf
argument_list|(
name|SendProcessor
operator|.
name|class
argument_list|,
name|outputProcessor
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|InstrumentationProcessor
name|interceptor
init|=
name|assertIsInstanceOf
argument_list|(
name|InstrumentationProcessor
operator|.
name|class
argument_list|,
name|outputProcessor
argument_list|)
decl_stmt|;
name|sendProcessor
operator|=
name|assertIsInstanceOf
argument_list|(
name|SendProcessor
operator|.
name|class
argument_list|,
name|interceptor
operator|.
name|getProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"For "
operator|+
name|endpointUri
operator|+
literal|" using: "
operator|+
name|sendProcessor
argument_list|)
expr_stmt|;
block|}
block|}
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
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"seda:errors"
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
literal|"seda:a"
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
name|unwrap
argument_list|(
name|consumerRoute
operator|.
name|getProcessor
argument_list|()
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|DeadLetterChannel
operator|.
name|class
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
block|}
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
literal|"seda:a"
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
name|processor
operator|=
name|unwrap
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|DeadLetterChannel
name|deadLetterChannel
init|=
name|assertIsInstanceOf
argument_list|(
name|DeadLetterChannel
operator|.
name|class
argument_list|,
name|processor
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
DECL|method|testDisablingInheritenceOfErrorHandlers ()
specifier|public
name|void
name|testDisablingInheritenceOfErrorHandlers
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
name|inheritErrorHandler
argument_list|(
literal|false
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
literal|"seda:a"
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
name|unwrap
argument_list|(
name|consumerRoute
operator|.
name|getProcessor
argument_list|()
argument_list|)
decl_stmt|;
name|LoggingErrorHandler
name|loggingProcessor
init|=
name|assertIsInstanceOf
argument_list|(
name|LoggingErrorHandler
operator|.
name|class
argument_list|,
name|processor
argument_list|)
decl_stmt|;
if|if
condition|(
name|Boolean
operator|.
name|getBoolean
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|DISABLED
argument_list|)
condition|)
block|{
name|processor
operator|=
name|loggingProcessor
operator|.
name|getOutput
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|InstrumentationProcessor
name|interceptor
init|=
name|assertIsInstanceOf
argument_list|(
name|InstrumentationProcessor
operator|.
name|class
argument_list|,
name|loggingProcessor
operator|.
name|getOutput
argument_list|()
argument_list|)
decl_stmt|;
name|processor
operator|=
name|interceptor
operator|.
name|getProcessor
argument_list|()
expr_stmt|;
block|}
name|FilterProcessor
name|filterProcessor
init|=
name|assertIsInstanceOf
argument_list|(
name|FilterProcessor
operator|.
name|class
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|SendProcessor
name|sendProcessor
init|=
name|assertIsInstanceOf
argument_list|(
name|SendProcessor
operator|.
name|class
argument_list|,
name|filterProcessor
operator|.
name|getProcessor
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
block|}
end_class

end_unit

