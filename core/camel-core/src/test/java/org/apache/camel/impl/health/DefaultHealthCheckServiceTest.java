begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.health
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|health
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
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
name|CamelContext
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
name|health
operator|.
name|HealthCheck
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
name|health
operator|.
name|HealthCheckRegistry
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
name|health
operator|.
name|HealthCheckResultBuilder
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
name|impl
operator|.
name|DefaultCamelContext
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
DECL|class|DefaultHealthCheckServiceTest
specifier|public
class|class
name|DefaultHealthCheckServiceTest
block|{
annotation|@
name|Test
argument_list|(
name|timeout
operator|=
literal|10000
argument_list|)
DECL|method|testDefaultHealthCheckService ()
specifier|public
name|void
name|testDefaultHealthCheckService
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|MyHealthCheck
name|check
init|=
operator|new
name|MyHealthCheck
argument_list|(
literal|""
argument_list|,
name|HealthCheck
operator|.
name|State
operator|.
name|UP
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|HealthCheck
operator|.
name|State
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|10
argument_list|)
decl_stmt|;
name|DefaultHealthCheckRegistry
name|registry
init|=
operator|new
name|DefaultHealthCheckRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|register
argument_list|(
name|check
argument_list|)
expr_stmt|;
name|DefaultHealthCheckService
name|service
init|=
operator|new
name|DefaultHealthCheckService
argument_list|()
decl_stmt|;
name|service
operator|.
name|setCheckInterval
argument_list|(
literal|500
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|service
operator|.
name|addStateChangeListener
argument_list|(
parameter_list|(
name|s
parameter_list|,
name|c
parameter_list|)
lambda|->
block|{
name|results
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|check
operator|.
name|flip
argument_list|()
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|setExtension
argument_list|(
name|HealthCheckRegistry
operator|.
name|class
argument_list|,
name|registry
argument_list|)
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|latch
operator|.
name|await
argument_list|()
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
name|results
operator|.
name|size
argument_list|()
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
name|Assert
operator|.
name|assertEquals
argument_list|(
name|HealthCheck
operator|.
name|State
operator|.
name|UP
argument_list|,
name|results
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
name|HealthCheck
operator|.
name|State
operator|.
name|DOWN
argument_list|,
name|results
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|service
operator|.
name|getResults
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|check
argument_list|,
name|service
operator|.
name|getResults
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getCheck
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|// ********************************
comment|//
comment|// ********************************
DECL|class|MyHealthCheck
specifier|private
class|class
name|MyHealthCheck
extends|extends
name|AbstractHealthCheck
block|{
DECL|field|state
specifier|private
name|HealthCheck
operator|.
name|State
name|state
decl_stmt|;
DECL|method|MyHealthCheck (String id, HealthCheck.State state)
name|MyHealthCheck
parameter_list|(
name|String
name|id
parameter_list|,
name|HealthCheck
operator|.
name|State
name|state
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|getConfiguration
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|state
operator|=
name|state
expr_stmt|;
block|}
DECL|method|flip ()
specifier|public
name|void
name|flip
parameter_list|()
block|{
name|this
operator|.
name|state
operator|=
name|this
operator|.
name|state
operator|==
name|State
operator|.
name|UP
condition|?
name|State
operator|.
name|DOWN
else|:
name|State
operator|.
name|UP
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doCall (HealthCheckResultBuilder builder, Map<String, Object> options)
specifier|public
name|void
name|doCall
parameter_list|(
name|HealthCheckResultBuilder
name|builder
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
block|{
name|builder
operator|.
name|state
argument_list|(
name|state
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

