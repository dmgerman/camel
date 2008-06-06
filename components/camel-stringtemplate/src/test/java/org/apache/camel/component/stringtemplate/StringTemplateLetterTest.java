begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.velocity
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|velocity
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
name|Message
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
comment|/**  * Unit test for wiki documentation  */
end_comment

begin_class
DECL|class|StringTemplateLetterTest
specifier|public
class|class
name|StringTemplateLetterTest
extends|extends
name|ContextTestSupport
block|{
comment|// START SNIPPET: e1
DECL|method|createLetter ()
specifier|private
name|Exchange
name|createLetter
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|Message
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"firstName"
argument_list|,
literal|"Claus"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"lastName"
argument_list|,
literal|"Ibsen"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"item"
argument_list|,
literal|"Camel in Action"
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|testVelocityLetter ()
specifier|public
name|void
name|testVelocityLetter
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Dear Ibsen, Claus\n\nThanks for the order of Camel in Action.\n\nRegards Camel Riders Bookstore"
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:a"
argument_list|,
name|createLetter
argument_list|()
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
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
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"string-template:org/apache/camel/component/stringtemplate/letter.tm"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|// END SNIPPET: e1
block|}
end_class

end_unit

