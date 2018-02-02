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
name|java
operator|.
name|util
operator|.
name|Objects
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
name|spi
operator|.
name|ExceptionHandler
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
comment|/**  * The MLLP Component configuration.  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|MllpConfiguration
specifier|public
class|class
name|MllpConfiguration
implements|implements
name|Cloneable
block|{
DECL|field|LOG
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MllpConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// URI Parameters overridden from DefaultEndpoint
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|bridgeErrorHandler
name|boolean
name|bridgeErrorHandler
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
literal|"InOut"
argument_list|)
DECL|field|exchangePattern
name|ExchangePattern
name|exchangePattern
init|=
name|ExchangePattern
operator|.
name|InOut
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
literal|"true"
argument_list|)
DECL|field|synchronous
name|boolean
name|synchronous
init|=
literal|true
decl_stmt|;
comment|// camel-mllp specific URI parameters
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced,consumer,tcp"
argument_list|,
name|defaultValue
operator|=
literal|"5"
argument_list|)
DECL|field|backlog
name|Integer
name|backlog
init|=
literal|5
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced,consumer,tcp,timeout"
argument_list|,
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
name|label
operator|=
literal|"advanced,consumer,tcp,timeout"
argument_list|,
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
name|label
operator|=
literal|"advanced,consumer,tcp"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|lenientBind
name|boolean
name|lenientBind
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced,consumer,tcp,timeout"
argument_list|,
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
name|label
operator|=
literal|"advanced,producer,tcp,timeout"
argument_list|,
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
name|label
operator|=
literal|"advanced,tcp,timeout"
argument_list|,
name|defaultValue
operator|=
literal|"15000"
argument_list|)
DECL|field|receiveTimeout
name|int
name|receiveTimeout
init|=
literal|15000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced,consumer,tcp"
argument_list|,
name|defaultValue
operator|=
literal|"5"
argument_list|)
DECL|field|maxConcurrentConsumers
name|int
name|maxConcurrentConsumers
init|=
literal|5
decl_stmt|;
annotation|@
name|Deprecated
comment|// use idleTimeout
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced,consumer,tcp,timeout"
argument_list|,
name|defaultValue
operator|=
literal|"null"
argument_list|)
DECL|field|maxReceiveTimeouts
name|Integer
name|maxReceiveTimeouts
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced,tcp,timeout"
argument_list|,
name|defaultValue
operator|=
literal|"null"
argument_list|)
DECL|field|idleTimeout
name|Integer
name|idleTimeout
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced,tcp,timeout"
argument_list|,
name|defaultValue
operator|=
literal|"5000"
argument_list|)
DECL|field|readTimeout
name|int
name|readTimeout
init|=
literal|5000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced,producer,tcp"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|keepAlive
name|Boolean
name|keepAlive
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced,producer,tcp"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|tcpNoDelay
name|Boolean
name|tcpNoDelay
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced,consumer,tcp"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|reuseAddress
name|Boolean
name|reuseAddress
init|=
literal|false
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced,tcp"
argument_list|,
name|defaultValue
operator|=
literal|"8192"
argument_list|)
DECL|field|receiveBufferSize
name|Integer
name|receiveBufferSize
init|=
literal|8192
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced,tcp"
argument_list|,
name|defaultValue
operator|=
literal|"8192"
argument_list|)
DECL|field|sendBufferSize
name|Integer
name|sendBufferSize
init|=
literal|8192
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
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|hl7Headers
name|boolean
name|hl7Headers
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
annotation|@
name|Deprecated
DECL|field|bufferWrites
name|boolean
name|bufferWrites
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|requireEndOfData
name|boolean
name|requireEndOfData
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
DECL|field|stringPayload
name|boolean
name|stringPayload
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
DECL|field|validatePayload
name|boolean
name|validatePayload
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"codec"
argument_list|)
DECL|field|charsetName
name|String
name|charsetName
decl_stmt|;
DECL|method|MllpConfiguration ()
specifier|public
name|MllpConfiguration
parameter_list|()
block|{     }
DECL|method|MllpConfiguration (MllpConfiguration source)
specifier|public
name|MllpConfiguration
parameter_list|(
name|MllpConfiguration
name|source
parameter_list|)
block|{
name|this
operator|.
name|copy
argument_list|(
name|source
argument_list|)
expr_stmt|;
block|}
DECL|method|copy (MllpConfiguration source, MllpConfiguration target)
specifier|public
specifier|static
name|void
name|copy
parameter_list|(
name|MllpConfiguration
name|source
parameter_list|,
name|MllpConfiguration
name|target
parameter_list|)
block|{
if|if
condition|(
name|source
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Values were not copied by MllpConfiguration.copy(MllpConfiguration source, MllpConfiguration target) - source argument is null"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|target
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Values were not copied by MllpConfiguration.copy(MllpConfiguration source, MllpConfiguration target) - target argument is null"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|target
operator|.
name|bridgeErrorHandler
operator|=
name|source
operator|.
name|bridgeErrorHandler
expr_stmt|;
name|target
operator|.
name|exchangePattern
operator|=
name|source
operator|.
name|exchangePattern
expr_stmt|;
name|target
operator|.
name|synchronous
operator|=
name|source
operator|.
name|synchronous
expr_stmt|;
name|target
operator|.
name|backlog
operator|=
name|source
operator|.
name|backlog
expr_stmt|;
name|target
operator|.
name|bindTimeout
operator|=
name|source
operator|.
name|bindTimeout
expr_stmt|;
name|target
operator|.
name|bindRetryInterval
operator|=
name|source
operator|.
name|bindRetryInterval
expr_stmt|;
name|target
operator|.
name|acceptTimeout
operator|=
name|source
operator|.
name|acceptTimeout
expr_stmt|;
name|target
operator|.
name|connectTimeout
operator|=
name|source
operator|.
name|connectTimeout
expr_stmt|;
name|target
operator|.
name|receiveTimeout
operator|=
name|source
operator|.
name|receiveTimeout
expr_stmt|;
name|target
operator|.
name|idleTimeout
operator|=
name|source
operator|.
name|idleTimeout
expr_stmt|;
name|target
operator|.
name|readTimeout
operator|=
name|source
operator|.
name|readTimeout
expr_stmt|;
name|target
operator|.
name|keepAlive
operator|=
name|source
operator|.
name|keepAlive
expr_stmt|;
name|target
operator|.
name|tcpNoDelay
operator|=
name|source
operator|.
name|tcpNoDelay
expr_stmt|;
name|target
operator|.
name|reuseAddress
operator|=
name|source
operator|.
name|reuseAddress
expr_stmt|;
name|target
operator|.
name|receiveBufferSize
operator|=
name|source
operator|.
name|receiveBufferSize
expr_stmt|;
name|target
operator|.
name|sendBufferSize
operator|=
name|source
operator|.
name|sendBufferSize
expr_stmt|;
name|target
operator|.
name|autoAck
operator|=
name|source
operator|.
name|autoAck
expr_stmt|;
name|target
operator|.
name|hl7Headers
operator|=
name|source
operator|.
name|hl7Headers
expr_stmt|;
name|target
operator|.
name|bufferWrites
operator|=
name|source
operator|.
name|bufferWrites
expr_stmt|;
name|target
operator|.
name|requireEndOfData
operator|=
name|source
operator|.
name|requireEndOfData
expr_stmt|;
name|target
operator|.
name|stringPayload
operator|=
name|source
operator|.
name|stringPayload
expr_stmt|;
name|target
operator|.
name|validatePayload
operator|=
name|source
operator|.
name|validatePayload
expr_stmt|;
name|target
operator|.
name|charsetName
operator|=
name|source
operator|.
name|charsetName
expr_stmt|;
block|}
block|}
DECL|method|copy ()
specifier|public
name|MllpConfiguration
name|copy
parameter_list|()
block|{
name|MllpConfiguration
name|target
init|=
operator|new
name|MllpConfiguration
argument_list|()
decl_stmt|;
name|MllpConfiguration
operator|.
name|copy
argument_list|(
name|this
argument_list|,
name|target
argument_list|)
expr_stmt|;
return|return
name|target
return|;
block|}
DECL|method|copy (MllpConfiguration source)
specifier|public
name|void
name|copy
parameter_list|(
name|MllpConfiguration
name|source
parameter_list|)
block|{
name|MllpConfiguration
operator|.
name|copy
argument_list|(
name|source
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|isBridgeErrorHandler ()
specifier|public
name|boolean
name|isBridgeErrorHandler
parameter_list|()
block|{
return|return
name|bridgeErrorHandler
return|;
block|}
comment|/**      * Allows for bridging the consumer to the Camel routing Error Handler, which mean any exceptions occurred while      * the consumer is trying to receive incoming messages, or the likes, will now be processed as a message and handled by the routing Error Handler.      *      * If disabled, the consumer will use the org.apache.camel.spi.ExceptionHandler to deal with exceptions by logging them at WARN or ERROR level and ignored.      *      * @param bridgeErrorHandler      */
DECL|method|setBridgeErrorHandler (boolean bridgeErrorHandler)
specifier|public
name|void
name|setBridgeErrorHandler
parameter_list|(
name|boolean
name|bridgeErrorHandler
parameter_list|)
block|{
name|this
operator|.
name|bridgeErrorHandler
operator|=
name|bridgeErrorHandler
expr_stmt|;
block|}
DECL|method|getExchangePattern ()
specifier|public
name|ExchangePattern
name|getExchangePattern
parameter_list|()
block|{
return|return
name|exchangePattern
return|;
block|}
comment|/**      * Sets the exchange pattern when the consumer creates an exchange.      *      * @param exchangePattern      */
DECL|method|setExchangePattern (ExchangePattern exchangePattern)
specifier|public
name|void
name|setExchangePattern
parameter_list|(
name|ExchangePattern
name|exchangePattern
parameter_list|)
block|{
name|this
operator|.
name|exchangePattern
operator|=
name|exchangePattern
expr_stmt|;
block|}
DECL|method|isSynchronous ()
specifier|public
name|boolean
name|isSynchronous
parameter_list|()
block|{
return|return
name|synchronous
return|;
block|}
comment|/**      * Sets whether synchronous processing should be strictly used (this component only supports synchronous operations).      *      * @param synchronous      */
DECL|method|setSynchronous (boolean synchronous)
specifier|public
name|void
name|setSynchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{     }
DECL|method|hasCharsetName ()
specifier|public
name|boolean
name|hasCharsetName
parameter_list|()
block|{
return|return
name|charsetName
operator|!=
literal|null
operator|&&
operator|!
name|charsetName
operator|.
name|isEmpty
argument_list|()
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
DECL|method|hasBacklog ()
specifier|public
name|boolean
name|hasBacklog
parameter_list|()
block|{
return|return
name|backlog
operator|!=
literal|null
operator|&&
name|backlog
operator|>
literal|0
return|;
block|}
DECL|method|getBacklog ()
specifier|public
name|Integer
name|getBacklog
parameter_list|()
block|{
return|return
name|backlog
return|;
block|}
comment|/**      * The maximum queue length for incoming connection indications (a request to connect) is set to the backlog parameter. If a connection indication arrives when the queue is full, the connection is      * refused.      */
DECL|method|setBacklog (Integer backlog)
specifier|public
name|void
name|setBacklog
parameter_list|(
name|Integer
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
DECL|method|isLenientBind ()
specifier|public
name|boolean
name|isLenientBind
parameter_list|()
block|{
return|return
name|lenientBind
return|;
block|}
comment|/**      * TCP Server Only - Allow the endpoint to start before the TCP ServerSocket is bound.      *      * In some environments, it may be desirable to allow the endpoint to start before the TCP ServerSocket      * is bound.      *      * @param lenientBind if true, the ServerSocket will be bound asynchronously; otherwise the ServerSocket will be bound synchronously.      */
DECL|method|setLenientBind (boolean lenientBind)
specifier|public
name|void
name|setLenientBind
parameter_list|(
name|boolean
name|lenientBind
parameter_list|)
block|{
name|this
operator|.
name|lenientBind
operator|=
name|lenientBind
expr_stmt|;
block|}
comment|/**      * Timeout (in milliseconds) while waiting for a TCP connection      *<p/>      * TCP Server Only      *      * @param acceptTimeout timeout in milliseconds      */
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
comment|/**      * Timeout (in milliseconds) for establishing for a TCP connection      *<p/>      * TCP Client only      *      * @param connectTimeout timeout in milliseconds      */
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
comment|/**      * The SO_TIMEOUT value (in milliseconds) used when waiting for the start of an MLLP frame      *      * @param receiveTimeout timeout in milliseconds      */
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
DECL|method|getMaxConcurrentConsumers ()
specifier|public
name|int
name|getMaxConcurrentConsumers
parameter_list|()
block|{
return|return
name|maxConcurrentConsumers
return|;
block|}
comment|/**      * The maximum number of concurrent MLLP Consumer connections that will be allowed.  If a new      * connection is received and the maximum is number are already established, the new connection will be reset immediately.      *      * @param maxConcurrentConsumers the maximum number of concurrent consumer connections allowed      */
DECL|method|setMaxConcurrentConsumers (int maxConcurrentConsumers)
specifier|public
name|void
name|setMaxConcurrentConsumers
parameter_list|(
name|int
name|maxConcurrentConsumers
parameter_list|)
block|{
name|this
operator|.
name|maxConcurrentConsumers
operator|=
name|maxConcurrentConsumers
expr_stmt|;
block|}
comment|/**      * Determine if the maxReceiveTimeouts URI parameter has been set      *      * @return true if the parameter has been set; false otherwise      *      * @deprecated Use the idleTimeout URI parameter      */
DECL|method|hasMaxReceiveTimeouts ()
specifier|public
name|boolean
name|hasMaxReceiveTimeouts
parameter_list|()
block|{
return|return
name|maxReceiveTimeouts
operator|!=
literal|null
return|;
block|}
comment|/**      * Retrieve the value of the maxReceiveTimeouts URI parameter.      *      * @return the maximum number of receive timeouts before the TCP Socket is reset      *      * @deprecated Use the idleTimeout URI parameter      */
DECL|method|getMaxReceiveTimeouts ()
specifier|public
name|Integer
name|getMaxReceiveTimeouts
parameter_list|()
block|{
return|return
name|maxReceiveTimeouts
return|;
block|}
comment|/**      * The maximum number of timeouts (specified by receiveTimeout) allowed before the TCP Connection will be reset.      *      * @param maxReceiveTimeouts maximum number of receiveTimeouts      *      * @deprecated Use the idleTimeout URI parameter.  For backward compibility, setting this parameter will result in an      * idle timeout of maxReceiveTimeouts * receiveTimeout.  If idleTimeout is also specified, this parameter will be ignored.      */
DECL|method|setMaxReceiveTimeouts (Integer maxReceiveTimeouts)
specifier|public
name|void
name|setMaxReceiveTimeouts
parameter_list|(
name|Integer
name|maxReceiveTimeouts
parameter_list|)
block|{
name|this
operator|.
name|maxReceiveTimeouts
operator|=
name|maxReceiveTimeouts
expr_stmt|;
block|}
DECL|method|hasIdleTimeout ()
specifier|public
name|boolean
name|hasIdleTimeout
parameter_list|()
block|{
return|return
name|idleTimeout
operator|!=
literal|null
operator|&&
name|idleTimeout
operator|>
literal|0
return|;
block|}
DECL|method|getIdleTimeout ()
specifier|public
name|Integer
name|getIdleTimeout
parameter_list|()
block|{
return|return
name|idleTimeout
return|;
block|}
comment|/**      * The approximate idle time allowed before the Client TCP Connection will be reset.      *      * A null value or a value less than or equal to zero will disable the idle timeout.      *      * @param idleTimeout timeout in milliseconds      */
DECL|method|setIdleTimeout (Integer idleTimeout)
specifier|public
name|void
name|setIdleTimeout
parameter_list|(
name|Integer
name|idleTimeout
parameter_list|)
block|{
name|this
operator|.
name|idleTimeout
operator|=
name|idleTimeout
expr_stmt|;
block|}
DECL|method|getReadTimeout ()
specifier|public
name|int
name|getReadTimeout
parameter_list|()
block|{
return|return
name|readTimeout
return|;
block|}
comment|/**      * The SO_TIMEOUT value (in milliseconds) used after the start of an MLLP frame has been received      *      * @param readTimeout timeout in milliseconds      */
DECL|method|setReadTimeout (int readTimeout)
specifier|public
name|void
name|setReadTimeout
parameter_list|(
name|int
name|readTimeout
parameter_list|)
block|{
name|this
operator|.
name|readTimeout
operator|=
name|readTimeout
expr_stmt|;
block|}
DECL|method|hasKeepAlive ()
specifier|public
name|boolean
name|hasKeepAlive
parameter_list|()
block|{
return|return
name|keepAlive
operator|!=
literal|null
return|;
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
comment|/**      * Enable/disable the SO_KEEPALIVE socket option.      *      * @param keepAlive enable SO_KEEPALIVE when true; disable SO_KEEPALIVE when false; use system default when null      */
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
DECL|method|hasTcpNoDelay ()
specifier|public
name|boolean
name|hasTcpNoDelay
parameter_list|()
block|{
return|return
name|tcpNoDelay
operator|!=
literal|null
return|;
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
comment|/**      * Enable/disable the TCP_NODELAY socket option.      *      * @param tcpNoDelay enable TCP_NODELAY when true; disable TCP_NODELAY when false; use system default when null      */
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
DECL|method|hasReuseAddress ()
specifier|public
name|boolean
name|hasReuseAddress
parameter_list|()
block|{
return|return
name|reuseAddress
operator|!=
literal|null
return|;
block|}
DECL|method|getReuseAddress ()
specifier|public
name|Boolean
name|getReuseAddress
parameter_list|()
block|{
return|return
name|reuseAddress
return|;
block|}
comment|/**      * Enable/disable the SO_REUSEADDR socket option.      *      * @param reuseAddress enable SO_REUSEADDR when true; disable SO_REUSEADDR when false; use system default when null      */
DECL|method|setReuseAddress (Boolean reuseAddress)
specifier|public
name|void
name|setReuseAddress
parameter_list|(
name|Boolean
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
DECL|method|hasReceiveBufferSize ()
specifier|public
name|boolean
name|hasReceiveBufferSize
parameter_list|()
block|{
return|return
name|receiveBufferSize
operator|!=
literal|null
operator|&&
name|receiveBufferSize
operator|>
literal|0
return|;
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
comment|/**      * Sets the SO_RCVBUF option to the specified value (in bytes)      *      * @param receiveBufferSize the SO_RCVBUF option value.  If null, the system default is used      */
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
DECL|method|hasSendBufferSize ()
specifier|public
name|boolean
name|hasSendBufferSize
parameter_list|()
block|{
return|return
name|sendBufferSize
operator|!=
literal|null
operator|&&
name|sendBufferSize
operator|>
literal|0
return|;
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
comment|/**      * Sets the SO_SNDBUF option to the specified value (in bytes)      *      * @param sendBufferSize the SO_SNDBUF option value.  If null, the system default is used      */
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
DECL|method|isHl7Headers ()
specifier|public
name|boolean
name|isHl7Headers
parameter_list|()
block|{
return|return
name|hl7Headers
return|;
block|}
DECL|method|getHl7Headers ()
specifier|public
name|boolean
name|getHl7Headers
parameter_list|()
block|{
return|return
name|isHl7Headers
argument_list|()
return|;
block|}
comment|/**      * Enable/Disable the automatic generation of message headers from the HL7 Message      *      * MLLP Consumers only      *      * @param hl7Headers enabled if true, otherwise disabled      */
DECL|method|setHl7Headers (boolean hl7Headers)
specifier|public
name|void
name|setHl7Headers
parameter_list|(
name|boolean
name|hl7Headers
parameter_list|)
block|{
name|this
operator|.
name|hl7Headers
operator|=
name|hl7Headers
expr_stmt|;
block|}
DECL|method|isRequireEndOfData ()
specifier|public
name|boolean
name|isRequireEndOfData
parameter_list|()
block|{
return|return
name|requireEndOfData
return|;
block|}
comment|/**      * Enable/Disable strict compliance to the MLLP standard.      *      * The MLLP standard specifies [START_OF_BLOCK]hl7 payload[END_OF_BLOCK][END_OF_DATA], however, some systems do not send      * the final END_OF_DATA byte.  This setting controls whether or not the final END_OF_DATA byte is required or optional.      *      * @param requireEndOfData the trailing END_OF_DATA byte is required if true; optional otherwise      */
DECL|method|setRequireEndOfData (boolean requireEndOfData)
specifier|public
name|void
name|setRequireEndOfData
parameter_list|(
name|boolean
name|requireEndOfData
parameter_list|)
block|{
name|this
operator|.
name|requireEndOfData
operator|=
name|requireEndOfData
expr_stmt|;
block|}
DECL|method|isStringPayload ()
specifier|public
name|boolean
name|isStringPayload
parameter_list|()
block|{
return|return
name|stringPayload
return|;
block|}
comment|/**      * Enable/Disable converting the payload to a String.      *      * If enabled, HL7 Payloads received from external systems will be validated converted to a String.      *      * If the charsetName property is set, that character set will be used for the conversion.  If the charsetName property is      * not set, the value of MSH-18 will be used to determine th appropriate character set.  If MSH-18 is not set, then      * the default ASCII character set will be use.      *      * @param stringPayload enabled if true, otherwise disabled      */
DECL|method|setStringPayload (boolean stringPayload)
specifier|public
name|void
name|setStringPayload
parameter_list|(
name|boolean
name|stringPayload
parameter_list|)
block|{
name|this
operator|.
name|stringPayload
operator|=
name|stringPayload
expr_stmt|;
block|}
DECL|method|isValidatePayload ()
specifier|public
name|boolean
name|isValidatePayload
parameter_list|()
block|{
return|return
name|validatePayload
return|;
block|}
comment|/**      * Enable/Disable the validation of HL7 Payloads      *      * If enabled, HL7 Payloads received from external systems will be validated (see Hl7Util.generateInvalidPayloadExceptionMessage      * for details on the validation). If and invalid payload is detected, a MllpInvalidMessageException (for consumers) or      * a MllpInvalidAcknowledgementException will be thrown.      *      * @param validatePayload enabled if true, otherwise disabled      */
DECL|method|setValidatePayload (boolean validatePayload)
specifier|public
name|void
name|setValidatePayload
parameter_list|(
name|boolean
name|validatePayload
parameter_list|)
block|{
name|this
operator|.
name|validatePayload
operator|=
name|validatePayload
expr_stmt|;
block|}
DECL|method|isBufferWrites ()
specifier|public
name|boolean
name|isBufferWrites
parameter_list|()
block|{
return|return
name|bufferWrites
return|;
block|}
comment|/**      * Enable/Disable the buffering of HL7 payloads before writing to the socket.      *      * @deprecated the parameter will be ignored      *      * @param bufferWrites enabled if true, otherwise disabled      */
DECL|method|setBufferWrites (boolean bufferWrites)
specifier|public
name|void
name|setBufferWrites
parameter_list|(
name|boolean
name|bufferWrites
parameter_list|)
block|{
name|this
operator|.
name|bufferWrites
operator|=
name|bufferWrites
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hash
argument_list|(
name|bridgeErrorHandler
argument_list|,
name|exchangePattern
argument_list|,
name|synchronous
argument_list|,
name|backlog
argument_list|,
name|bindTimeout
argument_list|,
name|bindRetryInterval
argument_list|,
name|acceptTimeout
argument_list|,
name|connectTimeout
argument_list|,
name|receiveTimeout
argument_list|,
name|maxConcurrentConsumers
argument_list|,
name|maxReceiveTimeouts
argument_list|,
name|idleTimeout
argument_list|,
name|readTimeout
argument_list|,
name|keepAlive
argument_list|,
name|tcpNoDelay
argument_list|,
name|reuseAddress
argument_list|,
name|receiveBufferSize
argument_list|,
name|sendBufferSize
argument_list|,
name|autoAck
argument_list|,
name|hl7Headers
argument_list|,
name|bufferWrites
argument_list|,
name|requireEndOfData
argument_list|,
name|stringPayload
argument_list|,
name|validatePayload
argument_list|,
name|charsetName
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|MllpConfiguration
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|MllpConfiguration
name|rhs
init|=
operator|(
name|MllpConfiguration
operator|)
name|o
decl_stmt|;
return|return
name|bridgeErrorHandler
operator|==
name|rhs
operator|.
name|bridgeErrorHandler
operator|&&
name|exchangePattern
operator|==
name|rhs
operator|.
name|exchangePattern
operator|&&
name|synchronous
operator|==
name|rhs
operator|.
name|synchronous
operator|&&
name|bindTimeout
operator|==
name|rhs
operator|.
name|bindTimeout
operator|&&
name|bindRetryInterval
operator|==
name|rhs
operator|.
name|bindRetryInterval
operator|&&
name|acceptTimeout
operator|==
name|rhs
operator|.
name|acceptTimeout
operator|&&
name|connectTimeout
operator|==
name|rhs
operator|.
name|connectTimeout
operator|&&
name|receiveTimeout
operator|==
name|rhs
operator|.
name|receiveTimeout
operator|&&
name|readTimeout
operator|==
name|rhs
operator|.
name|readTimeout
operator|&&
name|autoAck
operator|==
name|rhs
operator|.
name|autoAck
operator|&&
name|hl7Headers
operator|==
name|rhs
operator|.
name|hl7Headers
operator|&&
name|bufferWrites
operator|==
name|rhs
operator|.
name|bufferWrites
operator|&&
name|requireEndOfData
operator|==
name|rhs
operator|.
name|requireEndOfData
operator|&&
name|stringPayload
operator|==
name|rhs
operator|.
name|stringPayload
operator|&&
name|validatePayload
operator|==
name|rhs
operator|.
name|validatePayload
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|backlog
argument_list|,
name|rhs
operator|.
name|backlog
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|maxConcurrentConsumers
argument_list|,
name|rhs
operator|.
name|maxConcurrentConsumers
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|maxReceiveTimeouts
argument_list|,
name|rhs
operator|.
name|maxReceiveTimeouts
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|idleTimeout
argument_list|,
name|rhs
operator|.
name|idleTimeout
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|keepAlive
argument_list|,
name|rhs
operator|.
name|keepAlive
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|tcpNoDelay
argument_list|,
name|rhs
operator|.
name|tcpNoDelay
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|reuseAddress
argument_list|,
name|rhs
operator|.
name|reuseAddress
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|receiveBufferSize
argument_list|,
name|rhs
operator|.
name|receiveBufferSize
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|sendBufferSize
argument_list|,
name|rhs
operator|.
name|sendBufferSize
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|charsetName
argument_list|,
name|rhs
operator|.
name|charsetName
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"MllpConfiguration{"
operator|+
literal|"bridgeErrorHandler="
operator|+
name|bridgeErrorHandler
operator|+
literal|", exchangePattern="
operator|+
name|exchangePattern
operator|+
literal|", synchronous="
operator|+
name|synchronous
operator|+
literal|", backlog="
operator|+
name|backlog
operator|+
literal|", bindTimeout="
operator|+
name|bindTimeout
operator|+
literal|", bindRetryInterval="
operator|+
name|bindRetryInterval
operator|+
literal|", acceptTimeout="
operator|+
name|acceptTimeout
operator|+
literal|", connectTimeout="
operator|+
name|connectTimeout
operator|+
literal|", receiveTimeout="
operator|+
name|receiveTimeout
operator|+
literal|", maxConcurrentConsumers="
operator|+
name|maxConcurrentConsumers
operator|+
literal|", maxReceiveTimeouts="
operator|+
name|maxReceiveTimeouts
operator|+
literal|", idleTimeout="
operator|+
name|idleTimeout
operator|+
literal|", readTimeout="
operator|+
name|readTimeout
operator|+
literal|", keepAlive="
operator|+
name|keepAlive
operator|+
literal|", tcpNoDelay="
operator|+
name|tcpNoDelay
operator|+
literal|", reuseAddress="
operator|+
name|reuseAddress
operator|+
literal|", receiveBufferSize="
operator|+
name|receiveBufferSize
operator|+
literal|", sendBufferSize="
operator|+
name|sendBufferSize
operator|+
literal|", autoAck="
operator|+
name|autoAck
operator|+
literal|", hl7Headers="
operator|+
name|hl7Headers
operator|+
literal|", bufferWrites="
operator|+
name|bufferWrites
operator|+
literal|", requireEndOfData="
operator|+
name|requireEndOfData
operator|+
literal|", stringPayload="
operator|+
name|stringPayload
operator|+
literal|", validatePayload="
operator|+
name|validatePayload
operator|+
literal|", charsetName='"
operator|+
name|charsetName
operator|+
literal|'\''
operator|+
literal|'}'
return|;
block|}
block|}
end_class

end_unit

