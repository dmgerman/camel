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
name|java
operator|.
name|io
operator|.
name|OutputStream
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

begin_class
DECL|class|RemoteFileMessage
specifier|public
class|class
name|RemoteFileMessage
extends|extends
name|DefaultMessage
block|{
DECL|field|outputStream
specifier|private
name|OutputStream
name|outputStream
decl_stmt|;
DECL|field|fullFileName
specifier|private
name|String
name|fullFileName
decl_stmt|;
DECL|field|fileName
specifier|private
name|String
name|fileName
decl_stmt|;
DECL|field|hostname
specifier|private
name|String
name|hostname
decl_stmt|;
DECL|field|fileLength
specifier|private
name|long
name|fileLength
decl_stmt|;
DECL|method|RemoteFileMessage ()
specifier|public
name|RemoteFileMessage
parameter_list|()
block|{     }
DECL|method|RemoteFileMessage (String hostname, String fullFileName, String fileName, long fileLength, OutputStream outputStream)
specifier|public
name|RemoteFileMessage
parameter_list|(
name|String
name|hostname
parameter_list|,
name|String
name|fullFileName
parameter_list|,
name|String
name|fileName
parameter_list|,
name|long
name|fileLength
parameter_list|,
name|OutputStream
name|outputStream
parameter_list|)
block|{
name|this
operator|.
name|hostname
operator|=
name|hostname
expr_stmt|;
name|this
operator|.
name|fullFileName
operator|=
name|fullFileName
expr_stmt|;
name|this
operator|.
name|fileName
operator|=
name|fileName
expr_stmt|;
name|this
operator|.
name|fileLength
operator|=
name|fileLength
expr_stmt|;
name|this
operator|.
name|outputStream
operator|=
name|outputStream
expr_stmt|;
block|}
DECL|method|getHostname ()
specifier|public
name|String
name|getHostname
parameter_list|()
block|{
return|return
name|hostname
return|;
block|}
DECL|method|setHostname (String hostname)
specifier|public
name|void
name|setHostname
parameter_list|(
name|String
name|hostname
parameter_list|)
block|{
name|this
operator|.
name|hostname
operator|=
name|hostname
expr_stmt|;
block|}
DECL|method|getFullFileName ()
specifier|public
name|String
name|getFullFileName
parameter_list|()
block|{
return|return
name|fullFileName
return|;
block|}
DECL|method|setFullFileName (String fullFileName)
specifier|public
name|void
name|setFullFileName
parameter_list|(
name|String
name|fullFileName
parameter_list|)
block|{
name|this
operator|.
name|fullFileName
operator|=
name|fullFileName
expr_stmt|;
block|}
DECL|method|getOutputStream ()
specifier|public
name|OutputStream
name|getOutputStream
parameter_list|()
block|{
return|return
name|outputStream
return|;
block|}
DECL|method|setOutputStream (OutputStream outputStream)
specifier|public
name|void
name|setOutputStream
parameter_list|(
name|OutputStream
name|outputStream
parameter_list|)
block|{
name|this
operator|.
name|outputStream
operator|=
name|outputStream
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
if|if
condition|(
name|outputStream
operator|!=
literal|null
condition|)
block|{
return|return
name|getExchange
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|extractBodyFromOutputStream
argument_list|(
name|getExchange
argument_list|()
argument_list|,
name|outputStream
argument_list|)
return|;
block|}
return|return
literal|null
return|;
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
DECL|method|populateInitialHeaders (Map<String, Object> map)
specifier|protected
name|void
name|populateInitialHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
name|super
operator|.
name|populateInitialHeaders
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"file.remote.host"
argument_list|,
name|hostname
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"file.remote.fullName"
argument_list|,
name|fullFileName
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"file.remote.name"
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"CamelFileName"
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"CamelFilePath"
argument_list|,
name|fullFileName
argument_list|)
expr_stmt|;
comment|// set the parent if there is a parent folder
if|if
condition|(
name|fullFileName
operator|!=
literal|null
operator|&&
name|fullFileName
operator|.
name|indexOf
argument_list|(
literal|"/"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|parent
init|=
name|fullFileName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|fullFileName
operator|.
name|lastIndexOf
argument_list|(
literal|"/"
argument_list|)
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"CamelFileParent"
argument_list|,
name|parent
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|fileLength
operator|>
literal|0
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
literal|"CamelFileLength"
argument_list|,
operator|new
name|Long
argument_list|(
name|fileLength
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

