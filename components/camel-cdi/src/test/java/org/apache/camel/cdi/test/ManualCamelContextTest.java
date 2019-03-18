begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi.test
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|test
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
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
name|ServiceStatus
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
name|cdi
operator|.
name|CdiCamelExtension
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
name|cdi
operator|.
name|Uri
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
name|cdi
operator|.
name|bean
operator|.
name|ManualCamelRoute
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
name|cdi
operator|.
name|bean
operator|.
name|SimpleCamelRoute
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
name|cdi
operator|.
name|qualifier
operator|.
name|Manual
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
name|jboss
operator|.
name|arquillian
operator|.
name|container
operator|.
name|test
operator|.
name|api
operator|.
name|Deployment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|arquillian
operator|.
name|junit
operator|.
name|Arquillian
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|arquillian
operator|.
name|junit
operator|.
name|InSequence
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|Archive
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|ShrinkWrap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|asset
operator|.
name|EmptyAsset
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|spec
operator|.
name|JavaArchive
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
import|import static
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
operator|.
name|assertIsSatisfied
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|equalTo
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|is
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
name|assertThat
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|Arquillian
operator|.
name|class
argument_list|)
DECL|class|ManualCamelContextTest
specifier|public
class|class
name|ManualCamelContextTest
block|{
annotation|@
name|Inject
annotation|@
name|Uri
argument_list|(
literal|"direct:start"
argument_list|)
DECL|field|inbound
specifier|private
name|ProducerTemplate
name|inbound
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Uri
argument_list|(
literal|"mock:result"
argument_list|)
DECL|field|outbound
specifier|private
name|MockEndpoint
name|outbound
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Uri
argument_list|(
literal|"direct:manual"
argument_list|)
DECL|field|manual
specifier|private
name|ProducerTemplate
name|manual
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Uri
argument_list|(
literal|"mock:manual"
argument_list|)
DECL|field|mock
specifier|private
name|MockEndpoint
name|mock
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Manual
DECL|field|builder
specifier|private
name|ManualCamelRoute
name|builder
decl_stmt|;
annotation|@
name|Deployment
DECL|method|deployment ()
specifier|public
specifier|static
name|Archive
argument_list|<
name|?
argument_list|>
name|deployment
parameter_list|()
block|{
return|return
name|ShrinkWrap
operator|.
name|create
argument_list|(
name|JavaArchive
operator|.
name|class
argument_list|)
comment|// Camel CDI
operator|.
name|addPackage
argument_list|(
name|CdiCamelExtension
operator|.
name|class
operator|.
name|getPackage
argument_list|()
argument_list|)
comment|// Test classes
operator|.
name|addClasses
argument_list|(
name|SimpleCamelRoute
operator|.
name|class
argument_list|,
name|ManualCamelRoute
operator|.
name|class
argument_list|)
comment|// Bean archive deployment descriptor
operator|.
name|addAsManifestResource
argument_list|(
name|EmptyAsset
operator|.
name|INSTANCE
argument_list|,
literal|"beans.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
annotation|@
name|InSequence
argument_list|(
literal|1
argument_list|)
DECL|method|verifyContext (CamelContext context)
specifier|public
name|void
name|verifyContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|assertThat
argument_list|(
literal|"Number of routes is incorrect!"
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|is
argument_list|(
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
literal|"Configured route is incorrect!"
argument_list|,
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
literal|"simple"
argument_list|)
argument_list|,
name|is
argument_list|(
name|equalTo
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|InSequence
argument_list|(
literal|2
argument_list|)
DECL|method|addManualRoute (CamelContext context)
specifier|public
name|void
name|addManualRoute
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
literal|"Number of routes is incorrect!"
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|is
argument_list|(
name|equalTo
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
literal|"Configured route is incorrect!"
argument_list|,
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
literal|"manual"
argument_list|)
argument_list|,
name|is
argument_list|(
name|equalTo
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|InSequence
argument_list|(
literal|3
argument_list|)
DECL|method|sendMessageToInbound ()
specifier|public
name|void
name|sendMessageToInbound
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|outbound
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|outbound
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|inbound
operator|.
name|sendBody
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|assertIsSatisfied
argument_list|(
literal|2L
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|,
name|outbound
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|InSequence
argument_list|(
literal|4
argument_list|)
DECL|method|sendMessageToManual ()
specifier|public
name|void
name|sendMessageToManual
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"manual"
argument_list|)
expr_stmt|;
name|manual
operator|.
name|sendBody
argument_list|(
literal|"manual"
argument_list|)
expr_stmt|;
name|assertIsSatisfied
argument_list|(
literal|2L
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|,
name|mock
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

