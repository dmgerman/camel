begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servletlistener
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servletlistener
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletContextEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletContextListener
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
name|RoutesBuilder
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
name|builder
operator|.
name|RouteBuilder
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
name|RouteDefinition
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
name|RoutesDefinition
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
name|IOHelper
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
name|IntrospectionSupport
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ResourceHelper
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
name|jndi
operator|.
name|JndiContext
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

begin_comment
comment|/**  * A {@link ServletContextListener} which is used to bootstrap  * {@link org.apache.camel.CamelContext} in web applications.  */
end_comment

begin_class
DECL|class|CamelContextServletListener
specifier|public
class|class
name|CamelContextServletListener
implements|implements
name|ServletContextListener
block|{
comment|// TODO: Allow to lookup and configure some of the stuff we do in camel-core-xml
comment|// more easily with this listener as well
comment|// TODO: Add more tests
comment|// TODO: Allow to lookup route builders using package scanning
DECL|field|instance
specifier|public
specifier|static
name|ServletCamelContext
name|instance
decl_stmt|;
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
name|CamelContextServletListener
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|jndiContext
specifier|private
name|JndiContext
name|jndiContext
decl_stmt|;
DECL|field|camelContext
specifier|private
name|ServletCamelContext
name|camelContext
decl_stmt|;
DECL|field|camelContextLifecycle
specifier|private
name|CamelContextLifecycle
name|camelContextLifecycle
decl_stmt|;
DECL|field|test
specifier|private
name|boolean
name|test
decl_stmt|;
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|contextInitialized (ServletContextEvent sce)
specifier|public
name|void
name|contextInitialized
parameter_list|(
name|ServletContextEvent
name|sce
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"CamelContextServletListener initializing ..."
argument_list|)
expr_stmt|;
comment|// create jndi and camel context
try|try
block|{
name|jndiContext
operator|=
operator|new
name|JndiContext
argument_list|()
expr_stmt|;
name|camelContext
operator|=
operator|new
name|ServletCamelContext
argument_list|(
name|jndiContext
argument_list|,
name|sce
operator|.
name|getServletContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Error creating CamelContext."
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error creating CamelContext."
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// get the init parameters
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|extractInitParameters
argument_list|(
name|sce
argument_list|)
decl_stmt|;
comment|// special for test parameter
name|String
name|test
init|=
operator|(
name|String
operator|)
name|map
operator|.
name|remove
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
if|if
condition|(
name|test
operator|!=
literal|null
operator|&&
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|test
argument_list|)
condition|)
block|{
name|this
operator|.
name|test
operator|=
literal|true
expr_stmt|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"In test mode? {}"
argument_list|,
name|this
operator|.
name|test
argument_list|)
expr_stmt|;
comment|// set properties on the camel context from the init parameters
if|if
condition|(
operator|!
name|map
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
try|try
block|{
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|camelContext
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error setting init parameters on CamelContext."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|// get the routes and add to the CamelContext
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|routes
init|=
name|extractRoutes
argument_list|(
name|map
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|routes
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|instanceof
name|RouteBuilder
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Adding route(s) {} -> {}"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|camelContext
operator|.
name|addRoutes
argument_list|(
operator|(
name|RoutesBuilder
operator|)
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error adding route(s) "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|instanceof
name|Set
condition|)
block|{
comment|// its a set of route builders
for|for
control|(
name|Object
name|clazz
range|:
operator|(
name|Set
operator|)
name|entry
operator|.
name|getValue
argument_list|()
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Adding route(s) {} -> {}"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
try|try
block|{
name|camelContext
operator|.
name|addRoutes
argument_list|(
operator|(
name|RoutesBuilder
operator|)
name|clazz
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error adding route(s) "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|instanceof
name|RoutesDefinition
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Adding routes {} -> {}"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|camelContext
operator|.
name|addRouteDefinitions
argument_list|(
operator|(
operator|(
name|RoutesDefinition
operator|)
name|entry
operator|.
name|getValue
argument_list|()
operator|)
operator|.
name|getRoutes
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error adding route(s) "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|instanceof
name|RouteDefinition
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Adding routes {} -> {}"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|camelContext
operator|.
name|addRouteDefinition
argument_list|(
operator|(
name|RouteDefinition
operator|)
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error adding route(s) "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported route "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|" of type: "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|// any custom CamelContextLifecycle
name|String
name|lifecycle
init|=
operator|(
name|String
operator|)
name|map
operator|.
name|remove
argument_list|(
name|CamelContextLifecycle
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|lifecycle
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Class
argument_list|<
name|CamelContextLifecycle
argument_list|>
name|clazz
init|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|lifecycle
argument_list|,
name|CamelContextLifecycle
operator|.
name|class
argument_list|)
decl_stmt|;
name|camelContextLifecycle
operator|=
name|camelContext
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error creating CamelContextLifecycle class with name "
operator|+
name|lifecycle
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|// validate that we could set all the init parameters
if|if
condition|(
operator|!
name|map
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Error setting init parameters on CamelContext."
operator|+
literal|" There are "
operator|+
name|map
operator|.
name|size
argument_list|()
operator|+
literal|" unknown parameters. ["
operator|+
name|map
operator|+
literal|"]"
argument_list|)
throw|;
block|}
try|try
block|{
if|if
condition|(
name|camelContextLifecycle
operator|!=
literal|null
condition|)
block|{
name|camelContextLifecycle
operator|.
name|beforeStart
argument_list|(
name|camelContext
argument_list|,
name|jndiContext
argument_list|)
expr_stmt|;
block|}
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
if|if
condition|(
name|camelContextLifecycle
operator|!=
literal|null
condition|)
block|{
name|camelContextLifecycle
operator|.
name|afterStart
argument_list|(
name|camelContext
argument_list|,
name|jndiContext
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
name|error
argument_list|(
literal|"Error starting CamelContext."
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error starting CamelContext."
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|this
operator|.
name|test
condition|)
block|{
name|instance
operator|=
name|camelContext
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"CamelContextServletListener initialized"
argument_list|)
expr_stmt|;
block|}
DECL|method|extractRoutes (Map<String, Object> map)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|extractRoutes
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|routes
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|UK
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"routebuilder"
argument_list|)
condition|)
block|{
name|String
name|value
init|=
operator|(
name|String
operator|)
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|Object
name|target
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|value
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
comment|// a reference lookup in jndi
name|value
operator|=
name|value
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|target
operator|=
name|lookupJndi
argument_list|(
name|jndiContext
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ResourceHelper
operator|.
name|hasScheme
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
try|try
block|{
name|is
operator|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|camelContext
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|target
operator|=
name|camelContext
operator|.
name|loadRoutesDefinition
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error loading routes from resource: "
operator|+
name|value
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|value
operator|.
name|startsWith
argument_list|(
literal|"packagescan:"
argument_list|)
condition|)
block|{
comment|// using package scanning
name|String
name|path
init|=
name|value
operator|.
name|substring
argument_list|(
literal|12
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
name|camelContext
operator|.
name|getPackageScanClassResolver
argument_list|()
operator|.
name|findImplementations
argument_list|(
name|RouteBuilder
operator|.
name|class
argument_list|,
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|classes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Set
argument_list|<
name|RouteBuilder
argument_list|>
name|builders
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|RouteBuilder
argument_list|>
argument_list|()
decl_stmt|;
name|target
operator|=
name|builders
expr_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
range|:
name|classes
control|)
block|{
try|try
block|{
name|RouteBuilder
name|route
init|=
operator|(
name|RouteBuilder
operator|)
name|camelContext
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
name|builders
operator|.
name|add
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error creating RouteBuilder "
operator|+
name|clazz
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
else|else
block|{
comment|// assume its a FQN classname for a RouteBuilder class
try|try
block|{
name|Class
argument_list|<
name|RouteBuilder
argument_list|>
name|clazz
init|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|value
argument_list|,
name|RouteBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
name|target
operator|=
name|camelContext
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error creating RouteBuilder "
operator|+
name|value
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|target
operator|!=
literal|null
condition|)
block|{
name|routes
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// after adding the route builders we should remove them from the map
for|for
control|(
name|String
name|name
range|:
name|routes
operator|.
name|keySet
argument_list|()
control|)
block|{
name|map
operator|.
name|remove
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|routes
return|;
block|}
DECL|method|extractInitParameters (ServletContextEvent sce)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|extractInitParameters
parameter_list|(
name|ServletContextEvent
name|sce
parameter_list|)
block|{
comment|// configure CamelContext with the init parameter
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Enumeration
name|names
init|=
name|sce
operator|.
name|getServletContext
argument_list|()
operator|.
name|getInitParameterNames
argument_list|()
decl_stmt|;
while|while
condition|(
name|names
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
name|names
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|sce
operator|.
name|getServletContext
argument_list|()
operator|.
name|getInitParameter
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|Object
name|target
init|=
name|value
decl_stmt|;
if|if
condition|(
name|value
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
comment|// a reference lookup in jndi
name|value
operator|=
name|value
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|target
operator|=
name|lookupJndi
argument_list|(
name|jndiContext
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
name|map
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|map
return|;
block|}
DECL|method|lookupJndi (JndiContext jndiContext, String name)
specifier|private
specifier|static
name|Object
name|lookupJndi
parameter_list|(
name|JndiContext
name|jndiContext
parameter_list|,
name|String
name|name
parameter_list|)
block|{
try|try
block|{
return|return
name|jndiContext
operator|.
name|lookup
argument_list|(
name|name
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NamingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error looking up in jndi with name: "
operator|+
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|contextDestroyed (ServletContextEvent sce)
specifier|public
name|void
name|contextDestroyed
parameter_list|(
name|ServletContextEvent
name|sce
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"CamelContextServletListener destroying ..."
argument_list|)
expr_stmt|;
if|if
condition|(
name|camelContext
operator|!=
literal|null
condition|)
block|{
try|try
block|{
if|if
condition|(
name|camelContextLifecycle
operator|!=
literal|null
condition|)
block|{
name|camelContextLifecycle
operator|.
name|beforeStop
argument_list|(
name|camelContext
argument_list|,
name|jndiContext
argument_list|)
expr_stmt|;
block|}
name|camelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
if|if
condition|(
name|camelContextLifecycle
operator|!=
literal|null
condition|)
block|{
name|camelContextLifecycle
operator|.
name|afterStop
argument_list|(
name|camelContext
argument_list|,
name|jndiContext
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
literal|"Error stopping CamelContext. This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|camelContext
operator|=
literal|null
expr_stmt|;
name|jndiContext
operator|=
literal|null
expr_stmt|;
name|instance
operator|=
literal|null
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"CamelContextServletListener destroyed"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

