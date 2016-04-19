begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hystrix.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hystrix
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|hystrix
operator|.
name|HystrixCommand
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|hystrix
operator|.
name|HystrixCommandGroupKey
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
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Hystrix Command for the Camel Hystrix EIP.  */
end_comment

begin_class
DECL|class|HystrixProcessorCommand
specifier|public
class|class
name|HystrixProcessorCommand
extends|extends
name|HystrixCommand
argument_list|<
name|Exchange
argument_list|>
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
name|HystrixProcessorCommand
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|callback
specifier|private
specifier|final
name|AsyncCallback
name|callback
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|AsyncProcessor
name|processor
decl_stmt|;
DECL|field|fallback
specifier|private
specifier|final
name|AsyncProcessor
name|fallback
decl_stmt|;
DECL|method|HystrixProcessorCommand (Setter setter, Exchange exchange, AsyncCallback callback, AsyncProcessor processor, AsyncProcessor fallback)
specifier|public
name|HystrixProcessorCommand
parameter_list|(
name|Setter
name|setter
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|AsyncProcessor
name|processor
parameter_list|,
name|AsyncProcessor
name|fallback
parameter_list|)
block|{
name|super
argument_list|(
name|setter
argument_list|)
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
name|this
operator|.
name|fallback
operator|=
name|fallback
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getFallback ()
specifier|protected
name|Exchange
name|getFallback
parameter_list|()
block|{
comment|// only run fallback if there was an exception
name|Exception
name|exception
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|exception
operator|==
literal|null
condition|)
block|{
return|return
name|exchange
return|;
block|}
try|try
block|{
if|if
condition|(
name|fallback
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Error occurred processing. Will now run fallback. Exception class: {} message: {}."
argument_list|,
name|exception
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|exception
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
comment|// store the last to endpoint as the failure endpoint
if|if
condition|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_ENDPOINT
argument_list|)
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_ENDPOINT
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// give the rest of the pipeline another chance
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_HANDLED
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|exception
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// and we should not be regarded as exhausted as we are in a try .. catch block
name|exchange
operator|.
name|removeProperty
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_EXHAUSTED
argument_list|)
expr_stmt|;
comment|// run the fallback processor
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Running fallback: {} with exchange: {}"
argument_list|,
name|fallback
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|fallback
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
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
block|}
block|}
finally|finally
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Running fallback: {} with exchange: {} done"
argument_list|,
name|fallback
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|removeProperty
argument_list|(
name|Exchange
operator|.
name|TRY_ROUTE_BLOCK
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|exchange
return|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|protected
name|Exchange
name|run
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Running processor: {} with exchange: {}"
argument_list|,
name|processor
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// run this as if we run inside try .. catch so there is no regular Camel error handler
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|TRY_ROUTE_BLOCK
argument_list|,
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
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
comment|// is fallback enabled
name|Boolean
name|fallbackEnabled
init|=
name|getProperties
argument_list|()
operator|.
name|fallbackEnabled
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
comment|// if we failed then throw an exception if fallback is enabled
if|if
condition|(
name|fallbackEnabled
operator|==
literal|null
operator|||
name|fallbackEnabled
operator|&&
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
name|exchange
operator|.
name|getException
argument_list|()
throw|;
block|}
comment|// no fallback then we are done
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Running processor: {} with exchange: {} done"
argument_list|,
name|processor
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|removeProperty
argument_list|(
name|Exchange
operator|.
name|TRY_ROUTE_BLOCK
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
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
return|return
name|exchange
return|;
block|}
block|}
end_class

end_unit

