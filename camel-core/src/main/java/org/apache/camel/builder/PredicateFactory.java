begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Predicate
import|;
end_import

begin_comment
comment|/**  * A Factory of {@link Predicate} objects typically implemented by a builder such as @{XPathBuilder}  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|PredicateFactory
specifier|public
interface|interface
name|PredicateFactory
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
block|{
comment|/**      * Creates a predicate object      *      * @return the newly created expression object      */
DECL|method|createPredicate ()
name|Predicate
argument_list|<
name|E
argument_list|>
name|createPredicate
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

