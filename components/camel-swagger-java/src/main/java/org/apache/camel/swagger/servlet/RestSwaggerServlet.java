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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|impl
operator|.
name|DefaultClassResolver
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
name|ClassResolver
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|EndpointHelper
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
name|util
operator|.
name|ObjectHelper
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
comment|/**  * The default Camel swagger servlet to use when exposing the APIs of the rest-dsl using swagger.  *<p/>  * This requires Camel version 2.15 or better at runtime (and JMX to be enabled).  *  * @deprecated do not use this directly but use rest-dsl the regular way with rest-dsl configuration.  */
end_comment

begin_class
annotation|@
name|Deprecated
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
specifier|final
name|BeanConfig
name|swaggerConfig
init|=
operator|new
name|BeanConfig
argument_list|()
decl_stmt|;
DECL|field|support
specifier|private
specifier|final
name|RestSwaggerSupport
name|support
init|=
operator|new
name|RestSwaggerSupport
argument_list|()
decl_stmt|;
DECL|field|classResolver
specifier|private
specifier|final
name|ClassResolver
name|classResolver
init|=
operator|new
name|DefaultClassResolver
argument_list|()
decl_stmt|;
DECL|field|initDone
specifier|private
specifier|volatile
name|boolean
name|initDone
decl_stmt|;
DECL|field|apiContextIdPattern
specifier|private
name|String
name|apiContextIdPattern
decl_stmt|;
DECL|field|apiContextIdListing
specifier|private
name|boolean
name|apiContextIdListing
decl_stmt|;
DECL|field|translateContextPath
specifier|private
name|boolean
name|translateContextPath
init|=
literal|true
decl_stmt|;
DECL|method|getApiContextIdPattern ()
specifier|public
name|String
name|getApiContextIdPattern
parameter_list|()
block|{
return|return
name|apiContextIdPattern
return|;
block|}
comment|/**      * Optional CamelContext id pattern to only allow Rest APIs from rest services within CamelContext's which name matches the pattern.      *<p/>      * The pattern uses the rules from {@link org.apache.camel.util.EndpointHelper#matchPattern(String, String)}      *      * @param apiContextIdPattern  the pattern      */
DECL|method|setApiContextIdPattern (String apiContextIdPattern)
specifier|public
name|void
name|setApiContextIdPattern
parameter_list|(
name|String
name|apiContextIdPattern
parameter_list|)
block|{
name|this
operator|.
name|apiContextIdPattern
operator|=
name|apiContextIdPattern
expr_stmt|;
block|}
DECL|method|isApiContextIdListing ()
specifier|public
name|boolean
name|isApiContextIdListing
parameter_list|()
block|{
return|return
name|apiContextIdListing
return|;
block|}
comment|/**      * Sets whether listing of all available CamelContext's with REST services in the JVM is enabled. If enabled it allows to discover      * these contexts, if<tt>false</tt> then only if there is exactly one CamelContext then its used.      */
DECL|method|setApiContextIdListing (boolean apiContextIdListing)
specifier|public
name|void
name|setApiContextIdListing
parameter_list|(
name|boolean
name|apiContextIdListing
parameter_list|)
block|{
name|this
operator|.
name|apiContextIdListing
operator|=
name|apiContextIdListing
expr_stmt|;
block|}
DECL|method|isTranslateContextPath ()
specifier|public
name|boolean
name|isTranslateContextPath
parameter_list|()
block|{
return|return
name|translateContextPath
return|;
block|}
comment|/**      * Sets whether the context path of the request should be translated (true) or used as-is (false)      * Optional, Defaults to true      */
DECL|method|setTranslateContextPath (boolean translateContextPath)
specifier|public
name|void
name|setTranslateContextPath
parameter_list|(
name|boolean
name|translateContextPath
parameter_list|)
block|{
name|this
operator|.
name|translateContextPath
operator|=
name|translateContextPath
expr_stmt|;
block|}
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
argument_list|<>
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
comment|// when using servlet then use the cors filter to enable cors
if|if
condition|(
name|parameters
operator|.
name|get
argument_list|(
literal|"cors"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Use RestSwaggerCorsFilter when uisng this Servlet to enable CORS"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|remove
argument_list|(
literal|"cors"
argument_list|)
expr_stmt|;
block|}
name|support
operator|.
name|initSwagger
argument_list|(
name|swaggerConfig
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// allow to configure these options from the servlet config as well
name|Object
name|pattern
init|=
name|parameters
operator|.
name|remove
argument_list|(
literal|"apiContextIdPattern"
argument_list|)
decl_stmt|;
if|if
condition|(
name|pattern
operator|!=
literal|null
condition|)
block|{
name|apiContextIdPattern
operator|=
name|pattern
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|Object
name|listing
init|=
name|parameters
operator|.
name|remove
argument_list|(
literal|"apiContextIdListing"
argument_list|)
decl_stmt|;
if|if
condition|(
name|listing
operator|!=
literal|null
condition|)
block|{
name|apiContextIdListing
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|listing
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Object
name|translate
init|=
name|parameters
operator|.
name|remove
argument_list|(
literal|"translateContextPath"
argument_list|)
decl_stmt|;
if|if
condition|(
name|translate
operator|!=
literal|null
condition|)
block|{
name|translateContextPath
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|translate
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
init|=
literal|null
decl_stmt|;
name|String
name|route
init|=
name|request
operator|.
name|getPathInfo
argument_list|()
decl_stmt|;
name|String
name|accept
init|=
name|request
operator|.
name|getHeader
argument_list|(
literal|"Accept"
argument_list|)
decl_stmt|;
comment|// whether to use json or yaml
name|boolean
name|json
init|=
literal|false
decl_stmt|;
name|boolean
name|yaml
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|route
operator|!=
literal|null
operator|&&
name|route
operator|.
name|endsWith
argument_list|(
literal|"/swagger.json"
argument_list|)
condition|)
block|{
name|json
operator|=
literal|true
expr_stmt|;
name|route
operator|=
name|route
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|route
operator|.
name|length
argument_list|()
operator|-
literal|13
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|route
operator|!=
literal|null
operator|&&
name|route
operator|.
name|endsWith
argument_list|(
literal|"/swagger.yaml"
argument_list|)
condition|)
block|{
name|yaml
operator|=
literal|true
expr_stmt|;
name|route
operator|=
name|route
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|route
operator|.
name|length
argument_list|()
operator|-
literal|13
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|accept
operator|!=
literal|null
operator|&&
operator|!
name|json
operator|&&
operator|!
name|yaml
condition|)
block|{
name|json
operator|=
name|accept
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
operator|.
name|contains
argument_list|(
literal|"json"
argument_list|)
expr_stmt|;
name|yaml
operator|=
name|accept
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
operator|.
name|contains
argument_list|(
literal|"yaml"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|json
operator|&&
operator|!
name|yaml
condition|)
block|{
comment|// json is default
name|json
operator|=
literal|true
expr_stmt|;
block|}
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
name|apiContextIdListing
operator|&&
operator|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|route
argument_list|)
operator|||
name|route
operator|.
name|equals
argument_list|(
literal|"/"
argument_list|)
operator|)
condition|)
block|{
name|support
operator|.
name|renderCamelContexts
argument_list|(
name|adapter
argument_list|,
name|contextId
argument_list|,
name|apiContextIdPattern
argument_list|,
name|json
argument_list|,
name|yaml
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|name
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|route
argument_list|)
condition|)
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
name|name
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
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|route
operator|=
name|route
operator|.
name|substring
argument_list|(
name|name
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// listing not enabled then see if there is only one CamelContext and use that as the name
name|List
argument_list|<
name|String
argument_list|>
name|contexts
init|=
name|support
operator|.
name|findCamelContexts
argument_list|()
decl_stmt|;
if|if
condition|(
name|contexts
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|name
operator|=
name|contexts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
name|boolean
name|match
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|match
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|apiContextIdPattern
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"#name#"
operator|.
name|equals
argument_list|(
name|apiContextIdPattern
argument_list|)
condition|)
block|{
comment|// always match as we do not know what is the current CamelContext in a plain servlet
name|match
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|match
operator|=
name|EndpointHelper
operator|.
name|matchPattern
argument_list|(
name|name
argument_list|,
name|apiContextIdPattern
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Match contextId: {} with pattern: {} -> {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|name
block|,
name|apiContextIdPattern
block|,
name|match
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|match
condition|)
block|{
name|adapter
operator|.
name|noContent
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|support
operator|.
name|renderResourceListing
argument_list|(
name|adapter
argument_list|,
name|swaggerConfig
argument_list|,
name|name
argument_list|,
name|route
argument_list|,
name|json
argument_list|,
name|yaml
argument_list|,
name|classResolver
argument_list|,
operator|new
name|RestConfiguration
argument_list|()
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
literal|"Error rendering Swagger API due "
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
comment|// setup host if not configured
if|if
condition|(
name|swaggerConfig
operator|.
name|getHost
argument_list|()
operator|==
literal|null
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
operator|!
name|translateContextPath
condition|)
block|{
return|return
name|path
return|;
block|}
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

