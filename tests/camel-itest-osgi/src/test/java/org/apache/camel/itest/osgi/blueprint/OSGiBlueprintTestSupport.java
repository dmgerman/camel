begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.blueprint
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
name|Closeable
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
name|io
operator|.
name|OutputStream
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
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
name|junit
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
name|junit
operator|.
name|JUnit4TestRunner
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
name|UrlProvisionOption
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|store
operator|.
name|Store
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|store
operator|.
name|StoreFactory
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
name|CoreOptions
operator|.
name|wrappedBundle
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

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|swissbox
operator|.
name|tinybundles
operator|.
name|core
operator|.
name|TinyBundles
operator|.
name|newBundle
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
name|swissbox
operator|.
name|tinybundles
operator|.
name|core
operator|.
name|TinyBundles
operator|.
name|withBnd
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4TestRunner
operator|.
name|class
argument_list|)
DECL|class|OSGiBlueprintTestSupport
specifier|public
class|class
name|OSGiBlueprintTestSupport
extends|extends
name|AbstractIntegrationTest
block|{
annotation|@
name|Test
DECL|method|testRouteWithAllComponents ()
specifier|public
name|void
name|testRouteWithAllComponents
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|getOsgiService
argument_list|(
name|BlueprintContainer
operator|.
name|class
argument_list|,
literal|"(osgi.blueprint.container.symbolicname=CamelBlueprintTestBundle1)"
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"The blueprint container should not be available"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{         }
name|getInstalledBundle
argument_list|(
literal|"CamelBlueprintTestBundle1"
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
name|getOsgiService
argument_list|(
name|BlueprintContainer
operator|.
name|class
argument_list|,
literal|"(osgi.blueprint.container.symbolicname=CamelBlueprintTestBundle1)"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
name|getOsgiService
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|,
literal|"(camel.context.symbolicname=CamelBlueprintTestBundle1)"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRouteWithMissingComponent ()
specifier|public
name|void
name|testRouteWithMissingComponent
parameter_list|()
throws|throws
name|Exception
block|{
name|getInstalledBundle
argument_list|(
literal|"org.apache.camel.camel-mail"
argument_list|)
operator|.
name|stop
argument_list|()
expr_stmt|;
try|try
block|{
name|getOsgiService
argument_list|(
name|BlueprintContainer
operator|.
name|class
argument_list|,
literal|"(osgi.blueprint.container.symbolicname=CamelBlueprintTestBundle2)"
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"The blueprint container should not be available"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{         }
name|getInstalledBundle
argument_list|(
literal|"CamelBlueprintTestBundle2"
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|getOsgiService
argument_list|(
name|BlueprintContainer
operator|.
name|class
argument_list|,
literal|"(osgi.blueprint.container.symbolicname=CamelBlueprintTestBundle2)"
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"The blueprint container should not be available"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{         }
name|getInstalledBundle
argument_list|(
literal|"org.apache.camel.camel-mail"
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
name|getOsgiService
argument_list|(
name|BlueprintContainer
operator|.
name|class
argument_list|,
literal|"(osgi.blueprint.container.symbolicname=CamelBlueprintTestBundle2)"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
name|getOsgiService
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|,
literal|"(camel.context.symbolicname=CamelBlueprintTestBundle2)"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRouteWithMissingDataFormat ()
specifier|public
name|void
name|testRouteWithMissingDataFormat
parameter_list|()
throws|throws
name|Exception
block|{
name|getInstalledBundle
argument_list|(
literal|"org.apache.camel.camel-jaxb"
argument_list|)
operator|.
name|stop
argument_list|()
expr_stmt|;
try|try
block|{
name|getOsgiService
argument_list|(
name|BlueprintContainer
operator|.
name|class
argument_list|,
literal|"(osgi.blueprint.container.symbolicname=CamelBlueprintTestBundle3)"
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"The blueprint container should not be available"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{         }
name|getInstalledBundle
argument_list|(
literal|"CamelBlueprintTestBundle3"
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|getOsgiService
argument_list|(
name|BlueprintContainer
operator|.
name|class
argument_list|,
literal|"(osgi.blueprint.container.symbolicname=CamelBlueprintTestBundle3)"
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"The blueprint container should not be available"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{         }
name|getInstalledBundle
argument_list|(
literal|"org.apache.camel.camel-jaxb"
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
name|getOsgiService
argument_list|(
name|BlueprintContainer
operator|.
name|class
argument_list|,
literal|"(osgi.blueprint.container.symbolicname=CamelBlueprintTestBundle3)"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
name|getOsgiService
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|,
literal|"(camel.context.symbolicname=CamelBlueprintTestBundle3)"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
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
name|options
argument_list|(
name|bundle
argument_list|(
name|newBundle
argument_list|()
operator|.
name|add
argument_list|(
literal|"OSGI-INF/blueprint/test.xml"
argument_list|,
name|OSGiBlueprintTestSupport
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"blueprint-1.xml"
argument_list|)
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|BUNDLE_SYMBOLICNAME
argument_list|,
literal|"CamelBlueprintTestBundle1"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|noStart
argument_list|()
argument_list|,
name|bundle
argument_list|(
name|newBundle
argument_list|()
operator|.
name|add
argument_list|(
literal|"OSGI-INF/blueprint/test.xml"
argument_list|,
name|OSGiBlueprintTestSupport
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"blueprint-2.xml"
argument_list|)
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|BUNDLE_SYMBOLICNAME
argument_list|,
literal|"CamelBlueprintTestBundle2"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|noStart
argument_list|()
argument_list|,
name|bundle
argument_list|(
name|newBundle
argument_list|()
operator|.
name|add
argument_list|(
literal|"OSGI-INF/blueprint/test.xml"
argument_list|,
name|OSGiBlueprintTestSupport
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"blueprint-3.xml"
argument_list|)
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|BUNDLE_SYMBOLICNAME
argument_list|,
literal|"CamelBlueprintTestBundle3"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|noStart
argument_list|()
argument_list|,
comment|// install the spring dm profile
name|profile
argument_list|(
literal|"spring.dm"
argument_list|)
operator|.
name|version
argument_list|(
literal|"1.2.0"
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
literal|"TRACE"
argument_list|)
argument_list|,
comment|// install blueprint requirements
name|mavenBundle
argument_list|(
literal|"org.apache.felix"
argument_list|,
literal|"org.apache.felix.configadmin"
argument_list|)
argument_list|,
comment|// install tiny bundles
name|mavenBundle
argument_list|(
literal|"org.ops4j.base"
argument_list|,
literal|"ops4j-base-store"
argument_list|)
argument_list|,
name|wrappedBundle
argument_list|(
name|mavenBundle
argument_list|(
literal|"org.ops4j.pax.swissbox"
argument_list|,
literal|"pax-swissbox-bnd"
argument_list|)
argument_list|)
argument_list|,
name|mavenBundle
argument_list|(
literal|"org.ops4j.pax.swissbox"
argument_list|,
literal|"pax-swissbox-tinybundles"
argument_list|)
argument_list|,
comment|// using the features to install the camel components
name|scanFeatures
argument_list|(
name|getCamelKarafFeatureUrl
argument_list|()
argument_list|,
literal|"camel-core"
argument_list|,
literal|"camel-blueprint"
argument_list|,
literal|"camel-test"
argument_list|,
literal|"camel-mail"
argument_list|,
literal|"camel-jaxb"
argument_list|)
argument_list|,
name|workingDirectory
argument_list|(
literal|"target/paxrunner/"
argument_list|)
argument_list|,
comment|//felix(),
name|equinox
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|options
return|;
block|}
DECL|method|copy (InputStream input, OutputStream output, boolean close)
specifier|private
specifier|static
name|void
name|copy
parameter_list|(
name|InputStream
name|input
parameter_list|,
name|OutputStream
name|output
parameter_list|,
name|boolean
name|close
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|8192
index|]
decl_stmt|;
name|int
name|bytesRead
init|=
name|input
operator|.
name|read
argument_list|(
name|buf
argument_list|)
decl_stmt|;
while|while
condition|(
name|bytesRead
operator|!=
operator|-
literal|1
condition|)
block|{
name|output
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|bytesRead
argument_list|)
expr_stmt|;
name|bytesRead
operator|=
name|input
operator|.
name|read
argument_list|(
name|buf
argument_list|)
expr_stmt|;
block|}
name|output
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|close
condition|)
block|{
name|close
argument_list|(
name|input
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|close (Closeable... closeables)
specifier|private
specifier|static
name|void
name|close
parameter_list|(
name|Closeable
modifier|...
name|closeables
parameter_list|)
block|{
if|if
condition|(
name|closeables
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Closeable
name|c
range|:
name|closeables
control|)
block|{
try|try
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{                 }
block|}
block|}
block|}
DECL|method|bundle (final InputStream stream)
specifier|private
specifier|static
name|UrlProvisionOption
name|bundle
parameter_list|(
specifier|final
name|InputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
name|Store
argument_list|<
name|InputStream
argument_list|>
name|store
init|=
name|StoreFactory
operator|.
name|defaultStore
argument_list|()
decl_stmt|;
return|return
operator|new
name|UrlProvisionOption
argument_list|(
name|store
operator|.
name|getLocation
argument_list|(
name|store
operator|.
name|store
argument_list|(
name|stream
argument_list|)
argument_list|)
operator|.
name|toURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

