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
name|AsyncProcessor
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
DECL|method|process (Exchange original, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|original
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
name|original
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
if|if
condition|(
name|nextExchange
operator|.
name|isFailed
argument_list|()
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
literal|"Mesage exchange has failed so breaking out of pipeline: "
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
name|nextExchange
operator|.
name|getFault
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
if|if
condition|(
operator|!
name|processors
operator|.
name|hasNext
argument_list|()
condition|)
block|{
break|break;
block|}
name|AsyncProcessor
name|processor
init|=
name|AsyncProcessorTypeConverter
operator|.
name|convert
argument_list|(
name|processors
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
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
name|nextExchange
operator|=
name|createNextExchange
argument_list|(
name|processor
argument_list|,
name|nextExchange
argument_list|)
expr_stmt|;
block|}
name|boolean
name|sync
init|=
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
decl_stmt|;
comment|// Continue processing the pipeline synchronously ...
if|if
condition|(
operator|!
name|sync
condition|)
block|{
comment|// The pipeline will be completed async...
return|return
literal|false
return|;
block|}
block|}
comment|// If we get here then the pipeline was processed entirely
comment|// synchronously.
name|ExchangeHelper
operator|.
name|copyResults
argument_list|(
name|original
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
DECL|method|process (final Exchange original, final Exchange exchange, final AsyncCallback callback, final Iterator<Processor> processors, AsyncProcessor processor)
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
name|AsyncProcessor
name|processor
parameter_list|)
block|{
return|return
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
name|sync
parameter_list|)
block|{
comment|// We only have to handle async completion of
comment|// the pipeline..
if|if
condition|(
name|sync
condition|)
block|{
return|return;
block|}
comment|// Continue processing the pipeline...
name|Exchange
name|nextExchange
init|=
name|exchange
decl_stmt|;
while|while
condition|(
name|processors
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|AsyncProcessor
name|processor
init|=
name|AsyncProcessorTypeConverter
operator|.
name|convert
argument_list|(
name|processors
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|nextExchange
operator|.
name|isFailed
argument_list|()
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
literal|"Mesage exchange has failed so breaking out of pipeline: "
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
name|nextExchange
operator|.
name|getFault
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
name|nextExchange
operator|=
name|createNextExchange
argument_list|(
name|processor
argument_list|,
name|nextExchange
argument_list|)
expr_stmt|;
name|sync
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
name|sync
condition|)
block|{
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
return|;
block|}
comment|/**      * Strategy method to create the next exchange from the      *      * @param producer         the producer used to send to the endpoint      * @param previousExchange the previous exchange      * @return a new exchange      */
DECL|method|createNextExchange (Processor producer, Exchange previousExchange)
specifier|protected
name|Exchange
name|createNextExchange
parameter_list|(
name|Processor
name|producer
parameter_list|,
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
name|previousOut
init|=
name|previousExchange
operator|.
name|getOut
argument_list|(
literal|false
argument_list|)
decl_stmt|;
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
name|previousOut
operator|!=
literal|null
operator|&&
name|previousOut
operator|.
name|getBody
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|in
operator|.
name|copyFrom
argument_list|(
name|previousOut
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

