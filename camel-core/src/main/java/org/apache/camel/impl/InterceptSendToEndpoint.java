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
name|ShutdownableService
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * This is an endpoint when sending to it, is intercepted and is routed in a detour  *  * @version   */
end_comment

begin_class
DECL|class|InterceptSendToEndpoint
specifier|public
class|class
name|InterceptSendToEndpoint
implements|implements
name|Endpoint
implements|,
name|ShutdownableService
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
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
DECL|method|getDetour ()
specifier|public
name|Processor
name|getDetour
parameter_list|()
block|{
return|return
name|detour
return|;
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
return|return
name|createAsyncProducer
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createAsyncProducer ()
specifier|public
name|AsyncProducer
name|createAsyncProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|AsyncProducer
name|producer
init|=
name|delegate
operator|.
name|createAsyncProducer
argument_list|()
decl_stmt|;
return|return
operator|new
name|InterceptSendToEndpointProcessor
argument_list|(
name|this
argument_list|,
name|delegate
argument_list|,
name|producer
argument_list|,
name|skip
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
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|detour
argument_list|,
name|delegate
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
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|delegate
argument_list|,
name|detour
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopAndShutdownServices
argument_list|(
name|delegate
argument_list|,
name|detour
argument_list|)
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

