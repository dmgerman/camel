begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.concurrent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|concurrent
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
name|ScheduledExecutorService
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|ExecutorServiceHelperTest
specifier|public
class|class
name|ExecutorServiceHelperTest
extends|extends
name|TestCase
block|{
DECL|method|testGetThreadName ()
specifier|public
name|void
name|testGetThreadName
parameter_list|()
block|{
name|String
name|name
init|=
name|ExecutorServiceHelper
operator|.
name|getThreadName
argument_list|(
literal|"Camel Thread ${counter} - ${name}"
argument_list|,
literal|"foo"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"Camel Thread"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|name
operator|.
name|endsWith
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testNewScheduledThreadPool ()
specifier|public
name|void
name|testNewScheduledThreadPool
parameter_list|()
block|{
name|ScheduledExecutorService
name|pool
init|=
name|ExecutorServiceHelper
operator|.
name|newScheduledThreadPool
argument_list|(
literal|1
argument_list|,
literal|"MyPool ${name}"
argument_list|,
literal|"foo"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pool
argument_list|)
expr_stmt|;
block|}
DECL|method|testNewThreadPool ()
specifier|public
name|void
name|testNewThreadPool
parameter_list|()
block|{
name|ExecutorService
name|pool
init|=
name|ExecutorServiceHelper
operator|.
name|newThreadPool
argument_list|(
literal|"MyPool ${name}"
argument_list|,
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pool
argument_list|)
expr_stmt|;
block|}
DECL|method|testNewThreadPool2 ()
specifier|public
name|void
name|testNewThreadPool2
parameter_list|()
block|{
name|ExecutorService
name|pool
init|=
name|ExecutorServiceHelper
operator|.
name|newThreadPool
argument_list|(
literal|"MyPool ${name}"
argument_list|,
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|20
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pool
argument_list|)
expr_stmt|;
block|}
DECL|method|testNewThreadPool3 ()
specifier|public
name|void
name|testNewThreadPool3
parameter_list|()
block|{
name|ExecutorService
name|pool
init|=
name|ExecutorServiceHelper
operator|.
name|newThreadPool
argument_list|(
literal|"MyPool ${name}"
argument_list|,
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|30
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|,
literal|20
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pool
argument_list|)
expr_stmt|;
block|}
DECL|method|testNewCachedThreadPool ()
specifier|public
name|void
name|testNewCachedThreadPool
parameter_list|()
block|{
name|ExecutorService
name|pool
init|=
name|ExecutorServiceHelper
operator|.
name|newCachedThreadPool
argument_list|(
literal|"MyPool ${name}"
argument_list|,
literal|"foo"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pool
argument_list|)
expr_stmt|;
block|}
DECL|method|testNewFixedThreadPool ()
specifier|public
name|void
name|testNewFixedThreadPool
parameter_list|()
block|{
name|ExecutorService
name|pool
init|=
name|ExecutorServiceHelper
operator|.
name|newFixedThreadPool
argument_list|(
literal|1
argument_list|,
literal|"MyPool ${name}"
argument_list|,
literal|"foo"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pool
argument_list|)
expr_stmt|;
block|}
DECL|method|testNewSynchronousThreadPool ()
specifier|public
name|void
name|testNewSynchronousThreadPool
parameter_list|()
block|{
name|ExecutorService
name|pool
init|=
name|ExecutorServiceHelper
operator|.
name|newSynchronousThreadPool
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|pool
argument_list|)
expr_stmt|;
block|}
DECL|method|testNewSingleThreadPool ()
specifier|public
name|void
name|testNewSingleThreadPool
parameter_list|()
block|{
name|ExecutorService
name|pool
init|=
name|ExecutorServiceHelper
operator|.
name|newSingleThreadExecutor
argument_list|(
literal|"MyPool ${name}"
argument_list|,
literal|"foo"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pool
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

