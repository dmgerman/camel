begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
comment|/**   * Used to annotate the parameter of a {@see Fluent} method.  */
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
name|Target
argument_list|(
name|ElementType
operator|.
name|PARAMETER
argument_list|)
DECL|annotation|FluentArg
specifier|public
annotation_defn|@interface
name|FluentArg
block|{
DECL|method|value ()
name|String
name|value
parameter_list|()
function_decl|;
DECL|method|attribute ()
DECL|field|true
name|boolean
name|attribute
parameter_list|()
default|default
literal|true
function_decl|;
DECL|method|element ()
DECL|field|false
name|boolean
name|element
parameter_list|()
default|default
literal|false
function_decl|;
DECL|method|reference ()
DECL|field|false
name|boolean
name|reference
parameter_list|()
default|default
literal|false
function_decl|;
block|}
end_annotation_defn

end_unit

