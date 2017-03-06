begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
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
name|Future
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
name|Consumer
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
name|CamelExecutionException
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
name|FluentProducerTemplate
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
name|Message
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
name|ProducerTemplate
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
name|ConvertBodyProcessor
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
name|ServiceSupport
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
name|util
operator|.
name|ObjectHelper
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
name|ServiceHelper
import|;
end_import

begin_class
DECL|class|DefaultFluentProducerTemplate
specifier|public
class|class
name|DefaultFluentProducerTemplate
extends|extends
name|ServiceSupport
implements|implements
name|FluentProducerTemplate
block|{
DECL|field|context
specifier|private
specifier|final
name|CamelContext
name|context
decl_stmt|;
DECL|field|resultProcessors
specifier|private
specifier|final
name|ClassValue
argument_list|<
name|ConvertBodyProcessor
argument_list|>
name|resultProcessors
decl_stmt|;
DECL|field|headers
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
decl_stmt|;
DECL|field|body
specifier|private
name|Object
name|body
decl_stmt|;
DECL|field|templateCustomizer
specifier|private
name|Optional
argument_list|<
name|Consumer
argument_list|<
name|ProducerTemplate
argument_list|>
argument_list|>
name|templateCustomizer
decl_stmt|;
DECL|field|exchangeSupplier
specifier|private
name|Optional
argument_list|<
name|Supplier
argument_list|<
name|Exchange
argument_list|>
argument_list|>
name|exchangeSupplier
decl_stmt|;
DECL|field|processorSupplier
specifier|private
name|Optional
argument_list|<
name|Supplier
argument_list|<
name|Processor
argument_list|>
argument_list|>
name|processorSupplier
decl_stmt|;
DECL|field|endpoint
specifier|private
name|Optional
argument_list|<
name|Endpoint
argument_list|>
name|endpoint
decl_stmt|;
DECL|field|defaultEndpoint
specifier|private
name|Optional
argument_list|<
name|Endpoint
argument_list|>
name|defaultEndpoint
decl_stmt|;
DECL|field|maximumCacheSize
specifier|private
name|int
name|maximumCacheSize
decl_stmt|;
DECL|field|eventNotifierEnabled
specifier|private
name|boolean
name|eventNotifierEnabled
decl_stmt|;
DECL|field|template
specifier|private
specifier|volatile
name|ProducerTemplate
name|template
decl_stmt|;
DECL|method|DefaultFluentProducerTemplate (CamelContext context)
specifier|public
name|DefaultFluentProducerTemplate
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|Optional
operator|.
name|empty
argument_list|()
expr_stmt|;
name|this
operator|.
name|defaultEndpoint
operator|=
name|Optional
operator|.
name|empty
argument_list|()
expr_stmt|;
name|this
operator|.
name|eventNotifierEnabled
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|templateCustomizer
operator|=
name|Optional
operator|.
name|empty
argument_list|()
expr_stmt|;
name|this
operator|.
name|exchangeSupplier
operator|=
name|Optional
operator|.
name|empty
argument_list|()
expr_stmt|;
name|this
operator|.
name|processorSupplier
operator|=
name|Optional
operator|.
name|empty
argument_list|()
expr_stmt|;
name|this
operator|.
name|resultProcessors
operator|=
operator|new
name|ClassValue
argument_list|<
name|ConvertBodyProcessor
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|ConvertBodyProcessor
name|computeValue
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|new
name|ConvertBodyProcessor
argument_list|(
name|type
argument_list|)
return|;
block|}
block|}
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
annotation|@
name|Override
DECL|method|getCurrentCacheSize ()
specifier|public
name|int
name|getCurrentCacheSize
parameter_list|()
block|{
if|if
condition|(
name|template
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
name|template
operator|.
name|getCurrentCacheSize
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|cleanUp ()
specifier|public
name|void
name|cleanUp
parameter_list|()
block|{
if|if
condition|(
name|template
operator|!=
literal|null
condition|)
block|{
name|template
operator|.
name|cleanUp
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|setDefaultEndpointUri (String endpointUri)
specifier|public
name|void
name|setDefaultEndpointUri
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|setDefaultEndpoint
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
name|endpointUri
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDefaultEndpoint ()
specifier|public
name|Endpoint
name|getDefaultEndpoint
parameter_list|()
block|{
return|return
name|defaultEndpoint
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setDefaultEndpoint (Endpoint defaultEndpoint)
specifier|public
name|void
name|setDefaultEndpoint
parameter_list|(
name|Endpoint
name|defaultEndpoint
parameter_list|)
block|{
name|this
operator|.
name|defaultEndpoint
operator|=
name|Optional
operator|.
name|of
argument_list|(
name|defaultEndpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getMaximumCacheSize ()
specifier|public
name|int
name|getMaximumCacheSize
parameter_list|()
block|{
return|return
name|maximumCacheSize
return|;
block|}
annotation|@
name|Override
DECL|method|setMaximumCacheSize (int maximumCacheSize)
specifier|public
name|void
name|setMaximumCacheSize
parameter_list|(
name|int
name|maximumCacheSize
parameter_list|)
block|{
name|this
operator|.
name|maximumCacheSize
operator|=
name|maximumCacheSize
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isEventNotifierEnabled ()
specifier|public
name|boolean
name|isEventNotifierEnabled
parameter_list|()
block|{
return|return
name|eventNotifierEnabled
return|;
block|}
annotation|@
name|Override
DECL|method|setEventNotifierEnabled (boolean eventNotifierEnabled)
specifier|public
name|void
name|setEventNotifierEnabled
parameter_list|(
name|boolean
name|eventNotifierEnabled
parameter_list|)
block|{
name|this
operator|.
name|eventNotifierEnabled
operator|=
name|eventNotifierEnabled
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|withHeader (String key, Object value)
specifier|public
name|FluentProducerTemplate
name|withHeader
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|headers
operator|==
literal|null
condition|)
block|{
name|headers
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|headers
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|clearHeaders ()
specifier|public
name|FluentProducerTemplate
name|clearHeaders
parameter_list|()
block|{
if|if
condition|(
name|headers
operator|!=
literal|null
condition|)
block|{
name|headers
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|withBody (Object body)
specifier|public
name|FluentProducerTemplate
name|withBody
parameter_list|(
name|Object
name|body
parameter_list|)
block|{
name|this
operator|.
name|body
operator|=
name|body
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|withBodyAs (Object body, Class<?> type)
specifier|public
name|FluentProducerTemplate
name|withBodyAs
parameter_list|(
name|Object
name|body
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|this
operator|.
name|body
operator|=
name|type
operator|!=
literal|null
condition|?
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|body
argument_list|)
else|:
name|body
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|clearBody ()
specifier|public
name|FluentProducerTemplate
name|clearBody
parameter_list|()
block|{
name|this
operator|.
name|body
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|withTemplateCustomizer (final Consumer<ProducerTemplate> templateCustomizer)
specifier|public
name|FluentProducerTemplate
name|withTemplateCustomizer
parameter_list|(
specifier|final
name|Consumer
argument_list|<
name|ProducerTemplate
argument_list|>
name|templateCustomizer
parameter_list|)
block|{
name|this
operator|.
name|templateCustomizer
operator|=
name|Optional
operator|.
name|of
argument_list|(
name|templateCustomizer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|withExchange (final Exchange exchange)
specifier|public
name|FluentProducerTemplate
name|withExchange
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|withExchange
argument_list|(
parameter_list|()
lambda|->
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|withExchange (final Supplier<Exchange> exchangeSupplier)
specifier|public
name|FluentProducerTemplate
name|withExchange
parameter_list|(
specifier|final
name|Supplier
argument_list|<
name|Exchange
argument_list|>
name|exchangeSupplier
parameter_list|)
block|{
name|this
operator|.
name|exchangeSupplier
operator|=
name|Optional
operator|.
name|of
argument_list|(
name|exchangeSupplier
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|withProcessor (final Processor processor)
specifier|public
name|FluentProducerTemplate
name|withProcessor
parameter_list|(
specifier|final
name|Processor
name|processor
parameter_list|)
block|{
return|return
name|withProcessor
argument_list|(
parameter_list|()
lambda|->
name|processor
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|withProcessor (final Supplier<Processor> processorSupplier)
specifier|public
name|FluentProducerTemplate
name|withProcessor
parameter_list|(
specifier|final
name|Supplier
argument_list|<
name|Processor
argument_list|>
name|processorSupplier
parameter_list|)
block|{
name|this
operator|.
name|processorSupplier
operator|=
name|Optional
operator|.
name|of
argument_list|(
name|processorSupplier
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|to (String endpointUri)
specifier|public
name|FluentProducerTemplate
name|to
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
return|return
name|to
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
name|endpointUri
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|to (Endpoint endpoint)
specifier|public
name|FluentProducerTemplate
name|to
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|Optional
operator|.
name|of
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// ************************
comment|// REQUEST
comment|// ************************
annotation|@
name|Override
DECL|method|request ()
specifier|public
name|Object
name|request
parameter_list|()
throws|throws
name|CamelExecutionException
block|{
return|return
name|request
argument_list|(
name|Object
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|request (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|request
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|CamelExecutionException
block|{
comment|// Determine the target endpoint
specifier|final
name|Endpoint
name|target
init|=
name|target
argument_list|()
decl_stmt|;
comment|// Create the default processor if not provided.
specifier|final
name|Supplier
argument_list|<
name|Processor
argument_list|>
name|processorSupplier
init|=
name|this
operator|.
name|processorSupplier
operator|.
name|orElse
argument_list|(
parameter_list|()
lambda|->
name|defaultProcessor
argument_list|()
argument_list|)
decl_stmt|;
name|T
name|result
decl_stmt|;
if|if
condition|(
name|type
operator|==
name|Exchange
operator|.
name|class
condition|)
block|{
name|result
operator|=
operator|(
name|T
operator|)
name|template
argument_list|()
operator|.
name|request
argument_list|(
name|target
argument_list|,
name|processorSupplier
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|Message
operator|.
name|class
condition|)
block|{
name|Exchange
name|exchange
init|=
name|template
argument_list|()
operator|.
name|request
argument_list|(
name|target
argument_list|,
name|processorSupplier
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|result
operator|=
name|exchange
operator|.
name|hasOut
argument_list|()
condition|?
operator|(
name|T
operator|)
name|exchange
operator|.
name|getOut
argument_list|()
else|:
operator|(
name|T
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Exchange
name|exchange
init|=
name|template
argument_list|()
operator|.
name|send
argument_list|(
name|target
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|processorSupplier
operator|.
name|get
argument_list|()
argument_list|,
name|resultProcessors
operator|.
name|get
argument_list|(
name|type
argument_list|)
argument_list|)
decl_stmt|;
name|result
operator|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|ExchangeHelper
operator|.
name|extractResultBody
argument_list|(
name|exchange
argument_list|,
name|exchange
operator|.
name|getPattern
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|asyncRequest ()
specifier|public
name|Future
argument_list|<
name|Object
argument_list|>
name|asyncRequest
parameter_list|()
block|{
return|return
name|asyncRequest
argument_list|(
name|Object
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|asyncRequest (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Future
argument_list|<
name|T
argument_list|>
name|asyncRequest
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
comment|// Determine the target endpoint
specifier|final
name|Endpoint
name|target
init|=
name|target
argument_list|()
decl_stmt|;
name|Future
argument_list|<
name|T
argument_list|>
name|result
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|headers
argument_list|)
condition|)
block|{
comment|// Make a copy of the headers and body so that async processing won't
comment|// be invalidated by subsequent reuse of the template
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headersCopy
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|headers
argument_list|)
decl_stmt|;
specifier|final
name|Object
name|bodyCopy
init|=
name|body
decl_stmt|;
name|result
operator|=
name|template
argument_list|()
operator|.
name|asyncRequestBodyAndHeaders
argument_list|(
name|target
argument_list|,
name|bodyCopy
argument_list|,
name|headersCopy
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Make a copy of the and body so that async processing won't be
comment|// invalidated by subsequent reuse of the template
specifier|final
name|Object
name|bodyCopy
init|=
name|body
decl_stmt|;
name|result
operator|=
name|template
argument_list|()
operator|.
name|asyncRequestBody
argument_list|(
name|target
argument_list|,
name|bodyCopy
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|// ************************
comment|// SEND
comment|// ************************
annotation|@
name|Override
DECL|method|send ()
specifier|public
name|Exchange
name|send
parameter_list|()
throws|throws
name|CamelExecutionException
block|{
comment|// Determine the target endpoint
specifier|final
name|Endpoint
name|target
init|=
name|target
argument_list|()
decl_stmt|;
return|return
name|exchangeSupplier
operator|.
name|isPresent
argument_list|()
condition|?
name|template
argument_list|()
operator|.
name|send
argument_list|(
name|target
argument_list|,
name|exchangeSupplier
operator|.
name|get
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
else|:
name|template
argument_list|()
operator|.
name|send
argument_list|(
name|target
argument_list|,
name|processorSupplier
operator|.
name|orElse
argument_list|(
parameter_list|()
lambda|->
name|defaultProcessor
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|asyncSend ()
specifier|public
name|Future
argument_list|<
name|Exchange
argument_list|>
name|asyncSend
parameter_list|()
block|{
comment|// Determine the target endpoint
specifier|final
name|Endpoint
name|target
init|=
name|target
argument_list|()
decl_stmt|;
return|return
name|exchangeSupplier
operator|.
name|isPresent
argument_list|()
condition|?
name|template
argument_list|()
operator|.
name|asyncSend
argument_list|(
name|target
argument_list|,
name|exchangeSupplier
operator|.
name|get
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
else|:
name|template
argument_list|()
operator|.
name|asyncSend
argument_list|(
name|target
argument_list|,
name|processorSupplier
operator|.
name|orElse
argument_list|(
parameter_list|()
lambda|->
name|defaultAsyncProcessor
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
comment|// ************************
comment|// HELPERS
comment|// ************************
comment|/**      * Create the FluentProducerTemplate by setting the camel context      *      * @param context the camel context      */
DECL|method|on (CamelContext context)
specifier|public
specifier|static
name|FluentProducerTemplate
name|on
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|DefaultFluentProducerTemplate
argument_list|(
name|context
argument_list|)
return|;
block|}
DECL|method|template ()
specifier|private
name|ProducerTemplate
name|template
parameter_list|()
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|context
argument_list|,
literal|"CamelContext"
argument_list|)
expr_stmt|;
if|if
condition|(
name|template
operator|==
literal|null
condition|)
block|{
name|template
operator|=
name|maximumCacheSize
operator|>
literal|0
condition|?
name|context
operator|.
name|createProducerTemplate
argument_list|(
name|maximumCacheSize
argument_list|)
else|:
name|context
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
name|defaultEndpoint
operator|.
name|ifPresent
argument_list|(
name|template
operator|::
name|setDefaultEndpoint
argument_list|)
expr_stmt|;
name|template
operator|.
name|setEventNotifierEnabled
argument_list|(
name|eventNotifierEnabled
argument_list|)
expr_stmt|;
name|templateCustomizer
operator|.
name|ifPresent
argument_list|(
name|tc
lambda|->
name|tc
operator|.
name|accept
argument_list|(
name|template
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|template
return|;
block|}
DECL|method|defaultProcessor ()
specifier|private
name|Processor
name|defaultProcessor
parameter_list|()
block|{
return|return
name|exchange
lambda|->
block|{
name|ObjectHelper
operator|.
name|ifNotEmpty
argument_list|(
name|headers
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|::
name|putAll
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|ifNotEmpty
argument_list|(
name|body
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|::
name|setBody
argument_list|)
expr_stmt|;
block|}
return|;
block|}
DECL|method|defaultAsyncProcessor ()
specifier|private
name|Processor
name|defaultAsyncProcessor
parameter_list|()
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headersCopy
init|=
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|this
operator|.
name|headers
argument_list|)
condition|?
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|this
operator|.
name|headers
argument_list|)
else|:
literal|null
decl_stmt|;
specifier|final
name|Object
name|bodyCopy
init|=
name|this
operator|.
name|body
decl_stmt|;
return|return
name|exchange
lambda|->
block|{
name|ObjectHelper
operator|.
name|ifNotEmpty
argument_list|(
name|headersCopy
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|::
name|putAll
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|ifNotEmpty
argument_list|(
name|bodyCopy
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|::
name|setBody
argument_list|)
expr_stmt|;
block|}
return|;
block|}
DECL|method|target ()
specifier|private
name|Endpoint
name|target
parameter_list|()
block|{
if|if
condition|(
name|endpoint
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
name|endpoint
operator|.
name|get
argument_list|()
return|;
block|}
if|if
condition|(
name|defaultEndpoint
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
name|defaultEndpoint
operator|.
name|get
argument_list|()
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No endpoint configured on FluentProducerTemplate. You can configure an endpoint with to(uri)"
argument_list|)
throw|;
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
if|if
condition|(
name|template
operator|==
literal|null
condition|)
block|{
name|template
operator|=
name|template
argument_list|()
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|template
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
name|template
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

