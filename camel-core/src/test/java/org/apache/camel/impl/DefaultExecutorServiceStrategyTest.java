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

begin_comment
comment|/**  * Unit test to ensure the {@link org.apache.camel.spi.ExecutorServiceStrategy} still  * works to keep backwards compatibility.  *  * @version   */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|class|DefaultExecutorServiceStrategyTest
specifier|public
class|class
name|DefaultExecutorServiceStrategyTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testGetThreadNameDefaultPattern ()
specifier|public
name|void
name|testGetThreadNameDefaultPattern
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|foo
init|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadName
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|String
name|bar
init|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadName
argument_list|(
literal|"bar"
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|foo
argument_list|,
name|bar
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|foo
operator|.
name|startsWith
argument_list|(
literal|"Camel ("
operator|+
name|context
operator|.
name|getName
argument_list|()
operator|+
literal|") thread "
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|foo
operator|.
name|endsWith
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bar
operator|.
name|startsWith
argument_list|(
literal|"Camel ("
operator|+
name|context
operator|.
name|getName
argument_list|()
operator|+
literal|") thread "
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bar
operator|.
name|endsWith
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetThreadNameCustomPattern ()
specifier|public
name|void
name|testGetThreadNameCustomPattern
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|setThreadNamePattern
argument_list|(
literal|"#${counter} - ${name}"
argument_list|)
expr_stmt|;
name|String
name|foo
init|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadName
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|String
name|bar
init|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadName
argument_list|(
literal|"bar"
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|foo
argument_list|,
name|bar
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|foo
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|foo
operator|.
name|endsWith
argument_list|(
literal|" - foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bar
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bar
operator|.
name|endsWith
argument_list|(
literal|" - bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetThreadNameCustomPatternCamelId ()
specifier|public
name|void
name|testGetThreadNameCustomPatternCamelId
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|setThreadNamePattern
argument_list|(
literal|"#${camelId} - #${counter} - ${name}"
argument_list|)
expr_stmt|;
name|String
name|foo
init|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadName
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|String
name|bar
init|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadName
argument_list|(
literal|"bar"
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|foo
argument_list|,
name|bar
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|foo
operator|.
name|startsWith
argument_list|(
literal|"#"
operator|+
name|context
operator|.
name|getName
argument_list|()
operator|+
literal|" - #"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|foo
operator|.
name|endsWith
argument_list|(
literal|" - foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bar
operator|.
name|startsWith
argument_list|(
literal|"#"
operator|+
name|context
operator|.
name|getName
argument_list|()
operator|+
literal|" - #"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bar
operator|.
name|endsWith
argument_list|(
literal|" - bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetThreadNameCustomPatternWithDollar ()
specifier|public
name|void
name|testGetThreadNameCustomPatternWithDollar
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|setThreadNamePattern
argument_list|(
literal|"Hello - ${name}"
argument_list|)
expr_stmt|;
name|String
name|foo
init|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadName
argument_list|(
literal|"foo$bar"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello - foo$bar"
argument_list|,
name|foo
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetThreadNameCustomPatternLongName ()
specifier|public
name|void
name|testGetThreadNameCustomPatternLongName
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|setThreadNamePattern
argument_list|(
literal|"#${counter} - ${longName}"
argument_list|)
expr_stmt|;
name|String
name|foo
init|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadName
argument_list|(
literal|"foo?beer=Carlsberg"
argument_list|)
decl_stmt|;
name|String
name|bar
init|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadName
argument_list|(
literal|"bar"
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|foo
argument_list|,
name|bar
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|foo
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|foo
operator|.
name|endsWith
argument_list|(
literal|" - foo?beer=Carlsberg"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bar
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bar
operator|.
name|endsWith
argument_list|(
literal|" - bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetThreadNameCustomPatternWithParameters ()
specifier|public
name|void
name|testGetThreadNameCustomPatternWithParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|setThreadNamePattern
argument_list|(
literal|"#${counter} - ${name}"
argument_list|)
expr_stmt|;
name|String
name|foo
init|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadName
argument_list|(
literal|"foo?beer=Carlsberg"
argument_list|)
decl_stmt|;
name|String
name|bar
init|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadName
argument_list|(
literal|"bar"
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|foo
argument_list|,
name|bar
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|foo
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|foo
operator|.
name|endsWith
argument_list|(
literal|" - foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bar
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bar
operator|.
name|endsWith
argument_list|(
literal|" - bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetThreadNameCustomPatternNoCounter ()
specifier|public
name|void
name|testGetThreadNameCustomPatternNoCounter
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|setThreadNamePattern
argument_list|(
literal|"Cool ${name}"
argument_list|)
expr_stmt|;
name|String
name|foo
init|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadName
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|String
name|bar
init|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadName
argument_list|(
literal|"bar"
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|foo
argument_list|,
name|bar
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Cool foo"
argument_list|,
name|foo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Cool bar"
argument_list|,
name|bar
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetThreadNameCustomPatternInvalid ()
specifier|public
name|void
name|testGetThreadNameCustomPatternInvalid
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|setThreadNamePattern
argument_list|(
literal|"Cool ${xxx}"
argument_list|)
expr_stmt|;
try|try
block|{
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadName
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should thrown an exception"
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
literal|"Pattern is invalid: Cool ${xxx}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// reset it so we can shutdown properly
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|setThreadNamePattern
argument_list|(
literal|"Camel Thread ${counter} - ${name}"
argument_list|)
expr_stmt|;
block|}
DECL|method|testDefaultThreadPool ()
specifier|public
name|void
name|testDefaultThreadPool
parameter_list|()
throws|throws
name|Exception
block|{
name|ExecutorService
name|myPool
init|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|newDefaultThreadPool
argument_list|(
name|this
argument_list|,
literal|"myPool"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|myPool
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
comment|// should use default settings
name|ThreadPoolExecutor
name|executor
init|=
operator|(
name|ThreadPoolExecutor
operator|)
name|myPool
decl_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|executor
operator|.
name|getCorePoolSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|executor
operator|.
name|getMaximumPoolSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|60
argument_list|,
name|executor
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
literal|1000
argument_list|,
name|executor
operator|.
name|getQueue
argument_list|()
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|myPool
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testDefaultUnboundedQueueThreadPool ()
specifier|public
name|void
name|testDefaultUnboundedQueueThreadPool
parameter_list|()
throws|throws
name|Exception
block|{
name|ThreadPoolProfileSupport
name|custom
init|=
operator|new
name|ThreadPoolProfileSupport
argument_list|(
literal|"custom"
argument_list|)
decl_stmt|;
name|custom
operator|.
name|setPoolSize
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|custom
operator|.
name|setMaxPoolSize
argument_list|(
literal|30
argument_list|)
expr_stmt|;
name|custom
operator|.
name|setKeepAliveTime
argument_list|(
literal|50L
argument_list|)
expr_stmt|;
name|custom
operator|.
name|setMaxQueueSize
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|setDefaultThreadPoolProfile
argument_list|(
name|custom
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|custom
operator|.
name|isDefaultProfile
argument_list|()
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|ExecutorService
name|myPool
init|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|newDefaultThreadPool
argument_list|(
name|this
argument_list|,
literal|"myPool"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|myPool
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
comment|// should use default settings
name|ThreadPoolExecutor
name|executor
init|=
operator|(
name|ThreadPoolExecutor
operator|)
name|myPool
decl_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|executor
operator|.
name|getCorePoolSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|30
argument_list|,
name|executor
operator|.
name|getMaximumPoolSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|50
argument_list|,
name|executor
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
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
name|executor
operator|.
name|getQueue
argument_list|()
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|myPool
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCustomDefaultThreadPool ()
specifier|public
name|void
name|testCustomDefaultThreadPool
parameter_list|()
throws|throws
name|Exception
block|{
name|ThreadPoolProfileSupport
name|custom
init|=
operator|new
name|ThreadPoolProfileSupport
argument_list|(
literal|"custom"
argument_list|)
decl_stmt|;
name|custom
operator|.
name|setKeepAliveTime
argument_list|(
literal|20L
argument_list|)
expr_stmt|;
name|custom
operator|.
name|setMaxPoolSize
argument_list|(
literal|40
argument_list|)
expr_stmt|;
name|custom
operator|.
name|setPoolSize
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|custom
operator|.
name|setMaxQueueSize
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|setDefaultThreadPoolProfile
argument_list|(
name|custom
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|custom
operator|.
name|isDefaultProfile
argument_list|()
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|ExecutorService
name|myPool
init|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|newDefaultThreadPool
argument_list|(
name|this
argument_list|,
literal|"myPool"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|myPool
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
comment|// should use default settings
name|ThreadPoolExecutor
name|executor
init|=
operator|(
name|ThreadPoolExecutor
operator|)
name|myPool
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|executor
operator|.
name|getCorePoolSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|40
argument_list|,
name|executor
operator|.
name|getMaximumPoolSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|executor
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
literal|2000
argument_list|,
name|executor
operator|.
name|getQueue
argument_list|()
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|myPool
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetThreadPoolProfile ()
specifier|public
name|void
name|testGetThreadPoolProfile
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNull
argument_list|(
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadPoolProfile
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|ThreadPoolProfileSupport
name|foo
init|=
operator|new
name|ThreadPoolProfileSupport
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|foo
operator|.
name|setKeepAliveTime
argument_list|(
literal|20L
argument_list|)
expr_stmt|;
name|foo
operator|.
name|setMaxPoolSize
argument_list|(
literal|40
argument_list|)
expr_stmt|;
name|foo
operator|.
name|setPoolSize
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|foo
operator|.
name|setMaxQueueSize
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|registerThreadPoolProfile
argument_list|(
name|foo
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|foo
argument_list|,
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadPoolProfile
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTwoGetThreadPoolProfile ()
specifier|public
name|void
name|testTwoGetThreadPoolProfile
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNull
argument_list|(
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadPoolProfile
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|ThreadPoolProfileSupport
name|foo
init|=
operator|new
name|ThreadPoolProfileSupport
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|foo
operator|.
name|setKeepAliveTime
argument_list|(
literal|20L
argument_list|)
expr_stmt|;
name|foo
operator|.
name|setMaxPoolSize
argument_list|(
literal|40
argument_list|)
expr_stmt|;
name|foo
operator|.
name|setPoolSize
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|foo
operator|.
name|setMaxQueueSize
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|registerThreadPoolProfile
argument_list|(
name|foo
argument_list|)
expr_stmt|;
name|ThreadPoolProfileSupport
name|bar
init|=
operator|new
name|ThreadPoolProfileSupport
argument_list|(
literal|"bar"
argument_list|)
decl_stmt|;
name|bar
operator|.
name|setKeepAliveTime
argument_list|(
literal|40L
argument_list|)
expr_stmt|;
name|bar
operator|.
name|setMaxPoolSize
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|bar
operator|.
name|setPoolSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|bar
operator|.
name|setMaxQueueSize
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|registerThreadPoolProfile
argument_list|(
name|bar
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|foo
argument_list|,
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadPoolProfile
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|bar
argument_list|,
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadPoolProfile
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|foo
argument_list|,
name|bar
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadPoolProfile
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isDefaultProfile
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadPoolProfile
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|isDefaultProfile
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetThreadPoolProfileInheritDefaultValues ()
specifier|public
name|void
name|testGetThreadPoolProfileInheritDefaultValues
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNull
argument_list|(
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadPoolProfile
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|ThreadPoolProfileSupport
name|foo
init|=
operator|new
name|ThreadPoolProfileSupport
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|foo
operator|.
name|setMaxPoolSize
argument_list|(
literal|40
argument_list|)
expr_stmt|;
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|registerThreadPoolProfile
argument_list|(
name|foo
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|foo
argument_list|,
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadPoolProfile
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|ExecutorService
name|executor
init|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|newThreadPool
argument_list|(
name|this
argument_list|,
literal|"MyPool"
argument_list|,
literal|"foo"
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
literal|40
argument_list|,
name|tp
operator|.
name|getMaximumPoolSize
argument_list|()
argument_list|)
expr_stmt|;
comment|// should inherit the default values
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|tp
operator|.
name|getCorePoolSize
argument_list|()
argument_list|)
expr_stmt|;
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
name|assertIsInstanceOf
argument_list|(
name|ThreadPoolExecutor
operator|.
name|CallerRunsPolicy
operator|.
name|class
argument_list|,
name|tp
operator|.
name|getRejectedExecutionHandler
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetThreadPoolProfileInheritCustomDefaultValues ()
specifier|public
name|void
name|testGetThreadPoolProfileInheritCustomDefaultValues
parameter_list|()
throws|throws
name|Exception
block|{
name|ThreadPoolProfileSupport
name|newDefault
init|=
operator|new
name|ThreadPoolProfileSupport
argument_list|(
literal|"newDefault"
argument_list|)
decl_stmt|;
name|newDefault
operator|.
name|setKeepAliveTime
argument_list|(
literal|30L
argument_list|)
expr_stmt|;
name|newDefault
operator|.
name|setMaxPoolSize
argument_list|(
literal|50
argument_list|)
expr_stmt|;
name|newDefault
operator|.
name|setPoolSize
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|newDefault
operator|.
name|setMaxQueueSize
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|newDefault
operator|.
name|setRejectedPolicy
argument_list|(
name|ThreadPoolRejectedPolicy
operator|.
name|Abort
argument_list|)
expr_stmt|;
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|setDefaultThreadPoolProfile
argument_list|(
name|newDefault
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadPoolProfile
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|ThreadPoolProfileSupport
name|foo
init|=
operator|new
name|ThreadPoolProfileSupport
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|foo
operator|.
name|setMaxPoolSize
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|foo
operator|.
name|setPoolSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|registerThreadPoolProfile
argument_list|(
name|foo
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|foo
argument_list|,
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadPoolProfile
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|ExecutorService
name|executor
init|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|newThreadPool
argument_list|(
name|this
argument_list|,
literal|"MyPool"
argument_list|,
literal|"foo"
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
literal|25
argument_list|,
name|tp
operator|.
name|getMaximumPoolSize
argument_list|()
argument_list|)
expr_stmt|;
comment|// should inherit the default values
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
literal|30
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
name|assertIsInstanceOf
argument_list|(
name|ThreadPoolExecutor
operator|.
name|AbortPolicy
operator|.
name|class
argument_list|,
name|tp
operator|.
name|getRejectedExecutionHandler
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetThreadPoolProfileInheritCustomDefaultValues2 ()
specifier|public
name|void
name|testGetThreadPoolProfileInheritCustomDefaultValues2
parameter_list|()
throws|throws
name|Exception
block|{
name|ThreadPoolProfileSupport
name|newDefault
init|=
operator|new
name|ThreadPoolProfileSupport
argument_list|(
literal|"newDefault"
argument_list|)
decl_stmt|;
comment|// just change the max pool as the default profile should then inherit the old default profile
name|newDefault
operator|.
name|setMaxPoolSize
argument_list|(
literal|50
argument_list|)
expr_stmt|;
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|setDefaultThreadPoolProfile
argument_list|(
name|newDefault
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadPoolProfile
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|ThreadPoolProfileSupport
name|foo
init|=
operator|new
name|ThreadPoolProfileSupport
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|foo
operator|.
name|setPoolSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|registerThreadPoolProfile
argument_list|(
name|foo
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|foo
argument_list|,
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadPoolProfile
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|ExecutorService
name|executor
init|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|newThreadPool
argument_list|(
name|this
argument_list|,
literal|"MyPool"
argument_list|,
literal|"foo"
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
comment|// should inherit the default values
name|assertEquals
argument_list|(
literal|50
argument_list|,
name|tp
operator|.
name|getMaximumPoolSize
argument_list|()
argument_list|)
expr_stmt|;
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
name|assertIsInstanceOf
argument_list|(
name|ThreadPoolExecutor
operator|.
name|CallerRunsPolicy
operator|.
name|class
argument_list|,
name|tp
operator|.
name|getRejectedExecutionHandler
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testNewThreadPoolProfile ()
specifier|public
name|void
name|testNewThreadPoolProfile
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNull
argument_list|(
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadPoolProfile
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|ThreadPoolProfileSupport
name|foo
init|=
operator|new
name|ThreadPoolProfileSupport
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|foo
operator|.
name|setKeepAliveTime
argument_list|(
literal|20L
argument_list|)
expr_stmt|;
name|foo
operator|.
name|setMaxPoolSize
argument_list|(
literal|40
argument_list|)
expr_stmt|;
name|foo
operator|.
name|setPoolSize
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|foo
operator|.
name|setMaxQueueSize
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|registerThreadPoolProfile
argument_list|(
name|foo
argument_list|)
expr_stmt|;
name|ExecutorService
name|pool
init|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|newThreadPool
argument_list|(
name|this
argument_list|,
literal|"Cool"
argument_list|,
literal|"foo"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pool
argument_list|)
expr_stmt|;
name|ThreadPoolExecutor
name|tp
init|=
name|assertIsInstanceOf
argument_list|(
name|ThreadPoolExecutor
operator|.
name|class
argument_list|,
name|pool
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|20
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
literal|40
argument_list|,
name|tp
operator|.
name|getMaximumPoolSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|tp
operator|.
name|getCorePoolSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tp
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|tp
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLookupThreadPoolProfile ()
specifier|public
name|void
name|testLookupThreadPoolProfile
parameter_list|()
throws|throws
name|Exception
block|{
name|ExecutorService
name|pool
init|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|lookup
argument_list|(
name|this
argument_list|,
literal|"Cool"
argument_list|,
literal|"fooProfile"
argument_list|)
decl_stmt|;
comment|// does not exists yet
name|assertNull
argument_list|(
name|pool
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadPoolProfile
argument_list|(
literal|"fooProfile"
argument_list|)
argument_list|)
expr_stmt|;
name|ThreadPoolProfileSupport
name|foo
init|=
operator|new
name|ThreadPoolProfileSupport
argument_list|(
literal|"fooProfile"
argument_list|)
decl_stmt|;
name|foo
operator|.
name|setKeepAliveTime
argument_list|(
literal|20L
argument_list|)
expr_stmt|;
name|foo
operator|.
name|setMaxPoolSize
argument_list|(
literal|40
argument_list|)
expr_stmt|;
name|foo
operator|.
name|setPoolSize
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|foo
operator|.
name|setMaxQueueSize
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|registerThreadPoolProfile
argument_list|(
name|foo
argument_list|)
expr_stmt|;
name|pool
operator|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|lookup
argument_list|(
name|this
argument_list|,
literal|"Cool"
argument_list|,
literal|"fooProfile"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|pool
argument_list|)
expr_stmt|;
name|ThreadPoolExecutor
name|tp
init|=
name|assertIsInstanceOf
argument_list|(
name|ThreadPoolExecutor
operator|.
name|class
argument_list|,
name|pool
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|20
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
literal|40
argument_list|,
name|tp
operator|.
name|getMaximumPoolSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|tp
operator|.
name|getCorePoolSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tp
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|tp
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

