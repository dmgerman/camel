begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * A remote file when using SFTP.  */
end_comment

begin_interface
DECL|interface|SftpRemoteFile
specifier|public
interface|interface
name|SftpRemoteFile
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * Gets the remote file object.      */
DECL|method|getRemoteFile ()
name|T
name|getRemoteFile
parameter_list|()
function_decl|;
comment|/**      * The file name      */
DECL|method|getFilename ()
name|String
name|getFilename
parameter_list|()
function_decl|;
comment|/**      * The long file name      */
DECL|method|getLongname ()
name|String
name|getLongname
parameter_list|()
function_decl|;
comment|/**      * Whether its a directory      */
DECL|method|isDirectory ()
name|boolean
name|isDirectory
parameter_list|()
function_decl|;
comment|/**      * The file size      */
DECL|method|getFileLength ()
name|long
name|getFileLength
parameter_list|()
function_decl|;
comment|/**      * The file modification timestamp (in millis)      */
DECL|method|getLastModified ()
name|long
name|getLastModified
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

