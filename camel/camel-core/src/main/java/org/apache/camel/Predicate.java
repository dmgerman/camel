begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Evaluates a binary predicate on the message exchange  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|Predicate
specifier|public
interface|interface
name|Predicate
parameter_list|<
name|E
parameter_list|>
block|{
comment|/**      * Evaluates the predicate on the message exchange      *      * @param exchange the message exchange      * @return true if the predicate matches      */
DECL|method|evaluate (E exchange)
name|boolean
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

