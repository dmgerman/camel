begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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

begin_comment
comment|/**  * A map that uses case insensitive keys, but preserves the original keys in the keySet.  *<p/>  * This map allows you to do lookup using case insensitive keys so you can retrieve the value without worrying about  * whether some transport protocol affects the keys such as Http and Mail protocols can do.  *<p/>  * When copying from this map to a regular Map such as {@link java.util.HashMap} then the original keys are  * copied over and you get the old behavior back using a regular Map with case sensitive keys.  *<p/>  * This map is<b>not</b> designed to be thread safe as concurrent access to it is not supposed to be performed  * by the Camel routing engine.  *  * @version   */
end_comment

begin_class
DECL|class|CaseInsensitiveMap
specifier|public
class|class
name|CaseInsensitiveMap
extends|extends
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|8538318195477618308L
decl_stmt|;
comment|// holds a map of lower case key -> original key
DECL|field|originalKeys
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|originalKeys
decl_stmt|;
comment|// holds a snapshot view of current entry set
DECL|field|entrySetView
specifier|private
specifier|transient
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|entrySetView
decl_stmt|;
DECL|method|CaseInsensitiveMap ()
specifier|public
name|CaseInsensitiveMap
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|originalKeys
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
DECL|method|CaseInsensitiveMap (Map<? extends String, ?> map)
specifier|public
name|CaseInsensitiveMap
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|String
argument_list|,
name|?
argument_list|>
name|map
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
DECL|method|CaseInsensitiveMap (int initialCapacity, float loadFactor)
specifier|public
name|CaseInsensitiveMap
parameter_list|(
name|int
name|initialCapacity
parameter_list|,
name|float
name|loadFactor
parameter_list|)
block|{
name|super
argument_list|(
name|initialCapacity
argument_list|,
name|loadFactor
argument_list|)
expr_stmt|;
name|originalKeys
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|initialCapacity
argument_list|,
name|loadFactor
argument_list|)
expr_stmt|;
block|}
DECL|method|CaseInsensitiveMap (int initialCapacity)
specifier|public
name|CaseInsensitiveMap
parameter_list|(
name|int
name|initialCapacity
parameter_list|)
block|{
name|super
argument_list|(
name|initialCapacity
argument_list|)
expr_stmt|;
name|originalKeys
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|initialCapacity
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get (Object key)
specifier|public
name|Object
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|String
name|s
init|=
name|assembleKey
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|Object
name|answer
init|=
name|super
operator|.
name|get
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// fallback to lookup by original key
name|String
name|originalKey
init|=
name|originalKeys
operator|.
name|get
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|answer
operator|=
name|super
operator|.
name|get
argument_list|(
name|originalKey
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|put (String key, Object value)
specifier|public
specifier|synchronized
name|Object
name|put
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
comment|// invalidate views as we mutate
name|entrySetView
operator|=
literal|null
expr_stmt|;
name|String
name|s
init|=
name|assembleKey
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|startsWith
argument_list|(
literal|"Camel"
argument_list|)
condition|)
block|{
comment|// use intern String for headers which is Camel* headers
comment|// this reduces memory allocations needed for those common headers
name|originalKeys
operator|.
name|put
argument_list|(
name|s
argument_list|,
name|key
operator|.
name|intern
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|originalKeys
operator|.
name|put
argument_list|(
name|s
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
return|return
name|super
operator|.
name|put
argument_list|(
name|s
argument_list|,
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|putAll (Map<? extends String, ?> map)
specifier|public
specifier|synchronized
name|void
name|putAll
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|String
argument_list|,
name|?
argument_list|>
name|map
parameter_list|)
block|{
name|entrySetView
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
operator|&&
operator|!
name|map
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
extends|extends
name|String
argument_list|,
name|?
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|String
name|s
init|=
name|assembleKey
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|startsWith
argument_list|(
literal|"Camel"
argument_list|)
condition|)
block|{
comment|// use intern String for headers which is Camel* headers
comment|// this reduces memory allocations needed for those common headers
name|originalKeys
operator|.
name|put
argument_list|(
name|s
argument_list|,
name|key
operator|.
name|intern
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|originalKeys
operator|.
name|put
argument_list|(
name|s
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|put
argument_list|(
name|s
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|remove (Object key)
specifier|public
specifier|synchronized
name|Object
name|remove
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// invalidate views as we mutate
name|entrySetView
operator|=
literal|null
expr_stmt|;
name|String
name|s
init|=
name|assembleKey
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|originalKeys
operator|.
name|remove
argument_list|(
name|s
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|remove
argument_list|(
name|s
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
specifier|synchronized
name|void
name|clear
parameter_list|()
block|{
comment|// invalidate views as we mutate
name|entrySetView
operator|=
literal|null
expr_stmt|;
name|originalKeys
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
DECL|method|containsKey (Object key)
specifier|public
name|boolean
name|containsKey
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|String
name|s
init|=
name|assembleKey
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
name|super
operator|.
name|containsKey
argument_list|(
name|s
argument_list|)
return|;
block|}
DECL|method|assembleKey (Object key)
specifier|private
specifier|static
name|String
name|assembleKey
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|String
name|s
init|=
name|key
operator|.
name|toString
argument_list|()
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|startsWith
argument_list|(
literal|"camel"
argument_list|)
condition|)
block|{
comment|// use intern String for headers which is Camel* headers
comment|// this reduces memory allocations needed for those common headers
name|s
operator|=
name|s
operator|.
name|intern
argument_list|()
expr_stmt|;
block|}
return|return
name|s
return|;
block|}
annotation|@
name|Override
DECL|method|entrySet ()
specifier|public
specifier|synchronized
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
if|if
condition|(
name|entrySetView
operator|==
literal|null
condition|)
block|{
comment|// build the key set using the original keys so we retain their case
comment|// when for example we copy values to another map
name|entrySetView
operator|=
operator|new
name|HashSet
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|(
name|this
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|super
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|view
init|=
operator|new
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|String
name|getKey
parameter_list|()
block|{
name|String
name|s
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
comment|// use the original key so we can preserve it
return|return
name|originalKeys
operator|.
name|get
argument_list|(
name|s
argument_list|)
return|;
block|}
specifier|public
name|Object
name|getValue
parameter_list|()
block|{
return|return
name|entry
operator|.
name|getValue
argument_list|()
return|;
block|}
specifier|public
name|Object
name|setValue
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|entry
operator|.
name|setValue
argument_list|(
name|o
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|entrySetView
operator|.
name|add
argument_list|(
name|view
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|entrySetView
return|;
block|}
block|}
end_class

end_unit

