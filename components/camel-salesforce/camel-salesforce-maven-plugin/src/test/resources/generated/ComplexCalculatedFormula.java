begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Salesforce DTO generated by camel-salesforce-maven-plugin.  */
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
name|Attributes
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
comment|/**  * Salesforce DTO for SObject ComplexCalculatedFormula  */
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
literal|"ComplexCalculatedFormula"
argument_list|)
DECL|class|ComplexCalculatedFormula
specifier|public
class|class
name|ComplexCalculatedFormula
extends|extends
name|AbstractDescribedSObjectBase
block|{
DECL|method|ComplexCalculatedFormula ()
specifier|public
name|ComplexCalculatedFormula
parameter_list|()
block|{
name|getAttributes
argument_list|()
operator|.
name|setType
argument_list|(
literal|"ComplexCalculatedFormula"
argument_list|)
expr_stmt|;
block|}
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
DECL|field|ComplexCalculatedFormula
specifier|private
name|String
name|ComplexCalculatedFormula
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"ComplexCalculatedFormula"
argument_list|)
DECL|method|getComplexCalculatedFormula ()
specifier|public
name|String
name|getComplexCalculatedFormula
parameter_list|()
block|{
return|return
name|this
operator|.
name|ComplexCalculatedFormula
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"ComplexCalculatedFormula"
argument_list|)
DECL|method|setComplexCalculatedFormula (String ComplexCalculatedFormula)
specifier|public
name|void
name|setComplexCalculatedFormula
parameter_list|(
name|String
name|ComplexCalculatedFormula
parameter_list|)
block|{
name|this
operator|.
name|ComplexCalculatedFormula
operator|=
name|ComplexCalculatedFormula
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
name|setActivateable
argument_list|(
literal|false
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
name|setCreateable
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
name|description
operator|.
name|setCustomSetting
argument_list|(
literal|false
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
name|setDeprecatedAndHidden
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|description
operator|.
name|setFeedEnabled
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
literal|"ComplexCalculatedFormula"
argument_list|,
literal|"A complex calculated formula"
argument_list|,
literal|"string"
argument_list|,
literal|"xsd:string"
argument_list|,
literal|1300
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|true
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
name|sObjectField1
argument_list|)
expr_stmt|;
name|description
operator|.
name|setKeyPrefix
argument_list|(
literal|"500"
argument_list|)
expr_stmt|;
name|description
operator|.
name|setLabel
argument_list|(
literal|"Complex Calculated Formula"
argument_list|)
expr_stmt|;
name|description
operator|.
name|setLabelPlural
argument_list|(
literal|"ComplexCalculatedFormulas"
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
name|setMergeable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|description
operator|.
name|setName
argument_list|(
literal|"ComplexCalculatedFormula"
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
name|setReplicateable
argument_list|(
literal|true
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
name|setSearchLayoutable
argument_list|(
literal|"true"
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
name|setTriggerable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|description
operator|.
name|setUndeletable
argument_list|(
literal|true
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
name|setApprovalLayouts
argument_list|(
literal|"/services/data/v34.0/sobjects/Case/describe/approvalLayouts"
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
name|setCaseRowArticleSuggestions
argument_list|(
literal|"/services/data/v34.0/sobjects/Case/{ID}/suggestedArticles"
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
name|setDescribe
argument_list|(
literal|"/services/data/v34.0/sobjects/Case/describe"
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
name|setListviews
argument_list|(
literal|"/services/data/v34.0/sobjects/Case/listviews"
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
name|setRowTemplate
argument_list|(
literal|"/services/data/v34.0/sobjects/Case/{ID}"
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
name|setUiDetailTemplate
argument_list|(
literal|"https://salesforce-host/{ID}"
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
name|setUiNewRecord
argument_list|(
literal|"https://salesforce-host/500/e"
argument_list|)
expr_stmt|;
name|description
operator|.
name|setUrls
argument_list|(
name|sObjectDescriptionUrls1
argument_list|)
expr_stmt|;
return|return
name|description
return|;
block|}
block|}
end_class

end_unit

