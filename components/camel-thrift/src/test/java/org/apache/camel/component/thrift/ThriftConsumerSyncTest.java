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
name|transport
operator|.
name|TFramedTransport
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
DECL|class|ThriftConsumerSyncTest
specifier|public
class|class
name|ThriftConsumerSyncTest
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
name|ThriftConsumerSyncTest
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
name|Client
name|thriftClient
decl_stmt|;
DECL|field|protocol
specifier|private
name|TProtocol
name|protocol
decl_stmt|;
DECL|field|transport
specifier|private
name|TTransport
name|transport
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
name|TSocket
argument_list|(
literal|"localhost"
argument_list|,
name|THRIFT_TEST_PORT
argument_list|)
expr_stmt|;
name|transport
operator|.
name|open
argument_list|()
expr_stmt|;
name|protocol
operator|=
operator|new
name|TBinaryProtocol
argument_list|(
operator|new
name|TFramedTransport
argument_list|(
name|transport
argument_list|)
argument_list|)
expr_stmt|;
name|thriftClient
operator|=
operator|(
operator|new
name|Calculator
operator|.
name|Client
operator|.
name|Factory
argument_list|()
operator|)
operator|.
name|getClient
argument_list|(
name|protocol
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
name|int
name|calculateResult
init|=
name|thriftClient
operator|.
name|calculate
argument_list|(
literal|1
argument_list|,
name|work
argument_list|)
decl_stmt|;
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
DECL|method|testEchoMethodInvocation ()
specifier|public
name|void
name|testEchoMethodInvocation
parameter_list|()
throws|throws
name|Exception
block|{
name|Work
name|echoResult
init|=
name|thriftClient
operator|.
name|echo
argument_list|(
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
argument_list|)
decl_stmt|;
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
literal|"/org.apache.camel.component.thrift.generated.Calculator?synchronous=true"
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

