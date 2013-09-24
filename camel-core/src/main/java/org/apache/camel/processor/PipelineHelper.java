begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|slf4j
operator|.
name|Logger
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
name|ExchangeHelper
operator|.
name|hasExceptionBeenHandledByErrorHandler
import|;
end_import

begin_comment
comment|/**  * Helper for processing {@link org.apache.camel.Exchange} in a  *<a href="http://camel.apache.org/pipes-and-filters.html">pipeline</a>.  *  * @version   */
end_comment

begin_class
DECL|class|PipelineHelper
specifier|public
specifier|final
class|class
name|PipelineHelper
block|{
DECL|method|PipelineHelper ()
specifier|private
name|PipelineHelper
parameter_list|()
block|{     }
comment|/**      * Should we continue processing the exchange?      *      * @param exchange the next exchange      * @param message a message to use when logging that we should not continue processing      * @param log a logger      * @return<tt>true</tt> to continue processing,<tt>false</tt> to break out, for example if an exception occurred.      */
DECL|method|continueProcessing (Exchange exchange, String message, Logger log)
specifier|public
specifier|static
name|boolean
name|continueProcessing
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|message
parameter_list|,
name|Logger
name|log
parameter_list|)
block|{
comment|// check for error if so we should break out
name|boolean
name|exceptionHandled
init|=
name|hasExceptionBeenHandledByErrorHandler
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|isFailed
argument_list|()
operator|||
name|exchange
operator|.
name|isRollbackOnly
argument_list|()
operator|||
name|exceptionHandled
condition|)
block|{
comment|// We need to write a warning message when the exception and fault message be set at the same time
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
operator|&&
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|isFault
argument_list|()
operator|&&
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"Message exchange has failed: "
operator|+
name|message
operator|+
literal|" for exchange: "
argument_list|)
operator|.
name|append
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|" Warning: Both fault and exception exists on the exchange, its best practice to only set one of them."
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|" Exception: "
argument_list|)
operator|.
name|append
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|" Fault: "
argument_list|)
operator|.
name|append
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|exceptionHandled
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" Handled by the error handler."
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|warn
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// The Exchange.ERRORHANDLED_HANDLED property is only set if satisfactory handling was done
comment|// by the error handler. It's still an exception, the exchange still failed.
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"Message exchange has failed: "
operator|+
name|message
operator|+
literal|" for exchange: "
argument_list|)
operator|.
name|append
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|isRollbackOnly
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" Marked as rollback only."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" Exception: "
argument_list|)
operator|.
name|append
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
operator|&&
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|isFault
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" Fault: "
argument_list|)
operator|.
name|append
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exceptionHandled
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" Handled by the error handler."
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
comment|// check for stop
name|Object
name|stop
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|ROUTE_STOP
argument_list|)
decl_stmt|;
if|if
condition|(
name|stop
operator|!=
literal|null
condition|)
block|{
name|boolean
name|doStop
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Boolean
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|stop
argument_list|)
decl_stmt|;
if|if
condition|(
name|doStop
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"ExchangeId: {} is marked to stop routing: {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Strategy method to create the next exchange from the previous exchange.      *<p/>      * Remember to copy the original exchange id otherwise correlation of ids in the log is a problem      *      * @param previousExchange the previous exchange      * @return a new exchange      */
DECL|method|createNextExchange (Exchange previousExchange)
specifier|public
specifier|static
name|Exchange
name|createNextExchange
parameter_list|(
name|Exchange
name|previousExchange
parameter_list|)
block|{
name|Exchange
name|answer
init|=
name|previousExchange
decl_stmt|;
comment|// now lets set the input of the next exchange to the output of the
comment|// previous message if it is not null
if|if
condition|(
name|answer
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|answer
operator|.
name|setIn
argument_list|(
name|answer
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setOut
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

