begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.resequencer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|resequencer
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
name|Exchange
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

begin_comment
comment|/**  * A {@link SequenceElementComparator} that compares {@link Exchange}s based on  * the result of an expression evaluation.  */
end_comment

begin_interface
DECL|interface|ExpressionResultComparator
specifier|public
interface|interface
name|ExpressionResultComparator
extends|extends
name|SequenceElementComparator
argument_list|<
name|Exchange
argument_list|>
block|{
comment|/**      * Set the expression used for comparing {@link Exchange}s.      *       * @param expression the expression      */
DECL|method|setExpression (Expression expression)
name|void
name|setExpression
parameter_list|(
name|Expression
name|expression
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

