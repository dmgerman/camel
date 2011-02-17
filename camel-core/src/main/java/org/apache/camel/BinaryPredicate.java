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
comment|/**  * A predicate which is evaluating a binary expression.  *<p/>  * The predicate has a left and right hand side expressions which  * is matched based on an operator.  *<p/>  * This predicate offers the {@link #matchesReturningFailureMessage} method  * which evaluates and return a detailed failure message if the predicate did not match.  *  * @version   */
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
comment|/**      * Evaluates the predicate on the message exchange and returns<tt>null</tt> if this      * exchange matches the predicate. If it did<b>not</b> match, then a failure message      * is returned detailing why it did not fail, which can be used for end users to understand      * the failure.      *      * @param exchange the message exchange      * @return<tt>null</tt> if the predicate matches.      */
DECL|method|matchesReturningFailureMessage (Exchange exchange)
name|String
name|matchesReturningFailureMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

