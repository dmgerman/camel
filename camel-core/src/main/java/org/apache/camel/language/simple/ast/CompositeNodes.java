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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|builder
operator|.
name|ExpressionBuilder
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
comment|/**  * A node which contains other {@link SimpleNode nodes}.  */
end_comment

begin_class
DECL|class|CompositeNodes
specifier|public
class|class
name|CompositeNodes
extends|extends
name|BaseSimpleNode
block|{
DECL|field|children
specifier|private
specifier|final
name|List
argument_list|<
name|SimpleNode
argument_list|>
name|children
init|=
operator|new
name|ArrayList
argument_list|<
name|SimpleNode
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|CompositeNodes (SimpleToken token)
specifier|public
name|CompositeNodes
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
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|SimpleNode
name|child
range|:
name|children
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|child
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|addChild (SimpleNode child)
specifier|public
name|void
name|addChild
parameter_list|(
name|SimpleNode
name|child
parameter_list|)
block|{
name|children
operator|.
name|add
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
DECL|method|getChildren ()
specifier|public
name|List
argument_list|<
name|SimpleNode
argument_list|>
name|getChildren
parameter_list|()
block|{
return|return
name|children
return|;
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
if|if
condition|(
name|children
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|children
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|children
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|createExpression
argument_list|(
name|expression
argument_list|)
return|;
block|}
else|else
block|{
name|List
argument_list|<
name|Expression
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Expression
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|SimpleNode
name|child
range|:
name|children
control|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|child
operator|.
name|createExpression
argument_list|(
name|expression
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|ExpressionBuilder
operator|.
name|concatExpression
argument_list|(
name|answer
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

