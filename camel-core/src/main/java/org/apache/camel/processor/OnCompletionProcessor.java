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
name|impl
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
name|impl
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorServiceHelper
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|OnCompletionProcessor
specifier|public
class|class
name|OnCompletionProcessor
extends|extends
name|ServiceSupport
implements|implements
name|Processor
implements|,
name|Traceable
block|{
DECL|field|DEFAULT_THREADPOOL_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_THREADPOOL_SIZE
init|=
literal|10
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|OnCompletionProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
DECL|field|processor
specifier|private
name|Processor
name|processor
decl_stmt|;
DECL|field|onCompleteOnly
specifier|private
name|boolean
name|onCompleteOnly
decl_stmt|;
DECL|field|onFailureOnly
specifier|private
name|boolean
name|onFailureOnly
decl_stmt|;
DECL|field|onWhen
specifier|private
name|Predicate
name|onWhen
decl_stmt|;
DECL|method|OnCompletionProcessor (Processor processor, boolean onCompleteOnly, boolean onFailureOnly, Predicate onWhen)
specifier|public
name|OnCompletionProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|boolean
name|onCompleteOnly
parameter_list|,
name|boolean
name|onFailureOnly
parameter_list|,
name|Predicate
name|onWhen
parameter_list|)
block|{
name|this
operator|.
name|processor
operator|=
name|processor
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
block|}
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
if|if
condition|(
name|executorService
operator|!=
literal|null
condition|)
block|{
name|executorService
operator|.
name|shutdown
argument_list|()
expr_stmt|;
comment|// must null it so we can restart
name|executorService
operator|=
literal|null
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|processor
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
if|if
condition|(
name|processor
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// register callback
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|addSynchronization
argument_list|(
operator|new
name|SynchronizationAdapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|(
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
name|getExecutorService
argument_list|()
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
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Processing onComplete: "
operator|+
name|copy
argument_list|)
expr_stmt|;
block|}
name|processor
operator|.
name|process
argument_list|(
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
specifier|public
name|void
name|onFailure
parameter_list|(
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
comment|// must remove exception otherwise onFaulure routing will fail as well
comment|// the caused exception is stored as a property (Exchange.EXCEPTION_CAUGHT) on the exchange
name|copy
operator|.
name|setException
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|getExecutorService
argument_list|()
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
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Processing onFailure: "
operator|+
name|copy
argument_list|)
expr_stmt|;
block|}
name|processor
operator|.
name|process
argument_list|(
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
annotation|@
name|Override
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
argument_list|)
expr_stmt|;
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
comment|// must use a copy as we dont want it to cause side effects of the original exchange
specifier|final
name|Exchange
name|copy
init|=
name|ExchangeHelper
operator|.
name|createCorrelatedCopy
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
decl_stmt|;
comment|// set MEP to InOnly as this wire tap is a fire and forget
name|copy
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
comment|// add a header flag to indicate its a on completion exchange
name|copy
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
name|copy
return|;
block|}
DECL|method|getExecutorService ()
specifier|public
name|ExecutorService
name|getExecutorService
parameter_list|()
block|{
if|if
condition|(
name|executorService
operator|==
literal|null
condition|)
block|{
name|executorService
operator|=
name|createExecutorService
argument_list|()
expr_stmt|;
block|}
return|return
name|executorService
return|;
block|}
DECL|method|createExecutorService ()
specifier|private
name|ExecutorService
name|createExecutorService
parameter_list|()
block|{
return|return
name|ExecutorServiceHelper
operator|.
name|newScheduledThreadPool
argument_list|(
name|DEFAULT_THREADPOOL_SIZE
argument_list|,
name|this
operator|.
name|toString
argument_list|()
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|setExecutorService (ExecutorService executorService)
specifier|public
name|void
name|setExecutorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
name|this
operator|.
name|executorService
operator|=
name|executorService
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
literal|"OnCompletion"
return|;
block|}
block|}
end_class

end_unit

