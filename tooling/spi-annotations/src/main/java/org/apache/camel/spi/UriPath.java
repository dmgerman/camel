begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
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

begin_comment
comment|/**  * Represents an injection point of a Camel Uri path value (the remaining part of a Camel URI without any query arguments)  */
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
block|}
argument_list|)
DECL|annotation|UriPath
specifier|public
annotation_defn|@interface
name|UriPath
block|{
comment|/**      * Returns the name of the uri path.      *<p/>      * This can be used to name the uri path something meaningful, such as a<tt>directory</tt>,<tt>queueName</tt> etc.      *<p/>      * If this is not specified then the name of the field or property which has this annotation is used.      */
DECL|method|name ()
name|String
name|name
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * A human display name of the parameter.      *<p/>      * This is used for documentation and tooling only.      */
DECL|method|displayName ()
name|String
name|displayName
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * The default value of the parameter.      *<p/>      * Note that this attribute is only for documentation purpose. The default value in use at runtime is the value the Java field was assigned.      */
DECL|method|defaultValue ()
name|String
name|defaultValue
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * A special note about the default value.      *<p/>      * This can be used to document special cases about the default value.      */
DECL|method|defaultValueNote ()
name|String
name|defaultValueNote
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Returns a description of this uri path      *<p/>      * This is used for documentation and tooling only.      */
DECL|method|description ()
name|String
name|description
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Allows to define enums this options accepts.      *<p/>      * If the type is already an enum, then this option should not be used; instead you can use      * this option when the type is a String that only accept certain values.      *<p/>      * Multiple values is separated by comma.      */
DECL|method|enums ()
name|String
name|enums
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * To associate this path value with label(s).      *<p/>      * Multiple labels can be defined as a comma separated value.      *<p/>      * The labels is intended for grouping the parameters, such as<tt>consumer</tt>,<tt>producer</tt>,<tt>common</tt>,<tt>security</tt>, etc.      */
DECL|method|label ()
name|String
name|label
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Whether the option is secret/sensitive information such as a password.      */
DECL|method|secret ()
DECL|field|false
name|boolean
name|secret
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * To re-associate the preferred Java type of this parameter.      *<p/>      * This is used for parameters which are of a specialized type but can be configured by another Java type, such as from a String.      */
DECL|method|javaType ()
name|String
name|javaType
parameter_list|()
default|default
literal|""
function_decl|;
block|}
end_annotation_defn

end_unit

