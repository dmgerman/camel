begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|Ignore
import|;
end_import

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"Test manually by inspecting logs"
argument_list|)
DECL|class|ShutdownStrategyNotSuppressLoggingOnTimeoutTest
specifier|public
class|class
name|ShutdownStrategyNotSuppressLoggingOnTimeoutTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testSuppressLogging ()
specifier|public
name|void
name|testSuppressLogging
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|setTimeout
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|setSuppressLoggingOnTimeout
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
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
literal|"seda:foo"
argument_list|)
operator|.
name|delay
argument_list|(
literal|8000
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:out"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

