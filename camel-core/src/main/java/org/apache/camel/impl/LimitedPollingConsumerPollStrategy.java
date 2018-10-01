begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Consumer
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
name|Endpoint
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
name|Service
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
name|ServiceHelper
import|;
end_import

begin_comment
comment|/**  * A {@link org.apache.camel.spi.PollingConsumerPollStrategy} which supports suspending consumers if they  * failed for X number of times in a row.  *<p/>  * If Camel cannot successfully consumer from a given consumer, then after X consecutive failed attempts the consumer  * will be suspended/stopped. This prevents the log to get flooded with failed attempts, for example during nightly runs.  */
end_comment

begin_class
DECL|class|LimitedPollingConsumerPollStrategy
specifier|public
class|class
name|LimitedPollingConsumerPollStrategy
extends|extends
name|DefaultPollingConsumerPollStrategy
implements|implements
name|Service
block|{
DECL|field|state
specifier|private
specifier|final
name|Map
argument_list|<
name|Consumer
argument_list|,
name|Integer
argument_list|>
name|state
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|limit
specifier|private
name|int
name|limit
init|=
literal|3
decl_stmt|;
DECL|method|getLimit ()
specifier|public
name|int
name|getLimit
parameter_list|()
block|{
return|return
name|limit
return|;
block|}
comment|/**      * Sets the limit for how many straight rollbacks causes this strategy to suspend the fault consumer.      *<p/>      * When the consumer has been suspended, it has to be manually resumed/started to be active again.      * The limit is by default 3.      *      * @param limit  the limit      */
DECL|method|setLimit (int limit)
specifier|public
name|void
name|setLimit
parameter_list|(
name|int
name|limit
parameter_list|)
block|{
name|this
operator|.
name|limit
operator|=
name|limit
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|commit (Consumer consumer, Endpoint endpoint, int polledMessages)
specifier|public
name|void
name|commit
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|int
name|polledMessages
parameter_list|)
block|{
comment|// we could commit so clear state
name|state
operator|.
name|remove
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|method|rollback (Consumer consumer, Endpoint endpoint, int retryCounter, Exception cause)
specifier|public
name|boolean
name|rollback
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|int
name|retryCounter
parameter_list|,
name|Exception
name|cause
parameter_list|)
throws|throws
name|Exception
block|{
comment|// keep track how many times in a row we have rolled back
name|Integer
name|times
init|=
name|state
operator|.
name|get
argument_list|(
name|consumer
argument_list|)
decl_stmt|;
if|if
condition|(
name|times
operator|==
literal|null
condition|)
block|{
name|times
operator|=
literal|1
expr_stmt|;
block|}
else|else
block|{
name|times
operator|+=
literal|1
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Rollback occurred after {} times when consuming {}"
argument_list|,
name|times
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|boolean
name|retry
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|times
operator|>=
name|limit
condition|)
block|{
comment|// clear state when we suspend so if its restarted manually we start all over again
name|state
operator|.
name|remove
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|onSuspend
argument_list|(
name|consumer
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// error occurred
name|state
operator|.
name|put
argument_list|(
name|consumer
argument_list|,
name|times
argument_list|)
expr_stmt|;
name|retry
operator|=
name|onRollback
argument_list|(
name|consumer
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
return|return
name|retry
return|;
block|}
comment|/**      * The consumer is to be suspended because it exceeded the limit      *      * @param consumer the consumer      * @param endpoint the endpoint      * @throws Exception is thrown if error suspending the consumer      */
DECL|method|onSuspend (Consumer consumer, Endpoint endpoint)
specifier|protected
name|void
name|onSuspend
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Suspending consumer "
operator|+
name|consumer
operator|+
literal|" after "
operator|+
name|limit
operator|+
literal|" attempts to consume from "
operator|+
name|endpoint
operator|+
literal|". You have to manually resume the consumer!"
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|suspendService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
comment|/**      * Rollback occurred.      *      * @param consumer the consumer      * @param endpoint the endpoint      * @return whether or not to retry immediately, is default<tt>false</tt>      * @throws Exception can be thrown in case something goes wrong      */
DECL|method|onRollback (Consumer consumer, Endpoint endpoint)
specifier|protected
name|boolean
name|onRollback
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
comment|// do not retry by default
return|return
literal|false
return|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|state
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

