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
comment|/**      * Creates the bean processor from the existing bean instance      *      * @param camelContext  the camel context      * @param bean          the bean      * @param method        the method to invoke      * @return the created processor      * @throws Exception is thrown if error creating the processor      */
DECL|method|createBeanProcessor (CamelContext camelContext, Object bean, Method method)
name|Processor
name|createBeanProcessor
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Object
name|bean
parameter_list|,
name|Method
name|method
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Creates the bean processor from a given set of parameters that can refer      * to the bean via an existing bean, a reference to a bean, or its class name etc.      *      * @param camelContext  the camel context      * @param bean          the bean instance      * @param beanType      or the bean class name      * @param beanClass     or the bean class      * @param ref           or bean reference to lookup the bean from the registry      * @param method        optional name of method to invoke      * @param cacheBean    whether to cache lookup up the bean      * @return the created processor      * @throws Exception is thrown if error creating the processor      */
DECL|method|createBeanProcessor (CamelContext camelContext, Object bean, String beanType, Class<?> beanClass, String ref, String method, boolean cacheBean)
specifier|public
name|Processor
name|createBeanProcessor
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Object
name|bean
parameter_list|,
name|String
name|beanType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|beanClass
parameter_list|,
name|String
name|ref
parameter_list|,
name|String
name|method
parameter_list|,
name|boolean
name|cacheBean
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

