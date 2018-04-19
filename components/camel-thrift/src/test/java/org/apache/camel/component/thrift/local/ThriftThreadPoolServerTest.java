begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.thrift.local
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
operator|.
name|local
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
name|InetAddress
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
name|ThriftProducerSecurityTest
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
name|generated
operator|.
name|Calculator
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
name|impl
operator|.
name|CalculatorSyncServerImpl
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
name|test
operator|.
name|AvailablePortFinder
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
name|protocol
operator|.
name|TBinaryProtocol
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
name|protocol
operator|.
name|TProtocol
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
name|TTransport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
comment|/**  * TBD  */
end_comment

begin_class
DECL|class|ThriftThreadPoolServerTest
specifier|public
class|class
name|ThriftThreadPoolServerTest
extends|extends
name|CamelTestSupport
block|{
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
name|ThriftProducerSecurityTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|THRIFT_TEST_PORT
specifier|private
specifier|static
specifier|final
name|int
name|THRIFT_TEST_PORT
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|THRIFT_TEST_NUM1
specifier|private
specifier|static
specifier|final
name|int
name|THRIFT_TEST_NUM1
init|=
literal|12
decl_stmt|;
DECL|field|THRIFT_TEST_NUM2
specifier|private
specifier|static
specifier|final
name|int
name|THRIFT_TEST_NUM2
init|=
literal|13
decl_stmt|;
DECL|field|TRUST_STORE_PATH
specifier|private
specifier|static
specifier|final
name|String
name|TRUST_STORE_PATH
init|=
literal|"src/test/resources/certs/truststore.jks"
decl_stmt|;
DECL|field|KEY_STORE_PATH
specifier|private
specifier|static
specifier|final
name|String
name|KEY_STORE_PATH
init|=
literal|"src/test/resources/certs/keystore.jks"
decl_stmt|;
DECL|field|SECURITY_STORE_PASSWORD
specifier|private
specifier|static
specifier|final
name|String
name|SECURITY_STORE_PASSWORD
init|=
literal|"camelinaction"
decl_stmt|;
DECL|field|THRIFT_CLIENT_TIMEOUT
specifier|private
specifier|static
specifier|final
name|int
name|THRIFT_CLIENT_TIMEOUT
init|=
literal|2000
decl_stmt|;
DECL|field|serverTransport
specifier|private
specifier|static
name|TServerSocket
name|serverTransport
decl_stmt|;
DECL|field|clientTransport
specifier|private
specifier|static
name|TTransport
name|clientTransport
decl_stmt|;
DECL|field|server
specifier|private
specifier|static
name|TServer
name|server
decl_stmt|;
DECL|field|protocol
specifier|private
specifier|static
name|TProtocol
name|protocol
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|}
argument_list|)
DECL|field|processor
specifier|private
specifier|static
name|Calculator
operator|.
name|Processor
name|processor
decl_stmt|;
annotation|@
name|Before
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
DECL|method|startThriftServer ()
specifier|public
name|void
name|startThriftServer
parameter_list|()
throws|throws
name|Exception
block|{
name|processor
operator|=
operator|new
name|Calculator
operator|.
name|Processor
argument_list|(
operator|new
name|CalculatorSyncServerImpl
argument_list|()
argument_list|)
expr_stmt|;
name|TSSLTransportFactory
operator|.
name|TSSLTransportParameters
name|sslParams
init|=
operator|new
name|TSSLTransportFactory
operator|.
name|TSSLTransportParameters
argument_list|()
decl_stmt|;
name|sslParams
operator|.
name|setKeyStore
argument_list|(
name|KEY_STORE_PATH
argument_list|,
name|SECURITY_STORE_PASSWORD
argument_list|)
expr_stmt|;
name|serverTransport
operator|=
name|TSSLTransportFactory
operator|.
name|getServerSocket
argument_list|(
name|THRIFT_TEST_PORT
argument_list|,
name|THRIFT_CLIENT_TIMEOUT
argument_list|,
name|InetAddress
operator|.
name|getByName
argument_list|(
literal|"localhost"
argument_list|)
argument_list|,
name|sslParams
argument_list|)
expr_stmt|;
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
name|serverTransport
argument_list|)
decl_stmt|;
name|args
operator|.
name|processor
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|args
operator|.
name|executorService
argument_list|(
name|this
operator|.
name|context
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newThreadPool
argument_list|(
name|this
argument_list|,
literal|"test-server-invoker"
argument_list|,
literal|1
argument_list|,
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|args
operator|.
name|startThreadPool
argument_list|(
name|this
operator|.
name|context
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadExecutor
argument_list|(
name|this
argument_list|,
literal|"test-start-thread"
argument_list|)
argument_list|)
expr_stmt|;
name|args
operator|.
name|context
argument_list|(
name|this
operator|.
name|context
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
name|server
operator|.
name|serve
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Thrift secured server started on port: {}"
argument_list|,
name|THRIFT_TEST_PORT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|stopThriftServer ()
specifier|public
name|void
name|stopThriftServer
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
name|serverTransport
operator|.
name|close
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Thrift secured server stoped"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|clientConnectionTest ()
specifier|public
name|void
name|clientConnectionTest
parameter_list|()
throws|throws
name|TException
block|{
name|TSSLTransportFactory
operator|.
name|TSSLTransportParameters
name|sslParams
init|=
operator|new
name|TSSLTransportFactory
operator|.
name|TSSLTransportParameters
argument_list|()
decl_stmt|;
name|sslParams
operator|.
name|setTrustStore
argument_list|(
name|TRUST_STORE_PATH
argument_list|,
name|SECURITY_STORE_PASSWORD
argument_list|)
expr_stmt|;
name|clientTransport
operator|=
name|TSSLTransportFactory
operator|.
name|getClientSocket
argument_list|(
literal|"localhost"
argument_list|,
name|THRIFT_TEST_PORT
argument_list|,
literal|1000
argument_list|,
name|sslParams
argument_list|)
expr_stmt|;
name|protocol
operator|=
operator|new
name|TBinaryProtocol
argument_list|(
name|clientTransport
argument_list|)
expr_stmt|;
name|Calculator
operator|.
name|Client
name|client
init|=
operator|new
name|Calculator
operator|.
name|Client
argument_list|(
name|protocol
argument_list|)
decl_stmt|;
name|int
name|addResult
init|=
name|client
operator|.
name|add
argument_list|(
name|THRIFT_TEST_NUM1
argument_list|,
name|THRIFT_TEST_NUM2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|addResult
argument_list|,
name|THRIFT_TEST_NUM1
operator|+
name|THRIFT_TEST_NUM2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

