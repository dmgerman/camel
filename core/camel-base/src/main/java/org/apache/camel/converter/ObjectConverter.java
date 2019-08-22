begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|math
operator|.
name|BigInteger
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
name|apache
operator|.
name|camel
operator|.
name|Exchange
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Some core java.lang based<a  * href="http://camel.apache.org/type-converter.html">Type Converters</a>  */
end_comment

begin_class
annotation|@
name|Converter
argument_list|(
name|loader
operator|=
literal|true
argument_list|)
DECL|class|ObjectConverter
specifier|public
specifier|final
class|class
name|ObjectConverter
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|ObjectConverter ()
specifier|private
name|ObjectConverter
parameter_list|()
block|{     }
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
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot convert type: "
operator|+
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" to boolean"
argument_list|)
throw|;
block|}
return|return
name|answer
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
return|return
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|toBoolean
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**      * Creates an iterator over the value      */
annotation|@
name|Converter
DECL|method|iterator (Object value)
specifier|public
specifier|static
name|Iterator
argument_list|<
name|?
argument_list|>
name|iterator
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**      * Creates an iterable over the value      */
annotation|@
name|Converter
DECL|method|iterable (Object value)
specifier|public
specifier|static
name|Iterable
argument_list|<
name|?
argument_list|>
name|iterable
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|ObjectHelper
operator|.
name|createIterable
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**      * Returns the converted value, or null if the value is null      */
annotation|@
name|Converter
argument_list|(
name|allowNull
operator|=
literal|true
argument_list|)
DECL|method|toByte (Number value)
specifier|public
specifier|static
name|Byte
name|toByte
parameter_list|(
name|Number
name|value
parameter_list|)
block|{
if|if
condition|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|isNaN
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|value
operator|.
name|byteValue
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toByte (String value)
specifier|public
specifier|static
name|Byte
name|toByte
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|Byte
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
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
DECL|method|toCharacter (String value)
specifier|public
specifier|static
name|Character
name|toCharacter
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|toChar
argument_list|(
name|value
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toChar (String value)
specifier|public
specifier|static
name|char
name|toChar
parameter_list|(
name|String
name|value
parameter_list|)
block|{
comment|// must be string with the length of 1
if|if
condition|(
name|value
operator|.
name|length
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"String must have exactly a length of 1: "
operator|+
name|value
argument_list|)
throw|;
block|}
return|return
name|value
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
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
DECL|method|toClass (String value, Exchange exchange)
specifier|public
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|toClass
parameter_list|(
name|String
name|value
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// prefer to use class resolver API
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|loadClass
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
return|;
block|}
block|}
comment|/**      * Returns the converted value, or null if the value is null      */
annotation|@
name|Converter
argument_list|(
name|allowNull
operator|=
literal|true
argument_list|)
DECL|method|toShort (Number value)
specifier|public
specifier|static
name|Short
name|toShort
parameter_list|(
name|Number
name|value
parameter_list|)
block|{
if|if
condition|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|isNaN
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|value
operator|.
name|shortValue
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toShort (String value)
specifier|public
specifier|static
name|Short
name|toShort
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|Short
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**      * Returns the converted value, or null if the value is null      */
annotation|@
name|Converter
argument_list|(
name|allowNull
operator|=
literal|true
argument_list|)
DECL|method|toInteger (Number value)
specifier|public
specifier|static
name|Integer
name|toInteger
parameter_list|(
name|Number
name|value
parameter_list|)
block|{
if|if
condition|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|isNaN
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|value
operator|.
name|intValue
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toInteger (String value)
specifier|public
specifier|static
name|Integer
name|toInteger
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|Integer
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**      * Returns the converted value, or null if the value is null      */
annotation|@
name|Converter
argument_list|(
name|allowNull
operator|=
literal|true
argument_list|)
DECL|method|toLong (Number value)
specifier|public
specifier|static
name|Long
name|toLong
parameter_list|(
name|Number
name|value
parameter_list|)
block|{
if|if
condition|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|isNaN
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|value
operator|.
name|longValue
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toLong (String value)
specifier|public
specifier|static
name|Long
name|toLong
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|Long
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**      * Returns the converted value, or null if the value is null      */
annotation|@
name|Converter
argument_list|(
name|allowNull
operator|=
literal|true
argument_list|)
DECL|method|toBigInteger (Object value)
specifier|public
specifier|static
name|BigInteger
name|toBigInteger
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|isNaN
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
literal|null
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
operator|new
name|BigInteger
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
return|;
block|}
name|Long
name|num
init|=
literal|null
decl_stmt|;
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
name|num
operator|=
name|number
operator|.
name|longValue
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|num
operator|!=
literal|null
condition|)
block|{
return|return
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|num
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
DECL|method|toFloat (Number value)
specifier|public
specifier|static
name|Float
name|toFloat
parameter_list|(
name|Number
name|value
parameter_list|)
block|{
if|if
condition|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|isNaN
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|Float
operator|.
name|NaN
return|;
block|}
return|return
name|value
operator|.
name|floatValue
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toFloat (String value)
specifier|public
specifier|static
name|Float
name|toFloat
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|Float
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**      * Returns the converted value, or null if the value is null      */
annotation|@
name|Converter
DECL|method|toDouble (Number value)
specifier|public
specifier|static
name|Double
name|toDouble
parameter_list|(
name|Number
name|value
parameter_list|)
block|{
if|if
condition|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|isNaN
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|Double
operator|.
name|NaN
return|;
block|}
return|return
name|value
operator|.
name|doubleValue
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toDouble (String value)
specifier|public
specifier|static
name|Double
name|toDouble
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|Double
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|// add fast type converters from most common used
annotation|@
name|Converter
DECL|method|toString (Integer value)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|Integer
name|value
parameter_list|)
block|{
return|return
name|value
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toString (Long value)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|Long
name|value
parameter_list|)
block|{
return|return
name|value
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toString (Boolean value)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|Boolean
name|value
parameter_list|)
block|{
return|return
name|value
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toString (StringBuffer value)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|StringBuffer
name|value
parameter_list|)
block|{
return|return
name|value
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toString (StringBuilder value)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|StringBuilder
name|value
parameter_list|)
block|{
return|return
name|value
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toBoolean (String value)
specifier|public
specifier|static
name|Boolean
name|toBoolean
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|toBoolean
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

