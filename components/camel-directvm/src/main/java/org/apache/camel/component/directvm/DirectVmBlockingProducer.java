begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.directvm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|directvm
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
name|AsyncCallback
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
name|support
operator|.
name|DefaultAsyncProducer
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
name|StopWatch
import|;
end_import

begin_comment
comment|/**  * The direct producer.  *<p/>  * If blocking is enabled ({@code DirectEndpoint#isBlock}) then the  * DirectEndpoint will create an instance of this class instead of  * {@code DirectProducer}. This producers {@code process} method will block for  * the configured duration ({@code DirectEndpoint#getTimeout}, default to 30  * seconds). After which if a consumer is still unavailable a  * DirectConsumerNotAvailableException will be thrown.  *<p/>  * Implementation note: Concurrent Producers will block for the duration it  * takes to determine if a consumer is available, but actual consumer execution  * will happen concurrently.  */
end_comment

begin_class
DECL|class|DirectVmBlockingProducer
specifier|public
class|class
name|DirectVmBlockingProducer
extends|extends
name|DefaultAsyncProducer
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|DirectVmEndpoint
name|endpoint
decl_stmt|;
DECL|method|DirectVmBlockingProducer (DirectVmEndpoint endpoint)
specifier|public
name|DirectVmBlockingProducer
parameter_list|(
name|DirectVmEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|getConsumer
argument_list|(
name|exchange
argument_list|)
operator|.
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
try|try
block|{
return|return
name|getConsumer
argument_list|(
name|exchange
argument_list|)
operator|.
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
DECL|method|getConsumer (Exchange exchange)
specifier|protected
name|DirectVmConsumer
name|getConsumer
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|DirectVmConsumer
name|answer
init|=
name|endpoint
operator|.
name|getConsumer
argument_list|()
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// okay then await until we have a consumer or we timed out
if|if
condition|(
name|endpoint
operator|.
name|isFailIfNoConsumers
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|DirectVmConsumerNotAvailableException
argument_list|(
literal|"No consumers available on endpoint: "
operator|+
name|endpoint
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
else|else
block|{
name|answer
operator|=
name|awaitConsumer
argument_list|()
expr_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|DirectVmConsumerNotAvailableException
argument_list|(
literal|"No consumers available on endpoint: "
operator|+
name|endpoint
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|awaitConsumer ()
specifier|private
name|DirectVmConsumer
name|awaitConsumer
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|DirectVmConsumer
name|answer
init|=
literal|null
decl_stmt|;
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
name|boolean
name|done
init|=
literal|false
decl_stmt|;
while|while
condition|(
operator|!
name|done
condition|)
block|{
comment|// sleep a bit to give chance for the consumer to be ready
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Waited {} for consumer to be ready"
argument_list|,
name|watch
operator|.
name|taken
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|answer
operator|=
name|endpoint
operator|.
name|getConsumer
argument_list|()
expr_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
return|return
name|answer
return|;
block|}
comment|// we are done if we hit the timeout
name|done
operator|=
name|watch
operator|.
name|taken
argument_list|()
operator|>=
name|endpoint
operator|.
name|getTimeout
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

