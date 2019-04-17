begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.disruptor.vm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|disruptor
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
name|util
operator|.
name|StopWatch
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
DECL|class|DisruptorVmInOutChainedTimeoutTest
specifier|public
class|class
name|DisruptorVmInOutChainedTimeoutTest
extends|extends
name|AbstractVmTestSupport
block|{
annotation|@
name|Test
DECL|method|testDisruptorVmInOutChainedTimeout ()
specifier|public
name|void
name|testDisruptorVmInOutChainedTimeout
parameter_list|()
throws|throws
name|Exception
block|{
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
try|try
block|{
name|template2
operator|.
name|requestBody
argument_list|(
literal|"disruptor-vm:a?timeout=1000"
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
name|CamelExecutionException
name|e
parameter_list|)
block|{
comment|// the chained vm caused the timeout
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
literal|200
argument_list|,
name|cause
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|long
name|delta
init|=
name|watch
operator|.
name|taken
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should be faster than 1 sec, was: "
operator|+
name|delta
argument_list|,
name|delta
operator|<
literal|1100
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
literal|"disruptor-vm:b"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|delay
argument_list|(
literal|500
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
literal|"disruptor-vm:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
comment|// this timeout will trigger an exception to occur
operator|.
name|to
argument_list|(
literal|"disruptor-vm:b?timeout=200"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a2"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

