begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.printer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|printer
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
name|javax
operator|.
name|print
operator|.
name|Doc
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|DocFlavor
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|attribute
operator|.
name|DocAttributeSet
import|;
end_import

begin_class
DECL|class|PrintDocument
class|class
name|PrintDocument
implements|implements
name|Doc
block|{
DECL|field|docFlavor
specifier|private
name|DocFlavor
name|docFlavor
decl_stmt|;
DECL|field|stream
specifier|private
name|InputStream
name|stream
decl_stmt|;
DECL|field|reader
specifier|private
name|Reader
name|reader
decl_stmt|;
DECL|field|buffer
specifier|private
name|byte
index|[]
name|buffer
decl_stmt|;
DECL|method|PrintDocument (InputStream stream, DocFlavor docFlavor)
name|PrintDocument
parameter_list|(
name|InputStream
name|stream
parameter_list|,
name|DocFlavor
name|docFlavor
parameter_list|)
block|{
name|this
operator|.
name|stream
operator|=
name|stream
expr_stmt|;
name|this
operator|.
name|docFlavor
operator|=
name|docFlavor
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDocFlavor ()
specifier|public
name|DocFlavor
name|getDocFlavor
parameter_list|()
block|{
return|return
name|docFlavor
return|;
block|}
annotation|@
name|Override
DECL|method|getAttributes ()
specifier|public
name|DocAttributeSet
name|getAttributes
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getPrintData ()
specifier|public
name|Object
name|getPrintData
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|getStreamForBytes
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getReaderForText ()
specifier|public
name|Reader
name|getReaderForText
parameter_list|()
throws|throws
name|IOException
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|reader
operator|!=
literal|null
condition|)
block|{
return|return
name|reader
return|;
block|}
if|if
condition|(
name|docFlavor
operator|.
name|getMediaType
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"image"
argument_list|)
condition|)
block|{
name|reader
operator|=
literal|null
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|(
name|docFlavor
operator|.
name|getMediaType
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"text"
argument_list|)
operator|)
operator|||
operator|(
operator|(
name|docFlavor
operator|.
name|getMediaType
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"application"
argument_list|)
operator|)
operator|&&
operator|(
name|docFlavor
operator|.
name|getMediaSubtype
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"xml"
argument_list|)
operator|)
operator|)
condition|)
block|{
name|buffer
operator|=
operator|new
name|byte
index|[
name|stream
operator|.
name|available
argument_list|()
index|]
expr_stmt|;
name|int
name|n
init|=
name|stream
operator|.
name|available
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|buffer
index|[
name|i
index|]
operator|=
operator|(
name|byte
operator|)
name|stream
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
name|reader
operator|=
operator|new
name|StringReader
argument_list|(
operator|new
name|String
argument_list|(
name|buffer
argument_list|)
argument_list|)
expr_stmt|;
name|stream
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
return|return
name|reader
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getStreamForBytes ()
specifier|public
name|InputStream
name|getStreamForBytes
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|stream
return|;
block|}
block|}
end_class

end_unit

