begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.vm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|vm
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
name|ExchangeTimedOutException
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
name|Processor
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|VmTimeoutIssueTest
specifier|public
class|class
name|VmTimeoutIssueTest
extends|extends
name|AbstractVmTestSupport
block|{
annotation|@
name|Test
DECL|method|testVmTimeoutWithAnotherVm ()
specifier|public
name|void
name|testVmTimeoutWithAnotherVm
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|template2
operator|.
name|requestBody
argument_list|(
literal|"vm:start1?timeout=1000"
argument_list|,
literal|"Hello"
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
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|ExchangeTimedOutException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeTimedOutException
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
literal|500
argument_list|,
name|cause
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testVmTimeoutWithProcessor ()
specifier|public
name|void
name|testVmTimeoutWithProcessor
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|template2
operator|.
name|requestBody
argument_list|(
literal|"vm:start2?timeout=4000"
argument_list|,
literal|"Hello"
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
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|ExchangeTimedOutException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeTimedOutException
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
literal|2000
argument_list|,
name|cause
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
literal|"vm:end"
argument_list|)
operator|.
name|delay
argument_list|(
literal|1000
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilderForSecondContext ()
specifier|protected
name|RouteBuilder
name|createRouteBuilderForSecondContext
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
name|errorHandler
argument_list|(
name|noErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"vm:start1?timeout=1000"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:AFTER_START1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"vm:end?timeout=500"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:AFTER_END"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"vm:start2?timeout=4000"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:AFTER_START2"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// this exception will trigger to stop asap
throw|throw
operator|new
name|ExchangeTimedOutException
argument_list|(
name|exchange
argument_list|,
literal|2000
argument_list|)
throw|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:AFTER_PROCESSOR"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

