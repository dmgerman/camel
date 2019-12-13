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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|ServiceStatus
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
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|awaitility
operator|.
name|Awaitility
operator|.
name|waitAtMost
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
name|anyString
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
name|eq
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
name|never
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
name|verify
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
comment|/**  * Tests a producer that sends media information.  */
end_comment

begin_class
DECL|class|TelegramWebhookRegistrationTest
specifier|public
class|class
name|TelegramWebhookRegistrationTest
extends|extends
name|TelegramTestSupport
block|{
annotation|@
name|Test
DECL|method|testAutomaticRegistration ()
specifier|public
name|void
name|testAutomaticRegistration
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"webhook:telegram:bots?authorizationToken=mock-token"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:endpoint"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
argument_list|()
operator|.
name|start
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|this
operator|.
name|currentMockService
argument_list|()
argument_list|)
operator|.
name|setWebhook
argument_list|(
name|eq
argument_list|(
literal|"mock-token"
argument_list|)
argument_list|,
name|anyString
argument_list|()
argument_list|)
expr_stmt|;
name|context
argument_list|()
operator|.
name|stop
argument_list|()
expr_stmt|;
name|waitAtMost
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|until
argument_list|(
parameter_list|()
lambda|->
name|context
argument_list|()
operator|.
name|getStatus
argument_list|()
operator|==
name|ServiceStatus
operator|.
name|Stopped
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|this
operator|.
name|currentMockService
argument_list|()
argument_list|)
operator|.
name|removeWebhook
argument_list|(
literal|"mock-token"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoRegistration ()
specifier|public
name|void
name|testNoRegistration
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"webhook:telegram:bots?authorizationToken=mock-token&webhookAutoRegister=false"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:endpoint"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
argument_list|()
operator|.
name|start
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|this
operator|.
name|currentMockService
argument_list|()
argument_list|,
name|never
argument_list|()
argument_list|)
operator|.
name|setWebhook
argument_list|(
name|eq
argument_list|(
literal|"mock-token"
argument_list|)
argument_list|,
name|anyString
argument_list|()
argument_list|)
expr_stmt|;
name|context
argument_list|()
operator|.
name|stop
argument_list|()
expr_stmt|;
name|waitAtMost
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|until
argument_list|(
parameter_list|()
lambda|->
name|context
argument_list|()
operator|.
name|getStatus
argument_list|()
operator|==
name|ServiceStatus
operator|.
name|Stopped
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|this
operator|.
name|currentMockService
argument_list|()
argument_list|,
name|never
argument_list|()
argument_list|)
operator|.
name|removeWebhook
argument_list|(
literal|"mock-token"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doPreSetup ()
specifier|protected
name|void
name|doPreSetup
parameter_list|()
throws|throws
name|Exception
block|{
name|TelegramService
name|api
init|=
name|mockTelegramService
argument_list|()
decl_stmt|;
name|when
argument_list|(
name|api
operator|.
name|setWebhook
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|api
operator|.
name|removeWebhook
argument_list|(
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

