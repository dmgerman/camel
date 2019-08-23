begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mllp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Socket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketAddress
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
name|Date
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|support
operator|.
name|DefaultEndpoint
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

begin_comment
comment|/**  * Provides functionality required by Healthcare providers to communicate with other systems using the MLLP protocol.  *  *<p/>  * NOTE: MLLP payloads are not logged unless the logging level is set to DEBUG or TRACE to avoid introducing PHI into the log files.  Logging of PHI can be globally disabled by setting the  * org.apache.camel.mllp.logPHI system property to false.  *<p/>  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"MLLP Endpoint"
argument_list|)
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"mllp"
argument_list|,
name|firstVersion
operator|=
literal|"2.17.0"
argument_list|,
name|title
operator|=
literal|"MLLP"
argument_list|,
name|syntax
operator|=
literal|"mllp:hostname:port"
argument_list|,
name|label
operator|=
literal|"mllp"
argument_list|,
name|generateConfigurer
operator|=
literal|false
argument_list|)
DECL|class|MllpEndpoint
specifier|public
class|class
name|MllpEndpoint
extends|extends
name|DefaultEndpoint
block|{
comment|// Use constants from MllpProtocolConstants
annotation|@
name|Deprecated
argument_list|()
DECL|field|START_OF_BLOCK
specifier|public
specifier|static
specifier|final
name|char
name|START_OF_BLOCK
init|=
name|MllpProtocolConstants
operator|.
name|START_OF_BLOCK
decl_stmt|;
annotation|@
name|Deprecated
argument_list|()
DECL|field|END_OF_BLOCK
specifier|public
specifier|static
specifier|final
name|char
name|END_OF_BLOCK
init|=
name|MllpProtocolConstants
operator|.
name|END_OF_BLOCK
decl_stmt|;
annotation|@
name|Deprecated
argument_list|()
DECL|field|END_OF_DATA
specifier|public
specifier|static
specifier|final
name|char
name|END_OF_DATA
init|=
name|MllpProtocolConstants
operator|.
name|END_OF_DATA
decl_stmt|;
annotation|@
name|Deprecated
argument_list|()
DECL|field|END_OF_STREAM
specifier|public
specifier|static
specifier|final
name|int
name|END_OF_STREAM
init|=
name|MllpProtocolConstants
operator|.
name|END_OF_STREAM
decl_stmt|;
annotation|@
name|Deprecated
argument_list|()
DECL|field|SEGMENT_DELIMITER
specifier|public
specifier|static
specifier|final
name|char
name|SEGMENT_DELIMITER
init|=
name|MllpProtocolConstants
operator|.
name|SEGMENT_DELIMITER
decl_stmt|;
annotation|@
name|Deprecated
argument_list|()
DECL|field|MESSAGE_TERMINATOR
specifier|public
specifier|static
specifier|final
name|char
name|MESSAGE_TERMINATOR
init|=
name|MllpProtocolConstants
operator|.
name|MESSAGE_TERMINATOR
decl_stmt|;
annotation|@
name|Deprecated
comment|// Use MllpComponent.getDefaultCharset()
DECL|field|DEFAULT_CHARSET
specifier|public
specifier|static
specifier|final
name|Charset
name|DEFAULT_CHARSET
init|=
name|MllpComponent
operator|.
name|getDefaultCharset
argument_list|()
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|hostname
name|String
name|hostname
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|port
name|int
name|port
init|=
operator|-
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|configuration
name|MllpConfiguration
name|configuration
decl_stmt|;
DECL|field|lastConnectionActivityTicks
name|Long
name|lastConnectionActivityTicks
decl_stmt|;
DECL|field|lastConnectionEstablishedTicks
name|Long
name|lastConnectionEstablishedTicks
decl_stmt|;
DECL|field|lastConnectionTerminatedTicks
name|Long
name|lastConnectionTerminatedTicks
decl_stmt|;
DECL|method|MllpEndpoint (String uri, MllpComponent component, MllpConfiguration configuration)
specifier|public
name|MllpEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|MllpComponent
name|component
parameter_list|,
name|MllpConfiguration
name|configuration
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
name|configuration
operator|=
name|configuration
operator|.
name|copy
argument_list|()
expr_stmt|;
name|super
operator|.
name|setBridgeErrorHandler
argument_list|(
name|configuration
operator|.
name|isBridgeErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|setExchangePattern
argument_list|(
name|configuration
operator|.
name|getExchangePattern
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|setSynchronous
argument_list|(
name|configuration
operator|.
name|isSynchronous
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createExchange (ExchangePattern exchangePattern)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|exchangePattern
parameter_list|)
block|{
name|Exchange
name|mllpExchange
init|=
name|super
operator|.
name|createExchange
argument_list|(
name|exchangePattern
argument_list|)
decl_stmt|;
name|setExchangeProperties
argument_list|(
name|mllpExchange
argument_list|)
expr_stmt|;
return|return
name|mllpExchange
return|;
block|}
annotation|@
name|Override
DECL|method|setExchangePattern (ExchangePattern exchangePattern)
specifier|public
name|void
name|setExchangePattern
parameter_list|(
name|ExchangePattern
name|exchangePattern
parameter_list|)
block|{
name|configuration
operator|.
name|setExchangePattern
argument_list|(
name|exchangePattern
argument_list|)
expr_stmt|;
name|super
operator|.
name|setExchangePattern
argument_list|(
name|configuration
operator|.
name|getExchangePattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setSynchronous (boolean synchronous)
specifier|public
name|void
name|setSynchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|configuration
operator|.
name|setSynchronous
argument_list|(
name|synchronous
argument_list|)
expr_stmt|;
name|super
operator|.
name|setSynchronous
argument_list|(
name|configuration
operator|.
name|isSynchronous
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setBridgeErrorHandler (boolean bridgeErrorHandler)
specifier|public
name|void
name|setBridgeErrorHandler
parameter_list|(
name|boolean
name|bridgeErrorHandler
parameter_list|)
block|{
name|configuration
operator|.
name|setBridgeErrorHandler
argument_list|(
name|bridgeErrorHandler
argument_list|)
expr_stmt|;
name|super
operator|.
name|setBridgeErrorHandler
argument_list|(
name|configuration
operator|.
name|isBridgeErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|setExchangeProperties (Exchange mllpExchange)
specifier|private
name|void
name|setExchangeProperties
parameter_list|(
name|Exchange
name|mllpExchange
parameter_list|)
block|{
if|if
condition|(
name|configuration
operator|.
name|hasCharsetName
argument_list|()
condition|)
block|{
name|mllpExchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|configuration
operator|.
name|getCharsetName
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|log
operator|.
name|trace
argument_list|(
literal|"({}).createProducer()"
argument_list|,
name|this
operator|.
name|getEndpointKey
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|MllpTcpClientProducer
argument_list|(
name|this
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
name|log
operator|.
name|trace
argument_list|(
literal|"({}).createConsumer(Processor)"
argument_list|,
name|this
operator|.
name|getEndpointKey
argument_list|()
argument_list|)
expr_stmt|;
name|Consumer
name|consumer
init|=
operator|new
name|MllpTcpServerConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Last activity time"
argument_list|)
DECL|method|getLastConnectionActivityTime ()
specifier|public
name|Date
name|getLastConnectionActivityTime
parameter_list|()
block|{
if|if
condition|(
name|lastConnectionActivityTicks
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|Date
argument_list|(
name|lastConnectionActivityTicks
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Last connection established time"
argument_list|)
DECL|method|getLastConnectionEstablishedTime ()
specifier|public
name|Date
name|getLastConnectionEstablishedTime
parameter_list|()
block|{
if|if
condition|(
name|lastConnectionEstablishedTicks
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|Date
argument_list|(
name|lastConnectionEstablishedTicks
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Last connection terminated time"
argument_list|)
DECL|method|getLastConnectionTerminatedTime ()
specifier|public
name|Date
name|getLastConnectionTerminatedTime
parameter_list|()
block|{
return|return
name|lastConnectionTerminatedTicks
operator|!=
literal|null
condition|?
operator|new
name|Date
argument_list|(
name|lastConnectionTerminatedTicks
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|hasLastConnectionActivityTicks ()
specifier|public
name|boolean
name|hasLastConnectionActivityTicks
parameter_list|()
block|{
return|return
name|lastConnectionActivityTicks
operator|!=
literal|null
operator|&&
name|lastConnectionActivityTicks
operator|>
literal|0
return|;
block|}
DECL|method|getLastConnectionActivityTicks ()
specifier|public
name|Long
name|getLastConnectionActivityTicks
parameter_list|()
block|{
return|return
name|lastConnectionActivityTicks
return|;
block|}
DECL|method|updateLastConnectionActivityTicks ()
specifier|public
name|void
name|updateLastConnectionActivityTicks
parameter_list|()
block|{
name|updateLastConnectionActivityTicks
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|updateLastConnectionActivityTicks (long epochTicks)
specifier|public
name|void
name|updateLastConnectionActivityTicks
parameter_list|(
name|long
name|epochTicks
parameter_list|)
block|{
name|lastConnectionActivityTicks
operator|=
name|epochTicks
expr_stmt|;
block|}
DECL|method|updateLastConnectionEstablishedTicks ()
specifier|public
name|void
name|updateLastConnectionEstablishedTicks
parameter_list|()
block|{
name|lastConnectionEstablishedTicks
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
block|}
DECL|method|updateLastConnectionTerminatedTicks ()
specifier|public
name|void
name|updateLastConnectionTerminatedTicks
parameter_list|()
block|{
name|lastConnectionTerminatedTicks
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
block|}
DECL|method|getHostname ()
specifier|public
name|String
name|getHostname
parameter_list|()
block|{
return|return
name|hostname
return|;
block|}
comment|/**      * Hostname or IP for connection for the TCP connection.      *      * The default value is null, which means any local IP address      *      * @param hostname Hostname or IP      */
DECL|method|setHostname (String hostname)
specifier|public
name|void
name|setHostname
parameter_list|(
name|String
name|hostname
parameter_list|)
block|{
name|this
operator|.
name|hostname
operator|=
name|hostname
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
comment|/**      * Port number for the TCP connection      *      * @param port TCP port      */
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
DECL|method|hasConfiguration ()
specifier|public
name|boolean
name|hasConfiguration
parameter_list|()
block|{
return|return
name|configuration
operator|!=
literal|null
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|MllpConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (MllpConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|MllpConfiguration
name|configuration
parameter_list|)
block|{
if|if
condition|(
name|hasConfiguration
argument_list|()
condition|)
block|{
name|this
operator|.
name|configuration
operator|.
name|copy
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
operator|.
name|copy
argument_list|()
expr_stmt|;
block|}
block|}
comment|// Pass-through configuration methods
DECL|method|setBacklog (Integer backlog)
specifier|public
name|void
name|setBacklog
parameter_list|(
name|Integer
name|backlog
parameter_list|)
block|{
name|configuration
operator|.
name|setBacklog
argument_list|(
name|backlog
argument_list|)
expr_stmt|;
block|}
DECL|method|setBindTimeout (int bindTimeout)
specifier|public
name|void
name|setBindTimeout
parameter_list|(
name|int
name|bindTimeout
parameter_list|)
block|{
name|configuration
operator|.
name|setBindTimeout
argument_list|(
name|bindTimeout
argument_list|)
expr_stmt|;
block|}
DECL|method|setBindRetryInterval (int bindRetryInterval)
specifier|public
name|void
name|setBindRetryInterval
parameter_list|(
name|int
name|bindRetryInterval
parameter_list|)
block|{
name|configuration
operator|.
name|setBindRetryInterval
argument_list|(
name|bindRetryInterval
argument_list|)
expr_stmt|;
block|}
DECL|method|setLenientBind (boolean lenientBind)
specifier|public
name|void
name|setLenientBind
parameter_list|(
name|boolean
name|lenientBind
parameter_list|)
block|{
name|configuration
operator|.
name|setLenientBind
argument_list|(
name|lenientBind
argument_list|)
expr_stmt|;
block|}
DECL|method|setAcceptTimeout (int acceptTimeout)
specifier|public
name|void
name|setAcceptTimeout
parameter_list|(
name|int
name|acceptTimeout
parameter_list|)
block|{
name|configuration
operator|.
name|setAcceptTimeout
argument_list|(
name|acceptTimeout
argument_list|)
expr_stmt|;
block|}
DECL|method|setConnectTimeout (int connectTimeout)
specifier|public
name|void
name|setConnectTimeout
parameter_list|(
name|int
name|connectTimeout
parameter_list|)
block|{
name|configuration
operator|.
name|setConnectTimeout
argument_list|(
name|connectTimeout
argument_list|)
expr_stmt|;
block|}
DECL|method|setReceiveTimeout (int receiveTimeout)
specifier|public
name|void
name|setReceiveTimeout
parameter_list|(
name|int
name|receiveTimeout
parameter_list|)
block|{
name|configuration
operator|.
name|setReceiveTimeout
argument_list|(
name|receiveTimeout
argument_list|)
expr_stmt|;
block|}
DECL|method|setIdleTimeout (Integer idleTimeout)
specifier|public
name|void
name|setIdleTimeout
parameter_list|(
name|Integer
name|idleTimeout
parameter_list|)
block|{
name|configuration
operator|.
name|setIdleTimeout
argument_list|(
name|idleTimeout
argument_list|)
expr_stmt|;
block|}
DECL|method|setReadTimeout (int readTimeout)
specifier|public
name|void
name|setReadTimeout
parameter_list|(
name|int
name|readTimeout
parameter_list|)
block|{
name|configuration
operator|.
name|setReadTimeout
argument_list|(
name|readTimeout
argument_list|)
expr_stmt|;
block|}
DECL|method|setKeepAlive (Boolean keepAlive)
specifier|public
name|void
name|setKeepAlive
parameter_list|(
name|Boolean
name|keepAlive
parameter_list|)
block|{
name|configuration
operator|.
name|setKeepAlive
argument_list|(
name|keepAlive
argument_list|)
expr_stmt|;
block|}
DECL|method|setTcpNoDelay (Boolean tcpNoDelay)
specifier|public
name|void
name|setTcpNoDelay
parameter_list|(
name|Boolean
name|tcpNoDelay
parameter_list|)
block|{
name|configuration
operator|.
name|setTcpNoDelay
argument_list|(
name|tcpNoDelay
argument_list|)
expr_stmt|;
block|}
DECL|method|setReuseAddress (Boolean reuseAddress)
specifier|public
name|void
name|setReuseAddress
parameter_list|(
name|Boolean
name|reuseAddress
parameter_list|)
block|{
name|configuration
operator|.
name|setReuseAddress
argument_list|(
name|reuseAddress
argument_list|)
expr_stmt|;
block|}
DECL|method|setReceiveBufferSize (Integer receiveBufferSize)
specifier|public
name|void
name|setReceiveBufferSize
parameter_list|(
name|Integer
name|receiveBufferSize
parameter_list|)
block|{
name|configuration
operator|.
name|setReceiveBufferSize
argument_list|(
name|receiveBufferSize
argument_list|)
expr_stmt|;
block|}
DECL|method|setSendBufferSize (Integer sendBufferSize)
specifier|public
name|void
name|setSendBufferSize
parameter_list|(
name|Integer
name|sendBufferSize
parameter_list|)
block|{
name|configuration
operator|.
name|setSendBufferSize
argument_list|(
name|sendBufferSize
argument_list|)
expr_stmt|;
block|}
DECL|method|setAutoAck (Boolean autoAck)
specifier|public
name|void
name|setAutoAck
parameter_list|(
name|Boolean
name|autoAck
parameter_list|)
block|{
name|configuration
operator|.
name|setAutoAck
argument_list|(
name|autoAck
argument_list|)
expr_stmt|;
block|}
DECL|method|setHl7Headers (Boolean hl7Headers)
specifier|public
name|void
name|setHl7Headers
parameter_list|(
name|Boolean
name|hl7Headers
parameter_list|)
block|{
name|configuration
operator|.
name|setHl7Headers
argument_list|(
name|hl7Headers
argument_list|)
expr_stmt|;
block|}
comment|/**      * @deprecated this parameter will be ignored.      *      * @param bufferWrites      */
annotation|@
name|Deprecated
DECL|method|setBufferWrites (Boolean bufferWrites)
specifier|public
name|void
name|setBufferWrites
parameter_list|(
name|Boolean
name|bufferWrites
parameter_list|)
block|{
name|configuration
operator|.
name|setBufferWrites
argument_list|(
name|bufferWrites
argument_list|)
expr_stmt|;
block|}
DECL|method|setRequireEndOfData (Boolean requireEndOfData)
specifier|public
name|void
name|setRequireEndOfData
parameter_list|(
name|Boolean
name|requireEndOfData
parameter_list|)
block|{
name|configuration
operator|.
name|setRequireEndOfData
argument_list|(
name|requireEndOfData
argument_list|)
expr_stmt|;
block|}
DECL|method|setStringPayload (Boolean stringPayload)
specifier|public
name|void
name|setStringPayload
parameter_list|(
name|Boolean
name|stringPayload
parameter_list|)
block|{
name|configuration
operator|.
name|setStringPayload
argument_list|(
name|stringPayload
argument_list|)
expr_stmt|;
block|}
DECL|method|setValidatePayload (Boolean validatePayload)
specifier|public
name|void
name|setValidatePayload
parameter_list|(
name|Boolean
name|validatePayload
parameter_list|)
block|{
name|configuration
operator|.
name|setValidatePayload
argument_list|(
name|validatePayload
argument_list|)
expr_stmt|;
block|}
DECL|method|setCharsetName (String charsetName)
specifier|public
name|void
name|setCharsetName
parameter_list|(
name|String
name|charsetName
parameter_list|)
block|{
name|configuration
operator|.
name|setCharsetName
argument_list|(
name|charsetName
argument_list|)
expr_stmt|;
block|}
DECL|method|setMaxConcurrentConsumers (int maxConcurrentConsumers)
specifier|public
name|void
name|setMaxConcurrentConsumers
parameter_list|(
name|int
name|maxConcurrentConsumers
parameter_list|)
block|{
name|configuration
operator|.
name|setMaxConcurrentConsumers
argument_list|(
name|maxConcurrentConsumers
argument_list|)
expr_stmt|;
block|}
comment|// Utility methods for producers and consumers
DECL|method|checkBeforeSendProperties (Exchange exchange, Socket socket, Logger log)
specifier|public
name|boolean
name|checkBeforeSendProperties
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Socket
name|socket
parameter_list|,
name|Logger
name|log
parameter_list|)
block|{
specifier|final
name|String
name|logMessageFormat
init|=
literal|"Exchange property {} = {} - {} connection"
decl_stmt|;
name|boolean
name|answer
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|MllpConstants
operator|.
name|MLLP_RESET_CONNECTION_BEFORE_SEND
argument_list|,
name|boolean
operator|.
name|class
argument_list|)
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|logMessageFormat
argument_list|,
name|MllpConstants
operator|.
name|MLLP_RESET_CONNECTION_BEFORE_SEND
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|MllpConstants
operator|.
name|MLLP_RESET_CONNECTION_BEFORE_SEND
argument_list|)
argument_list|,
literal|"resetting"
argument_list|)
expr_stmt|;
name|doConnectionClose
argument_list|(
name|socket
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|answer
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|MllpConstants
operator|.
name|MLLP_CLOSE_CONNECTION_BEFORE_SEND
argument_list|,
name|boolean
operator|.
name|class
argument_list|)
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|logMessageFormat
argument_list|,
name|MllpConstants
operator|.
name|MLLP_CLOSE_CONNECTION_BEFORE_SEND
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|MllpConstants
operator|.
name|MLLP_CLOSE_CONNECTION_BEFORE_SEND
argument_list|)
argument_list|,
literal|"closing"
argument_list|)
expr_stmt|;
name|doConnectionClose
argument_list|(
name|socket
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|answer
operator|=
literal|false
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|checkAfterSendProperties (Exchange exchange, Socket socket, Logger log)
specifier|public
name|boolean
name|checkAfterSendProperties
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Socket
name|socket
parameter_list|,
name|Logger
name|log
parameter_list|)
block|{
specifier|final
name|String
name|logMessageFormat
init|=
literal|"Exchange property {} = {} - {} connection"
decl_stmt|;
name|boolean
name|answer
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|MllpConstants
operator|.
name|MLLP_RESET_CONNECTION_AFTER_SEND
argument_list|,
name|boolean
operator|.
name|class
argument_list|)
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|logMessageFormat
argument_list|,
name|MllpConstants
operator|.
name|MLLP_RESET_CONNECTION_AFTER_SEND
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|MllpConstants
operator|.
name|MLLP_RESET_CONNECTION_AFTER_SEND
argument_list|)
argument_list|,
literal|"resetting"
argument_list|)
expr_stmt|;
name|doConnectionClose
argument_list|(
name|socket
argument_list|,
literal|true
argument_list|,
name|log
argument_list|)
expr_stmt|;
name|answer
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|MllpConstants
operator|.
name|MLLP_CLOSE_CONNECTION_AFTER_SEND
argument_list|,
name|boolean
operator|.
name|class
argument_list|)
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|logMessageFormat
argument_list|,
name|MllpConstants
operator|.
name|MLLP_CLOSE_CONNECTION_AFTER_SEND
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|MllpConstants
operator|.
name|MLLP_CLOSE_CONNECTION_AFTER_SEND
argument_list|)
argument_list|,
literal|"closing"
argument_list|)
expr_stmt|;
name|doConnectionClose
argument_list|(
name|socket
argument_list|,
literal|false
argument_list|,
name|log
argument_list|)
expr_stmt|;
name|answer
operator|=
literal|false
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|doConnectionClose (Socket socket, boolean reset, Logger log)
specifier|public
name|void
name|doConnectionClose
parameter_list|(
name|Socket
name|socket
parameter_list|,
name|boolean
name|reset
parameter_list|,
name|Logger
name|log
parameter_list|)
block|{
name|String
name|ignoringCallLogFormat
init|=
literal|"Ignoring {} Connection request because - {}: localAddress={} remoteAddress={}"
decl_stmt|;
if|if
condition|(
name|socket
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|log
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|ignoringCallLogFormat
argument_list|,
name|reset
condition|?
literal|"Reset"
else|:
literal|"Close"
argument_list|,
literal|"Socket is null"
argument_list|,
literal|"null"
argument_list|,
literal|"null"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|SocketAddress
name|localSocketAddress
init|=
name|socket
operator|.
name|getLocalSocketAddress
argument_list|()
decl_stmt|;
name|SocketAddress
name|remoteSocketAddress
init|=
name|socket
operator|.
name|getRemoteSocketAddress
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|socket
operator|.
name|isConnected
argument_list|()
condition|)
block|{
if|if
condition|(
name|log
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|ignoringCallLogFormat
argument_list|,
name|reset
condition|?
literal|"Reset"
else|:
literal|"Close"
argument_list|,
literal|"Socket is not connected"
argument_list|,
name|localSocketAddress
argument_list|,
name|remoteSocketAddress
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|socket
operator|.
name|isClosed
argument_list|()
condition|)
block|{
if|if
condition|(
name|log
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|ignoringCallLogFormat
argument_list|,
name|reset
condition|?
literal|"Reset"
else|:
literal|"Close"
argument_list|,
literal|"Socket is already closed"
argument_list|,
name|localSocketAddress
argument_list|,
name|remoteSocketAddress
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|this
operator|.
name|updateLastConnectionTerminatedTicks
argument_list|()
expr_stmt|;
specifier|final
name|String
name|ignoringExceptionStringFormat
init|=
literal|"Ignoring %s encountered calling %s on Socket: localAddress=%s remoteAddress=%s"
decl_stmt|;
if|if
condition|(
operator|!
name|socket
operator|.
name|isInputShutdown
argument_list|()
condition|)
block|{
if|if
condition|(
name|log
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Shutting down input on Socket: localAddress={} remoteAddress={}"
argument_list|,
name|localSocketAddress
argument_list|,
name|remoteSocketAddress
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|socket
operator|.
name|shutdownInput
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ioEx
parameter_list|)
block|{
if|if
condition|(
name|log
operator|!=
literal|null
operator|&&
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|String
name|logMessage
init|=
name|String
operator|.
name|format
argument_list|(
name|ignoringExceptionStringFormat
argument_list|,
name|ioEx
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|,
literal|"shutdownInput()"
argument_list|,
name|localSocketAddress
argument_list|,
name|remoteSocketAddress
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
name|logMessage
argument_list|,
name|ioEx
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|socket
operator|.
name|isOutputShutdown
argument_list|()
condition|)
block|{
if|if
condition|(
name|log
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Shutting down output on Socket: localAddress={} remoteAddress={}"
argument_list|,
name|localSocketAddress
argument_list|,
name|remoteSocketAddress
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|socket
operator|.
name|shutdownOutput
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioEx
parameter_list|)
block|{
if|if
condition|(
name|log
operator|!=
literal|null
operator|&&
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|String
name|logMessage
init|=
name|String
operator|.
name|format
argument_list|(
name|ignoringExceptionStringFormat
argument_list|,
name|ioEx
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|,
literal|"shutdownOutput()"
argument_list|,
name|localSocketAddress
argument_list|,
name|remoteSocketAddress
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
name|logMessage
argument_list|,
name|ioEx
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|reset
condition|)
block|{
specifier|final
name|boolean
name|on
init|=
literal|true
decl_stmt|;
specifier|final
name|int
name|linger
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|log
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Setting SO_LINGER to {} on Socket: localAddress={} remoteAddress={}"
argument_list|,
name|localSocketAddress
argument_list|,
name|remoteSocketAddress
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|socket
operator|.
name|setSoLinger
argument_list|(
name|on
argument_list|,
name|linger
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioEx
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|String
name|methodString
init|=
name|String
operator|.
name|format
argument_list|(
literal|"setSoLinger(%b, %d)"
argument_list|,
name|on
argument_list|,
name|linger
argument_list|)
decl_stmt|;
name|String
name|logMessage
init|=
name|String
operator|.
name|format
argument_list|(
name|ignoringExceptionStringFormat
argument_list|,
name|ioEx
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|methodString
argument_list|,
name|localSocketAddress
argument_list|,
name|remoteSocketAddress
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
name|logMessage
argument_list|,
name|ioEx
argument_list|)
expr_stmt|;
block|}
block|}
block|}
try|try
block|{
if|if
condition|(
name|log
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Resetting Socket: localAddress={} remoteAddress={}"
argument_list|,
name|localSocketAddress
argument_list|,
name|remoteSocketAddress
argument_list|)
expr_stmt|;
block|}
name|socket
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioEx
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|String
name|warningMessage
init|=
name|String
operator|.
name|format
argument_list|(
name|ignoringExceptionStringFormat
argument_list|,
name|ioEx
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|,
literal|"close()"
argument_list|,
name|localSocketAddress
argument_list|,
name|remoteSocketAddress
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
name|warningMessage
argument_list|,
name|ioEx
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

