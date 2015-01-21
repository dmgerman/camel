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
name|Producer
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
name|CamelInternalProcessor
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
name|WireTapProcessor
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
name|Label
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
name|CamelContextHelper
import|;
end_import

begin_comment
comment|/**  * Routes a copy of a message (or creates a new message) to a secondary destination while continue routing the original message.  */
end_comment

begin_class
annotation|@
name|Label
argument_list|(
literal|"eip,management"
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
name|NoOutputDefinition
argument_list|<
name|WireTapDefinition
argument_list|<
name|Type
argument_list|>
argument_list|>
implements|implements
name|ExecutorServiceAwareDefinition
argument_list|<
name|WireTapDefinition
argument_list|<
name|Type
argument_list|>
argument_list|>
implements|,
name|EndpointRequiredDefinition
block|{
annotation|@
name|XmlAttribute
DECL|field|uri
specifier|protected
name|String
name|uri
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|ref
specifier|protected
name|String
name|ref
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|endpoint
specifier|protected
name|Endpoint
name|endpoint
decl_stmt|;
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
argument_list|<
name|SetHeaderDefinition
argument_list|>
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
DECL|method|WireTapDefinition (String uri)
specifier|public
name|WireTapDefinition
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|setUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
DECL|method|WireTapDefinition (Endpoint endpoint)
specifier|public
name|WireTapDefinition
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|setEndpoint
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpointUri ()
specifier|public
name|String
name|getEndpointUri
parameter_list|()
block|{
if|if
condition|(
name|uri
operator|!=
literal|null
condition|)
block|{
return|return
name|uri
return|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
return|return
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
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
comment|// executor service is mandatory for wire tap
name|boolean
name|shutdownThreadPool
init|=
name|ProcessorDefinitionHelper
operator|.
name|willCreateNewThreadPool
argument_list|(
name|routeContext
argument_list|,
name|this
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|ExecutorService
name|threadPool
init|=
name|ProcessorDefinitionHelper
operator|.
name|getConfiguredExecutorService
argument_list|(
name|routeContext
argument_list|,
literal|"WireTap"
argument_list|,
name|this
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// create the producer to send to the wire tapped endpoint
name|Endpoint
name|endpoint
init|=
name|resolveEndpoint
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
comment|// create error handler we need to use for processing the wire tapped
name|Processor
name|target
init|=
name|wrapInErrorHandler
argument_list|(
name|routeContext
argument_list|,
name|producer
argument_list|)
decl_stmt|;
comment|// and wrap in unit of work
name|String
name|routeId
init|=
name|routeContext
operator|.
name|getRoute
argument_list|()
operator|.
name|idOrCreate
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getNodeIdFactory
argument_list|()
argument_list|)
decl_stmt|;
name|CamelInternalProcessor
name|internal
init|=
operator|new
name|CamelInternalProcessor
argument_list|(
name|target
argument_list|)
decl_stmt|;
name|internal
operator|.
name|addAdvice
argument_list|(
operator|new
name|CamelInternalProcessor
operator|.
name|UnitOfWorkProcessorAdvice
argument_list|(
name|routeId
argument_list|)
argument_list|)
expr_stmt|;
comment|// is true bt default
name|boolean
name|isCopy
init|=
name|getCopy
argument_list|()
operator|==
literal|null
operator|||
name|getCopy
argument_list|()
decl_stmt|;
name|WireTapProcessor
name|answer
init|=
operator|new
name|WireTapProcessor
argument_list|(
name|endpoint
argument_list|,
name|internal
argument_list|,
name|getPattern
argument_list|()
argument_list|,
name|threadPool
argument_list|,
name|shutdownThreadPool
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setCopy
argument_list|(
name|isCopy
argument_list|)
expr_stmt|;
if|if
condition|(
name|newExchangeProcessorRef
operator|!=
literal|null
condition|)
block|{
name|newExchangeProcessor
operator|=
name|routeContext
operator|.
name|mandatoryLookup
argument_list|(
name|newExchangeProcessorRef
argument_list|,
name|Processor
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|newExchangeProcessor
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|addNewExchangeProcessor
argument_list|(
name|newExchangeProcessor
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|newExchangeExpression
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setNewExchangeExpression
argument_list|(
name|newExchangeExpression
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|headers
operator|!=
literal|null
operator|&&
operator|!
name|headers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|SetHeaderDefinition
name|header
range|:
name|headers
control|)
block|{
name|Processor
name|processor
init|=
name|createProcessor
argument_list|(
name|routeContext
argument_list|,
name|header
argument_list|)
decl_stmt|;
name|answer
operator|.
name|addNewExchangeProcessor
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|onPrepareRef
operator|!=
literal|null
condition|)
block|{
name|onPrepare
operator|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|onPrepareRef
argument_list|,
name|Processor
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|onPrepare
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setOnPrepare
argument_list|(
name|onPrepare
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
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
name|description
argument_list|()
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
name|getUri
argument_list|()
argument_list|,
name|getRef
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
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
name|description
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
DECL|method|resolveEndpoint (RouteContext context)
specifier|public
name|Endpoint
name|resolveEndpoint
parameter_list|(
name|RouteContext
name|context
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
return|return
name|context
operator|.
name|resolveEndpoint
argument_list|(
name|getUri
argument_list|()
argument_list|,
name|getRef
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|endpoint
return|;
block|}
block|}
comment|// Fluent API
comment|// -------------------------------------------------------------------------
comment|/**      * Uses a custom thread pool      *      * @param executorService a custom {@link ExecutorService} to use as thread pool      *                        for sending tapped exchanges      * @return the builder      */
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
comment|/**      * Uses a custom thread pool      *      * @param executorServiceRef reference to lookup a custom {@link ExecutorService}      *                           to use as thread pool for sending tapped exchanges      * @return the builder      */
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
comment|/**      * Uses a copy of the original exchange      *      * @param copy if it is true camel will copy the original exchange,      *             if it is false camel will not copy the original exchange       * @return the builder      */
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
comment|/**      * @deprecated will be removed in Camel 3.0 Instead use {@link #newExchangeBody(org.apache.camel.Expression)}      */
annotation|@
name|Deprecated
DECL|method|newExchange (Expression expression)
specifier|public
name|WireTapDefinition
argument_list|<
name|Type
argument_list|>
name|newExchange
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
return|return
name|newExchangeBody
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Sends a<i>new</i> Exchange, instead of tapping an existing, using {@link ExchangePattern#InOnly}      *      * @param expression expression that creates the new body to send      * @return the builder      * @see #newExchangeHeader(String, org.apache.camel.Expression)      */
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
name|expression
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sends a<i>new</i> Exchange, instead of tapping an existing, using {@link ExchangePattern#InOnly}      *      * @param ref reference to the {@link Processor} to lookup in the {@link org.apache.camel.spi.Registry} to      *            be used for preparing the new exchange to send      * @return the builder      */
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
comment|/**      * Sends a<i>new</i> Exchange, instead of tapping an existing, using {@link ExchangePattern#InOnly}      *      * @param processor  processor preparing the new exchange to send      * @return the builder      * @see #newExchangeHeader(String, org.apache.camel.Expression)      */
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
comment|/**      * Sets a header on the<i>new</i> Exchange, instead of tapping an existing, using {@link ExchangePattern#InOnly}.      *<p/>      * Use this together with the {@link #newExchange(org.apache.camel.Expression)} or {@link #newExchange(org.apache.camel.Processor)}      * methods.      *      * @param headerName  the header name      * @param expression  the expression setting the header value      * @return the builder      */
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
comment|/**      * Uses the {@link Processor} when preparing the {@link org.apache.camel.Exchange} to be send.      * This can be used to deep-clone messages that should be send, or any custom logic needed before      * the exchange is send.      *      * @param onPrepare the processor      * @return the builder      */
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
comment|/**      * Uses the {@link Processor} when preparing the {@link org.apache.camel.Exchange} to be send.      * This can be used to deep-clone messages that should be send, or any custom logic needed before      * the exchange is send.      *      * @param onPrepareRef reference to the processor to lookup in the {@link org.apache.camel.spi.Registry}      * @return the builder      */
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
DECL|method|getUri ()
specifier|public
name|String
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
comment|/**      * Uri of the endpoint to use as wire tap      */
DECL|method|setUri (String uri)
specifier|public
name|void
name|setUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
DECL|method|getRef ()
specifier|public
name|String
name|getRef
parameter_list|()
block|{
return|return
name|ref
return|;
block|}
comment|/**      * Reference of the endpoint to use as wire tap      */
DECL|method|setRef (String ref)
specifier|public
name|void
name|setRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|this
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
DECL|method|setEndpoint (Endpoint endpoint)
specifier|public
name|void
name|setEndpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
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
comment|/**      * To use a Processor for creating a new body as the message to use for wire tapping      */
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
comment|/**      * Reference to a Processor to use for creating a new body as the message to use for wire tapping      */
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
comment|/**      * Expression used for creating a new body as the message to use for wire tapping      */
DECL|method|setNewExchangeExpression (ExpressionSubElementDefinition expression)
specifier|public
name|void
name|setNewExchangeExpression
parameter_list|(
name|ExpressionSubElementDefinition
name|expression
parameter_list|)
block|{
name|this
operator|.
name|newExchangeExpression
operator|=
name|expression
expr_stmt|;
block|}
DECL|method|setNewExchangeExpression (Expression expression)
specifier|public
name|void
name|setNewExchangeExpression
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|this
operator|.
name|newExchangeExpression
operator|=
operator|new
name|ExpressionSubElementDefinition
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
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

