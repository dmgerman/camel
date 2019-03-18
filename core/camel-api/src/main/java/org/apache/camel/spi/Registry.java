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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RuntimeCamelException
import|;
end_import

begin_comment
comment|/**  * Represents a {@link BeanRepository} which may also be capable  * of binding beans to its repository.  */
end_comment

begin_interface
DECL|interface|Registry
specifier|public
interface|interface
name|Registry
extends|extends
name|BeanRepository
block|{
comment|/**      * Binds the bean to the repository (if possible).      *      * @param id   the id of the bean      * @param bean the bean      * @throws RuntimeCamelException is thrown if binding is not possible      */
DECL|method|bind (String id, Object bean)
specifier|default
name|void
name|bind
parameter_list|(
name|String
name|id
parameter_list|,
name|Object
name|bean
parameter_list|)
throws|throws
name|RuntimeCamelException
block|{
name|bind
argument_list|(
name|id
argument_list|,
name|bean
operator|.
name|getClass
argument_list|()
argument_list|,
name|bean
argument_list|)
expr_stmt|;
block|}
comment|/**      * Binds the bean to the repository (if possible).      *<p/>      * Binding by id and type allows to bind multiple entries with the same      * id but with different type.      *      * @param id   the id of the bean      * @param type the type of the bean to associate the binding      * @param bean the bean      * @throws RuntimeCamelException is thrown if binding is not possible      */
DECL|method|bind (String id, Class<?> type, Object bean)
name|void
name|bind
parameter_list|(
name|String
name|id
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Object
name|bean
parameter_list|)
throws|throws
name|RuntimeCamelException
function_decl|;
comment|/**      * Strategy to wrap the value to be stored in the registry.      *      * @param value  the value      * @return the value to store      */
DECL|method|wrap (Object value)
specifier|default
name|Object
name|wrap
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|value
return|;
block|}
block|}
end_interface

end_unit

