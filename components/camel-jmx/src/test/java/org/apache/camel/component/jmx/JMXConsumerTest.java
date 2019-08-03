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
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
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

begin_comment
comment|/**  * Tests that trigger notification events on our simple bean without  * requiring any special setup.  */
end_comment

begin_class
DECL|class|JMXConsumerTest
specifier|public
class|class
name|JMXConsumerTest
extends|extends
name|SimpleBeanFixture
block|{
DECL|field|simpleBean
name|ISimpleMXBean
name|simpleBean
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|simpleBean
operator|=
name|getSimpleMXBean
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|attributeChange ()
specifier|public
name|void
name|attributeChange
parameter_list|()
throws|throws
name|Exception
block|{
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
name|setStringValue
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|waitAndAssertMessageReceived
argument_list|(
literal|"src/test/resources/consumer-test/attributeChange-0.xml"
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
name|setStringValue
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|waitAndAssertMessageReceived
argument_list|(
literal|"src/test/resources/consumer-test/attributeChange-1.xml"
argument_list|)
expr_stmt|;
comment|// set the string to null
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
name|setStringValue
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|waitAndAssertMessageReceived
argument_list|(
literal|"src/test/resources/consumer-test/attributeChange-2.xml"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|notification ()
specifier|public
name|void
name|notification
parameter_list|()
throws|throws
name|Exception
block|{
name|simpleBean
operator|.
name|touch
argument_list|()
expr_stmt|;
name|waitAndAssertMessageReceived
argument_list|(
literal|"src/test/resources/consumer-test/touched.xml"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|userData ()
specifier|public
name|void
name|userData
parameter_list|()
throws|throws
name|Exception
block|{
name|simpleBean
operator|.
name|userData
argument_list|(
literal|"myUserData"
argument_list|)
expr_stmt|;
name|waitAndAssertMessageReceived
argument_list|(
literal|"src/test/resources/consumer-test/userdata.xml"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|jmxConnection ()
specifier|public
name|void
name|jmxConnection
parameter_list|()
throws|throws
name|Exception
block|{
name|simpleBean
operator|.
name|triggerConnectionNotification
argument_list|()
expr_stmt|;
name|waitAndAssertMessageReceived
argument_list|(
literal|"src/test/resources/consumer-test/jmxConnectionNotification.xml"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|mbeanServerNotification ()
specifier|public
name|void
name|mbeanServerNotification
parameter_list|()
throws|throws
name|Exception
block|{
name|simpleBean
operator|.
name|triggerMBeanServerNotification
argument_list|()
expr_stmt|;
name|waitAndAssertMessageReceived
argument_list|(
literal|"src/test/resources/consumer-test/mbeanServerNotification.xml"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
DECL|method|relationNotification ()
specifier|public
name|void
name|relationNotification
parameter_list|()
throws|throws
name|Exception
block|{
name|simpleBean
operator|.
name|triggerRelationNotification
argument_list|()
expr_stmt|;
name|waitAndAssertMessageReceived
argument_list|(
literal|"src/test/resources/consumer-test/relationNotification.xml"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|timerNotification ()
specifier|public
name|void
name|timerNotification
parameter_list|()
throws|throws
name|Exception
block|{
name|simpleBean
operator|.
name|triggerTimerNotification
argument_list|()
expr_stmt|;
name|waitAndAssertMessageReceived
argument_list|(
literal|"src/test/resources/consumer-test/timerNotification.xml"
argument_list|)
expr_stmt|;
block|}
DECL|method|waitAndAssertMessageReceived (String aExpectedFilePath)
specifier|private
name|void
name|waitAndAssertMessageReceived
parameter_list|(
name|String
name|aExpectedFilePath
parameter_list|)
throws|throws
name|InterruptedException
throws|,
name|Exception
block|{
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
name|aExpectedFilePath
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

