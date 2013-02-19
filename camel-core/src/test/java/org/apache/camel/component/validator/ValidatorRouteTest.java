begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|processor
operator|.
name|validation
operator|.
name|NoXmlHeaderValidationException
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

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
name|Override
DECL|method|setUp ()
specifier|protected
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
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

