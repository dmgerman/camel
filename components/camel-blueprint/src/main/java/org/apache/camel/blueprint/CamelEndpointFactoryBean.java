begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|blueprint
package|;
end_package

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
name|XmlRootElement
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
name|XmlTransient
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
name|Endpoint
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
name|core
operator|.
name|xml
operator|.
name|AbstractCamelEndpointFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|BlueprintContainer
import|;
end_import

begin_comment
comment|/**  * A factory which instantiates {@link Endpoint} objects  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"endpoint"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|CamelEndpointFactoryBean
specifier|public
class|class
name|CamelEndpointFactoryBean
extends|extends
name|AbstractCamelEndpointFactoryBean
block|{
annotation|@
name|XmlTransient
DECL|field|blueprintContainer
specifier|private
name|BlueprintContainer
name|blueprintContainer
decl_stmt|;
DECL|method|setBlueprintContainer (BlueprintContainer blueprintContainer)
specifier|public
name|void
name|setBlueprintContainer
parameter_list|(
name|BlueprintContainer
name|blueprintContainer
parameter_list|)
block|{
name|this
operator|.
name|blueprintContainer
operator|=
name|blueprintContainer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCamelContextWithId (String camelContextId)
specifier|protected
name|CamelContext
name|getCamelContextWithId
parameter_list|(
name|String
name|camelContextId
parameter_list|)
block|{
if|if
condition|(
name|blueprintContainer
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|CamelContext
operator|)
name|blueprintContainer
operator|.
name|getComponentInstance
argument_list|(
name|camelContextId
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

