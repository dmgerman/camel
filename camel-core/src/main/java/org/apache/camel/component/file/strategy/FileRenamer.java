begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|io
operator|.
name|File
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
name|FileExchange
import|;
end_import

begin_comment
comment|/**  * Used for renaming files.  *  * @version $Revision$  * @deprecated will be replaced with NewFile in Camel 2.0  */
end_comment

begin_interface
DECL|interface|FileRenamer
specifier|public
interface|interface
name|FileRenamer
block|{
comment|/**      * Renames the given file      *      * @param exchange  the exchange      * @param file      the original file.      * @return the renamed file.      */
DECL|method|renameFile (FileExchange exchange, File file)
name|File
name|renameFile
parameter_list|(
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

