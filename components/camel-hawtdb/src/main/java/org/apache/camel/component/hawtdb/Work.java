begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hawtdb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hawtdb
package|;
end_package

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|hawtdb
operator|.
name|api
operator|.
name|Transaction
import|;
end_import

begin_comment
comment|/**  * Demarcates the statements that need to be performed as a   * HawtDB transactional unit of work.  */
end_comment

begin_interface
DECL|interface|Work
interface|interface
name|Work
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * Executs the work within the bounds of the given transaction      *      * @param transaction the transaction      * @return result of the work, can be<tt>null</tt> if no result to return.      */
DECL|method|execute (Transaction transaction)
name|T
name|execute
parameter_list|(
name|Transaction
name|transaction
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

