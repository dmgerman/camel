begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|converter
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
name|List
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
name|TypeConverter
import|;
end_import

begin_comment
comment|/**  * A type converter which is used to convert to and from array types  * particularly for derived types of array component types and dealing with  * primitive array types.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ArrayTypeConverter
specifier|public
class|class
name|ArrayTypeConverter
implements|implements
name|TypeConverter
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|convertTo (Class<T> type, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|convertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|type
operator|.
name|isArray
argument_list|()
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
name|collection
init|=
operator|(
name|Collection
operator|)
name|value
decl_stmt|;
name|Object
name|array
init|=
name|Array
operator|.
name|newInstance
argument_list|(
name|type
operator|.
name|getComponentType
argument_list|()
argument_list|,
name|collection
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|instanceof
name|Object
index|[]
condition|)
block|{
name|collection
operator|.
name|toArray
argument_list|(
operator|(
name|Object
index|[]
operator|)
name|array
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|index
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Object
name|element
range|:
name|collection
control|)
block|{
name|Array
operator|.
name|set
argument_list|(
name|array
argument_list|,
name|index
operator|++
argument_list|,
name|element
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|(
name|T
operator|)
name|array
return|;
block|}
elseif|else
if|if
condition|(
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
condition|)
block|{
name|int
name|size
init|=
name|Array
operator|.
name|getLength
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|Object
name|answer
init|=
name|Array
operator|.
name|newInstance
argument_list|(
name|type
operator|.
name|getComponentType
argument_list|()
argument_list|,
name|size
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|Array
operator|.
name|set
argument_list|(
name|answer
argument_list|,
name|i
argument_list|,
name|Array
operator|.
name|get
argument_list|(
name|value
argument_list|,
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|T
operator|)
name|answer
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|Collection
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
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
name|Object
index|[]
condition|)
block|{
return|return
operator|(
name|T
operator|)
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
name|int
name|size
init|=
name|Array
operator|.
name|getLength
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|List
name|answer
init|=
operator|new
name|ArrayList
argument_list|(
name|size
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|Array
operator|.
name|get
argument_list|(
name|value
argument_list|,
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|T
operator|)
name|answer
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|convertTo (Class<T> type, Exchange exchange, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|convertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|convertTo
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|mandatoryConvertTo (Class<T> type, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|mandatoryConvertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|convertTo
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|mandatoryConvertTo (Class<T> type, Exchange exchange, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|mandatoryConvertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|convertTo
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

