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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|NewFileEndpoint
specifier|public
class|class
name|NewFileEndpoint
extends|extends
name|GenericFileEndpoint
argument_list|<
name|File
argument_list|>
block|{
DECL|field|operations
specifier|private
name|NewFileOperations
name|operations
init|=
operator|new
name|NewFileOperations
argument_list|(
name|this
argument_list|)
decl_stmt|;
DECL|field|file
specifier|private
name|File
name|file
decl_stmt|;
DECL|method|NewFileEndpoint ()
specifier|public
name|NewFileEndpoint
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
DECL|method|NewFileEndpoint (String endpointUri, Component component)
specifier|public
name|NewFileEndpoint
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
name|NewFileConsumer
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
name|NewFileConsumer
name|result
init|=
operator|new
name|NewFileConsumer
argument_list|(
name|this
argument_list|,
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
operator|(
name|getMoveNamePrefix
argument_list|()
operator|!=
literal|null
operator|||
name|getMoveNamePostfix
argument_list|()
operator|!=
literal|null
operator|||
name|getExpression
argument_list|()
operator|!=
literal|null
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"You cannot set delete=true and a moveNamePrefix, moveNamePostfix or expression option"
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
name|isIdempotent
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
name|GenericFileExchange
argument_list|<
name|File
argument_list|>
name|createExchange
parameter_list|(
name|GenericFile
argument_list|<
name|File
argument_list|>
name|file
parameter_list|)
block|{
name|GenericFileExchange
argument_list|<
name|File
argument_list|>
name|exchange
init|=
operator|new
name|GenericFileExchange
argument_list|<
name|File
argument_list|>
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setGenericFile
argument_list|(
name|file
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|createExchange ()
specifier|public
name|GenericFileExchange
name|createExchange
parameter_list|()
block|{
return|return
operator|new
name|GenericFileExchange
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getOperations ()
specifier|public
name|NewFileOperations
name|getOperations
parameter_list|()
block|{
return|return
name|operations
return|;
block|}
DECL|method|setOperations (NewFileOperations operations)
specifier|public
name|void
name|setOperations
parameter_list|(
name|NewFileOperations
name|operations
parameter_list|)
block|{
name|this
operator|.
name|operations
operator|=
name|operations
expr_stmt|;
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
name|setFile
argument_list|(
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
comment|// TODO change to file when this is ready
return|return
literal|"newfile"
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
literal|"file://"
operator|+
name|getFile
argument_list|()
operator|.
name|getAbsolutePath
argument_list|()
return|;
block|}
block|}
end_class

end_unit

