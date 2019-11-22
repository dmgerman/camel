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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
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
name|atomic
operator|.
name|AtomicInteger
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|RuntimeCamelException
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
name|support
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
DECL|field|endpointConfig
specifier|private
specifier|final
name|HdfsConfiguration
name|endpointConfig
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
DECL|field|hdfsInfoFactory
specifier|private
specifier|final
name|HdfsInfoFactory
name|hdfsInfoFactory
decl_stmt|;
DECL|field|rwLock
specifier|private
specifier|final
name|ReadWriteLock
name|rwLock
init|=
operator|new
name|ReentrantReadWriteLock
argument_list|()
decl_stmt|;
DECL|method|HdfsConsumer (HdfsEndpoint endpoint, Processor processor, HdfsConfiguration endpointConfig)
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
name|endpointConfig
parameter_list|)
block|{
name|this
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|,
name|endpointConfig
argument_list|,
operator|new
name|HdfsInfoFactory
argument_list|(
name|endpointConfig
argument_list|)
argument_list|,
name|endpointConfig
operator|.
name|getFileSystemType
argument_list|()
operator|.
name|getHdfsPath
argument_list|(
name|endpointConfig
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|HdfsConsumer (HdfsEndpoint endpoint, Processor processor, HdfsConfiguration endpointConfig, HdfsInfoFactory hdfsInfoFactory, StringBuilder hdfsPath)
name|HdfsConsumer
parameter_list|(
name|HdfsEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|HdfsConfiguration
name|endpointConfig
parameter_list|,
name|HdfsInfoFactory
name|hdfsInfoFactory
parameter_list|,
name|StringBuilder
name|hdfsPath
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
name|processor
operator|=
name|processor
expr_stmt|;
name|this
operator|.
name|endpointConfig
operator|=
name|endpointConfig
expr_stmt|;
name|this
operator|.
name|hdfsPath
operator|=
name|hdfsPath
expr_stmt|;
name|this
operator|.
name|hdfsInfoFactory
operator|=
name|hdfsInfoFactory
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
name|endpointConfig
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
name|IOException
block|{
name|String
name|hdfsFsDescription
init|=
name|endpointConfig
operator|.
name|getFileSystemLabel
argument_list|(
name|hdfsPath
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
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
literal|"Connecting to hdfs file-system {} (may take a while if connection is not available)"
argument_list|,
name|hdfsFsDescription
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Connecting to hdfs file-system {} (may take a while if connection is not available)"
argument_list|,
name|hdfsFsDescription
argument_list|)
expr_stmt|;
block|}
comment|// hadoop will cache the connection by default so its faster to get in the poll method
name|HdfsInfo
name|answer
init|=
name|hdfsInfoFactory
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
literal|"Connected to hdfs file-system {}"
argument_list|,
name|hdfsFsDescription
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Connected to hdfs file-system {}"
argument_list|,
name|hdfsFsDescription
argument_list|)
expr_stmt|;
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
name|IOException
block|{
class|class
name|ExcludePathFilter
implements|implements
name|PathFilter
block|{
annotation|@
name|Override
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
name|endpointConfig
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
name|endpointConfig
operator|.
name|getReadSuffix
argument_list|()
argument_list|)
operator|)
return|;
block|}
block|}
name|HdfsInfo
name|info
init|=
name|setupHdfs
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|FileStatus
index|[]
name|fileStatuses
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
name|endpointConfig
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
name|fileStatuses
operator|=
name|Optional
operator|.
name|ofNullable
argument_list|(
name|fileStatuses
argument_list|)
operator|.
name|orElse
argument_list|(
operator|new
name|FileStatus
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
return|return
name|processFileStatuses
argument_list|(
name|info
argument_list|,
name|fileStatuses
argument_list|)
return|;
block|}
DECL|method|processFileStatuses (HdfsInfo info, FileStatus[] fileStatuses)
specifier|private
name|int
name|processFileStatuses
parameter_list|(
name|HdfsInfo
name|info
parameter_list|,
name|FileStatus
index|[]
name|fileStatuses
parameter_list|)
block|{
specifier|final
name|AtomicInteger
name|totalMessageCount
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|HdfsInputStream
argument_list|>
name|hdfsFiles
init|=
name|Arrays
operator|.
name|stream
argument_list|(
name|fileStatuses
argument_list|)
operator|.
name|filter
argument_list|(
name|status
lambda|->
name|normalFileIsDirectoryHasSuccessFile
argument_list|(
name|status
argument_list|,
name|info
argument_list|)
argument_list|)
operator|.
name|filter
argument_list|(
name|this
operator|::
name|hasMatchingOwner
argument_list|)
operator|.
name|limit
argument_list|(
name|endpointConfig
operator|.
name|getMaxMessagesPerPoll
argument_list|()
argument_list|)
operator|.
name|map
argument_list|(
name|this
operator|::
name|asHdfsFile
argument_list|)
operator|.
name|filter
argument_list|(
name|Objects
operator|::
name|nonNull
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Processing [{}] valid files out of [{}] available."
argument_list|,
name|hdfsFiles
operator|.
name|size
argument_list|()
argument_list|,
name|fileStatuses
operator|.
name|length
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
name|hdfsFiles
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|HdfsInputStream
name|hdfsFile
init|=
name|hdfsFiles
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
try|try
block|{
name|int
name|messageCount
init|=
name|processHdfsInputStream
argument_list|(
name|hdfsFile
argument_list|,
name|totalMessageCount
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Processed [{}] files out of [{}]."
argument_list|,
name|i
argument_list|,
name|hdfsFiles
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"File [{}] was split to [{}] messages."
argument_list|,
name|i
argument_list|,
name|messageCount
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|hdfsFile
argument_list|,
literal|"hdfs file"
argument_list|,
name|log
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|totalMessageCount
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|processHdfsInputStream (HdfsInputStream hdfsFile, AtomicInteger totalMessageCount)
specifier|private
name|int
name|processHdfsInputStream
parameter_list|(
name|HdfsInputStream
name|hdfsFile
parameter_list|,
name|AtomicInteger
name|totalMessageCount
parameter_list|)
block|{
specifier|final
name|AtomicInteger
name|messageCount
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Holder
argument_list|<
name|Object
argument_list|>
name|currentKey
init|=
operator|new
name|Holder
argument_list|<>
argument_list|()
decl_stmt|;
name|Holder
argument_list|<
name|Object
argument_list|>
name|currentValue
init|=
operator|new
name|Holder
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
name|hdfsFile
operator|.
name|next
argument_list|(
name|currentKey
argument_list|,
name|currentValue
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|processHdfsInputStream
argument_list|(
name|hdfsFile
argument_list|,
name|currentKey
argument_list|,
name|currentValue
argument_list|,
name|messageCount
argument_list|,
name|totalMessageCount
argument_list|)
expr_stmt|;
name|messageCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
return|return
name|messageCount
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|processHdfsInputStream (HdfsInputStream hdfsFile, Holder<Object> key, Holder<Object> value, AtomicInteger messageCount, AtomicInteger totalMessageCount)
specifier|private
name|void
name|processHdfsInputStream
parameter_list|(
name|HdfsInputStream
name|hdfsFile
parameter_list|,
name|Holder
argument_list|<
name|Object
argument_list|>
name|key
parameter_list|,
name|Holder
argument_list|<
name|Object
argument_list|>
name|value
parameter_list|,
name|AtomicInteger
name|messageCount
parameter_list|,
name|AtomicInteger
name|totalMessageCount
parameter_list|)
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
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|fileName
init|=
name|StringUtils
operator|.
name|substringAfterLast
argument_list|(
name|hdfsFile
operator|.
name|getActualPath
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
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME_CONSUMED
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"CamelFileAbsolutePath"
argument_list|,
name|hdfsFile
operator|.
name|getActualPath
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|key
operator|.
name|getValue
argument_list|()
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
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|hdfsFile
operator|.
name|getNumOfReadBytes
argument_list|()
operator|>=
literal|0
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_LENGTH
argument_list|,
name|hdfsFile
operator|.
name|getNumOfReadBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|setBody
argument_list|(
name|value
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|updateNewExchange
argument_list|(
name|exchange
argument_list|,
name|messageCount
operator|.
name|get
argument_list|()
argument_list|,
name|hdfsFile
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Processing file [{}]"
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
name|totalMessageCount
operator|.
name|incrementAndGet
argument_list|()
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
block|}
DECL|method|normalFileIsDirectoryHasSuccessFile (FileStatus fileStatus, HdfsInfo info)
specifier|private
name|boolean
name|normalFileIsDirectoryHasSuccessFile
parameter_list|(
name|FileStatus
name|fileStatus
parameter_list|,
name|HdfsInfo
name|info
parameter_list|)
block|{
if|if
condition|(
name|endpointConfig
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
name|fileStatus
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
try|try
block|{
name|Path
name|successPath
init|=
operator|new
name|Path
argument_list|(
name|fileStatus
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
literal|false
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
return|return
literal|true
return|;
block|}
DECL|method|hasMatchingOwner (FileStatus fileStatus)
specifier|private
name|boolean
name|hasMatchingOwner
parameter_list|(
name|FileStatus
name|fileStatus
parameter_list|)
block|{
if|if
condition|(
name|endpointConfig
operator|.
name|getOwner
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|endpointConfig
operator|.
name|getOwner
argument_list|()
operator|.
name|equals
argument_list|(
name|fileStatus
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
name|fileStatus
operator|.
name|getPath
argument_list|()
argument_list|,
name|endpointConfig
operator|.
name|getOwner
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
DECL|method|asHdfsFile (FileStatus fileStatus)
specifier|private
name|HdfsInputStream
name|asHdfsFile
parameter_list|(
name|FileStatus
name|fileStatus
parameter_list|)
block|{
try|try
block|{
name|this
operator|.
name|rwLock
operator|.
name|writeLock
argument_list|()
operator|.
name|lock
argument_list|()
expr_stmt|;
return|return
name|HdfsInputStream
operator|.
name|createInputStream
argument_list|(
name|fileStatus
operator|.
name|getPath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|hdfsInfoFactory
argument_list|)
return|;
block|}
finally|finally
block|{
name|this
operator|.
name|rwLock
operator|.
name|writeLock
argument_list|()
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|updateNewExchange (Exchange exchange, int index, HdfsInputStream hdfsFile)
specifier|protected
name|void
name|updateNewExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|int
name|index
parameter_list|,
name|HdfsInputStream
name|hdfsFile
parameter_list|)
block|{
comment|// do not share unit of work
name|exchange
operator|.
name|setUnitOfWork
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|SPLIT_INDEX
argument_list|,
name|index
argument_list|)
expr_stmt|;
if|if
condition|(
name|hdfsFile
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|SPLIT_COMPLETE
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|SPLIT_COMPLETE
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
comment|// streaming mode, so set total size when we are complete based on the index
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|SPLIT_SIZE
argument_list|,
name|index
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

