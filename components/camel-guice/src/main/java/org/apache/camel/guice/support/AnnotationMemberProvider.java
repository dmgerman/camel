begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.guice.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|guice
operator|.
name|support
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
name|Field
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|TypeLiteral
import|;
end_import

begin_comment
comment|/**  * A provider of an annotation based injection point which can use the value of  * an annotation together with the member on which the annotation is placed to  * determine the value.  *   * @version  */
end_comment

begin_interface
DECL|interface|AnnotationMemberProvider
specifier|public
interface|interface
name|AnnotationMemberProvider
parameter_list|<
name|A
extends|extends
name|Annotation
parameter_list|>
block|{
comment|/** Returns the value to be injected for the given annotated field */
DECL|method|provide (A annotation, TypeLiteral<?> type, Field field)
name|Object
name|provide
parameter_list|(
name|A
name|annotation
parameter_list|,
name|TypeLiteral
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Field
name|field
parameter_list|)
function_decl|;
comment|/**      * Returns the value to be injected for the given annotated method parameter      * value      */
DECL|method|provide (A annotation, TypeLiteral<?> type, Method method, Class<?> parameterType, int parameterIndex)
name|Object
name|provide
parameter_list|(
name|A
name|annotation
parameter_list|,
name|TypeLiteral
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Method
name|method
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|parameterType
parameter_list|,
name|int
name|parameterIndex
parameter_list|)
function_decl|;
comment|/** Returns true if the given parameter on the annotated method can be null */
DECL|method|isNullParameterAllowed (A annotation, Method method, Class<?> parameterType, int parameterIndex)
name|boolean
name|isNullParameterAllowed
parameter_list|(
name|A
name|annotation
parameter_list|,
name|Method
name|method
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|parameterType
parameter_list|,
name|int
name|parameterIndex
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

