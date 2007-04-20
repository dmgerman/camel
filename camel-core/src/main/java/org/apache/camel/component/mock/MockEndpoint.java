begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mock
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mock
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
name|Component
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
name|DefaultExchange
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_comment
comment|/**  * A Mock endpoint which provides a literate, fluent API for testing routes using  * a<a href="http://jmock.org/">JMock style</a> API.  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|MockEndpoint
specifier|public
class|class
name|MockEndpoint
extends|extends
name|DefaultEndpoint
argument_list|<
name|Exchange
argument_list|>
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|expectedCount
specifier|private
name|int
name|expectedCount
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|processors
specifier|private
name|Map
argument_list|<
name|Integer
argument_list|,
name|Processor
argument_list|<
name|Exchange
argument_list|>
argument_list|>
name|processors
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Processor
argument_list|<
name|Exchange
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|exchangesReceived
specifier|private
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchangesReceived
init|=
operator|new
name|ArrayList
argument_list|<
name|Exchange
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|failures
specifier|private
name|List
argument_list|<
name|Throwable
argument_list|>
name|failures
init|=
operator|new
name|ArrayList
argument_list|<
name|Throwable
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|tests
specifier|private
name|List
argument_list|<
name|Runnable
argument_list|>
name|tests
init|=
operator|new
name|ArrayList
argument_list|<
name|Runnable
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|latch
specifier|private
name|CountDownLatch
name|latch
decl_stmt|;
DECL|field|sleepForEmptyTest
specifier|private
name|long
name|sleepForEmptyTest
init|=
literal|0L
decl_stmt|;
DECL|method|assertIsSatisfied (MockEndpoint... endpoints)
specifier|public
specifier|static
name|void
name|assertIsSatisfied
parameter_list|(
name|MockEndpoint
modifier|...
name|endpoints
parameter_list|)
throws|throws
name|InterruptedException
block|{
comment|// lets only wait on the first empty endpoint
name|int
name|count
init|=
literal|0
decl_stmt|;
for|for
control|(
name|MockEndpoint
name|endpoint
range|:
name|endpoints
control|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|getExpectedCount
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|count
operator|++
expr_stmt|;
block|}
block|}
for|for
control|(
name|MockEndpoint
name|endpoint
range|:
name|endpoints
control|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|getExpectedCount
argument_list|()
operator|==
literal|0
condition|)
block|{
if|if
condition|(
name|count
operator|==
literal|0
condition|)
block|{
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|count
operator|++
expr_stmt|;
block|}
else|else
block|{
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|expectsMessageCount (int count, MockEndpoint... endpoints)
specifier|public
specifier|static
name|void
name|expectsMessageCount
parameter_list|(
name|int
name|count
parameter_list|,
name|MockEndpoint
modifier|...
name|endpoints
parameter_list|)
throws|throws
name|InterruptedException
block|{
for|for
control|(
name|MockEndpoint
name|endpoint
range|:
name|endpoints
control|)
block|{
name|endpoint
operator|.
name|expectsMessageCount
argument_list|(
name|count
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|MockEndpoint (String endpointUri, Component component)
specifier|public
name|MockEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|createExchange ()
specifier|public
name|Exchange
name|createExchange
parameter_list|()
block|{
return|return
operator|new
name|DefaultExchange
argument_list|(
name|getContext
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor<Exchange> processor)
specifier|public
name|Consumer
argument_list|<
name|Exchange
argument_list|>
name|createConsumer
parameter_list|(
name|Processor
argument_list|<
name|Exchange
argument_list|>
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"You cannot consume from this endpoint"
argument_list|)
throw|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
argument_list|<
name|Exchange
argument_list|>
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|DefaultProducer
argument_list|<
name|Exchange
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|onExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|// Testing API
comment|//-------------------------------------------------------------------------
comment|/**      * Validates that all the available expectations on this endpoint are satisfied; or throw an exception      */
DECL|method|assertIsSatisfied ()
specifier|public
name|void
name|assertIsSatisfied
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|assertIsSatisfied
argument_list|(
name|sleepForEmptyTest
argument_list|)
expr_stmt|;
block|}
comment|/**      * Validates that all the available expectations on this endpoint are satisfied; or throw an exception      */
DECL|method|assertIsSatisfied (long timeoutForEmptyEndpoints)
specifier|public
name|void
name|assertIsSatisfied
parameter_list|(
name|long
name|timeoutForEmptyEndpoints
parameter_list|)
throws|throws
name|InterruptedException
block|{
if|if
condition|(
name|latch
operator|!=
literal|null
condition|)
block|{
comment|// now lets wait for the results
name|latch
operator|.
name|await
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|expectedCount
operator|==
literal|0
condition|)
block|{
comment|// lets wait a little bit just in case
if|if
condition|(
name|timeoutForEmptyEndpoints
operator|>
literal|0
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|timeoutForEmptyEndpoints
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|expectedCount
operator|>=
literal|0
condition|)
block|{
name|int
name|receivedCounter
init|=
name|getReceivedCounter
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Expected message count"
argument_list|,
name|expectedCount
argument_list|,
name|receivedCounter
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Runnable
name|test
range|:
name|tests
control|)
block|{
name|test
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|Throwable
name|failure
range|:
name|failures
control|)
block|{
if|if
condition|(
name|failure
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Caught: "
operator|+
name|failure
argument_list|,
name|failure
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"Failed due to caught exception: "
operator|+
name|failure
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Specifies the expected number of message exchanges that should be received by this endpoint      *      * @param expectedCount the number of message exchanges that should be expected by this endpoint      */
DECL|method|expectedMessageCount (int expectedCount)
specifier|public
name|void
name|expectedMessageCount
parameter_list|(
name|int
name|expectedCount
parameter_list|)
block|{
name|this
operator|.
name|expectedCount
operator|=
name|expectedCount
expr_stmt|;
if|if
condition|(
name|expectedCount
operator|<=
literal|0
condition|)
block|{
name|latch
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|latch
operator|=
operator|new
name|CountDownLatch
argument_list|(
name|expectedCount
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Adds an expectation that the given body values are received by this endpoint      */
DECL|method|expectedBodiesReceived (final List bodies)
specifier|public
name|void
name|expectedBodiesReceived
parameter_list|(
specifier|final
name|List
name|bodies
parameter_list|)
block|{
name|expectedMessageCount
argument_list|(
name|bodies
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|expects
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|int
name|counter
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Object
name|expectedBody
range|:
name|bodies
control|)
block|{
name|Exchange
name|exchange
init|=
name|getExchangesReceived
argument_list|()
operator|.
name|get
argument_list|(
name|counter
operator|++
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"No exchange received for counter: "
operator|+
name|counter
argument_list|,
name|exchange
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|Object
name|actualBody
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Body of message: "
operator|+
name|counter
argument_list|,
name|expectedBody
argument_list|,
name|actualBody
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
name|getEndpointUri
argument_list|()
operator|+
literal|">>>> message: "
operator|+
name|counter
operator|+
literal|" with body: "
operator|+
name|actualBody
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds an expectation that the given body values are received by this endpoint      */
DECL|method|expectedBodiesReceived (Object... bodies)
specifier|public
name|void
name|expectedBodiesReceived
parameter_list|(
name|Object
modifier|...
name|bodies
parameter_list|)
block|{
name|List
name|bodyList
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|body
range|:
name|bodies
control|)
block|{
name|bodyList
operator|.
name|add
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
name|expectedBodiesReceived
argument_list|(
name|bodyList
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds the expection which will be invoked when enough messages are received      */
DECL|method|expects (Runnable runnable)
specifier|public
name|void
name|expects
parameter_list|(
name|Runnable
name|runnable
parameter_list|)
block|{
name|tests
operator|.
name|add
argument_list|(
name|runnable
argument_list|)
expr_stmt|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getFailures ()
specifier|public
name|List
argument_list|<
name|Throwable
argument_list|>
name|getFailures
parameter_list|()
block|{
return|return
name|failures
return|;
block|}
DECL|method|getReceivedCounter ()
specifier|public
name|int
name|getReceivedCounter
parameter_list|()
block|{
return|return
name|getExchangesReceived
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|getExchangesReceived ()
specifier|public
name|List
argument_list|<
name|Exchange
argument_list|>
name|getExchangesReceived
parameter_list|()
block|{
return|return
name|exchangesReceived
return|;
block|}
DECL|method|getExpectedCount ()
specifier|public
name|int
name|getExpectedCount
parameter_list|()
block|{
return|return
name|expectedCount
return|;
block|}
DECL|method|getSleepForEmptyTest ()
specifier|public
name|long
name|getSleepForEmptyTest
parameter_list|()
block|{
return|return
name|sleepForEmptyTest
return|;
block|}
comment|/**      * Allows a sleep to be specified to wait to check that this endpoint really is empty when      * {@link #expectedMessageCount(int)} is called with zero      *      * @param sleepForEmptyTest the milliseconds to sleep for to determine that this endpoint really is empty      */
DECL|method|setSleepForEmptyTest (long sleepForEmptyTest)
specifier|public
name|void
name|setSleepForEmptyTest
parameter_list|(
name|long
name|sleepForEmptyTest
parameter_list|)
block|{
name|this
operator|.
name|sleepForEmptyTest
operator|=
name|sleepForEmptyTest
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|onExchange (Exchange exchange)
specifier|protected
specifier|synchronized
name|void
name|onExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
name|log
operator|.
name|debug
argument_list|(
name|getEndpointUri
argument_list|()
operator|+
literal|">>>> "
operator|+
name|exchange
argument_list|)
expr_stmt|;
name|exchangesReceived
operator|.
name|add
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|Processor
argument_list|<
name|Exchange
argument_list|>
name|processor
init|=
name|processors
operator|.
name|get
argument_list|(
name|getReceivedCounter
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|latch
operator|!=
literal|null
condition|)
block|{
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|failures
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertEquals (String message, Object expectedValue, Object actualValue)
specifier|protected
name|void
name|assertEquals
parameter_list|(
name|String
name|message
parameter_list|,
name|Object
name|expectedValue
parameter_list|,
name|Object
name|actualValue
parameter_list|)
block|{
if|if
condition|(
operator|!
name|ObjectHelper
operator|.
name|equals
argument_list|(
name|expectedValue
argument_list|,
name|actualValue
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|message
operator|+
literal|". Expected:<"
operator|+
name|expectedValue
operator|+
literal|"> but was:<"
operator|+
name|actualValue
operator|+
literal|">"
argument_list|)
throw|;
block|}
block|}
DECL|method|assertTrue (String message, boolean predicate)
specifier|protected
name|void
name|assertTrue
parameter_list|(
name|String
name|message
parameter_list|,
name|boolean
name|predicate
parameter_list|)
block|{
if|if
condition|(
operator|!
name|predicate
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|message
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

