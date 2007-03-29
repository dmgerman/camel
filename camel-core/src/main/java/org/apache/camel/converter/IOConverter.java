begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
package|;
end_package

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
name|java
operator|.
name|io
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_comment
comment|/**  * Some core java.io based converters  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|IOConverter
specifier|public
class|class
name|IOConverter
block|{
annotation|@
name|Converter
DECL|method|toInputStream (File file)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
return|return
operator|new
name|BufferedInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toReader (File file)
specifier|public
specifier|static
name|BufferedReader
name|toReader
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
return|return
operator|new
name|BufferedReader
argument_list|(
operator|new
name|FileReader
argument_list|(
name|file
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toOutputStream (File file)
specifier|public
specifier|static
name|OutputStream
name|toOutputStream
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
return|return
operator|new
name|BufferedOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toWriter (File file)
specifier|public
specifier|static
name|BufferedWriter
name|toWriter
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|BufferedWriter
argument_list|(
operator|new
name|FileWriter
argument_list|(
name|file
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toReader (InputStream in)
specifier|public
specifier|static
name|Reader
name|toReader
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
return|return
operator|new
name|InputStreamReader
argument_list|(
name|in
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toWriter (OutputStream out)
specifier|public
specifier|static
name|Writer
name|toWriter
parameter_list|(
name|OutputStream
name|out
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
return|return
operator|new
name|OutputStreamWriter
argument_list|(
name|out
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toInputStream (String text)
specifier|public
specifier|static
name|StringReader
name|toInputStream
parameter_list|(
name|String
name|text
parameter_list|)
block|{
comment|// TODO could we automatically find this?
return|return
operator|new
name|StringReader
argument_list|(
name|text
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toByteArray (String text)
specifier|public
specifier|static
name|byte
index|[]
name|toByteArray
parameter_list|(
name|String
name|text
parameter_list|)
block|{
comment|// TODO could we automatically find this?
return|return
name|text
operator|.
name|getBytes
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toString (byte[] data)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
block|{
return|return
operator|new
name|String
argument_list|(
name|data
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toInputStream (byte[] data)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
block|{
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
return|;
block|}
block|}
end_class

end_unit

