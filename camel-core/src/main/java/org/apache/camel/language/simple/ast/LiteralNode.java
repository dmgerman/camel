begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.simple.ast
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|simple
operator|.
name|ast
package|;
end_package

begin_comment
comment|/**  * Represents a node in the AST which contains literals  */
end_comment

begin_interface
DECL|interface|LiteralNode
specifier|public
interface|interface
name|LiteralNode
extends|extends
name|SimpleNode
block|{
comment|/**      * Adds the given text to this model.      *<p/>      * This operation can be invoked multiple times to add more text.      *      * @param text the text to add      */
DECL|method|addText (String text)
name|void
name|addText
parameter_list|(
name|String
name|text
parameter_list|)
function_decl|;
comment|/**      * Gets the text      *      * @return the text, will never be<tt>null</tt>, but may contain an empty string.      */
DECL|method|getText ()
name|String
name|getText
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

