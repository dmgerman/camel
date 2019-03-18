begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|persistence
operator|.
name|EntityManager
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
name|Exchange
import|;
end_import

begin_comment
comment|/**  * A strategy for deleting entity beans which have been processed; either by a real delete or by an update of some  * application specific properties so that the entity bean will not be found in future polling queries.  */
end_comment

begin_interface
DECL|interface|DeleteHandler
specifier|public
interface|interface
name|DeleteHandler
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * Deletes the entity bean after it has been processed either by actually      * deleting the object or updating it in a way so that future queries do not return this object again.      *      * @param entityManager the entity manager      * @param entityBean    the entity bean that has been processed and should be deleted      * @param exchange      the exchange that could be used to update the entityBean      */
DECL|method|deleteObject (EntityManager entityManager, Object entityBean, Exchange exchange)
name|void
name|deleteObject
parameter_list|(
name|EntityManager
name|entityManager
parameter_list|,
name|Object
name|entityBean
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

