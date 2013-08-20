begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|Endpoint
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
name|processor
operator|.
name|PollEnricher
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
comment|/**  * Represents an XML&lt;pollEnrich/&gt; element  *  * @see org.apache.camel.processor.Enricher  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"pollEnrich"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|PollEnrichDefinition
specifier|public
class|class
name|PollEnrichDefinition
extends|extends
name|NoOutputDefinition
argument_list|<
name|PollEnrichDefinition
argument_list|>
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"uri"
argument_list|)
DECL|field|resourceUri
specifier|private
name|String
name|resourceUri
decl_stmt|;
comment|// TODO: For Camel 3.0 we should remove this ref attribute as you can do that in the uri, by prefixing with ref:
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"ref"
argument_list|)
DECL|field|resourceRef
specifier|private
name|String
name|resourceRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|timeout
specifier|private
name|Long
name|timeout
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"strategyRef"
argument_list|)
DECL|field|aggregationStrategyRef
specifier|private
name|String
name|aggregationStrategyRef
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"strategyMethodName"
argument_list|)
DECL|field|aggregationStrategyMethodName
specifier|private
name|String
name|aggregationStrategyMethodName
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"strategyMethodAllowNull"
argument_list|)
DECL|field|aggregationStrategyMethodAllowNull
specifier|private
name|Boolean
name|aggregationStrategyMethodAllowNull
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|aggregationStrategy
specifier|private
name|AggregationStrategy
name|aggregationStrategy
decl_stmt|;
DECL|method|PollEnrichDefinition ()
specifier|public
name|PollEnrichDefinition
parameter_list|()
block|{     }
DECL|method|PollEnrichDefinition (AggregationStrategy aggregationStrategy, String resourceUri, long timeout)
specifier|public
name|PollEnrichDefinition
parameter_list|(
name|AggregationStrategy
name|aggregationStrategy
parameter_list|,
name|String
name|resourceUri
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|aggregationStrategy
operator|=
name|aggregationStrategy
expr_stmt|;
name|this
operator|.
name|resourceUri
operator|=
name|resourceUri
expr_stmt|;
name|this
operator|.
name|timeout
operator|=
name|timeout
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
literal|"PollEnrich["
operator|+
name|description
argument_list|()
operator|+
literal|" "
operator|+
name|aggregationStrategy
operator|+
literal|"]"
return|;
block|}
DECL|method|description ()
specifier|protected
name|String
name|description
parameter_list|()
block|{
return|return
name|FromDefinition
operator|.
name|description
argument_list|(
name|getResourceUri
argument_list|()
argument_list|,
name|getResourceRef
argument_list|()
argument_list|,
operator|(
name|Endpoint
operator|)
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"pollEnrich"
return|;
block|}
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
literal|"pollEnrich["
operator|+
name|description
argument_list|()
operator|+
literal|"]"
return|;
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
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|resourceUri
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|resourceRef
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Either uri or ref must be provided for resource endpoint"
argument_list|)
throw|;
block|}
comment|// lookup endpoint
name|Endpoint
name|endpoint
decl_stmt|;
if|if
condition|(
name|resourceUri
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|=
name|routeContext
operator|.
name|resolveEndpoint
argument_list|(
name|resourceUri
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|endpoint
operator|=
name|routeContext
operator|.
name|resolveEndpoint
argument_list|(
literal|null
argument_list|,
name|resourceRef
argument_list|)
expr_stmt|;
block|}
name|PollEnricher
name|enricher
decl_stmt|;
if|if
condition|(
name|timeout
operator|!=
literal|null
condition|)
block|{
name|enricher
operator|=
operator|new
name|PollEnricher
argument_list|(
literal|null
argument_list|,
name|endpoint
operator|.
name|createPollingConsumer
argument_list|()
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// if no timeout then we should block, and there use a negative timeout
name|enricher
operator|=
operator|new
name|PollEnricher
argument_list|(
literal|null
argument_list|,
name|endpoint
operator|.
name|createPollingConsumer
argument_list|()
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
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
operator|==
literal|null
condition|)
block|{
name|enricher
operator|.
name|setDefaultAggregationStrategy
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|enricher
operator|.
name|setAggregationStrategy
argument_list|(
name|strategy
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
name|getAggregationStrategy
argument_list|()
decl_stmt|;
if|if
condition|(
name|strategy
operator|==
literal|null
operator|&&
name|aggregationStrategyRef
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
name|aggregationStrategyRef
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
name|getAggregationStrategyMethodName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
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
name|getAggregationStrategyMethodAllowNull
argument_list|()
argument_list|)
expr_stmt|;
name|adapter
operator|.
name|setAllowNullOldExchange
argument_list|(
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
name|aggregationStrategyRef
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|strategy
operator|!=
literal|null
operator|&&
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
DECL|method|getResourceUri ()
specifier|public
name|String
name|getResourceUri
parameter_list|()
block|{
return|return
name|resourceUri
return|;
block|}
DECL|method|setResourceUri (String resourceUri)
specifier|public
name|void
name|setResourceUri
parameter_list|(
name|String
name|resourceUri
parameter_list|)
block|{
name|this
operator|.
name|resourceUri
operator|=
name|resourceUri
expr_stmt|;
block|}
DECL|method|getResourceRef ()
specifier|public
name|String
name|getResourceRef
parameter_list|()
block|{
return|return
name|resourceRef
return|;
block|}
DECL|method|setResourceRef (String resourceRef)
specifier|public
name|void
name|setResourceRef
parameter_list|(
name|String
name|resourceRef
parameter_list|)
block|{
name|this
operator|.
name|resourceRef
operator|=
name|resourceRef
expr_stmt|;
block|}
DECL|method|getTimeout ()
specifier|public
name|Long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|method|setTimeout (Long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|Long
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
DECL|method|getAggregationStrategyRef ()
specifier|public
name|String
name|getAggregationStrategyRef
parameter_list|()
block|{
return|return
name|aggregationStrategyRef
return|;
block|}
DECL|method|setAggregationStrategyRef (String aggregationStrategyRef)
specifier|public
name|void
name|setAggregationStrategyRef
parameter_list|(
name|String
name|aggregationStrategyRef
parameter_list|)
block|{
name|this
operator|.
name|aggregationStrategyRef
operator|=
name|aggregationStrategyRef
expr_stmt|;
block|}
DECL|method|getAggregationStrategyMethodName ()
specifier|public
name|String
name|getAggregationStrategyMethodName
parameter_list|()
block|{
return|return
name|aggregationStrategyMethodName
return|;
block|}
DECL|method|setAggregationStrategyMethodName (String aggregationStrategyMethodName)
specifier|public
name|void
name|setAggregationStrategyMethodName
parameter_list|(
name|String
name|aggregationStrategyMethodName
parameter_list|)
block|{
name|this
operator|.
name|aggregationStrategyMethodName
operator|=
name|aggregationStrategyMethodName
expr_stmt|;
block|}
DECL|method|getAggregationStrategyMethodAllowNull ()
specifier|public
name|Boolean
name|getAggregationStrategyMethodAllowNull
parameter_list|()
block|{
return|return
name|aggregationStrategyMethodAllowNull
return|;
block|}
DECL|method|setAggregationStrategyMethodAllowNull (Boolean aggregationStrategyMethodAllowNull)
specifier|public
name|void
name|setAggregationStrategyMethodAllowNull
parameter_list|(
name|Boolean
name|aggregationStrategyMethodAllowNull
parameter_list|)
block|{
name|this
operator|.
name|aggregationStrategyMethodAllowNull
operator|=
name|aggregationStrategyMethodAllowNull
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
block|}
end_class

end_unit

