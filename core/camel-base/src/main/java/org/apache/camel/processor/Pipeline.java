begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|spi
operator|.
name|IdAware
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
name|support
operator|.
name|AsyncProcessorSupport
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
name|support
operator|.
name|service
operator|.
name|ServiceHelper
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
comment|/**  * Creates a Pipeline pattern where the output of the previous step is sent as  * input to the next step, reusing the same message exchanges  */
end_comment

begin_class
DECL|class|Pipeline
specifier|public
class|class
name|Pipeline
extends|extends
name|AsyncProcessorSupport
implements|implements
name|Navigate
argument_list|<
name|Processor
argument_list|>
implements|,
name|Traceable
implements|,
name|IdAware
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|processors
specifier|private
name|List
argument_list|<
name|AsyncProcessor
argument_list|>
name|processors
decl_stmt|;
DECL|field|id
specifier|private
name|String
name|id
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
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|processors
operator|=
name|processors
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|AsyncProcessorConverterHelper
operator|::
name|convert
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
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
DECL|method|newInstance (final CamelContext camelContext, final Processor... processors)
specifier|public
specifier|static
name|Processor
name|newInstance
parameter_list|(
specifier|final
name|CamelContext
name|camelContext
parameter_list|,
specifier|final
name|Processor
modifier|...
name|processors
parameter_list|)
block|{
if|if
condition|(
name|processors
operator|==
literal|null
operator|||
name|processors
operator|.
name|length
operator|==
literal|0
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
name|length
operator|==
literal|1
condition|)
block|{
return|return
name|processors
index|[
literal|0
index|]
return|;
block|}
specifier|final
name|List
argument_list|<
name|Processor
argument_list|>
name|toBeProcessed
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|processors
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|Processor
name|processor
range|:
name|processors
control|)
block|{
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
name|toBeProcessed
operator|.
name|add
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|Pipeline
argument_list|(
name|camelContext
argument_list|,
name|toBeProcessed
argument_list|)
return|;
block|}
annotation|@
name|Override
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
name|exchange
operator|.
name|isTransacted
argument_list|()
condition|)
block|{
name|camelContext
operator|.
name|getReactiveExecutor
argument_list|()
operator|.
name|scheduleSync
argument_list|(
parameter_list|()
lambda|->
name|Pipeline
operator|.
name|this
operator|.
name|doProcess
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|processors
operator|.
name|iterator
argument_list|()
argument_list|,
literal|true
argument_list|)
argument_list|,
literal|"Step["
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|","
operator|+
name|Pipeline
operator|.
name|this
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|camelContext
operator|.
name|getReactiveExecutor
argument_list|()
operator|.
name|scheduleMain
argument_list|(
parameter_list|()
lambda|->
name|Pipeline
operator|.
name|this
operator|.
name|doProcess
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|processors
operator|.
name|iterator
argument_list|()
argument_list|,
literal|true
argument_list|)
argument_list|,
literal|"Step["
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|","
operator|+
name|Pipeline
operator|.
name|this
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|doProcess (Exchange exchange, AsyncCallback callback, Iterator<AsyncProcessor> processors, boolean first)
specifier|protected
name|void
name|doProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|Iterator
argument_list|<
name|AsyncProcessor
argument_list|>
name|processors
parameter_list|,
name|boolean
name|first
parameter_list|)
block|{
if|if
condition|(
name|continueRouting
argument_list|(
name|processors
argument_list|,
name|exchange
argument_list|)
operator|&&
operator|(
name|first
operator|||
name|continueProcessing
argument_list|(
name|exchange
argument_list|,
literal|"so breaking out of pipeline"
argument_list|,
name|log
argument_list|)
operator|)
condition|)
block|{
comment|// prepare for next run
name|ExchangeHelper
operator|.
name|prepareOutToIn
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// get the next processor
name|AsyncProcessor
name|processor
init|=
name|processors
operator|.
name|next
argument_list|()
decl_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|doneSync
lambda|->
name|camelContext
operator|.
name|getReactiveExecutor
argument_list|()
operator|.
name|schedule
argument_list|(
parameter_list|()
lambda|->
name|doProcess
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|processors
argument_list|,
literal|false
argument_list|)
argument_list|,
literal|"Step["
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|","
operator|+
name|Pipeline
operator|.
name|this
operator|+
literal|"]"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ExchangeHelper
operator|.
name|copyResults
argument_list|(
name|exchange
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// logging nextExchange as it contains the exchange that might have altered the payload and since
comment|// we are logging the completion if will be confusing if we log the original instead
comment|// we could also consider logging the original and the nextExchange then we have *before* and *after* snapshots
name|log
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
name|camelContext
operator|.
name|getReactiveExecutor
argument_list|()
operator|.
name|callback
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
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
name|log
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
return|return
literal|false
return|;
block|}
block|}
comment|// continue if there are more processors to route
name|boolean
name|answer
init|=
name|it
operator|.
name|hasNext
argument_list|()
decl_stmt|;
name|log
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
name|processors
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
name|processors
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
literal|"Pipeline["
operator|+
name|getProcessors
argument_list|()
operator|+
literal|"]"
return|;
block|}
DECL|method|getProcessors ()
specifier|public
name|List
argument_list|<
name|Processor
argument_list|>
name|getProcessors
parameter_list|()
block|{
return|return
operator|(
name|List
operator|)
name|processors
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
annotation|@
name|Override
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
annotation|@
name|Override
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|Override
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
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|processors
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|processors
operator|!=
literal|null
operator|&&
operator|!
name|processors
operator|.
name|isEmpty
argument_list|()
return|;
block|}
block|}
end_class

end_unit

