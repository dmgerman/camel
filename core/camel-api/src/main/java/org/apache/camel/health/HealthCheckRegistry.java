begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.health
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|health
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
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * A registry for health checks.  *<p>  * Note that this registry can be superseded by the future camel context internal  * registry, @see<a href="https://issues.apache.org/jira/browse/CAMEL-10792"/>.  */
end_comment

begin_interface
DECL|interface|HealthCheckRegistry
specifier|public
interface|interface
name|HealthCheckRegistry
extends|extends
name|HealthCheckRepository
extends|,
name|CamelContextAware
block|{
comment|/**      * Registers a service {@link HealthCheck}.      */
DECL|method|register (HealthCheck check)
name|boolean
name|register
parameter_list|(
name|HealthCheck
name|check
parameter_list|)
function_decl|;
comment|/**      * Unregisters a service {@link HealthCheck}.      */
DECL|method|unregister (HealthCheck check)
name|boolean
name|unregister
parameter_list|(
name|HealthCheck
name|check
parameter_list|)
function_decl|;
comment|/**      * Set the health check repositories to use..      */
DECL|method|setRepositories (Collection<HealthCheckRepository> repositories)
name|void
name|setRepositories
parameter_list|(
name|Collection
argument_list|<
name|HealthCheckRepository
argument_list|>
name|repositories
parameter_list|)
function_decl|;
comment|/**      * Get a collection of health check repositories.      */
DECL|method|getRepositories ()
name|Collection
argument_list|<
name|HealthCheckRepository
argument_list|>
name|getRepositories
parameter_list|()
function_decl|;
comment|/**      * Add an Health Check repository.      */
DECL|method|addRepository (HealthCheckRepository repository)
name|boolean
name|addRepository
parameter_list|(
name|HealthCheckRepository
name|repository
parameter_list|)
function_decl|;
comment|/**      * Remove an Health Check repository.      */
DECL|method|removeRepository (HealthCheckRepository repository)
name|boolean
name|removeRepository
parameter_list|(
name|HealthCheckRepository
name|repository
parameter_list|)
function_decl|;
comment|/**      * A collection of health check IDs.      */
DECL|method|getCheckIDs ()
specifier|default
name|Collection
argument_list|<
name|String
argument_list|>
name|getCheckIDs
parameter_list|()
block|{
return|return
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|HealthCheck
operator|::
name|getId
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns the check identified by the given<code>id</code> if available.      */
DECL|method|getCheck (String id)
specifier|default
name|Optional
argument_list|<
name|HealthCheck
argument_list|>
name|getCheck
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|check
lambda|->
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|check
operator|.
name|getId
argument_list|()
argument_list|,
name|id
argument_list|)
argument_list|)
operator|.
name|findFirst
argument_list|()
return|;
block|}
comment|/**      * Returns an optional {@link HealthCheckRegistry}, by default no registry is      * present and it must be explicit activated. Components can register/unregister      * health checks in response to life-cycle events (i.e. start/stop).      *      * This registry is not used by the camel context but it is up to the impl to      * properly use it, i.e.      *      * - a RouteController could use the registry to decide to restart a route      *   with failing health checks      * - spring boot could integrate such checks within its health endpoint or      *   make it available only as separate endpoint.      */
DECL|method|get (CamelContext context)
specifier|static
name|HealthCheckRegistry
name|get
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
return|return
name|context
operator|.
name|getExtension
argument_list|(
name|HealthCheckRegistry
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

