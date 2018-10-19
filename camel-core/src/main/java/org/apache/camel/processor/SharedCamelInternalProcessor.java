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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|RejectedExecutionException
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
name|Service
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
name|spi
operator|.
name|AsyncProcessorAwaitManager
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
name|spi
operator|.
name|RoutePolicy
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
name|spi
operator|.
name|Transformer
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
name|spi
operator|.
name|UnitOfWork
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
name|OrderedComparator
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
name|ReactiveHelper
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

begin_comment
comment|/**  * A Shared (thread safe) internal {@link Processor} that Camel routing engine used during routing for cross cutting functionality such as:  *<ul>  *<li>Execute {@link UnitOfWork}</li>  *<li>Keeping track which route currently is being routed</li>  *<li>Execute {@link RoutePolicy}</li>  *<li>Gather JMX performance statics</li>  *<li>Tracing</li>  *<li>Debugging</li>  *<li>Message History</li>  *<li>Stream Caching</li>  *<li>{@link Transformer}</li>  *</ul>  * ... and more.  *<p/>  * This implementation executes this cross cutting functionality as a {@link CamelInternalProcessorAdvice} advice (before and after advice)  * by executing the {@link CamelInternalProcessorAdvice#before(Exchange)} and  * {@link CamelInternalProcessorAdvice#after(Exchange, Object)} callbacks in correct order during routing.  * This reduces number of stack frames needed during routing, and reduce the number of lines in stacktraces, as well  * makes debugging the routing engine easier for end users.  *<p/>  *<b>Debugging tips:</b> Camel end users whom want to debug their Camel applications with the Camel source code, then make sure to  * read the source code of this class about the debugging tips, which you can find in the  * {@link #process(Exchange, AsyncCallback, AsyncProcessor, Processor)} method.  *<p/>  * The added advices can implement {@link Ordered} to control in which order the advices are executed.  */
end_comment

