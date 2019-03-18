begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.extension
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|extension
package|;
end_package

begin_comment
comment|/**  * Marker interface for component extensions.  *<p/>  * An extension is a feature provided by the component such as ComponentVerifier,  * ServiceMetaData.  */
end_comment

begin_interface
DECL|interface|ComponentExtension
specifier|public
interface|interface
name|ComponentExtension
block|{
comment|/**      * Access the underlying concrete ComponentExtension implementation to      * provide access to further features.      *      * @param clazz the proprietary class or interface of the underlying concrete ComponentExtension.      * @return an instance of the underlying concrete ComponentExtension as the required type.      */
DECL|method|unwrap (Class<T> clazz)
specifier|default
parameter_list|<
name|T
extends|extends
name|ComponentExtension
parameter_list|>
name|T
name|unwrap
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
if|if
condition|(
name|ComponentExtension
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|this
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to unwrap this ComponentExtension type ("
operator|+
name|getClass
argument_list|()
operator|+
literal|") to the required type ("
operator|+
name|clazz
operator|+
literal|")"
argument_list|)
throw|;
block|}
block|}
end_interface

end_unit

