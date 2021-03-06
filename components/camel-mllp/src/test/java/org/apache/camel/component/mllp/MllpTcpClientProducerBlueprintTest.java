begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mllp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|engine
operator|.
name|DefaultComponentResolver
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
name|spi
operator|.
name|ComponentResolver
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
name|test
operator|.
name|AvailablePortFinder
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
name|test
operator|.
name|blueprint
operator|.
name|CamelBlueprintTestSupport
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
name|test
operator|.
name|junit
operator|.
name|rule
operator|.
name|mllp
operator|.
name|MllpServerResource
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
name|test
operator|.
name|mllp
operator|.
name|Hl7TestMessageGenerator
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
name|KeyValueHolder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
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

begin_class
DECL|class|MllpTcpClientProducerBlueprintTest
specifier|public
class|class
name|MllpTcpClientProducerBlueprintTest
extends|extends
name|CamelBlueprintTestSupport
block|{
DECL|field|SOURCE_URI
specifier|static
specifier|final
name|String
name|SOURCE_URI
init|=
literal|"direct-vm://source"
decl_stmt|;
DECL|field|MOCK_ACKNOWLEDGED_URI
specifier|static
specifier|final
name|String
name|MOCK_ACKNOWLEDGED_URI
init|=
literal|"mock://acknowledged"
decl_stmt|;
DECL|field|MOCK_TIMEOUT_URI
specifier|static
specifier|final
name|String
name|MOCK_TIMEOUT_URI
init|=
literal|"mock://timeoutError-ex"
decl_stmt|;
DECL|field|MOCK_AE_EX_URI
specifier|static
specifier|final
name|String
name|MOCK_AE_EX_URI
init|=
literal|"mock://ae-ack"
decl_stmt|;
DECL|field|MOCK_AR_EX_URI
specifier|static
specifier|final
name|String
name|MOCK_AR_EX_URI
init|=
literal|"mock://ar-ack"
decl_stmt|;
DECL|field|MOCK_FRAME_EX_URI
specifier|static
specifier|final
name|String
name|MOCK_FRAME_EX_URI
init|=
literal|"mock://frameError-ex"
decl_stmt|;
annotation|@
name|Rule
DECL|field|mllpServer
specifier|public
name|MllpServerResource
name|mllpServer
init|=
operator|new
name|MllpServerResource
argument_list|(
literal|"0.0.0.0"
argument_list|,
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|MOCK_ACKNOWLEDGED_URI
argument_list|)
DECL|field|acknowledged
name|MockEndpoint
name|acknowledged
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|MOCK_TIMEOUT_URI
argument_list|)
DECL|field|timeout
name|MockEndpoint
name|timeout
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|MOCK_AE_EX_URI
argument_list|)
DECL|field|ae
name|MockEndpoint
name|ae
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|MOCK_AR_EX_URI
argument_list|)
DECL|field|ar
name|MockEndpoint
name|ar
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|MOCK_FRAME_EX_URI
argument_list|)
DECL|field|frame
name|MockEndpoint
name|frame
decl_stmt|;
annotation|@
name|Override
DECL|method|addServicesOnStartup (Map<String, KeyValueHolder<Object, Dictionary>> services)
specifier|protected
name|void
name|addServicesOnStartup
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|KeyValueHolder
argument_list|<
name|Object
argument_list|,
name|Dictionary
argument_list|>
argument_list|>
name|services
parameter_list|)
block|{
name|ComponentResolver
name|testResolver
init|=
operator|new
name|DefaultComponentResolver
argument_list|()
decl_stmt|;
name|services
operator|.
name|put
argument_list|(
name|ComponentResolver
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|asService
argument_list|(
name|testResolver
argument_list|,
literal|"component"
argument_list|,
literal|"mllp"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setConfigAdminInitialConfiguration (Properties props)
specifier|protected
name|String
name|setConfigAdminInitialConfiguration
parameter_list|(
name|Properties
name|props
parameter_list|)
block|{
name|props
operator|.
name|setProperty
argument_list|(
literal|"sourceUri"
argument_list|,
name|SOURCE_URI
argument_list|)
expr_stmt|;
name|props
operator|.
name|setProperty
argument_list|(
literal|"acknowledgedUri"
argument_list|,
name|MOCK_ACKNOWLEDGED_URI
argument_list|)
expr_stmt|;
name|props
operator|.
name|setProperty
argument_list|(
literal|"timeoutUri"
argument_list|,
name|MOCK_TIMEOUT_URI
argument_list|)
expr_stmt|;
name|props
operator|.
name|setProperty
argument_list|(
literal|"errorAcknowledgementUri"
argument_list|,
name|MOCK_AE_EX_URI
argument_list|)
expr_stmt|;
name|props
operator|.
name|setProperty
argument_list|(
literal|"rejectAcknowledgementUri"
argument_list|,
name|MOCK_AR_EX_URI
argument_list|)
expr_stmt|;
name|props
operator|.
name|setProperty
argument_list|(
literal|"mllp.port"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|mllpServer
operator|.
name|getListenPort
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|"MllpTcpClientProducer"
return|;
block|}
annotation|@
name|Override
DECL|method|getBlueprintDescriptor ()
specifier|protected
name|String
name|getBlueprintDescriptor
parameter_list|()
block|{
return|return
literal|"OSGI-INF/blueprint/mllp-tcp-client-producer-test.xml"
return|;
block|}
annotation|@
name|Test
argument_list|()
DECL|method|testSendMultipleMessages ()
specifier|public
name|void
name|testSendMultipleMessages
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|messageCount
init|=
literal|500
decl_stmt|;
name|acknowledged
operator|.
name|expectedMessageCount
argument_list|(
name|messageCount
argument_list|)
expr_stmt|;
name|timeout
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|frame
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|ae
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|ar
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|startCamelContext
argument_list|()
expr_stmt|;
comment|// Uncomment one of these lines to see the NACKs handled
comment|// mllpServer.setSendApplicationRejectAcknowledgementModulus(10);
comment|// mllpServer.setSendApplicationErrorAcknowledgementModulus(10);
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|messageCount
condition|;
operator|++
name|i
control|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Triggering message {}"
argument_list|,
name|i
argument_list|)
expr_stmt|;
comment|// Thread.sleep(5000);
name|Object
name|response
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
name|SOURCE_URI
argument_list|,
name|Hl7TestMessageGenerator
operator|.
name|generateMessage
argument_list|(
name|i
argument_list|)
argument_list|,
literal|"CamelMllpMessageControlId"
argument_list|,
name|String
operator|.
name|format
argument_list|(
literal|"%05d"
argument_list|,
name|i
argument_list|)
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"response {}\n{}"
argument_list|,
name|i
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|(
literal|15
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

