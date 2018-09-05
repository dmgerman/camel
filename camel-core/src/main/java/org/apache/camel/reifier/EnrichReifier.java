begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.reifier
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|reifier
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
name|model
operator|.
name|EnrichDefinition
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
name|model
operator|.
name|ProcessorDefinition
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
name|processor
operator|.
name|Enricher
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
name|processor
operator|.
name|aggregate
operator|.
name|AggregationStrategyBeanAdapter
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
name|RouteContext
import|;
end_import

begin_class
DECL|class|EnrichReifier
class|class
name|EnrichReifier
extends|extends
name|ExpressionReifier
argument_list|<
name|EnrichDefinition
argument_list|>
block|{
DECL|method|EnrichReifier (ProcessorDefinition<?> definition)
name|EnrichReifier
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
name|super
argument_list|(
name|EnrichDefinition
operator|.
name|class
operator|.
name|cast
argument_list|(
name|definition
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|definition
operator|.
name|getExpression
argument_list|()
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|boolean
name|isShareUnitOfWork
init|=
name|definition
operator|.
name|getShareUnitOfWork
argument_list|()
operator|!=
literal|null
operator|&&
name|definition
operator|.
name|getShareUnitOfWork
argument_list|()
decl_stmt|;
name|boolean
name|isIgnoreInvalidEndpoint
init|=
name|definition
operator|.
name|getIgnoreInvalidEndpoint
argument_list|()
operator|!=
literal|null
operator|&&
name|definition
operator|.
name|getIgnoreInvalidEndpoint
argument_list|()
decl_stmt|;
name|Enricher
name|enricher
init|=
operator|new
name|Enricher
argument_list|(
name|exp
argument_list|)
decl_stmt|;
name|enricher
operator|.
name|setShareUnitOfWork
argument_list|(
name|isShareUnitOfWork
argument_list|)
expr_stmt|;
name|enricher
operator|.
name|setIgnoreInvalidEndpoint
argument_list|(
name|isIgnoreInvalidEndpoint
argument_list|)
expr_stmt|;
name|AggregationStrategy
name|strategy
init|=
name|createAggregationStrategy
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|strategy
operator|!=
literal|null
condition|)
block|{
name|enricher
operator|.
name|setAggregationStrategy
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getAggregateOnException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|enricher
operator|.
name|setAggregateOnException
argument_list|(
name|definition
operator|.
name|getAggregateOnException
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|enricher
return|;
block|}
DECL|method|createAggregationStrategy (RouteContext routeContext)
specifier|private
name|AggregationStrategy
name|createAggregationStrategy
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
name|AggregationStrategy
name|strategy
init|=
name|definition
operator|.
name|getAggregationStrategy
argument_list|()
decl_stmt|;
if|if
condition|(
name|strategy
operator|==
literal|null
operator|&&
name|definition
operator|.
name|getAggregationStrategyRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Object
name|aggStrategy
init|=
name|routeContext
operator|.
name|lookup
argument_list|(
name|definition
operator|.
name|getAggregationStrategyRef
argument_list|()
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|aggStrategy
operator|instanceof
name|AggregationStrategy
condition|)
block|{
name|strategy
operator|=
operator|(
name|AggregationStrategy
operator|)
name|aggStrategy
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|aggStrategy
operator|!=
literal|null
condition|)
block|{
name|AggregationStrategyBeanAdapter
name|adapter
init|=
operator|new
name|AggregationStrategyBeanAdapter
argument_list|(
name|aggStrategy
argument_list|,
name|definition
operator|.
name|getAggregationStrategyMethodName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|definition
operator|.
name|getAggregationStrategyMethodAllowNull
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|adapter
operator|.
name|setAllowNullNewExchange
argument_list|(
name|definition
operator|.
name|getAggregationStrategyMethodAllowNull
argument_list|()
argument_list|)
expr_stmt|;
name|adapter
operator|.
name|setAllowNullOldExchange
argument_list|(
name|definition
operator|.
name|getAggregationStrategyMethodAllowNull
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|strategy
operator|=
name|adapter
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot find AggregationStrategy in Registry with name: "
operator|+
name|definition
operator|.
name|getAggregationStrategyRef
argument_list|()
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|strategy
operator|instanceof
name|CamelContextAware
condition|)
block|{
operator|(
operator|(
name|CamelContextAware
operator|)
name|strategy
operator|)
operator|.
name|setCamelContext
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|strategy
return|;
block|}
block|}
end_class

end_unit

