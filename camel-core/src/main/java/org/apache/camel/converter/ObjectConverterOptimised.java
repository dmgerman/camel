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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Optimised {@link ObjectConverter}  */
end_comment

begin_class
DECL|class|ObjectConverterOptimised
specifier|public
specifier|final
class|class
name|ObjectConverterOptimised
block|{
DECL|method|ObjectConverterOptimised ()
specifier|private
name|ObjectConverterOptimised
parameter_list|()
block|{     }
DECL|method|convertTo (final Class<?> type, final Exchange exchange, final Object value)
specifier|public
specifier|static
name|Object
name|convertTo
parameter_list|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|Object
name|value
parameter_list|)
throws|throws
name|Exception
block|{
comment|// converting to a String is very common
if|if
condition|(
name|type
operator|==
name|String
operator|.
name|class
condition|)
block|{
name|Class
name|fromType
init|=
name|value
operator|.
name|getClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|fromType
operator|==
name|boolean
operator|.
name|class
operator|||
name|fromType
operator|==
name|Boolean
operator|.
name|class
condition|)
block|{
return|return
name|value
operator|.
name|toString
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|fromType
operator|==
name|int
operator|.
name|class
operator|||
name|fromType
operator|==
name|Integer
operator|.
name|class
condition|)
block|{
return|return
name|value
operator|.
name|toString
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|fromType
operator|==
name|long
operator|.
name|class
operator|||
name|fromType
operator|==
name|Long
operator|.
name|class
condition|)
block|{
return|return
name|value
operator|.
name|toString
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|fromType
operator|==
name|char
index|[]
operator|.
name|class
condition|)
block|{
return|return
name|ObjectConverter
operator|.
name|fromCharArray
argument_list|(
operator|(
name|char
index|[]
operator|)
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|fromType
operator|==
name|StringBuffer
operator|.
name|class
operator|||
name|fromType
operator|==
name|StringBuilder
operator|.
name|class
condition|)
block|{
return|return
name|value
operator|.
name|toString
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
if|if
condition|(
name|type
operator|==
name|boolean
operator|.
name|class
operator|||
name|type
operator|==
name|Boolean
operator|.
name|class
condition|)
block|{
return|return
name|ObjectConverter
operator|.
name|toBoolean
argument_list|(
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|int
operator|.
name|class
operator|||
name|type
operator|==
name|Integer
operator|.
name|class
condition|)
block|{
return|return
name|ObjectConverter
operator|.
name|toInteger
argument_list|(
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|long
operator|.
name|class
operator|||
name|type
operator|==
name|Long
operator|.
name|class
condition|)
block|{
return|return
name|ObjectConverter
operator|.
name|toLong
argument_list|(
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|byte
operator|.
name|class
operator|||
name|type
operator|==
name|Byte
operator|.
name|class
condition|)
block|{
return|return
name|ObjectConverter
operator|.
name|toByte
argument_list|(
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|double
operator|.
name|class
operator|||
name|type
operator|==
name|Double
operator|.
name|class
condition|)
block|{
return|return
name|ObjectConverter
operator|.
name|toDouble
argument_list|(
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|float
operator|.
name|class
operator|||
name|type
operator|==
name|Float
operator|.
name|class
condition|)
block|{
return|return
name|ObjectConverter
operator|.
name|toFloat
argument_list|(
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|short
operator|.
name|class
operator|||
name|type
operator|==
name|Short
operator|.
name|class
condition|)
block|{
return|return
name|ObjectConverter
operator|.
name|toShort
argument_list|(
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
operator|(
name|type
operator|==
name|char
operator|.
name|class
operator|||
name|type
operator|==
name|Character
operator|.
name|class
operator|)
operator|&&
name|value
operator|.
name|getClass
argument_list|()
operator|==
name|String
operator|.
name|class
condition|)
block|{
return|return
name|ObjectConverter
operator|.
name|toCharacter
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
operator|(
name|type
operator|==
name|char
index|[]
operator|.
expr|class
operator|||
name|type
operator|==
name|Character
index|[]
operator|.
expr|class
operator|)
operator|&&
name|value
operator|.
name|getClass
argument_list|()
operator|==
name|String
operator|.
name|class
condition|)
block|{
return|return
name|ObjectConverter
operator|.
name|toCharArray
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
return|;
block|}
if|if
condition|(
name|type
operator|==
name|Iterator
operator|.
name|class
condition|)
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
elseif|else
if|if
condition|(
name|type
operator|==
name|Iterable
operator|.
name|class
condition|)
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
if|if
condition|(
name|type
operator|==
name|Class
operator|.
name|class
condition|)
block|{
return|return
name|ObjectConverter
operator|.
name|toClass
argument_list|(
name|value
argument_list|,
name|exchange
argument_list|)
return|;
block|}
comment|// no optimised type converter found
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

