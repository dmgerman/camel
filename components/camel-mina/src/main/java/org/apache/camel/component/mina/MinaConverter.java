begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina
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
name|ObjectInput
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
name|Converter
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
name|IOConverter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|common
operator|.
name|ByteBuffer
import|;
end_import

begin_comment
comment|/**  * A set of converter methods for working with MINA types  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|MinaConverter
specifier|public
class|class
name|MinaConverter
block|{
annotation|@
name|Converter
DECL|method|toByteArray (ByteBuffer buffer)
specifier|public
specifier|static
name|byte
index|[]
name|toByteArray
parameter_list|(
name|ByteBuffer
name|buffer
parameter_list|)
block|{
name|byte
index|[]
name|answer
init|=
operator|new
name|byte
index|[
name|buffer
operator|.
name|remaining
argument_list|()
index|]
decl_stmt|;
name|buffer
operator|.
name|get
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Converter
DECL|method|toString (ByteBuffer buffer)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|ByteBuffer
name|buffer
parameter_list|)
block|{
comment|// TODO: CAMEL-381, we should have type converters to strings that accepts a Charset parameter to handle encoding
return|return
name|IOConverter
operator|.
name|toString
argument_list|(
name|toByteArray
argument_list|(
name|buffer
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toInputStream (ByteBuffer buffer)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|ByteBuffer
name|buffer
parameter_list|)
block|{
return|return
name|buffer
operator|.
name|asInputStream
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toObjectInput (ByteBuffer buffer)
specifier|public
specifier|static
name|ObjectInput
name|toObjectInput
parameter_list|(
name|ByteBuffer
name|buffer
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|IOConverter
operator|.
name|toObjectInput
argument_list|(
name|toInputStream
argument_list|(
name|buffer
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toByteBuffer (byte[] bytes)
specifier|public
specifier|static
name|ByteBuffer
name|toByteBuffer
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|ByteBuffer
name|buf
init|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
name|bytes
operator|.
name|length
argument_list|)
decl_stmt|;
name|buf
operator|.
name|put
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
return|return
name|buf
return|;
block|}
block|}
end_class

end_unit

