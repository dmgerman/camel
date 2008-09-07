begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetSocketAddress
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
name|CharacterCodingException
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
name|nio
operator|.
name|charset
operator|.
name|CharsetEncoder
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
name|CamelContext
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
name|Endpoint
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
name|impl
operator|.
name|DefaultComponent
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
name|apache
operator|.
name|mina
operator|.
name|common
operator|.
name|ByteBuffer
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
name|common
operator|.
name|IoAcceptor
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
name|common
operator|.
name|IoConnector
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
name|common
operator|.
name|IoServiceConfig
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
name|common
operator|.
name|IoSession
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
name|LoggingFilter
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
name|apache
operator|.
name|mina
operator|.
name|filter
operator|.
name|codec
operator|.
name|ProtocolCodecFilter
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
name|ProtocolDecoder
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
name|ProtocolDecoderOutput
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
name|ProtocolEncoder
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
name|ProtocolEncoderOutput
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
name|serialization
operator|.
name|ObjectSerializationCodecFactory
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
name|textline
operator|.
name|TextLineCodecFactory
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
name|transport
operator|.
name|socket
operator|.
name|nio
operator|.
name|DatagramAcceptor
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
name|transport
operator|.
name|socket
operator|.
name|nio
operator|.
name|DatagramAcceptorConfig
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
name|transport
operator|.
name|socket
operator|.
name|nio
operator|.
name|DatagramConnector
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
name|transport
operator|.
name|socket
operator|.
name|nio
operator|.
name|DatagramConnectorConfig
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
name|transport
operator|.
name|socket
operator|.
name|nio
operator|.
name|SocketAcceptor
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
name|transport
operator|.
name|socket
operator|.
name|nio
operator|.
name|SocketAcceptorConfig
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
name|transport
operator|.
name|socket
operator|.
name|nio
operator|.
name|SocketConnector
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
name|transport
operator|.
name|socket
operator|.
name|nio
operator|.
name|SocketConnectorConfig
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
name|transport
operator|.
name|vmpipe
operator|.
name|VmPipeAcceptor
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
name|transport
operator|.
name|vmpipe
operator|.
name|VmPipeAddress
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
name|transport
operator|.
name|vmpipe
operator|.
name|VmPipeConnector
import|;
end_import

