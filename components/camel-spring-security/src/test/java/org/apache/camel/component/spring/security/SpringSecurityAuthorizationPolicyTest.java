begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.security
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|security
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|Subject
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
name|CamelAuthorizationException
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
name|Exchange
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
name|mock
operator|.
name|MockEndpoint
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
name|CamelSpringTestSupport
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
name|AbstractXmlApplicationContext
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|security
operator|.
name|authentication
operator|.
name|UsernamePasswordAuthenticationToken
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|security
operator|.
name|core
operator|.
name|Authentication
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|security
operator|.
name|core
operator|.
name|GrantedAuthority
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|security
operator|.
name|core
operator|.
name|authority
operator|.
name|GrantedAuthorityImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|security
operator|.
name|core
operator|.
name|context
operator|.
name|SecurityContextHolder
import|;
end_import

begin_class
DECL|class|SpringSecurityAuthorizationPolicyTest
specifier|public
class|class
name|SpringSecurityAuthorizationPolicyTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|Test
DECL|method|testAuthorizationPassed ()
specifier|public
name|void
name|testAuthorizationPassed
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|end
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
decl_stmt|;
name|end
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"hello world"
argument_list|)
expr_stmt|;
name|sendMessageWithAuthentication
argument_list|(
literal|"jim"
argument_list|,
literal|"jimspassword"
argument_list|,
literal|"ROLE_USER"
argument_list|,
literal|"ROLE_ADMIN"
argument_list|)
expr_stmt|;
name|end
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAuthorizationFailed ()
specifier|public
name|void
name|testAuthorizationFailed
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|end
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
decl_stmt|;
name|end
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|sendMessageWithAuthentication
argument_list|(
literal|"bob"
argument_list|,
literal|"bobspassword"
argument_list|,
literal|"ROLE_USER"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"we should get the access deny exception here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
name|exception
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
comment|// the exception should be caused by CamelAuthorizationException
name|assertTrue
argument_list|(
literal|"Expect CamelAuthorizationException here"
argument_list|,
name|exception
operator|.
name|getCause
argument_list|()
operator|instanceof
name|CamelAuthorizationException
argument_list|)
expr_stmt|;
block|}
name|end
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetAuthorizationTokenFromSecurityContextHolder ()
specifier|public
name|void
name|testGetAuthorizationTokenFromSecurityContextHolder
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|end
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
decl_stmt|;
name|end
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"hello world"
argument_list|)
expr_stmt|;
name|Authentication
name|authToken
init|=
name|createAuthenticationToken
argument_list|(
literal|"jim"
argument_list|,
literal|"jimspassword"
argument_list|,
literal|"ROLE_USER"
argument_list|,
literal|"ROLE_ADMIN"
argument_list|)
decl_stmt|;
name|SecurityContextHolder
operator|.
name|getContext
argument_list|()
operator|.
name|setAuthentication
argument_list|(
name|authToken
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"hello world"
argument_list|)
expr_stmt|;
name|end
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|SecurityContextHolder
operator|.
name|getContext
argument_list|()
operator|.
name|setAuthentication
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|createAuthenticationToken (String username, String password, String... roles)
specifier|private
name|Authentication
name|createAuthenticationToken
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|,
name|String
modifier|...
name|roles
parameter_list|)
block|{
name|Authentication
name|authToken
decl_stmt|;
if|if
condition|(
name|roles
operator|!=
literal|null
operator|&&
name|roles
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|List
argument_list|<
name|GrantedAuthority
argument_list|>
name|authorities
init|=
operator|new
name|ArrayList
argument_list|<
name|GrantedAuthority
argument_list|>
argument_list|(
name|roles
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|roles
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|authorities
operator|.
name|add
argument_list|(
operator|new
name|GrantedAuthorityImpl
argument_list|(
name|roles
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|authToken
operator|=
operator|new
name|UsernamePasswordAuthenticationToken
argument_list|(
name|username
argument_list|,
name|password
argument_list|,
name|authorities
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|authToken
operator|=
operator|new
name|UsernamePasswordAuthenticationToken
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
return|return
name|authToken
return|;
block|}
DECL|method|sendMessageWithAuthentication (String username, String password, String... roles)
specifier|private
name|void
name|sendMessageWithAuthentication
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|,
name|String
modifier|...
name|roles
parameter_list|)
block|{
name|Authentication
name|authToken
init|=
name|createAuthenticationToken
argument_list|(
name|username
argument_list|,
name|password
argument_list|,
name|roles
argument_list|)
decl_stmt|;
name|Subject
name|subject
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|add
argument_list|(
name|authToken
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"hello world"
argument_list|,
name|Exchange
operator|.
name|AUTHENTICATION
argument_list|,
name|subject
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"/org/apache/camel/component/spring/security/SpringSecurityCamelContext.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

