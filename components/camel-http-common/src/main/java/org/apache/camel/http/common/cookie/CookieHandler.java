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
name|CookiePolicy
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
comment|/**  * The interface for cookie handling will allow components to handle cookies for  * HTTP requests.  *<p>  * Note: The defined cookie policies apply. The default is  * CookiePolicy.ACCEPT_ORIGINAL_SERVER, so cookies will only be handled for  * fully qualified host names in the URI (not local host names like "myhost" or  * "localhost").  */
end_comment

begin_interface
DECL|interface|CookieHandler
specifier|public
interface|interface
name|CookieHandler
block|{
comment|/**      * Store cookies for a HTTP response in the cookie handler      *       * @param exchange the exchange      * @param uri the URI of the called HTTP service      * @param headerMap a map containing the HTTP headers returned by the server      * @throws IOException if the cookies cannot be stored      */
DECL|method|storeCookies (Exchange exchange, URI uri, Map<String, List<String>> headerMap)
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
function_decl|;
comment|/**      * Create cookie headers from the stored cookies appropriate for a given      * URI.      *       * @param exchange the exchange      * @param uri the URI of the called HTTP service      * @return a map containing the cookie headers that can be set to the HTTP      *         request. Only cookies that are supposed to be sent to the URI in      *         question are considered.      * @throws IOException if the cookies cannot be loaded      */
DECL|method|loadCookies (Exchange exchange, URI uri)
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
function_decl|;
comment|/**      * Get the CookieStore. This method can be used if the is using a CookieHandler by itself.      *      * @param exchange the exchange      * @return the CookieStore      */
DECL|method|getCookieStore (Exchange exchange)
name|CookieStore
name|getCookieStore
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Define a CookiePolicy for cookies stored by this CookieHandler      *       * @param cookiePolicy the CookiePolicy      */
DECL|method|setCookiePolicy (CookiePolicy cookiePolicy)
name|void
name|setCookiePolicy
parameter_list|(
name|CookiePolicy
name|cookiePolicy
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

