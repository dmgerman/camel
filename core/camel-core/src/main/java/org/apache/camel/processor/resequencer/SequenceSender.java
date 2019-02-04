begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.resequencer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|resequencer
package|;
end_package

begin_comment
comment|/**   * An interface used by the {@link ResequencerEngine#deliver()} and   * {@link ResequencerEngine#deliverNext()} methods to send out re-ordered   * elements.   *    */
end_comment

begin_interface
DECL|interface|SequenceSender
specifier|public
interface|interface
name|SequenceSender
parameter_list|<
name|E
parameter_list|>
block|{
comment|/**      * Sends the given element.      *      * @param o a re-ordered element.      * @throws Exception if delivery fails.      */
DECL|method|sendElement (E o)
name|void
name|sendElement
parameter_list|(
name|E
name|o
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

