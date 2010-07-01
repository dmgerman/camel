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
name|java
operator|.
name|util
operator|.
name|List
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
name|builder
operator|.
name|RouteBuilder
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
name|OSGiIntegrationTestSupport
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
name|spring
operator|.
name|SpringCamelContext
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
name|spring
operator|.
name|SpringRouteBuilder
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
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|orm
operator|.
name|jpa
operator|.
name|JpaTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|orm
operator|.
name|jpa
operator|.
name|JpaTransactionManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|osgi
operator|.
name|context
operator|.
name|support
operator|.
name|OsgiBundleXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|TransactionDefinition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|TransactionStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|support
operator|.
name|TransactionCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|support
operator|.
name|TransactionTemplate
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

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4TestRunner
operator|.
name|class
argument_list|)
DECL|class|JpaRouteTest
specifier|public
class|class
name|JpaRouteTest
extends|extends
name|OSGiIntegrationTestSupport
block|{
DECL|field|SELECT_ALL_STRING
specifier|protected
specifier|static
specifier|final
name|String
name|SELECT_ALL_STRING
init|=
literal|"select x from "
operator|+
name|SendEmail
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" x"
decl_stmt|;
DECL|field|applicationContext
specifier|protected
name|OsgiBundleXmlApplicationContext
name|applicationContext
decl_stmt|;
annotation|@
name|Inject
DECL|field|bundleContext
specifier|protected
name|BundleContext
name|bundleContext
decl_stmt|;
DECL|field|jpaTemplate
specifier|protected
name|JpaTemplate
name|jpaTemplate
decl_stmt|;
annotation|@
name|Test
DECL|method|testRouteJpa ()
specifier|public
name|void
name|testRouteJpa
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|SendEmail
argument_list|(
literal|"someone@somewhere.org"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEntityInDB
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
try|try
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|applicationContext
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|applicationContext
operator|.
name|isActive
argument_list|()
condition|)
block|{
name|applicationContext
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
name|applicationContext
operator|=
literal|null
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
comment|// Don't throw the exception in the tearDown method
block|}
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|applicationContext
operator|=
operator|new
name|OsgiBundleXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"org/apache/camel/itest/osgi/jpa/springJpaRouteContext.xml"
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|bundleContext
operator|!=
literal|null
condition|)
block|{
name|applicationContext
operator|.
name|setBundleContext
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
name|applicationContext
operator|.
name|refresh
argument_list|()
expr_stmt|;
block|}
name|cleanupRepository
argument_list|()
expr_stmt|;
return|return
name|SpringCamelContext
operator|.
name|springCamelContext
argument_list|(
name|applicationContext
argument_list|)
return|;
block|}
DECL|method|assertEntityInDB ()
specifier|private
name|void
name|assertEntityInDB
parameter_list|()
throws|throws
name|Exception
block|{
name|jpaTemplate
operator|=
operator|(
name|JpaTemplate
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"jpaTemplate"
argument_list|,
name|JpaTemplate
operator|.
name|class
argument_list|)
expr_stmt|;
name|List
name|list
init|=
name|jpaTemplate
operator|.
name|find
argument_list|(
name|SELECT_ALL_STRING
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|SendEmail
operator|.
name|class
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|cleanupRepository ()
specifier|protected
name|void
name|cleanupRepository
parameter_list|()
block|{
name|jpaTemplate
operator|=
operator|(
name|JpaTemplate
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"jpaTemplate"
argument_list|,
name|JpaTemplate
operator|.
name|class
argument_list|)
expr_stmt|;
name|TransactionTemplate
name|transactionTemplate
init|=
operator|new
name|TransactionTemplate
argument_list|()
decl_stmt|;
name|transactionTemplate
operator|.
name|setTransactionManager
argument_list|(
operator|new
name|JpaTransactionManager
argument_list|(
name|jpaTemplate
operator|.
name|getEntityManagerFactory
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|transactionTemplate
operator|.
name|setPropagationBehavior
argument_list|(
name|TransactionDefinition
operator|.
name|PROPAGATION_REQUIRED
argument_list|)
expr_stmt|;
name|transactionTemplate
operator|.
name|execute
argument_list|(
operator|new
name|TransactionCallback
argument_list|()
block|{
specifier|public
name|Object
name|doInTransaction
parameter_list|(
name|TransactionStatus
name|arg0
parameter_list|)
block|{
name|List
name|list
init|=
name|jpaTemplate
operator|.
name|find
argument_list|(
name|SELECT_ALL_STRING
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|item
range|:
name|list
control|)
block|{
name|jpaTemplate
operator|.
name|remove
argument_list|(
name|item
argument_list|)
expr_stmt|;
block|}
name|jpaTemplate
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
block|}
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
block|{
name|Option
index|[]
name|options
init|=
name|options
argument_list|(
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
literal|"INFO"
argument_list|)
argument_list|,
comment|//org.ops4j.pax.exam.CoreOptions.systemProperty("org.apache.servicemix.specs.debug").value("true"),
comment|//mavenBundle().groupId("net.sourceforge.serp").artifactId("com.springsource.serp").version("1.13.1"),
comment|// using the features to install the camel components
name|scanFeatures
argument_list|(
name|getCamelKarafFeatureUrl
argument_list|()
argument_list|,
literal|"camel-core"
argument_list|,
literal|"camel-spring"
argument_list|,
literal|"camel-test"
argument_list|,
literal|"camel-jpa"
argument_list|)
argument_list|,
comment|/* This the camel-jpa needed bundles              mavenBundle().groupId("org.apache.servicemix.specs").artifactId("org.apache.servicemix.specs.java-persistence-api-1.1.1").version("1.4-SNAPSHOT"),             mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.openjpa").version("1.2.1_1-SNAPSHOT"),             mavenBundle().groupId("org.apache.geronimo.specs").artifactId("geronimo-jta_1.1_spec").version("1.1.1"),             mavenBundle().groupId("org.apache.camel").artifactId("camel-jpa").version("2.1-SNAPSHOT"),             mavenBundle().groupId("org.springframework").artifactId("spring-jdbc").version("2.5.6"),             mavenBundle().groupId("org.springframework").artifactId("spring-tx").version("2.5.6"),             mavenBundle().groupId("org.springframework").artifactId("spring-orm").version("2.5.6"),             mavenBundle().groupId("commons-lang").artifactId("commons-lang").version("2.4"),                 mavenBundle().groupId("commons-collections").artifactId("commons-collections").version("3.2.1"),                 mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.ant").version("1.7.0_1"),                 mavenBundle().groupId("commons-pool").artifactId("commons-pool").version("1.4"),             mavenBundle().groupId("org.apache.geronimo.specs").artifactId("geronimo-jms_1.1_spec").version("1.1.1"),*/
name|mavenBundle
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.apache.derby"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"derby"
argument_list|)
operator|.
name|version
argument_list|(
literal|"10.4.2.0"
argument_list|)
argument_list|,
name|workingDirectory
argument_list|(
literal|"target/paxrunner/"
argument_list|)
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

