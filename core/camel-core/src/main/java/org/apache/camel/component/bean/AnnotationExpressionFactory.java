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
name|language
operator|.
name|LanguageAnnotation
import|;
end_import

begin_comment
comment|/**  * A factory which creates an {@link Expression} object from an annotation on a field, property or method parameter  * of a specified type.  */
end_comment

begin_interface
DECL|interface|AnnotationExpressionFactory
specifier|public
interface|interface
name|AnnotationExpressionFactory
block|{
DECL|method|createExpression (CamelContext camelContext, Annotation annotation, LanguageAnnotation languageAnnotation, Class<?> expressionReturnType)
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
function_decl|;
block|}
end_interface

end_unit

