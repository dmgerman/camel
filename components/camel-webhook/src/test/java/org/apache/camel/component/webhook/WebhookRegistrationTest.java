begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.webhook
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|webhook
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|webhook
operator|.
name|support
operator|.
name|TestComponent
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
name|spi
operator|.
name|Registry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|awaitility
operator|.
name|Awaitility
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

begin_class
DECL|class|WebhookRegistrationTest
specifier|public
class|class
name|WebhookRegistrationTest
extends|extends
name|WebhookTestBase
block|{
DECL|field|registered
specifier|private
name|AtomicInteger
name|registered
decl_stmt|;
DECL|field|unregistered
specifier|private
name|AtomicInteger
name|unregistered
decl_stmt|;
annotation|@
name|Before
DECL|method|initialize ()
specifier|public
name|void
name|initialize
parameter_list|()
block|{
name|this
operator|.
name|registered
operator|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|this
operator|.
name|unregistered
operator|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testContext ()
specifier|public
name|void
name|testContext
parameter_list|()
throws|throws
name|Exception
block|{
name|context
argument_list|()
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
name|restConfiguration
argument_list|()
operator|.
name|host
argument_list|(
literal|"0.0.0.0"
argument_list|)
operator|.
name|port
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"webhook:wb-delegate://xx"
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|prepend
argument_list|(
literal|"msg: "
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"webhook:wb-delegate://xx?webhookPath=/p2&webhookAutoRegister=false"
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|prepend
argument_list|(
literal|"uri: "
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|registered
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|unregistered
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|Awaitility
operator|.
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
name|registered
operator|.
name|get
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|unregistered
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|Awaitility
operator|.
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
name|unregistered
operator|.
name|get
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|registered
operator|.
name|get
argument_list|()
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
annotation|@
name|Override
DECL|method|bindToRegistry (Registry registry)
specifier|protected
name|void
name|bindToRegistry
parameter_list|(
name|Registry
name|registry
parameter_list|)
throws|throws
name|Exception
block|{
name|registry
operator|.
name|bind
argument_list|(
literal|"wb-delegate-component"
argument_list|,
operator|new
name|TestComponent
argument_list|(
name|endpoint
lambda|->
block|{
name|endpoint
operator|.
name|setWebhookHandler
argument_list|(
name|proc
lambda|->
name|ex
lambda|->
block|{
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"webhook"
argument_list|)
argument_list|;
name|proc
operator|.
name|process
argument_list|(
name|ex
argument_list|)
argument_list|;
block|}
argument_list|)
argument_list|;
name|endpoint
operator|.
name|setRegisterWebhook
argument_list|(
parameter_list|()
lambda|->
name|this
operator|.
name|registered
operator|.
name|incrementAndGet
argument_list|()
argument_list|)
argument_list|;
name|endpoint
operator|.
name|setUnregisterWebhook
argument_list|(
parameter_list|()
lambda|->
name|this
operator|.
name|unregistered
operator|.
name|incrementAndGet
argument_list|()
argument_list|)
argument_list|;
block|}
block|)
end_class

begin_empty_stmt
unit|)
empty_stmt|;
end_empty_stmt

unit|} }
end_unit

