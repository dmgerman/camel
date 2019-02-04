begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cloud
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

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
name|Optional
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
name|CamelContextAware
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
name|Ordered
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
name|Service
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
name|IdAware
import|;
end_import

begin_interface
DECL|interface|ServiceRegistry
specifier|public
interface|interface
name|ServiceRegistry
extends|extends
name|Service
extends|,
name|CamelContextAware
extends|,
name|IdAware
extends|,
name|Ordered
block|{
annotation|@
name|Override
DECL|method|getOrder ()
specifier|default
name|int
name|getOrder
parameter_list|()
block|{
return|return
name|Ordered
operator|.
name|LOWEST
return|;
block|}
comment|/**      * Attributes associated to the service.      */
DECL|method|getAttributes ()
specifier|default
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getAttributes
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
comment|/**      * Register the service definition.      *      * @param definition the service definition      */
DECL|method|register (ServiceDefinition definition)
name|void
name|register
parameter_list|(
name|ServiceDefinition
name|definition
parameter_list|)
function_decl|;
comment|/**      * Remove the service definition.      *      * @param definition the service definition      */
DECL|method|deregister (ServiceDefinition definition)
name|void
name|deregister
parameter_list|(
name|ServiceDefinition
name|definition
parameter_list|)
function_decl|;
comment|/**      * A selector used to pick up a service among a list.      */
annotation|@
name|FunctionalInterface
DECL|interface|Selector
interface|interface
name|Selector
block|{
comment|/**          * Select a specific ServiceRegistry instance among a collection.          */
DECL|method|select (Collection<ServiceRegistry> services)
name|Optional
argument_list|<
name|ServiceRegistry
argument_list|>
name|select
parameter_list|(
name|Collection
argument_list|<
name|ServiceRegistry
argument_list|>
name|services
parameter_list|)
function_decl|;
block|}
block|}
end_interface

end_unit

