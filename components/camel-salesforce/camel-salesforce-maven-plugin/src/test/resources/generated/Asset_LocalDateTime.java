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
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
comment|/**  * Salesforce DTO for SObject Asset  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.CamelSalesforceMojo"
argument_list|)
annotation|@
name|XStreamAlias
argument_list|(
literal|"Asset"
argument_list|)
DECL|class|Asset
specifier|public
class|class
name|Asset
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
comment|// InstallDate
DECL|field|InstallDate
specifier|private
name|java
operator|.
name|time
operator|.
name|LocalDateTime
name|InstallDate
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"InstallDate"
argument_list|)
DECL|method|getInstallDate ()
specifier|public
name|java
operator|.
name|time
operator|.
name|LocalDateTime
name|getInstallDate
parameter_list|()
block|{
return|return
name|this
operator|.
name|InstallDate
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"InstallDate"
argument_list|)
DECL|method|setInstallDate (java.time.LocalDateTime InstallDate)
specifier|public
name|void
name|setInstallDate
parameter_list|(
name|java
operator|.
name|time
operator|.
name|LocalDateTime
name|InstallDate
parameter_list|)
block|{
name|this
operator|.
name|InstallDate
operator|=
name|InstallDate
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
name|setCreateable
argument_list|(
literal|true
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
literal|"Asset"
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
name|setName
argument_list|(
literal|"Asset"
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
name|setDeprecatedAndHidden
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|description
operator|.
name|setMruEnabled
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
name|setFeedEnabled
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
name|setCustomSetting
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|description
operator|.
name|setKeyPrefix
argument_list|(
literal|"02i"
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
name|setSearchLayoutable
argument_list|(
literal|"true"
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
literal|"/services/data/v42.0/sobjects/Asset/describe"
argument_list|)
expr_stmt|;
name|sObjectDescriptionUrls1
operator|.
name|setLayouts
argument_list|(
literal|"/services/data/v42.0/sobjects/Asset/describe/layouts"
argument_list|)
expr_stmt|;
name|sObjectDescriptionUrls1
operator|.
name|setSobject
argument_list|(
literal|"/services/data/v42.0/sobjects/Asset"
argument_list|)
expr_stmt|;
name|sObjectDescriptionUrls1
operator|.
name|setQuickActions
argument_list|(
literal|"/services/data/v42.0/sobjects/Asset/quickActions"
argument_list|)
expr_stmt|;
name|sObjectDescriptionUrls1
operator|.
name|setUiEditTemplate
argument_list|(
literal|"https://eu11.salesforce.com/{ID}/e"
argument_list|)
expr_stmt|;
name|sObjectDescriptionUrls1
operator|.
name|setDefaultValues
argument_list|(
literal|"/services/data/v42.0/sobjects/Asset/defaultValues?recordTypeId&fields"
argument_list|)
expr_stmt|;
name|sObjectDescriptionUrls1
operator|.
name|setRowTemplate
argument_list|(
literal|"/services/data/v42.0/sobjects/Asset/{ID}"
argument_list|)
expr_stmt|;
name|sObjectDescriptionUrls1
operator|.
name|setListviews
argument_list|(
literal|"/services/data/v42.0/sobjects/Asset/listviews"
argument_list|)
expr_stmt|;
name|sObjectDescriptionUrls1
operator|.
name|setCompactLayouts
argument_list|(
literal|"/services/data/v42.0/sobjects/Asset/describe/compactLayouts"
argument_list|)
expr_stmt|;
name|sObjectDescriptionUrls1
operator|.
name|setApprovalLayouts
argument_list|(
literal|"/services/data/v42.0/sobjects/Asset/describe/approvalLayouts"
argument_list|)
expr_stmt|;
name|sObjectDescriptionUrls1
operator|.
name|setUiNewRecord
argument_list|(
literal|"https://eu11.salesforce.com/02i/e"
argument_list|)
expr_stmt|;
name|sObjectDescriptionUrls1
operator|.
name|setUiDetailTemplate
argument_list|(
literal|"https://eu11.salesforce.com/{ID}"
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
name|setCompactLayoutable
argument_list|(
literal|true
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
name|createField
argument_list|(
literal|"Id"
argument_list|,
literal|"Asset ID"
argument_list|,
literal|"id"
argument_list|,
literal|"tns:ID"
argument_list|,
literal|18
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|fields1
operator|.
name|add
argument_list|(
name|sObjectField1
argument_list|)
expr_stmt|;
specifier|final
name|SObjectField
name|sObjectField2
init|=
name|createField
argument_list|(
literal|"InstallDate"
argument_list|,
literal|"Install Date"
argument_list|,
literal|"date"
argument_list|,
literal|"xsd:date"
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|fields1
operator|.
name|add
argument_list|(
name|sObjectField2
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
literal|"Assets"
argument_list|)
expr_stmt|;
name|description
operator|.
name|setUpdateable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|description
operator|.
name|setDeletable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|description
return|;
block|}
block|}
end_class

end_unit

