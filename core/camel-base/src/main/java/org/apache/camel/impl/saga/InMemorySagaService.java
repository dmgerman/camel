begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.saga
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|saga
package|;
end_package

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
name|CompletableFuture
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
name|ConcurrentHashMap
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
name|ScheduledExecutorService
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
name|saga
operator|.
name|CamelSagaCoordinator
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
name|saga
operator|.
name|CamelSagaService
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
name|saga
operator|.
name|CamelSagaStep
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
name|support
operator|.
name|service
operator|.
name|ServiceSupport
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * A in-memory implementation of a saga service.  */
end_comment

begin_class
DECL|class|InMemorySagaService
specifier|public
class|class
name|InMemorySagaService
extends|extends
name|ServiceSupport
implements|implements
name|CamelSagaService
block|{
DECL|field|DEFAULT_MAX_RETRY_ATTEMPTS
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_MAX_RETRY_ATTEMPTS
init|=
literal|5
decl_stmt|;
DECL|field|DEFAULT_RETRY_DELAY_IN_MILLISECONDS
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_RETRY_DELAY_IN_MILLISECONDS
init|=
literal|5000
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|coordinators
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|CamelSagaCoordinator
argument_list|>
name|coordinators
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|executorService
specifier|private
name|ScheduledExecutorService
name|executorService
decl_stmt|;
DECL|field|maxRetryAttempts
specifier|private
name|int
name|maxRetryAttempts
init|=
name|DEFAULT_MAX_RETRY_ATTEMPTS
decl_stmt|;
DECL|field|retryDelayInMilliseconds
specifier|private
name|long
name|retryDelayInMilliseconds
init|=
name|DEFAULT_RETRY_DELAY_IN_MILLISECONDS
decl_stmt|;
annotation|@
name|Override
DECL|method|newSaga ()
specifier|public
name|CompletableFuture
argument_list|<
name|CamelSagaCoordinator
argument_list|>
name|newSaga
parameter_list|()
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
name|String
name|uuid
init|=
name|camelContext
operator|.
name|getUuidGenerator
argument_list|()
operator|.
name|generateUuid
argument_list|()
decl_stmt|;
name|CamelSagaCoordinator
name|coordinator
init|=
operator|new
name|InMemorySagaCoordinator
argument_list|(
name|camelContext
argument_list|,
name|this
argument_list|,
name|uuid
argument_list|)
decl_stmt|;
name|coordinators
operator|.
name|put
argument_list|(
name|uuid
argument_list|,
name|coordinator
argument_list|)
expr_stmt|;
return|return
name|CompletableFuture
operator|.
name|completedFuture
argument_list|(
name|coordinator
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getSaga (String id)
specifier|public
name|CompletableFuture
argument_list|<
name|CamelSagaCoordinator
argument_list|>
name|getSaga
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
name|CompletableFuture
operator|.
name|completedFuture
argument_list|(
name|coordinators
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|registerStep (CamelSagaStep step)
specifier|public
name|void
name|registerStep
parameter_list|(
name|CamelSagaStep
name|step
parameter_list|)
block|{
comment|// do nothing
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|this
operator|.
name|executorService
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|executorService
operator|=
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newDefaultScheduledThreadPool
argument_list|(
name|this
argument_list|,
literal|"saga"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|this
operator|.
name|executorService
operator|!=
literal|null
condition|)
block|{
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownGraceful
argument_list|(
name|this
operator|.
name|executorService
argument_list|)
expr_stmt|;
name|this
operator|.
name|executorService
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|getExecutorService ()
specifier|public
name|ScheduledExecutorService
name|getExecutorService
parameter_list|()
block|{
return|return
name|executorService
return|;
block|}
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|getMaxRetryAttempts ()
specifier|public
name|int
name|getMaxRetryAttempts
parameter_list|()
block|{
return|return
name|maxRetryAttempts
return|;
block|}
DECL|method|setMaxRetryAttempts (int maxRetryAttempts)
specifier|public
name|void
name|setMaxRetryAttempts
parameter_list|(
name|int
name|maxRetryAttempts
parameter_list|)
block|{
name|this
operator|.
name|maxRetryAttempts
operator|=
name|maxRetryAttempts
expr_stmt|;
block|}
DECL|method|getRetryDelayInMilliseconds ()
specifier|public
name|long
name|getRetryDelayInMilliseconds
parameter_list|()
block|{
return|return
name|retryDelayInMilliseconds
return|;
block|}
DECL|method|setRetryDelayInMilliseconds (long retryDelayInMilliseconds)
specifier|public
name|void
name|setRetryDelayInMilliseconds
parameter_list|(
name|long
name|retryDelayInMilliseconds
parameter_list|)
block|{
name|this
operator|.
name|retryDelayInMilliseconds
operator|=
name|retryDelayInMilliseconds
expr_stmt|;
block|}
block|}
end_class

end_unit
