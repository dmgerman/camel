begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeepermaster
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeepermaster
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
name|EndpointInject
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
name|Produce
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
name|Route
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
name|file
operator|.
name|remote
operator|.
name|SftpEndpoint
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
name|impl
operator|.
name|DefaultCamelContext
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
name|support
operator|.
name|ServiceHelper
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
name|AfterClass
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
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
annotation|@
name|ContextConfiguration
DECL|class|MasterEndpointTest
specifier|public
class|class
name|MasterEndpointTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
DECL|field|lastServerBean
specifier|protected
specifier|static
name|ZKServerFactoryBean
name|lastServerBean
decl_stmt|;
DECL|field|lastClientBean
specifier|protected
specifier|static
name|CuratorFactoryBean
name|lastClientBean
decl_stmt|;
annotation|@
name|Autowired
DECL|field|camelContext
specifier|protected
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:results"
argument_list|)
DECL|field|resultEndpoint
specifier|protected
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"seda:bar"
argument_list|)
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
comment|// Yeah this sucks.. why does the spring context not get shutdown
comment|// after each test case?  Not sure!
annotation|@
name|Autowired
DECL|field|zkServerBean
specifier|protected
name|ZKServerFactoryBean
name|zkServerBean
decl_stmt|;
annotation|@
name|Autowired
DECL|field|zkClientBean
specifier|protected
name|CuratorFactoryBean
name|zkClientBean
decl_stmt|;
annotation|@
name|Before
DECL|method|startService ()
specifier|public
name|void
name|startService
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|template
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|afterRun ()
specifier|public
name|void
name|afterRun
parameter_list|()
throws|throws
name|Exception
block|{
name|lastServerBean
operator|=
name|zkServerBean
expr_stmt|;
name|lastClientBean
operator|=
name|zkClientBean
expr_stmt|;
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|shutDownZK ()
specifier|public
specifier|static
name|void
name|shutDownZK
parameter_list|()
throws|throws
name|Exception
block|{
name|lastClientBean
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|lastServerBean
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEndpoint ()
specifier|public
name|void
name|testEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
comment|// check the endpoint configuration
name|List
argument_list|<
name|Route
argument_list|>
name|registeredRoutes
init|=
name|camelContext
operator|.
name|getRoutes
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"number of routes"
argument_list|,
literal|1
argument_list|,
name|registeredRoutes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|MasterEndpoint
name|endpoint
init|=
operator|(
name|MasterEndpoint
operator|)
name|registeredRoutes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"wrong endpoint uri"
argument_list|,
literal|"seda:bar"
argument_list|,
name|endpoint
operator|.
name|getConsumerEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|expectedBody
init|=
literal|"<matched/>"
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
comment|// lets wait for the entry to be registered...
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|expectedBody
argument_list|,
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRawPropertiesOnChild ()
specifier|public
name|void
name|testRawPropertiesOnChild
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|uri
init|=
literal|"zookeeper-master://name:sftp://myhost/inbox?password=RAW(_BEFORE_AMPERSAND_&_AFTER_AMPERSAND_)&username=jdoe"
decl_stmt|;
name|DefaultCamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|MasterEndpoint
name|master
init|=
operator|(
name|MasterEndpoint
operator|)
name|ctx
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|SftpEndpoint
name|sftp
init|=
operator|(
name|SftpEndpoint
operator|)
name|master
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"_BEFORE_AMPERSAND_&_AFTER_AMPERSAND_"
argument_list|,
name|sftp
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

