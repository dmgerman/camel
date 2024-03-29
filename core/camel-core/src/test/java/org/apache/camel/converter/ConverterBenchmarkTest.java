begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|LoggingLevel
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
name|converter
operator|.
name|DefaultTypeConverter
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
name|DefaultClassResolver
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
name|DefaultFactoryFinderResolver
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
name|DefaultPackageScanClassResolver
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
name|FactoryFinder
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
name|Injector
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
name|ReflectionInjector
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

begin_class
DECL|class|ConverterBenchmarkTest
specifier|public
class|class
name|ConverterBenchmarkTest
block|{
annotation|@
name|Ignore
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
comment|// You can be more specific if you'd like to run only one benchmark
comment|// per test.
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
name|AverageTime
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
literal|2
argument_list|)
argument_list|)
operator|.
name|warmupIterations
argument_list|(
literal|5
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
literal|5
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
comment|// .jvmArgs("-XX:+UnlockDiagnosticVMOptions", "-XX:+PrintInlining")
comment|// .addProfiler(WinPerfAsmProfiler.class)
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
DECL|field|packageScanClassResolver
name|DefaultPackageScanClassResolver
name|packageScanClassResolver
decl_stmt|;
DECL|field|injector
name|Injector
name|injector
decl_stmt|;
DECL|field|factoryFinder
name|FactoryFinder
name|factoryFinder
decl_stmt|;
DECL|field|converter
name|DefaultTypeConverter
name|converter
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
throws|throws
name|Exception
block|{
name|packageScanClassResolver
operator|=
operator|new
name|DefaultPackageScanClassResolver
argument_list|()
expr_stmt|;
name|injector
operator|=
operator|new
name|ReflectionInjector
argument_list|()
expr_stmt|;
name|factoryFinder
operator|=
operator|new
name|DefaultFactoryFinderResolver
argument_list|()
operator|.
name|resolveDefaultFactoryFinder
argument_list|(
operator|new
name|DefaultClassResolver
argument_list|()
argument_list|)
expr_stmt|;
name|converter
operator|=
operator|new
name|DefaultTypeConverter
argument_list|(
name|packageScanClassResolver
argument_list|,
name|injector
argument_list|,
name|factoryFinder
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|converter
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Benchmark
DECL|method|benchmarkLoadTime (BenchmarkState state, Blackhole bh)
specifier|public
name|void
name|benchmarkLoadTime
parameter_list|(
name|BenchmarkState
name|state
parameter_list|,
name|Blackhole
name|bh
parameter_list|)
throws|throws
name|Exception
block|{
name|DefaultPackageScanClassResolver
name|packageScanClassResolver
init|=
name|state
operator|.
name|packageScanClassResolver
decl_stmt|;
name|Injector
name|injector
init|=
name|state
operator|.
name|injector
decl_stmt|;
name|FactoryFinder
name|factoryFinder
init|=
name|state
operator|.
name|factoryFinder
decl_stmt|;
name|DefaultTypeConverter
name|converter
init|=
operator|new
name|DefaultTypeConverter
argument_list|(
name|packageScanClassResolver
argument_list|,
name|injector
argument_list|,
name|factoryFinder
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|converter
operator|.
name|start
argument_list|()
expr_stmt|;
name|bh
operator|.
name|consume
argument_list|(
name|converter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Benchmark
DECL|method|benchmarkConversionTimeEnum (BenchmarkState state, Blackhole bh)
specifier|public
name|void
name|benchmarkConversionTimeEnum
parameter_list|(
name|BenchmarkState
name|state
parameter_list|,
name|Blackhole
name|bh
parameter_list|)
block|{
name|DefaultTypeConverter
name|converter
init|=
name|state
operator|.
name|converter
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
literal|1000
condition|;
name|i
operator|++
control|)
block|{
name|bh
operator|.
name|consume
argument_list|(
name|converter
operator|.
name|convertTo
argument_list|(
name|LoggingLevel
operator|.
name|class
argument_list|,
literal|"DEBUG"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Benchmark
DECL|method|benchmarkConversionIntToLong (BenchmarkState state, Blackhole bh)
specifier|public
name|void
name|benchmarkConversionIntToLong
parameter_list|(
name|BenchmarkState
name|state
parameter_list|,
name|Blackhole
name|bh
parameter_list|)
block|{
name|DefaultTypeConverter
name|converter
init|=
name|state
operator|.
name|converter
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
literal|1000
condition|;
name|i
operator|++
control|)
block|{
name|bh
operator|.
name|consume
argument_list|(
name|converter
operator|.
name|convertTo
argument_list|(
name|Long
operator|.
name|class
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Benchmark
DECL|method|benchmarkConversionStringToChar (BenchmarkState state, Blackhole bh)
specifier|public
name|void
name|benchmarkConversionStringToChar
parameter_list|(
name|BenchmarkState
name|state
parameter_list|,
name|Blackhole
name|bh
parameter_list|)
block|{
name|DefaultTypeConverter
name|converter
init|=
name|state
operator|.
name|converter
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
literal|1000
condition|;
name|i
operator|++
control|)
block|{
name|bh
operator|.
name|consume
argument_list|(
name|converter
operator|.
name|convertTo
argument_list|(
name|char
index|[]
operator|.
expr|class
argument_list|,
literal|"Hello world"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Benchmark
DECL|method|benchmarkConversionStringToURI (BenchmarkState state, Blackhole bh)
specifier|public
name|void
name|benchmarkConversionStringToURI
parameter_list|(
name|BenchmarkState
name|state
parameter_list|,
name|Blackhole
name|bh
parameter_list|)
block|{
name|DefaultTypeConverter
name|converter
init|=
name|state
operator|.
name|converter
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
literal|1000
condition|;
name|i
operator|++
control|)
block|{
name|bh
operator|.
name|consume
argument_list|(
name|converter
operator|.
name|convertTo
argument_list|(
name|URI
operator|.
name|class
argument_list|,
literal|"uri:foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Benchmark
DECL|method|benchmarkConversionListToStringArray (BenchmarkState state, Blackhole bh)
specifier|public
name|void
name|benchmarkConversionListToStringArray
parameter_list|(
name|BenchmarkState
name|state
parameter_list|,
name|Blackhole
name|bh
parameter_list|)
block|{
name|DefaultTypeConverter
name|converter
init|=
name|state
operator|.
name|converter
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
literal|1000
condition|;
name|i
operator|++
control|)
block|{
name|bh
operator|.
name|consume
argument_list|(
name|converter
operator|.
name|convertTo
argument_list|(
name|String
index|[]
operator|.
expr|class
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"DEBUG"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|testConvertEnumPerfs ()
specifier|public
name|void
name|testConvertEnumPerfs
parameter_list|()
throws|throws
name|Exception
block|{
name|Blackhole
name|bh
init|=
operator|new
name|Blackhole
argument_list|(
literal|"Today's password is swordfish. I understand instantiating Blackholes directly is dangerous."
argument_list|)
decl_stmt|;
name|BenchmarkState
name|state
init|=
operator|new
name|BenchmarkState
argument_list|()
decl_stmt|;
name|state
operator|.
name|initialize
argument_list|()
expr_stmt|;
name|doTest
argument_list|(
name|bh
argument_list|,
name|state
argument_list|)
expr_stmt|;
block|}
DECL|method|doTest (Blackhole bh, BenchmarkState state)
specifier|private
name|void
name|doTest
parameter_list|(
name|Blackhole
name|bh
parameter_list|,
name|BenchmarkState
name|state
parameter_list|)
block|{
name|DefaultTypeConverter
name|converter
init|=
name|state
operator|.
name|converter
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
literal|1000000
condition|;
name|i
operator|++
control|)
block|{
name|bh
operator|.
name|consume
argument_list|(
name|converter
operator|.
name|convertTo
argument_list|(
name|Long
operator|.
name|class
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

