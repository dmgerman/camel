begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.jsonpath
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|jsonpath
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
name|com
operator|.
name|jayway
operator|.
name|jsonpath
operator|.
name|Option
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
comment|/**  * An annotation used to inject a<a href="http://commons.apache.org/jsonpath/">JsonPath</a>  * expression into a method parameter when using  *<a href="http://camel.apache.org/bean-integration.html">Bean Integration</a>  */
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
literal|"jsonpath"
argument_list|,
name|factory
operator|=
name|JsonPathAnnotationExpressionFactory
operator|.
name|class
argument_list|)
DECL|annotation|JsonPath
specifier|public
annotation_defn|@interface
name|JsonPath
block|{
DECL|method|value ()
name|String
name|value
parameter_list|()
function_decl|;
comment|/**      * Whether to suppress exceptions such as PathNotFoundException      */
DECL|method|suppressExceptions ()
DECL|field|false
name|boolean
name|suppressExceptions
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * Whether to allow in inlined simple exceptions in the JsonPath expression      */
DECL|method|allowSimple ()
DECL|field|true
name|boolean
name|allowSimple
parameter_list|()
default|default
literal|true
function_decl|;
comment|/**      * To configure the JsonPath options to use      */
DECL|method|options ()
name|Option
index|[]
name|options
argument_list|()
expr|default
block|{}
expr_stmt|;
block|}
end_annotation_defn

end_unit

