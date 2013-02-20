begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Exchange
import|;
end_import

begin_interface
DECL|interface|GenericFileOperations
specifier|public
interface|interface
name|GenericFileOperations
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * Sets the endpoint as some implementations need access to the endpoint and how its configured.      *      * @param endpoint the endpoint      */
DECL|method|setEndpoint (GenericFileEndpoint<T> endpoint)
name|void
name|setEndpoint
parameter_list|(
name|GenericFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
parameter_list|)
function_decl|;
comment|/**      * Deletes the file name by name, relative to the current directory      *      * @param name name of the file      * @return true if deleted, false if not      * @throws GenericFileOperationFailedException can be thrown      */
DECL|method|deleteFile (String name)
name|boolean
name|deleteFile
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
function_decl|;
comment|/**      * Determines whether the files exists or not      *      * @param name name of the file      * @return true if exists, false if not      * @throws GenericFileOperationFailedException can be thrown      */
DECL|method|existsFile (String name)
name|boolean
name|existsFile
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
function_decl|;
comment|/**      * Renames the file      *      * @param from original name      * @param to   the new name      * @return true if renamed, false if not      * @throws GenericFileOperationFailedException can be thrown      */
DECL|method|renameFile (String from, String to)
name|boolean
name|renameFile
parameter_list|(
name|String
name|from
parameter_list|,
name|String
name|to
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
function_decl|;
comment|/**      * Builds the directory structure. Will test if the      * folder already exists.      *      * @param directory the directory path to build as a relative string name      * @param absolute wether the directory is an absolute or relative path      * @return true if build or already exists, false if not possible (could be lack of permissions)      * @throws GenericFileOperationFailedException can be thrown      */
DECL|method|buildDirectory (String directory, boolean absolute)
name|boolean
name|buildDirectory
parameter_list|(
name|String
name|directory
parameter_list|,
name|boolean
name|absolute
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
function_decl|;
comment|/**      * Retrieves the file      *      * @param name     name of the file      * @param exchange stream to write the content of the file into      * @return true if file has been retrieved, false if not      * @throws GenericFileOperationFailedException can be thrown      */
DECL|method|retrieveFile (String name, Exchange exchange)
name|boolean
name|retrieveFile
parameter_list|(
name|String
name|name
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
function_decl|;
comment|/**      * Releases the resources consumed by a retrieved file      *       * @param exchange exchange with the content of the file      * @throws GenericFileOperationFailedException can be thrown      */
DECL|method|releaseRetreivedFileResources (Exchange exchange)
name|void
name|releaseRetreivedFileResources
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
function_decl|;
comment|/**      * Stores the content as a new remote file (upload)      *      * @param name     name of new file      * @param exchange with the content content of the file      * @return true if the file was stored, false if not      * @throws GenericFileOperationFailedException can be thrown      */
DECL|method|storeFile (String name, Exchange exchange)
name|boolean
name|storeFile
parameter_list|(
name|String
name|name
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
function_decl|;
comment|/**      * Gets the current remote directory      *      * @return the current directory path      * @throws GenericFileOperationFailedException can be thrown      */
DECL|method|getCurrentDirectory ()
name|String
name|getCurrentDirectory
parameter_list|()
throws|throws
name|GenericFileOperationFailedException
function_decl|;
comment|/**      * Change the current remote directory      *      * @param path the path to change to      * @throws GenericFileOperationFailedException can be thrown      */
DECL|method|changeCurrentDirectory (String path)
name|void
name|changeCurrentDirectory
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
function_decl|;
comment|/**      * Change the current remote directory to the parent      *      * @throws GenericFileOperationFailedException can be thrown      */
DECL|method|changeToParentDirectory ()
name|void
name|changeToParentDirectory
parameter_list|()
throws|throws
name|GenericFileOperationFailedException
function_decl|;
comment|/**      * List the files in the current directory      *      * @return a list of backing objects representing the files      * @throws GenericFileOperationFailedException can be thrown      */
DECL|method|listFiles ()
name|List
argument_list|<
name|T
argument_list|>
name|listFiles
parameter_list|()
throws|throws
name|GenericFileOperationFailedException
function_decl|;
comment|/**      * List the files in the given remote directory      *      * @param path the remote directory      * @return a list of backing objects representing the files      * @throws GenericFileOperationFailedException can be thrown      */
DECL|method|listFiles (String path)
name|List
argument_list|<
name|T
argument_list|>
name|listFiles
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
function_decl|;
block|}
end_interface

end_unit

