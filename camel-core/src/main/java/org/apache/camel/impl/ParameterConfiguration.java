begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|Field
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
name|spi
operator|.
name|UriParam
import|;
end_import

begin_comment
comment|/**  * Represents the configuration of a URI query parameter value to allow type conversion  * and better validation of the configuration of URIs and Endpoints  */
end_comment

begin_class
DECL|class|ParameterConfiguration
specifier|public
class|class
name|ParameterConfiguration
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|parameterType
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|parameterType
decl_stmt|;
DECL|method|ParameterConfiguration (String name, Class<?> parameterType)
specifier|public
name|ParameterConfiguration
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|parameterType
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|parameterType
operator|=
name|parameterType
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ParameterConfiguration["
operator|+
name|name
operator|+
literal|" on "
operator|+
name|parameterType
operator|+
literal|"]"
return|;
block|}
comment|/**      * Returns the name of the parameter value      */
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * Returns the type of the parameter value      */
DECL|method|getParameterType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getParameterType
parameter_list|()
block|{
return|return
name|parameterType
return|;
block|}
comment|/**      * Factory method to create a new ParameterConfiguration from a field      */
DECL|method|newInstance (String name, Field field, UriParam uriParam)
specifier|public
specifier|static
name|ParameterConfiguration
name|newInstance
parameter_list|(
name|String
name|name
parameter_list|,
name|Field
name|field
parameter_list|,
name|UriParam
name|uriParam
parameter_list|)
block|{
return|return
operator|new
name|AnnotatedParameterConfiguration
argument_list|(
name|name
argument_list|,
name|field
operator|.
name|getType
argument_list|()
argument_list|,
name|field
argument_list|)
return|;
block|}
block|}
end_class

end_unit

