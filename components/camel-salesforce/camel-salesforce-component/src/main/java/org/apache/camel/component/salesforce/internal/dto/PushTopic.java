begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.internal.dto
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
name|internal
operator|.
name|dto
package|;
end_package

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|annotations
operator|.
name|XStreamAlias
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|annotations
operator|.
name|XStreamConverter
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|PicklistEnumConverter
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|AbstractSObjectBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jackson
operator|.
name|annotate
operator|.
name|JsonProperty
import|;
end_import

begin_comment
comment|/**  * Salesforce DTO for SObject PushTopic  */
end_comment

begin_class
annotation|@
name|XStreamAlias
argument_list|(
literal|"PushTopic"
argument_list|)
DECL|class|PushTopic
specifier|public
class|class
name|PushTopic
extends|extends
name|AbstractSObjectBase
block|{
DECL|field|query
specifier|private
name|String
name|query
decl_stmt|;
DECL|field|apiVersion
specifier|private
name|Double
name|apiVersion
decl_stmt|;
DECL|field|isActive
specifier|private
name|Boolean
name|isActive
decl_stmt|;
annotation|@
name|XStreamConverter
argument_list|(
name|PicklistEnumConverter
operator|.
name|class
argument_list|)
DECL|field|notifyForFields
specifier|private
name|NotifyForFieldsEnum
name|notifyForFields
decl_stmt|;
annotation|@
name|XStreamConverter
argument_list|(
name|PicklistEnumConverter
operator|.
name|class
argument_list|)
DECL|field|notifyForOperations
specifier|private
name|NotifyForOperationsEnum
name|notifyForOperations
decl_stmt|;
DECL|field|description
specifier|private
name|String
name|description
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"Query"
argument_list|)
DECL|method|getQuery ()
specifier|public
name|String
name|getQuery
parameter_list|()
block|{
return|return
name|this
operator|.
name|query
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Query"
argument_list|)
DECL|method|setQuery (String query)
specifier|public
name|void
name|setQuery
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"ApiVersion"
argument_list|)
DECL|method|getApiVersion ()
specifier|public
name|Double
name|getApiVersion
parameter_list|()
block|{
return|return
name|this
operator|.
name|apiVersion
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"ApiVersion"
argument_list|)
DECL|method|setApiVersion (Double apiVersion)
specifier|public
name|void
name|setApiVersion
parameter_list|(
name|Double
name|apiVersion
parameter_list|)
block|{
name|this
operator|.
name|apiVersion
operator|=
name|apiVersion
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"IsActive"
argument_list|)
DECL|method|getIsActive ()
specifier|public
name|Boolean
name|getIsActive
parameter_list|()
block|{
return|return
name|this
operator|.
name|isActive
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"IsActive"
argument_list|)
DECL|method|setIsActive (Boolean isActive)
specifier|public
name|void
name|setIsActive
parameter_list|(
name|Boolean
name|isActive
parameter_list|)
block|{
name|this
operator|.
name|isActive
operator|=
name|isActive
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"NotifyForFields"
argument_list|)
DECL|method|getNotifyForFields ()
specifier|public
name|NotifyForFieldsEnum
name|getNotifyForFields
parameter_list|()
block|{
return|return
name|this
operator|.
name|notifyForFields
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"NotifyForFields"
argument_list|)
DECL|method|setNotifyForFields (NotifyForFieldsEnum notifyForFields)
specifier|public
name|void
name|setNotifyForFields
parameter_list|(
name|NotifyForFieldsEnum
name|notifyForFields
parameter_list|)
block|{
name|this
operator|.
name|notifyForFields
operator|=
name|notifyForFields
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"NotifyForOperations"
argument_list|)
DECL|method|getNotifyForOperations ()
specifier|public
name|NotifyForOperationsEnum
name|getNotifyForOperations
parameter_list|()
block|{
return|return
name|this
operator|.
name|notifyForOperations
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"NotifyForOperations"
argument_list|)
DECL|method|setNotifyForOperations (NotifyForOperationsEnum notifyForOperations)
specifier|public
name|void
name|setNotifyForOperations
parameter_list|(
name|NotifyForOperationsEnum
name|notifyForOperations
parameter_list|)
block|{
name|this
operator|.
name|notifyForOperations
operator|=
name|notifyForOperations
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Description"
argument_list|)
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|this
operator|.
name|description
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Description"
argument_list|)
DECL|method|setDescription (String description)
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
block|}
end_class

end_unit

