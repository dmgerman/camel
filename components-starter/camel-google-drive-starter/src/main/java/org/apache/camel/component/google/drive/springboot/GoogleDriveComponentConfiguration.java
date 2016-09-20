begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.drive.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|drive
operator|.
name|springboot
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
name|component
operator|.
name|google
operator|.
name|drive
operator|.
name|GoogleDriveClientFactory
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
name|google
operator|.
name|drive
operator|.
name|GoogleDriveConfiguration
import|;
end_import

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

begin_comment
comment|/**  * The google-drive component provides access to Google Drive file storage  * service.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.google-drive"
argument_list|)
DECL|class|GoogleDriveComponentConfiguration
specifier|public
class|class
name|GoogleDriveComponentConfiguration
block|{
comment|/**      * To use the shared configuration      */
DECL|field|configuration
specifier|private
name|GoogleDriveConfiguration
name|configuration
decl_stmt|;
comment|/**      * To use the GoogleCalendarClientFactory as factory for creating the      * client. Will by default use BatchGoogleDriveClientFactory      */
DECL|field|clientFactory
specifier|private
name|GoogleDriveClientFactory
name|clientFactory
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|GoogleDriveConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (GoogleDriveConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|GoogleDriveConfiguration
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
DECL|method|getClientFactory ()
specifier|public
name|GoogleDriveClientFactory
name|getClientFactory
parameter_list|()
block|{
return|return
name|clientFactory
return|;
block|}
DECL|method|setClientFactory (GoogleDriveClientFactory clientFactory)
specifier|public
name|void
name|setClientFactory
parameter_list|(
name|GoogleDriveClientFactory
name|clientFactory
parameter_list|)
block|{
name|this
operator|.
name|clientFactory
operator|=
name|clientFactory
expr_stmt|;
block|}
block|}
end_class

end_unit

