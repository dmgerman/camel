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
name|io
operator|.
name|InputStream
import|;
end_import

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
name|List
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
comment|/**      * Deletes the file from the remote server      *      * @param name name of the file      * @return true if deleted, false if not      * @throws GenericFileOperationFailedException      *          can be thrown      */
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
comment|/**      * Renames the file on the remote server      *      * @param from original name      * @param to   the new name      * @return true if renamed, false if not      * @throws GenericFileOperationFailedException      *          can be thrown      */
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
comment|/**      * Builds the directory structure on the remote server. Will test if the      * folder already exists.      *      * @param directory the directory path to build      * @return true if build or already exists, false if not possbile (could be      *         lack of permissions)      * @throws GenericFileOperationFailedException      *          can be thrown      */
DECL|method|buildDirectory (String directory)
name|boolean
name|buildDirectory
parameter_list|(
name|String
name|directory
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
function_decl|;
comment|/**      * Retrieves the remote file (download)      *      * @param name name of the file      * @param out  stream to write the content of the file into      * @return true if file has been retrieved, false if not      * @throws GenericFileOperationFailedException      *          can be thrown      */
DECL|method|retrieveFile (String name, OutputStream out)
name|boolean
name|retrieveFile
parameter_list|(
name|String
name|name
parameter_list|,
name|OutputStream
name|out
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
function_decl|;
comment|/**      * Stores the content as a new remote file (upload)      *      * @param name name of new file      * @param body content of the file      * @return true if the file was stored, false if not      * @throws GenericFileOperationFailedException      *          can be thrown      */
DECL|method|storeFile (String name, InputStream body)
name|boolean
name|storeFile
parameter_list|(
name|String
name|name
parameter_list|,
name|InputStream
name|body
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
function_decl|;
comment|/**      * Gets the current remote directory      *      * @return the current directory path      * @throws GenericFileOperationFailedException      *          can be thrown      */
DECL|method|getCurrentDirectory ()
name|String
name|getCurrentDirectory
parameter_list|()
throws|throws
name|GenericFileOperationFailedException
function_decl|;
comment|/**      * Change the current remote directory      *      * @param path the path to change to      * @throws GenericFileOperationFailedException      *          can be thrown      */
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
comment|/**      * List the files in the current remote directory      *      * @return a list of backing objects representing the files      * @throws GenericFileOperationFailedException      *          can be thrown      */
DECL|method|listFiles ()
name|List
name|listFiles
parameter_list|()
throws|throws
name|GenericFileOperationFailedException
function_decl|;
comment|/**      * List the files in the given remote directory      *      * @param path the remote directory      * @return a list of backing objects representing the files      * @throws GenericFileOperationFailedException      *          can be thrown      */
DECL|method|listFiles (String path)
name|List
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

