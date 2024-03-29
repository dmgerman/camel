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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|IntStream
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
name|CaseInsensitiveMap
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang3
operator|.
name|RandomStringUtils
operator|.
name|randomAlphabetic
import|;
end_import

begin_comment
comment|/**  * Tests {@link CaseInsensitiveMap}  */
end_comment

begin_class
DECL|class|CaseInsensitiveMapTest
specifier|public
class|class
name|CaseInsensitiveMapTest
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
name|SampleTime
argument_list|)
operator|.
name|timeUnit
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
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
literal|5
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
literal|1
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
name|measurementBatchSize
argument_list|(
literal|1000000
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
DECL|class|MapsBenchmarkState
specifier|public
specifier|static
class|class
name|MapsBenchmarkState
block|{
DECL|field|camelMap
name|CaseInsensitiveMap
name|camelMap
decl_stmt|;
DECL|field|cedarsoftMap
name|com
operator|.
name|cedarsoftware
operator|.
name|util
operator|.
name|CaseInsensitiveMap
name|cedarsoftMap
decl_stmt|;
DECL|field|hashMap
name|HashMap
name|hashMap
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
name|camelMap
operator|=
operator|new
name|CaseInsensitiveMap
argument_list|()
expr_stmt|;
name|cedarsoftMap
operator|=
operator|new
name|com
operator|.
name|cedarsoftware
operator|.
name|util
operator|.
name|CaseInsensitiveMap
argument_list|()
expr_stmt|;
name|hashMap
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|State
argument_list|(
name|Scope
operator|.
name|Benchmark
argument_list|)
DECL|class|MapsSourceDataBenchmarkState
specifier|public
specifier|static
class|class
name|MapsSourceDataBenchmarkState
block|{
DECL|field|map1
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map1
init|=
name|generateRandomMap
argument_list|(
literal|10
argument_list|)
decl_stmt|;
DECL|field|map2
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map2
init|=
name|generateRandomMap
argument_list|(
literal|10
argument_list|)
decl_stmt|;
DECL|method|generateRandomMap (int size)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|generateRandomMap
parameter_list|(
name|int
name|size
parameter_list|)
block|{
return|return
name|IntStream
operator|.
name|range
argument_list|(
literal|0
argument_list|,
name|size
argument_list|)
operator|.
name|boxed
argument_list|()
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toMap
argument_list|(
name|i
lambda|->
name|randomAlphabetic
argument_list|(
literal|10
argument_list|)
argument_list|,
name|i
lambda|->
name|randomAlphabetic
argument_list|(
literal|10
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
annotation|@
name|Benchmark
DECL|method|camelMapSimpleCase (MapsBenchmarkState state, Blackhole bh)
specifier|public
name|void
name|camelMapSimpleCase
parameter_list|(
name|MapsBenchmarkState
name|state
parameter_list|,
name|Blackhole
name|bh
parameter_list|)
block|{
name|Map
name|map
init|=
name|state
operator|.
name|camelMap
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|Object
name|o1
init|=
name|map
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|bh
operator|.
name|consume
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|Object
name|o2
init|=
name|map
operator|.
name|get
argument_list|(
literal|"FOO"
argument_list|)
decl_stmt|;
name|bh
operator|.
name|consume
argument_list|(
name|o2
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"BAR"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|Object
name|o3
init|=
name|map
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
decl_stmt|;
name|bh
operator|.
name|consume
argument_list|(
name|o3
argument_list|)
expr_stmt|;
name|Object
name|o4
init|=
name|map
operator|.
name|get
argument_list|(
literal|"BAR"
argument_list|)
decl_stmt|;
name|bh
operator|.
name|consume
argument_list|(
name|o4
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Benchmark
DECL|method|cedarsoftMapSimpleCase (MapsBenchmarkState state, Blackhole bh)
specifier|public
name|void
name|cedarsoftMapSimpleCase
parameter_list|(
name|MapsBenchmarkState
name|state
parameter_list|,
name|Blackhole
name|bh
parameter_list|)
block|{
name|Map
name|map
init|=
name|state
operator|.
name|cedarsoftMap
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|Object
name|o1
init|=
name|map
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|bh
operator|.
name|consume
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|Object
name|o2
init|=
name|map
operator|.
name|get
argument_list|(
literal|"FOO"
argument_list|)
decl_stmt|;
name|bh
operator|.
name|consume
argument_list|(
name|o2
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"BAR"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|Object
name|o3
init|=
name|map
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
decl_stmt|;
name|bh
operator|.
name|consume
argument_list|(
name|o3
argument_list|)
expr_stmt|;
name|Object
name|o4
init|=
name|map
operator|.
name|get
argument_list|(
literal|"BAR"
argument_list|)
decl_stmt|;
name|bh
operator|.
name|consume
argument_list|(
name|o4
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Benchmark
DECL|method|hashMapSimpleCase (MapsBenchmarkState state, Blackhole bh)
specifier|public
name|void
name|hashMapSimpleCase
parameter_list|(
name|MapsBenchmarkState
name|state
parameter_list|,
name|Blackhole
name|bh
parameter_list|)
block|{
name|Map
name|map
init|=
name|state
operator|.
name|hashMap
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|Object
name|o1
init|=
name|map
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|bh
operator|.
name|consume
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|Object
name|o2
init|=
name|map
operator|.
name|get
argument_list|(
literal|"FOO"
argument_list|)
decl_stmt|;
name|bh
operator|.
name|consume
argument_list|(
name|o2
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"BAR"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|Object
name|o3
init|=
name|map
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
decl_stmt|;
name|bh
operator|.
name|consume
argument_list|(
name|o3
argument_list|)
expr_stmt|;
name|Object
name|o4
init|=
name|map
operator|.
name|get
argument_list|(
literal|"BAR"
argument_list|)
decl_stmt|;
name|bh
operator|.
name|consume
argument_list|(
name|o4
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Benchmark
DECL|method|camelMapComplexCase (MapsBenchmarkState mapsBenchmarkState, MapsSourceDataBenchmarkState sourceDataState, Blackhole blackhole)
specifier|public
name|void
name|camelMapComplexCase
parameter_list|(
name|MapsBenchmarkState
name|mapsBenchmarkState
parameter_list|,
name|MapsSourceDataBenchmarkState
name|sourceDataState
parameter_list|,
name|Blackhole
name|blackhole
parameter_list|)
block|{
comment|// step 1 - initialize map with existing elements
name|Map
name|map
init|=
name|mapsBenchmarkState
operator|.
name|camelMap
decl_stmt|;
comment|// step 2 - add elements one by one
name|sourceDataState
operator|.
name|map2
operator|.
name|entrySet
argument_list|()
operator|.
name|forEach
argument_list|(
name|entry
lambda|->
name|blackhole
operator|.
name|consume
argument_list|(
name|map
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// step 3 - remove elements one by one
name|sourceDataState
operator|.
name|map1
operator|.
name|keySet
argument_list|()
operator|.
name|forEach
argument_list|(
name|key
lambda|->
name|blackhole
operator|.
name|consume
argument_list|(
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// step 4 - remove elements one by one
name|sourceDataState
operator|.
name|map1
operator|.
name|keySet
argument_list|()
operator|.
name|forEach
argument_list|(
name|key
lambda|->
name|blackhole
operator|.
name|consume
argument_list|(
name|map
operator|.
name|remove
argument_list|(
name|key
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// step 5 - add couple of element at once
name|map
operator|.
name|putAll
argument_list|(
name|sourceDataState
operator|.
name|map1
argument_list|)
expr_stmt|;
name|blackhole
operator|.
name|consume
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Benchmark
DECL|method|cedarsoftMapComplexCase (MapsBenchmarkState mapsBenchmarkState, MapsSourceDataBenchmarkState sourceDataState, Blackhole blackhole)
specifier|public
name|void
name|cedarsoftMapComplexCase
parameter_list|(
name|MapsBenchmarkState
name|mapsBenchmarkState
parameter_list|,
name|MapsSourceDataBenchmarkState
name|sourceDataState
parameter_list|,
name|Blackhole
name|blackhole
parameter_list|)
block|{
comment|// step 1 - initialize map with existing elements
name|Map
name|map
init|=
name|mapsBenchmarkState
operator|.
name|cedarsoftMap
decl_stmt|;
comment|// step 2 - add elements one by one
name|sourceDataState
operator|.
name|map2
operator|.
name|entrySet
argument_list|()
operator|.
name|forEach
argument_list|(
name|entry
lambda|->
name|blackhole
operator|.
name|consume
argument_list|(
name|map
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// step 3 - remove elements one by one
name|sourceDataState
operator|.
name|map1
operator|.
name|keySet
argument_list|()
operator|.
name|forEach
argument_list|(
name|key
lambda|->
name|blackhole
operator|.
name|consume
argument_list|(
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// step 4 - remove elements one by one
name|sourceDataState
operator|.
name|map1
operator|.
name|keySet
argument_list|()
operator|.
name|forEach
argument_list|(
name|key
lambda|->
name|blackhole
operator|.
name|consume
argument_list|(
name|map
operator|.
name|remove
argument_list|(
name|key
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// step 5 - add couple of element at once
name|map
operator|.
name|putAll
argument_list|(
name|sourceDataState
operator|.
name|map1
argument_list|)
expr_stmt|;
name|blackhole
operator|.
name|consume
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Benchmark
DECL|method|hashMapComplexCase (MapsBenchmarkState mapsBenchmarkState, MapsSourceDataBenchmarkState sourceDataState, Blackhole blackhole)
specifier|public
name|void
name|hashMapComplexCase
parameter_list|(
name|MapsBenchmarkState
name|mapsBenchmarkState
parameter_list|,
name|MapsSourceDataBenchmarkState
name|sourceDataState
parameter_list|,
name|Blackhole
name|blackhole
parameter_list|)
block|{
comment|// step 1 - initialize map with existing elements
name|Map
name|map
init|=
name|mapsBenchmarkState
operator|.
name|hashMap
decl_stmt|;
comment|// step 2 - add elements one by one
name|sourceDataState
operator|.
name|map2
operator|.
name|entrySet
argument_list|()
operator|.
name|forEach
argument_list|(
name|entry
lambda|->
name|blackhole
operator|.
name|consume
argument_list|(
name|map
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// step 3 - remove elements one by one
name|sourceDataState
operator|.
name|map1
operator|.
name|keySet
argument_list|()
operator|.
name|forEach
argument_list|(
name|key
lambda|->
name|blackhole
operator|.
name|consume
argument_list|(
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// step 4 - remove elements one by one
name|sourceDataState
operator|.
name|map1
operator|.
name|keySet
argument_list|()
operator|.
name|forEach
argument_list|(
name|key
lambda|->
name|blackhole
operator|.
name|consume
argument_list|(
name|map
operator|.
name|remove
argument_list|(
name|key
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// step 5 - add couple of element at once
name|map
operator|.
name|putAll
argument_list|(
name|sourceDataState
operator|.
name|map1
argument_list|)
expr_stmt|;
name|blackhole
operator|.
name|consume
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

