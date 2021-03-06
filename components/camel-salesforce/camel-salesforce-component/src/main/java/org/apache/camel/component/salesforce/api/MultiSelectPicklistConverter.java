begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
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
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|converters
operator|.
name|ConversionException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|converters
operator|.
name|Converter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|converters
operator|.
name|MarshallingContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|converters
operator|.
name|UnmarshallingContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|HierarchicalStreamReader
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|HierarchicalStreamWriter
import|;
end_import

begin_comment
comment|/**  * XStream converter for handling MSPs mapped to Picklist enum array fields.  */
end_comment

begin_class
DECL|class|MultiSelectPicklistConverter
specifier|public
class|class
name|MultiSelectPicklistConverter
implements|implements
name|Converter
block|{
DECL|field|FACTORY_METHOD
specifier|private
specifier|static
specifier|final
name|String
name|FACTORY_METHOD
init|=
literal|"fromValue"
decl_stmt|;
annotation|@
name|Override
DECL|method|marshal (Object o, HierarchicalStreamWriter writer, MarshallingContext context)
specifier|public
name|void
name|marshal
parameter_list|(
name|Object
name|o
parameter_list|,
name|HierarchicalStreamWriter
name|writer
parameter_list|,
name|MarshallingContext
name|context
parameter_list|)
block|{
comment|// get Picklist enum element class from array class
name|Class
argument_list|<
name|?
argument_list|>
name|arrayClass
init|=
name|o
operator|.
name|getClass
argument_list|()
decl_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|aClass
init|=
name|arrayClass
operator|.
name|getComponentType
argument_list|()
decl_stmt|;
try|try
block|{
name|Method
name|getterMethod
init|=
name|aClass
operator|.
name|getMethod
argument_list|(
literal|"value"
argument_list|)
decl_stmt|;
specifier|final
name|int
name|length
init|=
name|Array
operator|.
name|getLength
argument_list|(
name|o
argument_list|)
decl_stmt|;
comment|// construct a string of form value1;value2;...
specifier|final
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
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
name|length
condition|;
name|i
operator|++
control|)
block|{
name|buffer
operator|.
name|append
argument_list|(
operator|(
name|String
operator|)
name|getterMethod
operator|.
name|invoke
argument_list|(
name|Array
operator|.
name|get
argument_list|(
name|o
argument_list|,
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|<
operator|(
name|length
operator|-
literal|1
operator|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|';'
argument_list|)
expr_stmt|;
block|}
block|}
name|writer
operator|.
name|setValue
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConversionException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Exception writing pick list value %s of type %s: %s"
argument_list|,
name|o
argument_list|,
name|o
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|unmarshal (HierarchicalStreamReader reader, UnmarshallingContext context)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|HierarchicalStreamReader
name|reader
parameter_list|,
name|UnmarshallingContext
name|context
parameter_list|)
block|{
specifier|final
name|String
name|listValue
init|=
name|reader
operator|.
name|getValue
argument_list|()
decl_stmt|;
comment|// get Picklist enum element class from array class
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|requiredArrayType
init|=
name|context
operator|.
name|getRequiredType
argument_list|()
decl_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|requiredType
init|=
name|requiredArrayType
operator|.
name|getComponentType
argument_list|()
decl_stmt|;
try|try
block|{
name|Method
name|factoryMethod
init|=
name|requiredType
operator|.
name|getMethod
argument_list|(
name|FACTORY_METHOD
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// parse the string of the form value1;value2;...
specifier|final
name|String
index|[]
name|value
init|=
name|listValue
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
specifier|final
name|int
name|length
init|=
name|value
operator|.
name|length
decl_stmt|;
specifier|final
name|Object
name|resultArray
init|=
name|Array
operator|.
name|newInstance
argument_list|(
name|requiredType
argument_list|,
name|length
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
name|length
condition|;
name|i
operator|++
control|)
block|{
comment|// use factory method to create object
name|Array
operator|.
name|set
argument_list|(
name|resultArray
argument_list|,
name|i
argument_list|,
name|factoryMethod
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
name|value
index|[
name|i
index|]
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|resultArray
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConversionException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Exception reading pick list value %s of type %s: %s"
argument_list|,
name|listValue
argument_list|,
name|requiredArrayType
operator|.
name|getName
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|canConvert (Class aClass)
specifier|public
name|boolean
name|canConvert
parameter_list|(
name|Class
name|aClass
parameter_list|)
block|{
try|try
block|{
comment|// check whether the Class is an array, and whether the array elment
comment|// is a Picklist enum class
specifier|final
name|Class
name|componentType
init|=
name|aClass
operator|.
name|getComponentType
argument_list|()
decl_stmt|;
return|return
name|componentType
operator|!=
literal|null
operator|&&
name|Enum
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|componentType
argument_list|)
operator|&&
name|componentType
operator|.
name|getMethod
argument_list|(
name|FACTORY_METHOD
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|!=
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

