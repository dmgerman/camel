begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
operator|.
name|remote
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
name|impl
operator|.
name|DefaultMessage
import|;
end_import

begin_comment
comment|/**  * Remote file message  */
end_comment

begin_class
DECL|class|RemoteFileMessage
specifier|public
class|class
name|RemoteFileMessage
extends|extends
name|DefaultMessage
block|{
DECL|field|remoteFile
specifier|private
name|RemoteFile
name|remoteFile
decl_stmt|;
DECL|method|RemoteFileMessage ()
specifier|public
name|RemoteFileMessage
parameter_list|()
block|{     }
DECL|method|RemoteFileMessage (RemoteFile remoteFile)
specifier|public
name|RemoteFileMessage
parameter_list|(
name|RemoteFile
name|remoteFile
parameter_list|)
block|{
name|this
operator|.
name|remoteFile
operator|=
name|remoteFile
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getExchange ()
specifier|public
name|RemoteFileExchange
name|getExchange
parameter_list|()
block|{
return|return
operator|(
name|RemoteFileExchange
operator|)
name|super
operator|.
name|getExchange
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createBody ()
specifier|protected
name|Object
name|createBody
parameter_list|()
block|{
return|return
name|remoteFile
operator|.
name|getBody
argument_list|()
return|;
block|}
DECL|method|getRemoteFile ()
specifier|public
name|RemoteFile
name|getRemoteFile
parameter_list|()
block|{
return|return
name|remoteFile
return|;
block|}
DECL|method|setRemoteFile (RemoteFile remoteFile)
specifier|public
name|void
name|setRemoteFile
parameter_list|(
name|RemoteFile
name|remoteFile
parameter_list|)
block|{
name|this
operator|.
name|remoteFile
operator|=
name|remoteFile
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newInstance ()
specifier|public
name|RemoteFileMessage
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|RemoteFileMessage
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"RemoteFileMessage: "
operator|+
name|remoteFile
return|;
block|}
block|}
end_class

end_unit

