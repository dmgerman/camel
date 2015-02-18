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
name|java
operator|.
name|io
operator|.
name|StringReader
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
name|ParallelProcessableStream
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
comment|/**  * A {@link org.apache.camel.StreamCache} for String {@link java.io.Reader}s  */
end_comment

begin_class
DECL|class|ReaderCache
specifier|public
class|class
name|ReaderCache
extends|extends
name|StringReader
implements|implements
name|StreamCache
implements|,
name|ParallelProcessableStream
block|{
DECL|field|data
specifier|private
specifier|final
name|String
name|data
decl_stmt|;
DECL|method|ReaderCache (String data)
specifier|public
name|ReaderCache
parameter_list|(
name|String
name|data
parameter_list|)
block|{
name|super
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
block|}
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
comment|// Do not release the string for caching
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
name|os
operator|.
name|write
argument_list|(
name|data
operator|.
name|getBytes
argument_list|()
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
name|data
operator|.
name|length
argument_list|()
return|;
block|}
DECL|method|getData ()
name|String
name|getData
parameter_list|()
block|{
return|return
name|data
return|;
block|}
annotation|@
name|Override
DECL|method|copy ()
specifier|public
name|ParallelProcessableStream
name|copy
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|ReaderCache
argument_list|(
name|data
argument_list|)
return|;
block|}
block|}
end_class

end_unit

