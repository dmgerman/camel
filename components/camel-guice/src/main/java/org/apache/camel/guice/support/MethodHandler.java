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
name|InvocationTargetException
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

begin_comment
comment|/**  * Allows a method with a given annotation {@code A} on an injectee of type  * {@code I} to be processed in some way on each injectee using a custom  * strategy.  *   * @version  */
end_comment

begin_interface
DECL|interface|MethodHandler
specifier|public
interface|interface
name|MethodHandler
parameter_list|<
name|I
parameter_list|,
name|A
extends|extends
name|Annotation
parameter_list|>
block|{
comment|/**      * Process the given method which is annotated with the annotation on the      * injectee after the injectee has been injected      */
DECL|method|afterInjection (I injectee, A annotation, Method method)
name|void
name|afterInjection
parameter_list|(
name|I
name|injectee
parameter_list|,
name|A
name|annotation
parameter_list|,
name|Method
name|method
parameter_list|)
throws|throws
name|InvocationTargetException
throws|,
name|IllegalAccessException
function_decl|;
block|}
end_interface

end_unit

