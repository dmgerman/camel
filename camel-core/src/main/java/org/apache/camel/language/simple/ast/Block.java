begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.simple.ast
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|simple
operator|.
name|ast
package|;
end_package

begin_comment
comment|/**  * A model which is a block, containing other nodes.  *<p/>  * This node will keep adding other nodes until no longer  * accepted, by returning<tt>false</tt> in the {@link #acceptAndAddNode(SimpleNode)} method.  */
end_comment

begin_interface
DECL|interface|Block
specifier|public
interface|interface
name|Block
extends|extends
name|SimpleNode
block|{
comment|/**      * Whether to accept and add the given node in this block.      *      * @param node the other node.      * @return<tt>true</tt> to accept and add to this block,<tt>false</tt> to end this block.      */
DECL|method|acceptAndAddNode (SimpleNode node)
name|boolean
name|acceptAndAddNode
parameter_list|(
name|SimpleNode
name|node
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

