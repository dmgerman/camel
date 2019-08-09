begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pulsar
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pulsar
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
name|component
operator|.
name|pulsar
operator|.
name|utils
operator|.
name|message
operator|.
name|PulsarMessageHeaders
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
name|component
operator|.
name|pulsar
operator|.
name|utils
operator|.
name|message
operator|.
name|PulsarMessageUtils
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
name|ExceptionHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pulsar
operator|.
name|client
operator|.
name|api
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
name|pulsar
operator|.
name|client
operator|.
name|api
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pulsar
operator|.
name|client
operator|.
name|api
operator|.
name|MessageListener
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

begin_class
DECL|class|PulsarMessageListener
specifier|public
class|class
name|PulsarMessageListener
implements|implements
name|MessageListener
argument_list|<
name|byte
index|[]
argument_list|>
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|PulsarMessageListener
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|PulsarEndpoint
name|endpoint
decl_stmt|;
DECL|field|exceptionHandler
specifier|private
specifier|final
name|ExceptionHandler
name|exceptionHandler
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|method|PulsarMessageListener (PulsarEndpoint endpoint, ExceptionHandler exceptionHandler, Processor processor)
specifier|public
name|PulsarMessageListener
parameter_list|(
name|PulsarEndpoint
name|endpoint
parameter_list|,
name|ExceptionHandler
name|exceptionHandler
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|exceptionHandler
operator|=
name|exceptionHandler
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|received (final Consumer<byte[]> consumer, final Message<byte[]> message)
specifier|public
name|void
name|received
parameter_list|(
specifier|final
name|Consumer
argument_list|<
name|byte
index|[]
argument_list|>
name|consumer
parameter_list|,
specifier|final
name|Message
argument_list|<
name|byte
index|[]
argument_list|>
name|message
parameter_list|)
block|{
specifier|final
name|Exchange
name|exchange
init|=
name|PulsarMessageUtils
operator|.
name|updateExchange
argument_list|(
name|message
argument_list|,
name|endpoint
operator|.
name|createExchange
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|endpoint
operator|.
name|getPulsarConfiguration
argument_list|()
operator|.
name|isAllowManualAcknowledgement
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|PulsarMessageHeaders
operator|.
name|MESSAGE_RECEIPT
argument_list|,
name|endpoint
operator|.
name|getComponent
argument_list|()
operator|.
name|getPulsarMessageReceiptFactory
argument_list|()
operator|.
name|newInstance
argument_list|(
name|exchange
argument_list|,
name|message
argument_list|,
name|consumer
argument_list|)
argument_list|)
expr_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|acknowledge
argument_list|(
name|message
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
name|handleProcessorException
argument_list|(
name|exchange
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|handleProcessorException (final Exchange exchange, final Exception exception)
specifier|private
name|void
name|handleProcessorException
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|Exception
name|exception
parameter_list|)
block|{
specifier|final
name|Exchange
name|exchangeWithException
init|=
name|PulsarMessageUtils
operator|.
name|updateExchangeWithException
argument_list|(
name|exception
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|exceptionHandler
operator|.
name|handleException
argument_list|(
literal|"An error occurred"
argument_list|,
name|exchangeWithException
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

