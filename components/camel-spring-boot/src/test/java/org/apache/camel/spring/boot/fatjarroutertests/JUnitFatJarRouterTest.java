begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot.fatjarroutertests
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|fatjarroutertests
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
name|lang
operator|.
name|management
operator|.
name|ManagementFactory
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
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MalformedObjectNameException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|test
operator|.
name|context
operator|.
name|SpringBootTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|SpringRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|SocketUtils
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|SpringRunner
operator|.
name|class
argument_list|)
annotation|@
name|SpringBootTest
argument_list|(
name|classes
operator|=
name|TestFatJarRouter
operator|.
name|class
argument_list|,
name|properties
operator|=
literal|"spring.main.sources=org.apache.camel.spring.boot.fatjarroutertests"
argument_list|)
DECL|class|JUnitFatJarRouterTest
specifier|public
class|class
name|JUnitFatJarRouterTest
extends|extends
name|Assert
block|{
DECL|field|port
specifier|static
name|int
name|port
init|=
name|SocketUtils
operator|.
name|findAvailableTcpPort
argument_list|(
literal|20000
argument_list|)
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|beforeClass ()
specifier|public
specifier|static
name|void
name|beforeClass
parameter_list|()
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"http.port"
argument_list|,
name|port
operator|+
literal|""
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldStartCamelRoute ()
specifier|public
name|void
name|shouldStartCamelRoute
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
throws|,
name|MalformedObjectNameException
block|{
name|String
name|response
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|port
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"stringBean"
argument_list|,
name|response
argument_list|)
expr_stmt|;
comment|// There should be 3 routes running..
name|MBeanServer
name|mbs
init|=
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|objectNames
init|=
name|mbs
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.camel:type=routes,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|objectNames
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

