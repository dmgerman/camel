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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Navigate
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
name|converter
operator|.
name|AsyncProcessorTypeConverter
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

begin_comment
comment|/**  * Implements try/catch/finally type processing  *  * @version   */
end_comment

begin_class
DECL|class|TryProcessor
specifier|public
class|class
name|TryProcessor
extends|extends
name|ServiceSupport
implements|implements
name|AsyncProcessor
implements|,
name|Navigate
argument_list|<
name|Processor
argument_list|>
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
name|TryProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|tryProcessor
specifier|protected
specifier|final
name|AsyncProcessor
name|tryProcessor
decl_stmt|;
DECL|field|catchProcessor
specifier|protected
specifier|final
name|DoCatchProcessor
name|catchProcessor
decl_stmt|;
DECL|field|finallyProcessor
specifier|protected
specifier|final
name|DoFinallyProcessor
name|finallyProcessor
decl_stmt|;
DECL|field|processors
specifier|private
name|List
argument_list|<
name|AsyncProcessor
argument_list|>
name|processors
decl_stmt|;
DECL|method|TryProcessor (Processor tryProcessor, List<CatchProcessor> catchClauses, Processor finallyProcessor)
specifier|public
name|TryProcessor
parameter_list|(
name|Processor
name|tryProcessor
parameter_list|,
name|List
argument_list|<
name|CatchProcessor
argument_list|>
name|catchClauses
parameter_list|,
name|Processor
name|finallyProcessor
parameter_list|)
block|{
name|this
operator|.
name|tryProcessor
operator|=
name|AsyncProcessorTypeConverter
operator|.
name|convert
argument_list|(
name|tryProcessor
argument_list|)
expr_stmt|;
name|this
operator|.
name|catchProcessor
operator|=
operator|new
name|DoCatchProcessor
argument_list|(
name|catchClauses
argument_list|)
expr_stmt|;
name|this
operator|.
name|finallyProcessor
operator|=
operator|new
name|DoFinallyProcessor
argument_list|(
name|finallyProcessor
argument_list|)
expr_stmt|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|String
name|finallyText
init|=
operator|(
name|finallyProcessor
operator|==
literal|null
operator|)
condition|?
literal|""
else|:
literal|" Finally {"
operator|+
name|finallyProcessor
operator|+
literal|"}"
decl_stmt|;
return|return
literal|"Try {"
operator|+
name|tryProcessor
operator|+
literal|"} "
operator|+
operator|(
name|catchProcessor
operator|!=
literal|null
condition|?
name|catchProcessor
else|:
literal|""
operator|)
operator|+
name|finallyText
return|;
block|}
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
literal|"doTry"
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
name|Iterator
argument_list|<
name|AsyncProcessor
argument_list|>
name|processors
init|=
name|getProcessors
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|continueRouting
argument_list|(
name|processors
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|ExchangeHelper
operator|.
name|prepareOutToIn
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// process the next processor
name|AsyncProcessor
name|processor
init|=
name|processors
operator|.
name|next
argument_list|()
decl_stmt|;
name|boolean
name|sync
init|=
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|processor
argument_list|,
name|processors
argument_list|)
decl_stmt|;
comment|// continue as long its being processed synchronously
if|if
condition|(
operator|!
name|sync
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing exchangeId: {} is continued being processed asynchronously"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
comment|// the remainder of the try .. catch .. finally will be completed async
comment|// so we break out now, then the callback will be invoked which then continue routing from where we left here
return|return
literal|false
return|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing exchangeId: {} is continued being processed synchronously"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ExchangeHelper
operator|.
name|prepareOutToIn
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing complete for exchangeId: {}>>> {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
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
DECL|method|process (final Exchange exchange, final AsyncCallback callback, final AsyncProcessor processor, final Iterator<AsyncProcessor> processors)
specifier|protected
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
parameter_list|,
specifier|final
name|AsyncProcessor
name|processor
parameter_list|,
specifier|final
name|Iterator
argument_list|<
name|AsyncProcessor
argument_list|>
name|processors
parameter_list|)
block|{
comment|// this does the actual processing so log at trace level
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing exchangeId: {}>>> {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// implement asynchronous routing logic in callback so we can have the callback being
comment|// triggered and then continue routing where we left
name|boolean
name|sync
init|=
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|processor
argument_list|,
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
comment|// we only have to handle async completion of the pipeline
if|if
condition|(
name|doneSync
condition|)
block|{
return|return;
block|}
comment|// continue processing the try .. catch .. finally asynchronously
while|while
condition|(
name|continueRouting
argument_list|(
name|processors
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|ExchangeHelper
operator|.
name|prepareOutToIn
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// process the next processor
name|AsyncProcessor
name|processor
init|=
name|processors
operator|.
name|next
argument_list|()
decl_stmt|;
name|doneSync
operator|=
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|processor
argument_list|,
name|processors
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|doneSync
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing exchangeId: {} is continued being processed asynchronously"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
comment|// the remainder of the try .. catch .. finally will be completed async
comment|// so we break out now, then the callback will be invoked which then continue routing from where we left here
return|return;
block|}
block|}
name|ExchangeHelper
operator|.
name|prepareOutToIn
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing complete for exchangeId: {}>>> {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|sync
return|;
block|}
DECL|method|getProcessors ()
specifier|protected
name|Collection
argument_list|<
name|AsyncProcessor
argument_list|>
name|getProcessors
parameter_list|()
block|{
return|return
name|processors
return|;
block|}
DECL|method|continueRouting (Iterator<AsyncProcessor> it, Exchange exchange)
specifier|protected
name|boolean
name|continueRouting
parameter_list|(
name|Iterator
argument_list|<
name|AsyncProcessor
argument_list|>
name|it
parameter_list|,
name|Exchange
name|exchange
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
comment|// continue if there are more processors to route
return|return
name|it
operator|.
name|hasNext
argument_list|()
return|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|processors
operator|=
operator|new
name|ArrayList
argument_list|<
name|AsyncProcessor
argument_list|>
argument_list|()
expr_stmt|;
name|processors
operator|.
name|add
argument_list|(
name|tryProcessor
argument_list|)
expr_stmt|;
name|processors
operator|.
name|add
argument_list|(
name|catchProcessor
argument_list|)
expr_stmt|;
name|processors
operator|.
name|add
argument_list|(
name|finallyProcessor
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|tryProcessor
argument_list|,
name|catchProcessor
argument_list|,
name|finallyProcessor
argument_list|)
expr_stmt|;
block|}
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
name|stopServices
argument_list|(
name|finallyProcessor
argument_list|,
name|catchProcessor
argument_list|,
name|tryProcessor
argument_list|)
expr_stmt|;
name|processors
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|next ()
specifier|public
name|List
argument_list|<
name|Processor
argument_list|>
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasNext
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|Processor
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Processor
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|tryProcessor
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|tryProcessor
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|catchProcessor
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|catchProcessor
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|finallyProcessor
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|finallyProcessor
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|tryProcessor
operator|!=
literal|null
return|;
block|}
comment|/**      * Processor to handle do catch supporting asynchronous routing engine      */
DECL|class|DoCatchProcessor
specifier|private
specifier|final
class|class
name|DoCatchProcessor
extends|extends
name|ServiceSupport
implements|implements
name|AsyncProcessor
implements|,
name|Navigate
argument_list|<
name|Processor
argument_list|>
implements|,
name|Traceable
block|{
DECL|field|catchClauses
specifier|private
specifier|final
name|List
argument_list|<
name|CatchProcessor
argument_list|>
name|catchClauses
decl_stmt|;
DECL|method|DoCatchProcessor (List<CatchProcessor> catchClauses)
specifier|private
name|DoCatchProcessor
parameter_list|(
name|List
argument_list|<
name|CatchProcessor
argument_list|>
name|catchClauses
parameter_list|)
block|{
name|this
operator|.
name|catchClauses
operator|=
name|catchClauses
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
name|Exception
name|e
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|catchClauses
operator|==
literal|null
operator|||
name|e
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// find a catch clause to use
name|CatchProcessor
name|processor
init|=
literal|null
decl_stmt|;
for|for
control|(
name|CatchProcessor
name|catchClause
range|:
name|catchClauses
control|)
block|{
name|Throwable
name|caught
init|=
name|catchClause
operator|.
name|catches
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
decl_stmt|;
if|if
condition|(
name|caught
operator|!=
literal|null
condition|)
block|{
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
literal|"This TryProcessor catches the exception: {} caused by: {}"
argument_list|,
name|caught
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|processor
operator|=
name|catchClause
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
comment|// create the handle processor which performs the actual logic
comment|// this processor just lookup the right catch clause to use and then let the
comment|// HandleDoCatchProcessor do all the hard work (separate of concerns)
name|HandleDoCatchProcessor
name|cool
init|=
operator|new
name|HandleDoCatchProcessor
argument_list|(
name|processor
argument_list|)
decl_stmt|;
return|return
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|cool
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
else|else
block|{
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
literal|"This TryProcessor does not catch the exception: {} caused by: {}"
argument_list|,
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
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
name|catchClauses
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
name|stopServices
argument_list|(
name|catchClauses
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
return|return
literal|"Catches{"
operator|+
name|catchClauses
operator|+
literal|"}"
return|;
block|}
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
literal|"doCatch"
return|;
block|}
DECL|method|next ()
specifier|public
name|List
argument_list|<
name|Processor
argument_list|>
name|next
parameter_list|()
block|{
name|List
argument_list|<
name|Processor
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Processor
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|catchProcessor
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|addAll
argument_list|(
name|catchClauses
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|catchClauses
operator|!=
literal|null
operator|&&
name|catchClauses
operator|.
name|size
argument_list|()
operator|>
literal|0
return|;
block|}
block|}
comment|/**      * Processor to handle do finally supporting asynchronous routing engine      */
DECL|class|DoFinallyProcessor
specifier|private
specifier|final
class|class
name|DoFinallyProcessor
extends|extends
name|DelegateAsyncProcessor
implements|implements
name|Traceable
block|{
DECL|method|DoFinallyProcessor (Processor processor)
specifier|private
name|DoFinallyProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|processNext (final Exchange exchange, final AsyncCallback callback)
specifier|protected
name|boolean
name|processNext
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
comment|// clear exception so finally block can be executed
specifier|final
name|Exception
name|e
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// store the last to endpoint as the failure endpoint
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_ENDPOINT
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|)
argument_list|)
expr_stmt|;
name|boolean
name|sync
init|=
name|super
operator|.
name|processNext
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
comment|// we only have to handle async completion of the pipeline
if|if
condition|(
name|doneSync
condition|)
block|{
return|return;
block|}
comment|// set exception back on exchange
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
comment|// signal callback to continue routing async
name|ExchangeHelper
operator|.
name|prepareOutToIn
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing complete for exchangeId: {}>>> {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|sync
condition|)
block|{
comment|// set exception back on exchange
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sync
return|;
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
literal|"Finally{"
operator|+
name|getProcessor
argument_list|()
operator|+
literal|"}"
return|;
block|}
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
literal|"doFinally"
return|;
block|}
block|}
comment|/**      * Processor to handle do catch supporting asynchronous routing engine      */
DECL|class|HandleDoCatchProcessor
specifier|private
specifier|final
class|class
name|HandleDoCatchProcessor
extends|extends
name|DelegateAsyncProcessor
block|{
DECL|field|catchClause
specifier|private
specifier|final
name|CatchProcessor
name|catchClause
decl_stmt|;
DECL|method|HandleDoCatchProcessor (CatchProcessor processor)
specifier|private
name|HandleDoCatchProcessor
parameter_list|(
name|CatchProcessor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|catchClause
operator|=
name|processor
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|processNext (final Exchange exchange, final AsyncCallback callback)
specifier|protected
name|boolean
name|processNext
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
specifier|final
name|Exception
name|caught
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|caught
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// store the last to endpoint as the failure endpoint
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_ENDPOINT
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|)
argument_list|)
expr_stmt|;
comment|// give the rest of the pipeline another chance
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|caught
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// is the exception handled by the catch clause
specifier|final
name|Boolean
name|handled
init|=
name|catchClause
operator|.
name|handles
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
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
literal|"The exception is handled: {} for the exception: {} caused by: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|handled
block|,
name|caught
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
block|,
name|caught
operator|.
name|getMessage
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
name|boolean
name|sync
init|=
name|super
operator|.
name|processNext
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
comment|// we only have to handle async completion of the pipeline
if|if
condition|(
name|doneSync
condition|)
block|{
return|return;
block|}
if|if
condition|(
operator|!
name|handled
condition|)
block|{
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
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|Exception
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// signal callback to continue routing async
name|ExchangeHelper
operator|.
name|prepareOutToIn
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|sync
condition|)
block|{
comment|// set exception back on exchange
if|if
condition|(
operator|!
name|handled
condition|)
block|{
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
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|Exception
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|sync
return|;
block|}
block|}
block|}
end_class

end_unit

