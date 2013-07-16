begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
operator|.
name|http
package|;
end_package

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
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|login
operator|.
name|LoginException
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
name|CamelExecutionException
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
name|impl
operator|.
name|JndiRegistry
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
DECL|class|NettyHttpBasicAuthCustomSecurityAuthenticatorTest
specifier|public
class|class
name|NettyHttpBasicAuthCustomSecurityAuthenticatorTest
extends|extends
name|BaseNettyTest
block|{
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myAuthenticator"
argument_list|,
operator|new
name|MyAuthenticator
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Test
DECL|method|testBasicAuth ()
specifier|public
name|void
name|testBasicAuth
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty-http:http://localhost:{{port}}/foo"
argument_list|,
literal|"Hello World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should send back 401"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|NettyHttpOperationFailedException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|NettyHttpOperationFailedException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|401
argument_list|,
name|cause
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|getMockEndpoint
argument_list|(
literal|"mock:input"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
comment|// username:password is scott:secret
name|String
name|auth
init|=
literal|"Basic c2NvdHQ6c2VjcmV0"
decl_stmt|;
name|String
name|out
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"netty-http:http://localhost:{{port}}/foo"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"Authorization"
argument_list|,
name|auth
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out
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
literal|"netty-http:http://0.0.0.0:{{port}}/foo?securityConfiguration.realm=foo&securityConfiguration.securityAuthenticator=#myAuthenticator"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:input"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyAuthenticator
specifier|private
specifier|final
class|class
name|MyAuthenticator
implements|implements
name|SecurityAuthenticator
block|{
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// noop
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setRoleClassNames (String names)
specifier|public
name|void
name|setRoleClassNames
parameter_list|(
name|String
name|names
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|login (HttpPrincipal principal)
specifier|public
name|Subject
name|login
parameter_list|(
name|HttpPrincipal
name|principal
parameter_list|)
throws|throws
name|LoginException
block|{
if|if
condition|(
operator|!
name|principal
operator|.
name|getPassword
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"secret"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|LoginException
argument_list|(
literal|"Login denied"
argument_list|)
throw|;
block|}
comment|// login success so return a subject
return|return
operator|new
name|Subject
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|logout (Subject subject)
specifier|public
name|void
name|logout
parameter_list|(
name|Subject
name|subject
parameter_list|)
throws|throws
name|LoginException
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|getUserRoles (Subject subject)
specifier|public
name|String
name|getUserRoles
parameter_list|(
name|Subject
name|subject
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

