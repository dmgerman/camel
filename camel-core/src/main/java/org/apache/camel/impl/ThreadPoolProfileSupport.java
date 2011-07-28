begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|ThreadPoolProfile
import|;
end_import

begin_comment
comment|/**  * Use ThreadPoolProfile instead  */
end_comment

begin_class
annotation|@
name|Deprecated
DECL|class|ThreadPoolProfileSupport
specifier|public
class|class
name|ThreadPoolProfileSupport
extends|extends
name|ThreadPoolProfile
block|{
DECL|method|ThreadPoolProfileSupport (String id)
specifier|public
name|ThreadPoolProfileSupport
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

