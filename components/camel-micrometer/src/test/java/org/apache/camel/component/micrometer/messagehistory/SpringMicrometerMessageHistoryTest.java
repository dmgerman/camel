begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.micrometer.messagehistory
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|micrometer
operator|.
name|messagehistory
package|;
end_package

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|MeterRegistry
import|;
end_import

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|Timer
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
name|spring
operator|.
name|CamelSpringTestSupport
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
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractApplicationContext
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
name|support
operator|.
name|ClassPathXmlApplicationContext
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
name|micrometer
operator|.
name|MicrometerConstants
operator|.
name|DEFAULT_CAMEL_MESSAGE_HISTORY_METER_NAME
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
name|micrometer
operator|.
name|MicrometerConstants
operator|.
name|NODE_ID_TAG
import|;
end_import

begin_class
DECL|class|SpringMicrometerMessageHistoryTest
specifier|public
class|class
name|SpringMicrometerMessageHistoryTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/micrometer/messagehistory/SpringMetricsMessageHistoryTest.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testMetricsHistory ()
specifier|public
name|void
name|testMetricsHistory
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|count
init|=
literal|10
decl_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
name|count
operator|/
literal|2
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
name|count
operator|/
literal|2
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:baz"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
name|count
operator|/
literal|2
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|count
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|%
literal|2
operator|==
literal|0
condition|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"Hello "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:bar"
argument_list|,
literal|"Hello "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// there should be 3 names
name|MeterRegistry
name|registry
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByType
argument_list|(
name|MeterRegistry
operator|.
name|class
argument_list|)
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|registry
operator|.
name|getMeters
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Timer
name|fooTimer
init|=
name|registry
operator|.
name|find
argument_list|(
name|DEFAULT_CAMEL_MESSAGE_HISTORY_METER_NAME
argument_list|)
operator|.
name|tag
argument_list|(
name|NODE_ID_TAG
argument_list|,
literal|"foo"
argument_list|)
operator|.
name|timer
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|count
operator|/
literal|2
argument_list|,
name|fooTimer
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
name|Timer
name|barTimer
init|=
name|registry
operator|.
name|find
argument_list|(
name|DEFAULT_CAMEL_MESSAGE_HISTORY_METER_NAME
argument_list|)
operator|.
name|tag
argument_list|(
name|NODE_ID_TAG
argument_list|,
literal|"bar"
argument_list|)
operator|.
name|timer
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|count
operator|/
literal|2
argument_list|,
name|barTimer
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
name|Timer
name|bazTimer
init|=
name|registry
operator|.
name|find
argument_list|(
name|DEFAULT_CAMEL_MESSAGE_HISTORY_METER_NAME
argument_list|)
operator|.
name|tag
argument_list|(
name|NODE_ID_TAG
argument_list|,
literal|"baz"
argument_list|)
operator|.
name|timer
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|count
operator|/
literal|2
argument_list|,
name|bazTimer
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
comment|// get the message history service
name|MicrometerMessageHistoryService
name|service
init|=
name|context
operator|.
name|hasService
argument_list|(
name|MicrometerMessageHistoryService
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|String
name|json
init|=
name|service
operator|.
name|dumpStatisticsAsJson
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|json
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|json
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"nodeId\" : \"foo\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"nodeId\" : \"bar\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"nodeId\" : \"baz\""
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

