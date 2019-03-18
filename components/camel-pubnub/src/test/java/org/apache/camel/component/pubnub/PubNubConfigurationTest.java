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
name|Test
import|;
end_import

begin_class
DECL|class|PubNubConfigurationTest
specifier|public
class|class
name|PubNubConfigurationTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|createEndpointWithIllegalArguments ()
specifier|public
name|void
name|createEndpointWithIllegalArguments
parameter_list|()
throws|throws
name|Exception
block|{
name|PubNubComponent
name|component
init|=
operator|new
name|PubNubComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"pubnub"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEndpointWithMinimalConfiguration ()
specifier|public
name|void
name|createEndpointWithMinimalConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|PubNubComponent
name|component
init|=
operator|new
name|PubNubComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|PubNubEndpoint
name|endpoint
init|=
operator|(
name|PubNubEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"pubnub:xxx?subscribeKey=mysubkey"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"xxx"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mysubkey"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSubscribeKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isSecure
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEndpointWithMaximalConfiguration ()
specifier|public
name|void
name|createEndpointWithMaximalConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|PubNubComponent
name|component
init|=
operator|new
name|PubNubComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|PubNubEndpoint
name|endpoint
init|=
operator|(
name|PubNubEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"pubnub:xxx?subscribeKey=mysubkey&publishKey=mypubkey&secretKey=secrets&uuid=myuuid&operation=PUBLISH&secure=false&authKey=authKey"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"xxx"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mysubkey"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSubscribeKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mypubkey"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPublishKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"secrets"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSecretKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"myuuid"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getUuid
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"PUBLISH"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getOperation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"authKey"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAuthKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isSecure
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

