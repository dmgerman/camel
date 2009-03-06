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
name|Collections
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
name|DefaultExchange
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
comment|/**  * Base class for remote file consumers.  */
end_comment

begin_class
DECL|class|GenericFileConsumer
specifier|public
specifier|abstract
class|class
name|GenericFileConsumer
parameter_list|<
name|T
parameter_list|>
extends|extends
name|ScheduledPollConsumer
block|{
DECL|field|log
specifier|protected
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|protected
name|GenericFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
decl_stmt|;
DECL|field|operations
specifier|protected
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
decl_stmt|;
DECL|field|loggedIn
specifier|protected
name|boolean
name|loggedIn
decl_stmt|;
DECL|field|fileExpressionResult
specifier|protected
name|String
name|fileExpressionResult
decl_stmt|;
DECL|method|GenericFileConsumer (GenericFileEndpoint<T> endpoint, Processor processor, GenericFileOperations<T> operations)
specifier|public
name|GenericFileConsumer
parameter_list|(
name|GenericFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
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
name|operations
operator|=
name|operations
expr_stmt|;
block|}
comment|/**      * Poll for files      */
DECL|method|poll ()
specifier|protected
name|void
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
comment|// must reset for each poll
name|fileExpressionResult
operator|=
literal|null
expr_stmt|;
comment|// before we poll is there anything we need to check ? Such as are we
comment|// connected to the FTP Server Still ?
if|if
condition|(
operator|!
name|prePollCheck
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Skipping pool as pre poll check returned false"
argument_list|)
expr_stmt|;
block|}
comment|// gather list of files to process
name|List
argument_list|<
name|GenericFile
argument_list|<
name|T
argument_list|>
argument_list|>
name|files
init|=
operator|new
name|ArrayList
argument_list|<
name|GenericFile
argument_list|<
name|T
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getFile
argument_list|()
decl_stmt|;
name|pollDirectory
argument_list|(
name|name
argument_list|,
name|files
argument_list|)
expr_stmt|;
comment|// sort files using file comparator if provided
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
comment|// sort using build in sorters that is expression based
comment|// first we need to convert to RemoteFileExchange objects so we can sort
comment|// using expressions
name|List
argument_list|<
name|GenericFileExchange
argument_list|<
name|T
argument_list|>
argument_list|>
name|exchanges
init|=
operator|new
name|ArrayList
argument_list|<
name|GenericFileExchange
argument_list|<
name|T
argument_list|>
argument_list|>
argument_list|(
name|files
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
range|:
name|files
control|)
block|{
name|GenericFileExchange
argument_list|<
name|T
argument_list|>
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
name|exchanges
operator|.
name|add
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// sort files using exchange comparator if provided
if|if
condition|(
name|endpoint
operator|.
name|getSortBy
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|exchanges
argument_list|,
name|endpoint
operator|.
name|getSortBy
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// consume files one by one
name|int
name|total
init|=
name|exchanges
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|total
operator|>
literal|0
operator|&&
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
literal|"Total "
operator|+
name|total
operator|+
literal|" files to consume"
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|total
operator|&&
name|isRunAllowed
argument_list|()
condition|;
name|index
operator|++
control|)
block|{
comment|// only loop if we are started (allowed to run)
name|GenericFileExchange
argument_list|<
name|T
argument_list|>
name|exchange
init|=
name|exchanges
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
comment|// add current index and total as headers
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_BATCH_INDEX
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_BATCH_SIZE
argument_list|,
name|total
argument_list|)
expr_stmt|;
name|processExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Override if required. Perform some checks (and perhaps actions) before we      * poll.      *      * @return true to poll, false to skip this poll.      */
DECL|method|prePollCheck ()
specifier|protected
name|boolean
name|prePollCheck
parameter_list|()
throws|throws
name|Exception
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Polls the given directory for files to process      *      * @param fileName current directory or file      * @param fileList current list of files gathered      */
DECL|method|pollDirectory (String fileName, List<GenericFile<T>> fileList)
specifier|protected
specifier|abstract
name|void
name|pollDirectory
parameter_list|(
name|String
name|fileName
parameter_list|,
name|List
argument_list|<
name|GenericFile
argument_list|<
name|T
argument_list|>
argument_list|>
name|fileList
parameter_list|)
function_decl|;
comment|/**      * Polls the given file      *      * @param fileName the file name      * @param fileList current list of files gathered      */
DECL|method|pollFile (String fileName, List<GenericFile<T>> fileList)
specifier|protected
specifier|abstract
name|void
name|pollFile
parameter_list|(
name|String
name|fileName
parameter_list|,
name|List
argument_list|<
name|GenericFile
argument_list|<
name|T
argument_list|>
argument_list|>
name|fileList
parameter_list|)
function_decl|;
comment|/**      * Processes the exchange      *      * @param exchange the exchange      */
DECL|method|processExchange (final GenericFileExchange<T> exchange)
specifier|protected
name|void
name|processExchange
parameter_list|(
specifier|final
name|GenericFileExchange
argument_list|<
name|T
argument_list|>
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Processing remote file: "
operator|+
name|exchange
operator|.
name|getGenericFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
specifier|final
name|GenericFileProcessStrategy
argument_list|<
name|T
argument_list|>
name|processStrategy
init|=
name|endpoint
operator|.
name|getGenericFileProcessStrategy
argument_list|()
decl_stmt|;
if|if
condition|(
name|processStrategy
operator|.
name|begin
argument_list|(
name|operations
argument_list|,
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getGenericFile
argument_list|()
argument_list|)
condition|)
block|{
comment|// must use file from exchange as it can be updated due the
comment|// preMoveNamePrefix/preMoveNamePostfix options
specifier|final
name|GenericFile
argument_list|<
name|T
argument_list|>
name|target
init|=
name|exchange
operator|.
name|getGenericFile
argument_list|()
decl_stmt|;
comment|// must use full name when downloading so we have the correct path
specifier|final
name|String
name|name
init|=
name|target
operator|.
name|getAbsoluteFileName
argument_list|()
decl_stmt|;
comment|// retrieve the file using the stream
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Retreiving file: "
operator|+
name|name
operator|+
literal|" from: "
operator|+
name|endpoint
argument_list|)
expr_stmt|;
block|}
name|operations
operator|.
name|retrieveFile
argument_list|(
name|name
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Retrieved file: "
operator|+
name|name
operator|+
literal|" from: "
operator|+
name|endpoint
argument_list|)
expr_stmt|;
block|}
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
literal|"About to process file: "
operator|+
name|target
operator|+
literal|" using exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
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
specifier|final
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
init|=
name|exchange
operator|.
name|getGenericFile
argument_list|()
decl_stmt|;
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
name|log
operator|.
name|warn
argument_list|(
name|endpoint
operator|+
literal|" cannot process remote file: "
operator|+
name|exchange
operator|.
name|getGenericFile
argument_list|()
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
comment|/**      * Strategy when the file was processed and a commit should be executed.      *      * @param processStrategy the strategy to perform the commit      * @param exchange        the exchange      * @param file            the file processed      * @param failureHandled  is<tt>false</tt> if the exchange was processed succesfully,      *<tt>true</tt> if an exception occured during processing but it      *                        was handled by the failure processor (usually the DeadLetterChannel).      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|processStrategyCommit (GenericFileProcessStrategy<T> processStrategy, GenericFileExchange<T> exchange, GenericFile<T> file, boolean failureHandled)
specifier|protected
name|void
name|processStrategyCommit
parameter_list|(
name|GenericFileProcessStrategy
argument_list|<
name|T
argument_list|>
name|processStrategy
parameter_list|,
name|GenericFileExchange
argument_list|<
name|T
argument_list|>
name|exchange
parameter_list|,
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|,
name|boolean
name|failureHandled
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|isIdempotent
argument_list|()
condition|)
block|{
comment|// only add to idempotent repository if we could process the file
comment|// only use the filename as the key as the file could be moved into a done folder
name|endpoint
operator|.
name|getIdempotentRepository
argument_list|()
operator|.
name|add
argument_list|(
name|file
operator|.
name|getFileName
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
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
literal|"Committing remote file strategy: "
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
name|operations
argument_list|,
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
name|log
operator|.
name|warn
argument_list|(
literal|"Error committing remote file strategy: "
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
comment|/**      * Strategy when the file was not processed and a rollback should be      * executed.      *      * @param processStrategy the strategy to perform the commit      * @param exchange        the exchange      * @param file            the file processed      */
DECL|method|processStrategyRollback (GenericFileProcessStrategy<T> processStrategy, GenericFileExchange<T> exchange, GenericFile<T> file)
specifier|protected
name|void
name|processStrategyRollback
parameter_list|(
name|GenericFileProcessStrategy
argument_list|<
name|T
argument_list|>
name|processStrategy
parameter_list|,
name|GenericFileExchange
argument_list|<
name|T
argument_list|>
name|exchange
parameter_list|,
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|)
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
literal|"Rolling back remote file strategy: "
operator|+
name|processStrategy
operator|+
literal|" for file: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|processStrategy
operator|.
name|rollback
argument_list|(
name|operations
argument_list|,
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
name|log
operator|.
name|warn
argument_list|(
literal|"Error rolling back remote file strategy: "
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
comment|/**      * Strategy for validating if the given remote file should be included or      * not      *      * @param file        the remote file      * @param isDirectory wether the file is a directory or a file      * @return<tt>true</tt> to include the file,<tt>false</tt> to skip it      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|isValidFile (GenericFile<T> file, boolean isDirectory)
specifier|protected
name|boolean
name|isValidFile
parameter_list|(
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|,
name|boolean
name|isDirectory
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isMatched
argument_list|(
name|file
argument_list|,
name|isDirectory
argument_list|)
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Remote file did not match. Will skip this remote file: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
elseif|else
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
name|getFileName
argument_list|()
argument_list|)
condition|)
block|{
comment|// only use the filename as the key as the file could be moved into a done folder
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"RemoteFileConsumer is idempotent and the file has been consumed before. Will skip this remote file: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
comment|// file matched
return|return
literal|true
return|;
block|}
comment|/**      * Strategy to perform file matching based on endpoint configuration.      *<p/>      * Will always return<tt>false</tt> for certain files/folders:      *<ul>      *<li>Starting with a dot</li>      *<li>lock files</li>      *</ul>      * And then<tt>true</tt> for directories.      *      * @param file        the remote file      * @param isDirectory wether the file is a directory or a file      * @return<tt>true</tt> if the remote file is matched,<tt>false</tt> if      *         not      */
DECL|method|isMatched (GenericFile<T> file, boolean isDirectory)
specifier|protected
name|boolean
name|isMatched
parameter_list|(
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|,
name|boolean
name|isDirectory
parameter_list|)
block|{
name|String
name|name
init|=
name|file
operator|.
name|getFileName
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
name|FileComponent
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
name|isDirectory
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
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|endpoint
operator|.
name|getRegexPattern
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|name
operator|.
name|matches
argument_list|(
name|endpoint
operator|.
name|getRegexPattern
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
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|endpoint
operator|.
name|getExcludeNamePrefix
argument_list|()
argument_list|)
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
name|getExcludeNamePrefix
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
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|endpoint
operator|.
name|getExcludeNamePostfix
argument_list|()
argument_list|)
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
name|getExcludeNamePostfix
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
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|endpoint
operator|.
name|getIncludeNamePrefix
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|name
operator|.
name|startsWith
argument_list|(
name|endpoint
operator|.
name|getIncludeNamePrefix
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
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|endpoint
operator|.
name|getIncludeNamePostfix
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|name
operator|.
name|endsWith
argument_list|(
name|endpoint
operator|.
name|getIncludeNamePostfix
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
comment|// use file expression for a simple dynamic file filter
if|if
condition|(
name|endpoint
operator|.
name|getFileExpression
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|evaluteFileExpression
argument_list|()
expr_stmt|;
if|if
condition|(
name|fileExpressionResult
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|name
operator|.
name|equals
argument_list|(
name|fileExpressionResult
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
DECL|method|evaluteFileExpression ()
specifier|private
name|void
name|evaluteFileExpression
parameter_list|()
block|{
if|if
condition|(
name|fileExpressionResult
operator|==
literal|null
condition|)
block|{
comment|// create a dummy exchange as Exchange is needed for expression evaluation
name|Exchange
name|dummy
init|=
operator|new
name|DefaultExchange
argument_list|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|fileExpressionResult
operator|=
operator|(
name|String
operator|)
name|endpoint
operator|.
name|getFileExpression
argument_list|()
operator|.
name|evaluate
argument_list|(
name|dummy
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

