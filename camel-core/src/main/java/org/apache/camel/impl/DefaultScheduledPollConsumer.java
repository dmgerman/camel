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
name|Exchange
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
name|PollingConsumer
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

begin_comment
comment|/**  * A default implementation of an event driven {@link org.apache.camel.Consumer} which uses the  * {@link PollingConsumer}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultScheduledPollConsumer
specifier|public
class|class
name|DefaultScheduledPollConsumer
extends|extends
name|ScheduledPollConsumer
block|{
DECL|field|pollingConsumer
specifier|private
name|PollingConsumer
name|pollingConsumer
decl_stmt|;
DECL|method|DefaultScheduledPollConsumer (DefaultEndpoint defaultEndpoint, Processor processor)
specifier|public
name|DefaultScheduledPollConsumer
parameter_list|(
name|DefaultEndpoint
name|defaultEndpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|defaultEndpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultScheduledPollConsumer (Endpoint endpoint, Processor processor, ScheduledExecutorService executor)
specifier|public
name|DefaultScheduledPollConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ScheduledExecutorService
name|executor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|,
name|executor
argument_list|)
expr_stmt|;
block|}
DECL|method|poll ()
specifier|protected
name|void
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
while|while
condition|(
name|isPollAllowed
argument_list|()
condition|)
block|{
name|Exchange
name|exchange
init|=
name|pollingConsumer
operator|.
name|receiveNoWait
argument_list|()
decl_stmt|;
if|if
condition|(
name|exchange
operator|==
literal|null
condition|)
block|{
break|break;
block|}
comment|// if the result of the polled exchange has output we should create a new exchange and
comment|// use the output as input to the next processor
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
comment|// lets create a new exchange
name|Exchange
name|newExchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|copyFrom
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|=
name|newExchange
expr_stmt|;
block|}
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
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
name|pollingConsumer
operator|=
name|getEndpoint
argument_list|()
operator|.
name|createPollingConsumer
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|pollingConsumer
operator|!=
literal|null
condition|)
block|{
name|pollingConsumer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

