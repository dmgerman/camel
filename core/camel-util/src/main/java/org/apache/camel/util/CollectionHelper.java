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
name|lang
operator|.
name|reflect
operator|.
name|Array
import|;
end_import

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
name|Arrays
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
name|Collections
import|;
end_import

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
name|Iterator
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
name|function
operator|.
name|Supplier
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
import|;
end_import

begin_comment
comment|/**  * A number of helper methods for working with collections  */
end_comment

begin_class
DECL|class|CollectionHelper
specifier|public
specifier|final
class|class
name|CollectionHelper
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|CollectionHelper ()
specifier|private
name|CollectionHelper
parameter_list|()
block|{     }
comment|/**      * Returns the size of the collection if it can be determined to be a collection      *      * @param value the collection      * @return the size, or<tt>null</tt> if not a collection      */
DECL|method|size (Object value)
specifier|public
specifier|static
name|Integer
name|size
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|Collection
condition|)
block|{
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
init|=
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|value
decl_stmt|;
return|return
name|collection
operator|.
name|size
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Map
condition|)
block|{
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
init|=
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|value
decl_stmt|;
return|return
name|map
operator|.
name|size
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Object
index|[]
condition|)
block|{
name|Object
index|[]
name|array
init|=
operator|(
name|Object
index|[]
operator|)
name|value
decl_stmt|;
return|return
name|array
operator|.
name|length
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
block|{
return|return
name|Array
operator|.
name|getLength
argument_list|(
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|NodeList
condition|)
block|{
name|NodeList
name|nodeList
init|=
operator|(
name|NodeList
operator|)
name|value
decl_stmt|;
return|return
name|nodeList
operator|.
name|getLength
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Sets the value of the entry in the map for the given key, though if the      * map already contains a value for the given key then the value is appended      * to a list of values.      *      * @param map the map to add the entry to      * @param key the key in the map      * @param value the value to put in the map      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|appendValue (Map<String, Object> map, String key, Object value)
specifier|public
specifier|static
name|void
name|appendValue
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|,
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|Object
name|oldValue
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldValue
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|list
decl_stmt|;
if|if
condition|(
name|oldValue
operator|instanceof
name|List
condition|)
block|{
name|list
operator|=
operator|(
name|List
argument_list|<
name|Object
argument_list|>
operator|)
name|oldValue
expr_stmt|;
block|}
else|else
block|{
name|list
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|oldValue
argument_list|)
expr_stmt|;
comment|// replace old entry with list
name|map
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
name|list
operator|.
name|add
argument_list|(
name|value
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
name|value
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createSetContaining (T... contents)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Set
argument_list|<
name|T
argument_list|>
name|createSetContaining
parameter_list|(
name|T
modifier|...
name|contents
parameter_list|)
block|{
return|return
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|contents
argument_list|)
argument_list|)
return|;
block|}
DECL|method|collectionAsCommaDelimitedString (Collection<?> col)
specifier|public
specifier|static
name|String
name|collectionAsCommaDelimitedString
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|col
parameter_list|)
block|{
if|if
condition|(
name|col
operator|==
literal|null
operator|||
name|col
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|""
return|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|col
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|it
operator|.
name|next
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Traverses the given map recursively and flattern the keys by combining them with the optional separator.      *      * @param map  the map      * @param separator optional separator to use in key name, for example a hyphen or dot.      * @return the map with flattern keys      */
DECL|method|flattenKeysInMap (Map<String, Object> map, String separator)
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|flattenKeysInMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|,
name|String
name|separator
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|answer
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|doFlattenKeysInMap
argument_list|(
name|map
argument_list|,
literal|""
argument_list|,
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|separator
argument_list|)
condition|?
name|separator
else|:
literal|""
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|doFlattenKeysInMap (Map<String, Object> source, String prefix, String separator, Map<String, Object> target)
specifier|private
specifier|static
name|void
name|doFlattenKeysInMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|source
parameter_list|,
name|String
name|prefix
parameter_list|,
name|String
name|separator
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|target
parameter_list|)
block|{
for|for
control|(
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
name|source
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
name|newKey
init|=
name|prefix
operator|.
name|isEmpty
argument_list|()
condition|?
name|key
else|:
name|prefix
operator|+
name|separator
operator|+
name|key
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|Map
condition|)
block|{
name|Map
name|map
init|=
operator|(
name|Map
operator|)
name|value
decl_stmt|;
name|doFlattenKeysInMap
argument_list|(
name|map
argument_list|,
name|newKey
argument_list|,
name|separator
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|target
operator|.
name|put
argument_list|(
name|newKey
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Build an unmodifiable map on top of a given map. Note tha thew given map is      * copied if not null.      *      * @param map a map      * @return an unmodifiable map.      */
DECL|method|unmodifiableMap (Map<K, V> map)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|unmodifiableMap
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
block|{
return|return
name|map
operator|==
literal|null
condition|?
name|Collections
operator|.
name|emptyMap
argument_list|()
else|:
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|map
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Build a map from varargs.      */
DECL|method|mapOf (Supplier<Map<K, V>> creator, K key, V value, Object... keyVals)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|mapOf
parameter_list|(
name|Supplier
argument_list|<
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|creator
parameter_list|,
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|,
name|Object
modifier|...
name|keyVals
parameter_list|)
block|{
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
init|=
name|creator
operator|.
name|get
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|keyVals
operator|.
name|length
condition|;
name|i
operator|+=
literal|2
control|)
block|{
name|map
operator|.
name|put
argument_list|(
operator|(
name|K
operator|)
name|keyVals
index|[
name|i
index|]
argument_list|,
operator|(
name|V
operator|)
name|keyVals
index|[
name|i
operator|+
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
comment|/**      * Build an immutable map from varargs.      */
DECL|method|immutableMapOf (Supplier<Map<K, V>> creator, K key, V value, Object... keyVals)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|immutableMapOf
parameter_list|(
name|Supplier
argument_list|<
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|creator
parameter_list|,
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|,
name|Object
modifier|...
name|keyVals
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|mapOf
argument_list|(
name|creator
argument_list|,
name|key
argument_list|,
name|value
argument_list|,
name|keyVals
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Build a map from varargs.      */
DECL|method|mapOf (K key, V value, Object... keyVals)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|mapOf
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|,
name|Object
modifier|...
name|keyVals
parameter_list|)
block|{
return|return
name|mapOf
argument_list|(
name|HashMap
operator|::
operator|new
argument_list|,
name|key
argument_list|,
name|value
argument_list|,
name|keyVals
argument_list|)
return|;
block|}
comment|/**      * Build an immutable map from varargs.      */
DECL|method|immutableMapOf (K key, V value, Object... keyVals)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|immutableMapOf
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|,
name|Object
modifier|...
name|keyVals
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|mapOf
argument_list|(
name|HashMap
operator|::
operator|new
argument_list|,
name|key
argument_list|,
name|value
argument_list|,
name|keyVals
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