begin_comment
comment|/**  * Component for Apache MINA.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|MinaComponent
specifier|public
class|class
name|MinaComponent
extends|extends
name|DefaultComponent
argument_list|<
name|MinaExchange
argument_list|>
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
name|MinaComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|DEFAULT_CONNECT_TIMEOUT
specifier|private
specifier|static
specifier|final
name|long
name|DEFAULT_CONNECT_TIMEOUT
init|=
literal|30000
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
DECL|field|codec
specifier|private
name|String
name|codec
decl_stmt|;
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
DECL|field|timeout
specifier|private
name|long
name|timeout
decl_stmt|;
DECL|field|lazySessionCreation
specifier|private
name|boolean
name|lazySessionCreation
decl_stmt|;
DECL|field|transferExchange
specifier|private
name|boolean
name|transferExchange
decl_stmt|;
DECL|field|minaLogger
specifier|private
name|boolean
name|minaLogger
decl_stmt|;
comment|// encoder used for datagram
DECL|field|encoder
specifier|private
name|CharsetEncoder
name|encoder
decl_stmt|;
DECL|method|MinaComponent ()
specifier|public
name|MinaComponent
parameter_list|()
block|{     }
DECL|method|MinaComponent (CamelContext context)
specifier|public
name|MinaComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map parameters)
specifier|protected
name|Endpoint
argument_list|<
name|MinaExchange
argument_list|>
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
name|parameters
parameter_list|)
throws|throws
name|Exception
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
literal|"Creating MinaEndpoint from uri: "
operator|+
name|uri
argument_list|)
expr_stmt|;
block|}
name|setProperties
argument_list|(
name|this
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|URI
name|u
init|=
operator|new
name|URI
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
name|String
name|protocol
init|=
name|u
operator|.
name|getScheme
argument_list|()
decl_stmt|;
comment|// if mistyped uri then protocol can be null
if|if
condition|(
name|protocol
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|protocol
operator|.
name|equals
argument_list|(
literal|"tcp"
argument_list|)
condition|)
block|{
return|return
name|createSocketEndpoint
argument_list|(
name|uri
argument_list|,
name|u
argument_list|,
name|parameters
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|protocol
operator|.
name|equals
argument_list|(
literal|"udp"
argument_list|)
operator|||
name|protocol
operator|.
name|equals
argument_list|(
literal|"mcast"
argument_list|)
operator|||
name|protocol
operator|.
name|equals
argument_list|(
literal|"multicast"
argument_list|)
condition|)
block|{
return|return
name|createDatagramEndpoint
argument_list|(
name|uri
argument_list|,
name|u
argument_list|,
name|parameters
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|protocol
operator|.
name|equals
argument_list|(
literal|"vm"
argument_list|)
condition|)
block|{
return|return
name|createVmEndpoint
argument_list|(
name|uri
argument_list|,
name|u
argument_list|)
return|;
block|}
block|}
comment|// protocol not resolved so error
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unrecognised MINA protocol: "
operator|+
name|protocol
operator|+
literal|" for uri: "
operator|+
name|uri
argument_list|)
throw|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|createVmEndpoint (String uri, URI connectUri)
specifier|protected
name|MinaEndpoint
name|createVmEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|URI
name|connectUri
parameter_list|)
block|{
name|IoAcceptor
name|acceptor
init|=
operator|new
name|VmPipeAcceptor
argument_list|()
decl_stmt|;
name|SocketAddress
name|address
init|=
operator|new
name|VmPipeAddress
argument_list|(
name|connectUri
operator|.
name|getPort
argument_list|()
argument_list|)
decl_stmt|;
name|IoConnector
name|connector
init|=
operator|new
name|VmPipeConnector
argument_list|()
decl_stmt|;
return|return
operator|new
name|MinaEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|address
argument_list|,
name|acceptor
argument_list|,
literal|null
argument_list|,
name|connector
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|createSocketEndpoint (String uri, URI connectUri, Map parameters)
specifier|protected
name|MinaEndpoint
name|createSocketEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|URI
name|connectUri
parameter_list|,
name|Map
name|parameters
parameter_list|)
block|{
name|IoAcceptor
name|acceptor
init|=
operator|new
name|SocketAcceptor
argument_list|()
decl_stmt|;
name|SocketAddress
name|address
init|=
operator|new
name|InetSocketAddress
argument_list|(
name|connectUri
operator|.
name|getHost
argument_list|()
argument_list|,
name|connectUri
operator|.
name|getPort
argument_list|()
argument_list|)
decl_stmt|;
name|IoConnector
name|connector
init|=
operator|new
name|SocketConnector
argument_list|()
decl_stmt|;
comment|// connector config
name|SocketConnectorConfig
name|connectorConfig
init|=
operator|new
name|SocketConnectorConfig
argument_list|()
decl_stmt|;
name|configureSocketCodecFactory
argument_list|(
literal|"MinaProducer"
argument_list|,
name|connectorConfig
argument_list|,
name|textline
argument_list|,
name|encoding
argument_list|,
name|codec
argument_list|)
expr_stmt|;
if|if
condition|(
name|minaLogger
condition|)
block|{
name|connectorConfig
operator|.
name|getFilterChain
argument_list|()
operator|.
name|addLast
argument_list|(
literal|"logger"
argument_list|,
operator|new
name|LoggingFilter
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// set connect timeout to mina in seconds
name|long
name|connectTimeout
init|=
name|timeout
operator|>
literal|0
condition|?
name|timeout
else|:
name|DEFAULT_CONNECT_TIMEOUT
decl_stmt|;
name|connectorConfig
operator|.
name|setConnectTimeout
argument_list|(
call|(
name|int
call|)
argument_list|(
name|connectTimeout
operator|/
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
comment|// acceptor connectorConfig
name|SocketAcceptorConfig
name|acceptorConfig
init|=
operator|new
name|SocketAcceptorConfig
argument_list|()
decl_stmt|;
name|configureSocketCodecFactory
argument_list|(
literal|"MinaConsumer"
argument_list|,
name|acceptorConfig
argument_list|,
name|textline
argument_list|,
name|encoding
argument_list|,
name|codec
argument_list|)
expr_stmt|;
name|acceptorConfig
operator|.
name|setReuseAddress
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|acceptorConfig
operator|.
name|setDisconnectOnUnbind
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|minaLogger
condition|)
block|{
name|acceptorConfig
operator|.
name|getFilterChain
argument_list|()
operator|.
name|addLast
argument_list|(
literal|"logger"
argument_list|,
operator|new
name|LoggingFilter
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|MinaEndpoint
name|endpoint
init|=
operator|new
name|MinaEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|address
argument_list|,
name|acceptor
argument_list|,
name|acceptorConfig
argument_list|,
name|connector
argument_list|,
name|connectorConfig
argument_list|,
name|lazySessionCreation
argument_list|,
name|timeout
argument_list|,
name|transferExchange
argument_list|,
name|sync
argument_list|)
decl_stmt|;
if|if
condition|(
name|encoding
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setCharsetName
argument_list|(
name|getEncodingParameter
argument_list|(
literal|"MinaProducer"
argument_list|,
name|encoding
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// set sync or async mode after endpoint is created
if|if
condition|(
name|sync
condition|)
block|{
name|endpoint
operator|.
name|setExchangePattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|endpoint
operator|.
name|setExchangePattern
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoint
return|;
block|}
DECL|method|configureSocketCodecFactory (String type, IoServiceConfig config, boolean textline, String encoding, String codec)
specifier|protected
name|void
name|configureSocketCodecFactory
parameter_list|(
name|String
name|type
parameter_list|,
name|IoServiceConfig
name|config
parameter_list|,
name|boolean
name|textline
parameter_list|,
name|String
name|encoding
parameter_list|,
name|String
name|codec
parameter_list|)
block|{
name|ProtocolCodecFactory
name|codecFactory
init|=
name|getCodecFactory
argument_list|(
name|type
argument_list|,
name|codec
argument_list|)
decl_stmt|;
if|if
condition|(
name|codecFactory
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|textline
condition|)
block|{
name|Charset
name|charset
init|=
name|getEncodingParameter
argument_list|(
name|type
argument_list|,
name|encoding
argument_list|)
decl_stmt|;
name|codecFactory
operator|=
operator|new
name|TextLineCodecFactory
argument_list|(
name|charset
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
name|type
operator|+
literal|": Using TextLineCodecFactory: "
operator|+
name|codecFactory
operator|+
literal|" using encoding: "
operator|+
name|encoding
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|codecFactory
operator|=
operator|new
name|ObjectSerializationCodecFactory
argument_list|()
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
name|type
operator|+
literal|": Using ObjectSerializationCodecFactory: "
operator|+
name|codecFactory
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|addCodecFactory
argument_list|(
name|config
argument_list|,
name|codecFactory
argument_list|)
expr_stmt|;
block|}
DECL|method|createDatagramEndpoint (String uri, URI connectUri, Map parameters)
specifier|protected
name|MinaEndpoint
name|createDatagramEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|URI
name|connectUri
parameter_list|,
name|Map
name|parameters
parameter_list|)
block|{
name|IoAcceptor
name|acceptor
init|=
operator|new
name|DatagramAcceptor
argument_list|()
decl_stmt|;
name|SocketAddress
name|address
init|=
operator|new
name|InetSocketAddress
argument_list|(
name|connectUri
operator|.
name|getHost
argument_list|()
argument_list|,
name|connectUri
operator|.
name|getPort
argument_list|()
argument_list|)
decl_stmt|;
name|IoConnector
name|connector
init|=
operator|new
name|DatagramConnector
argument_list|()
decl_stmt|;
if|if
condition|(
name|transferExchange
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"transferExchange=true is not supported for datagram protocol"
argument_list|)
throw|;
block|}
name|DatagramConnectorConfig
name|connectorConfig
init|=
operator|new
name|DatagramConnectorConfig
argument_list|()
decl_stmt|;
name|configureDataGramCodecFactory
argument_list|(
literal|"MinaProducer"
argument_list|,
name|connectorConfig
argument_list|,
name|encoding
argument_list|,
name|codec
argument_list|)
expr_stmt|;
if|if
condition|(
name|minaLogger
condition|)
block|{
name|connectorConfig
operator|.
name|getFilterChain
argument_list|()
operator|.
name|addLast
argument_list|(
literal|"logger"
argument_list|,
operator|new
name|LoggingFilter
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// set connect timeout to mina in seconds
name|long
name|connectTimeout
init|=
name|timeout
operator|>
literal|0
condition|?
name|timeout
else|:
name|DEFAULT_CONNECT_TIMEOUT
decl_stmt|;
name|connectorConfig
operator|.
name|setConnectTimeout
argument_list|(
call|(
name|int
call|)
argument_list|(
name|connectTimeout
operator|/
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|DatagramAcceptorConfig
name|acceptorConfig
init|=
operator|new
name|DatagramAcceptorConfig
argument_list|()
decl_stmt|;
name|configureDataGramCodecFactory
argument_list|(
literal|"MinaConsumer"
argument_list|,
name|acceptorConfig
argument_list|,
name|encoding
argument_list|,
name|codec
argument_list|)
expr_stmt|;
name|acceptorConfig
operator|.
name|setDisconnectOnUnbind
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// reuse address is default true for datagram
if|if
condition|(
name|minaLogger
condition|)
block|{
name|acceptorConfig
operator|.
name|getFilterChain
argument_list|()
operator|.
name|addLast
argument_list|(
literal|"logger"
argument_list|,
operator|new
name|LoggingFilter
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|MinaEndpoint
name|endpoint
init|=
operator|new
name|MinaEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|address
argument_list|,
name|acceptor
argument_list|,
name|acceptorConfig
argument_list|,
name|connector
argument_list|,
name|connectorConfig
argument_list|,
name|lazySessionCreation
argument_list|,
name|timeout
argument_list|,
name|transferExchange
argument_list|,
name|sync
argument_list|)
decl_stmt|;
if|if
condition|(
name|encoding
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setCharsetName
argument_list|(
name|getEncodingParameter
argument_list|(
literal|"MinaProducer"
argument_list|,
name|encoding
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// set sync or async mode after endpoint is created
if|if
condition|(
name|sync
condition|)
block|{
name|endpoint
operator|.
name|setExchangePattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|endpoint
operator|.
name|setExchangePattern
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoint
return|;
block|}
DECL|method|getEncodingParameter (String type, String encoding)
specifier|private
name|Charset
name|getEncodingParameter
parameter_list|(
name|String
name|type
parameter_list|,
name|String
name|encoding
parameter_list|)
block|{
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
block|{
name|encoding
operator|=
name|Charset
operator|.
name|defaultCharset
argument_list|()
operator|.
name|name
argument_list|()
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
name|type
operator|+
literal|": No encoding parameter using default charset: "
operator|+
name|encoding
argument_list|)
expr_stmt|;
block|}
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
return|;
block|}
comment|/**      * For datagrams the entire message is available as a single ByteBuffer so lets just pass those around by default      * and try converting whatever they payload is into ByteBuffers unless some custom converter is specified      */
DECL|method|configureDataGramCodecFactory (String type, IoServiceConfig config, String encoding, String codec)
specifier|protected
name|void
name|configureDataGramCodecFactory
parameter_list|(
name|String
name|type
parameter_list|,
name|IoServiceConfig
name|config
parameter_list|,
name|String
name|encoding
parameter_list|,
name|String
name|codec
parameter_list|)
block|{
name|ProtocolCodecFactory
name|codecFactory
init|=
name|getCodecFactory
argument_list|(
name|type
argument_list|,
name|codec
argument_list|)
decl_stmt|;
if|if
condition|(
name|codecFactory
operator|==
literal|null
condition|)
block|{
name|codecFactory
operator|=
operator|new
name|ProtocolCodecFactory
argument_list|()
block|{
specifier|public
name|ProtocolEncoder
name|getEncoder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|ProtocolEncoder
argument_list|()
block|{
specifier|public
name|void
name|encode
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|Object
name|message
parameter_list|,
name|ProtocolEncoderOutput
name|out
parameter_list|)
throws|throws
name|Exception
block|{
name|ByteBuffer
name|buf
init|=
name|toByteBuffer
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|buf
operator|.
name|flip
argument_list|()
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|buf
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|dispose
parameter_list|(
name|IoSession
name|session
parameter_list|)
throws|throws
name|Exception
block|{
comment|// do nothing
block|}
block|}
return|;
block|}
specifier|public
name|ProtocolDecoder
name|getDecoder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|ProtocolDecoder
argument_list|()
block|{
specifier|public
name|void
name|decode
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|ByteBuffer
name|in
parameter_list|,
name|ProtocolDecoderOutput
name|out
parameter_list|)
throws|throws
name|Exception
block|{
comment|// must acquire the bytebuffer since we just pass it below instead of creating a new one (CAMEL-257)
name|in
operator|.
name|acquire
argument_list|()
expr_stmt|;
comment|// lets just pass the ByteBuffer in
name|out
operator|.
name|write
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|finishDecode
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|ProtocolDecoderOutput
name|out
parameter_list|)
throws|throws
name|Exception
block|{
comment|// do nothing
block|}
specifier|public
name|void
name|dispose
parameter_list|(
name|IoSession
name|session
parameter_list|)
throws|throws
name|Exception
block|{
comment|// do nothing
block|}
block|}
return|;
block|}
block|}
expr_stmt|;
comment|// set the encoder used for this datagram codec factory
name|Charset
name|charset
init|=
name|getEncodingParameter
argument_list|(
name|type
argument_list|,
name|encoding
argument_list|)
decl_stmt|;
name|encoder
operator|=
name|charset
operator|.
name|newEncoder
argument_list|()
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
name|type
operator|+
literal|": Using CodecFactory: "
operator|+
name|codecFactory
operator|+
literal|" using encoding: "
operator|+
name|encoding
argument_list|)
expr_stmt|;
block|}
block|}
name|addCodecFactory
argument_list|(
name|config
argument_list|,
name|codecFactory
argument_list|)
expr_stmt|;
block|}
DECL|method|toByteBuffer (Object message)
specifier|protected
name|ByteBuffer
name|toByteBuffer
parameter_list|(
name|Object
name|message
parameter_list|)
throws|throws
name|CharacterCodingException
block|{
name|ByteBuffer
name|answer
init|=
name|convertTo
argument_list|(
name|ByteBuffer
operator|.
name|class
argument_list|,
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|String
name|value
init|=
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|answer
operator|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
name|value
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|setAutoExpand
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|answer
operator|.
name|putString
argument_list|(
name|value
argument_list|,
name|encoder
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|getCodecFactory (String type, String codec)
specifier|protected
name|ProtocolCodecFactory
name|getCodecFactory
parameter_list|(
name|String
name|type
parameter_list|,
name|String
name|codec
parameter_list|)
block|{
name|ProtocolCodecFactory
name|codecFactory
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|codec
operator|!=
literal|null
condition|)
block|{
name|codecFactory
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|codec
argument_list|,
name|ProtocolCodecFactory
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|codecFactory
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Codec "
operator|+
name|codec
operator|+
literal|" not found in registry."
argument_list|)
throw|;
block|}
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
name|type
operator|+
literal|": Using custom CodecFactory: "
operator|+
name|codecFactory
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|codecFactory
return|;
block|}
DECL|method|addCodecFactory (IoServiceConfig config, ProtocolCodecFactory codecFactory)
specifier|protected
name|void
name|addCodecFactory
parameter_list|(
name|IoServiceConfig
name|config
parameter_list|,
name|ProtocolCodecFactory
name|codecFactory
parameter_list|)
block|{
name|config
operator|.
name|getFilterChain
argument_list|()
operator|.
name|addLast
argument_list|(
literal|"codec"
argument_list|,
operator|new
name|ProtocolCodecFilter
argument_list|(
name|codecFactory
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
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
DECL|method|getCodec ()
specifier|public
name|String
name|getCodec
parameter_list|()
block|{
return|return
name|codec
return|;
block|}
DECL|method|setCodec (String codec)
specifier|public
name|void
name|setCodec
parameter_list|(
name|String
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
block|}
end_class

end_unit

