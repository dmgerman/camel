begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jpa
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jpa
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|orm
operator|.
name|jpa
operator|.
name|JpaCallback
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_interface
DECL|interface|TransactionStrategy
specifier|public
interface|interface
name|TransactionStrategy
block|{
comment|/**      * Executes in a transaction.      *      * @param callback the callback      * @return the result      */
DECL|method|execute (JpaCallback<?> callback)
name|Object
name|execute
parameter_list|(
name|JpaCallback
argument_list|<
name|?
argument_list|>
name|callback
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

