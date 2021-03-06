begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
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
name|spi
operator|.
name|Metadata
import|;
end_import

begin_comment
comment|/**  * Enriches messages with data polled from a secondary resource  *  * @see org.apache.camel.processor.Enricher  */
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
name|ExpressionNode
block|{
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"-1"
argument_list|)
DECL|field|timeout
specifier|private
name|String
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
name|String
name|aggregationStrategyMethodAllowNull
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|aggregateOnException
specifier|private
name|String
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
DECL|field|cacheSize
specifier|private
name|String
name|cacheSize
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|ignoreInvalidEndpoint
specifier|private
name|String
name|ignoreInvalidEndpoint
decl_stmt|;
DECL|method|PollEnrichDefinition ()
specifier|public
name|PollEnrichDefinition
parameter_list|()
block|{     }
DECL|method|PollEnrichDefinition (AggregationStrategy aggregationStrategy, long timeout)
specifier|public
name|PollEnrichDefinition
parameter_list|(
name|AggregationStrategy
name|aggregationStrategy
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
name|timeout
operator|=
name|Long
operator|.
name|toString
argument_list|(
name|timeout
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
literal|"PollEnrich["
operator|+
name|getExpression
argument_list|()
operator|+
literal|"]"
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
name|getExpression
argument_list|()
operator|+
literal|"]"
return|;
block|}
comment|// Fluent API
comment|// -------------------------------------------------------------------------
comment|/**      * Timeout in millis when polling from the external service.      *<p/>      * The timeout has influence about the poll enrich behavior. It basically      * operations in three different modes:      *<ul>      *<li>negative value - Waits until a message is available and then returns      * it. Warning that this method could block indefinitely if no messages are      * available.</li>      *<li>0 - Attempts to receive a message exchange immediately without      * waiting and returning<tt>null</tt> if a message exchange is not      * available yet.</li>      *<li>positive value - Attempts to receive a message exchange, waiting up      * to the given timeout to expire if a message is not yet available. Returns      *<tt>null</tt> if timed out</li>      *</ul>      * The default value is -1 and therefore the method could block      * indefinitely, and therefore its recommended to use a timeout value      */
DECL|method|timeout (long timeout)
specifier|public
name|PollEnrichDefinition
name|timeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|setTimeout
argument_list|(
name|Long
operator|.
name|toString
argument_list|(
name|timeout
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the AggregationStrategy to be used to merge the reply from the      * external service, into a single outgoing message. By default Camel will      * use the reply from the external service as outgoing message.      */
DECL|method|aggregationStrategy (AggregationStrategy aggregationStrategy)
specifier|public
name|PollEnrichDefinition
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
comment|/**      * Sets the AggregationStrategy to be used to merge the reply from the      * external service, into a single outgoing message. By default Camel will      * use the reply from the external service as outgoing message.      */
DECL|method|aggregationStrategy (Supplier<AggregationStrategy> aggregationStrategy)
specifier|public
name|PollEnrichDefinition
name|aggregationStrategy
parameter_list|(
name|Supplier
argument_list|<
name|AggregationStrategy
argument_list|>
name|aggregationStrategy
parameter_list|)
block|{
name|setAggregationStrategy
argument_list|(
name|aggregationStrategy
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Refers to an AggregationStrategy to be used to merge the reply from the      * external service, into a single outgoing message. By default Camel will      * use the reply from the external service as outgoing message.      */
DECL|method|aggregationStrategyRef (String aggregationStrategyRef)
specifier|public
name|PollEnrichDefinition
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
comment|/**      * This option can be used to explicit declare the method name to use, when      * using POJOs as the AggregationStrategy.      */
DECL|method|aggregationStrategyMethodName (String aggregationStrategyMethodName)
specifier|public
name|PollEnrichDefinition
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
comment|/**      * If this option is false then the aggregate method is not used if there      * was no data to enrich. If this option is true then null values is used as      * the oldExchange (when no data to enrich), when using POJOs as the      * AggregationStrategy.      */
DECL|method|aggregationStrategyMethodAllowNull (boolean aggregationStrategyMethodAllowNull)
specifier|public
name|PollEnrichDefinition
name|aggregationStrategyMethodAllowNull
parameter_list|(
name|boolean
name|aggregationStrategyMethodAllowNull
parameter_list|)
block|{
name|setAggregationStrategyMethodAllowNull
argument_list|(
name|Boolean
operator|.
name|toString
argument_list|(
name|aggregationStrategyMethodAllowNull
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * If this option is false then the aggregate method is not used if there      * was an exception thrown while trying to retrieve the data to enrich from      * the resource. Setting this option to true allows end users to control      * what to do if there was an exception in the aggregate method. For example      * to suppress the exception or set a custom message body etc.      */
DECL|method|aggregateOnException (boolean aggregateOnException)
specifier|public
name|PollEnrichDefinition
name|aggregateOnException
parameter_list|(
name|boolean
name|aggregateOnException
parameter_list|)
block|{
name|setAggregateOnException
argument_list|(
name|Boolean
operator|.
name|toString
argument_list|(
name|aggregateOnException
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the maximum size used by the      * {@link org.apache.camel.spi.ConsumerCache} which is used to cache and      * reuse consumers when uris are reused.      *      * @param cacheSize the cache size, use<tt>0</tt> for default cache size,      *            or<tt>-1</tt> to turn cache off.      * @return the builder      */
DECL|method|cacheSize (int cacheSize)
specifier|public
name|PollEnrichDefinition
name|cacheSize
parameter_list|(
name|int
name|cacheSize
parameter_list|)
block|{
name|setCacheSize
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|cacheSize
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Ignore the invalidate endpoint exception when try to create a producer      * with that endpoint      *      * @return the builder      */
DECL|method|ignoreInvalidEndpoint ()
specifier|public
name|PollEnrichDefinition
name|ignoreInvalidEndpoint
parameter_list|()
block|{
name|setIgnoreInvalidEndpoint
argument_list|(
name|Boolean
operator|.
name|toString
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
comment|/**      * Expression that computes the endpoint uri to use as the resource endpoint      * to enrich from      */
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
DECL|method|getTimeout ()
specifier|public
name|String
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|method|setTimeout (String timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|String
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
name|String
name|getAggregationStrategyMethodAllowNull
parameter_list|()
block|{
return|return
name|aggregationStrategyMethodAllowNull
return|;
block|}
DECL|method|setAggregationStrategyMethodAllowNull (String aggregationStrategyMethodAllowNull)
specifier|public
name|void
name|setAggregationStrategyMethodAllowNull
parameter_list|(
name|String
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
name|String
name|getAggregateOnException
parameter_list|()
block|{
return|return
name|aggregateOnException
return|;
block|}
DECL|method|setAggregateOnException (String aggregateOnException)
specifier|public
name|void
name|setAggregateOnException
parameter_list|(
name|String
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
DECL|method|getCacheSize ()
specifier|public
name|String
name|getCacheSize
parameter_list|()
block|{
return|return
name|cacheSize
return|;
block|}
DECL|method|setCacheSize (String cacheSize)
specifier|public
name|void
name|setCacheSize
parameter_list|(
name|String
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
name|String
name|getIgnoreInvalidEndpoint
parameter_list|()
block|{
return|return
name|ignoreInvalidEndpoint
return|;
block|}
DECL|method|setIgnoreInvalidEndpoint (String ignoreInvalidEndpoint)
specifier|public
name|void
name|setIgnoreInvalidEndpoint
parameter_list|(
name|String
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

