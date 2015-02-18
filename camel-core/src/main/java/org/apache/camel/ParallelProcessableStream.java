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
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * Tagging interface to indicate that a stream can be used in parallel  * processing by offering a copy method.  *<p/>  * This is a useful feature for avoiding message re-readability issues which can  * occur when the same message is processed by several threads. This interface  * is mainly used by the {@link org.apache.camel.processor.MulticastProcessor}  * and should be implemented by all implementers of  * {@link org.apache.camel.StreamCache}  *   * @version  */
end_comment

begin_interface
DECL|interface|ParallelProcessableStream
specifier|public
interface|interface
name|ParallelProcessableStream
block|{
comment|/**      * Create a copy of the stream. If possible use the same cached data in the      * copied instance.      *<p>      * This method is useful for parallel processing.      *       * @throws java.io.IOException      *             if the copy fails      */
DECL|method|copy ()
name|ParallelProcessableStream
name|copy
parameter_list|()
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

