begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
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
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|LinkOption
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|attribute
operator|.
name|PosixFilePermission
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|attribute
operator|.
name|PosixFilePermissions
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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

begin_class
DECL|class|FileProducerDirectoryChmodOptionTest
specifier|public
class|class
name|FileProducerDirectoryChmodOptionTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|TEST_DIRECTORY
specifier|public
specifier|static
specifier|final
name|String
name|TEST_DIRECTORY
init|=
literal|"target/data/chmoddir/foo/"
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
name|deleteDirectory
argument_list|(
name|TEST_DIRECTORY
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
DECL|method|canTest ()
specifier|private
name|boolean
name|canTest
parameter_list|()
block|{
comment|// can not run on windows
return|return
operator|!
name|isPlatform
argument_list|(
literal|"windows"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testWriteValidNoDir ()
specifier|public
name|void
name|testWriteValidNoDir
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
name|runChmodCheck
argument_list|(
literal|"NoDir"
argument_list|,
literal|null
argument_list|,
literal|"rwxr-xr-x"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWriteValidChmod0755 ()
specifier|public
name|void
name|testWriteValidChmod0755
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
name|runChmodCheck
argument_list|(
literal|"0755"
argument_list|,
literal|"rwxrwxrwx"
argument_list|,
literal|"rwxr-xr-x"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWriteValidChmod666 ()
specifier|public
name|void
name|testWriteValidChmod666
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
name|runChmodCheck
argument_list|(
literal|"666"
argument_list|,
literal|"rwxrwxrwx"
argument_list|,
literal|"rw-rw-rw-"
argument_list|)
expr_stmt|;
block|}
DECL|method|runChmodCheck (String routeSuffix, String expectedDirectoryPermissions, String expectedPermissions)
specifier|private
name|void
name|runChmodCheck
parameter_list|(
name|String
name|routeSuffix
parameter_list|,
name|String
name|expectedDirectoryPermissions
parameter_list|,
name|String
name|expectedPermissions
parameter_list|)
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:chmod"
operator|+
name|routeSuffix
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|String
name|testFileName
init|=
literal|"chmod"
operator|+
name|routeSuffix
operator|+
literal|".txt"
decl_stmt|;
name|String
name|fullTestFileName
init|=
name|TEST_DIRECTORY
operator|+
name|testFileName
decl_stmt|;
name|String
name|testFileContent
init|=
literal|"Writing file with chmod "
operator|+
name|routeSuffix
operator|+
literal|" option at "
operator|+
operator|new
name|Date
argument_list|()
decl_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
name|fullTestFileName
argument_list|,
name|testFileContent
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:write"
operator|+
name|routeSuffix
argument_list|,
name|testFileContent
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|testFileName
argument_list|)
expr_stmt|;
if|if
condition|(
name|expectedDirectoryPermissions
operator|!=
literal|null
condition|)
block|{
name|File
name|d
init|=
operator|new
name|File
argument_list|(
name|TEST_DIRECTORY
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|PosixFilePermission
argument_list|>
name|permissions
init|=
name|Files
operator|.
name|getPosixFilePermissions
argument_list|(
name|d
operator|.
name|toPath
argument_list|()
argument_list|,
name|LinkOption
operator|.
name|NOFOLLOW_LINKS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedDirectoryPermissions
argument_list|,
name|PosixFilePermissions
operator|.
name|toString
argument_list|(
name|permissions
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedDirectoryPermissions
operator|.
name|replace
argument_list|(
literal|"-"
argument_list|,
literal|""
argument_list|)
operator|.
name|length
argument_list|()
argument_list|,
name|permissions
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|expectedPermissions
operator|!=
literal|null
condition|)
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|fullTestFileName
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|PosixFilePermission
argument_list|>
name|permissions
init|=
name|Files
operator|.
name|getPosixFilePermissions
argument_list|(
name|f
operator|.
name|toPath
argument_list|()
argument_list|,
name|LinkOption
operator|.
name|NOFOLLOW_LINKS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedPermissions
argument_list|,
name|PosixFilePermissions
operator|.
name|toString
argument_list|(
name|permissions
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedPermissions
operator|.
name|replace
argument_list|(
literal|"-"
argument_list|,
literal|""
argument_list|)
operator|.
name|length
argument_list|()
argument_list|,
name|permissions
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
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
block|{
comment|// Valid chmod values
name|from
argument_list|(
literal|"direct:write666"
argument_list|)
operator|.
name|to
argument_list|(
literal|"file://"
operator|+
name|TEST_DIRECTORY
operator|+
literal|"?chmodDirectory=777&chmod=666"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:chmod666"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:write0755"
argument_list|)
operator|.
name|to
argument_list|(
literal|"file://"
operator|+
name|TEST_DIRECTORY
operator|+
literal|"?chmodDirectory=777&chmod=0755"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:chmod0755"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:writeNoDir"
argument_list|)
operator|.
name|to
argument_list|(
literal|"file://"
operator|+
name|TEST_DIRECTORY
operator|+
literal|"?chmod=0755"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:chmodNoDir"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

