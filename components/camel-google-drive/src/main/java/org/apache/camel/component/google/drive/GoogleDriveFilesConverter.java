begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.drive
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|drive
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
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|client
operator|.
name|http
operator|.
name|FileContent
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|client
operator|.
name|http
operator|.
name|GenericUrl
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|client
operator|.
name|http
operator|.
name|HttpResponse
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|client
operator|.
name|http
operator|.
name|InputStreamContent
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|drive
operator|.
name|Drive
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
name|Converter
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
name|component
operator|.
name|file
operator|.
name|GenericFile
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

begin_class
annotation|@
name|Converter
DECL|class|GoogleDriveFilesConverter
specifier|public
specifier|final
class|class
name|GoogleDriveFilesConverter
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|GoogleDriveFilesConverter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|GoogleDriveFilesConverter ()
specifier|private
name|GoogleDriveFilesConverter
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|genericFileToGoogleDriveFile (GenericFile<?> file, Exchange exchange)
specifier|public
specifier|static
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|drive
operator|.
name|model
operator|.
name|File
name|genericFileToGoogleDriveFile
parameter_list|(
name|GenericFile
argument_list|<
name|?
argument_list|>
name|file
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|file
operator|.
name|getFile
argument_list|()
operator|instanceof
name|File
condition|)
block|{
name|File
name|f
init|=
operator|(
name|File
operator|)
name|file
operator|.
name|getFile
argument_list|()
decl_stmt|;
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|drive
operator|.
name|model
operator|.
name|File
name|fileMetadata
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|drive
operator|.
name|model
operator|.
name|File
argument_list|()
decl_stmt|;
name|fileMetadata
operator|.
name|setTitle
argument_list|(
name|f
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|FileContent
name|mediaContent
init|=
operator|new
name|FileContent
argument_list|(
literal|null
argument_list|,
name|f
argument_list|)
decl_stmt|;
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"CamelGoogleDrive.content"
argument_list|,
name|fileMetadata
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"CamelGoogleDrive.mediaContent"
argument_list|,
name|mediaContent
argument_list|)
expr_stmt|;
block|}
return|return
name|fileMetadata
return|;
block|}
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
comment|// body wasn't a java.io.File so let's try to convert it
name|file
operator|.
name|getBinding
argument_list|()
operator|.
name|loadContent
argument_list|(
name|exchange
argument_list|,
name|file
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
name|exchange
operator|.
name|getContext
argument_list|()
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
name|exchange
argument_list|,
name|file
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|drive
operator|.
name|model
operator|.
name|File
name|fileMetadata
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|drive
operator|.
name|model
operator|.
name|File
argument_list|()
decl_stmt|;
name|fileMetadata
operator|.
name|setTitle
argument_list|(
name|file
operator|.
name|getFileName
argument_list|()
argument_list|)
expr_stmt|;
name|InputStreamContent
name|mediaContent
init|=
operator|new
name|InputStreamContent
argument_list|(
literal|null
argument_list|,
name|is
argument_list|)
decl_stmt|;
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"CamelGoogleDrive.content"
argument_list|,
name|fileMetadata
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"CamelGoogleDrive.mediaContent"
argument_list|,
name|mediaContent
argument_list|)
expr_stmt|;
block|}
return|return
name|fileMetadata
return|;
block|}
return|return
literal|null
return|;
block|}
comment|// convenience method that takes google file metadata and converts that to an inputstream
annotation|@
name|Converter
DECL|method|download (com.google.api.services.drive.model.File fileMetadata, Exchange exchange)
specifier|public
specifier|static
name|InputStream
name|download
parameter_list|(
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|drive
operator|.
name|model
operator|.
name|File
name|fileMetadata
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|fileMetadata
operator|.
name|getDownloadUrl
argument_list|()
operator|!=
literal|null
operator|&&
name|fileMetadata
operator|.
name|getDownloadUrl
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
try|try
block|{
comment|// TODO maybe separate this out as custom drive API ex. google-drive://download...
name|HttpResponse
name|resp
init|=
name|getClient
argument_list|(
name|exchange
argument_list|)
operator|.
name|getRequestFactory
argument_list|()
operator|.
name|buildGetRequest
argument_list|(
operator|new
name|GenericUrl
argument_list|(
name|fileMetadata
operator|.
name|getDownloadUrl
argument_list|()
argument_list|)
argument_list|)
operator|.
name|execute
argument_list|()
decl_stmt|;
return|return
name|resp
operator|.
name|getContent
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Could not download file."
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
else|else
block|{
comment|// The file doesn't have any content stored on Drive.
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Converter
DECL|method|downloadContentAsString (com.google.api.services.drive.model.File fileMetadata, Exchange exchange)
specifier|public
specifier|static
name|String
name|downloadContentAsString
parameter_list|(
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|drive
operator|.
name|model
operator|.
name|File
name|fileMetadata
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|InputStream
name|is
init|=
name|download
argument_list|(
name|fileMetadata
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
return|return
name|exchange
operator|.
name|getContext
argument_list|()
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
name|exchange
argument_list|,
name|is
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Converter
DECL|method|genericStringToChildReference (String payload, Exchange exchange)
specifier|public
specifier|static
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|drive
operator|.
name|model
operator|.
name|ChildReference
name|genericStringToChildReference
parameter_list|(
name|String
name|payload
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|payload
operator|!=
literal|null
condition|)
block|{
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|drive
operator|.
name|model
operator|.
name|ChildReference
name|childReference
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|drive
operator|.
name|model
operator|.
name|ChildReference
argument_list|()
decl_stmt|;
name|childReference
operator|.
name|setId
argument_list|(
name|payload
argument_list|)
expr_stmt|;
return|return
name|childReference
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getClient (Exchange exchange)
specifier|private
specifier|static
name|Drive
name|getClient
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|GoogleDriveComponent
name|component
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"google-drive"
argument_list|,
name|GoogleDriveComponent
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|component
operator|.
name|getClient
argument_list|(
name|component
operator|.
name|getConfiguration
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

