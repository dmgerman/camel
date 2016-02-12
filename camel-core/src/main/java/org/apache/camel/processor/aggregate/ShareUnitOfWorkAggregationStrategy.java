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
comment|/**  * An {@link AggregationStrategy} which are used when the option<tt>shareUnitOfWork</tt> is enabled  * on EIPs such as multicast, splitter or recipientList.  *<p/>  * This strategy wraps the actual in use strategy to provide the logic needed for making shareUnitOfWork work.  *<p/>  * This strategy is<b>not</b> intended for end users to use.  */
end_comment

begin_class
DECL|class|ShareUnitOfWorkAggregationStrategy
specifier|public
specifier|final
class|class
name|ShareUnitOfWorkAggregationStrategy
implements|implements
name|AggregationStrategy
block|{
DECL|field|strategy
specifier|private
specifier|final
name|AggregationStrategy
name|strategy
decl_stmt|;
DECL|method|ShareUnitOfWorkAggregationStrategy (AggregationStrategy strategy)
specifier|public
name|ShareUnitOfWorkAggregationStrategy
parameter_list|(
name|AggregationStrategy
name|strategy
parameter_list|)
block|{
name|this
operator|.
name|strategy
operator|=
name|strategy
expr_stmt|;
block|}
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
comment|// aggreagate using the actual strategy first
name|Exchange
name|answer
init|=
name|strategy
operator|.
name|aggregate
argument_list|(
name|oldExchange
argument_list|,
name|newExchange
argument_list|)
decl_stmt|;
comment|// ensure any errors is propagated from the new exchange to the answer
name|propagateFailure
argument_list|(
name|answer
argument_list|,
name|newExchange
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|propagateFailure (Exchange answer, Exchange newExchange)
specifier|protected
name|void
name|propagateFailure
parameter_list|(
name|Exchange
name|answer
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
comment|// if new exchange failed then propagate all the error related properties to the answer
name|boolean
name|exceptionHandled
init|=
name|hasExceptionBeenHandledByErrorHandler
argument_list|(
name|newExchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|newExchange
operator|.
name|isFailed
argument_list|()
operator|||
name|newExchange
operator|.
name|isRollbackOnly
argument_list|()
operator|||
name|exceptionHandled
condition|)
block|{
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
operator|.
name|setException
argument_list|(
name|newExchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|newExchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|newExchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|newExchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_ENDPOINT
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_ENDPOINT
argument_list|,
name|newExchange
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
if|if
condition|(
name|newExchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_ROUTE_ID
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_ROUTE_ID
argument_list|,
name|newExchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_ROUTE_ID
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|newExchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|ERRORHANDLER_HANDLED
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|ERRORHANDLER_HANDLED
argument_list|,
name|newExchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|ERRORHANDLER_HANDLED
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|newExchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_HANDLED
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_HANDLED
argument_list|,
name|newExchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_HANDLED
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
literal|"ShareUnitOfWorkAggregationStrategy"
return|;
block|}
block|}
end_class

end_unit

