begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.swagger
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|swagger
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
name|List
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
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonInclude
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|SerializationFeature
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
name|io
operator|.
name|swagger
operator|.
name|models
operator|.
name|Swagger
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
name|model
operator|.
name|rest
operator|.
name|RestDefinition
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

begin_class
DECL|class|RestSwaggerApiDeclarationServlet
specifier|public
specifier|abstract
class|class
name|RestSwaggerApiDeclarationServlet
extends|extends
name|HttpServlet
block|{
DECL|field|LOG
specifier|private
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RestSwaggerApiDeclarationServlet
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|reader
specifier|private
name|RestSwaggerReader
name|reader
init|=
operator|new
name|RestSwaggerReader
argument_list|()
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
DECL|field|cors
specifier|private
name|boolean
name|cors
decl_stmt|;
DECL|field|initDone
specifier|private
specifier|volatile
name|boolean
name|initDone
decl_stmt|;
annotation|@
name|Override
DECL|method|init (ServletConfig config)
specifier|public
name|void
name|init
parameter_list|(
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
comment|// configure swagger options
name|String
name|s
init|=
name|config
operator|.
name|getInitParameter
argument_list|(
literal|"swagger.version"
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|swaggerConfig
operator|.
name|setVersion
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
name|s
operator|=
name|config
operator|.
name|getInitParameter
argument_list|(
literal|"base.path"
argument_list|)
expr_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|swaggerConfig
operator|.
name|setBasePath
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
name|s
operator|=
name|config
operator|.
name|getInitParameter
argument_list|(
literal|"host"
argument_list|)
expr_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|swaggerConfig
operator|.
name|setHost
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
name|s
operator|=
name|config
operator|.
name|getInitParameter
argument_list|(
literal|"cors"
argument_list|)
expr_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|cors
operator|=
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
name|String
name|title
init|=
name|config
operator|.
name|getInitParameter
argument_list|(
literal|"api.title"
argument_list|)
decl_stmt|;
name|String
name|description
init|=
name|config
operator|.
name|getInitParameter
argument_list|(
literal|"api.description"
argument_list|)
decl_stmt|;
name|String
name|termsOfServiceUrl
init|=
name|config
operator|.
name|getInitParameter
argument_list|(
literal|"api.termsOfServiceUrl"
argument_list|)
decl_stmt|;
name|String
name|contact
init|=
name|config
operator|.
name|getInitParameter
argument_list|(
literal|"api.contact"
argument_list|)
decl_stmt|;
name|String
name|license
init|=
name|config
operator|.
name|getInitParameter
argument_list|(
literal|"api.license"
argument_list|)
decl_stmt|;
name|String
name|licenseUrl
init|=
name|config
operator|.
name|getInitParameter
argument_list|(
literal|"api.licenseUrl"
argument_list|)
decl_stmt|;
name|swaggerConfig
operator|.
name|setTitle
argument_list|(
name|title
argument_list|)
expr_stmt|;
name|swaggerConfig
operator|.
name|setDescription
argument_list|(
name|description
argument_list|)
expr_stmt|;
name|swaggerConfig
operator|.
name|setTermsOfServiceUrl
argument_list|(
name|termsOfServiceUrl
argument_list|)
expr_stmt|;
name|swaggerConfig
operator|.
name|setContact
argument_list|(
name|contact
argument_list|)
expr_stmt|;
name|swaggerConfig
operator|.
name|setLicense
argument_list|(
name|license
argument_list|)
expr_stmt|;
name|swaggerConfig
operator|.
name|setLicenseUrl
argument_list|(
name|licenseUrl
argument_list|)
expr_stmt|;
block|}
DECL|method|getRestDefinitions (String camelId)
specifier|public
specifier|abstract
name|List
argument_list|<
name|RestDefinition
argument_list|>
name|getRestDefinitions
parameter_list|(
name|String
name|camelId
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|findCamelContexts ()
specifier|public
specifier|abstract
name|List
argument_list|<
name|String
argument_list|>
name|findCamelContexts
parameter_list|()
throws|throws
name|Exception
function_decl|;
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
name|renderCamelContexts
argument_list|(
name|request
argument_list|,
name|response
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
comment|// TODO: implement these
if|if
condition|(
operator|!
name|route
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
operator|&&
operator|!
name|route
operator|.
name|equals
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
comment|// render overview if the route is empty or is the root path
comment|// renderApiDeclaration(request, response, contextId, route);
block|}
else|else
block|{
name|renderResourceListing
argument_list|(
name|request
argument_list|,
name|response
argument_list|,
name|contextId
argument_list|)
expr_stmt|;
block|}
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
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|renderResourceListing (HttpServletRequest request, HttpServletResponse response, String contextId)
specifier|private
name|void
name|renderResourceListing
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|,
name|String
name|contextId
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"renderResourceListing"
argument_list|)
expr_stmt|;
if|if
condition|(
name|cors
condition|)
block|{
name|response
operator|.
name|addHeader
argument_list|(
literal|"Access-Control-Allow-Headers"
argument_list|,
literal|"Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers"
argument_list|)
expr_stmt|;
name|response
operator|.
name|addHeader
argument_list|(
literal|"Access-Control-Allow-Methods"
argument_list|,
literal|"GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, CONNECT, PATCH"
argument_list|)
expr_stmt|;
name|response
operator|.
name|addHeader
argument_list|(
literal|"Access-Control-Allow-Origin"
argument_list|,
literal|"*"
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|RestDefinition
argument_list|>
name|rests
init|=
name|getRestDefinitions
argument_list|(
name|contextId
argument_list|)
decl_stmt|;
if|if
condition|(
name|rests
operator|!=
literal|null
condition|)
block|{
comment|// TODO: combine the rests
for|for
control|(
name|RestDefinition
name|rest
range|:
name|rests
control|)
block|{
name|Swagger
name|swagger
init|=
name|reader
operator|.
name|read
argument_list|(
name|rest
argument_list|,
name|swaggerConfig
argument_list|)
decl_stmt|;
name|ObjectMapper
name|mapper
init|=
operator|new
name|ObjectMapper
argument_list|()
decl_stmt|;
name|mapper
operator|.
name|enable
argument_list|(
name|SerializationFeature
operator|.
name|INDENT_OUTPUT
argument_list|)
expr_stmt|;
name|mapper
operator|.
name|setSerializationInclusion
argument_list|(
name|JsonInclude
operator|.
name|Include
operator|.
name|NON_NULL
argument_list|)
expr_stmt|;
name|mapper
operator|.
name|writeValue
argument_list|(
name|response
operator|.
name|getOutputStream
argument_list|()
argument_list|,
name|swagger
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|response
operator|.
name|setStatus
argument_list|(
literal|204
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
comment|/**      * Renders a list of available CamelContexts in the JVM      */
DECL|method|renderCamelContexts (HttpServletRequest request, HttpServletResponse response)
specifier|private
name|void
name|renderCamelContexts
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"renderCamelContexts"
argument_list|)
expr_stmt|;
if|if
condition|(
name|cors
condition|)
block|{
name|response
operator|.
name|addHeader
argument_list|(
literal|"Access-Control-Allow-Headers"
argument_list|,
literal|"Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers"
argument_list|)
expr_stmt|;
name|response
operator|.
name|addHeader
argument_list|(
literal|"Access-Control-Allow-Methods"
argument_list|,
literal|"GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, CONNECT, PATCH"
argument_list|)
expr_stmt|;
name|response
operator|.
name|addHeader
argument_list|(
literal|"Access-Control-Allow-Origin"
argument_list|,
literal|"*"
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|contexts
init|=
name|findCamelContexts
argument_list|()
decl_stmt|;
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|print
argument_list|(
literal|"[\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|contexts
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|name
init|=
name|contexts
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|print
argument_list|(
literal|"{\"name\": \""
operator|+
name|name
operator|+
literal|"\"}"
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|<
name|contexts
operator|.
name|size
argument_list|()
operator|-
literal|1
condition|)
block|{
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|print
argument_list|(
literal|",\n"
argument_list|)
expr_stmt|;
block|}
block|}
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|print
argument_list|(
literal|"\n]"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

