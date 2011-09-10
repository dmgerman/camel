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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Expression
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
name|language
operator|.
name|simple
operator|.
name|SimpleParserException
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
name|language
operator|.
name|simple
operator|.
name|SimpleToken
import|;
end_import

begin_comment
comment|/**  * Represents a node in the Simple AST  */
end_comment

begin_interface
DECL|interface|SimpleNode
specifier|public
interface|interface
name|SimpleNode
block|{
comment|/**      * Gets the token by which this model was based upon      *      * @return the token      */
DECL|method|getToken ()
name|SimpleToken
name|getToken
parameter_list|()
function_decl|;
comment|/**      * Creates a Camel {@link Expression} based on this model.      *      * @param expression the input string      * @return the created {@link Expression}      * @throws org.apache.camel.language.simple.SimpleParserException      *          should be thrown if error parsing the model      */
DECL|method|createExpression (String expression)
name|Expression
name|createExpression
parameter_list|(
name|String
name|expression
parameter_list|)
throws|throws
name|SimpleParserException
function_decl|;
block|}
end_interface

end_unit

