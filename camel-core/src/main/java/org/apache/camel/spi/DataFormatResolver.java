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
name|CamelContext
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
name|model
operator|.
name|DataFormatDefinition
import|;
end_import

begin_comment
comment|/**  * Represents a resolver of data formats.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|DataFormatResolver
specifier|public
interface|interface
name|DataFormatResolver
block|{
comment|/**      * Resolves the given data format given its name.      *      * @param name the name of the data format to lookup in {@link org.apache.camel.spi.Registry} or create      * @param context the camel context      * @return the data format or<tt>null</tt> if not possible to resolve      */
DECL|method|resolveDataFormat (String name, CamelContext context)
name|DataFormat
name|resolveDataFormat
parameter_list|(
name|String
name|name
parameter_list|,
name|CamelContext
name|context
parameter_list|)
function_decl|;
comment|/**      * Resolves the given data format definition given its name.      *      * @param name the name of the data format to lookup in {@link org.apache.camel.spi.Registry} or create      * @param context the camel context      * @return the data format or<tt>null</tt> if not possible to resolve      */
DECL|method|resolveDataFormatDefinition (String name, CamelContext context)
name|DataFormatDefinition
name|resolveDataFormatDefinition
parameter_list|(
name|String
name|name
parameter_list|,
name|CamelContext
name|context
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

