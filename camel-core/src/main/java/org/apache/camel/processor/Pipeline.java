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
name|util
operator|.
name|AsyncProcessorConverterHelper
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
name|processor
operator|.
name|PipelineHelper
operator|.
name|continueProcessing
import|;
end_import

begin_comment
comment|/**  * Creates a Pipeline pattern where the output of the previous step is sent as  * input to the next step, reusing the same message exchanges  *  * @version   */
end_comment

begin_class
DECL|class|Pipeline
specifier|public
class|class
name|Pipeline
extends|extends
name|MulticastProcessor
implements|implements
name|AsyncProcessor
implements|,
name|Traceable
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
name|Pipeline
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|Pipeline (CamelContext camelContext, Collection<Processor> processors)
specifier|public
name|Pipeline
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Collection
argument_list|<
name|Processor
argument_list|>
name|processors
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|,
name|processors
argument_list|)
expr_stmt|;
block|}
DECL|method|newInstance (CamelContext camelContext, List<Processor> processors)
specifier|public
specifier|static
name|Processor
name|newInstance
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
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
name|camelContext
argument_list|,
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
name|AsyncProcessor
name|async
init|=
name|AsyncProcessorConverterHelper
operator|.
name|convert
argument_list|(
name|processor
argument_list|)
decl_stmt|;
name|boolean
name|sync
init|=
name|process
argument_list|(
name|exchange
argument_list|,
name|nextExchange
argument_list|,
name|callback
argument_list|,
name|processors
argument_list|,
name|async
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
comment|// the remainder of the pipeline will be completed async
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
comment|// check for error if so we should break out
if|if
condition|(
operator|!
name|continueProcessing
argument_list|(
name|nextExchange
argument_list|,
literal|"so breaking out of pipeline"
argument_list|,
name|LOG
argument_list|)
condition|)
block|{
break|break;
block|}
block|}
comment|// logging nextExchange as it contains the exchange that might have altered the payload and since
comment|// we are logging the completion if will be confusing if we log the original instead
comment|// we could also consider logging the original and the nextExchange then we have *before* and *after* snapshots
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
name|nextExchange
argument_list|)
expr_stmt|;
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
DECL|method|process (final Exchange original, final Exchange exchange, final AsyncCallback callback, final Iterator<Processor> processors, final AsyncProcessor asyncProcessor)
specifier|private
name|boolean
name|process
parameter_list|(
specifier|final
name|Exchange
name|original
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|,
specifier|final
name|Iterator
argument_list|<
name|Processor
argument_list|>
name|processors
parameter_list|,
specifier|final
name|AsyncProcessor
name|asyncProcessor
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
comment|//boolean sync = AsyncProcessorHelper.process(asyncProcessor, exchange,
name|boolean
name|sync
init|=
name|asyncProcessor
operator|.
name|process
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
comment|// continue processing the pipeline asynchronously
name|Exchange
name|nextExchange
init|=
name|exchange
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
name|AsyncProcessor
name|processor
init|=
name|AsyncProcessorConverterHelper
operator|.
name|convert
argument_list|(
name|processors
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
comment|// check for error if so we should break out
if|if
condition|(
operator|!
name|continueProcessing
argument_list|(
name|nextExchange
argument_list|,
literal|"so breaking out of pipeline"
argument_list|,
name|LOG
argument_list|)
condition|)
block|{
break|break;
block|}
name|nextExchange
operator|=
name|createNextExchange
argument_list|(
name|nextExchange
argument_list|)
expr_stmt|;
name|doneSync
operator|=
name|process
argument_list|(
name|original
argument_list|,
name|nextExchange
argument_list|,
name|callback
argument_list|,
name|processors
argument_list|,
name|processor
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
return|return;
block|}
block|}
name|ExchangeHelper
operator|.
name|copyResults
argument_list|(
name|original
argument_list|,
name|nextExchange
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing complete for exchangeId: {}>>> {}"
argument_list|,
name|original
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|original
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
decl_stmt|;
comment|// now lets set the input of the next exchange to the output of the
comment|// previous message if it is not null
if|if
condition|(
name|answer
operator|.
name|hasOut
argument_list|()
condition|)
block|{
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
name|boolean
name|answer
init|=
literal|true
decl_stmt|;
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
literal|"ExchangeId: {} is marked to stop routing: {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|answer
operator|=
literal|false
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// continue if there are more processors to route
name|answer
operator|=
name|it
operator|.
name|hasNext
argument_list|()
expr_stmt|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"ExchangeId: {} should continue routing: {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
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
literal|"Pipeline["
operator|+
name|getProcessors
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
literal|"pipeline"
return|;
block|}
block|}
end_class

end_unit

