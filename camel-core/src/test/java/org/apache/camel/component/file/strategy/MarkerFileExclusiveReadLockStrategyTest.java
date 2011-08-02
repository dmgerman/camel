begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.strategy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
operator|.
name|strategy
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
name|FileOutputStream
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Tests the MarkerFileExclusiveReadLockStrategy in a multi-threaded scenario.  */
end_comment

begin_class
DECL|class|MarkerFileExclusiveReadLockStrategyTest
specifier|public
class|class
name|MarkerFileExclusiveReadLockStrategyTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MarkerFileExclusiveReadLockStrategyTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|NUMBER_OF_THREADS
specifier|private
specifier|static
specifier|final
name|int
name|NUMBER_OF_THREADS
init|=
literal|5
decl_stmt|;
DECL|field|numberOfFilesProcessed
specifier|private
name|AtomicInteger
name|numberOfFilesProcessed
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
DECL|method|testMultithreadedLocking ()
specifier|public
name|void
name|testMultithreadedLocking
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/marker/"
argument_list|)
expr_stmt|;
name|createDirectory
argument_list|(
literal|"target/marker/in"
argument_list|)
expr_stmt|;
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
literal|2
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
literal|"target/marker/out/file1.dat"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
literal|"target/marker/out/file2.dat"
argument_list|)
expr_stmt|;
name|writeFiles
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|String
name|content
init|=
name|context
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
name|File
argument_list|(
literal|"target/marker/out/file1.dat"
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
argument_list|)
decl_stmt|;
name|String
index|[]
name|lines
init|=
name|content
operator|.
name|split
argument_list|(
name|LS
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
literal|20
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
literal|"Line "
operator|+
name|i
argument_list|,
name|lines
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|content
operator|=
name|context
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
name|File
argument_list|(
literal|"target/marker/out/file2.dat"
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
argument_list|)
expr_stmt|;
name|lines
operator|=
name|content
operator|.
name|split
argument_list|(
name|LS
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
literal|20
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
literal|"Line "
operator|+
name|i
argument_list|,
name|lines
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|waitUntilCompleted
argument_list|()
expr_stmt|;
name|assertFileDoesNotExists
argument_list|(
literal|"target/marker/in/file1.dat.camelLock"
argument_list|)
expr_stmt|;
name|assertFileDoesNotExists
argument_list|(
literal|"target/marker/in/file2.dat.camelLock"
argument_list|)
expr_stmt|;
name|assertFileDoesNotExists
argument_list|(
literal|"target/marker/in/file1.dat"
argument_list|)
expr_stmt|;
name|assertFileDoesNotExists
argument_list|(
literal|"target/marker/in/file2.dat"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|this
operator|.
name|numberOfFilesProcessed
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|writeFiles ()
specifier|private
name|void
name|writeFiles
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Writing files..."
argument_list|)
expr_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
literal|"target/marker/in/file1.dat"
argument_list|)
decl_stmt|;
name|FileOutputStream
name|fos2
init|=
operator|new
name|FileOutputStream
argument_list|(
literal|"target/marker/in/file2.dat"
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
literal|20
condition|;
name|i
operator|++
control|)
block|{
name|fos
operator|.
name|write
argument_list|(
operator|(
literal|"Line "
operator|+
name|i
operator|+
name|LS
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|fos2
operator|.
name|write
argument_list|(
operator|(
literal|"Line "
operator|+
name|i
operator|+
name|LS
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Writing line "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|fos
operator|.
name|flush
argument_list|()
expr_stmt|;
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
name|fos2
operator|.
name|flush
argument_list|()
expr_stmt|;
name|fos2
operator|.
name|close
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
name|from
argument_list|(
literal|"file:target/marker/in?readLock=markerFile"
argument_list|)
operator|.
name|onCompletion
argument_list|()
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|numberOfFilesProcessed
operator|.
name|addAndGet
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|threads
argument_list|(
name|NUMBER_OF_THREADS
argument_list|)
operator|.
name|to
argument_list|(
literal|"file:target/marker/out"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|waitUntilCompleted ()
specifier|private
name|void
name|waitUntilCompleted
parameter_list|()
block|{
while|while
condition|(
name|this
operator|.
name|numberOfFilesProcessed
operator|.
name|get
argument_list|()
operator|<
literal|2
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|10
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
DECL|method|assertFileDoesNotExists (String filename)
specifier|private
specifier|static
name|void
name|assertFileDoesNotExists
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|filename
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"File "
operator|+
name|filename
operator|+
literal|" should not exist, it should have been deleted after being processed"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

