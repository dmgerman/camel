begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|spi
operator|.
name|HasGroup
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
name|HasId
import|;
end_import

begin_interface
DECL|interface|HealthCheck
specifier|public
interface|interface
name|HealthCheck
extends|extends
name|HasGroup
extends|,
name|HasId
extends|,
name|Ordered
block|{
DECL|enum|State
enum|enum
name|State
block|{
DECL|enumConstant|UP
name|UP
block|,
DECL|enumConstant|DOWN
name|DOWN
block|,
DECL|enumConstant|UNKNOWN
name|UNKNOWN
block|}
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
comment|/**      * Return meta data associated with this {@link HealthCheck}.      */
DECL|method|getMetaData ()
specifier|default
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getMetaData
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
comment|/**      * Return the configuration associated with this {@link HealthCheck}.      */
DECL|method|getConfiguration ()
name|HealthCheckConfiguration
name|getConfiguration
parameter_list|()
function_decl|;
comment|/**      * Invoke the check.      *      * @see {@link #call(Map)}      */
DECL|method|call ()
specifier|default
name|Result
name|call
parameter_list|()
block|{
return|return
name|call
argument_list|(
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Invoke the check. The implementation is responsible to eventually perform      * the check according to the limitation of the third party system i.e.      * it should not be performed too often to avoid rate limiting. The options      * argument can be used to pass information specific to the check like      * forcing the check to be performed against the policies. The implementation      * is responsible to catch an handle any exception thrown by the underlying      * technology, including unchecked ones.      */
DECL|method|call (Map<String, Object> options)
name|Result
name|call
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
function_decl|;
comment|/**      * Response to an health check invocation.      */
DECL|interface|Result
interface|interface
name|Result
block|{
comment|/**          * The {@link HealthCheck} associated to this response.          */
DECL|method|getCheck ()
name|HealthCheck
name|getCheck
parameter_list|()
function_decl|;
comment|/**          * The state of the service.          */
DECL|method|getState ()
name|State
name|getState
parameter_list|()
function_decl|;
comment|/**          * A message associated to the result, used to provide more information          * for unhealthy services.          */
DECL|method|getMessage ()
name|Optional
argument_list|<
name|String
argument_list|>
name|getMessage
parameter_list|()
function_decl|;
comment|/**          * An error associated to the result, used to provide the error associated          * to unhealthy services.          */
DECL|method|getError ()
name|Optional
argument_list|<
name|Throwable
argument_list|>
name|getError
parameter_list|()
function_decl|;
comment|/**          * An key/value combination of details.          *          * @return a non null details map          */
DECL|method|getDetails ()
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getDetails
parameter_list|()
function_decl|;
block|}
block|}
end_interface

end_unit

