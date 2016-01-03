begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.sql.stored.template.ast
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sql
operator|.
name|stored
operator|.
name|template
operator|.
name|ast
package|;
end_package

begin_comment
comment|/**  * Created by snurmine on 1/3/16.  */
end_comment

begin_class
DECL|class|ParseRuntimeException
specifier|public
class|class
name|ParseRuntimeException
extends|extends
name|RuntimeException
block|{
DECL|method|ParseRuntimeException (String message)
specifier|public
name|ParseRuntimeException
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
DECL|method|ParseRuntimeException (Throwable cause)
specifier|public
name|ParseRuntimeException
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

