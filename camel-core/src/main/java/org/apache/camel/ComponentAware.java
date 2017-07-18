begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * An interface to represent an object which wishes to be injected with  * a {@link Component}.  */
end_comment

begin_interface
DECL|interface|ComponentAware
specifier|public
interface|interface
name|ComponentAware
block|{
comment|/**      * Injects the {@link Component}      *      * @param component the component      */
DECL|method|setComponent (Component component)
name|void
name|setComponent
parameter_list|(
name|Component
name|component
parameter_list|)
function_decl|;
comment|/**      * Get the {@link Component}      *      * @return the component      */
DECL|method|getComponent ()
name|Component
name|getComponent
parameter_list|()
function_decl|;
comment|/**      * Get the {@link Component} as the specified type.      *      * @param type the proprietary class or interface of the underlying concrete Component.      * @return an instance of the underlying concrete Component as the required type.      * @throws IllegalArgumentException if the component class can't be cast to required type,      */
DECL|method|getComponent (Class<T> type)
specifier|default
parameter_list|<
name|T
extends|extends
name|Component
parameter_list|>
name|T
name|getComponent
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
specifier|final
name|Component
name|component
init|=
name|getComponent
argument_list|()
decl_stmt|;
if|if
condition|(
name|component
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|Component
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|component
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to unwrap the Component type ("
operator|+
name|component
operator|.
name|getClass
argument_list|()
operator|+
literal|") to the required type ("
operator|+
name|type
operator|+
literal|")"
argument_list|)
throw|;
block|}
block|}
end_interface

end_unit

