begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.printer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|printer
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|Doc
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|PrintException
import|;
end_import

begin_interface
DECL|interface|PrinterOperationsInterface
specifier|public
interface|interface
name|PrinterOperationsInterface
block|{
comment|/**      * Prints the document.      *      * @param doc document to print      * @throws PrintException is thrown if printing failed      */
DECL|method|print (Doc doc, String jobName)
name|void
name|print
parameter_list|(
name|Doc
name|doc
parameter_list|,
name|String
name|jobName
parameter_list|)
throws|throws
name|PrintException
function_decl|;
block|}
end_interface

end_unit

