begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Test
import|;
end_import

begin_class
DECL|class|JMXMonitorTypeLongCounterTest
specifier|public
class|class
name|JMXMonitorTypeLongCounterTest
extends|extends
name|SimpleBeanFixture
block|{
annotation|@
name|Test
DECL|method|counter ()
specifier|public
name|void
name|counter
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
comment|// we should get an event after the monitor number reaches 3
name|simpleBean
operator|.
name|setLongNumber
argument_list|(
literal|1L
argument_list|)
expr_stmt|;
comment|// this should trigger a notification
name|simpleBean
operator|.
name|setLongNumber
argument_list|(
literal|3L
argument_list|)
expr_stmt|;
comment|// we should get 1 change from the counter bean
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
literal|"src/test/resources/monitor-consumer/monitorNotificationLong.xml"
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
literal|"counter"
argument_list|)
operator|.
name|withGranularityPeriod
argument_list|(
literal|500
argument_list|)
operator|.
name|withObservedAttribute
argument_list|(
literal|"LongNumber"
argument_list|)
operator|.
name|withInitThreshold
argument_list|(
literal|2
argument_list|)
operator|.
name|withOffset
argument_list|(
literal|2
argument_list|)
operator|.
name|withModulus
argument_list|(
literal|100
argument_list|)
operator|.
name|withDifferenceMode
argument_list|(
literal|false
argument_list|)
return|;
block|}
block|}
end_class

end_unit

