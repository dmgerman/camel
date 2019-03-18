begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.client.set
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|set
package|;
end_package

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Duration
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|collections
operator|.
name|DistributedSet
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|resource
operator|.
name|ReadConsistency
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
name|InvokeOnHeader
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
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|AbstractAtomixClientProducer
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|AtomixClientConstants
operator|.
name|RESOURCE_ACTION
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|AtomixClientConstants
operator|.
name|RESOURCE_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|AtomixClientConstants
operator|.
name|RESOURCE_READ_CONSISTENCY
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|AtomixClientConstants
operator|.
name|RESOURCE_TTL
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|AtomixClientConstants
operator|.
name|RESOURCE_VALUE
import|;
end_import

begin_class
DECL|class|AtomixSetProducer
specifier|final
class|class
name|AtomixSetProducer
extends|extends
name|AbstractAtomixClientProducer
argument_list|<
name|AtomixSetEndpoint
argument_list|,
name|DistributedSet
argument_list|>
block|{
DECL|field|configuration
specifier|private
specifier|final
name|AtomixSetConfiguration
name|configuration
decl_stmt|;
DECL|method|AtomixSetProducer (AtomixSetEndpoint endpoint)
specifier|protected
name|AtomixSetProducer
parameter_list|(
name|AtomixSetEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
block|}
comment|// *********************************
comment|// Handlers
comment|// *********************************
annotation|@
name|InvokeOnHeader
argument_list|(
literal|"ADD"
argument_list|)
DECL|method|onAdd (Message message, AsyncCallback callback)
name|boolean
name|onAdd
parameter_list|(
name|Message
name|message
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|DistributedSet
argument_list|<
name|Object
argument_list|>
name|set
init|=
name|getResource
argument_list|(
name|message
argument_list|)
decl_stmt|;
specifier|final
name|long
name|ttl
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|RESOURCE_TTL
argument_list|,
name|configuration
operator|::
name|getTtl
argument_list|,
name|long
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|Object
name|val
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|RESOURCE_VALUE
argument_list|,
name|message
operator|::
name|getBody
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|val
argument_list|,
name|RESOURCE_VALUE
argument_list|)
expr_stmt|;
if|if
condition|(
name|ttl
operator|>
literal|0
condition|)
block|{
name|set
operator|.
name|add
argument_list|(
name|val
argument_list|,
name|Duration
operator|.
name|ofMillis
argument_list|(
name|ttl
argument_list|)
argument_list|)
operator|.
name|thenAccept
argument_list|(
name|result
lambda|->
name|processResult
argument_list|(
name|message
argument_list|,
name|callback
argument_list|,
name|result
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|set
operator|.
name|add
argument_list|(
name|val
argument_list|)
operator|.
name|thenAccept
argument_list|(
name|result
lambda|->
name|processResult
argument_list|(
name|message
argument_list|,
name|callback
argument_list|,
name|result
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
literal|"CLEAR"
argument_list|)
DECL|method|onClear (Message message, AsyncCallback callback)
name|boolean
name|onClear
parameter_list|(
name|Message
name|message
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|DistributedSet
argument_list|<
name|Object
argument_list|>
name|set
init|=
name|getResource
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|set
operator|.
name|clear
argument_list|()
operator|.
name|thenAccept
argument_list|(
name|result
lambda|->
name|processResult
argument_list|(
name|message
argument_list|,
name|callback
argument_list|,
name|result
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
literal|"CONTAINS"
argument_list|)
DECL|method|onContains (Message message, AsyncCallback callback)
name|boolean
name|onContains
parameter_list|(
name|Message
name|message
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|DistributedSet
argument_list|<
name|Object
argument_list|>
name|set
init|=
name|getResource
argument_list|(
name|message
argument_list|)
decl_stmt|;
specifier|final
name|ReadConsistency
name|consistency
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|RESOURCE_READ_CONSISTENCY
argument_list|,
name|configuration
operator|::
name|getReadConsistency
argument_list|,
name|ReadConsistency
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|Object
name|value
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|RESOURCE_VALUE
argument_list|,
name|message
operator|::
name|getBody
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|value
argument_list|,
name|RESOURCE_VALUE
argument_list|)
expr_stmt|;
if|if
condition|(
name|consistency
operator|!=
literal|null
condition|)
block|{
name|set
operator|.
name|contains
argument_list|(
name|value
argument_list|,
name|consistency
argument_list|)
operator|.
name|thenAccept
argument_list|(
name|result
lambda|->
name|processResult
argument_list|(
name|message
argument_list|,
name|callback
argument_list|,
name|result
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|set
operator|.
name|contains
argument_list|(
name|value
argument_list|)
operator|.
name|thenAccept
argument_list|(
name|result
lambda|->
name|processResult
argument_list|(
name|message
argument_list|,
name|callback
argument_list|,
name|result
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
literal|"IS_EMPTY"
argument_list|)
DECL|method|onIsEmpty (Message message, AsyncCallback callback)
name|boolean
name|onIsEmpty
parameter_list|(
name|Message
name|message
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|DistributedSet
argument_list|<
name|Object
argument_list|>
name|set
init|=
name|getResource
argument_list|(
name|message
argument_list|)
decl_stmt|;
specifier|final
name|ReadConsistency
name|consistency
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|RESOURCE_READ_CONSISTENCY
argument_list|,
name|configuration
operator|::
name|getReadConsistency
argument_list|,
name|ReadConsistency
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|consistency
operator|!=
literal|null
condition|)
block|{
name|set
operator|.
name|isEmpty
argument_list|(
name|consistency
argument_list|)
operator|.
name|thenAccept
argument_list|(
name|result
lambda|->
name|processResult
argument_list|(
name|message
argument_list|,
name|callback
argument_list|,
name|result
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|set
operator|.
name|isEmpty
argument_list|()
operator|.
name|thenAccept
argument_list|(
name|result
lambda|->
name|processResult
argument_list|(
name|message
argument_list|,
name|callback
argument_list|,
name|result
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
literal|"REMOVE"
argument_list|)
DECL|method|onRemove (Message message, AsyncCallback callback)
name|boolean
name|onRemove
parameter_list|(
name|Message
name|message
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|DistributedSet
argument_list|<
name|Object
argument_list|>
name|set
init|=
name|getResource
argument_list|(
name|message
argument_list|)
decl_stmt|;
specifier|final
name|Object
name|value
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|RESOURCE_VALUE
argument_list|,
name|message
operator|::
name|getBody
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|value
argument_list|,
name|RESOURCE_VALUE
argument_list|)
expr_stmt|;
name|set
operator|.
name|remove
argument_list|(
name|value
argument_list|)
operator|.
name|thenAccept
argument_list|(
name|result
lambda|->
name|processResult
argument_list|(
name|message
argument_list|,
name|callback
argument_list|,
name|result
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
literal|"SIZE"
argument_list|)
DECL|method|onSize (Message message, AsyncCallback callback)
name|boolean
name|onSize
parameter_list|(
name|Message
name|message
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|DistributedSet
argument_list|<
name|Object
argument_list|>
name|set
init|=
name|getResource
argument_list|(
name|message
argument_list|)
decl_stmt|;
specifier|final
name|ReadConsistency
name|consistency
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|RESOURCE_READ_CONSISTENCY
argument_list|,
name|configuration
operator|::
name|getReadConsistency
argument_list|,
name|ReadConsistency
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|consistency
operator|!=
literal|null
condition|)
block|{
name|set
operator|.
name|size
argument_list|(
name|consistency
argument_list|)
operator|.
name|thenAccept
argument_list|(
name|result
lambda|->
name|processResult
argument_list|(
name|message
argument_list|,
name|callback
argument_list|,
name|result
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|set
operator|.
name|size
argument_list|()
operator|.
name|thenAccept
argument_list|(
name|result
lambda|->
name|processResult
argument_list|(
name|message
argument_list|,
name|callback
argument_list|,
name|result
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
comment|// *********************************
comment|// Implementation
comment|// *********************************
annotation|@
name|Override
DECL|method|getProcessorKey (Message message)
specifier|protected
name|String
name|getProcessorKey
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|message
operator|.
name|getHeader
argument_list|(
name|RESOURCE_ACTION
argument_list|,
name|configuration
operator|::
name|getDefaultAction
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getResourceName (Message message)
specifier|protected
name|String
name|getResourceName
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|message
operator|.
name|getHeader
argument_list|(
name|RESOURCE_NAME
argument_list|,
name|getAtomixEndpoint
argument_list|()
operator|::
name|getResourceName
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createResource (String resourceName)
specifier|protected
name|DistributedSet
argument_list|<
name|Object
argument_list|>
name|createResource
parameter_list|(
name|String
name|resourceName
parameter_list|)
block|{
return|return
name|getAtomixEndpoint
argument_list|()
operator|.
name|getAtomix
argument_list|()
operator|.
name|getSet
argument_list|(
name|resourceName
argument_list|,
operator|new
name|DistributedSet
operator|.
name|Config
argument_list|(
name|getAtomixEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getResourceOptions
argument_list|(
name|resourceName
argument_list|)
argument_list|)
argument_list|,
operator|new
name|DistributedSet
operator|.
name|Options
argument_list|(
name|getAtomixEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getResourceConfig
argument_list|(
name|resourceName
argument_list|)
argument_list|)
argument_list|)
operator|.
name|join
argument_list|()
return|;
block|}
block|}
end_class

end_unit

