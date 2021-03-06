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
name|NoSuchBeanException
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
name|AsyncProcessorSupport
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
name|service
operator|.
name|ServiceHelper
import|;
end_import

begin_comment
comment|/**  * A {@link Processor} which converts the inbound exchange to a method  * invocation on a POJO  */
end_comment

begin_class
DECL|class|AbstractBeanProcessor
specifier|public
specifier|abstract
class|class
name|AbstractBeanProcessor
extends|extends
name|AsyncProcessorSupport
block|{
DECL|field|beanHolder
specifier|private
specifier|final
name|BeanHolder
name|beanHolder
decl_stmt|;
DECL|field|processor
specifier|private
specifier|transient
name|Processor
name|processor
decl_stmt|;
DECL|field|lookupProcessorDone
specifier|private
specifier|transient
name|boolean
name|lookupProcessorDone
decl_stmt|;
DECL|field|lock
specifier|private
specifier|final
name|Object
name|lock
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
DECL|field|cache
specifier|private
name|Boolean
name|cache
decl_stmt|;
DECL|field|method
specifier|private
name|String
name|method
decl_stmt|;
DECL|field|shorthandMethod
specifier|private
name|boolean
name|shorthandMethod
decl_stmt|;
DECL|method|AbstractBeanProcessor (Object pojo, BeanInfo beanInfo)
specifier|public
name|AbstractBeanProcessor
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
DECL|method|AbstractBeanProcessor (Object pojo, CamelContext camelContext, ParameterMappingStrategy parameterMappingStrategy)
specifier|public
name|AbstractBeanProcessor
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
DECL|method|AbstractBeanProcessor (Object pojo, CamelContext camelContext)
specifier|public
name|AbstractBeanProcessor
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
DECL|method|AbstractBeanProcessor (BeanHolder beanHolder)
specifier|public
name|AbstractBeanProcessor
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
return|return
literal|"BeanProcessor["
operator|+
name|beanHolder
operator|+
operator|(
name|method
operator|!=
literal|null
condition|?
literal|"#"
operator|+
name|method
else|:
literal|""
operator|)
operator|+
literal|"]"
return|;
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
comment|// do we have an explicit method name we always should invoke (either configured on endpoint or as a header)
name|String
name|explicitMethodName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|BEAN_METHOD_NAME
argument_list|,
name|method
argument_list|,
name|String
operator|.
name|class
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
comment|// get bean info for this bean instance (to avoid thread issue)
name|beanInfo
operator|=
name|beanHolder
operator|.
name|getBeanInfo
argument_list|(
name|bean
argument_list|)
expr_stmt|;
if|if
condition|(
name|beanInfo
operator|==
literal|null
condition|)
block|{
comment|// fallback and use old way
name|beanInfo
operator|=
name|beanHolder
operator|.
name|getBeanInfo
argument_list|()
expr_stmt|;
block|}
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
comment|// but only do this if allowed
comment|// we need to check beanHolder is Processor is support, to avoid the bean cached issue
if|if
condition|(
name|allowProcessor
argument_list|(
name|explicitMethodName
argument_list|,
name|beanInfo
argument_list|)
condition|)
block|{
name|Processor
name|target
init|=
name|getProcessor
argument_list|()
decl_stmt|;
if|if
condition|(
name|target
operator|==
literal|null
condition|)
block|{
comment|// only attempt to lookup the processor once or nearly once
name|boolean
name|allowCache
init|=
name|cache
operator|==
literal|null
operator|||
name|cache
decl_stmt|;
comment|// allow cache by default
if|if
condition|(
name|allowCache
condition|)
block|{
if|if
condition|(
operator|!
name|lookupProcessorDone
condition|)
block|{
synchronized|synchronized
init|(
name|lock
init|)
block|{
name|lookupProcessorDone
operator|=
literal|true
expr_stmt|;
comment|// so if there is a custom type converter for the bean to processor
name|target
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|tryConvertTo
argument_list|(
name|Processor
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|bean
argument_list|)
expr_stmt|;
name|processor
operator|=
name|target
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|// so if there is a custom type converter for the bean to processor
name|target
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|tryConvertTo
argument_list|(
name|Processor
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|bean
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|target
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Using a custom adapter as bean invocation: {}"
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|target
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
block|}
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
comment|// set explicit method name to invoke as a header, which is how BeanInfo can detect it
if|if
condition|(
name|explicitMethodName
operator|!=
literal|null
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
name|explicitMethodName
argument_list|)
expr_stmt|;
block|}
name|MethodInvocation
name|invocation
decl_stmt|;
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
finally|finally
block|{
comment|// must remove headers as they were provisional
if|if
condition|(
name|explicitMethodName
operator|!=
literal|null
condition|)
block|{
name|in
operator|.
name|removeHeader
argument_list|(
name|Exchange
operator|.
name|BEAN_METHOD_NAME
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|invocation
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|IllegalStateException
argument_list|(
literal|"No method invocation could be created, no matching method could be found on: "
operator|+
name|bean
argument_list|)
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
comment|// invoke invocation
return|return
name|invocation
operator|.
name|proceed
argument_list|(
name|callback
argument_list|)
return|;
block|}
DECL|method|getProcessor ()
specifier|protected
name|Processor
name|getProcessor
parameter_list|()
block|{
return|return
name|processor
return|;
block|}
DECL|method|getBeanHolder ()
specifier|protected
name|BeanHolder
name|getBeanHolder
parameter_list|()
block|{
return|return
name|this
operator|.
name|beanHolder
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
DECL|method|getCache ()
specifier|public
name|Boolean
name|getCache
parameter_list|()
block|{
return|return
name|cache
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
name|this
operator|.
name|cache
operator|=
name|cache
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
comment|// optimize to only get (create) a processor if really needed
if|if
condition|(
name|beanHolder
operator|.
name|supportProcessor
argument_list|()
operator|&&
name|allowProcessor
argument_list|(
name|method
argument_list|,
name|beanHolder
operator|.
name|getBeanInfo
argument_list|()
argument_list|)
condition|)
block|{
name|processor
operator|=
name|beanHolder
operator|.
name|getProcessor
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|beanHolder
operator|instanceof
name|ConstantBeanHolder
condition|)
block|{
try|try
block|{
comment|// Start the bean if it implements Service interface and if cached
comment|// so meant to be reused
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|beanHolder
operator|.
name|getBean
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchBeanException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
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
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|beanHolder
operator|instanceof
name|ConstantBeanHolder
condition|)
block|{
try|try
block|{
comment|// Stop the bean if it implements Service interface and if cached
comment|// so meant to be reused
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|beanHolder
operator|.
name|getBean
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchBeanException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
DECL|method|allowProcessor (String explicitMethodName, BeanInfo info)
specifier|private
name|boolean
name|allowProcessor
parameter_list|(
name|String
name|explicitMethodName
parameter_list|,
name|BeanInfo
name|info
parameter_list|)
block|{
if|if
condition|(
name|explicitMethodName
operator|!=
literal|null
condition|)
block|{
comment|// don't allow if explicit method name is given, as we then must invoke this method
return|return
literal|false
return|;
block|}
comment|// don't allow if any of the methods has a @Handler annotation
comment|// as the @Handler annotation takes precedence and is supposed to trigger invocation
comment|// of the given method
if|if
condition|(
name|info
operator|.
name|hasAnyMethodHandlerAnnotation
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// fallback and allow using the processor
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

