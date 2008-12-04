begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.idempotent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|util
operator|.
name|LRUCache
import|;
end_import

begin_comment
comment|/**  * A memory based implementation of {@link org.apache.camel.spi.IdempotentRepository}.   *<p/>  * Care should be taken to use a suitable underlying {@link Map} to avoid this class being a  * memory leak.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|MemoryIdempotentRepository
specifier|public
class|class
name|MemoryIdempotentRepository
implements|implements
name|IdempotentRepository
argument_list|<
name|String
argument_list|>
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
name|memoryIdempotentRepository
argument_list|(
literal|1000
argument_list|)
return|;
block|}
comment|/**      * Creates a new memory based repository using a {@link LRUCache}.      *      * @param cacheSize  the cache size      */
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
operator|new
name|LRUCache
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
DECL|method|add (String messageId)
specifier|public
name|boolean
name|add
parameter_list|(
name|String
name|messageId
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
name|messageId
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
name|messageId
argument_list|,
name|messageId
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
block|}
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
DECL|method|setCache (Map<String, Object> cache)
specifier|public
name|void
name|setCache
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
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
block|}
block|}
end_class

end_unit

