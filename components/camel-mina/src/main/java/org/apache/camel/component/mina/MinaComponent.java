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
name|camel
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorServiceHelper
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
name|DefaultIoFilterChainBuilder
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
name|ThreadModel
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
name|LineDelimiter
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
name|executor
operator|.
name|ExecutorFilter
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
DECL|field|configuration
specifier|private
name|MinaConfiguration
name|configuration
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
comment|// Using the configuration which set by the component as a default one
comment|// Since the configuration's properties will be set by the URI
comment|// we need to copy or create a new MinaConfiguration here
name|MinaConfiguration
name|config
decl_stmt|;
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|config
operator|=
name|configuration
operator|.
name|copy
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|config
operator|=
operator|new
name|MinaConfiguration
argument_list|()
expr_stmt|;
block|}
name|URI
name|u
init|=
operator|new
name|URI
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
name|config
operator|.
name|setHost
argument_list|(
name|u
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setPort
argument_list|(
name|u
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setProtocol
argument_list|(
name|u
operator|.
name|getScheme
argument_list|()
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|config
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|createEndpoint
argument_list|(
name|uri
argument_list|,
name|config
argument_list|)
return|;
block|}
DECL|method|createEndpoint (MinaConfiguration config)
specifier|public
name|Endpoint
name|createEndpoint
parameter_list|(
name|MinaConfiguration
name|config
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createEndpoint
argument_list|(
literal|null
argument_list|,
name|config
argument_list|)
return|;
block|}
DECL|method|createEndpoint (String uri, MinaConfiguration config)
specifier|private
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|MinaConfiguration
name|config
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
name|String
name|protocol
init|=
name|config
operator|.
name|getProtocol
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
name|config
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
name|config
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
name|config
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
DECL|method|createVmEndpoint (String uri, MinaConfiguration configuration)
specifier|protected
name|MinaEndpoint
name|createVmEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|MinaConfiguration
name|configuration
parameter_list|)
block|{
name|boolean
name|minaLogger
init|=
name|configuration
operator|.
name|isMinaLogger
argument_list|()
decl_stmt|;
name|boolean
name|sync
init|=
name|configuration
operator|.
name|isSync
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|IoFilter
argument_list|>
name|filters
init|=
name|configuration
operator|.
name|getFilters
argument_list|()
decl_stmt|;
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
name|configuration
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
comment|// connector config
name|configureCodecFactory
argument_list|(
literal|"MinaProducer"
argument_list|,
name|connector
operator|.
name|getDefaultConfig
argument_list|()
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
if|if
condition|(
name|minaLogger
condition|)
block|{
name|connector
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
name|appendIoFiltersToChain
argument_list|(
name|filters
argument_list|,
name|connector
operator|.
name|getFilterChain
argument_list|()
argument_list|)
expr_stmt|;
comment|// acceptor connectorConfig
name|configureCodecFactory
argument_list|(
literal|"MinaConsumer"
argument_list|,
name|acceptor
operator|.
name|getDefaultConfig
argument_list|()
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
if|if
condition|(
name|minaLogger
condition|)
block|{
name|acceptor
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
name|appendIoFiltersToChain
argument_list|(
name|filters
argument_list|,
name|acceptor
operator|.
name|getFilterChain
argument_list|()
argument_list|)
expr_stmt|;
name|MinaEndpoint
name|endpoint
init|=
operator|new
name|MinaEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setAcceptor
argument_list|(
name|acceptor
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setConnector
argument_list|(
name|connector
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
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
DECL|method|createSocketEndpoint (String uri, MinaConfiguration configuration)
specifier|protected
name|MinaEndpoint
name|createSocketEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|MinaConfiguration
name|configuration
parameter_list|)
block|{
name|boolean
name|minaLogger
init|=
name|configuration
operator|.
name|isMinaLogger
argument_list|()
decl_stmt|;
name|long
name|timeout
init|=
name|configuration
operator|.
name|getTimeout
argument_list|()
decl_stmt|;
name|boolean
name|sync
init|=
name|configuration
operator|.
name|isSync
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|IoFilter
argument_list|>
name|filters
init|=
name|configuration
operator|.
name|getFilters
argument_list|()
decl_stmt|;
specifier|final
name|int
name|processorCount
init|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|availableProcessors
argument_list|()
operator|+
literal|1
decl_stmt|;
name|IoAcceptor
name|acceptor
init|=
operator|new
name|SocketAcceptor
argument_list|(
name|processorCount
argument_list|,
name|ExecutorServiceHelper
operator|.
name|newCachedThreadPool
argument_list|(
literal|"MinaSocketAcceptor"
argument_list|,
literal|true
argument_list|)
argument_list|)
decl_stmt|;
name|IoConnector
name|connector
init|=
operator|new
name|SocketConnector
argument_list|(
name|processorCount
argument_list|,
name|ExecutorServiceHelper
operator|.
name|newCachedThreadPool
argument_list|(
literal|"MinaSocketConnector"
argument_list|,
literal|true
argument_list|)
argument_list|)
decl_stmt|;
name|SocketAddress
name|address
init|=
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
decl_stmt|;
comment|// connector config
name|SocketConnectorConfig
name|connectorConfig
init|=
operator|new
name|SocketConnectorConfig
argument_list|()
decl_stmt|;
comment|// must use manual thread model according to Mina documentation
name|connectorConfig
operator|.
name|setThreadModel
argument_list|(
name|ThreadModel
operator|.
name|MANUAL
argument_list|)
expr_stmt|;
name|configureCodecFactory
argument_list|(
literal|"MinaProducer"
argument_list|,
name|connectorConfig
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
name|connectorConfig
operator|.
name|getFilterChain
argument_list|()
operator|.
name|addLast
argument_list|(
literal|"threadPool"
argument_list|,
operator|new
name|ExecutorFilter
argument_list|(
name|ExecutorServiceHelper
operator|.
name|newCachedThreadPool
argument_list|(
literal|"MinaThreadPool"
argument_list|,
literal|true
argument_list|)
argument_list|)
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
name|appendIoFiltersToChain
argument_list|(
name|filters
argument_list|,
name|connectorConfig
operator|.
name|getFilterChain
argument_list|()
argument_list|)
expr_stmt|;
comment|// set connect timeout to mina in seconds
name|connectorConfig
operator|.
name|setConnectTimeout
argument_list|(
call|(
name|int
call|)
argument_list|(
name|timeout
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
name|configureCodecFactory
argument_list|(
literal|"MinaConsumer"
argument_list|,
name|acceptorConfig
argument_list|,
name|configuration
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
name|acceptorConfig
operator|.
name|getFilterChain
argument_list|()
operator|.
name|addLast
argument_list|(
literal|"threadPool"
argument_list|,
operator|new
name|ExecutorFilter
argument_list|(
name|ExecutorServiceHelper
operator|.
name|newCachedThreadPool
argument_list|(
literal|"MinaThreadPool"
argument_list|,
literal|true
argument_list|)
argument_list|)
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
name|appendIoFiltersToChain
argument_list|(
name|filters
argument_list|,
name|acceptorConfig
operator|.
name|getFilterChain
argument_list|()
argument_list|)
expr_stmt|;
name|MinaEndpoint
name|endpoint
init|=
operator|new
name|MinaEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setAcceptor
argument_list|(
name|acceptor
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setAcceptorConfig
argument_list|(
name|acceptorConfig
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setConnector
argument_list|(
name|connector
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setConnectorConfig
argument_list|(
name|connectorConfig
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
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
DECL|method|configureCodecFactory (String type, IoServiceConfig config, MinaConfiguration configuration)
specifier|protected
name|void
name|configureCodecFactory
parameter_list|(
name|String
name|type
parameter_list|,
name|IoServiceConfig
name|config
parameter_list|,
name|MinaConfiguration
name|configuration
parameter_list|)
block|{
name|ProtocolCodecFactory
name|codecFactory
init|=
name|configuration
operator|.
name|getCodec
argument_list|()
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
name|configuration
operator|.
name|isTextline
argument_list|()
condition|)
block|{
name|Charset
name|charset
init|=
name|getEncodingParameter
argument_list|(
name|type
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
name|LineDelimiter
name|delimiter
init|=
name|getLineDelimiterParameter
argument_list|(
name|configuration
operator|.
name|getTextlineDelimiter
argument_list|()
argument_list|)
decl_stmt|;
name|TextLineCodecFactory
name|tmpCodecFactory
init|=
operator|new
name|TextLineCodecFactory
argument_list|(
name|charset
argument_list|,
name|delimiter
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getEncoderMaxLineLength
argument_list|()
operator|>
literal|0
condition|)
block|{
name|tmpCodecFactory
operator|.
name|setEncoderMaxLineLength
argument_list|(
name|configuration
operator|.
name|getEncoderMaxLineLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getDecoderMaxLineLength
argument_list|()
operator|>
literal|0
condition|)
block|{
name|tmpCodecFactory
operator|.
name|setDecoderMaxLineLength
argument_list|(
name|configuration
operator|.
name|getDecoderMaxLineLength
argument_list|()
argument_list|)
expr_stmt|;
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
literal|": Using TextLineCodecFactory: "
operator|+
name|codecFactory
operator|+
literal|" using encoding: "
operator|+
name|charset
operator|+
literal|" line delimiter: "
operator|+
name|configuration
operator|.
name|getTextlineDelimiter
argument_list|()
operator|+
literal|"("
operator|+
name|delimiter
operator|+
literal|")"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Encoder maximum line length: "
operator|+
name|tmpCodecFactory
operator|.
name|getEncoderMaxLineLength
argument_list|()
operator|+
literal|"Decoder maximum line length: "
operator|+
name|tmpCodecFactory
operator|.
name|getDecoderMaxLineLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|codecFactory
operator|=
name|tmpCodecFactory
expr_stmt|;
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
DECL|method|createDatagramEndpoint (String uri, MinaConfiguration configuration)
specifier|protected
name|MinaEndpoint
name|createDatagramEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|MinaConfiguration
name|configuration
parameter_list|)
block|{
name|boolean
name|minaLogger
init|=
name|configuration
operator|.
name|isMinaLogger
argument_list|()
decl_stmt|;
name|long
name|timeout
init|=
name|configuration
operator|.
name|getTimeout
argument_list|()
decl_stmt|;
name|boolean
name|transferExchange
init|=
name|configuration
operator|.
name|isTransferExchange
argument_list|()
decl_stmt|;
name|boolean
name|sync
init|=
name|configuration
operator|.
name|isSync
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|IoFilter
argument_list|>
name|filters
init|=
name|configuration
operator|.
name|getFilters
argument_list|()
decl_stmt|;
name|IoAcceptor
name|acceptor
init|=
operator|new
name|DatagramAcceptor
argument_list|(
name|ExecutorServiceHelper
operator|.
name|newCachedThreadPool
argument_list|(
literal|"MinaDatagramAcceptor"
argument_list|,
literal|true
argument_list|)
argument_list|)
decl_stmt|;
name|IoConnector
name|connector
init|=
operator|new
name|DatagramConnector
argument_list|(
name|ExecutorServiceHelper
operator|.
name|newCachedThreadPool
argument_list|(
literal|"MinaDatagramConnector"
argument_list|,
literal|true
argument_list|)
argument_list|)
decl_stmt|;
name|SocketAddress
name|address
init|=
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
name|configuration
argument_list|)
expr_stmt|;
name|connectorConfig
operator|.
name|getFilterChain
argument_list|()
operator|.
name|addLast
argument_list|(
literal|"threadPool"
argument_list|,
operator|new
name|ExecutorFilter
argument_list|(
name|ExecutorServiceHelper
operator|.
name|newCachedThreadPool
argument_list|(
literal|"MinaThreadPool"
argument_list|,
literal|true
argument_list|)
argument_list|)
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
name|appendIoFiltersToChain
argument_list|(
name|filters
argument_list|,
name|connectorConfig
operator|.
name|getFilterChain
argument_list|()
argument_list|)
expr_stmt|;
comment|// set connect timeout to mina in seconds
name|connectorConfig
operator|.
name|setConnectTimeout
argument_list|(
call|(
name|int
call|)
argument_list|(
name|timeout
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
name|configuration
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
name|appendIoFiltersToChain
argument_list|(
name|filters
argument_list|,
name|acceptorConfig
operator|.
name|getFilterChain
argument_list|()
argument_list|)
expr_stmt|;
name|MinaEndpoint
name|endpoint
init|=
operator|new
name|MinaEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setAcceptor
argument_list|(
name|acceptor
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setAcceptorConfig
argument_list|(
name|acceptorConfig
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setConnector
argument_list|(
name|connector
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setConnectorConfig
argument_list|(
name|connectorConfig
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
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
comment|/**      * For datagrams the entire message is available as a single ByteBuffer so lets just pass those around by default      * and try converting whatever they payload is into ByteBuffers unless some custom converter is specified      */
DECL|method|configureDataGramCodecFactory (final String type, final IoServiceConfig config, final MinaConfiguration configuration)
specifier|protected
name|void
name|configureDataGramCodecFactory
parameter_list|(
specifier|final
name|String
name|type
parameter_list|,
specifier|final
name|IoServiceConfig
name|config
parameter_list|,
specifier|final
name|MinaConfiguration
name|configuration
parameter_list|)
block|{
name|ProtocolCodecFactory
name|codecFactory
init|=
name|configuration
operator|.
name|getCodec
argument_list|()
decl_stmt|;
if|if
condition|(
name|codecFactory
operator|==
literal|null
condition|)
block|{
specifier|final
name|Charset
name|charset
init|=
name|getEncodingParameter
argument_list|(
name|type
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
name|codecFactory
operator|=
operator|new
name|MinaUdpProtocolCodecFactory
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
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
literal|": Using CodecFactory: "
operator|+
name|codecFactory
operator|+
literal|" using encoding: "
operator|+
name|charset
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
DECL|method|addCodecFactory (IoServiceConfig config, ProtocolCodecFactory codecFactory)
specifier|private
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
DECL|method|getLineDelimiterParameter (TextLineDelimiter delimiter)
specifier|private
specifier|static
name|LineDelimiter
name|getLineDelimiterParameter
parameter_list|(
name|TextLineDelimiter
name|delimiter
parameter_list|)
block|{
if|if
condition|(
name|delimiter
operator|==
literal|null
condition|)
block|{
return|return
name|LineDelimiter
operator|.
name|DEFAULT
return|;
block|}
switch|switch
condition|(
name|delimiter
condition|)
block|{
case|case
name|DEFAULT
case|:
return|return
name|LineDelimiter
operator|.
name|DEFAULT
return|;
case|case
name|AUTO
case|:
return|return
name|LineDelimiter
operator|.
name|AUTO
return|;
case|case
name|UNIX
case|:
return|return
name|LineDelimiter
operator|.
name|UNIX
return|;
case|case
name|WINDOWS
case|:
return|return
name|LineDelimiter
operator|.
name|WINDOWS
return|;
case|case
name|MAC
case|:
return|return
name|LineDelimiter
operator|.
name|MAC
return|;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown textline delimiter: "
operator|+
name|delimiter
argument_list|)
throw|;
block|}
block|}
DECL|method|getEncodingParameter (String type, MinaConfiguration configuration)
specifier|private
specifier|static
name|Charset
name|getEncodingParameter
parameter_list|(
name|String
name|type
parameter_list|,
name|MinaConfiguration
name|configuration
parameter_list|)
block|{
name|String
name|encoding
init|=
name|configuration
operator|.
name|getEncoding
argument_list|()
decl_stmt|;
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
comment|// set in on configuration so its updated
name|configuration
operator|.
name|setEncoding
argument_list|(
name|encoding
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
DECL|method|appendIoFiltersToChain (List<IoFilter> filters, DefaultIoFilterChainBuilder filterChain)
specifier|private
name|void
name|appendIoFiltersToChain
parameter_list|(
name|List
argument_list|<
name|IoFilter
argument_list|>
name|filters
parameter_list|,
name|DefaultIoFilterChainBuilder
name|filterChain
parameter_list|)
block|{
if|if
condition|(
name|filters
operator|!=
literal|null
operator|&&
name|filters
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|IoFilter
name|ioFilter
range|:
name|filters
control|)
block|{
name|filterChain
operator|.
name|addLast
argument_list|(
name|ioFilter
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|,
name|ioFilter
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getConfiguration ()
specifier|public
name|MinaConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (MinaConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|MinaConfiguration
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
block|}
end_class

end_unit

