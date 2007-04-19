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
DECL|field|receivedCounter
specifier|private
name|int
name|receivedCounter
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
DECL|field|latch
specifier|private
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
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
DECL|method|assertIsSatisfied ()
specifier|public
name|void
name|assertIsSatisfied
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
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|expectedCount
operator|>=
literal|0
condition|)
block|{
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
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|receivedCounter
operator|=
literal|0
expr_stmt|;
block|}
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
name|receivedCounter
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
name|exchangesReceived
operator|.
name|add
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
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
operator|++
name|receivedCounter
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
block|}
end_class

end_unit

