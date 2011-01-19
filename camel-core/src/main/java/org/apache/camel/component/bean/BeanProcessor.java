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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
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
name|Message
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
name|impl
operator|.
name|ServiceSupport
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
name|AsyncProcessorHelper
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ServiceHelper
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
comment|/**  * A {@link Processor} which converts the inbound exchange to a method  * invocation on a POJO  *  * @version $Revision$  */
end_comment

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
name|BeanProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|multiParameterArray
specifier|private
name|boolean
name|multiParameterArray
decl_stmt|;
DECL|field|methodObject
specifier|private
name|Method
name|methodObject
decl_stmt|;
DECL|field|method
specifier|private
name|String
name|method
decl_stmt|;
DECL|field|beanHolder
specifier|private
name|BeanHolder
name|beanHolder
decl_stmt|;
DECL|field|shorthandMethod
specifier|private
name|boolean
name|shorthandMethod
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
argument_list|(
operator|new
name|ConstantBeanHolder
argument_list|(
name|pojo
argument_list|,
name|beanInfo
argument_list|)
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
argument_list|(
name|pojo
argument_list|,
operator|new
name|BeanInfo
argument_list|(
name|camelContext
argument_list|,
name|pojo
operator|.
name|getClass
argument_list|()
argument_list|,
name|parameterMappingStrategy
argument_list|)
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
argument_list|(
name|pojo
argument_list|,
name|camelContext
argument_list|,
name|BeanInfo
operator|.
name|createParameterMappingStrategy
argument_list|(
name|camelContext
argument_list|)
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
name|beanHolder
operator|=
name|beanHolder
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|String
name|description
init|=
name|methodObject
operator|!=
literal|null
condition|?
literal|" "
operator|+
name|methodObject
else|:
literal|""
decl_stmt|;
return|return
literal|"BeanProcessor["
operator|+
name|beanHolder
operator|+
name|description
operator|+
literal|"]"
return|;
block|}
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
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
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
comment|// do we have an explicit method name we always should invoke
name|boolean
name|isExplicitMethod
init|=
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|method
argument_list|)
decl_stmt|;
name|Object
name|bean
decl_stmt|;
name|BeanInfo
name|beanInfo
decl_stmt|;
try|try
block|{
name|bean
operator|=
name|beanHolder
operator|.
name|getBean
argument_list|()
expr_stmt|;
name|beanInfo
operator|=
name|beanHolder
operator|.
name|getBeanInfo
argument_list|()
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
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// do we have a custom adapter for this POJO to a Processor
comment|// should not be invoked if an explicit method has been set
name|Processor
name|processor
init|=
name|getProcessor
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|isExplicitMethod
operator|&&
name|processor
operator|!=
literal|null
condition|)
block|{
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
literal|"Using a custom adapter as bean invocation: "
operator|+
name|processor
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|processor
operator|.
name|process
argument_list|(
name|exchange
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
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
comment|// Now it gets a bit complicated as ProxyHelper can proxy beans which we later
comment|// intend to invoke (for example to proxy and invoke using spring remoting).
comment|// and therefore the message body contains a BeanInvocation object.
comment|// However this can causes problem if we in a Camel route invokes another bean,
comment|// so we must test whether BeanHolder and BeanInvocation is the same bean or not
name|BeanInvocation
name|beanInvoke
init|=
name|in
operator|.
name|getBody
argument_list|(
name|BeanInvocation
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|beanInvoke
operator|!=
literal|null
condition|)
block|{
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
literal|"Exchange IN body is a BeanInvocation instance: "
operator|+
name|beanInvoke
argument_list|)
expr_stmt|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|beanInvoke
operator|.
name|getMethod
argument_list|()
operator|.
name|getDeclaringClass
argument_list|()
decl_stmt|;
name|boolean
name|sameBean
init|=
name|clazz
operator|.
name|isInstance
argument_list|(
name|bean
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
name|debug
argument_list|(
literal|"BeanHolder bean: "
operator|+
name|bean
operator|.
name|getClass
argument_list|()
operator|+
literal|" and beanInvocation bean: "
operator|+
name|clazz
operator|+
literal|" is same instance: "
operator|+
name|sameBean
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sameBean
condition|)
block|{
name|beanInvoke
operator|.
name|invoke
argument_list|(
name|bean
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// propagate headers
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|putAll
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
comment|// set temporary header which is a hint for the bean info that introspect the bean
if|if
condition|(
name|in
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|BEAN_MULTI_PARAMETER_ARRAY
argument_list|)
operator|==
literal|null
condition|)
block|{
name|in
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|BEAN_MULTI_PARAMETER_ARRAY
argument_list|,
name|isMultiParameterArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|prevMethod
init|=
literal|null
decl_stmt|;
name|MethodInvocation
name|invocation
decl_stmt|;
if|if
condition|(
name|methodObject
operator|!=
literal|null
condition|)
block|{
name|invocation
operator|=
name|beanInfo
operator|.
name|createInvocation
argument_list|(
name|methodObject
argument_list|,
name|bean
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// we just override the bean's invocation method name here
if|if
condition|(
name|isExplicitMethod
condition|)
block|{
name|prevMethod
operator|=
name|in
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|BEAN_METHOD_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|BEAN_METHOD_NAME
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|invocation
operator|=
name|beanInfo
operator|.
name|createInvocation
argument_list|(
name|bean
argument_list|,
name|exchange
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
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
if|if
condition|(
name|invocation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No method invocation could be created, no matching method could be found on: "
operator|+
name|bean
argument_list|)
throw|;
block|}
comment|// remove temporary header
name|in
operator|.
name|removeHeader
argument_list|(
name|Exchange
operator|.
name|BEAN_MULTI_PARAMETER_ARRAY
argument_list|)
expr_stmt|;
name|Object
name|value
init|=
literal|null
decl_stmt|;
try|try
block|{
name|AtomicBoolean
name|sync
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|value
operator|=
name|invocation
operator|.
name|proceed
argument_list|(
name|callback
argument_list|,
name|sync
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|sync
operator|.
name|get
argument_list|()
condition|)
block|{
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
literal|"Processing exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" is continued being processed asynchronously"
argument_list|)
expr_stmt|;
block|}
comment|// the remainder of the routing will be completed async
comment|// so we break out now, then the callback will be invoked which then continue routing from where we left here
return|return
literal|false
return|;
block|}
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
literal|"Processing exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" is continued being processed synchronously"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
comment|// lets unwrap the exception when its an invocation target exception
name|exchange
operator|.
name|setException
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
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
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|isExplicitMethod
condition|)
block|{
name|in
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|BEAN_METHOD_NAME
argument_list|,
name|prevMethod
argument_list|)
expr_stmt|;
block|}
block|}
comment|// if the method returns something then set the value returned on the Exchange
if|if
condition|(
operator|!
name|invocation
operator|.
name|getMethod
argument_list|()
operator|.
name|getReturnType
argument_list|()
operator|.
name|equals
argument_list|(
name|Void
operator|.
name|TYPE
argument_list|)
operator|&&
name|value
operator|!=
name|Void
operator|.
name|TYPE
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
comment|// force out creating if not already created (as its lazy)
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Setting bean invocation result on the OUT message: "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|value
argument_list|)
expr_stmt|;
comment|// propagate headers
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|putAll
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// if not out then set it on the in
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Setting bean invocation result on the IN message: "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|getProcessor ()
specifier|protected
name|Processor
name|getProcessor
parameter_list|()
block|{
return|return
name|beanHolder
operator|.
name|getProcessor
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
name|beanHolder
operator|.
name|getBean
argument_list|()
return|;
block|}
comment|// Properties
comment|// -----------------------------------------------------------------------
DECL|method|getMethodObject ()
specifier|public
name|Method
name|getMethodObject
parameter_list|()
block|{
return|return
name|methodObject
return|;
block|}
DECL|method|setMethodObject (Method methodObject)
specifier|public
name|void
name|setMethodObject
parameter_list|(
name|Method
name|methodObject
parameter_list|)
block|{
name|this
operator|.
name|methodObject
operator|=
name|methodObject
expr_stmt|;
block|}
DECL|method|getMethod ()
specifier|public
name|String
name|getMethod
parameter_list|()
block|{
return|return
name|method
return|;
block|}
DECL|method|isMultiParameterArray ()
specifier|public
name|boolean
name|isMultiParameterArray
parameter_list|()
block|{
return|return
name|multiParameterArray
return|;
block|}
DECL|method|setMultiParameterArray (boolean mpArray)
specifier|public
name|void
name|setMultiParameterArray
parameter_list|(
name|boolean
name|mpArray
parameter_list|)
block|{
name|multiParameterArray
operator|=
name|mpArray
expr_stmt|;
block|}
comment|/**      * Sets the method name to use      */
DECL|method|setMethod (String method)
specifier|public
name|void
name|setMethod
parameter_list|(
name|String
name|method
parameter_list|)
block|{
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
block|}
DECL|method|isShorthandMethod ()
specifier|public
name|boolean
name|isShorthandMethod
parameter_list|()
block|{
return|return
name|shorthandMethod
return|;
block|}
comment|/**      * Sets whether to support getter style method name, so you can      * say the method is called 'name' but it will invoke the 'getName' method.      *<p/>      * Is by default turned off.      */
DECL|method|setShorthandMethod (boolean shorthandMethod)
specifier|public
name|void
name|setShorthandMethod
parameter_list|(
name|boolean
name|shorthandMethod
parameter_list|)
block|{
name|this
operator|.
name|shorthandMethod
operator|=
name|shorthandMethod
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|getProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|getProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

