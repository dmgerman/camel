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
name|InvocationTargetException
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RuntimeCamelException
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
name|impl
operator|.
name|DefaultExchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * An {@link java.lang.reflect.InvocationHandler} which invokes a  * message exchange on a camel {@link Endpoint}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|CamelInvocationHandler
specifier|public
class|class
name|CamelInvocationHandler
implements|implements
name|InvocationHandler
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|CamelInvocationHandler
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|producer
specifier|private
specifier|final
name|Producer
name|producer
decl_stmt|;
DECL|field|methodInfoCache
specifier|private
specifier|final
name|MethodInfoCache
name|methodInfoCache
decl_stmt|;
DECL|method|CamelInvocationHandler (Endpoint endpoint, Producer producer, MethodInfoCache methodInfoCache)
specifier|public
name|CamelInvocationHandler
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Producer
name|producer
parameter_list|,
name|MethodInfoCache
name|methodInfoCache
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
name|producer
operator|=
name|producer
expr_stmt|;
name|this
operator|.
name|methodInfoCache
operator|=
name|methodInfoCache
expr_stmt|;
block|}
DECL|method|invoke (Object proxy, Method method, Object[] args)
specifier|public
name|Object
name|invoke
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
name|BeanInvocation
name|invocation
init|=
operator|new
name|BeanInvocation
argument_list|(
name|method
argument_list|,
name|args
argument_list|)
decl_stmt|;
name|ExchangePattern
name|pattern
init|=
name|ExchangePattern
operator|.
name|InOut
decl_stmt|;
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
if|if
condition|(
name|methodInfo
operator|!=
literal|null
condition|)
block|{
name|pattern
operator|=
name|methodInfo
operator|.
name|getPattern
argument_list|()
expr_stmt|;
block|}
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|endpoint
argument_list|,
name|pattern
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|invocation
argument_list|)
expr_stmt|;
comment|// process the exchange
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Proxied method call "
operator|+
name|method
operator|.
name|getName
argument_list|()
operator|+
literal|" invoking producer: "
operator|+
name|producer
argument_list|)
expr_stmt|;
block|}
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// check if we had an exception
name|Throwable
name|fault
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|fault
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|fault
operator|instanceof
name|RuntimeCamelException
condition|)
block|{
comment|// if the inner cause is a runtime exception we can throw it directly
if|if
condition|(
name|fault
operator|.
name|getCause
argument_list|()
operator|instanceof
name|RuntimeException
condition|)
block|{
throw|throw
call|(
name|RuntimeException
call|)
argument_list|(
operator|(
name|RuntimeCamelException
operator|)
name|fault
argument_list|)
operator|.
name|getCause
argument_list|()
throw|;
block|}
throw|throw
operator|(
name|RuntimeCamelException
operator|)
name|fault
throw|;
block|}
throw|throw
operator|new
name|InvocationTargetException
argument_list|(
name|fault
argument_list|)
throw|;
block|}
comment|// do not return a reply if the method is VOID or the MEP is not OUT capable
name|Class
argument_list|<
name|?
argument_list|>
name|to
init|=
name|method
operator|.
name|getReturnType
argument_list|()
decl_stmt|;
if|if
condition|(
name|to
operator|==
name|Void
operator|.
name|TYPE
operator|||
operator|!
name|pattern
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// use type converter so we can convert output in the desired type defined by the method
comment|// and let it be mandatory so we know wont return null if we cant convert it to the defined type
name|Object
name|answer
init|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|to
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Proxied method call "
operator|+
name|method
operator|.
name|getName
argument_list|()
operator|+
literal|" returning: "
operator|+
name|answer
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

