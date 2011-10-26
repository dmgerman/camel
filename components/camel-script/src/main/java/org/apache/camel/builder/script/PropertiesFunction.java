begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.script
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|script
package|;
end_package

begin_comment
comment|/**  * Function in scripting languages to leverage the Camel {@link org.apache.camel.component.properties.PropertiesComponent}  * to make it easier to resolve property placeholders.  */
end_comment

begin_interface
DECL|interface|PropertiesFunction
specifier|public
interface|interface
name|PropertiesFunction
block|{
comment|/**      * Resolve the given key using the property placeholders      *      * @param key the key      * @return the resolved value      */
DECL|method|resolve (String key)
name|String
name|resolve
parameter_list|(
name|String
name|key
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

