begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|net
operator|.
name|io
operator|.
name|CopyStreamListener
import|;
end_import

begin_comment
comment|/**  * Listener that captures the activity of the FTP Client such as connecting, login, upload and download etc.  */
end_comment

begin_interface
DECL|interface|FtpClientActivityListener
specifier|public
interface|interface
name|FtpClientActivityListener
extends|extends
name|CopyStreamListener
block|{
DECL|method|getLastLogActivity ()
name|String
name|getLastLogActivity
parameter_list|()
function_decl|;
DECL|method|getLastLogActivityTimestamp ()
name|long
name|getLastLogActivityTimestamp
parameter_list|()
function_decl|;
DECL|method|getLastVerboseLogActivity ()
name|String
name|getLastVerboseLogActivity
parameter_list|()
function_decl|;
DECL|method|getLastVerboseLogActivityTimestamp ()
name|long
name|getLastVerboseLogActivityTimestamp
parameter_list|()
function_decl|;
comment|/**      * Whether in download or upload mode      */
DECL|method|setDownload (boolean download)
name|void
name|setDownload
parameter_list|(
name|boolean
name|download
parameter_list|)
function_decl|;
DECL|method|setRemoteFileName (String fileName)
name|void
name|setRemoteFileName
parameter_list|(
name|String
name|fileName
parameter_list|)
function_decl|;
DECL|method|setRemoteFileSize (long size)
name|void
name|setRemoteFileSize
parameter_list|(
name|long
name|size
parameter_list|)
function_decl|;
DECL|method|onGeneralError (String host, String errorMessage)
name|void
name|onGeneralError
parameter_list|(
name|String
name|host
parameter_list|,
name|String
name|errorMessage
parameter_list|)
function_decl|;
DECL|method|onConnecting (String host)
name|void
name|onConnecting
parameter_list|(
name|String
name|host
parameter_list|)
function_decl|;
DECL|method|onConnected (String host)
name|void
name|onConnected
parameter_list|(
name|String
name|host
parameter_list|)
function_decl|;
DECL|method|onLogin (String host)
name|void
name|onLogin
parameter_list|(
name|String
name|host
parameter_list|)
function_decl|;
DECL|method|onLoginComplete (String host)
name|void
name|onLoginComplete
parameter_list|(
name|String
name|host
parameter_list|)
function_decl|;
DECL|method|onLoginFailed (int replyCode, String replyMessage)
name|void
name|onLoginFailed
parameter_list|(
name|int
name|replyCode
parameter_list|,
name|String
name|replyMessage
parameter_list|)
function_decl|;
DECL|method|onDisconnecting (String host)
name|void
name|onDisconnecting
parameter_list|(
name|String
name|host
parameter_list|)
function_decl|;
DECL|method|onDisconnected (String host)
name|void
name|onDisconnected
parameter_list|(
name|String
name|host
parameter_list|)
function_decl|;
DECL|method|onScanningForFiles (String host, String directory)
name|void
name|onScanningForFiles
parameter_list|(
name|String
name|host
parameter_list|,
name|String
name|directory
parameter_list|)
function_decl|;
DECL|method|onBeginDownloading (String host, String file)
name|void
name|onBeginDownloading
parameter_list|(
name|String
name|host
parameter_list|,
name|String
name|file
parameter_list|)
function_decl|;
DECL|method|onResumeDownloading (String host, String file, long position)
name|void
name|onResumeDownloading
parameter_list|(
name|String
name|host
parameter_list|,
name|String
name|file
parameter_list|,
name|long
name|position
parameter_list|)
function_decl|;
DECL|method|onDownload (String host, String file, long chunkSize, long totalChunkSize, long fileSize)
name|void
name|onDownload
parameter_list|(
name|String
name|host
parameter_list|,
name|String
name|file
parameter_list|,
name|long
name|chunkSize
parameter_list|,
name|long
name|totalChunkSize
parameter_list|,
name|long
name|fileSize
parameter_list|)
function_decl|;
DECL|method|onDownloadComplete (String host, String file)
name|void
name|onDownloadComplete
parameter_list|(
name|String
name|host
parameter_list|,
name|String
name|file
parameter_list|)
function_decl|;
DECL|method|onBeginUploading (String host, String file)
name|void
name|onBeginUploading
parameter_list|(
name|String
name|host
parameter_list|,
name|String
name|file
parameter_list|)
function_decl|;
DECL|method|onUpload (String host, String file, long chunkSize, long totalChunkSize, long fileSize)
name|void
name|onUpload
parameter_list|(
name|String
name|host
parameter_list|,
name|String
name|file
parameter_list|,
name|long
name|chunkSize
parameter_list|,
name|long
name|totalChunkSize
parameter_list|,
name|long
name|fileSize
parameter_list|)
function_decl|;
DECL|method|onUploadComplete (String host, String file)
name|void
name|onUploadComplete
parameter_list|(
name|String
name|host
parameter_list|,
name|String
name|file
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

