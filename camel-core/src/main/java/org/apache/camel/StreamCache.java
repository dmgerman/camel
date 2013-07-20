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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_comment
comment|/**  * Tagging interface to indicate that a type is capable of caching the underlying data stream.  *<p/>  * This is a useful feature for avoiding message re-readability issues.  * This interface is mainly used by the {@link org.apache.camel.processor.interceptor.StreamCachingInterceptor}  * for determining if/how to wrap a stream-based message.  *  * @version   */
end_comment

begin_interface
DECL|interface|StreamCache
specifier|public
interface|interface
name|StreamCache
block|{
DECL|field|DEFAULT_SPOOL_THRESHOLD
name|long
name|DEFAULT_SPOOL_THRESHOLD
init|=
literal|128
operator|*
literal|1024
decl_stmt|;
comment|/**      * Resets the StreamCache for a new stream consumption.      */
DECL|method|reset ()
name|void
name|reset
parameter_list|()
function_decl|;
comment|/**      * Writes the stream to the given output      *      * @param os the destination to write to      * @throws java.io.IOException is thrown if write fails      */
DECL|method|writeTo (OutputStream os)
name|void
name|writeTo
parameter_list|(
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Whether this {@link StreamCache} is in memory only or      * spooled to persistent storage such as files.      */
DECL|method|inMemory ()
name|boolean
name|inMemory
parameter_list|()
function_decl|;
comment|/**      * Gets the length of the cached stream.      *<p/>      * The implementation may return<tt>0</tt> in cases where the length      * cannot be computed, or if the implementation does not support this.      *      * @return number of bytes in the cache.      */
DECL|method|length ()
name|long
name|length
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

