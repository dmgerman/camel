begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.service.lra.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|service
operator|.
name|lra
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|service
operator|.
name|lra
operator|.
name|LRAConstants
operator|.
name|DEFAULT_COORDINATOR_CONTEXT_PATH
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|service
operator|.
name|lra
operator|.
name|LRAConstants
operator|.
name|DEFAULT_LOCAL_PARTICIPANT_CONTEXT_PATH
import|;
end_import

begin_comment
comment|/**  * Spring-boot Auto-configuration for LRA service.  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.service.lra"
argument_list|)
DECL|class|LraServiceConfiguration
specifier|public
class|class
name|LraServiceConfiguration
block|{
comment|/**      * Global option to enable/disable component auto-configuration, default is true.      */
DECL|field|enabled
specifier|private
name|boolean
name|enabled
init|=
literal|true
decl_stmt|;
comment|/**      * The base URL of the LRA coordinator service (e.g. http://lra-host:8080)      */
DECL|field|coordinatorUrl
specifier|private
name|String
name|coordinatorUrl
decl_stmt|;
comment|/**      * The context path of the LRA coordinator service      */
DECL|field|coordinatorContextPath
specifier|private
name|String
name|coordinatorContextPath
init|=
name|DEFAULT_COORDINATOR_CONTEXT_PATH
decl_stmt|;
comment|/**      * The local URL where the coordinator should send callbacks to (e.g. http://my-host-name:8080)      */
DECL|field|localParticipantUrl
specifier|private
name|String
name|localParticipantUrl
decl_stmt|;
comment|/**      * The context path of the local participant callback services      */
DECL|field|localParticipantContextPath
specifier|private
name|String
name|localParticipantContextPath
init|=
name|DEFAULT_LOCAL_PARTICIPANT_CONTEXT_PATH
decl_stmt|;
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
DECL|method|setEnabled (boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
block|}
DECL|method|getCoordinatorUrl ()
specifier|public
name|String
name|getCoordinatorUrl
parameter_list|()
block|{
return|return
name|coordinatorUrl
return|;
block|}
DECL|method|setCoordinatorUrl (String coordinatorUrl)
specifier|public
name|void
name|setCoordinatorUrl
parameter_list|(
name|String
name|coordinatorUrl
parameter_list|)
block|{
name|this
operator|.
name|coordinatorUrl
operator|=
name|coordinatorUrl
expr_stmt|;
block|}
DECL|method|getCoordinatorContextPath ()
specifier|public
name|String
name|getCoordinatorContextPath
parameter_list|()
block|{
return|return
name|coordinatorContextPath
return|;
block|}
DECL|method|setCoordinatorContextPath (String coordinatorContextPath)
specifier|public
name|void
name|setCoordinatorContextPath
parameter_list|(
name|String
name|coordinatorContextPath
parameter_list|)
block|{
name|this
operator|.
name|coordinatorContextPath
operator|=
name|coordinatorContextPath
expr_stmt|;
block|}
DECL|method|getLocalParticipantUrl ()
specifier|public
name|String
name|getLocalParticipantUrl
parameter_list|()
block|{
return|return
name|localParticipantUrl
return|;
block|}
DECL|method|setLocalParticipantUrl (String localParticipantUrl)
specifier|public
name|void
name|setLocalParticipantUrl
parameter_list|(
name|String
name|localParticipantUrl
parameter_list|)
block|{
name|this
operator|.
name|localParticipantUrl
operator|=
name|localParticipantUrl
expr_stmt|;
block|}
DECL|method|getLocalParticipantContextPath ()
specifier|public
name|String
name|getLocalParticipantContextPath
parameter_list|()
block|{
return|return
name|localParticipantContextPath
return|;
block|}
DECL|method|setLocalParticipantContextPath (String localParticipantContextPath)
specifier|public
name|void
name|setLocalParticipantContextPath
parameter_list|(
name|String
name|localParticipantContextPath
parameter_list|)
block|{
name|this
operator|.
name|localParticipantContextPath
operator|=
name|localParticipantContextPath
expr_stmt|;
block|}
block|}
end_class

end_unit

