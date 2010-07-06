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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|ChannelDownstreamHandler
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
name|ChannelUpstreamHandler
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
name|DelimiterBasedFrameDecoder
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
name|codec
operator|.
name|serialization
operator|.
name|ObjectDecoder
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
name|serialization
operator|.
name|ObjectEncoder
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
name|string
operator|.
name|StringDecoder
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
name|string
operator|.
name|StringEncoder
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

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|class|NettyConfiguration
specifier|public
class|class
name|NettyConfiguration
implements|implements
name|Cloneable
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|NettyConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|protocol
specifier|private
name|String
name|protocol
decl_stmt|;
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
DECL|field|keepAlive
specifier|private
name|boolean
name|keepAlive
init|=
literal|true
decl_stmt|;
DECL|field|tcpNoDelay
specifier|private
name|boolean
name|tcpNoDelay
init|=
literal|true
decl_stmt|;
DECL|field|broadcast
specifier|private
name|boolean
name|broadcast
decl_stmt|;
DECL|field|connectTimeout
specifier|private
name|long
name|connectTimeout
init|=
literal|10000
decl_stmt|;
DECL|field|timeout
specifier|private
name|long
name|timeout
init|=
literal|30000
decl_stmt|;
DECL|field|reuseAddress
specifier|private
name|boolean
name|reuseAddress
init|=
literal|true
decl_stmt|;
DECL|field|sync
specifier|private
name|boolean
name|sync
init|=
literal|true
decl_stmt|;
DECL|field|textline
specifier|private
name|boolean
name|textline
decl_stmt|;
DECL|field|delimiter
specifier|private
name|TextLineDelimiter
name|delimiter
init|=
name|TextLineDelimiter
operator|.
name|LINE
decl_stmt|;
DECL|field|autoAppendDelimiter
specifier|private
name|boolean
name|autoAppendDelimiter
init|=
literal|true
decl_stmt|;
DECL|field|decorderMaxLineLength
specifier|private
name|int
name|decorderMaxLineLength
init|=
literal|1024
decl_stmt|;
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
DECL|field|passphrase
specifier|private
name|String
name|passphrase
decl_stmt|;
DECL|field|keyStoreFile
specifier|private
name|File
name|keyStoreFile
decl_stmt|;
DECL|field|trustStoreFile
specifier|private
name|File
name|trustStoreFile
decl_stmt|;
DECL|field|sslHandler
specifier|private
name|SslHandler
name|sslHandler
decl_stmt|;
DECL|field|encoders
specifier|private
name|List
argument_list|<
name|ChannelDownstreamHandler
argument_list|>
name|encoders
init|=
operator|new
name|ArrayList
argument_list|<
name|ChannelDownstreamHandler
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|decoders
specifier|private
name|List
argument_list|<
name|ChannelUpstreamHandler
argument_list|>
name|decoders
init|=
operator|new
name|ArrayList
argument_list|<
name|ChannelUpstreamHandler
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|ssl
specifier|private
name|boolean
name|ssl
decl_stmt|;
DECL|field|sendBufferSize
specifier|private
name|long
name|sendBufferSize
init|=
literal|65536
decl_stmt|;
DECL|field|receiveBufferSize
specifier|private
name|long
name|receiveBufferSize
init|=
literal|65536
decl_stmt|;
DECL|field|corePoolSize
specifier|private
name|int
name|corePoolSize
init|=
literal|10
decl_stmt|;
DECL|field|maxPoolSize
specifier|private
name|int
name|maxPoolSize
init|=
literal|100
decl_stmt|;
DECL|field|keyStoreFormat
specifier|private
name|String
name|keyStoreFormat
decl_stmt|;
DECL|field|securityProvider
specifier|private
name|String
name|securityProvider
decl_stmt|;
DECL|field|disconnect
specifier|private
name|boolean
name|disconnect
decl_stmt|;
DECL|field|lazyChannelCreation
specifier|private
name|boolean
name|lazyChannelCreation
init|=
literal|true
decl_stmt|;
DECL|field|transferExchange
specifier|private
name|boolean
name|transferExchange
decl_stmt|;
DECL|field|disconnectOnNoReply
specifier|private
name|boolean
name|disconnectOnNoReply
init|=
literal|true
decl_stmt|;
DECL|field|noReplyLogLevel
specifier|private
name|LoggingLevel
name|noReplyLogLevel
init|=
name|LoggingLevel
operator|.
name|WARN
decl_stmt|;
DECL|field|allowDefaultCodec
specifier|private
name|boolean
name|allowDefaultCodec
init|=
literal|true
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
name|ChannelDownstreamHandler
argument_list|>
name|encodersCopy
init|=
operator|new
name|ArrayList
argument_list|<
name|ChannelDownstreamHandler
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
name|ChannelUpstreamHandler
argument_list|>
name|decodersCopy
init|=
operator|new
name|ArrayList
argument_list|<
name|ChannelUpstreamHandler
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
DECL|method|parseURI (URI uri, Map<String, Object> parameters, NettyComponent component)
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
if|if
condition|(
operator|(
operator|!
name|protocol
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"tcp"
argument_list|)
operator|)
operator|&&
operator|(
operator|!
name|protocol
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"udp"
argument_list|)
operator|)
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
name|sslHandler
operator|=
name|component
operator|.
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"sslHandler"
argument_list|,
name|SslHandler
operator|.
name|class
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|passphrase
operator|=
name|component
operator|.
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"passphrase"
argument_list|,
name|String
operator|.
name|class
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|keyStoreFormat
operator|=
name|component
operator|.
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"keyStoreFormat"
argument_list|,
name|String
operator|.
name|class
argument_list|,
literal|"JKS"
argument_list|)
expr_stmt|;
name|securityProvider
operator|=
name|component
operator|.
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"securityProvider"
argument_list|,
name|String
operator|.
name|class
argument_list|,
literal|"SunX509"
argument_list|)
expr_stmt|;
name|keyStoreFile
operator|=
name|component
operator|.
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"keyStoreFile"
argument_list|,
name|File
operator|.
name|class
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|trustStoreFile
operator|=
name|component
operator|.
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"trustStoreFile"
argument_list|,
name|File
operator|.
name|class
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// set custom encoders and decoders first
name|List
argument_list|<
name|ChannelDownstreamHandler
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
name|ChannelDownstreamHandler
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
name|ChannelDownstreamHandler
operator|.
name|class
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ChannelUpstreamHandler
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
name|ChannelUpstreamHandler
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
name|ChannelUpstreamHandler
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// then set parameters with the help of the camel context type converters
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
name|allowDefaultCodec
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
operator|new
name|StringEncoder
argument_list|(
name|charset
argument_list|)
argument_list|)
expr_stmt|;
name|decoders
operator|.
name|add
argument_list|(
operator|new
name|DelimiterBasedFrameDecoder
argument_list|(
name|decorderMaxLineLength
argument_list|,
literal|true
argument_list|,
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
argument_list|)
argument_list|)
expr_stmt|;
name|decoders
operator|.
name|add
argument_list|(
operator|new
name|StringDecoder
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
literal|"Using textline encoders and decoders with charset: "
operator|+
name|charset
operator|+
literal|", delimiter: "
operator|+
name|delimiter
operator|+
literal|" and decoderMaxLineLength: "
operator|+
name|decorderMaxLineLength
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
operator|new
name|ObjectEncoder
argument_list|()
argument_list|)
expr_stmt|;
name|decoders
operator|.
name|add
argument_list|(
operator|new
name|ObjectDecoder
argument_list|()
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
literal|"Using object encoders and decoders"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
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
literal|"No encoders and decoders will be used"
argument_list|)
expr_stmt|;
block|}
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
DECL|method|isTcp ()
specifier|public
name|boolean
name|isTcp
parameter_list|()
block|{
return|return
name|protocol
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"tcp"
argument_list|)
return|;
block|}
DECL|method|getProtocol ()
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
return|return
name|protocol
return|;
block|}
DECL|method|setProtocol (String protocol)
specifier|public
name|void
name|setProtocol
parameter_list|(
name|String
name|protocol
parameter_list|)
block|{
name|this
operator|.
name|protocol
operator|=
name|protocol
expr_stmt|;
block|}
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
DECL|method|isKeepAlive ()
specifier|public
name|boolean
name|isKeepAlive
parameter_list|()
block|{
return|return
name|keepAlive
return|;
block|}
DECL|method|setKeepAlive (boolean keepAlive)
specifier|public
name|void
name|setKeepAlive
parameter_list|(
name|boolean
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
DECL|method|isTcpNoDelay ()
specifier|public
name|boolean
name|isTcpNoDelay
parameter_list|()
block|{
return|return
name|tcpNoDelay
return|;
block|}
DECL|method|setTcpNoDelay (boolean tcpNoDelay)
specifier|public
name|void
name|setTcpNoDelay
parameter_list|(
name|boolean
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
DECL|method|isBroadcast ()
specifier|public
name|boolean
name|isBroadcast
parameter_list|()
block|{
return|return
name|broadcast
return|;
block|}
DECL|method|setBroadcast (boolean broadcast)
specifier|public
name|void
name|setBroadcast
parameter_list|(
name|boolean
name|broadcast
parameter_list|)
block|{
name|this
operator|.
name|broadcast
operator|=
name|broadcast
expr_stmt|;
block|}
DECL|method|getConnectTimeout ()
specifier|public
name|long
name|getConnectTimeout
parameter_list|()
block|{
return|return
name|connectTimeout
return|;
block|}
DECL|method|setConnectTimeout (long connectTimeout)
specifier|public
name|void
name|setConnectTimeout
parameter_list|(
name|long
name|connectTimeout
parameter_list|)
block|{
name|this
operator|.
name|connectTimeout
operator|=
name|connectTimeout
expr_stmt|;
block|}
DECL|method|isReuseAddress ()
specifier|public
name|boolean
name|isReuseAddress
parameter_list|()
block|{
return|return
name|reuseAddress
return|;
block|}
DECL|method|setReuseAddress (boolean reuseAddress)
specifier|public
name|void
name|setReuseAddress
parameter_list|(
name|boolean
name|reuseAddress
parameter_list|)
block|{
name|this
operator|.
name|reuseAddress
operator|=
name|reuseAddress
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
DECL|method|getDecorderMaxLineLength ()
specifier|public
name|int
name|getDecorderMaxLineLength
parameter_list|()
block|{
return|return
name|decorderMaxLineLength
return|;
block|}
DECL|method|setDecorderMaxLineLength (int decorderMaxLineLength)
specifier|public
name|void
name|setDecorderMaxLineLength
parameter_list|(
name|int
name|decorderMaxLineLength
parameter_list|)
block|{
name|this
operator|.
name|decorderMaxLineLength
operator|=
name|decorderMaxLineLength
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
DECL|method|getSslHandler ()
specifier|public
name|SslHandler
name|getSslHandler
parameter_list|()
block|{
return|return
name|sslHandler
return|;
block|}
DECL|method|setSslHandler (SslHandler sslHandler)
specifier|public
name|void
name|setSslHandler
parameter_list|(
name|SslHandler
name|sslHandler
parameter_list|)
block|{
name|this
operator|.
name|sslHandler
operator|=
name|sslHandler
expr_stmt|;
block|}
DECL|method|getEncoders ()
specifier|public
name|List
argument_list|<
name|ChannelDownstreamHandler
argument_list|>
name|getEncoders
parameter_list|()
block|{
return|return
name|encoders
return|;
block|}
DECL|method|getDecoders ()
specifier|public
name|List
argument_list|<
name|ChannelUpstreamHandler
argument_list|>
name|getDecoders
parameter_list|()
block|{
return|return
name|decoders
return|;
block|}
DECL|method|getEncoder ()
specifier|public
name|ChannelDownstreamHandler
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
DECL|method|setEncoder (ChannelDownstreamHandler encoder)
specifier|public
name|void
name|setEncoder
parameter_list|(
name|ChannelDownstreamHandler
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
DECL|method|setEncoders (List<ChannelDownstreamHandler> encoders)
specifier|public
name|void
name|setEncoders
parameter_list|(
name|List
argument_list|<
name|ChannelDownstreamHandler
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
DECL|method|getDecoder ()
specifier|public
name|ChannelUpstreamHandler
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
DECL|method|setDecoder (ChannelUpstreamHandler decoder)
specifier|public
name|void
name|setDecoder
parameter_list|(
name|ChannelUpstreamHandler
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
DECL|method|setDecoders (List<ChannelUpstreamHandler> decoders)
specifier|public
name|void
name|setDecoders
parameter_list|(
name|List
argument_list|<
name|ChannelUpstreamHandler
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
DECL|method|getTimeout ()
specifier|public
name|long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|method|setTimeout (long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|long
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
DECL|method|getSendBufferSize ()
specifier|public
name|long
name|getSendBufferSize
parameter_list|()
block|{
return|return
name|sendBufferSize
return|;
block|}
DECL|method|setSendBufferSize (long sendBufferSize)
specifier|public
name|void
name|setSendBufferSize
parameter_list|(
name|long
name|sendBufferSize
parameter_list|)
block|{
name|this
operator|.
name|sendBufferSize
operator|=
name|sendBufferSize
expr_stmt|;
block|}
DECL|method|isSsl ()
specifier|public
name|boolean
name|isSsl
parameter_list|()
block|{
return|return
name|ssl
return|;
block|}
DECL|method|setSsl (boolean ssl)
specifier|public
name|void
name|setSsl
parameter_list|(
name|boolean
name|ssl
parameter_list|)
block|{
name|this
operator|.
name|ssl
operator|=
name|ssl
expr_stmt|;
block|}
DECL|method|getReceiveBufferSize ()
specifier|public
name|long
name|getReceiveBufferSize
parameter_list|()
block|{
return|return
name|receiveBufferSize
return|;
block|}
DECL|method|setReceiveBufferSize (long receiveBufferSize)
specifier|public
name|void
name|setReceiveBufferSize
parameter_list|(
name|long
name|receiveBufferSize
parameter_list|)
block|{
name|this
operator|.
name|receiveBufferSize
operator|=
name|receiveBufferSize
expr_stmt|;
block|}
DECL|method|getPassphrase ()
specifier|public
name|String
name|getPassphrase
parameter_list|()
block|{
return|return
name|passphrase
return|;
block|}
DECL|method|setPassphrase (String passphrase)
specifier|public
name|void
name|setPassphrase
parameter_list|(
name|String
name|passphrase
parameter_list|)
block|{
name|this
operator|.
name|passphrase
operator|=
name|passphrase
expr_stmt|;
block|}
DECL|method|getKeyStoreFile ()
specifier|public
name|File
name|getKeyStoreFile
parameter_list|()
block|{
return|return
name|keyStoreFile
return|;
block|}
DECL|method|setKeyStoreFile (File keyStoreFile)
specifier|public
name|void
name|setKeyStoreFile
parameter_list|(
name|File
name|keyStoreFile
parameter_list|)
block|{
name|this
operator|.
name|keyStoreFile
operator|=
name|keyStoreFile
expr_stmt|;
block|}
DECL|method|getTrustStoreFile ()
specifier|public
name|File
name|getTrustStoreFile
parameter_list|()
block|{
return|return
name|trustStoreFile
return|;
block|}
DECL|method|setTrustStoreFile (File trustStoreFile)
specifier|public
name|void
name|setTrustStoreFile
parameter_list|(
name|File
name|trustStoreFile
parameter_list|)
block|{
name|this
operator|.
name|trustStoreFile
operator|=
name|trustStoreFile
expr_stmt|;
block|}
DECL|method|getCorePoolSize ()
specifier|public
name|int
name|getCorePoolSize
parameter_list|()
block|{
return|return
name|corePoolSize
return|;
block|}
DECL|method|setCorePoolSize (int corePoolSize)
specifier|public
name|void
name|setCorePoolSize
parameter_list|(
name|int
name|corePoolSize
parameter_list|)
block|{
name|this
operator|.
name|corePoolSize
operator|=
name|corePoolSize
expr_stmt|;
block|}
DECL|method|getMaxPoolSize ()
specifier|public
name|int
name|getMaxPoolSize
parameter_list|()
block|{
return|return
name|maxPoolSize
return|;
block|}
DECL|method|setMaxPoolSize (int maxPoolSize)
specifier|public
name|void
name|setMaxPoolSize
parameter_list|(
name|int
name|maxPoolSize
parameter_list|)
block|{
name|this
operator|.
name|maxPoolSize
operator|=
name|maxPoolSize
expr_stmt|;
block|}
DECL|method|getKeyStoreFormat ()
specifier|public
name|String
name|getKeyStoreFormat
parameter_list|()
block|{
return|return
name|keyStoreFormat
return|;
block|}
DECL|method|setKeyStoreFormat (String keyStoreFormat)
specifier|public
name|void
name|setKeyStoreFormat
parameter_list|(
name|String
name|keyStoreFormat
parameter_list|)
block|{
name|this
operator|.
name|keyStoreFormat
operator|=
name|keyStoreFormat
expr_stmt|;
block|}
DECL|method|getSecurityProvider ()
specifier|public
name|String
name|getSecurityProvider
parameter_list|()
block|{
return|return
name|securityProvider
return|;
block|}
DECL|method|setSecurityProvider (String securityProvider)
specifier|public
name|void
name|setSecurityProvider
parameter_list|(
name|String
name|securityProvider
parameter_list|)
block|{
name|this
operator|.
name|securityProvider
operator|=
name|securityProvider
expr_stmt|;
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
DECL|method|getAddress ()
specifier|public
name|String
name|getAddress
parameter_list|()
block|{
return|return
name|host
operator|+
literal|":"
operator|+
name|port
return|;
block|}
DECL|method|addToHandlersList (List configured, List handlers, Class<? extends T> handlerType)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|void
name|addToHandlersList
parameter_list|(
name|List
name|configured
parameter_list|,
name|List
name|handlers
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
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
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|handlers
operator|.
name|size
argument_list|()
condition|;
name|x
operator|++
control|)
block|{
name|Object
name|handler
init|=
name|handlers
operator|.
name|get
argument_list|(
name|x
argument_list|)
decl_stmt|;
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

