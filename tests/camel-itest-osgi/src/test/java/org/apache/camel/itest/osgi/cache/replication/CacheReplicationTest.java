begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.cache.replication
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
name|cache
operator|.
name|replication
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
name|itest
operator|.
name|osgi
operator|.
name|OSGiIntegrationSpringTestSupport
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

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4TestRunner
operator|.
name|class
argument_list|)
DECL|class|CacheReplicationTest
specifier|public
class|class
name|CacheReplicationTest
extends|extends
name|OSGiIntegrationSpringTestSupport
block|{
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|OsgiBundleXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|OsgiBundleXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"org/apache/camel/itest/osgi/cache/replication/JMSReplicationCamelContext.xml"
block|}
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testCache ()
specifier|public
name|void
name|testCache
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result1"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Am I replicated?"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result2"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Am I replicated?"
argument_list|)
expr_stmt|;
comment|// do some routes to let everything be initialized
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:getRoute1"
argument_list|,
literal|"Let initialize the route"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:getRoute2"
argument_list|,
literal|"Let initialize the route"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:addRoute"
argument_list|,
literal|"Am I replicated?"
argument_list|)
expr_stmt|;
comment|// give some time to make replication
name|Thread
operator|.
name|sleep
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:getRoute1"
argument_list|,
literal|"Will I get replicated cache"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:getRoute2"
argument_list|,
literal|"Will I get replicated cache"
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
comment|// Default karaf environment
name|getDefaultCamelKarafOptions
argument_list|()
argument_list|,
comment|// using the features to install the camel components
name|scanFeatures
argument_list|(
name|getCamelKarafFeatureUrl
argument_list|()
argument_list|,
literal|"jetty"
argument_list|,
literal|"camel-jms"
argument_list|,
literal|"camel-cache"
argument_list|)
argument_list|,
comment|// using the features to install AMQ
name|scanFeatures
argument_list|(
literal|"mvn:org.apache.activemq/activemq-karaf/5.5.0/xml/features"
argument_list|,
literal|"activemq"
argument_list|)
argument_list|,
name|felix
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

