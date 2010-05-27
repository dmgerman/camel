begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.security
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|security
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
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
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
name|component
operator|.
name|spring
operator|.
name|security
operator|.
name|converter
operator|.
name|DefaultAuthenticationConverter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|WSUsernameTokenPrincipal
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
name|providers
operator|.
name|UsernamePasswordAuthenticationToken
import|;
end_import

begin_class
DECL|class|MyAuthenticationConverter
specifier|public
class|class
name|MyAuthenticationConverter
extends|extends
name|DefaultAuthenticationConverter
block|{
DECL|method|convertToAuthentication (Subject subject)
specifier|protected
name|Authentication
name|convertToAuthentication
parameter_list|(
name|Subject
name|subject
parameter_list|)
block|{
name|Authentication
name|answer
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Principal
name|principal
range|:
name|subject
operator|.
name|getPrincipals
argument_list|()
control|)
block|{
if|if
condition|(
name|principal
operator|instanceof
name|WSUsernameTokenPrincipal
condition|)
block|{
name|WSUsernameTokenPrincipal
name|ut
init|=
operator|(
name|WSUsernameTokenPrincipal
operator|)
name|principal
decl_stmt|;
name|answer
operator|=
operator|new
name|UsernamePasswordAuthenticationToken
argument_list|(
name|ut
operator|.
name|getName
argument_list|()
argument_list|,
name|ut
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

