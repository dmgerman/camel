begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.jmh
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|jmh
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
name|impl
operator|.
name|engine
operator|.
name|DefaultUuidGenerator
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
name|openjdk
operator|.
name|jmh
operator|.
name|annotations
operator|.
name|Benchmark
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openjdk
operator|.
name|jmh
operator|.
name|annotations
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openjdk
operator|.
name|jmh
operator|.
name|annotations
operator|.
name|Measurement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openjdk
operator|.
name|jmh
operator|.
name|annotations
operator|.
name|Mode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openjdk
operator|.
name|jmh
operator|.
name|annotations
operator|.
name|Scope
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openjdk
operator|.
name|jmh
operator|.
name|annotations
operator|.
name|Setup
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openjdk
operator|.
name|jmh
operator|.
name|annotations
operator|.
name|State
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openjdk
operator|.
name|jmh
operator|.
name|infra
operator|.
name|Blackhole
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openjdk
operator|.
name|jmh
operator|.
name|runner
operator|.
name|Runner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openjdk
operator|.
name|jmh
operator|.
name|runner
operator|.
name|options
operator|.
name|Options
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openjdk
operator|.
name|jmh
operator|.
name|runner
operator|.
name|options
operator|.
name|OptionsBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openjdk
operator|.
name|jmh
operator|.
name|runner
operator|.
name|options
operator|.
name|TimeValue
import|;
end_import

begin_comment
comment|/**  * Tests the {@link DefaultUuidGenerator}.  *<p/>  * Thanks to this SO answer: https://stackoverflow.com/questions/30485856/how-to-run-jmh-from-inside-junit-tests  */
end_comment

begin_class
DECL|class|DefaultUuidGeneratorTest
specifier|public
class|class
name|DefaultUuidGeneratorTest
block|{
annotation|@
name|Test
DECL|method|launchBenchmark ()
specifier|public
name|void
name|launchBenchmark
parameter_list|()
throws|throws
name|Exception
block|{
name|Options
name|opt
init|=
operator|new
name|OptionsBuilder
argument_list|()
comment|// Specify which benchmarks to run.
comment|// You can be more specific if you'd like to run only one benchmark per test.
operator|.
name|include
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|".*"
argument_list|)
comment|// Set the following options as needed
operator|.
name|mode
argument_list|(
name|Mode
operator|.
name|All
argument_list|)
operator|.
name|timeUnit
argument_list|(
name|TimeUnit
operator|.
name|MICROSECONDS
argument_list|)
operator|.
name|warmupTime
argument_list|(
name|TimeValue
operator|.
name|seconds
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|warmupIterations
argument_list|(
literal|2
argument_list|)
operator|.
name|measurementTime
argument_list|(
name|TimeValue
operator|.
name|seconds
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|measurementIterations
argument_list|(
literal|2
argument_list|)
operator|.
name|threads
argument_list|(
literal|2
argument_list|)
operator|.
name|forks
argument_list|(
literal|1
argument_list|)
operator|.
name|shouldFailOnError
argument_list|(
literal|true
argument_list|)
operator|.
name|shouldDoGC
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
operator|new
name|Runner
argument_list|(
name|opt
argument_list|)
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
comment|// The JMH samples are the best documentation for how to use it
comment|// http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/
annotation|@
name|State
argument_list|(
name|Scope
operator|.
name|Thread
argument_list|)
DECL|class|BenchmarkState
specifier|public
specifier|static
class|class
name|BenchmarkState
block|{
DECL|field|uuid
name|DefaultUuidGenerator
name|uuid
decl_stmt|;
annotation|@
name|Setup
argument_list|(
name|Level
operator|.
name|Trial
argument_list|)
DECL|method|initialize ()
specifier|public
name|void
name|initialize
parameter_list|()
block|{
name|uuid
operator|=
operator|new
name|DefaultUuidGenerator
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Benchmark
annotation|@
name|Measurement
argument_list|(
name|batchSize
operator|=
literal|1000000
argument_list|)
DECL|method|benchmark (BenchmarkState state, Blackhole bh)
specifier|public
name|void
name|benchmark
parameter_list|(
name|BenchmarkState
name|state
parameter_list|,
name|Blackhole
name|bh
parameter_list|)
block|{
name|String
name|id
init|=
name|state
operator|.
name|uuid
operator|.
name|generateUuid
argument_list|()
decl_stmt|;
name|bh
operator|.
name|consume
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

