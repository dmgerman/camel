begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http4
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|http
operator|.
name|client
operator|.
name|CookieStore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|cookie
operator|.
name|Cookie
import|;
end_import

begin_comment
comment|/**  * A noop {@link CookieStore} used when bridging endpoints.  */
end_comment

begin_class
DECL|class|NoopCookieStore
specifier|public
class|class
name|NoopCookieStore
implements|implements
name|CookieStore
block|{
annotation|@
name|Override
DECL|method|addCookie (Cookie cookie)
specifier|public
name|void
name|addCookie
parameter_list|(
name|Cookie
name|cookie
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getCookies ()
specifier|public
name|List
argument_list|<
name|Cookie
argument_list|>
name|getCookies
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
annotation|@
name|Override
DECL|method|clearExpired (Date date)
specifier|public
name|boolean
name|clearExpired
parameter_list|(
name|Date
name|date
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
comment|// noop
block|}
block|}
end_class

end_unit

