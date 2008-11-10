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
name|FailedToCreateProducerException
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
name|impl
operator|.
name|converter
operator|.
name|AsyncProcessorTypeConverter
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
name|util
operator|.
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
import|;
end_import

begin_comment
comment|/**  * Cache containing created {@link Producer}.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ProducerCache
specifier|public
class|class
name|ProducerCache
extends|extends
name|ServiceSupport
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
name|ProducerCache
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|producers
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Producer
argument_list|>
name|producers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Producer
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|getProducer (Endpoint endpoint)
specifier|public
specifier|synchronized
name|Producer
name|getProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|String
name|key
init|=
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
decl_stmt|;
name|Producer
name|answer
init|=
name|producers
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|answer
operator|=
name|endpoint
operator|.
name|createProducer
argument_list|()
expr_stmt|;
name|answer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|FailedToCreateProducerException
argument_list|(
name|endpoint
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|producers
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Sends the exchange to the given endpoint      *      * @param endpoint the endpoint to send the exchange to      * @param exchange the exchange to send      */
DECL|method|send (Endpoint endpoint, Exchange exchange)
specifier|public
name|void
name|send
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
name|Producer
name|producer
init|=
name|getProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|producer
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
throw|throw
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Sends an exchange to an endpoint using a supplied      * {@link Processor} to populate the exchange      *      * @param endpoint the endpoint to send the exchange to      * @param processor the transformer used to populate the new exchange      */
DECL|method|send (Endpoint endpoint, Processor processor)
specifier|public
name|Exchange
name|send
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
try|try
block|{
name|Producer
name|producer
init|=
name|getProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|()
decl_stmt|;
return|return
name|sendExchange
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|,
name|processor
argument_list|,
name|exchange
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Sends an exchange to an endpoint using a supplied      * {@link Processor} to populate the exchange.  The callback      * will be called when the exchange is completed.      *      * @param endpoint the endpoint to send the exchange to      * @param processor the transformer used to populate the new exchange      */
DECL|method|send (Endpoint endpoint, Processor processor, AsyncCallback callback)
specifier|public
name|Exchange
name|send
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
try|try
block|{
name|Producer
name|producer
init|=
name|getProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|boolean
name|sync
init|=
name|sendExchange
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|,
name|processor
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|)
decl_stmt|;
name|setProcessedSync
argument_list|(
name|exchange
argument_list|,
name|sync
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|isProcessedSync (Exchange exchange)
specifier|public
specifier|static
name|boolean
name|isProcessedSync
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Boolean
name|rc
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|ProducerCache
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".SYNC"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|rc
operator|==
literal|null
condition|?
literal|false
else|:
name|rc
return|;
block|}
DECL|method|setProcessedSync (Exchange exchange, boolean b)
specifier|public
specifier|static
name|void
name|setProcessedSync
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|boolean
name|b
parameter_list|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|ProducerCache
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".SYNC"
argument_list|,
name|b
condition|?
name|Boolean
operator|.
name|TRUE
else|:
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sends an exchange to an endpoint using a supplied      * {@link Processor} to populate the exchange      *      * @param endpoint the endpoint to send the exchange to      * @param pattern the message {@link ExchangePattern} such as      *   {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param processor the transformer used to populate the new exchange      */
DECL|method|send (Endpoint endpoint, ExchangePattern pattern, Processor processor)
specifier|public
name|Exchange
name|send
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
try|try
block|{
name|Producer
name|producer
init|=
name|getProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|(
name|pattern
argument_list|)
decl_stmt|;
return|return
name|sendExchange
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|,
name|processor
argument_list|,
name|exchange
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|sendExchange (Endpoint endpoint, Producer producer, Processor processor, Exchange exchange)
specifier|protected
name|Exchange
name|sendExchange
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Producer
name|producer
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// lets populate using the processor callback
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// now lets dispatch
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
literal|">>>> "
operator|+
name|endpoint
operator|+
literal|" "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|sendExchange (Endpoint endpoint, Producer producer, Processor processor, Exchange exchange, AsyncCallback callback)
specifier|protected
name|boolean
name|sendExchange
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Producer
name|producer
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
throws|throws
name|Exception
block|{
comment|// lets populate using the processor callback
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// now lets dispatch
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
literal|">>>> "
operator|+
name|endpoint
operator|+
literal|" "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return
name|AsyncProcessorTypeConverter
operator|.
name|convert
argument_list|(
name|producer
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
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|producers
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|producers
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{     }
block|}
end_class

end_unit

