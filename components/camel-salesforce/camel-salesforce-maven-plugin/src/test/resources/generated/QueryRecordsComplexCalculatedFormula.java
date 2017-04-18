begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Salesforce Query DTO generated by camel-salesforce-maven-plugin  * Generated on: Thu Mar 09 16:15:49 ART 2017  */
end_comment

begin_package
DECL|package|$packageName
package|package
name|$packageName
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
name|XStreamImplicit
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
name|AbstractQueryRecordsBase
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

begin_comment
comment|/**  * Salesforce QueryRecords DTO for type ComplexCalculatedFormula  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.CamelSalesforceMojo"
argument_list|)
DECL|class|QueryRecordsComplexCalculatedFormula
specifier|public
class|class
name|QueryRecordsComplexCalculatedFormula
extends|extends
name|AbstractQueryRecordsBase
block|{
annotation|@
name|XStreamImplicit
DECL|field|records
specifier|private
name|List
argument_list|<
name|ComplexCalculatedFormula
argument_list|>
name|records
decl_stmt|;
DECL|method|getRecords ()
specifier|public
name|List
argument_list|<
name|ComplexCalculatedFormula
argument_list|>
name|getRecords
parameter_list|()
block|{
return|return
name|records
return|;
block|}
DECL|method|setRecords (List<ComplexCalculatedFormula> records)
specifier|public
name|void
name|setRecords
parameter_list|(
name|List
argument_list|<
name|ComplexCalculatedFormula
argument_list|>
name|records
parameter_list|)
block|{
name|this
operator|.
name|records
operator|=
name|records
expr_stmt|;
block|}
block|}
end_class

end_unit

