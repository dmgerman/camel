begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4
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
name|EventLoopGroup
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
name|ssl
operator|.
name|SslHandler
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

begin_class
annotation|@
name|UriParams
DECL|class|NettyServerBootstrapConfiguration
specifier|public
class|class
name|NettyServerBootstrapConfiguration
implements|implements
name|Cloneable
block|{
DECL|field|DEFAULT_ENABLED_PROTOCOLS
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_ENABLED_PROTOCOLS
init|=
literal|"TLSv1,TLSv1.1,TLSv1.2"
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
DECL|field|protocol
specifier|protected
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
specifier|protected
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
specifier|protected
name|int
name|port
decl_stmt|;
annotation|@
name|UriParam
DECL|field|broadcast
specifier|protected
name|boolean
name|broadcast
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"65536"
argument_list|)
DECL|field|sendBufferSize
specifier|protected
name|int
name|sendBufferSize
init|=
literal|65536
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"65536"
argument_list|)
DECL|field|receiveBufferSize
specifier|protected
name|int
name|receiveBufferSize
init|=
literal|65536
decl_stmt|;
annotation|@
name|UriParam
DECL|field|receiveBufferSizePredictor
specifier|protected
name|int
name|receiveBufferSizePredictor
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"1"
argument_list|)
DECL|field|bossCount
specifier|protected
name|int
name|bossCount
init|=
literal|1
decl_stmt|;
annotation|@
name|UriParam
DECL|field|workerCount
specifier|protected
name|int
name|workerCount
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|keepAlive
specifier|protected
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
specifier|protected
name|boolean
name|tcpNoDelay
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
DECL|field|reuseAddress
specifier|protected
name|boolean
name|reuseAddress
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"10000"
argument_list|)
DECL|field|connectTimeout
specifier|protected
name|int
name|connectTimeout
init|=
literal|10000
decl_stmt|;
annotation|@
name|UriParam
DECL|field|backlog
specifier|protected
name|int
name|backlog
decl_stmt|;
annotation|@
name|UriParam
DECL|field|serverInitializerFactory
specifier|protected
name|ServerInitializerFactory
name|serverInitializerFactory
decl_stmt|;
annotation|@
name|UriParam
DECL|field|nettyServerBootstrapFactory
specifier|protected
name|NettyServerBootstrapFactory
name|nettyServerBootstrapFactory
decl_stmt|;
DECL|field|options
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
decl_stmt|;
comment|// SSL options is also part of the server bootstrap as the server listener on port X is either plain or SSL
annotation|@
name|UriParam
DECL|field|ssl
specifier|protected
name|boolean
name|ssl
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sslClientCertHeaders
specifier|protected
name|boolean
name|sslClientCertHeaders
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sslHandler
specifier|protected
name|SslHandler
name|sslHandler
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sslContextParameters
specifier|protected
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
annotation|@
name|UriParam
DECL|field|needClientAuth
specifier|protected
name|boolean
name|needClientAuth
decl_stmt|;
annotation|@
name|UriParam
DECL|field|keyStoreFile
specifier|protected
name|File
name|keyStoreFile
decl_stmt|;
annotation|@
name|UriParam
DECL|field|trustStoreFile
specifier|protected
name|File
name|trustStoreFile
decl_stmt|;
annotation|@
name|UriParam
DECL|field|keyStoreResource
specifier|protected
name|String
name|keyStoreResource
decl_stmt|;
annotation|@
name|UriParam
DECL|field|trustStoreResource
specifier|protected
name|String
name|trustStoreResource
decl_stmt|;
annotation|@
name|UriParam
DECL|field|keyStoreFormat
specifier|protected
name|String
name|keyStoreFormat
decl_stmt|;
annotation|@
name|UriParam
DECL|field|securityProvider
specifier|protected
name|String
name|securityProvider
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
name|DEFAULT_ENABLED_PROTOCOLS
argument_list|)
DECL|field|enabledProtocols
specifier|protected
name|String
name|enabledProtocols
init|=
name|DEFAULT_ENABLED_PROTOCOLS
decl_stmt|;
annotation|@
name|UriParam
DECL|field|passphrase
specifier|protected
name|String
name|passphrase
decl_stmt|;
annotation|@
name|UriParam
DECL|field|bossGroup
specifier|protected
name|EventLoopGroup
name|bossGroup
decl_stmt|;
annotation|@
name|UriParam
DECL|field|workerGroup
specifier|protected
name|EventLoopGroup
name|workerGroup
decl_stmt|;
annotation|@
name|UriParam
DECL|field|networkInterface
specifier|protected
name|String
name|networkInterface
decl_stmt|;
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
DECL|method|setSendBufferSize (int sendBufferSize)
specifier|public
name|void
name|setSendBufferSize
parameter_list|(
name|int
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
DECL|method|setReceiveBufferSize (int receiveBufferSize)
specifier|public
name|void
name|setReceiveBufferSize
parameter_list|(
name|int
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
DECL|method|getReceiveBufferSizePredictor ()
specifier|public
name|int
name|getReceiveBufferSizePredictor
parameter_list|()
block|{
return|return
name|receiveBufferSizePredictor
return|;
block|}
DECL|method|setReceiveBufferSizePredictor (int receiveBufferSizePredictor)
specifier|public
name|void
name|setReceiveBufferSizePredictor
parameter_list|(
name|int
name|receiveBufferSizePredictor
parameter_list|)
block|{
name|this
operator|.
name|receiveBufferSizePredictor
operator|=
name|receiveBufferSizePredictor
expr_stmt|;
block|}
DECL|method|getWorkerCount ()
specifier|public
name|int
name|getWorkerCount
parameter_list|()
block|{
return|return
name|workerCount
return|;
block|}
DECL|method|setWorkerCount (int workerCount)
specifier|public
name|void
name|setWorkerCount
parameter_list|(
name|int
name|workerCount
parameter_list|)
block|{
name|this
operator|.
name|workerCount
operator|=
name|workerCount
expr_stmt|;
block|}
DECL|method|getBossCount ()
specifier|public
name|int
name|getBossCount
parameter_list|()
block|{
return|return
name|bossCount
return|;
block|}
DECL|method|setBossCount (int bossCount)
specifier|public
name|void
name|setBossCount
parameter_list|(
name|int
name|bossCount
parameter_list|)
block|{
name|this
operator|.
name|bossCount
operator|=
name|bossCount
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
DECL|method|isSslClientCertHeaders ()
specifier|public
name|boolean
name|isSslClientCertHeaders
parameter_list|()
block|{
return|return
name|sslClientCertHeaders
return|;
block|}
DECL|method|setSslClientCertHeaders (boolean sslClientCertHeaders)
specifier|public
name|void
name|setSslClientCertHeaders
parameter_list|(
name|boolean
name|sslClientCertHeaders
parameter_list|)
block|{
name|this
operator|.
name|sslClientCertHeaders
operator|=
name|sslClientCertHeaders
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
DECL|method|isNeedClientAuth ()
specifier|public
name|boolean
name|isNeedClientAuth
parameter_list|()
block|{
return|return
name|needClientAuth
return|;
block|}
DECL|method|setNeedClientAuth (boolean needClientAuth)
specifier|public
name|void
name|setNeedClientAuth
parameter_list|(
name|boolean
name|needClientAuth
parameter_list|)
block|{
name|this
operator|.
name|needClientAuth
operator|=
name|needClientAuth
expr_stmt|;
block|}
annotation|@
name|Deprecated
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
annotation|@
name|Deprecated
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
annotation|@
name|Deprecated
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
annotation|@
name|Deprecated
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
DECL|method|getKeyStoreResource ()
specifier|public
name|String
name|getKeyStoreResource
parameter_list|()
block|{
return|return
name|keyStoreResource
return|;
block|}
DECL|method|setKeyStoreResource (String keyStoreResource)
specifier|public
name|void
name|setKeyStoreResource
parameter_list|(
name|String
name|keyStoreResource
parameter_list|)
block|{
name|this
operator|.
name|keyStoreResource
operator|=
name|keyStoreResource
expr_stmt|;
block|}
DECL|method|getTrustStoreResource ()
specifier|public
name|String
name|getTrustStoreResource
parameter_list|()
block|{
return|return
name|trustStoreResource
return|;
block|}
DECL|method|setTrustStoreResource (String trustStoreResource)
specifier|public
name|void
name|setTrustStoreResource
parameter_list|(
name|String
name|trustStoreResource
parameter_list|)
block|{
name|this
operator|.
name|trustStoreResource
operator|=
name|trustStoreResource
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
comment|/**      * @deprecated use #getServerInitializerFactory      */
annotation|@
name|Deprecated
DECL|method|getServerPipelineFactory ()
specifier|public
name|ServerInitializerFactory
name|getServerPipelineFactory
parameter_list|()
block|{
return|return
name|serverInitializerFactory
return|;
block|}
comment|/**      * @deprecated use #setServerInitializerFactory      */
annotation|@
name|Deprecated
DECL|method|setServerPipelineFactory (ServerInitializerFactory serverPipelineFactory)
specifier|public
name|void
name|setServerPipelineFactory
parameter_list|(
name|ServerInitializerFactory
name|serverPipelineFactory
parameter_list|)
block|{
name|this
operator|.
name|serverInitializerFactory
operator|=
name|serverPipelineFactory
expr_stmt|;
block|}
DECL|method|getServerInitializerFactory ()
specifier|public
name|ServerInitializerFactory
name|getServerInitializerFactory
parameter_list|()
block|{
return|return
name|serverInitializerFactory
return|;
block|}
DECL|method|setServerInitializerFactory (ServerInitializerFactory serverInitializerFactory)
specifier|public
name|void
name|setServerInitializerFactory
parameter_list|(
name|ServerInitializerFactory
name|serverInitializerFactory
parameter_list|)
block|{
name|this
operator|.
name|serverInitializerFactory
operator|=
name|serverInitializerFactory
expr_stmt|;
block|}
DECL|method|getNettyServerBootstrapFactory ()
specifier|public
name|NettyServerBootstrapFactory
name|getNettyServerBootstrapFactory
parameter_list|()
block|{
return|return
name|nettyServerBootstrapFactory
return|;
block|}
DECL|method|setNettyServerBootstrapFactory (NettyServerBootstrapFactory nettyServerBootstrapFactory)
specifier|public
name|void
name|setNettyServerBootstrapFactory
parameter_list|(
name|NettyServerBootstrapFactory
name|nettyServerBootstrapFactory
parameter_list|)
block|{
name|this
operator|.
name|nettyServerBootstrapFactory
operator|=
name|nettyServerBootstrapFactory
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
DECL|method|getBossGroup ()
specifier|public
name|EventLoopGroup
name|getBossGroup
parameter_list|()
block|{
return|return
name|bossGroup
return|;
block|}
comment|/**      * Set the BossGroup which could be used for handling the new connection of the server side across the NettyEndpoint       * @param bossGroup      */
DECL|method|setBossGroup (EventLoopGroup bossGroup)
specifier|public
name|void
name|setBossGroup
parameter_list|(
name|EventLoopGroup
name|bossGroup
parameter_list|)
block|{
name|this
operator|.
name|bossGroup
operator|=
name|bossGroup
expr_stmt|;
block|}
DECL|method|getWorkerGroup ()
specifier|public
name|EventLoopGroup
name|getWorkerGroup
parameter_list|()
block|{
return|return
name|workerGroup
return|;
block|}
comment|/**      * Set the WorkerGroup which could be used for handling selector eventloop across the NettyEndpoint       * @param workerGroup      */
DECL|method|setWorkerGroup (EventLoopGroup workerGroup)
specifier|public
name|void
name|setWorkerGroup
parameter_list|(
name|EventLoopGroup
name|workerGroup
parameter_list|)
block|{
name|this
operator|.
name|workerGroup
operator|=
name|workerGroup
expr_stmt|;
block|}
DECL|method|getNetworkInterface ()
specifier|public
name|String
name|getNetworkInterface
parameter_list|()
block|{
return|return
name|networkInterface
return|;
block|}
DECL|method|setNetworkInterface (String networkInterface)
specifier|public
name|void
name|setNetworkInterface
parameter_list|(
name|String
name|networkInterface
parameter_list|)
block|{
name|this
operator|.
name|networkInterface
operator|=
name|networkInterface
expr_stmt|;
block|}
DECL|method|getEnabledProtocols ()
specifier|public
name|String
name|getEnabledProtocols
parameter_list|()
block|{
return|return
name|enabledProtocols
return|;
block|}
DECL|method|setEnabledProtocols (String enabledProtocols)
specifier|public
name|void
name|setEnabledProtocols
parameter_list|(
name|String
name|enabledProtocols
parameter_list|)
block|{
name|this
operator|.
name|enabledProtocols
operator|=
name|enabledProtocols
expr_stmt|;
block|}
comment|/**      * Checks if the other {@link NettyServerBootstrapConfiguration} is compatible      * with this, as a Netty listener bound on port X shares the same common      * {@link NettyServerBootstrapConfiguration}, which must be identical.      */
DECL|method|compatible (NettyServerBootstrapConfiguration other)
specifier|public
name|boolean
name|compatible
parameter_list|(
name|NettyServerBootstrapConfiguration
name|other
parameter_list|)
block|{
name|boolean
name|isCompatible
init|=
literal|true
decl_stmt|;
if|if
condition|(
operator|!
name|protocol
operator|.
name|equals
argument_list|(
name|other
operator|.
name|protocol
argument_list|)
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|host
operator|.
name|equals
argument_list|(
name|other
operator|.
name|host
argument_list|)
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|port
operator|!=
name|other
operator|.
name|port
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|broadcast
operator|!=
name|other
operator|.
name|broadcast
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|sendBufferSize
operator|!=
name|other
operator|.
name|sendBufferSize
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|receiveBufferSize
operator|!=
name|other
operator|.
name|receiveBufferSize
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|receiveBufferSizePredictor
operator|!=
name|other
operator|.
name|receiveBufferSizePredictor
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|workerCount
operator|!=
name|other
operator|.
name|workerCount
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|bossCount
operator|!=
name|other
operator|.
name|bossCount
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|keepAlive
operator|!=
name|other
operator|.
name|keepAlive
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|tcpNoDelay
operator|!=
name|other
operator|.
name|tcpNoDelay
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|reuseAddress
operator|!=
name|other
operator|.
name|reuseAddress
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|connectTimeout
operator|!=
name|other
operator|.
name|connectTimeout
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|backlog
operator|!=
name|other
operator|.
name|backlog
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|serverInitializerFactory
operator|!=
name|other
operator|.
name|serverInitializerFactory
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nettyServerBootstrapFactory
operator|!=
name|other
operator|.
name|nettyServerBootstrapFactory
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|options
operator|==
literal|null
operator|&&
name|other
operator|.
name|options
operator|!=
literal|null
condition|)
block|{
comment|// validate all the options is identical
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|options
operator|!=
literal|null
operator|&&
name|other
operator|.
name|options
operator|==
literal|null
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|options
operator|!=
literal|null
operator|&&
name|other
operator|.
name|options
operator|!=
literal|null
operator|&&
name|options
operator|.
name|size
argument_list|()
operator|!=
name|other
operator|.
name|options
operator|.
name|size
argument_list|()
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|options
operator|!=
literal|null
operator|&&
name|other
operator|.
name|options
operator|!=
literal|null
operator|&&
operator|!
name|options
operator|.
name|keySet
argument_list|()
operator|.
name|containsAll
argument_list|(
name|other
operator|.
name|options
operator|.
name|keySet
argument_list|()
argument_list|)
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|options
operator|!=
literal|null
operator|&&
name|other
operator|.
name|options
operator|!=
literal|null
operator|&&
operator|!
name|options
operator|.
name|values
argument_list|()
operator|.
name|containsAll
argument_list|(
name|other
operator|.
name|options
operator|.
name|values
argument_list|()
argument_list|)
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ssl
operator|!=
name|other
operator|.
name|ssl
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|sslHandler
operator|!=
name|other
operator|.
name|sslHandler
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|sslContextParameters
operator|!=
name|other
operator|.
name|sslContextParameters
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|needClientAuth
operator|!=
name|other
operator|.
name|needClientAuth
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|keyStoreFile
operator|!=
name|other
operator|.
name|keyStoreFile
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|trustStoreFile
operator|!=
name|other
operator|.
name|trustStoreFile
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|keyStoreResource
operator|!=
literal|null
operator|&&
operator|!
name|keyStoreResource
operator|.
name|equals
argument_list|(
name|other
operator|.
name|keyStoreResource
argument_list|)
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|trustStoreResource
operator|!=
literal|null
operator|&&
operator|!
name|trustStoreResource
operator|.
name|equals
argument_list|(
name|other
operator|.
name|trustStoreResource
argument_list|)
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|keyStoreFormat
operator|!=
literal|null
operator|&&
operator|!
name|keyStoreFormat
operator|.
name|equals
argument_list|(
name|other
operator|.
name|keyStoreFormat
argument_list|)
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|securityProvider
operator|!=
literal|null
operator|&&
operator|!
name|securityProvider
operator|.
name|equals
argument_list|(
name|other
operator|.
name|securityProvider
argument_list|)
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|passphrase
operator|!=
literal|null
operator|&&
operator|!
name|passphrase
operator|.
name|equals
argument_list|(
name|other
operator|.
name|passphrase
argument_list|)
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|bossGroup
operator|!=
name|other
operator|.
name|bossGroup
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|workerGroup
operator|!=
name|other
operator|.
name|workerGroup
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|networkInterface
operator|!=
literal|null
operator|&&
operator|!
name|networkInterface
operator|.
name|equals
argument_list|(
name|other
operator|.
name|networkInterface
argument_list|)
condition|)
block|{
name|isCompatible
operator|=
literal|false
expr_stmt|;
block|}
return|return
name|isCompatible
return|;
block|}
DECL|method|toStringBootstrapConfiguration ()
specifier|public
name|String
name|toStringBootstrapConfiguration
parameter_list|()
block|{
return|return
literal|"NettyServerBootstrapConfiguration{"
operator|+
literal|"protocol='"
operator|+
name|protocol
operator|+
literal|'\''
operator|+
literal|", host='"
operator|+
name|host
operator|+
literal|'\''
operator|+
literal|", port="
operator|+
name|port
operator|+
literal|", broadcast="
operator|+
name|broadcast
operator|+
literal|", sendBufferSize="
operator|+
name|sendBufferSize
operator|+
literal|", receiveBufferSize="
operator|+
name|receiveBufferSize
operator|+
literal|", receiveBufferSizePredictor="
operator|+
name|receiveBufferSizePredictor
operator|+
literal|", workerCount="
operator|+
name|workerCount
operator|+
literal|", bossCount="
operator|+
name|bossCount
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
literal|", connectTimeout="
operator|+
name|connectTimeout
operator|+
literal|", backlog="
operator|+
name|backlog
operator|+
literal|", serverInitializerFactory="
operator|+
name|serverInitializerFactory
operator|+
literal|", nettyServerBootstrapFactory="
operator|+
name|nettyServerBootstrapFactory
operator|+
literal|", options="
operator|+
name|options
operator|+
literal|", ssl="
operator|+
name|ssl
operator|+
literal|", sslHandler="
operator|+
name|sslHandler
operator|+
literal|", sslContextParameters='"
operator|+
name|sslContextParameters
operator|+
literal|'\''
operator|+
literal|", needClientAuth="
operator|+
name|needClientAuth
operator|+
literal|", enabledProtocols='"
operator|+
name|enabledProtocols
operator|+
literal|", keyStoreFile="
operator|+
name|keyStoreFile
operator|+
literal|", trustStoreFile="
operator|+
name|trustStoreFile
operator|+
literal|", keyStoreResource='"
operator|+
name|keyStoreResource
operator|+
literal|'\''
operator|+
literal|", trustStoreResource='"
operator|+
name|trustStoreResource
operator|+
literal|'\''
operator|+
literal|", keyStoreFormat='"
operator|+
name|keyStoreFormat
operator|+
literal|'\''
operator|+
literal|", securityProvider='"
operator|+
name|securityProvider
operator|+
literal|'\''
operator|+
literal|", passphrase='"
operator|+
name|passphrase
operator|+
literal|'\''
operator|+
literal|", bossGroup="
operator|+
name|bossGroup
operator|+
literal|", workerGroup="
operator|+
name|workerGroup
operator|+
literal|", networkInterface='"
operator|+
name|networkInterface
operator|+
literal|'\''
operator|+
literal|'}'
return|;
block|}
block|}
end_class

end_unit

