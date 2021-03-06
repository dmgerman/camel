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
name|Documented
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

begin_comment
comment|/**  * An annotation for injection of an XQuery expressions into a field, property, method or parameter when using  *<a href="http://camel.apache.org/bean-integration.html">Bean Integration</a>.  */
end_comment

begin_annotation_defn
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Documented
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
literal|"xquery"
argument_list|,
name|factory
operator|=
name|XQueryAnnotationExpressionFactory
operator|.
name|class
argument_list|)
DECL|annotation|XQuery
specifier|public
annotation_defn|@interface
name|XQuery
block|{
DECL|method|value ()
name|String
name|value
parameter_list|()
function_decl|;
DECL|method|stripsAllWhiteSpace ()
DECL|field|true
name|boolean
name|stripsAllWhiteSpace
parameter_list|()
default|default
literal|true
function_decl|;
DECL|method|namespaces ()
name|NamespacePrefix
index|[]
name|namespaces
argument_list|()
expr|default
block|{         @
name|NamespacePrefix
argument_list|(
name|prefix
operator|=
literal|"soap"
argument_list|,
name|uri
operator|=
literal|"http://www.w3.org/2003/05/soap-envelope"
argument_list|)
block|,         @
name|NamespacePrefix
argument_list|(
name|prefix
operator|=
literal|"xsd"
argument_list|,
name|uri
operator|=
literal|"http://www.w3.org/2001/XMLSchema"
argument_list|)
block|}
expr_stmt|;
comment|/**      * @return The name of the header we want to apply the Xquery expression to.      *  If this is empty then the Xquery expression will be applied to the body instead.      */
DECL|method|headerName ()
name|String
name|headerName
parameter_list|()
default|default
literal|""
function_decl|;
block|}
end_annotation_defn

end_unit

