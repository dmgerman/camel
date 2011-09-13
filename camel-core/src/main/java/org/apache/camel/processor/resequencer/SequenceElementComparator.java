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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_comment
comment|/**  * A strategy for comparing elements of a sequence.  *   * @version   */
end_comment

begin_interface
DECL|interface|SequenceElementComparator
specifier|public
interface|interface
name|SequenceElementComparator
parameter_list|<
name|E
parameter_list|>
extends|extends
name|Comparator
argument_list|<
name|E
argument_list|>
block|{
comment|/**      * Returns<code>true</code> if<code>o1</code> is an immediate predecessor      * of<code>o2</code>.      *       * @param o1 a sequence element.      * @param o2 a sequence element.      * @return true if its an immediate predecessor      */
DECL|method|predecessor (E o1, E o2)
name|boolean
name|predecessor
parameter_list|(
name|E
name|o1
parameter_list|,
name|E
name|o2
parameter_list|)
function_decl|;
comment|/**      * Returns<code>true</code> if<code>o1</code> is an immediate successor      * of<code>o2</code>.      *       * @param o1 a sequence element.      * @param o2 a sequence element.      * @return true if its an immediate successor      */
DECL|method|successor (E o1, E o2)
name|boolean
name|successor
parameter_list|(
name|E
name|o1
parameter_list|,
name|E
name|o2
parameter_list|)
function_decl|;
comment|/**      * Returns<tt>true</tt> if the<code>o1</code> can be used in this comparator.      *      * @param o1 a sequence element      * @return true if its usable for this comparator      */
DECL|method|isValid (E o1)
name|boolean
name|isValid
parameter_list|(
name|E
name|o1
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

