begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Producer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jclouds
operator|.
name|compute
operator|.
name|ComputeService
import|;
end_import

begin_class
DECL|class|JcloudsComputeEndpoint
specifier|public
class|class
name|JcloudsComputeEndpoint
extends|extends
name|JcloudsEndpoint
block|{
DECL|field|computeService
specifier|private
name|ComputeService
name|computeService
decl_stmt|;
DECL|field|imageId
specifier|private
name|String
name|imageId
decl_stmt|;
DECL|field|locationId
specifier|private
name|String
name|locationId
decl_stmt|;
DECL|field|hardwareId
specifier|private
name|String
name|hardwareId
decl_stmt|;
DECL|field|operation
specifier|private
name|String
name|operation
decl_stmt|;
DECL|field|nodeState
specifier|private
name|String
name|nodeState
decl_stmt|;
DECL|field|nodeId
specifier|private
name|String
name|nodeId
decl_stmt|;
DECL|field|group
specifier|private
name|String
name|group
decl_stmt|;
DECL|field|user
specifier|private
name|String
name|user
decl_stmt|;
DECL|method|JcloudsComputeEndpoint (String uri, JcloudsComponent component, ComputeService computeService)
specifier|public
name|JcloudsComputeEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|JcloudsComponent
name|component
parameter_list|,
name|ComputeService
name|computeService
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|computeService
operator|=
name|computeService
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
return|return
operator|new
name|JcloudsComputeProducer
argument_list|(
name|this
argument_list|,
name|computeService
argument_list|)
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
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Consumer not supported for JcloudsComputeEndpoint!"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|getImageId ()
specifier|public
name|String
name|getImageId
parameter_list|()
block|{
return|return
name|imageId
return|;
block|}
annotation|@
name|Override
DECL|method|setImageId (String imageId)
specifier|public
name|void
name|setImageId
parameter_list|(
name|String
name|imageId
parameter_list|)
block|{
name|this
operator|.
name|imageId
operator|=
name|imageId
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getLocationId ()
specifier|public
name|String
name|getLocationId
parameter_list|()
block|{
return|return
name|locationId
return|;
block|}
annotation|@
name|Override
DECL|method|setLocationId (String locationId)
specifier|public
name|void
name|setLocationId
parameter_list|(
name|String
name|locationId
parameter_list|)
block|{
name|this
operator|.
name|locationId
operator|=
name|locationId
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getHardwareId ()
specifier|public
name|String
name|getHardwareId
parameter_list|()
block|{
return|return
name|hardwareId
return|;
block|}
annotation|@
name|Override
DECL|method|setHardwareId (String hardwareId)
specifier|public
name|void
name|setHardwareId
parameter_list|(
name|String
name|hardwareId
parameter_list|)
block|{
name|this
operator|.
name|hardwareId
operator|=
name|hardwareId
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
annotation|@
name|Override
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
DECL|method|getNodeState ()
specifier|public
name|String
name|getNodeState
parameter_list|()
block|{
return|return
name|nodeState
return|;
block|}
annotation|@
name|Override
DECL|method|setNodeState (String nodeState)
specifier|public
name|void
name|setNodeState
parameter_list|(
name|String
name|nodeState
parameter_list|)
block|{
name|this
operator|.
name|nodeState
operator|=
name|nodeState
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getNodeId ()
specifier|public
name|String
name|getNodeId
parameter_list|()
block|{
return|return
name|nodeId
return|;
block|}
annotation|@
name|Override
DECL|method|setNodeId (String nodeId)
specifier|public
name|void
name|setNodeId
parameter_list|(
name|String
name|nodeId
parameter_list|)
block|{
name|this
operator|.
name|nodeId
operator|=
name|nodeId
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getGroup ()
specifier|public
name|String
name|getGroup
parameter_list|()
block|{
return|return
name|group
return|;
block|}
annotation|@
name|Override
DECL|method|setGroup (String group)
specifier|public
name|void
name|setGroup
parameter_list|(
name|String
name|group
parameter_list|)
block|{
name|this
operator|.
name|group
operator|=
name|group
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getUser ()
specifier|public
name|String
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
annotation|@
name|Override
DECL|method|setUser (String user)
specifier|public
name|void
name|setUser
parameter_list|(
name|String
name|user
parameter_list|)
block|{
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
block|}
block|}
end_class

end_unit

