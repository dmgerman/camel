begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gae.login
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
operator|.
name|login
package|;
end_package

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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
operator|.
name|login
operator|.
name|GLoginTestUtils
operator|.
name|createEndpoint
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
DECL|class|GLoginEndpointTest
specifier|public
class|class
name|GLoginEndpointTest
block|{
annotation|@
name|Test
DECL|method|testEndpointProperties ()
specifier|public
name|void
name|testEndpointProperties
parameter_list|()
throws|throws
name|Exception
block|{
comment|// test internet hostname
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"glogin:test.appspot.com"
argument_list|)
operator|.
name|append
argument_list|(
literal|"?"
argument_list|)
operator|.
name|append
argument_list|(
literal|"clientName=testClientName"
argument_list|)
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
operator|.
name|append
argument_list|(
literal|"userName=testUserName"
argument_list|)
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
operator|.
name|append
argument_list|(
literal|"password=testPassword"
argument_list|)
decl_stmt|;
name|GLoginEndpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"test.appspot.com"
argument_list|,
name|endpoint
operator|.
name|getHostName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"testClientName"
argument_list|,
name|endpoint
operator|.
name|getClientName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"testUserName"
argument_list|,
name|endpoint
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"testPassword"
argument_list|,
name|endpoint
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|endpoint
operator|.
name|isDevMode
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|=
name|createEndpoint
argument_list|(
literal|"glogin:test.appspot.com"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"apache-camel-2.x"
argument_list|,
name|endpoint
operator|.
name|getClientName
argument_list|()
argument_list|)
expr_stmt|;
comment|// test localhost with default port
name|endpoint
operator|=
name|createEndpoint
argument_list|(
literal|"glogin:localhost?devMode=true&devAdmin=true"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"localhost"
argument_list|,
name|endpoint
operator|.
name|getHostName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|8080
argument_list|,
name|endpoint
operator|.
name|getDevPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|isDevMode
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|isDevAdmin
argument_list|()
argument_list|)
expr_stmt|;
comment|// test localhost with custom port
name|endpoint
operator|=
name|createEndpoint
argument_list|(
literal|"glogin:localhost:9090?devMode=true"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"localhost"
argument_list|,
name|endpoint
operator|.
name|getHostName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|9090
argument_list|,
name|endpoint
operator|.
name|getDevPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|endpoint
operator|.
name|isDevAdmin
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

