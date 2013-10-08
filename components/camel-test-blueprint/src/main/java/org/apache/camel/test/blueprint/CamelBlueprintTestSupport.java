begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|blueprint
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Dictionary
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
name|Set
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
name|model
operator|.
name|ModelCamelContext
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
name|KeyValueHolder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|ServiceRegistration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|BlueprintContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|ConfigurationAdmin
import|;
end_import

begin_comment
comment|/**  * Base class for OSGi Blueprint unit tests with Camel.  */
end_comment

begin_class
DECL|class|CamelBlueprintTestSupport
specifier|public
specifier|abstract
class|class
name|CamelBlueprintTestSupport
extends|extends
name|CamelTestSupport
block|{
comment|/** Name of a system property that sets camel context creation timeout. */
DECL|field|SPROP_CAMEL_CONTEXT_CREATION_TIMEOUT
specifier|public
specifier|static
specifier|final
name|String
name|SPROP_CAMEL_CONTEXT_CREATION_TIMEOUT
init|=
literal|"org.apache.camel.test.blueprint.camelContextCreationTimeout"
decl_stmt|;
DECL|field|threadLocalBundleContext
specifier|private
specifier|static
name|ThreadLocal
argument_list|<
name|BundleContext
argument_list|>
name|threadLocalBundleContext
init|=
operator|new
name|ThreadLocal
argument_list|<
name|BundleContext
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|bundleContext
specifier|private
specifier|volatile
name|BundleContext
name|bundleContext
decl_stmt|;
DECL|field|services
specifier|private
specifier|final
name|Set
argument_list|<
name|ServiceRegistration
argument_list|<
name|?
argument_list|>
argument_list|>
name|services
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|ServiceRegistration
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Override this method if you don't want CamelBlueprintTestSupport create the test bundle      * @return includeTestBundle      * If the return value is true CamelBlueprintTestSupport creates the test bundle which includes blueprint configuration files      * If the return value is false CamelBlueprintTestSupport won't create the test bundle      *       */
DECL|method|includeTestBundle ()
specifier|protected
name|boolean
name|includeTestBundle
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
DECL|method|createBundleContext ()
specifier|protected
name|BundleContext
name|createBundleContext
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|symbolicName
init|=
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
decl_stmt|;
name|BundleContext
name|answer
init|=
name|CamelBlueprintHelper
operator|.
name|createBundleContext
argument_list|(
name|symbolicName
argument_list|,
name|getBlueprintDescriptor
argument_list|()
argument_list|,
name|includeTestBundle
argument_list|()
argument_list|,
name|getBundleFilter
argument_list|()
argument_list|,
name|getBundleVersion
argument_list|()
argument_list|,
name|getBundleDirectives
argument_list|()
argument_list|)
decl_stmt|;
comment|// must register override properties early in OSGi containers
name|Properties
name|extra
init|=
name|useOverridePropertiesWithPropertiesComponent
argument_list|()
decl_stmt|;
if|if
condition|(
name|extra
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|registerService
argument_list|(
name|PropertiesComponent
operator|.
name|OVERRIDE_PROPERTIES
argument_list|,
name|extra
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|KeyValueHolder
argument_list|<
name|Object
argument_list|,
name|Dictionary
argument_list|>
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|KeyValueHolder
argument_list|<
name|Object
argument_list|,
name|Dictionary
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|addServicesOnStartup
argument_list|(
name|map
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|KeyValueHolder
argument_list|<
name|Object
argument_list|,
name|Dictionary
argument_list|>
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|clazz
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|service
init|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Dictionary
name|dict
init|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Registering service {} -> {}"
argument_list|,
name|clazz
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|ServiceRegistration
argument_list|<
name|?
argument_list|>
name|reg
init|=
name|answer
operator|.
name|registerService
argument_list|(
name|clazz
argument_list|,
name|service
argument_list|,
name|dict
argument_list|)
decl_stmt|;
if|if
condition|(
name|reg
operator|!=
literal|null
condition|)
block|{
name|services
operator|.
name|add
argument_list|(
name|reg
argument_list|)
expr_stmt|;
block|}
block|}
comment|// must reuse props as we can do both load from .cfg file and override afterwards
name|Dictionary
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
comment|// load configuration file
name|String
index|[]
name|file
init|=
name|loadConfigAdminConfigurationFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
operator|&&
name|file
operator|.
name|length
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The returned String[] from loadConfigAdminConfigurationFile must be of length 2, was "
operator|+
name|file
operator|.
name|length
argument_list|)
throw|;
block|}
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
name|CamelBlueprintHelper
operator|.
name|setPersistentFileForConfigAdmin
argument_list|(
name|answer
argument_list|,
name|file
index|[
literal|1
index|]
argument_list|,
name|file
index|[
literal|0
index|]
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
comment|// allow end user to override properties
name|String
name|pid
init|=
name|useOverridePropertiesWithConfigAdmin
argument_list|(
name|props
argument_list|)
decl_stmt|;
if|if
condition|(
name|pid
operator|!=
literal|null
condition|)
block|{
name|ConfigurationAdmin
name|configAdmin
init|=
name|CamelBlueprintHelper
operator|.
name|getOsgiService
argument_list|(
name|answer
argument_list|,
name|ConfigurationAdmin
operator|.
name|class
argument_list|)
decl_stmt|;
name|Configuration
name|config
init|=
name|configAdmin
operator|.
name|getConfiguration
argument_list|(
name|pid
argument_list|)
decl_stmt|;
if|if
condition|(
name|config
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot find configuration with pid "
operator|+
name|pid
operator|+
literal|" in OSGi ConfigurationAdmin service."
argument_list|)
throw|;
block|}
name|log
operator|.
name|info
argument_list|(
literal|"Updating ConfigAdmin {} by overriding properties {}"
argument_list|,
name|config
argument_list|,
name|props
argument_list|)
expr_stmt|;
name|config
operator|.
name|update
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Before
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"skipStartingCamelContext"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"registerBlueprintCamelContextEager"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|String
name|symbolicName
init|=
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
decl_stmt|;
if|if
condition|(
name|isCreateCamelContextPerClass
argument_list|()
condition|)
block|{
comment|// test is per class, so only setup once (the first time)
name|boolean
name|first
init|=
name|threadLocalBundleContext
operator|.
name|get
argument_list|()
operator|==
literal|null
decl_stmt|;
if|if
condition|(
name|first
condition|)
block|{
name|threadLocalBundleContext
operator|.
name|set
argument_list|(
name|createBundleContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|bundleContext
operator|=
name|threadLocalBundleContext
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|bundleContext
operator|=
name|createBundleContext
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
comment|// start context when we are ready
name|log
operator|.
name|debug
argument_list|(
literal|"Staring CamelContext: {}"
argument_list|,
name|context
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// must wait for blueprint container to be published then the namespace parser is complete and we are ready for testing
name|log
operator|.
name|debug
argument_list|(
literal|"Waiting for BlueprintContainer to be published with symbolicName: {}"
argument_list|,
name|symbolicName
argument_list|)
expr_stmt|;
name|getOsgiService
argument_list|(
name|BlueprintContainer
operator|.
name|class
argument_list|,
literal|"(osgi.blueprint.container.symbolicname="
operator|+
name|symbolicName
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Override this method to add services to be registered on startup.      *<p/>      * You can use the builder methods {@link #asService(Object, java.util.Dictionary)}, {@link #asService(Object, String, String)}      * to make it easy to add the services to the map.      */
DECL|method|addServicesOnStartup (Map<String, KeyValueHolder<Object, Dictionary>> services)
specifier|protected
name|void
name|addServicesOnStartup
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|KeyValueHolder
argument_list|<
name|Object
argument_list|,
name|Dictionary
argument_list|>
argument_list|>
name|services
parameter_list|)
block|{
comment|// noop
block|}
comment|/**      * Creates a holder for the given service, which make it easier to use {@link #addServicesOnStartup(java.util.Map)}      */
DECL|method|asService (Object service, Dictionary dict)
name|KeyValueHolder
argument_list|<
name|Object
argument_list|,
name|Dictionary
argument_list|>
name|asService
parameter_list|(
name|Object
name|service
parameter_list|,
name|Dictionary
name|dict
parameter_list|)
block|{
return|return
operator|new
name|KeyValueHolder
argument_list|<
name|Object
argument_list|,
name|Dictionary
argument_list|>
argument_list|(
name|service
argument_list|,
name|dict
argument_list|)
return|;
block|}
comment|/**      * Creates a holder for the given service, which make it easier to use {@link #addServicesOnStartup(java.util.Map)}      */
DECL|method|asService (Object service, String key, String value)
name|KeyValueHolder
argument_list|<
name|Object
argument_list|,
name|Dictionary
argument_list|>
name|asService
parameter_list|(
name|Object
name|service
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|Properties
name|prop
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
operator|&&
name|value
operator|!=
literal|null
condition|)
block|{
name|prop
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|KeyValueHolder
argument_list|<
name|Object
argument_list|,
name|Dictionary
argument_list|>
argument_list|(
name|service
argument_list|,
name|prop
argument_list|)
return|;
block|}
comment|/**      * Override this method to override config admin properties.      *      * @param props properties where you add the properties to override      * @return the PID of the OSGi {@link ConfigurationAdmin} which are defined in the Blueprint XML file.      */
DECL|method|useOverridePropertiesWithConfigAdmin (Dictionary props)
specifier|protected
name|String
name|useOverridePropertiesWithConfigAdmin
parameter_list|(
name|Dictionary
name|props
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
comment|/**      * Override this method and provide the name of the .cfg configuration file to use for      * Blueprint ConfigAdmin service.      *      * @return the name of the path for the .cfg file to load, and the persistence-id of the property placeholder.      */
DECL|method|loadConfigAdminConfigurationFile ()
specifier|protected
name|String
index|[]
name|loadConfigAdminConfigurationFile
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|After
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|clearProperty
argument_list|(
literal|"skipStartingCamelContext"
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"registerBlueprintCamelContextEager"
argument_list|)
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|isCreateCamelContextPerClass
argument_list|()
condition|)
block|{
comment|// we tear down in after class
return|return;
block|}
comment|// unregister services
if|if
condition|(
name|bundleContext
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ServiceRegistration
argument_list|<
name|?
argument_list|>
name|reg
range|:
name|services
control|)
block|{
name|bundleContext
operator|.
name|ungetService
argument_list|(
name|reg
operator|.
name|getReference
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|CamelBlueprintHelper
operator|.
name|disposeBundleContext
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|tearDownAfterClass ()
specifier|public
specifier|static
name|void
name|tearDownAfterClass
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|threadLocalBundleContext
operator|.
name|get
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|CamelBlueprintHelper
operator|.
name|disposeBundleContext
argument_list|(
name|threadLocalBundleContext
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|threadLocalBundleContext
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
name|CamelTestSupport
operator|.
name|tearDownAfterClass
argument_list|()
expr_stmt|;
block|}
comment|/**      * Return the system bundle context      */
DECL|method|getBundleContext ()
specifier|protected
name|BundleContext
name|getBundleContext
parameter_list|()
block|{
return|return
name|bundleContext
return|;
block|}
comment|/**      * Gets the bundle descriptor from the classpath.      *<p/>      * Return the location(s) of the bundle descriptors from the classpath.      * Separate multiple locations by comma, or return a single location.      *<p/>      * For example override this method and return<tt>OSGI-INF/blueprint/camel-context.xml</tt>      *      * @return the location of the bundle descriptor file.      */
DECL|method|getBlueprintDescriptor ()
specifier|protected
name|String
name|getBlueprintDescriptor
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * Gets filter expression of bundle descriptors.      * Modify this method if you wish to change default behavior.      *      * @return filter expression for OSGi bundles.      */
DECL|method|getBundleFilter ()
specifier|protected
name|String
name|getBundleFilter
parameter_list|()
block|{
return|return
name|CamelBlueprintHelper
operator|.
name|BUNDLE_FILTER
return|;
block|}
comment|/**      * Gets test bundle version.      * Modify this method if you wish to change default behavior.      *      * @return test bundle version      */
DECL|method|getBundleVersion ()
specifier|protected
name|String
name|getBundleVersion
parameter_list|()
block|{
return|return
name|CamelBlueprintHelper
operator|.
name|BUNDLE_VERSION
return|;
block|}
comment|/**      * Gets the bundle directives.      *<p/>      * Modify this method if you wish to add some directives.      */
DECL|method|getBundleDirectives ()
specifier|protected
name|String
name|getBundleDirectives
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * Returns how long to wait for Camel Context      * to be created.      *       * @return timeout in milliseconds.      */
DECL|method|getCamelContextCreationTimeout ()
specifier|protected
name|Long
name|getCamelContextCreationTimeout
parameter_list|()
block|{
name|String
name|tm
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|SPROP_CAMEL_CONTEXT_CREATION_TIMEOUT
argument_list|)
decl_stmt|;
if|if
condition|(
name|tm
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
name|Long
name|val
init|=
name|Long
operator|.
name|valueOf
argument_list|(
name|tm
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Value of "
operator|+
name|SPROP_CAMEL_CONTEXT_CREATION_TIMEOUT
operator|+
literal|" cannot be negative."
argument_list|)
throw|;
block|}
return|return
name|val
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Value of "
operator|+
name|SPROP_CAMEL_CONTEXT_CREATION_TIMEOUT
operator|+
literal|" has wrong format."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|answer
init|=
literal|null
decl_stmt|;
name|Long
name|timeout
init|=
name|getCamelContextCreationTimeout
argument_list|()
decl_stmt|;
if|if
condition|(
name|timeout
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|CamelBlueprintHelper
operator|.
name|getOsgiService
argument_list|(
name|bundleContext
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|timeout
operator|>=
literal|0
condition|)
block|{
name|answer
operator|=
name|CamelBlueprintHelper
operator|.
name|getOsgiService
argument_list|(
name|bundleContext
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"getCamelContextCreationTimeout cannot return a negative value."
argument_list|)
throw|;
block|}
comment|// must override context so we use the correct one in testing
name|context
operator|=
operator|(
name|ModelCamelContext
operator|)
name|answer
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|getOsgiService (Class<T> type)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getOsgiService
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|CamelBlueprintHelper
operator|.
name|getOsgiService
argument_list|(
name|bundleContext
argument_list|,
name|type
argument_list|)
return|;
block|}
DECL|method|getOsgiService (Class<T> type, long timeout)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getOsgiService
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
return|return
name|CamelBlueprintHelper
operator|.
name|getOsgiService
argument_list|(
name|bundleContext
argument_list|,
name|type
argument_list|,
name|timeout
argument_list|)
return|;
block|}
DECL|method|getOsgiService (Class<T> type, String filter)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getOsgiService
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|String
name|filter
parameter_list|)
block|{
return|return
name|CamelBlueprintHelper
operator|.
name|getOsgiService
argument_list|(
name|bundleContext
argument_list|,
name|type
argument_list|,
name|filter
argument_list|)
return|;
block|}
DECL|method|getOsgiService (Class<T> type, String filter, long timeout)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getOsgiService
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|String
name|filter
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
return|return
name|CamelBlueprintHelper
operator|.
name|getOsgiService
argument_list|(
name|bundleContext
argument_list|,
name|type
argument_list|,
name|filter
argument_list|,
name|timeout
argument_list|)
return|;
block|}
block|}
end_class

end_unit

