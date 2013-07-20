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
name|ByteArrayInputStream
import|;
end_import

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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|StreamCache
import|;
end_import

begin_comment
comment|/**  * A {@link StreamCache} for caching using an in-memory byte array.  */
end_comment

begin_class
DECL|class|InputStreamCache
specifier|public
specifier|final
class|class
name|InputStreamCache
extends|extends
name|ByteArrayInputStream
implements|implements
name|StreamCache
block|{
DECL|method|InputStreamCache (byte[] data)
specifier|public
name|InputStreamCache
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
block|{
name|super
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
DECL|method|InputStreamCache (byte[] data, int count)
specifier|public
name|InputStreamCache
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|int
name|count
parameter_list|)
block|{
name|super
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|super
operator|.
name|count
operator|=
name|count
expr_stmt|;
block|}
DECL|method|writeTo (OutputStream os)
specifier|public
name|void
name|writeTo
parameter_list|(
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
block|{
name|os
operator|.
name|write
argument_list|(
name|buf
argument_list|,
name|pos
argument_list|,
name|count
operator|-
name|pos
argument_list|)
expr_stmt|;
block|}
DECL|method|inMemory ()
specifier|public
name|boolean
name|inMemory
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|length ()
specifier|public
name|long
name|length
parameter_list|()
block|{
return|return
name|count
return|;
block|}
block|}
end_class

end_unit

