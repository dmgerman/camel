begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.client.map
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
name|map
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
name|ConcurrentMap
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
name|DistributedMap
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
name|AbstractAsyncAtomixClientProducer
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
name|AtomixClientAction
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
name|ExchangeHelper
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
name|RESOURCE_ACTION_HAS_RESULT
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
name|RESOURCE_DEFAULT_VALUE
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
name|RESOURCE_KEY
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
name|RESOURCE_OLD_VALUE
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
DECL|class|AtomixClientMapProducer
specifier|final
class|class
name|AtomixClientMapProducer
extends|extends
name|AbstractAsyncAtomixClientProducer
argument_list|<
name|AtomixClientMapEndpoint
argument_list|>
block|{
DECL|field|mapName
specifier|private
specifier|final
name|String
name|mapName
decl_stmt|;
DECL|field|maps
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|DistributedMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|maps
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|AtomixClientMapConfiguration
name|configuration
decl_stmt|;
DECL|method|AtomixClientMapProducer (AtomixClientMapEndpoint endpoint, String mapName)
specifier|protected
name|AtomixClientMapProducer
parameter_list|(
name|AtomixClientMapEndpoint
name|endpoint
parameter_list|,
name|String
name|mapName
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|mapName
operator|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|mapName
argument_list|,
literal|"map name"
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|endpoint
operator|.
name|getAtomixConfiguration
argument_list|()
expr_stmt|;
name|this
operator|.
name|maps
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getAction (Message message)
specifier|protected
name|AtomixClientAction
name|getAction
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
name|AtomixClientAction
operator|.
name|class
argument_list|)
return|;
block|}
comment|// *********************************
comment|// Handlers
comment|// *********************************
annotation|@
name|AsyncInvokeOnHeader
argument_list|(
name|AtomixClientAction
operator|.
name|PUT
argument_list|)
DECL|method|onPut (Message message, AsyncCallback callback)
name|boolean
name|onPut
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
name|DistributedMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|getMap
argument_list|(
name|message
argument_list|)
decl_stmt|;
specifier|final
name|Object
name|key
init|=
name|ExchangeHelper
operator|.
name|getMandatoryHeader
argument_list|(
name|message
argument_list|,
name|RESOURCE_KEY
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|Duration
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
name|Duration
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ttl
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|message
operator|.
name|getMandatoryBody
argument_list|()
argument_list|,
name|ttl
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
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|message
operator|.
name|getMandatoryBody
argument_list|()
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
name|AsyncInvokeOnHeader
argument_list|(
name|AtomixClientAction
operator|.
name|PUT_IF_ABSENT
argument_list|)
DECL|method|onPutIfAbsent (Message message, AsyncCallback callback)
name|boolean
name|onPutIfAbsent
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
name|DistributedMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|getMap
argument_list|(
name|message
argument_list|)
decl_stmt|;
specifier|final
name|Object
name|key
init|=
name|ExchangeHelper
operator|.
name|getMandatoryHeader
argument_list|(
name|message
argument_list|,
name|RESOURCE_KEY
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|Duration
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
name|Duration
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ttl
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|putIfAbsent
argument_list|(
name|key
argument_list|,
name|message
operator|.
name|getMandatoryBody
argument_list|()
argument_list|,
name|ttl
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
name|map
operator|.
name|putIfAbsent
argument_list|(
name|key
argument_list|,
name|message
operator|.
name|getMandatoryBody
argument_list|()
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
name|AsyncInvokeOnHeader
argument_list|(
name|AtomixClientAction
operator|.
name|GET
argument_list|)
DECL|method|onGet (Message message, AsyncCallback callback)
name|boolean
name|onGet
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
name|DistributedMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|getMap
argument_list|(
name|message
argument_list|)
decl_stmt|;
specifier|final
name|Object
name|key
init|=
name|ExchangeHelper
operator|.
name|getMandatoryHeader
argument_list|(
name|message
argument_list|,
name|RESOURCE_KEY
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|Object
name|defaultValue
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|RESOURCE_DEFAULT_VALUE
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
if|if
condition|(
name|defaultValue
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|getOrDefault
argument_list|(
name|key
argument_list|,
name|defaultValue
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
name|map
operator|.
name|get
argument_list|(
name|key
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
block|}
else|else
block|{
if|if
condition|(
name|defaultValue
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|getOrDefault
argument_list|(
name|key
argument_list|,
name|defaultValue
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
name|map
operator|.
name|get
argument_list|(
name|key
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
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|AsyncInvokeOnHeader
argument_list|(
name|AtomixClientAction
operator|.
name|CLEAR
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
name|DistributedMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|getMap
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|map
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
name|AsyncInvokeOnHeader
argument_list|(
name|AtomixClientAction
operator|.
name|SIZE
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
name|DistributedMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|getMap
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
name|map
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
name|map
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
annotation|@
name|AsyncInvokeOnHeader
argument_list|(
name|AtomixClientAction
operator|.
name|IS_EMPTY
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
name|DistributedMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|getMap
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
name|map
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
name|map
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
name|AsyncInvokeOnHeader
argument_list|(
name|AtomixClientAction
operator|.
name|ENTRY_SET
argument_list|)
DECL|method|onEntrySet (Message message, AsyncCallback callback)
name|boolean
name|onEntrySet
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
name|DistributedMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|getMap
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
name|map
operator|.
name|entrySet
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
name|map
operator|.
name|entrySet
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
name|AsyncInvokeOnHeader
argument_list|(
name|AtomixClientAction
operator|.
name|VALUES
argument_list|)
DECL|method|onValues (Message message, AsyncCallback callback)
name|boolean
name|onValues
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
name|DistributedMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|getMap
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
name|map
operator|.
name|values
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
name|map
operator|.
name|values
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
name|AsyncInvokeOnHeader
argument_list|(
name|AtomixClientAction
operator|.
name|CONTAINS_KEY
argument_list|)
DECL|method|onContainsKey (Message message, AsyncCallback callback)
name|boolean
name|onContainsKey
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
name|DistributedMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|getMap
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
name|key
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|RESOURCE_KEY
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
name|key
argument_list|,
name|RESOURCE_KEY
argument_list|)
expr_stmt|;
if|if
condition|(
name|consistency
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|containsKey
argument_list|(
name|key
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
name|map
operator|.
name|containsKey
argument_list|(
name|key
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
name|AsyncInvokeOnHeader
argument_list|(
name|AtomixClientAction
operator|.
name|CONTAINS_VALUE
argument_list|)
DECL|method|onContainsValue (Message message, AsyncCallback callback)
name|boolean
name|onContainsValue
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
name|DistributedMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|getMap
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
name|map
operator|.
name|containsValue
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
name|map
operator|.
name|containsValue
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
name|AsyncInvokeOnHeader
argument_list|(
name|AtomixClientAction
operator|.
name|REMOVE
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
name|DistributedMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|getMap
argument_list|(
name|message
argument_list|)
decl_stmt|;
specifier|final
name|Object
name|key
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|RESOURCE_KEY
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
name|key
argument_list|,
name|RESOURCE_VALUE
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|remove
argument_list|(
name|key
argument_list|,
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
else|else
block|{
name|map
operator|.
name|remove
argument_list|(
name|key
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
name|AsyncInvokeOnHeader
argument_list|(
name|AtomixClientAction
operator|.
name|REPLACE
argument_list|)
DECL|method|onReplace (Message message, AsyncCallback callback)
name|boolean
name|onReplace
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
name|DistributedMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|getMap
argument_list|(
name|message
argument_list|)
decl_stmt|;
specifier|final
name|Duration
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
name|Duration
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|Object
name|key
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|RESOURCE_KEY
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
specifier|final
name|Object
name|newValue
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
specifier|final
name|Object
name|oldValue
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|RESOURCE_OLD_VALUE
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
name|key
argument_list|,
name|RESOURCE_VALUE
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|newValue
argument_list|,
name|RESOURCE_VALUE
argument_list|)
expr_stmt|;
if|if
condition|(
name|ttl
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|oldValue
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|replace
argument_list|(
name|key
argument_list|,
name|oldValue
argument_list|,
name|newValue
argument_list|,
name|ttl
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
name|map
operator|.
name|replace
argument_list|(
name|key
argument_list|,
name|newValue
argument_list|,
name|ttl
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
block|}
else|else
block|{
if|if
condition|(
name|oldValue
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|replace
argument_list|(
name|key
argument_list|,
name|oldValue
argument_list|,
name|newValue
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
name|map
operator|.
name|replace
argument_list|(
name|key
argument_list|,
name|newValue
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
block|}
return|return
literal|false
return|;
block|}
comment|// *********************************
comment|// Helpers
comment|// *********************************
DECL|method|processResult (Message message, AsyncCallback callback, Object result)
specifier|private
name|void
name|processResult
parameter_list|(
name|Message
name|message
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|Object
name|result
parameter_list|)
block|{
if|if
condition|(
name|result
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|result
operator|instanceof
name|Void
operator|)
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|RESOURCE_ACTION_HAS_RESULT
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|String
name|resultHeader
init|=
name|configuration
operator|.
name|getResultHeader
argument_list|()
decl_stmt|;
if|if
condition|(
name|resultHeader
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|resultHeader
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|message
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|RESOURCE_ACTION_HAS_RESULT
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|getMap (Message message)
specifier|private
name|DistributedMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|getMap
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|maps
operator|.
name|computeIfAbsent
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|RESOURCE_NAME
argument_list|,
name|getAtomixEndpoint
argument_list|()
operator|.
name|getMapName
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|name
lambda|->
block|{
return|return
name|getAtomixEndpoint
argument_list|()
operator|.
name|getAtomix
argument_list|()
operator|.
name|getMap
argument_list|(
name|name
argument_list|,
name|getAtomixEndpoint
argument_list|()
operator|.
name|getAtomixConfiguration
argument_list|()
operator|.
name|getConfig
argument_list|()
argument_list|,
name|getAtomixEndpoint
argument_list|()
operator|.
name|getAtomixConfiguration
argument_list|()
operator|.
name|getOptions
argument_list|()
argument_list|)
operator|.
name|join
argument_list|()
return|;
block|}
argument_list|)
return|;
block|}
block|}
end_class

end_unit

