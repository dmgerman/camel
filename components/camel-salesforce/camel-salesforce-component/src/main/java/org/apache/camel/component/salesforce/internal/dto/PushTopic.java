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
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonProperty
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

begin_comment
comment|//CHECKSTYLE:OFF
end_comment

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
comment|// WARNING: these fields have case sensitive names,
comment|// the field name MUST match the field name used by Salesforce
comment|// DO NOT change these field names to camel case!!!
DECL|field|Query
specifier|private
name|String
name|Query
decl_stmt|;
DECL|field|ApiVersion
specifier|private
name|Double
name|ApiVersion
decl_stmt|;
DECL|field|IsActive
specifier|private
name|Boolean
name|IsActive
decl_stmt|;
annotation|@
name|XStreamConverter
argument_list|(
name|PicklistEnumConverter
operator|.
name|class
argument_list|)
DECL|field|NotifyForFields
specifier|private
name|NotifyForFieldsEnum
name|NotifyForFields
decl_stmt|;
annotation|@
name|XStreamConverter
argument_list|(
name|PicklistEnumConverter
operator|.
name|class
argument_list|)
DECL|field|NotifyForOperations
specifier|private
name|NotifyForOperationsEnum
name|NotifyForOperations
decl_stmt|;
DECL|field|Description
specifier|private
name|String
name|Description
decl_stmt|;
DECL|field|NotifyForOperationCreate
specifier|private
name|Boolean
name|NotifyForOperationCreate
decl_stmt|;
DECL|field|NotifyForOperationUpdate
specifier|private
name|Boolean
name|NotifyForOperationUpdate
decl_stmt|;
DECL|field|NotifyForOperationDelete
specifier|private
name|Boolean
name|NotifyForOperationDelete
decl_stmt|;
DECL|field|NotifyForOperationUndelete
specifier|private
name|Boolean
name|NotifyForOperationUndelete
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
name|Query
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
name|Query
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
name|ApiVersion
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
name|ApiVersion
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
name|IsActive
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
name|IsActive
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
name|NotifyForFields
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
name|NotifyForFields
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
name|NotifyForOperations
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
name|NotifyForOperations
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
name|Description
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
name|Description
operator|=
name|description
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"NotifyForOperationCreate"
argument_list|)
DECL|method|getNotifyForOperationCreate ()
specifier|public
name|Boolean
name|getNotifyForOperationCreate
parameter_list|()
block|{
return|return
name|this
operator|.
name|NotifyForOperationCreate
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"NotifyForOperationCreate"
argument_list|)
DECL|method|setNotifyForOperationCreate (Boolean notifyForOperationCreate)
specifier|public
name|void
name|setNotifyForOperationCreate
parameter_list|(
name|Boolean
name|notifyForOperationCreate
parameter_list|)
block|{
name|this
operator|.
name|NotifyForOperationCreate
operator|=
name|notifyForOperationCreate
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"NotifyForOperationUpdate"
argument_list|)
DECL|method|getNotifyForOperationUpdate ()
specifier|public
name|Boolean
name|getNotifyForOperationUpdate
parameter_list|()
block|{
return|return
name|this
operator|.
name|NotifyForOperationUpdate
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"NotifyForOperationUpdate"
argument_list|)
DECL|method|setNotifyForOperationUpdate (Boolean notifyForOperationUpdate)
specifier|public
name|void
name|setNotifyForOperationUpdate
parameter_list|(
name|Boolean
name|notifyForOperationUpdate
parameter_list|)
block|{
name|this
operator|.
name|NotifyForOperationUpdate
operator|=
name|notifyForOperationUpdate
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"NotifyForOperationDelete"
argument_list|)
DECL|method|getNotifyForOperationDelete ()
specifier|public
name|Boolean
name|getNotifyForOperationDelete
parameter_list|()
block|{
return|return
name|this
operator|.
name|NotifyForOperationDelete
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"NotifyForOperationDelete"
argument_list|)
DECL|method|setNotifyForOperationDelete (Boolean notifyForOperationDelete)
specifier|public
name|void
name|setNotifyForOperationDelete
parameter_list|(
name|Boolean
name|notifyForOperationDelete
parameter_list|)
block|{
name|this
operator|.
name|NotifyForOperationDelete
operator|=
name|notifyForOperationDelete
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"NotifyForOperationUndelete"
argument_list|)
DECL|method|getNotifyForOperationUndelete ()
specifier|public
name|Boolean
name|getNotifyForOperationUndelete
parameter_list|()
block|{
return|return
name|this
operator|.
name|NotifyForOperationUndelete
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"NotifyForOperationUndelete"
argument_list|)
DECL|method|setNotifyForOperationUndelete (Boolean notifyForOperationUndelete)
specifier|public
name|void
name|setNotifyForOperationUndelete
parameter_list|(
name|Boolean
name|notifyForOperationUndelete
parameter_list|)
block|{
name|this
operator|.
name|NotifyForOperationUndelete
operator|=
name|notifyForOperationUndelete
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|//CHECKSTYLE:ON
end_comment

end_unit

