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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * Implementations support navigating a graph where you can traverse forward and each next  * returns a {@link List} of outputs of type<tt>T</tt> that can contain<tt>0..n</tt> nodes.  */
end_comment

begin_interface
DECL|interface|Navigate
specifier|public
interface|interface
name|Navigate
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * Next group of outputs      *<p/>      * Important only invoke this once, as this method do not carry state, and is not intended to be used in a while loop,      * but used by a if statement instead.      *      * @return next group or<tt>null</tt> if no more outputs      */
DECL|method|next ()
name|List
argument_list|<
name|T
argument_list|>
name|next
parameter_list|()
function_decl|;
comment|/**      * Are there more outputs?      *<p/>      * Important only invoke this once, as this method do not carry state, and is not intended to be used in a while loop,      * but used by a if statement instead.      *      * @return<tt>true</tt> if more outputs      */
DECL|method|hasNext ()
name|boolean
name|hasNext
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

