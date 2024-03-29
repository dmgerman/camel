begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.async
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|async
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
name|Callable
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
name|ExecutorService
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
name|Executors
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
name|AsyncCallback
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
name|CamelExchangeException
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
name|support
operator|.
name|DefaultAsyncProducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|MyAsyncProducer
specifier|public
class|class
name|MyAsyncProducer
extends|extends
name|DefaultAsyncProducer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MyAsyncProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|executor
specifier|private
specifier|final
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newCachedThreadPool
argument_list|()
decl_stmt|;
DECL|field|counter
specifier|private
specifier|final
name|AtomicInteger
name|counter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|method|MyAsyncProducer (MyAsyncEndpoint endpoint)
specifier|public
name|MyAsyncProducer
parameter_list|(
name|MyAsyncEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|MyAsyncEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|MyAsyncEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|process (final Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|executor
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Simulating a task which takes "
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getDelay
argument_list|()
operator|+
literal|" millis to reply"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getDelay
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|count
init|=
name|counter
operator|.
name|incrementAndGet
argument_list|()
decl_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getFailFirstAttempts
argument_list|()
operator|>=
name|count
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Simulating a failure at attempt "
operator|+
name|count
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|CamelExchangeException
argument_list|(
literal|"Simulated error at attempt "
operator|+
name|count
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|reply
init|=
name|getEndpoint
argument_list|()
operator|.
name|getReply
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|reply
argument_list|)
expr_stmt|;
comment|// propagate headers
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Setting reply "
operator|+
name|reply
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Callback done(false)"
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// indicate from this point forward its being routed asynchronously
name|LOG
operator|.
name|info
argument_list|(
literal|"Task submitted, now tell Camel routing engine to that this Exchange is being continued asynchronously"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

