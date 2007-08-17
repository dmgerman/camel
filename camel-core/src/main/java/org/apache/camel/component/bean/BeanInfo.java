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
comment|/**  * Represents the metadata about a bean type created via a combination of  * introspection and annotations together with some useful sensible defaults  *   * @version $Revision: $  */
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
DECL|field|type
specifier|private
name|Class
name|type
decl_stmt|;
DECL|field|strategy
specifier|private
name|ParameterMappingStrategy
name|strategy
decl_stmt|;
DECL|field|operations
specifier|private
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
DECL|field|defaultMethod
specifier|private
name|MethodInfo
name|defaultMethod
decl_stmt|;
DECL|field|operationsWithBody
specifier|private
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
DECL|method|BeanInfo (Class type, ParameterMappingStrategy strategy)
specifier|public
name|BeanInfo
parameter_list|(
name|Class
name|type
parameter_list|,
name|ParameterMappingStrategy
name|strategy
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
comment|// TODO use some other mechanism?
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
name|BeanProcessor
operator|.
name|METHOD_NAME
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
DECL|method|introspect (Class clazz)
specifier|protected
name|void
name|introspect
parameter_list|(
name|Class
name|clazz
parameter_list|)
block|{
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
specifier|final
name|Expression
index|[]
name|parameterExpressions
init|=
operator|new
name|Expression
index|[
name|parameterTypes
operator|.
name|length
index|]
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
if|if
condition|(
name|expression
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|bodyParameters
operator|.
name|isEmpty
argument_list|()
condition|)
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
else|else
block|{
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
literal|"No expression available for method: "
operator|+
name|method
operator|.
name|toString
argument_list|()
operator|+
literal|" which already has a body so ignoring parameter: "
operator|+
name|i
operator|+
literal|" so ignoring method"
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
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
name|isPossibleBodyParameter
argument_list|(
name|parameterAnnotations
argument_list|)
condition|)
block|{
name|bodyParameters
operator|.
name|add
argument_list|(
name|parameterInfo
argument_list|)
expr_stmt|;
block|}
block|}
comment|// now lets add the method to the repository
name|String
name|opName
init|=
name|method
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/*          *           * TODO allow an annotation to expose the operation name to use          *           * if (method.getAnnotation(Operation.class) != null) { String name =          * method.getAnnotation(Operation.class).name(); if (name != null&&          * name.length()> 0) { opName = name; } }          */
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
argument_list|)
decl_stmt|;
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
return|return
name|methodInfo
return|;
block|}
comment|/**      * Lets try choose one of the available methods to invoke if we can match      * the message body to the body parameter      *       * @param pojo the bean to invoke a method on      * @param exchange the message exchange      * @return the method to invoke or null if no definitive method could be      *         matched      */
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
comment|// lets see if we can find a method who's body param type matches
comment|// the message body
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
for|for
control|(
name|MethodInfo
name|methodInfo
range|:
name|operationsWithBody
control|)
block|{
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
name|possibles
operator|.
name|add
argument_list|(
name|methodInfo
argument_list|)
expr_stmt|;
block|}
block|}
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
name|operationsWithBody
control|)
block|{
name|Object
name|value
init|=
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
decl_stmt|;
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
block|}
return|return
literal|null
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Creates an expression for the given parameter type if the parameter can      * be mapped automatically or null if the parameter cannot be mapped due to      * unsufficient annotations or not fitting with the default type      * conventions.      */
DECL|method|createParameterUnmarshalExpression (Class clazz, Method method, Class parameterType, Annotation[] parameterAnnotation)
specifier|protected
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
comment|// TODO look for a parameter annotation that converts into an expression
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
return|return
name|strategy
operator|.
name|getDefaultParameterTypeExpression
argument_list|(
name|parameterType
argument_list|)
return|;
block|}
DECL|method|isPossibleBodyParameter (Annotation[] annotations)
specifier|protected
name|boolean
name|isPossibleBodyParameter
parameter_list|(
name|Annotation
index|[]
name|annotations
parameter_list|)
block|{
if|if
condition|(
name|annotations
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Annotation
name|annotation
range|:
name|annotations
control|)
block|{
if|if
condition|(
operator|(
name|annotation
operator|instanceof
name|Property
operator|)
operator|||
operator|(
name|annotation
operator|instanceof
name|Header
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
DECL|method|createParameterUnmarshalExpressionForAnnotation (Class clazz, Method method, Class parameterType, Annotation annotation)
specifier|protected
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
name|name
argument_list|()
argument_list|)
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
name|name
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|annotation
operator|instanceof
name|Body
condition|)
block|{
name|Body
name|content
init|=
operator|(
name|Body
operator|)
name|annotation
decl_stmt|;
return|return
name|ExpressionBuilder
operator|.
name|bodyExpression
argument_list|(
name|parameterType
argument_list|)
return|;
comment|// TODO allow annotations to be used to create expressions?
comment|/*              * } else if (annotation instanceof XPath) { XPath xpathAnnotation =              * (XPath) annotation; return new              * JAXPStringXPathExpression(xpathAnnotation.xpath()); }              */
block|}
return|return
literal|null
return|;
block|}
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
return|return
name|Modifier
operator|.
name|isPublic
argument_list|(
name|method
operator|.
name|getModifiers
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

