begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http.helper
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
operator|.
name|helper
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequestWrapper
import|;
end_import

begin_comment
comment|/**  * Special adapter when {@link org.apache.camel.component.http.HttpServletUrlRewrite} is in use,  * and the route started from came-jetty/camel-serlvet.  *<p/>  * This adapter ensures that we can control the context-path returned from the  * {@link javax.servlet.http.HttpServletRequest#getContextPath()} method.  * This allows us to ensure the context-path is based on the endpoint path, as the  * camel-jetty/camel-servlet server implementation uses the root ("/") context-path  * for all the servlets/endpoints.  */
end_comment

begin_class
DECL|class|UrlRewriteHttpServletRequestAdapter
specifier|public
specifier|final
class|class
name|UrlRewriteHttpServletRequestAdapter
extends|extends
name|HttpServletRequestWrapper
block|{
DECL|field|contextPath
specifier|private
specifier|final
name|String
name|contextPath
decl_stmt|;
comment|/**      * Creates this adapter      * @param delegate    the real http servlet request to delegate.      * @param contextPath use to override and return this context-path      */
DECL|method|UrlRewriteHttpServletRequestAdapter (HttpServletRequest delegate, String contextPath)
specifier|public
name|UrlRewriteHttpServletRequestAdapter
parameter_list|(
name|HttpServletRequest
name|delegate
parameter_list|,
name|String
name|contextPath
parameter_list|)
block|{
name|super
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|this
operator|.
name|contextPath
operator|=
name|contextPath
expr_stmt|;
block|}
DECL|method|getContextPath ()
specifier|public
name|String
name|getContextPath
parameter_list|()
block|{
return|return
name|contextPath
operator|!=
literal|null
condition|?
name|contextPath
else|:
name|super
operator|.
name|getContextPath
argument_list|()
return|;
block|}
block|}
end_class

end_unit

