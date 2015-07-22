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
name|processor
operator|.
name|PipelineHelper
operator|.
name|continueProcessing
import|;
end_import

begin_comment
comment|/**  * Implements a Choice structure where one or more predicates are used which if  * they are true their processors are used, with a default otherwise clause used  * if none match.  *   * @version   */
end_comment

begin_class
DECL|class|ChoiceProcessor
specifier|public
class|class
name|ChoiceProcessor
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
name|ChoiceProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|filters
specifier|private
specifier|final
name|List
argument_list|<
name|FilterProcessor
argument_list|>
name|filters
decl_stmt|;
DECL|field|otherwise
specifier|private
specifier|final
name|Processor
name|otherwise
decl_stmt|;
DECL|method|ChoiceProcessor (List<FilterProcessor> filters, Processor otherwise)
specifier|public
name|ChoiceProcessor
parameter_list|(
name|List
argument_list|<
name|FilterProcessor
argument_list|>
name|filters
parameter_list|,
name|Processor
name|otherwise
parameter_list|)
block|{
name|this
operator|.
name|filters
operator|=
name|filters
expr_stmt|;
name|this
operator|.
name|otherwise
operator|=
name|otherwise
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
comment|// callback to restore existing FILTER_MATCHED property on the Exchange
specifier|final
name|Object
name|existing
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|FILTER_MATCHED
argument_list|)
decl_stmt|;
specifier|final
name|AsyncCallback
name|choiceCallback
init|=
operator|new
name|AsyncCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
if|if
condition|(
name|existing
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|FILTER_MATCHED
argument_list|,
name|existing
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|removeProperty
argument_list|(
name|Exchange
operator|.
name|FILTER_MATCHED
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
comment|// as we only pick one processor to process, then no need to have async callback that has a while loop as well
comment|// as this should not happen, eg we pick the first filter processor that matches, or the otherwise (if present)
comment|// and if not, we just continue without using any processor
while|while
condition|(
name|processors
operator|.
name|hasNext
argument_list|()
condition|)
block|{
comment|// get the next processor
name|Processor
name|processor
init|=
name|processors
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// evaluate the predicate on filter predicate early to be faster
comment|// and avoid issues when having nested choices
comment|// as we should only pick one processor
name|boolean
name|matches
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|processor
operator|instanceof
name|FilterProcessor
condition|)
block|{
name|FilterProcessor
name|filter
init|=
operator|(
name|FilterProcessor
operator|)
name|processor
decl_stmt|;
try|try
block|{
name|matches
operator|=
name|filter
operator|.
name|getPredicate
argument_list|()
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|FILTER_MATCHED
argument_list|,
name|matches
argument_list|)
expr_stmt|;
comment|// as we have pre evaluated the predicate then use its processor directly when routing
name|processor
operator|=
name|filter
operator|.
name|getProcessor
argument_list|()
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
else|else
block|{
comment|// its the otherwise processor, so its a match
name|matches
operator|=
literal|true
expr_stmt|;
block|}
comment|// check for error if so we should break out
if|if
condition|(
operator|!
name|continueProcessing
argument_list|(
name|exchange
argument_list|,
literal|"so breaking out of choice"
argument_list|,
name|LOG
argument_list|)
condition|)
block|{
break|break;
block|}
comment|// if we did not match then continue to next filter
if|if
condition|(
operator|!
name|matches
condition|)
block|{
continue|continue;
block|}
comment|// okay we found a filter or its the otherwise we are processing
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
return|return
name|async
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|choiceCallback
argument_list|)
return|;
block|}
comment|// when no filter matches and there is no otherwise, then just continue
name|choiceCallback
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"choice{"
argument_list|)
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|Processor
name|processor
range|:
name|filters
control|)
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
name|builder
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
literal|"when "
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|otherwise
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|", otherwise: "
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|otherwise
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
literal|"}"
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
literal|"choice"
return|;
block|}
DECL|method|getFilters ()
specifier|public
name|List
argument_list|<
name|FilterProcessor
argument_list|>
name|getFilters
parameter_list|()
block|{
return|return
name|filters
return|;
block|}
DECL|method|getOtherwise ()
specifier|public
name|Processor
name|getOtherwise
parameter_list|()
block|{
return|return
name|otherwise
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
name|filters
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|addAll
argument_list|(
name|filters
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|otherwise
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|otherwise
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
name|otherwise
operator|!=
literal|null
operator|||
operator|(
name|filters
operator|!=
literal|null
operator|&&
operator|!
name|filters
operator|.
name|isEmpty
argument_list|()
operator|)
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
name|filters
argument_list|,
name|otherwise
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
name|otherwise
argument_list|,
name|filters
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

