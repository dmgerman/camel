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
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|util
operator|.
name|jsse
operator|.
name|SSLContextParameters
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
DECL|class|CamelBlueprint8Test
specifier|public
class|class
name|CamelBlueprint8Test
extends|extends
name|OSGiBlueprintTestSupport
block|{
annotation|@
name|Test
DECL|method|testEndpointInjection ()
specifier|public
name|void
name|testEndpointInjection
parameter_list|()
throws|throws
name|Exception
block|{
name|getInstalledBundle
argument_list|(
literal|"CamelBlueprintTestBundle10"
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
literal|"(osgi.blueprint.container.symbolicname=CamelBlueprintTestBundle10)"
argument_list|,
literal|10000
argument_list|)
decl_stmt|;
name|Object
name|producer
init|=
name|ctn
operator|.
name|getComponentInstance
argument_list|(
literal|"producer"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|producer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TestProducer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|producer
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Method
name|mth
init|=
name|producer
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getTestEndpoint"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|mth
operator|.
name|invoke
argument_list|(
name|producer
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testJsseUtilNamespace ()
specifier|public
name|void
name|testJsseUtilNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|getInstalledBundle
argument_list|(
literal|"CamelBlueprintTestBundle18"
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
literal|"(osgi.blueprint.container.symbolicname=CamelBlueprintTestBundle18)"
argument_list|,
literal|10000
argument_list|)
decl_stmt|;
name|SSLContextParameters
name|scp
init|=
operator|(
name|SSLContextParameters
operator|)
name|ctn
operator|.
name|getComponentInstance
argument_list|(
literal|"sslContextParameters"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"TLS"
argument_list|,
name|scp
operator|.
name|getSecureSocketProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|scp
operator|.
name|getKeyManagers
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"changeit"
argument_list|,
name|scp
operator|.
name|getKeyManagers
argument_list|()
operator|.
name|getKeyPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|scp
operator|.
name|getKeyManagers
argument_list|()
operator|.
name|getProvider
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|scp
operator|.
name|getKeyManagers
argument_list|()
operator|.
name|getKeyStore
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|scp
operator|.
name|getKeyManagers
argument_list|()
operator|.
name|getKeyStore
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|scp
operator|.
name|getTrustManagers
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|scp
operator|.
name|getTrustManagers
argument_list|()
operator|.
name|getProvider
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|scp
operator|.
name|getTrustManagers
argument_list|()
operator|.
name|getKeyStore
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|scp
operator|.
name|getTrustManagers
argument_list|()
operator|.
name|getKeyStore
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|scp
operator|.
name|getSecureRandom
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|scp
operator|.
name|getClientParameters
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|scp
operator|.
name|getServerParameters
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|scp
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|scp
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|scp
operator|.
name|getKeyManagers
argument_list|()
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|scp
operator|.
name|getKeyManagers
argument_list|()
operator|.
name|getKeyStore
argument_list|()
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|scp
operator|.
name|getTrustManagers
argument_list|()
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|scp
operator|.
name|getTrustManagers
argument_list|()
operator|.
name|getKeyStore
argument_list|()
operator|.
name|getCamelContext
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
literal|"blueprint-10.xml"
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
name|TestProducer
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
literal|"CamelBlueprintTestBundle10"
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
literal|"blueprint-18.xml"
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
name|JsseUtilTester
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
literal|"localhost.ks"
argument_list|,
name|OSGiBlueprintTestSupport
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/org/apache/camel/itest/osgi/util/jsse/localhost.ks"
argument_list|)
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|BUNDLE_SYMBOLICNAME
argument_list|,
literal|"CamelBlueprintTestBundle18"
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
argument_list|)
argument_list|)
decl_stmt|;
comment|// for remote debugging
comment|// vmOption("-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5008"));
return|return
name|options
return|;
block|}
block|}
end_class

end_unit

