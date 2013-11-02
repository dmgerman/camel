begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.splunk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|splunk
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|splunk
operator|.
name|event
operator|.
name|SplunkEvent
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

begin_class
DECL|class|SplunkEventTest
specifier|public
class|class
name|SplunkEventTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testEventDataWithQuotedValues ()
specifier|public
name|void
name|testEventDataWithQuotedValues
parameter_list|()
block|{
name|Date
name|now
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|SplunkEvent
name|event
init|=
operator|new
name|SplunkEvent
argument_list|(
literal|"testevent"
argument_list|,
literal|"123"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|event
operator|.
name|addPair
argument_list|(
literal|"key1"
argument_list|,
literal|"value1"
argument_list|)
expr_stmt|;
name|event
operator|.
name|addPair
argument_list|(
literal|"key2"
argument_list|,
literal|"value2 with whitespace"
argument_list|)
expr_stmt|;
name|event
operator|.
name|addPair
argument_list|(
name|SplunkEvent
operator|.
name|COMMON_DVC_TIME
argument_list|,
name|now
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Values should be quoted"
argument_list|,
literal|"name=\"testevent\" event_id=\"123\" key1=\"value1\" key2=\"value2 with whitespace\" dvc_time=\""
operator|+
name|now
operator|.
name|toString
argument_list|()
operator|+
literal|"\"\n"
argument_list|,
name|event
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|event
operator|.
name|getEventData
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|event
operator|.
name|getEventData
argument_list|()
operator|.
name|get
argument_list|(
literal|"key2"
argument_list|)
operator|.
name|equals
argument_list|(
literal|"value2 with whitespace"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEventDataFromMap ()
specifier|public
name|void
name|testEventDataFromMap
parameter_list|()
block|{
name|String
name|rawString
init|=
literal|"2013-10-26    15:16:38:011+0200 name=\"twitter-message\" from_user=\"MyNameIsZack_98\" in_reply_to=\"null\" start_time=\"Sat Oct 26 15:16:21 CEST 2013\" "
operator|+
literal|"event_id=\"394090123278974976\" text=\"RT @RGIII: Just something about music that it can vibe with your soul\" retweet_count=\"1393\""
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|eventData
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|eventData
operator|.
name|put
argument_list|(
literal|"_subsecond"
argument_list|,
literal|".011"
argument_list|)
expr_stmt|;
name|eventData
operator|.
name|put
argument_list|(
literal|"_raw"
argument_list|,
name|rawString
argument_list|)
expr_stmt|;
name|SplunkEvent
name|splunkEvent
init|=
operator|new
name|SplunkEvent
argument_list|(
name|eventData
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|splunkEvent
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"_subsecond=\".011\" _raw=\""
operator|+
name|rawString
operator|+
literal|"\"\n"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

