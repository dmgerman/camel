begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * A auto generated {@link PropertyConfigurer} for fast configuration of Camel components& endpoints.  */
end_comment

begin_interface
DECL|interface|GeneratedPropertyConfigurer
specifier|public
interface|interface
name|GeneratedPropertyConfigurer
extends|extends
name|PropertyConfigurer
block|{
comment|/**      * Configures the property      *      * @param camelContext  the Camel context      * @param target        the target instance such as {@link org.apache.camel.Endpoint} or {@link org.apache.camel.Component}.      * @param name          the property name      * @param value         the property value      * @param ignoreCase    whether to ignore case for matching the property name      * @return<tt>true</tt> if the configurer configured the property,<tt>false</tt> if the property does not exists      */
DECL|method|configure (CamelContext camelContext, Object target, String name, Object value, boolean ignoreCase)
name|boolean
name|configure
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Object
name|target
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|,
name|boolean
name|ignoreCase
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

