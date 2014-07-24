begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|spring
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
name|ServiceStatus
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
name|Before
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
annotation|@
name|UseAdviceWith
DECL|class|CamelSpringJUnit4ClassRunnerUseAdviceWithTest
specifier|public
class|class
name|CamelSpringJUnit4ClassRunnerUseAdviceWithTest
extends|extends
name|CamelSpringJUnit4ClassRunnerPlainTest
block|{
annotation|@
name|Before
DECL|method|testContextStarted ()
specifier|public
name|void
name|testContextStarted
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Stopped
argument_list|,
name|camelContext
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Stopped
argument_list|,
name|camelContext2
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|camelContext2
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// just sleep a little to simulate testing take a bit time
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStopwatch ()
specifier|public
name|void
name|testStopwatch
parameter_list|()
block|{
name|StopWatch
name|stopWatch
init|=
name|StopWatchTestExecutionListener
operator|.
name|getStopWatch
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|stopWatch
argument_list|)
expr_stmt|;
name|long
name|taken
init|=
name|stopWatch
operator|.
name|taken
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|taken
operator|+
literal|"> 0, but was: "
operator|+
name|taken
argument_list|,
name|taken
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|taken
operator|+
literal|"< 3000, but was: "
operator|+
name|taken
argument_list|,
name|taken
operator|<
literal|3000
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

