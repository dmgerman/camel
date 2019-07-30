begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.caffeine.load
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|caffeine
operator|.
name|load
package|;
end_package

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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|benmanes
operator|.
name|caffeine
operator|.
name|cache
operator|.
name|LoadingCache
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
name|CamelExchangeException
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
name|caffeine
operator|.
name|CaffeineConfiguration
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
name|caffeine
operator|.
name|CaffeineConstants
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
name|spi
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
name|support
operator|.
name|HeaderSelectorProducer
import|;
end_import

begin_class
DECL|class|CaffeineLoadCacheProducer
specifier|public
class|class
name|CaffeineLoadCacheProducer
extends|extends
name|HeaderSelectorProducer
block|{
DECL|field|configuration
specifier|private
specifier|final
name|CaffeineConfiguration
name|configuration
decl_stmt|;
DECL|field|cache
specifier|private
specifier|final
name|LoadingCache
name|cache
decl_stmt|;
DECL|method|CaffeineLoadCacheProducer (CaffeineLoadCacheEndpoint endpoint, String cacheName, CaffeineConfiguration configuration, LoadingCache cache)
specifier|public
name|CaffeineLoadCacheProducer
parameter_list|(
name|CaffeineLoadCacheEndpoint
name|endpoint
parameter_list|,
name|String
name|cacheName
parameter_list|,
name|CaffeineConfiguration
name|configuration
parameter_list|,
name|LoadingCache
name|cache
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|CaffeineConstants
operator|.
name|ACTION
argument_list|,
parameter_list|()
lambda|->
name|configuration
operator|.
name|getAction
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
block|}
comment|// ****************************
comment|// Handlers
comment|// ****************************
annotation|@
name|InvokeOnHeader
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_CLEANUP
argument_list|)
DECL|method|onCleanUp (Message message)
specifier|public
name|void
name|onCleanUp
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|cache
operator|.
name|cleanUp
argument_list|()
expr_stmt|;
name|setResult
argument_list|(
name|message
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_PUT
argument_list|)
DECL|method|onPut (Message message)
specifier|public
name|void
name|onPut
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|cache
operator|.
name|put
argument_list|(
name|getKey
argument_list|(
name|message
argument_list|)
argument_list|,
name|getValue
argument_list|(
name|message
argument_list|,
name|configuration
operator|.
name|getValueType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setResult
argument_list|(
name|message
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_PUT_ALL
argument_list|)
DECL|method|onPutAll (Message message)
specifier|public
name|void
name|onPutAll
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|cache
operator|.
name|putAll
argument_list|(
operator|(
name|Map
operator|)
name|getValue
argument_list|(
name|message
argument_list|,
name|Map
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|setResult
argument_list|(
name|message
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_GET
argument_list|)
DECL|method|onGet (Message message)
specifier|public
name|void
name|onGet
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|result
init|=
name|cache
operator|.
name|get
argument_list|(
name|getKey
argument_list|(
name|message
argument_list|)
argument_list|)
decl_stmt|;
name|setResult
argument_list|(
name|message
argument_list|,
literal|true
argument_list|,
name|result
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_GET_ALL
argument_list|)
DECL|method|onGetAll (Message message)
specifier|public
name|void
name|onGetAll
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|result
init|=
name|cache
operator|.
name|getAll
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|CaffeineConstants
operator|.
name|KEYS
argument_list|,
name|Collections
operator|::
name|emptySet
argument_list|,
name|Set
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|setResult
argument_list|(
name|message
argument_list|,
literal|true
argument_list|,
name|result
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_INVALIDATE
argument_list|)
DECL|method|onInvalidate (Message message)
specifier|public
name|void
name|onInvalidate
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|cache
operator|.
name|invalidate
argument_list|(
name|getKey
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|setResult
argument_list|(
name|message
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_INVALIDATE_ALL
argument_list|)
DECL|method|onInvalidateAll (Message message)
specifier|public
name|void
name|onInvalidateAll
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|cache
operator|.
name|invalidateAll
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|CaffeineConstants
operator|.
name|KEYS
argument_list|,
name|Collections
operator|::
name|emptySet
argument_list|,
name|Set
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|setResult
argument_list|(
name|message
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|// ****************************
comment|// Helpers
comment|// ****************************
DECL|method|getKey (final Message message)
specifier|private
name|Object
name|getKey
parameter_list|(
specifier|final
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|value
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|CaffeineConstants
operator|.
name|KEY
argument_list|,
name|configuration
operator|.
name|getKeyType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
name|configuration
operator|.
name|getKey
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"No value provided in header or as default value ("
operator|+
name|CaffeineConstants
operator|.
name|KEY
operator|+
literal|")"
argument_list|,
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|value
return|;
block|}
DECL|method|getValue (final Message message, final Class<?> type)
specifier|private
name|Object
name|getValue
parameter_list|(
specifier|final
name|Message
name|message
parameter_list|,
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|value
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|CaffeineConstants
operator|.
name|VALUE
argument_list|,
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
name|message
operator|.
name|getBody
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"No value provided in header or body ("
operator|+
name|CaffeineConstants
operator|.
name|VALUE
operator|+
literal|")"
argument_list|,
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|value
return|;
block|}
DECL|method|setResult (Message message, boolean success, Object result, Object oldValue)
specifier|private
name|void
name|setResult
parameter_list|(
name|Message
name|message
parameter_list|,
name|boolean
name|success
parameter_list|,
name|Object
name|result
parameter_list|,
name|Object
name|oldValue
parameter_list|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_SUCCEEDED
argument_list|,
name|success
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_HAS_RESULT
argument_list|,
name|oldValue
operator|!=
literal|null
operator|||
name|result
operator|!=
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|oldValue
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|CaffeineConstants
operator|.
name|OLD_VALUE
argument_list|,
name|oldValue
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
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
block|}
end_class

end_unit

