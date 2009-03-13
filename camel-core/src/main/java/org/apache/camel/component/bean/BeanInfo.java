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
name|annotation
operator|.
name|Annotation
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
name|lang
operator|.
name|reflect
operator|.
name|Modifier
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|Body
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
name|ExchangeException
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
name|Header
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
name|Headers
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
name|NoTypeConversionAvailableException
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
name|OutHeaders
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
name|Properties
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
name|Property
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
name|builder
operator|.
name|ExpressionBuilder
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
name|language
operator|.
name|LanguageAnnotation
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
name|spi
operator|.
name|Registry
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ExchangeHelper
operator|.
name|convertToType
import|;
end_import

begin_comment
comment|/**  * Represents the metadata about a bean type created via a combination of  * introspection and annotations together with some useful sensible defaults  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|BeanInfo
specifier|public
class|class
name|BeanInfo
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
name|BeanInfo
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|Class
name|type
decl_stmt|;
DECL|field|strategy
specifier|private
specifier|final
name|ParameterMappingStrategy
name|strategy
decl_stmt|;
DECL|field|operations
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|MethodInfo
argument_list|>
name|operations
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|MethodInfo
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|operationsWithBody
specifier|private
specifier|final
name|List
argument_list|<
name|MethodInfo
argument_list|>
name|operationsWithBody
init|=
operator|new
name|ArrayList
argument_list|<
name|MethodInfo
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|operationsWithCustomAnnotation
specifier|private
specifier|final
name|List
argument_list|<
name|MethodInfo
argument_list|>
name|operationsWithCustomAnnotation
init|=
operator|new
name|ArrayList
argument_list|<
name|MethodInfo
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|methodMap
specifier|private
specifier|final
name|Map
argument_list|<
name|Method
argument_list|,
name|MethodInfo
argument_list|>
name|methodMap
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|Method
argument_list|,
name|MethodInfo
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|defaultMethod
specifier|private
name|MethodInfo
name|defaultMethod
decl_stmt|;
DECL|field|superBeanInfo
specifier|private
name|BeanInfo
name|superBeanInfo
decl_stmt|;
DECL|method|BeanInfo (CamelContext camelContext, Class type)
specifier|public
name|BeanInfo
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Class
name|type
parameter_list|)
block|{
name|this
argument_list|(
name|camelContext
argument_list|,
name|type
argument_list|,
name|createParameterMappingStrategy
argument_list|(
name|camelContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|BeanInfo (CamelContext camelContext, Class type, ParameterMappingStrategy strategy)
specifier|public
name|BeanInfo
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Class
name|type
parameter_list|,
name|ParameterMappingStrategy
name|strategy
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|strategy
operator|=
name|strategy
expr_stmt|;
name|introspect
argument_list|(
name|getType
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|operations
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|Collection
argument_list|<
name|MethodInfo
argument_list|>
name|methodInfos
init|=
name|operations
operator|.
name|values
argument_list|()
decl_stmt|;
for|for
control|(
name|MethodInfo
name|methodInfo
range|:
name|methodInfos
control|)
block|{
name|defaultMethod
operator|=
name|methodInfo
expr_stmt|;
block|}
block|}
block|}
DECL|method|getType ()
specifier|public
name|Class
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|createParameterMappingStrategy (CamelContext camelContext)
specifier|public
specifier|static
name|ParameterMappingStrategy
name|createParameterMappingStrategy
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
comment|// lookup in registry first if there is a strategy defined
name|Registry
name|registry
init|=
name|camelContext
operator|.
name|getRegistry
argument_list|()
decl_stmt|;
name|ParameterMappingStrategy
name|answer
init|=
name|registry
operator|.
name|lookup
argument_list|(
name|ParameterMappingStrategy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|ParameterMappingStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// no then use the default one
name|answer
operator|=
operator|new
name|DefaultParameterMappingStrategy
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|createInvocation (Method method, Object pojo, Exchange exchange)
specifier|public
name|MethodInvocation
name|createInvocation
parameter_list|(
name|Method
name|method
parameter_list|,
name|Object
name|pojo
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|RuntimeCamelException
block|{
name|MethodInfo
name|methodInfo
init|=
name|introspect
argument_list|(
name|type
argument_list|,
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
return|return
name|methodInfo
operator|.
name|createMethodInvocation
argument_list|(
name|pojo
argument_list|,
name|exchange
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|createInvocation (Object pojo, Exchange exchange)
specifier|public
name|MethodInvocation
name|createInvocation
parameter_list|(
name|Object
name|pojo
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|RuntimeCamelException
throws|,
name|AmbiguousMethodCallException
block|{
name|MethodInfo
name|methodInfo
init|=
literal|null
decl_stmt|;
name|String
name|name
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
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|methodInfo
operator|=
name|operations
operator|.
name|get
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|methodInfo
operator|==
literal|null
condition|)
block|{
name|methodInfo
operator|=
name|chooseMethod
argument_list|(
name|pojo
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|methodInfo
operator|==
literal|null
condition|)
block|{
name|methodInfo
operator|=
name|defaultMethod
expr_stmt|;
block|}
if|if
condition|(
name|methodInfo
operator|!=
literal|null
condition|)
block|{
return|return
name|methodInfo
operator|.
name|createMethodInvocation
argument_list|(
name|pojo
argument_list|,
name|exchange
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Introspects the given class      *      * @param clazz the class      */
DECL|method|introspect (Class clazz)
specifier|protected
name|void
name|introspect
parameter_list|(
name|Class
name|clazz
parameter_list|)
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
literal|"Introspecting class: "
operator|+
name|clazz
argument_list|)
expr_stmt|;
block|}
name|Method
index|[]
name|methods
init|=
name|clazz
operator|.
name|getDeclaredMethods
argument_list|()
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|methods
control|)
block|{
if|if
condition|(
name|isValidMethod
argument_list|(
name|clazz
argument_list|,
name|method
argument_list|)
condition|)
block|{
name|introspect
argument_list|(
name|clazz
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
block|}
name|Class
name|superclass
init|=
name|clazz
operator|.
name|getSuperclass
argument_list|()
decl_stmt|;
if|if
condition|(
name|superclass
operator|!=
literal|null
operator|&&
operator|!
name|superclass
operator|.
name|equals
argument_list|(
name|Object
operator|.
name|class
argument_list|)
condition|)
block|{
name|introspect
argument_list|(
name|superclass
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Introspects the given method      *      * @param clazz the class      * @param method the method      */
DECL|method|introspect (Class clazz, Method method)
specifier|protected
name|MethodInfo
name|introspect
parameter_list|(
name|Class
name|clazz
parameter_list|,
name|Method
name|method
parameter_list|)
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
literal|"Introspecting class: "
operator|+
name|clazz
operator|+
literal|", method: "
operator|+
name|method
argument_list|)
expr_stmt|;
block|}
name|String
name|opName
init|=
name|method
operator|.
name|getName
argument_list|()
decl_stmt|;
name|MethodInfo
name|methodInfo
init|=
name|createMethodInfo
argument_list|(
name|clazz
argument_list|,
name|method
argument_list|)
decl_stmt|;
comment|// methods already registered should be preferred to use instead of super classes of existing methods
comment|// we want to us the method from the sub class over super classes, so if we have already registered
comment|// the method then use it (we are traversing upwards: sub (child) -> super (farther) )
name|MethodInfo
name|existingMethodInfo
init|=
name|overridesExistingMethod
argument_list|(
name|methodInfo
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingMethodInfo
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
literal|"This method is already overriden in a subclass, so the method from the sub class is prefered: "
operator|+
name|existingMethodInfo
argument_list|)
expr_stmt|;
block|}
return|return
name|existingMethodInfo
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
literal|"Adding operation: "
operator|+
name|opName
operator|+
literal|" for method: "
operator|+
name|methodInfo
argument_list|)
expr_stmt|;
block|}
name|operations
operator|.
name|put
argument_list|(
name|opName
argument_list|,
name|methodInfo
argument_list|)
expr_stmt|;
if|if
condition|(
name|methodInfo
operator|.
name|hasBodyParameter
argument_list|()
condition|)
block|{
name|operationsWithBody
operator|.
name|add
argument_list|(
name|methodInfo
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|methodInfo
operator|.
name|isHasCustomAnnotation
argument_list|()
operator|&&
operator|!
name|methodInfo
operator|.
name|hasBodyParameter
argument_list|()
condition|)
block|{
name|operationsWithCustomAnnotation
operator|.
name|add
argument_list|(
name|methodInfo
argument_list|)
expr_stmt|;
block|}
comment|// must add to method map last otherwise we break stuff
name|methodMap
operator|.
name|put
argument_list|(
name|method
argument_list|,
name|methodInfo
argument_list|)
expr_stmt|;
return|return
name|methodInfo
return|;
block|}
comment|/**      * Returns the {@link MethodInfo} for the given method if it exists or null      * if there is no metadata available for the given method      */
DECL|method|getMethodInfo (Method method)
specifier|public
name|MethodInfo
name|getMethodInfo
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|MethodInfo
name|answer
init|=
name|methodMap
operator|.
name|get
argument_list|(
name|method
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// maybe the method is defined on a base class?
if|if
condition|(
name|superBeanInfo
operator|==
literal|null
operator|&&
name|type
operator|!=
name|Object
operator|.
name|class
condition|)
block|{
name|Class
name|superclass
init|=
name|type
operator|.
name|getSuperclass
argument_list|()
decl_stmt|;
if|if
condition|(
name|superclass
operator|!=
literal|null
operator|&&
name|superclass
operator|!=
name|Object
operator|.
name|class
condition|)
block|{
name|superBeanInfo
operator|=
operator|new
name|BeanInfo
argument_list|(
name|camelContext
argument_list|,
name|superclass
argument_list|,
name|strategy
argument_list|)
expr_stmt|;
return|return
name|superBeanInfo
operator|.
name|getMethodInfo
argument_list|(
name|method
argument_list|)
return|;
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createMethodInfo (Class clazz, Method method)
specifier|protected
name|MethodInfo
name|createMethodInfo
parameter_list|(
name|Class
name|clazz
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
name|Class
index|[]
name|parameterTypes
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
name|Annotation
index|[]
index|[]
name|parametersAnnotations
init|=
name|method
operator|.
name|getParameterAnnotations
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ParameterInfo
argument_list|>
name|parameters
init|=
operator|new
name|ArrayList
argument_list|<
name|ParameterInfo
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ParameterInfo
argument_list|>
name|bodyParameters
init|=
operator|new
name|ArrayList
argument_list|<
name|ParameterInfo
argument_list|>
argument_list|()
decl_stmt|;
name|boolean
name|hasCustomAnnotation
init|=
literal|false
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|parameterTypes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Class
name|parameterType
init|=
name|parameterTypes
index|[
name|i
index|]
decl_stmt|;
name|Annotation
index|[]
name|parameterAnnotations
init|=
name|parametersAnnotations
index|[
name|i
index|]
decl_stmt|;
name|Expression
name|expression
init|=
name|createParameterUnmarshalExpression
argument_list|(
name|clazz
argument_list|,
name|method
argument_list|,
name|parameterType
argument_list|,
name|parameterAnnotations
argument_list|)
decl_stmt|;
name|hasCustomAnnotation
operator||=
name|expression
operator|!=
literal|null
expr_stmt|;
name|ParameterInfo
name|parameterInfo
init|=
operator|new
name|ParameterInfo
argument_list|(
name|i
argument_list|,
name|parameterType
argument_list|,
name|parameterAnnotations
argument_list|,
name|expression
argument_list|)
decl_stmt|;
name|parameters
operator|.
name|add
argument_list|(
name|parameterInfo
argument_list|)
expr_stmt|;
if|if
condition|(
name|expression
operator|==
literal|null
condition|)
block|{
name|hasCustomAnnotation
operator||=
name|ObjectHelper
operator|.
name|hasAnnotation
argument_list|(
name|parameterAnnotations
argument_list|,
name|Body
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|bodyParameters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|Exchange
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|parameterType
argument_list|)
condition|)
block|{
comment|// use exchange
name|expression
operator|=
name|ExpressionBuilder
operator|.
name|exchangeExpression
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// lets assume its the body
name|expression
operator|=
name|ExpressionBuilder
operator|.
name|bodyExpression
argument_list|(
name|parameterType
argument_list|)
expr_stmt|;
block|}
name|parameterInfo
operator|.
name|setExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|bodyParameters
operator|.
name|add
argument_list|(
name|parameterInfo
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// will ignore the expression for parameter evaluation
block|}
block|}
block|}
comment|// now lets add the method to the repository
name|MethodInfo
name|methodInfo
init|=
operator|new
name|MethodInfo
argument_list|(
name|clazz
argument_list|,
name|method
argument_list|,
name|parameters
argument_list|,
name|bodyParameters
argument_list|,
name|hasCustomAnnotation
argument_list|)
decl_stmt|;
return|return
name|methodInfo
return|;
block|}
comment|/**      * Lets try choose one of the available methods to invoke if we can match      * the message body to the body parameter      *      * @param pojo the bean to invoke a method on      * @param exchange the message exchange      * @return the method to invoke or null if no definitive method could be      *         matched      * @throws AmbiguousMethodCallException is thrown if cannot chose method due to ambiguous      */
DECL|method|chooseMethod (Object pojo, Exchange exchange)
specifier|protected
name|MethodInfo
name|chooseMethod
parameter_list|(
name|Object
name|pojo
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|AmbiguousMethodCallException
block|{
if|if
condition|(
name|operationsWithBody
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
comment|// only one body possible so we got a hit
return|return
name|operationsWithBody
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|operationsWithBody
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// multiple operations so find the best suited if possible
return|return
name|chooseMethodWithMatchingBody
argument_list|(
name|exchange
argument_list|,
name|operationsWithBody
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|operationsWithCustomAnnotation
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
comment|// if there is one method with an annotation then use that one
return|return
name|operationsWithCustomAnnotation
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
comment|// no we could not find a method to invoke, so either there are none or there are ambigiuous methods.
return|return
literal|null
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|chooseMethodWithMatchingBody (Exchange exchange, Collection<MethodInfo> operationList)
specifier|private
name|MethodInfo
name|chooseMethodWithMatchingBody
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Collection
argument_list|<
name|MethodInfo
argument_list|>
name|operationList
parameter_list|)
throws|throws
name|AmbiguousMethodCallException
block|{
comment|// lets see if we can find a method who's body param type matches the message body
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Object
name|body
init|=
name|in
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|!=
literal|null
condition|)
block|{
name|Class
name|bodyType
init|=
name|body
operator|.
name|getClass
argument_list|()
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
literal|"Matching for method with a single parameter that matches type: "
operator|+
name|bodyType
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|MethodInfo
argument_list|>
name|possibles
init|=
operator|new
name|ArrayList
argument_list|<
name|MethodInfo
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MethodInfo
argument_list|>
name|possiblesWithException
init|=
operator|new
name|ArrayList
argument_list|<
name|MethodInfo
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|MethodInfo
name|methodInfo
range|:
name|operationList
control|)
block|{
comment|// TODO: AOP proxies have additional methods - consider having a static
comment|// method exclude list to skip all known AOP proxy methods
comment|// TODO: This class could use some TRACE logging
comment|// test for MEP pattern matching
name|boolean
name|out
init|=
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
decl_stmt|;
if|if
condition|(
name|out
operator|&&
name|methodInfo
operator|.
name|isReturnTypeVoid
argument_list|()
condition|)
block|{
comment|// skip this method as the MEP is Out so the method must return something
continue|continue;
block|}
comment|// try to match the arguments
if|if
condition|(
name|methodInfo
operator|.
name|bodyParameterMatches
argument_list|(
name|bodyType
argument_list|)
condition|)
block|{
if|if
condition|(
name|methodInfo
operator|.
name|hasExceptionParameter
argument_list|()
condition|)
block|{
comment|// methods with accepts exceptions
name|possiblesWithException
operator|.
name|add
argument_list|(
name|methodInfo
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// regular methods with no exceptions
name|possibles
operator|.
name|add
argument_list|(
name|methodInfo
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// TODO refactor below into a separate method
comment|// find best suited method to use
name|Exception
name|exception
init|=
name|ExpressionBuilder
operator|.
name|exchangeExceptionExpression
argument_list|()
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|exception
operator|!=
literal|null
operator|&&
name|possiblesWithException
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
comment|// prefer the method that accepts exception in case we have an exception also
return|return
name|possiblesWithException
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|possibles
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|possibles
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|possibles
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// TODO: Make sure this is properly unit tested
comment|// lets try converting
name|Object
name|newBody
init|=
literal|null
decl_stmt|;
name|MethodInfo
name|matched
init|=
literal|null
decl_stmt|;
for|for
control|(
name|MethodInfo
name|methodInfo
range|:
name|operationList
control|)
block|{
name|Object
name|value
decl_stmt|;
try|try
block|{
name|value
operator|=
name|convertToType
argument_list|(
name|exchange
argument_list|,
name|methodInfo
operator|.
name|getBodyParameterType
argument_list|()
argument_list|,
name|body
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|newBody
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|AmbiguousMethodCallException
argument_list|(
name|exchange
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|matched
argument_list|,
name|methodInfo
argument_list|)
argument_list|)
throw|;
block|}
else|else
block|{
name|newBody
operator|=
name|value
expr_stmt|;
name|matched
operator|=
name|methodInfo
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|NoTypeConversionAvailableException
name|e
parameter_list|)
block|{
comment|// we can safely ignore this exception as we want a behaviour similar to
comment|// that if convertToType return null
block|}
block|}
if|if
condition|(
name|matched
operator|!=
literal|null
condition|)
block|{
name|in
operator|.
name|setBody
argument_list|(
name|newBody
argument_list|)
expr_stmt|;
return|return
name|matched
return|;
block|}
block|}
else|else
block|{
comment|// if we only have a single method with custom annotations, lets use that one
if|if
condition|(
name|operationsWithCustomAnnotation
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|operationsWithCustomAnnotation
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
return|return
name|chooseMethodWithCustomAnnotations
argument_list|(
name|exchange
argument_list|,
name|possibles
argument_list|)
return|;
block|}
block|}
comment|// no match so return null
return|return
literal|null
return|;
block|}
comment|/**      * Validates wheter the given method is a valid candidate for Camel Bean Binding.      *      * @param clazz   the class      * @param method  the method      * @return true if valid, false to skip the method      */
DECL|method|isValidMethod (Class clazz, Method method)
specifier|protected
name|boolean
name|isValidMethod
parameter_list|(
name|Class
name|clazz
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
comment|// must be a public method
if|if
condition|(
operator|!
name|Modifier
operator|.
name|isPublic
argument_list|(
name|method
operator|.
name|getModifiers
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// return type must not be an Exchange
if|if
condition|(
name|method
operator|.
name|getReturnType
argument_list|()
operator|!=
literal|null
operator|&&
name|Exchange
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|method
operator|.
name|getReturnType
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Does the given method info override an existing method registered before (from a subclass)      *      * @param methodInfo  the method to test      * @return the already registered method to use, null if not overriding any      */
DECL|method|overridesExistingMethod (MethodInfo methodInfo)
specifier|private
name|MethodInfo
name|overridesExistingMethod
parameter_list|(
name|MethodInfo
name|methodInfo
parameter_list|)
block|{
for|for
control|(
name|MethodInfo
name|info
range|:
name|methodMap
operator|.
name|values
argument_list|()
control|)
block|{
comment|// name test
if|if
condition|(
operator|!
name|info
operator|.
name|getMethod
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|methodInfo
operator|.
name|getMethod
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
comment|// parameter types
if|if
condition|(
name|info
operator|.
name|getMethod
argument_list|()
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|!=
name|methodInfo
operator|.
name|getMethod
argument_list|()
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
condition|)
block|{
continue|continue;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|info
operator|.
name|getMethod
argument_list|()
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Class
name|type1
init|=
name|info
operator|.
name|getMethod
argument_list|()
operator|.
name|getParameterTypes
argument_list|()
index|[
name|i
index|]
decl_stmt|;
name|Class
name|type2
init|=
name|methodInfo
operator|.
name|getMethod
argument_list|()
operator|.
name|getParameterTypes
argument_list|()
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
operator|!
name|type1
operator|.
name|equals
argument_list|(
name|type2
argument_list|)
condition|)
block|{
continue|continue;
block|}
block|}
comment|// same name, same parameters, then its overrides an existing class
return|return
name|info
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|chooseMethodWithCustomAnnotations (Exchange exchange, Collection<MethodInfo> possibles)
specifier|private
name|MethodInfo
name|chooseMethodWithCustomAnnotations
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Collection
argument_list|<
name|MethodInfo
argument_list|>
name|possibles
parameter_list|)
throws|throws
name|AmbiguousMethodCallException
block|{
comment|// if we have only one method with custom annotations lets choose that
name|MethodInfo
name|chosen
init|=
literal|null
decl_stmt|;
for|for
control|(
name|MethodInfo
name|possible
range|:
name|possibles
control|)
block|{
if|if
condition|(
name|possible
operator|.
name|isHasCustomAnnotation
argument_list|()
condition|)
block|{
if|if
condition|(
name|chosen
operator|!=
literal|null
condition|)
block|{
name|chosen
operator|=
literal|null
expr_stmt|;
break|break;
block|}
else|else
block|{
name|chosen
operator|=
name|possible
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|chosen
operator|!=
literal|null
condition|)
block|{
return|return
name|chosen
return|;
block|}
throw|throw
operator|new
name|AmbiguousMethodCallException
argument_list|(
name|exchange
argument_list|,
name|possibles
argument_list|)
throw|;
block|}
comment|/**      * Creates an expression for the given parameter type if the parameter can      * be mapped automatically or null if the parameter cannot be mapped due to      * insufficient annotations or not fitting with the default type      * conventions.      */
DECL|method|createParameterUnmarshalExpression (Class clazz, Method method, Class parameterType, Annotation[] parameterAnnotation)
specifier|private
name|Expression
name|createParameterUnmarshalExpression
parameter_list|(
name|Class
name|clazz
parameter_list|,
name|Method
name|method
parameter_list|,
name|Class
name|parameterType
parameter_list|,
name|Annotation
index|[]
name|parameterAnnotation
parameter_list|)
block|{
comment|// look for a parameter annotation that converts into an expression
for|for
control|(
name|Annotation
name|annotation
range|:
name|parameterAnnotation
control|)
block|{
name|Expression
name|answer
init|=
name|createParameterUnmarshalExpressionForAnnotation
argument_list|(
name|clazz
argument_list|,
name|method
argument_list|,
name|parameterType
argument_list|,
name|annotation
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
return|return
name|answer
return|;
block|}
block|}
comment|// no annotations then try the default parameter mappings
return|return
name|strategy
operator|.
name|getDefaultParameterTypeExpression
argument_list|(
name|parameterType
argument_list|)
return|;
block|}
DECL|method|createParameterUnmarshalExpressionForAnnotation (Class clazz, Method method, Class parameterType, Annotation annotation)
specifier|private
name|Expression
name|createParameterUnmarshalExpressionForAnnotation
parameter_list|(
name|Class
name|clazz
parameter_list|,
name|Method
name|method
parameter_list|,
name|Class
name|parameterType
parameter_list|,
name|Annotation
name|annotation
parameter_list|)
block|{
if|if
condition|(
name|annotation
operator|instanceof
name|Property
condition|)
block|{
name|Property
name|propertyAnnotation
init|=
operator|(
name|Property
operator|)
name|annotation
decl_stmt|;
return|return
name|ExpressionBuilder
operator|.
name|propertyExpression
argument_list|(
name|propertyAnnotation
operator|.
name|value
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|annotation
operator|instanceof
name|Properties
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|propertiesExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|annotation
operator|instanceof
name|Header
condition|)
block|{
name|Header
name|headerAnnotation
init|=
operator|(
name|Header
operator|)
name|annotation
decl_stmt|;
return|return
name|ExpressionBuilder
operator|.
name|headerExpression
argument_list|(
name|headerAnnotation
operator|.
name|value
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|annotation
operator|instanceof
name|Headers
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|headersExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|annotation
operator|instanceof
name|OutHeaders
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|outHeadersExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|annotation
operator|instanceof
name|ExchangeException
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|exchangeExceptionExpression
argument_list|()
return|;
block|}
else|else
block|{
name|LanguageAnnotation
name|languageAnnotation
init|=
name|annotation
operator|.
name|annotationType
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|LanguageAnnotation
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|languageAnnotation
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|languageAnnotation
operator|.
name|factory
argument_list|()
decl_stmt|;
name|Object
name|object
init|=
name|camelContext
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|object
operator|instanceof
name|AnnotationExpressionFactory
condition|)
block|{
name|AnnotationExpressionFactory
name|expressionFactory
init|=
operator|(
name|AnnotationExpressionFactory
operator|)
name|object
decl_stmt|;
return|return
name|expressionFactory
operator|.
name|createExpression
argument_list|(
name|camelContext
argument_list|,
name|annotation
argument_list|,
name|languageAnnotation
argument_list|,
name|parameterType
argument_list|)
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Ignoring bad annotation: "
operator|+
name|languageAnnotation
operator|+
literal|"on method: "
operator|+
name|method
operator|+
literal|" which declares a factory: "
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|" which does not implement "
operator|+
name|AnnotationExpressionFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

