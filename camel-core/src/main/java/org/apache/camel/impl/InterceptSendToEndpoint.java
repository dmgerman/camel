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
name|ExchangePattern
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Producer
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
comment|/**  * This is an endpoint when sending to it, is intercepted and is routed in a detour  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|InterceptSendToEndpoint
specifier|public
class|class
name|InterceptSendToEndpoint
implements|implements
name|Endpoint
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|InterceptSendToEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|delegate
specifier|private
specifier|final
name|Endpoint
name|delegate
decl_stmt|;
DECL|field|producer
specifier|private
name|Producer
name|producer
decl_stmt|;
DECL|field|detour
specifier|private
name|Processor
name|detour
decl_stmt|;
DECL|field|skip
specifier|private
name|boolean
name|skip
decl_stmt|;
comment|/**      * Intercepts sending to the given endpoint      *      * @param destination  the original endpoint      * @param skip<tt>true</tt> to skip sending after the detour to the original endpoint      */
DECL|method|InterceptSendToEndpoint (final Endpoint destination, boolean skip)
specifier|public
name|InterceptSendToEndpoint
parameter_list|(
specifier|final
name|Endpoint
name|destination
parameter_list|,
name|boolean
name|skip
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|destination
expr_stmt|;
name|this
operator|.
name|skip
operator|=
name|skip
expr_stmt|;
block|}
DECL|method|setDetour (Processor detour)
specifier|public
name|void
name|setDetour
parameter_list|(
name|Processor
name|detour
parameter_list|)
block|{
name|this
operator|.
name|detour
operator|=
name|detour
expr_stmt|;
block|}
DECL|method|getDelegate ()
specifier|public
name|Endpoint
name|getDelegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
DECL|method|getEndpointUri ()
specifier|public
name|String
name|getEndpointUri
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getEndpointUri
argument_list|()
return|;
block|}
DECL|method|getEndpointKey ()
specifier|public
name|String
name|getEndpointKey
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getEndpointKey
argument_list|()
return|;
block|}
DECL|method|createExchange ()
specifier|public
name|Exchange
name|createExchange
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|createExchange
argument_list|()
return|;
block|}
DECL|method|createExchange (ExchangePattern pattern)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|createExchange
argument_list|(
name|pattern
argument_list|)
return|;
block|}
DECL|method|createExchange (Exchange exchange)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|createExchange
argument_list|(
name|exchange
argument_list|)
return|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getCamelContext
argument_list|()
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|producer
operator|=
name|delegate
operator|.
name|createProducer
argument_list|()
expr_stmt|;
return|return
operator|new
name|Producer
argument_list|()
block|{
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
specifier|public
name|Exchange
name|createExchange
parameter_list|()
block|{
return|return
name|producer
operator|.
name|createExchange
argument_list|()
return|;
block|}
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
return|return
name|producer
operator|.
name|createExchange
argument_list|(
name|pattern
argument_list|)
return|;
block|}
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|producer
operator|.
name|createExchange
argument_list|(
name|exchange
argument_list|)
return|;
block|}
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
comment|// process the detour so we do the detour routing
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Sending to endpoint: "
operator|+
name|getEndpointUri
argument_list|()
operator|+
literal|" is intercepted and detoured to: "
operator|+
name|detour
operator|+
literal|" for exchange: "
operator|+
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
try|try
block|{
name|detour
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
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
block|}
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
name|getEndpointUri
argument_list|()
argument_list|,
name|LOG
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
operator|!
name|skip
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
comment|// route to original destination
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Stop() means skip sending exchange to original intended destination: "
operator|+
name|getEndpointUri
argument_list|()
operator|+
literal|" for exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
name|detour
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|detour
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|hasExceptionBeenHandledByErrorHandler (Exchange nextExchange)
specifier|private
specifier|static
name|boolean
name|hasExceptionBeenHandledByErrorHandler
parameter_list|(
name|Exchange
name|nextExchange
parameter_list|)
block|{
return|return
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|nextExchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|ERRORHANDLER_HANDLED
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|delegate
operator|.
name|createConsumer
argument_list|(
name|processor
argument_list|)
return|;
block|}
DECL|method|createPollingConsumer ()
specifier|public
name|PollingConsumer
name|createPollingConsumer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|delegate
operator|.
name|createPollingConsumer
argument_list|()
return|;
block|}
DECL|method|configureProperties (Map<String, Object> options)
specifier|public
name|void
name|configureProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
block|{
name|delegate
operator|.
name|configureProperties
argument_list|(
name|options
argument_list|)
expr_stmt|;
block|}
DECL|method|setCamelContext (CamelContext context)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|delegate
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|isLenientProperties ()
specifier|public
name|boolean
name|isLenientProperties
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|isLenientProperties
argument_list|()
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
name|delegate
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
name|delegate
operator|.
name|start
argument_list|()
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
name|delegate
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

