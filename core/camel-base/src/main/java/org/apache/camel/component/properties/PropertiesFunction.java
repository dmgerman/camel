begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * A function that is applied instead of looking up a property placeholder.  */
end_comment

begin_interface
DECL|interface|PropertiesFunction
specifier|public
interface|interface
name|PropertiesFunction
block|{
comment|/**      * Name of the function which is used as<tt>name:</tt> to let the properties component know its a function.      */
DECL|method|getName ()
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Applies the function      *      * @param remainder    the remainder value      * @return a value as the result of the function      */
DECL|method|apply (String remainder)
name|String
name|apply
parameter_list|(
name|String
name|remainder
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

