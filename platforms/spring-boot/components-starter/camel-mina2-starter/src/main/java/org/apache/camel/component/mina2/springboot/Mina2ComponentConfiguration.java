begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina2.springboot
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
operator|.
name|springboot
package|;
end_package

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
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|component
operator|.
name|mina2
operator|.
name|Mina2TextLineDelimiter
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
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
name|filter
operator|.
name|codec
operator|.
name|ProtocolCodecFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * Socket level networking using TCP or UDP with the Apache Mina 2.x library.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.mina2"
argument_list|)
DECL|class|Mina2ComponentConfiguration
specifier|public
class|class
name|Mina2ComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the mina2 component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * To use the shared mina configuration.      */
DECL|field|configuration
specifier|private
name|Mina2ConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * Enable usage of global SSL context parameters.      */
DECL|field|useGlobalSslContextParameters
specifier|private
name|Boolean
name|useGlobalSslContextParameters
init|=
literal|false
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
comment|/**      * Whether the component should use basic property binding (Camel 2.x) or      * the newer property binding with additional capabilities      */
DECL|field|basicPropertyBinding
specifier|private
name|Boolean
name|basicPropertyBinding
init|=
literal|false
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|Mina2ConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( Mina2ConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|Mina2ConfigurationNestedConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getUseGlobalSslContextParameters ()
specifier|public
name|Boolean
name|getUseGlobalSslContextParameters
parameter_list|()
block|{
return|return
name|useGlobalSslContextParameters
return|;
block|}
DECL|method|setUseGlobalSslContextParameters ( Boolean useGlobalSslContextParameters)
specifier|public
name|void
name|setUseGlobalSslContextParameters
parameter_list|(
name|Boolean
name|useGlobalSslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|useGlobalSslContextParameters
operator|=
name|useGlobalSslContextParameters
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
DECL|method|getBasicPropertyBinding ()
specifier|public
name|Boolean
name|getBasicPropertyBinding
parameter_list|()
block|{
return|return
name|basicPropertyBinding
return|;
block|}
DECL|method|setBasicPropertyBinding (Boolean basicPropertyBinding)
specifier|public
name|void
name|setBasicPropertyBinding
parameter_list|(
name|Boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|this
operator|.
name|basicPropertyBinding
operator|=
name|basicPropertyBinding
expr_stmt|;
block|}
DECL|class|Mina2ConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|Mina2ConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina2
operator|.
name|Mina2Configuration
operator|.
name|class
decl_stmt|;
comment|/**          * Protocol to use          */
DECL|field|protocol
specifier|private
name|String
name|protocol
decl_stmt|;
comment|/**          * Hostname to use. Use localhost or 0.0.0.0 for local server as          * consumer. For producer use the hostname or ip address of the remote          * server.          */
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
comment|/**          * Port number          */
DECL|field|port
specifier|private
name|Integer
name|port
decl_stmt|;
comment|/**          * Setting to set endpoint as one-way or request-response.          */
DECL|field|sync
specifier|private
name|Boolean
name|sync
init|=
literal|true
decl_stmt|;
comment|/**          * Only used for TCP. If no codec is specified, you can use this flag to          * indicate a text line based codec; if not specified or the value is          * false, then Object Serialization is assumed over TCP.          */
DECL|field|textline
specifier|private
name|Boolean
name|textline
init|=
literal|false
decl_stmt|;
comment|/**          * Only used for TCP and if textline=true. Sets the text line delimiter          * to use. If none provided, Camel will use DEFAULT. This delimiter is          * used to mark the end of text.          */
DECL|field|textlineDelimiter
specifier|private
name|Mina2TextLineDelimiter
name|textlineDelimiter
decl_stmt|;
comment|/**          * To use a custom minda codec implementation.          */
DECL|field|codec
specifier|private
name|ProtocolCodecFactory
name|codec
decl_stmt|;
comment|/**          * You can configure the encoding (a charset name) to use for the TCP          * textline codec and the UDP protocol. If not provided, Camel will use          * the JVM default Charset          */
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
comment|/**          * Maximum amount of time it should take to send data to the MINA          * session. Default is 10000 milliseconds.          */
DECL|field|writeTimeout
specifier|private
name|Long
name|writeTimeout
init|=
literal|10000L
decl_stmt|;
comment|/**          * You can configure the timeout that specifies how long to wait for a          * response from a remote server. The timeout unit is in milliseconds,          * so 60000 is 60 seconds.          */
DECL|field|timeout
specifier|private
name|Long
name|timeout
init|=
literal|30000L
decl_stmt|;
comment|/**          * Sessions can be lazily created to avoid exceptions, if the remote          * server is not up and running when the Camel producer is started.          */
DECL|field|lazySessionCreation
specifier|private
name|Boolean
name|lazySessionCreation
init|=
literal|true
decl_stmt|;
comment|/**          * Only used for TCP. You can transfer the exchange over the wire          * instead of just the body. The following fields are transferred: In          * body, Out body, fault body, In headers, Out headers, fault headers,          * exchange properties, exchange exception. This requires that the          * objects are serializable. Camel will exclude any non-serializable          * objects and log it at WARN level.          */
DECL|field|transferExchange
specifier|private
name|Boolean
name|transferExchange
init|=
literal|false
decl_stmt|;
comment|/**          * To set the textline protocol encoder max line length. By default the          * default value of Mina itself is used which are Integer.MAX_VALUE.          */
DECL|field|encoderMaxLineLength
specifier|private
name|Integer
name|encoderMaxLineLength
init|=
operator|-
literal|1
decl_stmt|;
comment|/**          * To set the textline protocol decoder max line length. By default the          * default value of Mina itself is used which are 1024.          */
DECL|field|decoderMaxLineLength
specifier|private
name|Integer
name|decoderMaxLineLength
init|=
literal|1024
decl_stmt|;
comment|/**          * You can enable the Apache MINA logging filter. Apache MINA uses slf4j          * logging at INFO level to log all input and output.          */
DECL|field|minaLogger
specifier|private
name|Boolean
name|minaLogger
init|=
literal|false
decl_stmt|;
comment|/**          * You can set a list of Mina IoFilters to use.          */
DECL|field|filters
specifier|private
name|List
name|filters
decl_stmt|;
comment|/**          * The mina component installs a default codec if both, codec is null          * and textline is false. Setting allowDefaultCodec to false prevents          * the mina component from installing a default codec as the first          * element in the filter chain. This is useful in scenarios where          * another filter must be the first in the filter chain, like the SSL          * filter.          */
DECL|field|allowDefaultCodec
specifier|private
name|Boolean
name|allowDefaultCodec
init|=
literal|true
decl_stmt|;
comment|/**          * Whether or not to disconnect(close) from Mina session right after          * use. Can be used for both consumer and producer.          */
DECL|field|disconnect
specifier|private
name|Boolean
name|disconnect
init|=
literal|false
decl_stmt|;
comment|/**          * If sync is enabled then this option dictates MinaConsumer if it          * should disconnect where there is no reply to send back.          */
DECL|field|disconnectOnNoReply
specifier|private
name|Boolean
name|disconnectOnNoReply
init|=
literal|true
decl_stmt|;
comment|/**          * If sync is enabled this option dictates MinaConsumer which logging          * level to use when logging a there is no reply to send back.          */
DECL|field|noReplyLogLevel
specifier|private
name|LoggingLevel
name|noReplyLogLevel
init|=
name|LoggingLevel
operator|.
name|WARN
decl_stmt|;
comment|/**          * To configure SSL security.          */
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
comment|/**          * Whether to auto start SSL handshake.          */
DECL|field|autoStartTls
specifier|private
name|Boolean
name|autoStartTls
init|=
literal|true
decl_stmt|;
comment|/**          * Number of worker threads in the worker pool for TCP and UDP          */
DECL|field|maximumPoolSize
specifier|private
name|Integer
name|maximumPoolSize
init|=
literal|16
decl_stmt|;
comment|/**          * Whether to use ordered thread pool, to ensure events are processed          * orderly on the same channel.          */
DECL|field|orderedThreadPoolExecutor
specifier|private
name|Boolean
name|orderedThreadPoolExecutor
init|=
literal|true
decl_stmt|;
comment|/**          * Whether to create the InetAddress once and reuse. Setting this to          * false allows to pickup DNS changes in the network.          */
DECL|field|cachedAddress
specifier|private
name|Boolean
name|cachedAddress
init|=
literal|true
decl_stmt|;
comment|/**          * If the clientMode is true, mina consumer will connect the address as          * a TCP client.          */
DECL|field|clientMode
specifier|private
name|Boolean
name|clientMode
init|=
literal|false
decl_stmt|;
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
name|Integer
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (Integer port)
specifier|public
name|void
name|setPort
parameter_list|(
name|Integer
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
DECL|method|getSync ()
specifier|public
name|Boolean
name|getSync
parameter_list|()
block|{
return|return
name|sync
return|;
block|}
DECL|method|setSync (Boolean sync)
specifier|public
name|void
name|setSync
parameter_list|(
name|Boolean
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
DECL|method|getTextline ()
specifier|public
name|Boolean
name|getTextline
parameter_list|()
block|{
return|return
name|textline
return|;
block|}
DECL|method|setTextline (Boolean textline)
specifier|public
name|void
name|setTextline
parameter_list|(
name|Boolean
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
DECL|method|setTextlineDelimiter ( Mina2TextLineDelimiter textlineDelimiter)
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
DECL|method|getWriteTimeout ()
specifier|public
name|Long
name|getWriteTimeout
parameter_list|()
block|{
return|return
name|writeTimeout
return|;
block|}
DECL|method|setWriteTimeout (Long writeTimeout)
specifier|public
name|void
name|setWriteTimeout
parameter_list|(
name|Long
name|writeTimeout
parameter_list|)
block|{
name|this
operator|.
name|writeTimeout
operator|=
name|writeTimeout
expr_stmt|;
block|}
DECL|method|getTimeout ()
specifier|public
name|Long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|method|setTimeout (Long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|Long
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
DECL|method|getLazySessionCreation ()
specifier|public
name|Boolean
name|getLazySessionCreation
parameter_list|()
block|{
return|return
name|lazySessionCreation
return|;
block|}
DECL|method|setLazySessionCreation (Boolean lazySessionCreation)
specifier|public
name|void
name|setLazySessionCreation
parameter_list|(
name|Boolean
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
DECL|method|getTransferExchange ()
specifier|public
name|Boolean
name|getTransferExchange
parameter_list|()
block|{
return|return
name|transferExchange
return|;
block|}
DECL|method|setTransferExchange (Boolean transferExchange)
specifier|public
name|void
name|setTransferExchange
parameter_list|(
name|Boolean
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
DECL|method|getEncoderMaxLineLength ()
specifier|public
name|Integer
name|getEncoderMaxLineLength
parameter_list|()
block|{
return|return
name|encoderMaxLineLength
return|;
block|}
DECL|method|setEncoderMaxLineLength (Integer encoderMaxLineLength)
specifier|public
name|void
name|setEncoderMaxLineLength
parameter_list|(
name|Integer
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
DECL|method|getDecoderMaxLineLength ()
specifier|public
name|Integer
name|getDecoderMaxLineLength
parameter_list|()
block|{
return|return
name|decoderMaxLineLength
return|;
block|}
DECL|method|setDecoderMaxLineLength (Integer decoderMaxLineLength)
specifier|public
name|void
name|setDecoderMaxLineLength
parameter_list|(
name|Integer
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
DECL|method|getMinaLogger ()
specifier|public
name|Boolean
name|getMinaLogger
parameter_list|()
block|{
return|return
name|minaLogger
return|;
block|}
DECL|method|setMinaLogger (Boolean minaLogger)
specifier|public
name|void
name|setMinaLogger
parameter_list|(
name|Boolean
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
name|getFilters
parameter_list|()
block|{
return|return
name|filters
return|;
block|}
DECL|method|setFilters (List filters)
specifier|public
name|void
name|setFilters
parameter_list|(
name|List
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
DECL|method|getAllowDefaultCodec ()
specifier|public
name|Boolean
name|getAllowDefaultCodec
parameter_list|()
block|{
return|return
name|allowDefaultCodec
return|;
block|}
DECL|method|setAllowDefaultCodec (Boolean allowDefaultCodec)
specifier|public
name|void
name|setAllowDefaultCodec
parameter_list|(
name|Boolean
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
DECL|method|getDisconnect ()
specifier|public
name|Boolean
name|getDisconnect
parameter_list|()
block|{
return|return
name|disconnect
return|;
block|}
DECL|method|setDisconnect (Boolean disconnect)
specifier|public
name|void
name|setDisconnect
parameter_list|(
name|Boolean
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
DECL|method|getDisconnectOnNoReply ()
specifier|public
name|Boolean
name|getDisconnectOnNoReply
parameter_list|()
block|{
return|return
name|disconnectOnNoReply
return|;
block|}
DECL|method|setDisconnectOnNoReply (Boolean disconnectOnNoReply)
specifier|public
name|void
name|setDisconnectOnNoReply
parameter_list|(
name|Boolean
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
DECL|method|setSslContextParameters ( SSLContextParameters sslContextParameters)
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
DECL|method|getAutoStartTls ()
specifier|public
name|Boolean
name|getAutoStartTls
parameter_list|()
block|{
return|return
name|autoStartTls
return|;
block|}
DECL|method|setAutoStartTls (Boolean autoStartTls)
specifier|public
name|void
name|setAutoStartTls
parameter_list|(
name|Boolean
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
name|Integer
name|getMaximumPoolSize
parameter_list|()
block|{
return|return
name|maximumPoolSize
return|;
block|}
DECL|method|setMaximumPoolSize (Integer maximumPoolSize)
specifier|public
name|void
name|setMaximumPoolSize
parameter_list|(
name|Integer
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
DECL|method|getOrderedThreadPoolExecutor ()
specifier|public
name|Boolean
name|getOrderedThreadPoolExecutor
parameter_list|()
block|{
return|return
name|orderedThreadPoolExecutor
return|;
block|}
DECL|method|setOrderedThreadPoolExecutor ( Boolean orderedThreadPoolExecutor)
specifier|public
name|void
name|setOrderedThreadPoolExecutor
parameter_list|(
name|Boolean
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
DECL|method|getCachedAddress ()
specifier|public
name|Boolean
name|getCachedAddress
parameter_list|()
block|{
return|return
name|cachedAddress
return|;
block|}
DECL|method|setCachedAddress (Boolean cachedAddress)
specifier|public
name|void
name|setCachedAddress
parameter_list|(
name|Boolean
name|cachedAddress
parameter_list|)
block|{
name|this
operator|.
name|cachedAddress
operator|=
name|cachedAddress
expr_stmt|;
block|}
DECL|method|getClientMode ()
specifier|public
name|Boolean
name|getClientMode
parameter_list|()
block|{
return|return
name|clientMode
return|;
block|}
DECL|method|setClientMode (Boolean clientMode)
specifier|public
name|void
name|setClientMode
parameter_list|(
name|Boolean
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
block|}
block|}
end_class

end_unit

