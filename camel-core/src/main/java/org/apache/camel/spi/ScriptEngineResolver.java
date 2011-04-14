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
name|javax
operator|.
name|script
operator|.
name|ScriptEngine
import|;
end_import

begin_comment
comment|/**  * Used to load scripting engines when the default service registration  * mechanism does not work (e.g. OSGi)  */
end_comment

begin_interface
DECL|interface|ScriptEngineResolver
specifier|public
interface|interface
name|ScriptEngineResolver
block|{
comment|/**      * Resolves the given script engine given its name.      *      * @param name    the name of the script engine you're looking for      * @return the script engine factory or<tt>null</tt> if not possible to resolve      */
DECL|method|resolveScriptEngine (String name)
name|ScriptEngine
name|resolveScriptEngine
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

