begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
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
name|List
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
name|atomic
operator|.
name|AtomicInteger
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
name|osgi
operator|.
name|CamelContextFactory
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
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Configuration
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
name|MavenArtifactProvisionOption
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
name|OptionUtils
operator|.
name|combine
import|;
end_import

begin_class
DECL|class|OSGiIntegrationTestSupport
specifier|public
class|class
name|OSGiIntegrationTestSupport
extends|extends
name|CamelTestSupport
block|{
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
name|OSGiIntegrationTestSupport
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|COUNTER
specifier|protected
specifier|static
specifier|final
name|AtomicInteger
name|COUNTER
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|workDir
specifier|protected
specifier|static
name|String
name|workDir
init|=
literal|"target/paxrunner/"
decl_stmt|;
annotation|@
name|Inject
DECL|field|bundleContext
specifier|protected
name|BundleContext
name|bundleContext
decl_stmt|;
DECL|method|getInstalledBundle (String symbolicName)
specifier|protected
name|Bundle
name|getInstalledBundle
parameter_list|(
name|String
name|symbolicName
parameter_list|)
block|{
for|for
control|(
name|Bundle
name|b
range|:
name|bundleContext
operator|.
name|getBundles
argument_list|()
control|)
block|{
if|if
condition|(
name|b
operator|.
name|getSymbolicName
argument_list|()
operator|.
name|equals
argument_list|(
name|symbolicName
argument_list|)
condition|)
block|{
return|return
name|b
return|;
block|}
block|}
for|for
control|(
name|Bundle
name|b
range|:
name|bundleContext
operator|.
name|getBundles
argument_list|()
control|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Bundle: "
operator|+
name|b
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
literal|"Bundle "
operator|+
name|symbolicName
operator|+
literal|" does not exist"
argument_list|)
throw|;
block|}
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Get the bundleContext is "
operator|+
name|bundleContext
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Application installed as bundle id: "
operator|+
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|)
expr_stmt|;
name|setThreadContextClassLoader
argument_list|()
expr_stmt|;
name|CamelContextFactory
name|factory
init|=
operator|new
name|CamelContextFactory
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setBundleContext
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setRegistry
argument_list|(
name|createRegistry
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|factory
operator|.
name|createContext
argument_list|()
return|;
block|}
DECL|method|setThreadContextClassLoader ()
specifier|protected
name|void
name|setThreadContextClassLoader
parameter_list|()
block|{
comment|// set the thread context classloader current bundle classloader
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getCamelKarafFeatureUrl ()
specifier|public
specifier|static
name|UrlReference
name|getCamelKarafFeatureUrl
parameter_list|()
block|{
return|return
name|getCamelKarafFeatureUrl
argument_list|(
literal|null
argument_list|)
return|;
block|}
DECL|method|getCamelKarafFeatureUrl (String version)
specifier|public
specifier|static
name|UrlReference
name|getCamelKarafFeatureUrl
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|String
name|type
init|=
literal|"xml/features"
decl_stmt|;
name|MavenArtifactProvisionOption
name|mavenOption
init|=
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
decl_stmt|;
if|if
condition|(
name|version
operator|==
literal|null
condition|)
block|{
return|return
name|mavenOption
operator|.
name|versionAsInProject
argument_list|()
operator|.
name|type
argument_list|(
name|type
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|mavenOption
operator|.
name|version
argument_list|(
name|version
argument_list|)
operator|.
name|type
argument_list|(
name|type
argument_list|)
return|;
block|}
block|}
DECL|method|getKarafFeatureUrl ()
specifier|public
specifier|static
name|UrlReference
name|getKarafFeatureUrl
parameter_list|()
block|{
name|String
name|karafVersion
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"karafVersion"
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"*** The karaf version is "
operator|+
name|karafVersion
operator|+
literal|" ***"
argument_list|)
expr_stmt|;
name|String
name|type
init|=
literal|"xml/features"
decl_stmt|;
return|return
name|mavenBundle
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.apache.karaf.assemblies.features"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"standard"
argument_list|)
operator|.
name|version
argument_list|(
name|karafVersion
argument_list|)
operator|.
name|type
argument_list|(
name|type
argument_list|)
return|;
block|}
DECL|method|getKarafEnterpriseFeatureUrl ()
specifier|public
specifier|static
name|UrlReference
name|getKarafEnterpriseFeatureUrl
parameter_list|()
block|{
name|String
name|karafVersion
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"karafVersion"
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"*** The karaf version is "
operator|+
name|karafVersion
operator|+
literal|" ***"
argument_list|)
expr_stmt|;
name|String
name|type
init|=
literal|"xml/features"
decl_stmt|;
return|return
name|mavenBundle
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.apache.karaf.assemblies.features"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"enterprise"
argument_list|)
operator|.
name|version
argument_list|(
name|karafVersion
argument_list|)
operator|.
name|type
argument_list|(
name|type
argument_list|)
return|;
block|}
DECL|method|loadCamelFeatures (String... features)
specifier|public
specifier|static
name|Option
name|loadCamelFeatures
parameter_list|(
name|String
modifier|...
name|features
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
literal|"cxf-jaxb"
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
literal|"camel-core"
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
literal|"camel-spring"
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
literal|"camel-test"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|feature
range|:
name|features
control|)
block|{
name|result
operator|.
name|add
argument_list|(
name|feature
argument_list|)
expr_stmt|;
block|}
return|return
name|scanFeatures
argument_list|(
name|getCamelKarafFeatureUrl
argument_list|()
argument_list|,
name|result
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|4
operator|+
name|features
operator|.
name|length
index|]
argument_list|)
argument_list|)
return|;
block|}
DECL|method|scanFeatures (UrlReference ref, String ... features)
specifier|public
specifier|static
name|Option
name|scanFeatures
parameter_list|(
name|UrlReference
name|ref
parameter_list|,
name|String
modifier|...
name|features
parameter_list|)
block|{
return|return
name|KarafDistributionOption
operator|.
name|features
argument_list|(
name|ref
argument_list|,
name|features
argument_list|)
return|;
block|}
DECL|method|scanFeatures (String ref, String ... features)
specifier|public
specifier|static
name|Option
name|scanFeatures
parameter_list|(
name|String
name|ref
parameter_list|,
name|String
modifier|...
name|features
parameter_list|)
block|{
return|return
name|KarafDistributionOption
operator|.
name|features
argument_list|(
name|ref
argument_list|,
name|features
argument_list|)
return|;
block|}
DECL|method|felix ()
specifier|public
specifier|static
name|Option
name|felix
parameter_list|()
block|{
return|return
name|KarafDistributionOption
operator|.
name|editConfigurationFileExtend
argument_list|(
literal|"etc/config.properties"
argument_list|,
literal|"karaf.framework"
argument_list|,
literal|"felix"
argument_list|)
return|;
block|}
DECL|method|equinox ()
specifier|public
specifier|static
name|Option
name|equinox
parameter_list|()
block|{
return|return
name|KarafDistributionOption
operator|.
name|editConfigurationFileExtend
argument_list|(
literal|"etc/config.properties"
argument_list|,
literal|"karaf.framework"
argument_list|,
literal|"equinox"
argument_list|)
return|;
block|}
DECL|method|getKarafVersion ()
specifier|private
specifier|static
name|String
name|getKarafVersion
parameter_list|()
block|{
name|String
name|karafVersion
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"karafVersion"
argument_list|)
decl_stmt|;
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
literal|"2.3.6"
expr_stmt|;
block|}
return|return
name|karafVersion
return|;
block|}
DECL|method|getDefaultCamelKarafOptions ()
specifier|public
specifier|static
name|Option
index|[]
name|getDefaultCamelKarafOptions
parameter_list|()
block|{
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
literal|"*** The karaf version is "
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
comment|// Set the karaf environment with some customer configuration
operator|new
name|Option
index|[]
block|{
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
name|KarafDistributionOption
operator|.
name|keepRuntimeFolder
argument_list|()
block|,
comment|// override the config.properties (to fix pax-exam bug)
name|KarafDistributionOption
operator|.
name|replaceConfigurationFile
argument_list|(
literal|"etc/config.properties"
argument_list|,
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/camel/itest/karaf/config.properties"
argument_list|)
argument_list|)
block|,
name|KarafDistributionOption
operator|.
name|replaceConfigurationFile
argument_list|(
literal|"etc/custom.properties"
argument_list|,
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/camel/itest/karaf/custom.properties"
argument_list|)
argument_list|)
block|,
name|KarafDistributionOption
operator|.
name|replaceConfigurationFile
argument_list|(
literal|"etc/org.ops4j.pax.url.mvn.cfg"
argument_list|,
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/camel/itest/karaf/org.ops4j.pax.url.mvn.cfg"
argument_list|)
argument_list|)
block|,
comment|// we need INFO logging otherwise we cannot see what happens
operator|new
name|LogLevelOption
argument_list|(
name|LogLevelOption
operator|.
name|LogLevel
operator|.
name|INFO
argument_list|)
block|,
comment|// install the cxf jaxb spec as the karaf doesn't provide it by default
name|scanFeatures
argument_list|(
name|getCamelKarafFeatureUrl
argument_list|()
argument_list|,
literal|"cxf-jaxb"
argument_list|,
literal|"camel-core"
argument_list|,
literal|"camel-spring"
argument_list|,
literal|"camel-test"
argument_list|)
block|}
decl_stmt|;
return|return
name|options
return|;
block|}
annotation|@
name|Configuration
DECL|method|configure ()
specifier|public
specifier|static
name|Option
index|[]
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|Option
index|[]
name|options
init|=
name|combine
argument_list|(
name|getDefaultCamelKarafOptions
argument_list|()
argument_list|)
decl_stmt|;
comment|// for remote debugging
comment|// vmOption("-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5008"),
return|return
name|options
return|;
block|}
block|}
end_class

end_unit

