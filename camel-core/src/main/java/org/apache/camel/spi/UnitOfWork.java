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
name|Exchange
import|;
end_import

begin_comment
comment|/**  * An object representing the unit of work processing an {@link Exchange}  * which allows the use of {@link Synchronization} hooks. This object might map one-to-one with  * a transaction in JPA or Spring; or might not.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|UnitOfWork
specifier|public
interface|interface
name|UnitOfWork
block|{
comment|/**      * Adds a synchronization hook      *      * @param synchronization      */
DECL|method|addSynchronization (Synchronization synchronization)
name|void
name|addSynchronization
parameter_list|(
name|Synchronization
name|synchronization
parameter_list|)
function_decl|;
comment|/**      * Removes a synchronization hook      *      * @param synchronization      */
DECL|method|removeSynchronization (Synchronization synchronization)
name|void
name|removeSynchronization
parameter_list|(
name|Synchronization
name|synchronization
parameter_list|)
function_decl|;
comment|/**      * Invoked when this unit of work has been completed, whether it has failed or completed      */
DECL|method|done (Exchange exchange)
name|void
name|done
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Returns the unique ID of this unit of work, lazily creating one if it does not yet have one      *      * @return      */
DECL|method|getId ()
name|String
name|getId
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

