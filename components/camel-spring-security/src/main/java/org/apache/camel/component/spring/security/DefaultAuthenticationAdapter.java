begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Set
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
name|springframework
operator|.
name|security
operator|.
name|core
operator|.
name|Authentication
import|;
end_import

begin_class
DECL|class|DefaultAuthenticationAdapter
specifier|public
class|class
name|DefaultAuthenticationAdapter
implements|implements
name|AuthenticationAdapter
block|{
annotation|@
name|Override
DECL|method|toAuthentication (Subject subject)
specifier|public
name|Authentication
name|toAuthentication
parameter_list|(
name|Subject
name|subject
parameter_list|)
block|{
if|if
condition|(
name|subject
operator|==
literal|null
operator|||
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Set
argument_list|<
name|Authentication
argument_list|>
name|authentications
init|=
name|subject
operator|.
name|getPrincipals
argument_list|(
name|Authentication
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|authentications
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// just return the first one
return|return
name|authentications
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|convertToAuthentication
argument_list|(
name|subject
argument_list|)
return|;
block|}
block|}
comment|/**      * You can add the customer convert code here      */
DECL|method|convertToAuthentication (Subject subject)
specifier|protected
name|Authentication
name|convertToAuthentication
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
end_class

end_unit

