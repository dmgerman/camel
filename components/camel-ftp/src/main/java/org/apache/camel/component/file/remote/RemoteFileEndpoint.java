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
name|component
operator|.
name|file
operator|.
name|GenericFile
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
name|GenericFileEndpoint
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
name|GenericFileProducer
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
comment|/**  * Remote file endpoint.  */
end_comment

begin_class
DECL|class|RemoteFileEndpoint
specifier|public
specifier|abstract
class|class
name|RemoteFileEndpoint
parameter_list|<
name|T
parameter_list|>
extends|extends
name|GenericFileEndpoint
argument_list|<
name|T
argument_list|>
block|{
DECL|field|maximumReconnectAttempts
specifier|private
name|int
name|maximumReconnectAttempts
init|=
literal|3
decl_stmt|;
DECL|field|reconnectDelay
specifier|private
name|long
name|reconnectDelay
init|=
literal|1000
decl_stmt|;
DECL|method|RemoteFileEndpoint ()
specifier|public
name|RemoteFileEndpoint
parameter_list|()
block|{
comment|// no args constructor for spring bean endpoint configuration
block|}
DECL|method|RemoteFileEndpoint (String uri, RemoteFileComponent<T> component, RemoteFileConfiguration configuration)
specifier|public
name|RemoteFileEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|RemoteFileComponent
argument_list|<
name|T
argument_list|>
name|component
parameter_list|,
name|RemoteFileConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createExchange (GenericFile<T> file)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|)
block|{
name|Exchange
name|answer
init|=
operator|new
name|DefaultExchange
argument_list|(
name|this
argument_list|)
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
name|answer
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|GenericFileProducer
argument_list|<
name|T
argument_list|>
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|afterPropertiesSet
argument_list|()
expr_stmt|;
return|return
name|buildProducer
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|RemoteFileConsumer
argument_list|<
name|T
argument_list|>
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|afterPropertiesSet
argument_list|()
expr_stmt|;
name|RemoteFileConsumer
argument_list|<
name|T
argument_list|>
name|consumer
init|=
name|buildConsumer
argument_list|(
name|processor
argument_list|)
decl_stmt|;
comment|// we assume its a file if the name has a dot in it (eg foo.txt)
if|if
condition|(
name|configuration
operator|.
name|getDirectory
argument_list|()
operator|.
name|contains
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Only directory is supported. Endpoint must be configured with a valid directory: "
operator|+
name|configuration
operator|.
name|getDirectory
argument_list|()
argument_list|)
throw|;
block|}
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
literal|"You cannot both set delete=true and move options"
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
comment|// set max messages per poll
name|consumer
operator|.
name|setMaxMessagesPerPoll
argument_list|(
name|getMaxMessagesPerPoll
argument_list|()
argument_list|)
expr_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
comment|/**      * Validates this endpoint if its configured properly.      *      * @throws Exception is thrown if endpoint is invalid configured for its mandatory options      */
DECL|method|afterPropertiesSet ()
specifier|protected
name|void
name|afterPropertiesSet
parameter_list|()
throws|throws
name|Exception
block|{
name|RemoteFileConfiguration
name|config
init|=
operator|(
name|RemoteFileConfiguration
operator|)
name|getConfiguration
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|config
operator|.
name|getHost
argument_list|()
argument_list|,
literal|"host"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|config
operator|.
name|getProtocol
argument_list|()
argument_list|,
literal|"protocol"
argument_list|)
expr_stmt|;
if|if
condition|(
name|config
operator|.
name|getPort
argument_list|()
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"port is not assigned to a positive value"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Remote File Endpoints, impl this method to create a custom consumer specific to their "protocol" etc.      *      * @param processor  the processor      * @return the created consumer      */
DECL|method|buildConsumer (Processor processor)
specifier|protected
specifier|abstract
name|RemoteFileConsumer
argument_list|<
name|T
argument_list|>
name|buildConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Remote File Endpoints, impl this method to create a custom producer specific to their "protocol" etc.      *      * @return the created producer      */
DECL|method|buildProducer ()
specifier|protected
specifier|abstract
name|GenericFileProducer
argument_list|<
name|T
argument_list|>
name|buildProducer
parameter_list|()
function_decl|;
comment|/**      * Returns human readable server information for logging purpose      */
DECL|method|remoteServerInformation ()
specifier|public
name|String
name|remoteServerInformation
parameter_list|()
block|{
return|return
operator|(
operator|(
name|RemoteFileConfiguration
operator|)
name|configuration
operator|)
operator|.
name|remoteServerInformation
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
literal|'/'
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
return|return
name|name
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
return|;
block|}
DECL|method|getMaximumReconnectAttempts ()
specifier|public
name|int
name|getMaximumReconnectAttempts
parameter_list|()
block|{
return|return
name|maximumReconnectAttempts
return|;
block|}
DECL|method|setMaximumReconnectAttempts (int maximumReconnectAttempts)
specifier|public
name|void
name|setMaximumReconnectAttempts
parameter_list|(
name|int
name|maximumReconnectAttempts
parameter_list|)
block|{
name|this
operator|.
name|maximumReconnectAttempts
operator|=
name|maximumReconnectAttempts
expr_stmt|;
block|}
DECL|method|getReconnectDelay ()
specifier|public
name|long
name|getReconnectDelay
parameter_list|()
block|{
return|return
name|reconnectDelay
return|;
block|}
DECL|method|setReconnectDelay (long reconnectDelay)
specifier|public
name|void
name|setReconnectDelay
parameter_list|(
name|long
name|reconnectDelay
parameter_list|)
block|{
name|this
operator|.
name|reconnectDelay
operator|=
name|reconnectDelay
expr_stmt|;
block|}
block|}
end_class

end_unit

