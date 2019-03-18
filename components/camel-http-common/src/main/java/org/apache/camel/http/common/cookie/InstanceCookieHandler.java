begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.http.common.cookie
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|http
operator|.
name|common
operator|.
name|cookie
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|CookieManager
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|CookiePolicy
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

begin_comment
comment|/**  * This implementation of the  * {@link org.apache.camel.http.common.cookie.CookieHandler} interface keeps the  * cookies with the instance of this object. If it is shared between endpoints  * the sessions will be shared as long as they are sent to the same domain.  */
end_comment

begin_class
DECL|class|InstanceCookieHandler
specifier|public
class|class
name|InstanceCookieHandler
extends|extends
name|BaseCookieHandler
block|{
DECL|field|cookieHandler
specifier|private
name|CookieManager
name|cookieHandler
decl_stmt|;
annotation|@
name|Override
DECL|method|getCookieManager (Exchange exchange)
specifier|protected
name|CookieManager
name|getCookieManager
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getCookieManagerInternal
argument_list|()
return|;
block|}
DECL|method|getCookieManagerInternal ()
specifier|private
name|CookieManager
name|getCookieManagerInternal
parameter_list|()
block|{
if|if
condition|(
name|cookieHandler
operator|==
literal|null
condition|)
block|{
name|cookieHandler
operator|=
operator|new
name|CookieManager
argument_list|()
expr_stmt|;
block|}
return|return
name|cookieHandler
return|;
block|}
annotation|@
name|Override
DECL|method|setCookiePolicy (CookiePolicy cookiePolicy)
specifier|public
name|void
name|setCookiePolicy
parameter_list|(
name|CookiePolicy
name|cookiePolicy
parameter_list|)
block|{
name|getCookieManagerInternal
argument_list|()
operator|.
name|setCookiePolicy
argument_list|(
name|cookiePolicy
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

