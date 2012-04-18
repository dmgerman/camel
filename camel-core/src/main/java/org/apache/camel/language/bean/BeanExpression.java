begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|Expression
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
name|ExpressionIllegalSyntaxException
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
name|Predicate
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
name|component
operator|.
name|bean
operator|.
name|BeanHolder
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
name|component
operator|.
name|bean
operator|.
name|BeanProcessor
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
name|component
operator|.
name|bean
operator|.
name|ConstantBeanHolder
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
name|component
operator|.
name|bean
operator|.
name|ConstantTypeBeanHolder
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
name|component
operator|.
name|bean
operator|.
name|RegistryBean
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
name|KeyValueHolder
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
name|OgnlHelper
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
name|StringHelper
import|;
end_import

begin_comment
comment|/**  * Evaluates an expression using a bean method invocation  *  * @version   */
end_comment

begin_class
DECL|class|BeanExpression
specifier|public
class|class
name|BeanExpression
implements|implements
name|Expression
implements|,
name|Predicate
block|{
DECL|field|bean
specifier|private
name|Object
name|bean
decl_stmt|;
DECL|field|beanName
specifier|private
name|String
name|beanName
decl_stmt|;
DECL|field|type
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|type
decl_stmt|;
DECL|field|method
specifier|private
name|String
name|method
decl_stmt|;
DECL|method|BeanExpression (Object bean, String method)
specifier|public
name|BeanExpression
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|method
parameter_list|)
block|{
name|this
operator|.
name|bean
operator|=
name|bean
expr_stmt|;
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
block|}
DECL|method|BeanExpression (String beanName, String method)
specifier|public
name|BeanExpression
parameter_list|(
name|String
name|beanName
parameter_list|,
name|String
name|method
parameter_list|)
block|{
name|this
operator|.
name|beanName
operator|=
name|beanName
expr_stmt|;
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
block|}
DECL|method|BeanExpression (Class<?> type, String method)
specifier|public
name|BeanExpression
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|String
name|method
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|method
operator|=
name|method
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
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"BeanExpression["
argument_list|)
decl_stmt|;
if|if
condition|(
name|bean
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|bean
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|beanName
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|beanName
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|ObjectHelper
operator|.
name|className
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" method: "
argument_list|)
operator|.
name|append
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|evaluate (Exchange exchange)
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// either use registry lookup or a constant bean
name|BeanHolder
name|holder
decl_stmt|;
if|if
condition|(
name|bean
operator|!=
literal|null
condition|)
block|{
name|holder
operator|=
operator|new
name|ConstantBeanHolder
argument_list|(
name|bean
argument_list|,
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|beanName
operator|!=
literal|null
condition|)
block|{
name|holder
operator|=
operator|new
name|RegistryBean
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|beanName
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|holder
operator|=
operator|new
name|ConstantTypeBeanHolder
argument_list|(
name|type
argument_list|,
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Either bean, beanName or type should be set on "
operator|+
name|this
argument_list|)
throw|;
block|}
comment|// invoking the bean can either be the easy way or using OGNL
comment|// validate OGNL
if|if
condition|(
name|OgnlHelper
operator|.
name|isInvalidValidOgnlExpression
argument_list|(
name|method
argument_list|)
condition|)
block|{
name|ExpressionIllegalSyntaxException
name|cause
init|=
operator|new
name|ExpressionIllegalSyntaxException
argument_list|(
name|method
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|RuntimeBeanExpressionException
argument_list|(
name|exchange
argument_list|,
name|beanName
argument_list|,
name|method
argument_list|,
name|cause
argument_list|)
throw|;
block|}
if|if
condition|(
name|OgnlHelper
operator|.
name|isValidOgnlExpression
argument_list|(
name|method
argument_list|)
condition|)
block|{
comment|// okay the method is an ognl expression
name|OgnlInvokeProcessor
name|ognl
init|=
operator|new
name|OgnlInvokeProcessor
argument_list|(
name|holder
argument_list|,
name|method
argument_list|)
decl_stmt|;
try|try
block|{
name|ognl
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return
name|ognl
operator|.
name|getResult
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeBeanExpressionException
argument_list|(
name|exchange
argument_list|,
name|beanName
argument_list|,
name|method
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
comment|// regular non ognl invocation
name|InvokeProcessor
name|invoke
init|=
operator|new
name|InvokeProcessor
argument_list|(
name|holder
argument_list|,
name|method
argument_list|)
decl_stmt|;
try|try
block|{
name|invoke
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return
name|invoke
operator|.
name|getResult
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeBeanExpressionException
argument_list|(
name|exchange
argument_list|,
name|beanName
argument_list|,
name|method
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|evaluate (Exchange exchange, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Object
name|result
init|=
name|evaluate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
return|return
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
name|type
argument_list|,
name|result
argument_list|)
return|;
block|}
DECL|method|matches (Exchange exchange)
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|value
init|=
name|evaluate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
return|return
name|ObjectHelper
operator|.
name|evaluateValuePredicate
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**      * Invokes a given bean holder. The method name is optional.      */
DECL|class|InvokeProcessor
specifier|private
specifier|final
class|class
name|InvokeProcessor
implements|implements
name|Processor
block|{
DECL|field|beanHolder
specifier|private
name|BeanHolder
name|beanHolder
decl_stmt|;
DECL|field|methodName
specifier|private
name|String
name|methodName
decl_stmt|;
DECL|field|result
specifier|private
name|Object
name|result
decl_stmt|;
DECL|method|InvokeProcessor (BeanHolder beanHolder, String methodName)
specifier|private
name|InvokeProcessor
parameter_list|(
name|BeanHolder
name|beanHolder
parameter_list|,
name|String
name|methodName
parameter_list|)
block|{
name|this
operator|.
name|beanHolder
operator|=
name|beanHolder
expr_stmt|;
name|this
operator|.
name|methodName
operator|=
name|methodName
expr_stmt|;
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
name|BeanProcessor
name|processor
init|=
operator|new
name|BeanProcessor
argument_list|(
name|beanHolder
argument_list|)
decl_stmt|;
if|if
condition|(
name|methodName
operator|!=
literal|null
condition|)
block|{
name|processor
operator|.
name|setMethod
argument_list|(
name|methodName
argument_list|)
expr_stmt|;
comment|// enable OGNL like invocation
name|processor
operator|.
name|setShorthandMethod
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
try|try
block|{
comment|// copy the original exchange to avoid side effects on it
name|Exchange
name|resultExchange
init|=
name|exchange
operator|.
name|copy
argument_list|()
decl_stmt|;
comment|// remove any existing exception in case we do OGNL on the exception
name|resultExchange
operator|.
name|setException
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// force to use InOut to retrieve the result on the OUT message
name|resultExchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|resultExchange
argument_list|)
expr_stmt|;
name|result
operator|=
name|resultExchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
comment|// propagate properties and headers from result
if|if
condition|(
name|resultExchange
operator|.
name|hasProperties
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getProperties
argument_list|()
operator|.
name|putAll
argument_list|(
name|resultExchange
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|resultExchange
operator|.
name|getOut
argument_list|()
operator|.
name|hasHeaders
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|putAll
argument_list|(
name|resultExchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// propagate exceptions
if|if
condition|(
name|resultExchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|resultExchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeBeanExpressionException
argument_list|(
name|exchange
argument_list|,
name|beanName
argument_list|,
name|methodName
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getResult ()
specifier|public
name|Object
name|getResult
parameter_list|()
block|{
return|return
name|result
return|;
block|}
block|}
comment|/**      * To invoke a bean using a OGNL notation which denotes the chain of methods to invoke.      *<p/>      * For more advanced OGNL you may have to look for a real framework such as OGNL, Mvel or dynamic      * programming language such as Groovy, JuEL, JavaScript.      */
DECL|class|OgnlInvokeProcessor
specifier|private
specifier|final
class|class
name|OgnlInvokeProcessor
implements|implements
name|Processor
block|{
DECL|field|ognl
specifier|private
specifier|final
name|String
name|ognl
decl_stmt|;
DECL|field|beanHolder
specifier|private
specifier|final
name|BeanHolder
name|beanHolder
decl_stmt|;
DECL|field|result
specifier|private
name|Object
name|result
decl_stmt|;
DECL|method|OgnlInvokeProcessor (BeanHolder beanHolder, String ognl)
specifier|public
name|OgnlInvokeProcessor
parameter_list|(
name|BeanHolder
name|beanHolder
parameter_list|,
name|String
name|ognl
parameter_list|)
block|{
name|this
operator|.
name|beanHolder
operator|=
name|beanHolder
expr_stmt|;
name|this
operator|.
name|ognl
operator|=
name|ognl
expr_stmt|;
comment|// we must start with having bean as the result
name|this
operator|.
name|result
operator|=
name|beanHolder
operator|.
name|getBean
argument_list|()
expr_stmt|;
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
comment|// copy the original exchange to avoid side effects on it
name|Exchange
name|resultExchange
init|=
name|exchange
operator|.
name|copy
argument_list|()
decl_stmt|;
comment|// remove any existing exception in case we do OGNL on the exception
name|resultExchange
operator|.
name|setException
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// force to use InOut to retrieve the result on the OUT message
name|resultExchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
comment|// do not propagate any method name when using OGNL, as with OGNL we
comment|// compute and provide the method name to explicit to invoke
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|Exchange
operator|.
name|BEAN_METHOD_NAME
argument_list|)
expr_stmt|;
comment|// current ognl path as we go along
name|String
name|ognlPath
init|=
literal|""
decl_stmt|;
comment|// loop and invoke each method
name|Object
name|beanToCall
init|=
name|beanHolder
operator|.
name|getBean
argument_list|()
decl_stmt|;
comment|// there must be a bean to call with, we currently does not support OGNL expressions on using purely static methods
if|if
condition|(
name|beanToCall
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Bean instance is null. OGNL bean expressions requires bean instances."
argument_list|)
throw|;
block|}
comment|// Split ognl except when this is not a Map, Array
comment|// and we would like to keep the dots within the key name
name|List
argument_list|<
name|String
argument_list|>
name|methods
init|=
name|OgnlHelper
operator|.
name|splitOgnl
argument_list|(
name|ognl
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|methodName
range|:
name|methods
control|)
block|{
name|BeanHolder
name|holder
init|=
operator|new
name|ConstantBeanHolder
argument_list|(
name|beanToCall
argument_list|,
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
comment|// support the null safe operator
name|boolean
name|nullSafe
init|=
name|OgnlHelper
operator|.
name|isNullSafeOperator
argument_list|(
name|methodName
argument_list|)
decl_stmt|;
comment|// keep up with how far are we doing
name|ognlPath
operator|+=
name|methodName
expr_stmt|;
comment|// get rid of leading ?. or . as we only needed that to determine if null safe was enabled or not
name|methodName
operator|=
name|OgnlHelper
operator|.
name|removeLeadingOperators
argument_list|(
name|methodName
argument_list|)
expr_stmt|;
comment|// are we doing an index lookup (eg in Map/List/array etc)?
name|String
name|key
init|=
literal|null
decl_stmt|;
name|KeyValueHolder
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|index
init|=
name|OgnlHelper
operator|.
name|isOgnlIndex
argument_list|(
name|methodName
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|!=
literal|null
condition|)
block|{
name|methodName
operator|=
name|index
operator|.
name|getKey
argument_list|()
expr_stmt|;
name|key
operator|=
name|index
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
comment|// only invoke if we have a method name to use to invoke
if|if
condition|(
name|methodName
operator|!=
literal|null
condition|)
block|{
name|InvokeProcessor
name|invoke
init|=
operator|new
name|InvokeProcessor
argument_list|(
name|holder
argument_list|,
name|methodName
argument_list|)
decl_stmt|;
name|invoke
operator|.
name|process
argument_list|(
name|resultExchange
argument_list|)
expr_stmt|;
comment|// check for exception and rethrow if we failed
if|if
condition|(
name|resultExchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeBeanExpressionException
argument_list|(
name|exchange
argument_list|,
name|beanName
argument_list|,
name|methodName
argument_list|,
name|resultExchange
operator|.
name|getException
argument_list|()
argument_list|)
throw|;
block|}
name|result
operator|=
name|invoke
operator|.
name|getResult
argument_list|()
expr_stmt|;
block|}
comment|// if there was a key then we need to lookup using the key
if|if
condition|(
name|key
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|lookupResult
argument_list|(
name|resultExchange
argument_list|,
name|key
argument_list|,
name|result
argument_list|,
name|nullSafe
argument_list|,
name|ognlPath
argument_list|,
name|holder
operator|.
name|getBean
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// check null safe for null results
if|if
condition|(
name|result
operator|==
literal|null
operator|&&
name|nullSafe
condition|)
block|{
return|return;
block|}
comment|// prepare for next bean to invoke
name|beanToCall
operator|=
name|result
expr_stmt|;
block|}
block|}
DECL|method|lookupResult (Exchange exchange, String key, Object result, boolean nullSafe, String ognlPath, Object bean)
specifier|private
name|Object
name|lookupResult
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|key
parameter_list|,
name|Object
name|result
parameter_list|,
name|boolean
name|nullSafe
parameter_list|,
name|String
name|ognlPath
parameter_list|,
name|Object
name|bean
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|key
argument_list|,
literal|"key"
argument_list|,
literal|"in Simple language ognl path: "
operator|+
name|ognlPath
argument_list|)
expr_stmt|;
comment|// trim key
name|key
operator|=
name|key
operator|.
name|trim
argument_list|()
expr_stmt|;
comment|// remove any enclosing quotes
name|key
operator|=
name|StringHelper
operator|.
name|removeLeadingAndEndingQuotes
argument_list|(
name|key
argument_list|)
expr_stmt|;
comment|// try map first
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
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
name|Map
operator|.
name|class
argument_list|,
name|result
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
condition|)
block|{
return|return
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|// special for list is last keyword
name|Integer
name|num
init|=
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
name|Integer
operator|.
name|class
argument_list|,
name|key
argument_list|)
decl_stmt|;
name|boolean
name|checkList
init|=
name|key
operator|.
name|startsWith
argument_list|(
literal|"last"
argument_list|)
operator|||
name|num
operator|!=
literal|null
decl_stmt|;
if|if
condition|(
name|checkList
condition|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|list
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
name|List
operator|.
name|class
argument_list|,
name|result
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|key
operator|.
name|startsWith
argument_list|(
literal|"last"
argument_list|)
condition|)
block|{
name|num
operator|=
name|list
operator|.
name|size
argument_list|()
operator|-
literal|1
expr_stmt|;
comment|// maybe its an expression to subtract a number after last
name|String
name|after
init|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|key
argument_list|,
literal|"-"
argument_list|)
decl_stmt|;
if|if
condition|(
name|after
operator|!=
literal|null
condition|)
block|{
name|Integer
name|redux
init|=
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
name|Integer
operator|.
name|class
argument_list|,
name|after
operator|.
name|trim
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|redux
operator|!=
literal|null
condition|)
block|{
name|num
operator|-=
name|redux
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|ExpressionIllegalSyntaxException
argument_list|(
name|key
argument_list|)
throw|;
block|}
block|}
block|}
if|if
condition|(
name|num
operator|!=
literal|null
operator|&&
name|num
operator|>=
literal|0
operator|&&
name|list
operator|.
name|size
argument_list|()
operator|>
name|num
operator|-
literal|1
condition|)
block|{
return|return
name|list
operator|.
name|get
argument_list|(
name|num
argument_list|)
return|;
block|}
if|if
condition|(
operator|!
name|nullSafe
condition|)
block|{
comment|// not null safe then its mandatory so thrown out of bounds exception
throw|throw
operator|new
name|IndexOutOfBoundsException
argument_list|(
literal|"Index: "
operator|+
name|num
operator|+
literal|", Size: "
operator|+
name|list
operator|.
name|size
argument_list|()
operator|+
literal|" out of bounds with List from bean: "
operator|+
name|bean
operator|+
literal|"using OGNL path ["
operator|+
name|ognlPath
operator|+
literal|"]"
argument_list|)
throw|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|nullSafe
condition|)
block|{
throw|throw
operator|new
name|IndexOutOfBoundsException
argument_list|(
literal|"Key: "
operator|+
name|key
operator|+
literal|" not found in bean: "
operator|+
name|bean
operator|+
literal|" of type: "
operator|+
name|ObjectHelper
operator|.
name|classCanonicalName
argument_list|(
name|bean
argument_list|)
operator|+
literal|" using OGNL path ["
operator|+
name|ognlPath
operator|+
literal|"]"
argument_list|)
throw|;
block|}
else|else
block|{
comment|// null safe so we can return null
return|return
literal|null
return|;
block|}
block|}
DECL|method|getResult ()
specifier|public
name|Object
name|getResult
parameter_list|()
block|{
return|return
name|result
return|;
block|}
block|}
block|}
end_class

end_unit

