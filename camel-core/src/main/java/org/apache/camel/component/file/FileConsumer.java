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
name|component
operator|.
name|file
operator|.
name|strategy
operator|.
name|FileStrategy
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
name|void
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
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
expr_stmt|;
name|lastPollTime
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
block|}
DECL|method|pollFileOrDirectory (File fileOrDirectory, boolean processDir)
specifier|protected
name|void
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
name|pollFile
argument_list|(
name|fileOrDirectory
argument_list|)
expr_stmt|;
comment|// process the file
block|}
elseif|else
if|if
condition|(
name|processDir
condition|)
block|{
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
block|}
block|}
DECL|method|pollFile (final File file)
specifier|protected
name|void
name|pollFile
parameter_list|(
specifier|final
name|File
name|file
parameter_list|)
block|{
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
operator|&&
name|file
operator|.
name|lastModified
argument_list|()
operator|>
name|lastPollTime
condition|)
block|{
if|if
condition|(
name|isValidFile
argument_list|(
name|file
argument_list|)
condition|)
block|{
name|FileStrategy
name|strategy
init|=
name|endpoint
operator|.
name|getFileStrategy
argument_list|()
decl_stmt|;
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
name|strategy
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
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|strategy
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
block|}
block|}
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
if|if
condition|(
name|isMatched
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
block|}
end_class

end_unit

