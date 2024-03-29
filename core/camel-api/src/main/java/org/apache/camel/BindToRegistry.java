begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
comment|/**  * Used for binding a bean to the registry.  *  * This annotation is not supported with camel-spring or camel-spring-boot as they have  * their own set of annotations for registering beans in spring bean registry.  * Instead this annotation is intended for Camel standalone such as camel-main or camel-quarkus  * or similar runtimes.  *  * If no name is specified then the bean will have its name auto computed based on the  * class name, field name, or method name where the annotation is configured.  */
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
name|TYPE
block|,
name|ElementType
operator|.
name|FIELD
block|,
name|ElementType
operator|.
name|METHOD
block|}
argument_list|)
DECL|annotation|BindToRegistry
specifier|public
annotation_defn|@interface
name|BindToRegistry
block|{
comment|/**      * The name of the bean      */
DECL|method|value ()
name|String
name|value
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Whether to perform bean post processing (dependency injection) on the bean      */
DECL|method|beanPostProcess ()
DECL|field|false
name|boolean
name|beanPostProcess
parameter_list|()
default|default
literal|false
function_decl|;
block|}
end_annotation_defn

end_unit

