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
name|util
operator|.
name|concurrent
operator|.
name|ScheduledExecutorService
import|;
end_import

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

begin_class
DECL|class|SftpConsumer
specifier|public
class|class
name|SftpConsumer
extends|extends
name|RemoteFileConsumer
argument_list|<
name|RemoteFileExchange
argument_list|>
block|{
DECL|field|recursive
specifier|private
name|boolean
name|recursive
init|=
literal|true
decl_stmt|;
DECL|field|regexPattern
specifier|private
name|String
name|regexPattern
init|=
literal|""
decl_stmt|;
DECL|field|lastPollTime
specifier|private
name|long
name|lastPollTime
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|SftpEndpoint
name|endpoint
decl_stmt|;
DECL|field|channel
specifier|private
name|ChannelSftp
name|channel
decl_stmt|;
DECL|method|SftpConsumer (SftpEndpoint endpoint, Processor processor, ChannelSftp channel)
specifier|public
name|SftpConsumer
parameter_list|(
name|SftpEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ChannelSftp
name|channel
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|channel
operator|=
name|channel
expr_stmt|;
block|}
DECL|method|SftpConsumer (SftpEndpoint endpoint, Processor processor, ChannelSftp channel, ScheduledExecutorService executor)
specifier|public
name|SftpConsumer
parameter_list|(
name|SftpEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ChannelSftp
name|channel
parameter_list|,
name|ScheduledExecutorService
name|executor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|,
name|executor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|channel
operator|=
name|channel
expr_stmt|;
block|}
DECL|method|poll ()
specifier|protected
name|void
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|fileName
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|pollDirectory
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|channel
operator|.
name|cd
argument_list|(
name|fileName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|fileName
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|ChannelSftp
operator|.
name|LsEntry
name|file
init|=
operator|(
name|ChannelSftp
operator|.
name|LsEntry
operator|)
name|channel
operator|.
name|ls
argument_list|(
name|fileName
operator|.
name|substring
argument_list|(
name|fileName
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
operator|+
literal|1
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|pollFile
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
name|lastPollTime
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
block|}
DECL|method|pollDirectory (String dir)
specifier|protected
name|void
name|pollDirectory
parameter_list|(
name|String
name|dir
parameter_list|)
throws|throws
name|Exception
block|{
name|channel
operator|.
name|cd
argument_list|(
name|dir
argument_list|)
expr_stmt|;
for|for
control|(
name|ChannelSftp
operator|.
name|LsEntry
name|sftpFile
range|:
operator|(
name|ChannelSftp
operator|.
name|LsEntry
index|[]
operator|)
name|channel
operator|.
name|ls
argument_list|(
literal|"."
argument_list|)
operator|.
name|toArray
argument_list|(
operator|new
name|ChannelSftp
operator|.
name|LsEntry
index|[]
block|{}
argument_list|)
control|)
block|{
if|if
condition|(
name|sftpFile
operator|.
name|getFilename
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
comment|// skip
block|}
elseif|else
if|if
condition|(
name|sftpFile
operator|.
name|getAttrs
argument_list|()
operator|.
name|isDir
argument_list|()
condition|)
block|{
if|if
condition|(
name|isRecursive
argument_list|()
condition|)
block|{
name|pollDirectory
argument_list|(
name|getFullFileName
argument_list|(
name|sftpFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|pollFile
argument_list|(
name|sftpFile
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getFullFileName (ChannelSftp.LsEntry sftpFile)
specifier|protected
name|String
name|getFullFileName
parameter_list|(
name|ChannelSftp
operator|.
name|LsEntry
name|sftpFile
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|channel
operator|.
name|pwd
argument_list|()
operator|+
literal|"/"
operator|+
name|sftpFile
operator|.
name|getFilename
argument_list|()
return|;
block|}
DECL|method|pollFile (ChannelSftp.LsEntry sftpFile)
specifier|private
name|void
name|pollFile
parameter_list|(
name|ChannelSftp
operator|.
name|LsEntry
name|sftpFile
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|sftpFile
operator|.
name|getAttrs
argument_list|()
operator|.
name|getMTime
argument_list|()
operator|*
literal|1000
operator|>
name|lastPollTime
condition|)
block|{
comment|// TODO do
comment|// we need
comment|// to adjust
comment|// the TZ?
if|if
condition|(
name|isMatched
argument_list|(
name|sftpFile
argument_list|)
condition|)
block|{
specifier|final
name|ByteArrayOutputStream
name|byteArrayOutputStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|channel
operator|.
name|get
argument_list|(
name|sftpFile
operator|.
name|getFilename
argument_list|()
argument_list|,
name|byteArrayOutputStream
argument_list|)
expr_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|endpoint
operator|.
name|createExchange
argument_list|(
name|getFullFileName
argument_list|(
name|sftpFile
argument_list|)
argument_list|,
name|byteArrayOutputStream
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|isMatched (ChannelSftp.LsEntry sftpFile)
specifier|protected
name|boolean
name|isMatched
parameter_list|(
name|ChannelSftp
operator|.
name|LsEntry
name|sftpFile
parameter_list|)
block|{
name|boolean
name|result
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|regexPattern
operator|!=
literal|null
operator|&&
name|regexPattern
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|result
operator|=
name|sftpFile
operator|.
name|getFilename
argument_list|()
operator|.
name|matches
argument_list|(
name|getRegexPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|isRecursive ()
specifier|public
name|boolean
name|isRecursive
parameter_list|()
block|{
return|return
name|recursive
return|;
block|}
DECL|method|setRecursive (boolean recursive)
specifier|public
name|void
name|setRecursive
parameter_list|(
name|boolean
name|recursive
parameter_list|)
block|{
name|this
operator|.
name|recursive
operator|=
name|recursive
expr_stmt|;
block|}
DECL|method|getLastPollTime ()
specifier|public
name|long
name|getLastPollTime
parameter_list|()
block|{
return|return
name|lastPollTime
return|;
block|}
DECL|method|setLastPollTime (long lastPollTime)
specifier|public
name|void
name|setLastPollTime
parameter_list|(
name|long
name|lastPollTime
parameter_list|)
block|{
name|this
operator|.
name|lastPollTime
operator|=
name|lastPollTime
expr_stmt|;
block|}
DECL|method|getRegexPattern ()
specifier|public
name|String
name|getRegexPattern
parameter_list|()
block|{
return|return
name|regexPattern
return|;
block|}
DECL|method|setRegexPattern (String regexPattern)
specifier|public
name|void
name|setRegexPattern
parameter_list|(
name|String
name|regexPattern
parameter_list|)
block|{
name|this
operator|.
name|regexPattern
operator|=
name|regexPattern
expr_stmt|;
block|}
block|}
end_class

end_unit

