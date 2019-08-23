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
name|Endpoint
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
name|impl
operator|.
name|DefaultCamelContext
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
name|annotations
operator|.
name|TearDown
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
comment|/**  * Tests fast property binding on endpoints  */
end_comment

begin_class
DECL|class|LogEndpointTest
specifier|public
class|class
name|LogEndpointTest
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
DECL|field|camel
name|CamelContext
name|camel
decl_stmt|;
DECL|field|counter
name|AtomicInteger
name|counter
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
name|camel
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|camel
operator|.
name|getGlobalOptions
argument_list|()
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|MAXIMUM_ENDPOINT_CACHE_SIZE
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|counter
operator|=
operator|new
name|AtomicInteger
argument_list|()
expr_stmt|;
block|}
annotation|@
name|TearDown
argument_list|(
name|Level
operator|.
name|Trial
argument_list|)
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
try|try
block|{
name|camel
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
annotation|@
name|Benchmark
annotation|@
name|Measurement
argument_list|(
name|batchSize
operator|=
literal|1000
argument_list|)
DECL|method|logEndpoint (BenchmarkState state, Blackhole bh)
specifier|public
name|void
name|logEndpoint
parameter_list|(
name|BenchmarkState
name|state
parameter_list|,
name|Blackhole
name|bh
parameter_list|)
block|{
comment|// use the legacy binding which uses reflection
comment|// Endpoint out = state.camel.getEndpoint("log:foo?basicPropertyBinding=true&showAll=true&groupSize=" + state.counter.incrementAndGet());
name|Endpoint
name|out
init|=
name|state
operator|.
name|camel
operator|.
name|getEndpoint
argument_list|(
literal|"log:foo?showAll=true&groupSize="
operator|+
name|state
operator|.
name|counter
operator|.
name|incrementAndGet
argument_list|()
argument_list|)
decl_stmt|;
name|bh
operator|.
name|consume
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

