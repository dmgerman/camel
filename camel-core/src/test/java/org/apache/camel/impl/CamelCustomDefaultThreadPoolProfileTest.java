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
name|CamelContext
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
name|ThreadPoolRejectedPolicy
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
name|spi
operator|.
name|ThreadPoolProfile
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
DECL|class|CamelCustomDefaultThreadPoolProfileTest
specifier|public
class|class
name|CamelCustomDefaultThreadPoolProfileTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camel
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|ThreadPoolProfile
name|profile
init|=
operator|new
name|ThreadPoolProfile
argument_list|(
literal|"custom"
argument_list|)
decl_stmt|;
name|profile
operator|.
name|setPoolSize
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|profile
operator|.
name|setMaxPoolSize
argument_list|(
literal|15
argument_list|)
expr_stmt|;
name|profile
operator|.
name|setKeepAliveTime
argument_list|(
literal|25L
argument_list|)
expr_stmt|;
name|profile
operator|.
name|setMaxQueueSize
argument_list|(
literal|250
argument_list|)
expr_stmt|;
name|profile
operator|.
name|setAllowCoreThreadTimeOut
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|profile
operator|.
name|setRejectedPolicy
argument_list|(
name|ThreadPoolRejectedPolicy
operator|.
name|Abort
argument_list|)
expr_stmt|;
name|camel
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|setDefaultThreadPoolProfile
argument_list|(
name|profile
argument_list|)
expr_stmt|;
return|return
name|camel
return|;
block|}
annotation|@
name|Test
DECL|method|testCamelCustomDefaultThreadPoolProfile ()
specifier|public
name|void
name|testCamelCustomDefaultThreadPoolProfile
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultExecutorServiceManager
name|manager
init|=
operator|(
name|DefaultExecutorServiceManager
operator|)
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
decl_stmt|;
name|ThreadPoolProfile
name|profile
init|=
name|manager
operator|.
name|getDefaultThreadPoolProfile
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|profile
operator|.
name|getPoolSize
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|15
argument_list|,
name|profile
operator|.
name|getMaxPoolSize
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|25
argument_list|,
name|profile
operator|.
name|getKeepAliveTime
argument_list|()
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|250
argument_list|,
name|profile
operator|.
name|getMaxQueueSize
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|profile
operator|.
name|getAllowCoreThreadTimeOut
argument_list|()
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ThreadPoolRejectedPolicy
operator|.
name|Abort
argument_list|,
name|profile
operator|.
name|getRejectedPolicy
argument_list|()
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
name|threads
argument_list|(
literal|25
argument_list|,
literal|45
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

