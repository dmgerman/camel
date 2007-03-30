begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Collections
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
name|Arrays
import|;
end_import

begin_comment
comment|/**  * Some core java.lang based converters  *   * @version $Revision$  */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|ObjectConverter
specifier|public
class|class
name|ObjectConverter
block|{
comment|/**      * Creates an iterator over the value if the value is a collection, an Object[] or a primitive type array; otherwise      * to simplify the caller's code, we just create a singleton collection iterator over a single value      */
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
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|value
argument_list|)
operator|.
name|iterator
argument_list|()
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
comment|/**      * Converts the given value to a boolean, handling strings or Boolean objects; otherwise returning true if non-null      */
annotation|@
name|Converter
DECL|method|toBoolean (Object value)
specifier|public
specifier|static
name|boolean
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
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

