begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_class
DECL|class|RouteBuilderAction
specifier|public
class|class
name|RouteBuilderAction
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|method|RouteBuilderAction (String name)
specifier|public
name|RouteBuilderAction
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
DECL|method|create (RouteBuilder builder)
specifier|public
name|void
name|create
parameter_list|(
name|RouteBuilder
name|builder
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
block|}
end_class

end_unit

