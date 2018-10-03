begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|channels
operator|.
name|ClosedChannelException
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
name|CamelExchangeException
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
name|LoggingLevel
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
name|camel
operator|.
name|support
operator|.
name|CamelLogger
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
name|ObjectHelper
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
DECL|class|NettyConsumerExceptionHandler
specifier|public
class|class
name|NettyConsumerExceptionHandler
implements|implements
name|ExceptionHandler
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
name|NettyConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|logger
specifier|private
specifier|final
name|CamelLogger
name|logger
decl_stmt|;
DECL|field|closedLoggingLevel
specifier|private
specifier|final
name|LoggingLevel
name|closedLoggingLevel
decl_stmt|;
DECL|method|NettyConsumerExceptionHandler (NettyConsumer consumer)
specifier|public
name|NettyConsumerExceptionHandler
parameter_list|(
name|NettyConsumer
name|consumer
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
operator|new
name|CamelLogger
argument_list|(
name|LOG
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getServerExceptionCaughtLogLevel
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|closedLoggingLevel
operator|=
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getServerClosedChannelExceptionCaughtLogLevel
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|handleException (Throwable exception)
specifier|public
name|void
name|handleException
parameter_list|(
name|Throwable
name|exception
parameter_list|)
block|{
name|handleException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|handleException (String message, Throwable exception)
specifier|public
name|void
name|handleException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|exception
parameter_list|)
block|{
name|handleException
argument_list|(
name|message
argument_list|,
literal|null
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|handleException (String message, Exchange exchange, Throwable exception)
specifier|public
name|void
name|handleException
parameter_list|(
name|String
name|message
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|exception
parameter_list|)
block|{
try|try
block|{
name|String
name|msg
init|=
name|CamelExchangeException
operator|.
name|createExceptionMessage
argument_list|(
name|message
argument_list|,
name|exchange
argument_list|,
name|exception
argument_list|)
decl_stmt|;
name|boolean
name|closed
init|=
name|ObjectHelper
operator|.
name|getException
argument_list|(
name|ClosedChannelException
operator|.
name|class
argument_list|,
name|exception
argument_list|)
operator|!=
literal|null
decl_stmt|;
if|if
condition|(
name|closed
condition|)
block|{
name|logger
operator|.
name|log
argument_list|(
name|msg
argument_list|,
name|exception
argument_list|,
name|closedLoggingLevel
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|.
name|log
argument_list|(
name|msg
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// the logging exception handler must not cause new exceptions to occur
block|}
block|}
block|}
end_class

end_unit

