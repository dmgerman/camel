begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.paho
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|paho
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|broker
operator|.
name|BrokerService
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
name|junit4
operator|.
name|CamelTestSupport
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

begin_class
DECL|class|PahoOverrideTopicTest
specifier|public
class|class
name|PahoOverrideTopicTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|broker
name|BrokerService
name|broker
decl_stmt|;
DECL|field|mqttPort
name|int
name|mqttPort
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|useJmx ()
specifier|protected
name|boolean
name|useJmx
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|doPreSetup ()
specifier|public
name|void
name|doPreSetup
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doPreSetup
argument_list|()
expr_stmt|;
name|broker
operator|=
operator|new
name|BrokerService
argument_list|()
expr_stmt|;
name|broker
operator|.
name|setPersistent
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|broker
operator|.
name|addConnector
argument_list|(
literal|"mqtt://localhost:"
operator|+
name|mqttPort
argument_list|)
expr_stmt|;
name|broker
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|broker
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
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
name|PahoComponent
name|paho
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"paho"
argument_list|,
name|PahoComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|paho
operator|.
name|setBrokerUrl
argument_list|(
literal|"tcp://localhost:"
operator|+
name|mqttPort
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"paho:queue"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Message sent"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"paho:myoverride"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Message received"
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
comment|// Tests
annotation|@
name|Test
DECL|method|shouldOverride ()
specifier|public
name|void
name|shouldOverride
parameter_list|()
throws|throws
name|InterruptedException
block|{
comment|// Given
name|getMockEndpoint
argument_list|(
literal|"mock:test"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// When
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:test"
argument_list|,
literal|"Hello World"
argument_list|,
name|PahoConstants
operator|.
name|CAMEL_PAHO_OVERRIDE_TOPIC
argument_list|,
literal|"myoverride"
argument_list|)
expr_stmt|;
comment|// Then
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

