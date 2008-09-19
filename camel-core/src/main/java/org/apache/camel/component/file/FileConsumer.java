begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|RandomAccessFile
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|channels
operator|.
name|FileChannel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|channels
operator|.
name|FileLock
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
name|ConcurrentHashMap
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
name|AsyncCallback
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
name|processor
operator|.
name|DeadLetterChannel
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
name|ObjectHelper
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
name|logging
operator|.
name|Log
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
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * For consuming files.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|FileConsumer
specifier|public
class|class
name|FileConsumer
extends|extends
name|ScheduledPollConsumer
argument_list|<
name|FileExchange
argument_list|>
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|FileConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
name|FileEndpoint
name|endpoint
decl_stmt|;
DECL|field|filesBeingProcessed
specifier|private
name|ConcurrentHashMap
argument_list|<
name|File
argument_list|,
name|File
argument_list|>
name|filesBeingProcessed
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|File
argument_list|,
name|File
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|fileSizes
specifier|private
name|ConcurrentHashMap
argument_list|<
name|File
argument_list|,
name|Long
argument_list|>
name|fileSizes
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|File
argument_list|,
name|Long
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|noopMap
specifier|private
name|ConcurrentHashMap
argument_list|<
name|File
argument_list|,
name|Long
argument_list|>
name|noopMap
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|File
argument_list|,
name|Long
argument_list|>
argument_list|()
decl_stmt|;
comment|// the options below is @deprecated and will be removed in Camel 2.0
DECL|field|lastPollTime
specifier|private
name|long
name|lastPollTime
decl_stmt|;
DECL|field|unchangedDelay
specifier|private
name|int
name|unchangedDelay
decl_stmt|;
DECL|field|unchangedSize
specifier|private
name|boolean
name|unchangedSize
decl_stmt|;
DECL|field|generateEmptyExchangeWhenIdle
specifier|private
name|boolean
name|generateEmptyExchangeWhenIdle
decl_stmt|;
DECL|field|alwaysConsume
specifier|private
name|boolean
name|alwaysConsume
decl_stmt|;
DECL|field|recursive
specifier|private
name|boolean
name|recursive
decl_stmt|;
DECL|field|regexPattern
specifier|private
name|String
name|regexPattern
init|=
literal|""
decl_stmt|;
DECL|field|exclusiveReadLock
specifier|private
name|boolean
name|exclusiveReadLock
init|=
literal|true
decl_stmt|;
DECL|method|FileConsumer (final FileEndpoint endpoint, Processor processor)
specifier|public
name|FileConsumer
parameter_list|(
specifier|final
name|FileEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
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
block|}
DECL|method|poll ()
specifier|protected
specifier|synchronized
name|void
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
comment|// should be true the first time as its the top directory
name|int
name|rc
init|=
name|pollFileOrDirectory
argument_list|(
name|endpoint
operator|.
name|getFile
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// if no files consumes and using generateEmptyExchangeWhenIdle option then process an empty exchange
if|if
condition|(
name|rc
operator|==
literal|0
operator|&&
name|generateEmptyExchangeWhenIdle
condition|)
block|{
specifier|final
name|FileExchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
operator|(
name|File
operator|)
literal|null
argument_list|)
decl_stmt|;
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|sync
parameter_list|)
block|{                 }
block|}
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
comment|/**      * Pools the given file or directory for files to process.      *      * @param fileOrDirectory  file or directory      * @param processDir  recursive      * @return the number of files processed or being processed async.      */
DECL|method|pollFileOrDirectory (File fileOrDirectory, boolean processDir)
specifier|protected
name|int
name|pollFileOrDirectory
parameter_list|(
name|File
name|fileOrDirectory
parameter_list|,
name|boolean
name|processDir
parameter_list|)
block|{
if|if
condition|(
operator|!
name|fileOrDirectory
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
comment|// process the file
return|return
name|pollFile
argument_list|(
name|fileOrDirectory
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|processDir
condition|)
block|{
comment|// directory that can be recursive
name|int
name|rc
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|isValidFile
argument_list|(
name|fileOrDirectory
argument_list|)
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Polling directory "
operator|+
name|fileOrDirectory
argument_list|)
expr_stmt|;
block|}
name|File
index|[]
name|files
init|=
name|fileOrDirectory
operator|.
name|listFiles
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|file
range|:
name|files
control|)
block|{
name|rc
operator|+=
name|pollFileOrDirectory
argument_list|(
name|file
argument_list|,
name|isRecursive
argument_list|()
argument_list|)
expr_stmt|;
comment|// self-recursion
block|}
block|}
return|return
name|rc
return|;
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Skipping directory "
operator|+
name|fileOrDirectory
argument_list|)
expr_stmt|;
block|}
return|return
literal|0
return|;
block|}
block|}
comment|/**      * Polls the given file      *      * @param file  the file      * @return returns 1 if the file was processed, 0 otherwise.      */
DECL|method|pollFile (final File file)
specifier|protected
name|int
name|pollFile
parameter_list|(
specifier|final
name|File
name|file
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Polling file: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
literal|0
return|;
block|}
if|if
condition|(
operator|!
name|isValidFile
argument_list|(
name|file
argument_list|)
condition|)
block|{
return|return
literal|0
return|;
block|}
comment|// we only care about file modified times if we are not deleting/moving files
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isNoop
argument_list|()
condition|)
block|{
if|if
condition|(
name|filesBeingProcessed
operator|.
name|contains
argument_list|(
name|file
argument_list|)
condition|)
block|{
return|return
literal|1
return|;
block|}
name|filesBeingProcessed
operator|.
name|put
argument_list|(
name|file
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
specifier|final
name|FileProcessStrategy
name|processStrategy
init|=
name|endpoint
operator|.
name|getFileStrategy
argument_list|()
decl_stmt|;
specifier|final
name|FileExchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|configureMessage
argument_list|(
name|file
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
comment|// is we use excluse read then acquire the exclusive read (waiting until we got it)
if|if
condition|(
name|exclusiveReadLock
condition|)
block|{
name|acquireExclusiveReadLock
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"About to process file: "
operator|+
name|file
operator|+
literal|" using exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|processStrategy
operator|.
name|begin
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|file
argument_list|)
condition|)
block|{
comment|// Use the async processor interface so that processing of
comment|// the exchange can happen asynchronously
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|sync
parameter_list|)
block|{
name|boolean
name|failed
init|=
name|exchange
operator|.
name|isFailed
argument_list|()
decl_stmt|;
name|boolean
name|handled
init|=
name|DeadLetterChannel
operator|.
name|isFailureHandled
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Done processing file: "
operator|+
name|file
operator|+
literal|". Status is: "
operator|+
operator|(
name|failed
condition|?
literal|"failed: "
operator|+
name|failed
operator|+
literal|", handled by failure processor: "
operator|+
name|handled
else|:
literal|"processed OK"
operator|)
argument_list|)
expr_stmt|;
block|}
name|boolean
name|committed
init|=
literal|false
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|failed
operator|||
name|handled
condition|)
block|{
comment|// commit the file strategy if there was no failure or already handled by the DeadLetterChannel
name|processStrategyCommit
argument_list|(
name|processStrategy
argument_list|,
name|exchange
argument_list|,
name|file
argument_list|,
name|handled
argument_list|)
expr_stmt|;
name|committed
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
comment|// there was an exception but it was not handled by the DeadLetterChannel
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
finally|finally
block|{
if|if
condition|(
operator|!
name|committed
condition|)
block|{
name|processStrategyRollback
argument_list|(
name|processStrategy
argument_list|,
name|exchange
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
name|filesBeingProcessed
operator|.
name|remove
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|endpoint
operator|+
literal|" can not process file: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|1
return|;
block|}
comment|/**      * Acquires exclusive read lock to the given file. Will wait until the lock is granted.      * After granting the read lock it is realeased, we just want to make sure that when we start      * consuming the file its not currently in progress of being written by third party.      */
DECL|method|acquireExclusiveReadLock (File file)
specifier|protected
name|void
name|acquireExclusiveReadLock
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Waiting for exclusive read lock to file: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
comment|// try to acquire rw lock on the file before we can consume it
name|FileChannel
name|channel
init|=
operator|new
name|RandomAccessFile
argument_list|(
name|file
argument_list|,
literal|"rw"
argument_list|)
operator|.
name|getChannel
argument_list|()
decl_stmt|;
try|try
block|{
name|FileLock
name|lock
init|=
name|channel
operator|.
name|lock
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Acquired exclusive read lock: "
operator|+
name|lock
operator|+
literal|" to file: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
comment|// just release it now we dont want to hold it during the rest of the processing
name|lock
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
comment|// must close channel
name|ObjectHelper
operator|.
name|close
argument_list|(
name|channel
argument_list|,
literal|"FileConsumer during acquiring of exclusive read lock"
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Strategy when the file was processed and a commit should be executed.      *      * @param processStrategy   the strategy to perform the commit      * @param exchange          the exchange      * @param file              the file processed      * @param failureHandled    is<tt>false</tt> if the exchange was processed succesfully,<tt>true</tt> if      * an exception occured during processing but it was handled by the failure processor (usually the      * DeadLetterChannel).      */
DECL|method|processStrategyCommit (FileProcessStrategy processStrategy, FileExchange exchange, File file, boolean failureHandled)
specifier|protected
name|void
name|processStrategyCommit
parameter_list|(
name|FileProcessStrategy
name|processStrategy
parameter_list|,
name|FileExchange
name|exchange
parameter_list|,
name|File
name|file
parameter_list|,
name|boolean
name|failureHandled
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Committing file strategy: "
operator|+
name|processStrategy
operator|+
literal|" for file: "
operator|+
name|file
operator|+
operator|(
name|failureHandled
condition|?
literal|" that was handled by the failure processor."
else|:
literal|""
operator|)
argument_list|)
expr_stmt|;
block|}
name|processStrategy
operator|.
name|commit
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error committing file strategy: "
operator|+
name|processStrategy
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Strategy when the file was not processed and a rollback should be executed.      *      * @param processStrategy   the strategy to perform the commit      * @param exchange          the exchange      * @param file              the file processed      */
DECL|method|processStrategyRollback (FileProcessStrategy processStrategy, FileExchange exchange, File file)
specifier|protected
name|void
name|processStrategyRollback
parameter_list|(
name|FileProcessStrategy
name|processStrategy
parameter_list|,
name|FileExchange
name|exchange
parameter_list|,
name|File
name|file
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Rolling back file strategy: "
operator|+
name|processStrategy
operator|+
literal|" for file: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
name|processStrategy
operator|.
name|rollback
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
DECL|method|isValidFile (File file)
specifier|protected
name|boolean
name|isValidFile
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|boolean
name|result
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
operator|&&
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// TODO: maybe use a configurable strategy instead of the hardcoded one based on last file change
if|if
condition|(
name|isMatched
argument_list|(
name|file
argument_list|)
operator|&&
operator|(
name|alwaysConsume
operator|||
name|isChanged
argument_list|(
name|file
argument_list|)
operator|)
condition|)
block|{
name|result
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|isChanged (File file)
specifier|protected
name|boolean
name|isChanged
parameter_list|(
name|File
name|file
parameter_list|)
block|{
if|if
condition|(
name|file
operator|==
literal|null
condition|)
block|{
comment|// Sanity check
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
comment|// Allow recursive polling to descend into this directory
return|return
literal|true
return|;
block|}
else|else
block|{
comment|// @deprecated will be removed on Camel 2.0
comment|// the code below is kinda hard to maintain. We should strive to remove
comment|// this stuff in Camel 2.0 to keep this component simple and no surprises for end-users
comment|// this stuff is not persistent so restarting Camel will reset the state
name|boolean
name|lastModifiedCheck
init|=
literal|false
decl_stmt|;
name|long
name|modifiedDuration
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|getUnchangedDelay
argument_list|()
operator|>
literal|0
condition|)
block|{
name|modifiedDuration
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|file
operator|.
name|lastModified
argument_list|()
expr_stmt|;
name|lastModifiedCheck
operator|=
name|modifiedDuration
operator|>=
name|getUnchangedDelay
argument_list|()
expr_stmt|;
block|}
name|long
name|fileModified
init|=
name|file
operator|.
name|lastModified
argument_list|()
decl_stmt|;
name|Long
name|previousModified
init|=
name|noopMap
operator|.
name|get
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|noopMap
operator|.
name|put
argument_list|(
name|file
argument_list|,
name|fileModified
argument_list|)
expr_stmt|;
if|if
condition|(
name|previousModified
operator|==
literal|null
operator|||
name|fileModified
operator|>
name|previousModified
condition|)
block|{
name|lastModifiedCheck
operator|=
literal|true
expr_stmt|;
block|}
name|boolean
name|sizeCheck
init|=
literal|false
decl_stmt|;
name|long
name|sizeDifference
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|isUnchangedSize
argument_list|()
condition|)
block|{
name|Long
name|value
init|=
name|fileSizes
operator|.
name|get
argument_list|(
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|sizeCheck
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|sizeCheck
operator|=
name|file
operator|.
name|length
argument_list|()
operator|!=
name|value
expr_stmt|;
block|}
block|}
name|boolean
name|answer
init|=
name|lastModifiedCheck
operator|||
name|sizeCheck
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"file:"
operator|+
name|file
operator|+
literal|" isChanged:"
operator|+
name|answer
operator|+
literal|" "
operator|+
literal|"sizeCheck:"
operator|+
name|sizeCheck
operator|+
literal|"("
operator|+
name|sizeDifference
operator|+
literal|") "
operator|+
literal|"lastModifiedCheck:"
operator|+
name|lastModifiedCheck
operator|+
literal|"("
operator|+
name|modifiedDuration
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isUnchangedSize
argument_list|()
condition|)
block|{
if|if
condition|(
name|answer
condition|)
block|{
name|fileSizes
operator|.
name|put
argument_list|(
name|file
argument_list|,
name|file
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fileSizes
operator|.
name|remove
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
block|}
DECL|method|isMatched (File file)
specifier|protected
name|boolean
name|isMatched
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|String
name|name
init|=
name|file
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// folders/names starting with dot is always skipped (eg. ".", ".camel", ".camelLock")
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// lock files should be skipped
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
name|FileEndpoint
operator|.
name|DEFAULT_LOCK_FILE_POSTFIX
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// directories so far is always regarded as matched (matching on the name is only for files)
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
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
if|if
condition|(
operator|!
name|name
operator|.
name|matches
argument_list|(
name|regexPattern
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|if
condition|(
name|endpoint
operator|.
name|getExcludedNamePrefix
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|endpoint
operator|.
name|getExcludedNamePrefix
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
name|String
index|[]
name|prefixes
init|=
name|endpoint
operator|.
name|getExcludedNamePrefixes
argument_list|()
decl_stmt|;
if|if
condition|(
name|prefixes
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|prefix
range|:
name|prefixes
control|)
block|{
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
if|if
condition|(
name|endpoint
operator|.
name|getExcludedNamePostfix
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
name|endpoint
operator|.
name|getExcludedNamePostfix
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
name|String
index|[]
name|postfixes
init|=
name|endpoint
operator|.
name|getExcludedNamePostfixes
argument_list|()
decl_stmt|;
if|if
condition|(
name|postfixes
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|postfix
range|:
name|postfixes
control|)
block|{
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
name|postfix
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
DECL|method|isRecursive ()
specifier|public
name|boolean
name|isRecursive
parameter_list|()
block|{
return|return
name|this
operator|.
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
DECL|method|getRegexPattern ()
specifier|public
name|String
name|getRegexPattern
parameter_list|()
block|{
return|return
name|this
operator|.
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
DECL|method|isGenerateEmptyExchangeWhenIdle ()
specifier|public
name|boolean
name|isGenerateEmptyExchangeWhenIdle
parameter_list|()
block|{
return|return
name|generateEmptyExchangeWhenIdle
return|;
block|}
comment|/**      * @deprecated will be removed in Camel 2.0      */
DECL|method|setGenerateEmptyExchangeWhenIdle (boolean generateEmptyExchangeWhenIdle)
specifier|public
name|void
name|setGenerateEmptyExchangeWhenIdle
parameter_list|(
name|boolean
name|generateEmptyExchangeWhenIdle
parameter_list|)
block|{
name|this
operator|.
name|generateEmptyExchangeWhenIdle
operator|=
name|generateEmptyExchangeWhenIdle
expr_stmt|;
block|}
DECL|method|getUnchangedDelay ()
specifier|public
name|int
name|getUnchangedDelay
parameter_list|()
block|{
return|return
name|unchangedDelay
return|;
block|}
comment|/**      * @deprecated will be removed in Camel 2.0      */
DECL|method|setUnchangedDelay (int unchangedDelay)
specifier|public
name|void
name|setUnchangedDelay
parameter_list|(
name|int
name|unchangedDelay
parameter_list|)
block|{
name|this
operator|.
name|unchangedDelay
operator|=
name|unchangedDelay
expr_stmt|;
block|}
DECL|method|isUnchangedSize ()
specifier|public
name|boolean
name|isUnchangedSize
parameter_list|()
block|{
return|return
name|unchangedSize
return|;
block|}
comment|/**      * @deprecated will be removed in Camel 2.0      */
DECL|method|setUnchangedSize (boolean unchangedSize)
specifier|public
name|void
name|setUnchangedSize
parameter_list|(
name|boolean
name|unchangedSize
parameter_list|)
block|{
name|this
operator|.
name|unchangedSize
operator|=
name|unchangedSize
expr_stmt|;
block|}
DECL|method|isExclusiveReadLock ()
specifier|public
name|boolean
name|isExclusiveReadLock
parameter_list|()
block|{
return|return
name|exclusiveReadLock
return|;
block|}
DECL|method|setExclusiveReadLock (boolean exclusiveReadLock)
specifier|public
name|void
name|setExclusiveReadLock
parameter_list|(
name|boolean
name|exclusiveReadLock
parameter_list|)
block|{
name|this
operator|.
name|exclusiveReadLock
operator|=
name|exclusiveReadLock
expr_stmt|;
block|}
DECL|method|isAlwaysConsume ()
specifier|public
name|boolean
name|isAlwaysConsume
parameter_list|()
block|{
return|return
name|alwaysConsume
return|;
block|}
comment|/**      * @deprecated will be removed in Camel 2.0 (not needed when we get rid of last polltimestamp)      */
DECL|method|setAlwaysConsume (boolean alwaysConsume)
specifier|public
name|void
name|setAlwaysConsume
parameter_list|(
name|boolean
name|alwaysConsume
parameter_list|)
block|{
name|this
operator|.
name|alwaysConsume
operator|=
name|alwaysConsume
expr_stmt|;
block|}
DECL|method|isTimestamp ()
specifier|public
name|boolean
name|isTimestamp
parameter_list|()
block|{
return|return
operator|!
name|alwaysConsume
return|;
block|}
comment|/**      * @deprecated will be removed in Camel 2.0 (not needed when we get rid of last polltimestamp)      */
DECL|method|setTimestamp (boolean timestamp)
specifier|public
name|void
name|setTimestamp
parameter_list|(
name|boolean
name|timestamp
parameter_list|)
block|{
name|this
operator|.
name|alwaysConsume
operator|=
operator|!
name|timestamp
expr_stmt|;
block|}
block|}
end_class

end_unit

