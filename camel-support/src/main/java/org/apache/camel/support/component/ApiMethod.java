begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|component
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * Interface for proxy methods.  */
end_comment

begin_interface
DECL|interface|ApiMethod
specifier|public
interface|interface
name|ApiMethod
block|{
comment|/**      * Returns method name.      * @return name      */
DECL|method|getName ()
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Returns method result type.      * @return result type      */
DECL|method|getResultType ()
name|Class
argument_list|<
name|?
argument_list|>
name|getResultType
parameter_list|()
function_decl|;
comment|/**      * Returns method argument names.      * @return argument names      */
DECL|method|getArgNames ()
name|List
argument_list|<
name|String
argument_list|>
name|getArgNames
parameter_list|()
function_decl|;
comment|/**      * Return method argument types.      * @return argument types      */
DECL|method|getArgTypes ()
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getArgTypes
parameter_list|()
function_decl|;
comment|/**      * Returns {@link Method} in proxy type.      * @return method      */
DECL|method|getMethod ()
name|Method
name|getMethod
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

