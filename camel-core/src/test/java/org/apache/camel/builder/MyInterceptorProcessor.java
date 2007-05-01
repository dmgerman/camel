begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *   */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|Exchange
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
name|processor
operator|.
name|DelegateProcessor
import|;
end_import

begin_class
DECL|class|MyInterceptorProcessor
specifier|public
class|class
name|MyInterceptorProcessor
extends|extends
name|DelegateProcessor
argument_list|<
name|Exchange
argument_list|>
block|{
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"START of onExchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
name|next
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"END of onExchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

