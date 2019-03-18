begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hipchat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hipchat
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|CamelContext
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
name|mockito
operator|.
name|Mockito
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
name|assertTrue
import|;
end_import

begin_class
DECL|class|HipchatComponentTest
specifier|public
class|class
name|HipchatComponentTest
block|{
annotation|@
name|Test
DECL|method|testUriParseNoSlashAndNoPort ()
specifier|public
name|void
name|testUriParseNoSlashAndNoPort
parameter_list|()
throws|throws
name|Exception
block|{
name|HipchatComponent
name|component
init|=
operator|new
name|HipchatComponent
argument_list|(
name|Mockito
operator|.
name|mock
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|HipchatEndpoint
name|endpoint
init|=
operator|(
name|HipchatEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"hipchat:https:localhost?authToken=token"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"localhost"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|80
argument_list|)
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"https"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"token"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAuthToken
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"https://localhost:80"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|hipChatUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a?auth_token=token"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|withAuthToken
argument_list|(
literal|"/a"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUriParseFull ()
specifier|public
name|void
name|testUriParseFull
parameter_list|()
throws|throws
name|Exception
block|{
name|HipchatComponent
name|component
init|=
operator|new
name|HipchatComponent
argument_list|(
name|Mockito
operator|.
name|mock
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|HipchatEndpoint
name|endpoint
init|=
operator|(
name|HipchatEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"hipchat:https://localhost:8080?authToken=token&consumeUsers=@auser,@buser"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"localhost"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|8080
argument_list|)
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"https"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"token"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAuthToken
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"https://localhost:8080"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|hipChatUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/a?auth_token=token"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|withAuthToken
argument_list|(
literal|"/a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|consumableUsers
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|consumableUsers
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
literal|"@auser"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|consumableUsers
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
literal|"@buser"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

