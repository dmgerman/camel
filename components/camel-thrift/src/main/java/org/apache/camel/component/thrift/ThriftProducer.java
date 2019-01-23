begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|AsyncProducer
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
name|Message
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
name|client
operator|.
name|AsyncClientMethodCallback
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
name|DefaultAsyncProducer
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
name|TException
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
name|TNonblockingSocket
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
name|TNonblockingTransport
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
name|TSocket
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
name|TTransport
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

begin_comment
comment|/**  * Represents asynchronous and synchronous Thrift producer implementations  */
end_comment

begin_class
DECL|class|ThriftProducer
specifier|public
class|class
name|ThriftProducer
extends|extends
name|DefaultAsyncProducer
implements|implements
name|AsyncProducer
block|{
DECL|field|configuration
specifier|protected
specifier|final
name|ThriftConfiguration
name|configuration
decl_stmt|;
DECL|field|endpoint
specifier|protected
specifier|final
name|ThriftEndpoint
name|endpoint
decl_stmt|;
DECL|field|syncTransport
specifier|private
name|TTransport
name|syncTransport
decl_stmt|;
DECL|field|asyncTransport
specifier|private
name|TNonblockingTransport
name|asyncTransport
decl_stmt|;
DECL|field|thriftClient
specifier|private
name|Object
name|thriftClient
decl_stmt|;
DECL|method|ThriftProducer (ThriftEndpoint endpoint, ThriftConfiguration configuration)
specifier|public
name|ThriftProducer
parameter_list|(
name|ThriftEndpoint
name|endpoint
parameter_list|,
name|ThriftConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
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
annotation|@
name|Override
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
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
try|try
block|{
name|ThriftUtils
operator|.
name|invokeAsyncMethod
argument_list|(
name|thriftClient
argument_list|,
name|configuration
operator|.
name|getMethod
argument_list|()
argument_list|,
name|message
operator|.
name|getBody
argument_list|()
argument_list|,
operator|new
name|AsyncClientMethodCallback
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|TException
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
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
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
try|try
block|{
name|Object
name|outBody
init|=
name|ThriftUtils
operator|.
name|invokeSyncMethod
argument_list|(
name|thriftClient
argument_list|,
name|configuration
operator|.
name|getMethod
argument_list|()
argument_list|,
name|message
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|outBody
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|TException
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|Exception
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
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
name|configuration
operator|.
name|getNegotiationType
argument_list|()
operator|==
name|ThriftNegotiationType
operator|.
name|SSL
condition|)
block|{
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isSynchronous
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The SSL negotiation type requires to set syncronous communication mode"
argument_list|)
throw|;
block|}
if|if
condition|(
name|syncTransport
operator|==
literal|null
condition|)
block|{
name|initializeSslTransport
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Getting synchronous secured client implementation"
argument_list|)
expr_stmt|;
name|thriftClient
operator|=
name|ThriftUtils
operator|.
name|constructClientInstance
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
name|syncTransport
argument_list|,
name|configuration
operator|.
name|getExchangeProtocol
argument_list|()
argument_list|,
name|configuration
operator|.
name|getNegotiationType
argument_list|()
argument_list|,
name|configuration
operator|.
name|getCompressionType
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|.
name|isSynchronous
argument_list|()
condition|)
block|{
if|if
condition|(
name|syncTransport
operator|==
literal|null
condition|)
block|{
name|initializeSyncTransport
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Getting synchronous client implementation"
argument_list|)
expr_stmt|;
name|thriftClient
operator|=
name|ThriftUtils
operator|.
name|constructClientInstance
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
name|syncTransport
argument_list|,
name|configuration
operator|.
name|getExchangeProtocol
argument_list|()
argument_list|,
name|configuration
operator|.
name|getNegotiationType
argument_list|()
argument_list|,
name|configuration
operator|.
name|getCompressionType
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|asyncTransport
operator|==
literal|null
condition|)
block|{
name|initializeAsyncTransport
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Getting asynchronous client implementation"
argument_list|)
expr_stmt|;
name|thriftClient
operator|=
name|ThriftUtils
operator|.
name|constructAsyncClientInstance
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
name|asyncTransport
argument_list|,
name|configuration
operator|.
name|getExchangeProtocol
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|syncTransport
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Terminating synchronous transport the remote Thrift server"
argument_list|)
expr_stmt|;
name|syncTransport
operator|.
name|close
argument_list|()
expr_stmt|;
name|syncTransport
operator|=
literal|null
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|asyncTransport
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Terminating asynchronous transport the remote Thrift server"
argument_list|)
expr_stmt|;
name|asyncTransport
operator|.
name|close
argument_list|()
expr_stmt|;
name|asyncTransport
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
DECL|method|initializeSyncTransport ()
specifier|protected
name|void
name|initializeSyncTransport
parameter_list|()
throws|throws
name|TTransportException
block|{
if|if
condition|(
operator|!
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
operator|!
name|ObjectHelper
operator|.
name|isEmpty
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
name|info
argument_list|(
literal|"Creating transport to the remote Thrift server {}:{}"
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
name|syncTransport
operator|=
operator|new
name|TSocket
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
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No connection properties (host, port) specified"
argument_list|)
throw|;
block|}
name|syncTransport
operator|.
name|open
argument_list|()
expr_stmt|;
block|}
DECL|method|initializeAsyncTransport ()
specifier|protected
name|void
name|initializeAsyncTransport
parameter_list|()
throws|throws
name|IOException
throws|,
name|TTransportException
block|{
if|if
condition|(
operator|!
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
operator|!
name|ObjectHelper
operator|.
name|isEmpty
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
name|info
argument_list|(
literal|"Creating transport to the remote Thrift server {}:{}"
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
name|asyncTransport
operator|=
operator|new
name|TNonblockingSocket
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
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No connection properties (host, port) specified"
argument_list|)
throw|;
block|}
block|}
DECL|method|initializeSslTransport ()
specifier|protected
name|void
name|initializeSslTransport
parameter_list|()
throws|throws
name|TTransportException
throws|,
name|IOException
block|{
if|if
condition|(
operator|!
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
operator|!
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
condition|)
block|{
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
name|getTrustManagers
argument_list|()
operator|.
name|getKeyStore
argument_list|()
operator|.
name|getResource
argument_list|()
argument_list|,
literal|"Trust store path"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|sslParameters
operator|.
name|getTrustManagers
argument_list|()
operator|.
name|getKeyStore
argument_list|()
operator|.
name|getPassword
argument_list|()
argument_list|,
literal|"Trust store password"
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Creating secured transport to the remote Thrift server {}:{}"
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
name|TSSLTransportFactory
operator|.
name|TSSLTransportParameters
name|sslParams
decl_stmt|;
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
name|getTrustManagers
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
name|getTrustManagers
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
name|setTrustStore
argument_list|(
name|ResourceHelper
operator|.
name|resolveResourceAsInputStream
argument_list|(
name|classResolver
argument_list|,
name|sslParameters
operator|.
name|getTrustManagers
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
name|getTrustManagers
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
name|getTrustManagers
argument_list|()
operator|.
name|getProvider
argument_list|()
argument_list|,
name|sslParameters
operator|.
name|getTrustManagers
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
name|setTrustStore
argument_list|(
name|sslParameters
operator|.
name|getTrustManagers
argument_list|()
operator|.
name|getKeyStore
argument_list|()
operator|.
name|getResource
argument_list|()
argument_list|,
name|sslParameters
operator|.
name|getTrustManagers
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
name|syncTransport
operator|=
name|TSSLTransportFactory
operator|.
name|getClientSocket
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
argument_list|,
name|configuration
operator|.
name|getClientTimeout
argument_list|()
argument_list|,
name|sslParams
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No connection properties (host, port) specified"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

