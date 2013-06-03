begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.direct
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|direct
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
name|SuspendableService
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
comment|/**  * The direct consumer.  *  * @version   */
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
implements|,
name|SuspendableService
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
DECL|method|getEndpoint ()
specifier|public
name|DirectEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|DirectEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
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
comment|// add consumer to endpoint
name|boolean
name|existing
init|=
name|this
operator|==
name|endpoint
operator|.
name|getConsumer
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|existing
operator|&&
name|endpoint
operator|.
name|hasConsumer
argument_list|(
name|this
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot add a 2nd consumer to the same endpoint. Endpoint "
operator|+
name|endpoint
operator|+
literal|" only allows one consumer."
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|existing
condition|)
block|{
name|endpoint
operator|.
name|addConsumer
argument_list|(
name|this
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
name|endpoint
operator|.
name|removeConsumer
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doSuspend ()
specifier|protected
name|void
name|doSuspend
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|removeConsumer
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doResume ()
specifier|protected
name|void
name|doResume
parameter_list|()
throws|throws
name|Exception
block|{
comment|// resume by using the start logic
name|doStart
argument_list|()
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
DECL|method|prepareShutdown (boolean forced)
specifier|public
name|void
name|prepareShutdown
parameter_list|(
name|boolean
name|forced
parameter_list|)
block|{
comment|// noop
block|}
block|}
end_class

end_unit

