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
name|Inet6Address
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
DECL|field|remoteInterfaceAddressV6
specifier|private
name|String
name|remoteInterfaceAddressV6
decl_stmt|;
DECL|method|InterfacesTest ()
specifier|public
name|InterfacesTest
parameter_list|()
throws|throws
name|IOException
block|{
comment|// Retrieve an address of some remote network interface
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
operator|(
name|remoteInterfaceAddressV6
operator|==
literal|null
operator|||
name|remoteInterfaceAddress
operator|==
literal|null
operator|)
operator|&&
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
name|InetAddress
name|nextAddress
init|=
name|addresses
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|nextAddress
operator|.
name|isLoopbackAddress
argument_list|()
operator|||
operator|!
name|nextAddress
operator|.
name|isReachable
argument_list|(
literal|2000
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|nextAddress
operator|instanceof
name|Inet6Address
condition|)
block|{
name|remoteInterfaceAddressV6
operator|=
name|nextAddress
operator|.
name|getHostAddress
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|remoteInterfaceAddress
operator|=
name|nextAddress
operator|.
name|getHostAddress
argument_list|()
expr_stmt|;
block|}
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
name|int
name|expectedMessages
init|=
operator|(
name|remoteInterfaceAddress
operator|!=
literal|null
operator|)
condition|?
literal|3
else|:
literal|2
decl_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:endpoint"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
name|expectedMessages
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
if|if
condition|(
name|remoteInterfaceAddress
operator|!=
literal|null
condition|)
block|{
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
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testInterfaceIpV6Handled ()
specifier|public
name|void
name|testInterfaceIpV6Handled
parameter_list|()
throws|throws
name|IOException
throws|,
name|InterruptedException
block|{
if|if
condition|(
name|remoteInterfaceAddressV6
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|getMockEndpoint
argument_list|(
literal|"mock:endpoint"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|URL
name|allInterfacesUrl
init|=
operator|new
name|URL
argument_list|(
literal|"http://["
operator|+
name|remoteInterfaceAddressV6
operator|+
literal|"]:5567/testRoute"
argument_list|)
decl_stmt|;
name|String
name|allInterfacesResponse
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|allInterfacesUrl
operator|.
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"allInterfacesV6"
argument_list|,
name|allInterfacesResponse
argument_list|)
expr_stmt|;
name|URL
name|oneInterfaceUrl
init|=
operator|new
name|URL
argument_list|(
literal|"http://["
operator|+
name|remoteInterfaceAddressV6
operator|+
literal|"]:5568/testRoute"
argument_list|)
decl_stmt|;
name|String
name|oneInterfaceResponse
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|oneInterfaceUrl
operator|.
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"remoteV6"
argument_list|,
name|oneInterfaceResponse
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testAllInterfaces ()
specifier|public
name|void
name|testAllInterfaces
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|expectedMessages
init|=
operator|(
name|remoteInterfaceAddress
operator|!=
literal|null
operator|)
condition|?
literal|2
else|:
literal|1
decl_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:endpoint"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
name|expectedMessages
argument_list|)
expr_stmt|;
name|URL
name|localUrl
init|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:4569/allInterfaces"
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
literal|"allInterfaces"
argument_list|,
name|localResponse
argument_list|)
expr_stmt|;
if|if
condition|(
name|remoteInterfaceAddress
operator|!=
literal|null
condition|)
block|{
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
literal|":4569/allInterfaces"
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
literal|"allInterfaces"
argument_list|,
name|remoteResponse
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|remoteInterfaceAddress
operator|!=
literal|null
condition|)
block|{
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
name|from
argument_list|(
literal|"jetty:http://0.0.0.0:4569/allInterfaces"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|"allInterfaces"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:endpoint"
argument_list|)
expr_stmt|;
if|if
condition|(
name|remoteInterfaceAddressV6
operator|!=
literal|null
condition|)
block|{
name|from
argument_list|(
literal|"jetty:http://[0:0:0:0:0:0:0:0]:5567/testRoute"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|"allInterfacesV6"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:endpoint"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://["
operator|+
name|remoteInterfaceAddressV6
operator|+
literal|"]:5568/testRoute"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|"remoteV6"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:endpoint"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

