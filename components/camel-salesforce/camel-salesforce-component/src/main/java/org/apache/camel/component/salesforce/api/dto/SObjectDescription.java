begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|XStreamImplicit
import|;
end_import

begin_class
DECL|class|SObjectDescription
specifier|public
class|class
name|SObjectDescription
extends|extends
name|SObject
block|{
annotation|@
name|XStreamImplicit
DECL|field|actionOverrides
specifier|private
name|List
argument_list|<
name|ActionOverride
argument_list|>
name|actionOverrides
decl_stmt|;
annotation|@
name|XStreamImplicit
DECL|field|fields
specifier|private
name|List
argument_list|<
name|SObjectField
argument_list|>
name|fields
decl_stmt|;
DECL|field|urls
specifier|private
name|SObjectDescriptionUrls
name|urls
decl_stmt|;
annotation|@
name|XStreamImplicit
DECL|field|childRelationships
specifier|private
name|List
argument_list|<
name|ChildRelationShip
argument_list|>
name|childRelationships
decl_stmt|;
annotation|@
name|XStreamImplicit
DECL|field|recordTypeInfos
specifier|private
name|List
argument_list|<
name|RecordTypeInfo
argument_list|>
name|recordTypeInfos
decl_stmt|;
annotation|@
name|XStreamImplicit
DECL|field|namedLayoutInfos
specifier|private
name|List
argument_list|<
name|NamedLayoutInfo
argument_list|>
name|namedLayoutInfos
decl_stmt|;
DECL|method|getActionOverrides ()
specifier|public
name|List
argument_list|<
name|ActionOverride
argument_list|>
name|getActionOverrides
parameter_list|()
block|{
return|return
name|actionOverrides
return|;
block|}
DECL|method|setActionOverrides (List<ActionOverride> actionOverrides)
specifier|public
name|void
name|setActionOverrides
parameter_list|(
name|List
argument_list|<
name|ActionOverride
argument_list|>
name|actionOverrides
parameter_list|)
block|{
name|this
operator|.
name|actionOverrides
operator|=
name|actionOverrides
expr_stmt|;
block|}
DECL|method|getFields ()
specifier|public
name|List
argument_list|<
name|SObjectField
argument_list|>
name|getFields
parameter_list|()
block|{
return|return
name|fields
return|;
block|}
DECL|method|setFields (List<SObjectField> fields)
specifier|public
name|void
name|setFields
parameter_list|(
name|List
argument_list|<
name|SObjectField
argument_list|>
name|fields
parameter_list|)
block|{
name|this
operator|.
name|fields
operator|=
name|fields
expr_stmt|;
block|}
DECL|method|getUrls ()
specifier|public
name|SObjectDescriptionUrls
name|getUrls
parameter_list|()
block|{
return|return
name|urls
return|;
block|}
DECL|method|setUrls (SObjectDescriptionUrls urls)
specifier|public
name|void
name|setUrls
parameter_list|(
name|SObjectDescriptionUrls
name|urls
parameter_list|)
block|{
name|this
operator|.
name|urls
operator|=
name|urls
expr_stmt|;
block|}
DECL|method|getChildRelationships ()
specifier|public
name|List
argument_list|<
name|ChildRelationShip
argument_list|>
name|getChildRelationships
parameter_list|()
block|{
return|return
name|childRelationships
return|;
block|}
DECL|method|setChildRelationships (List<ChildRelationShip> childRelationships)
specifier|public
name|void
name|setChildRelationships
parameter_list|(
name|List
argument_list|<
name|ChildRelationShip
argument_list|>
name|childRelationships
parameter_list|)
block|{
name|this
operator|.
name|childRelationships
operator|=
name|childRelationships
expr_stmt|;
block|}
DECL|method|getRecordTypeInfos ()
specifier|public
name|List
argument_list|<
name|RecordTypeInfo
argument_list|>
name|getRecordTypeInfos
parameter_list|()
block|{
return|return
name|recordTypeInfos
return|;
block|}
DECL|method|setRecordTypeInfos (List<RecordTypeInfo> recordTypeInfos)
specifier|public
name|void
name|setRecordTypeInfos
parameter_list|(
name|List
argument_list|<
name|RecordTypeInfo
argument_list|>
name|recordTypeInfos
parameter_list|)
block|{
name|this
operator|.
name|recordTypeInfos
operator|=
name|recordTypeInfos
expr_stmt|;
block|}
DECL|method|getNamedLayoutInfos ()
specifier|public
name|List
argument_list|<
name|NamedLayoutInfo
argument_list|>
name|getNamedLayoutInfos
parameter_list|()
block|{
return|return
name|namedLayoutInfos
return|;
block|}
DECL|method|setNamedLayoutInfos (List<NamedLayoutInfo> namedLayoutInfos)
specifier|public
name|void
name|setNamedLayoutInfos
parameter_list|(
name|List
argument_list|<
name|NamedLayoutInfo
argument_list|>
name|namedLayoutInfos
parameter_list|)
block|{
name|this
operator|.
name|namedLayoutInfos
operator|=
name|namedLayoutInfos
expr_stmt|;
block|}
comment|/**      * Removes some of the less used properties from this object. Useful to reduce serialized form or for code      * generation that relies on reflection.      */
DECL|method|prune ()
specifier|public
name|SObjectDescription
name|prune
parameter_list|()
block|{
specifier|final
name|SObjectDescription
name|pruned
init|=
operator|new
name|SObjectDescription
argument_list|()
decl_stmt|;
name|pruned
operator|.
name|setName
argument_list|(
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|pruned
operator|.
name|setLabel
argument_list|(
name|getLabel
argument_list|()
argument_list|)
expr_stmt|;
name|pruned
operator|.
name|setLabelPlural
argument_list|(
name|getLabelPlural
argument_list|()
argument_list|)
expr_stmt|;
name|pruned
operator|.
name|fields
operator|=
name|fields
expr_stmt|;
name|pruned
operator|.
name|urls
operator|=
name|urls
expr_stmt|;
return|return
name|pruned
return|;
block|}
block|}
end_class

end_unit

