begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|$
block|{
name|packageName
block|}
end_package

begin_empty_stmt
empty_stmt|;
end_empty_stmt

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
name|Processor
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
name|ShutdownRunningTask
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
name|DefaultConsumer
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
name|ShutdownAware
import|;
end_import

begin_comment
comment|/**  * The direct consumer.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DirectConsumer
specifier|public
class|class
name|DirectConsumer
extends|extends
name|DefaultConsumer
implements|implements
name|ShutdownAware
block|{
DECL|field|endpoint
specifier|private
name|DirectEndpoint
name|endpoint
decl_stmt|;
DECL|method|DirectConsumer (Endpoint endpoint, Processor processor)
specifier|public
name|DirectConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
operator|(
name|DirectEndpoint
operator|)
name|endpoint
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
comment|// only add as consumer if not already registered
if|if
condition|(
operator|!
name|endpoint
operator|.
name|getConsumers
argument_list|()
operator|.
name|contains
argument_list|(
name|this
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|endpoint
operator|.
name|getConsumers
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Endpoint "
operator|+
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
operator|+
literal|" only allows 1 active consumer but you attempted to start a 2nd consumer."
argument_list|)
throw|;
block|}
name|endpoint
operator|.
name|getConsumers
argument_list|()
operator|.
name|add
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|stop
argument_list|()
expr_stmt|;
name|endpoint
operator|.
name|getConsumers
argument_list|()
operator|.
name|remove
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|deferShutdown (ShutdownRunningTask shutdownRunningTask)
specifier|public
name|boolean
name|deferShutdown
parameter_list|(
name|ShutdownRunningTask
name|shutdownRunningTask
parameter_list|)
block|{
comment|// deny stopping on shutdown as we want direct consumers to run in case some other queues
comment|// depend on this consumer to run, so it can complete its exchanges
return|return
literal|true
return|;
block|}
DECL|method|getPendingExchangesSize ()
specifier|public
name|int
name|getPendingExchangesSize
parameter_list|()
block|{
comment|// return 0 as we do not have an internal memory queue with a variable size
comment|// of inflight messages.
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit

