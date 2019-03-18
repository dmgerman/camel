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
DECL|class|SObjectField
specifier|public
class|class
name|SObjectField
extends|extends
name|AbstractDTOBase
block|{
DECL|field|length
specifier|private
name|Integer
name|length
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|type
specifier|private
name|String
name|type
decl_stmt|;
DECL|field|defaultValue
specifier|private
name|String
name|defaultValue
decl_stmt|;
DECL|field|label
specifier|private
name|String
name|label
decl_stmt|;
DECL|field|updateable
specifier|private
name|Boolean
name|updateable
decl_stmt|;
DECL|field|calculated
specifier|private
name|Boolean
name|calculated
decl_stmt|;
DECL|field|caseSensitive
specifier|private
name|Boolean
name|caseSensitive
decl_stmt|;
DECL|field|controllerName
specifier|private
name|String
name|controllerName
decl_stmt|;
DECL|field|unique
specifier|private
name|Boolean
name|unique
decl_stmt|;
DECL|field|nillable
specifier|private
name|Boolean
name|nillable
decl_stmt|;
DECL|field|precision
specifier|private
name|Integer
name|precision
decl_stmt|;
DECL|field|scale
specifier|private
name|Integer
name|scale
decl_stmt|;
DECL|field|byteLength
specifier|private
name|Integer
name|byteLength
decl_stmt|;
DECL|field|nameField
specifier|private
name|Boolean
name|nameField
decl_stmt|;
DECL|field|sortable
specifier|private
name|Boolean
name|sortable
decl_stmt|;
DECL|field|filterable
specifier|private
name|Boolean
name|filterable
decl_stmt|;
DECL|field|writeRequiresMasterRead
specifier|private
name|Boolean
name|writeRequiresMasterRead
decl_stmt|;
DECL|field|externalId
specifier|private
name|Boolean
name|externalId
decl_stmt|;
DECL|field|idLookup
specifier|private
name|Boolean
name|idLookup
decl_stmt|;
DECL|field|inlineHelpText
specifier|private
name|String
name|inlineHelpText
decl_stmt|;
DECL|field|createable
specifier|private
name|Boolean
name|createable
decl_stmt|;
DECL|field|soapType
specifier|private
name|String
name|soapType
decl_stmt|;
DECL|field|autoNumber
specifier|private
name|Boolean
name|autoNumber
decl_stmt|;
DECL|field|restrictedPicklist
specifier|private
name|Boolean
name|restrictedPicklist
decl_stmt|;
DECL|field|namePointing
specifier|private
name|Boolean
name|namePointing
decl_stmt|;
DECL|field|custom
specifier|private
name|Boolean
name|custom
decl_stmt|;
DECL|field|defaultedOnCreate
specifier|private
name|Boolean
name|defaultedOnCreate
decl_stmt|;
DECL|field|deprecatedAndHidden
specifier|private
name|Boolean
name|deprecatedAndHidden
decl_stmt|;
DECL|field|htmlFormatted
specifier|private
name|Boolean
name|htmlFormatted
decl_stmt|;
DECL|field|defaultValueFormula
specifier|private
name|String
name|defaultValueFormula
decl_stmt|;
DECL|field|calculatedFormula
specifier|private
name|String
name|calculatedFormula
decl_stmt|;
annotation|@
name|XStreamImplicit
DECL|field|picklistValues
specifier|private
name|List
argument_list|<
name|PickListValue
argument_list|>
name|picklistValues
decl_stmt|;
DECL|field|dependentPicklist
specifier|private
name|Boolean
name|dependentPicklist
decl_stmt|;
annotation|@
name|XStreamImplicit
DECL|field|referenceTo
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|referenceTo
decl_stmt|;
DECL|field|relationshipName
specifier|private
name|String
name|relationshipName
decl_stmt|;
DECL|field|relationshipOrder
specifier|private
name|String
name|relationshipOrder
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
DECL|field|digits
specifier|private
name|String
name|digits
decl_stmt|;
DECL|field|groupable
specifier|private
name|Boolean
name|groupable
decl_stmt|;
DECL|field|permissionable
specifier|private
name|Boolean
name|permissionable
decl_stmt|;
DECL|field|displayLocationInDecimal
specifier|private
name|Boolean
name|displayLocationInDecimal
decl_stmt|;
DECL|field|extraTypeInfo
specifier|private
name|String
name|extraTypeInfo
decl_stmt|;
DECL|field|filteredLookupInfo
specifier|private
name|FilteredLookupInfo
name|filteredLookupInfo
decl_stmt|;
DECL|field|highScaleNumber
specifier|private
name|Boolean
name|highScaleNumber
decl_stmt|;
DECL|field|mask
specifier|private
name|String
name|mask
decl_stmt|;
DECL|field|maskType
specifier|private
name|String
name|maskType
decl_stmt|;
DECL|field|queryByDistance
specifier|private
name|Boolean
name|queryByDistance
decl_stmt|;
DECL|field|referenceTargetField
specifier|private
name|String
name|referenceTargetField
decl_stmt|;
DECL|field|encrypted
specifier|private
name|Boolean
name|encrypted
decl_stmt|;
DECL|method|getLength ()
specifier|public
name|Integer
name|getLength
parameter_list|()
block|{
return|return
name|length
return|;
block|}
DECL|method|setLength (Integer length)
specifier|public
name|void
name|setLength
parameter_list|(
name|Integer
name|length
parameter_list|)
block|{
name|this
operator|.
name|length
operator|=
name|length
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|setType (String type)
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getDefaultValue ()
specifier|public
name|String
name|getDefaultValue
parameter_list|()
block|{
return|return
name|defaultValue
return|;
block|}
DECL|method|setDefaultValue (String defaultValue)
specifier|public
name|void
name|setDefaultValue
parameter_list|(
name|String
name|defaultValue
parameter_list|)
block|{
name|this
operator|.
name|defaultValue
operator|=
name|defaultValue
expr_stmt|;
block|}
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
name|label
return|;
block|}
DECL|method|setLabel (String label)
specifier|public
name|void
name|setLabel
parameter_list|(
name|String
name|label
parameter_list|)
block|{
name|this
operator|.
name|label
operator|=
name|label
expr_stmt|;
block|}
DECL|method|isUpdateable ()
specifier|public
name|Boolean
name|isUpdateable
parameter_list|()
block|{
return|return
name|updateable
return|;
block|}
DECL|method|setUpdateable (Boolean updateable)
specifier|public
name|void
name|setUpdateable
parameter_list|(
name|Boolean
name|updateable
parameter_list|)
block|{
name|this
operator|.
name|updateable
operator|=
name|updateable
expr_stmt|;
block|}
DECL|method|isCalculated ()
specifier|public
name|Boolean
name|isCalculated
parameter_list|()
block|{
return|return
name|calculated
return|;
block|}
DECL|method|setCalculated (Boolean calculated)
specifier|public
name|void
name|setCalculated
parameter_list|(
name|Boolean
name|calculated
parameter_list|)
block|{
name|this
operator|.
name|calculated
operator|=
name|calculated
expr_stmt|;
block|}
DECL|method|isCaseSensitive ()
specifier|public
name|Boolean
name|isCaseSensitive
parameter_list|()
block|{
return|return
name|caseSensitive
return|;
block|}
DECL|method|setCaseSensitive (Boolean caseSensitive)
specifier|public
name|void
name|setCaseSensitive
parameter_list|(
name|Boolean
name|caseSensitive
parameter_list|)
block|{
name|this
operator|.
name|caseSensitive
operator|=
name|caseSensitive
expr_stmt|;
block|}
DECL|method|getControllerName ()
specifier|public
name|String
name|getControllerName
parameter_list|()
block|{
return|return
name|controllerName
return|;
block|}
DECL|method|setControllerName (String controllerName)
specifier|public
name|void
name|setControllerName
parameter_list|(
name|String
name|controllerName
parameter_list|)
block|{
name|this
operator|.
name|controllerName
operator|=
name|controllerName
expr_stmt|;
block|}
DECL|method|isUnique ()
specifier|public
name|Boolean
name|isUnique
parameter_list|()
block|{
return|return
name|unique
return|;
block|}
DECL|method|setUnique (Boolean unique)
specifier|public
name|void
name|setUnique
parameter_list|(
name|Boolean
name|unique
parameter_list|)
block|{
name|this
operator|.
name|unique
operator|=
name|unique
expr_stmt|;
block|}
DECL|method|isNillable ()
specifier|public
name|Boolean
name|isNillable
parameter_list|()
block|{
return|return
name|nillable
return|;
block|}
DECL|method|setNillable (Boolean nillable)
specifier|public
name|void
name|setNillable
parameter_list|(
name|Boolean
name|nillable
parameter_list|)
block|{
name|this
operator|.
name|nillable
operator|=
name|nillable
expr_stmt|;
block|}
DECL|method|getPrecision ()
specifier|public
name|Integer
name|getPrecision
parameter_list|()
block|{
return|return
name|precision
return|;
block|}
DECL|method|setPrecision (Integer precision)
specifier|public
name|void
name|setPrecision
parameter_list|(
name|Integer
name|precision
parameter_list|)
block|{
name|this
operator|.
name|precision
operator|=
name|precision
expr_stmt|;
block|}
DECL|method|getScale ()
specifier|public
name|Integer
name|getScale
parameter_list|()
block|{
return|return
name|scale
return|;
block|}
DECL|method|setScale (Integer scale)
specifier|public
name|void
name|setScale
parameter_list|(
name|Integer
name|scale
parameter_list|)
block|{
name|this
operator|.
name|scale
operator|=
name|scale
expr_stmt|;
block|}
DECL|method|getByteLength ()
specifier|public
name|Integer
name|getByteLength
parameter_list|()
block|{
return|return
name|byteLength
return|;
block|}
DECL|method|setByteLength (Integer byteLength)
specifier|public
name|void
name|setByteLength
parameter_list|(
name|Integer
name|byteLength
parameter_list|)
block|{
name|this
operator|.
name|byteLength
operator|=
name|byteLength
expr_stmt|;
block|}
DECL|method|isNameField ()
specifier|public
name|Boolean
name|isNameField
parameter_list|()
block|{
return|return
name|nameField
return|;
block|}
DECL|method|setNameField (Boolean nameField)
specifier|public
name|void
name|setNameField
parameter_list|(
name|Boolean
name|nameField
parameter_list|)
block|{
name|this
operator|.
name|nameField
operator|=
name|nameField
expr_stmt|;
block|}
DECL|method|isSortable ()
specifier|public
name|Boolean
name|isSortable
parameter_list|()
block|{
return|return
name|sortable
return|;
block|}
DECL|method|setSortable (Boolean sortable)
specifier|public
name|void
name|setSortable
parameter_list|(
name|Boolean
name|sortable
parameter_list|)
block|{
name|this
operator|.
name|sortable
operator|=
name|sortable
expr_stmt|;
block|}
DECL|method|isFilterable ()
specifier|public
name|Boolean
name|isFilterable
parameter_list|()
block|{
return|return
name|filterable
return|;
block|}
DECL|method|setFilterable (Boolean filterable)
specifier|public
name|void
name|setFilterable
parameter_list|(
name|Boolean
name|filterable
parameter_list|)
block|{
name|this
operator|.
name|filterable
operator|=
name|filterable
expr_stmt|;
block|}
DECL|method|isWriteRequiresMasterRead ()
specifier|public
name|Boolean
name|isWriteRequiresMasterRead
parameter_list|()
block|{
return|return
name|writeRequiresMasterRead
return|;
block|}
DECL|method|setWriteRequiresMasterRead (Boolean writeRequiresMasterRead)
specifier|public
name|void
name|setWriteRequiresMasterRead
parameter_list|(
name|Boolean
name|writeRequiresMasterRead
parameter_list|)
block|{
name|this
operator|.
name|writeRequiresMasterRead
operator|=
name|writeRequiresMasterRead
expr_stmt|;
block|}
DECL|method|isExternalId ()
specifier|public
name|Boolean
name|isExternalId
parameter_list|()
block|{
return|return
name|externalId
return|;
block|}
DECL|method|setExternalId (Boolean externalId)
specifier|public
name|void
name|setExternalId
parameter_list|(
name|Boolean
name|externalId
parameter_list|)
block|{
name|this
operator|.
name|externalId
operator|=
name|externalId
expr_stmt|;
block|}
DECL|method|isIdLookup ()
specifier|public
name|Boolean
name|isIdLookup
parameter_list|()
block|{
return|return
name|idLookup
return|;
block|}
DECL|method|setIdLookup (Boolean idLookup)
specifier|public
name|void
name|setIdLookup
parameter_list|(
name|Boolean
name|idLookup
parameter_list|)
block|{
name|this
operator|.
name|idLookup
operator|=
name|idLookup
expr_stmt|;
block|}
DECL|method|getInlineHelpText ()
specifier|public
name|String
name|getInlineHelpText
parameter_list|()
block|{
return|return
name|inlineHelpText
return|;
block|}
DECL|method|setInlineHelpText (String inlineHelpText)
specifier|public
name|void
name|setInlineHelpText
parameter_list|(
name|String
name|inlineHelpText
parameter_list|)
block|{
name|this
operator|.
name|inlineHelpText
operator|=
name|inlineHelpText
expr_stmt|;
block|}
DECL|method|isCreateable ()
specifier|public
name|Boolean
name|isCreateable
parameter_list|()
block|{
return|return
name|createable
return|;
block|}
DECL|method|setCreateable (Boolean createable)
specifier|public
name|void
name|setCreateable
parameter_list|(
name|Boolean
name|createable
parameter_list|)
block|{
name|this
operator|.
name|createable
operator|=
name|createable
expr_stmt|;
block|}
DECL|method|getSoapType ()
specifier|public
name|String
name|getSoapType
parameter_list|()
block|{
return|return
name|soapType
return|;
block|}
DECL|method|setSoapType (String soapType)
specifier|public
name|void
name|setSoapType
parameter_list|(
name|String
name|soapType
parameter_list|)
block|{
name|this
operator|.
name|soapType
operator|=
name|soapType
expr_stmt|;
block|}
DECL|method|isAutoNumber ()
specifier|public
name|Boolean
name|isAutoNumber
parameter_list|()
block|{
return|return
name|autoNumber
return|;
block|}
DECL|method|setAutoNumber (Boolean autoNumber)
specifier|public
name|void
name|setAutoNumber
parameter_list|(
name|Boolean
name|autoNumber
parameter_list|)
block|{
name|this
operator|.
name|autoNumber
operator|=
name|autoNumber
expr_stmt|;
block|}
DECL|method|isRestrictedPicklist ()
specifier|public
name|Boolean
name|isRestrictedPicklist
parameter_list|()
block|{
return|return
name|restrictedPicklist
return|;
block|}
DECL|method|setRestrictedPicklist (Boolean restrictedPicklist)
specifier|public
name|void
name|setRestrictedPicklist
parameter_list|(
name|Boolean
name|restrictedPicklist
parameter_list|)
block|{
name|this
operator|.
name|restrictedPicklist
operator|=
name|restrictedPicklist
expr_stmt|;
block|}
DECL|method|isNamePointing ()
specifier|public
name|Boolean
name|isNamePointing
parameter_list|()
block|{
return|return
name|namePointing
return|;
block|}
DECL|method|setNamePointing (Boolean namePointing)
specifier|public
name|void
name|setNamePointing
parameter_list|(
name|Boolean
name|namePointing
parameter_list|)
block|{
name|this
operator|.
name|namePointing
operator|=
name|namePointing
expr_stmt|;
block|}
DECL|method|isCustom ()
specifier|public
name|Boolean
name|isCustom
parameter_list|()
block|{
return|return
name|custom
return|;
block|}
DECL|method|setCustom (Boolean custom)
specifier|public
name|void
name|setCustom
parameter_list|(
name|Boolean
name|custom
parameter_list|)
block|{
name|this
operator|.
name|custom
operator|=
name|custom
expr_stmt|;
block|}
DECL|method|isDefaultedOnCreate ()
specifier|public
name|Boolean
name|isDefaultedOnCreate
parameter_list|()
block|{
return|return
name|defaultedOnCreate
return|;
block|}
DECL|method|setDefaultedOnCreate (Boolean defaultedOnCreate)
specifier|public
name|void
name|setDefaultedOnCreate
parameter_list|(
name|Boolean
name|defaultedOnCreate
parameter_list|)
block|{
name|this
operator|.
name|defaultedOnCreate
operator|=
name|defaultedOnCreate
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
DECL|method|isHtmlFormatted ()
specifier|public
name|Boolean
name|isHtmlFormatted
parameter_list|()
block|{
return|return
name|htmlFormatted
return|;
block|}
DECL|method|setHtmlFormatted (Boolean htmlFormatted)
specifier|public
name|void
name|setHtmlFormatted
parameter_list|(
name|Boolean
name|htmlFormatted
parameter_list|)
block|{
name|this
operator|.
name|htmlFormatted
operator|=
name|htmlFormatted
expr_stmt|;
block|}
DECL|method|getDefaultValueFormula ()
specifier|public
name|String
name|getDefaultValueFormula
parameter_list|()
block|{
return|return
name|defaultValueFormula
return|;
block|}
DECL|method|setDefaultValueFormula (String defaultValueFormula)
specifier|public
name|void
name|setDefaultValueFormula
parameter_list|(
name|String
name|defaultValueFormula
parameter_list|)
block|{
name|this
operator|.
name|defaultValueFormula
operator|=
name|defaultValueFormula
expr_stmt|;
block|}
DECL|method|getCalculatedFormula ()
specifier|public
name|String
name|getCalculatedFormula
parameter_list|()
block|{
return|return
name|calculatedFormula
return|;
block|}
DECL|method|setCalculatedFormula (String calculatedFormula)
specifier|public
name|void
name|setCalculatedFormula
parameter_list|(
name|String
name|calculatedFormula
parameter_list|)
block|{
name|this
operator|.
name|calculatedFormula
operator|=
name|calculatedFormula
expr_stmt|;
block|}
DECL|method|getPicklistValues ()
specifier|public
name|List
argument_list|<
name|PickListValue
argument_list|>
name|getPicklistValues
parameter_list|()
block|{
return|return
name|picklistValues
return|;
block|}
DECL|method|setPicklistValues (List<PickListValue> picklistValues)
specifier|public
name|void
name|setPicklistValues
parameter_list|(
name|List
argument_list|<
name|PickListValue
argument_list|>
name|picklistValues
parameter_list|)
block|{
name|this
operator|.
name|picklistValues
operator|=
name|picklistValues
expr_stmt|;
block|}
DECL|method|isDependentPicklist ()
specifier|public
name|Boolean
name|isDependentPicklist
parameter_list|()
block|{
return|return
name|dependentPicklist
return|;
block|}
DECL|method|setDependentPicklist (Boolean dependentPicklist)
specifier|public
name|void
name|setDependentPicklist
parameter_list|(
name|Boolean
name|dependentPicklist
parameter_list|)
block|{
name|this
operator|.
name|dependentPicklist
operator|=
name|dependentPicklist
expr_stmt|;
block|}
DECL|method|getReferenceTo ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getReferenceTo
parameter_list|()
block|{
return|return
name|referenceTo
return|;
block|}
DECL|method|setReferenceTo (List<String> referenceTo)
specifier|public
name|void
name|setReferenceTo
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|referenceTo
parameter_list|)
block|{
name|this
operator|.
name|referenceTo
operator|=
name|referenceTo
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
DECL|method|getRelationshipOrder ()
specifier|public
name|String
name|getRelationshipOrder
parameter_list|()
block|{
return|return
name|relationshipOrder
return|;
block|}
DECL|method|setRelationshipOrder (String relationshipOrder)
specifier|public
name|void
name|setRelationshipOrder
parameter_list|(
name|String
name|relationshipOrder
parameter_list|)
block|{
name|this
operator|.
name|relationshipOrder
operator|=
name|relationshipOrder
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
DECL|method|getDigits ()
specifier|public
name|String
name|getDigits
parameter_list|()
block|{
return|return
name|digits
return|;
block|}
DECL|method|setDigits (String digits)
specifier|public
name|void
name|setDigits
parameter_list|(
name|String
name|digits
parameter_list|)
block|{
name|this
operator|.
name|digits
operator|=
name|digits
expr_stmt|;
block|}
DECL|method|isGroupable ()
specifier|public
name|Boolean
name|isGroupable
parameter_list|()
block|{
return|return
name|groupable
return|;
block|}
DECL|method|setGroupable (Boolean groupable)
specifier|public
name|void
name|setGroupable
parameter_list|(
name|Boolean
name|groupable
parameter_list|)
block|{
name|this
operator|.
name|groupable
operator|=
name|groupable
expr_stmt|;
block|}
DECL|method|isPermissionable ()
specifier|public
name|Boolean
name|isPermissionable
parameter_list|()
block|{
return|return
name|permissionable
return|;
block|}
DECL|method|setPermissionable (Boolean permissionable)
specifier|public
name|void
name|setPermissionable
parameter_list|(
name|Boolean
name|permissionable
parameter_list|)
block|{
name|this
operator|.
name|permissionable
operator|=
name|permissionable
expr_stmt|;
block|}
DECL|method|isDisplayLocationInDecimal ()
specifier|public
name|Boolean
name|isDisplayLocationInDecimal
parameter_list|()
block|{
return|return
name|displayLocationInDecimal
return|;
block|}
DECL|method|setDisplayLocationInDecimal (Boolean displayLocationInDecimal)
specifier|public
name|void
name|setDisplayLocationInDecimal
parameter_list|(
name|Boolean
name|displayLocationInDecimal
parameter_list|)
block|{
name|this
operator|.
name|displayLocationInDecimal
operator|=
name|displayLocationInDecimal
expr_stmt|;
block|}
DECL|method|getExtraTypeInfo ()
specifier|public
name|String
name|getExtraTypeInfo
parameter_list|()
block|{
return|return
name|extraTypeInfo
return|;
block|}
DECL|method|setExtraTypeInfo (String extraTypeInfo)
specifier|public
name|void
name|setExtraTypeInfo
parameter_list|(
name|String
name|extraTypeInfo
parameter_list|)
block|{
name|this
operator|.
name|extraTypeInfo
operator|=
name|extraTypeInfo
expr_stmt|;
block|}
DECL|method|getFilteredLookupInfo ()
specifier|public
name|FilteredLookupInfo
name|getFilteredLookupInfo
parameter_list|()
block|{
return|return
name|filteredLookupInfo
return|;
block|}
DECL|method|setFilteredLookupInfo (FilteredLookupInfo filteredLookupInfo)
specifier|public
name|void
name|setFilteredLookupInfo
parameter_list|(
name|FilteredLookupInfo
name|filteredLookupInfo
parameter_list|)
block|{
name|this
operator|.
name|filteredLookupInfo
operator|=
name|filteredLookupInfo
expr_stmt|;
block|}
DECL|method|getHighScaleNumber ()
specifier|public
name|Boolean
name|getHighScaleNumber
parameter_list|()
block|{
return|return
name|highScaleNumber
return|;
block|}
DECL|method|setHighScaleNumber (Boolean highScaleNumber)
specifier|public
name|void
name|setHighScaleNumber
parameter_list|(
name|Boolean
name|highScaleNumber
parameter_list|)
block|{
name|this
operator|.
name|highScaleNumber
operator|=
name|highScaleNumber
expr_stmt|;
block|}
DECL|method|getMask ()
specifier|public
name|String
name|getMask
parameter_list|()
block|{
return|return
name|mask
return|;
block|}
DECL|method|setMask (String mask)
specifier|public
name|void
name|setMask
parameter_list|(
name|String
name|mask
parameter_list|)
block|{
name|this
operator|.
name|mask
operator|=
name|mask
expr_stmt|;
block|}
DECL|method|getMaskType ()
specifier|public
name|String
name|getMaskType
parameter_list|()
block|{
return|return
name|maskType
return|;
block|}
DECL|method|setMaskType (String maskType)
specifier|public
name|void
name|setMaskType
parameter_list|(
name|String
name|maskType
parameter_list|)
block|{
name|this
operator|.
name|maskType
operator|=
name|maskType
expr_stmt|;
block|}
DECL|method|getQueryByDistance ()
specifier|public
name|Boolean
name|getQueryByDistance
parameter_list|()
block|{
return|return
name|queryByDistance
return|;
block|}
DECL|method|setQueryByDistance (Boolean queryByDistance)
specifier|public
name|void
name|setQueryByDistance
parameter_list|(
name|Boolean
name|queryByDistance
parameter_list|)
block|{
name|this
operator|.
name|queryByDistance
operator|=
name|queryByDistance
expr_stmt|;
block|}
DECL|method|getReferenceTargetField ()
specifier|public
name|String
name|getReferenceTargetField
parameter_list|()
block|{
return|return
name|referenceTargetField
return|;
block|}
DECL|method|setReferenceTargetField (String referenceTargetField)
specifier|public
name|void
name|setReferenceTargetField
parameter_list|(
name|String
name|referenceTargetField
parameter_list|)
block|{
name|this
operator|.
name|referenceTargetField
operator|=
name|referenceTargetField
expr_stmt|;
block|}
DECL|method|getEncrypted ()
specifier|public
name|Boolean
name|getEncrypted
parameter_list|()
block|{
return|return
name|encrypted
return|;
block|}
DECL|method|setEncrypted (Boolean encrypted)
specifier|public
name|void
name|setEncrypted
parameter_list|(
name|Boolean
name|encrypted
parameter_list|)
block|{
name|this
operator|.
name|encrypted
operator|=
name|encrypted
expr_stmt|;
block|}
block|}
end_class

end_unit

