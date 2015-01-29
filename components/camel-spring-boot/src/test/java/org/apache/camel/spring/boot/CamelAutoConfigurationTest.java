begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
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
name|ConsumerTemplate
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
name|TypeConverter
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
name|junit
operator|.
name|Assert
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
name|boot
operator|.
name|autoconfigure
operator|.
name|EnableAutoConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|test
operator|.
name|IntegrationTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|test
operator|.
name|SpringApplicationConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Bean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Configuration
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
name|SpringJUnit4ClassRunner
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
name|spring
operator|.
name|boot
operator|.
name|TestConfig
operator|.
name|ROUTE_ID
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|SpringJUnit4ClassRunner
operator|.
name|class
argument_list|)
annotation|@
name|EnableAutoConfiguration
annotation|@
name|SpringApplicationConfiguration
argument_list|(
name|classes
operator|=
block|{
name|TestConfig
operator|.
name|class
block|,
name|CamelAutoConfigurationTest
operator|.
name|class
block|,
name|RouteConfigWithCamelContextInjected
operator|.
name|class
block|}
argument_list|)
annotation|@
name|IntegrationTest
argument_list|(
block|{
literal|"camel.springboot.consumerTemplateCacheSize=100"
block|,
literal|"camel.springboot.jmxEnabled=true"
block|,
literal|"camel.springboot.name=customName"
block|}
argument_list|)
DECL|class|CamelAutoConfigurationTest
specifier|public
class|class
name|CamelAutoConfigurationTest
extends|extends
name|Assert
block|{
comment|// Collaborators fixtures
annotation|@
name|Autowired
DECL|field|camelContext
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|Autowired
DECL|field|camelContextConfiguration
name|CamelContextConfiguration
name|camelContextConfiguration
decl_stmt|;
annotation|@
name|Autowired
DECL|field|producerTemplate
name|ProducerTemplate
name|producerTemplate
decl_stmt|;
annotation|@
name|Autowired
DECL|field|consumerTemplate
name|ConsumerTemplate
name|consumerTemplate
decl_stmt|;
annotation|@
name|Autowired
DECL|field|typeConverter
name|TypeConverter
name|typeConverter
decl_stmt|;
comment|// Spring context fixtures
comment|// Tests
annotation|@
name|Test
DECL|method|shouldCreateCamelContext ()
specifier|public
name|void
name|shouldCreateCamelContext
parameter_list|()
block|{
name|assertNotNull
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldDetectRoutes ()
specifier|public
name|void
name|shouldDetectRoutes
parameter_list|()
block|{
comment|// When
name|Route
name|route
init|=
name|camelContext
operator|.
name|getRoute
argument_list|(
name|ROUTE_ID
argument_list|)
decl_stmt|;
comment|// Then
name|assertNotNull
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldLoadProducerTemplate ()
specifier|public
name|void
name|shouldLoadProducerTemplate
parameter_list|()
block|{
name|assertNotNull
argument_list|(
name|producerTemplate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldLoadConsumerTemplate ()
specifier|public
name|void
name|shouldLoadConsumerTemplate
parameter_list|()
block|{
name|assertNotNull
argument_list|(
name|consumerTemplate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldLoadConsumerTemplateWithSizeFromProperties ()
specifier|public
name|void
name|shouldLoadConsumerTemplateWithSizeFromProperties
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|consumerTemplate
operator|.
name|getMaximumCacheSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSendAndReceiveMessageWithTemplates ()
specifier|public
name|void
name|shouldSendAndReceiveMessageWithTemplates
parameter_list|()
block|{
comment|// Given
name|String
name|message
init|=
literal|"message"
decl_stmt|;
name|String
name|seda
init|=
literal|"seda:test"
decl_stmt|;
comment|// When
name|producerTemplate
operator|.
name|sendBody
argument_list|(
name|seda
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|String
name|receivedBody
init|=
name|consumerTemplate
operator|.
name|receiveBody
argument_list|(
name|seda
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Then
name|assertEquals
argument_list|(
name|message
argument_list|,
name|receivedBody
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldLoadTypeConverters ()
specifier|public
name|void
name|shouldLoadTypeConverters
parameter_list|()
block|{
comment|// Given
name|Long
name|hundred
init|=
literal|100L
decl_stmt|;
comment|// When
name|Long
name|convertedLong
init|=
name|typeConverter
operator|.
name|convertTo
argument_list|(
name|Long
operator|.
name|class
argument_list|,
name|hundred
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
comment|// Then
name|assertEquals
argument_list|(
name|hundred
argument_list|,
name|convertedLong
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldCallCamelContextConfiguration ()
specifier|public
name|void
name|shouldCallCamelContextConfiguration
parameter_list|()
block|{
name|verify
argument_list|(
name|camelContextConfiguration
argument_list|)
operator|.
name|beforeApplicationStart
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldChangeContextNameViaConfigurationCallback ()
specifier|public
name|void
name|shouldChangeContextNameViaConfigurationCallback
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"customName"
argument_list|,
name|camelContext
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|camelContext
operator|.
name|getName
argument_list|()
argument_list|,
name|camelContext
operator|.
name|getManagementName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldStartRoute ()
specifier|public
name|void
name|shouldStartRoute
parameter_list|()
block|{
comment|// Given
name|String
name|message
init|=
literal|"msg"
decl_stmt|;
comment|// When
name|producerTemplate
operator|.
name|sendBody
argument_list|(
literal|"seda:test"
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|String
name|receivedMessage
init|=
name|consumerTemplate
operator|.
name|receiveBody
argument_list|(
literal|"seda:test"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Then
name|assertEquals
argument_list|(
name|message
argument_list|,
name|receivedMessage
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_class
annotation|@
name|Configuration
DECL|class|TestConfig
class|class
name|TestConfig
block|{
comment|// Constants
DECL|field|ROUTE_ID
specifier|static
specifier|final
name|String
name|ROUTE_ID
init|=
literal|"testRoute"
decl_stmt|;
comment|// Test beans
annotation|@
name|Bean
DECL|method|routeBuilder ()
name|RouteBuilder
name|routeBuilder
parameter_list|()
block|{
return|return
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
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:test"
argument_list|)
operator|.
name|routeId
argument_list|(
name|ROUTE_ID
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:test"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Bean
DECL|method|camelContextConfiguration ()
name|CamelContextConfiguration
name|camelContextConfiguration
parameter_list|()
block|{
return|return
name|mock
argument_list|(
name|CamelContextConfiguration
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

