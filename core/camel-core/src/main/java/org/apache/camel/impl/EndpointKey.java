begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ValueHolder
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
name|util
operator|.
name|StringHelper
import|;
end_import

begin_comment
comment|/**  * Key used in {@link DefaultEndpointRegistry} in {@link DefaultCamelContext},  * to ensure a consistent lookup.  */
end_comment

begin_class
DECL|class|EndpointKey
specifier|public
specifier|final
class|class
name|EndpointKey
extends|extends
name|ValueHolder
argument_list|<
name|String
argument_list|>
block|{
DECL|method|EndpointKey (String uri)
specifier|public
name|EndpointKey
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|this
argument_list|(
name|uri
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Optimized when the uri is already normalized.      */
DECL|method|EndpointKey (String uri, boolean normalized)
specifier|public
name|EndpointKey
parameter_list|(
name|String
name|uri
parameter_list|,
name|boolean
name|normalized
parameter_list|)
block|{
name|super
argument_list|(
name|normalized
condition|?
name|uri
else|:
name|DefaultCamelContext
operator|.
name|normalizeEndpointUri
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|uri
argument_list|,
literal|"uri"
argument_list|)
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
name|get
argument_list|()
return|;
block|}
block|}
end_class

end_unit

