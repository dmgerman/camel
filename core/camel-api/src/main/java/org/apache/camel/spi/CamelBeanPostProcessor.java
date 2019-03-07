begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * Bean post processor.  */
end_comment

begin_interface
DECL|interface|CamelBeanPostProcessor
specifier|public
interface|interface
name|CamelBeanPostProcessor
block|{
comment|/**      * Apply this post processor to the given new bean instance<i>before</i> any bean      * initialization callbacks (like<code>afterPropertiesSet</code>      * or a custom init-method). The bean will already be populated with property values.      * The returned bean instance may be a wrapper around the original.      *      * @param bean the new bean instance      * @param beanName the name of the bean      * @return the bean instance to use, either the original or a wrapped one; if      *<code>null</code>, no subsequent BeanPostProcessors will be invoked      * @throws Exception is thrown if error post processing bean      */
DECL|method|postProcessBeforeInitialization (Object bean, String beanName)
specifier|default
name|Object
name|postProcessBeforeInitialization
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|beanName
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|bean
return|;
block|}
comment|/**      * Apply this post processor to the given new bean instance<i>after</i> any bean      * initialization callbacks (like<code>afterPropertiesSet</code>      * or a custom init-method). The bean will already be populated with property values.      * The returned bean instance may be a wrapper around the original.      *      * @param bean the new bean instance      * @param beanName the name of the bean      * @return the bean instance to use, either the original or a wrapped one; if      *<code>null</code>, no subsequent BeanPostProcessors will be invoked      * @throws Exception is thrown if error post processing bean      */
DECL|method|postProcessAfterInitialization (Object bean, String beanName)
specifier|default
name|Object
name|postProcessAfterInitialization
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|beanName
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|bean
return|;
block|}
block|}
end_interface

end_unit

