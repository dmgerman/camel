begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.thrift
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|thrift
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
name|FailedToCreateRouteException
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
name|dataformat
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
name|dataformat
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
name|spring
operator|.
name|CamelSpringTestSupport
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
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|ThriftMarshalAndUnmarshalSpringTest
specifier|public
class|class
name|ThriftMarshalAndUnmarshalSpringTest
extends|extends
name|CamelSpringTestSupport
block|{
DECL|field|WORK_TEST_COMMENT
specifier|private
specifier|static
specifier|final
name|String
name|WORK_TEST_COMMENT
init|=
literal|"This is a test thrift data"
decl_stmt|;
DECL|field|WORK_TEST_NUM1
specifier|private
specifier|static
specifier|final
name|int
name|WORK_TEST_NUM1
init|=
literal|1
decl_stmt|;
DECL|field|WORK_TEST_NUM2
specifier|private
specifier|static
specifier|final
name|int
name|WORK_TEST_NUM2
init|=
literal|100
decl_stmt|;
DECL|field|WORK_TEST_OPERATION
specifier|private
specifier|static
specifier|final
name|Operation
name|WORK_TEST_OPERATION
init|=
name|Operation
operator|.
name|MULTIPLY
decl_stmt|;
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|ClassPathXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/dataformat/thrift/springDataFormat.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testMarshalAndUnmarshalWithDataFormat ()
specifier|public
name|void
name|testMarshalAndUnmarshalWithDataFormat
parameter_list|()
throws|throws
name|Exception
block|{
name|marshalAndUnmarshal
argument_list|(
literal|"direct:in"
argument_list|,
literal|"direct:back"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshalAndUnmarshalWithDSL1 ()
specifier|public
name|void
name|testMarshalAndUnmarshalWithDSL1
parameter_list|()
throws|throws
name|Exception
block|{
name|marshalAndUnmarshal
argument_list|(
literal|"direct:marshal"
argument_list|,
literal|"direct:unmarshalA"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshalAndUnmarshalWithDSL2 ()
specifier|public
name|void
name|testMarshalAndUnmarshalWithDSL2
parameter_list|()
throws|throws
name|Exception
block|{
name|marshalAndUnmarshal
argument_list|(
literal|"direct:marshal"
argument_list|,
literal|"direct:unmarshalB"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshalAndUnmarshalWithDSL3 ()
specifier|public
name|void
name|testMarshalAndUnmarshalWithDSL3
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:unmarshalC"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|thrift
argument_list|(
operator|new
name|CamelException
argument_list|(
literal|"wrong instance"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:reverse"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expect the exception here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Expect FailedToCreateRouteException"
argument_list|,
name|ex
operator|instanceof
name|FailedToCreateRouteException
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Get a wrong reason"
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|IllegalArgumentException
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|marshalAndUnmarshal (String inURI, String outURI)
specifier|private
name|void
name|marshalAndUnmarshal
parameter_list|(
name|String
name|inURI
parameter_list|,
name|String
name|outURI
parameter_list|)
throws|throws
name|Exception
block|{
name|Work
name|input
init|=
operator|new
name|Work
argument_list|()
decl_stmt|;
name|input
operator|.
name|num1
operator|=
name|WORK_TEST_NUM1
expr_stmt|;
name|input
operator|.
name|num2
operator|=
name|WORK_TEST_NUM2
expr_stmt|;
name|input
operator|.
name|op
operator|=
name|WORK_TEST_OPERATION
expr_stmt|;
name|input
operator|.
name|comment
operator|=
name|WORK_TEST_COMMENT
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:reverse"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|Work
operator|.
name|class
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|Object
name|marshalled
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|inURI
argument_list|,
name|input
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|outURI
argument_list|,
name|marshalled
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Work
name|output
init|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Work
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|WORK_TEST_COMMENT
argument_list|,
name|output
operator|.
name|getComment
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|WORK_TEST_OPERATION
argument_list|,
name|output
operator|.
name|getOp
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|WORK_TEST_NUM2
argument_list|,
name|output
operator|.
name|getNum2
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

