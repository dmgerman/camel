begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.processor.idempotent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|processor
operator|.
name|idempotent
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedOperation
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|IdempotentRepository
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
name|LRUCache
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
name|LRUCacheFactory
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
name|service
operator|.
name|ServiceSupport
import|;
end_import

begin_comment
comment|/**  * A memory based implementation of {@link org.apache.camel.spi.IdempotentRepository}.   *<p/>  * Care should be taken to use a suitable underlying {@link Map} to avoid this class being a  * memory leak.  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Memory based idempotent repository"
argument_list|)
DECL|class|MemoryIdempotentRepository
specifier|public
class|class
name|MemoryIdempotentRepository
extends|extends
name|ServiceSupport
implements|implements
name|IdempotentRepository
block|{
DECL|field|cache
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|cache
decl_stmt|;
DECL|field|cacheSize
specifier|private
name|int
name|cacheSize
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|MemoryIdempotentRepository ()
specifier|public
name|MemoryIdempotentRepository
parameter_list|()
block|{
name|this
operator|.
name|cache
operator|=
name|LRUCacheFactory
operator|.
name|newLRUCache
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
DECL|method|MemoryIdempotentRepository (Map<String, Object> set)
specifier|public
name|MemoryIdempotentRepository
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|set
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|set
expr_stmt|;
block|}
comment|/**      * Creates a new memory based repository using a {@link LRUCache}      * with a default of 1000 entries in the cache.      */
DECL|method|memoryIdempotentRepository ()
specifier|public
specifier|static
name|IdempotentRepository
name|memoryIdempotentRepository
parameter_list|()
block|{
return|return
operator|new
name|MemoryIdempotentRepository
argument_list|()
return|;
block|}
comment|/**      * Creates a new memory based repository using a {@link LRUCache}.      *      * @param cacheSize  the cache size      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|memoryIdempotentRepository (int cacheSize)
specifier|public
specifier|static
name|IdempotentRepository
name|memoryIdempotentRepository
parameter_list|(
name|int
name|cacheSize
parameter_list|)
block|{
return|return
name|memoryIdempotentRepository
argument_list|(
name|LRUCacheFactory
operator|.
name|newLRUCache
argument_list|(
name|cacheSize
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates a new memory based repository using the given {@link Map} to      * use to store the processed message ids.      *<p/>      * Care should be taken to use a suitable underlying {@link Map} to avoid this class being a      * memory leak.      *      * @param cache  the cache      */
DECL|method|memoryIdempotentRepository (Map<String, Object> cache)
specifier|public
specifier|static
name|IdempotentRepository
name|memoryIdempotentRepository
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|cache
parameter_list|)
block|{
return|return
operator|new
name|MemoryIdempotentRepository
argument_list|(
name|cache
argument_list|)
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Adds the key to the store"
argument_list|)
DECL|method|add (String key)
specifier|public
name|boolean
name|add
parameter_list|(
name|String
name|key
parameter_list|)
block|{
synchronized|synchronized
init|(
name|cache
init|)
block|{
if|if
condition|(
name|cache
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
else|else
block|{
name|cache
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|key
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Does the store contain the given key"
argument_list|)
DECL|method|contains (String key)
specifier|public
name|boolean
name|contains
parameter_list|(
name|String
name|key
parameter_list|)
block|{
synchronized|synchronized
init|(
name|cache
init|)
block|{
return|return
name|cache
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Remove the key from the store"
argument_list|)
DECL|method|remove (String key)
specifier|public
name|boolean
name|remove
parameter_list|(
name|String
name|key
parameter_list|)
block|{
synchronized|synchronized
init|(
name|cache
init|)
block|{
return|return
name|cache
operator|.
name|remove
argument_list|(
name|key
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
DECL|method|confirm (String key)
specifier|public
name|boolean
name|confirm
parameter_list|(
name|String
name|key
parameter_list|)
block|{
comment|// noop
return|return
literal|true
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Clear the store"
argument_list|)
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
synchronized|synchronized
init|(
name|cache
init|)
block|{
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getCache ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getCache
parameter_list|()
block|{
return|return
name|cache
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The current cache size"
argument_list|)
DECL|method|getCacheSize ()
specifier|public
name|int
name|getCacheSize
parameter_list|()
block|{
return|return
name|cache
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|setCacheSize (int cacheSize)
specifier|public
name|void
name|setCacheSize
parameter_list|(
name|int
name|cacheSize
parameter_list|)
block|{
name|this
operator|.
name|cacheSize
operator|=
name|cacheSize
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|cacheSize
operator|>
literal|0
condition|)
block|{
name|cache
operator|=
name|LRUCacheFactory
operator|.
name|newLRUCache
argument_list|(
name|cacheSize
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

