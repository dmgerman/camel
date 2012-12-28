begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
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
name|Producer
import|;
end_import

begin_comment
comment|/**  * Extended {@link UrlRewrite} which leverages {@link HttpServletRequest}  * during the rewrite process.  *<p/>  * For example the camel-urlrewrite component supports  * {@link HttpServletUrlRewrite} implementations.  */
end_comment

begin_interface
DECL|interface|HttpServletUrlRewrite
specifier|public
interface|interface
name|HttpServletUrlRewrite
extends|extends
name|UrlRewrite
block|{
comment|/**      * Rewrite the url.      *      * @param url  the absolute url (eg with scheme://host:port/path?query)      * @param relativeUrl optional relative url, if bridging endpoints, which then would be without the base path from the      *                    endpoint from the given producer.      * @param producer the producer to use the rewritten url      * @param request  the http servlet request      * @return the rewritten url, or<tt>null</tt> to use the original url      * @throws Exception is thrown if error rewriting the url      */
DECL|method|rewrite (String url, String relativeUrl, Producer producer, HttpServletRequest request)
name|String
name|rewrite
parameter_list|(
name|String
name|url
parameter_list|,
name|String
name|relativeUrl
parameter_list|,
name|Producer
name|producer
parameter_list|,
name|HttpServletRequest
name|request
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

