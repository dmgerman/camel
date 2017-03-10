begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Salesforce DTO generated by camel-salesforce-maven-plugin  * Generated on: Thu Mar 09 16:15:49 ART 2017  */
end_comment

begin_package
DECL|package|$packageName
package|package
name|$packageName
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|AbstractDescribedSObjectBase
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
name|ChildRelationShip
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
name|InfoUrls
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
name|NamedLayoutInfo
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
name|RecordTypeInfo
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
name|SObjectDescription
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
name|SObjectDescriptionUrls
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
name|SObjectField
import|;
end_import

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

begin_comment
comment|/**  * Salesforce DTO for SObject Case  */
end_comment

begin_class
annotation|@
name|XStreamAlias
argument_list|(
literal|"Case"
argument_list|)
DECL|class|Case
specifier|public
class|class
name|Case
extends|extends
name|AbstractDescribedSObjectBase
block|{
DECL|field|DESCRIPTION
specifier|private
specifier|static
specifier|final
name|SObjectDescription
name|DESCRIPTION
init|=
name|createSObjectDescription
argument_list|()
decl_stmt|;
comment|// PickListAccentMark
annotation|@
name|XStreamConverter
argument_list|(
name|PicklistEnumConverter
operator|.
name|class
argument_list|)
DECL|field|PickListAccentMark
specifier|private
name|Case_PickListAccentMarkEnum
name|PickListAccentMark
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"PickListAccentMark"
argument_list|)
DECL|method|getPickListAccentMark ()
specifier|public
name|Case_PickListAccentMarkEnum
name|getPickListAccentMark
parameter_list|()
block|{
return|return
name|this
operator|.
name|PickListAccentMark
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"PickListAccentMark"
argument_list|)
DECL|method|setPickListAccentMark (Case_PickListAccentMarkEnum PickListAccentMark)
specifier|public
name|void
name|setPickListAccentMark
parameter_list|(
name|Case_PickListAccentMarkEnum
name|PickListAccentMark
parameter_list|)
block|{
name|this
operator|.
name|PickListAccentMark
operator|=
name|PickListAccentMark
expr_stmt|;
block|}
comment|// PickListSlash
annotation|@
name|XStreamConverter
argument_list|(
name|PicklistEnumConverter
operator|.
name|class
argument_list|)
DECL|field|PickListSlash
specifier|private
name|Case_PickListSlashEnum
name|PickListSlash
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"PickListSlash"
argument_list|)
DECL|method|getPickListSlash ()
specifier|public
name|Case_PickListSlashEnum
name|getPickListSlash
parameter_list|()
block|{
return|return
name|this
operator|.
name|PickListSlash
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"PickListSlash"
argument_list|)
DECL|method|setPickListSlash (Case_PickListSlashEnum PickListSlash)
specifier|public
name|void
name|setPickListSlash
parameter_list|(
name|Case_PickListSlashEnum
name|PickListSlash
parameter_list|)
block|{
name|this
operator|.
name|PickListSlash
operator|=
name|PickListSlash
expr_stmt|;
block|}
comment|// PickListQuotationMark
annotation|@
name|XStreamConverter
argument_list|(
name|PicklistEnumConverter
operator|.
name|class
argument_list|)
DECL|field|PickListQuotationMark
specifier|private
name|Case_PickListQuotationMarkEnum
name|PickListQuotationMark
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"PickListQuotationMark"
argument_list|)
DECL|method|getPickListQuotationMark ()
specifier|public
name|Case_PickListQuotationMarkEnum
name|getPickListQuotationMark
parameter_list|()
block|{
return|return
name|this
operator|.
name|PickListQuotationMark
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"PickListQuotationMark"
argument_list|)
DECL|method|setPickListQuotationMark (Case_PickListQuotationMarkEnum PickListQuotationMark)
specifier|public
name|void
name|setPickListQuotationMark
parameter_list|(
name|Case_PickListQuotationMarkEnum
name|PickListQuotationMark
parameter_list|)
block|{
name|this
operator|.
name|PickListQuotationMark
operator|=
name|PickListQuotationMark
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|description ()
specifier|public
specifier|final
name|SObjectDescription
name|description
parameter_list|()
block|{
return|return
name|DESCRIPTION
return|;
block|}
DECL|method|createSObjectDescription ()
specifier|private
specifier|static
name|SObjectDescription
name|createSObjectDescription
parameter_list|()
block|{
specifier|final
name|SObjectDescription
name|description
init|=
operator|new
name|SObjectDescription
argument_list|()
decl_stmt|;
name|description
operator|.
name|setMergeable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|description
operator|.
name|setUndeletable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|description
operator|.
name|setCreateable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|description
operator|.
name|setSearchLayoutable
argument_list|(
literal|"true"
argument_list|)
expr_stmt|;
name|description
operator|.
name|setQueryable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|description
operator|.
name|setLabel
argument_list|(
literal|"Caso"
argument_list|)
expr_stmt|;
name|description
operator|.
name|setTriggerable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|description
operator|.
name|setReplicateable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|description
operator|.
name|setCustom
argument_list|(
literal|false
argument_list|)
expr_stmt|;
specifier|final
name|SObjectDescriptionUrls
name|sObjectDescriptionUrls1
init|=
operator|new
name|SObjectDescriptionUrls
argument_list|()
decl_stmt|;
name|sObjectDescriptionUrls1
operator|.
name|setDescribe
argument_list|(
literal|"/services/data/v34.0/sobjects/Case/describe"
argument_list|)
expr_stmt|;
name|sObjectDescriptionUrls1
operator|.
name|setCaseArticleSuggestions
argument_list|(
literal|"/services/data/v34.0/sobjects/Case/suggestedArticles"
argument_list|)
expr_stmt|;
name|sObjectDescriptionUrls1
operator|.
name|setLayouts
argument_list|(
literal|"/services/data/v34.0/sobjects/Case/describe/layouts"
argument_list|)
expr_stmt|;
name|sObjectDescriptionUrls1
operator|.
name|setSobject
argument_list|(
literal|"/services/data/v34.0/sobjects/Case"
argument_list|)
expr_stmt|;
name|sObjectDescriptionUrls1
operator|.
name|setUiEditTemplate
argument_list|(
literal|"https://salesforce-host/{ID}/e"
argument_list|)
expr_stmt|;
name|sObjectDescriptionUrls1
operator|.
name|setQuickActions
argument_list|(
literal|"/services/data/v34.0/sobjects/Case/quickActions"
argument_list|)
expr_stmt|;
name|sObjectDescriptionUrls1
operator|.
name|setCaseRowArticleSuggestions
argument_list|(
literal|"/services/data/v34.0/sobjects/Case/{ID}/suggestedArticles"
argument_list|)
expr_stmt|;
name|sObjectDescriptionUrls1
operator|.
name|setRowTemplate
argument_list|(
literal|"/services/data/v34.0/sobjects/Case/{ID}"
argument_list|)
expr_stmt|;
name|sObjectDescriptionUrls1
operator|.
name|setListviews
argument_list|(
literal|"/services/data/v34.0/sobjects/Case/listviews"
argument_list|)
expr_stmt|;
name|sObjectDescriptionUrls1
operator|.
name|setCompactLayouts
argument_list|(
literal|"/services/data/v34.0/sobjects/Case/describe/compactLayouts"
argument_list|)
expr_stmt|;
name|sObjectDescriptionUrls1
operator|.
name|setApprovalLayouts
argument_list|(
literal|"/services/data/v34.0/sobjects/Case/describe/approvalLayouts"
argument_list|)
expr_stmt|;
name|sObjectDescriptionUrls1
operator|.
name|setUiNewRecord
argument_list|(
literal|"https://salesforce-host/500/e"
argument_list|)
expr_stmt|;
name|sObjectDescriptionUrls1
operator|.
name|setUiDetailTemplate
argument_list|(
literal|"https://salesforce-host/{ID}"
argument_list|)
expr_stmt|;
name|description
operator|.
name|setUrls
argument_list|(
name|sObjectDescriptionUrls1
argument_list|)
expr_stmt|;
name|description
operator|.
name|setName
argument_list|(
literal|"Case"
argument_list|)
expr_stmt|;
name|description
operator|.
name|setLayoutable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|description
operator|.
name|setCompactLayoutable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|description
operator|.
name|setDeprecatedAndHidden
argument_list|(
literal|false
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|SObjectField
argument_list|>
name|fields1
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|description
operator|.
name|setFields
argument_list|(
name|fields1
argument_list|)
expr_stmt|;
specifier|final
name|SObjectField
name|sObjectField1
init|=
operator|new
name|SObjectField
argument_list|()
decl_stmt|;
name|fields1
operator|.
name|add
argument_list|(
name|sObjectField1
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setWriteRequiresMasterRead
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setNillable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setCreateable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setEncrypted
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setDigits
argument_list|(
literal|"0"
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setDependentPicklist
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setInlineHelpText
argument_list|(
literal|"This is a picklist accent mark test \u00F3"
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setLabel
argument_list|(
literal|"Accent Mark"
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setHighScaleNumber
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setDisplayLocationInDecimal
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setName
argument_list|(
literal|"PickListAccentMark"
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setHtmlFormatted
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setDeprecatedAndHidden
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setRestrictedPicklist
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setNameField
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setCaseSensitive
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setPermissionable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setCascadeDelete
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setDefaultedOnCreate
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setExternalId
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setSoapType
argument_list|(
literal|"xsd:string"
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setGroupable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setCustom
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setScale
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setCalculated
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setRestrictedDelete
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setNamePointing
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setIdLookup
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setType
argument_list|(
literal|"picklist"
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setSortable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setLength
argument_list|(
literal|40
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setPrecision
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setByteLength
argument_list|(
literal|120
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setQueryByDistance
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setFilterable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setUpdateable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setUnique
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField1
operator|.
name|setAutoNumber
argument_list|(
literal|false
argument_list|)
expr_stmt|;
specifier|final
name|SObjectField
name|sObjectField2
init|=
operator|new
name|SObjectField
argument_list|()
decl_stmt|;
name|fields1
operator|.
name|add
argument_list|(
name|sObjectField2
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setWriteRequiresMasterRead
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setNillable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setCreateable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setEncrypted
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setDigits
argument_list|(
literal|"0"
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setDependentPicklist
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setInlineHelpText
argument_list|(
literal|"This is a picklist slash test /"
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setLabel
argument_list|(
literal|"Slash"
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setHighScaleNumber
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setDisplayLocationInDecimal
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setName
argument_list|(
literal|"PickListSlash"
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setHtmlFormatted
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setDeprecatedAndHidden
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setRestrictedPicklist
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setNameField
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setCaseSensitive
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setPermissionable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setCascadeDelete
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setDefaultedOnCreate
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setExternalId
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setSoapType
argument_list|(
literal|"xsd:string"
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setGroupable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setCustom
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setScale
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setCalculated
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setRestrictedDelete
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setNamePointing
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setIdLookup
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setType
argument_list|(
literal|"picklist"
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setSortable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setLength
argument_list|(
literal|255
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setPrecision
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setByteLength
argument_list|(
literal|765
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setQueryByDistance
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setFilterable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setUpdateable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setUnique
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField2
operator|.
name|setAutoNumber
argument_list|(
literal|false
argument_list|)
expr_stmt|;
specifier|final
name|SObjectField
name|sObjectField3
init|=
operator|new
name|SObjectField
argument_list|()
decl_stmt|;
name|fields1
operator|.
name|add
argument_list|(
name|sObjectField3
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setWriteRequiresMasterRead
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setNillable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setCreateable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setEncrypted
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setDigits
argument_list|(
literal|"0"
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setDependentPicklist
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setInlineHelpText
argument_list|(
literal|"This is a picklist quotation mark test \""
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setLabel
argument_list|(
literal|"QuotationMark"
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setHighScaleNumber
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setDisplayLocationInDecimal
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setName
argument_list|(
literal|"PickListQuotationMark"
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setHtmlFormatted
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setDeprecatedAndHidden
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setRestrictedPicklist
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setNameField
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setCaseSensitive
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setPermissionable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setCascadeDelete
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setDefaultedOnCreate
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setExternalId
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setSoapType
argument_list|(
literal|"xsd:string"
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setGroupable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setCustom
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setScale
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setCalculated
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setRestrictedDelete
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setNamePointing
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setIdLookup
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setType
argument_list|(
literal|"picklist"
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setSortable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setLength
argument_list|(
literal|255
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setPrecision
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setByteLength
argument_list|(
literal|765
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setQueryByDistance
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setFilterable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setUpdateable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setUnique
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sObjectField3
operator|.
name|setAutoNumber
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|description
operator|.
name|setActivateable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|description
operator|.
name|setLabelPlural
argument_list|(
literal|"Casos"
argument_list|)
expr_stmt|;
name|description
operator|.
name|setDeletable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|description
operator|.
name|setFeedEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|description
operator|.
name|setSearchable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|description
operator|.
name|setCustomSetting
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|description
operator|.
name|setRetrieveable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|description
operator|.
name|setKeyPrefix
argument_list|(
literal|"500"
argument_list|)
expr_stmt|;
return|return
name|description
return|;
block|}
block|}
end_class

end_unit

