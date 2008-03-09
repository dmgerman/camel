begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.mina
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina
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
name|ResolveEndpointFailedException
import|;
end_import

begin_comment
comment|/**  * For testing various minor holes that hasn't been covered by other unit tests.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|MinaComponentTest
specifier|public
class|class
name|MinaComponentTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testUnknownProtocol ()
specifier|public
name|void
name|testUnknownProtocol
parameter_list|()
block|{
try|try
block|{
name|template
operator|.
name|setDefaultEndpointUri
argument_list|(
literal|"mina:xxx://localhost:8080"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"mina:xxx://localhost:8080"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a ResolveEndpointFailedException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResolveEndpointFailedException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Should be an IAE exception"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|IllegalArgumentException
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unrecognised MINA protocol: xxx for uri: mina:xxx://localhost:8080"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

