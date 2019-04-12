begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xquery
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xquery
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
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|functions
operator|.
name|CollectionFn
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
name|support
operator|.
name|language
operator|.
name|DefaultAnnotationExpressionFactory
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
name|support
operator|.
name|language
operator|.
name|NamespacePrefix
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

begin_class
DECL|class|XQueryAnnotationExpressionFactory
specifier|public
class|class
name|XQueryAnnotationExpressionFactory
extends|extends
name|DefaultAnnotationExpressionFactory
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
name|xQuery
init|=
name|getExpressionFromAnnotation
argument_list|(
name|annotation
argument_list|)
decl_stmt|;
name|XQueryBuilder
name|builder
init|=
name|XQueryBuilder
operator|.
name|xquery
argument_list|(
name|xQuery
argument_list|)
decl_stmt|;
if|if
condition|(
name|annotation
operator|instanceof
name|XQuery
condition|)
block|{
name|XQuery
name|xQueryAnnotation
init|=
operator|(
name|XQuery
operator|)
name|annotation
decl_stmt|;
name|builder
operator|.
name|setStripsAllWhiteSpace
argument_list|(
name|xQueryAnnotation
operator|.
name|stripsAllWhiteSpace
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|xQueryAnnotation
operator|.
name|headerName
argument_list|()
argument_list|)
condition|)
block|{
name|builder
operator|.
name|setHeaderName
argument_list|(
name|xQueryAnnotation
operator|.
name|headerName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|NamespacePrefix
index|[]
name|namespaces
init|=
name|xQueryAnnotation
operator|.
name|namespaces
argument_list|()
decl_stmt|;
if|if
condition|(
name|namespaces
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|NamespacePrefix
name|namespacePrefix
range|:
name|namespaces
control|)
block|{
name|builder
operator|=
name|builder
operator|.
name|namespace
argument_list|(
name|namespacePrefix
operator|.
name|prefix
argument_list|()
argument_list|,
name|namespacePrefix
operator|.
name|uri
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|expressionReturnType
operator|.
name|isAssignableFrom
argument_list|(
name|String
operator|.
name|class
argument_list|)
condition|)
block|{
name|builder
operator|.
name|setResultsFormat
argument_list|(
name|ResultFormat
operator|.
name|String
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|expressionReturnType
operator|.
name|isAssignableFrom
argument_list|(
name|CollectionFn
operator|.
name|class
argument_list|)
condition|)
block|{
name|builder
operator|.
name|setResultsFormat
argument_list|(
name|ResultFormat
operator|.
name|List
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|expressionReturnType
operator|.
name|isAssignableFrom
argument_list|(
name|Node
operator|.
name|class
argument_list|)
condition|)
block|{
name|builder
operator|.
name|setResultsFormat
argument_list|(
name|ResultFormat
operator|.
name|DOM
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|expressionReturnType
operator|.
name|isAssignableFrom
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
condition|)
block|{
name|builder
operator|.
name|setResultsFormat
argument_list|(
name|ResultFormat
operator|.
name|Bytes
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
return|;
block|}
block|}
end_class

end_unit

