begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jmx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jmx
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|jmx
operator|.
name|beans
operator|.
name|ISimpleMXBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|JMXMonitorTypeGaugeTest
specifier|public
class|class
name|JMXMonitorTypeGaugeTest
extends|extends
name|SimpleBeanFixture
block|{
annotation|@
name|Test
DECL|method|gauge ()
specifier|public
name|void
name|gauge
parameter_list|()
throws|throws
name|Exception
block|{
name|ISimpleMXBean
name|simpleBean
init|=
name|getSimpleMXBean
argument_list|()
decl_stmt|;
comment|// we should get an event after the number exceeds 100 or drops below 50
name|simpleBean
operator|.
name|setMonitorNumber
argument_list|(
literal|75
argument_list|)
expr_stmt|;
name|simpleBean
operator|.
name|setMonitorNumber
argument_list|(
literal|110
argument_list|)
expr_stmt|;
name|getMockFixture
argument_list|()
operator|.
name|waitForMessages
argument_list|()
expr_stmt|;
name|getMockFixture
argument_list|()
operator|.
name|assertMessageReceived
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/monitor-consumer/gaugeNotification-high.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|getMockFixture
argument_list|()
operator|.
name|getMockEndpoint
argument_list|()
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|simpleBean
operator|.
name|setMonitorNumber
argument_list|(
literal|90
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|600
argument_list|)
expr_stmt|;
name|simpleBean
operator|.
name|setMonitorNumber
argument_list|(
literal|60
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|600
argument_list|)
expr_stmt|;
name|simpleBean
operator|.
name|setMonitorNumber
argument_list|(
literal|40
argument_list|)
expr_stmt|;
name|getMockFixture
argument_list|()
operator|.
name|waitForMessages
argument_list|()
expr_stmt|;
name|getMockFixture
argument_list|()
operator|.
name|assertMessageReceived
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/monitor-consumer/gaugeNotification-low.xml"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|buildFromURI ()
specifier|protected
name|JMXUriBuilder
name|buildFromURI
parameter_list|()
block|{
return|return
name|super
operator|.
name|buildFromURI
argument_list|()
operator|.
name|withMonitorType
argument_list|(
literal|"gauge"
argument_list|)
operator|.
name|withGranularityPeriod
argument_list|(
literal|500
argument_list|)
operator|.
name|withObservedAttribute
argument_list|(
literal|"MonitorNumber"
argument_list|)
operator|.
name|withNotifyHigh
argument_list|(
literal|true
argument_list|)
operator|.
name|withNotifyLow
argument_list|(
literal|true
argument_list|)
operator|.
name|withDifferenceMode
argument_list|(
literal|false
argument_list|)
operator|.
name|withThresholdHigh
argument_list|(
literal|100
argument_list|)
operator|.
name|withThresholdLow
argument_list|(
literal|50
argument_list|)
return|;
block|}
block|}
end_class

end_unit

