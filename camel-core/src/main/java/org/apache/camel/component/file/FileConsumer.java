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
name|ArrayList
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
name|Collections
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
comment|// gather list of files to process
name|List
argument_list|<
name|File
argument_list|>
name|files
init|=
operator|new
name|ArrayList
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
name|scanFilesToPoll
argument_list|(
name|endpoint
operator|.
name|getFile
argument_list|()
argument_list|,
literal|true
argument_list|,
name|files
argument_list|)
expr_stmt|;
comment|// resort files if provided
if|if
condition|(
name|endpoint
operator|.
name|getSorter
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|files
argument_list|,
name|endpoint
operator|.
name|getSorter
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// consume files one by one
name|int
name|total
init|=
name|files
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|files
operator|.
name|size
argument_list|()
condition|;
name|index
operator|++
control|)
block|{
name|File
name|file
init|=
name|files
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|processFile
argument_list|(
name|file
argument_list|,
name|total
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Scans the given file or directory for files to process.      *      * @param fileOrDirectory  current file or directory when doing recursion      * @param processDir  recursive      * @param fileList  current list of files gathered      */
DECL|method|scanFilesToPoll (File fileOrDirectory, boolean processDir, List<File> fileList)
specifier|protected
name|void
name|scanFilesToPoll
parameter_list|(
name|File
name|fileOrDirectory
parameter_list|,
name|boolean
name|processDir
parameter_list|,
name|List
argument_list|<
name|File
argument_list|>
name|fileList
parameter_list|)
block|{
if|if
condition|(
name|fileOrDirectory
operator|==
literal|null
operator|||
operator|!
name|fileOrDirectory
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// not a file so skip it
return|return;
block|}
if|if
condition|(
operator|!
name|fileOrDirectory
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|addFile
argument_list|(
name|fileOrDirectory
argument_list|,
name|fileList
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|processDir
condition|)
block|{
comment|// directory that can be recursive
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
comment|// recursive add the files
name|scanFilesToPoll
argument_list|(
name|file
argument_list|,
name|isRecursive
argument_list|()
argument_list|,
name|fileList
argument_list|)
expr_stmt|;
block|}
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
block|}
block|}
comment|/**      * Processes the given file      *      * @param file  the file      * @param total  total number of files in this batch      * @param index  current index out of total in this batch                                            */
DECL|method|processFile (final File file, int total, int index)
specifier|protected
name|void
name|processFile
parameter_list|(
specifier|final
name|File
name|file
parameter_list|,
name|int
name|total
parameter_list|,
name|int
name|index
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
literal|"Processing file: "
operator|+
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|FileComponent
operator|.
name|HEADER_FILE_BATCH_TOTAL
argument_list|,
name|total
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|FileComponent
operator|.
name|HEADER_FILE_BATCH_INDEX
argument_list|,
name|index
argument_list|)
expr_stmt|;
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
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
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
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * Strategy for validating if the given file should be included or not      * @param file  the file      * @return true to include the file, false to skip it      */
DECL|method|validateFile (File file)
specifier|protected
name|boolean
name|validateFile
parameter_list|(
name|File
name|file
parameter_list|)
block|{
comment|// NOTE: contains will add if we had a miss
if|if
condition|(
name|endpoint
operator|.
name|isIdempotent
argument_list|()
operator|&&
name|endpoint
operator|.
name|getIdempotentRepository
argument_list|()
operator|.
name|contains
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
comment|// skip as we have already processed it
return|return
literal|false
return|;
block|}
return|return
name|matchFile
argument_list|(
name|file
argument_list|)
return|;
block|}
comment|/**      * Strategy to perform file matching based on endpoint configuration.      *<p/>      * Will always return false for certain files:      *<ul>      *<li>Starting with a dot</li>      *<li>lock files</li>      *</ul>      *      * @param file  the file      * @return true if the file is matche, false if not      */
DECL|method|matchFile (File file)
specifier|protected
name|boolean
name|matchFile
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
name|endpoint
operator|.
name|getFilter
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|endpoint
operator|.
name|getFilter
argument_list|()
operator|.
name|accept
argument_list|(
name|file
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
return|return
literal|true
return|;
block|}
DECL|method|addFile (File file, List<File> fileList)
specifier|private
name|void
name|addFile
parameter_list|(
name|File
name|file
parameter_list|,
name|List
argument_list|<
name|File
argument_list|>
name|fileList
parameter_list|)
block|{
if|if
condition|(
name|validateFile
argument_list|(
name|file
argument_list|)
condition|)
block|{
name|fileList
operator|.
name|add
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
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
block|}
end_class

end_unit

