begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|DatagramSocket
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
name|InetSocketAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MulticastSocket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|ServerSocket
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertFalse
import|;
end_import

begin_class
DECL|class|AvailablePortFinderTest
specifier|public
class|class
name|AvailablePortFinderTest
block|{
annotation|@
name|Test
DECL|method|testNotAvailableTcpPort ()
specifier|public
name|void
name|testNotAvailableTcpPort
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|p1
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
name|ServerSocket
name|socket
init|=
operator|new
name|ServerSocket
argument_list|(
name|p1
argument_list|)
decl_stmt|;
name|int
name|p2
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|p1
operator|==
name|p2
argument_list|,
literal|"Port "
operator|+
name|p1
operator|+
literal|" Port2 "
operator|+
name|p2
argument_list|)
expr_stmt|;
name|socket
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNotAvailableUdpPort ()
specifier|public
name|void
name|testNotAvailableUdpPort
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|p1
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
name|DatagramSocket
name|socket
init|=
operator|new
name|DatagramSocket
argument_list|(
name|p1
argument_list|)
decl_stmt|;
name|int
name|p2
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|p1
operator|==
name|p2
argument_list|,
literal|"Port "
operator|+
name|p1
operator|+
literal|" Port2 "
operator|+
name|p2
argument_list|)
expr_stmt|;
name|socket
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNotAvailableMulticastPort ()
specifier|public
name|void
name|testNotAvailableMulticastPort
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|p1
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
name|MulticastSocket
name|socket
init|=
operator|new
name|MulticastSocket
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|socket
operator|.
name|setReuseAddress
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// true is default for MulticastSocket, we wan to fail if port is occupied
name|socket
operator|.
name|bind
argument_list|(
operator|new
name|InetSocketAddress
argument_list|(
name|InetAddress
operator|.
name|getLocalHost
argument_list|()
argument_list|,
name|p1
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|p2
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|p1
operator|==
name|p2
argument_list|,
literal|"Port "
operator|+
name|p1
operator|+
literal|" Port2 "
operator|+
name|p2
argument_list|)
expr_stmt|;
name|socket
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

