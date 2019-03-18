begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.docker.it
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|docker
operator|.
name|it
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|AttachContainerCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|AuthCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|BuildImageCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|CommitCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|ConnectToNetworkCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|ContainerDiffCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|CopyArchiveFromContainerCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|CopyArchiveToContainerCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|CopyFileFromContainerCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|CreateContainerCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|CreateImageCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|CreateNetworkCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|CreateVolumeCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|DisconnectFromNetworkCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|DockerCmdExecFactory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|EventsCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|ExecCreateCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|ExecStartCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|InfoCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|InspectContainerCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|InspectExecCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|InspectImageCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|InspectNetworkCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|InspectVolumeCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|KillContainerCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|ListContainersCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|ListImagesCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|ListNetworksCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|ListVolumesCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|LoadImageCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|LogContainerCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|PauseContainerCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|PingCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|PullImageCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|PushImageCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|RemoveContainerCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|RemoveImageCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|RemoveNetworkCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|RemoveVolumeCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|RenameContainerCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|RestartContainerCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|SaveImageCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|SearchImagesCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|StartContainerCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|StatsCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|StopContainerCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|TagImageCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|TopContainerCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|UnpauseContainerCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|UpdateContainerCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|VersionCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|WaitContainerCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|model
operator|.
name|Version
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|core
operator|.
name|DockerClientConfig
import|;
end_import

