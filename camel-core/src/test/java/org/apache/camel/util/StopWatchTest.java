begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

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
DECL|class|StopWatchTest
specifier|public
class|class
name|StopWatchTest
extends|extends
name|TestCase
block|{
DECL|method|testStopWatch ()
specifier|public
name|void
name|testStopWatch
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
name|Thread
operator|.
name|sleep
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|long
name|taken
init|=
name|watch
operator|.
name|stop
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|taken
argument_list|,
name|watch
operator|.
name|taken
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should take approx 200 millis, was: "
operator|+
name|taken
argument_list|,
name|taken
operator|>
literal|190
argument_list|)
expr_stmt|;
block|}
DECL|method|testStopWatchNotStarted ()
specifier|public
name|void
name|testStopWatchNotStarted
parameter_list|()
throws|throws
name|Exception
block|{
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|long
name|taken
init|=
name|watch
operator|.
name|stop
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|taken
argument_list|)
expr_stmt|;
name|watch
operator|.
name|restart
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|taken
operator|=
name|watch
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|taken
argument_list|,
name|watch
operator|.
name|taken
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should take approx 200 millis, was: "
operator|+
name|taken
argument_list|,
name|taken
operator|>
literal|190
argument_list|)
expr_stmt|;
block|}
DECL|method|testStopWatchRestart ()
specifier|public
name|void
name|testStopWatchRestart
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
name|Thread
operator|.
name|sleep
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|long
name|taken
init|=
name|watch
operator|.
name|stop
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|taken
argument_list|,
name|watch
operator|.
name|taken
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should take approx 200 millis, was: "
operator|+
name|taken
argument_list|,
name|taken
operator|>
literal|190
argument_list|)
expr_stmt|;
name|watch
operator|.
name|restart
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|taken
operator|=
name|watch
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|taken
argument_list|,
name|watch
operator|.
name|taken
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should take approx 100 millis, was: "
operator|+
name|taken
argument_list|,
name|taken
operator|>
literal|90
argument_list|)
expr_stmt|;
block|}
DECL|method|testStopWatchTaken ()
specifier|public
name|void
name|testStopWatchTaken
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
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|long
name|taken
init|=
name|watch
operator|.
name|taken
argument_list|()
decl_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|long
name|taken2
init|=
name|watch
operator|.
name|taken
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|taken
argument_list|,
name|taken2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|taken2
operator|>
name|taken
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

