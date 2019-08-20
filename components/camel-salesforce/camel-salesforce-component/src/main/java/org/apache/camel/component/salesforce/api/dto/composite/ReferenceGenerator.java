begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.dto.composite
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|composite
package|;
end_package

begin_comment
comment|/**  * SObject tree Composite API interface for {@code referenceId} generation. For  * each object given to the {@link ReferenceGenerator#nextReferenceFor(Object)}  * method, the implementation should generate reference identifiers. Reference  * identifiers need to be unique within one SObject tree request and should  * start with alphanumeric character.  *<p/>  * For example you can provide your {@link ReferenceGenerator} implementation  * that uses identities within your own system as references, i.e. primary keys  * of records in your database.  *  * @see Counter  */
end_comment

begin_interface
DECL|interface|ReferenceGenerator
specifier|public
interface|interface
name|ReferenceGenerator
block|{
comment|/**      * Generates unique, within a request, reference identifier for the given      * object. Reference identifier must start with an alphanumeric.      *      * @param object object to generate reference identifier for      * @return generated reference identifier      */
DECL|method|nextReferenceFor (Object object)
name|String
name|nextReferenceFor
parameter_list|(
name|Object
name|object
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

