begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
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
name|AccessibleObject
import|;
end_import

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
name|AsyncCallback
import|;
end_import

begin_comment
comment|/**  * Information used by Camel to perform method invocation.  *  * @version   */
end_comment

begin_interface
DECL|interface|MethodInvocation
specifier|public
interface|interface
name|MethodInvocation
block|{
DECL|method|getMethod ()
name|Method
name|getMethod
parameter_list|()
function_decl|;
DECL|method|getArguments ()
name|Object
index|[]
name|getArguments
parameter_list|()
function_decl|;
comment|/**      * Proceed and invokes the method.      *      * @param callback   the callback      * @return see {@link org.apache.camel.AsyncProcessor#process(org.apache.camel.Exchange, org.apache.camel.AsyncCallback)}      */
DECL|method|proceed (AsyncCallback callback)
name|boolean
name|proceed
parameter_list|(
name|AsyncCallback
name|callback
parameter_list|)
function_decl|;
DECL|method|getThis ()
name|Object
name|getThis
parameter_list|()
function_decl|;
DECL|method|getStaticPart ()
name|AccessibleObject
name|getStaticPart
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

