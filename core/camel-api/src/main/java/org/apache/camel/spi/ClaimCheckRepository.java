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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Service
import|;
end_import

begin_comment
comment|/**  * Access to a repository of keys to implement the  *<a href="http://camel.apache.org/claim-check.html">Claim Check</a> pattern.  *<p/>  * The<tt>add</tt> and<tt>contains</tt> methods is operating according to the {@link java.util.Map} contract,  * and the<tt>push</tt> and<tt>pop</tt> methods is operating according to the {@link java.util.Stack} contract.  *<p/>  * See important details about the Claim Check EIP implementation in Apache Camel at {@link org.apache.camel.processor.ClaimCheckProcessor}.  */
end_comment

begin_interface
DECL|interface|ClaimCheckRepository
specifier|public
interface|interface
name|ClaimCheckRepository
extends|extends
name|Service
block|{
comment|/**      * Adds the exchange to the repository.      *      * @param key the claim check key      * @return<tt>true</tt> if this repository did<b>not</b> already contain the specified key      */
DECL|method|add (String key, Exchange exchange)
name|boolean
name|add
parameter_list|(
name|String
name|key
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Returns<tt>true</tt> if this repository contains the specified key.      *      * @param key the claim check key      * @return<tt>true</tt> if this repository contains the specified key      */
DECL|method|contains (String key)
name|boolean
name|contains
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
comment|/**      * Gets the exchange from the repository.      *      * @param key the claim check key      */
DECL|method|get (String key)
name|Exchange
name|get
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
comment|/**      * Gets and removes the exchange from the repository.      *      * @param key the claim check key      * @return the removed exchange, or<tt>null</tt> if the key did not exists.      */
DECL|method|getAndRemove (String key)
name|Exchange
name|getAndRemove
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
comment|/**      * Pushes the exchange on top of the repository.      */
DECL|method|push (Exchange exchange)
name|void
name|push
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Pops the repository and returns the latest. Or returns<tt>null</tt> if the stack is empty.      */
DECL|method|pop ()
name|Exchange
name|pop
parameter_list|()
function_decl|;
comment|/**      * Clear the repository.      */
DECL|method|clear ()
name|void
name|clear
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

