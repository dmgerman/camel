begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.thrift
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|thrift
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
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

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
name|UnknownHostException
import|;
end_import

begin_import
import|import
name|javassist
operator|.
name|util
operator|.
name|proxy
operator|.
name|MethodHandler
import|;
end_import

begin_import
import|import
name|javassist
operator|.
name|util
operator|.
name|proxy
operator|.
name|Proxy
import|;
end_import

begin_import
import|import
name|javassist
operator|.
name|util
operator|.
name|proxy
operator|.
name|ProxyFactory
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
name|AsyncCallback
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
name|component
operator|.
name|thrift
operator|.
name|server
operator|.
name|ThriftHsHaServer
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
name|thrift
operator|.
name|server
operator|.
name|ThriftMethodHandler
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
name|thrift
operator|.
name|server
operator|.
name|ThriftThreadPoolServer
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
name|ClassResolver
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
name|DefaultConsumer
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
name|ResourceHelper
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
name|thrift
operator|.
name|TProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|thrift
operator|.
name|server
operator|.
name|TServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|thrift
operator|.
name|transport
operator|.
name|TNonblockingServerSocket
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|thrift
operator|.
name|transport
operator|.
name|TSSLTransportFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|thrift
operator|.
name|transport
operator|.
name|TServerSocket
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|thrift
operator|.
name|transport
operator|.
name|TTransportException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|thrift
operator|.
name|transport
operator|.
name|TZlibTransport
import|;
end_import

begin_comment
comment|/**  * Represents Thrift server consumer implementation  */
end_comment

