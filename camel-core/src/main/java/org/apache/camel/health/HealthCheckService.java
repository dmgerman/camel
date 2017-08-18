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
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|BiConsumer
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
name|Service
import|;
end_import

begin_comment
comment|/**  * An health check service that invokes the checks registered on the {@link HealthCheckRegistry}  * according to a schedule.  */
end_comment

begin_interface
DECL|interface|HealthCheckService
specifier|public
interface|interface
name|HealthCheckService
extends|extends
name|Service
extends|,
name|CamelContextAware
block|{
comment|/**      * Add a listener to invoke when the state of a check change.      *      * @param consumer the event listener.      */
DECL|method|addStateChangeListener (BiConsumer<HealthCheck.State, HealthCheck> consumer)
name|void
name|addStateChangeListener
parameter_list|(
name|BiConsumer
argument_list|<
name|HealthCheck
operator|.
name|State
argument_list|,
name|HealthCheck
argument_list|>
name|consumer
parameter_list|)
function_decl|;
comment|/**      * Remove the state change listener.      *      * @param consumer the event listener to remove.      */
DECL|method|removeStateChangeListener (BiConsumer<HealthCheck.State, HealthCheck> consumer)
name|void
name|removeStateChangeListener
parameter_list|(
name|BiConsumer
argument_list|<
name|HealthCheck
operator|.
name|State
argument_list|,
name|HealthCheck
argument_list|>
name|consumer
parameter_list|)
function_decl|;
comment|/**      * Sets the options to be used when invoking the check identified by the      * given id.      *      * @param id the health check id.      * @param options the health check options.      */
DECL|method|setHealthCheckOptions (String id, Map<String, Object> options)
name|void
name|setHealthCheckOptions
parameter_list|(
name|String
name|id
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
function_decl|;
comment|/**      * @see {@link #call(String, Map)}      *      * @param id the health check id.      * @return the result of the check or {@link Optional#empty()} if the id is unknown.      */
DECL|method|call (String id)
specifier|default
name|Optional
argument_list|<
name|HealthCheck
operator|.
name|Result
argument_list|>
name|call
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
name|call
argument_list|(
name|id
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Invokes the check identified by the given<code>id</code> with the given      *<code>options</code>.      *      * @param id the health check id.      * @param options the health check options.      * @return the result of the check or {@link Optional#empty()} if the id is unknown.      */
DECL|method|call (String id, Map<String, Object> options)
name|Optional
argument_list|<
name|HealthCheck
operator|.
name|Result
argument_list|>
name|call
parameter_list|(
name|String
name|id
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
function_decl|;
comment|/**      * Notify the service that a check has changed status. This may be useful for      * stateful checks like checks rely on tcp/ip connections.      *      * @param check the health check.      * @param result the health check result.      */
DECL|method|notify (HealthCheck check, HealthCheck.Result result)
name|void
name|notify
parameter_list|(
name|HealthCheck
name|check
parameter_list|,
name|HealthCheck
operator|.
name|Result
name|result
parameter_list|)
function_decl|;
comment|/**      * Return a list of the known checks status.      *      * @return the list of results.      */
DECL|method|getResults ()
name|Collection
argument_list|<
name|HealthCheck
operator|.
name|Result
argument_list|>
name|getResults
parameter_list|()
function_decl|;
comment|/**      * Access the underlying concrete HealthCheckService implementation to      * provide access to further features.      *      * @param clazz the proprietary class or interface of the underlying concrete HealthCheckService.      * @return an instance of the underlying concrete HealthCheckService as the required type.      */
DECL|method|unwrap (Class<T> clazz)
specifier|default
parameter_list|<
name|T
extends|extends
name|HealthCheckService
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
name|HealthCheckService
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
literal|"Unable to unwrap this HealthCheckService type ("
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

