begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.builder.script
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|script
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|script
operator|.
name|ScriptBuilder
operator|.
name|groovy
import|;
end_import

begin_comment
comment|/**  * Unit test for a Groovy script based on end-user question.  */
end_comment

begin_class
DECL|class|GroovyScriptRouteTest
specifier|public
class|class
name|GroovyScriptRouteTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testGroovyScript ()
specifier|public
name|void
name|testGroovyScript
parameter_list|()
throws|throws
name|Exception
block|{
comment|//TODO: fix me
comment|//MockEndpoint mock = getMockEndpoint("mock:result");
comment|//mock.expectedHeaderReceived("foo", "Hello World");
comment|//template.sendBodyAndHeader("seda:a", "Hello World", "foo", "London");
comment|//mock.assertIsSatisfied();
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
literal|"seda:a"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
name|groovy
argument_list|(
literal|"request.body"
argument_list|)
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
block|}
end_class

end_unit

