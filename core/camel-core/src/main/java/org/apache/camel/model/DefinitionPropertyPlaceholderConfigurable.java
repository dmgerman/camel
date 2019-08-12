begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p>  * http://www.apache.org/licenses/LICENSE-2.0  *<p>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
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
name|model
operator|.
name|placeholder
operator|.
name|DefinitionPropertiesPlaceholderProviderHelper
import|;
end_import

begin_comment
comment|/**  * To be used for configuring property placeholder options on the EIP models.  */
end_comment

begin_interface
DECL|interface|DefinitionPropertyPlaceholderConfigurable
specifier|public
interface|interface
name|DefinitionPropertyPlaceholderConfigurable
block|{
comment|/**      * Gets the options on the model definition which supports property placeholders and can be resolved.      * This will be all the string based options.      *      * @return key/values of options      */
DECL|method|getReadPropertyPlaceholderOptions (CamelContext camelContext)
specifier|default
name|Map
argument_list|<
name|String
argument_list|,
name|Supplier
argument_list|<
name|String
argument_list|>
argument_list|>
name|getReadPropertyPlaceholderOptions
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|DefinitionPropertyPlaceholderConfigurable
name|aware
init|=
name|DefinitionPropertiesPlaceholderProviderHelper
operator|.
name|provider
argument_list|(
name|this
argument_list|)
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
decl_stmt|;
return|return
name|aware
operator|!=
literal|null
condition|?
name|aware
operator|.
name|getReadPropertyPlaceholderOptions
argument_list|(
name|camelContext
argument_list|)
else|:
literal|null
return|;
block|}
comment|/**      * To update an existing property using the function with the key/value and returning the changed value      * This will be all the string based options.      */
DECL|method|getWritePropertyPlaceholderOptions (CamelContext camelContext)
specifier|default
name|Map
argument_list|<
name|String
argument_list|,
name|Consumer
argument_list|<
name|String
argument_list|>
argument_list|>
name|getWritePropertyPlaceholderOptions
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|DefinitionPropertyPlaceholderConfigurable
name|aware
init|=
name|DefinitionPropertiesPlaceholderProviderHelper
operator|.
name|provider
argument_list|(
name|this
argument_list|)
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
decl_stmt|;
return|return
name|aware
operator|!=
literal|null
condition|?
name|aware
operator|.
name|getWritePropertyPlaceholderOptions
argument_list|(
name|camelContext
argument_list|)
else|:
literal|null
return|;
block|}
block|}
end_interface

end_unit

