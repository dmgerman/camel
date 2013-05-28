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
name|AsyncProcessor
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
name|ExchangePattern
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
name|Ordered
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
name|Predicate
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
name|Traceable
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
name|ServiceSupport
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
name|SynchronizationAdapter
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
name|AsyncProcessorHelper
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
name|ExchangeHelper
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
name|ServiceHelper
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|notNull
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|OnCompletionProcessor
specifier|public
class|class
name|OnCompletionProcessor
extends|extends
name|ServiceSupport
implements|implements
name|AsyncProcessor
implements|,
name|Traceable
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|OnCompletionProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|field|executorService
specifier|private
specifier|final
name|ExecutorService
name|executorService
decl_stmt|;
DECL|field|shutdownExecutorService
specifier|private
specifier|final
name|boolean
name|shutdownExecutorService
decl_stmt|;
DECL|field|onCompleteOnly
specifier|private
specifier|final
name|boolean
name|onCompleteOnly
decl_stmt|;
DECL|field|onFailureOnly
specifier|private
specifier|final
name|boolean
name|onFailureOnly
decl_stmt|;
DECL|field|onWhen
specifier|private
specifier|final
name|Predicate
name|onWhen
decl_stmt|;
DECL|field|useOriginalBody
specifier|private
specifier|final
name|boolean
name|useOriginalBody
decl_stmt|;
DECL|method|OnCompletionProcessor (CamelContext camelContext, Processor processor, ExecutorService executorService, boolean shutdownExecutorService, boolean onCompleteOnly, boolean onFailureOnly, Predicate onWhen, boolean useOriginalBody)
specifier|public
name|OnCompletionProcessor
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ExecutorService
name|executorService
parameter_list|,
name|boolean
name|shutdownExecutorService
parameter_list|,
name|boolean
name|onCompleteOnly
parameter_list|,
name|boolean
name|onFailureOnly
parameter_list|,
name|Predicate
name|onWhen
parameter_list|,
name|boolean
name|useOriginalBody
parameter_list|)
block|{
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
name|notNull
argument_list|(
name|processor
argument_list|,
literal|"processor"
argument_list|)
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
name|this
operator|.
name|executorService
operator|=
name|executorService
expr_stmt|;
name|this
operator|.
name|shutdownExecutorService
operator|=
name|shutdownExecutorService
expr_stmt|;
name|this
operator|.
name|onCompleteOnly
operator|=
name|onCompleteOnly
expr_stmt|;
name|this
operator|.
name|onFailureOnly
operator|=
name|onFailureOnly
expr_stmt|;
name|this
operator|.
name|onWhen
operator|=
name|onWhen
expr_stmt|;
name|this
operator|.
name|useOriginalBody
operator|=
name|useOriginalBody
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
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|processor
argument_list|)
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
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopAndShutdownService
argument_list|(
name|processor
argument_list|)
expr_stmt|;
if|if
condition|(
name|shutdownExecutorService
condition|)
block|{
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
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
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
comment|// register callback
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|addSynchronization
argument_list|(
operator|new
name|OnCompletionSynchronization
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|/**      * Processes the exchange by the processors      *      * @param processor the processor      * @param exchange the exchange      */
DECL|method|doProcess (Processor processor, Exchange exchange)
specifier|protected
specifier|static
name|void
name|doProcess
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Prepares the {@link Exchange} to send as onCompletion.      *      * @param exchange the current exchange      * @return the exchange to be routed in onComplete      */
DECL|method|prepareExchange (Exchange exchange)
specifier|protected
name|Exchange
name|prepareExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Exchange
name|answer
decl_stmt|;
comment|// for asynchronous routing we must use a copy as we dont want it
comment|// to cause side effects of the original exchange
comment|// (the original thread will run in parallel)
name|answer
operator|=
name|ExchangeHelper
operator|.
name|createCorrelatedCopy
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|.
name|hasOut
argument_list|()
condition|)
block|{
comment|// move OUT to IN (pipes and filters)
name|answer
operator|.
name|setIn
argument_list|(
name|answer
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setOut
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
comment|// set MEP to InOnly as this wire tap is a fire and forget
name|answer
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
if|if
condition|(
name|useOriginalBody
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using the original IN message instead of current"
argument_list|)
expr_stmt|;
name|Message
name|original
init|=
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|getOriginalInMessage
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setIn
argument_list|(
name|original
argument_list|)
expr_stmt|;
block|}
comment|// add a header flag to indicate its a on completion exchange
name|answer
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|ON_COMPLETION
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|class|OnCompletionSynchronization
specifier|private
specifier|final
class|class
name|OnCompletionSynchronization
extends|extends
name|SynchronizationAdapter
implements|implements
name|Ordered
block|{
DECL|method|getOrder ()
specifier|public
name|int
name|getOrder
parameter_list|()
block|{
comment|// we want to be last
return|return
name|Ordered
operator|.
name|LOWEST
return|;
block|}
annotation|@
name|Override
DECL|method|onComplete (final Exchange exchange)
specifier|public
name|void
name|onComplete
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|onFailureOnly
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|onWhen
operator|!=
literal|null
operator|&&
operator|!
name|onWhen
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
comment|// predicate did not match so do not route the onComplete
return|return;
block|}
comment|// must use a copy as we dont want it to cause side effects of the original exchange
specifier|final
name|Exchange
name|copy
init|=
name|prepareExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|executorService
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Exchange
argument_list|>
argument_list|()
block|{
specifier|public
name|Exchange
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Processing onComplete: {}"
argument_list|,
name|copy
argument_list|)
expr_stmt|;
name|doProcess
argument_list|(
name|processor
argument_list|,
name|copy
argument_list|)
expr_stmt|;
return|return
name|copy
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|onFailure (final Exchange exchange)
specifier|public
name|void
name|onFailure
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|onCompleteOnly
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|onWhen
operator|!=
literal|null
operator|&&
operator|!
name|onWhen
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
comment|// predicate did not match so do not route the onComplete
return|return;
block|}
comment|// must use a copy as we dont want it to cause side effects of the original exchange
specifier|final
name|Exchange
name|copy
init|=
name|prepareExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
comment|// must remove exception otherwise onFailure routing will fail as well
comment|// the caused exception is stored as a property (Exchange.EXCEPTION_CAUGHT) on the exchange
name|copy
operator|.
name|setException
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|executorService
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Exchange
argument_list|>
argument_list|()
block|{
specifier|public
name|Exchange
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Processing onFailure: {}"
argument_list|,
name|copy
argument_list|)
expr_stmt|;
name|doProcess
argument_list|(
name|processor
argument_list|,
name|copy
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
operator|!
name|onCompleteOnly
operator|&&
operator|!
name|onFailureOnly
condition|)
block|{
return|return
literal|"onCompleteOrFailure"
return|;
block|}
elseif|else
if|if
condition|(
name|onCompleteOnly
condition|)
block|{
return|return
literal|"onCompleteOnly"
return|;
block|}
else|else
block|{
return|return
literal|"onFailureOnly"
return|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"OnCompletionProcessor["
operator|+
name|processor
operator|+
literal|"]"
return|;
block|}
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
literal|"onCompletion"
return|;
block|}
block|}
end_class

end_unit

