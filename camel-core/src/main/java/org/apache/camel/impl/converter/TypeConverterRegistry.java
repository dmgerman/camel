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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|TypeConverter
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
name|spi
operator|.
name|Injector
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|TypeConverterRegistry
specifier|public
interface|interface
name|TypeConverterRegistry
block|{
comment|/**      * Allows a new type converter to be bregistered      *      * @param toType        the type to convert to      * @param fromType      the type to convert from      * @param typeConverter the type converter to use      */
DECL|method|addTypeConverter (Class toType, Class fromType, TypeConverter typeConverter)
name|void
name|addTypeConverter
parameter_list|(
name|Class
name|toType
parameter_list|,
name|Class
name|fromType
parameter_list|,
name|TypeConverter
name|typeConverter
parameter_list|)
function_decl|;
DECL|method|getInjector ()
name|Injector
name|getInjector
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

