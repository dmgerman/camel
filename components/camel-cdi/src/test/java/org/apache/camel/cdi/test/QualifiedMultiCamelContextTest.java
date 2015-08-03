begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|enterprise
operator|.
name|context
operator|.
name|ApplicationScoped
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
name|javax
operator|.
name|inject
operator|.
name|Named
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
name|ContextName
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
name|DefaultCamelContextBean
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
name|FirstCamelContextRoute
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
name|UriEndpointRoute
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
name|BarQualifier
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
name|FooQualifier
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
name|cdi
operator|.
name|expression
operator|.
name|ExchangeExpression
operator|.
name|fromCamelContext
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
DECL|class|QualifiedMultiCamelContextTest
specifier|public
class|class
name|QualifiedMultiCamelContextTest
block|{
annotation|@
name|Inject
DECL|field|defaultCamelContext
specifier|private
name|CamelContext
name|defaultCamelContext
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Uri
argument_list|(
literal|"direct:inbound"
argument_list|)
DECL|field|defaultInbound
specifier|private
name|ProducerTemplate
name|defaultInbound
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Uri
argument_list|(
literal|"mock:outbound"
argument_list|)
DECL|field|defaultOutbound
specifier|private
name|MockEndpoint
name|defaultOutbound
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|FooQualifier
DECL|field|firstCamelContext
specifier|private
name|CamelContext
name|firstCamelContext
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|FooQualifier
annotation|@
name|Uri
argument_list|(
literal|"direct:inbound"
argument_list|)
DECL|field|firstInbound
specifier|private
name|ProducerTemplate
name|firstInbound
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|FooQualifier
annotation|@
name|Uri
argument_list|(
literal|"mock:outbound"
argument_list|)
DECL|field|firstOutbound
specifier|private
name|MockEndpoint
name|firstOutbound
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|BarQualifier
DECL|field|secondCamelContext
specifier|private
name|CamelContext
name|secondCamelContext
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|BarQualifier
annotation|@
name|Uri
argument_list|(
literal|"direct:inbound"
argument_list|)
DECL|field|secondInbound
specifier|private
name|ProducerTemplate
name|secondInbound
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|BarQualifier
annotation|@
name|Uri
argument_list|(
literal|"mock:outbound"
argument_list|)
DECL|field|secondOutbound
specifier|private
name|MockEndpoint
name|secondOutbound
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
name|DefaultCamelContextBean
operator|.
name|class
argument_list|,
name|UriEndpointRoute
operator|.
name|class
argument_list|,
name|FooCamelContext
operator|.
name|class
argument_list|,
name|FirstCamelContextRoute
operator|.
name|class
argument_list|,
name|BarCamelContext
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
DECL|method|verifyCamelContexts ()
specifier|public
name|void
name|verifyCamelContexts
parameter_list|()
block|{
name|assertThat
argument_list|(
name|defaultCamelContext
operator|.
name|getName
argument_list|()
argument_list|,
name|is
argument_list|(
name|equalTo
argument_list|(
literal|"camel-cdi"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|firstCamelContext
operator|.
name|getName
argument_list|()
argument_list|,
name|is
argument_list|(
name|equalTo
argument_list|(
literal|"first"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|secondCamelContext
operator|.
name|getName
argument_list|()
argument_list|,
name|is
argument_list|(
name|equalTo
argument_list|(
literal|"second"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|defaultOutbound
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|is
argument_list|(
name|equalTo
argument_list|(
name|defaultCamelContext
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|firstOutbound
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|is
argument_list|(
name|equalTo
argument_list|(
name|firstCamelContext
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|secondOutbound
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|is
argument_list|(
name|equalTo
argument_list|(
name|secondCamelContext
operator|.
name|getName
argument_list|()
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
DECL|method|configureCamelContexts ()
specifier|public
name|void
name|configureCamelContexts
parameter_list|()
throws|throws
name|Exception
block|{
name|secondCamelContext
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:inbound"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"context"
argument_list|)
operator|.
name|constant
argument_list|(
literal|"second"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:outbound"
argument_list|)
expr_stmt|;
block|}
block|}
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
DECL|method|sendMessageToDefaultCamelContextInbound ()
specifier|public
name|void
name|sendMessageToDefaultCamelContextInbound
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|defaultOutbound
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|defaultOutbound
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"test-default"
argument_list|)
expr_stmt|;
name|defaultOutbound
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchange
argument_list|()
operator|.
name|matches
argument_list|(
name|fromCamelContext
argument_list|(
literal|"camel-cdi"
argument_list|)
argument_list|)
expr_stmt|;
name|defaultInbound
operator|.
name|sendBody
argument_list|(
literal|"test-default"
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
name|defaultOutbound
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
DECL|method|sendMessageToFirstCamelContextInbound ()
specifier|public
name|void
name|sendMessageToFirstCamelContextInbound
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|firstOutbound
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|firstOutbound
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"test-first"
argument_list|)
expr_stmt|;
name|firstOutbound
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"context"
argument_list|,
literal|"first"
argument_list|)
expr_stmt|;
name|firstOutbound
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchange
argument_list|()
operator|.
name|matches
argument_list|(
name|fromCamelContext
argument_list|(
literal|"first"
argument_list|)
argument_list|)
expr_stmt|;
name|firstInbound
operator|.
name|sendBody
argument_list|(
literal|"test-first"
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
name|firstOutbound
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|InSequence
argument_list|(
literal|5
argument_list|)
DECL|method|sendMessageToSecondCamelContextInbound ()
specifier|public
name|void
name|sendMessageToSecondCamelContextInbound
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|secondOutbound
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|secondOutbound
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"test-second"
argument_list|)
expr_stmt|;
name|secondOutbound
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"context"
argument_list|,
literal|"second"
argument_list|)
expr_stmt|;
name|secondOutbound
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchange
argument_list|()
operator|.
name|matches
argument_list|(
name|fromCamelContext
argument_list|(
literal|"second"
argument_list|)
argument_list|)
expr_stmt|;
name|secondInbound
operator|.
name|sendBody
argument_list|(
literal|"test-second"
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
name|secondOutbound
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_class
annotation|@
name|FooQualifier
annotation|@
name|ContextName
argument_list|(
literal|"first"
argument_list|)
annotation|@
name|ApplicationScoped
DECL|class|FooCamelContext
class|class
name|FooCamelContext
extends|extends
name|DefaultCamelContext
block|{  }
end_class

begin_class
annotation|@
name|BarQualifier
annotation|@
name|Named
argument_list|(
literal|"second"
argument_list|)
annotation|@
name|ApplicationScoped
DECL|class|BarCamelContext
class|class
name|BarCamelContext
extends|extends
name|DefaultCamelContext
block|{  }
end_class

end_unit

