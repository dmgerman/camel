begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|lang
operator|.
name|reflect
operator|.
name|InvocationHandler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|Producer
import|;
end_import

begin_comment
comment|/**  * An {@link java.lang.reflect.InvocationHandler} which invokes a message  * exchange on a camel {@link Endpoint}  */
end_comment

begin_class
DECL|class|CamelInvocationHandler
specifier|public
class|class
name|CamelInvocationHandler
extends|extends
name|AbstractCamelInvocationHandler
implements|implements
name|InvocationHandler
block|{
DECL|field|methodInfoCache
specifier|private
specifier|final
name|MethodInfoCache
name|methodInfoCache
decl_stmt|;
DECL|field|binding
specifier|private
specifier|final
name|boolean
name|binding
decl_stmt|;
DECL|method|CamelInvocationHandler (Endpoint endpoint, boolean binding, Producer producer, MethodInfoCache methodInfoCache)
specifier|public
name|CamelInvocationHandler
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|boolean
name|binding
parameter_list|,
name|Producer
name|producer
parameter_list|,
name|MethodInfoCache
name|methodInfoCache
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|)
expr_stmt|;
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
name|this
operator|.
name|methodInfoCache
operator|=
name|methodInfoCache
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doInvokeProxy (Object proxy, Method method, Object[] args)
specifier|public
name|Object
name|doInvokeProxy
parameter_list|(
name|Object
name|proxy
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
block|{
name|MethodInfo
name|methodInfo
init|=
name|methodInfoCache
operator|.
name|getMethodInfo
argument_list|(
name|method
argument_list|)
decl_stmt|;
specifier|final
name|ExchangePattern
name|pattern
init|=
name|methodInfo
operator|!=
literal|null
condition|?
name|methodInfo
operator|.
name|getPattern
argument_list|()
else|:
name|ExchangePattern
operator|.
name|InOut
decl_stmt|;
return|return
name|invokeProxy
argument_list|(
name|method
argument_list|,
name|pattern
argument_list|,
name|args
argument_list|,
name|binding
argument_list|)
return|;
block|}
block|}
end_class

end_unit

