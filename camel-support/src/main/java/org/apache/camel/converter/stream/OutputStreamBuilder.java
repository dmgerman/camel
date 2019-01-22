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
name|support
operator|.
name|ExchangeHelper
import|;
end_import

begin_comment
comment|// TODO: We can move this under org.apache.camel.support to have them all under the same package name
end_comment

begin_comment
comment|/**  * Utility to hide the complexity of choosing which OutputStream  * implementation to choose.  *<p/>  * Itself masquerades as an OutputStream, but really delegates to a  * CachedOutputStream of a ByteArrayOutputStream.  */
end_comment

begin_class
DECL|class|OutputStreamBuilder
specifier|public
specifier|final
class|class
name|OutputStreamBuilder
extends|extends
name|OutputStream
block|{
DECL|field|outputStream
specifier|private
specifier|final
name|OutputStream
name|outputStream
decl_stmt|;
DECL|method|OutputStreamBuilder (final Exchange exchange)
specifier|private
name|OutputStreamBuilder
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|ExchangeHelper
operator|.
name|isStreamCachingEnabled
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|outputStream
operator|=
operator|new
name|CachedOutputStream
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|outputStream
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Creates a new OutputStreamBuilder with the current exchange      *<p/>      * Use the {@link #build()} when writing to the stream is finished,      * and you need the result of this operation.      *      * @param exchange the current Exchange      * @return the builder      */
DECL|method|withExchange (final Exchange exchange)
specifier|public
specifier|static
name|OutputStreamBuilder
name|withExchange
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
operator|new
name|OutputStreamBuilder
argument_list|(
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|write (final byte[] b, final int off, final int len)
specifier|public
name|void
name|write
parameter_list|(
specifier|final
name|byte
index|[]
name|b
parameter_list|,
specifier|final
name|int
name|off
parameter_list|,
specifier|final
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
name|outputStream
operator|.
name|write
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|write (final byte[] b)
specifier|public
name|void
name|write
parameter_list|(
specifier|final
name|byte
index|[]
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|outputStream
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|write (final int b)
specifier|public
name|void
name|write
parameter_list|(
specifier|final
name|int
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|outputStream
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|flush ()
specifier|public
name|void
name|flush
parameter_list|()
throws|throws
name|IOException
block|{
name|outputStream
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|outputStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Builds the result of using this builder as either a      * {@link org.apache.camel.converter.stream.CachedOutputStream} if stream caching is enabled,      * otherwise byte[].      */
DECL|method|build ()
specifier|public
name|Object
name|build
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|outputStream
operator|instanceof
name|CachedOutputStream
condition|)
block|{
return|return
operator|(
operator|(
name|CachedOutputStream
operator|)
name|outputStream
operator|)
operator|.
name|newStreamCache
argument_list|()
return|;
block|}
return|return
operator|(
operator|(
name|ByteArrayOutputStream
operator|)
name|outputStream
operator|)
operator|.
name|toByteArray
argument_list|()
return|;
block|}
block|}
end_class

end_unit

