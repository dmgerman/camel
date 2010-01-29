begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.jaxp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|jaxp
package|;
end_package

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
name|Writer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLEventReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLEventWriter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLInputFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLOutputFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
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
name|Result
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
name|Source
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
name|converter
operator|.
name|IOConverter
import|;
end_import

begin_comment
comment|/**  * A converter of StAX objects  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|StaxConverter
specifier|public
class|class
name|StaxConverter
block|{
DECL|field|inputFactory
specifier|private
name|XMLInputFactory
name|inputFactory
decl_stmt|;
DECL|field|outputFactory
specifier|private
name|XMLOutputFactory
name|outputFactory
decl_stmt|;
annotation|@
name|Converter
DECL|method|createXMLEventWriter (OutputStream out, Exchange exchange)
specifier|public
name|XMLEventWriter
name|createXMLEventWriter
parameter_list|(
name|OutputStream
name|out
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|getOutputFactory
argument_list|()
operator|.
name|createXMLEventWriter
argument_list|(
name|out
argument_list|,
name|IOConverter
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Deprecated
DECL|method|createXMLEventWriter (OutputStream out)
specifier|public
name|XMLEventWriter
name|createXMLEventWriter
parameter_list|(
name|OutputStream
name|out
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|getOutputFactory
argument_list|()
operator|.
name|createXMLEventWriter
argument_list|(
name|out
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|createXMLEventWriter (Writer writer)
specifier|public
name|XMLEventWriter
name|createXMLEventWriter
parameter_list|(
name|Writer
name|writer
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|getOutputFactory
argument_list|()
operator|.
name|createXMLEventWriter
argument_list|(
name|writer
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|createXMLEventWriter (Result result)
specifier|public
name|XMLEventWriter
name|createXMLEventWriter
parameter_list|(
name|Result
name|result
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|getOutputFactory
argument_list|()
operator|.
name|createXMLEventWriter
argument_list|(
name|result
argument_list|)
return|;
block|}
annotation|@
name|Deprecated
DECL|method|createXMLStreamWriter (OutputStream outputStream)
specifier|public
name|XMLStreamWriter
name|createXMLStreamWriter
parameter_list|(
name|OutputStream
name|outputStream
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|getOutputFactory
argument_list|()
operator|.
name|createXMLStreamWriter
argument_list|(
name|outputStream
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|createXMLStreamWriter (OutputStream outputStream, Exchange exchange)
specifier|public
name|XMLStreamWriter
name|createXMLStreamWriter
parameter_list|(
name|OutputStream
name|outputStream
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|getOutputFactory
argument_list|()
operator|.
name|createXMLStreamWriter
argument_list|(
name|outputStream
argument_list|,
name|IOConverter
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|createXMLStreamWriter (Writer writer)
specifier|public
name|XMLStreamWriter
name|createXMLStreamWriter
parameter_list|(
name|Writer
name|writer
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|getOutputFactory
argument_list|()
operator|.
name|createXMLStreamWriter
argument_list|(
name|writer
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|createXMLStreamWriter (Result result)
specifier|public
name|XMLStreamWriter
name|createXMLStreamWriter
parameter_list|(
name|Result
name|result
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|getOutputFactory
argument_list|()
operator|.
name|createXMLStreamWriter
argument_list|(
name|result
argument_list|)
return|;
block|}
annotation|@
name|Deprecated
DECL|method|createXMLStreamReader (InputStream in)
specifier|public
name|XMLStreamReader
name|createXMLStreamReader
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|getInputFactory
argument_list|()
operator|.
name|createXMLStreamReader
argument_list|(
name|in
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|createXMLStreamReader (InputStream in, Exchange exchange)
specifier|public
name|XMLStreamReader
name|createXMLStreamReader
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|getInputFactory
argument_list|()
operator|.
name|createXMLStreamReader
argument_list|(
name|in
argument_list|,
name|IOConverter
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|createXMLStreamReader (Reader in)
specifier|public
name|XMLStreamReader
name|createXMLStreamReader
parameter_list|(
name|Reader
name|in
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|getInputFactory
argument_list|()
operator|.
name|createXMLStreamReader
argument_list|(
name|in
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|createXMLStreamReader (Source in)
specifier|public
name|XMLStreamReader
name|createXMLStreamReader
parameter_list|(
name|Source
name|in
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|getInputFactory
argument_list|()
operator|.
name|createXMLStreamReader
argument_list|(
name|in
argument_list|)
return|;
block|}
annotation|@
name|Deprecated
DECL|method|createXMLEventReader (InputStream in)
specifier|public
name|XMLEventReader
name|createXMLEventReader
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|getInputFactory
argument_list|()
operator|.
name|createXMLEventReader
argument_list|(
name|in
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|createXMLEventReader (InputStream in, Exchange exchange)
specifier|public
name|XMLEventReader
name|createXMLEventReader
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|getInputFactory
argument_list|()
operator|.
name|createXMLEventReader
argument_list|(
name|in
argument_list|,
name|IOConverter
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|createXMLEventReader (Reader in)
specifier|public
name|XMLEventReader
name|createXMLEventReader
parameter_list|(
name|Reader
name|in
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|getInputFactory
argument_list|()
operator|.
name|createXMLEventReader
argument_list|(
name|in
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|createXMLEventReader (XMLStreamReader in)
specifier|public
name|XMLEventReader
name|createXMLEventReader
parameter_list|(
name|XMLStreamReader
name|in
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|getInputFactory
argument_list|()
operator|.
name|createXMLEventReader
argument_list|(
name|in
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|createXMLEventReader (Source in)
specifier|public
name|XMLEventReader
name|createXMLEventReader
parameter_list|(
name|Source
name|in
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|getInputFactory
argument_list|()
operator|.
name|createXMLEventReader
argument_list|(
name|in
argument_list|)
return|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getInputFactory ()
specifier|public
name|XMLInputFactory
name|getInputFactory
parameter_list|()
block|{
if|if
condition|(
name|inputFactory
operator|==
literal|null
condition|)
block|{
name|inputFactory
operator|=
name|XMLInputFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
return|return
name|inputFactory
return|;
block|}
DECL|method|setInputFactory (XMLInputFactory inputFactory)
specifier|public
name|void
name|setInputFactory
parameter_list|(
name|XMLInputFactory
name|inputFactory
parameter_list|)
block|{
name|this
operator|.
name|inputFactory
operator|=
name|inputFactory
expr_stmt|;
block|}
DECL|method|getOutputFactory ()
specifier|public
name|XMLOutputFactory
name|getOutputFactory
parameter_list|()
block|{
if|if
condition|(
name|outputFactory
operator|==
literal|null
condition|)
block|{
name|outputFactory
operator|=
name|XMLOutputFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
return|return
name|outputFactory
return|;
block|}
DECL|method|setOutputFactory (XMLOutputFactory outputFactory)
specifier|public
name|void
name|setOutputFactory
parameter_list|(
name|XMLOutputFactory
name|outputFactory
parameter_list|)
block|{
name|this
operator|.
name|outputFactory
operator|=
name|outputFactory
expr_stmt|;
block|}
block|}
end_class

end_unit

