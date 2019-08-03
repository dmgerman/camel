begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|xml
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
name|InputStream
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|converter
operator|.
name|stream
operator|.
name|CachedOutputStream
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
name|converter
operator|.
name|stream
operator|.
name|ReaderCache
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
comment|/**  * A {@link org.apache.camel.StreamCache} for {@link javax.xml.transform.stream.StreamSource}s  */
end_comment

begin_class
DECL|class|StreamSourceCache
specifier|public
specifier|final
class|class
name|StreamSourceCache
extends|extends
name|StreamSource
implements|implements
name|StreamCache
block|{
DECL|field|streamCache
specifier|private
specifier|final
name|StreamCache
name|streamCache
decl_stmt|;
DECL|field|readCache
specifier|private
specifier|final
name|ReaderCache
name|readCache
decl_stmt|;
DECL|method|StreamSourceCache (StreamSource source, Exchange exchange)
specifier|public
name|StreamSourceCache
parameter_list|(
name|StreamSource
name|source
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|source
operator|.
name|getInputStream
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// set up CachedOutputStream with the properties
name|CachedOutputStream
name|cos
init|=
operator|new
name|CachedOutputStream
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|IOHelper
operator|.
name|copyAndCloseInput
argument_list|(
name|source
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|cos
argument_list|)
expr_stmt|;
name|streamCache
operator|=
name|cos
operator|.
name|newStreamCache
argument_list|()
expr_stmt|;
name|readCache
operator|=
literal|null
expr_stmt|;
name|setSystemId
argument_list|(
name|source
operator|.
name|getSystemId
argument_list|()
argument_list|)
expr_stmt|;
name|setInputStream
argument_list|(
operator|(
name|InputStream
operator|)
name|streamCache
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|source
operator|.
name|getReader
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|data
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|source
operator|.
name|getReader
argument_list|()
argument_list|)
decl_stmt|;
name|readCache
operator|=
operator|new
name|ReaderCache
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|streamCache
operator|=
literal|null
expr_stmt|;
name|setReader
argument_list|(
name|readCache
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|streamCache
operator|=
literal|null
expr_stmt|;
name|readCache
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|StreamSourceCache (StreamCache streamCache)
specifier|public
name|StreamSourceCache
parameter_list|(
name|StreamCache
name|streamCache
parameter_list|)
block|{
name|this
operator|.
name|streamCache
operator|=
name|streamCache
expr_stmt|;
if|if
condition|(
name|streamCache
operator|instanceof
name|InputStream
condition|)
block|{
name|setInputStream
argument_list|(
operator|(
name|InputStream
operator|)
name|streamCache
argument_list|)
expr_stmt|;
name|this
operator|.
name|readCache
operator|=
literal|null
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|streamCache
operator|instanceof
name|ReaderCache
condition|)
block|{
name|this
operator|.
name|readCache
operator|=
operator|(
name|ReaderCache
operator|)
name|streamCache
expr_stmt|;
name|setReader
argument_list|(
operator|(
name|java
operator|.
name|io
operator|.
name|Reader
operator|)
name|streamCache
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|readCache
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
block|{
if|if
condition|(
name|streamCache
operator|!=
literal|null
condition|)
block|{
name|streamCache
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|readCache
operator|!=
literal|null
condition|)
block|{
name|readCache
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
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
if|if
condition|(
name|streamCache
operator|!=
literal|null
condition|)
block|{
name|streamCache
operator|.
name|writeTo
argument_list|(
name|os
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|readCache
operator|!=
literal|null
condition|)
block|{
name|readCache
operator|.
name|writeTo
argument_list|(
name|os
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
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
name|streamCache
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|StreamSourceCache
argument_list|(
name|streamCache
operator|.
name|copy
argument_list|(
name|exchange
argument_list|)
argument_list|)
return|;
block|}
if|if
condition|(
name|readCache
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|StreamSourceCache
argument_list|(
name|readCache
operator|.
name|copy
argument_list|(
name|exchange
argument_list|)
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|inMemory ()
specifier|public
name|boolean
name|inMemory
parameter_list|()
block|{
if|if
condition|(
name|streamCache
operator|!=
literal|null
condition|)
block|{
return|return
name|streamCache
operator|.
name|inMemory
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|readCache
operator|!=
literal|null
condition|)
block|{
return|return
name|readCache
operator|.
name|inMemory
argument_list|()
return|;
block|}
else|else
block|{
comment|// should not happen
return|return
literal|true
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|length ()
specifier|public
name|long
name|length
parameter_list|()
block|{
if|if
condition|(
name|streamCache
operator|!=
literal|null
condition|)
block|{
return|return
name|streamCache
operator|.
name|length
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|readCache
operator|!=
literal|null
condition|)
block|{
return|return
name|readCache
operator|.
name|length
argument_list|()
return|;
block|}
else|else
block|{
comment|// should not happen
return|return
literal|0
return|;
block|}
block|}
block|}
end_class

end_unit

