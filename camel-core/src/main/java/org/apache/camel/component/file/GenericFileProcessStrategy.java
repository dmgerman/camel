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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_comment
comment|/**  * Represents a pluggable strategy when processing files.  */
end_comment

begin_interface
DECL|interface|GenericFileProcessStrategy
specifier|public
interface|interface
name|GenericFileProcessStrategy
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * Allows custom logic to be run on startup preparing the strategy,      * such as removing old lock files etc.      *      * @param operations file operations      * @param endpoint   the endpoint      * @throws Exception can be thrown in case of errors which causes startup to fail      */
DECL|method|prepareOnStartup (GenericFileOperations<T> operations, GenericFileEndpoint<T> endpoint)
name|void
name|prepareOnStartup
parameter_list|(
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
parameter_list|,
name|GenericFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Called when work is about to begin on this file. This method may attempt      * to acquire some file lock before returning true; returning false if the      * file lock could not be obtained so that the file should be ignored.      *      * @param operations file operations      * @param endpoint   the endpoint      * @param exchange   the exchange      * @param file       the file      * @return true if the file can be processed (such as if a file lock could be obtained)      * @throws Exception can be thrown in case of errors      */
DECL|method|begin (GenericFileOperations<T> operations, GenericFileEndpoint<T> endpoint, Exchange exchange, GenericFile<T> file)
name|boolean
name|begin
parameter_list|(
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
parameter_list|,
name|GenericFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Called when a begin is aborted, for example to release any resources which may have      * been acquired during the {@link #begin(GenericFileOperations, GenericFileEndpoint, org.apache.camel.Exchange, GenericFile)}      * operation.      *      * @param operations file operations      * @param endpoint   the endpoint      * @param exchange   the exchange      * @param file       the file      * @throws Exception can be thrown in case of errors      */
DECL|method|abort (GenericFileOperations<T> operations, GenericFileEndpoint<T> endpoint, Exchange exchange, GenericFile<T> file)
name|void
name|abort
parameter_list|(
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
parameter_list|,
name|GenericFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Releases any file locks and possibly deletes or moves the file after      * successful processing      *      * @param operations file operations      * @param endpoint   the endpoint      * @param exchange   the exchange      * @param file       the file      * @throws Exception can be thrown in case of errors      */
DECL|method|commit (GenericFileOperations<T> operations, GenericFileEndpoint<T> endpoint, Exchange exchange, GenericFile<T> file)
name|void
name|commit
parameter_list|(
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
parameter_list|,
name|GenericFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Releases any file locks and possibly deletes or moves the file after      * unsuccessful processing      *      * @param operations file operations      * @param endpoint   the endpoint      * @param exchange   the exchange      * @param file       the file      * @throws Exception can be thrown in case of errors      */
DECL|method|rollback (GenericFileOperations<T> operations, GenericFileEndpoint<T> endpoint, Exchange exchange, GenericFile<T> file)
name|void
name|rollback
parameter_list|(
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
parameter_list|,
name|GenericFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

