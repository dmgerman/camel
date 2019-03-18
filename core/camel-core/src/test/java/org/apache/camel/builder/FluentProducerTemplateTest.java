begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Future
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
name|CamelExecutionException
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
name|FluentProducerTemplate
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Unit test for FluentProducerTemplate  */
end_comment

begin_class
DECL|class|FluentProducerTemplateTest
specifier|public
class|class
name|FluentProducerTemplateTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testNoEndpoint ()
specifier|public
name|void
name|testNoEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|FluentProducerTemplate
name|fluent
init|=
name|context
operator|.
name|createFluentProducerTemplate
argument_list|()
decl_stmt|;
try|try
block|{
name|fluent
operator|.
name|withBody
argument_list|(
literal|"Hello World"
argument_list|)
operator|.
name|send
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// expected
block|}
try|try
block|{
name|fluent
operator|.
name|withBody
argument_list|(
literal|"Hello World"
argument_list|)
operator|.
name|request
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
DECL|method|testDefaultEndpoint ()
specifier|public
name|void
name|testDefaultEndpoint
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
name|FluentProducerTemplate
name|fluent
init|=
name|context
operator|.
name|createFluentProducerTemplate
argument_list|()
decl_stmt|;
name|fluent
operator|.
name|setDefaultEndpointUri
argument_list|(
literal|"direct:in"
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|fluent
operator|.
name|withBody
argument_list|(
literal|"Hello World"
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
name|fluent
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFromCamelContext ()
specifier|public
name|void
name|testFromCamelContext
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
name|FluentProducerTemplate
name|fluent
init|=
name|context
operator|.
name|createFluentProducerTemplate
argument_list|()
decl_stmt|;
name|Object
name|result
init|=
name|fluent
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
name|fluent
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
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
name|DefaultFluentProducerTemplate
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
annotation|@
name|Test
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
name|DefaultFluentProducerTemplate
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
annotation|@
name|Test
DECL|method|testInOutWithBodyConversion ()
specifier|public
name|void
name|testInOutWithBodyConversion
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
literal|11
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|DefaultFluentProducerTemplate
operator|.
name|on
argument_list|(
name|context
argument_list|)
operator|.
name|withBodyAs
argument_list|(
literal|"10"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:sum"
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
literal|11
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOutWithBodyConversionFault ()
specifier|public
name|void
name|testInOutWithBodyConversionFault
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
name|DefaultFluentProducerTemplate
operator|.
name|on
argument_list|(
name|context
argument_list|)
operator|.
name|withBodyAs
argument_list|(
literal|"10"
argument_list|,
name|Double
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:sum"
argument_list|)
operator|.
name|request
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
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
literal|"Expected body of type Integer"
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
annotation|@
name|Test
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
name|DefaultFluentProducerTemplate
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
annotation|@
name|Test
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
name|DefaultFluentProducerTemplate
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
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
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
name|DefaultFluentProducerTemplate
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
annotation|@
name|Test
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
name|DefaultFluentProducerTemplate
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
annotation|@
name|Test
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
name|DefaultFluentProducerTemplate
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
annotation|@
name|Test
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
name|DefaultFluentProducerTemplate
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
annotation|@
name|Test
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
name|DefaultFluentProducerTemplate
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
annotation|@
name|Test
DECL|method|testWithExchange ()
specifier|public
name|void
name|testWithExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|ExchangeBuilder
operator|.
name|anExchange
argument_list|(
name|context
argument_list|)
operator|.
name|withBody
argument_list|(
literal|"Hello!"
argument_list|)
operator|.
name|withPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|exchange
operator|=
name|context
operator|.
name|createFluentProducerTemplate
argument_list|()
operator|.
name|withExchange
argument_list|(
name|exchange
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:in"
argument_list|)
operator|.
name|send
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|String
name|out
init|=
name|context
operator|.
name|createFluentProducerTemplate
argument_list|()
operator|.
name|withExchange
argument_list|(
name|exchange
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:in"
argument_list|)
operator|.
name|request
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|fail
argument_list|(
literal|"Should throw exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"withExchange not supported on FluentProducerTemplate.request method. Use send method instead."
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
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
name|DefaultFluentProducerTemplate
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
name|Test
DECL|method|testAsyncRequest ()
specifier|public
name|void
name|testAsyncRequest
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:async"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderValuesReceivedInAnyOrder
argument_list|(
literal|"action"
argument_list|,
literal|"action-1"
argument_list|,
literal|"action-2"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"body-1"
argument_list|,
literal|"body-2"
argument_list|)
expr_stmt|;
name|FluentProducerTemplate
name|fluent
init|=
name|context
operator|.
name|createFluentProducerTemplate
argument_list|()
decl_stmt|;
name|Future
argument_list|<
name|String
argument_list|>
name|future1
init|=
name|fluent
operator|.
name|to
argument_list|(
literal|"direct:async"
argument_list|)
operator|.
name|withHeader
argument_list|(
literal|"action"
argument_list|,
literal|"action-1"
argument_list|)
operator|.
name|withBody
argument_list|(
literal|"body-1"
argument_list|)
operator|.
name|asyncRequest
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Future
argument_list|<
name|String
argument_list|>
name|future2
init|=
name|fluent
operator|.
name|to
argument_list|(
literal|"direct:async"
argument_list|)
operator|.
name|withHeader
argument_list|(
literal|"action"
argument_list|,
literal|"action-2"
argument_list|)
operator|.
name|withBody
argument_list|(
literal|"body-2"
argument_list|)
operator|.
name|asyncRequest
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|result1
init|=
name|future1
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|result2
init|=
name|future2
operator|.
name|get
argument_list|()
decl_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"body-1"
argument_list|,
name|result1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"body-2"
argument_list|,
name|result2
argument_list|)
expr_stmt|;
name|String
name|action
init|=
name|mock
operator|.
name|getExchanges
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
name|getHeader
argument_list|(
literal|"action"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|action
operator|.
name|equals
argument_list|(
literal|"action-1"
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
literal|"body-1"
argument_list|,
name|mock
operator|.
name|getExchanges
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
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|action
operator|.
name|equals
argument_list|(
literal|"action-2"
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
literal|"body-2"
argument_list|,
name|mock
operator|.
name|getExchanges
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
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testAsyncSend ()
specifier|public
name|void
name|testAsyncSend
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:async"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|FluentProducerTemplate
name|fluent
init|=
name|context
operator|.
name|createFluentProducerTemplate
argument_list|()
decl_stmt|;
name|Future
argument_list|<
name|Exchange
argument_list|>
name|future1
init|=
name|fluent
operator|.
name|to
argument_list|(
literal|"direct:async"
argument_list|)
operator|.
name|withHeader
argument_list|(
literal|"action"
argument_list|,
literal|"action-1"
argument_list|)
operator|.
name|withBody
argument_list|(
literal|"body-1"
argument_list|)
operator|.
name|asyncSend
argument_list|()
decl_stmt|;
name|Future
argument_list|<
name|Exchange
argument_list|>
name|future2
init|=
name|fluent
operator|.
name|to
argument_list|(
literal|"direct:async"
argument_list|)
operator|.
name|withHeader
argument_list|(
literal|"action"
argument_list|,
literal|"action-2"
argument_list|)
operator|.
name|withBody
argument_list|(
literal|"body-2"
argument_list|)
operator|.
name|asyncSend
argument_list|()
decl_stmt|;
name|Exchange
name|exchange1
init|=
name|future1
operator|.
name|get
argument_list|()
decl_stmt|;
name|Exchange
name|exchange2
init|=
name|future2
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"action-1"
argument_list|,
name|exchange1
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"action"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"body-1"
argument_list|,
name|exchange1
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"action-2"
argument_list|,
name|exchange2
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"action"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"body-2"
argument_list|,
name|exchange2
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
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
literal|"direct:sum"
argument_list|)
operator|.
name|process
argument_list|(
name|exchange
lambda|->
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|Integer
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
operator|(
name|Integer
operator|)
name|body
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Expected body of type Integer"
argument_list|)
throw|;
block|}
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
name|from
argument_list|(
literal|"direct:async"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:async"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

