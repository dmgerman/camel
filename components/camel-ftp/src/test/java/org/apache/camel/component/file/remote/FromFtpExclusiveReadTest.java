begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote
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
name|remote
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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Unit test to verify exclusive read - that we do not poll files that is in progress of being written.  */
end_comment

begin_class
DECL|class|FromFtpExclusiveReadTest
specifier|public
class|class
name|FromFtpExclusiveReadTest
extends|extends
name|FtpServerTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|FromFtpExclusiveReadTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|port
specifier|private
name|String
name|port
init|=
literal|"20025"
decl_stmt|;
DECL|field|ftpUrl
specifier|private
name|String
name|ftpUrl
init|=
literal|"ftp://admin@localhost:"
operator|+
name|port
operator|+
literal|"/slowfile?password=admin&binary=false&consumer.exclusiveRead=true&consumer.delay=500"
decl_stmt|;
DECL|method|getPort ()
specifier|public
name|String
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|testPollFileWhileSlowFileIsBeingWritten ()
specifier|public
name|void
name|testPollFileWhileSlowFileIsBeingWritten
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"./res/home"
argument_list|)
expr_stmt|;
name|createDirectory
argument_list|(
literal|"./res/home/slowfile"
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
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello WorldLine #0Line #1Line #2Bye World"
argument_list|)
expr_stmt|;
name|createSlowFile
argument_list|()
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|createSlowFile ()
specifier|private
name|void
name|createSlowFile
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Creating a slow file ..."
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"./res/home/slowfile/hello.txt"
argument_list|)
decl_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|fos
operator|.
name|write
argument_list|(
literal|"Hello World"
operator|.
name|getBytes
argument_list|()
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
literal|3
condition|;
name|i
operator|++
control|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|fos
operator|.
name|write
argument_list|(
operator|(
literal|"Line #"
operator|+
name|i
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Appending to slowfile"
argument_list|)
expr_stmt|;
block|}
name|fos
operator|.
name|write
argument_list|(
literal|"Bye World"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"... done creating slowfile"
argument_list|)
expr_stmt|;
block|}
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|ftpUrl
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
DECL|method|createDirectory (String s)
specifier|private
specifier|static
name|void
name|createDirectory
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|file
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

