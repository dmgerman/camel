begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hdfs2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hdfs2
package|;
end_package

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
name|locks
operator|.
name|ReadWriteLock
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
name|locks
operator|.
name|ReentrantReadWriteLock
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|login
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
name|Message
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
name|impl
operator|.
name|DefaultMessage
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
name|impl
operator|.
name|ScheduledPollConsumer
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
name|util
operator|.
name|IOHelper
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
name|lang
operator|.
name|StringUtils
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
name|FileStatus
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
name|apache
operator|.
name|hadoop
operator|.
name|fs
operator|.
name|PathFilter
import|;
end_import

begin_class
DECL|class|HdfsConsumer
specifier|public
specifier|final
class|class
name|HdfsConsumer
extends|extends
name|ScheduledPollConsumer
block|{
DECL|field|config
specifier|private
specifier|final
name|HdfsConfiguration
name|config
decl_stmt|;
DECL|field|hdfsPath
specifier|private
specifier|final
name|StringBuilder
name|hdfsPath
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|field|rwlock
specifier|private
specifier|final
name|ReadWriteLock
name|rwlock
init|=
operator|new
name|ReentrantReadWriteLock
argument_list|()
decl_stmt|;
DECL|field|istream
specifier|private
specifier|volatile
name|HdfsInputStream
name|istream
decl_stmt|;
DECL|method|HdfsConsumer (HdfsEndpoint endpoint, Processor processor, HdfsConfiguration config)
specifier|public
name|HdfsConsumer
parameter_list|(
name|HdfsEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|HdfsConfiguration
name|config
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
name|config
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|hdfsPath
operator|=
name|config
operator|.
name|getFileSystemType
argument_list|()
operator|.
name|getHdfsPath
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
name|setInitialDelay
argument_list|(
name|config
operator|.
name|getInitialDelay
argument_list|()
argument_list|)
expr_stmt|;
name|setDelay
argument_list|(
name|config
operator|.
name|getDelay
argument_list|()
argument_list|)
expr_stmt|;
name|setUseFixedDelay
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|HdfsEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|HdfsEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
if|if
condition|(
name|config
operator|.
name|isConnectOnStartup
argument_list|()
condition|)
block|{
comment|// setup hdfs if configured to do on startup
name|setupHdfs
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setupHdfs (boolean onStartup)
specifier|private
name|HdfsInfo
name|setupHdfs
parameter_list|(
name|boolean
name|onStartup
parameter_list|)
throws|throws
name|Exception
block|{
comment|// if we are starting up then log at info level, and if runtime then log at debug level to not flood the log
if|if
condition|(
name|onStartup
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Connecting to hdfs file-system {}:{}/{} (may take a while if connection is not available)"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|config
operator|.
name|getHostName
argument_list|()
block|,
name|config
operator|.
name|getPort
argument_list|()
block|,
name|hdfsPath
operator|.
name|toString
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Connecting to hdfs file-system {}:{}/{} (may take a while if connection is not available)"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|config
operator|.
name|getHostName
argument_list|()
block|,
name|config
operator|.
name|getPort
argument_list|()
block|,
name|hdfsPath
operator|.
name|toString
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
block|}
comment|// hadoop will cache the connection by default so its faster to get in the poll method
name|HdfsInfo
name|answer
init|=
name|HdfsInfoFactory
operator|.
name|newHdfsInfo
argument_list|(
name|this
operator|.
name|hdfsPath
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|onStartup
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Connected to hdfs file-system {}:{}/{}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|config
operator|.
name|getHostName
argument_list|()
block|,
name|config
operator|.
name|getPort
argument_list|()
block|,
name|hdfsPath
operator|.
name|toString
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Connected to hdfs file-system {}:{}/{}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|config
operator|.
name|getHostName
argument_list|()
block|,
name|config
operator|.
name|getPort
argument_list|()
block|,
name|hdfsPath
operator|.
name|toString
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|protected
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
comment|// need to remember auth as Hadoop will override that, which otherwise means the Auth is broken afterwards
name|Configuration
name|auth
init|=
name|HdfsComponent
operator|.
name|getJAASConfiguration
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|doPoll
argument_list|()
return|;
block|}
finally|finally
block|{
name|HdfsComponent
operator|.
name|setJAASConfiguration
argument_list|(
name|auth
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doPoll ()
specifier|protected
name|int
name|doPoll
parameter_list|()
throws|throws
name|Exception
block|{
class|class
name|ExcludePathFilter
implements|implements
name|PathFilter
block|{
specifier|public
name|boolean
name|accept
parameter_list|(
name|Path
name|path
parameter_list|)
block|{
return|return
operator|!
operator|(
name|path
operator|.
name|toString
argument_list|()
operator|.
name|endsWith
argument_list|(
name|config
operator|.
name|getOpenedSuffix
argument_list|()
argument_list|)
operator|||
name|path
operator|.
name|toString
argument_list|()
operator|.
name|endsWith
argument_list|(
name|config
operator|.
name|getReadSuffix
argument_list|()
argument_list|)
operator|)
return|;
block|}
block|}
name|int
name|numMessages
init|=
literal|0
decl_stmt|;
name|HdfsInfo
name|info
init|=
name|setupHdfs
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|FileStatus
name|fileStatuses
index|[]
decl_stmt|;
if|if
condition|(
name|info
operator|.
name|getFileSystem
argument_list|()
operator|.
name|isFile
argument_list|(
name|info
operator|.
name|getPath
argument_list|()
argument_list|)
condition|)
block|{
name|fileStatuses
operator|=
name|info
operator|.
name|getFileSystem
argument_list|()
operator|.
name|globStatus
argument_list|(
name|info
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Path
name|pattern
init|=
name|info
operator|.
name|getPath
argument_list|()
operator|.
name|suffix
argument_list|(
literal|"/"
operator|+
name|this
operator|.
name|config
operator|.
name|getPattern
argument_list|()
argument_list|)
decl_stmt|;
name|fileStatuses
operator|=
name|info
operator|.
name|getFileSystem
argument_list|()
operator|.
name|globStatus
argument_list|(
name|pattern
argument_list|,
operator|new
name|ExcludePathFilter
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|FileStatus
name|status
range|:
name|fileStatuses
control|)
block|{
if|if
condition|(
name|normalFileIsDirectoryNoSuccessFile
argument_list|(
name|status
argument_list|,
name|info
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|config
operator|.
name|getOwner
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// must match owner
if|if
condition|(
operator|!
name|config
operator|.
name|getOwner
argument_list|()
operator|.
name|equals
argument_list|(
name|status
operator|.
name|getOwner
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Skipping file: {} as not matching owner: {}"
argument_list|,
name|status
operator|.
name|getPath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|config
operator|.
name|getOwner
argument_list|()
argument_list|)
expr_stmt|;
block|}
continue|continue;
block|}
block|}
try|try
block|{
name|this
operator|.
name|rwlock
operator|.
name|writeLock
argument_list|()
operator|.
name|lock
argument_list|()
expr_stmt|;
name|this
operator|.
name|istream
operator|=
name|HdfsInputStream
operator|.
name|createInputStream
argument_list|(
name|status
operator|.
name|getPath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|this
operator|.
name|config
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|this
operator|.
name|rwlock
operator|.
name|writeLock
argument_list|()
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|Holder
argument_list|<
name|Object
argument_list|>
name|key
init|=
operator|new
name|Holder
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Holder
argument_list|<
name|Object
argument_list|>
name|value
init|=
operator|new
name|Holder
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
while|while
condition|(
name|this
operator|.
name|istream
operator|.
name|next
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
operator|!=
literal|0
condition|)
block|{
name|Exchange
name|exchange
init|=
name|this
operator|.
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|DefaultMessage
argument_list|()
decl_stmt|;
name|String
name|fileName
init|=
name|StringUtils
operator|.
name|substringAfterLast
argument_list|(
name|status
operator|.
name|getPath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
if|if
condition|(
name|key
operator|.
name|value
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|HdfsHeader
operator|.
name|KEY
operator|.
name|name
argument_list|()
argument_list|,
name|key
operator|.
name|value
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|setBody
argument_list|(
name|value
operator|.
name|value
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Processing file {}"
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
try|try
block|{
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|// in case of unhandled exceptions then let the exception handler handle them
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|numMessages
operator|++
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|istream
argument_list|,
literal|"input stream"
argument_list|,
name|log
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|numMessages
return|;
block|}
DECL|method|normalFileIsDirectoryNoSuccessFile (FileStatus status, HdfsInfo info)
specifier|private
name|boolean
name|normalFileIsDirectoryNoSuccessFile
parameter_list|(
name|FileStatus
name|status
parameter_list|,
name|HdfsInfo
name|info
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|config
operator|.
name|getFileType
argument_list|()
operator|.
name|equals
argument_list|(
name|HdfsFileType
operator|.
name|NORMAL_FILE
argument_list|)
operator|&&
name|status
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|Path
name|successPath
init|=
operator|new
name|Path
argument_list|(
name|status
operator|.
name|getPath
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|"/_SUCCESS"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|info
operator|.
name|getFileSystem
argument_list|()
operator|.
name|exists
argument_list|(
name|successPath
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

