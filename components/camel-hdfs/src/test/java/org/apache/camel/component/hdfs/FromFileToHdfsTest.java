begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hdfs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hdfs
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
name|builder
operator|.
name|NotifyBuilder
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
name|junit
operator|.
name|After
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|FromFileToHdfsTest
specifier|public
class|class
name|FromFileToHdfsTest
extends|extends
name|HdfsTestSupport
block|{
DECL|field|TEMP_DIR
specifier|private
specifier|static
specifier|final
name|Path
name|TEMP_DIR
init|=
operator|new
name|Path
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/outbox/"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Override
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
name|deleteDirectory
argument_list|(
literal|"target/inbox"
argument_list|)
expr_stmt|;
name|deleteDirectory
argument_list|(
literal|"target/outbox"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|After
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
literal|"target/outbox"
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
annotation|@
name|Test
DECL|method|testFileToHdfs ()
specifier|public
name|void
name|testFileToHdfs
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
name|NotifyBuilder
name|notify
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|whenDone
argument_list|(
literal|1
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/inbox"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|notify
operator|.
name|matchesWaitTime
argument_list|()
expr_stmt|;
name|File
name|delete
init|=
operator|new
name|File
argument_list|(
literal|"target/inbox/hello.txt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File should be deleted "
operator|+
name|delete
argument_list|,
operator|!
name|delete
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|create
init|=
operator|new
name|File
argument_list|(
name|TEMP_DIR
operator|+
literal|"/output.txt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File should be created "
operator|+
name|create
argument_list|,
name|create
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTwoFilesToHdfs ()
specifier|public
name|void
name|testTwoFilesToHdfs
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
name|NotifyBuilder
name|notify
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|whenDone
argument_list|(
literal|2
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/inbox"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/inbox"
argument_list|,
literal|"Bye World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"bye.txt"
argument_list|)
expr_stmt|;
name|notify
operator|.
name|matchesWaitTime
argument_list|()
expr_stmt|;
name|File
name|delete
init|=
operator|new
name|File
argument_list|(
literal|"target/inbox/hello.txt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File should be deleted "
operator|+
name|delete
argument_list|,
operator|!
name|delete
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|delete
operator|=
operator|new
name|File
argument_list|(
literal|"target/inbox/bye.txt"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"File should be deleted "
operator|+
name|delete
argument_list|,
operator|!
name|delete
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|create
init|=
operator|new
name|File
argument_list|(
name|TEMP_DIR
operator|+
literal|"/output.txt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File should be created "
operator|+
name|create
argument_list|,
name|create
operator|.
name|exists
argument_list|()
argument_list|)
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
literal|"file:target/inbox?delete=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"hdfs:localhost/"
operator|+
name|TEMP_DIR
operator|.
name|toUri
argument_list|()
operator|+
literal|"/output.txt?fileSystemType=LOCAL"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

