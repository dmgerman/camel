begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pubnub
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pubnub
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
name|HashMap
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
name|Map
import|;
end_import

begin_import
import|import
name|com
operator|.
name|pubnub
operator|.
name|api
operator|.
name|models
operator|.
name|consumer
operator|.
name|history
operator|.
name|PNHistoryItemResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|pubnub
operator|.
name|api
operator|.
name|models
operator|.
name|consumer
operator|.
name|presence
operator|.
name|PNGetStateResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|pubnub
operator|.
name|api
operator|.
name|models
operator|.
name|consumer
operator|.
name|presence
operator|.
name|PNHereNowResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|pubnub
operator|.
name|api
operator|.
name|models
operator|.
name|consumer
operator|.
name|presence
operator|.
name|PNSetStateResult
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
name|Test
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|client
operator|.
name|WireMock
operator|.
name|aResponse
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|client
operator|.
name|WireMock
operator|.
name|get
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|client
operator|.
name|WireMock
operator|.
name|stubFor
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|client
operator|.
name|WireMock
operator|.
name|urlPathEqualTo
import|;
end_import

begin_class
DECL|class|PubNubOperationsTest
specifier|public
class|class
name|PubNubOperationsTest
extends|extends
name|PubNubTestBase
block|{
annotation|@
name|Test
DECL|method|testWhereNow ()
specifier|public
name|void
name|testWhereNow
parameter_list|()
throws|throws
name|Exception
block|{
name|stubFor
argument_list|(
name|get
argument_list|(
name|urlPathEqualTo
argument_list|(
literal|"/v2/presence/sub-key/mySubscribeKey/uuid/myUUID"
argument_list|)
argument_list|)
operator|.
name|willReturn
argument_list|(
name|aResponse
argument_list|()
operator|.
name|withBody
argument_list|(
literal|"{\"status\": 200, \"message\": \"OK\", \"payload\": {\"channels\": [\"channel-a\",\"channel-b\"]}, \"service\": \"Presence\"}"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|PubNubConstants
operator|.
name|OPERATION
argument_list|,
literal|"WHERENOW"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|PubNubConstants
operator|.
name|UUID
argument_list|,
literal|"myUUID"
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|response
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:publish"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertListSize
argument_list|(
name|response
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"channel-a"
argument_list|,
name|response
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHereNow ()
specifier|public
name|void
name|testHereNow
parameter_list|()
throws|throws
name|Exception
block|{
name|stubFor
argument_list|(
name|get
argument_list|(
name|urlPathEqualTo
argument_list|(
literal|"/v2/presence/sub_key/mySubscribeKey/channel/myChannel"
argument_list|)
argument_list|)
operator|.
name|willReturn
argument_list|(
name|aResponse
argument_list|()
operator|.
name|withBody
argument_list|(
literal|"{\"status\" : 200, \"message\" : \"OK\", \"service\" : \"Presence\", \"uuids\" : [{\"uuid\" : \"myUUID0\"}, {\"state\" : {\"abcd\" : {\"age\" : 15}}, "
operator|+
literal|"\"uuid\" : \"myUUID1\"}, {\"uuid\" : \"b9eb408c-bcec-4d34-b4c4-fabec057ad0d\"}, {\"state\" : {\"abcd\" : {\"age\" : 15}}, \"uuid\" : \"myUUID2\"},"
operator|+
literal|" {\"state\" : {\"abcd\" : {\"age\" : 24}}, \"uuid\" : \"myUUID9\"}], \"occupancy\" : 5}"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|PubNubConstants
operator|.
name|OPERATION
argument_list|,
literal|"HERENOW"
argument_list|)
expr_stmt|;
name|PNHereNowResult
name|response
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:publish"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|,
name|PNHereNowResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|response
operator|.
name|getTotalOccupancy
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetHistory ()
specifier|public
name|void
name|testGetHistory
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|testArray
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|historyItems
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|historyEnvelope1
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|historyItem1
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|historyItem1
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|11
argument_list|)
expr_stmt|;
name|historyItem1
operator|.
name|put
argument_list|(
literal|"b"
argument_list|,
literal|22
argument_list|)
expr_stmt|;
name|historyEnvelope1
operator|.
name|put
argument_list|(
literal|"timetoken"
argument_list|,
literal|1111
argument_list|)
expr_stmt|;
name|historyEnvelope1
operator|.
name|put
argument_list|(
literal|"message"
argument_list|,
name|historyItem1
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|historyEnvelope2
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|historyItem2
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|historyItem2
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|33
argument_list|)
expr_stmt|;
name|historyItem2
operator|.
name|put
argument_list|(
literal|"b"
argument_list|,
literal|44
argument_list|)
expr_stmt|;
name|historyEnvelope2
operator|.
name|put
argument_list|(
literal|"timetoken"
argument_list|,
literal|2222
argument_list|)
expr_stmt|;
name|historyEnvelope2
operator|.
name|put
argument_list|(
literal|"message"
argument_list|,
name|historyItem2
argument_list|)
expr_stmt|;
name|historyItems
operator|.
name|add
argument_list|(
name|historyEnvelope1
argument_list|)
expr_stmt|;
name|historyItems
operator|.
name|add
argument_list|(
name|historyEnvelope2
argument_list|)
expr_stmt|;
name|testArray
operator|.
name|add
argument_list|(
name|historyItems
argument_list|)
expr_stmt|;
name|testArray
operator|.
name|add
argument_list|(
literal|1234
argument_list|)
expr_stmt|;
name|testArray
operator|.
name|add
argument_list|(
literal|4321
argument_list|)
expr_stmt|;
name|stubFor
argument_list|(
name|get
argument_list|(
name|urlPathEqualTo
argument_list|(
literal|"/v2/history/sub-key/mySubscribeKey/channel/myChannel"
argument_list|)
argument_list|)
operator|.
name|willReturn
argument_list|(
name|aResponse
argument_list|()
operator|.
name|withBody
argument_list|(
name|getPubnub
argument_list|()
operator|.
name|getMapper
argument_list|()
operator|.
name|toJson
argument_list|(
name|testArray
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|PubNubConstants
operator|.
name|OPERATION
argument_list|,
literal|"GETHISTORY"
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|PNHistoryItemResult
argument_list|>
name|response
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:publish"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertListSize
argument_list|(
name|response
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetState ()
specifier|public
name|void
name|testGetState
parameter_list|()
throws|throws
name|Exception
block|{
name|stubFor
argument_list|(
name|get
argument_list|(
name|urlPathEqualTo
argument_list|(
literal|"/v2/presence/sub-key/mySubscribeKey/channel/myChannel/uuid/myuuid"
argument_list|)
argument_list|)
operator|.
name|willReturn
argument_list|(
name|aResponse
argument_list|()
operator|.
name|withBody
argument_list|(
literal|"{ \"status\": 200, \"message\": \"OK\", \"payload\": "
operator|+
literal|"{ \"myChannel\": { \"age\" : 20, \"status\" : \"online\"}, \"ch2\": { \"age\": 100, \"status\": \"offline\" } }, \"service\": \"Presence\"}"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|PubNubConstants
operator|.
name|OPERATION
argument_list|,
literal|"GETSTATE"
argument_list|)
expr_stmt|;
name|PNGetStateResult
name|response
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:publish"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|,
name|PNGetStateResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|response
operator|.
name|getStateByUUID
argument_list|()
operator|.
name|get
argument_list|(
literal|"myChannel"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSetState ()
specifier|public
name|void
name|testSetState
parameter_list|()
throws|throws
name|Exception
block|{
name|stubFor
argument_list|(
name|get
argument_list|(
name|urlPathEqualTo
argument_list|(
literal|"/v2/presence/sub-key/mySubscribeKey/channel/myChannel/uuid/myuuid/data"
argument_list|)
argument_list|)
operator|.
name|willReturn
argument_list|(
name|aResponse
argument_list|()
operator|.
name|withBody
argument_list|(
literal|"{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"age\" : 20, \"status\" : \"online\" }, \"service\": \"Presence\"}"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|myState
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|myState
operator|.
name|put
argument_list|(
literal|"age"
argument_list|,
literal|20
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|PubNubConstants
operator|.
name|OPERATION
argument_list|,
literal|"SETSTATE"
argument_list|)
expr_stmt|;
name|PNSetStateResult
name|response
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:publish"
argument_list|,
name|myState
argument_list|,
name|headers
argument_list|,
name|PNSetStateResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|response
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|response
operator|.
name|getState
argument_list|()
operator|.
name|getAsJsonObject
argument_list|()
operator|.
name|get
argument_list|(
literal|"age"
argument_list|)
operator|.
name|getAsInt
argument_list|()
argument_list|)
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:publish"
argument_list|)
operator|.
name|to
argument_list|(
literal|"pubnub://myChannel?uuid=myuuid&pubnub=#pubnub"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

