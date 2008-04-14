begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|ExpressionSupport
import|;
end_import

begin_comment
comment|/**  * A helper class for developers wishing to implement an {@link Expression} using Java code with a minimum amount  * of code to write so that the developer only needs to implement the {@link #evaluate(Exchange)} method.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ExpressionAdapter
specifier|public
specifier|abstract
class|class
name|ExpressionAdapter
extends|extends
name|ExpressionSupport
argument_list|<
name|Exchange
argument_list|>
block|{
DECL|method|evaluate (Exchange exchange)
specifier|public
specifier|abstract
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
DECL|method|assertionFailureMessage (Exchange exchange)
specifier|protected
name|String
name|assertionFailureMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

