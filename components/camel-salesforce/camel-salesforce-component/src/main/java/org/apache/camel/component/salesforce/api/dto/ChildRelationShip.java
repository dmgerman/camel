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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|ChildRelationShip
specifier|public
class|class
name|ChildRelationShip
extends|extends
name|AbstractDTOBase
block|{
DECL|field|field
specifier|private
name|String
name|field
decl_stmt|;
DECL|field|deprecatedAndHidden
specifier|private
name|Boolean
name|deprecatedAndHidden
decl_stmt|;
DECL|field|relationshipName
specifier|private
name|String
name|relationshipName
decl_stmt|;
DECL|field|cascadeDelete
specifier|private
name|Boolean
name|cascadeDelete
decl_stmt|;
DECL|field|restrictedDelete
specifier|private
name|Boolean
name|restrictedDelete
decl_stmt|;
DECL|field|childSObject
specifier|private
name|String
name|childSObject
decl_stmt|;
DECL|field|junctionIdListName
specifier|private
name|String
name|junctionIdListName
decl_stmt|;
DECL|field|junctionReferenceTo
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|junctionReferenceTo
decl_stmt|;
DECL|method|getField ()
specifier|public
name|String
name|getField
parameter_list|()
block|{
return|return
name|field
return|;
block|}
DECL|method|setField (String field)
specifier|public
name|void
name|setField
parameter_list|(
name|String
name|field
parameter_list|)
block|{
name|this
operator|.
name|field
operator|=
name|field
expr_stmt|;
block|}
DECL|method|isDeprecatedAndHidden ()
specifier|public
name|Boolean
name|isDeprecatedAndHidden
parameter_list|()
block|{
return|return
name|deprecatedAndHidden
return|;
block|}
DECL|method|setDeprecatedAndHidden (Boolean deprecatedAndHidden)
specifier|public
name|void
name|setDeprecatedAndHidden
parameter_list|(
name|Boolean
name|deprecatedAndHidden
parameter_list|)
block|{
name|this
operator|.
name|deprecatedAndHidden
operator|=
name|deprecatedAndHidden
expr_stmt|;
block|}
DECL|method|getRelationshipName ()
specifier|public
name|String
name|getRelationshipName
parameter_list|()
block|{
return|return
name|relationshipName
return|;
block|}
DECL|method|setRelationshipName (String relationshipName)
specifier|public
name|void
name|setRelationshipName
parameter_list|(
name|String
name|relationshipName
parameter_list|)
block|{
name|this
operator|.
name|relationshipName
operator|=
name|relationshipName
expr_stmt|;
block|}
DECL|method|isCascadeDelete ()
specifier|public
name|Boolean
name|isCascadeDelete
parameter_list|()
block|{
return|return
name|cascadeDelete
return|;
block|}
DECL|method|setCascadeDelete (Boolean cascadeDelete)
specifier|public
name|void
name|setCascadeDelete
parameter_list|(
name|Boolean
name|cascadeDelete
parameter_list|)
block|{
name|this
operator|.
name|cascadeDelete
operator|=
name|cascadeDelete
expr_stmt|;
block|}
DECL|method|isRestrictedDelete ()
specifier|public
name|Boolean
name|isRestrictedDelete
parameter_list|()
block|{
return|return
name|restrictedDelete
return|;
block|}
DECL|method|setRestrictedDelete (Boolean restrictedDelete)
specifier|public
name|void
name|setRestrictedDelete
parameter_list|(
name|Boolean
name|restrictedDelete
parameter_list|)
block|{
name|this
operator|.
name|restrictedDelete
operator|=
name|restrictedDelete
expr_stmt|;
block|}
DECL|method|getChildSObject ()
specifier|public
name|String
name|getChildSObject
parameter_list|()
block|{
return|return
name|childSObject
return|;
block|}
DECL|method|setChildSObject (String childSObject)
specifier|public
name|void
name|setChildSObject
parameter_list|(
name|String
name|childSObject
parameter_list|)
block|{
name|this
operator|.
name|childSObject
operator|=
name|childSObject
expr_stmt|;
block|}
DECL|method|getJunctionIdListName ()
specifier|public
name|String
name|getJunctionIdListName
parameter_list|()
block|{
return|return
name|junctionIdListName
return|;
block|}
DECL|method|setJunctionIdListName (String junctionIdListName)
specifier|public
name|void
name|setJunctionIdListName
parameter_list|(
name|String
name|junctionIdListName
parameter_list|)
block|{
name|this
operator|.
name|junctionIdListName
operator|=
name|junctionIdListName
expr_stmt|;
block|}
DECL|method|getJunctionReferenceTo ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getJunctionReferenceTo
parameter_list|()
block|{
return|return
name|junctionReferenceTo
return|;
block|}
DECL|method|setJunctionReferenceTo (List<String> junctionReferenceTo)
specifier|public
name|void
name|setJunctionReferenceTo
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|junctionReferenceTo
parameter_list|)
block|{
name|this
operator|.
name|junctionReferenceTo
operator|=
name|junctionReferenceTo
expr_stmt|;
block|}
block|}
end_class

end_unit

