begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

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
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

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
name|LoggingLevel
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
name|RuntimeCamelException
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
name|UriParams
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
name|EndpointHelper
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
name|IntrospectionSupport
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
name|jboss
operator|.
name|netty
operator|.
name|buffer
operator|.
name|ChannelBuffer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|channel
operator|.
name|ChannelHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|frame
operator|.
name|Delimiters
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|handler
operator|.
name|ssl
operator|.
name|SslHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|util
operator|.
name|CharsetUtil
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

begin_class
annotation|@
name|UriParams
DECL|class|NettyConfiguration
specifier|public
class|class
name|NettyConfiguration
extends|extends
name|NettyServerBootstrapConfiguration
implements|implements
name|Cloneable
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
name|NettyConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|UriParam
DECL|field|requestTimeout
specifier|private
name|long
name|requestTimeout
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|sync
specifier|private
name|boolean
name|sync
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|textline
specifier|private
name|boolean
name|textline
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"LINE"
argument_list|)
DECL|field|delimiter
specifier|private
name|TextLineDelimiter
name|delimiter
init|=
name|TextLineDelimiter
operator|.
name|LINE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|autoAppendDelimiter
specifier|private
name|boolean
name|autoAppendDelimiter
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"1024"
argument_list|)
DECL|field|decoderMaxLineLength
specifier|private
name|int
name|decoderMaxLineLength
init|=
literal|1024
decl_stmt|;
annotation|@
name|UriParam
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
DECL|field|encoders
specifier|private
name|List
argument_list|<
name|ChannelHandler
argument_list|>
name|encoders
init|=
operator|new
name|ArrayList
argument_list|<
name|ChannelHandler
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|decoders
specifier|private
name|List
argument_list|<
name|ChannelHandler
argument_list|>
name|decoders
init|=
operator|new
name|ArrayList
argument_list|<
name|ChannelHandler
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|disconnect
specifier|private
name|boolean
name|disconnect
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|lazyChannelCreation
specifier|private
name|boolean
name|lazyChannelCreation
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|transferExchange
specifier|private
name|boolean
name|transferExchange
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|disconnectOnNoReply
specifier|private
name|boolean
name|disconnectOnNoReply
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"WARN"
argument_list|)
DECL|field|noReplyLogLevel
specifier|private
name|LoggingLevel
name|noReplyLogLevel
init|=
name|LoggingLevel
operator|.
name|WARN
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"WARN"
argument_list|)
DECL|field|serverExceptionCaughtLogLevel
specifier|private
name|LoggingLevel
name|serverExceptionCaughtLogLevel
init|=
name|LoggingLevel
operator|.
name|WARN
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"DEBUG"
argument_list|)
DECL|field|serverClosedChannelExceptionCaughtLogLevel
specifier|private
name|LoggingLevel
name|serverClosedChannelExceptionCaughtLogLevel
init|=
name|LoggingLevel
operator|.
name|DEBUG
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|allowDefaultCodec
specifier|private
name|boolean
name|allowDefaultCodec
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|clientPipelineFactory
specifier|private
name|ClientPipelineFactory
name|clientPipelineFactory
decl_stmt|;
comment|//CAMEL-8031 Moved this option to NettyComponent
DECL|field|maximumPoolSize
specifier|private
name|int
name|maximumPoolSize
init|=
literal|16
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|orderedThreadPoolExecutor
specifier|private
name|boolean
name|orderedThreadPoolExecutor
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"-1"
argument_list|)
DECL|field|producerPoolMaxActive
specifier|private
name|int
name|producerPoolMaxActive
init|=
operator|-
literal|1
decl_stmt|;
annotation|@
name|UriParam
DECL|field|producerPoolMinIdle
specifier|private
name|int
name|producerPoolMinIdle
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"100"
argument_list|)
DECL|field|producerPoolMaxIdle
specifier|private
name|int
name|producerPoolMaxIdle
init|=
literal|100
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|""
operator|+
literal|5
operator|*
literal|60
operator|*
literal|1000L
argument_list|)
DECL|field|producerPoolMinEvictableIdle
specifier|private
name|long
name|producerPoolMinEvictableIdle
init|=
literal|5
operator|*
literal|60
operator|*
literal|1000L
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|producerPoolEnabled
specifier|private
name|boolean
name|producerPoolEnabled
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|udpConnectionlessSending
specifier|private
name|boolean
name|udpConnectionlessSending
decl_stmt|;
comment|/**      * Returns a copy of this configuration      */
DECL|method|copy ()
specifier|public
name|NettyConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
name|NettyConfiguration
name|answer
init|=
operator|(
name|NettyConfiguration
operator|)
name|clone
argument_list|()
decl_stmt|;
comment|// make sure the lists is copied in its own instance
name|List
argument_list|<
name|ChannelHandler
argument_list|>
name|encodersCopy
init|=
operator|new
name|ArrayList
argument_list|<
name|ChannelHandler
argument_list|>
argument_list|(
name|encoders
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setEncoders
argument_list|(
name|encodersCopy
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ChannelHandler
argument_list|>
name|decodersCopy
init|=
operator|new
name|ArrayList
argument_list|<
name|ChannelHandler
argument_list|>
argument_list|(
name|decoders
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setDecoders
argument_list|(
name|decodersCopy
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|validateConfiguration ()
specifier|public
name|void
name|validateConfiguration
parameter_list|()
block|{
comment|// validate that the encoders is either shareable or is a handler factory
for|for
control|(
name|ChannelHandler
name|encoder
range|:
name|encoders
control|)
block|{
if|if
condition|(
name|encoder
operator|instanceof
name|ChannelHandlerFactory
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|getAnnotation
argument_list|(
name|encoder
argument_list|,
name|ChannelHandler
operator|.
name|Sharable
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
continue|continue;
block|}
name|LOG
operator|.
name|warn
argument_list|(
literal|"The encoder {} is not @Shareable or an ChannelHandlerFactory instance. The encoder cannot safely be used."
argument_list|,
name|encoder
argument_list|)
expr_stmt|;
block|}
comment|// validate that the decoders is either shareable or is a handler factory
for|for
control|(
name|ChannelHandler
name|decoder
range|:
name|decoders
control|)
block|{
if|if
condition|(
name|decoder
operator|instanceof
name|ChannelHandlerFactory
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|getAnnotation
argument_list|(
name|decoder
argument_list|,
name|ChannelHandler
operator|.
name|Sharable
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
continue|continue;
block|}
name|LOG
operator|.
name|warn
argument_list|(
literal|"The decoder {} is not @Shareable or an ChannelHandlerFactory instance. The decoder cannot safely be used."
argument_list|,
name|decoder
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sslHandler
operator|!=
literal|null
condition|)
block|{
name|boolean
name|factory
init|=
name|sslHandler
operator|instanceof
name|ChannelHandlerFactory
decl_stmt|;
name|boolean
name|shareable
init|=
name|ObjectHelper
operator|.
name|getAnnotation
argument_list|(
name|sslHandler
argument_list|,
name|ChannelHandler
operator|.
name|Sharable
operator|.
name|class
argument_list|)
operator|!=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|factory
operator|&&
operator|!
name|shareable
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"The sslHandler {} is not @Shareable or an ChannelHandlerFactory instance. The sslHandler cannot safely be used."
argument_list|,
name|sslHandler
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|parseURI (URI uri, Map<String, Object> parameters, NettyComponent component, String... supportedProtocols)
specifier|public
name|void
name|parseURI
parameter_list|(
name|URI
name|uri
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|NettyComponent
name|component
parameter_list|,
name|String
modifier|...
name|supportedProtocols
parameter_list|)
throws|throws
name|Exception
block|{
name|protocol
operator|=
name|uri
operator|.
name|getScheme
argument_list|()
expr_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|String
name|supportedProtocol
range|:
name|supportedProtocols
control|)
block|{
if|if
condition|(
name|protocol
operator|!=
literal|null
operator|&&
name|protocol
operator|.
name|equalsIgnoreCase
argument_list|(
name|supportedProtocol
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unrecognized Netty protocol: "
operator|+
name|protocol
operator|+
literal|" for uri: "
operator|+
name|uri
argument_list|)
throw|;
block|}
name|setHost
argument_list|(
name|uri
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|setPort
argument_list|(
name|uri
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|ssl
operator|=
name|component
operator|.
name|getAndRemoveOrResolveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"ssl"
argument_list|,
name|boolean
operator|.
name|class
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|sslHandler
operator|=
name|component
operator|.
name|getAndRemoveOrResolveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"sslHandler"
argument_list|,
name|SslHandler
operator|.
name|class
argument_list|,
name|sslHandler
argument_list|)
expr_stmt|;
name|passphrase
operator|=
name|component
operator|.
name|getAndRemoveOrResolveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"passphrase"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|passphrase
argument_list|)
expr_stmt|;
name|keyStoreFormat
operator|=
name|component
operator|.
name|getAndRemoveOrResolveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"keyStoreFormat"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|keyStoreFormat
operator|==
literal|null
condition|?
literal|"JKS"
else|:
name|keyStoreFormat
argument_list|)
expr_stmt|;
name|securityProvider
operator|=
name|component
operator|.
name|getAndRemoveOrResolveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"securityProvider"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|securityProvider
operator|==
literal|null
condition|?
literal|"SunX509"
else|:
name|securityProvider
argument_list|)
expr_stmt|;
name|keyStoreFile
operator|=
name|component
operator|.
name|getAndRemoveOrResolveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"keyStoreFile"
argument_list|,
name|File
operator|.
name|class
argument_list|,
name|keyStoreFile
argument_list|)
expr_stmt|;
name|trustStoreFile
operator|=
name|component
operator|.
name|getAndRemoveOrResolveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"trustStoreFile"
argument_list|,
name|File
operator|.
name|class
argument_list|,
name|trustStoreFile
argument_list|)
expr_stmt|;
name|keyStoreResource
operator|=
name|component
operator|.
name|getAndRemoveOrResolveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"keyStoreResource"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|keyStoreResource
argument_list|)
expr_stmt|;
name|trustStoreResource
operator|=
name|component
operator|.
name|getAndRemoveOrResolveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"trustStoreResource"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|trustStoreResource
argument_list|)
expr_stmt|;
name|clientPipelineFactory
operator|=
name|component
operator|.
name|getAndRemoveOrResolveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"clientPipelineFactory"
argument_list|,
name|ClientPipelineFactory
operator|.
name|class
argument_list|,
name|clientPipelineFactory
argument_list|)
expr_stmt|;
name|serverPipelineFactory
operator|=
name|component
operator|.
name|getAndRemoveOrResolveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"serverPipelineFactory"
argument_list|,
name|ServerPipelineFactory
operator|.
name|class
argument_list|,
name|serverPipelineFactory
argument_list|)
expr_stmt|;
comment|// set custom encoders and decoders first
name|List
argument_list|<
name|ChannelHandler
argument_list|>
name|referencedEncoders
init|=
name|component
operator|.
name|resolveAndRemoveReferenceListParameter
argument_list|(
name|parameters
argument_list|,
literal|"encoders"
argument_list|,
name|ChannelHandler
operator|.
name|class
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|addToHandlersList
argument_list|(
name|encoders
argument_list|,
name|referencedEncoders
argument_list|,
name|ChannelHandler
operator|.
name|class
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ChannelHandler
argument_list|>
name|referencedDecoders
init|=
name|component
operator|.
name|resolveAndRemoveReferenceListParameter
argument_list|(
name|parameters
argument_list|,
literal|"decoders"
argument_list|,
name|ChannelHandler
operator|.
name|class
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|addToHandlersList
argument_list|(
name|decoders
argument_list|,
name|referencedDecoders
argument_list|,
name|ChannelHandler
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// then set parameters with the help of the camel context type converters
name|EndpointHelper
operator|.
name|setReferenceProperties
argument_list|(
name|component
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|this
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|EndpointHelper
operator|.
name|setProperties
argument_list|(
name|component
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|this
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// additional netty options, we don't want to store an empty map, so set it as null if empty
name|options
operator|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
literal|"option."
argument_list|)
expr_stmt|;
if|if
condition|(
name|options
operator|!=
literal|null
operator|&&
name|options
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|options
operator|=
literal|null
expr_stmt|;
block|}
comment|// add default encoders and decoders
if|if
condition|(
name|encoders
operator|.
name|isEmpty
argument_list|()
operator|&&
name|decoders
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|isAllowDefaultCodec
argument_list|()
condition|)
block|{
comment|// are we textline or object?
if|if
condition|(
name|isTextline
argument_list|()
condition|)
block|{
name|Charset
name|charset
init|=
name|getEncoding
argument_list|()
operator|!=
literal|null
condition|?
name|Charset
operator|.
name|forName
argument_list|(
name|getEncoding
argument_list|()
argument_list|)
else|:
name|CharsetUtil
operator|.
name|UTF_8
decl_stmt|;
name|encoders
operator|.
name|add
argument_list|(
name|ChannelHandlerFactories
operator|.
name|newStringEncoder
argument_list|(
name|charset
argument_list|)
argument_list|)
expr_stmt|;
name|ChannelBuffer
index|[]
name|delimiters
init|=
name|delimiter
operator|==
name|TextLineDelimiter
operator|.
name|LINE
condition|?
name|Delimiters
operator|.
name|lineDelimiter
argument_list|()
else|:
name|Delimiters
operator|.
name|nulDelimiter
argument_list|()
decl_stmt|;
name|decoders
operator|.
name|add
argument_list|(
name|ChannelHandlerFactories
operator|.
name|newDelimiterBasedFrameDecoder
argument_list|(
name|decoderMaxLineLength
argument_list|,
name|delimiters
argument_list|)
argument_list|)
expr_stmt|;
name|decoders
operator|.
name|add
argument_list|(
name|ChannelHandlerFactories
operator|.
name|newStringDecoder
argument_list|(
name|charset
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using textline encoders and decoders with charset: {}, delimiter: {} and decoderMaxLineLength: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|charset
block|,
name|delimiter
block|,
name|decoderMaxLineLength
block|}
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// object serializable is then used
name|encoders
operator|.
name|add
argument_list|(
name|ChannelHandlerFactories
operator|.
name|newObjectEncoder
argument_list|()
argument_list|)
expr_stmt|;
name|decoders
operator|.
name|add
argument_list|(
name|ChannelHandlerFactories
operator|.
name|newObjectDecoder
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using object encoders and decoders"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No encoders and decoders will be used"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using configured encoders and/or decoders"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getCharsetName ()
specifier|public
name|String
name|getCharsetName
parameter_list|()
block|{
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
operator|!
name|Charset
operator|.
name|isSupported
argument_list|(
name|encoding
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The encoding: "
operator|+
name|encoding
operator|+
literal|" is not supported"
argument_list|)
throw|;
block|}
return|return
name|Charset
operator|.
name|forName
argument_list|(
name|encoding
argument_list|)
operator|.
name|name
argument_list|()
return|;
block|}
DECL|method|getRequestTimeout ()
specifier|public
name|long
name|getRequestTimeout
parameter_list|()
block|{
return|return
name|requestTimeout
return|;
block|}
DECL|method|setRequestTimeout (long requestTimeout)
specifier|public
name|void
name|setRequestTimeout
parameter_list|(
name|long
name|requestTimeout
parameter_list|)
block|{
name|this
operator|.
name|requestTimeout
operator|=
name|requestTimeout
expr_stmt|;
block|}
DECL|method|isSync ()
specifier|public
name|boolean
name|isSync
parameter_list|()
block|{
return|return
name|sync
return|;
block|}
DECL|method|setSync (boolean sync)
specifier|public
name|void
name|setSync
parameter_list|(
name|boolean
name|sync
parameter_list|)
block|{
name|this
operator|.
name|sync
operator|=
name|sync
expr_stmt|;
block|}
DECL|method|isTextline ()
specifier|public
name|boolean
name|isTextline
parameter_list|()
block|{
return|return
name|textline
return|;
block|}
DECL|method|setTextline (boolean textline)
specifier|public
name|void
name|setTextline
parameter_list|(
name|boolean
name|textline
parameter_list|)
block|{
name|this
operator|.
name|textline
operator|=
name|textline
expr_stmt|;
block|}
DECL|method|getDecoderMaxLineLength ()
specifier|public
name|int
name|getDecoderMaxLineLength
parameter_list|()
block|{
return|return
name|decoderMaxLineLength
return|;
block|}
DECL|method|setDecoderMaxLineLength (int decoderMaxLineLength)
specifier|public
name|void
name|setDecoderMaxLineLength
parameter_list|(
name|int
name|decoderMaxLineLength
parameter_list|)
block|{
name|this
operator|.
name|decoderMaxLineLength
operator|=
name|decoderMaxLineLength
expr_stmt|;
block|}
DECL|method|getDelimiter ()
specifier|public
name|TextLineDelimiter
name|getDelimiter
parameter_list|()
block|{
return|return
name|delimiter
return|;
block|}
DECL|method|setDelimiter (TextLineDelimiter delimiter)
specifier|public
name|void
name|setDelimiter
parameter_list|(
name|TextLineDelimiter
name|delimiter
parameter_list|)
block|{
name|this
operator|.
name|delimiter
operator|=
name|delimiter
expr_stmt|;
block|}
DECL|method|isAutoAppendDelimiter ()
specifier|public
name|boolean
name|isAutoAppendDelimiter
parameter_list|()
block|{
return|return
name|autoAppendDelimiter
return|;
block|}
DECL|method|setAutoAppendDelimiter (boolean autoAppendDelimiter)
specifier|public
name|void
name|setAutoAppendDelimiter
parameter_list|(
name|boolean
name|autoAppendDelimiter
parameter_list|)
block|{
name|this
operator|.
name|autoAppendDelimiter
operator|=
name|autoAppendDelimiter
expr_stmt|;
block|}
DECL|method|getEncoding ()
specifier|public
name|String
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
DECL|method|setEncoding (String encoding)
specifier|public
name|void
name|setEncoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|this
operator|.
name|encoding
operator|=
name|encoding
expr_stmt|;
block|}
DECL|method|getDecoders ()
specifier|public
name|List
argument_list|<
name|ChannelHandler
argument_list|>
name|getDecoders
parameter_list|()
block|{
return|return
name|decoders
return|;
block|}
DECL|method|setDecoders (List<ChannelHandler> decoders)
specifier|public
name|void
name|setDecoders
parameter_list|(
name|List
argument_list|<
name|ChannelHandler
argument_list|>
name|decoders
parameter_list|)
block|{
name|this
operator|.
name|decoders
operator|=
name|decoders
expr_stmt|;
block|}
DECL|method|getEncoders ()
specifier|public
name|List
argument_list|<
name|ChannelHandler
argument_list|>
name|getEncoders
parameter_list|()
block|{
return|return
name|encoders
return|;
block|}
DECL|method|setEncoders (List<ChannelHandler> encoders)
specifier|public
name|void
name|setEncoders
parameter_list|(
name|List
argument_list|<
name|ChannelHandler
argument_list|>
name|encoders
parameter_list|)
block|{
name|this
operator|.
name|encoders
operator|=
name|encoders
expr_stmt|;
block|}
DECL|method|getEncoder ()
specifier|public
name|ChannelHandler
name|getEncoder
parameter_list|()
block|{
return|return
name|encoders
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|encoders
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
DECL|method|setEncoder (ChannelHandler encoder)
specifier|public
name|void
name|setEncoder
parameter_list|(
name|ChannelHandler
name|encoder
parameter_list|)
block|{
if|if
condition|(
operator|!
name|encoders
operator|.
name|contains
argument_list|(
name|encoder
argument_list|)
condition|)
block|{
name|encoders
operator|.
name|add
argument_list|(
name|encoder
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getDecoder ()
specifier|public
name|ChannelHandler
name|getDecoder
parameter_list|()
block|{
return|return
name|decoders
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|decoders
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
DECL|method|setDecoder (ChannelHandler decoder)
specifier|public
name|void
name|setDecoder
parameter_list|(
name|ChannelHandler
name|decoder
parameter_list|)
block|{
if|if
condition|(
operator|!
name|decoders
operator|.
name|contains
argument_list|(
name|decoder
argument_list|)
condition|)
block|{
name|decoders
operator|.
name|add
argument_list|(
name|decoder
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isDisconnect ()
specifier|public
name|boolean
name|isDisconnect
parameter_list|()
block|{
return|return
name|disconnect
return|;
block|}
DECL|method|setDisconnect (boolean disconnect)
specifier|public
name|void
name|setDisconnect
parameter_list|(
name|boolean
name|disconnect
parameter_list|)
block|{
name|this
operator|.
name|disconnect
operator|=
name|disconnect
expr_stmt|;
block|}
DECL|method|isLazyChannelCreation ()
specifier|public
name|boolean
name|isLazyChannelCreation
parameter_list|()
block|{
return|return
name|lazyChannelCreation
return|;
block|}
DECL|method|setLazyChannelCreation (boolean lazyChannelCreation)
specifier|public
name|void
name|setLazyChannelCreation
parameter_list|(
name|boolean
name|lazyChannelCreation
parameter_list|)
block|{
name|this
operator|.
name|lazyChannelCreation
operator|=
name|lazyChannelCreation
expr_stmt|;
block|}
DECL|method|isTransferExchange ()
specifier|public
name|boolean
name|isTransferExchange
parameter_list|()
block|{
return|return
name|transferExchange
return|;
block|}
DECL|method|setTransferExchange (boolean transferExchange)
specifier|public
name|void
name|setTransferExchange
parameter_list|(
name|boolean
name|transferExchange
parameter_list|)
block|{
name|this
operator|.
name|transferExchange
operator|=
name|transferExchange
expr_stmt|;
block|}
DECL|method|isDisconnectOnNoReply ()
specifier|public
name|boolean
name|isDisconnectOnNoReply
parameter_list|()
block|{
return|return
name|disconnectOnNoReply
return|;
block|}
DECL|method|setDisconnectOnNoReply (boolean disconnectOnNoReply)
specifier|public
name|void
name|setDisconnectOnNoReply
parameter_list|(
name|boolean
name|disconnectOnNoReply
parameter_list|)
block|{
name|this
operator|.
name|disconnectOnNoReply
operator|=
name|disconnectOnNoReply
expr_stmt|;
block|}
DECL|method|getNoReplyLogLevel ()
specifier|public
name|LoggingLevel
name|getNoReplyLogLevel
parameter_list|()
block|{
return|return
name|noReplyLogLevel
return|;
block|}
DECL|method|setNoReplyLogLevel (LoggingLevel noReplyLogLevel)
specifier|public
name|void
name|setNoReplyLogLevel
parameter_list|(
name|LoggingLevel
name|noReplyLogLevel
parameter_list|)
block|{
name|this
operator|.
name|noReplyLogLevel
operator|=
name|noReplyLogLevel
expr_stmt|;
block|}
DECL|method|getServerExceptionCaughtLogLevel ()
specifier|public
name|LoggingLevel
name|getServerExceptionCaughtLogLevel
parameter_list|()
block|{
return|return
name|serverExceptionCaughtLogLevel
return|;
block|}
DECL|method|setServerExceptionCaughtLogLevel (LoggingLevel serverExceptionCaughtLogLevel)
specifier|public
name|void
name|setServerExceptionCaughtLogLevel
parameter_list|(
name|LoggingLevel
name|serverExceptionCaughtLogLevel
parameter_list|)
block|{
name|this
operator|.
name|serverExceptionCaughtLogLevel
operator|=
name|serverExceptionCaughtLogLevel
expr_stmt|;
block|}
DECL|method|getServerClosedChannelExceptionCaughtLogLevel ()
specifier|public
name|LoggingLevel
name|getServerClosedChannelExceptionCaughtLogLevel
parameter_list|()
block|{
return|return
name|serverClosedChannelExceptionCaughtLogLevel
return|;
block|}
DECL|method|setServerClosedChannelExceptionCaughtLogLevel (LoggingLevel serverClosedChannelExceptionCaughtLogLevel)
specifier|public
name|void
name|setServerClosedChannelExceptionCaughtLogLevel
parameter_list|(
name|LoggingLevel
name|serverClosedChannelExceptionCaughtLogLevel
parameter_list|)
block|{
name|this
operator|.
name|serverClosedChannelExceptionCaughtLogLevel
operator|=
name|serverClosedChannelExceptionCaughtLogLevel
expr_stmt|;
block|}
DECL|method|isAllowDefaultCodec ()
specifier|public
name|boolean
name|isAllowDefaultCodec
parameter_list|()
block|{
return|return
name|allowDefaultCodec
return|;
block|}
DECL|method|setAllowDefaultCodec (boolean allowDefaultCodec)
specifier|public
name|void
name|setAllowDefaultCodec
parameter_list|(
name|boolean
name|allowDefaultCodec
parameter_list|)
block|{
name|this
operator|.
name|allowDefaultCodec
operator|=
name|allowDefaultCodec
expr_stmt|;
block|}
DECL|method|setClientPipelineFactory (ClientPipelineFactory clientPipelineFactory)
specifier|public
name|void
name|setClientPipelineFactory
parameter_list|(
name|ClientPipelineFactory
name|clientPipelineFactory
parameter_list|)
block|{
name|this
operator|.
name|clientPipelineFactory
operator|=
name|clientPipelineFactory
expr_stmt|;
block|}
DECL|method|getClientPipelineFactory ()
specifier|public
name|ClientPipelineFactory
name|getClientPipelineFactory
parameter_list|()
block|{
return|return
name|clientPipelineFactory
return|;
block|}
DECL|method|getMaximumPoolSize ()
specifier|public
name|int
name|getMaximumPoolSize
parameter_list|()
block|{
return|return
name|maximumPoolSize
return|;
block|}
DECL|method|setMaximumPoolSize (int maximumPoolSize)
specifier|public
name|void
name|setMaximumPoolSize
parameter_list|(
name|int
name|maximumPoolSize
parameter_list|)
block|{
name|this
operator|.
name|maximumPoolSize
operator|=
name|maximumPoolSize
expr_stmt|;
block|}
DECL|method|isOrderedThreadPoolExecutor ()
specifier|public
name|boolean
name|isOrderedThreadPoolExecutor
parameter_list|()
block|{
return|return
name|orderedThreadPoolExecutor
return|;
block|}
DECL|method|setOrderedThreadPoolExecutor (boolean orderedThreadPoolExecutor)
specifier|public
name|void
name|setOrderedThreadPoolExecutor
parameter_list|(
name|boolean
name|orderedThreadPoolExecutor
parameter_list|)
block|{
name|this
operator|.
name|orderedThreadPoolExecutor
operator|=
name|orderedThreadPoolExecutor
expr_stmt|;
block|}
DECL|method|getProducerPoolMaxActive ()
specifier|public
name|int
name|getProducerPoolMaxActive
parameter_list|()
block|{
return|return
name|producerPoolMaxActive
return|;
block|}
DECL|method|setProducerPoolMaxActive (int producerPoolMaxActive)
specifier|public
name|void
name|setProducerPoolMaxActive
parameter_list|(
name|int
name|producerPoolMaxActive
parameter_list|)
block|{
name|this
operator|.
name|producerPoolMaxActive
operator|=
name|producerPoolMaxActive
expr_stmt|;
block|}
DECL|method|getProducerPoolMinIdle ()
specifier|public
name|int
name|getProducerPoolMinIdle
parameter_list|()
block|{
return|return
name|producerPoolMinIdle
return|;
block|}
DECL|method|setProducerPoolMinIdle (int producerPoolMinIdle)
specifier|public
name|void
name|setProducerPoolMinIdle
parameter_list|(
name|int
name|producerPoolMinIdle
parameter_list|)
block|{
name|this
operator|.
name|producerPoolMinIdle
operator|=
name|producerPoolMinIdle
expr_stmt|;
block|}
DECL|method|getProducerPoolMaxIdle ()
specifier|public
name|int
name|getProducerPoolMaxIdle
parameter_list|()
block|{
return|return
name|producerPoolMaxIdle
return|;
block|}
DECL|method|setProducerPoolMaxIdle (int producerPoolMaxIdle)
specifier|public
name|void
name|setProducerPoolMaxIdle
parameter_list|(
name|int
name|producerPoolMaxIdle
parameter_list|)
block|{
name|this
operator|.
name|producerPoolMaxIdle
operator|=
name|producerPoolMaxIdle
expr_stmt|;
block|}
DECL|method|getProducerPoolMinEvictableIdle ()
specifier|public
name|long
name|getProducerPoolMinEvictableIdle
parameter_list|()
block|{
return|return
name|producerPoolMinEvictableIdle
return|;
block|}
DECL|method|setProducerPoolMinEvictableIdle (long producerPoolMinEvictableIdle)
specifier|public
name|void
name|setProducerPoolMinEvictableIdle
parameter_list|(
name|long
name|producerPoolMinEvictableIdle
parameter_list|)
block|{
name|this
operator|.
name|producerPoolMinEvictableIdle
operator|=
name|producerPoolMinEvictableIdle
expr_stmt|;
block|}
DECL|method|isProducerPoolEnabled ()
specifier|public
name|boolean
name|isProducerPoolEnabled
parameter_list|()
block|{
return|return
name|producerPoolEnabled
return|;
block|}
DECL|method|setProducerPoolEnabled (boolean producerPoolEnabled)
specifier|public
name|void
name|setProducerPoolEnabled
parameter_list|(
name|boolean
name|producerPoolEnabled
parameter_list|)
block|{
name|this
operator|.
name|producerPoolEnabled
operator|=
name|producerPoolEnabled
expr_stmt|;
block|}
DECL|method|isUdpConnectionlessSending ()
specifier|public
name|boolean
name|isUdpConnectionlessSending
parameter_list|()
block|{
return|return
name|udpConnectionlessSending
return|;
block|}
DECL|method|setUdpConnectionlessSending (boolean udpConnectionlessSending)
specifier|public
name|void
name|setUdpConnectionlessSending
parameter_list|(
name|boolean
name|udpConnectionlessSending
parameter_list|)
block|{
name|this
operator|.
name|udpConnectionlessSending
operator|=
name|udpConnectionlessSending
expr_stmt|;
block|}
DECL|method|addToHandlersList (List<T> configured, List<T> handlers, Class<T> handlerType)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|void
name|addToHandlersList
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|configured
parameter_list|,
name|List
argument_list|<
name|T
argument_list|>
name|handlers
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|handlerType
parameter_list|)
block|{
if|if
condition|(
name|handlers
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|T
name|handler
range|:
name|handlers
control|)
block|{
if|if
condition|(
name|handlerType
operator|.
name|isInstance
argument_list|(
name|handler
argument_list|)
condition|)
block|{
name|configured
operator|.
name|add
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

