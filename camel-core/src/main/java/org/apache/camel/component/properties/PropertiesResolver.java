begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.properties
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|properties
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|CamelContext
import|;
end_import

begin_comment
comment|/**  * A resolver to load properties from a given source such as a file from a classpath.  *<p/>  * Implementations can also load properties from another source source as JNDI.  *  * @version   */
end_comment

begin_interface
DECL|interface|PropertiesResolver
specifier|public
interface|interface
name|PropertiesResolver
block|{
comment|/**      * Resolve properties from the given uri      *      *      * @param context the camel context      * @param ignoreMissingLocation ignore silently if the property file is missing      * @param uri uri(s) defining the source(s)  @return the properties      * @throws Exception is thrown if resolving the properties failed      */
DECL|method|resolveProperties (CamelContext context, boolean ignoreMissingLocation, String... uri)
name|Properties
name|resolveProperties
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|boolean
name|ignoreMissingLocation
parameter_list|,
name|String
modifier|...
name|uri
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

