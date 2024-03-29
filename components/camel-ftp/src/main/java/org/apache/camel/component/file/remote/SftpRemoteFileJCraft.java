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
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|ChannelSftp
import|;
end_import

begin_class
DECL|class|SftpRemoteFileJCraft
specifier|public
class|class
name|SftpRemoteFileJCraft
implements|implements
name|SftpRemoteFile
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
block|{
DECL|field|entry
specifier|private
specifier|final
name|ChannelSftp
operator|.
name|LsEntry
name|entry
decl_stmt|;
DECL|method|SftpRemoteFileJCraft (ChannelSftp.LsEntry entry)
specifier|public
name|SftpRemoteFileJCraft
parameter_list|(
name|ChannelSftp
operator|.
name|LsEntry
name|entry
parameter_list|)
block|{
name|this
operator|.
name|entry
operator|=
name|entry
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRemoteFile ()
specifier|public
name|ChannelSftp
operator|.
name|LsEntry
name|getRemoteFile
parameter_list|()
block|{
return|return
name|entry
return|;
block|}
annotation|@
name|Override
DECL|method|getFilename ()
specifier|public
name|String
name|getFilename
parameter_list|()
block|{
return|return
name|entry
operator|.
name|getFilename
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getLongname ()
specifier|public
name|String
name|getLongname
parameter_list|()
block|{
return|return
name|entry
operator|.
name|getLongname
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isDirectory ()
specifier|public
name|boolean
name|isDirectory
parameter_list|()
block|{
return|return
name|entry
operator|.
name|getAttrs
argument_list|()
operator|.
name|isDir
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getFileLength ()
specifier|public
name|long
name|getFileLength
parameter_list|()
block|{
return|return
name|entry
operator|.
name|getAttrs
argument_list|()
operator|.
name|getSize
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getLastModified ()
specifier|public
name|long
name|getLastModified
parameter_list|()
block|{
return|return
name|entry
operator|.
name|getAttrs
argument_list|()
operator|.
name|getMTime
argument_list|()
operator|*
literal|1000L
return|;
block|}
block|}
end_class

end_unit

