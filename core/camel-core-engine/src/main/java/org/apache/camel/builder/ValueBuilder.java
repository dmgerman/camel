begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|support
operator|.
name|builder
operator|.
name|Namespaces
import|;
end_import

begin_comment
comment|/**  * A builder of expressions or predicates based on values.  */
end_comment

begin_class
DECL|class|ValueBuilder
specifier|public
class|class
name|ValueBuilder
extends|extends
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|builder
operator|.
name|ValueBuilder
block|{
DECL|method|ValueBuilder (Expression expression)
specifier|public
name|ValueBuilder
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|super
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
comment|// Expression builders
comment|// -------------------------------------------------------------------------
DECL|method|tokenizeXML (String tagName, String inheritNamespaceTagName)
specifier|public
name|ValueBuilder
name|tokenizeXML
parameter_list|(
name|String
name|tagName
parameter_list|,
name|String
name|inheritNamespaceTagName
parameter_list|)
block|{
name|Expression
name|newExp
init|=
name|ExpressionBuilder
operator|.
name|tokenizeXMLExpression
argument_list|(
name|tagName
argument_list|,
name|inheritNamespaceTagName
argument_list|)
decl_stmt|;
return|return
name|onNewValueBuilder
argument_list|(
name|newExp
argument_list|)
return|;
block|}
DECL|method|xtokenize (String path, Namespaces namespaces)
specifier|public
name|ValueBuilder
name|xtokenize
parameter_list|(
name|String
name|path
parameter_list|,
name|Namespaces
name|namespaces
parameter_list|)
block|{
return|return
name|xtokenize
argument_list|(
name|path
argument_list|,
literal|'i'
argument_list|,
name|namespaces
argument_list|)
return|;
block|}
DECL|method|xtokenize (String path, char mode, Namespaces namespaces)
specifier|public
name|ValueBuilder
name|xtokenize
parameter_list|(
name|String
name|path
parameter_list|,
name|char
name|mode
parameter_list|,
name|Namespaces
name|namespaces
parameter_list|)
block|{
name|Expression
name|newExp
init|=
name|ExpressionBuilder
operator|.
name|tokenizeXMLAwareExpression
argument_list|(
name|path
argument_list|,
name|mode
argument_list|,
literal|1
argument_list|,
name|namespaces
argument_list|)
decl_stmt|;
return|return
name|onNewValueBuilder
argument_list|(
name|newExp
argument_list|)
return|;
block|}
DECL|method|tokenizePair (String startToken, String endToken, boolean includeTokens)
specifier|public
name|ValueBuilder
name|tokenizePair
parameter_list|(
name|String
name|startToken
parameter_list|,
name|String
name|endToken
parameter_list|,
name|boolean
name|includeTokens
parameter_list|)
block|{
name|Expression
name|newExp
init|=
name|ExpressionBuilder
operator|.
name|tokenizePairExpression
argument_list|(
name|startToken
argument_list|,
name|endToken
argument_list|,
name|includeTokens
argument_list|)
decl_stmt|;
return|return
name|onNewValueBuilder
argument_list|(
name|newExp
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|onNewValueBuilder (Expression exp)
specifier|protected
name|ValueBuilder
name|onNewValueBuilder
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
operator|new
name|ValueBuilder
argument_list|(
name|exp
argument_list|)
return|;
block|}
block|}
end_class

end_unit

