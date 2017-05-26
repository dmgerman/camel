begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|impl
operator|.
name|validator
operator|.
name|ValidatorKey
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
name|model
operator|.
name|validator
operator|.
name|ValidatorDefinition
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
name|DataType
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
name|Validator
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
name|ValidatorRegistry
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
name|CamelContextHelper
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
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ServiceHelper
import|;
end_import

begin_comment
comment|/**  * Default implementation of {@link org.apache.camel.spi.ValidatorRegistry}.  */
end_comment

begin_class
DECL|class|DefaultValidatorRegistry
specifier|public
class|class
name|DefaultValidatorRegistry
extends|extends
name|LRUCache
argument_list|<
name|ValidatorKey
argument_list|,
name|Validator
argument_list|>
implements|implements
name|ValidatorRegistry
argument_list|<
name|ValidatorKey
argument_list|>
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
DECL|field|staticMap
specifier|private
name|ConcurrentMap
argument_list|<
name|ValidatorKey
argument_list|,
name|Validator
argument_list|>
name|staticMap
decl_stmt|;
DECL|field|context
specifier|private
specifier|final
name|CamelContext
name|context
decl_stmt|;
DECL|method|DefaultValidatorRegistry (CamelContext context)
specifier|public
name|DefaultValidatorRegistry
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|this
argument_list|(
name|context
argument_list|,
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultValidatorRegistry (CamelContext context, List<ValidatorDefinition> definitions)
specifier|public
name|DefaultValidatorRegistry
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|List
argument_list|<
name|ValidatorDefinition
argument_list|>
name|definitions
parameter_list|)
throws|throws
name|Exception
block|{
comment|// do not stop on eviction, as the validator may still be in use
name|super
argument_list|(
name|CamelContextHelper
operator|.
name|getMaximumValidatorCacheSize
argument_list|(
name|context
argument_list|)
argument_list|,
name|CamelContextHelper
operator|.
name|getMaximumValidatorCacheSize
argument_list|(
name|context
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// static map to hold validator we do not want to be evicted
name|this
operator|.
name|staticMap
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
for|for
control|(
name|ValidatorDefinition
name|def
range|:
name|definitions
control|)
block|{
name|Validator
name|validator
init|=
name|def
operator|.
name|createValidator
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|context
operator|.
name|addService
argument_list|(
name|validator
argument_list|)
expr_stmt|;
name|put
argument_list|(
operator|new
name|ValidatorKey
argument_list|(
operator|new
name|DataType
argument_list|(
name|def
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|validator
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|resolveValidator (ValidatorKey key)
specifier|public
name|Validator
name|resolveValidator
parameter_list|(
name|ValidatorKey
name|key
parameter_list|)
block|{
name|Validator
name|answer
init|=
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|key
operator|.
name|getType
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|answer
operator|=
name|get
argument_list|(
operator|new
name|ValidatorKey
argument_list|(
operator|new
name|DataType
argument_list|(
name|key
operator|.
name|getType
argument_list|()
operator|.
name|getModel
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|resetStatistics
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get (Object o)
specifier|public
name|Validator
name|get
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
comment|// try static map first
name|Validator
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
name|super
operator|.
name|get
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|hits
operator|.
name|increment
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|put (ValidatorKey key, Validator validator)
specifier|public
name|Validator
name|put
parameter_list|(
name|ValidatorKey
name|key
parameter_list|,
name|Validator
name|validator
parameter_list|)
block|{
comment|// at first we must see if the key already exists and then replace it back, so it stays the same spot
name|Validator
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
name|validator
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
name|answer
operator|=
name|super
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
name|super
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|validator
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|// we want validators to be static if they are part of setting up or starting routes
if|if
condition|(
name|context
operator|.
name|isSetupRoutes
argument_list|()
operator|||
name|context
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
name|validator
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|super
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|validator
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|putAll (Map<? extends ValidatorKey, ? extends Validator> map)
specifier|public
name|void
name|putAll
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|ValidatorKey
argument_list|,
name|?
extends|extends
name|Validator
argument_list|>
name|map
parameter_list|)
block|{
comment|// need to use put instead of putAll to ensure the entries gets added to either static or dynamic map
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
extends|extends
name|ValidatorKey
argument_list|,
name|?
extends|extends
name|Validator
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|super
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
name|super
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
name|super
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
annotation|@
name|Override
DECL|method|dynamicSize ()
specifier|public
name|int
name|dynamicSize
parameter_list|()
block|{
return|return
name|super
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
name|super
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|remove (Object o)
specifier|public
name|Validator
name|remove
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|Validator
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
name|super
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
name|super
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|keySet ()
specifier|public
name|Set
argument_list|<
name|ValidatorKey
argument_list|>
name|keySet
parameter_list|()
block|{
name|Set
argument_list|<
name|ValidatorKey
argument_list|>
name|answer
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|answer
operator|.
name|addAll
argument_list|(
name|staticMap
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|addAll
argument_list|(
name|super
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|values ()
specifier|public
name|Collection
argument_list|<
name|Validator
argument_list|>
name|values
parameter_list|()
block|{
name|Collection
argument_list|<
name|Validator
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|answer
operator|.
name|addAll
argument_list|(
name|staticMap
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|addAll
argument_list|(
name|super
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|entrySet ()
specifier|public
name|Set
argument_list|<
name|Entry
argument_list|<
name|ValidatorKey
argument_list|,
name|Validator
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
name|Set
argument_list|<
name|Entry
argument_list|<
name|ValidatorKey
argument_list|,
name|Validator
argument_list|>
argument_list|>
name|answer
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|answer
operator|.
name|addAll
argument_list|(
name|staticMap
operator|.
name|entrySet
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|addAll
argument_list|(
name|super
operator|.
name|entrySet
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|getMaximumCacheSize ()
specifier|public
name|int
name|getMaximumCacheSize
parameter_list|()
block|{
return|return
name|super
operator|.
name|getMaxCacheSize
argument_list|()
return|;
block|}
comment|/**      * Purges the cache      */
annotation|@
name|Override
DECL|method|purge ()
specifier|public
name|void
name|purge
parameter_list|()
block|{
comment|// only purge the dynamic part
name|super
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isStatic (DataType type)
specifier|public
name|boolean
name|isStatic
parameter_list|(
name|DataType
name|type
parameter_list|)
block|{
return|return
name|staticMap
operator|.
name|containsKey
argument_list|(
operator|new
name|ValidatorKey
argument_list|(
name|type
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isDynamic (DataType type)
specifier|public
name|boolean
name|isDynamic
parameter_list|(
name|DataType
name|type
parameter_list|)
block|{
return|return
name|super
operator|.
name|containsKey
argument_list|(
operator|new
name|ValidatorKey
argument_list|(
name|type
argument_list|)
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
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|staticMap
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
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
literal|"ValidatorRegistry for "
operator|+
name|context
operator|.
name|getName
argument_list|()
operator|+
literal|", capacity: "
operator|+
name|getMaxCacheSize
argument_list|()
return|;
block|}
block|}
end_class

end_unit

