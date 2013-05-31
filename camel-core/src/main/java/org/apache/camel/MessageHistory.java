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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_comment
comment|/**  * Represents the history of a Camel {@link Message} how it was routed by the Camel routing engine.  */
end_comment

begin_interface
DECL|interface|MessageHistory
specifier|public
interface|interface
name|MessageHistory
block|{
comment|/**      * Gets the route id at the point of this history.      */
DECL|method|getRouteId ()
name|String
name|getRouteId
parameter_list|()
function_decl|;
comment|/**      * Gets the node at the point of this history.      */
DECL|method|getNode ()
name|NamedNode
name|getNode
parameter_list|()
function_decl|;
comment|/**      * Gets the timestamp at the point of this history.      */
DECL|method|getTimestamp ()
name|Date
name|getTimestamp
parameter_list|()
function_decl|;
comment|/**      * Gets the elapsed time in millis processing the node took      */
DECL|method|getElapsed ()
name|long
name|getElapsed
parameter_list|()
function_decl|;
comment|/**      * Used for signalling that processing of the node is done.      */
DECL|method|nodeProcessingDone ()
name|void
name|nodeProcessingDone
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

