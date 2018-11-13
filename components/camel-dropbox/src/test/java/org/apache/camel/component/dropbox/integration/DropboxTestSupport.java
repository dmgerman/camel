begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dropbox.integration
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dropbox
operator|.
name|integration
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
name|ByteArrayOutputStream
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
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|com
operator|.
name|dropbox
operator|.
name|core
operator|.
name|DbxDownloader
import|;
end_import

begin_import
import|import
name|com
operator|.
name|dropbox
operator|.
name|core
operator|.
name|DbxException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|dropbox
operator|.
name|core
operator|.
name|DbxRequestConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|dropbox
operator|.
name|core
operator|.
name|v2
operator|.
name|DbxClientV2
import|;
end_import

begin_import
import|import
name|com
operator|.
name|dropbox
operator|.
name|core
operator|.
name|v2
operator|.
name|files
operator|.
name|FileMetadata
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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

begin_class
DECL|class|DropboxTestSupport
specifier|public
class|class
name|DropboxTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|properties
specifier|protected
specifier|final
name|Properties
name|properties
decl_stmt|;
DECL|field|workdir
specifier|protected
name|String
name|workdir
decl_stmt|;
DECL|field|token
specifier|protected
name|String
name|token
decl_stmt|;
DECL|field|client
specifier|private
name|DbxClientV2
name|client
decl_stmt|;
DECL|method|DropboxTestSupport ()
specifier|protected
name|DropboxTestSupport
parameter_list|()
block|{
name|properties
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
try|try
init|(
name|InputStream
name|inStream
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/test-options.properties"
argument_list|)
init|)
block|{
name|properties
operator|.
name|load
argument_list|(
name|inStream
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IllegalAccessError
argument_list|(
literal|"test-options.properties could not be found"
argument_list|)
throw|;
block|}
name|workdir
operator|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"workDir"
argument_list|)
expr_stmt|;
name|token
operator|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"accessToken"
argument_list|)
expr_stmt|;
name|DbxRequestConfig
name|config
init|=
name|DbxRequestConfig
operator|.
name|newBuilder
argument_list|(
name|properties
operator|.
name|getProperty
argument_list|(
literal|"clientIdentifier"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|client
operator|=
operator|new
name|DbxClientV2
argument_list|(
name|config
argument_list|,
name|token
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
DECL|method|setUpWorkingFolder ()
specifier|public
name|void
name|setUpWorkingFolder
parameter_list|()
throws|throws
name|DbxException
block|{
name|createDir
argument_list|(
name|workdir
argument_list|)
expr_stmt|;
block|}
DECL|method|createDir (String name)
specifier|protected
name|void
name|createDir
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|DbxException
block|{
try|try
block|{
name|removeDir
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|client
operator|.
name|files
argument_list|()
operator|.
name|createFolder
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|removeDir (String name)
specifier|protected
name|void
name|removeDir
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|DbxException
block|{
name|client
operator|.
name|files
argument_list|()
operator|.
name|delete
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
DECL|method|createFile (String fileName, String content)
specifier|protected
name|void
name|createFile
parameter_list|(
name|String
name|fileName
parameter_list|,
name|String
name|content
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|client
operator|.
name|files
argument_list|()
operator|.
name|uploadBuilder
argument_list|(
name|workdir
operator|+
literal|"/"
operator|+
name|fileName
argument_list|)
operator|.
name|uploadAndFinish
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|content
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DbxException
name|e
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"folder is already created"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getFileContent (String path)
specifier|protected
name|String
name|getFileContent
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|DbxException
throws|,
name|IOException
block|{
name|ByteArrayOutputStream
name|target
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|DbxDownloader
argument_list|<
name|FileMetadata
argument_list|>
name|downloadedFile
init|=
name|client
operator|.
name|files
argument_list|()
operator|.
name|download
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|downloadedFile
operator|!=
literal|null
condition|)
block|{
name|downloadedFile
operator|.
name|download
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|String
argument_list|(
name|target
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|useOverridePropertiesWithPropertiesComponent ()
specifier|protected
name|Properties
name|useOverridePropertiesWithPropertiesComponent
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
block|}
end_class

end_unit

