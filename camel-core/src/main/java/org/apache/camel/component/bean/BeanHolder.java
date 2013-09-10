begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|NoSuchBeanException
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
comment|/**  * Object holder for a bean.  *  * @version   */
end_comment

begin_interface
DECL|interface|BeanHolder
specifier|public
interface|interface
name|BeanHolder
block|{
comment|/**      * Gets the bean.      *      * @throws NoSuchBeanException is thrown if the bean cannot be found.      */
DECL|method|getBean ()
name|Object
name|getBean
parameter_list|()
throws|throws
name|NoSuchBeanException
function_decl|;
comment|/**      * Gets a {@link Processor} for this bean, if supported.      *      * @return the {@link Processor}, or<tt>null</tt> if not supported.      */
DECL|method|getProcessor ()
name|Processor
name|getProcessor
parameter_list|()
function_decl|;
comment|/**      * Gets bean info for the bean.      */
DECL|method|getBeanInfo ()
name|BeanInfo
name|getBeanInfo
parameter_list|()
function_decl|;
comment|/**      * Gets bean info for the given bean.      *<p/>      * This implementation allows a thread safe usage for {@link BeanHolder} implementations      * such as the {@link RegistryBean}.      *      * @param bean the bean      * @return<tt>null</tt> if not supported, then use {@link #getBeanInfo()} instead.      */
DECL|method|getBeanInfo (Object bean)
name|BeanInfo
name|getBeanInfo
parameter_list|(
name|Object
name|bean
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

