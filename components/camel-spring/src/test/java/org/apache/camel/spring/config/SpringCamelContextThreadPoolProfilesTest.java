begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.config
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|config
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
name|ThreadPoolExecutor
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
name|spi
operator|.
name|ThreadPoolProfile
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
name|spring
operator|.
name|SpringTestSupport
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
name|concurrent
operator|.
name|ThreadPoolRejectedPolicy
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|SpringCamelContextThreadPoolProfilesTest
specifier|public
class|class
name|SpringCamelContextThreadPoolProfilesTest
extends|extends
name|SpringTestSupport
block|{
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/config/SpringCamelContextThreadPoolProfilesTest.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testLowProfile ()
specifier|public
name|void
name|testLowProfile
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|getMandatoryBean
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|,
literal|"camel-C"
argument_list|)
decl_stmt|;
name|ThreadPoolProfile
name|profile
init|=
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|getThreadPoolProfile
argument_list|(
literal|"low"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
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
literal|5
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
literal|null
argument_list|,
name|profile
operator|.
name|getKeepAliveTime
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|profile
operator|.
name|getMaxQueueSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|profile
operator|.
name|getRejectedPolicy
argument_list|()
argument_list|)
expr_stmt|;
comment|// create a thread pool from low
name|ExecutorService
name|executor
init|=
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newThreadPool
argument_list|(
name|this
argument_list|,
literal|"MyLow"
argument_list|,
literal|"low"
argument_list|)
decl_stmt|;
name|ThreadPoolExecutor
name|tp
init|=
name|assertIsInstanceOf
argument_list|(
name|ThreadPoolExecutor
operator|.
name|class
argument_list|,
name|executor
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tp
operator|.
name|getCorePoolSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|tp
operator|.
name|getMaximumPoolSize
argument_list|()
argument_list|)
expr_stmt|;
comment|// should inherit default options
name|assertEquals
argument_list|(
literal|60
argument_list|,
name|tp
operator|.
name|getKeepAliveTime
argument_list|(
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CallerRuns"
argument_list|,
name|tp
operator|.
name|getRejectedExecutionHandler
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBigProfile ()
specifier|public
name|void
name|testBigProfile
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|getMandatoryBean
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|,
literal|"camel-C"
argument_list|)
decl_stmt|;
name|ThreadPoolProfile
name|profile
init|=
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|getThreadPoolProfile
argument_list|(
literal|"big"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|50
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
literal|100
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
name|ThreadPoolRejectedPolicy
operator|.
name|DiscardOldest
argument_list|,
name|profile
operator|.
name|getRejectedPolicy
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|profile
operator|.
name|getKeepAliveTime
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|profile
operator|.
name|getMaxQueueSize
argument_list|()
argument_list|)
expr_stmt|;
comment|// create a thread pool from big
name|ExecutorService
name|executor
init|=
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newThreadPool
argument_list|(
name|this
argument_list|,
literal|"MyBig"
argument_list|,
literal|"big"
argument_list|)
decl_stmt|;
name|ThreadPoolExecutor
name|tp
init|=
name|assertIsInstanceOf
argument_list|(
name|ThreadPoolExecutor
operator|.
name|class
argument_list|,
name|executor
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|50
argument_list|,
name|tp
operator|.
name|getCorePoolSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|tp
operator|.
name|getMaximumPoolSize
argument_list|()
argument_list|)
expr_stmt|;
comment|// should inherit default options
name|assertEquals
argument_list|(
literal|60
argument_list|,
name|tp
operator|.
name|getKeepAliveTime
argument_list|(
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"DiscardOldest"
argument_list|,
name|tp
operator|.
name|getRejectedExecutionHandler
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

