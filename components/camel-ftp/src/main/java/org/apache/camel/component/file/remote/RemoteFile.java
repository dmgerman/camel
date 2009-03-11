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
name|component
operator|.
name|file
operator|.
name|GenericFile
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
name|FileUtil
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Represents a remote file of some sort of backing object  *  * @param<T> the type of file that these remote endpoints provide  */
end_comment

begin_class
DECL|class|RemoteFile
specifier|public
class|class
name|RemoteFile
parameter_list|<
name|T
parameter_list|>
extends|extends
name|GenericFile
argument_list|<
name|T
argument_list|>
implements|implements
name|Cloneable
block|{
DECL|field|hostname
specifier|private
name|String
name|hostname
decl_stmt|;
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
annotation|@
name|Override
DECL|method|getFileSeparator ()
specifier|public
name|char
name|getFileSeparator
parameter_list|()
block|{
comment|// always use / as separator for FTP
return|return
literal|'/'
return|;
block|}
annotation|@
name|Override
DECL|method|isAbsolute (String name)
specifier|protected
name|boolean
name|isAbsolute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|name
operator|.
name|startsWith
argument_list|(
literal|""
operator|+
name|getFileSeparator
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|copyFrom (RemoteFile<T> source)
specifier|public
name|RemoteFile
argument_list|<
name|T
argument_list|>
name|copyFrom
parameter_list|(
name|RemoteFile
argument_list|<
name|T
argument_list|>
name|source
parameter_list|)
block|{
name|RemoteFile
argument_list|<
name|T
argument_list|>
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|source
operator|.
name|getClass
argument_list|()
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|// align these setters with GenericFile
name|result
operator|.
name|setEndpointPath
argument_list|(
name|source
operator|.
name|getEndpointPath
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setAbsolute
argument_list|(
name|source
operator|.
name|isAbsolute
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setAbsoluteFilePath
argument_list|(
name|source
operator|.
name|getAbsoluteFilePath
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setRelativeFilePath
argument_list|(
name|source
operator|.
name|getRelativeFilePath
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setFileName
argument_list|(
name|source
operator|.
name|getFileName
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setFileNameOnly
argument_list|(
name|source
operator|.
name|getFileNameOnly
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setFileLength
argument_list|(
name|source
operator|.
name|getFileLength
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setLastModified
argument_list|(
name|source
operator|.
name|getLastModified
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setFile
argument_list|(
name|source
operator|.
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setBody
argument_list|(
name|source
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setBinding
argument_list|(
name|source
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setHostname
argument_list|(
name|source
operator|.
name|getHostname
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|normalizePathToProtocol (String path)
specifier|protected
name|String
name|normalizePathToProtocol
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|path
operator|=
name|super
operator|.
name|normalizePathToProtocol
argument_list|(
name|path
argument_list|)
expr_stmt|;
comment|// strip leading / for FTP protocol to avoid files with absolute paths
return|return
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_class

end_unit

