begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.jpa
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
name|jpa
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
name|ProducerTemplate
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
name|mock
operator|.
name|MockEndpoint
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
name|itest
operator|.
name|osgi
operator|.
name|blueprint
operator|.
name|OSGiBlueprintTestSupport
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
name|Customizer
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
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|OptionUtils
operator|.
name|combine
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
name|swissbox
operator|.
name|tinybundles
operator|.
name|core
operator|.
name|TinyBundles
operator|.
name|modifyBundle
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
DECL|class|JpaBlueprintRouteTest
specifier|public
class|class
name|JpaBlueprintRouteTest
extends|extends
name|OSGiBlueprintTestSupport
block|{
annotation|@
name|Test
DECL|method|testBlueprintRouteJpa ()
specifier|public
name|void
name|testBlueprintRouteJpa
parameter_list|()
throws|throws
name|Exception
block|{
name|getInstalledBundle
argument_list|(
literal|"CamelBlueprintJpaTestBundle"
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
name|BlueprintContainer
name|ctn
init|=
name|getOsgiService
argument_list|(
name|BlueprintContainer
operator|.
name|class
argument_list|,
literal|"(osgi.blueprint.container.symbolicname=CamelBlueprintJpaTestBundle)"
argument_list|,
literal|20000
argument_list|)
decl_stmt|;
name|CamelContext
name|ctx
init|=
name|getOsgiService
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|,
literal|"(camel.context.symbolicname=CamelBlueprintJpaTestBundle)"
argument_list|,
literal|20000
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mock
init|=
operator|(
name|MockEndpoint
operator|)
name|ctx
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|ProducerTemplate
name|template
init|=
name|ctx
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|SendEmail
argument_list|(
literal|1L
argument_list|,
literal|"someone@somewhere.org"
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|SendEmail
argument_list|(
literal|2L
argument_list|,
literal|"someoneelse@somewhere.org"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
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
operator|new
name|Customizer
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|InputStream
name|customizeTestProbe
parameter_list|(
name|InputStream
name|testProbe
parameter_list|)
block|{
return|return
name|modifyBundle
argument_list|(
name|testProbe
argument_list|)
operator|.
name|add
argument_list|(
name|SendEmail
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
literal|"META-INF/persistence.xml"
argument_list|,
name|JpaBlueprintRouteTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/META-INF/persistence.xml"
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
literal|"OSGI-INF/blueprint/test.xml"
argument_list|,
name|JpaBlueprintRouteTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"blueprintCamelContext.xml"
argument_list|)
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|BUNDLE_SYMBOLICNAME
argument_list|,
literal|"CamelBlueprintJpaTestBundle"
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
name|set
argument_list|(
literal|"Meta-Persistence"
argument_list|,
literal|"META-INF/persistence.xml"
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
argument_list|,
name|scanFeatures
argument_list|(
name|getKarafFeatureUrl
argument_list|()
argument_list|,
literal|"spring"
argument_list|)
argument_list|,
name|scanFeatures
argument_list|(
name|getKarafEnterpriseFeatureUrl
argument_list|()
argument_list|,
literal|"jndi"
argument_list|,
literal|"jpa"
argument_list|,
literal|"transaction"
argument_list|)
argument_list|,
name|mavenBundle
argument_list|(
literal|"org.apache.derby"
argument_list|,
literal|"derby"
argument_list|,
literal|"10.4.2.0"
argument_list|)
argument_list|,
comment|// using the features to install the camel components
name|scanFeatures
argument_list|(
name|getCamelKarafFeatureUrl
argument_list|()
argument_list|,
literal|"camel-blueprint"
argument_list|,
literal|"camel-jpa"
argument_list|)
argument_list|,
name|felix
argument_list|()
argument_list|,
name|equinox
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|options
return|;
block|}
block|}
end_class

end_unit

