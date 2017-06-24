begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Date
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
name|MessageHistory
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
name|NamedNode
import|;
end_import

begin_comment
comment|/**  * A factory to create {@link MessageHistory} instances.  */
end_comment

begin_interface
DECL|interface|MessageHistoryFactory
specifier|public
interface|interface
name|MessageHistoryFactory
block|{
comment|/**      * Creates a new {@link MessageHistory}      *      * @param routeId   the route id      * @param node      the node in the route      * @param timestamp the time the message processed at this node.      * @return a new {@link MessageHistory}      * @deprecated use {@link #newMessageHistory(String, NamedNode, long)}      */
annotation|@
name|Deprecated
DECL|method|newMessageHistory (String routeId, NamedNode node, Date timestamp)
name|MessageHistory
name|newMessageHistory
parameter_list|(
name|String
name|routeId
parameter_list|,
name|NamedNode
name|node
parameter_list|,
name|Date
name|timestamp
parameter_list|)
function_decl|;
comment|/**      * Creates a new {@link MessageHistory}      *      * @param routeId   the route id      * @param node      the node in the route      * @param timestamp the time the message processed at this node.      * @return a new {@link MessageHistory}      */
DECL|method|newMessageHistory (String routeId, NamedNode node, long timestamp)
name|MessageHistory
name|newMessageHistory
parameter_list|(
name|String
name|routeId
parameter_list|,
name|NamedNode
name|node
parameter_list|,
name|long
name|timestamp
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

