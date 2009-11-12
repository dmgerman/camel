begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
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
name|TypeConverter
import|;
end_import

begin_comment
comment|/**  * Registry for type converters.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|TypeConverterRegistry
specifier|public
interface|interface
name|TypeConverterRegistry
block|{
comment|/**      * Registers a new type converter      *      * @param toType        the type to convert to      * @param fromType      the type to convert from      * @param typeConverter the type converter to use      */
DECL|method|addTypeConverter (Class<?> toType, Class<?> fromType, TypeConverter typeConverter)
name|void
name|addTypeConverter
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|toType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|fromType
parameter_list|,
name|TypeConverter
name|typeConverter
parameter_list|)
function_decl|;
comment|/**      * Registers a new fallback type converter      *      * @param typeConverter the type converter to use      */
DECL|method|addFallbackTypeConverter (TypeConverter typeConverter)
name|void
name|addFallbackTypeConverter
parameter_list|(
name|TypeConverter
name|typeConverter
parameter_list|)
function_decl|;
comment|/**      * Performs a lookup for a given type converter.      *      * @param toType        the type to convert to      * @param fromType      the type to convert from      * @return the type converter or null if not found.      */
DECL|method|lookup (Class<?> toType, Class<?> fromType)
name|TypeConverter
name|lookup
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|toType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|fromType
parameter_list|)
function_decl|;
comment|/**      * Sets the injector to be used for creating new instances during type convertions.      */
DECL|method|setInjector (Injector injector)
name|void
name|setInjector
parameter_list|(
name|Injector
name|injector
parameter_list|)
function_decl|;
comment|/**      * Gets the injector      */
DECL|method|getInjector ()
name|Injector
name|getInjector
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

