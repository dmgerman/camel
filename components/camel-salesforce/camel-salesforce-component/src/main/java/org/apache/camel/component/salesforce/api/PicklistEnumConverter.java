begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_class
DECL|class|PicklistEnumConverter
specifier|public
class|class
name|PicklistEnumConverter
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
name|Class
argument_list|<
name|?
argument_list|>
name|aClass
init|=
name|o
operator|.
name|getClass
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
name|writer
operator|.
name|setValue
argument_list|(
operator|(
name|String
operator|)
name|getterMethod
operator|.
name|invoke
argument_list|(
name|o
argument_list|)
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
name|IllegalArgumentException
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
name|String
name|value
init|=
name|reader
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|requiredType
init|=
name|context
operator|.
name|getRequiredType
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
comment|// use factory method to create object
return|return
name|factoryMethod
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
name|value
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Security Exception reading pick list value %s of type %s: %s"
argument_list|,
name|value
argument_list|,
name|context
operator|.
name|getRequiredType
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
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Exception reading pick list value %s of type %s: %s"
argument_list|,
name|value
argument_list|,
name|context
operator|.
name|getRequiredType
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
return|return
name|Enum
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|aClass
argument_list|)
operator|&&
name|aClass
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

