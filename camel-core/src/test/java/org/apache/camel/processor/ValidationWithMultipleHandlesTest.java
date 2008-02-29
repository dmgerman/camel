begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *   */
end_comment

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ValidationException
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_comment
comment|/**  * The handle catch clause has a pipeline processing the exception.  *   * @author<a href="mailto:nsandhu">nsandhu</a>  *   */
end_comment

begin_class
DECL|class|ValidationWithMultipleHandlesTest
specifier|public
class|class
name|ValidationWithMultipleHandlesTest
extends|extends
name|ValidationTest
block|{
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|tryBlock
argument_list|()
operator|.
name|process
argument_list|(
name|validator
argument_list|)
operator|.
name|handle
argument_list|(
name|ValidationException
operator|.
name|class
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"xxx"
argument_list|,
name|constant
argument_list|(
literal|"yyy"
argument_list|)
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|tryBlock
argument_list|()
operator|.
name|process
argument_list|(
name|validator
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:valid"
argument_list|)
operator|.
name|handle
argument_list|(
name|ValidationException
operator|.
name|class
argument_list|)
operator|.
name|pipeline
argument_list|(
literal|"direct:a"
argument_list|,
literal|"mock:invalid"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

