begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|Vector
import|;
end_import

begin_comment
comment|/**  * This class is an ordered {@link Properties} where the key/values are stored in the order they are added or loaded.  *<p/>  * Note: This implementation is only intended as implementation detail for the Camel properties component, and has only  * been designed to provide the needed functionality.  * The complex logic for loading properties has been kept from the JDK {@link Properties} class.  */
end_comment

begin_class
DECL|class|OrderedProperties
specifier|public
specifier|final
class|class
name|OrderedProperties
extends|extends
name|Properties
block|{
DECL|field|map
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|OrderedProperties ()
specifier|public
name|OrderedProperties
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|put (Object key, Object value)
specifier|public
specifier|synchronized
name|Object
name|put
parameter_list|(
name|Object
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|map
operator|.
name|put
argument_list|(
name|key
operator|.
name|toString
argument_list|()
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|putAll (Map<?, ?> t)
specifier|public
specifier|synchronized
name|void
name|putAll
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|t
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|entry
range|:
name|t
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
DECL|method|get (Object key)
specifier|public
specifier|synchronized
name|Object
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isEmpty ()
specifier|public
specifier|synchronized
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|map
operator|.
name|isEmpty
argument_list|()
return|;
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
return|return
name|map
operator|.
name|remove
argument_list|(
name|key
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
name|map
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getProperty (String key)
specifier|public
name|String
name|getProperty
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getProperty (String key, String defaultValue)
specifier|public
name|String
name|getProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
return|return
name|map
operator|.
name|getOrDefault
argument_list|(
name|key
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|keys ()
specifier|public
specifier|synchronized
name|Enumeration
argument_list|<
name|Object
argument_list|>
name|keys
parameter_list|()
block|{
return|return
operator|new
name|Vector
argument_list|<
name|Object
argument_list|>
argument_list|(
name|map
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|elements
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|keySet ()
specifier|public
name|Set
argument_list|<
name|Object
argument_list|>
name|keySet
parameter_list|()
block|{
return|return
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|(
name|map
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|entrySet ()
specifier|public
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
name|Set
name|entrySet
init|=
name|map
operator|.
name|entrySet
argument_list|()
decl_stmt|;
return|return
name|entrySet
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
specifier|synchronized
name|int
name|size
parameter_list|()
block|{
return|return
name|map
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|stringPropertyNames ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|stringPropertyNames
parameter_list|()
block|{
return|return
name|map
operator|.
name|keySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|values ()
specifier|public
name|Collection
argument_list|<
name|Object
argument_list|>
name|values
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|map
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
specifier|synchronized
name|String
name|toString
parameter_list|()
block|{
return|return
name|map
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

