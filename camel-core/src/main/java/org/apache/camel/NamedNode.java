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

begin_interface
DECL|interface|NamedNode
specifier|public
interface|interface
name|NamedNode
block|{
comment|/**      * Gets the value of the id property.      */
DECL|method|getId ()
name|String
name|getId
parameter_list|()
function_decl|;
comment|/**      * Returns a short name for this node which can be useful for ID generation or referring to related resources like images      *      * @return defaults to "node" but derived nodes should overload this to provide a unique name      */
DECL|method|getShortName ()
name|String
name|getShortName
parameter_list|()
function_decl|;
comment|/**      * Returns the description text or null if there is no description text associated with this node      */
DECL|method|getDescriptionText ()
name|String
name|getDescriptionText
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

