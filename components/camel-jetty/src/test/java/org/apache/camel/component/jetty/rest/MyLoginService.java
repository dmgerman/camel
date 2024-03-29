begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty.rest
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
operator|.
name|rest
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
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
name|javax
operator|.
name|servlet
operator|.
name|ServletRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|security
operator|.
name|DefaultIdentityService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|security
operator|.
name|IdentityService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|security
operator|.
name|LoginService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|UserIdentity
import|;
end_import

begin_class
DECL|class|MyLoginService
specifier|public
class|class
name|MyLoginService
implements|implements
name|LoginService
block|{
DECL|field|is
specifier|private
name|IdentityService
name|is
init|=
operator|new
name|DefaultIdentityService
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"mylogin"
return|;
block|}
annotation|@
name|Override
DECL|method|login (String username, Object password, ServletRequest servletRequest)
specifier|public
name|UserIdentity
name|login
parameter_list|(
name|String
name|username
parameter_list|,
name|Object
name|password
parameter_list|,
name|ServletRequest
name|servletRequest
parameter_list|)
block|{
if|if
condition|(
literal|"donald"
operator|.
name|equals
argument_list|(
name|username
argument_list|)
condition|)
block|{
name|Subject
name|subject
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
name|Principal
name|principal
init|=
operator|new
name|Principal
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"camel"
return|;
block|}
block|}
decl_stmt|;
return|return
name|getIdentityService
argument_list|()
operator|.
name|newUserIdentity
argument_list|(
name|subject
argument_list|,
name|principal
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"admin"
block|}
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|validate (UserIdentity userIdentity)
specifier|public
name|boolean
name|validate
parameter_list|(
name|UserIdentity
name|userIdentity
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|getIdentityService ()
specifier|public
name|IdentityService
name|getIdentityService
parameter_list|()
block|{
return|return
name|is
return|;
block|}
annotation|@
name|Override
DECL|method|setIdentityService (IdentityService identityService)
specifier|public
name|void
name|setIdentityService
parameter_list|(
name|IdentityService
name|identityService
parameter_list|)
block|{
name|this
operator|.
name|is
operator|=
name|identityService
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|logout (UserIdentity userIdentity)
specifier|public
name|void
name|logout
parameter_list|(
name|UserIdentity
name|userIdentity
parameter_list|)
block|{
comment|// noop
block|}
block|}
end_class

end_unit

