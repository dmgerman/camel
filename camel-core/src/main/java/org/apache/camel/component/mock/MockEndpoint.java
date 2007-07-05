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
name|Expression
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
name|Message
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
name|ExpressionComparator
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
name|CopyOnWriteArrayList
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
DECL|field|counter
specifier|private
name|int
name|counter
init|=
literal|0
decl_stmt|;
DECL|field|processors
specifier|private
name|Map
argument_list|<
name|Integer
argument_list|,
name|Processor
argument_list|>
name|processors
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Processor
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|receivedExchanges
specifier|private
name|List
argument_list|<
name|Exchange
argument_list|>
name|receivedExchanges
init|=
operator|new
name|CopyOnWriteArrayList
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
name|CopyOnWriteArrayList
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
name|CopyOnWriteArrayList
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
literal|2000L
decl_stmt|;
DECL|field|defaulResultWaitMillis
specifier|private
name|long
name|defaulResultWaitMillis
init|=
literal|10000L
decl_stmt|;
DECL|field|expectedMinimumCount
specifier|private
name|int
name|expectedMinimumCount
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|expectedBodyValues
specifier|private
name|List
name|expectedBodyValues
decl_stmt|;
DECL|field|actualBodyValues
specifier|private
name|List
name|actualBodyValues
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
DECL|method|assertWait (long timeout, TimeUnit unit, MockEndpoint... endpoints)
specifier|public
specifier|static
name|void
name|assertWait
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|,
name|MockEndpoint
modifier|...
name|endpoints
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|long
name|left
init|=
name|unit
operator|.
name|toMillis
argument_list|(
name|timeout
argument_list|)
decl_stmt|;
name|long
name|end
init|=
name|start
operator|+
name|left
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
operator|!
name|endpoint
operator|.
name|await
argument_list|(
name|left
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"Timeout waiting for endpoints to receive enough messages. "
operator|+
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
operator|+
literal|" timed out."
argument_list|)
throw|;
block|}
name|left
operator|=
name|end
operator|-
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
if|if
condition|(
name|left
operator|<=
literal|0
condition|)
block|{
name|left
operator|=
literal|0
expr_stmt|;
block|}
block|}
block|}
DECL|method|assertIsSatisfied (long timeout, TimeUnit unit, MockEndpoint... endpoints)
specifier|public
specifier|static
name|void
name|assertIsSatisfied
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|,
name|MockEndpoint
modifier|...
name|endpoints
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|assertWait
argument_list|(
name|timeout
argument_list|,
name|unit
argument_list|,
name|endpoints
argument_list|)
expr_stmt|;
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
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
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
name|assertIsSatisfied
argument_list|()
expr_stmt|;
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
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
argument_list|<
name|Exchange
argument_list|>
name|createConsumer
parameter_list|(
name|Processor
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
comment|/**      * Validates that all the available expectations on this endpoint are satisfied; or throw an exception      *      * @param timeoutForEmptyEndpoints the timeout in milliseconds that we should wait for the test to be true      */
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
name|expectedCount
operator|>=
literal|0
condition|)
block|{
if|if
condition|(
name|expectedCount
operator|!=
name|getReceivedCounter
argument_list|()
condition|)
block|{
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
name|log
operator|.
name|debug
argument_list|(
literal|"Sleeping for: "
operator|+
name|timeoutForEmptyEndpoints
operator|+
literal|" millis to check there really are no messages received"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|timeoutForEmptyEndpoints
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|waitForCompleteLatch
argument_list|()
expr_stmt|;
block|}
block|}
name|assertEquals
argument_list|(
literal|"Received message count"
argument_list|,
name|expectedCount
argument_list|,
name|getReceivedCounter
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|expectedMinimumCount
operator|>
literal|0
operator|&&
name|getReceivedCounter
argument_list|()
operator|<
name|expectedMinimumCount
condition|)
block|{
name|waitForCompleteLatch
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|expectedMinimumCount
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
name|assertTrue
argument_list|(
literal|"Received message count "
operator|+
name|receivedCounter
operator|+
literal|", expected at least "
operator|+
name|expectedCount
argument_list|,
name|expectedCount
operator|<=
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
literal|"Caught on "
operator|+
name|getEndpointUri
argument_list|()
operator|+
literal|" Exception: "
operator|+
name|failure
argument_list|,
name|failure
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failed due to caught exception: "
operator|+
name|failure
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Validates that the assertions fail on this endpoint      */
DECL|method|assertIsNotSatisfied ()
specifier|public
name|void
name|assertIsNotSatisfied
parameter_list|()
throws|throws
name|InterruptedException
block|{
try|try
block|{
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected assertion failure!"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionError
name|e
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Caught expected failure: "
operator|+
name|e
argument_list|)
expr_stmt|;
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
comment|/**      * Specifies the minimum number of expected message exchanges that should be received by this endpoint      *      * @param expectedCount the number of message exchanges that should be expected by this endpoint      */
DECL|method|expectedMinimumMessageCount (int expectedCount)
specifier|public
name|void
name|expectedMinimumMessageCount
parameter_list|(
name|int
name|expectedCount
parameter_list|)
block|{
name|this
operator|.
name|expectedMinimumCount
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
name|expectedMinimumCount
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
name|this
operator|.
name|expectedBodyValues
operator|=
name|bodies
expr_stmt|;
name|this
operator|.
name|actualBodyValues
operator|=
operator|new
name|ArrayList
argument_list|()
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
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|expectedBodyValues
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Exchange
name|exchange
init|=
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"No exchange received for counter: "
operator|+
name|i
argument_list|,
name|exchange
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|Object
name|expectedBody
init|=
name|expectedBodyValues
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Object
name|actualBody
init|=
name|actualBodyValues
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Body of message: "
operator|+
name|i
argument_list|,
name|expectedBody
argument_list|,
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
comment|/**      * Adds an expectation that messages received should have ascending values of the given expression      * such as a user generated counter value      *      * @param expression      */
DECL|method|expectsAscending (final Expression<Exchange> expression)
specifier|public
name|void
name|expectsAscending
parameter_list|(
specifier|final
name|Expression
argument_list|<
name|Exchange
argument_list|>
name|expression
parameter_list|)
block|{
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
name|assertMessagesAscending
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds an expectation that messages received should have descending values of the given expression      * such as a user generated counter value      *      * @param expression      */
DECL|method|expectsDescending (final Expression<Exchange> expression)
specifier|public
name|void
name|expectsDescending
parameter_list|(
specifier|final
name|Expression
argument_list|<
name|Exchange
argument_list|>
name|expression
parameter_list|)
block|{
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
name|assertMessagesDescending
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds an expectation that no duplicate messages should be received using the      * expression to determine the message ID      *      * @param expression the expression used to create a unique message ID for      * message comparison (which could just be the message payload if the payload      * can be tested for uniqueness using {@link Object#equals(Object)}      * and {@link Object#hashCode()}      */
DECL|method|expectsNoDuplicates (final Expression<Exchange> expression)
specifier|public
name|void
name|expectsNoDuplicates
parameter_list|(
specifier|final
name|Expression
argument_list|<
name|Exchange
argument_list|>
name|expression
parameter_list|)
block|{
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
name|assertNoDuplicates
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Asserts that the messages have ascending values of the given expression      */
DECL|method|assertMessagesAscending (Expression<Exchange> expression)
specifier|public
name|void
name|assertMessagesAscending
parameter_list|(
name|Expression
argument_list|<
name|Exchange
argument_list|>
name|expression
parameter_list|)
block|{
name|assertMessagesSorted
argument_list|(
name|expression
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Asserts that the messages have descending values of the given expression      */
DECL|method|assertMessagesDescending (Expression<Exchange> expression)
specifier|public
name|void
name|assertMessagesDescending
parameter_list|(
name|Expression
argument_list|<
name|Exchange
argument_list|>
name|expression
parameter_list|)
block|{
name|assertMessagesSorted
argument_list|(
name|expression
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|assertMessagesSorted (Expression<Exchange> expression, boolean ascending)
specifier|protected
name|void
name|assertMessagesSorted
parameter_list|(
name|Expression
argument_list|<
name|Exchange
argument_list|>
name|expression
parameter_list|,
name|boolean
name|ascending
parameter_list|)
block|{
name|String
name|type
init|=
operator|(
name|ascending
operator|)
condition|?
literal|"ascending"
else|:
literal|"descending"
decl_stmt|;
name|ExpressionComparator
name|comparator
init|=
operator|new
name|ExpressionComparator
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|getReceivedExchanges
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|list
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|int
name|j
init|=
name|i
operator|-
literal|1
decl_stmt|;
name|Exchange
name|e1
init|=
name|list
operator|.
name|get
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|Exchange
name|e2
init|=
name|list
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|int
name|result
init|=
name|comparator
operator|.
name|compare
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|0
condition|)
block|{
name|fail
argument_list|(
literal|"Messages not "
operator|+
name|type
operator|+
literal|". Messages"
operator|+
name|j
operator|+
literal|" and "
operator|+
name|i
operator|+
literal|" are equal with value: "
operator|+
name|expression
operator|.
name|evaluate
argument_list|(
name|e1
argument_list|)
operator|+
literal|" for expression: "
operator|+
name|expression
operator|+
literal|". Exchanges: "
operator|+
name|e1
operator|+
literal|" and "
operator|+
name|e2
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|ascending
condition|)
block|{
name|result
operator|=
name|result
operator|*
operator|-
literal|1
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|>
literal|0
condition|)
block|{
name|fail
argument_list|(
literal|"Messages not "
operator|+
name|type
operator|+
literal|". Message "
operator|+
name|j
operator|+
literal|" has value: "
operator|+
name|expression
operator|.
name|evaluate
argument_list|(
name|e1
argument_list|)
operator|+
literal|" and message "
operator|+
name|i
operator|+
literal|" has value: "
operator|+
name|expression
operator|.
name|evaluate
argument_list|(
name|e2
argument_list|)
operator|+
literal|" for expression: "
operator|+
name|expression
operator|+
literal|". Exchanges: "
operator|+
name|e1
operator|+
literal|" and "
operator|+
name|e2
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|assertNoDuplicates (Expression<Exchange> expression)
specifier|public
name|void
name|assertNoDuplicates
parameter_list|(
name|Expression
argument_list|<
name|Exchange
argument_list|>
name|expression
parameter_list|)
block|{
name|Map
argument_list|<
name|Object
argument_list|,
name|Exchange
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|Exchange
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|getReceivedExchanges
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|list
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Exchange
name|e2
init|=
name|list
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Object
name|key
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|e2
argument_list|)
decl_stmt|;
name|Exchange
name|e1
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|e1
operator|!=
literal|null
condition|)
block|{
name|fail
argument_list|(
literal|"Duplicate message found on message "
operator|+
name|i
operator|+
literal|" has value: "
operator|+
name|key
operator|+
literal|" for expression: "
operator|+
name|expression
operator|+
literal|". Exchanges: "
operator|+
name|e1
operator|+
literal|" and "
operator|+
name|e2
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|e2
argument_list|)
expr_stmt|;
block|}
block|}
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
comment|/**      * Adds an assertion to the given message index      *      * @param messageIndex the number of the message      * @return the assertion clause      */
DECL|method|message (final int messageIndex)
specifier|public
name|AssertionClause
name|message
parameter_list|(
specifier|final
name|int
name|messageIndex
parameter_list|)
block|{
name|AssertionClause
name|clause
init|=
operator|new
name|AssertionClause
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|applyAssertionOn
argument_list|(
name|MockEndpoint
operator|.
name|this
argument_list|,
name|messageIndex
argument_list|,
name|assertExchangeReceived
argument_list|(
name|messageIndex
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|expects
argument_list|(
name|clause
argument_list|)
expr_stmt|;
return|return
name|clause
return|;
block|}
comment|/**      * Adds an assertion to all the received messages      *      * @return the assertion clause      */
DECL|method|allMessages ()
specifier|public
name|AssertionClause
name|allMessages
parameter_list|()
block|{
name|AssertionClause
name|clause
init|=
operator|new
name|AssertionClause
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|getReceivedExchanges
argument_list|()
decl_stmt|;
name|int
name|index
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|list
control|)
block|{
name|applyAssertionOn
argument_list|(
name|MockEndpoint
operator|.
name|this
argument_list|,
name|index
operator|++
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
name|expects
argument_list|(
name|clause
argument_list|)
expr_stmt|;
return|return
name|clause
return|;
block|}
comment|/**      * Asserts that the given index of message is received (starting at zero)      */
DECL|method|assertExchangeReceived (int index)
specifier|public
name|Exchange
name|assertExchangeReceived
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|int
name|count
init|=
name|getReceivedCounter
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Not enough messages received. Was: "
operator|+
name|count
argument_list|,
name|count
operator|>
name|index
argument_list|)
expr_stmt|;
return|return
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
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
name|getReceivedExchanges
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|getReceivedExchanges ()
specifier|public
name|List
argument_list|<
name|Exchange
argument_list|>
name|getReceivedExchanges
parameter_list|()
block|{
return|return
name|receivedExchanges
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
DECL|method|getDefaulResultWaitMillis ()
specifier|public
name|long
name|getDefaulResultWaitMillis
parameter_list|()
block|{
return|return
name|defaulResultWaitMillis
return|;
block|}
comment|/**      * Sets the maximum amount of time the {@link #assertIsSatisfied()}      * will wait on a latch until it is satisfied      */
DECL|method|setDefaulResultWaitMillis (long defaulResultWaitMillis)
specifier|public
name|void
name|setDefaulResultWaitMillis
parameter_list|(
name|long
name|defaulResultWaitMillis
parameter_list|)
block|{
name|this
operator|.
name|defaulResultWaitMillis
operator|=
name|defaulResultWaitMillis
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
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Object
name|actualBody
init|=
name|in
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|expectedBodyValues
operator|!=
literal|null
condition|)
block|{
name|int
name|index
init|=
name|actualBodyValues
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|expectedBodyValues
operator|.
name|size
argument_list|()
operator|>
name|index
condition|)
block|{
name|Object
name|expectedBody
init|=
name|expectedBodyValues
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|expectedBody
operator|!=
literal|null
condition|)
block|{
name|actualBody
operator|=
name|in
operator|.
name|getBody
argument_list|(
name|expectedBody
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|actualBodyValues
operator|.
name|add
argument_list|(
name|actualBody
argument_list|)
expr_stmt|;
block|}
block|}
name|log
operator|.
name|debug
argument_list|(
name|getEndpointUri
argument_list|()
operator|+
literal|">>>> "
operator|+
operator|(
operator|++
name|counter
operator|)
operator|+
literal|" : "
operator|+
name|exchange
operator|+
literal|" with body: "
operator|+
name|actualBody
argument_list|)
expr_stmt|;
name|receivedExchanges
operator|.
name|add
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|Processor
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
DECL|method|waitForCompleteLatch ()
specifier|protected
name|void
name|waitForCompleteLatch
parameter_list|()
throws|throws
name|InterruptedException
block|{
if|if
condition|(
name|latch
operator|==
literal|null
condition|)
block|{
name|fail
argument_list|(
literal|"Should have a latch!"
argument_list|)
expr_stmt|;
block|}
comment|// now lets wait for the results
name|log
operator|.
name|debug
argument_list|(
literal|"Waiting on the latch for: "
operator|+
name|defaulResultWaitMillis
operator|+
literal|" millis"
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|(
name|defaulResultWaitMillis
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
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
name|fail
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
expr_stmt|;
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
name|fail
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|fail (Object message)
specifier|protected
name|void
name|fail
parameter_list|(
name|Object
name|message
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|getEndpointUri
argument_list|()
operator|+
literal|" "
operator|+
name|message
argument_list|)
throw|;
block|}
DECL|method|getExpectedMinimumCount ()
specifier|public
name|int
name|getExpectedMinimumCount
parameter_list|()
block|{
return|return
name|expectedMinimumCount
return|;
block|}
DECL|method|await ()
specifier|public
name|void
name|await
parameter_list|()
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
name|latch
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|await (long timeout, TimeUnit unit)
specifier|public
name|boolean
name|await
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
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
return|return
name|latch
operator|.
name|await
argument_list|(
name|timeout
argument_list|,
name|unit
argument_list|)
return|;
block|}
return|return
literal|true
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
block|}
end_class

end_unit

