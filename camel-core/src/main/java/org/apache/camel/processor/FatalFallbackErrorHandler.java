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
name|Processor
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
comment|/**  * An {@link org.apache.camel.processor.ErrorHandler} used as a safe fallback when  * processing by other error handlers such as the {@link org.apache.camel.model.OnExceptionDefinition}.  *  * @version  */
end_comment

begin_class
DECL|class|FatalFallbackErrorHandler
specifier|public
class|class
name|FatalFallbackErrorHandler
extends|extends
name|DelegateAsyncProcessor
implements|implements
name|ErrorHandler
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
name|FatalFallbackErrorHandler
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|FatalFallbackErrorHandler (Processor processor)
specifier|public
name|FatalFallbackErrorHandler
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|processNext (final Exchange exchange, final AsyncCallback callback)
specifier|protected
name|boolean
name|processNext
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
comment|// support the asynchronous routing engine
name|boolean
name|sync
init|=
name|super
operator|.
name|processNext
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
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
comment|// an exception occurred during processing onException
comment|// log a warning
name|LOG
operator|.
name|error
argument_list|(
literal|"Exception occurred while processing exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" using: ["
operator|+
name|processor
operator|+
literal|"] caused by: "
operator|+
name|exchange
operator|.
name|getException
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
comment|// we can propagated that exception to the caught property on the exchange
comment|// which will shadow any previously caught exception and cause this new exception
comment|// to be visible in the error handler
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
comment|// mark this exchange as already been error handler handled (just by having this property)
comment|// the false value mean the caught exception will be kept on the exchange, causing the
comment|// exception to be propagated back to the caller, and to break out routing
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|ERRORHANDLER_HANDLED
argument_list|,
literal|false
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
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|sync
return|;
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
literal|"FatalFallbackErrorHandler["
operator|+
name|processor
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

