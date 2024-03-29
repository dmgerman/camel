begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.language
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|language
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
name|spi
operator|.
name|Language
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
name|support
operator|.
name|PredicateToExpressionAdapter
import|;
end_import

begin_comment
comment|/**  * Default implementation of the {@link AnnotationExpressionFactory}.  */
end_comment

begin_class
DECL|class|DefaultAnnotationExpressionFactory
specifier|public
class|class
name|DefaultAnnotationExpressionFactory
implements|implements
name|AnnotationExpressionFactory
block|{
annotation|@
name|Override
DECL|method|createExpression (CamelContext camelContext, Annotation annotation, LanguageAnnotation languageAnnotation, Class<?> expressionReturnType)
specifier|public
name|Expression
name|createExpression
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Annotation
name|annotation
parameter_list|,
name|LanguageAnnotation
name|languageAnnotation
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|expressionReturnType
parameter_list|)
block|{
name|String
name|languageName
init|=
name|languageAnnotation
operator|.
name|language
argument_list|()
decl_stmt|;
if|if
condition|(
name|languageName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot determine the language from the annotation: "
operator|+
name|annotation
argument_list|)
throw|;
block|}
name|Language
name|language
init|=
name|camelContext
operator|.
name|resolveLanguage
argument_list|(
name|languageName
argument_list|)
decl_stmt|;
if|if
condition|(
name|language
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot find the language: "
operator|+
name|languageName
operator|+
literal|" on the classpath"
argument_list|)
throw|;
block|}
name|String
name|expression
init|=
name|getExpressionFromAnnotation
argument_list|(
name|annotation
argument_list|)
decl_stmt|;
if|if
condition|(
name|expressionReturnType
operator|==
name|Boolean
operator|.
name|class
operator|||
name|expressionReturnType
operator|==
name|boolean
operator|.
name|class
condition|)
block|{
name|Predicate
name|predicate
init|=
name|language
operator|.
name|createPredicate
argument_list|(
name|expression
argument_list|)
decl_stmt|;
return|return
name|PredicateToExpressionAdapter
operator|.
name|toExpression
argument_list|(
name|predicate
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|language
operator|.
name|createExpression
argument_list|(
name|expression
argument_list|)
return|;
block|}
block|}
DECL|method|getExpressionFromAnnotation (Annotation annotation)
specifier|protected
name|String
name|getExpressionFromAnnotation
parameter_list|(
name|Annotation
name|annotation
parameter_list|)
block|{
name|Object
name|value
init|=
name|getAnnotationObjectValue
argument_list|(
name|annotation
argument_list|,
literal|"value"
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot determine the expression from the annotation: "
operator|+
name|annotation
argument_list|)
throw|;
block|}
return|return
name|value
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * @param annotation The annotation to get the value of       * @param methodName The annotation name       * @return The value of the annotation      */
DECL|method|getAnnotationObjectValue (Annotation annotation, String methodName)
specifier|protected
name|Object
name|getAnnotationObjectValue
parameter_list|(
name|Annotation
name|annotation
parameter_list|,
name|String
name|methodName
parameter_list|)
block|{
try|try
block|{
name|Method
name|method
init|=
name|annotation
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
name|methodName
argument_list|)
decl_stmt|;
name|Object
name|value
init|=
name|ObjectHelper
operator|.
name|invokeMethod
argument_list|(
name|method
argument_list|,
name|annotation
argument_list|)
decl_stmt|;
return|return
name|value
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot determine the Object value of the annotation: "
operator|+
name|annotation
operator|+
literal|" as it does not have the method: "
operator|+
name|methodName
operator|+
literal|"() method"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

