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
name|RollbackExchangeException
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
name|util
operator|.
name|ExchangeHelper
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
comment|/**  * A default implementation of {@link ExceptionHandler} which uses a {@link org.apache.camel.processor.CamelLogger} to  * log to an arbitrary {@link org.apache.commons.logging.Log Logger} with some {@link LoggingLevel}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|LoggingExceptionHandler
specifier|public
class|class
name|LoggingExceptionHandler
implements|implements
name|ExceptionHandler
block|{
DECL|field|logger
specifier|private
specifier|final
name|CamelLogger
name|logger
decl_stmt|;
DECL|method|LoggingExceptionHandler (Class<?> ownerType)
specifier|public
name|LoggingExceptionHandler
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|ownerType
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|CamelLogger
argument_list|(
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ownerType
argument_list|)
argument_list|,
name|LoggingLevel
operator|.
name|ERROR
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|LoggingExceptionHandler (CamelLogger logger)
specifier|public
name|LoggingExceptionHandler
parameter_list|(
name|CamelLogger
name|logger
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
block|}
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
name|String
name|msg
init|=
name|ExchangeHelper
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
if|if
condition|(
name|isCausedByRollbackExchangeException
argument_list|(
name|exception
argument_list|)
condition|)
block|{
comment|// do not log stacktrace for intended rollbacks
name|logger
operator|.
name|log
argument_list|(
name|msg
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
DECL|method|isCausedByRollbackExchangeException (Throwable exception)
specifier|protected
name|boolean
name|isCausedByRollbackExchangeException
parameter_list|(
name|Throwable
name|exception
parameter_list|)
block|{
if|if
condition|(
name|exception
operator|instanceof
name|RollbackExchangeException
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|exception
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// recursive children
return|return
name|isCausedByRollbackExchangeException
argument_list|(
name|exception
operator|.
name|getCause
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

