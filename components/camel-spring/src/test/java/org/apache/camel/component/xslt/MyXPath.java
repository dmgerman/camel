begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xslt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xslt
package|;
end_package

begin_comment
comment|//START SNIPPET: example
end_comment

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
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
name|Retention
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
name|RetentionPolicy
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
name|Target
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
name|XPathAnnotationExpressionFactory
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
name|language
operator|.
name|NamespacePrefix
import|;
end_import

begin_annotation_defn
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Target
argument_list|(
block|{
name|ElementType
operator|.
name|FIELD
block|,
name|ElementType
operator|.
name|METHOD
block|,
name|ElementType
operator|.
name|PARAMETER
block|}
argument_list|)
annotation|@
name|LanguageAnnotation
argument_list|(
name|language
operator|=
literal|"xpath"
argument_list|,
name|factory
operator|=
name|XPathAnnotationExpressionFactory
operator|.
name|class
argument_list|)
DECL|annotation|MyXPath
specifier|public
annotation_defn|@interface
name|MyXPath
block|{
DECL|method|value ()
name|String
name|value
parameter_list|()
function_decl|;
comment|// You can add the namespaces as the default value of the annotation
DECL|method|namespaces ()
name|NamespacePrefix
index|[]
name|namespaces
argument_list|()
expr|default
block|{     @
name|NamespacePrefix
argument_list|(
name|prefix
operator|=
literal|"n1"
argument_list|,
name|uri
operator|=
literal|"http://example.org/ns1"
argument_list|)
block|,     @
name|NamespacePrefix
argument_list|(
name|prefix
operator|=
literal|"n2"
argument_list|,
name|uri
operator|=
literal|"http://example.org/ns2"
argument_list|)
block|}
expr_stmt|;
block|}
end_annotation_defn

begin_comment
comment|//END SNIPPET: example
end_comment

end_unit

