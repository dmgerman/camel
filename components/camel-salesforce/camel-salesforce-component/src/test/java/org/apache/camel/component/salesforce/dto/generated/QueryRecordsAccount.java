begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.salesforce.dto.generated
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
name|dto
operator|.
name|generated
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

begin_class
DECL|class|QueryRecordsAccount
specifier|public
class|class
name|QueryRecordsAccount
extends|extends
name|AbstractQueryRecordsBase
block|{
annotation|@
name|XStreamImplicit
DECL|field|records
specifier|private
name|List
argument_list|<
name|Account
argument_list|>
name|records
decl_stmt|;
DECL|method|getRecords ()
specifier|public
name|List
argument_list|<
name|Account
argument_list|>
name|getRecords
parameter_list|()
block|{
return|return
name|records
return|;
block|}
DECL|method|setRecords (List<Account> records)
specifier|public
name|void
name|setRecords
parameter_list|(
name|List
argument_list|<
name|Account
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

