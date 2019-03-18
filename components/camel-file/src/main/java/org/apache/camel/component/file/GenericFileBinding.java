begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|IOException
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

begin_comment
comment|/**  * Binding between the generic file and the body content.  */
end_comment

begin_interface
DECL|interface|GenericFileBinding
specifier|public
interface|interface
name|GenericFileBinding
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * Gets the body of the file      *      * @param file the file      * @return the body      */
DECL|method|getBody (GenericFile<T> file)
name|Object
name|getBody
parameter_list|(
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|)
function_decl|;
comment|/**      * Sets the body from the given file      *      * @param file the file      * @param body the body      */
DECL|method|setBody (GenericFile<T> file, Object body)
name|void
name|setBody
parameter_list|(
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|,
name|Object
name|body
parameter_list|)
function_decl|;
comment|/**      * Ensures the content is loaded from the file into memory      *      * @param exchange the current exchange      * @param file the file      * @throws java.io.IOException is thrown if the content could not be loaded      */
DECL|method|loadContent (Exchange exchange, GenericFile<?> file)
name|void
name|loadContent
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|GenericFile
argument_list|<
name|?
argument_list|>
name|file
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

