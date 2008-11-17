begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|NetworkInterface
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|IOUtils
import|;
end_import

begin_class
DECL|class|InterfacesTest
specifier|public
class|class
name|InterfacesTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|remoteInterfaceAddress
specifier|private
name|String
name|remoteInterfaceAddress
decl_stmt|;
DECL|method|InterfacesTest ()
specifier|public
name|InterfacesTest
parameter_list|()
throws|throws
name|SocketException
block|{
comment|// retirieve an address of some remote network interface
name|Enumeration
argument_list|<
name|NetworkInterface
argument_list|>
name|interfaces
init|=
name|NetworkInterface
operator|.
name|getNetworkInterfaces
argument_list|()
decl_stmt|;
while|while
condition|(
name|interfaces
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|NetworkInterface
name|interfaze
init|=
name|interfaces
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|interfaze
operator|.
name|isUp
argument_list|()
operator|||
name|interfaze
operator|.
name|isLoopback
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|Enumeration
argument_list|<
name|InetAddress
argument_list|>
name|addresses
init|=
name|interfaze
operator|.
name|getInetAddresses
argument_list|()
decl_stmt|;
if|if
condition|(
name|addresses
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|remoteInterfaceAddress
operator|=
name|addresses
operator|.
name|nextElement
argument_list|()
operator|.
name|getHostAddress
argument_list|()
expr_stmt|;
block|}
block|}
empty_stmt|;
block|}
DECL|method|testLocalInterfaceHandled ()
specifier|public
name|void
name|testLocalInterfaceHandled
parameter_list|()
throws|throws
name|IOException
throws|,
name|InterruptedException
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:endpoint"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|URL
name|localUrl
init|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:4567/testRoute"
argument_list|)
decl_stmt|;
name|String
name|localResponse
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|localUrl
operator|.
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"local"
argument_list|,
name|localResponse
argument_list|)
expr_stmt|;
comment|// 127.0.0.1 is an alias of localhost so should work
name|localUrl
operator|=
operator|new
name|URL
argument_list|(
literal|"http://127.0.0.1:4568/testRoute"
argument_list|)
expr_stmt|;
name|localResponse
operator|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|localUrl
operator|.
name|openStream
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"local-differentPort"
argument_list|,
name|localResponse
argument_list|)
expr_stmt|;
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
literal|"http://"
operator|+
name|remoteInterfaceAddress
operator|+
literal|":4567/testRoute"
argument_list|)
decl_stmt|;
name|String
name|remoteResponse
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"remote"
argument_list|,
name|remoteResponse
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"jetty:http://localhost:4567/testRoute"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|"local"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:endpoint"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:4568/testRoute"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|"local-differentPort"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:endpoint"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://"
operator|+
name|remoteInterfaceAddress
operator|+
literal|":4567/testRoute"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|"remote"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:endpoint"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

