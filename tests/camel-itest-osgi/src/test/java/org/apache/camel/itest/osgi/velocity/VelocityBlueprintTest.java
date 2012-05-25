begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.velocity
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
name|velocity
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
name|Exchange
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
name|InvalidPayloadException
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
name|Message
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
name|Processor
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
name|CoreOptions
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
DECL|class|VelocityBlueprintTest
specifier|public
class|class
name|VelocityBlueprintTest
extends|extends
name|OSGiBlueprintTestSupport
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|mytemplate
specifier|private
name|ProducerTemplate
name|mytemplate
decl_stmt|;
annotation|@
name|Test
DECL|method|testReceivesResponse ()
specifier|public
name|void
name|testReceivesResponse
parameter_list|()
throws|throws
name|Exception
block|{
name|assertRespondsWith
argument_list|(
literal|"foo"
argument_list|,
literal|"<header>foo</header><hello>foo</hello>"
argument_list|)
expr_stmt|;
name|assertRespondsWith
argument_list|(
literal|"bar"
argument_list|,
literal|"<header>bar</header><hello>bar</hello>"
argument_list|)
expr_stmt|;
block|}
DECL|method|assertRespondsWith (final String value, String expectedBody)
specifier|protected
name|void
name|assertRespondsWith
parameter_list|(
specifier|final
name|String
name|value
parameter_list|,
name|String
name|expectedBody
parameter_list|)
throws|throws
name|InvalidPayloadException
block|{
name|Exchange
name|response
init|=
name|mytemplate
operator|.
name|request
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
literal|"answer"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"cheese"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertOutMessageBodyEquals
argument_list|(
name|response
argument_list|,
name|expectedBody
argument_list|)
expr_stmt|;
block|}
DECL|method|doPostSetup ()
specifier|protected
name|void
name|doPostSetup
parameter_list|()
throws|throws
name|Exception
block|{
name|getInstalledBundle
argument_list|(
literal|"VelocityBlueprintRouterTest"
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
name|camelContext
operator|=
name|getOsgiService
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|,
literal|"(camel.context.symbolicname=VelocityBlueprintRouterTest)"
argument_list|,
literal|10000
argument_list|)
expr_stmt|;
name|mytemplate
operator|=
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
name|mytemplate
operator|.
name|start
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
comment|// using the features to install the camel components
name|loadCamelFeatures
argument_list|(
literal|"camel-blueprint"
argument_list|,
literal|"camel-velocity"
argument_list|)
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
name|VelocityBlueprintTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"VelocityBlueprintRouter.xml"
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
literal|"example.vm"
argument_list|,
name|VelocityBlueprintTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"example.vm"
argument_list|)
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|BUNDLE_SYMBOLICNAME
argument_list|,
literal|"VelocityBlueprintRouterTest"
argument_list|)
operator|.
name|build
argument_list|(
name|withBnd
argument_list|()
argument_list|)
argument_list|)
operator|.
name|noStart
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

