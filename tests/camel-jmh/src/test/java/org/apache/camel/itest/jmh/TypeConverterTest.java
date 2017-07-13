begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Scanner
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
name|w3c
operator|.
name|dom
operator|.
name|Document
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
comment|/**  * Tests {@link org.apache.camel.TypeConverter}  */
end_comment

begin_class
DECL|class|TypeConverterTest
specifier|public
class|class
name|TypeConverterTest
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
literal|3
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
literal|100000
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
DECL|class|BenchmarkCamelContextState
specifier|public
specifier|static
class|class
name|BenchmarkCamelContextState
block|{
DECL|field|someInteger
name|Integer
name|someInteger
init|=
literal|12345
decl_stmt|;
DECL|field|someIntegerString
name|String
name|someIntegerString
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|someInteger
argument_list|)
decl_stmt|;
DECL|field|xmlAsString
name|String
name|xmlAsString
decl_stmt|;
DECL|field|xmlAsBytes
name|byte
index|[]
name|xmlAsBytes
decl_stmt|;
DECL|field|camel
name|CamelContext
name|camel
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
name|IOException
block|{
name|camel
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
try|try
block|{
name|camel
operator|.
name|start
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
name|xmlAsString
operator|=
name|getResourceAsString
argument_list|(
literal|"sample_soap.xml"
argument_list|)
expr_stmt|;
name|xmlAsBytes
operator|=
name|xmlAsString
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
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
DECL|method|getResourceAsString (String resource)
specifier|private
name|String
name|getResourceAsString
parameter_list|(
name|String
name|resource
parameter_list|)
block|{
name|Scanner
name|s
init|=
operator|new
name|Scanner
argument_list|(
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|resource
argument_list|)
argument_list|)
operator|.
name|useDelimiter
argument_list|(
literal|"\\A"
argument_list|)
decl_stmt|;
return|return
name|s
operator|.
name|hasNext
argument_list|()
condition|?
name|s
operator|.
name|next
argument_list|()
else|:
literal|""
return|;
block|}
block|}
annotation|@
name|Benchmark
DECL|method|typeConvertIntegerToString (BenchmarkCamelContextState state, Blackhole bh)
specifier|public
name|void
name|typeConvertIntegerToString
parameter_list|(
name|BenchmarkCamelContextState
name|state
parameter_list|,
name|Blackhole
name|bh
parameter_list|)
block|{
name|String
name|string
init|=
name|state
operator|.
name|camel
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|state
operator|.
name|someInteger
argument_list|)
decl_stmt|;
name|bh
operator|.
name|consume
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Benchmark
DECL|method|typeConvertStringToInteger (BenchmarkCamelContextState state, Blackhole bh)
specifier|public
name|void
name|typeConvertStringToInteger
parameter_list|(
name|BenchmarkCamelContextState
name|state
parameter_list|,
name|Blackhole
name|bh
parameter_list|)
block|{
name|Integer
name|integer
init|=
name|state
operator|.
name|camel
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|state
operator|.
name|someIntegerString
argument_list|)
decl_stmt|;
name|bh
operator|.
name|consume
argument_list|(
name|integer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Benchmark
DECL|method|typeConvertTheSameTypes (BenchmarkCamelContextState state, Blackhole bh)
specifier|public
name|void
name|typeConvertTheSameTypes
parameter_list|(
name|BenchmarkCamelContextState
name|state
parameter_list|,
name|Blackhole
name|bh
parameter_list|)
block|{
name|String
name|string
init|=
name|state
operator|.
name|camel
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|state
operator|.
name|someIntegerString
argument_list|)
decl_stmt|;
name|bh
operator|.
name|consume
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Benchmark
DECL|method|typeConvertInputStreamToString (BenchmarkCamelContextState state, Blackhole bh)
specifier|public
name|void
name|typeConvertInputStreamToString
parameter_list|(
name|BenchmarkCamelContextState
name|state
parameter_list|,
name|Blackhole
name|bh
parameter_list|)
block|{
name|String
name|string
init|=
name|state
operator|.
name|camel
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|state
operator|.
name|xmlAsBytes
argument_list|)
argument_list|)
decl_stmt|;
name|bh
operator|.
name|consume
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Benchmark
DECL|method|typeConvertStringToInputStream (BenchmarkCamelContextState state, Blackhole bh)
specifier|public
name|void
name|typeConvertStringToInputStream
parameter_list|(
name|BenchmarkCamelContextState
name|state
parameter_list|,
name|Blackhole
name|bh
parameter_list|)
block|{
name|InputStream
name|inputStream
init|=
name|state
operator|.
name|camel
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|state
operator|.
name|xmlAsString
argument_list|)
decl_stmt|;
name|bh
operator|.
name|consume
argument_list|(
name|inputStream
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Benchmark
DECL|method|typeConvertStringToDocument (BenchmarkCamelContextState state, Blackhole bh)
specifier|public
name|void
name|typeConvertStringToDocument
parameter_list|(
name|BenchmarkCamelContextState
name|state
parameter_list|,
name|Blackhole
name|bh
parameter_list|)
block|{
name|Document
name|document
init|=
name|state
operator|.
name|camel
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Document
operator|.
name|class
argument_list|,
name|state
operator|.
name|xmlAsString
argument_list|)
decl_stmt|;
name|bh
operator|.
name|consume
argument_list|(
name|document
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Benchmark
DECL|method|typeConvertStringToByteArray (BenchmarkCamelContextState state, Blackhole bh)
specifier|public
name|void
name|typeConvertStringToByteArray
parameter_list|(
name|BenchmarkCamelContextState
name|state
parameter_list|,
name|Blackhole
name|bh
parameter_list|)
block|{
name|byte
index|[]
name|bytes
init|=
name|state
operator|.
name|camel
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|state
operator|.
name|xmlAsString
argument_list|)
decl_stmt|;
name|bh
operator|.
name|consume
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Benchmark
DECL|method|typeConvertByteArrayToString (BenchmarkCamelContextState state, Blackhole bh)
specifier|public
name|void
name|typeConvertByteArrayToString
parameter_list|(
name|BenchmarkCamelContextState
name|state
parameter_list|,
name|Blackhole
name|bh
parameter_list|)
block|{
name|String
name|string
init|=
name|state
operator|.
name|camel
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|state
operator|.
name|xmlAsBytes
argument_list|)
decl_stmt|;
name|bh
operator|.
name|consume
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

