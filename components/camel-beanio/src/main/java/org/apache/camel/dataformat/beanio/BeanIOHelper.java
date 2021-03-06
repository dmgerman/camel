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
name|beanio
operator|.
name|BeanReaderErrorHandler
import|;
end_import

begin_comment
comment|/**  * Helper class  */
end_comment

begin_class
DECL|class|BeanIOHelper
specifier|public
specifier|final
class|class
name|BeanIOHelper
block|{
DECL|method|BeanIOHelper ()
specifier|private
name|BeanIOHelper
parameter_list|()
block|{
comment|// utility class
block|}
DECL|method|getOrCreateBeanReaderErrorHandler (BeanIOConfiguration configuration, Exchange exchange, List<Object> results, BeanIOIterator iterator)
specifier|public
specifier|static
name|BeanReaderErrorHandler
name|getOrCreateBeanReaderErrorHandler
parameter_list|(
name|BeanIOConfiguration
name|configuration
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|results
parameter_list|,
name|BeanIOIterator
name|iterator
parameter_list|)
throws|throws
name|Exception
block|{
name|BeanReaderErrorHandler
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getBeanReaderErrorHandlerType
argument_list|()
argument_list|)
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|configuration
operator|.
name|getBeanReaderErrorHandlerType
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|instance
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
name|answer
operator|=
operator|(
name|BeanReaderErrorHandler
operator|)
name|instance
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getBeanReaderErrorHandler
argument_list|()
argument_list|)
condition|)
block|{
name|answer
operator|=
name|configuration
operator|.
name|getBeanReaderErrorHandler
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
operator|new
name|BeanIOErrorHandler
argument_list|()
expr_stmt|;
block|}
comment|// if the error handler extends BeanIOErrorHandler then its prototype scoped
comment|// and then inject the current exchange and init
if|if
condition|(
name|answer
operator|instanceof
name|BeanIOErrorHandler
condition|)
block|{
name|BeanIOErrorHandler
name|eh
init|=
operator|(
name|BeanIOErrorHandler
operator|)
name|answer
decl_stmt|;
name|eh
operator|.
name|setConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|eh
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|eh
operator|.
name|setResults
argument_list|(
name|results
argument_list|)
expr_stmt|;
name|eh
operator|.
name|setIterator
argument_list|(
name|iterator
argument_list|)
expr_stmt|;
name|eh
operator|.
name|init
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

