begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|RejectedExecutionException
import|;
end_import

begin_comment
comment|/**  * Created by david on 02/07/14.  */
end_comment

begin_class
DECL|class|ThrottlerRejectedExecutionException
specifier|public
class|class
name|ThrottlerRejectedExecutionException
extends|extends
name|RejectedExecutionException
block|{
DECL|method|ThrottlerRejectedExecutionException (String message)
specifier|public
name|ThrottlerRejectedExecutionException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

