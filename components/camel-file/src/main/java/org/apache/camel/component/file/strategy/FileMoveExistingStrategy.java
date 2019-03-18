begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.strategy
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
name|strategy
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
name|GenericFileEndpoint
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
comment|/**  * This is the interface to be implemented when a custom implementation needs to be   * provided in case of fileExists=Move is in use while moving any existing file in producer  * endpoints.  */
end_comment

begin_interface
DECL|interface|FileMoveExistingStrategy
specifier|public
interface|interface
name|FileMoveExistingStrategy
block|{
comment|/**      * Moves any existing file due fileExists=Move is in use.      *      * @param endpoint  the given endpoint of the component      * @param operations file operations API of the relevant component's API       * @return result of the file opeartion can be returned      *         note that for now, implemetion classes for file component      *         and ftp components, always returned true. However,if such      *         a need of direct usage of File API returning true|false,      *         you can use that return value for implementation's return value.      */
DECL|method|moveExistingFile (GenericFileEndpoint endpoint, GenericFileOperations operations, String fileName)
name|boolean
name|moveExistingFile
parameter_list|(
name|GenericFileEndpoint
name|endpoint
parameter_list|,
name|GenericFileOperations
name|operations
parameter_list|,
name|String
name|fileName
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
function_decl|;
block|}
end_interface

end_unit

