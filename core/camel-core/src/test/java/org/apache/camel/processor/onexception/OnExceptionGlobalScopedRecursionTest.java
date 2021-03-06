begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.onexception
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|onexception
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
name|builder
operator|.
name|RouteBuilder
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
comment|/**  * Test that exceptions in an onException handler route do not go into recursion  */
end_comment

begin_class
DECL|class|OnExceptionGlobalScopedRecursionTest
specifier|public
class|class
name|OnExceptionGlobalScopedRecursionTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|testRecursion ()
specifier|public
name|void
name|testRecursion
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
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
name|onException
argument_list|(
name|Throwable
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|log
argument_list|(
literal|"onException"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|NullPointerException
argument_list|(
literal|"A NPE error here"
argument_list|)
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|log
argument_list|(
literal|"test"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalStateException
argument_list|(
literal|"Bad state"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:test"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|NullPointerException
name|npe
init|=
name|assertIsInstanceOf
argument_list|(
name|NullPointerException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"A NPE error here"
argument_list|,
name|npe
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|IllegalStateException
name|ise
init|=
name|assertIsInstanceOf
argument_list|(
name|IllegalStateException
operator|.
name|class
argument_list|,
name|npe
operator|.
name|getSuppressed
argument_list|()
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bad state"
argument_list|,
name|ise
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testRecursionHandled ()
specifier|public
name|void
name|testRecursionHandled
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
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
name|onException
argument_list|(
name|Throwable
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|log
argument_list|(
literal|"onException"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|NullPointerException
argument_list|(
literal|"A NPE error here"
argument_list|)
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|log
argument_list|(
literal|"test"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalStateException
argument_list|(
literal|"Bad state"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:test"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|NullPointerException
name|npe
init|=
name|assertIsInstanceOf
argument_list|(
name|NullPointerException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"A NPE error here"
argument_list|,
name|npe
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|IllegalStateException
name|ise
init|=
name|assertIsInstanceOf
argument_list|(
name|IllegalStateException
operator|.
name|class
argument_list|,
name|npe
operator|.
name|getSuppressed
argument_list|()
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bad state"
argument_list|,
name|ise
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testRecursionDirectNoErrorHandler ()
specifier|public
name|void
name|testRecursionDirectNoErrorHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:d"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
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
name|onException
argument_list|(
name|Throwable
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|log
argument_list|(
literal|"onException"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:error"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|log
argument_list|(
literal|"test"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalStateException
argument_list|(
literal|"Bad state"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
expr_stmt|;
comment|// need to turn off error handler when linked with direct, in
comment|// case you want the same as inlined
name|from
argument_list|(
literal|"direct:error"
argument_list|)
operator|.
name|errorHandler
argument_list|(
name|noErrorHandler
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:d"
argument_list|)
operator|.
name|log
argument_list|(
literal|"error"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|NullPointerException
argument_list|(
literal|"A NPE error here"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:test"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|NullPointerException
name|npe
init|=
name|assertIsInstanceOf
argument_list|(
name|NullPointerException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"A NPE error here"
argument_list|,
name|npe
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|IllegalStateException
name|ise
init|=
name|assertIsInstanceOf
argument_list|(
name|IllegalStateException
operator|.
name|class
argument_list|,
name|npe
operator|.
name|getSuppressed
argument_list|()
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bad state"
argument_list|,
name|ise
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testRecursionHandledDirectNoErrorHandler ()
specifier|public
name|void
name|testRecursionHandledDirectNoErrorHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:d"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
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
name|onException
argument_list|(
name|Throwable
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|log
argument_list|(
literal|"onException"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:error"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|log
argument_list|(
literal|"test"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalStateException
argument_list|(
literal|"Bad state"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
expr_stmt|;
comment|// need to turn off error handler when linked with direct, in
comment|// case you want the same as inlined
name|from
argument_list|(
literal|"direct:error"
argument_list|)
operator|.
name|errorHandler
argument_list|(
name|noErrorHandler
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:d"
argument_list|)
operator|.
name|log
argument_list|(
literal|"error"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|NullPointerException
argument_list|(
literal|"A NPE error here"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:test"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|NullPointerException
name|npe
init|=
name|assertIsInstanceOf
argument_list|(
name|NullPointerException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"A NPE error here"
argument_list|,
name|npe
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|IllegalStateException
name|ise
init|=
name|assertIsInstanceOf
argument_list|(
name|IllegalStateException
operator|.
name|class
argument_list|,
name|npe
operator|.
name|getSuppressed
argument_list|()
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bad state"
argument_list|,
name|ise
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testRecursionDirect ()
specifier|public
name|void
name|testRecursionDirect
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:d"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
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
name|onException
argument_list|(
name|Throwable
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|log
argument_list|(
literal|"onException"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:error"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|log
argument_list|(
literal|"test"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalStateException
argument_list|(
literal|"Bad state"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:error"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:d"
argument_list|)
operator|.
name|log
argument_list|(
literal|"error"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|NullPointerException
argument_list|(
literal|"A NPE error here"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:test"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|NullPointerException
name|npe
init|=
name|assertIsInstanceOf
argument_list|(
name|NullPointerException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"A NPE error here"
argument_list|,
name|npe
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
comment|// we can only see the NPE from the direct route
block|}
block|}
annotation|@
name|Test
DECL|method|testRecursionHandledDirect ()
specifier|public
name|void
name|testRecursionHandledDirect
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:d"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
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
name|onException
argument_list|(
name|Throwable
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|log
argument_list|(
literal|"onException"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:error"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|log
argument_list|(
literal|"test"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalStateException
argument_list|(
literal|"Bad state"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:error"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:d"
argument_list|)
operator|.
name|log
argument_list|(
literal|"error"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|NullPointerException
argument_list|(
literal|"A NPE error here"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:test"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|NullPointerException
name|npe
init|=
name|assertIsInstanceOf
argument_list|(
name|NullPointerException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"A NPE error here"
argument_list|,
name|npe
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
comment|// we can only see the NPE from the direct route
block|}
block|}
block|}
end_class

end_unit

