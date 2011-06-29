begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|InterfacesTest
specifier|public
class|class
name|InterfacesTest
extends|extends
name|BaseJettyTest
block|{
DECL|field|isMacOS
specifier|private
specifier|static
name|boolean
name|isMacOS
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"Mac"
argument_list|)
decl_stmt|;
DECL|field|remoteInterfaceAddress
specifier|private
name|String
name|remoteInterfaceAddress
decl_stmt|;
DECL|field|port1
specifier|private
name|int
name|port1
decl_stmt|;
DECL|field|port2
specifier|private
name|int
name|port2
decl_stmt|;
DECL|field|port3
specifier|private
name|int
name|port3
decl_stmt|;
DECL|field|port4
specifier|private
name|int
name|port4
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
name|remoteInterfaceAddress
operator|==
literal|null
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
continue|continue;
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
block|}
annotation|@
name|Test
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
literal|"http://localhost:"
operator|+
name|port1
operator|+
literal|"/testRoute"
argument_list|)
decl_stmt|;
name|String
name|localResponse
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
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
if|if
condition|(
operator|!
name|isMacOS
condition|)
block|{
name|localUrl
operator|=
operator|new
name|URL
argument_list|(
literal|"http://127.0.0.1:"
operator|+
name|port2
operator|+
literal|"/testRoute"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|localUrl
operator|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|port2
operator|+
literal|"/testRoute"
argument_list|)
expr_stmt|;
block|}
name|localResponse
operator|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
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
literal|":"
operator|+
name|port3
operator|+
literal|"/testRoute"
argument_list|)
decl_stmt|;
name|String
name|remoteResponse
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
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
annotation|@
name|Test
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
literal|"http://localhost:"
operator|+
name|port4
operator|+
literal|"/allInterfaces"
argument_list|)
decl_stmt|;
name|String
name|localResponse
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
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
literal|":"
operator|+
name|port4
operator|+
literal|"/allInterfaces"
argument_list|)
decl_stmt|;
name|String
name|remoteResponse
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
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
name|port1
operator|=
name|getPort
argument_list|()
expr_stmt|;
name|port2
operator|=
name|getNextPort
argument_list|()
expr_stmt|;
name|port3
operator|=
name|getNextPort
argument_list|()
expr_stmt|;
name|port4
operator|=
name|getNextPort
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:"
operator|+
name|port1
operator|+
literal|"/testRoute"
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
literal|"jetty:http://localhost:"
operator|+
name|port2
operator|+
literal|"/testRoute"
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
literal|":"
operator|+
name|port3
operator|+
literal|"/testRoute"
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
literal|"jetty:http://0.0.0.0:"
operator|+
name|port4
operator|+
literal|"/allInterfaces"
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
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