begin_class
DECL|class|SharedCamelInternalProcessor
specifier|public
class|class
name|SharedCamelInternalProcessor
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
name|SharedCamelInternalProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|advices
specifier|private
specifier|final
name|List
argument_list|<
name|CamelInternalProcessorAdvice
argument_list|>
name|advices
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|SharedCamelInternalProcessor (CamelInternalProcessorAdvice... advices)
specifier|public
name|SharedCamelInternalProcessor
parameter_list|(
name|CamelInternalProcessorAdvice
modifier|...
name|advices
parameter_list|)
block|{
if|if
condition|(
name|advices
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|advices
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|advices
argument_list|)
argument_list|)
expr_stmt|;
comment|// ensure advices are sorted so they are in the order we want
name|this
operator|.
name|advices
operator|.
name|sort
argument_list|(
name|OrderedComparator
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Synchronous API      */
DECL|method|process (Exchange exchange, AsyncProcessor processor, Processor resultProcessor)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncProcessor
name|processor
parameter_list|,
name|Processor
name|resultProcessor
parameter_list|)
block|{
specifier|final
name|AsyncProcessorAwaitManager
name|awaitManager
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getAsyncProcessorAwaitManager
argument_list|()
decl_stmt|;
name|awaitManager
operator|.
name|process
argument_list|(
operator|new
name|AsyncProcessor
argument_list|()
block|{
annotation|@
name|Override
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
return|return
name|SharedCamelInternalProcessor
operator|.
name|this
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|processor
argument_list|,
name|resultProcessor
argument_list|)
return|;
block|}
annotation|@
name|Override
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
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
block|}
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|/**      * Asynchronous API      */
DECL|method|process (Exchange exchange, AsyncCallback ocallback, AsyncProcessor processor, Processor resultProcessor)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|ocallback
parameter_list|,
name|AsyncProcessor
name|processor
parameter_list|,
name|Processor
name|resultProcessor
parameter_list|)
block|{
comment|// ----------------------------------------------------------
comment|// CAMEL END USER - READ ME FOR DEBUGGING TIPS
comment|// ----------------------------------------------------------
comment|// If you want to debug the Camel routing engine, then there is a lot of internal functionality
comment|// the routing engine executes during routing messages. You can skip debugging this internal
comment|// functionality and instead debug where the routing engine continues routing to the next node
comment|// in the routes. The CamelInternalProcessor is a vital part of the routing engine, as its
comment|// being used in between the nodes. As an end user you can just debug the code in this class
comment|// in between the:
comment|//   CAMEL END USER - DEBUG ME HERE +++ START +++
comment|//   CAMEL END USER - DEBUG ME HERE +++ END +++
comment|// you can see in the code below.
comment|// ----------------------------------------------------------
if|if
condition|(
name|processor
operator|==
literal|null
operator|||
operator|!
name|continueProcessing
argument_list|(
name|exchange
argument_list|,
name|processor
argument_list|)
condition|)
block|{
comment|// no processor or we should not continue then we are done
name|ocallback
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
comment|// optimise to use object array for states
specifier|final
name|Object
index|[]
name|states
init|=
operator|new
name|Object
index|[
name|advices
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
comment|// optimise for loop using index access to avoid creating iterator object
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|advices
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|CamelInternalProcessorAdvice
name|task
init|=
name|advices
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
try|try
block|{
name|Object
name|state
init|=
name|task
operator|.
name|before
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|states
index|[
name|i
index|]
operator|=
name|state
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
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
name|ocallback
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
block|}
comment|// create internal callback which will execute the advices in reverse order when done
name|AsyncCallback
name|callback
init|=
operator|new
name|InternalCallback
argument_list|(
name|states
argument_list|,
name|exchange
argument_list|,
name|ocallback
argument_list|,
name|resultProcessor
argument_list|)
decl_stmt|;
comment|// UNIT_OF_WORK_PROCESS_SYNC is @deprecated and we should remove it from Camel 3.0
name|Object
name|synchronous
init|=
name|exchange
operator|.
name|removeProperty
argument_list|(
name|Exchange
operator|.
name|UNIT_OF_WORK_PROCESS_SYNC
argument_list|)
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|isTransacted
argument_list|()
operator|||
name|synchronous
operator|!=
literal|null
condition|)
block|{
comment|// must be synchronized for transacted exchanges
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|isTransacted
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Transacted Exchange must be routed synchronously for exchangeId: {} -> {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Synchronous UnitOfWork Exchange must be routed synchronously for exchangeId: {} -> {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
comment|// ----------------------------------------------------------
comment|// CAMEL END USER - DEBUG ME HERE +++ START +++
comment|// ----------------------------------------------------------
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
name|Throwable
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
comment|// ----------------------------------------------------------
comment|// CAMEL END USER - DEBUG ME HERE +++ END +++
comment|// ----------------------------------------------------------
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
else|else
block|{
specifier|final
name|UnitOfWork
name|uow
init|=
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
decl_stmt|;
comment|// allow unit of work to wrap callback in case it need to do some special work
comment|// for example the MDCUnitOfWork
name|AsyncCallback
name|async
init|=
name|callback
decl_stmt|;
if|if
condition|(
name|uow
operator|!=
literal|null
condition|)
block|{
name|async
operator|=
name|uow
operator|.
name|beforeProcess
argument_list|(
name|processor
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
comment|// ----------------------------------------------------------
comment|// CAMEL END USER - DEBUG ME HERE +++ START +++
comment|// ----------------------------------------------------------
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing exchange for exchangeId: {} -> {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
name|boolean
name|sync
init|=
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|async
argument_list|)
decl_stmt|;
comment|// ----------------------------------------------------------
comment|// CAMEL END USER - DEBUG ME HERE +++ END +++
comment|// ----------------------------------------------------------
name|ReactiveHelper
operator|.
name|scheduleLast
argument_list|(
parameter_list|()
lambda|->
block|{
comment|// execute any after processor work (in current thread, not in the callback)
if|if
condition|(
name|uow
operator|!=
literal|null
condition|)
block|{
name|uow
operator|.
name|afterProcess
argument_list|(
name|processor
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|,
name|sync
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Exchange processed and is continued routed asynchronously for exchangeId: {} -> {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
literal|"SharedCamelInternalProcessor - UnitOfWork - afterProcess - "
operator|+
name|processor
operator|+
literal|" - "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|sync
return|;
block|}
block|}
comment|/**      * Internal callback that executes the after advices.      */
DECL|class|InternalCallback
specifier|private
specifier|final
class|class
name|InternalCallback
implements|implements
name|AsyncCallback
block|{
DECL|field|states
specifier|private
specifier|final
name|Object
index|[]
name|states
decl_stmt|;
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|callback
specifier|private
specifier|final
name|AsyncCallback
name|callback
decl_stmt|;
DECL|field|resultProcessor
specifier|private
specifier|final
name|Processor
name|resultProcessor
decl_stmt|;
DECL|method|InternalCallback (Object[] states, Exchange exchange, AsyncCallback callback, Processor resultProcessor)
specifier|private
name|InternalCallback
parameter_list|(
name|Object
index|[]
name|states
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|Processor
name|resultProcessor
parameter_list|)
block|{
name|this
operator|.
name|states
operator|=
name|states
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
name|this
operator|.
name|resultProcessor
operator|=
name|resultProcessor
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|done (boolean doneSync)
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
comment|// NOTE: if you are debugging Camel routes, then all the code in the for loop below is internal only
comment|// so you can step straight to the finally block and invoke the callback
if|if
condition|(
name|resultProcessor
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|resultProcessor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
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
comment|// we should call after in reverse order
try|try
block|{
for|for
control|(
name|int
name|i
init|=
name|advices
operator|.
name|size
argument_list|()
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|CamelInternalProcessorAdvice
name|task
init|=
name|advices
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Object
name|state
init|=
name|states
index|[
name|i
index|]
decl_stmt|;
try|try
block|{
name|task
operator|.
name|after
argument_list|(
name|exchange
argument_list|,
name|state
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
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
comment|// allow all advices to complete even if there was an exception
block|}
block|}
block|}
finally|finally
block|{
comment|// ----------------------------------------------------------
comment|// CAMEL END USER - DEBUG ME HERE +++ START +++
comment|// ----------------------------------------------------------
comment|// callback must be called
name|ReactiveHelper
operator|.
name|callback
argument_list|(
name|callback
argument_list|)
expr_stmt|;
comment|// ----------------------------------------------------------
comment|// CAMEL END USER - DEBUG ME HERE +++ END +++
comment|// ----------------------------------------------------------
block|}
block|}
block|}
comment|/**      * Strategy to determine if we should continue processing the {@link Exchange}.      */
DECL|method|continueProcessing (Exchange exchange, AsyncProcessor processor)
specifier|protected
name|boolean
name|continueProcessing
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncProcessor
name|processor
parameter_list|)
block|{
name|Object
name|stop
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|ROUTE_STOP
argument_list|)
decl_stmt|;
if|if
condition|(
name|stop
operator|!=
literal|null
condition|)
block|{
name|boolean
name|doStop
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Boolean
operator|.
name|class
argument_list|,
name|stop
argument_list|)
decl_stmt|;
if|if
condition|(
name|doStop
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Exchange is marked to stop routing: {}"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
comment|// determine if we can still run, or the camel context is forcing a shutdown
if|if
condition|(
name|processor
operator|instanceof
name|Service
condition|)
block|{
name|boolean
name|forceShutdown
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|forceShutdown
argument_list|(
operator|(
name|Service
operator|)
name|processor
argument_list|)
decl_stmt|;
if|if
condition|(
name|forceShutdown
condition|)
block|{
name|String
name|msg
init|=
literal|"Run not allowed as ShutdownStrategy is forcing shutting down, will reject executing exchange: "
operator|+
name|exchange
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
name|msg
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|RejectedExecutionException
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
block|}
comment|// yes we can continue
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

