begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.scr
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|scr
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|ParameterizedType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledFuture
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|CamelContext
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
name|component
operator|.
name|properties
operator|.
name|PropertiesComponent
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
name|core
operator|.
name|osgi
operator|.
name|OsgiCamelContextPublisher
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
name|core
operator|.
name|osgi
operator|.
name|OsgiDefaultCamelContext
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
name|core
operator|.
name|osgi
operator|.
name|utils
operator|.
name|BundleDelegatingClassLoader
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
name|DefaultCamelContext
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
name|ExplicitCamelContextNameStrategy
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
name|SimpleRegistry
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
name|ReflectionHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceReference
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

begin_class
DECL|class|AbstractCamelRunner
specifier|public
specifier|abstract
class|class
name|AbstractCamelRunner
implements|implements
name|Runnable
block|{
DECL|field|START_DELAY
specifier|public
specifier|static
specifier|final
name|int
name|START_DELAY
init|=
literal|5000
decl_stmt|;
DECL|field|PROPERTY_PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|PROPERTY_PREFIX
init|=
literal|"camel.scr.properties.prefix"
decl_stmt|;
DECL|field|log
specifier|protected
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|context
specifier|protected
name|CamelContext
name|context
decl_stmt|;
DECL|field|registry
specifier|protected
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
comment|// Configured fields
DECL|field|camelContextId
specifier|private
name|String
name|camelContextId
decl_stmt|;
DECL|field|active
specifier|private
name|boolean
name|active
decl_stmt|;
DECL|field|executor
specifier|private
name|ScheduledExecutorService
name|executor
init|=
name|Executors
operator|.
name|newSingleThreadScheduledExecutor
argument_list|()
decl_stmt|;
DECL|field|starter
specifier|private
name|ScheduledFuture
name|starter
decl_stmt|;
DECL|field|activated
specifier|private
specifier|volatile
name|boolean
name|activated
decl_stmt|;
DECL|field|started
specifier|private
specifier|volatile
name|boolean
name|started
decl_stmt|;
DECL|method|activate (final BundleContext bundleContext, final Map<String, String> props)
specifier|public
specifier|synchronized
name|void
name|activate
parameter_list|(
specifier|final
name|BundleContext
name|bundleContext
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|activated
condition|)
block|{
return|return;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"activated!"
argument_list|)
expr_stmt|;
name|activated
operator|=
literal|true
expr_stmt|;
name|prepare
argument_list|(
name|bundleContext
argument_list|,
name|props
argument_list|)
expr_stmt|;
name|runWithDelay
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|prepare (final BundleContext bundleContext, final Map<String, String> props)
specifier|public
specifier|synchronized
name|void
name|prepare
parameter_list|(
specifier|final
name|BundleContext
name|bundleContext
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
parameter_list|)
throws|throws
name|Exception
block|{
name|createCamelContext
argument_list|(
name|bundleContext
argument_list|,
name|props
argument_list|)
expr_stmt|;
comment|// Configure fields from properties
name|configure
argument_list|(
name|context
argument_list|,
name|this
argument_list|,
name|log
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|setupCamelContext
argument_list|(
name|bundleContext
argument_list|,
name|camelContextId
argument_list|)
expr_stmt|;
block|}
DECL|method|createCamelContext (final BundleContext bundleContext, final Map<String, String> props)
specifier|protected
name|void
name|createCamelContext
parameter_list|(
specifier|final
name|BundleContext
name|bundleContext
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
parameter_list|)
block|{
if|if
condition|(
name|bundleContext
operator|!=
literal|null
condition|)
block|{
name|context
operator|=
operator|new
name|OsgiDefaultCamelContext
argument_list|(
name|bundleContext
argument_list|,
name|registry
argument_list|)
expr_stmt|;
comment|// Setup the application context classloader with the bundle classloader
name|context
operator|.
name|setApplicationContextClassLoader
argument_list|(
operator|new
name|BundleDelegatingClassLoader
argument_list|(
name|bundleContext
operator|.
name|getBundle
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// and make sure the TCCL is our classloader
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|context
operator|.
name|getApplicationContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
expr_stmt|;
block|}
name|setupPropertiesComponent
argument_list|(
name|context
argument_list|,
name|props
argument_list|,
name|log
argument_list|)
expr_stmt|;
block|}
DECL|method|setupCamelContext (final BundleContext bundleContext, final String camelContextId)
specifier|protected
name|void
name|setupCamelContext
parameter_list|(
specifier|final
name|BundleContext
name|bundleContext
parameter_list|,
specifier|final
name|String
name|camelContextId
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Set up CamelContext
if|if
condition|(
name|camelContextId
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|setNameStrategy
argument_list|(
operator|new
name|ExplicitCamelContextNameStrategy
argument_list|(
name|camelContextId
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|setUseMDCLogging
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|setUseBreadcrumb
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Add routes
for|for
control|(
name|RoutesBuilder
name|route
range|:
name|getRouteBuilders
argument_list|()
control|)
block|{
name|context
operator|.
name|addRoutes
argument_list|(
name|configure
argument_list|(
name|context
argument_list|,
name|route
argument_list|,
name|log
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// ensure we publish this CamelContext to the OSGi service registry
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|addEventNotifier
argument_list|(
operator|new
name|OsgiCamelContextPublisher
argument_list|(
name|bundleContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|setupPropertiesComponent (final CamelContext context, final Map<String, String> props, Logger log)
specifier|public
specifier|static
name|void
name|setupPropertiesComponent
parameter_list|(
specifier|final
name|CamelContext
name|context
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
parameter_list|,
name|Logger
name|log
parameter_list|)
block|{
comment|// Set up PropertiesComponent
name|PropertiesComponent
name|pc
init|=
operator|new
name|PropertiesComponent
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
operator|.
name|getComponentNames
argument_list|()
operator|.
name|contains
argument_list|(
literal|"properties"
argument_list|)
condition|)
block|{
name|pc
operator|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"properties"
argument_list|,
name|PropertiesComponent
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|context
operator|.
name|addComponent
argument_list|(
literal|"properties"
argument_list|,
name|pc
argument_list|)
expr_stmt|;
block|}
comment|// Set property prefix
if|if
condition|(
literal|null
operator|!=
name|System
operator|.
name|getProperty
argument_list|(
name|PROPERTY_PREFIX
argument_list|)
condition|)
block|{
name|pc
operator|.
name|setPropertyPrefix
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
name|PROPERTY_PREFIX
argument_list|)
operator|+
literal|"."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|!=
name|props
condition|)
block|{
name|Properties
name|initialProps
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|initialProps
operator|.
name|putAll
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Added %d initial properties"
argument_list|,
name|props
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|pc
operator|.
name|setInitialProperties
argument_list|(
name|initialProps
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodError
name|e
parameter_list|)
block|{
comment|// For Camel versions without setInitialProperties
name|pc
operator|.
name|setOverrideProperties
argument_list|(
name|initialProps
argument_list|)
expr_stmt|;
name|pc
operator|.
name|setLocation
argument_list|(
literal|"default.properties"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getRouteBuilders ()
specifier|protected
specifier|abstract
name|List
argument_list|<
name|RoutesBuilder
argument_list|>
name|getRouteBuilders
parameter_list|()
function_decl|;
comment|// Run after a delay unless the method is called again
DECL|method|runWithDelay (final Runnable runnable)
specifier|private
name|void
name|runWithDelay
parameter_list|(
specifier|final
name|Runnable
name|runnable
parameter_list|)
block|{
if|if
condition|(
name|activated
operator|&&
operator|!
name|started
condition|)
block|{
name|cancelDelayedRun
argument_list|()
expr_stmt|;
comment|// Run after a delay
name|starter
operator|=
name|executor
operator|.
name|schedule
argument_list|(
name|runnable
argument_list|,
name|START_DELAY
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|cancelDelayedRun ()
specifier|private
name|void
name|cancelDelayedRun
parameter_list|()
block|{
if|if
condition|(
literal|null
operator|!=
name|starter
condition|)
block|{
comment|// Cancel but don't interrupt
name|starter
operator|.
name|cancel
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|run ()
specifier|public
specifier|synchronized
name|void
name|run
parameter_list|()
block|{
name|startCamelContext
argument_list|()
expr_stmt|;
block|}
DECL|method|deactivate ()
specifier|public
specifier|synchronized
name|void
name|deactivate
parameter_list|()
block|{
if|if
condition|(
operator|!
name|activated
condition|)
block|{
return|return;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"deactivated!"
argument_list|)
expr_stmt|;
name|activated
operator|=
literal|false
expr_stmt|;
name|cancelDelayedRun
argument_list|()
expr_stmt|;
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|stop ()
specifier|public
specifier|synchronized
name|void
name|stop
parameter_list|()
block|{
name|stopCamelContext
argument_list|()
expr_stmt|;
block|}
DECL|method|startCamelContext ()
specifier|private
name|void
name|startCamelContext
parameter_list|()
block|{
if|if
condition|(
name|started
condition|)
block|{
return|return;
block|}
try|try
block|{
if|if
condition|(
operator|!
name|active
condition|)
block|{
name|context
operator|.
name|setAutoStartup
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|started
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Failed to start Camel context. Will try again when more Camel components have been registered."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|stopCamelContext ()
specifier|private
name|void
name|stopCamelContext
parameter_list|()
block|{
if|if
condition|(
operator|!
name|started
condition|)
block|{
return|return;
block|}
try|try
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Failed to stop Camel context."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// Even if stopping failed we consider Camel context stopped
name|started
operator|=
literal|false
expr_stmt|;
block|}
block|}
DECL|method|getContext ()
specifier|public
name|CamelContext
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
DECL|method|gotCamelComponent (final ServiceReference serviceReference)
specifier|protected
name|void
name|gotCamelComponent
parameter_list|(
specifier|final
name|ServiceReference
name|serviceReference
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Got a new Camel Component."
argument_list|)
expr_stmt|;
name|runWithDelay
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|lostCamelComponent (final ServiceReference serviceReference)
specifier|protected
name|void
name|lostCamelComponent
parameter_list|(
specifier|final
name|ServiceReference
name|serviceReference
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Lost a Camel Component."
argument_list|)
expr_stmt|;
block|}
DECL|method|configure (final CamelContext context, final T target, final Logger log)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|configure
parameter_list|(
specifier|final
name|CamelContext
name|context
parameter_list|,
specifier|final
name|T
name|target
parameter_list|,
specifier|final
name|Logger
name|log
parameter_list|)
block|{
return|return
name|configure
argument_list|(
name|context
argument_list|,
name|target
argument_list|,
name|log
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|configure (final CamelContext context, final T target, final Logger log, final boolean deep)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|configure
parameter_list|(
specifier|final
name|CamelContext
name|context
parameter_list|,
specifier|final
name|T
name|target
parameter_list|,
specifier|final
name|Logger
name|log
parameter_list|,
specifier|final
name|boolean
name|deep
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|target
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Configuring {}"
argument_list|,
name|clazz
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Field
argument_list|>
name|fields
init|=
operator|new
name|ArrayList
argument_list|<
name|Field
argument_list|>
argument_list|()
decl_stmt|;
name|fields
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|clazz
operator|.
name|getDeclaredFields
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|deep
condition|)
block|{
name|fields
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|clazz
operator|.
name|getFields
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|fields
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|clazz
operator|.
name|getSuperclass
argument_list|()
operator|.
name|getDeclaredFields
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Field
name|field
range|:
name|fields
control|)
block|{
name|String
name|propertyValue
decl_stmt|;
try|try
block|{
name|propertyValue
operator|=
name|context
operator|.
name|resolvePropertyPlaceholders
argument_list|(
literal|"{{"
operator|+
name|field
operator|.
name|getName
argument_list|()
operator|+
literal|"}}"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Skipped field {}"
argument_list|,
name|field
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
try|try
block|{
if|if
condition|(
operator|!
name|propertyValue
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// Try to convert the value and set the field
name|Object
name|convertedValue
init|=
name|convertValue
argument_list|(
name|propertyValue
argument_list|,
name|field
operator|.
name|getGenericType
argument_list|()
argument_list|)
decl_stmt|;
name|ReflectionHelper
operator|.
name|setField
argument_list|(
name|field
argument_list|,
name|target
argument_list|,
name|convertedValue
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Configured field {} with value {}"
argument_list|,
name|field
operator|.
name|getName
argument_list|()
argument_list|,
name|propertyValue
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
name|log
operator|.
name|error
argument_list|(
literal|"Error setting field "
operator|+
name|field
operator|.
name|getName
argument_list|()
operator|+
literal|" due: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|target
return|;
block|}
DECL|method|convertValue (final String value, final Type type)
specifier|public
specifier|static
name|Object
name|convertValue
parameter_list|(
specifier|final
name|String
name|value
parameter_list|,
specifier|final
name|Type
name|type
parameter_list|)
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|type
operator|instanceof
name|ParameterizedType
condition|)
block|{
name|clazz
operator|=
call|(
name|Class
argument_list|<
name|?
argument_list|>
call|)
argument_list|(
operator|(
name|ParameterizedType
operator|)
name|type
argument_list|)
operator|.
name|getRawType
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|instanceof
name|Class
condition|)
block|{
name|clazz
operator|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|type
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|!=
name|value
condition|)
block|{
if|if
condition|(
name|clazz
operator|.
name|isInstance
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|value
return|;
block|}
elseif|else
if|if
condition|(
name|clazz
operator|==
name|String
operator|.
name|class
condition|)
block|{
return|return
name|value
return|;
block|}
elseif|else
if|if
condition|(
name|clazz
operator|==
name|Boolean
operator|.
name|class
operator|||
name|clazz
operator|==
name|boolean
operator|.
name|class
condition|)
block|{
return|return
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|clazz
operator|==
name|Integer
operator|.
name|class
operator|||
name|clazz
operator|==
name|int
operator|.
name|class
condition|)
block|{
return|return
name|Integer
operator|.
name|parseInt
argument_list|(
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|clazz
operator|==
name|Long
operator|.
name|class
operator|||
name|clazz
operator|==
name|long
operator|.
name|class
condition|)
block|{
return|return
name|Long
operator|.
name|parseLong
argument_list|(
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|clazz
operator|==
name|Double
operator|.
name|class
operator|||
name|clazz
operator|==
name|double
operator|.
name|class
condition|)
block|{
return|return
name|Double
operator|.
name|parseDouble
argument_list|(
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|clazz
operator|==
name|File
operator|.
name|class
condition|)
block|{
return|return
operator|new
name|File
argument_list|(
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|clazz
operator|==
name|URI
operator|.
name|class
condition|)
block|{
return|return
operator|new
name|URI
argument_list|(
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|clazz
operator|==
name|URL
operator|.
name|class
condition|)
block|{
return|return
operator|new
name|URL
argument_list|(
name|value
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported type: "
operator|+
operator|(
name|clazz
operator|!=
literal|null
condition|?
name|clazz
operator|.
name|getName
argument_list|()
else|:
literal|null
operator|)
argument_list|)
throw|;
block|}
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

