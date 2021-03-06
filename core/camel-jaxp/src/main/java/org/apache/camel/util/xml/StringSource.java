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
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Externalizable
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
name|ObjectInput
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutput
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
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
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
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

begin_comment
comment|/**  * A helper class which provides a JAXP {@link javax.xml.transform.Source  * Source} from a String which can be read as many times as required. Encoding  * is default UTF-8.  */
end_comment

begin_class
DECL|class|StringSource
specifier|public
class|class
name|StringSource
extends|extends
name|StreamSource
implements|implements
name|Externalizable
block|{
DECL|field|text
specifier|private
name|String
name|text
decl_stmt|;
DECL|field|encoding
specifier|private
name|String
name|encoding
init|=
literal|"UTF-8"
decl_stmt|;
DECL|method|StringSource ()
specifier|public
name|StringSource
parameter_list|()
block|{     }
DECL|method|StringSource (String text)
specifier|public
name|StringSource
parameter_list|(
name|String
name|text
parameter_list|)
block|{
if|if
condition|(
name|text
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"text must be specified"
argument_list|)
throw|;
block|}
name|this
operator|.
name|text
operator|=
name|text
expr_stmt|;
block|}
DECL|method|StringSource (String text, String systemId)
specifier|public
name|StringSource
parameter_list|(
name|String
name|text
parameter_list|,
name|String
name|systemId
parameter_list|)
block|{
name|this
argument_list|(
name|text
argument_list|)
expr_stmt|;
if|if
condition|(
name|systemId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"systemId must be specified"
argument_list|)
throw|;
block|}
name|setSystemId
argument_list|(
name|systemId
argument_list|)
expr_stmt|;
block|}
DECL|method|StringSource (String text, String systemId, String encoding)
specifier|public
name|StringSource
parameter_list|(
name|String
name|text
parameter_list|,
name|String
name|systemId
parameter_list|,
name|String
name|encoding
parameter_list|)
block|{
name|this
argument_list|(
name|text
argument_list|,
name|systemId
argument_list|)
expr_stmt|;
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"encoding must be specified"
argument_list|)
throw|;
block|}
name|this
operator|.
name|encoding
operator|=
name|encoding
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getInputStream ()
specifier|public
name|InputStream
name|getInputStream
parameter_list|()
block|{
try|try
block|{
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|text
operator|.
name|getBytes
argument_list|(
name|encoding
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|getReader ()
specifier|public
name|Reader
name|getReader
parameter_list|()
block|{
return|return
operator|new
name|StringReader
argument_list|(
name|text
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"StringSource["
operator|+
name|text
operator|+
literal|"]"
return|;
block|}
DECL|method|getText ()
specifier|public
name|String
name|getText
parameter_list|()
block|{
return|return
name|text
return|;
block|}
DECL|method|getEncoding ()
specifier|public
name|String
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
annotation|@
name|Override
DECL|method|writeExternal (ObjectOutput out)
specifier|public
name|void
name|writeExternal
parameter_list|(
name|ObjectOutput
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|b
init|=
operator|(
name|text
operator|!=
literal|null
condition|?
literal|0x01
else|:
literal|0x00
operator|)
operator|+
operator|(
name|encoding
operator|!=
literal|null
condition|?
literal|0x02
else|:
literal|0x00
operator|)
operator|+
operator|(
name|getPublicId
argument_list|()
operator|!=
literal|null
condition|?
literal|0x04
else|:
literal|0x00
operator|)
operator|+
operator|(
name|getSystemId
argument_list|()
operator|!=
literal|null
condition|?
literal|0x08
else|:
literal|0x00
operator|)
decl_stmt|;
name|out
operator|.
name|writeByte
argument_list|(
name|b
argument_list|)
expr_stmt|;
if|if
condition|(
operator|(
name|b
operator|&
literal|0x01
operator|)
operator|!=
literal|0
condition|)
block|{
name|out
operator|.
name|writeUTF
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|b
operator|&
literal|0x02
operator|)
operator|!=
literal|0
condition|)
block|{
name|out
operator|.
name|writeUTF
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|b
operator|&
literal|0x04
operator|)
operator|!=
literal|0
condition|)
block|{
name|out
operator|.
name|writeUTF
argument_list|(
name|getPublicId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|b
operator|&
literal|0x08
operator|)
operator|!=
literal|0
condition|)
block|{
name|out
operator|.
name|writeUTF
argument_list|(
name|getSystemId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|readExternal (ObjectInput in)
specifier|public
name|void
name|readExternal
parameter_list|(
name|ObjectInput
name|in
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|int
name|b
init|=
name|in
operator|.
name|readByte
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|b
operator|&
literal|0x01
operator|)
operator|!=
literal|0
condition|)
block|{
name|text
operator|=
name|in
operator|.
name|readUTF
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|b
operator|&
literal|0x02
operator|)
operator|!=
literal|0
condition|)
block|{
name|encoding
operator|=
name|in
operator|.
name|readUTF
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|b
operator|&
literal|0x04
operator|)
operator|!=
literal|0
condition|)
block|{
name|setPublicId
argument_list|(
name|in
operator|.
name|readUTF
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|b
operator|&
literal|0x08
operator|)
operator|!=
literal|0
condition|)
block|{
name|setSystemId
argument_list|(
name|in
operator|.
name|readUTF
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

