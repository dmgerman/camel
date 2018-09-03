begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
operator|.
name|http
package|;
end_package

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
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|ChannelHandlerContext
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|FullHttpRequest
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
name|component
operator|.
name|netty4
operator|.
name|NettyConfiguration
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
name|netty4
operator|.
name|NettyEndpoint
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
name|SynchronousDelegateProducer
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
name|StringHelper
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

begin_comment
comment|/**  * Netty HTTP server and client using the Netty 4.x library.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.14.0"
argument_list|,
name|scheme
operator|=
literal|"netty4-http"
argument_list|,
name|extendsScheme
operator|=
literal|"netty4"
argument_list|,
name|title
operator|=
literal|"Netty4 HTTP"
argument_list|,
name|syntax
operator|=
literal|"netty4-http:protocol:host:port/path"
argument_list|,
name|consumerClass
operator|=
name|NettyHttpConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"http"
argument_list|,
name|lenientProperties
operator|=
literal|true
argument_list|,
name|excludeProperties
operator|=
literal|"textline,delimiter,autoAppendDelimiter,decoderMaxLineLength,encoding,allowDefaultCodec,udpConnectionlessSending,networkInterface"
operator|+
literal|",clientMode,reconnect,reconnectInterval,useByteBuf,udpByteArrayCodec,broadcast,correlationManager"
argument_list|)
DECL|class|NettyHttpEndpoint
specifier|public
class|class
name|NettyHttpEndpoint
extends|extends
name|NettyEndpoint
implements|implements
name|AsyncEndpoint
implements|,
name|HeaderFilterStrategyAware
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
name|NettyHttpEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|NettyHttpConfiguration
name|configuration
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|name
operator|=
literal|"configuration"
argument_list|,
name|javaType
operator|=
literal|"org.apache.camel.component.netty4.http.NettyHttpConfiguration"
argument_list|,
name|description
operator|=
literal|"To use a custom configured NettyHttpConfiguration for configuring this endpoint."
argument_list|)
DECL|field|httpConfiguration
specifier|private
name|Object
name|httpConfiguration
decl_stmt|;
comment|// to include in component docs as NettyHttpConfiguration is a @UriParams class
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|nettyHttpBinding
specifier|private
name|NettyHttpBinding
name|nettyHttpBinding
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
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|)
DECL|field|traceEnabled
specifier|private
name|boolean
name|traceEnabled
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
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
literal|"consumer,advanced"
argument_list|)
DECL|field|nettySharedHttpServer
specifier|private
name|NettySharedHttpServer
name|nettySharedHttpServer
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,security"
argument_list|)
DECL|field|securityConfiguration
specifier|private
name|NettyHttpSecurityConfiguration
name|securityConfiguration
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,security"
argument_list|,
name|prefix
operator|=
literal|"securityConfiguration."
argument_list|,
name|multiValue
operator|=
literal|true
argument_list|)
DECL|field|securityOptions
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|securityOptions
decl_stmt|;
comment|// to include in component docs
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
DECL|method|NettyHttpEndpoint (String endpointUri, NettyHttpComponent component, NettyConfiguration configuration)
specifier|public
name|NettyHttpEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|NettyHttpComponent
name|component
parameter_list|,
name|NettyConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|NettyHttpComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|NettyHttpComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
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
name|NettyHttpConsumer
name|answer
init|=
operator|new
name|NettyHttpConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|getConfiguration
argument_list|()
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
if|if
condition|(
name|nettySharedHttpServer
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setNettyServerBootstrapFactory
argument_list|(
name|nettySharedHttpServer
operator|.
name|getServerBootstrapFactory
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"NettyHttpConsumer: {} is using NettySharedHttpServer on port: {}"
argument_list|,
name|answer
argument_list|,
name|nettySharedHttpServer
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// reuse pipeline factory for the same address
name|HttpServerBootstrapFactory
name|factory
init|=
name|getComponent
argument_list|()
operator|.
name|getOrCreateHttpNettyServerBootstrapFactory
argument_list|(
name|answer
argument_list|)
decl_stmt|;
comment|// force using our server bootstrap factory
name|answer
operator|.
name|setNettyServerBootstrapFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Created NettyHttpConsumer: {} using HttpServerBootstrapFactory: {}"
argument_list|,
name|answer
argument_list|,
name|factory
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
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
name|Producer
name|answer
init|=
operator|new
name|NettyHttpProducer
argument_list|(
name|this
argument_list|,
name|getConfiguration
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|isSynchronous
argument_list|()
condition|)
block|{
return|return
operator|new
name|SynchronousDelegateProducer
argument_list|(
name|answer
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|answer
return|;
block|}
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
DECL|method|createExchange (ChannelHandlerContext ctx, Object message)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|Object
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|()
decl_stmt|;
name|FullHttpRequest
name|request
init|=
operator|(
name|FullHttpRequest
operator|)
name|message
decl_stmt|;
name|Message
name|in
init|=
name|getNettyHttpBinding
argument_list|()
operator|.
name|toCamelMessage
argument_list|(
name|request
argument_list|,
name|exchange
argument_list|,
name|getConfiguration
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|in
argument_list|)
expr_stmt|;
comment|// setup the common message headers
name|updateMessageHeader
argument_list|(
name|in
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
comment|// honor the character encoding
name|String
name|contentType
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|charset
init|=
name|NettyHttpHelper
operator|.
name|getCharsetFromContentType
argument_list|(
name|contentType
argument_list|)
decl_stmt|;
if|if
condition|(
name|charset
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|charset
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
name|charset
argument_list|)
expr_stmt|;
block|}
return|return
name|exchange
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
comment|// true to allow dynamic URI options to be configured and passed to external system for eg. the HttpProducer
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|setConfiguration (NettyConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|NettyConfiguration
name|configuration
parameter_list|)
block|{
name|super
operator|.
name|setConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
operator|(
name|NettyHttpConfiguration
operator|)
name|configuration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getConfiguration ()
specifier|public
name|NettyHttpConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
operator|(
name|NettyHttpConfiguration
operator|)
name|super
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
DECL|method|getNettyHttpBinding ()
specifier|public
name|NettyHttpBinding
name|getNettyHttpBinding
parameter_list|()
block|{
return|return
name|nettyHttpBinding
return|;
block|}
comment|/**      * To use a custom org.apache.camel.component.netty4.http.NettyHttpBinding for binding to/from Netty and Camel Message API.      */
DECL|method|setNettyHttpBinding (NettyHttpBinding nettyHttpBinding)
specifier|public
name|void
name|setNettyHttpBinding
parameter_list|(
name|NettyHttpBinding
name|nettyHttpBinding
parameter_list|)
block|{
name|this
operator|.
name|nettyHttpBinding
operator|=
name|nettyHttpBinding
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
comment|/**      * To use a custom org.apache.camel.spi.HeaderFilterStrategy to filter headers.      */
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
name|getNettyHttpBinding
argument_list|()
operator|.
name|setHeaderFilterStrategy
argument_list|(
name|headerFilterStrategy
argument_list|)
expr_stmt|;
block|}
DECL|method|isTraceEnabled ()
specifier|public
name|boolean
name|isTraceEnabled
parameter_list|()
block|{
return|return
name|traceEnabled
return|;
block|}
comment|/**      * Specifies whether to enable HTTP TRACE for this Netty HTTP consumer. By default TRACE is turned off.      */
DECL|method|setTraceEnabled (boolean traceEnabled)
specifier|public
name|void
name|setTraceEnabled
parameter_list|(
name|boolean
name|traceEnabled
parameter_list|)
block|{
name|this
operator|.
name|traceEnabled
operator|=
name|traceEnabled
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
comment|/**      * To disable HTTP methods on the Netty HTTP consumer. You can specify multiple separated by comma.      */
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
DECL|method|getNettySharedHttpServer ()
specifier|public
name|NettySharedHttpServer
name|getNettySharedHttpServer
parameter_list|()
block|{
return|return
name|nettySharedHttpServer
return|;
block|}
comment|/**      * To use a shared Netty HTTP server. See Netty HTTP Server Example for more details.      */
DECL|method|setNettySharedHttpServer (NettySharedHttpServer nettySharedHttpServer)
specifier|public
name|void
name|setNettySharedHttpServer
parameter_list|(
name|NettySharedHttpServer
name|nettySharedHttpServer
parameter_list|)
block|{
name|this
operator|.
name|nettySharedHttpServer
operator|=
name|nettySharedHttpServer
expr_stmt|;
block|}
DECL|method|getSecurityConfiguration ()
specifier|public
name|NettyHttpSecurityConfiguration
name|getSecurityConfiguration
parameter_list|()
block|{
return|return
name|securityConfiguration
return|;
block|}
comment|/**      * Refers to a org.apache.camel.component.netty4.http.NettyHttpSecurityConfiguration for configuring secure web resources.      */
DECL|method|setSecurityConfiguration (NettyHttpSecurityConfiguration securityConfiguration)
specifier|public
name|void
name|setSecurityConfiguration
parameter_list|(
name|NettyHttpSecurityConfiguration
name|securityConfiguration
parameter_list|)
block|{
name|this
operator|.
name|securityConfiguration
operator|=
name|securityConfiguration
expr_stmt|;
block|}
DECL|method|getSecurityOptions ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getSecurityOptions
parameter_list|()
block|{
return|return
name|securityOptions
return|;
block|}
comment|/**      * To configure NettyHttpSecurityConfiguration using key/value pairs from the map      */
DECL|method|setSecurityOptions (Map<String, Object> securityOptions)
specifier|public
name|void
name|setSecurityOptions
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|securityOptions
parameter_list|)
block|{
name|this
operator|.
name|securityOptions
operator|=
name|securityOptions
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|nettyHttpBinding
argument_list|,
literal|"nettyHttpBinding"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|headerFilterStrategy
argument_list|,
literal|"headerFilterStrategy"
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|securityConfiguration
operator|!=
literal|null
condition|)
block|{
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|securityConfiguration
operator|.
name|getRealm
argument_list|()
argument_list|,
literal|"realm"
argument_list|,
name|securityConfiguration
argument_list|)
expr_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|securityConfiguration
operator|.
name|getConstraint
argument_list|()
argument_list|,
literal|"restricted"
argument_list|,
name|securityConfiguration
argument_list|)
expr_stmt|;
if|if
condition|(
name|securityConfiguration
operator|.
name|getSecurityAuthenticator
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// setup default JAAS authenticator if none was configured
name|JAASSecurityAuthenticator
name|jaas
init|=
operator|new
name|JAASSecurityAuthenticator
argument_list|()
decl_stmt|;
name|jaas
operator|.
name|setName
argument_list|(
name|securityConfiguration
operator|.
name|getRealm
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"No SecurityAuthenticator configured, using JAASSecurityAuthenticator as authenticator: {}"
argument_list|,
name|jaas
argument_list|)
expr_stmt|;
name|securityConfiguration
operator|.
name|setSecurityAuthenticator
argument_list|(
name|jaas
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

