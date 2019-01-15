begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.stream
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|stream
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_comment
comment|/**  * A {@link ByteArrayOutputStream} that is capable of returning a  * {@link InputStreamCache} view of the buffer.  *<p/>  * This implementation avoids any buffer copying when caching in memory {@link java.io.InputStream}  * as the buffer can be shared.  */
end_comment

begin_class
DECL|class|CachedByteArrayOutputStream
specifier|public
specifier|final
class|class
name|CachedByteArrayOutputStream
extends|extends
name|ByteArrayOutputStream
block|{
DECL|method|CachedByteArrayOutputStream (int size)
specifier|public
name|CachedByteArrayOutputStream
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|super
argument_list|(
name|size
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new {@link InputStreamCache} view of the byte array      */
DECL|method|newInputStreamCache ()
specifier|public
name|InputStreamCache
name|newInputStreamCache
parameter_list|()
block|{
return|return
operator|new
name|InputStreamCache
argument_list|(
name|buf
argument_list|,
name|count
argument_list|)
return|;
block|}
block|}
end_class

end_unit

