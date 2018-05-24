begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.undertow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|server
operator|.
name|HttpServerExchange
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
name|AsyncEndpoint
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
name|Consumer
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
name|PollingConsumer
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
name|cloud
operator|.
name|DiscoverableService
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
name|cloud
operator|.
name|ServiceDefinition
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
name|component
operator|.
name|undertow
operator|.
name|UndertowConstants
operator|.
name|EventType
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
name|component
operator|.
name|undertow
operator|.
name|handlers
operator|.
name|CamelWebSocketHandler
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
name|http
operator|.
name|common
operator|.
name|cookie
operator|.
name|CookieHandler
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
name|DefaultEndpoint
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
name|HeaderFilterStrategy
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
name|HeaderFilterStrategyAware
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
name|UriEndpoint
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
name|UriParam
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
name|UriPath
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
name|CollectionHelper
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
name|jsse
operator|.
name|SSLContextParameters
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xnio
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xnio
operator|.
name|OptionMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xnio
operator|.
name|Options
import|;
end_import

begin_comment
comment|/**  * The undertow component provides HTTP and WebSocket based endpoints for consuming and producing HTTP/WebSocket requests.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.16.0"
argument_list|,
name|scheme
operator|=
literal|"undertow"
argument_list|,
name|title
operator|=
literal|"Undertow"
argument_list|,
name|syntax
operator|=
literal|"undertow:httpURI"
argument_list|,
name|consumerClass
operator|=
name|UndertowConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"http,websocket"
argument_list|,
name|lenientProperties
operator|=
literal|true
argument_list|)
DECL|class|UndertowEndpoint
specifier|public
class|class
name|UndertowEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|AsyncEndpoint
implements|,
name|HeaderFilterStrategyAware
implements|,
name|DiscoverableService
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|UndertowEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|component
specifier|private
name|UndertowComponent
name|component
decl_stmt|;
DECL|field|sslContext
specifier|private
name|SSLContext
name|sslContext
decl_stmt|;
DECL|field|optionMap
specifier|private
name|OptionMap
name|optionMap
decl_stmt|;
DECL|field|registrationInfo
specifier|private
name|HttpHandlerRegistrationInfo
name|registrationInfo
decl_stmt|;
DECL|field|webSocketHttpHandler
specifier|private
name|CamelWebSocketHandler
name|webSocketHttpHandler
decl_stmt|;
DECL|field|isWebSocket
specifier|private
name|boolean
name|isWebSocket
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|httpURI
specifier|private
name|URI
name|httpURI
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|undertowHttpBinding
specifier|private
name|UndertowHttpBinding
name|undertowHttpBinding
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
init|=
operator|new
name|UndertowHeaderFilterStrategy
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|httpMethodRestrict
specifier|private
name|String
name|httpMethodRestrict
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|matchOnUriPrefix
specifier|private
name|Boolean
name|matchOnUriPrefix
init|=
name|Boolean
operator|.
name|FALSE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|throwExceptionOnFailure
specifier|private
name|Boolean
name|throwExceptionOnFailure
init|=
name|Boolean
operator|.
name|TRUE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|transferException
specifier|private
name|Boolean
name|transferException
init|=
name|Boolean
operator|.
name|FALSE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|keepAlive
specifier|private
name|Boolean
name|keepAlive
init|=
name|Boolean
operator|.
name|TRUE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|tcpNoDelay
specifier|private
name|Boolean
name|tcpNoDelay
init|=
name|Boolean
operator|.
name|TRUE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|reuseAddresses
specifier|private
name|Boolean
name|reuseAddresses
init|=
name|Boolean
operator|.
name|TRUE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|prefix
operator|=
literal|"option."
argument_list|,
name|multiValue
operator|=
literal|true
argument_list|)
DECL|field|options
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|optionsEnabled
specifier|private
name|boolean
name|optionsEnabled
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|cookieHandler
specifier|private
name|CookieHandler
name|cookieHandler
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer,websocket"
argument_list|)
DECL|field|sendToAll
specifier|private
name|Boolean
name|sendToAll
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer,websocket"
argument_list|,
name|defaultValue
operator|=
literal|"30000"
argument_list|)
DECL|field|sendTimeout
specifier|private
name|Integer
name|sendTimeout
init|=
literal|30000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,websocket"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|useStreaming
specifier|private
name|boolean
name|useStreaming
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,websocket"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|fireWebSocketChannelEvents
specifier|private
name|boolean
name|fireWebSocketChannelEvents
decl_stmt|;
DECL|method|UndertowEndpoint (String uri, UndertowComponent component)
specifier|public
name|UndertowEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|UndertowComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|UndertowComponent
name|getComponent
parameter_list|()
block|{
return|return
name|component
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|UndertowProducer
argument_list|(
name|this
argument_list|,
name|optionMap
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|UndertowConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createPollingConsumer ()
specifier|public
name|PollingConsumer
name|createPollingConsumer
parameter_list|()
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"This component does not support polling consumer"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|isLenientProperties ()
specifier|public
name|boolean
name|isLenientProperties
parameter_list|()
block|{
comment|// true to allow dynamic URI options to be configured and passed to external system for eg. the UndertowProducer
return|return
literal|true
return|;
block|}
comment|// Service Registration
comment|//-------------------------------------------------------------------------
annotation|@
name|Override
DECL|method|getServiceProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getServiceProperties
parameter_list|()
block|{
return|return
name|CollectionHelper
operator|.
name|immutableMapOf
argument_list|(
name|ServiceDefinition
operator|.
name|SERVICE_META_PORT
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|httpURI
operator|.
name|getPort
argument_list|()
argument_list|)
argument_list|,
name|ServiceDefinition
operator|.
name|SERVICE_META_PATH
argument_list|,
name|httpURI
operator|.
name|getPath
argument_list|()
argument_list|,
name|ServiceDefinition
operator|.
name|SERVICE_META_PROTOCOL
argument_list|,
name|httpURI
operator|.
name|getScheme
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createExchange (HttpServerExchange httpExchange)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|HttpServerExchange
name|httpExchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
name|Message
name|in
init|=
name|getUndertowHttpBinding
argument_list|()
operator|.
name|toCamelMessage
argument_list|(
name|httpExchange
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|httpExchange
operator|.
name|getRequestCharset
argument_list|()
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_CHARACTER_ENCODING
argument_list|,
name|httpExchange
operator|.
name|getRequestCharset
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|in
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|getSslContext ()
specifier|public
name|SSLContext
name|getSslContext
parameter_list|()
block|{
return|return
name|sslContext
return|;
block|}
DECL|method|getHttpURI ()
specifier|public
name|URI
name|getHttpURI
parameter_list|()
block|{
return|return
name|httpURI
return|;
block|}
comment|/**      * The url of the HTTP endpoint to use.      */
DECL|method|setHttpURI (URI httpURI)
specifier|public
name|void
name|setHttpURI
parameter_list|(
name|URI
name|httpURI
parameter_list|)
block|{
name|this
operator|.
name|httpURI
operator|=
name|UndertowHelper
operator|.
name|makeHttpURI
argument_list|(
name|httpURI
argument_list|)
expr_stmt|;
block|}
DECL|method|getHttpMethodRestrict ()
specifier|public
name|String
name|getHttpMethodRestrict
parameter_list|()
block|{
return|return
name|httpMethodRestrict
return|;
block|}
comment|/**      * Used to only allow consuming if the HttpMethod matches, such as GET/POST/PUT etc. Multiple methods can be specified separated by comma.      */
DECL|method|setHttpMethodRestrict (String httpMethodRestrict)
specifier|public
name|void
name|setHttpMethodRestrict
parameter_list|(
name|String
name|httpMethodRestrict
parameter_list|)
block|{
name|this
operator|.
name|httpMethodRestrict
operator|=
name|httpMethodRestrict
expr_stmt|;
block|}
DECL|method|getMatchOnUriPrefix ()
specifier|public
name|Boolean
name|getMatchOnUriPrefix
parameter_list|()
block|{
return|return
name|matchOnUriPrefix
return|;
block|}
comment|/**      * Whether or not the consumer should try to find a target consumer by matching the URI prefix if no exact match is found.      */
DECL|method|setMatchOnUriPrefix (Boolean matchOnUriPrefix)
specifier|public
name|void
name|setMatchOnUriPrefix
parameter_list|(
name|Boolean
name|matchOnUriPrefix
parameter_list|)
block|{
name|this
operator|.
name|matchOnUriPrefix
operator|=
name|matchOnUriPrefix
expr_stmt|;
block|}
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
comment|/**      * To use a custom HeaderFilterStrategy to filter header to and from Camel message.      */
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy headerFilterStrategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
name|headerFilterStrategy
expr_stmt|;
block|}
DECL|method|getSslContextParameters ()
specifier|public
name|SSLContextParameters
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
comment|/**      * To configure security using SSLContextParameters      */
DECL|method|setSslContextParameters (SSLContextParameters sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|SSLContextParameters
name|sslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|sslContextParameters
operator|=
name|sslContextParameters
expr_stmt|;
block|}
DECL|method|getThrowExceptionOnFailure ()
specifier|public
name|Boolean
name|getThrowExceptionOnFailure
parameter_list|()
block|{
return|return
name|throwExceptionOnFailure
return|;
block|}
comment|/**      * Option to disable throwing the HttpOperationFailedException in case of failed responses from the remote server.      * This allows you to get all responses regardless of the HTTP status code.      */
DECL|method|setThrowExceptionOnFailure (Boolean throwExceptionOnFailure)
specifier|public
name|void
name|setThrowExceptionOnFailure
parameter_list|(
name|Boolean
name|throwExceptionOnFailure
parameter_list|)
block|{
name|this
operator|.
name|throwExceptionOnFailure
operator|=
name|throwExceptionOnFailure
expr_stmt|;
block|}
DECL|method|getTransferException ()
specifier|public
name|Boolean
name|getTransferException
parameter_list|()
block|{
return|return
name|transferException
return|;
block|}
comment|/**      * If enabled and an Exchange failed processing on the consumer side and if the caused Exception       * was send back serialized in the response as a application/x-java-serialized-object content type.       * On the producer side the exception will be deserialized and thrown as is instead of the HttpOperationFailedException. The caused exception is required to be serialized.       * This is by default turned off. If you enable this       * then be aware that Java will deserialize the incoming data from the request to Java and that can be a potential security risk.      *       */
DECL|method|setTransferException (Boolean transferException)
specifier|public
name|void
name|setTransferException
parameter_list|(
name|Boolean
name|transferException
parameter_list|)
block|{
name|this
operator|.
name|transferException
operator|=
name|transferException
expr_stmt|;
block|}
DECL|method|getUndertowHttpBinding ()
specifier|public
name|UndertowHttpBinding
name|getUndertowHttpBinding
parameter_list|()
block|{
if|if
condition|(
name|undertowHttpBinding
operator|==
literal|null
condition|)
block|{
comment|// create a new binding and use the options from this endpoint
name|undertowHttpBinding
operator|=
operator|new
name|DefaultUndertowHttpBinding
argument_list|()
expr_stmt|;
name|undertowHttpBinding
operator|.
name|setHeaderFilterStrategy
argument_list|(
name|getHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|undertowHttpBinding
operator|.
name|setTransferException
argument_list|(
name|getTransferException
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|undertowHttpBinding
return|;
block|}
comment|/**      * To use a custom UndertowHttpBinding to control the mapping between Camel message and undertow.      */
DECL|method|setUndertowHttpBinding (UndertowHttpBinding undertowHttpBinding)
specifier|public
name|void
name|setUndertowHttpBinding
parameter_list|(
name|UndertowHttpBinding
name|undertowHttpBinding
parameter_list|)
block|{
name|this
operator|.
name|undertowHttpBinding
operator|=
name|undertowHttpBinding
expr_stmt|;
block|}
DECL|method|getKeepAlive ()
specifier|public
name|Boolean
name|getKeepAlive
parameter_list|()
block|{
return|return
name|keepAlive
return|;
block|}
comment|/**      * Setting to ensure socket is not closed due to inactivity      */
DECL|method|setKeepAlive (Boolean keepAlive)
specifier|public
name|void
name|setKeepAlive
parameter_list|(
name|Boolean
name|keepAlive
parameter_list|)
block|{
name|this
operator|.
name|keepAlive
operator|=
name|keepAlive
expr_stmt|;
block|}
DECL|method|getTcpNoDelay ()
specifier|public
name|Boolean
name|getTcpNoDelay
parameter_list|()
block|{
return|return
name|tcpNoDelay
return|;
block|}
comment|/**      * Setting to improve TCP protocol performance      */
DECL|method|setTcpNoDelay (Boolean tcpNoDelay)
specifier|public
name|void
name|setTcpNoDelay
parameter_list|(
name|Boolean
name|tcpNoDelay
parameter_list|)
block|{
name|this
operator|.
name|tcpNoDelay
operator|=
name|tcpNoDelay
expr_stmt|;
block|}
DECL|method|getReuseAddresses ()
specifier|public
name|Boolean
name|getReuseAddresses
parameter_list|()
block|{
return|return
name|reuseAddresses
return|;
block|}
comment|/**      * Setting to facilitate socket multiplexing      */
DECL|method|setReuseAddresses (Boolean reuseAddresses)
specifier|public
name|void
name|setReuseAddresses
parameter_list|(
name|Boolean
name|reuseAddresses
parameter_list|)
block|{
name|this
operator|.
name|reuseAddresses
operator|=
name|reuseAddresses
expr_stmt|;
block|}
DECL|method|getOptions ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getOptions
parameter_list|()
block|{
return|return
name|options
return|;
block|}
comment|/**      * Sets additional channel options. The options that can be used are defined in {@link org.xnio.Options}.      * To configure from endpoint uri, then prefix each option with<tt>option.</tt>, such as<tt>option.close-abort=true&option.send-buffer=8192</tt>      */
DECL|method|setOptions (Map<String, Object> options)
specifier|public
name|void
name|setOptions
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
block|{
name|this
operator|.
name|options
operator|=
name|options
expr_stmt|;
block|}
DECL|method|isOptionsEnabled ()
specifier|public
name|boolean
name|isOptionsEnabled
parameter_list|()
block|{
return|return
name|optionsEnabled
return|;
block|}
comment|/**      * Specifies whether to enable HTTP OPTIONS for this Servlet consumer. By default OPTIONS is turned off.      */
DECL|method|setOptionsEnabled (boolean optionsEnabled)
specifier|public
name|void
name|setOptionsEnabled
parameter_list|(
name|boolean
name|optionsEnabled
parameter_list|)
block|{
name|this
operator|.
name|optionsEnabled
operator|=
name|optionsEnabled
expr_stmt|;
block|}
DECL|method|getCookieHandler ()
specifier|public
name|CookieHandler
name|getCookieHandler
parameter_list|()
block|{
return|return
name|cookieHandler
return|;
block|}
comment|/**      * Configure a cookie handler to maintain a HTTP session      */
DECL|method|setCookieHandler (CookieHandler cookieHandler)
specifier|public
name|void
name|setCookieHandler
parameter_list|(
name|CookieHandler
name|cookieHandler
parameter_list|)
block|{
name|this
operator|.
name|cookieHandler
operator|=
name|cookieHandler
expr_stmt|;
block|}
DECL|method|getSendToAll ()
specifier|public
name|Boolean
name|getSendToAll
parameter_list|()
block|{
return|return
name|sendToAll
return|;
block|}
comment|/**      * To send to all websocket subscribers. Can be used to configure on endpoint level, instead of having to use the      * {@code UndertowConstants.SEND_TO_ALL} header on the message.      */
DECL|method|setSendToAll (Boolean sendToAll)
specifier|public
name|void
name|setSendToAll
parameter_list|(
name|Boolean
name|sendToAll
parameter_list|)
block|{
name|this
operator|.
name|sendToAll
operator|=
name|sendToAll
expr_stmt|;
block|}
DECL|method|getSendTimeout ()
specifier|public
name|Integer
name|getSendTimeout
parameter_list|()
block|{
return|return
name|sendTimeout
return|;
block|}
comment|/**      * Timeout in milliseconds when sending to a websocket channel.      * The default timeout is 30000 (30 seconds).      */
DECL|method|setSendTimeout (Integer sendTimeout)
specifier|public
name|void
name|setSendTimeout
parameter_list|(
name|Integer
name|sendTimeout
parameter_list|)
block|{
name|this
operator|.
name|sendTimeout
operator|=
name|sendTimeout
expr_stmt|;
block|}
DECL|method|isUseStreaming ()
specifier|public
name|boolean
name|isUseStreaming
parameter_list|()
block|{
return|return
name|useStreaming
return|;
block|}
comment|/**      * if {@code true}, text and binary messages coming through a WebSocket will be wrapped as java.io.Reader and      * java.io.InputStream respectively before they are passed to an {@link Exchange}; otherwise they will be passed as      * String and byte[] respectively.      */
DECL|method|setUseStreaming (boolean useStreaming)
specifier|public
name|void
name|setUseStreaming
parameter_list|(
name|boolean
name|useStreaming
parameter_list|)
block|{
name|this
operator|.
name|useStreaming
operator|=
name|useStreaming
expr_stmt|;
block|}
DECL|method|isFireWebSocketChannelEvents ()
specifier|public
name|boolean
name|isFireWebSocketChannelEvents
parameter_list|()
block|{
return|return
name|fireWebSocketChannelEvents
return|;
block|}
comment|/**      * if {@code true}, the consumer will post notifications to the route when a new WebSocket peer connects,      * disconnects, etc. See {@code UndertowConstants.EVENT_TYPE} and {@link EventType}.      */
DECL|method|setFireWebSocketChannelEvents (boolean fireWebSocketChannelEvents)
specifier|public
name|void
name|setFireWebSocketChannelEvents
parameter_list|(
name|boolean
name|fireWebSocketChannelEvents
parameter_list|)
block|{
name|this
operator|.
name|fireWebSocketChannelEvents
operator|=
name|fireWebSocketChannelEvents
expr_stmt|;
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
specifier|final
name|String
name|scheme
init|=
name|httpURI
operator|.
name|getScheme
argument_list|()
decl_stmt|;
name|this
operator|.
name|isWebSocket
operator|=
name|UndertowConstants
operator|.
name|WS_PROTOCOL
operator|.
name|equalsIgnoreCase
argument_list|(
name|scheme
argument_list|)
operator|||
name|UndertowConstants
operator|.
name|WSS_PROTOCOL
operator|.
name|equalsIgnoreCase
argument_list|(
name|scheme
argument_list|)
expr_stmt|;
if|if
condition|(
name|sslContextParameters
operator|!=
literal|null
condition|)
block|{
name|sslContext
operator|=
name|sslContextParameters
operator|.
name|createSSLContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// create options map
if|if
condition|(
name|options
operator|!=
literal|null
operator|&&
operator|!
name|options
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// favor to use the classloader that loaded the user application
name|ClassLoader
name|cl
init|=
name|getComponent
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getApplicationContextClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|cl
operator|==
literal|null
condition|)
block|{
name|cl
operator|=
name|Options
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
expr_stmt|;
block|}
name|OptionMap
operator|.
name|Builder
name|builder
init|=
name|OptionMap
operator|.
name|builder
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
name|entry
range|:
name|options
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
operator|&&
name|value
operator|!=
literal|null
condition|)
block|{
comment|// upper case and dash as underscore
name|key
operator|=
name|key
operator|.
name|toUpperCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
operator|.
name|replace
argument_list|(
literal|'-'
argument_list|,
literal|'_'
argument_list|)
expr_stmt|;
comment|// must be field name
name|key
operator|=
name|Options
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|key
expr_stmt|;
name|Option
name|option
init|=
name|Option
operator|.
name|fromString
argument_list|(
name|key
argument_list|,
name|cl
argument_list|)
decl_stmt|;
name|value
operator|=
name|option
operator|.
name|parseValue
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|,
name|cl
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Parsed option {}={}"
argument_list|,
name|option
operator|.
name|getName
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|builder
operator|.
name|set
argument_list|(
name|option
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
name|optionMap
operator|=
name|builder
operator|.
name|getMap
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// use an empty map
name|optionMap
operator|=
name|OptionMap
operator|.
name|EMPTY
expr_stmt|;
block|}
comment|// and then configure these default options if they have not been explicit configured
if|if
condition|(
name|keepAlive
operator|!=
literal|null
operator|&&
operator|!
name|optionMap
operator|.
name|contains
argument_list|(
name|Options
operator|.
name|KEEP_ALIVE
argument_list|)
condition|)
block|{
comment|// rebuild map
name|OptionMap
operator|.
name|Builder
name|builder
init|=
name|OptionMap
operator|.
name|builder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|addAll
argument_list|(
name|optionMap
argument_list|)
operator|.
name|set
argument_list|(
name|Options
operator|.
name|KEEP_ALIVE
argument_list|,
name|keepAlive
argument_list|)
expr_stmt|;
name|optionMap
operator|=
name|builder
operator|.
name|getMap
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|tcpNoDelay
operator|!=
literal|null
operator|&&
operator|!
name|optionMap
operator|.
name|contains
argument_list|(
name|Options
operator|.
name|TCP_NODELAY
argument_list|)
condition|)
block|{
comment|// rebuild map
name|OptionMap
operator|.
name|Builder
name|builder
init|=
name|OptionMap
operator|.
name|builder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|addAll
argument_list|(
name|optionMap
argument_list|)
operator|.
name|set
argument_list|(
name|Options
operator|.
name|TCP_NODELAY
argument_list|,
name|tcpNoDelay
argument_list|)
expr_stmt|;
name|optionMap
operator|=
name|builder
operator|.
name|getMap
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|reuseAddresses
operator|!=
literal|null
operator|&&
operator|!
name|optionMap
operator|.
name|contains
argument_list|(
name|Options
operator|.
name|REUSE_ADDRESSES
argument_list|)
condition|)
block|{
comment|// rebuild map
name|OptionMap
operator|.
name|Builder
name|builder
init|=
name|OptionMap
operator|.
name|builder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|addAll
argument_list|(
name|optionMap
argument_list|)
operator|.
name|set
argument_list|(
name|Options
operator|.
name|REUSE_ADDRESSES
argument_list|,
name|reuseAddresses
argument_list|)
expr_stmt|;
name|optionMap
operator|=
name|builder
operator|.
name|getMap
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * @return {@code true} if {@link #getHttpURI()}'s scheme is {@code ws} or {@code wss}      */
DECL|method|isWebSocket ()
specifier|public
name|boolean
name|isWebSocket
parameter_list|()
block|{
return|return
name|isWebSocket
return|;
block|}
DECL|method|getHttpHandlerRegistrationInfo ()
specifier|public
name|HttpHandlerRegistrationInfo
name|getHttpHandlerRegistrationInfo
parameter_list|()
block|{
if|if
condition|(
name|registrationInfo
operator|==
literal|null
condition|)
block|{
name|registrationInfo
operator|=
operator|new
name|HttpHandlerRegistrationInfo
argument_list|(
name|getHttpURI
argument_list|()
argument_list|,
name|getHttpMethodRestrict
argument_list|()
argument_list|,
name|getMatchOnUriPrefix
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|registrationInfo
return|;
block|}
DECL|method|getWebSocketHttpHandler ()
specifier|public
name|CamelWebSocketHandler
name|getWebSocketHttpHandler
parameter_list|()
block|{
if|if
condition|(
name|webSocketHttpHandler
operator|==
literal|null
condition|)
block|{
name|webSocketHttpHandler
operator|=
operator|new
name|CamelWebSocketHandler
argument_list|()
expr_stmt|;
block|}
return|return
name|webSocketHttpHandler
return|;
block|}
block|}
end_class

end_unit