begin_class
DECL|class|FakeDockerCmdExecFactory
specifier|public
class|class
name|FakeDockerCmdExecFactory
implements|implements
name|DockerCmdExecFactory
block|{
DECL|field|FAKE_VERSION
specifier|public
specifier|static
specifier|final
name|String
name|FAKE_VERSION
init|=
literal|"Fake Camel Version 1.0"
decl_stmt|;
DECL|method|FakeDockerCmdExecFactory ()
specifier|public
name|FakeDockerCmdExecFactory
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|init (DockerClientConfig dockerClientConfig)
specifier|public
name|void
name|init
parameter_list|(
name|DockerClientConfig
name|dockerClientConfig
parameter_list|)
block|{
comment|// Noop
block|}
annotation|@
name|Override
DECL|method|createVersionCmdExec ()
specifier|public
name|VersionCmd
operator|.
name|Exec
name|createVersionCmdExec
parameter_list|()
block|{
return|return
operator|new
name|VersionCmd
operator|.
name|Exec
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Version
name|exec
parameter_list|(
name|VersionCmd
name|versionCmd
parameter_list|)
block|{
return|return
operator|new
name|Version
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|FAKE_VERSION
return|;
block|}
block|}
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|createAuthCmdExec ()
specifier|public
name|AuthCmd
operator|.
name|Exec
name|createAuthCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createInfoCmdExec ()
specifier|public
name|InfoCmd
operator|.
name|Exec
name|createInfoCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createPingCmdExec ()
specifier|public
name|PingCmd
operator|.
name|Exec
name|createPingCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createExecCmdExec ()
specifier|public
name|ExecCreateCmd
operator|.
name|Exec
name|createExecCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createPullImageCmdExec ()
specifier|public
name|PullImageCmd
operator|.
name|Exec
name|createPullImageCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createPushImageCmdExec ()
specifier|public
name|PushImageCmd
operator|.
name|Exec
name|createPushImageCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createSaveImageCmdExec ()
specifier|public
name|SaveImageCmd
operator|.
name|Exec
name|createSaveImageCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createCreateImageCmdExec ()
specifier|public
name|CreateImageCmd
operator|.
name|Exec
name|createCreateImageCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createLoadImageCmdExec ()
specifier|public
name|LoadImageCmd
operator|.
name|Exec
name|createLoadImageCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createSearchImagesCmdExec ()
specifier|public
name|SearchImagesCmd
operator|.
name|Exec
name|createSearchImagesCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createRemoveImageCmdExec ()
specifier|public
name|RemoveImageCmd
operator|.
name|Exec
name|createRemoveImageCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createListImagesCmdExec ()
specifier|public
name|ListImagesCmd
operator|.
name|Exec
name|createListImagesCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createInspectImageCmdExec ()
specifier|public
name|InspectImageCmd
operator|.
name|Exec
name|createInspectImageCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createListContainersCmdExec ()
specifier|public
name|ListContainersCmd
operator|.
name|Exec
name|createListContainersCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createCreateContainerCmdExec ()
specifier|public
name|CreateContainerCmd
operator|.
name|Exec
name|createCreateContainerCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createStartContainerCmdExec ()
specifier|public
name|StartContainerCmd
operator|.
name|Exec
name|createStartContainerCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createInspectContainerCmdExec ()
specifier|public
name|InspectContainerCmd
operator|.
name|Exec
name|createInspectContainerCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createRemoveContainerCmdExec ()
specifier|public
name|RemoveContainerCmd
operator|.
name|Exec
name|createRemoveContainerCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createWaitContainerCmdExec ()
specifier|public
name|WaitContainerCmd
operator|.
name|Exec
name|createWaitContainerCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createAttachContainerCmdExec ()
specifier|public
name|AttachContainerCmd
operator|.
name|Exec
name|createAttachContainerCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createExecStartCmdExec ()
specifier|public
name|ExecStartCmd
operator|.
name|Exec
name|createExecStartCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createInspectExecCmdExec ()
specifier|public
name|InspectExecCmd
operator|.
name|Exec
name|createInspectExecCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createLogContainerCmdExec ()
specifier|public
name|LogContainerCmd
operator|.
name|Exec
name|createLogContainerCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createCopyFileFromContainerCmdExec ()
specifier|public
name|CopyFileFromContainerCmd
operator|.
name|Exec
name|createCopyFileFromContainerCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createCopyArchiveFromContainerCmdExec ()
specifier|public
name|CopyArchiveFromContainerCmd
operator|.
name|Exec
name|createCopyArchiveFromContainerCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createCopyArchiveToContainerCmdExec ()
specifier|public
name|CopyArchiveToContainerCmd
operator|.
name|Exec
name|createCopyArchiveToContainerCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createStopContainerCmdExec ()
specifier|public
name|StopContainerCmd
operator|.
name|Exec
name|createStopContainerCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createContainerDiffCmdExec ()
specifier|public
name|ContainerDiffCmd
operator|.
name|Exec
name|createContainerDiffCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createKillContainerCmdExec ()
specifier|public
name|KillContainerCmd
operator|.
name|Exec
name|createKillContainerCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createUpdateContainerCmdExec ()
specifier|public
name|UpdateContainerCmd
operator|.
name|Exec
name|createUpdateContainerCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createRenameContainerCmdExec ()
specifier|public
name|RenameContainerCmd
operator|.
name|Exec
name|createRenameContainerCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createRestartContainerCmdExec ()
specifier|public
name|RestartContainerCmd
operator|.
name|Exec
name|createRestartContainerCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createCommitCmdExec ()
specifier|public
name|CommitCmd
operator|.
name|Exec
name|createCommitCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createBuildImageCmdExec ()
specifier|public
name|BuildImageCmd
operator|.
name|Exec
name|createBuildImageCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createTopContainerCmdExec ()
specifier|public
name|TopContainerCmd
operator|.
name|Exec
name|createTopContainerCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createTagImageCmdExec ()
specifier|public
name|TagImageCmd
operator|.
name|Exec
name|createTagImageCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createPauseContainerCmdExec ()
specifier|public
name|PauseContainerCmd
operator|.
name|Exec
name|createPauseContainerCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createUnpauseContainerCmdExec ()
specifier|public
name|UnpauseContainerCmd
operator|.
name|Exec
name|createUnpauseContainerCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createEventsCmdExec ()
specifier|public
name|EventsCmd
operator|.
name|Exec
name|createEventsCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createStatsCmdExec ()
specifier|public
name|StatsCmd
operator|.
name|Exec
name|createStatsCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createCreateVolumeCmdExec ()
specifier|public
name|CreateVolumeCmd
operator|.
name|Exec
name|createCreateVolumeCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createInspectVolumeCmdExec ()
specifier|public
name|InspectVolumeCmd
operator|.
name|Exec
name|createInspectVolumeCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createRemoveVolumeCmdExec ()
specifier|public
name|RemoveVolumeCmd
operator|.
name|Exec
name|createRemoveVolumeCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createListVolumesCmdExec ()
specifier|public
name|ListVolumesCmd
operator|.
name|Exec
name|createListVolumesCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createListNetworksCmdExec ()
specifier|public
name|ListNetworksCmd
operator|.
name|Exec
name|createListNetworksCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createInspectNetworkCmdExec ()
specifier|public
name|InspectNetworkCmd
operator|.
name|Exec
name|createInspectNetworkCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createCreateNetworkCmdExec ()
specifier|public
name|CreateNetworkCmd
operator|.
name|Exec
name|createCreateNetworkCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createRemoveNetworkCmdExec ()
specifier|public
name|RemoveNetworkCmd
operator|.
name|Exec
name|createRemoveNetworkCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createConnectToNetworkCmdExec ()
specifier|public
name|ConnectToNetworkCmd
operator|.
name|Exec
name|createConnectToNetworkCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createDisconnectFromNetworkCmdExec ()
specifier|public
name|DisconnectFromNetworkCmd
operator|.
name|Exec
name|createDisconnectFromNetworkCmdExec
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{      }
block|}
end_class

end_unit

