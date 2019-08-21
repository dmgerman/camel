begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|Metadata
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
name|CollectionStringBuffer
import|;
end_import

begin_comment
comment|/**  * Route to be executed when Hystrix EIP executes fallback  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"eip,routing,circuitbreaker"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"onFallback"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|OnFallbackDefinition
specifier|public
class|class
name|OnFallbackDefinition
extends|extends
name|OutputDefinition
argument_list|<
name|OnFallbackDefinition
argument_list|>
block|{
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"command"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|fallbackViaNetwork
specifier|private
name|Boolean
name|fallbackViaNetwork
decl_stmt|;
DECL|method|OnFallbackDefinition ()
specifier|public
name|OnFallbackDefinition
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|fallbackViaNetwork
operator|!=
literal|null
operator|&&
name|fallbackViaNetwork
condition|)
block|{
return|return
literal|"OnFallbackViaNetwork["
operator|+
name|getOutputs
argument_list|()
operator|+
literal|"]"
return|;
block|}
else|else
block|{
return|return
literal|"OnFallback["
operator|+
name|getOutputs
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"onFallback"
return|;
block|}
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
name|String
name|name
init|=
name|fallbackViaNetwork
operator|!=
literal|null
operator|&&
name|fallbackViaNetwork
condition|?
literal|"onFallbackViaNetwork"
else|:
literal|"onFallback"
decl_stmt|;
name|CollectionStringBuffer
name|buffer
init|=
operator|new
name|CollectionStringBuffer
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"["
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|list
init|=
name|getOutputs
argument_list|()
decl_stmt|;
for|for
control|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|type
range|:
name|list
control|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|type
operator|.
name|getLabel
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getFallbackViaNetwork ()
specifier|public
name|Boolean
name|getFallbackViaNetwork
parameter_list|()
block|{
return|return
name|fallbackViaNetwork
return|;
block|}
comment|/**      * Whether the fallback goes over the network.      *<p/>      * If the fallback will go over the network it is another possible point of      * failure and so it also needs to be wrapped by a HystrixCommand. It is      * important to execute the fallback command on a separate thread-pool,      * otherwise if the main command were to become latent and fill the      * thread-pool this would prevent the fallback from running if the two      * commands share the same pool.      */
DECL|method|setFallbackViaNetwork (Boolean fallbackViaNetwork)
specifier|public
name|void
name|setFallbackViaNetwork
parameter_list|(
name|Boolean
name|fallbackViaNetwork
parameter_list|)
block|{
name|this
operator|.
name|fallbackViaNetwork
operator|=
name|fallbackViaNetwork
expr_stmt|;
block|}
DECL|method|isFallbackViaNetwork ()
specifier|public
name|boolean
name|isFallbackViaNetwork
parameter_list|()
block|{
comment|// is default false
return|return
name|fallbackViaNetwork
operator|!=
literal|null
operator|&&
name|fallbackViaNetwork
return|;
block|}
block|}
end_class

end_unit

