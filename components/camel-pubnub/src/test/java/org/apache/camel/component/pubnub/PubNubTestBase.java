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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|junit
operator|.
name|WireMockRule
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
name|PNConfiguration
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
name|PubNub
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
name|enums
operator|.
name|PNLogVerbosity
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
name|BindToRegistry
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
name|Before
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
import|import static
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|core
operator|.
name|WireMockConfiguration
operator|.
name|options
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|pubnub
operator|.
name|api
operator|.
name|enums
operator|.
name|PNHeartbeatNotificationOptions
operator|.
name|NONE
import|;
end_import

begin_class
DECL|class|PubNubTestBase
specifier|public
class|class
name|PubNubTestBase
extends|extends
name|CamelTestSupport
block|{
DECL|field|port
specifier|private
specifier|final
name|int
name|port
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"pubnub"
argument_list|)
DECL|field|pubnub
specifier|private
name|PubNub
name|pubnub
init|=
name|createPubNubInstance
argument_list|()
decl_stmt|;
annotation|@
name|Rule
DECL|field|wireMockRule
specifier|public
name|WireMockRule
name|wireMockRule
init|=
operator|new
name|WireMockRule
argument_list|(
name|options
argument_list|()
operator|.
name|port
argument_list|(
name|port
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|Before
DECL|method|beforeEach ()
specifier|public
name|void
name|beforeEach
parameter_list|()
throws|throws
name|IOException
block|{
name|wireMockRule
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|afterEach ()
specifier|public
name|void
name|afterEach
parameter_list|()
block|{
name|pubnub
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
DECL|method|getPubnub ()
specifier|protected
name|PubNub
name|getPubnub
parameter_list|()
block|{
return|return
name|pubnub
return|;
block|}
DECL|method|createPubNubInstance ()
specifier|private
name|PubNub
name|createPubNubInstance
parameter_list|()
block|{
name|PNConfiguration
name|pnConfiguration
init|=
operator|new
name|PNConfiguration
argument_list|()
decl_stmt|;
name|pnConfiguration
operator|.
name|setOrigin
argument_list|(
literal|"localhost"
operator|+
literal|":"
operator|+
name|port
argument_list|)
expr_stmt|;
name|pnConfiguration
operator|.
name|setSecure
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|pnConfiguration
operator|.
name|setSubscribeKey
argument_list|(
literal|"mySubscribeKey"
argument_list|)
expr_stmt|;
name|pnConfiguration
operator|.
name|setPublishKey
argument_list|(
literal|"myPublishKey"
argument_list|)
expr_stmt|;
name|pnConfiguration
operator|.
name|setUuid
argument_list|(
literal|"myUUID"
argument_list|)
expr_stmt|;
name|pnConfiguration
operator|.
name|setLogVerbosity
argument_list|(
name|PNLogVerbosity
operator|.
name|NONE
argument_list|)
expr_stmt|;
name|pnConfiguration
operator|.
name|setHeartbeatNotificationOptions
argument_list|(
name|NONE
argument_list|)
expr_stmt|;
class|class
name|MockedTimePubNub
extends|extends
name|PubNub
block|{
name|MockedTimePubNub
parameter_list|(
name|PNConfiguration
name|initialConfig
parameter_list|)
block|{
name|super
argument_list|(
name|initialConfig
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getTimestamp
parameter_list|()
block|{
return|return
literal|1337
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
literal|"suchJava"
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getInstanceId
parameter_list|()
block|{
return|return
literal|"PubNubInstanceId"
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getRequestId
parameter_list|()
block|{
return|return
literal|"PubNubRequestId"
return|;
block|}
block|}
return|return
operator|new
name|MockedTimePubNub
argument_list|(
name|pnConfiguration
argument_list|)
return|;
block|}
block|}
end_class

end_unit

