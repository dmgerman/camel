begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.stream
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stream
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
name|ContextTestSupport
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
comment|/**  * Unit test for encoding option  */
end_comment

begin_class
DECL|class|StreamEncodingTest
specifier|public
class|class
name|StreamEncodingTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testStringContent ()
specifier|public
name|void
name|testStringContent
parameter_list|()
throws|throws
name|Exception
block|{
comment|// include a UTF-8 char in the text \u0E08 is a Thai elephant
name|String
name|body
init|=
literal|"Hello Thai Elephant \u0E08"
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:in"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
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
literal|"direct:in"
argument_list|)
operator|.
name|to
argument_list|(
literal|"stream:out?encoding=UTF-8"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

