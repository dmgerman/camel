begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jclouds
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jclouds
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
name|support
operator|.
name|DefaultEndpoint
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

begin_comment
comment|/**  * For interacting with cloud compute& blobstore service via jclouds.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.9.0"
argument_list|,
name|scheme
operator|=
literal|"jclouds"
argument_list|,
name|title
operator|=
literal|"JClouds"
argument_list|,
name|syntax
operator|=
literal|"jclouds:command:providerId"
argument_list|,
name|label
operator|=
literal|"api,cloud"
argument_list|)
DECL|class|JcloudsEndpoint
specifier|public
specifier|abstract
class|class
name|JcloudsEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|JcloudsConfiguration
name|configuration
init|=
operator|new
name|JcloudsConfiguration
argument_list|()
decl_stmt|;
DECL|method|JcloudsEndpoint (String uri, JcloudsComponent component)
specifier|public
name|JcloudsEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|JcloudsComponent
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
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|JcloudsConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (JcloudsConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|JcloudsConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getCommand ()
specifier|public
name|JcloudsCommand
name|getCommand
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getCommand
argument_list|()
return|;
block|}
DECL|method|setCommand (JcloudsCommand command)
specifier|public
name|void
name|setCommand
parameter_list|(
name|JcloudsCommand
name|command
parameter_list|)
block|{
name|configuration
operator|.
name|setCommand
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
DECL|method|getProviderId ()
specifier|public
name|String
name|getProviderId
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getProviderId
argument_list|()
return|;
block|}
DECL|method|setProviderId (String providerId)
specifier|public
name|void
name|setProviderId
parameter_list|(
name|String
name|providerId
parameter_list|)
block|{
name|configuration
operator|.
name|setProviderId
argument_list|(
name|providerId
argument_list|)
expr_stmt|;
block|}
DECL|method|getImageId ()
specifier|public
name|String
name|getImageId
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getImageId
argument_list|()
return|;
block|}
DECL|method|setImageId (String imageId)
specifier|public
name|void
name|setImageId
parameter_list|(
name|String
name|imageId
parameter_list|)
block|{
name|configuration
operator|.
name|setImageId
argument_list|(
name|imageId
argument_list|)
expr_stmt|;
block|}
DECL|method|getLocationId ()
specifier|public
name|String
name|getLocationId
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getLocationId
argument_list|()
return|;
block|}
DECL|method|setLocationId (String locationId)
specifier|public
name|void
name|setLocationId
parameter_list|(
name|String
name|locationId
parameter_list|)
block|{
name|configuration
operator|.
name|setLocationId
argument_list|(
name|locationId
argument_list|)
expr_stmt|;
block|}
DECL|method|getHardwareId ()
specifier|public
name|String
name|getHardwareId
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getHardwareId
argument_list|()
return|;
block|}
DECL|method|setHardwareId (String hardwareId)
specifier|public
name|void
name|setHardwareId
parameter_list|(
name|String
name|hardwareId
parameter_list|)
block|{
name|configuration
operator|.
name|setHardwareId
argument_list|(
name|hardwareId
argument_list|)
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|String
name|getOperation
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getOperation
argument_list|()
return|;
block|}
DECL|method|setOperation (String operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|configuration
operator|.
name|setOperation
argument_list|(
name|operation
argument_list|)
expr_stmt|;
block|}
DECL|method|getNodeState ()
specifier|public
name|String
name|getNodeState
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getNodeState
argument_list|()
return|;
block|}
DECL|method|setNodeState (String nodeState)
specifier|public
name|void
name|setNodeState
parameter_list|(
name|String
name|nodeState
parameter_list|)
block|{
name|configuration
operator|.
name|setNodeState
argument_list|(
name|nodeState
argument_list|)
expr_stmt|;
block|}
DECL|method|getNodeId ()
specifier|public
name|String
name|getNodeId
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getNodeId
argument_list|()
return|;
block|}
DECL|method|setNodeId (String nodeId)
specifier|public
name|void
name|setNodeId
parameter_list|(
name|String
name|nodeId
parameter_list|)
block|{
name|configuration
operator|.
name|setNodeId
argument_list|(
name|nodeId
argument_list|)
expr_stmt|;
block|}
DECL|method|getGroup ()
specifier|public
name|String
name|getGroup
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getGroup
argument_list|()
return|;
block|}
DECL|method|setGroup (String group)
specifier|public
name|void
name|setGroup
parameter_list|(
name|String
name|group
parameter_list|)
block|{
name|configuration
operator|.
name|setGroup
argument_list|(
name|group
argument_list|)
expr_stmt|;
block|}
DECL|method|getUser ()
specifier|public
name|String
name|getUser
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getUser
argument_list|()
return|;
block|}
DECL|method|setUser (String user)
specifier|public
name|void
name|setUser
parameter_list|(
name|String
name|user
parameter_list|)
block|{
name|configuration
operator|.
name|setUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
block|}
DECL|method|getContainer ()
specifier|public
name|String
name|getContainer
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getContainer
argument_list|()
return|;
block|}
DECL|method|setContainer (String container)
specifier|public
name|void
name|setContainer
parameter_list|(
name|String
name|container
parameter_list|)
block|{
name|configuration
operator|.
name|setContainer
argument_list|(
name|container
argument_list|)
expr_stmt|;
block|}
DECL|method|getDirectory ()
specifier|public
name|String
name|getDirectory
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getDirectory
argument_list|()
return|;
block|}
DECL|method|setDirectory (String directory)
specifier|public
name|void
name|setDirectory
parameter_list|(
name|String
name|directory
parameter_list|)
block|{
name|configuration
operator|.
name|setDirectory
argument_list|(
name|directory
argument_list|)
expr_stmt|;
block|}
DECL|method|getBlobName ()
specifier|public
name|String
name|getBlobName
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getBlobName
argument_list|()
return|;
block|}
DECL|method|setBlobName (String blobName)
specifier|public
name|void
name|setBlobName
parameter_list|(
name|String
name|blobName
parameter_list|)
block|{
name|configuration
operator|.
name|setBlobName
argument_list|(
name|blobName
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

