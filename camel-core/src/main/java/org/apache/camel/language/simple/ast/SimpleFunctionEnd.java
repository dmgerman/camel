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
name|types
operator|.
name|SimpleToken
import|;
end_import

begin_comment
comment|/**  * Ends a function  */
end_comment

begin_class
DECL|class|SimpleFunctionEnd
specifier|public
class|class
name|SimpleFunctionEnd
extends|extends
name|BaseSimpleNode
implements|implements
name|BlockEnd
block|{
DECL|method|SimpleFunctionEnd (SimpleToken token)
specifier|public
name|SimpleFunctionEnd
parameter_list|(
name|SimpleToken
name|token
parameter_list|)
block|{
name|super
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createExpression (String expression)
specifier|public
name|Expression
name|createExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

