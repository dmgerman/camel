begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|example
operator|.
name|server
operator|.
name|Multiplier
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
name|test
operator|.
name|AvailablePortFinder
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
name|test
operator|.
name|junit4
operator|.
name|TestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
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
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|SpringJmsClientRemotingServerTest
specifier|public
class|class
name|SpringJmsClientRemotingServerTest
extends|extends
name|TestSupport
block|{
DECL|field|appCtx
specifier|private
specifier|static
name|ClassPathXmlApplicationContext
name|appCtx
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|setupFreePort ()
specifier|public
specifier|static
name|void
name|setupFreePort
parameter_list|()
throws|throws
name|Exception
block|{
comment|// find a free port number, and write that in the custom.properties file
comment|// which we will use for the unit tests, to avoid port number in use problems
name|int
name|port
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
name|String
name|bank1
init|=
literal|"tcp.port="
operator|+
name|port
decl_stmt|;
name|File
name|custom
init|=
operator|new
name|File
argument_list|(
literal|"target/custom.properties"
argument_list|)
decl_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|custom
argument_list|)
decl_stmt|;
name|fos
operator|.
name|write
argument_list|(
name|bank1
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
name|appCtx
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"/META-INF/spring/camel-server.xml"
argument_list|,
literal|"camel-client-remoting.xml"
argument_list|)
expr_stmt|;
name|appCtx
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|stopSpring ()
specifier|public
specifier|static
name|void
name|stopSpring
parameter_list|()
block|{
name|appCtx
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCamelRemotingInvocation ()
specifier|public
name|void
name|testCamelRemotingInvocation
parameter_list|()
block|{
comment|// just get the proxy to the service and we as the client can use the "proxy" as it was
comment|// a local object we are invoking. Camel will under the covers do the remote communication
comment|// to the remote ActiveMQ server and fetch the response.
name|Multiplier
name|multiplier
init|=
name|appCtx
operator|.
name|getBean
argument_list|(
literal|"multiplierProxy"
argument_list|,
name|Multiplier
operator|.
name|class
argument_list|)
decl_stmt|;
name|int
name|response
init|=
name|multiplier
operator|.
name|multiply
argument_list|(
literal|33
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response"
argument_list|,
literal|99
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

