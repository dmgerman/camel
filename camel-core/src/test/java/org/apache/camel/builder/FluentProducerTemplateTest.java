begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|RuntimeCamelException
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

begin_comment
comment|/**  * Unit test for DefaultProducerTemplate  */
end_comment

begin_class
DECL|class|FluentProducerTemplateTest
specifier|public
class|class
name|FluentProducerTemplateTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testIn ()
specifier|public
name|void
name|testIn
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|FluentProducerTemplate
operator|.
name|on
argument_list|(
name|context
argument_list|)
operator|.
name|withBody
argument_list|(
literal|"Hello World"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:in"
argument_list|)
operator|.
name|request
argument_list|()
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|template
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testInOut ()
specifier|public
name|void
name|testInOut
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye Bye World"
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|FluentProducerTemplate
operator|.
name|on
argument_list|(
name|context
argument_list|)
operator|.
name|withBody
argument_list|(
literal|"Hello World"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:out"
argument_list|)
operator|.
name|request
argument_list|()
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye Bye World"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|testFault ()
specifier|public
name|void
name|testFault
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|FluentProducerTemplate
operator|.
name|on
argument_list|(
name|context
argument_list|)
operator|.
name|withBody
argument_list|(
literal|"Hello World"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:fault"
argument_list|)
operator|.
name|request
argument_list|()
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Faulty World"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
comment|// TODO: to review
DECL|method|testExceptionUsingBody ()
specifier|public
name|void
name|testExceptionUsingBody
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|Exchange
name|out
init|=
name|FluentProducerTemplate
operator|.
name|on
argument_list|(
name|context
argument_list|)
operator|.
name|withBody
argument_list|(
literal|"Hello World"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:exception"
argument_list|)
operator|.
name|send
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|isFailed
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|getException
argument_list|()
operator|instanceof
name|IllegalArgumentException
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Forced exception by unit test"
argument_list|,
name|out
operator|.
name|getException
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
comment|/*         try {             Exchange out =  FluentProducerTemplate.on(context)                 .withBody("Hello World")                 .to("direct:exception")                 .send();              assertTrue(out.isFailed());             fail("Should have thrown RuntimeCamelException");         } catch (RuntimeCamelException e) {             assertTrue(e.getCause() instanceof IllegalArgumentException);             assertEquals("Forced exception by unit test", e.getCause().getMessage());         }         */
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
comment|// TODO: to review
DECL|method|testExceptionUsingProcessor ()
specifier|public
name|void
name|testExceptionUsingProcessor
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|Exchange
name|out
init|=
name|FluentProducerTemplate
operator|.
name|on
argument_list|(
name|context
argument_list|)
operator|.
name|withProcessor
argument_list|(
name|exchange
lambda|->
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:exception"
argument_list|)
operator|.
name|send
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|isFailed
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Forced exception by unit test"
argument_list|,
name|out
operator|.
name|getException
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testExceptionUsingExchange ()
specifier|public
name|void
name|testExceptionUsingExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|Exchange
name|out
init|=
name|FluentProducerTemplate
operator|.
name|on
argument_list|(
name|context
argument_list|)
operator|.
name|withExchange
argument_list|(
parameter_list|()
lambda|->
block|{
name|Exchange
name|exchange
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:exception"
argument_list|)
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
literal|"Hello World"
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:exception"
argument_list|)
operator|.
name|send
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|isFailed
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Forced exception by unit test"
argument_list|,
name|out
operator|.
name|getException
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testRequestExceptionUsingBody ()
specifier|public
name|void
name|testRequestExceptionUsingBody
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|FluentProducerTemplate
operator|.
name|on
argument_list|(
name|context
argument_list|)
operator|.
name|withBody
argument_list|(
literal|"Hello World"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:exception"
argument_list|)
operator|.
name|request
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown RuntimeCamelException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|IllegalArgumentException
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Forced exception by unit test"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testRequestExceptionUsingProcessor ()
specifier|public
name|void
name|testRequestExceptionUsingProcessor
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|Exchange
name|out
init|=
name|FluentProducerTemplate
operator|.
name|on
argument_list|(
name|context
argument_list|)
operator|.
name|withProcessor
argument_list|(
name|exchange
lambda|->
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:exception"
argument_list|)
operator|.
name|request
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|isFailed
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Forced exception by unit test"
argument_list|,
name|out
operator|.
name|getException
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testRequestExceptionUsingExchange ()
specifier|public
name|void
name|testRequestExceptionUsingExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|Exchange
name|out
init|=
name|FluentProducerTemplate
operator|.
name|on
argument_list|(
name|context
argument_list|)
operator|.
name|withExchange
argument_list|(
parameter_list|()
lambda|->
block|{
name|Exchange
name|exchange
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:exception"
argument_list|)
operator|.
name|createExchange
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:exception"
argument_list|)
operator|.
name|send
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|isFailed
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Forced exception by unit test"
argument_list|,
name|out
operator|.
name|getException
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testRequestBody ()
specifier|public
name|void
name|testRequestBody
parameter_list|()
throws|throws
name|Exception
block|{
comment|// with endpoint as string uri
name|FluentProducerTemplate
name|template
init|=
name|FluentProducerTemplate
operator|.
name|on
argument_list|(
name|context
argument_list|)
decl_stmt|;
specifier|final
name|Integer
name|expectedResult
init|=
operator|new
name|Integer
argument_list|(
literal|123
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedResult
argument_list|,
name|template
operator|.
name|clearBody
argument_list|()
operator|.
name|clearHeaders
argument_list|()
operator|.
name|withBody
argument_list|(
literal|"Hello"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:inout"
argument_list|)
operator|.
name|request
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedResult
argument_list|,
name|template
operator|.
name|clearBody
argument_list|()
operator|.
name|clearHeaders
argument_list|()
operator|.
name|withHeader
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
operator|.
name|withBody
argument_list|(
literal|"Hello"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:inout"
argument_list|)
operator|.
name|request
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedResult
argument_list|,
name|template
operator|.
name|clearBody
argument_list|()
operator|.
name|clearHeaders
argument_list|()
operator|.
name|withBody
argument_list|(
literal|"Hello"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:inout"
argument_list|)
operator|.
name|request
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedResult
argument_list|,
name|template
operator|.
name|clearBody
argument_list|()
operator|.
name|clearHeaders
argument_list|()
operator|.
name|withBody
argument_list|(
literal|"Hello"
argument_list|)
operator|.
name|to
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:inout"
argument_list|)
argument_list|)
operator|.
name|request
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedResult
argument_list|,
name|template
operator|.
name|clearBody
argument_list|()
operator|.
name|clearHeaders
argument_list|()
operator|.
name|withHeader
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
operator|.
name|withBody
argument_list|(
literal|"Hello"
argument_list|)
operator|.
name|to
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:inout"
argument_list|)
argument_list|)
operator|.
name|request
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedResult
argument_list|,
name|template
operator|.
name|clearBody
argument_list|()
operator|.
name|clearHeaders
argument_list|()
operator|.
name|withBody
argument_list|(
literal|"Hello"
argument_list|)
operator|.
name|to
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:inout"
argument_list|)
argument_list|)
operator|.
name|request
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// for faster unit test
name|errorHandler
argument_list|(
name|noErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:in"
argument_list|)
operator|.
name|process
argument_list|(
name|exchange
lambda|->
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Bye World"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:out"
argument_list|)
operator|.
name|process
argument_list|(
name|exchange
lambda|->
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Bye Bye World"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:fault"
argument_list|)
operator|.
name|process
argument_list|(
name|exchange
lambda|->
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setFault
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Faulty World"
argument_list|)
expr_stmt|;
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:exception"
argument_list|)
operator|.
name|process
argument_list|(
name|exchange
lambda|->
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Forced exception by unit test"
argument_list|)
throw|;
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:inout"
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

