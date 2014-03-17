begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hdfs2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hdfs2
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|apache
operator|.
name|hadoop
operator|.
name|conf
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|fs
operator|.
name|FSDataOutputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|fs
operator|.
name|FileSystem
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|fs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|io
operator|.
name|ArrayFile
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|io
operator|.
name|BooleanWritable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|io
operator|.
name|ByteWritable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|io
operator|.
name|BytesWritable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|io
operator|.
name|DoubleWritable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|io
operator|.
name|FloatWritable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|io
operator|.
name|IntWritable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|io
operator|.
name|LongWritable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|io
operator|.
name|NullWritable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|io
operator|.
name|SequenceFile
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|io
operator|.
name|SequenceFile
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|io
operator|.
name|Text
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|util
operator|.
name|Progressable
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
name|apache
operator|.
name|hadoop
operator|.
name|io
operator|.
name|SequenceFile
operator|.
name|CompressionType
import|;
end_import

begin_class
DECL|class|HdfsConsumerTest
specifier|public
class|class
name|HdfsConsumerTest
extends|extends
name|HdfsTestSupport
block|{
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// must be able to get security configuration
try|try
block|{
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|login
operator|.
name|Configuration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return;
block|}
name|deleteDirectory
argument_list|(
literal|"target/test"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSimpleConsumer ()
specifier|public
name|void
name|testSimpleConsumer
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
argument_list|()
condition|)
block|{
return|return;
block|}
specifier|final
name|Path
name|file
init|=
operator|new
name|Path
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-camel-normal-file"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
name|Configuration
name|conf
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|FileSystem
name|fs
init|=
name|FileSystem
operator|.
name|get
argument_list|(
name|file
operator|.
name|toUri
argument_list|()
argument_list|,
name|conf
argument_list|)
decl_stmt|;
name|FSDataOutputStream
name|out
init|=
name|fs
operator|.
name|create
argument_list|(
name|file
argument_list|)
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
literal|1024
condition|;
operator|++
name|i
control|)
block|{
name|out
operator|.
name|write
argument_list|(
operator|(
literal|"PIPPO"
operator|+
name|i
operator|)
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|MockEndpoint
name|resultEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"hdfs2:///"
operator|+
name|file
operator|.
name|toUri
argument_list|()
operator|+
literal|"?fileSystemType=LOCAL&chunkSize=4096&initialDelay=0"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReadBoolean ()
specifier|public
name|void
name|testReadBoolean
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
argument_list|()
condition|)
block|{
return|return;
block|}
specifier|final
name|Path
name|file
init|=
operator|new
name|Path
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-camel-boolean"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
name|Configuration
name|conf
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|SequenceFile
operator|.
name|Writer
name|writer
init|=
name|createWriter
argument_list|(
name|conf
argument_list|,
name|file
argument_list|,
name|NullWritable
operator|.
name|class
argument_list|,
name|BooleanWritable
operator|.
name|class
argument_list|)
decl_stmt|;
name|NullWritable
name|keyWritable
init|=
name|NullWritable
operator|.
name|get
argument_list|()
decl_stmt|;
name|BooleanWritable
name|valueWritable
init|=
operator|new
name|BooleanWritable
argument_list|()
decl_stmt|;
name|valueWritable
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|writer
operator|.
name|append
argument_list|(
name|keyWritable
argument_list|,
name|valueWritable
argument_list|)
expr_stmt|;
name|writer
operator|.
name|sync
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"hdfs2:///"
operator|+
name|file
operator|.
name|toUri
argument_list|()
operator|+
literal|"?fileSystemType=LOCAL&fileType=SEQUENCE_FILE&initialDelay=0"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MockEndpoint
name|resultEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReadByte ()
specifier|public
name|void
name|testReadByte
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
argument_list|()
condition|)
block|{
return|return;
block|}
specifier|final
name|Path
name|file
init|=
operator|new
name|Path
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-camel-byte"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
name|Configuration
name|conf
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|SequenceFile
operator|.
name|Writer
name|writer
init|=
name|createWriter
argument_list|(
name|conf
argument_list|,
name|file
argument_list|,
name|NullWritable
operator|.
name|class
argument_list|,
name|ByteWritable
operator|.
name|class
argument_list|)
decl_stmt|;
name|NullWritable
name|keyWritable
init|=
name|NullWritable
operator|.
name|get
argument_list|()
decl_stmt|;
name|ByteWritable
name|valueWritable
init|=
operator|new
name|ByteWritable
argument_list|()
decl_stmt|;
name|byte
name|value
init|=
literal|3
decl_stmt|;
name|valueWritable
operator|.
name|set
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|writer
operator|.
name|append
argument_list|(
name|keyWritable
argument_list|,
name|valueWritable
argument_list|)
expr_stmt|;
name|writer
operator|.
name|sync
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|MockEndpoint
name|resultEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|(
name|byte
operator|.
name|class
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"hdfs2:///"
operator|+
name|file
operator|.
name|toUri
argument_list|()
operator|+
literal|"?fileSystemType=LOCAL&fileType=SEQUENCE_FILE&initialDelay=0"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReadFloat ()
specifier|public
name|void
name|testReadFloat
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
argument_list|()
condition|)
block|{
return|return;
block|}
specifier|final
name|Path
name|file
init|=
operator|new
name|Path
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-camel-float"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
name|Configuration
name|conf
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|SequenceFile
operator|.
name|Writer
name|writer
init|=
name|createWriter
argument_list|(
name|conf
argument_list|,
name|file
argument_list|,
name|NullWritable
operator|.
name|class
argument_list|,
name|FloatWritable
operator|.
name|class
argument_list|)
decl_stmt|;
name|NullWritable
name|keyWritable
init|=
name|NullWritable
operator|.
name|get
argument_list|()
decl_stmt|;
name|FloatWritable
name|valueWritable
init|=
operator|new
name|FloatWritable
argument_list|()
decl_stmt|;
name|float
name|value
init|=
literal|3.1415926535f
decl_stmt|;
name|valueWritable
operator|.
name|set
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|writer
operator|.
name|append
argument_list|(
name|keyWritable
argument_list|,
name|valueWritable
argument_list|)
expr_stmt|;
name|writer
operator|.
name|sync
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|MockEndpoint
name|resultEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"hdfs2:///"
operator|+
name|file
operator|.
name|toUri
argument_list|()
operator|+
literal|"??fileSystemType=LOCAL&fileType=SEQUENCE_FILE&initialDelay=0"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReadDouble ()
specifier|public
name|void
name|testReadDouble
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
argument_list|()
condition|)
block|{
return|return;
block|}
specifier|final
name|Path
name|file
init|=
operator|new
name|Path
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-camel-double"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
name|Configuration
name|conf
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|SequenceFile
operator|.
name|Writer
name|writer
init|=
name|createWriter
argument_list|(
name|conf
argument_list|,
name|file
argument_list|,
name|NullWritable
operator|.
name|class
argument_list|,
name|DoubleWritable
operator|.
name|class
argument_list|)
decl_stmt|;
name|NullWritable
name|keyWritable
init|=
name|NullWritable
operator|.
name|get
argument_list|()
decl_stmt|;
name|DoubleWritable
name|valueWritable
init|=
operator|new
name|DoubleWritable
argument_list|()
decl_stmt|;
name|double
name|value
init|=
literal|3.1415926535
decl_stmt|;
name|valueWritable
operator|.
name|set
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|writer
operator|.
name|append
argument_list|(
name|keyWritable
argument_list|,
name|valueWritable
argument_list|)
expr_stmt|;
name|writer
operator|.
name|sync
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|MockEndpoint
name|resultEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"hdfs2:///"
operator|+
name|file
operator|.
name|toUri
argument_list|()
operator|+
literal|"??fileSystemType=LOCAL&fileType=SEQUENCE_FILE&initialDelay=0"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReadInt ()
specifier|public
name|void
name|testReadInt
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
argument_list|()
condition|)
block|{
return|return;
block|}
specifier|final
name|Path
name|file
init|=
operator|new
name|Path
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-camel-int"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
name|Configuration
name|conf
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|SequenceFile
operator|.
name|Writer
name|writer
init|=
name|createWriter
argument_list|(
name|conf
argument_list|,
name|file
argument_list|,
name|NullWritable
operator|.
name|class
argument_list|,
name|IntWritable
operator|.
name|class
argument_list|)
decl_stmt|;
name|NullWritable
name|keyWritable
init|=
name|NullWritable
operator|.
name|get
argument_list|()
decl_stmt|;
name|IntWritable
name|valueWritable
init|=
operator|new
name|IntWritable
argument_list|()
decl_stmt|;
name|int
name|value
init|=
literal|314159265
decl_stmt|;
name|valueWritable
operator|.
name|set
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|writer
operator|.
name|append
argument_list|(
name|keyWritable
argument_list|,
name|valueWritable
argument_list|)
expr_stmt|;
name|writer
operator|.
name|sync
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|MockEndpoint
name|resultEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"hdfs2:///"
operator|+
name|file
operator|.
name|toUri
argument_list|()
operator|+
literal|"?fileSystemType=LOCAL&fileType=SEQUENCE_FILE&initialDelay=0"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReadLong ()
specifier|public
name|void
name|testReadLong
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
argument_list|()
condition|)
block|{
return|return;
block|}
specifier|final
name|Path
name|file
init|=
operator|new
name|Path
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-camel-long"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
name|Configuration
name|conf
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|SequenceFile
operator|.
name|Writer
name|writer
init|=
name|createWriter
argument_list|(
name|conf
argument_list|,
name|file
argument_list|,
name|NullWritable
operator|.
name|class
argument_list|,
name|LongWritable
operator|.
name|class
argument_list|)
decl_stmt|;
name|NullWritable
name|keyWritable
init|=
name|NullWritable
operator|.
name|get
argument_list|()
decl_stmt|;
name|LongWritable
name|valueWritable
init|=
operator|new
name|LongWritable
argument_list|()
decl_stmt|;
name|long
name|value
init|=
literal|31415926535L
decl_stmt|;
name|valueWritable
operator|.
name|set
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|writer
operator|.
name|append
argument_list|(
name|keyWritable
argument_list|,
name|valueWritable
argument_list|)
expr_stmt|;
name|writer
operator|.
name|sync
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|MockEndpoint
name|resultEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"hdfs2:///"
operator|+
name|file
operator|.
name|toUri
argument_list|()
operator|+
literal|"?fileSystemType=LOCAL&fileType=SEQUENCE_FILE&initialDelay=0"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReadBytes ()
specifier|public
name|void
name|testReadBytes
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
argument_list|()
condition|)
block|{
return|return;
block|}
specifier|final
name|Path
name|file
init|=
operator|new
name|Path
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-camel-bytes"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
name|Configuration
name|conf
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|SequenceFile
operator|.
name|Writer
name|writer
init|=
name|createWriter
argument_list|(
name|conf
argument_list|,
name|file
argument_list|,
name|NullWritable
operator|.
name|class
argument_list|,
name|BytesWritable
operator|.
name|class
argument_list|)
decl_stmt|;
name|NullWritable
name|keyWritable
init|=
name|NullWritable
operator|.
name|get
argument_list|()
decl_stmt|;
name|BytesWritable
name|valueWritable
init|=
operator|new
name|BytesWritable
argument_list|()
decl_stmt|;
name|String
name|value
init|=
literal|"CIAO!"
decl_stmt|;
name|valueWritable
operator|.
name|set
argument_list|(
name|value
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|0
argument_list|,
name|value
operator|.
name|getBytes
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|writer
operator|.
name|append
argument_list|(
name|keyWritable
argument_list|,
name|valueWritable
argument_list|)
expr_stmt|;
name|writer
operator|.
name|sync
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|MockEndpoint
name|resultEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"hdfs2:///"
operator|+
name|file
operator|.
name|toUri
argument_list|()
operator|+
literal|"?fileSystemType=LOCAL&fileType=SEQUENCE_FILE&initialDelay=0"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReadString ()
specifier|public
name|void
name|testReadString
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
argument_list|()
condition|)
block|{
return|return;
block|}
specifier|final
name|Path
name|file
init|=
operator|new
name|Path
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-camel-string"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
name|Configuration
name|conf
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|SequenceFile
operator|.
name|Writer
name|writer
init|=
name|createWriter
argument_list|(
name|conf
argument_list|,
name|file
argument_list|,
name|NullWritable
operator|.
name|class
argument_list|,
name|Text
operator|.
name|class
argument_list|)
decl_stmt|;
name|NullWritable
name|keyWritable
init|=
name|NullWritable
operator|.
name|get
argument_list|()
decl_stmt|;
name|Text
name|valueWritable
init|=
operator|new
name|Text
argument_list|()
decl_stmt|;
name|String
name|value
init|=
literal|"CIAO!"
decl_stmt|;
name|valueWritable
operator|.
name|set
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|writer
operator|.
name|append
argument_list|(
name|keyWritable
argument_list|,
name|valueWritable
argument_list|)
expr_stmt|;
name|writer
operator|.
name|sync
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|MockEndpoint
name|resultEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"hdfs2:///"
operator|+
name|file
operator|.
name|toUri
argument_list|()
operator|+
literal|"?fileSystemType=LOCAL&fileType=SEQUENCE_FILE&initialDelay=0"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReadStringArrayFile ()
specifier|public
name|void
name|testReadStringArrayFile
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
argument_list|()
condition|)
block|{
return|return;
block|}
specifier|final
name|Path
name|file
init|=
operator|new
name|Path
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-camel-string"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
name|Configuration
name|conf
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|FileSystem
name|fs1
init|=
name|FileSystem
operator|.
name|get
argument_list|(
name|file
operator|.
name|toUri
argument_list|()
argument_list|,
name|conf
argument_list|)
decl_stmt|;
name|ArrayFile
operator|.
name|Writer
name|writer
init|=
operator|new
name|ArrayFile
operator|.
name|Writer
argument_list|(
name|conf
argument_list|,
name|fs1
argument_list|,
literal|"target/test/test-camel-string1"
argument_list|,
name|Text
operator|.
name|class
argument_list|,
name|CompressionType
operator|.
name|NONE
argument_list|,
operator|new
name|Progressable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|progress
parameter_list|()
block|{             }
block|}
argument_list|)
decl_stmt|;
name|Text
name|valueWritable
init|=
operator|new
name|Text
argument_list|()
decl_stmt|;
name|String
name|value
init|=
literal|"CIAO!"
decl_stmt|;
name|valueWritable
operator|.
name|set
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|writer
operator|.
name|append
argument_list|(
name|valueWritable
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|MockEndpoint
name|resultEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"hdfs2:///"
operator|+
name|file
operator|.
name|getParent
argument_list|()
operator|.
name|toUri
argument_list|()
operator|+
literal|"?fileSystemType=LOCAL&fileType=ARRAY_FILE&initialDelay=0"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
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
operator|!
name|canTest
argument_list|()
condition|)
block|{
return|return;
block|}
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|Configuration
name|conf
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|Path
name|dir
init|=
operator|new
name|Path
argument_list|(
literal|"target/test"
argument_list|)
decl_stmt|;
name|FileSystem
name|fs
init|=
name|FileSystem
operator|.
name|get
argument_list|(
name|dir
operator|.
name|toUri
argument_list|()
argument_list|,
name|conf
argument_list|)
decl_stmt|;
name|fs
operator|.
name|delete
argument_list|(
name|dir
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|createWriter (Configuration conf, Path file, Class<?> keyClass, Class<?> valueClass)
specifier|private
name|Writer
name|createWriter
parameter_list|(
name|Configuration
name|conf
parameter_list|,
name|Path
name|file
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|keyClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|valueClass
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|SequenceFile
operator|.
name|createWriter
argument_list|(
name|conf
argument_list|,
name|SequenceFile
operator|.
name|Writer
operator|.
name|file
argument_list|(
name|file
argument_list|)
argument_list|,
name|SequenceFile
operator|.
name|Writer
operator|.
name|keyClass
argument_list|(
name|keyClass
argument_list|)
argument_list|,
name|SequenceFile
operator|.
name|Writer
operator|.
name|valueClass
argument_list|(
name|valueClass
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

