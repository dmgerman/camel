begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina2
package|;
end_package

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
name|List
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
name|jsse
operator|.
name|SSLContextParameters
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|core
operator|.
name|filterchain
operator|.
name|IoFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|filter
operator|.
name|codec
operator|.
name|ProtocolCodecFactory
import|;
end_import

begin_comment
comment|/**  * Mina2 configuration  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|Mina2Configuration
specifier|public
class|class
name|Mina2Configuration
implements|implements
name|Cloneable
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|protocol
specifier|private
name|String
name|protocol
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
DECL|field|host
specifier|private
name|String
name|host
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
DECL|field|port
specifier|private
name|int
name|port
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
name|label
operator|=
literal|"codec"
argument_list|)
DECL|field|textline
specifier|private
name|boolean
name|textline
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"codec"
argument_list|)
DECL|field|textlineDelimiter
specifier|private
name|Mina2TextLineDelimiter
name|textlineDelimiter
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"codec"
argument_list|)
DECL|field|codec
specifier|private
name|ProtocolCodecFactory
name|codec
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"codec"
argument_list|)
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"30000"
argument_list|)
DECL|field|timeout
specifier|private
name|long
name|timeout
init|=
literal|30000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer,advanced"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|lazySessionCreation
specifier|private
name|boolean
name|lazySessionCreation
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|transferExchange
specifier|private
name|boolean
name|transferExchange
decl_stmt|;
annotation|@
name|UriParam
DECL|field|minaLogger
specifier|private
name|boolean
name|minaLogger
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"codec"
argument_list|,
name|defaultValue
operator|=
literal|"-1"
argument_list|)
DECL|field|encoderMaxLineLength
specifier|private
name|int
name|encoderMaxLineLength
init|=
operator|-
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"codec"
argument_list|,
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
argument_list|(
name|label
operator|=
literal|"codec"
argument_list|)
DECL|field|filters
specifier|private
name|List
argument_list|<
name|IoFilter
argument_list|>
name|filters
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"codec"
argument_list|,
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
DECL|field|disconnect
specifier|private
name|boolean
name|disconnect
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|,
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
name|label
operator|=
literal|"consumer,advanced"
argument_list|,
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
literal|"security"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|autoStartTls
specifier|private
name|boolean
name|autoStartTls
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"16"
argument_list|)
DECL|field|maximumPoolSize
specifier|private
name|int
name|maximumPoolSize
init|=
literal|16
decl_stmt|;
comment|// 16 is the default mina setting
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
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
name|label
operator|=
literal|"producer,advanced"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|cachedAddress
specifier|private
name|boolean
name|cachedAddress
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|clientMode
specifier|private
name|boolean
name|clientMode
decl_stmt|;
comment|/**      * Returns a copy of this configuration      */
DECL|method|copy ()
specifier|public
name|Mina2Configuration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|Mina2Configuration
operator|)
name|clone
argument_list|()
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
comment|/**      * Protocol to use      */
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
comment|/**      * Hostname to use. Use localhost or 0.0.0.0 for local server as consumer. For producer use the hostname or ip address of the remote server.      */
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
comment|/**      * Port number      */
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
comment|/**      * Setting to set endpoint as one-way or request-response.      */
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
comment|/**      * Only used for TCP. If no codec is specified, you can use this flag to indicate a text line based codec;      * if not specified or the value is false, then Object Serialization is assumed over TCP.      */
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
DECL|method|getTextlineDelimiter ()
specifier|public
name|Mina2TextLineDelimiter
name|getTextlineDelimiter
parameter_list|()
block|{
return|return
name|textlineDelimiter
return|;
block|}
comment|/**      * Only used for TCP and if textline=true. Sets the text line delimiter to use.      * If none provided, Camel will use DEFAULT.      * This delimiter is used to mark the end of text.      */
DECL|method|setTextlineDelimiter (Mina2TextLineDelimiter textlineDelimiter)
specifier|public
name|void
name|setTextlineDelimiter
parameter_list|(
name|Mina2TextLineDelimiter
name|textlineDelimiter
parameter_list|)
block|{
name|this
operator|.
name|textlineDelimiter
operator|=
name|textlineDelimiter
expr_stmt|;
block|}
DECL|method|getCodec ()
specifier|public
name|ProtocolCodecFactory
name|getCodec
parameter_list|()
block|{
return|return
name|codec
return|;
block|}
comment|/**      * To use a custom minda codec implementation.      */
DECL|method|setCodec (ProtocolCodecFactory codec)
specifier|public
name|void
name|setCodec
parameter_list|(
name|ProtocolCodecFactory
name|codec
parameter_list|)
block|{
name|this
operator|.
name|codec
operator|=
name|codec
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
comment|/**      * You can configure the encoding (a charset name) to use for the TCP textline codec and the UDP protocol.      * If not provided, Camel will use the JVM default Charset      */
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
comment|/**      * You can configure the timeout that specifies how long to wait for a response from a remote server.      * The timeout unit is in milliseconds, so 60000 is 60 seconds.      */
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
DECL|method|isLazySessionCreation ()
specifier|public
name|boolean
name|isLazySessionCreation
parameter_list|()
block|{
return|return
name|lazySessionCreation
return|;
block|}
comment|/**      * Sessions can be lazily created to avoid exceptions, if the remote server is not up and running when the Camel producer is started.      */
DECL|method|setLazySessionCreation (boolean lazySessionCreation)
specifier|public
name|void
name|setLazySessionCreation
parameter_list|(
name|boolean
name|lazySessionCreation
parameter_list|)
block|{
name|this
operator|.
name|lazySessionCreation
operator|=
name|lazySessionCreation
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
comment|/**      * Only used for TCP. You can transfer the exchange over the wire instead of just the body.      * The following fields are transferred: In body, Out body, fault body, In headers, Out headers, fault headers, exchange properties, exchange exception.      * This requires that the objects are serializable. Camel will exclude any non-serializable objects and log it at WARN level.      */
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
comment|/**      * To set the textline protocol encoder max line length. By default the default value of Mina itself is used which are Integer.MAX_VALUE.      */
DECL|method|setEncoderMaxLineLength (int encoderMaxLineLength)
specifier|public
name|void
name|setEncoderMaxLineLength
parameter_list|(
name|int
name|encoderMaxLineLength
parameter_list|)
block|{
name|this
operator|.
name|encoderMaxLineLength
operator|=
name|encoderMaxLineLength
expr_stmt|;
block|}
DECL|method|getEncoderMaxLineLength ()
specifier|public
name|int
name|getEncoderMaxLineLength
parameter_list|()
block|{
return|return
name|encoderMaxLineLength
return|;
block|}
comment|/**      * To set the textline protocol decoder max line length. By default the default value of Mina itself is used which are 1024.      */
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
DECL|method|isMinaLogger ()
specifier|public
name|boolean
name|isMinaLogger
parameter_list|()
block|{
return|return
name|minaLogger
return|;
block|}
comment|/**      * You can enable the Apache MINA logging filter. Apache MINA uses slf4j logging at INFO level to log all input and output.      */
DECL|method|setMinaLogger (boolean minaLogger)
specifier|public
name|void
name|setMinaLogger
parameter_list|(
name|boolean
name|minaLogger
parameter_list|)
block|{
name|this
operator|.
name|minaLogger
operator|=
name|minaLogger
expr_stmt|;
block|}
DECL|method|getFilters ()
specifier|public
name|List
argument_list|<
name|IoFilter
argument_list|>
name|getFilters
parameter_list|()
block|{
return|return
name|filters
return|;
block|}
comment|/**      * You can set a list of Mina IoFilters to use.      */
DECL|method|setFilters (List<IoFilter> filters)
specifier|public
name|void
name|setFilters
parameter_list|(
name|List
argument_list|<
name|IoFilter
argument_list|>
name|filters
parameter_list|)
block|{
name|this
operator|.
name|filters
operator|=
name|filters
expr_stmt|;
block|}
DECL|method|isDatagramProtocol ()
specifier|public
name|boolean
name|isDatagramProtocol
parameter_list|()
block|{
return|return
name|protocol
operator|.
name|equals
argument_list|(
literal|"udp"
argument_list|)
return|;
block|}
comment|/**      * The mina component installs a default codec if both, codec is null and textline is false.      * Setting allowDefaultCodec to false prevents the mina component from installing a default codec as the first element in the filter chain.      * This is useful in scenarios where another filter must be the first in the filter chain, like the SSL filter.      */
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
comment|/**      * Whether or not to disconnect(close) from Mina session right after use. Can be used for both consumer and producer.      */
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
comment|/**      * If sync is enabled then this option dictates MinaConsumer if it should disconnect where there is no reply to send back.      */
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
comment|/**      * If sync is enabled this option dictates MinaConsumer which logging level to use when logging a there is no reply to send back.      */
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
comment|/**      * To configure SSL security.      */
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
DECL|method|isAutoStartTls ()
specifier|public
name|boolean
name|isAutoStartTls
parameter_list|()
block|{
return|return
name|autoStartTls
return|;
block|}
comment|/**      * Whether to auto start SSL handshake.      */
DECL|method|setAutoStartTls (boolean autoStartTls)
specifier|public
name|void
name|setAutoStartTls
parameter_list|(
name|boolean
name|autoStartTls
parameter_list|)
block|{
name|this
operator|.
name|autoStartTls
operator|=
name|autoStartTls
expr_stmt|;
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
comment|/**      * Number of worker threads in the worker pool for TCP and UDP      */
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
comment|/**      * Whether to use ordered thread pool, to ensure events are processed orderly on the same channel.      */
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
comment|/**      * Whether to create the InetAddress once and reuse. Setting this to false allows to pickup DNS changes in the network.      */
DECL|method|setCachedAddress (boolean shouldCacheAddress)
specifier|public
name|void
name|setCachedAddress
parameter_list|(
name|boolean
name|shouldCacheAddress
parameter_list|)
block|{
name|this
operator|.
name|cachedAddress
operator|=
name|shouldCacheAddress
expr_stmt|;
block|}
DECL|method|isCachedAddress ()
specifier|public
name|boolean
name|isCachedAddress
parameter_list|()
block|{
return|return
name|cachedAddress
return|;
block|}
comment|/**      * If the clientMode is true, mina consumer will connect the address as a TCP client.      */
DECL|method|setClientMode (boolean clientMode)
specifier|public
name|void
name|setClientMode
parameter_list|(
name|boolean
name|clientMode
parameter_list|)
block|{
name|this
operator|.
name|clientMode
operator|=
name|clientMode
expr_stmt|;
block|}
DECL|method|isClientMode ()
specifier|public
name|boolean
name|isClientMode
parameter_list|()
block|{
return|return
name|clientMode
return|;
block|}
comment|// here we just shows the option setting of host, port, protocol
DECL|method|getUriString ()
specifier|public
name|String
name|getUriString
parameter_list|()
block|{
return|return
literal|"mina2:"
operator|+
name|getProtocol
argument_list|()
operator|+
literal|":"
operator|+
name|getHost
argument_list|()
operator|+
literal|":"
operator|+
name|getPort
argument_list|()
return|;
block|}
block|}
end_class

end_unit

