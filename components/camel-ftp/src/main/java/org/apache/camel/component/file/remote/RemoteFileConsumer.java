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
name|util
operator|.
name|concurrent
operator|.
name|ScheduledExecutorService
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

begin_class
DECL|class|RemoteFileConsumer
specifier|public
specifier|abstract
class|class
name|RemoteFileConsumer
parameter_list|<
name|T
extends|extends
name|RemoteFileExchange
parameter_list|>
extends|extends
name|ScheduledPollConsumer
argument_list|<
name|T
argument_list|>
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
name|RemoteFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
decl_stmt|;
comment|// @deprecated lastPollTime to be removed in Camel 2.0
DECL|field|lastPollTime
specifier|protected
name|long
name|lastPollTime
decl_stmt|;
DECL|field|recursive
specifier|protected
name|boolean
name|recursive
decl_stmt|;
DECL|field|regexPattern
specifier|protected
name|String
name|regexPattern
decl_stmt|;
DECL|field|setNames
specifier|protected
name|boolean
name|setNames
init|=
literal|true
decl_stmt|;
DECL|field|exclusiveReadLock
specifier|protected
name|boolean
name|exclusiveReadLock
decl_stmt|;
DECL|field|deleteFile
specifier|protected
name|boolean
name|deleteFile
decl_stmt|;
DECL|field|moveNamePrefix
specifier|protected
name|String
name|moveNamePrefix
decl_stmt|;
DECL|field|moveNamePostfix
specifier|protected
name|String
name|moveNamePostfix
decl_stmt|;
DECL|field|excludedNamePrefix
specifier|protected
name|String
name|excludedNamePrefix
decl_stmt|;
DECL|field|excludedNamePostfix
specifier|protected
name|String
name|excludedNamePostfix
decl_stmt|;
DECL|field|timestamp
specifier|private
name|boolean
name|timestamp
decl_stmt|;
DECL|method|RemoteFileConsumer (RemoteFileEndpoint<T> endpoint, Processor processor)
specifier|public
name|RemoteFileConsumer
parameter_list|(
name|RemoteFileEndpoint
argument_list|<
name|T
argument_list|>
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
DECL|method|RemoteFileConsumer (RemoteFileEndpoint<T> endpoint, Processor processor, ScheduledExecutorService executor)
specifier|public
name|RemoteFileConsumer
parameter_list|(
name|RemoteFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
parameter_list|,
name|Processor
name|processor
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
block|}
comment|/**      * Gets the filename.      *      * @param file the file object for the given consumer implementation.      * @return the filename as String.      */
DECL|method|getFileName (Object file)
specifier|protected
specifier|abstract
name|String
name|getFileName
parameter_list|(
name|Object
name|file
parameter_list|)
function_decl|;
comment|/**      * Is the given file matched to be consumed.      */
DECL|method|isMatched (Object file)
specifier|protected
name|boolean
name|isMatched
parameter_list|(
name|Object
name|file
parameter_list|)
block|{
name|String
name|name
init|=
name|getFileName
argument_list|(
name|file
argument_list|)
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
name|excludedNamePrefix
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
name|excludedNamePrefix
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
name|excludedNamePostfix
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
name|excludedNamePostfix
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
comment|/**      * Should the file be moved after consuming?      */
DECL|method|isMoveFile ()
specifier|protected
name|boolean
name|isMoveFile
parameter_list|()
block|{
return|return
name|moveNamePostfix
operator|!=
literal|null
operator|||
name|moveNamePrefix
operator|!=
literal|null
operator|||
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getExpression
argument_list|()
operator|!=
literal|null
return|;
block|}
comment|/**      * Gets the to filename for moving.      *      * @param name the original filename      * @param exchange the current exchange      * @return the move filename      */
DECL|method|getMoveFileName (String name, Exchange exchange)
specifier|protected
name|String
name|getMoveFileName
parameter_list|(
name|String
name|name
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// move according to the expression
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getExpression
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Object
name|result
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getExpression
argument_list|()
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|result
argument_list|)
return|;
block|}
comment|// move according to the pre and postfix
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
if|if
condition|(
name|moveNamePrefix
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|moveNamePrefix
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|moveNamePostfix
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|moveNamePostfix
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|remoteServer ()
specifier|protected
name|String
name|remoteServer
parameter_list|()
block|{
return|return
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|remoteServerInformation
argument_list|()
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
DECL|method|isSetNames ()
specifier|public
name|boolean
name|isSetNames
parameter_list|()
block|{
return|return
name|setNames
return|;
block|}
DECL|method|setSetNames (boolean setNames)
specifier|public
name|void
name|setSetNames
parameter_list|(
name|boolean
name|setNames
parameter_list|)
block|{
name|this
operator|.
name|setNames
operator|=
name|setNames
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
DECL|method|isDeleteFile ()
specifier|public
name|boolean
name|isDeleteFile
parameter_list|()
block|{
return|return
name|deleteFile
return|;
block|}
DECL|method|setDeleteFile (boolean deleteFile)
specifier|public
name|void
name|setDeleteFile
parameter_list|(
name|boolean
name|deleteFile
parameter_list|)
block|{
name|this
operator|.
name|deleteFile
operator|=
name|deleteFile
expr_stmt|;
block|}
DECL|method|getMoveNamePrefix ()
specifier|public
name|String
name|getMoveNamePrefix
parameter_list|()
block|{
return|return
name|moveNamePrefix
return|;
block|}
DECL|method|setMoveNamePrefix (String moveNamePrefix)
specifier|public
name|void
name|setMoveNamePrefix
parameter_list|(
name|String
name|moveNamePrefix
parameter_list|)
block|{
name|this
operator|.
name|moveNamePrefix
operator|=
name|moveNamePrefix
expr_stmt|;
block|}
DECL|method|getMoveNamePostfix ()
specifier|public
name|String
name|getMoveNamePostfix
parameter_list|()
block|{
return|return
name|moveNamePostfix
return|;
block|}
DECL|method|setMoveNamePostfix (String moveNamePostfix)
specifier|public
name|void
name|setMoveNamePostfix
parameter_list|(
name|String
name|moveNamePostfix
parameter_list|)
block|{
name|this
operator|.
name|moveNamePostfix
operator|=
name|moveNamePostfix
expr_stmt|;
block|}
DECL|method|getExcludedNamePrefix ()
specifier|public
name|String
name|getExcludedNamePrefix
parameter_list|()
block|{
return|return
name|excludedNamePrefix
return|;
block|}
DECL|method|setExcludedNamePrefix (String excludedNamePrefix)
specifier|public
name|void
name|setExcludedNamePrefix
parameter_list|(
name|String
name|excludedNamePrefix
parameter_list|)
block|{
name|this
operator|.
name|excludedNamePrefix
operator|=
name|excludedNamePrefix
expr_stmt|;
block|}
DECL|method|getExcludedNamePostfix ()
specifier|public
name|String
name|getExcludedNamePostfix
parameter_list|()
block|{
return|return
name|excludedNamePostfix
return|;
block|}
DECL|method|setExcludedNamePostfix (String excludedNamePostfix)
specifier|public
name|void
name|setExcludedNamePostfix
parameter_list|(
name|String
name|excludedNamePostfix
parameter_list|)
block|{
name|this
operator|.
name|excludedNamePostfix
operator|=
name|excludedNamePostfix
expr_stmt|;
block|}
comment|/**      * @deprecated the timestamp feature will be removed in Camel 2.0      */
DECL|method|isTimestamp ()
specifier|public
name|boolean
name|isTimestamp
parameter_list|()
block|{
return|return
name|timestamp
return|;
block|}
comment|/**      * Sets wether polling should use last poll timestamp for filtering only new files.      * @deprecated the timestamp feature will be removed in Camel 2.0      */
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
name|timestamp
operator|=
name|timestamp
expr_stmt|;
block|}
block|}
end_class

end_unit

