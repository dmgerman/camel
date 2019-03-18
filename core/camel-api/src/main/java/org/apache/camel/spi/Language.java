begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
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
name|Predicate
import|;
end_import

begin_comment
comment|/**  * Represents a language to be used for {@link Expression} or {@link Predicate} instances  */
end_comment

begin_interface
DECL|interface|Language
specifier|public
interface|interface
name|Language
block|{
comment|/**      * Creates a predicate based on the given string input      *      * @param expression  the expression      * @return the created predicate      */
DECL|method|createPredicate (String expression)
name|Predicate
name|createPredicate
parameter_list|(
name|String
name|expression
parameter_list|)
function_decl|;
comment|/**      * Creates an expression based on the given string input      *      * @param expression  the expression as a string input      * @return the created expression      */
DECL|method|createExpression (String expression)
name|Expression
name|createExpression
parameter_list|(
name|String
name|expression
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

