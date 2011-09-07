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
name|ChoiceProcessor
operator|.
name|class
argument_list|)
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
name|AsyncProcessor
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
name|AsyncProcessorConverterHelper
operator|.
name|convert
argument_list|(
name|otherwise
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
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|filters
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|FilterProcessor
name|filter
init|=
name|filters
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Predicate
name|predicate
init|=
name|filter
operator|.
name|getPredicate
argument_list|()
decl_stmt|;
name|boolean
name|matches
init|=
literal|false
decl_stmt|;
try|try
block|{
comment|// ensure we handle exceptions thrown when matching predicate
if|if
condition|(
name|predicate
operator|!=
literal|null
condition|)
block|{
name|matches
operator|=
name|predicate
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
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
literal|"#{} - {} matches: {} for: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|i
block|,
name|predicate
block|,
name|matches
block|,
name|exchange
block|}
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|matches
condition|)
block|{
comment|// process next will also take care (has not null test) if next was a stop().
comment|// stop() has no processor to execute, and thus we will end in a NPE
return|return
name|filter
operator|.
name|processNext
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
block|}
if|if
condition|(
name|otherwise
operator|!=
literal|null
condition|)
block|{
return|return
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|otherwise
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
else|else
block|{
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
name|FilterProcessor
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
operator|.
name|getPredicate
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|processor
operator|.
name|getProcessor
argument_list|()
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

