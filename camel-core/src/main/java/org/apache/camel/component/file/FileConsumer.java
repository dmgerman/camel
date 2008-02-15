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
name|ScheduledPollConsumer
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
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|ExecutionException
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
name|Future
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 523016 $  */
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
specifier|final
name|FileEndpoint
name|endpoint
decl_stmt|;
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
DECL|field|generateEmptyExchangeWhenIdle
name|boolean
name|generateEmptyExchangeWhenIdle
decl_stmt|;
DECL|field|unchangedDelay
specifier|private
name|int
name|unchangedDelay
init|=
literal|0
decl_stmt|;
DECL|field|unchangedSize
specifier|private
name|boolean
name|unchangedSize
init|=
literal|false
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
name|isRecursive
argument_list|()
argument_list|)
decl_stmt|;
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
comment|/**      *       * @param fileOrDirectory      * @param processDir      * @return the number of files processed or being processed async.      */
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
return|return
name|pollFile
argument_list|(
name|fileOrDirectory
argument_list|)
return|;
comment|// process the file
block|}
elseif|else
if|if
condition|(
name|processDir
condition|)
block|{
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Polling directory "
operator|+
name|fileOrDirectory
argument_list|)
expr_stmt|;
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
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|files
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|rc
operator|+=
name|pollFileOrDirectory
argument_list|(
name|files
index|[
name|i
index|]
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Skipping directory "
operator|+
name|fileOrDirectory
argument_list|)
expr_stmt|;
return|return
literal|0
return|;
block|}
block|}
DECL|field|filesBeingProcessed
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
comment|/**      * @param file      * @return the number of files processed or being processed async.      */
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
name|endpoint
operator|.
name|isNoop
argument_list|()
condition|)
block|{
name|long
name|fileModified
init|=
name|file
operator|.
name|lastModified
argument_list|()
decl_stmt|;
if|if
condition|(
name|fileModified
operator|<=
name|lastPollTime
condition|)
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
literal|"Ignoring file: "
operator|+
name|file
operator|+
literal|" as modified time: "
operator|+
name|fileModified
operator|+
literal|" less than last poll time: "
operator|+
name|lastPollTime
argument_list|)
expr_stmt|;
block|}
return|return
literal|0
return|;
block|}
block|}
else|else
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
literal|"About to process file:  "
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
comment|// the
comment|// exchange can happen asynchronously
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
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|processStrategy
operator|.
name|commit
argument_list|(
name|endpoint
argument_list|,
operator|(
name|FileExchange
operator|)
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
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|handleException
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
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
literal|" cannot process file: "
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
comment|// TODO: maybe use a configurable strategy instead of the
comment|// hardcoded one based on last file change
if|if
condition|(
name|isMatched
argument_list|(
name|file
argument_list|)
operator|&&
name|isUnchanged
argument_list|(
name|file
argument_list|)
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
DECL|method|isUnchanged (File file)
specifier|protected
name|boolean
name|isUnchanged
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
name|boolean
name|lastModifiedCheck
init|=
literal|true
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
operator|(
name|modifiedDuration
operator|>=
name|getUnchangedDelay
argument_list|()
operator|)
expr_stmt|;
block|}
name|boolean
name|sizeCheck
init|=
literal|true
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
name|long
name|prevFileSize
init|=
operator|(
name|fileSizes
operator|.
name|get
argument_list|(
name|file
argument_list|)
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|fileSizes
operator|.
name|get
argument_list|(
name|file
argument_list|)
operator|.
name|longValue
argument_list|()
decl_stmt|;
name|sizeDifference
operator|=
name|file
operator|.
name|length
argument_list|()
operator|-
name|prevFileSize
expr_stmt|;
name|sizeCheck
operator|=
operator|(
literal|0
operator|==
name|sizeDifference
operator|)
expr_stmt|;
block|}
name|boolean
name|answer
init|=
name|lastModifiedCheck
operator|&&
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
literal|" isUnchanged:"
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
name|remove
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
else|else
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
comment|/**      * @return the recursive      */
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
comment|/**      * @param recursive the recursive to set      */
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
comment|/**      * @return the regexPattern      */
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
comment|/**      * @param regexPattern the regexPattern to set      */
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
block|}
end_class

end_unit

