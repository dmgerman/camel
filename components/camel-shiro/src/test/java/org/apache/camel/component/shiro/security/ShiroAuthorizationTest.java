begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.shiro.security
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|shiro
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
name|List
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
name|EndpointInject
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
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|shiro
operator|.
name|authz
operator|.
name|Permission
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|shiro
operator|.
name|authz
operator|.
name|permission
operator|.
name|WildcardPermission
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
DECL|class|ShiroAuthorizationTest
specifier|public
class|class
name|ShiroAuthorizationTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:success"
argument_list|)
DECL|field|successEndpoint
specifier|protected
name|MockEndpoint
name|successEndpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:authorizationException"
argument_list|)
DECL|field|failureEndpoint
specifier|protected
name|MockEndpoint
name|failureEndpoint
decl_stmt|;
DECL|field|passPhrase
specifier|private
name|byte
index|[]
name|passPhrase
init|=
block|{
operator|(
name|byte
operator|)
literal|0x08
block|,
operator|(
name|byte
operator|)
literal|0x09
block|,
operator|(
name|byte
operator|)
literal|0x0A
block|,
operator|(
name|byte
operator|)
literal|0x0B
block|,
operator|(
name|byte
operator|)
literal|0x0C
block|,
operator|(
name|byte
operator|)
literal|0x0D
block|,
operator|(
name|byte
operator|)
literal|0x0E
block|,
operator|(
name|byte
operator|)
literal|0x0F
block|,
operator|(
name|byte
operator|)
literal|0x10
block|,
operator|(
name|byte
operator|)
literal|0x11
block|,
operator|(
name|byte
operator|)
literal|0x12
block|,
operator|(
name|byte
operator|)
literal|0x13
block|,
operator|(
name|byte
operator|)
literal|0x14
block|,
operator|(
name|byte
operator|)
literal|0x15
block|,
operator|(
name|byte
operator|)
literal|0x16
block|,
operator|(
name|byte
operator|)
literal|0x17
block|}
decl_stmt|;
annotation|@
name|Test
DECL|method|testShiroAuthorizationFailure ()
specifier|public
name|void
name|testShiroAuthorizationFailure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// The user ringo has role sec-level1 with permission set as zone1:readonly:*
comment|// Since the required permission is zone1:readwrite:*, this request should fail authorization
name|ShiroSecurityToken
name|shiroSecurityToken
init|=
operator|new
name|ShiroSecurityToken
argument_list|(
literal|"ringo"
argument_list|,
literal|"starr"
argument_list|)
decl_stmt|;
name|TestShiroSecurityTokenInjector
name|shiroSecurityTokenInjector
init|=
operator|new
name|TestShiroSecurityTokenInjector
argument_list|(
name|shiroSecurityToken
argument_list|,
name|passPhrase
argument_list|)
decl_stmt|;
name|successEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|failureEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:secureEndpoint"
argument_list|,
name|shiroSecurityTokenInjector
argument_list|)
expr_stmt|;
name|successEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|failureEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSuccessfulAuthorization ()
specifier|public
name|void
name|testSuccessfulAuthorization
parameter_list|()
throws|throws
name|Exception
block|{
comment|// The user john has role sec-level2 with permission set as zone1:*
comment|// Since the required permission incorporates zone1:readwrite:*, this request should successfully pass authorization
name|ShiroSecurityToken
name|shiroSecurityToken
init|=
operator|new
name|ShiroSecurityToken
argument_list|(
literal|"john"
argument_list|,
literal|"lennon"
argument_list|)
decl_stmt|;
name|TestShiroSecurityTokenInjector
name|shiroSecurityTokenInjector
init|=
operator|new
name|TestShiroSecurityTokenInjector
argument_list|(
name|shiroSecurityToken
argument_list|,
name|passPhrase
argument_list|)
decl_stmt|;
name|successEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|failureEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:secureEndpoint"
argument_list|,
name|shiroSecurityTokenInjector
argument_list|)
expr_stmt|;
name|successEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|failureEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSuccessfulAuthorizationForHigherScope ()
specifier|public
name|void
name|testSuccessfulAuthorizationForHigherScope
parameter_list|()
throws|throws
name|Exception
block|{
comment|// The user john has role sec-level3 with permission set as *
comment|// Since the required permission incorporates zone1:readwrite:*, this request should successfully pass authorization
name|ShiroSecurityToken
name|shiroSecurityToken
init|=
operator|new
name|ShiroSecurityToken
argument_list|(
literal|"paul"
argument_list|,
literal|"mccartney"
argument_list|)
decl_stmt|;
name|TestShiroSecurityTokenInjector
name|shiroSecurityTokenInjector
init|=
operator|new
name|TestShiroSecurityTokenInjector
argument_list|(
name|shiroSecurityToken
argument_list|,
name|passPhrase
argument_list|)
decl_stmt|;
name|successEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|failureEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:secureEndpoint"
argument_list|,
name|shiroSecurityTokenInjector
argument_list|)
expr_stmt|;
name|successEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|failureEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Permission
argument_list|>
name|permissionsList
init|=
operator|new
name|ArrayList
argument_list|<
name|Permission
argument_list|>
argument_list|()
decl_stmt|;
name|Permission
name|permission
init|=
operator|new
name|WildcardPermission
argument_list|(
literal|"zone1:readwrite:*"
argument_list|)
decl_stmt|;
name|permissionsList
operator|.
name|add
argument_list|(
name|permission
argument_list|)
expr_stmt|;
specifier|final
name|ShiroSecurityPolicy
name|securityPolicy
init|=
operator|new
name|ShiroSecurityPolicy
argument_list|(
literal|"src/test/resources/securityconfig.ini"
argument_list|,
name|passPhrase
argument_list|,
literal|true
argument_list|,
name|permissionsList
argument_list|)
decl_stmt|;
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|onException
argument_list|(
name|CamelAuthorizationException
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:authorizationException"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:secureEndpoint"
argument_list|)
operator|.
name|policy
argument_list|(
name|securityPolicy
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:incoming payload"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:success"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|TestShiroSecurityTokenInjector
specifier|private
specifier|static
class|class
name|TestShiroSecurityTokenInjector
extends|extends
name|ShiroSecurityTokenInjector
block|{
DECL|method|TestShiroSecurityTokenInjector (ShiroSecurityToken shiroSecurityToken, byte[] bytes)
specifier|public
name|TestShiroSecurityTokenInjector
parameter_list|(
name|ShiroSecurityToken
name|shiroSecurityToken
parameter_list|,
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|super
argument_list|(
name|shiroSecurityToken
argument_list|,
name|bytes
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"SHIRO_SECURITY_TOKEN"
argument_list|,
name|encrypt
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Beatle Mania"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

