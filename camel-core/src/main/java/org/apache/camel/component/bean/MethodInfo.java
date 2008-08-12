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
name|AccessibleObject
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
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|AnnotatedElement
import|;
end_import

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
name|List
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
name|Pattern
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
name|ExchangeHelper
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

begin_comment
comment|/**  * Information about a method to be used for invocation.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|MethodInfo
specifier|public
class|class
name|MethodInfo
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
name|MethodInfo
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|type
specifier|private
name|Class
name|type
decl_stmt|;
DECL|field|method
specifier|private
name|Method
name|method
decl_stmt|;
DECL|field|parameters
specifier|private
specifier|final
name|List
argument_list|<
name|ParameterInfo
argument_list|>
name|parameters
decl_stmt|;
DECL|field|bodyParameters
specifier|private
specifier|final
name|List
argument_list|<
name|ParameterInfo
argument_list|>
name|bodyParameters
decl_stmt|;
DECL|field|hasCustomAnnotation
specifier|private
specifier|final
name|boolean
name|hasCustomAnnotation
decl_stmt|;
DECL|field|parametersExpression
specifier|private
name|Expression
name|parametersExpression
decl_stmt|;
DECL|field|pattern
specifier|private
name|ExchangePattern
name|pattern
init|=
name|ExchangePattern
operator|.
name|InOut
decl_stmt|;
DECL|method|MethodInfo (Class type, Method method, List<ParameterInfo> parameters, List<ParameterInfo> bodyParameters, boolean hasCustomAnnotation)
specifier|public
name|MethodInfo
parameter_list|(
name|Class
name|type
parameter_list|,
name|Method
name|method
parameter_list|,
name|List
argument_list|<
name|ParameterInfo
argument_list|>
name|parameters
parameter_list|,
name|List
argument_list|<
name|ParameterInfo
argument_list|>
name|bodyParameters
parameter_list|,
name|boolean
name|hasCustomAnnotation
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
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
name|this
operator|.
name|bodyParameters
operator|=
name|bodyParameters
expr_stmt|;
name|this
operator|.
name|hasCustomAnnotation
operator|=
name|hasCustomAnnotation
expr_stmt|;
name|this
operator|.
name|parametersExpression
operator|=
name|createParametersExpression
argument_list|()
expr_stmt|;
name|Pattern
name|oneway
init|=
name|findOneWayAnnotation
argument_list|(
name|method
argument_list|)
decl_stmt|;
if|if
condition|(
name|oneway
operator|!=
literal|null
condition|)
block|{
name|pattern
operator|=
name|oneway
operator|.
name|value
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|method
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|createMethodInvocation (final Object pojo, final Exchange messageExchange)
specifier|public
name|MethodInvocation
name|createMethodInvocation
parameter_list|(
specifier|final
name|Object
name|pojo
parameter_list|,
specifier|final
name|Exchange
name|messageExchange
parameter_list|)
block|{
specifier|final
name|Object
index|[]
name|arguments
init|=
operator|(
name|Object
index|[]
operator|)
name|parametersExpression
operator|.
name|evaluate
argument_list|(
name|messageExchange
argument_list|)
decl_stmt|;
return|return
operator|new
name|MethodInvocation
argument_list|()
block|{
specifier|public
name|Method
name|getMethod
parameter_list|()
block|{
return|return
name|method
return|;
block|}
specifier|public
name|Object
index|[]
name|getArguments
parameter_list|()
block|{
return|return
name|arguments
return|;
block|}
specifier|public
name|Object
name|proceed
parameter_list|()
throws|throws
name|Throwable
block|{
return|return
name|invoke
argument_list|(
name|method
argument_list|,
name|pojo
argument_list|,
name|arguments
argument_list|,
name|messageExchange
argument_list|)
return|;
block|}
specifier|public
name|Object
name|getThis
parameter_list|()
block|{
return|return
name|pojo
return|;
block|}
specifier|public
name|AccessibleObject
name|getStaticPart
parameter_list|()
block|{
return|return
name|method
return|;
block|}
block|}
return|;
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
DECL|method|getMethod ()
specifier|public
name|Method
name|getMethod
parameter_list|()
block|{
return|return
name|method
return|;
block|}
comment|/**      * Returns the {@link org.apache.camel.ExchangePattern} that should be used when invoking this method. This value      * defaults to {@link org.apache.camel.ExchangePattern#InOut} unless some {@link org.apache.camel.Pattern} annotation is used      * to override the message exchange pattern.      *      * @return the exchange pattern to use for invoking this method.      */
DECL|method|getPattern ()
specifier|public
name|ExchangePattern
name|getPattern
parameter_list|()
block|{
return|return
name|pattern
return|;
block|}
DECL|method|getParametersExpression ()
specifier|public
name|Expression
name|getParametersExpression
parameter_list|()
block|{
return|return
name|parametersExpression
return|;
block|}
DECL|method|getBodyParameters ()
specifier|public
name|List
argument_list|<
name|ParameterInfo
argument_list|>
name|getBodyParameters
parameter_list|()
block|{
return|return
name|bodyParameters
return|;
block|}
DECL|method|getBodyParameterType ()
specifier|public
name|Class
name|getBodyParameterType
parameter_list|()
block|{
name|ParameterInfo
name|parameterInfo
init|=
name|bodyParameters
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
return|return
name|parameterInfo
operator|.
name|getType
argument_list|()
return|;
block|}
DECL|method|bodyParameterMatches (Class bodyType)
specifier|public
name|boolean
name|bodyParameterMatches
parameter_list|(
name|Class
name|bodyType
parameter_list|)
block|{
name|Class
name|actualType
init|=
name|getBodyParameterType
argument_list|()
decl_stmt|;
return|return
name|actualType
operator|!=
literal|null
operator|&&
name|ObjectHelper
operator|.
name|isAssignableFrom
argument_list|(
name|bodyType
argument_list|,
name|actualType
argument_list|)
return|;
block|}
DECL|method|getParameters ()
specifier|public
name|List
argument_list|<
name|ParameterInfo
argument_list|>
name|getParameters
parameter_list|()
block|{
return|return
name|parameters
return|;
block|}
DECL|method|hasBodyParameter ()
specifier|public
name|boolean
name|hasBodyParameter
parameter_list|()
block|{
return|return
operator|!
name|bodyParameters
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|isHasCustomAnnotation ()
specifier|public
name|boolean
name|isHasCustomAnnotation
parameter_list|()
block|{
return|return
name|hasCustomAnnotation
return|;
block|}
DECL|method|isReturnTypeVoid ()
specifier|public
name|boolean
name|isReturnTypeVoid
parameter_list|()
block|{
return|return
name|method
operator|.
name|getReturnType
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"void"
argument_list|)
return|;
block|}
DECL|method|invoke (Method mth, Object pojo, Object[] arguments, Exchange exchange)
specifier|protected
name|Object
name|invoke
parameter_list|(
name|Method
name|mth
parameter_list|,
name|Object
name|pojo
parameter_list|,
name|Object
index|[]
name|arguments
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IllegalAccessException
throws|,
name|InvocationTargetException
block|{
return|return
name|mth
operator|.
name|invoke
argument_list|(
name|pojo
argument_list|,
name|arguments
argument_list|)
return|;
block|}
DECL|method|createParametersExpression ()
specifier|protected
name|Expression
name|createParametersExpression
parameter_list|()
block|{
specifier|final
name|int
name|size
init|=
name|parameters
operator|.
name|size
argument_list|()
decl_stmt|;
specifier|final
name|Expression
index|[]
name|expressions
init|=
operator|new
name|Expression
index|[
name|size
index|]
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
name|size
condition|;
name|i
operator|++
control|)
block|{
name|Expression
name|parameterExpression
init|=
name|parameters
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getExpression
argument_list|()
decl_stmt|;
name|expressions
index|[
name|i
index|]
operator|=
name|parameterExpression
expr_stmt|;
block|}
return|return
operator|new
name|Expression
argument_list|<
name|Exchange
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
index|[]
name|answer
init|=
operator|new
name|Object
index|[
name|size
index|]
decl_stmt|;
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|boolean
name|multiParameterArray
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|BeanProcessor
operator|.
name|MULTI_PARAMETER_ARRAY
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|multiParameterArray
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|BeanProcessor
operator|.
name|MULTI_PARAMETER_ARRAY
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|size
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|value
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|multiParameterArray
condition|)
block|{
name|value
operator|=
operator|(
operator|(
name|Object
index|[]
operator|)
name|body
operator|)
index|[
name|i
index|]
expr_stmt|;
block|}
else|else
block|{
name|value
operator|=
name|expressions
index|[
name|i
index|]
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// now lets try to coerce the value to the required type
name|Class
name|expectedType
init|=
name|parameters
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getType
argument_list|()
decl_stmt|;
name|value
operator|=
name|ExchangeHelper
operator|.
name|convertToType
argument_list|(
name|exchange
argument_list|,
name|expectedType
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|answer
index|[
name|i
index|]
operator|=
name|value
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ParametersExpression: "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|expressions
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**      * Finds the oneway annotation in priority order; look for method level annotations first, then the class level annotations,      * then super class annotations then interface annotations      *      * @param method the method on which to search      * @return the first matching annotation or none if it is not available      */
DECL|method|findOneWayAnnotation (Method method)
specifier|protected
name|Pattern
name|findOneWayAnnotation
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|Pattern
name|answer
init|=
name|getPatternAnnotation
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
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|method
operator|.
name|getDeclaringClass
argument_list|()
decl_stmt|;
comment|// lets create the search order of types to scan
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|typesToSearch
init|=
operator|new
name|ArrayList
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|addTypeAndSuperTypes
argument_list|(
name|type
argument_list|,
name|typesToSearch
argument_list|)
expr_stmt|;
name|Class
index|[]
name|interfaces
init|=
name|type
operator|.
name|getInterfaces
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
name|anInterface
range|:
name|interfaces
control|)
block|{
name|addTypeAndSuperTypes
argument_list|(
name|anInterface
argument_list|,
name|typesToSearch
argument_list|)
expr_stmt|;
block|}
comment|// now lets scan for a type which the current declared class overloads
name|answer
operator|=
name|findOneWayAnnotationOnMethod
argument_list|(
name|typesToSearch
argument_list|,
name|method
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|findOneWayAnnotation
argument_list|(
name|typesToSearch
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Returns the pattern annotation on the given annotated element; either as a direct annotation or      * on an annotation which is also annotated      *      * @param annotatedElement the element to look for the annotation      * @return the first matching annotation or null if none could be found      */
DECL|method|getPatternAnnotation (AnnotatedElement annotatedElement)
specifier|protected
name|Pattern
name|getPatternAnnotation
parameter_list|(
name|AnnotatedElement
name|annotatedElement
parameter_list|)
block|{
return|return
name|getPatternAnnotation
argument_list|(
name|annotatedElement
argument_list|,
literal|2
argument_list|)
return|;
block|}
comment|/**      * Returns the pattern annotation on the given annotated element; either as a direct annotation or      * on an annotation which is also annotated      *      * @param annotatedElement the element to look for the annotation      * @return the first matching annotation or null if none could be found      */
DECL|method|getPatternAnnotation (AnnotatedElement annotatedElement, int depth)
specifier|protected
name|Pattern
name|getPatternAnnotation
parameter_list|(
name|AnnotatedElement
name|annotatedElement
parameter_list|,
name|int
name|depth
parameter_list|)
block|{
name|Pattern
name|answer
init|=
name|annotatedElement
operator|.
name|getAnnotation
argument_list|(
name|Pattern
operator|.
name|class
argument_list|)
decl_stmt|;
name|int
name|nextDepth
init|=
name|depth
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|nextDepth
operator|>
literal|0
condition|)
block|{
comment|// lets look at all the annotations to see if any of those are annotated
name|Annotation
index|[]
name|annotations
init|=
name|annotatedElement
operator|.
name|getAnnotations
argument_list|()
decl_stmt|;
for|for
control|(
name|Annotation
name|annotation
range|:
name|annotations
control|)
block|{
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotationType
init|=
name|annotation
operator|.
name|annotationType
argument_list|()
decl_stmt|;
if|if
condition|(
name|annotation
operator|instanceof
name|Pattern
operator|||
name|annotationType
operator|.
name|equals
argument_list|(
name|annotatedElement
argument_list|)
condition|)
block|{
continue|continue;
block|}
else|else
block|{
name|Pattern
name|another
init|=
name|getPatternAnnotation
argument_list|(
name|annotationType
argument_list|,
name|nextDepth
argument_list|)
decl_stmt|;
if|if
condition|(
name|pattern
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|another
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Duplicate pattern annotation: "
operator|+
name|another
operator|+
literal|" found on annotation: "
operator|+
name|annotation
operator|+
literal|" which will be ignored"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Adds the current class and all of its base classes (apart from {@link Object} to the given list      * @param type      * @param result      */
DECL|method|addTypeAndSuperTypes (Class<?> type, List<Class<?>> result)
specifier|protected
name|void
name|addTypeAndSuperTypes
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|result
parameter_list|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|t
init|=
name|type
init|;
name|t
operator|!=
literal|null
operator|&&
name|t
operator|!=
name|Object
operator|.
name|class
condition|;
name|t
operator|=
name|t
operator|.
name|getSuperclass
argument_list|()
control|)
block|{
name|result
operator|.
name|add
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Finds the first annotation on the base methods defined in the list of classes      */
DECL|method|findOneWayAnnotationOnMethod (List<Class<?>> classes, Method method)
specifier|protected
name|Pattern
name|findOneWayAnnotationOnMethod
parameter_list|(
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
range|:
name|classes
control|)
block|{
try|try
block|{
name|Method
name|definedMethod
init|=
name|type
operator|.
name|getMethod
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|,
name|method
operator|.
name|getParameterTypes
argument_list|()
argument_list|)
decl_stmt|;
name|Pattern
name|answer
init|=
name|getPatternAnnotation
argument_list|(
name|definedMethod
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
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Finds the first annotation on the given list of classes      */
DECL|method|findOneWayAnnotation (List<Class<?>> classes)
specifier|protected
name|Pattern
name|findOneWayAnnotation
parameter_list|(
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
parameter_list|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
range|:
name|classes
control|)
block|{
name|Pattern
name|answer
init|=
name|getPatternAnnotation
argument_list|(
name|type
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
literal|null
return|;
block|}
block|}
end_class

end_unit

