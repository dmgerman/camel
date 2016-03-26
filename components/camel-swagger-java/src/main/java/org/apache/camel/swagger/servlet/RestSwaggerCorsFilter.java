begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.swagger.servlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|swagger
operator|.
name|servlet
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
name|util
operator|.
name|HashMap
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
name|javax
operator|.
name|servlet
operator|.
name|Filter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterChain
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterConfig
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
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
name|javax
operator|.
name|servlet
operator|.
name|ServletResponse
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
name|HttpServletResponse
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
name|spi
operator|.
name|RestConfiguration
import|;
end_import

begin_comment
comment|/**  * A simple CORS filter that can used to allow the swagger ui or other API browsers from remote origins to access the  * Rest services exposes by this Camel swagger component.  *<p/>  * You can configure CORS headers in the init parameters to the Servlet Filter using the names:  *<ul>  *<li>Access-Control-Allow-Origin</li>  *<li>Access-Control-Allow-Methods</li>  *<li>Access-Control-Allow-Headers</li>  *<li>Access-Control-Max-Age</li>  *</ul>  * If a parameter is not configured then the default value is used.  * The default values are defined as:  *<ul>  *<li>{@link RestConfiguration#CORS_ACCESS_CONTROL_ALLOW_ORIGIN}</li>  *<li>{@link RestConfiguration#CORS_ACCESS_CONTROL_ALLOW_METHODS}</li>  *<li>{@link RestConfiguration#CORS_ACCESS_CONTROL_ALLOW_HEADERS}</li>  *<li>{@link RestConfiguration#CORS_ACCESS_CONTROL_MAX_AGE}</li>  *</ul>  */
end_comment

begin_class
DECL|class|RestSwaggerCorsFilter
specifier|public
class|class
name|RestSwaggerCorsFilter
implements|implements
name|Filter
block|{
DECL|field|corsHeaders
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|corsHeaders
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|init (FilterConfig filterConfig)
specifier|public
name|void
name|init
parameter_list|(
name|FilterConfig
name|filterConfig
parameter_list|)
throws|throws
name|ServletException
block|{
name|String
name|s
init|=
name|filterConfig
operator|.
name|getInitParameter
argument_list|(
literal|"Access-Control-Allow-Origin"
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|corsHeaders
operator|.
name|put
argument_list|(
literal|"Access-Control-Allow-Origin"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
name|s
operator|=
name|filterConfig
operator|.
name|getInitParameter
argument_list|(
literal|"Access-Control-Allow-Methods"
argument_list|)
expr_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|corsHeaders
operator|.
name|put
argument_list|(
literal|"Access-Control-Allow-Methods"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
name|s
operator|=
name|filterConfig
operator|.
name|getInitParameter
argument_list|(
literal|"Access-Control-Allow-Headers"
argument_list|)
expr_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|corsHeaders
operator|.
name|put
argument_list|(
literal|"Access-Control-Allow-Headers"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
name|s
operator|=
name|filterConfig
operator|.
name|getInitParameter
argument_list|(
literal|"Access-Control-Max-Age"
argument_list|)
expr_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|corsHeaders
operator|.
name|put
argument_list|(
literal|"Access-Control-Max-Age"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|doFilter (ServletRequest request, ServletResponse response, FilterChain chain)
specifier|public
name|void
name|doFilter
parameter_list|(
name|ServletRequest
name|request
parameter_list|,
name|ServletResponse
name|response
parameter_list|,
name|FilterChain
name|chain
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
name|HttpServletResponse
name|res
init|=
operator|(
name|HttpServletResponse
operator|)
name|response
decl_stmt|;
name|setupCorsHeaders
argument_list|(
name|res
argument_list|,
name|corsHeaders
argument_list|)
expr_stmt|;
name|chain
operator|.
name|doFilter
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
DECL|method|setupCorsHeaders (HttpServletResponse response, Map<String, String> corsHeaders)
specifier|private
specifier|static
name|void
name|setupCorsHeaders
parameter_list|(
name|HttpServletResponse
name|response
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|corsHeaders
parameter_list|)
block|{
comment|// use default value if none has been configured
name|String
name|allowOrigin
init|=
name|corsHeaders
operator|!=
literal|null
condition|?
name|corsHeaders
operator|.
name|get
argument_list|(
literal|"Access-Control-Allow-Origin"
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|allowOrigin
operator|==
literal|null
condition|)
block|{
name|allowOrigin
operator|=
name|RestConfiguration
operator|.
name|CORS_ACCESS_CONTROL_ALLOW_ORIGIN
expr_stmt|;
block|}
name|String
name|allowMethods
init|=
name|corsHeaders
operator|!=
literal|null
condition|?
name|corsHeaders
operator|.
name|get
argument_list|(
literal|"Access-Control-Allow-Methods"
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|allowMethods
operator|==
literal|null
condition|)
block|{
name|allowMethods
operator|=
name|RestConfiguration
operator|.
name|CORS_ACCESS_CONTROL_ALLOW_METHODS
expr_stmt|;
block|}
name|String
name|allowHeaders
init|=
name|corsHeaders
operator|!=
literal|null
condition|?
name|corsHeaders
operator|.
name|get
argument_list|(
literal|"Access-Control-Allow-Headers"
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|allowHeaders
operator|==
literal|null
condition|)
block|{
name|allowHeaders
operator|=
name|RestConfiguration
operator|.
name|CORS_ACCESS_CONTROL_ALLOW_HEADERS
expr_stmt|;
block|}
name|String
name|maxAge
init|=
name|corsHeaders
operator|!=
literal|null
condition|?
name|corsHeaders
operator|.
name|get
argument_list|(
literal|"Access-Control-Max-Age"
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|maxAge
operator|==
literal|null
condition|)
block|{
name|maxAge
operator|=
name|RestConfiguration
operator|.
name|CORS_ACCESS_CONTROL_MAX_AGE
expr_stmt|;
block|}
name|response
operator|.
name|setHeader
argument_list|(
literal|"Access-Control-Allow-Origin"
argument_list|,
name|allowOrigin
argument_list|)
expr_stmt|;
name|response
operator|.
name|setHeader
argument_list|(
literal|"Access-Control-Allow-Methods"
argument_list|,
name|allowMethods
argument_list|)
expr_stmt|;
name|response
operator|.
name|setHeader
argument_list|(
literal|"Access-Control-Allow-Headers"
argument_list|,
name|allowHeaders
argument_list|)
expr_stmt|;
name|response
operator|.
name|setHeader
argument_list|(
literal|"Access-Control-Max-Age"
argument_list|,
name|maxAge
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

