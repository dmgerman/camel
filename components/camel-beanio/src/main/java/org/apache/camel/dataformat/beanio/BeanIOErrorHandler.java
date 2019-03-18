begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.beanio
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|beanio
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|beanio
operator|.
name|BeanReaderErrorHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|beanio
operator|.
name|BeanReaderErrorHandlerSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|beanio
operator|.
name|InvalidRecordException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|beanio
operator|.
name|UnexpectedRecordException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|beanio
operator|.
name|UnidentifiedRecordException
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
comment|/**  * A {@link BeanReaderErrorHandler} to handle errors during parsing. This error handler is prototype scoped  * and provides access to the current {@link Exchange}.  * You can perform any custom initialization logic in the {@link #init()} method.  */
end_comment

begin_class
DECL|class|BeanIOErrorHandler
specifier|public
class|class
name|BeanIOErrorHandler
extends|extends
name|BeanReaderErrorHandlerSupport
block|{
DECL|field|LOG_PREFIX
specifier|static
specifier|final
name|String
name|LOG_PREFIX
init|=
literal|"BeanIO: "
decl_stmt|;
DECL|field|LOG
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BeanIOErrorHandler
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|configuration
specifier|private
name|BeanIOConfiguration
name|configuration
decl_stmt|;
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
decl_stmt|;
DECL|field|results
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|results
decl_stmt|;
DECL|field|iterator
specifier|private
name|BeanIOIterator
name|iterator
decl_stmt|;
DECL|method|BeanIOErrorHandler ()
specifier|public
name|BeanIOErrorHandler
parameter_list|()
block|{     }
DECL|method|init ()
specifier|public
name|void
name|init
parameter_list|()
block|{
comment|// any custom init code here
block|}
comment|/**      * The configuration      */
DECL|method|getConfiguration ()
specifier|public
name|BeanIOConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (BeanIOConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|BeanIOConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
comment|/**      * The current exchange      */
DECL|method|getExchange ()
specifier|public
name|Exchange
name|getExchange
parameter_list|()
block|{
return|return
name|exchange
return|;
block|}
DECL|method|setExchange (Exchange exchange)
specifier|public
name|void
name|setExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
block|}
DECL|method|setResults (List<Object> results)
name|void
name|setResults
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|results
parameter_list|)
block|{
name|this
operator|.
name|results
operator|=
name|results
expr_stmt|;
block|}
DECL|method|setIterator (BeanIOIterator iterator)
name|void
name|setIterator
parameter_list|(
name|BeanIOIterator
name|iterator
parameter_list|)
block|{
name|this
operator|.
name|iterator
operator|=
name|iterator
expr_stmt|;
block|}
comment|/**      * Sets a custom POJO as the result from handling an beanio error.      */
DECL|method|handleErrorAndAddAsResult (Object result)
specifier|public
name|void
name|handleErrorAndAddAsResult
parameter_list|(
name|Object
name|result
parameter_list|)
block|{
if|if
condition|(
name|results
operator|!=
literal|null
condition|)
block|{
name|results
operator|.
name|add
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|iterator
operator|!=
literal|null
condition|)
block|{
name|iterator
operator|.
name|setNext
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|invalidRecord (InvalidRecordException ex)
specifier|public
name|void
name|invalidRecord
parameter_list|(
name|InvalidRecordException
name|ex
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|msg
init|=
name|LOG_PREFIX
operator|+
literal|"InvalidRecord: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
operator|+
literal|": "
operator|+
name|ex
operator|.
name|getRecordContext
argument_list|()
operator|.
name|getRecordText
argument_list|()
decl_stmt|;
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|isIgnoreInvalidRecords
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|msg
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|unexpectedRecord (UnexpectedRecordException ex)
specifier|public
name|void
name|unexpectedRecord
parameter_list|(
name|UnexpectedRecordException
name|ex
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|msg
init|=
name|LOG_PREFIX
operator|+
literal|"UnexpectedRecord: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
operator|+
literal|": "
operator|+
name|ex
operator|.
name|getRecordContext
argument_list|()
operator|.
name|getRecordText
argument_list|()
decl_stmt|;
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|isIgnoreUnexpectedRecords
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|msg
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|unidentifiedRecord (UnidentifiedRecordException ex)
specifier|public
name|void
name|unidentifiedRecord
parameter_list|(
name|UnidentifiedRecordException
name|ex
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|msg
init|=
name|LOG_PREFIX
operator|+
literal|"UnidentifiedRecord: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
operator|+
literal|": "
operator|+
name|ex
operator|.
name|getRecordContext
argument_list|()
operator|.
name|getRecordText
argument_list|()
decl_stmt|;
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|isIgnoreUnidentifiedRecords
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|msg
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
block|}
end_class

end_unit

