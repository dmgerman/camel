begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
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
name|Exchange
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
name|util
operator|.
name|ExchangeHelper
operator|.
name|hasExceptionBeenHandledByErrorHandler
import|;
end_import

begin_comment
comment|/**  * An {@link AggregationStrategy} which just uses the latest exchange which is useful  * for status messages where old status messages have no real value. Another example is things  * like market data prices, where old stock prices are not that relevant, only the current price is.  */
end_comment

begin_class
DECL|class|UseLatestAggregationStrategy
specifier|public
class|class
name|UseLatestAggregationStrategy
implements|implements
name|AggregationStrategy
block|{
DECL|method|aggregate (Exchange oldExchange, Exchange newExchange)
specifier|public
name|Exchange
name|aggregate
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
if|if
condition|(
name|newExchange
operator|==
literal|null
condition|)
block|{
return|return
name|oldExchange
return|;
block|}
if|if
condition|(
name|oldExchange
operator|==
literal|null
condition|)
block|{
return|return
name|newExchange
return|;
block|}
name|Exchange
name|answer
init|=
literal|null
decl_stmt|;
comment|// propagate exception first
name|propagateException
argument_list|(
name|oldExchange
argument_list|,
name|newExchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|newExchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|newExchange
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// the propagate failures
name|answer
operator|=
name|propagateFailure
argument_list|(
name|oldExchange
argument_list|,
name|newExchange
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|propagateException (Exchange oldExchange, Exchange newExchange)
specifier|protected
name|void
name|propagateException
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
if|if
condition|(
name|oldExchange
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// propagate exception from old exchange if there isn't already an exception
if|if
condition|(
name|newExchange
operator|.
name|getException
argument_list|()
operator|==
literal|null
condition|)
block|{
name|newExchange
operator|.
name|setException
argument_list|(
name|oldExchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|newExchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_ENDPOINT
argument_list|,
name|oldExchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_ENDPOINT
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|propagateFailure (Exchange oldExchange, Exchange newExchange)
specifier|protected
name|Exchange
name|propagateFailure
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
if|if
condition|(
name|oldExchange
operator|==
literal|null
condition|)
block|{
return|return
name|newExchange
return|;
block|}
comment|// propagate exception from old exchange if there isn't already an exception
name|boolean
name|exceptionHandled
init|=
name|hasExceptionBeenHandledByErrorHandler
argument_list|(
name|oldExchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldExchange
operator|.
name|isFailed
argument_list|()
operator|||
name|oldExchange
operator|.
name|isRollbackOnly
argument_list|()
operator|||
name|exceptionHandled
condition|)
block|{
comment|// propagate failure by using old exchange as the answer
return|return
name|oldExchange
return|;
block|}
return|return
name|newExchange
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
literal|"UseLatestAggregationStrategy"
return|;
block|}
block|}
end_class

end_unit

