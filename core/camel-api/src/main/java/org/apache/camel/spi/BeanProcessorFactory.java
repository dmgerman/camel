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
name|reflect
operator|.
name|Method
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
name|Processor
import|;
end_import

begin_comment
comment|/**  * Factory for creating a {@link Processor} that can invoke a method on a bean and supporting using Camel  * bean parameter bindings.  *<p/>  * This requires to have camel-bean on the classpath.  */
end_comment

begin_interface
DECL|interface|BeanProcessorFactory
specifier|public
interface|interface
name|BeanProcessorFactory
block|{
comment|/**      * Creates the bean processor      *      * @param camelContext  the camel context      * @param pojo          the bean      * @param method        the method to invoke      * @return the created processor      * @throws Exception is thrown if error creating the processor      */
DECL|method|createBeanProcessor (CamelContext camelContext, Object pojo, Method method)
name|Processor
name|createBeanProcessor
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Object
name|pojo
parameter_list|,
name|Method
name|method
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

