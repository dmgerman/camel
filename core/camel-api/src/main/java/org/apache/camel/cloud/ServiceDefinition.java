begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Map
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
comment|/**  * Represents a Service.  *  * @see ServiceChooser  * @see ServiceDiscovery  */
end_comment

begin_interface
DECL|interface|ServiceDefinition
specifier|public
interface|interface
name|ServiceDefinition
block|{
DECL|field|SERVICE_META_PREFIX
name|String
name|SERVICE_META_PREFIX
init|=
literal|"service."
decl_stmt|;
comment|// default service meta-data keys
DECL|field|SERVICE_META_ID
name|String
name|SERVICE_META_ID
init|=
literal|"service.id"
decl_stmt|;
DECL|field|SERVICE_META_NAME
name|String
name|SERVICE_META_NAME
init|=
literal|"service.name"
decl_stmt|;
DECL|field|SERVICE_META_HOST
name|String
name|SERVICE_META_HOST
init|=
literal|"service.host"
decl_stmt|;
DECL|field|SERVICE_META_PORT
name|String
name|SERVICE_META_PORT
init|=
literal|"service.port"
decl_stmt|;
DECL|field|SERVICE_META_ZONE
name|String
name|SERVICE_META_ZONE
init|=
literal|"service.zone"
decl_stmt|;
DECL|field|SERVICE_META_PROTOCOL
name|String
name|SERVICE_META_PROTOCOL
init|=
literal|"service.protocol"
decl_stmt|;
DECL|field|SERVICE_META_PATH
name|String
name|SERVICE_META_PATH
init|=
literal|"service.path"
decl_stmt|;
comment|/**      * Gets the service id.      */
DECL|method|getId ()
name|String
name|getId
parameter_list|()
function_decl|;
comment|/**      * Gets the service name.      */
DECL|method|getName ()
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Gets the IP or hostname of the server hosting the service.      */
DECL|method|getHost ()
name|String
name|getHost
parameter_list|()
function_decl|;
comment|/**      * Gets the port number of the server hosting the service.      */
DECL|method|getPort ()
name|int
name|getPort
parameter_list|()
function_decl|;
comment|/**      * Gets the health.      */
DECL|method|getHealth ()
name|ServiceHealth
name|getHealth
parameter_list|()
function_decl|;
comment|/**      * Gets a key/value metadata associated with the service.      */
DECL|method|getMetadata ()
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getMetadata
parameter_list|()
function_decl|;
comment|/**      * Check if a service definition matches.      */
DECL|method|matches (ServiceDefinition other)
specifier|default
name|boolean
name|matches
parameter_list|(
name|ServiceDefinition
name|other
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|equals
argument_list|(
name|other
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|getPort
argument_list|()
operator|==
name|other
operator|.
name|getPort
argument_list|()
operator|&&
name|StringHelper
operator|.
name|matches
argument_list|(
name|getName
argument_list|()
argument_list|,
name|other
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|StringHelper
operator|.
name|matches
argument_list|(
name|getId
argument_list|()
argument_list|,
name|other
operator|.
name|getId
argument_list|()
argument_list|)
operator|&&
name|StringHelper
operator|.
name|matches
argument_list|(
name|getHost
argument_list|()
argument_list|,
name|other
operator|.
name|getHost
argument_list|()
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

