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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|AggregationStrategy
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
name|CamelContextAware
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
name|impl
operator|.
name|engine
operator|.
name|DefaultClaimCheckRepository
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
name|ClaimCheckRepository
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
name|LanguageSupport
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
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * ClaimCheck EIP implementation.  *<p/>  * The current Claim Check EIP implementation in Camel is only intended for temporary memory repository. Likewise  * the repository is not shared among {@link Exchange}s, but a private instance is created per {@link Exchange}.  * This guards against concurrent and thread-safe issues. For off-memory persistent storage of data, then use  * any of the many Camel components that support persistent storage, and do not use this Claim Check EIP implementation.  */
end_comment

begin_class
DECL|class|ClaimCheckProcessor
specifier|public
class|class
name|ClaimCheckProcessor
extends|extends
name|AsyncProcessorSupport
implements|implements
name|IdAware
implements|,
name|CamelContextAware
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|operation
specifier|private
name|String
name|operation
decl_stmt|;
DECL|field|aggregationStrategy
specifier|private
name|AggregationStrategy
name|aggregationStrategy
decl_stmt|;
DECL|field|key
specifier|private
name|String
name|key
decl_stmt|;
DECL|field|keyExpression
specifier|private
name|Expression
name|keyExpression
decl_stmt|;
DECL|field|filter
specifier|private
name|String
name|filter
decl_stmt|;
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
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
DECL|method|getOperation ()
specifier|public
name|String
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
DECL|method|setOperation (String operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
DECL|method|getAggregationStrategy ()
specifier|public
name|AggregationStrategy
name|getAggregationStrategy
parameter_list|()
block|{
return|return
name|aggregationStrategy
return|;
block|}
DECL|method|setAggregationStrategy (AggregationStrategy aggregationStrategy)
specifier|public
name|void
name|setAggregationStrategy
parameter_list|(
name|AggregationStrategy
name|aggregationStrategy
parameter_list|)
block|{
name|this
operator|.
name|aggregationStrategy
operator|=
name|aggregationStrategy
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|String
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
DECL|method|setKey (String key)
specifier|public
name|void
name|setKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
block|}
DECL|method|getFilter ()
specifier|public
name|String
name|getFilter
parameter_list|()
block|{
return|return
name|filter
return|;
block|}
DECL|method|setFilter (String filter)
specifier|public
name|void
name|setFilter
parameter_list|(
name|String
name|filter
parameter_list|)
block|{
name|this
operator|.
name|filter
operator|=
name|filter
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
comment|// the repository is scoped per exchange
name|ClaimCheckRepository
name|repo
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|CLAIM_CHECK_REPOSITORY
argument_list|,
name|ClaimCheckRepository
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|repo
operator|==
literal|null
condition|)
block|{
name|repo
operator|=
operator|new
name|DefaultClaimCheckRepository
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CLAIM_CHECK_REPOSITORY
argument_list|,
name|repo
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|String
name|claimKey
init|=
name|keyExpression
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
if|if
condition|(
literal|"Set"
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
comment|// copy exchange, and do not share the unit of work
name|Exchange
name|copy
init|=
name|ExchangeHelper
operator|.
name|createCorrelatedCopy
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|boolean
name|addedNew
init|=
name|repo
operator|.
name|add
argument_list|(
name|claimKey
argument_list|,
name|copy
argument_list|)
decl_stmt|;
if|if
condition|(
name|addedNew
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Add: {} -> {}"
argument_list|,
name|claimKey
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Override: {} -> {}"
argument_list|,
name|claimKey
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"Get"
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|Exchange
name|copy
init|=
name|repo
operator|.
name|get
argument_list|(
name|claimKey
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Get: {} -> {}"
argument_list|,
name|claimKey
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|copy
operator|!=
literal|null
condition|)
block|{
name|Exchange
name|result
init|=
name|aggregationStrategy
operator|.
name|aggregate
argument_list|(
name|exchange
argument_list|,
name|copy
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
name|ExchangeHelper
operator|.
name|copyResultsPreservePattern
argument_list|(
name|exchange
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
literal|"GetAndRemove"
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|Exchange
name|copy
init|=
name|repo
operator|.
name|getAndRemove
argument_list|(
name|claimKey
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"GetAndRemove: {} -> {}"
argument_list|,
name|claimKey
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|copy
operator|!=
literal|null
condition|)
block|{
comment|// prepare the exchanges for aggregation
name|ExchangeHelper
operator|.
name|prepareAggregation
argument_list|(
name|exchange
argument_list|,
name|copy
argument_list|)
expr_stmt|;
name|Exchange
name|result
init|=
name|aggregationStrategy
operator|.
name|aggregate
argument_list|(
name|exchange
argument_list|,
name|copy
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
name|ExchangeHelper
operator|.
name|copyResultsPreservePattern
argument_list|(
name|exchange
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
literal|"Push"
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
comment|// copy exchange, and do not share the unit of work
name|Exchange
name|copy
init|=
name|ExchangeHelper
operator|.
name|createCorrelatedCopy
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Push: {} -> {}"
argument_list|,
name|claimKey
argument_list|,
name|copy
argument_list|)
expr_stmt|;
name|repo
operator|.
name|push
argument_list|(
name|copy
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"Pop"
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|Exchange
name|copy
init|=
name|repo
operator|.
name|pop
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Pop: {} -> {}"
argument_list|,
name|claimKey
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|copy
operator|!=
literal|null
condition|)
block|{
comment|// prepare the exchanges for aggregation
name|ExchangeHelper
operator|.
name|prepareAggregation
argument_list|(
name|exchange
argument_list|,
name|copy
argument_list|)
expr_stmt|;
name|Exchange
name|result
init|=
name|aggregationStrategy
operator|.
name|aggregate
argument_list|(
name|exchange
argument_list|,
name|copy
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
name|ExchangeHelper
operator|.
name|copyResultsPreservePattern
argument_list|(
name|exchange
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
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
block|}
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|operation
argument_list|,
literal|"operation"
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|aggregationStrategy
operator|==
literal|null
condition|)
block|{
name|aggregationStrategy
operator|=
name|createAggregationStrategy
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|aggregationStrategy
operator|instanceof
name|CamelContextAware
condition|)
block|{
operator|(
operator|(
name|CamelContextAware
operator|)
name|aggregationStrategy
operator|)
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LanguageSupport
operator|.
name|hasSimpleFunction
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|keyExpression
operator|=
name|camelContext
operator|.
name|resolveLanguage
argument_list|(
literal|"simple"
argument_list|)
operator|.
name|createExpression
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|keyExpression
operator|=
name|camelContext
operator|.
name|resolveLanguage
argument_list|(
literal|"constant"
argument_list|)
operator|.
name|createExpression
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|aggregationStrategy
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
name|aggregationStrategy
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
literal|"ClaimCheck["
operator|+
name|operation
operator|+
literal|"]"
return|;
block|}
DECL|method|createAggregationStrategy ()
specifier|protected
name|AggregationStrategy
name|createAggregationStrategy
parameter_list|()
block|{
name|ClaimCheckAggregationStrategy
name|answer
init|=
operator|new
name|ClaimCheckAggregationStrategy
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setFilter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

