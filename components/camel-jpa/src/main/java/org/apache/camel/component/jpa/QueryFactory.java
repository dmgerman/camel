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
name|javax
operator|.
name|persistence
operator|.
name|Query
import|;
end_import

begin_comment
comment|/**  * A Strategy to create a query to search for objects in a database  */
end_comment

begin_interface
DECL|interface|QueryFactory
specifier|public
interface|interface
name|QueryFactory
block|{
comment|/**      * Creates a new query to find objects to be processed      *      * @param entityManager the enity manager      * @return the query configured with any parameters etc      */
DECL|method|createQuery (EntityManager entityManager)
name|Query
name|createQuery
parameter_list|(
name|EntityManager
name|entityManager
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

