begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|main
operator|.
name|Main
import|;
end_import

begin_import
import|import
name|org
operator|.
name|json
operator|.
name|JSONObject
import|;
end_import

begin_comment
comment|//@formatter:off
end_comment

begin_comment
comment|/**  * Just a small http server hack to try out pubnub method calls.   * HERE_NOW, WHERE_NOW, GET_STATE, SET_STATE, GET_HISTORY, PUBLISH;   * usage :   * do a GET with http param CamelPubNubOperation=METHOD_TO_ACTIVATE eg. HERE_NOW   *   * SET_STATE requires a http param 'body' with some json that is used as pubnub state object.   * Can be any valid json string.  *  */
end_comment

begin_comment
comment|//@formatter:on
end_comment

begin_class
DECL|class|PubNubOperationsExample
specifier|public
specifier|final
class|class
name|PubNubOperationsExample
block|{
DECL|method|PubNubOperationsExample ()
specifier|private
name|PubNubOperationsExample
parameter_list|()
block|{     }
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
name|main
operator|.
name|addRouteBuilder
argument_list|(
operator|new
name|RestRoute
argument_list|()
argument_list|)
expr_stmt|;
name|main
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
DECL|class|RestRoute
specifier|static
class|class
name|RestRoute
extends|extends
name|RouteBuilder
block|{
DECL|field|pubnub
specifier|private
name|String
name|pubnub
init|=
literal|"pubnub://pubsub:iot?publisherKey="
operator|+
name|PubNubExampleConstants
operator|.
name|PUBNUB_PUBLISHER_KEY
operator|+
literal|"&subscriberKey="
operator|+
name|PubNubExampleConstants
operator|.
name|PUBNUB_SUBSCRIBER_KEY
decl_stmt|;
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|//@formatter:off
name|from
argument_list|(
literal|"netty-http:http://0.0.0.0:8080?urlDecodeHeaders=true"
argument_list|)
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
literal|"${header.body}"
argument_list|)
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|JSONObject
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|pubnub
argument_list|)
expr_stmt|;
comment|//@formatter:on
block|}
block|}
block|}
end_class

end_unit

