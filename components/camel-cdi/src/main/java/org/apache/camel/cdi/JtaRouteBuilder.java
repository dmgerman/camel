begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|transaction
operator|.
name|JtaTransactionErrorHandlerBuilder
import|;
end_import

begin_comment
comment|/**  * An extension of the {@link RouteBuilder} to provide some additional helper  * methods  *  * @version  */
end_comment

begin_class
DECL|class|JtaRouteBuilder
specifier|public
specifier|abstract
class|class
name|JtaRouteBuilder
extends|extends
name|RouteBuilder
block|{
comment|/**      * Creates a transaction error handler that will lookup in application      * context for an exiting transaction manager.      *      * @return the created error handler      */
DECL|method|transactionErrorHandler ()
specifier|public
name|JtaTransactionErrorHandlerBuilder
name|transactionErrorHandler
parameter_list|()
block|{
return|return
operator|new
name|JtaTransactionErrorHandlerBuilder
argument_list|()
return|;
block|}
block|}
end_class

end_unit

