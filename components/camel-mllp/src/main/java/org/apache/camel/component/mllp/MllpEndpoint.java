begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Represents a MLLP endpoint.  *<p/>  * NOTE: MLLP payloads are not logged unless the logging level is set to DEBUG or TRACE to avoid introducing PHI  * into the log files.  Logging of PHI can be globally disabled by setting the org.apache.camel.mllp.logPHI system  * property to false.  *<p/>  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"mllp"
argument_list|,
name|title
operator|=
literal|"mllp"
argument_list|,
name|syntax
operator|=
literal|"mllp:hostname:port"
argument_list|,
name|consumerClass
operator|=
name|MllpTcpServerConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"mllp"
argument_list|)
DECL|class|MllpEndpoint
specifier|public
class|class
name|MllpEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|START_OF_BLOCK
specifier|public
specifier|static
specifier|final
name|char
name|START_OF_BLOCK
init|=
literal|0x0b
decl_stmt|;
comment|// VT (vertical tab)        - decimal 11, octal 013
DECL|field|END_OF_BLOCK
specifier|public
specifier|static
specifier|final
name|char
name|END_OF_BLOCK
init|=
literal|0x1c
decl_stmt|;
comment|// FS (file separator)      - decimal 28, octal 034
DECL|field|END_OF_DATA
specifier|public
specifier|static
specifier|final
name|char
name|END_OF_DATA
init|=
literal|0x0d
decl_stmt|;
comment|// CR (carriage return)     - decimal 13, octal 015
DECL|field|END_OF_STREAM
specifier|public
specifier|static
specifier|final
name|int
name|END_OF_STREAM
init|=
operator|-
literal|1
decl_stmt|;
comment|//
DECL|field|SEGMENT_DELIMITER
specifier|public
specifier|static
specifier|final
name|char
name|SEGMENT_DELIMITER
init|=
literal|0x0d
decl_stmt|;
comment|// CR (carriage return)     - decimal 13, octal 015
DECL|field|MESSAGE_TERMINATOR
specifier|public
specifier|static
specifier|final
name|char
name|MESSAGE_TERMINATOR
init|=
literal|0x0a
decl_stmt|;
comment|// LF (line feed, new line) - decimal 10, octal 012
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
name|MllpEndpoint
operator|.
name|class
argument_list|)
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
literal|"true"
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
name|defaultValue
operator|=
literal|"5"
argument_list|)
DECL|field|backlog
name|int
name|backlog
init|=
literal|5
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"30000"
argument_list|)
DECL|field|bindTimeout
name|int
name|bindTimeout
init|=
literal|30000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"5000"
argument_list|)
DECL|field|bindRetryInterval
name|int
name|bindRetryInterval
init|=
literal|5000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"60000"
argument_list|)
DECL|field|acceptTimeout
name|int
name|acceptTimeout
init|=
literal|60000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"30000"
argument_list|)
DECL|field|connectTimeout
name|int
name|connectTimeout
init|=
literal|30000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"10000"
argument_list|)
DECL|field|receiveTimeout
name|int
name|receiveTimeout
init|=
literal|10000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|keepAlive
name|boolean
name|keepAlive
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|tcpNoDelay
name|boolean
name|tcpNoDelay
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|reuseAddress
name|boolean
name|reuseAddress
decl_stmt|;
annotation|@
name|UriParam
DECL|field|receiveBufferSize
name|Integer
name|receiveBufferSize
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sendBufferSize
name|Integer
name|sendBufferSize
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|autoAck
name|boolean
name|autoAck
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|charsetName
name|String
name|charsetName
decl_stmt|;
DECL|method|MllpEndpoint (String uri, MllpComponent component)
specifier|public
name|MllpEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|MllpComponent
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
block|}
annotation|@
name|Override
DECL|method|getExchangePattern ()
specifier|public
name|ExchangePattern
name|getExchangePattern
parameter_list|()
block|{
return|return
name|ExchangePattern
operator|.
name|InOut
return|;
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
DECL|method|createExchange (Exchange exchange)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Exchange
name|mllpExchange
init|=
name|super
operator|.
name|createExchange
argument_list|(
name|exchange
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
name|charsetName
operator|!=
literal|null
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
name|charsetName
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
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
name|LOG
operator|.
name|trace
argument_list|(
literal|"({}).createConsumer(processor)"
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
name|Override
DECL|method|isSynchronous ()
specifier|public
name|boolean
name|isSynchronous
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
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
DECL|method|getCharsetName ()
specifier|public
name|String
name|getCharsetName
parameter_list|()
block|{
return|return
name|charsetName
return|;
block|}
comment|/**      * Set the CamelCharsetName property on the exchange      *      * @param charsetName the charset      */
DECL|method|setCharsetName (String charsetName)
specifier|public
name|void
name|setCharsetName
parameter_list|(
name|String
name|charsetName
parameter_list|)
block|{
name|this
operator|.
name|charsetName
operator|=
name|charsetName
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
DECL|method|getBacklog ()
specifier|public
name|int
name|getBacklog
parameter_list|()
block|{
return|return
name|backlog
return|;
block|}
comment|/**      * The maximum queue length for incoming connection indications (a request to connect) is set to the backlog parameter. If a connection indication arrives when the queue is full, the connection      * is refused.      */
DECL|method|setBacklog (int backlog)
specifier|public
name|void
name|setBacklog
parameter_list|(
name|int
name|backlog
parameter_list|)
block|{
name|this
operator|.
name|backlog
operator|=
name|backlog
expr_stmt|;
block|}
DECL|method|getBindTimeout ()
specifier|public
name|int
name|getBindTimeout
parameter_list|()
block|{
return|return
name|bindTimeout
return|;
block|}
comment|/**      * TCP Server Only - The number of milliseconds to retry binding to a server port      */
DECL|method|setBindTimeout (int bindTimeout)
specifier|public
name|void
name|setBindTimeout
parameter_list|(
name|int
name|bindTimeout
parameter_list|)
block|{
name|this
operator|.
name|bindTimeout
operator|=
name|bindTimeout
expr_stmt|;
block|}
DECL|method|getBindRetryInterval ()
specifier|public
name|int
name|getBindRetryInterval
parameter_list|()
block|{
return|return
name|bindRetryInterval
return|;
block|}
comment|/**      * TCP Server Only - The number of milliseconds to wait between bind attempts      */
DECL|method|setBindRetryInterval (int bindRetryInterval)
specifier|public
name|void
name|setBindRetryInterval
parameter_list|(
name|int
name|bindRetryInterval
parameter_list|)
block|{
name|this
operator|.
name|bindRetryInterval
operator|=
name|bindRetryInterval
expr_stmt|;
block|}
DECL|method|getAcceptTimeout ()
specifier|public
name|int
name|getAcceptTimeout
parameter_list|()
block|{
return|return
name|acceptTimeout
return|;
block|}
comment|/**      * Timeout value while waiting for a TCP connection      *<p/>      * TCP Server Only      *      * @param acceptTimeout timeout in milliseconds      */
DECL|method|setAcceptTimeout (int acceptTimeout)
specifier|public
name|void
name|setAcceptTimeout
parameter_list|(
name|int
name|acceptTimeout
parameter_list|)
block|{
name|this
operator|.
name|acceptTimeout
operator|=
name|acceptTimeout
expr_stmt|;
block|}
DECL|method|getConnectTimeout ()
specifier|public
name|int
name|getConnectTimeout
parameter_list|()
block|{
return|return
name|connectTimeout
return|;
block|}
comment|/**      * Timeout value for establishing for a TCP connection      *<p/>      * TCP Client only      *      * @param connectTimeout timeout in milliseconds      */
DECL|method|setConnectTimeout (int connectTimeout)
specifier|public
name|void
name|setConnectTimeout
parameter_list|(
name|int
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
DECL|method|getReceiveTimeout ()
specifier|public
name|int
name|getReceiveTimeout
parameter_list|()
block|{
return|return
name|receiveTimeout
return|;
block|}
comment|/**      * The SO_TIMEOUT value used when waiting for the start of an MLLP frame      *      * @param receiveTimeout timeout in milliseconds      */
DECL|method|setReceiveTimeout (int receiveTimeout)
specifier|public
name|void
name|setReceiveTimeout
parameter_list|(
name|int
name|receiveTimeout
parameter_list|)
block|{
name|this
operator|.
name|receiveTimeout
operator|=
name|receiveTimeout
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
comment|/**      * Enable/disable the SO_KEEPALIVE socket option.      *      * @param keepAlive enable SO_KEEPALIVE when true; otherwise disable SO_KEEPALIVE      */
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
comment|/**      * Enable/disable the TCP_NODELAY socket option.      *      * @param tcpNoDelay enable TCP_NODELAY when true; otherwise disable TCP_NODELAY      */
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
comment|/**      * Enable/disable the SO_REUSEADDR socket option.      *      * @param reuseAddress enable SO_REUSEADDR when true; otherwise disable SO_REUSEADDR      */
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
DECL|method|getReceiveBufferSize ()
specifier|public
name|int
name|getReceiveBufferSize
parameter_list|()
block|{
return|return
name|receiveBufferSize
return|;
block|}
comment|/**      * Sets the SO_RCVBUF option to the specified value      *      * @param receiveBufferSize the SO_RCVBUF option value.  If null, the system default is used      */
DECL|method|setReceiveBufferSize (Integer receiveBufferSize)
specifier|public
name|void
name|setReceiveBufferSize
parameter_list|(
name|Integer
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
DECL|method|getSendBufferSize ()
specifier|public
name|int
name|getSendBufferSize
parameter_list|()
block|{
return|return
name|sendBufferSize
return|;
block|}
comment|/**      * Sets the SO_SNDBUF option to the specified value      *      * @param sendBufferSize the SO_SNDBUF option value.  If null, the system default is used      */
DECL|method|setSendBufferSize (Integer sendBufferSize)
specifier|public
name|void
name|setSendBufferSize
parameter_list|(
name|Integer
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
DECL|method|isAutoAck ()
specifier|public
name|boolean
name|isAutoAck
parameter_list|()
block|{
return|return
name|autoAck
return|;
block|}
comment|/**      * Enable/Disable the automatic generation of a MLLP Acknowledgement      *      * MLLP Consumers only      *      * @param autoAck enabled if true, otherwise disabled      */
DECL|method|setAutoAck (boolean autoAck)
specifier|public
name|void
name|setAutoAck
parameter_list|(
name|boolean
name|autoAck
parameter_list|)
block|{
name|this
operator|.
name|autoAck
operator|=
name|autoAck
expr_stmt|;
block|}
block|}
end_class

end_unit

