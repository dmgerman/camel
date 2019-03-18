begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FilterInputStream
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
name|Exchange
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|IOHelper
import|;
end_import

begin_comment
comment|/**  * A {@link StreamCache} for {@link java.io.ByteArrayInputStream}  */
end_comment

begin_class
DECL|class|ByteArrayInputStreamCache
specifier|public
class|class
name|ByteArrayInputStreamCache
extends|extends
name|FilterInputStream
implements|implements
name|StreamCache
block|{
DECL|field|length
specifier|private
specifier|final
name|int
name|length
decl_stmt|;
DECL|field|byteArrayForCopy
specifier|private
name|byte
index|[]
name|byteArrayForCopy
decl_stmt|;
DECL|method|ByteArrayInputStreamCache (ByteArrayInputStream in)
specifier|public
name|ByteArrayInputStreamCache
parameter_list|(
name|ByteArrayInputStream
name|in
parameter_list|)
block|{
name|super
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|this
operator|.
name|length
operator|=
name|in
operator|.
name|available
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
block|{
try|try
block|{
name|super
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
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
name|IOHelper
operator|.
name|copyAndCloseInput
argument_list|(
name|in
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
DECL|method|copy (Exchange exchange)
specifier|public
name|StreamCache
name|copy
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|byteArrayForCopy
operator|==
literal|null
condition|)
block|{
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|(
name|in
operator|.
name|available
argument_list|()
argument_list|)
decl_stmt|;
name|IOHelper
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|baos
argument_list|)
expr_stmt|;
comment|// reset so that the stream can be reused
name|reset
argument_list|()
expr_stmt|;
comment|// cache the byte array, in order not to copy the byte array in the next call again
name|byteArrayForCopy
operator|=
name|baos
operator|.
name|toByteArray
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|InputStreamCache
argument_list|(
name|byteArrayForCopy
argument_list|)
return|;
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
annotation|@
name|Override
DECL|method|length ()
specifier|public
name|long
name|length
parameter_list|()
block|{
return|return
name|length
return|;
block|}
block|}
end_class

end_unit

