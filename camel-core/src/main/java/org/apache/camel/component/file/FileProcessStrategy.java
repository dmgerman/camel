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
name|File
import|;
end_import

begin_comment
comment|/**  * Represents a strategy for marking that a file is processed.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|FileProcessStrategy
specifier|public
interface|interface
name|FileProcessStrategy
block|{
comment|/**      * Called when work is about to begin on this file. This method may attempt to acquire some file lock before      * returning true; returning false if the file lock could not be obtained so that the file should be ignored.      *      * @param endpoint  the endpoint      * @param exchange  the exchange      * @param file      the file      * @return true if the file can be processed (such as if a file lock could be obtained)      * @throws Exception can be thrown in case of errors      */
DECL|method|begin (FileEndpoint endpoint, FileExchange exchange, File file)
name|boolean
name|begin
parameter_list|(
name|FileEndpoint
name|endpoint
parameter_list|,
name|FileExchange
name|exchange
parameter_list|,
name|File
name|file
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Releases any file locks and possibly deletes or moves the file after successful processing      *      * @param endpoint  the endpoint      * @param exchange  the exchange      * @param file      the file      * @throws Exception can be thrown in case of errors      */
DECL|method|commit (FileEndpoint endpoint, FileExchange exchange, File file)
name|void
name|commit
parameter_list|(
name|FileEndpoint
name|endpoint
parameter_list|,
name|FileExchange
name|exchange
parameter_list|,
name|File
name|file
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Releases any file locks and possibly deletes or moves the file after unsuccessful processing      *      * @param endpoint  the endpoint      * @param exchange  the exchange      * @param file      the file      */
DECL|method|rollback (FileEndpoint endpoint, FileExchange exchange, File file)
name|void
name|rollback
parameter_list|(
name|FileEndpoint
name|endpoint
parameter_list|,
name|FileExchange
name|exchange
parameter_list|,
name|File
name|file
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

