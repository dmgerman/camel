begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_comment
comment|/**  * This class is used by the toknizer to extract data while reading from the stream.  * REVIST it is used package internally but may be moved to some common package.  */
end_comment

begin_class
DECL|class|RecordableInputStream
class|class
name|RecordableInputStream
extends|extends
name|FilterInputStream
block|{
DECL|field|buf
specifier|private
name|TrimmableByteArrayOutputStream
name|buf
decl_stmt|;
DECL|field|charset
specifier|private
name|String
name|charset
decl_stmt|;
DECL|field|recording
specifier|private
name|boolean
name|recording
decl_stmt|;
DECL|method|RecordableInputStream (InputStream in, String charset)
specifier|protected
name|RecordableInputStream
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|String
name|charset
parameter_list|)
block|{
name|super
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|this
operator|.
name|buf
operator|=
operator|new
name|TrimmableByteArrayOutputStream
argument_list|()
expr_stmt|;
name|this
operator|.
name|charset
operator|=
name|charset
expr_stmt|;
name|this
operator|.
name|recording
operator|=
literal|true
expr_stmt|;
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
name|int
name|c
init|=
name|super
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|>
literal|0
operator|&&
name|recording
condition|)
block|{
name|buf
operator|.
name|write
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
return|return
name|c
return|;
block|}
annotation|@
name|Override
DECL|method|read (byte[] b, int off, int len)
specifier|public
name|int
name|read
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|n
init|=
name|super
operator|.
name|read
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
decl_stmt|;
if|if
condition|(
name|n
operator|>
literal|0
operator|&&
name|recording
condition|)
block|{
name|buf
operator|.
name|write
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
return|return
name|n
return|;
block|}
DECL|method|getText (int pos)
specifier|public
name|String
name|getText
parameter_list|(
name|int
name|pos
parameter_list|)
block|{
name|String
name|t
init|=
literal|null
decl_stmt|;
name|recording
operator|=
literal|false
expr_stmt|;
try|try
block|{
if|if
condition|(
name|charset
operator|==
literal|null
condition|)
block|{
name|t
operator|=
operator|new
name|String
argument_list|(
name|buf
operator|.
name|getByteArray
argument_list|()
argument_list|,
literal|0
argument_list|,
name|pos
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|t
operator|=
operator|new
name|String
argument_list|(
name|buf
operator|.
name|getByteArray
argument_list|()
argument_list|,
literal|0
argument_list|,
name|pos
argument_list|,
name|charset
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
comment|// ignore it as this encoding exception should have been caught earlier while scanning.
block|}
finally|finally
block|{
name|buf
operator|.
name|trim
argument_list|(
name|pos
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
return|return
name|t
return|;
block|}
DECL|method|getBytes (int pos)
specifier|public
name|byte
index|[]
name|getBytes
parameter_list|(
name|int
name|pos
parameter_list|)
block|{
name|recording
operator|=
literal|false
expr_stmt|;
name|byte
index|[]
name|b
init|=
name|buf
operator|.
name|toByteArray
argument_list|(
name|pos
argument_list|)
decl_stmt|;
name|buf
operator|.
name|trim
argument_list|(
name|pos
argument_list|,
literal|0
argument_list|)
expr_stmt|;
return|return
name|b
return|;
block|}
DECL|method|record ()
specifier|public
name|void
name|record
parameter_list|()
block|{
name|recording
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|size ()
name|int
name|size
parameter_list|()
block|{
return|return
name|buf
operator|.
name|size
argument_list|()
return|;
block|}
DECL|class|TrimmableByteArrayOutputStream
specifier|private
specifier|static
class|class
name|TrimmableByteArrayOutputStream
extends|extends
name|ByteArrayOutputStream
block|{
DECL|method|trim (int head, int tail)
specifier|public
name|void
name|trim
parameter_list|(
name|int
name|head
parameter_list|,
name|int
name|tail
parameter_list|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|buf
argument_list|,
name|head
argument_list|,
name|buf
argument_list|,
literal|0
argument_list|,
name|count
operator|-
name|head
operator|-
name|tail
argument_list|)
expr_stmt|;
name|count
operator|-=
name|head
operator|+
name|tail
expr_stmt|;
block|}
DECL|method|toByteArray (int len)
specifier|public
name|byte
index|[]
name|toByteArray
parameter_list|(
name|int
name|len
parameter_list|)
block|{
name|byte
index|[]
name|b
init|=
operator|new
name|byte
index|[
name|len
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|b
argument_list|,
literal|0
argument_list|,
name|len
argument_list|)
expr_stmt|;
return|return
name|b
return|;
block|}
DECL|method|getByteArray ()
name|byte
index|[]
name|getByteArray
parameter_list|()
block|{
return|return
name|buf
return|;
block|}
block|}
block|}
end_class

end_unit

