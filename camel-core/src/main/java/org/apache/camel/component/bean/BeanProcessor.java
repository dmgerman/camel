begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CompletableFuture
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
name|support
operator|.
name|ServiceSupport
import|;
end_import

begin_class
DECL|class|BeanProcessor
specifier|public
class|class
name|BeanProcessor
extends|extends
name|ServiceSupport
implements|implements
name|AsyncProcessor
block|{
DECL|field|delegate
specifier|private
specifier|final
name|DelegateBeanProcessor
name|delegate
decl_stmt|;
DECL|method|BeanProcessor (Object pojo, BeanInfo beanInfo)
specifier|public
name|BeanProcessor
parameter_list|(
name|Object
name|pojo
parameter_list|,
name|BeanInfo
name|beanInfo
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
operator|new
name|DelegateBeanProcessor
argument_list|(
name|pojo
argument_list|,
name|beanInfo
argument_list|)
expr_stmt|;
block|}
DECL|method|BeanProcessor (Object pojo, CamelContext camelContext, ParameterMappingStrategy parameterMappingStrategy)
specifier|public
name|BeanProcessor
parameter_list|(
name|Object
name|pojo
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|,
name|ParameterMappingStrategy
name|parameterMappingStrategy
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
operator|new
name|DelegateBeanProcessor
argument_list|(
name|pojo
argument_list|,
name|camelContext
argument_list|,
name|parameterMappingStrategy
argument_list|)
expr_stmt|;
block|}
DECL|method|BeanProcessor (Object pojo, CamelContext camelContext)
specifier|public
name|BeanProcessor
parameter_list|(
name|Object
name|pojo
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
operator|new
name|DelegateBeanProcessor
argument_list|(
name|pojo
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
block|}
DECL|method|BeanProcessor (BeanHolder beanHolder)
specifier|public
name|BeanProcessor
parameter_list|(
name|BeanHolder
name|beanHolder
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
operator|new
name|DelegateBeanProcessor
argument_list|(
name|beanHolder
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|delegate
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|processAsync (Exchange exchange)
specifier|public
name|CompletableFuture
argument_list|<
name|Exchange
argument_list|>
name|processAsync
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|processAsync
argument_list|(
name|exchange
argument_list|)
return|;
block|}
DECL|method|getProcessor ()
specifier|public
name|Processor
name|getProcessor
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getProcessor
argument_list|()
return|;
block|}
DECL|method|getBeanHolder ()
specifier|public
name|BeanHolder
name|getBeanHolder
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getBeanHolder
argument_list|()
return|;
block|}
DECL|method|getBean ()
specifier|public
name|Object
name|getBean
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getBean
argument_list|()
return|;
block|}
DECL|method|getMethod ()
specifier|public
name|String
name|getMethod
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getMethod
argument_list|()
return|;
block|}
DECL|method|getCache ()
specifier|public
name|Boolean
name|getCache
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getCache
argument_list|()
return|;
block|}
DECL|method|setCache (Boolean cache)
specifier|public
name|void
name|setCache
parameter_list|(
name|Boolean
name|cache
parameter_list|)
block|{
name|delegate
operator|.
name|setCache
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
DECL|method|setMethod (String method)
specifier|public
name|void
name|setMethod
parameter_list|(
name|String
name|method
parameter_list|)
block|{
name|delegate
operator|.
name|setMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
DECL|method|isShorthandMethod ()
specifier|public
name|boolean
name|isShorthandMethod
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|isShorthandMethod
argument_list|()
return|;
block|}
DECL|method|setShorthandMethod (boolean shorthandMethod)
specifier|public
name|void
name|setShorthandMethod
parameter_list|(
name|boolean
name|shorthandMethod
parameter_list|)
block|{
name|delegate
operator|.
name|setShorthandMethod
argument_list|(
name|shorthandMethod
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|delegate
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|delegate
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|class|DelegateBeanProcessor
specifier|private
specifier|static
specifier|final
class|class
name|DelegateBeanProcessor
extends|extends
name|AbstractBeanProcessor
block|{
DECL|method|DelegateBeanProcessor (Object pojo, BeanInfo beanInfo)
specifier|public
name|DelegateBeanProcessor
parameter_list|(
name|Object
name|pojo
parameter_list|,
name|BeanInfo
name|beanInfo
parameter_list|)
block|{
name|super
argument_list|(
name|pojo
argument_list|,
name|beanInfo
argument_list|)
expr_stmt|;
block|}
DECL|method|DelegateBeanProcessor (Object pojo, CamelContext camelContext, ParameterMappingStrategy parameterMappingStrategy)
specifier|public
name|DelegateBeanProcessor
parameter_list|(
name|Object
name|pojo
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|,
name|ParameterMappingStrategy
name|parameterMappingStrategy
parameter_list|)
block|{
name|super
argument_list|(
name|pojo
argument_list|,
name|camelContext
argument_list|,
name|parameterMappingStrategy
argument_list|)
expr_stmt|;
block|}
DECL|method|DelegateBeanProcessor (Object pojo, CamelContext camelContext)
specifier|public
name|DelegateBeanProcessor
parameter_list|(
name|Object
name|pojo
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
argument_list|(
name|pojo
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
block|}
DECL|method|DelegateBeanProcessor (BeanHolder beanHolder)
specifier|public
name|DelegateBeanProcessor
parameter_list|(
name|BeanHolder
name|beanHolder
parameter_list|)
block|{
name|super
argument_list|(
name|beanHolder
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

