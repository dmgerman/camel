begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.engine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|engine
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|AbstractMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|AbstractSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
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
name|StaticService
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
name|ServiceHelper
import|;
end_import

begin_comment
comment|/**  * Base implementation for {@link org.apache.camel.spi.TransformerRegistry}, {@link org.apache.camel.spi.ValidatorRegistry}  * and {@link org.apache.camel.spi.EndpointRegistry}.  */
end_comment

begin_class
DECL|class|AbstractDynamicRegistry
specifier|public
class|class
name|AbstractDynamicRegistry
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|implements
name|StaticService
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|context
specifier|protected
specifier|final
name|CamelContext
name|context
decl_stmt|;
DECL|field|maxCacheSize
specifier|protected
specifier|final
name|int
name|maxCacheSize
decl_stmt|;
DECL|field|dynamicMap
specifier|protected
specifier|final
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|dynamicMap
decl_stmt|;
DECL|field|staticMap
specifier|protected
specifier|final
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|staticMap
decl_stmt|;
DECL|method|AbstractDynamicRegistry (CamelContext context, int maxCacheSize)
specifier|public
name|AbstractDynamicRegistry
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|int
name|maxCacheSize
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|maxCacheSize
operator|=
name|maxCacheSize
expr_stmt|;
comment|// do not stop on eviction, as the transformer may still be in use
name|this
operator|.
name|dynamicMap
operator|=
name|LRUCacheFactory
operator|.
name|newLRUCache
argument_list|(
name|this
operator|.
name|maxCacheSize
argument_list|,
name|this
operator|.
name|maxCacheSize
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// static map to hold transformers we do not want to be evicted
name|this
operator|.
name|staticMap
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
if|if
condition|(
name|dynamicMap
operator|instanceof
name|LRUCache
condition|)
block|{
operator|(
operator|(
name|LRUCache
operator|)
name|dynamicMap
operator|)
operator|.
name|resetStatistics
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|get (Object o)
specifier|public
name|V
name|get
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
comment|// try static map first
name|V
name|answer
init|=
name|staticMap
operator|.
name|get
argument_list|(
name|o
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|dynamicMap
operator|.
name|get
argument_list|(
name|o
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
operator|&&
operator|(
name|context
operator|.
name|isSetupRoutes
argument_list|()
operator|||
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|isStartingRoutes
argument_list|()
operator|)
condition|)
block|{
name|dynamicMap
operator|.
name|remove
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|staticMap
operator|.
name|put
argument_list|(
operator|(
name|K
operator|)
name|o
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|put (K key, V transformer)
specifier|public
name|V
name|put
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|transformer
parameter_list|)
block|{
comment|// at first we must see if the key already exists and then replace it back, so it stays the same spot
name|V
name|answer
init|=
name|staticMap
operator|.
name|remove
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
comment|// replace existing
name|staticMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|transformer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
name|answer
operator|=
name|dynamicMap
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
comment|// replace existing
name|dynamicMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|transformer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|// we want transformers to be static if they are part of setting up or starting routes
if|if
condition|(
name|context
operator|.
name|isSetupRoutes
argument_list|()
operator|||
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|isStartingRoutes
argument_list|()
condition|)
block|{
name|answer
operator|=
name|staticMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|transformer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|dynamicMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|transformer
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|containsKey (Object o)
specifier|public
name|boolean
name|containsKey
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|staticMap
operator|.
name|containsKey
argument_list|(
name|o
argument_list|)
operator|||
name|dynamicMap
operator|.
name|containsKey
argument_list|(
name|o
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|containsValue (Object o)
specifier|public
name|boolean
name|containsValue
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|staticMap
operator|.
name|containsValue
argument_list|(
name|o
argument_list|)
operator|||
name|dynamicMap
operator|.
name|containsValue
argument_list|(
name|o
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|staticMap
operator|.
name|size
argument_list|()
operator|+
name|dynamicMap
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|staticSize ()
specifier|public
name|int
name|staticSize
parameter_list|()
block|{
return|return
name|staticMap
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|dynamicSize ()
specifier|public
name|int
name|dynamicSize
parameter_list|()
block|{
return|return
name|dynamicMap
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|staticMap
operator|.
name|isEmpty
argument_list|()
operator|&&
name|dynamicMap
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|remove (Object o)
specifier|public
name|V
name|remove
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|V
name|answer
init|=
name|staticMap
operator|.
name|remove
argument_list|(
name|o
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|dynamicMap
operator|.
name|remove
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|staticMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|dynamicMap
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|entrySet ()
specifier|public
name|Set
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
return|return
operator|new
name|AbstractSet
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|CompoundIterator
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|staticMap
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|,
name|dynamicMap
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|staticMap
operator|.
name|size
argument_list|()
operator|+
name|dynamicMap
operator|.
name|size
argument_list|()
return|;
block|}
block|}
return|;
block|}
DECL|method|getMaximumCacheSize ()
specifier|public
name|int
name|getMaximumCacheSize
parameter_list|()
block|{
return|return
name|maxCacheSize
return|;
block|}
comment|/**      * Purges the cache      */
DECL|method|purge ()
specifier|public
name|void
name|purge
parameter_list|()
block|{
comment|// only purge the dynamic part
name|dynamicMap
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|cleanUp ()
specifier|public
name|void
name|cleanUp
parameter_list|()
block|{
if|if
condition|(
name|dynamicMap
operator|instanceof
name|LRUCache
condition|)
block|{
operator|(
operator|(
name|LRUCache
operator|)
name|dynamicMap
operator|)
operator|.
name|cleanUp
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|isStatic (K key)
specifier|public
name|boolean
name|isStatic
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|staticMap
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
DECL|method|isDynamic (K key)
specifier|public
name|boolean
name|isDynamic
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|dynamicMap
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|staticMap
operator|.
name|values
argument_list|()
argument_list|,
name|dynamicMap
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|purge
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Registry for "
operator|+
name|context
operator|.
name|getName
argument_list|()
operator|+
literal|", capacity: "
operator|+
name|maxCacheSize
return|;
block|}
block|}
end_class

end_unit

