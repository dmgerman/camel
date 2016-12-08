begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.dto
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
package|;
end_package

begin_comment
comment|/**  * Subclass of {@link AbstractSObjectBase} that contains additional metadata about SObject. The  * {@code camel-salesforce-maven-plugin} generates Data Transfer Objects (DTO) by subclassing this class and  * implementing the {@link AbstractDescribedSObjectBase#description()} method from the metadata received from  * Salesforce. Note that there are no guarantees about all fields in the {@link SObjectDescription} being set. This is  * to prevent unnecessary memory usage, and to prevent running into Java method length limit.  */
end_comment

begin_class
DECL|class|AbstractDescribedSObjectBase
specifier|public
specifier|abstract
class|class
name|AbstractDescribedSObjectBase
extends|extends
name|AbstractSObjectBase
block|{
comment|/**      * Additional metadata about this SObject. There are no guarantees that all of the fields of      * {@link SObjectDescription} will be set.      *      * @return metadata description of this SObject      */
DECL|method|description ()
specifier|public
specifier|abstract
name|SObjectDescription
name|description
parameter_list|()
function_decl|;
block|}
end_class

end_unit

