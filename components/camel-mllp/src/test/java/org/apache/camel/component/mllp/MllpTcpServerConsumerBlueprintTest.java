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
name|MllpClientResource
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
DECL|class|MllpTcpServerConsumerBlueprintTest
specifier|public
class|class
name|MllpTcpServerConsumerBlueprintTest
extends|extends
name|CamelBlueprintTestSupport
block|{
DECL|field|RECEIVED_URI
specifier|static
specifier|final
name|String
name|RECEIVED_URI
init|=
literal|"mock://received"
decl_stmt|;
DECL|field|MLLP_HOST
specifier|static
specifier|final
name|String
name|MLLP_HOST
init|=
literal|"localhost"
decl_stmt|;
annotation|@
name|Rule
DECL|field|mllpClient
specifier|public
name|MllpClientResource
name|mllpClient
init|=
operator|new
name|MllpClientResource
argument_list|()
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|RECEIVED_URI
argument_list|)
DECL|field|received
name|MockEndpoint
name|received
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
DECL|method|getBlueprintDescriptor ()
specifier|protected
name|String
name|getBlueprintDescriptor
parameter_list|()
block|{
return|return
literal|"OSGI-INF/blueprint/mllp-tcp-server-consumer-test.xml"
return|;
block|}
annotation|@
name|Override
DECL|method|useOverridePropertiesWithPropertiesComponent ()
specifier|protected
name|Properties
name|useOverridePropertiesWithPropertiesComponent
parameter_list|()
block|{
name|mllpClient
operator|.
name|setMllpHost
argument_list|(
name|MLLP_HOST
argument_list|)
expr_stmt|;
name|mllpClient
operator|.
name|setMllpPort
argument_list|(
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
argument_list|)
expr_stmt|;
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|setProperty
argument_list|(
literal|"RECEIVED_URI"
argument_list|,
name|RECEIVED_URI
argument_list|)
expr_stmt|;
name|props
operator|.
name|setProperty
argument_list|(
literal|"mllp.host"
argument_list|,
name|mllpClient
operator|.
name|getMllpHost
argument_list|()
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
name|mllpClient
operator|.
name|getMllpPort
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|props
return|;
block|}
comment|/*         This doesn't seem to work     @Override     protected String useOverridePropertiesWithConfigAdmin(Dictionary props) throws Exception {         mllpClient.setMllpPort(AvailablePortFinder.getNextAvailable());          props.put("mllp.port", mllpClient.getMllpPort() );          return "MllpTcpServerConsumerBlueprintTest";     }     */
annotation|@
name|Test
DECL|method|testReceiveMultipleMessages ()
specifier|public
name|void
name|testReceiveMultipleMessages
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|sendMessageCount
init|=
literal|5
decl_stmt|;
name|received
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|mllpClient
operator|.
name|connect
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|sendMessageCount
condition|;
operator|++
name|i
control|)
block|{
name|mllpClient
operator|.
name|sendMessageAndWaitForAcknowledgement
argument_list|(
name|Hl7TestMessageGenerator
operator|.
name|generateMessage
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|mllpClient
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|(
literal|10
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

