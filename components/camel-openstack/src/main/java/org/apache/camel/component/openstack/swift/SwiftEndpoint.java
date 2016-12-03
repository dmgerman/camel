begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.openstack.swift
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|openstack
operator|.
name|swift
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
name|Producer
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
name|component
operator|.
name|openstack
operator|.
name|common
operator|.
name|AbstractOpenstackEndpoint
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
name|component
operator|.
name|openstack
operator|.
name|swift
operator|.
name|producer
operator|.
name|ContainerProducer
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
name|component
operator|.
name|openstack
operator|.
name|swift
operator|.
name|producer
operator|.
name|ObjectProducer
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
name|spi
operator|.
name|UriEndpoint
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
name|UriPath
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openstack4j
operator|.
name|core
operator|.
name|transport
operator|.
name|Config
import|;
end_import

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"openstack-swift"
argument_list|,
name|title
operator|=
literal|"OpenStack-Swift"
argument_list|,
name|syntax
operator|=
literal|"openstack-swift:host"
argument_list|,
name|label
operator|=
literal|"cloud, virtualization"
argument_list|)
DECL|class|SwiftEndpoint
specifier|public
class|class
name|SwiftEndpoint
extends|extends
name|AbstractOpenstackEndpoint
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"default"
argument_list|)
DECL|field|domain
specifier|private
name|String
name|domain
init|=
literal|"default"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|enums
operator|=
literal|"objects, containers"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|subsystem
name|String
name|subsystem
decl_stmt|;
annotation|@
name|UriParam
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|project
specifier|private
name|String
name|project
decl_stmt|;
annotation|@
name|UriParam
DECL|field|operation
specifier|private
name|String
name|operation
decl_stmt|;
annotation|@
name|UriParam
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
annotation|@
name|UriParam
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|UriParam
DECL|field|config
specifier|private
name|Config
name|config
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
name|v3
argument_list|,
name|enums
operator|=
literal|"v2, v3"
argument_list|)
DECL|field|apiVersion
specifier|private
name|String
name|apiVersion
init|=
name|v3
decl_stmt|;
DECL|method|SwiftEndpoint (String uri, SwiftComponent component)
specifier|public
name|SwiftEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|SwiftComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
switch|switch
condition|(
name|subsystem
condition|)
block|{
case|case
name|SwiftConstants
operator|.
name|SWIFT_SUBSYSTEM_OBJECTS
case|:
return|return
operator|new
name|ObjectProducer
argument_list|(
name|this
argument_list|,
name|createClient
argument_list|()
argument_list|)
return|;
case|case
name|SwiftConstants
operator|.
name|SWIFT_SUBSYSTEM_CONTAINERS
case|:
return|return
operator|new
name|ContainerProducer
argument_list|(
name|this
argument_list|,
name|createClient
argument_list|()
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Can't create producer with subsystem "
operator|+
name|subsystem
argument_list|)
throw|;
block|}
block|}
DECL|method|getSubsystem ()
specifier|public
name|String
name|getSubsystem
parameter_list|()
block|{
return|return
name|subsystem
return|;
block|}
comment|/** 	 * OpenStack Swift subsystem 	 */
DECL|method|setSubsystem (String subsystem)
specifier|public
name|void
name|setSubsystem
parameter_list|(
name|String
name|subsystem
parameter_list|)
block|{
name|this
operator|.
name|subsystem
operator|=
name|subsystem
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDomain ()
specifier|public
name|String
name|getDomain
parameter_list|()
block|{
return|return
name|domain
return|;
block|}
comment|/** 	 * Authentication domain 	 */
DECL|method|setDomain (String domain)
specifier|public
name|void
name|setDomain
parameter_list|(
name|String
name|domain
parameter_list|)
block|{
name|this
operator|.
name|domain
operator|=
name|domain
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getProject ()
specifier|public
name|String
name|getProject
parameter_list|()
block|{
return|return
name|project
return|;
block|}
comment|/** 	 * The project ID 	 */
DECL|method|setProject (String project)
specifier|public
name|void
name|setProject
parameter_list|(
name|String
name|project
parameter_list|)
block|{
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getOperation ()
specifier|public
name|String
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
comment|/** 	 * The operation to do 	 */
DECL|method|setOperation (String operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
comment|/** 	 * OpenStack username 	 */
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
comment|/** 	 * OpenStack password 	 */
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
comment|/** 	 * OpenStack host url 	 */
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
DECL|method|getConfig ()
specifier|public
name|Config
name|getConfig
parameter_list|()
block|{
return|return
name|config
return|;
block|}
comment|/** 	 *OpenStack configuration 	 */
DECL|method|setConfig (Config config)
specifier|public
name|void
name|setConfig
parameter_list|(
name|Config
name|config
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
DECL|method|getApiVersion ()
specifier|public
name|String
name|getApiVersion
parameter_list|()
block|{
return|return
name|apiVersion
return|;
block|}
comment|/** 	 * OpenStack API version 	 */
DECL|method|setApiVersion (String apiVersion)
specifier|public
name|void
name|setApiVersion
parameter_list|(
name|String
name|apiVersion
parameter_list|)
block|{
name|this
operator|.
name|apiVersion
operator|=
name|apiVersion
expr_stmt|;
block|}
block|}
end_class

end_unit