begin_class
DECL|class|ThriftConsumer
specifier|public
class|class
name|ThriftConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|asyncServerTransport
specifier|private
name|TNonblockingServerSocket
name|asyncServerTransport
decl_stmt|;
DECL|field|syncServerTransport
specifier|private
name|TServerSocket
name|syncServerTransport
decl_stmt|;
DECL|field|server
specifier|private
name|TServer
name|server
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|ThriftConfiguration
name|configuration
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|ThriftEndpoint
name|endpoint
decl_stmt|;
DECL|method|ThriftConsumer (ThriftEndpoint endpoint, Processor processor, ThriftConfiguration configuration)
specifier|public
name|ThriftConsumer
parameter_list|(
name|ThriftEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ThriftConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|ThriftConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
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
if|if
condition|(
name|server
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Starting the Thrift server"
argument_list|)
expr_stmt|;
name|initializeServer
argument_list|()
expr_stmt|;
name|server
operator|.
name|serve
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Thrift server started and listening on port: {}"
argument_list|,
name|asyncServerTransport
operator|==
literal|null
condition|?
name|syncServerTransport
operator|.
name|getServerSocket
argument_list|()
operator|.
name|getLocalPort
argument_list|()
else|:
name|asyncServerTransport
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Terminating Thrift server"
argument_list|)
expr_stmt|;
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|asyncServerTransport
argument_list|)
condition|)
block|{
name|asyncServerTransport
operator|.
name|close
argument_list|()
expr_stmt|;
name|asyncServerTransport
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|syncServerTransport
argument_list|)
condition|)
block|{
name|syncServerTransport
operator|.
name|close
argument_list|()
expr_stmt|;
name|syncServerTransport
operator|=
literal|null
expr_stmt|;
block|}
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
name|server
operator|=
literal|null
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
DECL|method|initializeServer ()
specifier|protected
name|void
name|initializeServer
parameter_list|()
throws|throws
name|TTransportException
throws|,
name|IOException
block|{
name|Class
name|serverImplementationClass
decl_stmt|;
name|Object
name|serverImplementationInstance
decl_stmt|;
name|Object
name|serverProcessor
decl_stmt|;
name|ProxyFactory
name|serviceProxy
init|=
operator|new
name|ProxyFactory
argument_list|()
decl_stmt|;
name|MethodHandler
name|methodHandler
init|=
operator|new
name|ThriftMethodHandler
argument_list|(
name|endpoint
argument_list|,
name|this
argument_list|)
decl_stmt|;
try|try
block|{
name|Class
name|serverInterface
init|=
name|ThriftUtils
operator|.
name|getServerInterface
argument_list|(
name|endpoint
operator|.
name|getServicePackage
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getServiceName
argument_list|()
argument_list|,
name|endpoint
operator|.
name|isSynchronous
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|serviceProxy
operator|.
name|setInterfaces
argument_list|(
operator|new
name|Class
index|[]
block|{
name|serverInterface
block|}
argument_list|)
expr_stmt|;
name|serverImplementationClass
operator|=
name|serviceProxy
operator|.
name|createClass
argument_list|()
expr_stmt|;
name|serverImplementationInstance
operator|=
name|serverImplementationClass
operator|.
name|getConstructor
argument_list|()
operator|.
name|newInstance
argument_list|()
expr_stmt|;
operator|(
operator|(
name|Proxy
operator|)
name|serverImplementationInstance
operator|)
operator|.
name|setHandler
argument_list|(
name|methodHandler
argument_list|)
expr_stmt|;
name|serverProcessor
operator|=
name|ThriftUtils
operator|.
name|constructServerProcessor
argument_list|(
name|endpoint
operator|.
name|getServicePackage
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getServiceName
argument_list|()
argument_list|,
name|serverImplementationInstance
argument_list|,
name|endpoint
operator|.
name|isSynchronous
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
decl||
name|InstantiationException
decl||
name|IllegalAccessException
decl||
name|InvocationTargetException
decl||
name|NoSuchMethodException
decl||
name|SecurityException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to create server implementation proxy service for "
operator|+
name|configuration
operator|.
name|getService
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getNegotiationType
argument_list|()
operator|==
name|ThriftNegotiationType
operator|.
name|SSL
operator|&&
name|endpoint
operator|.
name|isSynchronous
argument_list|()
condition|)
block|{
name|ClassResolver
name|classResolver
init|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
decl_stmt|;
name|SSLContextParameters
name|sslParameters
init|=
name|configuration
operator|.
name|getSslParameters
argument_list|()
decl_stmt|;
if|if
condition|(
name|sslParameters
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"SSL parameters must be initialized if negotiation type is set to "
operator|+
name|configuration
operator|.
name|getNegotiationType
argument_list|()
argument_list|)
throw|;
block|}
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|sslParameters
operator|.
name|getSecureSocketProtocol
argument_list|()
argument_list|,
literal|"Security protocol"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|sslParameters
operator|.
name|getKeyManagers
argument_list|()
operator|.
name|getKeyStore
argument_list|()
operator|.
name|getResource
argument_list|()
argument_list|,
literal|"Keystore path"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|sslParameters
operator|.
name|getKeyManagers
argument_list|()
operator|.
name|getKeyStore
argument_list|()
operator|.
name|getPassword
argument_list|()
argument_list|,
literal|"Keystore password"
argument_list|)
expr_stmt|;
name|TSSLTransportFactory
operator|.
name|TSSLTransportParameters
name|sslParams
decl_stmt|;
name|sslParams
operator|=
operator|new
name|TSSLTransportFactory
operator|.
name|TSSLTransportParameters
argument_list|(
name|sslParameters
operator|.
name|getSecureSocketProtocol
argument_list|()
argument_list|,
name|sslParameters
operator|.
name|getCipherSuites
argument_list|()
operator|==
literal|null
condition|?
literal|null
else|:
name|sslParameters
operator|.
name|getCipherSuites
argument_list|()
operator|.
name|getCipherSuite
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|toArray
argument_list|(
name|String
index|[]
operator|::
operator|new
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|sslParameters
operator|.
name|getKeyManagers
argument_list|()
operator|.
name|getKeyStore
argument_list|()
operator|.
name|getProvider
argument_list|()
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|sslParameters
operator|.
name|getKeyManagers
argument_list|()
operator|.
name|getKeyStore
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|sslParams
operator|.
name|setKeyStore
argument_list|(
name|ResourceHelper
operator|.
name|resolveResourceAsInputStream
argument_list|(
name|classResolver
argument_list|,
name|sslParameters
operator|.
name|getKeyManagers
argument_list|()
operator|.
name|getKeyStore
argument_list|()
operator|.
name|getResource
argument_list|()
argument_list|)
argument_list|,
name|sslParameters
operator|.
name|getKeyManagers
argument_list|()
operator|.
name|getKeyStore
argument_list|()
operator|.
name|getPassword
argument_list|()
argument_list|,
name|sslParameters
operator|.
name|getKeyManagers
argument_list|()
operator|.
name|getKeyStore
argument_list|()
operator|.
name|getProvider
argument_list|()
argument_list|,
name|sslParameters
operator|.
name|getKeyManagers
argument_list|()
operator|.
name|getKeyStore
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sslParams
operator|.
name|setKeyStore
argument_list|(
name|ResourceHelper
operator|.
name|resolveResourceAsInputStream
argument_list|(
name|classResolver
argument_list|,
name|sslParameters
operator|.
name|getKeyManagers
argument_list|()
operator|.
name|getKeyStore
argument_list|()
operator|.
name|getResource
argument_list|()
argument_list|)
argument_list|,
name|sslParameters
operator|.
name|getKeyManagers
argument_list|()
operator|.
name|getKeyStore
argument_list|()
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|syncServerTransport
operator|=
name|TSSLTransportFactory
operator|.
name|getServerSocket
argument_list|(
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|,
name|configuration
operator|.
name|getClientTimeout
argument_list|()
argument_list|,
name|InetAddress
operator|.
name|getByName
argument_list|(
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
argument_list|,
name|sslParams
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnknownHostException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown host defined: "
operator|+
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
throw|;
block|}
name|ThriftThreadPoolServer
operator|.
name|Args
name|args
init|=
operator|new
name|ThriftThreadPoolServer
operator|.
name|Args
argument_list|(
name|syncServerTransport
argument_list|)
decl_stmt|;
name|args
operator|.
name|processor
argument_list|(
operator|(
name|TProcessor
operator|)
name|serverProcessor
argument_list|)
expr_stmt|;
name|args
operator|.
name|executorService
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newThreadPool
argument_list|(
name|this
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPoolSize
argument_list|()
argument_list|,
name|configuration
operator|.
name|getMaxPoolSize
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|args
operator|.
name|startThreadPool
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadExecutor
argument_list|(
name|this
argument_list|,
literal|"start-"
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|args
operator|.
name|context
argument_list|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|server
operator|=
operator|new
name|ThriftThreadPoolServer
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|configuration
operator|.
name|getCompressionType
argument_list|()
operator|==
name|ThriftCompressionType
operator|.
name|ZLIB
operator|&&
name|endpoint
operator|.
name|isSynchronous
argument_list|()
condition|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Building sync Thrift server on {}:{}"
argument_list|,
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|syncServerTransport
operator|=
operator|new
name|TServerSocket
argument_list|(
operator|new
name|InetSocketAddress
argument_list|(
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
argument_list|,
name|configuration
operator|.
name|getClientTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Building sync Thrift server on<any address>:{}"
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|syncServerTransport
operator|=
operator|new
name|TServerSocket
argument_list|(
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|,
name|configuration
operator|.
name|getClientTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No server start properties (host, port) specified"
argument_list|)
throw|;
block|}
name|ThriftThreadPoolServer
operator|.
name|Args
name|args
init|=
operator|new
name|ThriftThreadPoolServer
operator|.
name|Args
argument_list|(
name|syncServerTransport
argument_list|)
decl_stmt|;
name|args
operator|.
name|processor
argument_list|(
operator|(
name|TProcessor
operator|)
name|serverProcessor
argument_list|)
expr_stmt|;
name|args
operator|.
name|transportFactory
argument_list|(
operator|new
name|TZlibTransport
operator|.
name|Factory
argument_list|()
argument_list|)
expr_stmt|;
name|args
operator|.
name|executorService
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newThreadPool
argument_list|(
name|this
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPoolSize
argument_list|()
argument_list|,
name|configuration
operator|.
name|getMaxPoolSize
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|args
operator|.
name|startThreadPool
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadExecutor
argument_list|(
name|this
argument_list|,
literal|"start-"
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|args
operator|.
name|context
argument_list|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|server
operator|=
operator|new
name|ThriftThreadPoolServer
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Building Thrift server on {}:{}"
argument_list|,
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|asyncServerTransport
operator|=
operator|new
name|TNonblockingServerSocket
argument_list|(
operator|new
name|InetSocketAddress
argument_list|(
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
argument_list|,
name|configuration
operator|.
name|getClientTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Building Thrift server on<any address>:{}"
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|asyncServerTransport
operator|=
operator|new
name|TNonblockingServerSocket
argument_list|(
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|,
name|configuration
operator|.
name|getClientTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No server start properties (host, port) specified"
argument_list|)
throw|;
block|}
name|ThriftHsHaServer
operator|.
name|Args
name|args
init|=
operator|new
name|ThriftHsHaServer
operator|.
name|Args
argument_list|(
name|asyncServerTransport
argument_list|)
decl_stmt|;
name|args
operator|.
name|processor
argument_list|(
operator|(
name|TProcessor
operator|)
name|serverProcessor
argument_list|)
expr_stmt|;
name|args
operator|.
name|executorService
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newThreadPool
argument_list|(
name|this
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPoolSize
argument_list|()
argument_list|,
name|configuration
operator|.
name|getMaxPoolSize
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|args
operator|.
name|startThreadPool
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadExecutor
argument_list|(
name|this
argument_list|,
literal|"start-"
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|args
operator|.
name|context
argument_list|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|server
operator|=
operator|new
name|ThriftHsHaServer
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
return|return
name|doSend
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
DECL|method|doSend (Exchange exchange, AsyncCallback callback)
specifier|private
name|boolean
name|doSend
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
if|if
condition|(
name|isRunAllowed
argument_list|()
condition|)
block|{
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|doneSync
lambda|->
block|{
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Consumer not ready to process exchanges. The exchange {} will be discarded"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
block|}
end_class

end_unit

