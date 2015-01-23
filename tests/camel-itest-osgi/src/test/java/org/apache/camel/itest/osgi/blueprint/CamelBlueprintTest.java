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
name|junit
operator|.
name|PaxExam
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
name|OptionUtils
operator|.
name|combine
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|PaxExam
operator|.
name|class
argument_list|)
DECL|class|CamelBlueprintTest
specifier|public
class|class
name|CamelBlueprintTest
extends|extends
name|OSGiBlueprintTestSupport
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
name|getInstalledBundle
argument_list|(
literal|"CamelBlueprintTestBundle1"
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
literal|10000
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
literal|10000
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
literal|"CamelBlueprintTestBundle2"
argument_list|)
operator|.
name|stop
argument_list|()
expr_stmt|;
name|getInstalledBundle
argument_list|(
literal|"org.apache.camel.camel-ftp"
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
literal|500
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
literal|500
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
literal|"org.apache.camel.camel-ftp"
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
literal|10000
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
literal|10000
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
literal|"CamelBlueprintTestBundle3"
argument_list|)
operator|.
name|stop
argument_list|()
expr_stmt|;
name|getInstalledBundle
argument_list|(
literal|"org.apache.camel.camel-jackson"
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
literal|500
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
literal|500
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
literal|"org.apache.camel.camel-jackson"
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
literal|10000
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
literal|10000
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRouteWithPackage ()
specifier|public
name|void
name|testRouteWithPackage
parameter_list|()
throws|throws
name|Exception
block|{
name|getInstalledBundle
argument_list|(
literal|"CamelBlueprintTestBundle4"
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
literal|"(osgi.blueprint.container.symbolicname=CamelBlueprintTestBundle4)"
argument_list|,
literal|10000
argument_list|)
expr_stmt|;
name|CamelContext
name|ctx
init|=
name|getOsgiService
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|,
literal|"(camel.context.symbolicname=CamelBlueprintTestBundle4)"
argument_list|,
literal|10000
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|ctx
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRouteWithPackageScan ()
specifier|public
name|void
name|testRouteWithPackageScan
parameter_list|()
throws|throws
name|Exception
block|{
name|getInstalledBundle
argument_list|(
literal|"CamelBlueprintTestBundle5"
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
literal|"(osgi.blueprint.container.symbolicname=CamelBlueprintTestBundle5)"
argument_list|,
literal|10000
argument_list|)
expr_stmt|;
name|CamelContext
name|ctx
init|=
name|getOsgiService
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|,
literal|"(camel.context.symbolicname=CamelBlueprintTestBundle5)"
argument_list|,
literal|10000
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|ctx
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
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
argument_list|,
name|bundle
argument_list|(
name|TinyBundles
operator|.
name|bundle
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
name|set
argument_list|(
name|Constants
operator|.
name|DYNAMICIMPORT_PACKAGE
argument_list|,
literal|"*"
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
name|TinyBundles
operator|.
name|bundle
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
name|set
argument_list|(
name|Constants
operator|.
name|DYNAMICIMPORT_PACKAGE
argument_list|,
literal|"*"
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
name|TinyBundles
operator|.
name|bundle
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
name|set
argument_list|(
name|Constants
operator|.
name|DYNAMICIMPORT_PACKAGE
argument_list|,
literal|"*"
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
name|TinyBundles
operator|.
name|bundle
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
literal|"blueprint-4.xml"
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
name|TestRouteBuilder
operator|.
name|class
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|BUNDLE_SYMBOLICNAME
argument_list|,
literal|"CamelBlueprintTestBundle4"
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|DYNAMICIMPORT_PACKAGE
argument_list|,
literal|"*"
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
name|TinyBundles
operator|.
name|bundle
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
literal|"blueprint-5.xml"
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
name|TestRouteBuilder
operator|.
name|class
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|BUNDLE_SYMBOLICNAME
argument_list|,
literal|"CamelBlueprintTestBundle5"
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|DYNAMICIMPORT_PACKAGE
argument_list|,
literal|"*"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|noStart
argument_list|()
argument_list|,
comment|// using the features to install the camel components
name|loadCamelFeatures
argument_list|(
literal|"camel-blueprint"
argument_list|,
literal|"camel-ftp"
argument_list|,
literal|"camel-jackson"
argument_list|,
literal|"camel-jms"
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|options
return|;
block|}
block|}
end_class

end_unit

