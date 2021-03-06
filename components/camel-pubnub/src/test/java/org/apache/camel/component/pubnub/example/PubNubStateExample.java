begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pubnub.example
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
operator|.
name|example
package|;
end_package

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
name|RoutesBuilder
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
name|component
operator|.
name|pubnub
operator|.
name|PubNubConstants
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
name|pubnub
operator|.
name|example
operator|.
name|PubNubExampleConstants
operator|.
name|PUBNUB_PUBLISH_KEY
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
name|pubnub
operator|.
name|example
operator|.
name|PubNubExampleConstants
operator|.
name|PUBNUB_SUBSCRIBE_KEY
import|;
end_import

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"Integration test that requires a pub/sub key to run"
argument_list|)
DECL|class|PubNubStateExample
specifier|public
class|class
name|PubNubStateExample
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testStateChange ()
specifier|public
name|void
name|testStateChange
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"state"
argument_list|,
literal|"online"
argument_list|)
expr_stmt|;
name|myState
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"preben"
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
literal|"preben"
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
literal|"name"
argument_list|)
operator|.
name|getAsString
argument_list|()
argument_list|)
expr_stmt|;
name|resetMocks
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|headers
operator|.
name|clear
argument_list|()
expr_stmt|;
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
name|getStateResult
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:publish"
argument_list|,
literal|null
argument_list|,
name|PubNubConstants
operator|.
name|OPERATION
argument_list|,
literal|"GETSTATE"
argument_list|,
name|PNGetStateResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"preben"
argument_list|,
name|getStateResult
operator|.
name|getStateByUUID
argument_list|()
operator|.
name|get
argument_list|(
literal|"iot"
argument_list|)
operator|.
name|getAsJsonObject
argument_list|()
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
operator|.
name|getAsString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
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
literal|"pubnub:iot?uuid=myuuid&publishKey="
operator|+
name|PUBNUB_PUBLISH_KEY
operator|+
literal|"&subscribeKey="
operator|+
name|PUBNUB_SUBSCRIBE_KEY
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

