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
name|Properties
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
DECL|field|bundleContext
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
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
name|String
name|symbolicName
init|=
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
decl_stmt|;
name|this
operator|.
name|bundleContext
operator|=
name|CamelBlueprintHelper
operator|.
name|createBundleContext
argument_list|(
name|symbolicName
argument_list|,
name|getBlueprintDescriptor
argument_list|()
argument_list|,
literal|true
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
expr_stmt|;
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
name|bundleContext
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
comment|// allow end users to override config admin service with extra properties
name|Dictionary
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
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
name|getOsgiService
argument_list|(
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
name|super
operator|.
name|setUp
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
comment|/**      * Override this method to override config admin properties.      *      * @param props properties where you add the properties to override      * @return the PID of the OSGi {@link ConfigurationAdmin} which are defined in the Blueprint XML file.      */
DECL|method|useOverridePropertiesWithConfigAdmin (Dictionary props)
specifier|protected
name|String
name|useOverridePropertiesWithConfigAdmin
parameter_list|(
name|Dictionary
name|props
parameter_list|)
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
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|CamelBlueprintHelper
operator|.
name|disposeBundleContext
argument_list|(
name|bundleContext
argument_list|)
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
comment|/**      * Gets the bundle directives.      * Modify this method if you wish to add some directives.      * @return      */
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
decl_stmt|;
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

