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
name|junit
operator|.
name|Ignore
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
name|osgi
operator|.
name|framework
operator|.
name|Constants
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
name|newBundle
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4TestRunner
operator|.
name|class
argument_list|)
DECL|class|BlueprintExplicitPropertiesRouteTest
specifier|public
class|class
name|BlueprintExplicitPropertiesRouteTest
extends|extends
name|OSGiBlueprintTestSupport
block|{
DECL|field|name
specifier|private
name|String
name|name
init|=
name|BlueprintExplicitPropertiesRouteTest
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testBlueprintProperties ()
specifier|public
name|void
name|testBlueprintProperties
parameter_list|()
throws|throws
name|Exception
block|{
comment|// start bundle
name|getInstalledBundle
argument_list|(
name|name
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// must use the camel context from osgi
name|CamelContext
name|ctx
init|=
name|getOsgiService
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|,
literal|"(camel.context.symbolicname="
operator|+
name|name
operator|+
literal|")"
argument_list|,
literal|10000
argument_list|)
decl_stmt|;
name|ProducerTemplate
name|myTemplate
init|=
name|ctx
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|myTemplate
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// do our testing
name|MockEndpoint
name|foo
init|=
name|ctx
operator|.
name|getEndpoint
argument_list|(
literal|"mock:foo"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|foo
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|MockEndpoint
name|result
init|=
name|ctx
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|myTemplate
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|foo
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|myTemplate
operator|.
name|stop
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
name|bundle
argument_list|(
name|newBundle
argument_list|()
operator|.
name|add
argument_list|(
literal|"OSGI-INF/blueprint/test.xml"
argument_list|,
name|BlueprintExplicitPropertiesRouteTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"blueprint-16.xml"
argument_list|)
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|BUNDLE_SYMBOLICNAME
argument_list|,
name|BlueprintExplicitPropertiesRouteTest
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|BUNDLE_VERSION
argument_list|,
literal|"1.0.0"
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
name|scanFeatures
argument_list|(
name|getCamelKarafFeatureUrl
argument_list|()
argument_list|,
literal|"camel-blueprint"
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

