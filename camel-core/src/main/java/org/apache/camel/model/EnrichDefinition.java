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
name|language
operator|.
name|ExpressionDefinition
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
name|Metadata
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

begin_comment
comment|/**  * Enriches a message with data from a secondary resource  *  * @see Enricher  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"eip,transformation"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"enrich"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|EnrichDefinition
specifier|public
class|class
name|EnrichDefinition
extends|extends
name|NoOutputExpressionNode
block|{
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
name|XmlAttribute
DECL|field|aggregateOnException
specifier|private
name|Boolean
name|aggregateOnException
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|aggregationStrategy
specifier|private
name|AggregationStrategy
name|aggregationStrategy
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|shareUnitOfWork
specifier|private
name|Boolean
name|shareUnitOfWork
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|cacheSize
specifier|private
name|Integer
name|cacheSize
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|ignoreInvalidEndpoint
specifier|private
name|Boolean
name|ignoreInvalidEndpoint
decl_stmt|;
DECL|method|EnrichDefinition ()
specifier|public
name|EnrichDefinition
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|EnrichDefinition (AggregationStrategy aggregationStrategy)
specifier|public
name|EnrichDefinition
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Enrich["
operator|+
name|getExpression
argument_list|()
operator|+
literal|"]"
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
literal|"enrich["
operator|+
name|getExpression
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
name|Expression
name|exp
init|=
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
name|getShareUnitOfWork
argument_list|()
operator|!=
literal|null
operator|&&
name|getShareUnitOfWork
argument_list|()
decl_stmt|;
name|boolean
name|isIgnoreInvalidEndpoint
init|=
name|getIgnoreInvalidEndpoint
argument_list|()
operator|!=
literal|null
operator|&&
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
name|aggregateOnException
operator|!=
literal|null
condition|)
block|{
name|enricher
operator|.
name|setAggregateOnException
argument_list|(
name|aggregateOnException
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
comment|// Fluent API
comment|// -------------------------------------------------------------------------
comment|/**      * Sets the AggregationStrategy to be used to merge the reply from the external service, into a single outgoing message.      * By default Camel will use the reply from the external service as outgoing message.      */
DECL|method|aggregationStrategy (AggregationStrategy aggregationStrategy)
specifier|public
name|EnrichDefinition
name|aggregationStrategy
parameter_list|(
name|AggregationStrategy
name|aggregationStrategy
parameter_list|)
block|{
name|setAggregationStrategy
argument_list|(
name|aggregationStrategy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Refers to an AggregationStrategy to be used to merge the reply from the external service, into a single outgoing message.      * By default Camel will use the reply from the external service as outgoing message.      */
DECL|method|aggregationStrategyRef (String aggregationStrategyRef)
specifier|public
name|EnrichDefinition
name|aggregationStrategyRef
parameter_list|(
name|String
name|aggregationStrategyRef
parameter_list|)
block|{
name|setAggregationStrategyRef
argument_list|(
name|aggregationStrategyRef
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * This option can be used to explicit declare the method name to use, when using POJOs as the AggregationStrategy.      */
DECL|method|aggregationStrategyMethodName (String aggregationStrategyMethodName)
specifier|public
name|EnrichDefinition
name|aggregationStrategyMethodName
parameter_list|(
name|String
name|aggregationStrategyMethodName
parameter_list|)
block|{
name|setAggregationStrategyMethodName
argument_list|(
name|aggregationStrategyMethodName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * If this option is false then the aggregate method is not used if there was no data to enrich.      * If this option is true then null values is used as the oldExchange (when no data to enrich),      * when using POJOs as the AggregationStrategy.      */
DECL|method|aggregationStrategyMethodAllowNull (boolean aggregationStrategyMethodAllowNull)
specifier|public
name|EnrichDefinition
name|aggregationStrategyMethodAllowNull
parameter_list|(
name|boolean
name|aggregationStrategyMethodAllowNull
parameter_list|)
block|{
name|setAggregationStrategyMethodAllowNull
argument_list|(
name|aggregationStrategyMethodAllowNull
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * If this option is false then the aggregate method is not used if there was an exception thrown while trying      * to retrieve the data to enrich from the resource. Setting this option to true allows end users to control what      * to do if there was an exception in the aggregate method. For example to suppress the exception      * or set a custom message body etc.      */
DECL|method|aggregateOnException (boolean aggregateOnException)
specifier|public
name|EnrichDefinition
name|aggregateOnException
parameter_list|(
name|boolean
name|aggregateOnException
parameter_list|)
block|{
name|setAggregateOnException
argument_list|(
name|aggregateOnException
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Shares the {@link org.apache.camel.spi.UnitOfWork} with the parent and the resource exchange.      * Enrich will by default not share unit of work between the parent exchange and the resource exchange.      * This means the resource exchange has its own individual unit of work.      */
DECL|method|shareUnitOfWork ()
specifier|public
name|EnrichDefinition
name|shareUnitOfWork
parameter_list|()
block|{
name|setShareUnitOfWork
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the maximum size used by the {@link org.apache.camel.impl.ConsumerCache} which is used      * to cache and reuse consumers when using this pollEnrich, when uris are reused.      *      * @param cacheSize  the cache size, use<tt>0</tt> for default cache size, or<tt>-1</tt> to turn cache off.      * @return the builder      */
DECL|method|cacheSize (int cacheSize)
specifier|public
name|EnrichDefinition
name|cacheSize
parameter_list|(
name|int
name|cacheSize
parameter_list|)
block|{
name|setCacheSize
argument_list|(
name|cacheSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Ignore the invalidate endpoint exception when try to create a producer with that endpoint      *      * @return the builder      */
DECL|method|ignoreInvalidEndpoint ()
specifier|public
name|EnrichDefinition
name|ignoreInvalidEndpoint
parameter_list|()
block|{
name|setIgnoreInvalidEndpoint
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
comment|/**      * Expression that computes the endpoint uri to use as the resource endpoint to enrich from      */
annotation|@
name|Override
DECL|method|setExpression (ExpressionDefinition expression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|ExpressionDefinition
name|expression
parameter_list|)
block|{
comment|// override to include javadoc what the expression is used for
name|super
operator|.
name|setExpression
argument_list|(
name|expression
argument_list|)
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
DECL|method|getAggregateOnException ()
specifier|public
name|Boolean
name|getAggregateOnException
parameter_list|()
block|{
return|return
name|aggregateOnException
return|;
block|}
DECL|method|setAggregateOnException (Boolean aggregateOnException)
specifier|public
name|void
name|setAggregateOnException
parameter_list|(
name|Boolean
name|aggregateOnException
parameter_list|)
block|{
name|this
operator|.
name|aggregateOnException
operator|=
name|aggregateOnException
expr_stmt|;
block|}
DECL|method|getShareUnitOfWork ()
specifier|public
name|Boolean
name|getShareUnitOfWork
parameter_list|()
block|{
return|return
name|shareUnitOfWork
return|;
block|}
DECL|method|setShareUnitOfWork (Boolean shareUnitOfWork)
specifier|public
name|void
name|setShareUnitOfWork
parameter_list|(
name|Boolean
name|shareUnitOfWork
parameter_list|)
block|{
name|this
operator|.
name|shareUnitOfWork
operator|=
name|shareUnitOfWork
expr_stmt|;
block|}
DECL|method|getCacheSize ()
specifier|public
name|Integer
name|getCacheSize
parameter_list|()
block|{
return|return
name|cacheSize
return|;
block|}
DECL|method|setCacheSize (Integer cacheSize)
specifier|public
name|void
name|setCacheSize
parameter_list|(
name|Integer
name|cacheSize
parameter_list|)
block|{
name|this
operator|.
name|cacheSize
operator|=
name|cacheSize
expr_stmt|;
block|}
DECL|method|getIgnoreInvalidEndpoint ()
specifier|public
name|Boolean
name|getIgnoreInvalidEndpoint
parameter_list|()
block|{
return|return
name|ignoreInvalidEndpoint
return|;
block|}
DECL|method|setIgnoreInvalidEndpoint (Boolean ignoreInvalidEndpoint)
specifier|public
name|void
name|setIgnoreInvalidEndpoint
parameter_list|(
name|Boolean
name|ignoreInvalidEndpoint
parameter_list|)
block|{
name|this
operator|.
name|ignoreInvalidEndpoint
operator|=
name|ignoreInvalidEndpoint
expr_stmt|;
block|}
block|}
end_class

end_unit

