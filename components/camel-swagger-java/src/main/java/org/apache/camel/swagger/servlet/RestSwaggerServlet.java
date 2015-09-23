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
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|ServletConfig
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
name|http
operator|.
name|HttpServlet
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
name|HttpServletResponse
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|jaxrs
operator|.
name|config
operator|.
name|BeanConfig
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
name|swagger
operator|.
name|RestApiResponseAdapter
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
name|swagger
operator|.
name|RestSwaggerSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|swagger
operator|.
name|SwaggerHelper
operator|.
name|buildUrl
import|;
end_import

begin_comment
comment|/**  * The default Camel swagger servlet to use when exposing the APIs of the rest-dsl using swagger.  *<p/>  * This requires Camel version 2.15 or better at runtime (and JMX to be enabled).  */
end_comment

begin_class
DECL|class|RestSwaggerServlet
specifier|public
class|class
name|RestSwaggerServlet
extends|extends
name|HttpServlet
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RestSwaggerServlet
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|swaggerConfig
specifier|private
name|BeanConfig
name|swaggerConfig
init|=
operator|new
name|BeanConfig
argument_list|()
decl_stmt|;
DECL|field|swagger
specifier|private
name|RestSwaggerSupport
name|swagger
init|=
operator|new
name|RestSwaggerSupport
argument_list|()
decl_stmt|;
DECL|field|initDone
specifier|private
specifier|volatile
name|boolean
name|initDone
decl_stmt|;
annotation|@
name|Override
DECL|method|init (final ServletConfig config)
specifier|public
name|void
name|init
parameter_list|(
specifier|final
name|ServletConfig
name|config
parameter_list|)
throws|throws
name|ServletException
block|{
name|super
operator|.
name|init
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Enumeration
name|en
init|=
name|config
operator|.
name|getInitParameterNames
argument_list|()
decl_stmt|;
while|while
condition|(
name|en
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|en
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|config
operator|.
name|getInitParameter
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
name|swagger
operator|.
name|initSwagger
argument_list|(
name|swaggerConfig
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doGet (HttpServletRequest request, HttpServletResponse response)
specifier|protected
name|void
name|doGet
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
if|if
condition|(
operator|!
name|initDone
condition|)
block|{
name|initBaseAndApiPaths
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
name|String
name|contextId
decl_stmt|;
name|String
name|route
init|=
name|request
operator|.
name|getPathInfo
argument_list|()
decl_stmt|;
name|RestApiResponseAdapter
name|adapter
init|=
operator|new
name|ServletRestApiResponseAdapter
argument_list|(
name|response
argument_list|)
decl_stmt|;
try|try
block|{
comment|// render list of camel contexts as root
if|if
condition|(
name|route
operator|==
literal|null
operator|||
name|route
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
operator|||
name|route
operator|.
name|equals
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|swagger
operator|.
name|renderCamelContexts
argument_list|(
name|adapter
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// first part is the camel context
if|if
condition|(
name|route
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|route
operator|=
name|route
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// the remainder is the route part
name|contextId
operator|=
name|route
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
index|[
literal|0
index|]
expr_stmt|;
if|if
condition|(
name|route
operator|.
name|startsWith
argument_list|(
name|contextId
argument_list|)
condition|)
block|{
name|route
operator|=
name|route
operator|.
name|substring
argument_list|(
name|contextId
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|swagger
operator|.
name|renderResourceListing
argument_list|(
name|adapter
argument_list|,
name|swaggerConfig
argument_list|,
name|contextId
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error rendering swagger due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|initBaseAndApiPaths (HttpServletRequest request)
specifier|private
name|void
name|initBaseAndApiPaths
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
throws|throws
name|MalformedURLException
block|{
name|String
name|base
init|=
name|swaggerConfig
operator|.
name|getBasePath
argument_list|()
decl_stmt|;
if|if
condition|(
name|base
operator|==
literal|null
operator|||
operator|!
name|base
operator|.
name|startsWith
argument_list|(
literal|"http"
argument_list|)
condition|)
block|{
comment|// base path is configured using relative, so lets calculate the absolute url now we have the http request
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|request
operator|.
name|getRequestURL
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|base
operator|==
literal|null
condition|)
block|{
name|base
operator|=
literal|""
expr_stmt|;
block|}
name|String
name|path
init|=
name|translateContextPath
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|swaggerConfig
operator|.
name|setHost
argument_list|(
name|url
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|url
operator|.
name|getPort
argument_list|()
operator|!=
literal|80
operator|&&
name|url
operator|.
name|getPort
argument_list|()
operator|!=
operator|-
literal|1
condition|)
block|{
name|swaggerConfig
operator|.
name|setHost
argument_list|(
name|url
operator|.
name|getHost
argument_list|()
operator|+
literal|":"
operator|+
name|url
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|swaggerConfig
operator|.
name|setHost
argument_list|(
name|url
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|swaggerConfig
operator|.
name|setBasePath
argument_list|(
name|buildUrl
argument_list|(
name|path
argument_list|,
name|base
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|initDone
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * We do only want the base context-path and not sub paths      */
DECL|method|translateContextPath (HttpServletRequest request)
specifier|private
name|String
name|translateContextPath
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
block|{
name|String
name|path
init|=
name|request
operator|.
name|getContextPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|isEmpty
argument_list|()
operator|||
name|path
operator|.
name|equals
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
return|return
literal|""
return|;
block|}
else|else
block|{
name|int
name|idx
init|=
name|path
operator|.
name|lastIndexOf
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
return|return
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
return|;
block|}
block|}
return|return
name|path
return|;
block|}
block|}
end_class

end_unit

