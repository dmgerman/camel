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
name|GenericFileOperationFailedException
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
name|file
operator|.
name|GenericFileOperations
import|;
end_import

begin_comment
comment|/**  * Remote file operations based on some backing framework  */
end_comment

begin_interface
DECL|interface|RemoteFileOperations
specifier|public
interface|interface
name|RemoteFileOperations
parameter_list|<
name|T
parameter_list|>
extends|extends
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
block|{
comment|/**      * Connects to the remote server      *      * @param configuration configuration      * @return<tt>true</tt> if connected      * @throws GenericFileOperationFailedException can be thrown      */
DECL|method|connect (RemoteFileConfiguration configuration)
name|boolean
name|connect
parameter_list|(
name|RemoteFileConfiguration
name|configuration
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
function_decl|;
comment|/**      * Returns whether we are connected to the remote server or not      *      * @return<tt>true</tt> if connected,<tt>false</tt> if not      * @throws GenericFileOperationFailedException can be thrown      */
DECL|method|isConnected ()
name|boolean
name|isConnected
parameter_list|()
throws|throws
name|GenericFileOperationFailedException
function_decl|;
comment|/**      * Disconnects from the remote server      *      * @throws GenericFileOperationFailedException can be thrown      */
DECL|method|disconnect ()
name|void
name|disconnect
parameter_list|()
throws|throws
name|GenericFileOperationFailedException
function_decl|;
comment|/**      * Forces a hard disconnect from the remote server and cause the client to be re-created on next poll.      *      * @throws GenericFileOperationFailedException can be thrown      */
DECL|method|forceDisconnect ()
name|void
name|forceDisconnect
parameter_list|()
throws|throws
name|GenericFileOperationFailedException
function_decl|;
comment|/**      * Sends a noop command to the remote server      *      * @return<tt>true</tt> if the noop was a success,<tt>false</tt> otherwise      * @throws GenericFileOperationFailedException can be thrown      */
DECL|method|sendNoop ()
name|boolean
name|sendNoop
parameter_list|()
throws|throws
name|GenericFileOperationFailedException
function_decl|;
comment|/**      * Sends a site command to the remote server      *      * @param command  the command      * @return<tt>true</tt> if the command was a success,<tt>false</tt> otherwise      * @throws GenericFileOperationFailedException can be thrown      */
DECL|method|sendSiteCommand (String command)
name|boolean
name|sendSiteCommand
parameter_list|(
name|String
name|command
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
function_decl|;
block|}
end_interface

end_unit

