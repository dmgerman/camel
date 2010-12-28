begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.routebox.strategy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|routebox
operator|.
name|strategy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Exchange
import|;
end_import

begin_comment
comment|/**  * A strategy for identifying the route consumer in the routebox where the exchange should to be dispatched  */
end_comment

begin_interface
DECL|interface|RouteboxDispatchStrategy
specifier|public
interface|interface
name|RouteboxDispatchStrategy
block|{
comment|/**      * Receives an incoming exchange and consumer list and identifies the inner route consumer for dispatching the exchange      *      * @param destinations the list of possible real-time inner route consumers available      *        to where the exchange can be dispatched in the routebox      * @param exchange the incoming exchange      * @return a selected consumer to whom the exchange can be directed      * @throws Exception is thrown if error      */
DECL|method|selectDestinationUri (List<URI> destinations, Exchange exchange)
name|URI
name|selectDestinationUri
parameter_list|(
name|List
argument_list|<
name|URI
argument_list|>
name|destinations
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

