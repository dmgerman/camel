begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.karaf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|karaf
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
name|InputStream
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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
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
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
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
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
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
name|Component
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
name|DataFormat
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
name|Language
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
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
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|ProbeBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|TestProbeBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|karaf
operator|.
name|options
operator|.
name|LogLevelOption
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|options
operator|.
name|UrlReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|tinybundles
operator|.
name|core
operator|.
name|TinyBundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|tinybundles
operator|.
name|core
operator|.
name|TinyBundles
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
name|Bundle
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
name|BundleException
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
name|Constants
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
name|Filter
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
name|FrameworkUtil
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
name|InvalidSyntaxException
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
name|util
operator|.
name|tracker
operator|.
name|ServiceTracker
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
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|maven
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|mavenBundle
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|vmOption
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|configureConsole
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|editConfigurationFilePut
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|features
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|keepRuntimeFolder
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|logLevel
import|;
end_import

begin_class
DECL|class|AbstractFeatureTest
specifier|public
specifier|abstract
class|class
name|AbstractFeatureTest
block|{
DECL|field|SERVICE_TIMEOUT
specifier|public
specifier|static
specifier|final
name|Long
name|SERVICE_TIMEOUT
init|=
literal|30000L
decl_stmt|;
DECL|field|LOG
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AbstractFeatureTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Inject
DECL|field|bundleContext
specifier|protected
name|BundleContext
name|bundleContext
decl_stmt|;
annotation|@
name|Inject
DECL|field|blueprintContainer
specifier|protected
name|BlueprintContainer
name|blueprintContainer
decl_stmt|;
annotation|@
name|Inject
DECL|field|featuresService
specifier|protected
name|FeaturesService
name|featuresService
decl_stmt|;
annotation|@
name|ProbeBuilder
DECL|method|probeConfiguration (TestProbeBuilder probe)
specifier|public
name|TestProbeBuilder
name|probeConfiguration
parameter_list|(
name|TestProbeBuilder
name|probe
parameter_list|)
block|{
comment|// makes sure the generated Test-Bundle contains this import!
name|probe
operator|.
name|setHeader
argument_list|(
name|Constants
operator|.
name|DYNAMICIMPORT_PACKAGE
argument_list|,
literal|"*"
argument_list|)
expr_stmt|;
return|return
name|probe
return|;
block|}
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"setUp() using BundleContext: {}"
argument_list|,
name|bundleContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"tearDown()"
argument_list|)
expr_stmt|;
block|}
DECL|method|installBlueprintAsBundle (String name, URL url, boolean start)
specifier|protected
name|Bundle
name|installBlueprintAsBundle
parameter_list|(
name|String
name|name
parameter_list|,
name|URL
name|url
parameter_list|,
name|boolean
name|start
parameter_list|)
throws|throws
name|BundleException
block|{
name|TinyBundle
name|bundle
init|=
name|TinyBundles
operator|.
name|bundle
argument_list|()
decl_stmt|;
name|bundle
operator|.
name|add
argument_list|(
literal|"OSGI-INF/blueprint/blueprint-"
operator|+
name|name
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
operator|+
literal|".xml"
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|bundle
operator|.
name|set
argument_list|(
literal|"Manifest-Version"
argument_list|,
literal|"2"
argument_list|)
operator|.
name|set
argument_list|(
literal|"Bundle-ManifestVersion"
argument_list|,
literal|"2"
argument_list|)
operator|.
name|set
argument_list|(
literal|"Bundle-SymbolicName"
argument_list|,
name|name
argument_list|)
operator|.
name|set
argument_list|(
literal|"Bundle-Version"
argument_list|,
literal|"1.0.0"
argument_list|)
expr_stmt|;
name|Bundle
name|answer
init|=
name|bundleContext
operator|.
name|installBundle
argument_list|(
name|name
argument_list|,
name|bundle
operator|.
name|build
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|start
condition|)
block|{
name|answer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|installSpringAsBundle (String name, URL url, boolean start)
specifier|protected
name|Bundle
name|installSpringAsBundle
parameter_list|(
name|String
name|name
parameter_list|,
name|URL
name|url
parameter_list|,
name|boolean
name|start
parameter_list|)
throws|throws
name|BundleException
block|{
name|TinyBundle
name|bundle
init|=
name|TinyBundles
operator|.
name|bundle
argument_list|()
decl_stmt|;
name|bundle
operator|.
name|add
argument_list|(
literal|"META-INF/spring/spring-"
operator|+
name|name
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
operator|+
literal|".xml"
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|bundle
operator|.
name|set
argument_list|(
literal|"Manifest-Version"
argument_list|,
literal|"2"
argument_list|)
operator|.
name|set
argument_list|(
literal|"Bundle-ManifestVersion"
argument_list|,
literal|"2"
argument_list|)
operator|.
name|set
argument_list|(
literal|"Bundle-SymbolicName"
argument_list|,
name|name
argument_list|)
operator|.
name|set
argument_list|(
literal|"Bundle-Version"
argument_list|,
literal|"1.0.0"
argument_list|)
expr_stmt|;
name|Bundle
name|answer
init|=
name|bundleContext
operator|.
name|installBundle
argument_list|(
name|name
argument_list|,
name|bundle
operator|.
name|build
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|start
condition|)
block|{
name|answer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|installCamelFeature (String mainFeature)
specifier|protected
name|void
name|installCamelFeature
parameter_list|(
name|String
name|mainFeature
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|mainFeature
operator|.
name|startsWith
argument_list|(
literal|"camel-"
argument_list|)
condition|)
block|{
name|mainFeature
operator|=
literal|"camel-"
operator|+
name|mainFeature
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Install main feature: {}"
argument_list|,
name|mainFeature
argument_list|)
expr_stmt|;
comment|// do not refresh bundles causing out bundle context to be invalid
comment|// TODO: see if we can find a way maybe to install camel.xml as bundle/feature instead of part of unit test (see src/test/resources/OSGI-INF/blueprint)
name|featuresService
operator|.
name|installFeature
argument_list|(
name|mainFeature
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|FeaturesService
operator|.
name|Option
operator|.
name|NoAutoRefreshBundles
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testComponent (String component)
specifier|protected
name|void
name|testComponent
parameter_list|(
name|String
name|component
parameter_list|)
throws|throws
name|Exception
block|{
name|testComponent
argument_list|(
literal|"camel-"
operator|+
name|component
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|testComponent (String mainFeature, String component)
specifier|protected
name|void
name|testComponent
parameter_list|(
name|String
name|mainFeature
parameter_list|,
name|String
name|component
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Looking up CamelContext(myCamel) in OSGi Service Registry"
argument_list|)
expr_stmt|;
name|installCamelFeature
argument_list|(
name|mainFeature
argument_list|)
expr_stmt|;
name|CamelContext
name|camelContext
init|=
name|getOsgiService
argument_list|(
name|bundleContext
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|,
literal|"(camel.context.name=myCamel)"
argument_list|,
name|SERVICE_TIMEOUT
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Cannot find CamelContext with name myCamel"
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Getting Camel component: {}"
argument_list|,
name|component
argument_list|)
expr_stmt|;
comment|// do not auto start the component as it may not have been configured properly and fail in its start method
name|Component
name|comp
init|=
name|camelContext
operator|.
name|getComponent
argument_list|(
name|component
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Cannot get component with name: "
operator|+
name|component
argument_list|,
name|comp
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Found Camel component: {} instance: {} with className: {}"
argument_list|,
name|component
argument_list|,
name|comp
argument_list|,
name|comp
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testDataFormat (String dataFormat)
specifier|protected
name|void
name|testDataFormat
parameter_list|(
name|String
name|dataFormat
parameter_list|)
throws|throws
name|Exception
block|{
name|testDataFormat
argument_list|(
literal|"camel-"
operator|+
name|dataFormat
argument_list|,
name|dataFormat
argument_list|)
expr_stmt|;
block|}
DECL|method|testDataFormat (String mainFeature, String dataFormat)
specifier|protected
name|void
name|testDataFormat
parameter_list|(
name|String
name|mainFeature
parameter_list|,
name|String
name|dataFormat
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Looking up CamelContext(myCamel) in OSGi Service Registry"
argument_list|)
expr_stmt|;
name|installCamelFeature
argument_list|(
name|mainFeature
argument_list|)
expr_stmt|;
name|CamelContext
name|camelContext
init|=
name|getOsgiService
argument_list|(
name|bundleContext
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|,
literal|"(camel.context.name=myCamel)"
argument_list|,
name|SERVICE_TIMEOUT
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Cannot find CamelContext with name myCamel"
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Getting Camel dataformat: {}"
argument_list|,
name|dataFormat
argument_list|)
expr_stmt|;
name|DataFormat
name|df
init|=
name|camelContext
operator|.
name|resolveDataFormat
argument_list|(
name|dataFormat
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Cannot get dataformat with name: "
operator|+
name|dataFormat
argument_list|,
name|df
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Found Camel dataformat: {} instance: {} with className: {}"
argument_list|,
name|dataFormat
argument_list|,
name|df
argument_list|,
name|df
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLanguage (String language)
specifier|protected
name|void
name|testLanguage
parameter_list|(
name|String
name|language
parameter_list|)
throws|throws
name|Exception
block|{
name|testLanguage
argument_list|(
literal|"camel-"
operator|+
name|language
argument_list|,
name|language
argument_list|)
expr_stmt|;
block|}
DECL|method|testLanguage (String mainFeature, String language)
specifier|protected
name|void
name|testLanguage
parameter_list|(
name|String
name|mainFeature
parameter_list|,
name|String
name|language
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Looking up CamelContext(myCamel) in OSGi Service Registry"
argument_list|)
expr_stmt|;
name|installCamelFeature
argument_list|(
name|mainFeature
argument_list|)
expr_stmt|;
name|CamelContext
name|camelContext
init|=
name|getOsgiService
argument_list|(
name|bundleContext
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|,
literal|"(camel.context.name=myCamel)"
argument_list|,
literal|20000
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Cannot find CamelContext with name myCamel"
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Getting Camel language: {}"
argument_list|,
name|language
argument_list|)
expr_stmt|;
name|Language
name|lan
init|=
name|camelContext
operator|.
name|resolveLanguage
argument_list|(
name|language
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Cannot get language with name: "
operator|+
name|language
argument_list|,
name|lan
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Found Camel language: {} instance: {} with className: {}"
argument_list|,
name|language
argument_list|,
name|lan
argument_list|,
name|lan
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|extractName (Class<?> clazz)
specifier|public
specifier|static
name|String
name|extractName
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|String
name|name
init|=
name|clazz
operator|.
name|getName
argument_list|()
decl_stmt|;
name|int
name|id0
init|=
name|name
operator|.
name|indexOf
argument_list|(
literal|"Camel"
argument_list|)
operator|+
literal|"Camel"
operator|.
name|length
argument_list|()
decl_stmt|;
name|int
name|id1
init|=
name|name
operator|.
name|indexOf
argument_list|(
literal|"Test"
argument_list|)
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|id0
init|;
name|i
operator|<
name|id1
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|name
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|Character
operator|.
name|isUpperCase
argument_list|(
name|c
argument_list|)
operator|&&
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"-"
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|Character
operator|.
name|toLowerCase
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getCamelKarafFeatureUrl ()
specifier|public
specifier|static
name|UrlReference
name|getCamelKarafFeatureUrl
parameter_list|()
block|{
return|return
name|mavenBundle
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.apache.camel.karaf"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"apache-camel"
argument_list|)
operator|.
name|version
argument_list|(
name|getCamelKarafFeatureVersion
argument_list|()
argument_list|)
operator|.
name|type
argument_list|(
literal|"xml/features"
argument_list|)
return|;
block|}
DECL|method|getCamelKarafFeatureVersion ()
specifier|private
specifier|static
name|String
name|getCamelKarafFeatureVersion
parameter_list|()
block|{
name|String
name|camelKarafFeatureVersion
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"camelKarafFeatureVersion"
argument_list|)
decl_stmt|;
if|if
condition|(
name|camelKarafFeatureVersion
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Please specify the maven artifact version to use for org.apache.camel.karaf/apache-camel through the camelKarafFeatureVersion System property"
argument_list|)
throw|;
block|}
return|return
name|camelKarafFeatureVersion
return|;
block|}
DECL|method|switchPlatformEncodingToUTF8 ()
specifier|private
specifier|static
name|void
name|switchPlatformEncodingToUTF8
parameter_list|()
block|{
try|try
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"file.encoding"
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|Field
name|charset
init|=
name|Charset
operator|.
name|class
operator|.
name|getDeclaredField
argument_list|(
literal|"defaultCharset"
argument_list|)
decl_stmt|;
name|charset
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|charset
operator|.
name|set
argument_list|(
literal|null
argument_list|,
literal|null
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
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getKarafVersion ()
specifier|private
specifier|static
name|String
name|getKarafVersion
parameter_list|()
block|{
name|InputStream
name|ins
init|=
name|AbstractFeatureTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/META-INF/maven/dependencies.properties"
argument_list|)
decl_stmt|;
name|Properties
name|p
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
block|{
name|p
operator|.
name|load
argument_list|(
name|ins
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// ignore
block|}
name|String
name|karafVersion
init|=
name|p
operator|.
name|getProperty
argument_list|(
literal|"org.apache.karaf/apache-karaf/version"
argument_list|)
decl_stmt|;
if|if
condition|(
name|karafVersion
operator|==
literal|null
condition|)
block|{
name|karafVersion
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"karafVersion"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|karafVersion
operator|==
literal|null
condition|)
block|{
comment|// setup the default version of it
name|karafVersion
operator|=
literal|"4.0.8"
expr_stmt|;
block|}
return|return
name|karafVersion
return|;
block|}
DECL|method|configure (String... extra)
specifier|public
specifier|static
name|Option
index|[]
name|configure
parameter_list|(
name|String
modifier|...
name|extra
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|camel
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|camel
operator|.
name|add
argument_list|(
literal|"camel"
argument_list|)
expr_stmt|;
if|if
condition|(
name|extra
operator|!=
literal|null
operator|&&
name|extra
operator|.
name|length
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|String
name|e
range|:
name|extra
control|)
block|{
name|camel
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
name|String
index|[]
name|camelFeatures
init|=
name|camel
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|camel
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
name|switchPlatformEncodingToUTF8
argument_list|()
expr_stmt|;
name|String
name|karafVersion
init|=
name|getKarafVersion
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"*** Apache Karaf version is "
operator|+
name|karafVersion
operator|+
literal|" ***"
argument_list|)
expr_stmt|;
name|Option
index|[]
name|options
init|=
operator|new
name|Option
index|[]
block|{
comment|// for remote debugging
comment|//org.ops4j.pax.exam.CoreOptions.vmOption("-Xdebug"),
comment|//org.ops4j.pax.exam.CoreOptions.vmOption("-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5008"),
name|KarafDistributionOption
operator|.
name|karafDistributionConfiguration
argument_list|()
operator|.
name|frameworkUrl
argument_list|(
name|maven
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.apache.karaf"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"apache-karaf"
argument_list|)
operator|.
name|type
argument_list|(
literal|"tar.gz"
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
argument_list|)
operator|.
name|karafVersion
argument_list|(
name|karafVersion
argument_list|)
operator|.
name|name
argument_list|(
literal|"Apache Karaf"
argument_list|)
operator|.
name|useDeployFolder
argument_list|(
literal|false
argument_list|)
operator|.
name|unpackDirectory
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/paxexam/unpack/"
argument_list|)
argument_list|)
block|,
name|logLevel
argument_list|(
name|LogLevelOption
operator|.
name|LogLevel
operator|.
name|INFO
argument_list|)
block|,
comment|// keep the folder so we can look inside when something fails
name|keepRuntimeFolder
argument_list|()
block|,
comment|// Disable the SSH port
name|configureConsole
argument_list|()
operator|.
name|ignoreRemoteShell
argument_list|()
block|,
comment|// need to modify the jre.properties to export some com.sun packages that some features rely on
comment|//            KarafDistributionOption.replaceConfigurationFile("etc/jre.properties", new File("src/test/resources/jre.properties")),
name|vmOption
argument_list|(
literal|"-Dfile.encoding=UTF-8"
argument_list|)
block|,
comment|// Disable the Karaf shutdown port
name|editConfigurationFilePut
argument_list|(
literal|"etc/custom.properties"
argument_list|,
literal|"karaf.shutdown.port"
argument_list|,
literal|"-1"
argument_list|)
block|,
comment|// Assign unique ports for Karaf
comment|//            editConfigurationFilePut("etc/org.ops4j.pax.web.cfg", "org.osgi.service.http.port", Integer.toString(AvailablePortFinder.getNextAvailable())),
comment|//            editConfigurationFilePut("etc/org.apache.karaf.management.cfg", "rmiRegistryPort", Integer.toString(AvailablePortFinder.getNextAvailable())),
comment|//            editConfigurationFilePut("etc/org.apache.karaf.management.cfg", "rmiServerPort", Integer.toString(AvailablePortFinder.getNextAvailable())),
comment|// install junit
name|CoreOptions
operator|.
name|junitBundles
argument_list|()
block|,
comment|// install camel
name|features
argument_list|(
name|getCamelKarafFeatureUrl
argument_list|()
argument_list|,
name|camelFeatures
argument_list|)
block|,
comment|// install camel-test-karaf as bundle (not feature as the feature causes a bundle refresh that invalidates the @Inject bundleContext)
name|mavenBundle
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.apache.camel"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"camel-test-karaf"
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
block|}
decl_stmt|;
return|return
name|options
return|;
block|}
DECL|method|getOsgiService (BundleContext bundleContext, Class<T> type)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getOsgiService
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|getOsgiService
argument_list|(
name|bundleContext
argument_list|,
name|type
argument_list|,
literal|null
argument_list|,
name|SERVICE_TIMEOUT
argument_list|)
return|;
block|}
DECL|method|getOsgiService (BundleContext bundleContext, Class<T> type, long timeout)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getOsgiService
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
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
name|getOsgiService
argument_list|(
name|bundleContext
argument_list|,
name|type
argument_list|,
literal|null
argument_list|,
name|timeout
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getOsgiService (BundleContext bundleContext, Class<T> type, String filter, long timeout)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|getOsgiService
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
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
name|ServiceTracker
name|tracker
decl_stmt|;
try|try
block|{
name|String
name|flt
decl_stmt|;
if|if
condition|(
name|filter
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|filter
operator|.
name|startsWith
argument_list|(
literal|"("
argument_list|)
condition|)
block|{
name|flt
operator|=
literal|"(&("
operator|+
name|Constants
operator|.
name|OBJECTCLASS
operator|+
literal|"="
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|")"
operator|+
name|filter
operator|+
literal|")"
expr_stmt|;
block|}
else|else
block|{
name|flt
operator|=
literal|"(&("
operator|+
name|Constants
operator|.
name|OBJECTCLASS
operator|+
literal|"="
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|")("
operator|+
name|filter
operator|+
literal|"))"
expr_stmt|;
block|}
block|}
else|else
block|{
name|flt
operator|=
literal|"("
operator|+
name|Constants
operator|.
name|OBJECTCLASS
operator|+
literal|"="
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|")"
expr_stmt|;
block|}
name|Filter
name|osgiFilter
init|=
name|FrameworkUtil
operator|.
name|createFilter
argument_list|(
name|flt
argument_list|)
decl_stmt|;
name|tracker
operator|=
operator|new
name|ServiceTracker
argument_list|(
name|bundleContext
argument_list|,
name|osgiFilter
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|tracker
operator|.
name|open
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Note that the tracker is not closed to keep the reference
comment|// This is buggy, as the service reference may change i think
name|Object
name|svc
init|=
name|tracker
operator|.
name|waitForService
argument_list|(
name|timeout
argument_list|)
decl_stmt|;
if|if
condition|(
name|svc
operator|==
literal|null
condition|)
block|{
name|Dictionary
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|dic
init|=
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
literal|"Test bundle headers: "
operator|+
name|explode
argument_list|(
name|dic
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|ServiceReference
name|ref
range|:
name|asCollection
argument_list|(
name|bundleContext
operator|.
name|getAllServiceReferences
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
control|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"ServiceReference: "
operator|+
name|ref
operator|+
literal|", bundle: "
operator|+
name|ref
operator|.
name|getBundle
argument_list|()
operator|+
literal|", symbolicName: "
operator|+
name|ref
operator|.
name|getBundle
argument_list|()
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|ServiceReference
name|ref
range|:
name|asCollection
argument_list|(
name|bundleContext
operator|.
name|getAllServiceReferences
argument_list|(
literal|null
argument_list|,
name|flt
argument_list|)
argument_list|)
control|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Filtered ServiceReference: "
operator|+
name|ref
operator|+
literal|", bundle: "
operator|+
name|ref
operator|.
name|getBundle
argument_list|()
operator|+
literal|", symbolicName: "
operator|+
name|ref
operator|.
name|getBundle
argument_list|()
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Gave up waiting for service "
operator|+
name|flt
argument_list|)
throw|;
block|}
return|return
name|type
operator|.
name|cast
argument_list|(
name|svc
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InvalidSyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid filter"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Explode the dictionary into a<code>,</code> delimited list of<code>key=value</code> pairs.      */
DECL|method|explode (Dictionary<?, ?> dictionary)
specifier|private
specifier|static
name|String
name|explode
parameter_list|(
name|Dictionary
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|dictionary
parameter_list|)
block|{
name|Enumeration
argument_list|<
name|?
argument_list|>
name|keys
init|=
name|dictionary
operator|.
name|keys
argument_list|()
decl_stmt|;
name|StringBuilder
name|result
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
while|while
condition|(
name|keys
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|Object
name|key
init|=
name|keys
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|result
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s=%s"
argument_list|,
name|key
argument_list|,
name|dictionary
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|keys
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|result
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Provides an iterable collection of references, even if the original array is<code>null</code>.      */
DECL|method|asCollection (ServiceReference[] references)
specifier|private
specifier|static
name|Collection
argument_list|<
name|ServiceReference
argument_list|>
name|asCollection
parameter_list|(
name|ServiceReference
index|[]
name|references
parameter_list|)
block|{
return|return
name|references
operator|==
literal|null
condition|?
operator|new
name|ArrayList
argument_list|<
name|ServiceReference
argument_list|>
argument_list|(
literal|0
argument_list|)
else|:
name|Arrays
operator|.
name|asList
argument_list|(
name|references
argument_list|)
return|;
block|}
block|}
end_class

end_unit

