begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
package|;
end_package

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_comment
comment|// START SNIPPET: e1
end_comment

begin_interface
DECL|interface|OrderService
specifier|public
interface|interface
name|OrderService
block|{
DECL|method|submitOrderStringReturnString (String order)
name|String
name|submitOrderStringReturnString
parameter_list|(
name|String
name|order
parameter_list|)
function_decl|;
DECL|method|submitOrderStringReturnDocument (String order)
name|Document
name|submitOrderStringReturnDocument
parameter_list|(
name|String
name|order
parameter_list|)
function_decl|;
DECL|method|submitOrderDocumentReturnString (Document order)
name|String
name|submitOrderDocumentReturnString
parameter_list|(
name|Document
name|order
parameter_list|)
function_decl|;
DECL|method|submitOrderDocumentReturnDocument (Document order)
name|Document
name|submitOrderDocumentReturnDocument
parameter_list|(
name|Document
name|order
parameter_list|)
function_decl|;
DECL|method|doNothing (String s)
name|void
name|doNothing
parameter_list|(
name|String
name|s
parameter_list|)
function_decl|;
DECL|method|invalidReturnType (String s)
name|Integer
name|invalidReturnType
parameter_list|(
name|String
name|s
parameter_list|)
function_decl|;
DECL|method|doAbsolutelyNothing ()
name|String
name|doAbsolutelyNothing
parameter_list|()
function_decl|;
block|}
end_interface

begin_comment
comment|// END SNIPPET: e1
end_comment

end_unit

