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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

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
name|XmlElement
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
name|XmlElementRef
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
name|ExchangePattern
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
name|spi
operator|.
name|Metadata
import|;
end_import

begin_comment
comment|/**  * Routes a copy of a message (or creates a new message) to a secondary  * destination while continue routing the original message.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"eip,endpoint,routing"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"wireTap"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|WireTapDefinition
specifier|public
class|class
name|WireTapDefinition
parameter_list|<
name|Type
extends|extends
name|ProcessorDefinition
parameter_list|<
name|Type
parameter_list|>
parameter_list|>
extends|extends
name|ToDynamicDefinition
implements|implements
name|ExecutorServiceAwareDefinition
argument_list|<
name|WireTapDefinition
argument_list|<
name|Type
argument_list|>
argument_list|>
block|{
annotation|@
name|XmlTransient
DECL|field|newExchangeProcessor
specifier|private
name|Processor
name|newExchangeProcessor
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"processorRef"
argument_list|)
DECL|field|newExchangeProcessorRef
specifier|private
name|String
name|newExchangeProcessorRef
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"body"
argument_list|)
DECL|field|newExchangeExpression
specifier|private
name|ExpressionSubElementDefinition
name|newExchangeExpression
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|field|headers
specifier|private
name|List
argument_list|<
name|SetHeaderDefinition
argument_list|>
name|headers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|executorServiceRef
specifier|private
name|String
name|executorServiceRef
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|copy
specifier|private
name|Boolean
name|copy
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|dynamicUri
specifier|private
name|Boolean
name|dynamicUri
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|onPrepareRef
specifier|private
name|String
name|onPrepareRef
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|onPrepare
specifier|private
name|Processor
name|onPrepare
decl_stmt|;
DECL|method|WireTapDefinition ()
specifier|public
name|WireTapDefinition
parameter_list|()
block|{     }
DECL|method|isDynamic ()
specifier|public
name|boolean
name|isDynamic
parameter_list|()
block|{
comment|// its dynamic by default
return|return
name|dynamicUri
operator|==
literal|null
operator|||
name|dynamicUri
return|;
block|}
annotation|@
name|Override
DECL|method|getPattern ()
specifier|public
name|ExchangePattern
name|getPattern
parameter_list|()
block|{
return|return
name|ExchangePattern
operator|.
name|InOnly
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
literal|"WireTap["
operator|+
name|getUri
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
literal|"wireTap"
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
literal|"wireTap["
operator|+
name|getUri
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|end ()
specifier|public
name|Type
name|end
parameter_list|()
block|{
comment|// allow end() to return to previous type so you can continue in the DSL
return|return
operator|(
name|Type
operator|)
name|super
operator|.
name|end
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|addOutput (ProcessorDefinition<?> output)
specifier|public
name|void
name|addOutput
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|output
parameter_list|)
block|{
comment|// add outputs on parent as this wiretap does not support outputs
name|getParent
argument_list|()
operator|.
name|addOutput
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
comment|// Fluent API
comment|// -------------------------------------------------------------------------
comment|/**      * Uses a custom thread pool      *      * @param executorService a custom {@link ExecutorService} to use as thread      *            pool for sending tapped exchanges      * @return the builder      */
annotation|@
name|Override
DECL|method|executorService (ExecutorService executorService)
specifier|public
name|WireTapDefinition
argument_list|<
name|Type
argument_list|>
name|executorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
name|setExecutorService
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Uses a custom thread pool      *      * @param executorServiceRef reference to lookup a custom      *            {@link ExecutorService} to use as thread pool for sending      *            tapped exchanges      * @return the builder      */
annotation|@
name|Override
DECL|method|executorServiceRef (String executorServiceRef)
specifier|public
name|WireTapDefinition
argument_list|<
name|Type
argument_list|>
name|executorServiceRef
parameter_list|(
name|String
name|executorServiceRef
parameter_list|)
block|{
name|setExecutorServiceRef
argument_list|(
name|executorServiceRef
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Uses a copy of the original exchange      *      * @return the builder      */
DECL|method|copy ()
specifier|public
name|WireTapDefinition
argument_list|<
name|Type
argument_list|>
name|copy
parameter_list|()
block|{
name|setCopy
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Uses a copy of the original exchange      *      * @param copy if it is true camel will copy the original exchange, if it is      *            false camel will not copy the original exchange      * @return the builder      */
DECL|method|copy (boolean copy)
specifier|public
name|WireTapDefinition
argument_list|<
name|Type
argument_list|>
name|copy
parameter_list|(
name|boolean
name|copy
parameter_list|)
block|{
name|setCopy
argument_list|(
name|copy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether the uri is dynamic or static. If the uri is dynamic then the      * simple language is used to evaluate a dynamic uri to use as the wire-tap      * destination, for each incoming message. This works similar to how the      *<tt>toD</tt> EIP pattern works. If static then the uri is used as-is as      * the wire-tap destination.      *      * @param dynamicUri whether to use dynamic or static uris      * @return the builder      */
DECL|method|dynamicUri (boolean dynamicUri)
specifier|public
name|WireTapDefinition
argument_list|<
name|Type
argument_list|>
name|dynamicUri
parameter_list|(
name|boolean
name|dynamicUri
parameter_list|)
block|{
name|setDynamicUri
argument_list|(
name|dynamicUri
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sends a<i>new</i> Exchange, instead of tapping an existing, using      * {@link ExchangePattern#InOnly}      *      * @param expression expression that creates the new body to send      * @return the builder      * @see #newExchangeHeader(String, org.apache.camel.Expression)      */
DECL|method|newExchangeBody (Expression expression)
specifier|public
name|WireTapDefinition
argument_list|<
name|Type
argument_list|>
name|newExchangeBody
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|setNewExchangeExpression
argument_list|(
operator|new
name|ExpressionSubElementDefinition
argument_list|(
name|expression
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sends a<i>new</i> Exchange, instead of tapping an existing, using      * {@link ExchangePattern#InOnly}      *      * @param ref reference to the {@link Processor} to lookup in the      *            {@link org.apache.camel.spi.Registry} to be used for preparing      *            the new exchange to send      * @return the builder      */
DECL|method|newExchangeRef (String ref)
specifier|public
name|WireTapDefinition
argument_list|<
name|Type
argument_list|>
name|newExchangeRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|setNewExchangeProcessorRef
argument_list|(
name|ref
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sends a<i>new</i> Exchange, instead of tapping an existing, using      * {@link ExchangePattern#InOnly}      *      * @param processor processor preparing the new exchange to send      * @return the builder      * @see #newExchangeHeader(String, org.apache.camel.Expression)      */
DECL|method|newExchange (Processor processor)
specifier|public
name|WireTapDefinition
argument_list|<
name|Type
argument_list|>
name|newExchange
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|setNewExchangeProcessor
argument_list|(
name|processor
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sends a<i>new</i> Exchange, instead of tapping an existing, using      * {@link ExchangePattern#InOnly}      *      * @param processor processor preparing the new exchange to send      * @return the builder      * @see #newExchangeHeader(String, org.apache.camel.Expression)      */
DECL|method|newExchange (Supplier<Processor> processor)
specifier|public
name|WireTapDefinition
argument_list|<
name|Type
argument_list|>
name|newExchange
parameter_list|(
name|Supplier
argument_list|<
name|Processor
argument_list|>
name|processor
parameter_list|)
block|{
name|setNewExchangeProcessor
argument_list|(
name|processor
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets a header on the<i>new</i> Exchange, instead of tapping an existing,      * using {@link ExchangePattern#InOnly}.      *<p/>      * Use this together with the      * {@link #newExchangeBody(org.apache.camel.Expression)} or      * {@link #newExchange(org.apache.camel.Processor)} methods.      *      * @param headerName the header name      * @param expression the expression setting the header value      * @return the builder      */
DECL|method|newExchangeHeader (String headerName, Expression expression)
specifier|public
name|WireTapDefinition
argument_list|<
name|Type
argument_list|>
name|newExchangeHeader
parameter_list|(
name|String
name|headerName
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
name|headers
operator|.
name|add
argument_list|(
operator|new
name|SetHeaderDefinition
argument_list|(
name|headerName
argument_list|,
name|expression
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Uses the {@link Processor} when preparing the      * {@link org.apache.camel.Exchange} to be send. This can be used to      * deep-clone messages that should be send, or any custom logic needed      * before the exchange is send.      *      * @param onPrepare the processor      * @return the builder      */
DECL|method|onPrepare (Processor onPrepare)
specifier|public
name|WireTapDefinition
argument_list|<
name|Type
argument_list|>
name|onPrepare
parameter_list|(
name|Processor
name|onPrepare
parameter_list|)
block|{
name|setOnPrepare
argument_list|(
name|onPrepare
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Uses the {@link Processor} when preparing the      * {@link org.apache.camel.Exchange} to be send. This can be used to      * deep-clone messages that should be send, or any custom logic needed      * before the exchange is send.      *      * @param onPrepare the processor      * @return the builder      */
DECL|method|onPrepare (Supplier<Processor> onPrepare)
specifier|public
name|WireTapDefinition
argument_list|<
name|Type
argument_list|>
name|onPrepare
parameter_list|(
name|Supplier
argument_list|<
name|Processor
argument_list|>
name|onPrepare
parameter_list|)
block|{
name|setOnPrepare
argument_list|(
name|onPrepare
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Uses the {@link Processor} when preparing the      * {@link org.apache.camel.Exchange} to be send. This can be used to      * deep-clone messages that should be send, or any custom logic needed      * before the exchange is send.      *      * @param onPrepareRef reference to the processor to lookup in the      *            {@link org.apache.camel.spi.Registry}      * @return the builder      */
DECL|method|onPrepareRef (String onPrepareRef)
specifier|public
name|WireTapDefinition
argument_list|<
name|Type
argument_list|>
name|onPrepareRef
parameter_list|(
name|String
name|onPrepareRef
parameter_list|)
block|{
name|setOnPrepareRef
argument_list|(
name|onPrepareRef
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the maximum size used by the      * {@link org.apache.camel.spi.ProducerCache} which is used to cache and      * reuse producers, when uris are reused.      *      * @param cacheSize the cache size, use<tt>0</tt> for default cache size,      *            or<tt>-1</tt> to turn cache off.      * @return the builder      */
annotation|@
name|Override
DECL|method|cacheSize (int cacheSize)
specifier|public
name|WireTapDefinition
argument_list|<
name|Type
argument_list|>
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
comment|/**      * Ignore the invalidate endpoint exception when try to create a producer      * with that endpoint      *      * @return the builder      */
annotation|@
name|Override
DECL|method|ignoreInvalidEndpoint ()
specifier|public
name|WireTapDefinition
argument_list|<
name|Type
argument_list|>
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
annotation|@
name|Override
DECL|method|getUri ()
specifier|public
name|String
name|getUri
parameter_list|()
block|{
return|return
name|super
operator|.
name|getUri
argument_list|()
return|;
block|}
comment|/**      * The uri of the endpoint to wiretap to. The uri can be dynamic computed      * using the {@link org.apache.camel.language.simple.SimpleLanguage}      * expression.      */
annotation|@
name|Override
DECL|method|setUri (String uri)
specifier|public
name|void
name|setUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|super
operator|.
name|setUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
DECL|method|getNewExchangeProcessor ()
specifier|public
name|Processor
name|getNewExchangeProcessor
parameter_list|()
block|{
return|return
name|newExchangeProcessor
return|;
block|}
comment|/**      * To use a Processor for creating a new body as the message to use for wire      * tapping      */
DECL|method|setNewExchangeProcessor (Processor processor)
specifier|public
name|void
name|setNewExchangeProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|this
operator|.
name|newExchangeProcessor
operator|=
name|processor
expr_stmt|;
block|}
DECL|method|getNewExchangeProcessorRef ()
specifier|public
name|String
name|getNewExchangeProcessorRef
parameter_list|()
block|{
return|return
name|newExchangeProcessorRef
return|;
block|}
comment|/**      * Reference to a Processor to use for creating a new body as the message to      * use for wire tapping      */
DECL|method|setNewExchangeProcessorRef (String ref)
specifier|public
name|void
name|setNewExchangeProcessorRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|this
operator|.
name|newExchangeProcessorRef
operator|=
name|ref
expr_stmt|;
block|}
DECL|method|getNewExchangeExpression ()
specifier|public
name|ExpressionSubElementDefinition
name|getNewExchangeExpression
parameter_list|()
block|{
return|return
name|newExchangeExpression
return|;
block|}
comment|/**      * Uses the expression for creating a new body as the message to use for      * wire tapping      */
DECL|method|setNewExchangeExpression (ExpressionSubElementDefinition newExchangeExpression)
specifier|public
name|void
name|setNewExchangeExpression
parameter_list|(
name|ExpressionSubElementDefinition
name|newExchangeExpression
parameter_list|)
block|{
name|this
operator|.
name|newExchangeExpression
operator|=
name|newExchangeExpression
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getExecutorService ()
specifier|public
name|ExecutorService
name|getExecutorService
parameter_list|()
block|{
return|return
name|executorService
return|;
block|}
annotation|@
name|Override
DECL|method|setExecutorService (ExecutorService executorService)
specifier|public
name|void
name|setExecutorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
name|this
operator|.
name|executorService
operator|=
name|executorService
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getExecutorServiceRef ()
specifier|public
name|String
name|getExecutorServiceRef
parameter_list|()
block|{
return|return
name|executorServiceRef
return|;
block|}
annotation|@
name|Override
DECL|method|setExecutorServiceRef (String executorServiceRef)
specifier|public
name|void
name|setExecutorServiceRef
parameter_list|(
name|String
name|executorServiceRef
parameter_list|)
block|{
name|this
operator|.
name|executorServiceRef
operator|=
name|executorServiceRef
expr_stmt|;
block|}
DECL|method|getCopy ()
specifier|public
name|Boolean
name|getCopy
parameter_list|()
block|{
return|return
name|copy
return|;
block|}
DECL|method|setCopy (Boolean copy)
specifier|public
name|void
name|setCopy
parameter_list|(
name|Boolean
name|copy
parameter_list|)
block|{
name|this
operator|.
name|copy
operator|=
name|copy
expr_stmt|;
block|}
DECL|method|getDynamicUri ()
specifier|public
name|Boolean
name|getDynamicUri
parameter_list|()
block|{
return|return
name|dynamicUri
return|;
block|}
DECL|method|setDynamicUri (Boolean dynamicUri)
specifier|public
name|void
name|setDynamicUri
parameter_list|(
name|Boolean
name|dynamicUri
parameter_list|)
block|{
name|this
operator|.
name|dynamicUri
operator|=
name|dynamicUri
expr_stmt|;
block|}
DECL|method|getOnPrepareRef ()
specifier|public
name|String
name|getOnPrepareRef
parameter_list|()
block|{
return|return
name|onPrepareRef
return|;
block|}
DECL|method|setOnPrepareRef (String onPrepareRef)
specifier|public
name|void
name|setOnPrepareRef
parameter_list|(
name|String
name|onPrepareRef
parameter_list|)
block|{
name|this
operator|.
name|onPrepareRef
operator|=
name|onPrepareRef
expr_stmt|;
block|}
DECL|method|getOnPrepare ()
specifier|public
name|Processor
name|getOnPrepare
parameter_list|()
block|{
return|return
name|onPrepare
return|;
block|}
DECL|method|setOnPrepare (Processor onPrepare)
specifier|public
name|void
name|setOnPrepare
parameter_list|(
name|Processor
name|onPrepare
parameter_list|)
block|{
name|this
operator|.
name|onPrepare
operator|=
name|onPrepare
expr_stmt|;
block|}
DECL|method|getHeaders ()
specifier|public
name|List
argument_list|<
name|SetHeaderDefinition
argument_list|>
name|getHeaders
parameter_list|()
block|{
return|return
name|headers
return|;
block|}
DECL|method|setHeaders (List<SetHeaderDefinition> headers)
specifier|public
name|void
name|setHeaders
parameter_list|(
name|List
argument_list|<
name|SetHeaderDefinition
argument_list|>
name|headers
parameter_list|)
block|{
name|this
operator|.
name|headers
operator|=
name|headers
expr_stmt|;
block|}
block|}
end_class

end_unit

