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
name|io
operator|.
name|File
import|;
end_import

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
name|io
operator|.
name|InputStream
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
name|Arrays
import|;
end_import

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
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|LinkedList
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
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|compendium
operator|.
name|cm
operator|.
name|CmNamespaceHandler
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
name|BlueprintEvent
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
comment|/**      * Override this method if you don't want CamelBlueprintTestSupport create the test bundle      * @return includeTestBundle      * If the return value is true CamelBlueprintTestSupport creates the test bundle which includes blueprint configuration files      * If the return value is false CamelBlueprintTestSupport won't create the test bundle      */
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
comment|/**      *<p>Override this method if you want to start Blueprint containers asynchronously using the thread      * that starts the bundles itself.      * By default this method returns<code>true</code> which means Blueprint Extender will use thread pool      * (threads named "<code>Blueprint Extender: N</code>") to startup Blueprint containers.</p>      *<p>Karaf and Fuse OSGi containers use synchronous startup.</p>      *<p>Asynchronous startup is more in the<em>spirit</em> of OSGi and usually means that if everything works fine      * asynchronously, it'll work synchronously as well. This isn't always true otherwise.</p>      * @return      */
DECL|method|useAsynchronousBlueprintStartup ()
specifier|protected
name|boolean
name|useAsynchronousBlueprintStartup
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
name|System
operator|.
name|setProperty
argument_list|(
literal|"org.apache.aries.blueprint.synchronous"
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
operator|!
name|useAsynchronousBlueprintStartup
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
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
condition|)
block|{
if|if
condition|(
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
operator|!
operator|new
name|File
argument_list|(
name|file
index|[
literal|0
index|]
argument_list|)
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The provided file \""
operator|+
name|file
index|[
literal|0
index|]
operator|+
literal|"\" from loadConfigAdminConfigurationFile doesn't exist"
argument_list|)
throw|;
block|}
block|}
specifier|final
name|String
name|symbolicName
init|=
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
decl_stmt|;
specifier|final
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
argument_list|,
name|file
argument_list|)
decl_stmt|;
name|boolean
name|expectReload
init|=
name|expectBlueprintContainerReloadOnConfigAdminUpdate
argument_list|()
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
name|List
argument_list|<
name|KeyValueHolder
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
argument_list|>
name|servicesList
init|=
operator|new
name|LinkedList
argument_list|<
name|KeyValueHolder
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
name|servicesList
operator|.
name|add
argument_list|(
name|asKeyValueService
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|addServicesOnStartup
argument_list|(
name|servicesList
argument_list|)
expr_stmt|;
for|for
control|(
name|KeyValueHolder
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
name|item
range|:
name|servicesList
control|)
block|{
name|String
name|clazz
init|=
name|item
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|service
init|=
name|item
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
name|item
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
comment|// if blueprint XML uses<cm:property-placeholder> (any update-strategy and any default properties)
comment|// - org.apache.aries.blueprint.compendium.cm.ManagedObjectManager.register() is called
comment|// - ManagedServiceUpdate is scheduled in felix.cm
comment|// - org.apache.felix.cm.impl.ConfigurationImpl.setDynamicBundleLocation() is called
comment|// - CM_LOCATION_CHANGED event is fired
comment|// - if BP was alredy created, it's<cm:property-placeholder> receives the event and
comment|// - org.apache.aries.blueprint.compendium.cm.CmPropertyPlaceholder.updated() is called,
comment|//   but no BP reload occurs
comment|// we will however wait for BP container of the test bundle to become CREATED for the first time
comment|// each configadmin update *may* lead to reload of BP container, if it uses<cm:property-placeholder>
comment|// with update-strategy="reload"
comment|// we will gather timestamps of BP events. We don't want to be fooled but repeated events related
comment|// to the same state of BP container
name|Set
argument_list|<
name|Long
argument_list|>
name|bpEvents
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|CamelBlueprintHelper
operator|.
name|waitForBlueprintContainer
argument_list|(
name|bpEvents
argument_list|,
name|answer
argument_list|,
name|symbolicName
argument_list|,
name|BlueprintEvent
operator|.
name|CREATED
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// must reuse props as we can do both load from .cfg file and override afterwards
specifier|final
name|Dictionary
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
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
comment|// we will update the configuration again
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
comment|// passing null as second argument ties the configuration to correct bundle.
comment|// using single-arg method causes:
comment|// *ERROR* Cannot use configuration xxx.properties for [org.osgi.service.cm.ManagedService, id=N, bundle=N/jar:file:xyz.jar!/]: No visibility to configuration bound to felix-connect
specifier|final
name|Configuration
name|config
init|=
name|configAdmin
operator|.
name|getConfiguration
argument_list|(
name|pid
argument_list|,
literal|null
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
comment|// lets merge configurations
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|currentProperties
init|=
name|config
operator|.
name|getProperties
argument_list|()
decl_stmt|;
specifier|final
name|Dictionary
name|newProps
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
if|if
condition|(
name|currentProperties
operator|==
literal|null
condition|)
block|{
name|currentProperties
operator|=
name|newProps
expr_stmt|;
block|}
for|for
control|(
name|Enumeration
argument_list|<
name|String
argument_list|>
name|ek
init|=
name|currentProperties
operator|.
name|keys
argument_list|()
init|;
name|ek
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|String
name|k
init|=
name|ek
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|newProps
operator|.
name|put
argument_list|(
name|k
argument_list|,
name|currentProperties
operator|.
name|get
argument_list|(
name|k
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|p
range|:
operator|(
operator|(
name|Properties
operator|)
name|props
operator|)
operator|.
name|stringPropertyNames
argument_list|()
control|)
block|{
name|newProps
operator|.
name|put
argument_list|(
name|p
argument_list|,
operator|(
operator|(
name|Properties
operator|)
name|props
operator|)
operator|.
name|getProperty
argument_list|(
name|p
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|info
argument_list|(
literal|"Updating ConfigAdmin {} by overriding properties {}"
argument_list|,
name|config
argument_list|,
name|newProps
argument_list|)
expr_stmt|;
if|if
condition|(
name|expectReload
condition|)
block|{
name|CamelBlueprintHelper
operator|.
name|waitForBlueprintContainer
argument_list|(
name|bpEvents
argument_list|,
name|answer
argument_list|,
name|symbolicName
argument_list|,
name|BlueprintEvent
operator|.
name|CREATED
argument_list|,
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|config
operator|.
name|update
argument_list|(
name|newProps
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|config
operator|.
name|update
argument_list|(
name|newProps
argument_list|)
expr_stmt|;
block|}
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
comment|// we don't have to wait for BP container's OSGi service - we've already waited
comment|// for BlueprintEvent.CREATED
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
comment|/**      * This method may be overriden to instruct BP test support that BP container will reloaded when      * Config Admin configuration is updated. By default, this is expected, when blueprint XML definition      * contains<code>&lt;cm:property-placeholder persistent-id="PID" update-strategy="reload"&gt;</code>      */
DECL|method|expectBlueprintContainerReloadOnConfigAdminUpdate ()
specifier|protected
name|boolean
name|expectBlueprintContainerReloadOnConfigAdminUpdate
parameter_list|()
block|{
name|boolean
name|expectedReload
init|=
literal|false
decl_stmt|;
name|DocumentBuilderFactory
name|dbf
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|dbf
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
comment|// cm-1.0 doesn't define update-strategy attribute
name|Set
argument_list|<
name|String
argument_list|>
name|cmNamesaces
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|CmNamespaceHandler
operator|.
name|BLUEPRINT_CM_NAMESPACE_1_1
argument_list|,
name|CmNamespaceHandler
operator|.
name|BLUEPRINT_CM_NAMESPACE_1_2
argument_list|,
name|CmNamespaceHandler
operator|.
name|BLUEPRINT_CM_NAMESPACE_1_3
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|URL
name|descriptor
range|:
name|CamelBlueprintHelper
operator|.
name|getBlueprintDescriptors
argument_list|(
name|getBlueprintDescriptor
argument_list|()
argument_list|)
control|)
block|{
name|DocumentBuilder
name|db
init|=
name|dbf
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
try|try
init|(
name|InputStream
name|is
init|=
name|descriptor
operator|.
name|openStream
argument_list|()
init|)
block|{
name|Document
name|doc
init|=
name|db
operator|.
name|parse
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|NodeList
name|nl
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|nl
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|node
init|=
name|nl
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|pp
init|=
operator|(
name|Element
operator|)
name|node
decl_stmt|;
if|if
condition|(
name|cmNamesaces
operator|.
name|contains
argument_list|(
name|pp
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|us
init|=
name|pp
operator|.
name|getAttribute
argument_list|(
literal|"update-strategy"
argument_list|)
decl_stmt|;
if|if
condition|(
name|us
operator|!=
literal|null
operator|&&
name|us
operator|.
name|equals
argument_list|(
literal|"reload"
argument_list|)
condition|)
block|{
name|expectedReload
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
block|}
block|}
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
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|expectedReload
return|;
block|}
comment|/**      * Override this method to add services to be registered on startup.      *<p/>      * You can use the builder methods {@link #asKeyValueService(String, Object, Dictionary)}      * to make it easy to add the services to the List.      */
DECL|method|addServicesOnStartup (List<KeyValueHolder<String, KeyValueHolder<Object, Dictionary>>> services)
specifier|protected
name|void
name|addServicesOnStartup
parameter_list|(
name|List
argument_list|<
name|KeyValueHolder
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
argument_list|>
name|services
parameter_list|)
block|{
comment|// noop
block|}
comment|/**      * Creates a holder for the given service, which make it easier to use {@link #addServicesOnStartup(java.util.Map)}      */
DECL|method|asService (Object service, Dictionary dict)
specifier|protected
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
comment|/**      * Creates a holder for the given service, which make it easier to use {@link #addServicesOnStartup(java.util.List)}      */
DECL|method|asKeyValueService (String name, Object service, Dictionary dict)
specifier|protected
name|KeyValueHolder
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
name|asKeyValueService
parameter_list|(
name|String
name|name
parameter_list|,
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
name|String
argument_list|,
name|KeyValueHolder
argument_list|<
name|Object
argument_list|,
name|Dictionary
argument_list|>
argument_list|>
argument_list|(
name|name
argument_list|,
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
argument_list|)
return|;
block|}
comment|/**      * Creates a holder for the given service, which make it easier to use {@link #addServicesOnStartup(java.util.Map)}      */
DECL|method|asService (Object service, String key, String value)
specifier|protected
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
comment|/**      * Override this method to override config admin properties. Overriden properties will be passed to      * {@link Configuration#update(Dictionary)} and may or may not lead to reload of Blueprint container - this      * depends on<code>update-strategy="reload|none"</code> in<code>&lt;cm:property-placeholder&gt;</code>      *      * @param props properties where you add the properties to override      * @return the PID of the OSGi {@link ConfigurationAdmin} which are defined in the Blueprint XML file.      */
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
comment|/**      * Override this method and provide the name of the .cfg configuration file to use for      * ConfigAdmin service. Provided file will be used to initialize ConfigAdmin configuration before Blueprint      * container is loaded.      *      * @return the name of the path for the .cfg file to load, and the persistence-id of the property placeholder.      */
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

