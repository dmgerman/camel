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
name|Expression
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
name|NoTypeConversionAvailableException
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
name|ReactiveHelper
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
comment|/**  * The processor which sends messages in a loop.  */
end_comment

begin_class
DECL|class|LoopProcessor
specifier|public
class|class
name|LoopProcessor
extends|extends
name|DelegateAsyncProcessor
implements|implements
name|Traceable
implements|,
name|IdAware
block|{
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|expression
specifier|private
specifier|final
name|Expression
name|expression
decl_stmt|;
DECL|field|predicate
specifier|private
specifier|final
name|Predicate
name|predicate
decl_stmt|;
DECL|field|copy
specifier|private
specifier|final
name|boolean
name|copy
decl_stmt|;
DECL|method|LoopProcessor (Processor processor, Expression expression, Predicate predicate, boolean copy)
specifier|public
name|LoopProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|Expression
name|expression
parameter_list|,
name|Predicate
name|predicate
parameter_list|,
name|boolean
name|copy
parameter_list|)
block|{
name|super
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
name|this
operator|.
name|predicate
operator|=
name|predicate
expr_stmt|;
name|this
operator|.
name|copy
operator|=
name|copy
expr_stmt|;
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
try|try
block|{
name|LoopState
name|state
init|=
operator|new
name|LoopState
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
decl_stmt|;
name|ReactiveHelper
operator|.
name|scheduleMain
argument_list|(
name|state
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|Exception
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
block|}
comment|/**      * Class holding state for loop processing      */
DECL|class|LoopState
class|class
name|LoopState
implements|implements
name|Runnable
block|{
DECL|field|exchange
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|callback
specifier|final
name|AsyncCallback
name|callback
decl_stmt|;
DECL|field|current
name|Exchange
name|current
decl_stmt|;
DECL|field|index
name|int
name|index
decl_stmt|;
DECL|field|count
name|int
name|count
decl_stmt|;
DECL|method|LoopState (Exchange exchange, AsyncCallback callback)
specifier|public
name|LoopState
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
throws|throws
name|NoTypeConversionAvailableException
block|{
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
name|current
operator|=
name|exchange
expr_stmt|;
comment|// evaluate expression / predicate
if|if
condition|(
name|expression
operator|!=
literal|null
condition|)
block|{
comment|// Intermediate conversion to String is needed when direct conversion to Integer is not available
comment|// but evaluation result is a textual representation of a numeric value.
name|String
name|text
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|count
operator|=
name|ExchangeHelper
operator|.
name|convertToMandatoryType
argument_list|(
name|exchange
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
name|text
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|LOOP_SIZE
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
comment|// check for error if so we should break out
name|boolean
name|cont
init|=
name|continueProcessing
argument_list|(
name|current
argument_list|,
literal|"so breaking out of loop"
argument_list|,
name|log
argument_list|)
decl_stmt|;
name|boolean
name|doWhile
init|=
name|predicate
operator|==
literal|null
operator|||
name|predicate
operator|.
name|matches
argument_list|(
name|current
argument_list|)
decl_stmt|;
name|boolean
name|doLoop
init|=
name|expression
operator|==
literal|null
operator|||
name|index
operator|<
name|count
decl_stmt|;
comment|// iterate
if|if
condition|(
name|cont
operator|&&
name|doWhile
operator|&&
name|doLoop
condition|)
block|{
comment|// and prepare for next iteration
name|current
operator|=
name|prepareExchange
argument_list|(
name|exchange
argument_list|,
name|index
argument_list|)
expr_stmt|;
comment|// set current index as property
name|log
operator|.
name|debug
argument_list|(
literal|"LoopProcessor: iteration #{}"
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|current
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|LOOP_INDEX
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|current
argument_list|,
name|doneSync
lambda|->
block|{
comment|// increment counter after done
name|index
operator|++
expr_stmt|;
name|ReactiveHelper
operator|.
name|schedule
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// we are done so prepare the result
name|ExchangeHelper
operator|.
name|copyResults
argument_list|(
name|exchange
argument_list|,
name|current
argument_list|)
expr_stmt|;
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
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Processing failed for exchangeId: {}>>> {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
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
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"LoopState["
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
comment|/**      * Prepares the exchange for the next iteration      *      * @param exchange the exchange      * @param index the index of the next iteration      * @return the exchange to use      */
DECL|method|prepareExchange (Exchange exchange, int index)
specifier|protected
name|Exchange
name|prepareExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
name|copy
condition|)
block|{
comment|// use a copy but let it reuse the same exchange id so it appear as one exchange
comment|// use the original exchange rather than the looping exchange (esp. with the async routing engine)
return|return
name|ExchangeHelper
operator|.
name|createCopy
argument_list|(
name|exchange
argument_list|,
literal|true
argument_list|)
return|;
block|}
else|else
block|{
name|ExchangeHelper
operator|.
name|prepareOutToIn
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
block|}
DECL|method|getExpression ()
specifier|public
name|Expression
name|getExpression
parameter_list|()
block|{
return|return
name|expression
return|;
block|}
DECL|method|getPredicate ()
specifier|public
name|Predicate
name|getPredicate
parameter_list|()
block|{
return|return
name|predicate
return|;
block|}
DECL|method|isCopy ()
specifier|public
name|boolean
name|isCopy
parameter_list|()
block|{
return|return
name|copy
return|;
block|}
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
if|if
condition|(
name|predicate
operator|!=
literal|null
condition|)
block|{
return|return
literal|"loopWhile["
operator|+
name|predicate
operator|+
literal|"]"
return|;
block|}
else|else
block|{
return|return
literal|"loop["
operator|+
name|expression
operator|+
literal|"]"
return|;
block|}
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|predicate
operator|!=
literal|null
condition|)
block|{
return|return
literal|"Loop[while: "
operator|+
name|predicate
operator|+
literal|" do: "
operator|+
name|getProcessor
argument_list|()
operator|+
literal|"]"
return|;
block|}
else|else
block|{
return|return
literal|"Loop[for: "
operator|+
name|expression
operator|+
literal|" times do: "
operator|+
name|getProcessor
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
block|}
end_class

end_unit

