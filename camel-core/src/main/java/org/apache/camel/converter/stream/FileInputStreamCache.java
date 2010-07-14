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
name|Closeable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RuntimeCamelException
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

begin_class
DECL|class|FileInputStreamCache
specifier|public
class|class
name|FileInputStreamCache
extends|extends
name|InputStream
implements|implements
name|StreamCache
implements|,
name|Closeable
block|{
DECL|field|stream
specifier|private
name|InputStream
name|stream
decl_stmt|;
DECL|field|file
specifier|private
name|File
name|file
decl_stmt|;
DECL|method|FileInputStreamCache (File file)
specifier|public
name|FileInputStreamCache
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
name|this
operator|.
name|file
operator|=
name|file
expr_stmt|;
name|this
operator|.
name|stream
operator|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
if|if
condition|(
name|isSteamOpened
argument_list|()
condition|)
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|getInputStream
argument_list|()
argument_list|)
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
try|try
block|{
comment|// reset by closing and creating a new stream based on the file
name|close
argument_list|()
expr_stmt|;
comment|// reset by creating a new stream based on the file
name|stream
operator|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Cannot reset stream from file "
operator|+
name|file
argument_list|,
name|e
argument_list|)
throw|;
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
name|copy
argument_list|(
name|getInputStream
argument_list|()
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|available ()
specifier|public
name|int
name|available
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|getInputStream
argument_list|()
operator|.
name|available
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|read ()
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|getInputStream
argument_list|()
operator|.
name|read
argument_list|()
return|;
block|}
DECL|method|getInputStream ()
specifier|protected
name|InputStream
name|getInputStream
parameter_list|()
block|{
return|return
name|stream
return|;
block|}
DECL|method|isSteamOpened ()
specifier|private
name|boolean
name|isSteamOpened
parameter_list|()
block|{
if|if
condition|(
name|stream
operator|!=
literal|null
operator|&&
name|stream
operator|instanceof
name|FileInputStream
condition|)
block|{
return|return
operator|(
operator|(
name|FileInputStream
operator|)
name|stream
operator|)
operator|.
name|getChannel
argument_list|()
operator|.
name|isOpen
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|stream
operator|!=
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

