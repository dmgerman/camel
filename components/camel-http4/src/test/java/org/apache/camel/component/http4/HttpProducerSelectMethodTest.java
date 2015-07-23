begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http4
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
name|component
operator|.
name|http4
operator|.
name|handler
operator|.
name|BasicValidationHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|bootstrap
operator|.
name|HttpServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|bootstrap
operator|.
name|ServerBootstrap
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http4
operator|.
name|HttpMethods
operator|.
name|GET
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http4
operator|.
name|HttpMethods
operator|.
name|POST
import|;
end_import

begin_comment
comment|/**  * Unit test to verify the algorithm for selecting either GET or POST.  *  * @version   */
end_comment

begin_class
DECL|class|HttpProducerSelectMethodTest
specifier|public
class|class
name|HttpProducerSelectMethodTest
extends|extends
name|BaseHttpTest
block|{
DECL|field|localServer
specifier|private
name|HttpServer
name|localServer
decl_stmt|;
annotation|@
name|Before
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|localServer
operator|=
name|ServerBootstrap
operator|.
name|bootstrap
argument_list|()
operator|.
name|setHttpProcessor
argument_list|(
name|getBasicHttpProcessor
argument_list|()
argument_list|)
operator|.
name|setConnectionReuseStrategy
argument_list|(
name|getConnectionReuseStrategy
argument_list|()
argument_list|)
operator|.
name|setResponseFactory
argument_list|(
name|getHttpResponseFactory
argument_list|()
argument_list|)
operator|.
name|setExpectationVerifier
argument_list|(
name|getHttpExpectationVerifier
argument_list|()
argument_list|)
operator|.
name|setSslContext
argument_list|(
name|getSSLContext
argument_list|()
argument_list|)
operator|.
name|registerHandler
argument_list|(
literal|"/myget"
argument_list|,
operator|new
name|BasicValidationHandler
argument_list|(
literal|"GET"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|getExpectedContent
argument_list|()
argument_list|)
argument_list|)
operator|.
name|registerHandler
argument_list|(
literal|"/mypost"
argument_list|,
operator|new
name|BasicValidationHandler
argument_list|(
literal|"POST"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|getExpectedContent
argument_list|()
argument_list|)
argument_list|)
operator|.
name|registerHandler
argument_list|(
literal|"/myget2"
argument_list|,
operator|new
name|BasicValidationHandler
argument_list|(
literal|"GET"
argument_list|,
literal|"q=Camel"
argument_list|,
literal|null
argument_list|,
name|getExpectedContent
argument_list|()
argument_list|)
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|localServer
operator|.
name|start
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|localServer
operator|!=
literal|null
condition|)
block|{
name|localServer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|noDataDefaultIsGet ()
specifier|public
name|void
name|noDataDefaultIsGet
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"http4"
argument_list|,
name|HttpComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|HttpEndpoint
name|endpoiont
init|=
operator|(
name|HttpEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"http4://"
operator|+
name|localServer
operator|.
name|getInetAddress
argument_list|()
operator|.
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|localServer
operator|.
name|getLocalPort
argument_list|()
operator|+
literal|"/myget"
argument_list|)
decl_stmt|;
name|HttpProducer
name|producer
init|=
operator|new
name|HttpProducer
argument_list|(
name|endpoiont
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|dataDefaultIsPost ()
specifier|public
name|void
name|dataDefaultIsPost
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"http4"
argument_list|,
name|HttpComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|HttpEndpoint
name|endpoiont
init|=
operator|(
name|HttpEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"http4://"
operator|+
name|localServer
operator|.
name|getInetAddress
argument_list|()
operator|.
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|localServer
operator|.
name|getLocalPort
argument_list|()
operator|+
literal|"/mypost"
argument_list|)
decl_stmt|;
name|HttpProducer
name|producer
init|=
operator|new
name|HttpProducer
argument_list|(
name|endpoiont
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"This is some data to post"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|withMethodPostInHeader ()
specifier|public
name|void
name|withMethodPostInHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"http4"
argument_list|,
name|HttpComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|HttpEndpoint
name|endpoiont
init|=
operator|(
name|HttpEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"http4://"
operator|+
name|localServer
operator|.
name|getInetAddress
argument_list|()
operator|.
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|localServer
operator|.
name|getLocalPort
argument_list|()
operator|+
literal|"/mypost"
argument_list|)
decl_stmt|;
name|HttpProducer
name|producer
init|=
operator|new
name|HttpProducer
argument_list|(
name|endpoiont
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|POST
argument_list|)
expr_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|withMethodGetInHeader ()
specifier|public
name|void
name|withMethodGetInHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"http4"
argument_list|,
name|HttpComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|HttpEndpoint
name|endpoiont
init|=
operator|(
name|HttpEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"http4://"
operator|+
name|localServer
operator|.
name|getInetAddress
argument_list|()
operator|.
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|localServer
operator|.
name|getLocalPort
argument_list|()
operator|+
literal|"/myget"
argument_list|)
decl_stmt|;
name|HttpProducer
name|producer
init|=
operator|new
name|HttpProducer
argument_list|(
name|endpoiont
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|GET
argument_list|)
expr_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|withMethodCommonHttpGetInHeader ()
specifier|public
name|void
name|withMethodCommonHttpGetInHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"http4"
argument_list|,
name|HttpComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|HttpEndpoint
name|endpoiont
init|=
operator|(
name|HttpEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"http4://"
operator|+
name|localServer
operator|.
name|getInetAddress
argument_list|()
operator|.
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|localServer
operator|.
name|getLocalPort
argument_list|()
operator|+
literal|"/myget"
argument_list|)
decl_stmt|;
name|HttpProducer
name|producer
init|=
operator|new
name|HttpProducer
argument_list|(
name|endpoiont
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|http
operator|.
name|common
operator|.
name|HttpMethods
operator|.
name|GET
argument_list|)
expr_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|withEndpointQuery ()
specifier|public
name|void
name|withEndpointQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"http4"
argument_list|,
name|HttpComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|HttpEndpoint
name|endpoiont
init|=
operator|(
name|HttpEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"http4://"
operator|+
name|localServer
operator|.
name|getInetAddress
argument_list|()
operator|.
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|localServer
operator|.
name|getLocalPort
argument_list|()
operator|+
literal|"/myget2?q=Camel"
argument_list|)
decl_stmt|;
name|HttpProducer
name|producer
init|=
operator|new
name|HttpProducer
argument_list|(
name|endpoiont
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|withQueryInHeader ()
specifier|public
name|void
name|withQueryInHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"http4"
argument_list|,
name|HttpComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|HttpEndpoint
name|endpoiont
init|=
operator|(
name|HttpEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"http4://"
operator|+
name|localServer
operator|.
name|getInetAddress
argument_list|()
operator|.
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|localServer
operator|.
name|getLocalPort
argument_list|()
operator|+
literal|"/myget2"
argument_list|)
decl_stmt|;
name|HttpProducer
name|producer
init|=
operator|new
name|HttpProducer
argument_list|(
name|endpoiont
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|,
literal|"q=Camel"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|withHttpURIInHeader ()
specifier|public
name|void
name|withHttpURIInHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"http4"
argument_list|,
name|HttpComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|HttpEndpoint
name|endpoiont
init|=
operator|(
name|HttpEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"http4://"
operator|+
name|localServer
operator|.
name|getInetAddress
argument_list|()
operator|.
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|localServer
operator|.
name|getLocalPort
argument_list|()
operator|+
literal|"/myget2"
argument_list|)
decl_stmt|;
name|HttpProducer
name|producer
init|=
operator|new
name|HttpProducer
argument_list|(
name|endpoiont
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_URI
argument_list|,
literal|"http://"
operator|+
name|localServer
operator|.
name|getInetAddress
argument_list|()
operator|.
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|localServer
operator|.
name|getLocalPort
argument_list|()
operator|+
literal|"/myget2?q=Camel"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|withQueryInHeaderOverrideEndpoint ()
specifier|public
name|void
name|withQueryInHeaderOverrideEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"http4"
argument_list|,
name|HttpComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|HttpEndpoint
name|endpoiont
init|=
operator|(
name|HttpEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"http4://"
operator|+
name|localServer
operator|.
name|getInetAddress
argument_list|()
operator|.
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|localServer
operator|.
name|getLocalPort
argument_list|()
operator|+
literal|"/myget2?q=Donkey"
argument_list|)
decl_stmt|;
name|HttpProducer
name|producer
init|=
operator|new
name|HttpProducer
argument_list|(
name|endpoiont
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|,
literal|"q=Camel"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

