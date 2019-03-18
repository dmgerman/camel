begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Method
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
name|RuntimeCamelException
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
name|TypeConverterSupport
import|;
end_import

begin_comment
comment|/**  * A type converter which is used to convert from String to enum type  */
end_comment

begin_class
DECL|class|EnumTypeConverter
specifier|public
class|class
name|EnumTypeConverter
extends|extends
name|TypeConverterSupport
block|{
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
name|EnumTypeConverter
operator|.
name|doConvertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|doConvertTo (Class<T> type, Exchange exchange, Object value)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|doConvertTo
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
if|if
condition|(
name|type
operator|.
name|isEnum
argument_list|()
condition|)
block|{
name|String
name|text
init|=
name|value
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|Enum
argument_list|>
name|enumClass
init|=
operator|(
name|Class
argument_list|<
name|Enum
argument_list|>
operator|)
name|type
decl_stmt|;
comment|// we want to match case insensitive for enums
for|for
control|(
name|Enum
name|enumValue
range|:
name|enumClass
operator|.
name|getEnumConstants
argument_list|()
control|)
block|{
if|if
condition|(
name|enumValue
operator|.
name|name
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|text
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|enumValue
argument_list|)
return|;
block|}
block|}
comment|// fallback to the JDK valueOf which is case-sensitive and throws exception if not found
name|Method
name|method
decl_stmt|;
try|try
block|{
name|method
operator|=
name|type
operator|.
name|getMethod
argument_list|(
literal|"valueOf"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Could not find valueOf method on enum type: "
operator|+
name|type
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
return|return
operator|(
name|T
operator|)
name|ObjectHelper
operator|.
name|invokeMethod
argument_list|(
name|method
argument_list|,
literal|null
argument_list|,
name|text
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

