begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|ProducerCache
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * A client helper object (named like Spring's TransactionTemplate& JmsTemplate  * et al) for working with Camel and sending {@link Message} instances in an  * {@link Exchange} to an {@link Endpoint}.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|CamelTemplate
specifier|public
class|class
name|CamelTemplate
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
extends|extends
name|ServiceSupport
implements|implements
name|ProducerTemplate
argument_list|<
name|E
argument_list|>
block|{
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
DECL|field|producerCache
specifier|private
specifier|final
name|ProducerCache
argument_list|<
name|E
argument_list|>
name|producerCache
init|=
operator|new
name|ProducerCache
argument_list|<
name|E
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|useEndpointCache
specifier|private
name|boolean
name|useEndpointCache
init|=
literal|true
decl_stmt|;
DECL|field|endpointCache
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Endpoint
argument_list|<
name|E
argument_list|>
argument_list|>
name|endpointCache
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Endpoint
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|defaultEndpoint
specifier|private
name|Endpoint
argument_list|<
name|E
argument_list|>
name|defaultEndpoint
decl_stmt|;
DECL|method|CamelTemplate (CamelContext context)
specifier|public
name|CamelTemplate
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
block|}
DECL|method|CamelTemplate (CamelContext context, Endpoint defaultEndpoint)
specifier|public
name|CamelTemplate
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Endpoint
name|defaultEndpoint
parameter_list|)
block|{
name|this
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|defaultEndpoint
operator|=
name|defaultEndpoint
expr_stmt|;
block|}
DECL|method|send (String endpointUri, E exchange)
specifier|public
name|E
name|send
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|E
name|exchange
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
return|return
name|send
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
return|;
block|}
DECL|method|send (String endpointUri, Processor processor)
specifier|public
name|E
name|send
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
return|return
name|send
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|send (String endpointUri, Processor processor, AsyncCallback callback)
specifier|public
name|E
name|send
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
return|return
name|send
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|,
name|callback
argument_list|)
return|;
block|}
DECL|method|send (String endpointUri, ExchangePattern pattern, Processor processor)
specifier|public
name|E
name|send
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
return|return
name|send
argument_list|(
name|endpoint
argument_list|,
name|pattern
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|send (Endpoint<E> endpoint, E exchange)
specifier|public
name|E
name|send
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|,
name|E
name|exchange
parameter_list|)
block|{
comment|//E convertedExchange = endpoint.createExchange(exchange);
name|E
name|convertedExchange
init|=
name|exchange
decl_stmt|;
name|producerCache
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
name|convertedExchange
argument_list|)
expr_stmt|;
return|return
name|convertedExchange
return|;
block|}
DECL|method|send (Endpoint<E> endpoint, Processor processor)
specifier|public
name|E
name|send
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
return|return
name|producerCache
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|send (Endpoint<E> endpoint, Processor processor, AsyncCallback callback)
specifier|public
name|E
name|send
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
return|return
name|producerCache
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|,
name|callback
argument_list|)
return|;
block|}
DECL|method|send (Endpoint<E> endpoint, ExchangePattern pattern, Processor processor)
specifier|public
name|E
name|send
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
return|return
name|producerCache
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
name|pattern
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|sendBody (Endpoint<E> endpoint, ExchangePattern pattern, Object body)
specifier|public
name|Object
name|sendBody
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|Object
name|body
parameter_list|)
block|{
name|E
name|result
init|=
name|send
argument_list|(
name|endpoint
argument_list|,
name|pattern
argument_list|,
name|createSetBodyProcessor
argument_list|(
name|body
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|extractResultBody
argument_list|(
name|result
argument_list|)
return|;
block|}
DECL|method|sendBody (Endpoint<E> endpoint, Object body)
specifier|public
name|Object
name|sendBody
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|)
block|{
name|E
name|result
init|=
name|send
argument_list|(
name|endpoint
argument_list|,
name|createSetBodyProcessor
argument_list|(
name|body
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|extractResultBody
argument_list|(
name|result
argument_list|)
return|;
block|}
DECL|method|sendBody (String endpointUri, Object body)
specifier|public
name|Object
name|sendBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
return|return
name|sendBody
argument_list|(
name|endpoint
argument_list|,
name|body
argument_list|)
return|;
block|}
DECL|method|sendBody (String endpointUri, ExchangePattern pattern, Object body)
specifier|public
name|Object
name|sendBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|Object
name|body
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
return|return
name|sendBody
argument_list|(
name|endpoint
argument_list|,
name|pattern
argument_list|,
name|body
argument_list|)
return|;
block|}
DECL|method|sendBodyAndHeader (String endpointUri, final Object body, final String header, final Object headerValue)
specifier|public
name|Object
name|sendBodyAndHeader
parameter_list|(
name|String
name|endpointUri
parameter_list|,
specifier|final
name|Object
name|body
parameter_list|,
specifier|final
name|String
name|header
parameter_list|,
specifier|final
name|Object
name|headerValue
parameter_list|)
block|{
return|return
name|sendBodyAndHeader
argument_list|(
name|resolveMandatoryEndpoint
argument_list|(
name|endpointUri
argument_list|)
argument_list|,
name|body
argument_list|,
name|header
argument_list|,
name|headerValue
argument_list|)
return|;
block|}
DECL|method|sendBodyAndHeader (Endpoint endpoint, final Object body, final String header, final Object headerValue)
specifier|public
name|Object
name|sendBodyAndHeader
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
specifier|final
name|Object
name|body
parameter_list|,
specifier|final
name|String
name|header
parameter_list|,
specifier|final
name|Object
name|headerValue
parameter_list|)
block|{
name|E
name|result
init|=
name|send
argument_list|(
name|endpoint
argument_list|,
name|createBodyAndHeaderProcessor
argument_list|(
name|body
argument_list|,
name|header
argument_list|,
name|headerValue
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|extractResultBody
argument_list|(
name|result
argument_list|)
return|;
block|}
DECL|method|sendBodyAndHeader (Endpoint endpoint, ExchangePattern pattern, final Object body, final String header, final Object headerValue)
specifier|public
name|Object
name|sendBodyAndHeader
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
specifier|final
name|Object
name|body
parameter_list|,
specifier|final
name|String
name|header
parameter_list|,
specifier|final
name|Object
name|headerValue
parameter_list|)
block|{
name|E
name|result
init|=
name|send
argument_list|(
name|endpoint
argument_list|,
name|pattern
argument_list|,
name|createBodyAndHeaderProcessor
argument_list|(
name|body
argument_list|,
name|header
argument_list|,
name|headerValue
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|extractResultBody
argument_list|(
name|result
argument_list|)
return|;
block|}
DECL|method|sendBodyAndHeader (String endpoint, ExchangePattern pattern, final Object body, final String header, final Object headerValue)
specifier|public
name|Object
name|sendBodyAndHeader
parameter_list|(
name|String
name|endpoint
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
specifier|final
name|Object
name|body
parameter_list|,
specifier|final
name|String
name|header
parameter_list|,
specifier|final
name|Object
name|headerValue
parameter_list|)
block|{
name|E
name|result
init|=
name|send
argument_list|(
name|endpoint
argument_list|,
name|pattern
argument_list|,
name|createBodyAndHeaderProcessor
argument_list|(
name|body
argument_list|,
name|header
argument_list|,
name|headerValue
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|extractResultBody
argument_list|(
name|result
argument_list|)
return|;
block|}
DECL|method|sendBodyAndHeaders (String endpointUri, final Object body, final Map<String, Object> headers)
specifier|public
name|Object
name|sendBodyAndHeaders
parameter_list|(
name|String
name|endpointUri
parameter_list|,
specifier|final
name|Object
name|body
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
block|{
return|return
name|sendBodyAndHeaders
argument_list|(
name|resolveMandatoryEndpoint
argument_list|(
name|endpointUri
argument_list|)
argument_list|,
name|body
argument_list|,
name|headers
argument_list|)
return|;
block|}
DECL|method|sendBodyAndHeaders (Endpoint endpoint, final Object body, final Map<String, Object> headers)
specifier|public
name|Object
name|sendBodyAndHeaders
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
specifier|final
name|Object
name|body
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
block|{
name|E
name|result
init|=
name|send
argument_list|(
name|endpoint
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|header
range|:
name|headers
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|in
operator|.
name|setHeader
argument_list|(
name|header
operator|.
name|getKey
argument_list|()
argument_list|,
name|header
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|in
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|extractResultBody
argument_list|(
name|result
argument_list|)
return|;
block|}
comment|// Methods using an InOut ExchangePattern
comment|// -----------------------------------------------------------------------
DECL|method|request (Endpoint<E> endpoint, Processor processor)
specifier|public
name|E
name|request
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
return|return
name|send
argument_list|(
name|endpoint
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|requestBody (Endpoint<E> endpoint, Object body)
specifier|public
name|Object
name|requestBody
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|)
block|{
return|return
name|sendBody
argument_list|(
name|endpoint
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|body
argument_list|)
return|;
block|}
DECL|method|requestBodyAndHeader (Endpoint<E> endpoint, Object body, String header, Object headerValue)
specifier|public
name|Object
name|requestBodyAndHeader
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|,
name|String
name|header
parameter_list|,
name|Object
name|headerValue
parameter_list|)
block|{
return|return
name|sendBodyAndHeader
argument_list|(
name|endpoint
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|body
argument_list|,
name|header
argument_list|,
name|headerValue
argument_list|)
return|;
block|}
DECL|method|request (String endpoint, Processor processor)
specifier|public
name|E
name|request
parameter_list|(
name|String
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
return|return
name|send
argument_list|(
name|endpoint
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|requestBody (String endpoint, Object body)
specifier|public
name|Object
name|requestBody
parameter_list|(
name|String
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|)
block|{
return|return
name|sendBody
argument_list|(
name|endpoint
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|body
argument_list|)
return|;
block|}
DECL|method|requestBodyAndHeader (String endpoint, Object body, String header, Object headerValue)
specifier|public
name|Object
name|requestBodyAndHeader
parameter_list|(
name|String
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|,
name|String
name|header
parameter_list|,
name|Object
name|headerValue
parameter_list|)
block|{
return|return
name|sendBodyAndHeader
argument_list|(
name|endpoint
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|body
argument_list|,
name|header
argument_list|,
name|headerValue
argument_list|)
return|;
block|}
comment|// Methods using the default endpoint
comment|// -----------------------------------------------------------------------
DECL|method|sendBody (Object body)
specifier|public
name|Object
name|sendBody
parameter_list|(
name|Object
name|body
parameter_list|)
block|{
return|return
name|sendBody
argument_list|(
name|getMandatoryDefaultEndpoint
argument_list|()
argument_list|,
name|body
argument_list|)
return|;
block|}
DECL|method|send (E exchange)
specifier|public
name|E
name|send
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
return|return
name|send
argument_list|(
name|getMandatoryDefaultEndpoint
argument_list|()
argument_list|,
name|exchange
argument_list|)
return|;
block|}
DECL|method|send (Processor processor)
specifier|public
name|E
name|send
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
return|return
name|send
argument_list|(
name|getMandatoryDefaultEndpoint
argument_list|()
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|sendBodyAndHeader (Object body, String header, Object headerValue)
specifier|public
name|Object
name|sendBodyAndHeader
parameter_list|(
name|Object
name|body
parameter_list|,
name|String
name|header
parameter_list|,
name|Object
name|headerValue
parameter_list|)
block|{
return|return
name|sendBodyAndHeader
argument_list|(
name|getMandatoryDefaultEndpoint
argument_list|()
argument_list|,
name|body
argument_list|,
name|header
argument_list|,
name|headerValue
argument_list|)
return|;
block|}
DECL|method|sendBodyAndHeaders (Object body, Map<String, Object> headers)
specifier|public
name|Object
name|sendBodyAndHeaders
parameter_list|(
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
block|{
return|return
name|sendBodyAndHeaders
argument_list|(
name|getMandatoryDefaultEndpoint
argument_list|()
argument_list|,
name|body
argument_list|,
name|headers
argument_list|)
return|;
block|}
comment|// Properties
comment|// -----------------------------------------------------------------------
DECL|method|getProducer (Endpoint<E> endpoint)
specifier|public
name|Producer
argument_list|<
name|E
argument_list|>
name|getProducer
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|)
block|{
return|return
name|producerCache
operator|.
name|getProducer
argument_list|(
name|endpoint
argument_list|)
return|;
block|}
DECL|method|getContext ()
specifier|public
name|CamelContext
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
DECL|method|getDefaultEndpoint ()
specifier|public
name|Endpoint
argument_list|<
name|E
argument_list|>
name|getDefaultEndpoint
parameter_list|()
block|{
return|return
name|defaultEndpoint
return|;
block|}
DECL|method|setDefaultEndpoint (Endpoint<E> defaultEndpoint)
specifier|public
name|void
name|setDefaultEndpoint
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|defaultEndpoint
parameter_list|)
block|{
name|this
operator|.
name|defaultEndpoint
operator|=
name|defaultEndpoint
expr_stmt|;
block|}
comment|/**      * Sets the default endpoint to use if none is specified      */
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
name|getContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
name|endpointUri
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|isUseEndpointCache ()
specifier|public
name|boolean
name|isUseEndpointCache
parameter_list|()
block|{
return|return
name|useEndpointCache
return|;
block|}
DECL|method|setUseEndpointCache (boolean useEndpointCache)
specifier|public
name|void
name|setUseEndpointCache
parameter_list|(
name|boolean
name|useEndpointCache
parameter_list|)
block|{
name|this
operator|.
name|useEndpointCache
operator|=
name|useEndpointCache
expr_stmt|;
block|}
DECL|method|getResolvedEndpoint (String endpointUri, Class<T> expectedClass)
specifier|public
parameter_list|<
name|T
extends|extends
name|Endpoint
argument_list|<
name|?
argument_list|>
parameter_list|>
name|T
name|getResolvedEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|expectedClass
parameter_list|)
block|{
name|Endpoint
argument_list|<
name|?
argument_list|>
name|e
init|=
literal|null
decl_stmt|;
synchronized|synchronized
init|(
name|endpointCache
init|)
block|{
name|e
operator|=
name|endpointCache
operator|.
name|get
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|e
operator|!=
literal|null
operator|&&
name|expectedClass
operator|.
name|isAssignableFrom
argument_list|(
name|e
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|expectedClass
operator|.
name|asSubclass
argument_list|(
name|expectedClass
argument_list|)
operator|.
name|cast
argument_list|(
name|e
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|// Implementation methods
comment|// -----------------------------------------------------------------------
DECL|method|createBodyAndHeaderProcessor (final Object body, final String header, final Object headerValue)
specifier|protected
name|Processor
name|createBodyAndHeaderProcessor
parameter_list|(
specifier|final
name|Object
name|body
parameter_list|,
specifier|final
name|String
name|header
parameter_list|,
specifier|final
name|Object
name|headerValue
parameter_list|)
block|{
return|return
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|header
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
name|in
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|createSetBodyProcessor (final Object body)
specifier|protected
name|Processor
name|createSetBodyProcessor
parameter_list|(
specifier|final
name|Object
name|body
parameter_list|)
block|{
return|return
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|resolveMandatoryEndpoint (String endpointUri)
specifier|protected
name|Endpoint
name|resolveMandatoryEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isUseEndpointCache
argument_list|()
condition|)
block|{
synchronized|synchronized
init|(
name|endpointCache
init|)
block|{
name|endpoint
operator|=
name|endpointCache
operator|.
name|get
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
name|endpoint
operator|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
name|endpointCache
operator|.
name|put
argument_list|(
name|endpointUri
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|endpoint
operator|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchEndpointException
argument_list|(
name|endpointUri
argument_list|)
throw|;
block|}
return|return
name|endpoint
return|;
block|}
DECL|method|getMandatoryDefaultEndpoint ()
specifier|protected
name|Endpoint
argument_list|<
name|E
argument_list|>
name|getMandatoryDefaultEndpoint
parameter_list|()
block|{
name|Endpoint
argument_list|<
name|E
argument_list|>
name|answer
init|=
name|getDefaultEndpoint
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|answer
argument_list|,
literal|"defaultEndpoint"
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|producerCache
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|producerCache
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|extractResultBody (E result)
specifier|protected
name|Object
name|extractResultBody
parameter_list|(
name|E
name|result
parameter_list|)
block|{
name|Object
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
name|Message
name|out
init|=
name|result
operator|.
name|getOut
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|out
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|out
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|result
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

