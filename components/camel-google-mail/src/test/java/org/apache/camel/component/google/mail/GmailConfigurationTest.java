begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.mail
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|mail
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
name|CamelContext
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
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|mail
operator|.
name|internal
operator|.
name|GmailUsersMessagesApiMethod
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
name|google
operator|.
name|mail
operator|.
name|internal
operator|.
name|GoogleMailApiCollection
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
name|impl
operator|.
name|DefaultCamelContext
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Test class for {@link GoogleMailConfiguration}.  */
end_comment

begin_class
DECL|class|GmailConfigurationTest
specifier|public
class|class
name|GmailConfigurationTest
extends|extends
name|AbstractGoogleMailTestSupport
block|{
comment|// userid of the currently authenticated user
DECL|field|CURRENT_USERID
specifier|public
specifier|static
specifier|final
name|String
name|CURRENT_USERID
init|=
literal|"me"
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|GmailConfigurationTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|PATH_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|PATH_PREFIX
init|=
name|GoogleMailApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getApiName
argument_list|(
name|GmailUsersMessagesApiMethod
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
DECL|field|TEST_URI
specifier|private
specifier|static
specifier|final
name|String
name|TEST_URI
init|=
literal|"google-mail://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/send?clientId=a&clientSecret=b&applicationName=c&accessToken=d&refreshToken=e"
decl_stmt|;
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|createRegistry
argument_list|()
argument_list|)
decl_stmt|;
comment|// add GoogleMailComponent to Camel context but don't set up configuration
specifier|final
name|GoogleMailComponent
name|component
init|=
operator|new
name|GoogleMailComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"google-mail"
argument_list|,
name|component
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testConfiguration ()
specifier|public
name|void
name|testConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|GoogleMailEndpoint
name|endpoint
init|=
name|getMandatoryEndpoint
argument_list|(
name|TEST_URI
argument_list|,
name|GoogleMailEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|GoogleMailConfiguration
name|configuration
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|configuration
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|configuration
operator|.
name|getClientSecret
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"c"
argument_list|,
name|configuration
operator|.
name|getApplicationName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"d"
argument_list|,
name|configuration
operator|.
name|getAccessToken
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"e"
argument_list|,
name|configuration
operator|.
name|getRefreshToken
argument_list|()
argument_list|)
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
block|{
comment|// test route for send
name|from
argument_list|(
literal|"direct://SEND"
argument_list|)
operator|.
name|to
argument_list|(
name|TEST_URI
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

