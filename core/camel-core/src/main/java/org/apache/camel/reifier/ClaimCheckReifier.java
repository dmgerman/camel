begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ClaimCheckDefinition
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
name|ClaimCheckProcessor
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
name|ObjectHelper
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
name|ObjectHelper
operator|.
name|notNull
import|;
end_import

begin_class
DECL|class|ClaimCheckReifier
class|class
name|ClaimCheckReifier
extends|extends
name|ProcessorReifier
argument_list|<
name|ClaimCheckDefinition
argument_list|>
block|{
DECL|method|ClaimCheckReifier (ProcessorDefinition<?> definition)
name|ClaimCheckReifier
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
name|ClaimCheckDefinition
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
name|notNull
argument_list|(
name|definition
operator|.
name|getOperation
argument_list|()
argument_list|,
literal|"operation"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ClaimCheckProcessor
name|claim
init|=
operator|new
name|ClaimCheckProcessor
argument_list|()
decl_stmt|;
name|claim
operator|.
name|setOperation
argument_list|(
name|definition
operator|.
name|getOperation
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|claim
operator|.
name|setKey
argument_list|(
name|definition
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|claim
operator|.
name|setFilter
argument_list|(
name|definition
operator|.
name|getFilter
argument_list|()
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
name|claim
operator|.
name|setAggregationStrategy
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
block|}
comment|// only filter or aggregation strategy can be configured not both
if|if
condition|(
name|definition
operator|.
name|getFilter
argument_list|()
operator|!=
literal|null
operator|&&
name|strategy
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot use both filter and custom aggregation strategy on ClaimCheck EIP"
argument_list|)
throw|;
block|}
comment|// validate filter, we cannot have both +/- at the same time
if|if
condition|(
name|definition
operator|.
name|getFilter
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Iterable
name|it
init|=
name|ObjectHelper
operator|.
name|createIterable
argument_list|(
name|definition
operator|.
name|getFilter
argument_list|()
argument_list|,
literal|","
argument_list|)
decl_stmt|;
name|boolean
name|includeBody
init|=
literal|false
decl_stmt|;
name|boolean
name|excludeBody
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|it
control|)
block|{
name|String
name|pattern
init|=
name|o
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"body"
operator|.
name|equals
argument_list|(
name|pattern
argument_list|)
operator|||
literal|"+body"
operator|.
name|equals
argument_list|(
name|pattern
argument_list|)
condition|)
block|{
name|includeBody
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"-body"
operator|.
name|equals
argument_list|(
name|pattern
argument_list|)
condition|)
block|{
name|excludeBody
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|includeBody
operator|&&
name|excludeBody
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot have both include and exclude body at the same time in the filter: "
operator|+
name|definition
operator|.
name|getFilter
argument_list|()
argument_list|)
throw|;
block|}
name|boolean
name|includeHeaders
init|=
literal|false
decl_stmt|;
name|boolean
name|excludeHeaders
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|it
control|)
block|{
name|String
name|pattern
init|=
name|o
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"headers"
operator|.
name|equals
argument_list|(
name|pattern
argument_list|)
operator|||
literal|"+headers"
operator|.
name|equals
argument_list|(
name|pattern
argument_list|)
condition|)
block|{
name|includeHeaders
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"-headers"
operator|.
name|equals
argument_list|(
name|pattern
argument_list|)
condition|)
block|{
name|excludeHeaders
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|includeHeaders
operator|&&
name|excludeHeaders
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot have both include and exclude headers at the same time in the filter: "
operator|+
name|definition
operator|.
name|getFilter
argument_list|()
argument_list|)
throw|;
block|}
name|boolean
name|includeHeader
init|=
literal|false
decl_stmt|;
name|boolean
name|excludeHeader
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|it
control|)
block|{
name|String
name|pattern
init|=
name|o
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|pattern
operator|.
name|startsWith
argument_list|(
literal|"header:"
argument_list|)
operator|||
name|pattern
operator|.
name|startsWith
argument_list|(
literal|"+header:"
argument_list|)
condition|)
block|{
name|includeHeader
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|pattern
operator|.
name|startsWith
argument_list|(
literal|"-header:"
argument_list|)
condition|)
block|{
name|excludeHeader
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|includeHeader
operator|&&
name|excludeHeader
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot have both include and exclude header at the same time in the filter: "
operator|+
name|definition
operator|.
name|getFilter
argument_list|()
argument_list|)
throw|;
block|}
block|}
return|return
name|claim
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
name|strategy
operator|=
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

