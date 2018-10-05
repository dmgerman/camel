begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.seda
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|seda
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
name|IsSingleton
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
name|RuntimeCamelException
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
name|PollingConsumerSupport
import|;
end_import

begin_class
DECL|class|SedaPollingConsumer
specifier|public
class|class
name|SedaPollingConsumer
extends|extends
name|PollingConsumerSupport
implements|implements
name|IsSingleton
block|{
DECL|method|SedaPollingConsumer (Endpoint endpoint)
specifier|public
name|SedaPollingConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|SedaEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|SedaEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getProcessor ()
specifier|public
name|Processor
name|getProcessor
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|receive ()
specifier|public
name|Exchange
name|receive
parameter_list|()
block|{
try|try
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getQueue
argument_list|()
operator|.
name|take
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|receiveNoWait ()
specifier|public
name|Exchange
name|receiveNoWait
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getQueue
argument_list|()
operator|.
name|poll
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|receive (long timeout)
specifier|public
name|Exchange
name|receive
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
try|try
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getQueue
argument_list|()
operator|.
name|poll
argument_list|(
name|timeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
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
comment|// noop
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
comment|// noop
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

