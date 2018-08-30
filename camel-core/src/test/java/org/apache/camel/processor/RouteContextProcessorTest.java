begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|ProducerTemplate
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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

begin_comment
comment|/**  * This is a manual test to run  *  * @version  */
end_comment

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"Manual test"
argument_list|)
DECL|class|RouteContextProcessorTest
specifier|public
class|class
name|RouteContextProcessorTest
extends|extends
name|ContextTestSupport
block|{
comment|// Number of concurrent processing threads
DECL|field|CONCURRENCY
specifier|public
specifier|static
specifier|final
name|int
name|CONCURRENCY
init|=
literal|10
decl_stmt|;
comment|// Additional resequencer time-out above theoretical time-out
DECL|field|SAFETY_TIMEOUT
specifier|public
specifier|static
specifier|final
name|long
name|SAFETY_TIMEOUT
init|=
literal|100
decl_stmt|;
comment|// Additional resequencer capacity above theoretical capacity
DECL|field|SAFETY_CAPACITY
specifier|public
specifier|static
specifier|final
name|int
name|SAFETY_CAPACITY
init|=
literal|10
decl_stmt|;
comment|// Resequencer time-out
DECL|field|TIMEOUT
specifier|public
specifier|static
specifier|final
name|long
name|TIMEOUT
init|=
name|SAFETY_TIMEOUT
operator|+
operator|(
name|RandomSleepProcessor
operator|.
name|MAX_PROCESS_TIME
operator|-
name|RandomSleepProcessor
operator|.
name|MIN_PROCESS_TIME
operator|)
decl_stmt|;
comment|// Resequencer capacity
DECL|field|CAPACITY
specifier|public
specifier|static
specifier|final
name|int
name|CAPACITY
init|=
name|SAFETY_CAPACITY
operator|+
call|(
name|int
call|)
argument_list|(
name|CONCURRENCY
operator|*
name|TIMEOUT
operator|/
name|RandomSleepProcessor
operator|.
name|MIN_PROCESS_TIME
argument_list|)
decl_stmt|;
DECL|field|NUMBER_OF_MESSAGES
specifier|private
specifier|static
specifier|final
name|int
name|NUMBER_OF_MESSAGES
init|=
literal|10000
decl_stmt|;
annotation|@
name|Test
DECL|method|testForkAndJoin ()
specifier|public
name|void
name|testForkAndJoin
parameter_list|()
throws|throws
name|InterruptedException
block|{
comment|// enable the other test method for manual testing
block|}
DECL|method|xxxTestForkAndJoin ()
specifier|public
name|void
name|xxxTestForkAndJoin
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
name|NUMBER_OF_MESSAGES
argument_list|)
expr_stmt|;
name|ProducerTemplate
name|template
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|NUMBER_OF_MESSAGES
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"seda:fork"
argument_list|,
literal|"Test Message: "
operator|+
name|i
argument_list|,
literal|"seqnum"
argument_list|,
operator|new
name|Long
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|long
name|expectedTime
init|=
name|NUMBER_OF_MESSAGES
operator|*
operator|(
name|RandomSleepProcessor
operator|.
name|MAX_PROCESS_TIME
operator|+
name|RandomSleepProcessor
operator|.
name|MIN_PROCESS_TIME
operator|)
operator|/
literal|2
operator|/
name|CONCURRENCY
operator|+
name|TIMEOUT
decl_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|expectedTime
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
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
name|Processor
name|myProcessor
init|=
operator|new
name|RandomSleepProcessor
argument_list|()
decl_stmt|;
name|from
argument_list|(
literal|"seda:fork?concurrentConsumers="
operator|+
name|CONCURRENCY
argument_list|)
operator|.
name|process
argument_list|(
name|myProcessor
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:join"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:join"
argument_list|)
operator|.
name|resequence
argument_list|(
name|header
argument_list|(
literal|"seqnum"
argument_list|)
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|capacity
argument_list|(
name|CAPACITY
argument_list|)
operator|.
name|timeout
argument_list|(
name|TIMEOUT
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
comment|/**      * Simulation processor that sleeps a random time between MIN_PROCESS_TIME      * and MAX_PROCESS_TIME milliseconds.      */
DECL|class|RandomSleepProcessor
specifier|public
specifier|static
class|class
name|RandomSleepProcessor
implements|implements
name|Processor
block|{
DECL|field|MIN_PROCESS_TIME
specifier|public
specifier|static
specifier|final
name|long
name|MIN_PROCESS_TIME
init|=
literal|5
decl_stmt|;
DECL|field|MAX_PROCESS_TIME
specifier|public
specifier|static
specifier|final
name|long
name|MAX_PROCESS_TIME
init|=
literal|50
decl_stmt|;
annotation|@
name|Override
DECL|method|process (Exchange arg0)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|arg0
parameter_list|)
throws|throws
name|Exception
block|{
name|long
name|processTime
init|=
call|(
name|long
call|)
argument_list|(
name|MIN_PROCESS_TIME
operator|+
name|Math
operator|.
name|random
argument_list|()
operator|*
operator|(
name|MAX_PROCESS_TIME
operator|-
name|MIN_PROCESS_TIME
operator|)
argument_list|)
decl_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|processTime
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

