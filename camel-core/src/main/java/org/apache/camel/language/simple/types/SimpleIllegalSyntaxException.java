begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.simple.types
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
name|types
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
name|ExpressionIllegalSyntaxException
import|;
end_import

begin_comment
comment|/**  * Syntax error in the simple language expression.  */
end_comment

begin_class
DECL|class|SimpleIllegalSyntaxException
specifier|public
class|class
name|SimpleIllegalSyntaxException
extends|extends
name|ExpressionIllegalSyntaxException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|index
specifier|private
specifier|final
name|int
name|index
decl_stmt|;
DECL|field|message
specifier|private
specifier|final
name|String
name|message
decl_stmt|;
DECL|method|SimpleIllegalSyntaxException (String expression, int index, String message)
specifier|public
name|SimpleIllegalSyntaxException
parameter_list|(
name|String
name|expression
parameter_list|,
name|int
name|index
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|this
operator|.
name|index
operator|=
name|index
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
DECL|method|SimpleIllegalSyntaxException (String expression, int index, String message, Throwable cause)
specifier|public
name|SimpleIllegalSyntaxException
parameter_list|(
name|String
name|expression
parameter_list|,
name|int
name|index
parameter_list|,
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|expression
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|this
operator|.
name|index
operator|=
name|index
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
comment|/**      * Index where the parsing error occurred      *      * @return index of the parsing error in the input, returns<tt>-1</tt> if the cause of the problem      * is not applicable to specific index in the input      */
DECL|method|getIndex ()
specifier|public
name|int
name|getIndex
parameter_list|()
block|{
return|return
name|index
return|;
block|}
comment|/**      * Gets a short error message.      */
DECL|method|getShortMessage ()
specifier|public
name|String
name|getShortMessage
parameter_list|()
block|{
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
return|return
literal|"[null]"
return|;
block|}
return|return
name|message
return|;
block|}
annotation|@
name|Override
DECL|method|getMessage ()
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
return|return
literal|"[null]"
return|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>
operator|-
literal|1
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" at location "
argument_list|)
operator|.
name|append
argument_list|(
name|index
argument_list|)
expr_stmt|;
comment|// create a nice looking message with indicator where the problem is
name|sb
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
operator|.
name|append
argument_list|(
name|getExpression
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|index
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"*\n"
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
block|}
end_class

end_unit

