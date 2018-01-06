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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|CamelLogger
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
name|net
operator|.
name|io
operator|.
name|CopyStreamEvent
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
name|net
operator|.
name|io
operator|.
name|CopyStreamListener
import|;
end_import

begin_comment
comment|/**  * Listener used for logging the progress of the upload or download of files.  */
end_comment

begin_class
DECL|class|FtpCopyStreamListener
specifier|public
class|class
name|FtpCopyStreamListener
implements|implements
name|CopyStreamListener
block|{
DECL|field|logger
specifier|private
specifier|final
name|CamelLogger
name|logger
decl_stmt|;
DECL|field|fileName
specifier|private
specifier|final
name|String
name|fileName
decl_stmt|;
DECL|field|download
specifier|private
specifier|final
name|boolean
name|download
decl_stmt|;
DECL|method|FtpCopyStreamListener (CamelLogger logger, String fileName, boolean download)
specifier|public
name|FtpCopyStreamListener
parameter_list|(
name|CamelLogger
name|logger
parameter_list|,
name|String
name|fileName
parameter_list|,
name|boolean
name|download
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
name|this
operator|.
name|fileName
operator|=
name|fileName
expr_stmt|;
name|this
operator|.
name|download
operator|=
name|download
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|bytesTransferred (CopyStreamEvent event)
specifier|public
name|void
name|bytesTransferred
parameter_list|(
name|CopyStreamEvent
name|event
parameter_list|)
block|{
comment|// not in use
block|}
annotation|@
name|Override
DECL|method|bytesTransferred (long totalBytesTransferred, int bytesTransferred, long streamSize)
specifier|public
name|void
name|bytesTransferred
parameter_list|(
name|long
name|totalBytesTransferred
parameter_list|,
name|int
name|bytesTransferred
parameter_list|,
name|long
name|streamSize
parameter_list|)
block|{
comment|// stream size is always -1 from the FTP client
if|if
condition|(
name|download
condition|)
block|{
name|logger
operator|.
name|log
argument_list|(
literal|"Downloading: "
operator|+
name|fileName
operator|+
literal|" (chunk: "
operator|+
name|bytesTransferred
operator|+
literal|", total chunk: "
operator|+
name|totalBytesTransferred
operator|+
literal|" bytes)"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|.
name|log
argument_list|(
literal|"Uploading: "
operator|+
name|fileName
operator|+
literal|" (chunk: "
operator|+
name|bytesTransferred
operator|+
literal|", total chunk: "
operator|+
name|totalBytesTransferred
operator|+
literal|" bytes)"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

