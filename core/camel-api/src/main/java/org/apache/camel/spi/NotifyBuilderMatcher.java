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
name|Exchange
import|;
end_import

begin_comment
comment|/**  * Allows to be used in combination with<tt>NotifyBuilder</tt> as external predicate implementations to compute  * if the exchange matches.  *<p/>  * This is used by the mock endpoint, for example.  */
end_comment

begin_interface
DECL|interface|NotifyBuilderMatcher
specifier|public
interface|interface
name|NotifyBuilderMatcher
block|{
comment|/**      * When an exchange was received      *      * @param exchange the exchange      */
DECL|method|notifyBuilderOnExchange (Exchange exchange)
name|void
name|notifyBuilderOnExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Whether the predicate matches      */
DECL|method|notifyBuilderMatches ()
name|boolean
name|notifyBuilderMatches
parameter_list|()
function_decl|;
comment|/**      * Reset state      */
DECL|method|notifyBuilderReset ()
name|void
name|notifyBuilderReset
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

