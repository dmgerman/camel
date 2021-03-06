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
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|CountDownLatch
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
name|TimeUnit
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
name|builder
operator|.
name|RouteBuilder
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
name|mock
operator|.
name|MockEndpoint
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
name|generated
operator|.
name|Operation
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
name|Work
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
name|async
operator|.
name|AsyncMethodCallback
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
name|async
operator|.
name|TAsyncClientManager
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
name|TTransportException
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

begin_class
DECL|class|ThriftConsumerAsyncTest
specifier|public
class|class
name|ThriftConsumerAsyncTest
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
name|ThriftConsumerAsyncTest
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
DECL|field|thriftClient
specifier|private
specifier|static
name|Calculator
operator|.
name|AsyncClient
name|thriftClient
decl_stmt|;
DECL|field|transport
specifier|private
name|TNonblockingTransport
name|transport
decl_stmt|;
DECL|field|calculateResult
specifier|private
name|int
name|calculateResult
decl_stmt|;
DECL|field|zipResult
specifier|private
name|int
name|zipResult
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|pingResult
specifier|private
name|int
name|pingResult
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|allTypesResult
specifier|private
name|int
name|allTypesResult
decl_stmt|;
DECL|field|echoResult
specifier|private
name|Work
name|echoResult
decl_stmt|;
annotation|@
name|Before
DECL|method|startThriftClient ()
specifier|public
name|void
name|startThriftClient
parameter_list|()
throws|throws
name|IOException
throws|,
name|TTransportException
block|{
if|if
condition|(
name|transport
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Connecting to the Thrift server on port: {}"
argument_list|,
name|THRIFT_TEST_PORT
argument_list|)
expr_stmt|;
name|transport
operator|=
operator|new
name|TNonblockingSocket
argument_list|(
literal|"localhost"
argument_list|,
name|THRIFT_TEST_PORT
argument_list|)
expr_stmt|;
name|thriftClient
operator|=
operator|(
operator|new
name|Calculator
operator|.
name|AsyncClient
operator|.
name|Factory
argument_list|(
operator|new
name|TAsyncClientManager
argument_list|()
argument_list|,
operator|new
name|TBinaryProtocol
operator|.
name|Factory
argument_list|()
argument_list|)
operator|)
operator|.
name|getAsyncClient
argument_list|(
name|transport
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|After
DECL|method|stopThriftClient ()
specifier|public
name|void
name|stopThriftClient
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|transport
operator|!=
literal|null
condition|)
block|{
name|transport
operator|.
name|close
argument_list|()
expr_stmt|;
name|transport
operator|=
literal|null
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Connection to the Thrift server closed"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testCalculateMethodInvocation ()
specifier|public
name|void
name|testCalculateMethodInvocation
parameter_list|()
throws|throws
name|Exception
block|{
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Work
name|work
init|=
operator|new
name|Work
argument_list|(
name|THRIFT_TEST_NUM1
argument_list|,
name|THRIFT_TEST_NUM2
argument_list|,
name|Operation
operator|.
name|MULTIPLY
argument_list|)
decl_stmt|;
name|thriftClient
operator|.
name|calculate
argument_list|(
literal|1
argument_list|,
name|work
argument_list|,
operator|new
name|AsyncMethodCallback
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|(
name|Integer
name|response
parameter_list|)
block|{
name|calculateResult
operator|=
name|response
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onError
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Exception"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:thrift-service"
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|expectedHeaderValuesReceivedInAnyOrder
argument_list|(
name|ThriftConstants
operator|.
name|THRIFT_METHOD_NAME_HEADER
argument_list|,
literal|"calculate"
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|THRIFT_TEST_NUM1
operator|*
name|THRIFT_TEST_NUM2
argument_list|,
name|calculateResult
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testVoidMethodInvocation ()
specifier|public
name|void
name|testVoidMethodInvocation
parameter_list|()
throws|throws
name|Exception
block|{
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|thriftClient
operator|.
name|ping
argument_list|(
operator|new
name|AsyncMethodCallback
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|(
name|Void
name|response
parameter_list|)
block|{
name|pingResult
operator|=
literal|0
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onError
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Exception"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:thrift-service"
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|expectedHeaderValuesReceivedInAnyOrder
argument_list|(
name|ThriftConstants
operator|.
name|THRIFT_METHOD_NAME_HEADER
argument_list|,
literal|"ping"
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|pingResult
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOneWayMethodInvocation ()
specifier|public
name|void
name|testOneWayMethodInvocation
parameter_list|()
throws|throws
name|Exception
block|{
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|thriftClient
operator|.
name|zip
argument_list|(
operator|new
name|AsyncMethodCallback
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|(
name|Void
name|response
parameter_list|)
block|{
name|zipResult
operator|=
literal|0
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onError
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Exception"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:thrift-service"
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|expectedHeaderValuesReceivedInAnyOrder
argument_list|(
name|ThriftConstants
operator|.
name|THRIFT_METHOD_NAME_HEADER
argument_list|,
literal|"zip"
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|zipResult
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAllTypesMethodInvocation ()
specifier|public
name|void
name|testAllTypesMethodInvocation
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Thrift method with all possile types async test start"
argument_list|)
expr_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|thriftClient
operator|.
name|alltypes
argument_list|(
literal|true
argument_list|,
operator|(
name|byte
operator|)
name|THRIFT_TEST_NUM1
argument_list|,
operator|(
name|short
operator|)
name|THRIFT_TEST_NUM1
argument_list|,
name|THRIFT_TEST_NUM1
argument_list|,
name|THRIFT_TEST_NUM1
argument_list|,
name|THRIFT_TEST_NUM1
argument_list|,
literal|"empty"
argument_list|,
name|ByteBuffer
operator|.
name|allocate
argument_list|(
literal|10
argument_list|)
argument_list|,
operator|new
name|Work
argument_list|(
name|THRIFT_TEST_NUM1
argument_list|,
name|THRIFT_TEST_NUM2
argument_list|,
name|Operation
operator|.
name|MULTIPLY
argument_list|)
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|()
argument_list|,
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
argument_list|,
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
argument_list|()
argument_list|,
operator|new
name|AsyncMethodCallback
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|(
name|Integer
name|response
parameter_list|)
block|{
name|allTypesResult
operator|=
name|response
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onError
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Exception"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:thrift-service"
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|expectedHeaderValuesReceivedInAnyOrder
argument_list|(
name|ThriftConstants
operator|.
name|THRIFT_METHOD_NAME_HEADER
argument_list|,
literal|"alltypes"
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|THRIFT_TEST_NUM1
argument_list|,
name|allTypesResult
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEchoMethodInvocation ()
specifier|public
name|void
name|testEchoMethodInvocation
parameter_list|()
throws|throws
name|Exception
block|{
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Work
name|work
init|=
operator|new
name|Work
argument_list|(
name|THRIFT_TEST_NUM1
argument_list|,
name|THRIFT_TEST_NUM2
argument_list|,
name|Operation
operator|.
name|MULTIPLY
argument_list|)
decl_stmt|;
name|thriftClient
operator|.
name|echo
argument_list|(
name|work
argument_list|,
operator|new
name|AsyncMethodCallback
argument_list|<
name|Work
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|(
name|Work
name|response
parameter_list|)
block|{
name|echoResult
operator|=
name|response
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onError
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Exception"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:thrift-service"
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|expectedHeaderValuesReceivedInAnyOrder
argument_list|(
name|ThriftConstants
operator|.
name|THRIFT_METHOD_NAME_HEADER
argument_list|,
literal|"echo"
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|echoResult
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|echoResult
operator|instanceof
name|Work
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|THRIFT_TEST_NUM1
argument_list|,
name|echoResult
operator|.
name|num1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Operation
operator|.
name|MULTIPLY
argument_list|,
name|echoResult
operator|.
name|op
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"thrift://localhost:"
operator|+
name|THRIFT_TEST_PORT
operator|+
literal|"/org.apache.camel.component.thrift.generated.Calculator"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:thrift-service"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
name|ThriftConstants
operator|.
name|THRIFT_METHOD_NAME_HEADER
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"calculate"
argument_list|)
argument_list|)
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
operator|new
name|Integer
argument_list|(
name|THRIFT_TEST_NUM1
operator|*
name|THRIFT_TEST_NUM2
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
name|ThriftConstants
operator|.
name|THRIFT_METHOD_NAME_HEADER
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"ping"
argument_list|)
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
name|ThriftConstants
operator|.
name|THRIFT_METHOD_NAME_HEADER
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"zip"
argument_list|)
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
name|ThriftConstants
operator|.
name|THRIFT_METHOD_NAME_HEADER
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"alltypes"
argument_list|)
argument_list|)
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
operator|new
name|Integer
argument_list|(
name|THRIFT_TEST_NUM1
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
name|ThriftConstants
operator|.
name|THRIFT_METHOD_NAME_HEADER
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"echo"
argument_list|)
argument_list|)
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
literal|"${body[0]}"
argument_list|)
argument_list|)
operator|.
name|bean
argument_list|(
operator|new
name|CalculatorMessageBuilder
argument_list|()
argument_list|,
literal|"echo"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|CalculatorMessageBuilder
specifier|public
class|class
name|CalculatorMessageBuilder
block|{
DECL|method|echo (Work work)
specifier|public
name|Work
name|echo
parameter_list|(
name|Work
name|work
parameter_list|)
block|{
return|return
name|work
operator|.
name|deepCopy
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

