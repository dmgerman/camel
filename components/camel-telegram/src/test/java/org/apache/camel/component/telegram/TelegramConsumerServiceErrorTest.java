begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.telegram
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|telegram
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
name|EndpointInject
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
name|mock
operator|.
name|MockEndpoint
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
name|telegram
operator|.
name|model
operator|.
name|UpdateResult
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
name|telegram
operator|.
name|util
operator|.
name|TelegramTestSupport
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
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_comment
comment|/**  * Test the recovery after service unavailability.  */
end_comment

begin_class
DECL|class|TelegramConsumerServiceErrorTest
specifier|public
class|class
name|TelegramConsumerServiceErrorTest
extends|extends
name|TelegramTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:telegram"
argument_list|)
DECL|field|endpoint
specifier|private
name|MockEndpoint
name|endpoint
decl_stmt|;
annotation|@
name|Before
DECL|method|mockAPIs ()
specifier|public
name|void
name|mockAPIs
parameter_list|()
block|{
name|TelegramService
name|api
init|=
name|mockTelegramService
argument_list|()
decl_stmt|;
name|UpdateResult
name|res1
init|=
name|getJSONResource
argument_list|(
literal|"messages/updates-single.json"
argument_list|,
name|UpdateResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|res1
operator|.
name|getUpdates
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getMessage
argument_list|()
operator|.
name|setText
argument_list|(
literal|"message1"
argument_list|)
expr_stmt|;
name|UpdateResult
name|logicalErrorRes
init|=
name|getJSONResource
argument_list|(
literal|"messages/updates-empty.json"
argument_list|,
name|UpdateResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|logicalErrorRes
operator|.
name|setOk
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|UpdateResult
name|defaultRes
init|=
name|getJSONResource
argument_list|(
literal|"messages/updates-empty.json"
argument_list|,
name|UpdateResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|api
operator|.
name|getUpdates
argument_list|(
name|any
argument_list|()
argument_list|,
name|any
argument_list|()
argument_list|,
name|any
argument_list|()
argument_list|,
name|any
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenThrow
argument_list|(
operator|new
name|RuntimeException
argument_list|(
literal|"Service exception"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|logicalErrorRes
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|res1
argument_list|)
operator|.
name|thenAnswer
argument_list|(
parameter_list|(
name|i
parameter_list|)
lambda|->
name|defaultRes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumerRecovery ()
specifier|public
name|void
name|testConsumerRecovery
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"message1"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"telegram:bots?authorizationToken=mock-token"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:telegram"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

