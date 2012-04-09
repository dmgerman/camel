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
name|concurrent
operator|.
name|ExecutorService
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
name|CamelException
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
name|impl
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
name|util
operator|.
name|ExchangeHelper
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
name|IOHelper
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
name|core
operator|.
name|service
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
name|core
operator|.
name|service
operator|.
name|IoHandlerAdapter
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
name|service
operator|.
name|IoService
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
name|session
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
name|filter
operator|.
name|executor
operator|.
name|UnorderedThreadPoolExecutor
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
name|logging
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
name|ssl
operator|.
name|SslFilter
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
name|NioDatagramAcceptor
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
name|NioProcessor
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
name|NioSocketAcceptor
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
comment|/**  * A {@link org.apache.camel.Consumer Consumer} implementation for Apache MINA.  *  * @version   */
end_comment

begin_class
DECL|class|Mina2Consumer
specifier|public
class|class
name|Mina2Consumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Mina2Consumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|address
specifier|private
name|SocketAddress
name|address
decl_stmt|;
DECL|field|acceptor
specifier|private
name|IoAcceptor
name|acceptor
decl_stmt|;
DECL|field|configuration
specifier|private
name|Mina2Configuration
name|configuration
decl_stmt|;
DECL|field|workerPool
specifier|private
name|ExecutorService
name|workerPool
decl_stmt|;
DECL|method|Mina2Consumer (final Mina2Endpoint endpoint, Processor processor)
specifier|public
name|Mina2Consumer
parameter_list|(
specifier|final
name|Mina2Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
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
name|configuration
operator|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
comment|//
comment|// All mina2 endpoints are InOut. The endpoints are asynchronous.
comment|// Endpoints can send "n" messages and receive "m" messages.
comment|//
name|this
operator|.
name|getEndpoint
argument_list|()
operator|.
name|setExchangePattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|String
name|protocol
init|=
name|configuration
operator|.
name|getProtocol
argument_list|()
decl_stmt|;
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
name|setupSocketProtocol
argument_list|(
name|protocol
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|configuration
operator|.
name|isDatagramProtocol
argument_list|()
condition|)
block|{
name|setupDatagramProtocol
argument_list|(
name|protocol
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
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
name|setupVmProtocol
argument_list|(
name|protocol
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
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
name|acceptor
operator|.
name|setHandler
argument_list|(
operator|new
name|ReceiveHandler
argument_list|()
argument_list|)
expr_stmt|;
name|acceptor
operator|.
name|bind
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Bound to server address: {} using acceptor: {}"
argument_list|,
name|address
argument_list|,
name|acceptor
argument_list|)
expr_stmt|;
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Unbinding from server address: {} using acceptor: {}"
argument_list|,
name|address
argument_list|,
name|acceptor
argument_list|)
expr_stmt|;
name|acceptor
operator|.
name|unbind
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|workerPool
operator|!=
literal|null
condition|)
block|{
name|workerPool
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doShutdown
argument_list|()
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|setupVmProtocol (String uri, Mina2Configuration configuration)
specifier|protected
name|void
name|setupVmProtocol
parameter_list|(
name|String
name|uri
parameter_list|,
name|Mina2Configuration
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
name|address
operator|=
operator|new
name|VmPipeAddress
argument_list|(
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|acceptor
operator|=
operator|new
name|VmPipeAcceptor
argument_list|()
expr_stmt|;
comment|// acceptor connectorConfig
name|configureCodecFactory
argument_list|(
literal|"Mina2Consumer"
argument_list|,
name|acceptor
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
if|if
condition|(
name|configuration
operator|.
name|getSslContextParameters
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Using vm protocol"
operator|+
literal|", but an SSLContextParameters instance was provided.  SSLContextParameters is only supported on the TCP protocol."
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setupSocketProtocol (String uri, Mina2Configuration configuration)
specifier|protected
name|void
name|setupSocketProtocol
parameter_list|(
name|String
name|uri
parameter_list|,
name|Mina2Configuration
name|configuration
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"createSocketEndpoint"
argument_list|)
expr_stmt|;
name|boolean
name|minaLogger
init|=
name|configuration
operator|.
name|isMinaLogger
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
name|address
operator|=
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
expr_stmt|;
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
name|acceptor
operator|=
operator|new
name|NioSocketAcceptor
argument_list|(
name|processorCount
argument_list|)
expr_stmt|;
comment|// acceptor connectorConfig
name|configureCodecFactory
argument_list|(
literal|"Mina2Consumer"
argument_list|,
name|acceptor
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
operator|(
operator|(
name|NioSocketAcceptor
operator|)
name|acceptor
operator|)
operator|.
name|setReuseAddress
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|acceptor
operator|.
name|setCloseOnDeactivation
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// using the unordered thread pool is fine as we dont need ordered invocation in our response handler
name|workerPool
operator|=
operator|new
name|UnorderedThreadPoolExecutor
argument_list|(
name|configuration
operator|.
name|getMaximumPoolSize
argument_list|()
argument_list|)
expr_stmt|;
name|acceptor
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
name|workerPool
argument_list|)
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
if|if
condition|(
name|configuration
operator|.
name|getSslContextParameters
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|SslFilter
name|filter
init|=
operator|new
name|SslFilter
argument_list|(
name|configuration
operator|.
name|getSslContextParameters
argument_list|()
operator|.
name|createSSLContext
argument_list|()
argument_list|,
name|configuration
operator|.
name|isAutoStartTls
argument_list|()
argument_list|)
decl_stmt|;
name|filter
operator|.
name|setUseClientMode
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|acceptor
operator|.
name|getFilterChain
argument_list|()
operator|.
name|addFirst
argument_list|(
literal|"sslFilter"
argument_list|,
name|filter
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|configureCodecFactory (String type, IoService service, Mina2Configuration configuration)
specifier|protected
name|void
name|configureCodecFactory
parameter_list|(
name|String
name|type
parameter_list|,
name|IoService
name|service
parameter_list|,
name|Mina2Configuration
name|configuration
parameter_list|)
block|{
if|if
condition|(
name|configuration
operator|.
name|getCodec
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|addCodecFactory
argument_list|(
name|service
argument_list|,
name|configuration
operator|.
name|getCodec
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|configuration
operator|.
name|isAllowDefaultCodec
argument_list|()
condition|)
block|{
name|configureDefaultCodecFactory
argument_list|(
name|type
argument_list|,
name|service
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|configureDefaultCodecFactory (String type, IoService service, Mina2Configuration configuration)
specifier|protected
name|void
name|configureDefaultCodecFactory
parameter_list|(
name|String
name|type
parameter_list|,
name|IoService
name|service
parameter_list|,
name|Mina2Configuration
name|configuration
parameter_list|)
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
name|Mina2TextLineCodecFactory
name|codecFactory
init|=
operator|new
name|Mina2TextLineCodecFactory
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
name|codecFactory
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
name|codecFactory
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
name|addCodecFactory
argument_list|(
name|service
argument_list|,
name|codecFactory
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
literal|"{}: Using TextLineCodecFactory: {} using encoding: {} line delimiter: {}({})"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|type
block|,
name|codecFactory
block|,
name|charset
block|,
name|configuration
operator|.
name|getTextlineDelimiter
argument_list|()
block|,
name|delimiter
block|}
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Encoder maximum line length: {}. Decoder maximum line length: {}"
argument_list|,
name|codecFactory
operator|.
name|getEncoderMaxLineLength
argument_list|()
argument_list|,
name|codecFactory
operator|.
name|getDecoderMaxLineLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|ObjectSerializationCodecFactory
name|codecFactory
init|=
operator|new
name|ObjectSerializationCodecFactory
argument_list|()
decl_stmt|;
name|addCodecFactory
argument_list|(
name|service
argument_list|,
name|codecFactory
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"{}: Using ObjectSerializationCodecFactory: {}"
argument_list|,
name|type
argument_list|,
name|codecFactory
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setupDatagramProtocol (String uri, Mina2Configuration configuration)
specifier|protected
name|void
name|setupDatagramProtocol
parameter_list|(
name|String
name|uri
parameter_list|,
name|Mina2Configuration
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
name|address
operator|=
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
expr_stmt|;
name|acceptor
operator|=
operator|new
name|NioDatagramAcceptor
argument_list|()
expr_stmt|;
comment|// acceptor connectorConfig
name|configureDataGramCodecFactory
argument_list|(
literal|"MinaConsumer"
argument_list|,
name|acceptor
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
name|acceptor
operator|.
name|setCloseOnDeactivation
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
if|if
condition|(
name|configuration
operator|.
name|getSslContextParameters
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Using datagram protocol, "
operator|+
name|configuration
operator|.
name|getProtocol
argument_list|()
operator|+
literal|", but an SSLContextParameters instance was provided.  SSLContextParameters is only supported on the TCP protocol."
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * For datagrams the entire message is available as a single IoBuffer so lets just pass those around by default      * and try converting whatever they payload is into IoBuffer unless some custom converter is specified      */
DECL|method|configureDataGramCodecFactory (final String type, final IoService service, final Mina2Configuration configuration)
specifier|protected
name|void
name|configureDataGramCodecFactory
parameter_list|(
specifier|final
name|String
name|type
parameter_list|,
specifier|final
name|IoService
name|service
parameter_list|,
specifier|final
name|Mina2Configuration
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
name|Mina2UdpProtocolCodecFactory
argument_list|(
name|this
operator|.
name|getEndpoint
argument_list|()
operator|.
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
literal|"{}: Using CodecFactory: {} using encoding: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|type
block|,
name|codecFactory
block|,
name|charset
block|}
argument_list|)
expr_stmt|;
block|}
block|}
name|addCodecFactory
argument_list|(
name|service
argument_list|,
name|codecFactory
argument_list|)
expr_stmt|;
block|}
DECL|method|addCodecFactory (IoService service, ProtocolCodecFactory codecFactory)
specifier|private
name|void
name|addCodecFactory
parameter_list|(
name|IoService
name|service
parameter_list|,
name|ProtocolCodecFactory
name|codecFactory
parameter_list|)
block|{
name|service
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
DECL|method|getLineDelimiterParameter (Mina2TextLineDelimiter delimiter)
specifier|private
specifier|static
name|LineDelimiter
name|getLineDelimiterParameter
parameter_list|(
name|Mina2TextLineDelimiter
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
DECL|method|getEncodingParameter (String type, Mina2Configuration configuration)
specifier|private
name|Charset
name|getEncodingParameter
parameter_list|(
name|String
name|type
parameter_list|,
name|Mina2Configuration
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"{}: No encoding parameter using default charset: {}"
argument_list|,
name|type
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
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
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|Mina2Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|Mina2Endpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|getAcceptor ()
specifier|public
name|IoAcceptor
name|getAcceptor
parameter_list|()
block|{
return|return
name|acceptor
return|;
block|}
DECL|method|setAcceptor (IoAcceptor acceptor)
specifier|public
name|void
name|setAcceptor
parameter_list|(
name|IoAcceptor
name|acceptor
parameter_list|)
block|{
name|this
operator|.
name|acceptor
operator|=
name|acceptor
expr_stmt|;
block|}
comment|/**      * Handles consuming messages and replying if the exchange is out capable.      */
DECL|class|ReceiveHandler
specifier|private
specifier|final
class|class
name|ReceiveHandler
extends|extends
name|IoHandlerAdapter
block|{
annotation|@
name|Override
DECL|method|exceptionCaught (IoSession session, Throwable cause)
specifier|public
name|void
name|exceptionCaught
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|Throwable
name|cause
parameter_list|)
throws|throws
name|Exception
block|{
comment|// close invalid session
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Closing session as an exception was thrown from MINA"
argument_list|)
expr_stmt|;
name|session
operator|.
name|close
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|// must wrap and rethrow since cause can be of Throwable and we must only throw Exception
throw|throw
operator|new
name|CamelException
argument_list|(
name|cause
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|messageReceived (IoSession session, Object object)
specifier|public
name|void
name|messageReceived
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|Object
name|object
parameter_list|)
throws|throws
name|Exception
block|{
comment|// log what we received
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|Object
name|in
init|=
name|object
decl_stmt|;
if|if
condition|(
name|in
operator|instanceof
name|byte
index|[]
condition|)
block|{
comment|// byte arrays is not readable so convert to string
name|in
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|in
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received body: {}"
argument_list|,
name|in
argument_list|)
expr_stmt|;
block|}
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|session
argument_list|,
name|object
argument_list|)
decl_stmt|;
comment|//Set the exchange charset property for converting
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getCharsetName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|IOHelper
operator|.
name|normalizeCharset
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getCharsetName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|//
comment|// If there's a response to send, send it.
comment|//
name|boolean
name|disconnect
init|=
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isDisconnect
argument_list|()
decl_stmt|;
name|Object
name|response
init|=
literal|null
decl_stmt|;
name|response
operator|=
name|Mina2PayloadHelper
operator|.
name|getOut
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|boolean
name|failed
init|=
name|exchange
operator|.
name|isFailed
argument_list|()
decl_stmt|;
if|if
condition|(
name|failed
operator|&&
operator|!
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isTransferExchange
argument_list|()
condition|)
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
name|response
operator|=
name|exchange
operator|.
name|getException
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// failed and no exception, must be a fault
name|response
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|response
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Writing body: {}"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|Mina2Helper
operator|.
name|writeBody
argument_list|(
name|session
argument_list|,
name|response
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Writing no response"
argument_list|)
expr_stmt|;
name|disconnect
operator|=
name|Boolean
operator|.
name|TRUE
expr_stmt|;
block|}
comment|// should session be closed after complete?
name|Boolean
name|close
decl_stmt|;
if|if
condition|(
name|ExchangeHelper
operator|.
name|isOutCapable
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|close
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Mina2Constants
operator|.
name|MINA_CLOSE_SESSION_WHEN_COMPLETE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|close
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Mina2Constants
operator|.
name|MINA_CLOSE_SESSION_WHEN_COMPLETE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|// should we disconnect, the header can override the configuration
if|if
condition|(
name|close
operator|!=
literal|null
condition|)
block|{
name|disconnect
operator|=
name|close
expr_stmt|;
block|}
if|if
condition|(
name|disconnect
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Closing session when complete at address: {}"
argument_list|,
name|address
argument_list|)
expr_stmt|;
name|session
operator|.
name|close
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

