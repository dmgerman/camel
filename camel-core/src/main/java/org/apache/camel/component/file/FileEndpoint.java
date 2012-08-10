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
name|FileNotFoundException
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
name|Component
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
name|processor
operator|.
name|idempotent
operator|.
name|MemoryIdempotentRepository
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
name|FileUtil
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

begin_comment
comment|/**  * File endpoint.  */
end_comment

begin_class
DECL|class|FileEndpoint
specifier|public
class|class
name|FileEndpoint
extends|extends
name|GenericFileEndpoint
argument_list|<
name|File
argument_list|>
block|{
DECL|field|operations
specifier|private
name|FileOperations
name|operations
init|=
operator|new
name|FileOperations
argument_list|(
name|this
argument_list|)
decl_stmt|;
DECL|field|file
specifier|private
name|File
name|file
decl_stmt|;
DECL|field|copyAndDeleteOnRenameFail
specifier|private
name|boolean
name|copyAndDeleteOnRenameFail
init|=
literal|true
decl_stmt|;
DECL|method|FileEndpoint ()
specifier|public
name|FileEndpoint
parameter_list|()
block|{
comment|// use marker file as default exclusive read locks
name|this
operator|.
name|readLock
operator|=
literal|"markerFile"
expr_stmt|;
block|}
DECL|method|FileEndpoint (String endpointUri, Component component)
specifier|public
name|FileEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
comment|// use marker file as default exclusive read locks
name|this
operator|.
name|readLock
operator|=
literal|"markerFile"
expr_stmt|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|FileConsumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|operations
argument_list|,
literal|"operations"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|file
argument_list|,
literal|"file"
argument_list|)
expr_stmt|;
comment|// auto create starting directory if needed
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
if|if
condition|(
name|isAutoCreate
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Creating non existing starting directory: {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
name|boolean
name|absolute
init|=
name|FileUtil
operator|.
name|isAbsolute
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|boolean
name|created
init|=
name|operations
operator|.
name|buildDirectory
argument_list|(
name|file
operator|.
name|getPath
argument_list|()
argument_list|,
name|absolute
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|created
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot auto create starting directory: {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|isStartingDirectoryMustExist
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
literal|"Starting directory does not exist: "
operator|+
name|file
argument_list|)
throw|;
block|}
block|}
name|FileConsumer
name|result
init|=
name|newFileConsumer
argument_list|(
name|processor
argument_list|,
name|operations
argument_list|)
decl_stmt|;
if|if
condition|(
name|isDelete
argument_list|()
operator|&&
name|getMove
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"You cannot set both delete=true and move options"
argument_list|)
throw|;
block|}
comment|// if noop=true then idempotent should also be configured
if|if
condition|(
name|isNoop
argument_list|()
operator|&&
operator|!
name|isIdempotentSet
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Endpoint is configured with noop=true so forcing endpoint to be idempotent as well"
argument_list|)
expr_stmt|;
name|setIdempotent
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|// if idempotent and no repository set then create a default one
if|if
condition|(
name|isIdempotentSet
argument_list|()
operator|&&
name|isIdempotent
argument_list|()
operator|&&
name|idempotentRepository
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Using default memory based idempotent repository with cache max size: "
operator|+
name|DEFAULT_IDEMPOTENT_CACHE_SIZE
argument_list|)
expr_stmt|;
name|idempotentRepository
operator|=
name|MemoryIdempotentRepository
operator|.
name|memoryIdempotentRepository
argument_list|(
name|DEFAULT_IDEMPOTENT_CACHE_SIZE
argument_list|)
expr_stmt|;
block|}
comment|// set max messages per poll
name|result
operator|.
name|setMaxMessagesPerPoll
argument_list|(
name|getMaxMessagesPerPoll
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setEagerLimitMaxMessagesPerPoll
argument_list|(
name|isEagerMaxMessagesPerPoll
argument_list|()
argument_list|)
expr_stmt|;
name|configureConsumer
argument_list|(
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|GenericFileProducer
argument_list|<
name|File
argument_list|>
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|operations
argument_list|,
literal|"operations"
argument_list|)
expr_stmt|;
comment|// you cannot use temp prefix and file exists append
if|if
condition|(
name|getFileExist
argument_list|()
operator|==
name|GenericFileExist
operator|.
name|Append
operator|&&
name|getTempPrefix
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"You cannot set both fileExist=Append and tempPrefix options"
argument_list|)
throw|;
block|}
return|return
operator|new
name|GenericFileProducer
argument_list|<
name|File
argument_list|>
argument_list|(
name|this
argument_list|,
name|operations
argument_list|)
return|;
block|}
DECL|method|createExchange (GenericFile<File> file)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|GenericFile
argument_list|<
name|File
argument_list|>
name|file
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
name|file
operator|.
name|bindToExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return
name|exchange
return|;
block|}
comment|/**      * Strategy to create a new {@link FileConsumer}      *      * @param processor  the given processor      * @param operations file operations      * @return the created consumer      */
DECL|method|newFileConsumer (Processor processor, GenericFileOperations<File> operations)
specifier|protected
name|FileConsumer
name|newFileConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|GenericFileOperations
argument_list|<
name|File
argument_list|>
name|operations
parameter_list|)
block|{
return|return
operator|new
name|FileConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|operations
argument_list|)
return|;
block|}
DECL|method|getFile ()
specifier|public
name|File
name|getFile
parameter_list|()
block|{
return|return
name|file
return|;
block|}
DECL|method|setFile (File file)
specifier|public
name|void
name|setFile
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|this
operator|.
name|file
operator|=
name|file
expr_stmt|;
comment|// update configuration as well
name|getConfiguration
argument_list|()
operator|.
name|setDirectory
argument_list|(
name|FileUtil
operator|.
name|isAbsolute
argument_list|(
name|file
argument_list|)
condition|?
name|file
operator|.
name|getAbsolutePath
argument_list|()
else|:
name|file
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getScheme ()
specifier|public
name|String
name|getScheme
parameter_list|()
block|{
return|return
literal|"file"
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
name|getFile
argument_list|()
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getFileSeparator ()
specifier|public
name|char
name|getFileSeparator
parameter_list|()
block|{
return|return
name|File
operator|.
name|separatorChar
return|;
block|}
annotation|@
name|Override
DECL|method|isAbsolute (String name)
specifier|public
name|boolean
name|isAbsolute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// relative or absolute path?
return|return
name|FileUtil
operator|.
name|isAbsolute
argument_list|(
operator|new
name|File
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isCopyAndDeleteOnRenameFail ()
specifier|public
name|boolean
name|isCopyAndDeleteOnRenameFail
parameter_list|()
block|{
return|return
name|copyAndDeleteOnRenameFail
return|;
block|}
DECL|method|setCopyAndDeleteOnRenameFail (boolean copyAndDeleteOnRenameFail)
specifier|public
name|void
name|setCopyAndDeleteOnRenameFail
parameter_list|(
name|boolean
name|copyAndDeleteOnRenameFail
parameter_list|)
block|{
name|this
operator|.
name|copyAndDeleteOnRenameFail
operator|=
name|copyAndDeleteOnRenameFail
expr_stmt|;
block|}
block|}
end_class

end_unit

