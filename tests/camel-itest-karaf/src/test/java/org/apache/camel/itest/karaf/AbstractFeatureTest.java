begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.karaf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
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
name|List
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
name|blueprint
operator|.
name|BlueprintCamelContext
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
name|DefaultRouteContext
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
name|DataFormatDefinition
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
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|rbc
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

begin_class
DECL|class|AbstractFeatureTest
specifier|public
specifier|abstract
class|class
name|AbstractFeatureTest
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
name|long
name|max
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
literal|10000
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
name|assertNotNull
argument_list|(
literal|"Cannot get component with name: "
operator|+
name|component
argument_list|,
name|createCamelContext
argument_list|()
operator|.
name|getComponent
argument_list|(
name|component
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|<
name|max
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|t
throw|;
block|}
block|}
block|}
block|}
DECL|method|testComponent ()
specifier|protected
name|void
name|testComponent
parameter_list|()
throws|throws
name|Exception
block|{
name|testComponent
argument_list|(
name|extractName
argument_list|(
name|getClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testDataFormat (String format)
specifier|protected
name|void
name|testDataFormat
parameter_list|(
name|String
name|format
parameter_list|)
throws|throws
name|Exception
block|{
name|long
name|max
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
literal|10000
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
name|DataFormatDefinition
name|dataFormatDefinition
init|=
name|createDataformatDefinition
argument_list|(
name|format
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dataFormatDefinition
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|dataFormatDefinition
operator|.
name|getDataFormat
argument_list|(
operator|new
name|DefaultRouteContext
argument_list|(
name|createCamelContext
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|<
name|max
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|t
throw|;
block|}
block|}
block|}
block|}
DECL|method|createDataformatDefinition (String format)
specifier|protected
name|DataFormatDefinition
name|createDataformatDefinition
parameter_list|(
name|String
name|format
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
DECL|method|testLanguage (String lang)
specifier|protected
name|void
name|testLanguage
parameter_list|(
name|String
name|lang
parameter_list|)
throws|throws
name|Exception
block|{
name|long
name|max
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
literal|10000
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
name|assertNotNull
argument_list|(
name|createCamelContext
argument_list|()
operator|.
name|resolveLanguage
argument_list|(
name|lang
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|<
name|max
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|t
throw|;
block|}
block|}
block|}
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
literal|"Creating CamelContext using BundleContext: {} and BlueprintContainer: {}"
argument_list|,
name|bundleContext
argument_list|,
name|blueprintContainer
argument_list|)
expr_stmt|;
name|setThreadContextClassLoader
argument_list|()
expr_stmt|;
name|BlueprintCamelContext
name|context
init|=
operator|new
name|BlueprintCamelContext
argument_list|(
name|bundleContext
argument_list|,
name|blueprintContainer
argument_list|)
decl_stmt|;
return|return
name|context
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
name|versionAsInProject
argument_list|()
operator|.
name|type
argument_list|(
literal|"xml/features"
argument_list|)
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
literal|"2.4.4"
expr_stmt|;
block|}
return|return
name|karafVersion
return|;
block|}
DECL|method|getJUnitBundle ()
specifier|public
specifier|static
name|MavenArtifactProvisionOption
name|getJUnitBundle
parameter_list|()
block|{
name|MavenArtifactProvisionOption
name|mavenOption
init|=
name|mavenBundle
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.apache.servicemix.bundles"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"org.apache.servicemix.bundles.junit"
argument_list|)
decl_stmt|;
name|mavenOption
operator|.
name|versionAsInProject
argument_list|()
operator|.
name|start
argument_list|(
literal|true
argument_list|)
operator|.
name|startLevel
argument_list|(
literal|10
argument_list|)
expr_stmt|;
return|return
name|mavenOption
return|;
block|}
DECL|method|configure (String mainFeature, String... extraFeatures)
specifier|public
specifier|static
name|Option
index|[]
name|configure
parameter_list|(
name|String
name|mainFeature
parameter_list|,
name|String
modifier|...
name|extraFeatures
parameter_list|)
block|{
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
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"camel-core"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"camel-blueprint"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"camel-"
operator|+
name|mainFeature
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|extra
range|:
name|extraFeatures
control|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"camel-"
operator|+
name|extra
argument_list|)
expr_stmt|;
block|}
name|String
index|[]
name|features
init|=
name|list
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|list
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
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
comment|// keep the folder so we can look inside when something fails
name|KarafDistributionOption
operator|.
name|keepRuntimeFolder
argument_list|()
block|,
name|vmOption
argument_list|(
literal|"-Dfile.encoding=UTF-8"
argument_list|)
block|,
comment|// install junit
name|getJUnitBundle
argument_list|()
block|,
comment|// install the features
name|KarafDistributionOption
operator|.
name|features
argument_list|(
name|getCamelKarafFeatureUrl
argument_list|()
argument_list|,
name|features
argument_list|)
block|}
decl_stmt|;
return|return
name|options
return|;
block|}
DECL|method|configureComponent ()
specifier|protected
name|Option
index|[]
name|configureComponent
parameter_list|()
block|{
return|return
name|configure
argument_list|(
name|extractName
argument_list|(
name|getClass
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

