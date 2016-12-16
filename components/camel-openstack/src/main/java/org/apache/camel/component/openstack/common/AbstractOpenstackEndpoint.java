begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.openstack.common
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
name|common
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
name|Component
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
name|Consumer
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
name|Processor
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
name|impl
operator|.
name|DefaultEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openstack4j
operator|.
name|api
operator|.
name|OSClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openstack4j
operator|.
name|api
operator|.
name|client
operator|.
name|IOSClientBuilder
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

begin_import
import|import
name|org
operator|.
name|openstack4j
operator|.
name|model
operator|.
name|common
operator|.
name|Identifier
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openstack4j
operator|.
name|openstack
operator|.
name|OSFactory
import|;
end_import

begin_class
DECL|class|AbstractOpenstackEndpoint
specifier|public
specifier|abstract
class|class
name|AbstractOpenstackEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|V2
specifier|public
specifier|static
specifier|final
name|String
name|V2
init|=
literal|"V2"
decl_stmt|;
DECL|field|V3
specifier|public
specifier|static
specifier|final
name|String
name|V3
init|=
literal|"V3"
decl_stmt|;
DECL|method|AbstractOpenstackEndpoint (String endpointUri, Component component)
specifier|public
name|AbstractOpenstackEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|getHost ()
specifier|protected
specifier|abstract
name|String
name|getHost
parameter_list|()
function_decl|;
DECL|method|getUsername ()
specifier|protected
specifier|abstract
name|String
name|getUsername
parameter_list|()
function_decl|;
DECL|method|getDomain ()
specifier|protected
specifier|abstract
name|String
name|getDomain
parameter_list|()
function_decl|;
DECL|method|getPassword ()
specifier|protected
specifier|abstract
name|String
name|getPassword
parameter_list|()
function_decl|;
DECL|method|getProject ()
specifier|protected
specifier|abstract
name|String
name|getProject
parameter_list|()
function_decl|;
DECL|method|getOperation ()
specifier|protected
specifier|abstract
name|String
name|getOperation
parameter_list|()
function_decl|;
DECL|method|getConfig ()
specifier|protected
specifier|abstract
name|Config
name|getConfig
parameter_list|()
function_decl|;
DECL|method|getApiVersion ()
specifier|protected
specifier|abstract
name|String
name|getApiVersion
parameter_list|()
function_decl|;
DECL|method|createClient ()
specifier|protected
name|OSClient
name|createClient
parameter_list|()
block|{
comment|//client should reAuthenticate itself when token expires
if|if
condition|(
name|V2
operator|.
name|equals
argument_list|(
name|getApiVersion
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|createV2Client
argument_list|()
return|;
block|}
return|return
name|createV3Client
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"There is no consumer available for OpenStack"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|createV3Client ()
specifier|private
name|OSClient
operator|.
name|OSClientV3
name|createV3Client
parameter_list|()
block|{
name|IOSClientBuilder
operator|.
name|V3
name|builder
init|=
name|OSFactory
operator|.
name|builderV3
argument_list|()
operator|.
name|endpoint
argument_list|(
name|getHost
argument_list|()
argument_list|)
decl_stmt|;
name|builder
operator|.
name|credentials
argument_list|(
name|getUsername
argument_list|()
argument_list|,
name|getPassword
argument_list|()
argument_list|,
name|Identifier
operator|.
name|byId
argument_list|(
name|getDomain
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|scopeToProject
argument_list|(
name|Identifier
operator|.
name|byId
argument_list|(
name|getProject
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|getConfig
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|withConfig
argument_list|(
name|getConfig
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|authenticate
argument_list|()
return|;
block|}
DECL|method|createV2Client ()
specifier|private
name|OSClient
operator|.
name|OSClientV2
name|createV2Client
parameter_list|()
block|{
name|IOSClientBuilder
operator|.
name|V2
name|builder
init|=
name|OSFactory
operator|.
name|builderV2
argument_list|()
operator|.
name|endpoint
argument_list|(
name|getHost
argument_list|()
argument_list|)
decl_stmt|;
name|builder
operator|.
name|credentials
argument_list|(
name|getUsername
argument_list|()
argument_list|,
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|tenantId
argument_list|(
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|getConfig
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|withConfig
argument_list|(
name|getConfig
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|authenticate
argument_list|()
return|;
block|}
block|}
end_class

end_unit

