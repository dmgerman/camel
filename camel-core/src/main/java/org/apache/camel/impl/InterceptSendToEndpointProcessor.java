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
name|Arrays
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
name|AsyncProcessor
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
name|AsyncProducer
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
name|processor
operator|.
name|Pipeline
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
name|AsyncProcessorConverterHelper
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
name|AsyncProcessorSupport
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
name|support
operator|.
name|service
operator|.
name|ServiceHelper
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|PipelineHelper
operator|.
name|continueProcessing
import|;
end_import

begin_comment
comment|/**  * {@link org.apache.camel.Processor} used to interceptor and detour the routing  * when using the {@link DefaultInterceptSendToEndpoint} functionality.  */
end_comment

begin_class
DECL|class|InterceptSendToEndpointProcessor
specifier|public
class|class
name|InterceptSendToEndpointProcessor
extends|extends
name|DefaultAsyncProducer
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|DefaultInterceptSendToEndpoint
name|endpoint
decl_stmt|;
DECL|field|delegate
specifier|private
specifier|final
name|Endpoint
name|delegate
decl_stmt|;
DECL|field|producer
specifier|private
specifier|final
name|AsyncProducer
name|producer
decl_stmt|;
DECL|field|skip
specifier|private
specifier|final
name|boolean
name|skip
decl_stmt|;
DECL|method|InterceptSendToEndpointProcessor (DefaultInterceptSendToEndpoint endpoint, Endpoint delegate, AsyncProducer producer, boolean skip)
specifier|public
name|InterceptSendToEndpointProcessor
parameter_list|(
name|DefaultInterceptSendToEndpoint
name|endpoint
parameter_list|,
name|Endpoint
name|delegate
parameter_list|,
name|AsyncProducer
name|producer
parameter_list|,
name|boolean
name|skip
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|producer
operator|=
name|producer
expr_stmt|;
name|this
operator|.
name|skip
operator|=
name|skip
expr_stmt|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|producer
operator|.
name|getEndpoint
argument_list|()
return|;
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
comment|// process the detour so we do the detour routing
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
literal|"Sending to endpoint: {} is intercepted and detoured to: {} for exchange: {}"
argument_list|,
name|getEndpoint
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getDetour
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// add header with the real endpoint uri
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|INTERCEPTED_ENDPOINT
argument_list|,
name|delegate
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getDetour
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// detour the exchange using synchronous processing
name|AsyncProcessor
name|detour
init|=
name|AsyncProcessorConverterHelper
operator|.
name|convert
argument_list|(
name|endpoint
operator|.
name|getDetour
argument_list|()
argument_list|)
decl_stmt|;
name|AsyncProcessor
name|ascb
init|=
operator|new
name|AsyncProcessorSupport
argument_list|()
block|{
annotation|@
name|Override
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
return|return
name|callback
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
literal|true
argument_list|)
return|;
block|}
block|}
decl_stmt|;
return|return
operator|new
name|Pipeline
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|detour
argument_list|,
name|ascb
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
return|return
name|callback
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|callback (Exchange exchange, AsyncCallback callback, boolean doneSync)
specifier|private
name|boolean
name|callback
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|boolean
name|doneSync
parameter_list|)
block|{
comment|// Decide whether to continue or not; similar logic to the Pipeline
comment|// check for error if so we should break out
if|if
condition|(
operator|!
name|continueProcessing
argument_list|(
name|exchange
argument_list|,
literal|"skip sending to original intended destination: "
operator|+
name|getEndpoint
argument_list|()
argument_list|,
name|log
argument_list|)
condition|)
block|{
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
return|return
name|doneSync
return|;
block|}
comment|// determine if we should skip or not
name|boolean
name|shouldSkip
init|=
name|skip
decl_stmt|;
comment|// if then interceptor had a when predicate, then we should only skip if it matched
name|Boolean
name|whenMatches
init|=
operator|(
name|Boolean
operator|)
name|exchange
operator|.
name|removeProperty
argument_list|(
name|Exchange
operator|.
name|INTERCEPT_SEND_TO_ENDPOINT_WHEN_MATCHED
argument_list|)
decl_stmt|;
if|if
condition|(
name|whenMatches
operator|!=
literal|null
condition|)
block|{
name|shouldSkip
operator|=
name|skip
operator|&&
name|whenMatches
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|shouldSkip
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
comment|// replace OUT with IN as detour changed something
name|exchange
operator|.
name|setIn
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setOut
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
comment|// route to original destination leveraging the asynchronous routing engine if possible
name|boolean
name|s
init|=
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|ds
lambda|->
block|{
name|callback
operator|.
name|done
argument_list|(
name|doneSync
operator|&&
name|ds
argument_list|)
expr_stmt|;
block|}
argument_list|)
decl_stmt|;
return|return
name|doneSync
operator|&&
name|s
return|;
block|}
else|else
block|{
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
literal|"Stop() means skip sending exchange to original intended destination: {} for exchange: {}"
argument_list|,
name|getEndpoint
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
return|return
name|doneSync
return|;
block|}
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
name|producer
operator|.
name|isSingleton
argument_list|()
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
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|endpoint
operator|.
name|getDetour
argument_list|()
argument_list|)
expr_stmt|;
comment|// here we also need to start the producer
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// do not stop detour as it should only be stopped when the interceptor stops
comment|// we should stop the producer here
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

