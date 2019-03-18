begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.validator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|validator
package|;
end_package

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
name|ExecutorService
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
name|Executors
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
name|ContextTestSupport
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
name|ValidationException
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
name|support
operator|.
name|processor
operator|.
name|validation
operator|.
name|NoXmlHeaderValidationException
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

begin_class
DECL|class|ValidatorRouteTest
specifier|public
class|class
name|ValidatorRouteTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|validEndpoint
specifier|protected
name|MockEndpoint
name|validEndpoint
decl_stmt|;
DECL|field|finallyEndpoint
specifier|protected
name|MockEndpoint
name|finallyEndpoint
decl_stmt|;
DECL|field|invalidEndpoint
specifier|protected
name|MockEndpoint
name|invalidEndpoint
decl_stmt|;
annotation|@
name|Test
DECL|method|testValidMessage ()
specifier|public
name|void
name|testValidMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|validEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|finallyEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<mail xmlns='http://foo.com/bar'><subject>Hey</subject><body>Hello world!</body></mail>"
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|validEndpoint
argument_list|,
name|invalidEndpoint
argument_list|,
name|finallyEndpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testValidMessageInHeader ()
specifier|public
name|void
name|testValidMessageInHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|validEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|finallyEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:startHeaders"
argument_list|,
literal|null
argument_list|,
literal|"headerToValidate"
argument_list|,
literal|"<mail xmlns='http://foo.com/bar'><subject>Hey</subject><body>Hello world!</body></mail>"
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|validEndpoint
argument_list|,
name|invalidEndpoint
argument_list|,
name|finallyEndpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvalidMessage ()
specifier|public
name|void
name|testInvalidMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|invalidEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|finallyEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<mail xmlns='http://foo.com/bar'><body>Hello world!</body></mail>"
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|validEndpoint
argument_list|,
name|invalidEndpoint
argument_list|,
name|finallyEndpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvalidMessageInHeader ()
specifier|public
name|void
name|testInvalidMessageInHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|invalidEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|finallyEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:startHeaders"
argument_list|,
literal|null
argument_list|,
literal|"headerToValidate"
argument_list|,
literal|"<mail xmlns='http://foo.com/bar'><body>Hello world!</body></mail>"
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|validEndpoint
argument_list|,
name|invalidEndpoint
argument_list|,
name|finallyEndpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNullHeaderNoFail ()
specifier|public
name|void
name|testNullHeaderNoFail
parameter_list|()
throws|throws
name|Exception
block|{
name|validEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:startNullHeaderNoFail"
argument_list|,
literal|null
argument_list|,
literal|"headerToValidate"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|validEndpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNullHeader ()
specifier|public
name|void
name|testNullHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|validEndpoint
operator|.
name|setExpectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|Exchange
name|in
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"direct:startNoHeaderException"
argument_list|)
operator|.
name|createExchange
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
name|in
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|in
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"headerToValidate"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Exchange
name|out
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:startNoHeaderException"
argument_list|,
name|in
argument_list|)
decl_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|validEndpoint
argument_list|,
name|invalidEndpoint
argument_list|,
name|finallyEndpoint
argument_list|)
expr_stmt|;
name|Exception
name|exception
init|=
name|out
operator|.
name|getException
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should be failed"
argument_list|,
name|out
operator|.
name|isFailed
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Exception should be correct type"
argument_list|,
name|exception
operator|instanceof
name|NoXmlHeaderValidationException
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Exception should mention missing header"
argument_list|,
name|exception
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"headerToValidate"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvalideBytesMessage ()
specifier|public
name|void
name|testInvalideBytesMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|invalidEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|finallyEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<mail xmlns='http://foo.com/bar'><body>Hello world!</body></mail>"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|validEndpoint
argument_list|,
name|invalidEndpoint
argument_list|,
name|finallyEndpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvalidBytesMessageInHeader ()
specifier|public
name|void
name|testInvalidBytesMessageInHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|invalidEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|finallyEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:startHeaders"
argument_list|,
literal|null
argument_list|,
literal|"headerToValidate"
argument_list|,
literal|"<mail xmlns='http://foo.com/bar'><body>Hello world!</body></mail>"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|validEndpoint
argument_list|,
name|invalidEndpoint
argument_list|,
name|finallyEndpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUseNotASharedSchema ()
specifier|public
name|void
name|testUseNotASharedSchema
parameter_list|()
throws|throws
name|Exception
block|{
name|validEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:useNotASharedSchema"
argument_list|,
literal|"<mail xmlns='http://foo.com/bar'><subject>Hey</subject><body>Hello world!</body></mail>"
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|validEndpoint
argument_list|,
name|invalidEndpoint
argument_list|,
name|finallyEndpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConcurrentUseNotASharedSchema ()
specifier|public
name|void
name|testConcurrentUseNotASharedSchema
parameter_list|()
throws|throws
name|Exception
block|{
name|validEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|10
argument_list|)
expr_stmt|;
comment|// latch for the 10 exchanges we expect
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|10
argument_list|)
decl_stmt|;
comment|// setup a task executor to be able send the messages in parallel
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newCachedThreadPool
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|executor
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:useNotASharedSchema"
argument_list|,
literal|"<mail xmlns='http://foo.com/bar'><subject>Hey</subject><body>Hello world!</body></mail>"
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
block|}
try|try
block|{
comment|// wait for test completion, timeout after 30 sec to let other unit test run to not wait forever
name|assertTrue
argument_list|(
name|latch
operator|.
name|await
argument_list|(
literal|30000L
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Latch should be zero"
argument_list|,
literal|0
argument_list|,
name|latch
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|executor
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|validEndpoint
argument_list|,
name|invalidEndpoint
argument_list|,
name|finallyEndpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|validEndpoint
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:valid"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|invalidEndpoint
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:invalid"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|finallyEndpoint
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:finally"
argument_list|,
name|MockEndpoint
operator|.
name|class
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
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|doTry
argument_list|()
operator|.
name|to
argument_list|(
literal|"validator:org/apache/camel/component/validator/schema.xsd"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:valid"
argument_list|)
operator|.
name|doCatch
argument_list|(
name|ValidationException
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:invalid"
argument_list|)
operator|.
name|doFinally
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:finally"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:startHeaders"
argument_list|)
operator|.
name|doTry
argument_list|()
operator|.
name|to
argument_list|(
literal|"validator:org/apache/camel/component/validator/schema.xsd?headerName=headerToValidate"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:valid"
argument_list|)
operator|.
name|doCatch
argument_list|(
name|ValidationException
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:invalid"
argument_list|)
operator|.
name|doFinally
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:finally"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:startNoHeaderException"
argument_list|)
operator|.
name|to
argument_list|(
literal|"validator:org/apache/camel/component/validator/schema.xsd?headerName=headerToValidate"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:valid"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:startNullHeaderNoFail"
argument_list|)
operator|.
name|to
argument_list|(
literal|"validator:org/apache/camel/component/validator/schema.xsd?headerName=headerToValidate&failOnNullHeader=false"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:valid"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:useNotASharedSchema"
argument_list|)
operator|.
name|to
argument_list|(
literal|"validator:org/apache/camel/component/validator/schema.xsd?useSharedSchema=false"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:valid"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

