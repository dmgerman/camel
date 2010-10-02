begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_comment
comment|/**  * A predicate which is evaluating a binary expression.  *<p/>  * The predicate has a left and right hand side expressions which  * is matched based on an operator.  *<p/>  * This predicate can return information about the evaluated expressions  * which allows you to get detailed information, so you better understand  * why the predicate did not match.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|BinaryPredicate
specifier|public
interface|interface
name|BinaryPredicate
extends|extends
name|Predicate
block|{
comment|/**      * Gets the operator      *      * @return the operator text      */
DECL|method|getOperator ()
name|String
name|getOperator
parameter_list|()
function_decl|;
comment|/**      * Gets the left hand side expression      *      * @return the left expression      */
DECL|method|getLeft ()
name|Expression
name|getLeft
parameter_list|()
function_decl|;
comment|/**      * Gets the right hand side expression      *      * @return the right expression      */
DECL|method|getRight ()
name|Expression
name|getRight
parameter_list|()
function_decl|;
comment|/**      * Gets the evaluated left hand side value.      *<p/>      * Beware of thread safety that the result of the {@link #getRightValue()} may in fact be from another evaluation.      *      * @return the left value, may be<tt>null</tt> if predicate has not been matched yet.      */
DECL|method|getLeftValue ()
name|Object
name|getLeftValue
parameter_list|()
function_decl|;
comment|/**      * Gets the evaluated right hand side value.      *<p/>      * Beware of thread safety that the result of the {@link #getLeftValue()} may in fact be from another evaluation.      *      * @return the right value, may be<tt>null</tt> if predicate has not been matched yet.      */
DECL|method|getRightValue ()
name|Object
name|getRightValue
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

