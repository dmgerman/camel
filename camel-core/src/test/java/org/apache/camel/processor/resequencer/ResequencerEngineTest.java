begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.resequencer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|resequencer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
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
name|TestSupport
import|;
end_import

begin_class
DECL|class|ResequencerEngineTest
specifier|public
class|class
name|ResequencerEngineTest
extends|extends
name|TestSupport
block|{
DECL|field|IGNORE_LOAD_TESTS
specifier|private
specifier|static
specifier|final
name|boolean
name|IGNORE_LOAD_TESTS
init|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"ignore.load.tests"
argument_list|,
literal|"true"
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|resequencer
specifier|private
name|ResequencerEngineSync
argument_list|<
name|Integer
argument_list|>
name|resequencer
decl_stmt|;
DECL|field|runner
specifier|private
name|ResequencerRunner
argument_list|<
name|Integer
argument_list|>
name|runner
decl_stmt|;
DECL|field|buffer
specifier|private
name|SequenceBuffer
argument_list|<
name|Integer
argument_list|>
name|buffer
decl_stmt|;
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{     }
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|runner
operator|!=
literal|null
condition|)
block|{
name|runner
operator|.
name|cancel
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|resequencer
operator|!=
literal|null
condition|)
block|{
name|resequencer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|testTimeout1 ()
specifier|public
name|void
name|testTimeout1
parameter_list|()
throws|throws
name|Exception
block|{
name|initResequencer
argument_list|(
literal|500
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|resequencer
operator|.
name|insert
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|buffer
operator|.
name|poll
argument_list|(
literal|250
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Integer
operator|)
literal|4
argument_list|,
name|buffer
operator|.
name|take
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Integer
operator|)
literal|4
argument_list|,
name|resequencer
operator|.
name|getLastDelivered
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTimeout2 ()
specifier|public
name|void
name|testTimeout2
parameter_list|()
throws|throws
name|Exception
block|{
name|initResequencer
argument_list|(
literal|500
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|resequencer
operator|.
name|setLastDelivered
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|resequencer
operator|.
name|insert
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|buffer
operator|.
name|poll
argument_list|(
literal|250
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Integer
operator|)
literal|4
argument_list|,
name|buffer
operator|.
name|take
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Integer
operator|)
literal|4
argument_list|,
name|resequencer
operator|.
name|getLastDelivered
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTimeout3 ()
specifier|public
name|void
name|testTimeout3
parameter_list|()
throws|throws
name|Exception
block|{
name|initResequencer
argument_list|(
literal|500
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|resequencer
operator|.
name|setLastDelivered
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|resequencer
operator|.
name|insert
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Integer
operator|)
literal|4
argument_list|,
name|buffer
operator|.
name|poll
argument_list|(
literal|250
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Integer
operator|)
literal|4
argument_list|,
name|resequencer
operator|.
name|getLastDelivered
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTimeout4 ()
specifier|public
name|void
name|testTimeout4
parameter_list|()
throws|throws
name|Exception
block|{
name|initResequencer
argument_list|(
literal|500
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|resequencer
operator|.
name|setLastDelivered
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|resequencer
operator|.
name|insert
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|resequencer
operator|.
name|insert
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Integer
operator|)
literal|3
argument_list|,
name|buffer
operator|.
name|poll
argument_list|(
literal|250
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Integer
operator|)
literal|4
argument_list|,
name|buffer
operator|.
name|poll
argument_list|(
literal|250
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Integer
operator|)
literal|4
argument_list|,
name|resequencer
operator|.
name|getLastDelivered
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testRandom ()
specifier|public
name|void
name|testRandom
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|IGNORE_LOAD_TESTS
condition|)
block|{
return|return;
block|}
name|int
name|input
init|=
literal|1000
decl_stmt|;
name|initResequencer
argument_list|(
literal|1000
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|list
init|=
operator|new
name|LinkedList
argument_list|<
name|Integer
argument_list|>
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
name|input
condition|;
name|i
operator|++
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
name|Random
name|random
init|=
operator|new
name|Random
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|4000
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"Input sequence: "
argument_list|)
expr_stmt|;
name|long
name|millis
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|input
init|;
name|i
operator|>
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|int
name|r
init|=
name|random
operator|.
name|nextInt
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|int
name|next
init|=
name|list
operator|.
name|remove
argument_list|(
name|r
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|next
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|resequencer
operator|.
name|insert
argument_list|(
name|next
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|info
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// clear
name|sb
operator|.
name|delete
argument_list|(
literal|0
argument_list|,
name|sb
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"Output sequence: "
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|input
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|buffer
operator|.
name|take
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|millis
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|millis
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Duration = "
operator|+
name|millis
operator|+
literal|" ms"
argument_list|)
expr_stmt|;
block|}
DECL|method|testReverse1 ()
specifier|public
name|void
name|testReverse1
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|IGNORE_LOAD_TESTS
condition|)
block|{
return|return;
block|}
name|testReverse
argument_list|(
literal|10
argument_list|)
expr_stmt|;
block|}
DECL|method|testReverse2 ()
specifier|public
name|void
name|testReverse2
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|IGNORE_LOAD_TESTS
condition|)
block|{
return|return;
block|}
name|testReverse
argument_list|(
literal|100
argument_list|)
expr_stmt|;
block|}
DECL|method|testReverse (int capacity)
specifier|private
name|void
name|testReverse
parameter_list|(
name|int
name|capacity
parameter_list|)
throws|throws
name|Exception
block|{
name|initResequencer
argument_list|(
literal|1
argument_list|,
name|capacity
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|99
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|resequencer
operator|.
name|insert
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|2500
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"Output sequence: "
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|100
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|buffer
operator|.
name|take
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|info
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|initResequencer (long timeout, int capacity)
specifier|private
name|void
name|initResequencer
parameter_list|(
name|long
name|timeout
parameter_list|,
name|int
name|capacity
parameter_list|)
block|{
name|ResequencerEngine
argument_list|<
name|Integer
argument_list|>
name|engine
decl_stmt|;
name|buffer
operator|=
operator|new
name|SequenceBuffer
argument_list|<
name|Integer
argument_list|>
argument_list|()
expr_stmt|;
name|engine
operator|=
operator|new
name|ResequencerEngine
argument_list|<
name|Integer
argument_list|>
argument_list|(
operator|new
name|IntegerComparator
argument_list|()
argument_list|)
expr_stmt|;
name|engine
operator|.
name|setSequenceSender
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
name|engine
operator|.
name|setTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
name|engine
operator|.
name|start
argument_list|()
expr_stmt|;
name|resequencer
operator|=
operator|new
name|ResequencerEngineSync
argument_list|<
name|Integer
argument_list|>
argument_list|(
name|engine
argument_list|)
expr_stmt|;
name|runner
operator|=
operator|new
name|ResequencerRunner
argument_list|<
name|Integer
argument_list|>
argument_list|(
name|resequencer
argument_list|,
literal|50
argument_list|)
expr_stmt|;
name|runner
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// give the runner time to start
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
end_class

end_unit

