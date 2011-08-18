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
name|karaf
operator|.
name|testing
operator|.
name|Helper
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
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
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
name|Method
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

begin_comment
comment|/**  * Test cases to ensure that the Blueprint component is correctly setting the Thread's context classloader when starting  * the routes  *  * @version   */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4TestRunner
operator|.
name|class
argument_list|)
DECL|class|CamelBlueprintTcclTest
specifier|public
class|class
name|CamelBlueprintTcclTest
extends|extends
name|OSGiBlueprintTestSupport
block|{
DECL|field|BUNDLE_SYMBOLICNAME
specifier|private
specifier|static
specifier|final
name|String
name|BUNDLE_SYMBOLICNAME
init|=
literal|"CamelBlueprintTcclTestBundle"
decl_stmt|;
annotation|@
name|Test
DECL|method|testCorrectTcclSetForRoutes ()
specifier|public
name|void
name|testCorrectTcclSetForRoutes
parameter_list|()
throws|throws
name|Exception
block|{
name|BlueprintContainer
name|ctn
init|=
name|getOsgiService
argument_list|(
name|BlueprintContainer
operator|.
name|class
argument_list|,
literal|"(osgi.blueprint.container.symbolicname=CamelBlueprintTcclTestBundle)"
argument_list|,
literal|10000
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
literal|"(camel.context.symbolicname=CamelBlueprintTcclTestBundle)"
argument_list|,
literal|10000
argument_list|)
decl_stmt|;
name|assertBundleDelegatingClassLoader
argument_list|(
name|ctx
operator|.
name|getApplicationContextClassLoader
argument_list|()
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
name|MockEndpoint
name|mock
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
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|ClassLoader
name|tccl
init|=
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getProperty
argument_list|(
name|ThreadContextClassLoaderBean
operator|.
name|THREAD_CONTEXT_CLASS_LOADER
argument_list|,
name|ClassLoader
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Exchange property containing TCCL should have been set"
argument_list|,
name|tccl
argument_list|)
expr_stmt|;
name|assertBundleDelegatingClassLoader
argument_list|(
name|tccl
argument_list|)
expr_stmt|;
name|template
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|assertBundleDelegatingClassLoader (ClassLoader tccl)
specifier|private
name|void
name|assertBundleDelegatingClassLoader
parameter_list|(
name|ClassLoader
name|tccl
parameter_list|)
throws|throws
name|NoSuchMethodException
throws|,
name|IllegalAccessException
throws|,
name|InvocationTargetException
block|{
comment|// camel-blueprint does not export the BundleDelegatingClassLoader package so we need a little pinch of reflection here
name|assertTrue
argument_list|(
literal|"Expected a BundleDelegatingClassLoader instance"
argument_list|,
name|tccl
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"BundleDelegatingClassLoader"
argument_list|)
argument_list|)
expr_stmt|;
name|Method
name|getBundle
init|=
name|tccl
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getBundle"
argument_list|)
decl_stmt|;
name|Bundle
name|bundle
init|=
operator|(
name|Bundle
operator|)
name|getBundle
operator|.
name|invoke
argument_list|(
name|tccl
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|BUNDLE_SYMBOLICNAME
argument_list|,
name|bundle
operator|.
name|getSymbolicName
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
return|return
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
literal|"blueprint-tccl.xml"
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
name|ThreadContextClassLoaderBean
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
name|BUNDLE_SYMBOLICNAME
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|IMPORT_PACKAGE
argument_list|,
literal|"org.apache.camel"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
argument_list|,
comment|// using the features to install the camel components
name|scanFeatures
argument_list|(
name|getCamelKarafFeatureUrl
argument_list|()
argument_list|,
literal|"camel-blueprint"
argument_list|)
argument_list|,
name|workingDirectory
argument_list|(
literal|"target/paxrunner/"
argument_list|)
argument_list|,
name|felix
argument_list|()
argument_list|,
name|equinox
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Camel {@link Processor} that injects startup thread context classloader into the exchange for testing purposes      */
DECL|class|ThreadContextClassLoaderBean
specifier|public
specifier|static
specifier|final
class|class
name|ThreadContextClassLoaderBean
implements|implements
name|Processor
block|{
DECL|field|THREAD_CONTEXT_CLASS_LOADER
specifier|public
specifier|static
specifier|final
name|String
name|THREAD_CONTEXT_CLASS_LOADER
init|=
literal|"CamelThreadContextClassLoader"
decl_stmt|;
DECL|field|tccl
specifier|private
specifier|final
name|ClassLoader
name|tccl
decl_stmt|;
DECL|method|ThreadContextClassLoaderBean ()
specifier|public
name|ThreadContextClassLoaderBean
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|tccl
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
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
name|exchange
operator|.
name|setProperty
argument_list|(
name|THREAD_CONTEXT_CLASS_LOADER
argument_list|,
name|tccl
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

