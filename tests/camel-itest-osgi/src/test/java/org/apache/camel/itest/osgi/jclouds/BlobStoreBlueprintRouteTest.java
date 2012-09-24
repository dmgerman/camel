begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.jclouds
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
name|jclouds
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
name|jclouds
operator|.
name|JcloudsConstants
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
name|jclouds
operator|.
name|blobstore
operator|.
name|BlobStore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jclouds
operator|.
name|blobstore
operator|.
name|BlobStoreContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jclouds
operator|.
name|blobstore
operator|.
name|BlobStoreContextFactory
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
name|provision
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

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4TestRunner
operator|.
name|class
argument_list|)
DECL|class|BlobStoreBlueprintRouteTest
specifier|public
class|class
name|BlobStoreBlueprintRouteTest
extends|extends
name|OSGiBlueprintTestSupport
block|{
DECL|field|TEST_CONTAINER
specifier|private
specifier|static
specifier|final
name|String
name|TEST_CONTAINER
init|=
literal|"testContainer"
decl_stmt|;
comment|/**      * Strategy to perform any pre setup, before {@link org.apache.camel.CamelContext} is created      */
annotation|@
name|Override
DECL|method|doPreSetup ()
specifier|protected
name|void
name|doPreSetup
parameter_list|()
throws|throws
name|Exception
block|{
name|BlobStoreContextFactory
name|contextFactory
init|=
operator|new
name|BlobStoreContextFactory
argument_list|()
decl_stmt|;
name|BlobStoreContext
name|blobStoreContext
init|=
name|contextFactory
operator|.
name|createContext
argument_list|(
literal|"transient"
argument_list|,
literal|"identity"
argument_list|,
literal|"credential"
argument_list|)
decl_stmt|;
name|BlobStore
name|blobStore
init|=
name|blobStoreContext
operator|.
name|getBlobStore
argument_list|()
decl_stmt|;
name|blobStore
operator|.
name|createContainerInLocation
argument_list|(
literal|null
argument_list|,
name|TEST_CONTAINER
argument_list|)
expr_stmt|;
name|blobStore
operator|.
name|clearContainer
argument_list|(
name|TEST_CONTAINER
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProducerAndConsumer ()
specifier|public
name|void
name|testProducerAndConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|getInstalledBundle
argument_list|(
literal|"CamelBlueprintJcloudsTestBundle"
argument_list|)
operator|.
name|start
argument_list|()
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
literal|"(camel.context.symbolicname=CamelBlueprintJcloudsTestBundle)"
argument_list|,
literal|20000
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|ctx
operator|.
name|getEndpoint
argument_list|(
literal|"mock:results"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|ProducerTemplate
name|template
init|=
name|ctx
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Test 1"
argument_list|,
name|JcloudsConstants
operator|.
name|BLOB_NAME
argument_list|,
literal|"blob1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Test 2"
argument_list|,
name|JcloudsConstants
operator|.
name|BLOB_NAME
argument_list|,
literal|"blob2"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|template
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
comment|//Helper.setLogLevel("INFO"),
name|provision
argument_list|(
name|newBundle
argument_list|()
operator|.
name|add
argument_list|(
literal|"META-INF/persistence.xml"
argument_list|,
name|BlobStoreBlueprintRouteTest
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
name|BlobStoreBlueprintRouteTest
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
literal|"CamelBlueprintJcloudsTestBundle"
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
name|BlobStoreBlueprintRouteTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"blueprintBlobStoreService.xml"
argument_list|)
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|BUNDLE_SYMBOLICNAME
argument_list|,
literal|"org.apache.camel.jclouds.blobstore.service"
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
name|start
argument_list|()
argument_list|,
comment|// using the features to install the camel components
name|loadCamelFeatures
argument_list|(
literal|"camel-blueprint"
argument_list|,
literal|"camel-jclouds"
argument_list|)
argument_list|,
name|workingDirectory
argument_list|(
literal|"target/paxrunner/"
argument_list|)
argument_list|,
name|felix
argument_list|()
argument_list|)
decl_stmt|;
comment|// TODO: equinox fails for some reason
return|return
name|options
return|;
block|}
block|}
end_class

end_unit

