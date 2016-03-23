begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ignite
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ignite
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Set
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
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Joiner
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Lists
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|IgniteException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|compute
operator|.
name|ComputeJob
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|compute
operator|.
name|ComputeJobResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|compute
operator|.
name|ComputeTask
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|compute
operator|.
name|ComputeTaskSplitAdapter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|events
operator|.
name|Event
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|lang
operator|.
name|IgniteCallable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|lang
operator|.
name|IgniteClosure
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|lang
operator|.
name|IgnitePredicate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|lang
operator|.
name|IgniteReducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|lang
operator|.
name|IgniteRunnable
import|;
end_import

begin_class
DECL|class|TestIgniteComputeResources
specifier|public
specifier|final
class|class
name|TestIgniteComputeResources
block|{
DECL|field|COUNTER
specifier|public
specifier|static
specifier|final
name|AtomicInteger
name|COUNTER
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
DECL|field|TEST_RUNNABLE
specifier|public
specifier|static
specifier|final
name|IgniteRunnable
name|TEST_RUNNABLE
init|=
operator|new
name|IgniteRunnable
argument_list|()
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|4961602602993218883L
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Hello from a runnable"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
DECL|field|TEST_RUNNABLE_COUNTER
specifier|public
specifier|static
specifier|final
name|IgniteRunnable
name|TEST_RUNNABLE_COUNTER
init|=
operator|new
name|IgniteRunnable
argument_list|()
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|386219709871673366L
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|COUNTER
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
DECL|field|EVENT_COUNTER
specifier|public
specifier|static
specifier|final
name|IgnitePredicate
argument_list|<
name|Event
argument_list|>
name|EVENT_COUNTER
init|=
operator|new
name|IgnitePredicate
argument_list|<
name|Event
argument_list|>
argument_list|()
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|4214894278107593791L
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|Event
name|event
parameter_list|)
block|{
name|COUNTER
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
DECL|field|TEST_CALLABLE
specifier|public
specifier|static
specifier|final
name|IgniteCallable
argument_list|<
name|String
argument_list|>
name|TEST_CALLABLE
init|=
operator|new
name|IgniteCallable
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|986972344531961815L
decl_stmt|;
annotation|@
name|Override
specifier|public
name|String
name|call
parameter_list|()
throws|throws
name|Exception
block|{
return|return
literal|"hello"
return|;
block|}
block|}
decl_stmt|;
DECL|field|TEST_CLOSURE
specifier|public
specifier|static
specifier|final
name|IgniteClosure
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|TEST_CLOSURE
init|=
operator|new
name|IgniteClosure
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|3969758431961263815L
decl_stmt|;
annotation|@
name|Override
specifier|public
name|String
name|apply
parameter_list|(
name|String
name|input
parameter_list|)
block|{
return|return
literal|"hello "
operator|+
name|input
return|;
block|}
block|}
decl_stmt|;
DECL|field|COMPUTE_TASK
specifier|public
specifier|static
specifier|final
name|ComputeTask
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|COMPUTE_TASK
init|=
operator|new
name|ComputeTaskSplitAdapter
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|3040624379256407732L
decl_stmt|;
annotation|@
name|Override
specifier|public
name|String
name|reduce
parameter_list|(
name|List
argument_list|<
name|ComputeJobResult
argument_list|>
name|results
parameter_list|)
throws|throws
name|IgniteException
block|{
name|StringBuilder
name|answer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|ComputeJobResult
name|res
range|:
name|results
control|)
block|{
name|Object
name|data
init|=
name|res
operator|.
name|getData
argument_list|()
decl_stmt|;
name|answer
operator|.
name|append
argument_list|(
name|data
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|deleteCharAt
argument_list|(
name|answer
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
return|return
name|answer
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Collection
argument_list|<
name|?
extends|extends
name|ComputeJob
argument_list|>
name|split
parameter_list|(
name|int
name|gridSize
parameter_list|,
specifier|final
name|Integer
name|arg
parameter_list|)
throws|throws
name|IgniteException
block|{
name|Set
argument_list|<
name|ComputeJob
argument_list|>
name|answer
init|=
operator|new
name|HashSet
argument_list|<>
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
name|arg
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|int
name|c
init|=
name|i
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|ComputeJob
argument_list|()
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|3365213549618276779L
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|()
throws|throws
name|IgniteException
block|{
return|return
literal|"a"
operator|+
name|c
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|cancel
parameter_list|()
block|{
comment|// nothing
block|}
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
decl_stmt|;
DECL|field|STRING_JOIN_REDUCER
specifier|public
specifier|static
specifier|final
name|IgniteReducer
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|STRING_JOIN_REDUCER
init|=
operator|new
name|IgniteReducer
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|collect
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|list
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|reduce
parameter_list|()
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|String
name|answer
init|=
name|Joiner
operator|.
name|on
argument_list|(
literal|""
argument_list|)
operator|.
name|join
argument_list|(
name|list
argument_list|)
decl_stmt|;
name|list
operator|.
name|clear
argument_list|()
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
decl_stmt|;
DECL|method|TestIgniteComputeResources ()
specifier|private
name|TestIgniteComputeResources
parameter_list|()
block|{              }
block|}
end_class

end_unit

