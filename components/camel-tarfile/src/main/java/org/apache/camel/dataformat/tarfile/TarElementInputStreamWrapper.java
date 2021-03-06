begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.tarfile
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|tarfile
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedInputStream
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

begin_comment
comment|/**  * Keeps a handle on the original {@link InputStream} even after closing the buffered input stream.  */
end_comment

begin_class
DECL|class|TarElementInputStreamWrapper
specifier|public
class|class
name|TarElementInputStreamWrapper
extends|extends
name|BufferedInputStream
block|{
DECL|method|TarElementInputStreamWrapper (InputStream in, int size)
specifier|public
name|TarElementInputStreamWrapper
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|super
argument_list|(
name|in
argument_list|,
name|size
argument_list|)
expr_stmt|;
block|}
DECL|method|TarElementInputStreamWrapper (InputStream in)
specifier|public
name|TarElementInputStreamWrapper
parameter_list|(
name|InputStream
name|in
parameter_list|)
block|{
name|super
argument_list|(
name|in
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
throws|throws
name|IOException
block|{
name|InputStream
name|input
init|=
name|in
decl_stmt|;
try|try
block|{
name|in
operator|=
literal|null
expr_stmt|;
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|in
operator|=
name|input
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

