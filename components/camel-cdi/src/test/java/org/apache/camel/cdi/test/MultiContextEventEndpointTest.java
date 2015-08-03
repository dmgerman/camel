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
name|ArrayList
import|;
end_import

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
name|enterprise
operator|.
name|event
operator|.
name|Event
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|event
operator|.
name|Observes
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
name|FirstCamelContextBean
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
name|FirstCamelContextEventConsumingRoute
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
name|FirstCamelContextEventProducingRoute
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
name|SecondCamelContextBean
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
name|SecondCamelContextEventConsumingRoute
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
name|SecondCamelContextEventProducingRoute
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
name|hamcrest
operator|.
name|Matchers
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
name|contains
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
DECL|class|MultiContextEventEndpointTest
specifier|public
class|class
name|MultiContextEventEndpointTest
block|{
annotation|@
name|Inject
annotation|@
name|ContextName
argument_list|(
literal|"first"
argument_list|)
annotation|@
name|Uri
argument_list|(
literal|"mock:consumeString"
argument_list|)
DECL|field|firstConsumeString
specifier|private
name|MockEndpoint
name|firstConsumeString
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|ContextName
argument_list|(
literal|"second"
argument_list|)
annotation|@
name|Uri
argument_list|(
literal|"mock:consumeString"
argument_list|)
DECL|field|secondConsumeString
specifier|private
name|MockEndpoint
name|secondConsumeString
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|ContextName
argument_list|(
literal|"first"
argument_list|)
annotation|@
name|Uri
argument_list|(
literal|"direct:produceString"
argument_list|)
DECL|field|firstProduceString
specifier|private
name|ProducerTemplate
name|firstProduceString
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|ContextName
argument_list|(
literal|"second"
argument_list|)
annotation|@
name|Uri
argument_list|(
literal|"direct:produceString"
argument_list|)
DECL|field|secondProduceString
specifier|private
name|ProducerTemplate
name|secondProduceString
decl_stmt|;
annotation|@
name|Inject
DECL|field|objectEvent
specifier|private
name|Event
argument_list|<
name|Object
argument_list|>
name|objectEvent
decl_stmt|;
annotation|@
name|Inject
DECL|field|observer
specifier|private
name|EventObserver
name|observer
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
name|FirstCamelContextBean
operator|.
name|class
argument_list|,
name|FirstCamelContextEventConsumingRoute
operator|.
name|class
argument_list|,
name|FirstCamelContextEventProducingRoute
operator|.
name|class
argument_list|,
name|SecondCamelContextBean
operator|.
name|class
argument_list|,
name|SecondCamelContextEventConsumingRoute
operator|.
name|class
argument_list|,
name|SecondCamelContextEventProducingRoute
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
DECL|method|configureCamelContexts (@ontextNameR) CamelContext secondContext)
specifier|public
name|void
name|configureCamelContexts
parameter_list|(
annotation|@
name|ContextName
argument_list|(
literal|"second"
argument_list|)
name|CamelContext
name|secondContext
parameter_list|)
throws|throws
name|Exception
block|{
name|secondContext
operator|.
name|startAllRoutes
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|InSequence
argument_list|(
literal|2
argument_list|)
DECL|method|sendEventsToConsumers ()
specifier|public
name|void
name|sendEventsToConsumers
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|firstConsumeString
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|firstConsumeString
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"testFirst"
argument_list|)
expr_stmt|;
name|secondConsumeString
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|secondConsumeString
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"testSecond1"
argument_list|,
literal|"testSecond2"
argument_list|)
expr_stmt|;
name|objectEvent
operator|.
name|select
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|ContextName
operator|.
name|Literal
argument_list|(
literal|"first"
argument_list|)
argument_list|)
operator|.
name|fire
argument_list|(
literal|"testFirst"
argument_list|)
expr_stmt|;
name|objectEvent
operator|.
name|select
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|ContextName
operator|.
name|Literal
argument_list|(
literal|"second"
argument_list|)
argument_list|)
operator|.
name|fire
argument_list|(
literal|"testSecond1"
argument_list|)
expr_stmt|;
name|objectEvent
operator|.
name|select
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|ContextName
operator|.
name|Literal
argument_list|(
literal|"second"
argument_list|)
argument_list|)
operator|.
name|fire
argument_list|(
literal|"testSecond2"
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
name|firstConsumeString
argument_list|,
name|secondConsumeString
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
DECL|method|sendMessagesToProducers ()
specifier|public
name|void
name|sendMessagesToProducers
parameter_list|()
block|{
name|firstProduceString
operator|.
name|sendBody
argument_list|(
literal|"testFirst"
argument_list|)
expr_stmt|;
name|secondProduceString
operator|.
name|sendBody
argument_list|(
literal|"testSecond"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|observer
operator|.
name|getObjectEvents
argument_list|()
argument_list|,
name|Matchers
operator|.
expr|<
name|Object
operator|>
name|contains
argument_list|(
literal|"testFirst"
argument_list|,
literal|"testSecond"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|observer
operator|.
name|getStringEvents
argument_list|()
argument_list|,
name|contains
argument_list|(
literal|"testFirst"
argument_list|,
literal|"testSecond"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|observer
operator|.
name|getFirstStringEvents
argument_list|()
argument_list|,
name|contains
argument_list|(
literal|"testFirst"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|observer
operator|.
name|secondStringEvents
argument_list|()
argument_list|,
name|contains
argument_list|(
literal|"testSecond"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
DECL|method|resetCollectedEvents ()
specifier|public
name|void
name|resetCollectedEvents
parameter_list|()
block|{
name|observer
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
annotation|@
name|ApplicationScoped
DECL|class|EventObserver
specifier|static
class|class
name|EventObserver
block|{
DECL|field|objectEvents
specifier|private
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|objectEvents
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|stringEvents
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|stringEvents
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|firstStringEvents
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|firstStringEvents
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|secondStringEvents
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|secondStringEvents
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|collectObjectEvents (@bserves Object event)
name|void
name|collectObjectEvents
parameter_list|(
annotation|@
name|Observes
name|Object
name|event
parameter_list|)
block|{
name|objectEvents
operator|.
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
DECL|method|collectStringEvents (@bserves String event)
name|void
name|collectStringEvents
parameter_list|(
annotation|@
name|Observes
name|String
name|event
parameter_list|)
block|{
name|stringEvents
operator|.
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
DECL|method|collectFirstStringEvents (@bserves @ontextNameR) String event)
name|void
name|collectFirstStringEvents
parameter_list|(
annotation|@
name|Observes
annotation|@
name|ContextName
argument_list|(
literal|"first"
argument_list|)
name|String
name|event
parameter_list|)
block|{
name|firstStringEvents
operator|.
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
DECL|method|collectSecondStringEvents (@bserves @ontextNameR) String event)
name|void
name|collectSecondStringEvents
parameter_list|(
annotation|@
name|Observes
annotation|@
name|ContextName
argument_list|(
literal|"second"
argument_list|)
name|String
name|event
parameter_list|)
block|{
name|secondStringEvents
operator|.
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
DECL|method|getObjectEvents ()
name|List
argument_list|<
name|Object
argument_list|>
name|getObjectEvents
parameter_list|()
block|{
return|return
name|objectEvents
return|;
block|}
DECL|method|getStringEvents ()
name|List
argument_list|<
name|String
argument_list|>
name|getStringEvents
parameter_list|()
block|{
return|return
name|stringEvents
return|;
block|}
DECL|method|getFirstStringEvents ()
name|List
argument_list|<
name|String
argument_list|>
name|getFirstStringEvents
parameter_list|()
block|{
return|return
name|firstStringEvents
return|;
block|}
DECL|method|secondStringEvents ()
name|List
argument_list|<
name|String
argument_list|>
name|secondStringEvents
parameter_list|()
block|{
return|return
name|secondStringEvents
return|;
block|}
DECL|method|reset ()
name|void
name|reset
parameter_list|()
block|{
name|objectEvents
operator|.
name|clear
argument_list|()
expr_stmt|;
name|stringEvents
operator|.
name|clear
argument_list|()
expr_stmt|;
name|firstStringEvents
operator|.
name|clear
argument_list|()
expr_stmt|;
name|secondStringEvents
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

