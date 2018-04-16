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
implements|,
name|IdAware
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
name|TryProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|id
specifier|protected
name|String
name|id
decl_stmt|;
DECL|field|tryProcessor
specifier|protected
specifier|final
name|Processor
name|tryProcessor
decl_stmt|;
DECL|field|catchClauses
specifier|protected
specifier|final
name|List
argument_list|<
name|Processor
argument_list|>
name|catchClauses
decl_stmt|;
DECL|field|finallyProcessor
specifier|protected
specifier|final
name|Processor
name|finallyProcessor
decl_stmt|;
DECL|method|TryProcessor (Processor tryProcessor, List<Processor> catchClauses, Processor finallyProcessor)
specifier|public
name|TryProcessor
parameter_list|(
name|Processor
name|tryProcessor
parameter_list|,
name|List
argument_list|<
name|Processor
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
name|tryProcessor
expr_stmt|;
name|this
operator|.
name|catchClauses
operator|=
name|catchClauses
expr_stmt|;
name|this
operator|.
name|finallyProcessor
operator|=
name|finallyProcessor
expr_stmt|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|String
name|catchText
init|=
name|catchClauses
operator|==
literal|null
operator|||
name|catchClauses
operator|.
name|isEmpty
argument_list|()
condition|?
literal|""
else|:
literal|" Catches {"
operator|+
name|catchClauses
operator|+
literal|"}"
decl_stmt|;
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
literal|"}"
operator|+
name|catchText
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
name|Processor
argument_list|>
name|processors
init|=
name|next
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Object
name|lastHandled
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_HANDLED
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_HANDLED
argument_list|,
literal|null
argument_list|)
expr_stmt|;
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
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|TRY_ROUTE_BLOCK
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|ExchangeHelper
operator|.
name|prepareOutToIn
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// process the next processor
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
name|callback
argument_list|,
name|processors
argument_list|,
name|async
argument_list|,
name|lastHandled
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
name|exchange
operator|.
name|removeProperty
argument_list|(
name|Exchange
operator|.
name|TRY_ROUTE_BLOCK
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_HANDLED
argument_list|,
name|lastHandled
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
DECL|method|process (final Exchange exchange, final AsyncCallback callback, final Iterator<Processor> processors, final AsyncProcessor processor, final Object lastHandled)
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
name|Iterator
argument_list|<
name|Processor
argument_list|>
name|processors
parameter_list|,
specifier|final
name|AsyncProcessor
name|processor
parameter_list|,
specifier|final
name|Object
name|lastHandled
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
name|processor
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
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|TRY_ROUTE_BLOCK
argument_list|,
literal|true
argument_list|)
expr_stmt|;
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
name|doneSync
operator|=
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|processors
argument_list|,
name|processor
argument_list|,
name|lastHandled
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
name|exchange
operator|.
name|removeProperty
argument_list|(
name|Exchange
operator|.
name|TRY_ROUTE_BLOCK
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_HANDLED
argument_list|,
name|lastHandled
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
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|tryProcessor
argument_list|,
name|catchClauses
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
name|tryProcessor
argument_list|,
name|catchClauses
argument_list|,
name|finallyProcessor
argument_list|)
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
argument_list|<>
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
name|catchClauses
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
operator|||
name|catchClauses
operator|!=
literal|null
operator|&&
operator|!
name|catchClauses
operator|.
name|isEmpty
argument_list|()
operator|||
name|finallyProcessor
operator|!=
literal|null
return|;
block|}
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
block|}
end_class

end_unit

