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
comment|/**  * Creates a Pipeline pattern where the output of the previous step is sent as  * input to the next step, reusing the same message exchanges  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|Pipeline
specifier|public
class|class
name|Pipeline
extends|extends
name|MulticastProcessor
implements|implements
name|Processor
block|{
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
name|Pipeline
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|Pipeline (Collection<Processor> processors)
specifier|public
name|Pipeline
parameter_list|(
name|Collection
argument_list|<
name|Processor
argument_list|>
name|processors
parameter_list|)
block|{
name|super
argument_list|(
name|processors
argument_list|)
expr_stmt|;
block|}
DECL|method|newInstance (List<Processor> processors)
specifier|public
specifier|static
name|Processor
name|newInstance
parameter_list|(
name|List
argument_list|<
name|Processor
argument_list|>
name|processors
parameter_list|)
block|{
if|if
condition|(
name|processors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|processors
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|processors
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
return|return
operator|new
name|Pipeline
argument_list|(
name|processors
argument_list|)
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
name|Iterator
argument_list|<
name|Processor
argument_list|>
name|processors
init|=
name|getProcessors
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Exchange
name|nextExchange
init|=
name|exchange
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
while|while
condition|(
name|continueRouting
argument_list|(
name|processors
argument_list|,
name|nextExchange
argument_list|)
condition|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
comment|// prepare for next run
name|nextExchange
operator|=
name|createNextExchange
argument_list|(
name|nextExchange
argument_list|)
expr_stmt|;
block|}
comment|// get the next processor
name|Processor
name|processor
init|=
name|processors
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// process the next exchange
try|try
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
comment|// this does the actual processing so log at trace level
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing exchangeId: "
operator|+
name|nextExchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|">>> "
operator|+
name|nextExchange
argument_list|)
expr_stmt|;
block|}
name|processor
operator|.
name|process
argument_list|(
name|nextExchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|nextExchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|// check for error if so we should break out
name|boolean
name|exceptionHandled
init|=
name|hasExceptionBeenHandled
argument_list|(
name|nextExchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|nextExchange
operator|.
name|isFailed
argument_list|()
operator|||
name|exceptionHandled
condition|)
block|{
comment|// The Exchange.EXCEPTION_HANDLED property is only set if satisfactory handling was done
comment|// by the error handler. It's still an exception, the exchange still failed.
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
literal|"Message exchange has failed so breaking out of pipeline: "
operator|+
name|nextExchange
operator|+
literal|" exception: "
operator|+
name|nextExchange
operator|.
name|getException
argument_list|()
operator|+
literal|" fault: "
operator|+
operator|(
name|nextExchange
operator|.
name|hasFault
argument_list|()
condition|?
name|nextExchange
operator|.
name|getFault
argument_list|()
else|:
literal|null
operator|)
operator|+
operator|(
name|exceptionHandled
condition|?
literal|" handled by the error handler"
else|:
literal|""
operator|)
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
block|}
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
comment|// logging nextExchange as it contains the exchange that might have altered the payload and since
comment|// we are logging the completion if will be confusing if we log the original instead
comment|// we could also consider logging the original and the nextExchange then we have *before* and *after* snapshots
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing compelete for exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|">>> "
operator|+
name|nextExchange
argument_list|)
expr_stmt|;
block|}
comment|// copy results back to the original exchange
name|ExchangeHelper
operator|.
name|copyResults
argument_list|(
name|exchange
argument_list|,
name|nextExchange
argument_list|)
expr_stmt|;
block|}
DECL|method|hasExceptionBeenHandled (Exchange nextExchange)
specifier|private
specifier|static
name|boolean
name|hasExceptionBeenHandled
parameter_list|(
name|Exchange
name|nextExchange
parameter_list|)
block|{
return|return
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|nextExchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_HANDLED
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Strategy method to create the next exchange from the previous exchange.      *<p/>      * Remember to copy the original exchange id otherwise correlation of ids in the log is a problem      *      * @param previousExchange the previous exchange      * @return a new exchange      */
DECL|method|createNextExchange (Exchange previousExchange)
specifier|protected
name|Exchange
name|createNextExchange
parameter_list|(
name|Exchange
name|previousExchange
parameter_list|)
block|{
name|Exchange
name|answer
init|=
name|previousExchange
operator|.
name|newInstance
argument_list|()
decl_stmt|;
comment|// we must use the same id as this is a snapshot strategy where Camel copies a snapshot
comment|// before processing the next step in the pipeline, so we have a snapshot of the exchange
comment|// just before. This snapshot is used if Camel should do redeliveries (re try) using
comment|// DeadLetterChannel. That is why it's important the id is the same, as it is the *same*
comment|// exchange being routed.
name|answer
operator|.
name|setExchangeId
argument_list|(
name|previousExchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|getProperties
argument_list|()
operator|.
name|putAll
argument_list|(
name|previousExchange
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
comment|// now lets set the input of the next exchange to the output of the
comment|// previous message if it is not null
name|Message
name|in
init|=
name|answer
operator|.
name|getIn
argument_list|()
decl_stmt|;
if|if
condition|(
name|previousExchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|in
operator|.
name|copyFrom
argument_list|(
name|previousExchange
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|in
operator|.
name|copyFrom
argument_list|(
name|previousExchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|continueRouting (Iterator<Processor> it, Exchange exchange)
specifier|protected
name|boolean
name|continueRouting
parameter_list|(
name|Iterator
argument_list|<
name|Processor
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
literal|"Exchange is marked to stop routing: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
else|else
block|{
return|return
literal|true
return|;
block|}
block|}
else|else
block|{
return|return
name|it
operator|.
name|hasNext
argument_list|()
return|;
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
literal|"Pipeline"
operator|+
name|getProcessors
argument_list|()
return|;
block|}
block|}
end_class

end_unit

