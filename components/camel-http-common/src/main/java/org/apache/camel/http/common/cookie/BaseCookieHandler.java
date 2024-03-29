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
name|io
operator|.
name|IOException
import|;
end_import

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
name|CookieStore
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
comment|/**  * A basic implementation of a CamelCookie handler based on the JDK  * CookieManager.  */
end_comment

begin_class
DECL|class|BaseCookieHandler
specifier|public
specifier|abstract
class|class
name|BaseCookieHandler
implements|implements
name|CookieHandler
block|{
annotation|@
name|Override
DECL|method|storeCookies (Exchange exchange, URI uri, Map<String, List<String>> headerMap)
specifier|public
name|void
name|storeCookies
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|URI
name|uri
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headerMap
parameter_list|)
throws|throws
name|IOException
block|{
name|getCookieManager
argument_list|(
name|exchange
argument_list|)
operator|.
name|put
argument_list|(
name|uri
argument_list|,
name|headerMap
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|loadCookies (Exchange exchange, URI uri)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|loadCookies
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|URI
name|uri
parameter_list|)
throws|throws
name|IOException
block|{
comment|// the map is not used, so we do not need to fetch the headers from the
comment|// exchange
return|return
name|getCookieManager
argument_list|(
name|exchange
argument_list|)
operator|.
name|get
argument_list|(
name|uri
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getCookieStore (Exchange exchange)
specifier|public
name|CookieStore
name|getCookieStore
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getCookieManager
argument_list|(
name|exchange
argument_list|)
operator|.
name|getCookieStore
argument_list|()
return|;
block|}
DECL|method|getCookieManager (Exchange exchange)
specifier|protected
specifier|abstract
name|CookieManager
name|getCookieManager
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
block|}
end_class

end_unit

