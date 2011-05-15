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
name|Inject
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
name|equinox
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
name|felix
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
name|options
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
name|container
operator|.
name|def
operator|.
name|PaxRunnerOptions
operator|.
name|profile
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
name|container
operator|.
name|def
operator|.
name|PaxRunnerOptions
operator|.
name|scanFeatures
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
name|container
operator|.
name|def
operator|.
name|PaxRunnerOptions
operator|.
name|workingDirectory
import|;
end_import

begin_class
DECL|class|AbstractFeatureTest
specifier|public
specifier|abstract
class|class
name|AbstractFeatureTest
block|{
DECL|field|log
specifier|protected
specifier|final
specifier|transient
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
annotation|@
name|Inject
DECL|field|bundleContext
specifier|protected
name|BundleContext
name|bundleContext
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
block|{     }
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{     }
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
name|Exception
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
name|Exception
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
continue|continue;
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
name|Exception
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
continue|continue;
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
name|log
operator|.
name|info
argument_list|(
literal|"Get the bundleContext is "
operator|+
name|bundleContext
argument_list|)
expr_stmt|;
return|return
name|factory
operator|.
name|createContext
argument_list|()
return|;
block|}
DECL|method|extractName (Class clazz)
specifier|public
specifier|static
name|String
name|extractName
parameter_list|(
name|Class
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
name|String
name|springVersion
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"springVersion"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"*** The spring version is "
operator|+
name|springVersion
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
name|type
argument_list|)
return|;
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
literal|"2.2.1"
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
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
DECL|method|configure (String feature)
specifier|public
specifier|static
name|Option
index|[]
name|configure
parameter_list|(
name|String
name|feature
parameter_list|)
block|{
return|return
name|configure
argument_list|(
name|feature
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|configure (String feature, boolean useFelix, boolean useEquinox)
specifier|public
specifier|static
name|Option
index|[]
name|configure
parameter_list|(
name|String
name|feature
parameter_list|,
name|boolean
name|useFelix
parameter_list|,
name|boolean
name|useEquinox
parameter_list|)
block|{
name|Option
index|[]
name|options
init|=
name|options
argument_list|(
name|profile
argument_list|(
literal|"log"
argument_list|)
operator|.
name|version
argument_list|(
literal|"1.4"
argument_list|)
argument_list|,
comment|// this is how you set the default log level when using pax logging (logProfile)
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
name|systemProperty
argument_list|(
literal|"org.ops4j.pax.logging.DefaultServiceLog.level"
argument_list|)
operator|.
name|value
argument_list|(
literal|"DEBUG"
argument_list|)
argument_list|,
comment|//need to install some karaf features
name|mavenBundle
argument_list|(
literal|"org.apache.felix"
argument_list|,
literal|"org.apache.felix.configadmin"
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
argument_list|,
name|mavenBundle
argument_list|(
literal|"org.apache.servicemix.bundles"
argument_list|,
literal|"org.apache.servicemix.bundles.jaxp-ri"
argument_list|)
operator|.
name|version
argument_list|(
literal|"1.4.2_4"
argument_list|)
argument_list|,
name|scanFeatures
argument_list|(
name|getKarafFeatureUrl
argument_list|()
argument_list|,
literal|"http"
argument_list|)
argument_list|,
comment|// and the camel feature to be tested
name|scanFeatures
argument_list|(
name|getCamelKarafFeatureUrl
argument_list|()
argument_list|,
literal|"camel-spring"
argument_list|,
literal|"camel-"
operator|+
name|feature
argument_list|)
argument_list|,
name|workingDirectory
argument_list|(
literal|"target/paxrunner/"
argument_list|)
argument_list|,
name|useFelix
condition|?
name|felix
argument_list|()
else|:
literal|null
argument_list|,
name|useEquinox
condition|?
name|equinox
argument_list|()
else|:
literal|null
argument_list|)
decl_stmt|;
return|return
name|options
return|;
block|}
block|}
end_class

end_unit

