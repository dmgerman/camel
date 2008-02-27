begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Converter
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
name|Node
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
name|Iterator
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

begin_comment
comment|/**  * Some core java.lang based<a  * href="http://activemq.apache.org/camel/type-converter.html">Type Converters</a>  *   * @version $Revision$  */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|ObjectConverter
specifier|public
class|class
name|ObjectConverter
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|ObjectConverter ()
specifier|private
name|ObjectConverter
parameter_list|()
block|{             }
DECL|method|isCollection (Object value)
specifier|public
specifier|static
name|boolean
name|isCollection
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
comment|// TODO we should handle primitive array types?
return|return
name|value
operator|instanceof
name|Collection
operator|||
operator|(
name|value
operator|!=
literal|null
operator|&&
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
operator|)
return|;
block|}
comment|/**      * Creates an iterator over the value if the value is a collection, an      * Object[] or a primitive type array; otherwise to simplify the caller's      * code, we just create a singleton collection iterator over a single value      */
annotation|@
name|Converter
DECL|method|iterator (Object value)
specifier|public
specifier|static
name|Iterator
name|iterator
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|EMPTY_LIST
operator|.
name|iterator
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Collection
condition|)
block|{
name|Collection
name|collection
init|=
operator|(
name|Collection
operator|)
name|value
decl_stmt|;
return|return
name|collection
operator|.
name|iterator
argument_list|()
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
comment|// TODO we should handle primitive array types?
name|List
argument_list|<
name|Object
argument_list|>
name|list
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|(
name|Object
index|[]
operator|)
name|value
argument_list|)
decl_stmt|;
return|return
name|list
operator|.
name|iterator
argument_list|()
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
comment|// lets iterate through DOM results after performing XPaths
specifier|final
name|NodeList
name|nodeList
init|=
operator|(
name|NodeList
operator|)
name|value
decl_stmt|;
return|return
operator|new
name|Iterator
argument_list|<
name|Node
argument_list|>
argument_list|()
block|{
name|int
name|idx
init|=
operator|-
literal|1
decl_stmt|;
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
operator|++
name|idx
operator|<
name|nodeList
operator|.
name|getLength
argument_list|()
return|;
block|}
specifier|public
name|Node
name|next
parameter_list|()
block|{
return|return
name|nodeList
operator|.
name|item
argument_list|(
name|idx
argument_list|)
return|;
block|}
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
return|;
block|}
else|else
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|value
argument_list|)
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
comment|/**      * Converts the given value to a boolean, handling strings or Boolean      * objects; otherwise returning false if the value could not be converted to      * a boolean      */
annotation|@
name|Converter
DECL|method|toBool (Object value)
specifier|public
specifier|static
name|boolean
name|toBool
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|Boolean
name|answer
init|=
name|toBoolean
argument_list|(
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
return|return
name|answer
operator|.
name|booleanValue
argument_list|()
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Converts the given value to a Boolean, handling strings or Boolean      * objects; otherwise returning null if the value cannot be converted to a      * boolean      */
annotation|@
name|Converter
DECL|method|toBoolean (Object value)
specifier|public
specifier|static
name|Boolean
name|toBoolean
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|Boolean
condition|)
block|{
return|return
operator|(
name|Boolean
operator|)
name|value
return|;
block|}
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
return|return
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
condition|?
name|Boolean
operator|.
name|TRUE
else|:
name|Boolean
operator|.
name|FALSE
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns the boolean value, or null if the value is null      */
annotation|@
name|Converter
DECL|method|toBoolean (Boolean value)
specifier|public
specifier|static
name|Boolean
name|toBoolean
parameter_list|(
name|Boolean
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
return|return
name|value
operator|.
name|booleanValue
argument_list|()
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Returns the converted value, or null if the value is null      */
annotation|@
name|Converter
DECL|method|toByte (Object value)
specifier|public
specifier|static
name|Byte
name|toByte
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|Byte
condition|)
block|{
return|return
operator|(
name|Byte
operator|)
name|value
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Number
condition|)
block|{
name|Number
name|number
init|=
operator|(
name|Number
operator|)
name|value
decl_stmt|;
return|return
name|number
operator|.
name|byteValue
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
return|return
name|Byte
operator|.
name|parseByte
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Converter
DECL|method|toByteArray (String value)
specifier|public
specifier|static
name|byte
index|[]
name|toByteArray
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|value
operator|.
name|getBytes
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toCharArray (String value)
specifier|public
specifier|static
name|char
index|[]
name|toCharArray
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|value
operator|.
name|toCharArray
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|fromCharArray (char[] value)
specifier|public
specifier|static
name|String
name|fromCharArray
parameter_list|(
name|char
index|[]
name|value
parameter_list|)
block|{
return|return
operator|new
name|String
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**      * Returns the converted value, or null if the value is null      */
annotation|@
name|Converter
DECL|method|toShort (Object value)
specifier|public
specifier|static
name|Short
name|toShort
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|Short
condition|)
block|{
return|return
operator|(
name|Short
operator|)
name|value
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Number
condition|)
block|{
name|Number
name|number
init|=
operator|(
name|Number
operator|)
name|value
decl_stmt|;
return|return
name|number
operator|.
name|shortValue
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
return|return
name|Short
operator|.
name|parseShort
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Returns the converted value, or null if the value is null      */
annotation|@
name|Converter
DECL|method|toInteger (Object value)
specifier|public
specifier|static
name|Integer
name|toInteger
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|Integer
condition|)
block|{
return|return
operator|(
name|Integer
operator|)
name|value
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Number
condition|)
block|{
name|Number
name|number
init|=
operator|(
name|Number
operator|)
name|value
decl_stmt|;
return|return
name|number
operator|.
name|intValue
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
return|return
name|Integer
operator|.
name|parseInt
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Returns the converted value, or null if the value is null      */
annotation|@
name|Converter
DECL|method|toLong (Object value)
specifier|public
specifier|static
name|Long
name|toLong
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|Long
condition|)
block|{
return|return
operator|(
name|Long
operator|)
name|value
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Number
condition|)
block|{
name|Number
name|number
init|=
operator|(
name|Number
operator|)
name|value
decl_stmt|;
return|return
name|number
operator|.
name|longValue
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
return|return
name|Long
operator|.
name|parseLong
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Returns the converted value, or null if the value is null      */
annotation|@
name|Converter
DECL|method|toFloat (Object value)
specifier|public
specifier|static
name|Float
name|toFloat
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|Float
condition|)
block|{
return|return
operator|(
name|Float
operator|)
name|value
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Number
condition|)
block|{
name|Number
name|number
init|=
operator|(
name|Number
operator|)
name|value
decl_stmt|;
return|return
name|number
operator|.
name|floatValue
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
return|return
name|Float
operator|.
name|parseFloat
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Returns the converted value, or null if the value is null      */
annotation|@
name|Converter
DECL|method|toDouble (Object value)
specifier|public
specifier|static
name|Double
name|toDouble
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|Double
condition|)
block|{
return|return
operator|(
name|Double
operator|)
name|value
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Number
condition|)
block|{
name|Number
name|number
init|=
operator|(
name|Number
operator|)
name|value
decl_stmt|;
return|return
name|number
operator|.
name|doubleValue
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
return|return
name|Double
operator|.
name|parseDouble
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

