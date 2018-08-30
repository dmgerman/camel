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
name|junit
operator|.
name|Test
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
name|LoggingLevel
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|LoggingErrorHandlerBuilderTest
specifier|public
class|class
name|LoggingErrorHandlerBuilderTest
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
DECL|method|testLoggingErrorHandler ()
specifier|public
name|void
name|testLoggingErrorHandler
parameter_list|()
throws|throws
name|Exception
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
name|LoggingErrorHandlerBuilder
name|eh
init|=
name|loggingErrorHandler
argument_list|()
decl_stmt|;
name|eh
operator|.
name|setLevel
argument_list|(
name|LoggingLevel
operator|.
name|ERROR
argument_list|)
expr_stmt|;
name|eh
operator|.
name|setLog
argument_list|(
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LoggingLevel
operator|.
name|ERROR
argument_list|,
name|eh
operator|.
name|getLevel
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|eh
operator|.
name|getLog
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|eh
operator|.
name|supportTransacted
argument_list|()
argument_list|)
expr_stmt|;
name|errorHandler
argument_list|(
name|eh
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
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
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
DECL|method|testLoggingErrorHandler2 ()
specifier|public
name|void
name|testLoggingErrorHandler2
parameter_list|()
throws|throws
name|Exception
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
name|errorHandler
argument_list|(
name|loggingErrorHandler
argument_list|()
operator|.
name|level
argument_list|(
name|LoggingLevel
operator|.
name|WARN
argument_list|)
operator|.
name|log
argument_list|(
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
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
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
DECL|method|testLoggingErrorHandler3 ()
specifier|public
name|void
name|testLoggingErrorHandler3
parameter_list|()
throws|throws
name|Exception
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
name|LoggingErrorHandlerBuilder
name|eh
init|=
name|loggingErrorHandler
argument_list|(
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
decl_stmt|;
name|eh
operator|.
name|setLevel
argument_list|(
name|LoggingLevel
operator|.
name|ERROR
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LoggingLevel
operator|.
name|ERROR
argument_list|,
name|eh
operator|.
name|getLevel
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|eh
operator|.
name|getLog
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|eh
operator|.
name|supportTransacted
argument_list|()
argument_list|)
expr_stmt|;
name|errorHandler
argument_list|(
name|eh
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
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
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
DECL|method|testLoggingErrorHandler4 ()
specifier|public
name|void
name|testLoggingErrorHandler4
parameter_list|()
throws|throws
name|Exception
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
name|LoggingErrorHandlerBuilder
name|eh
init|=
name|loggingErrorHandler
argument_list|(
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
literal|"foo"
argument_list|)
argument_list|,
name|LoggingLevel
operator|.
name|ERROR
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|LoggingLevel
operator|.
name|ERROR
argument_list|,
name|eh
operator|.
name|getLevel
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|eh
operator|.
name|getLog
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|eh
operator|.
name|supportTransacted
argument_list|()
argument_list|)
expr_stmt|;
name|errorHandler
argument_list|(
name|eh
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
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
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
DECL|method|testLoggingErrorHandler5 ()
specifier|public
name|void
name|testLoggingErrorHandler5
parameter_list|()
throws|throws
name|Exception
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
name|errorHandler
argument_list|(
name|loggingErrorHandler
argument_list|()
operator|.
name|level
argument_list|(
name|LoggingLevel
operator|.
name|ERROR
argument_list|)
operator|.
name|logName
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
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
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
DECL|method|testLoggingErrorHandler6 ()
specifier|public
name|void
name|testLoggingErrorHandler6
parameter_list|()
throws|throws
name|Exception
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
name|errorHandler
argument_list|(
name|loggingErrorHandler
argument_list|()
operator|.
name|level
argument_list|(
name|LoggingLevel
operator|.
name|WARN
argument_list|)
operator|.
name|logName
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"myRoute"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
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
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
block|}
end_class

end_unit

