begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|StringReader
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
name|java
operator|.
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedAction
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|BlockingQueue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|LinkedBlockingQueue
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
name|XMLResolver
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
name|support
operator|.
name|ExchangeHelper
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A converter of StAX objects  */
end_comment

begin_class
annotation|@
name|Converter
argument_list|(
name|generateLoader
operator|=
literal|true
argument_list|)
DECL|class|StaxConverter
specifier|public
class|class
name|StaxConverter
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|StaxConverter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|INPUT_FACTORY_POOL
specifier|private
specifier|static
specifier|final
name|BlockingQueue
argument_list|<
name|XMLInputFactory
argument_list|>
name|INPUT_FACTORY_POOL
decl_stmt|;
DECL|field|OUTPUT_FACTORY_POOL
specifier|private
specifier|static
specifier|final
name|BlockingQueue
argument_list|<
name|XMLOutputFactory
argument_list|>
name|OUTPUT_FACTORY_POOL
decl_stmt|;
static|static
block|{
name|int
name|i
init|=
literal|20
decl_stmt|;
try|try
block|{
name|String
name|s
init|=
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|run
parameter_list|()
block|{
return|return
name|System
operator|.
name|getProperty
argument_list|(
literal|"org.apache.cxf.staxutils.pool-size"
argument_list|,
literal|"-1"
argument_list|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|i
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
name|i
operator|=
literal|20
expr_stmt|;
block|}
try|try
block|{
comment|// if we have more cores than 20, then use that
name|int
name|cores
init|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|availableProcessors
argument_list|()
decl_stmt|;
if|if
condition|(
name|cores
operator|>
name|i
condition|)
block|{
name|i
operator|=
name|cores
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// ignore
name|i
operator|=
literal|20
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|<=
literal|0
condition|)
block|{
name|i
operator|=
literal|20
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"StaxConverter pool size: {}"
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|INPUT_FACTORY_POOL
operator|=
operator|new
name|LinkedBlockingQueue
argument_list|<>
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|OUTPUT_FACTORY_POOL
operator|=
operator|new
name|LinkedBlockingQueue
argument_list|<>
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
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
name|XMLOutputFactory
name|factory
init|=
name|getOutputFactory
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|factory
operator|.
name|createXMLEventWriter
argument_list|(
name|IOHelper
operator|.
name|buffered
argument_list|(
name|out
argument_list|)
argument_list|,
name|ExchangeHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
return|;
block|}
finally|finally
block|{
name|returnXMLOutputFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
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
name|XMLOutputFactory
name|factory
init|=
name|getOutputFactory
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|factory
operator|.
name|createXMLEventWriter
argument_list|(
name|IOHelper
operator|.
name|buffered
argument_list|(
name|writer
argument_list|)
argument_list|)
return|;
block|}
finally|finally
block|{
name|returnXMLOutputFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
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
name|XMLOutputFactory
name|factory
init|=
name|getOutputFactory
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|factory
operator|.
name|createXMLEventWriter
argument_list|(
name|result
argument_list|)
return|;
block|}
finally|finally
block|{
name|returnXMLOutputFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
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
name|XMLOutputFactory
name|factory
init|=
name|getOutputFactory
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|factory
operator|.
name|createXMLStreamWriter
argument_list|(
name|IOHelper
operator|.
name|buffered
argument_list|(
name|outputStream
argument_list|)
argument_list|,
name|ExchangeHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
return|;
block|}
finally|finally
block|{
name|returnXMLOutputFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
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
name|XMLOutputFactory
name|factory
init|=
name|getOutputFactory
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|factory
operator|.
name|createXMLStreamWriter
argument_list|(
name|IOHelper
operator|.
name|buffered
argument_list|(
name|writer
argument_list|)
argument_list|)
return|;
block|}
finally|finally
block|{
name|returnXMLOutputFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
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
name|XMLOutputFactory
name|factory
init|=
name|getOutputFactory
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|factory
operator|.
name|createXMLStreamWriter
argument_list|(
name|result
argument_list|)
return|;
block|}
finally|finally
block|{
name|returnXMLOutputFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
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
name|XMLInputFactory
name|factory
init|=
name|getInputFactory
argument_list|()
decl_stmt|;
try|try
block|{
name|String
name|charsetName
init|=
name|ExchangeHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|charsetName
operator|==
literal|null
condition|)
block|{
return|return
name|factory
operator|.
name|createXMLStreamReader
argument_list|(
name|IOHelper
operator|.
name|buffered
argument_list|(
name|in
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|factory
operator|.
name|createXMLStreamReader
argument_list|(
name|IOHelper
operator|.
name|buffered
argument_list|(
name|in
argument_list|)
argument_list|,
name|charsetName
argument_list|)
return|;
block|}
block|}
finally|finally
block|{
name|returnXMLInputFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Converter
DECL|method|createXMLStreamReader (File file, Exchange exchange)
specifier|public
name|XMLStreamReader
name|createXMLStreamReader
parameter_list|(
name|File
name|file
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|XMLStreamException
throws|,
name|FileNotFoundException
block|{
name|XMLInputFactory
name|factory
init|=
name|getInputFactory
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|factory
operator|.
name|createXMLStreamReader
argument_list|(
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
argument_list|,
name|ExchangeHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
return|;
block|}
finally|finally
block|{
name|returnXMLInputFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Converter
DECL|method|createXMLStreamReader (Reader reader)
specifier|public
name|XMLStreamReader
name|createXMLStreamReader
parameter_list|(
name|Reader
name|reader
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|XMLInputFactory
name|factory
init|=
name|getInputFactory
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|factory
operator|.
name|createXMLStreamReader
argument_list|(
name|IOHelper
operator|.
name|buffered
argument_list|(
name|reader
argument_list|)
argument_list|)
return|;
block|}
finally|finally
block|{
name|returnXMLInputFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
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
name|XMLInputFactory
name|factory
init|=
name|getInputFactory
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|factory
operator|.
name|createXMLStreamReader
argument_list|(
name|in
argument_list|)
return|;
block|}
finally|finally
block|{
name|returnXMLInputFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Converter
DECL|method|createXMLStreamReader (String string)
specifier|public
name|XMLStreamReader
name|createXMLStreamReader
parameter_list|(
name|String
name|string
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|XMLInputFactory
name|factory
init|=
name|getInputFactory
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|factory
operator|.
name|createXMLStreamReader
argument_list|(
operator|new
name|StringReader
argument_list|(
name|string
argument_list|)
argument_list|)
return|;
block|}
finally|finally
block|{
name|returnXMLInputFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
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
name|XMLInputFactory
name|factory
init|=
name|getInputFactory
argument_list|()
decl_stmt|;
try|try
block|{
name|String
name|charsetName
init|=
name|ExchangeHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|charsetName
operator|==
literal|null
condition|)
block|{
return|return
name|factory
operator|.
name|createXMLEventReader
argument_list|(
name|IOHelper
operator|.
name|buffered
argument_list|(
name|in
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|factory
operator|.
name|createXMLEventReader
argument_list|(
name|IOHelper
operator|.
name|buffered
argument_list|(
name|in
argument_list|)
argument_list|,
name|charsetName
argument_list|)
return|;
block|}
block|}
finally|finally
block|{
name|returnXMLInputFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Converter
DECL|method|createXMLEventReader (File file, Exchange exchange)
specifier|public
name|XMLEventReader
name|createXMLEventReader
parameter_list|(
name|File
name|file
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|XMLStreamException
throws|,
name|FileNotFoundException
block|{
name|XMLInputFactory
name|factory
init|=
name|getInputFactory
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|factory
operator|.
name|createXMLEventReader
argument_list|(
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
argument_list|,
name|ExchangeHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
return|;
block|}
finally|finally
block|{
name|returnXMLInputFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Converter
DECL|method|createXMLEventReader (Reader reader)
specifier|public
name|XMLEventReader
name|createXMLEventReader
parameter_list|(
name|Reader
name|reader
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|XMLInputFactory
name|factory
init|=
name|getInputFactory
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|factory
operator|.
name|createXMLEventReader
argument_list|(
name|IOHelper
operator|.
name|buffered
argument_list|(
name|reader
argument_list|)
argument_list|)
return|;
block|}
finally|finally
block|{
name|returnXMLInputFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Converter
DECL|method|createXMLEventReader (XMLStreamReader reader)
specifier|public
name|XMLEventReader
name|createXMLEventReader
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|XMLInputFactory
name|factory
init|=
name|getInputFactory
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|factory
operator|.
name|createXMLEventReader
argument_list|(
name|reader
argument_list|)
return|;
block|}
finally|finally
block|{
name|returnXMLInputFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
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
name|XMLInputFactory
name|factory
init|=
name|getInputFactory
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|factory
operator|.
name|createXMLEventReader
argument_list|(
name|in
argument_list|)
return|;
block|}
finally|finally
block|{
name|returnXMLInputFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Converter
DECL|method|createInputStream (XMLStreamReader reader, Exchange exchange)
specifier|public
name|InputStream
name|createInputStream
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|XMLOutputFactory
name|factory
init|=
name|getOutputFactory
argument_list|()
decl_stmt|;
try|try
block|{
name|String
name|charsetName
init|=
name|ExchangeHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
decl_stmt|;
return|return
operator|new
name|XMLStreamReaderInputStream
argument_list|(
name|reader
argument_list|,
name|charsetName
argument_list|,
name|factory
argument_list|)
return|;
block|}
finally|finally
block|{
name|returnXMLOutputFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Converter
DECL|method|createReader (XMLStreamReader reader, Exchange exchange)
specifier|public
name|Reader
name|createReader
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|XMLOutputFactory
name|factory
init|=
name|getOutputFactory
argument_list|()
decl_stmt|;
try|try
block|{
return|return
operator|new
name|XMLStreamReaderReader
argument_list|(
name|reader
argument_list|,
name|factory
argument_list|)
return|;
block|}
finally|finally
block|{
name|returnXMLOutputFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isWoodstox (Object factory)
specifier|private
specifier|static
name|boolean
name|isWoodstox
parameter_list|(
name|Object
name|factory
parameter_list|)
block|{
return|return
name|factory
operator|.
name|getClass
argument_list|()
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"com.ctc.wstx"
argument_list|)
return|;
block|}
DECL|method|getXMLInputFactory ()
specifier|private
name|XMLInputFactory
name|getXMLInputFactory
parameter_list|()
block|{
name|XMLInputFactory
name|f
init|=
name|INPUT_FACTORY_POOL
operator|.
name|poll
argument_list|()
decl_stmt|;
if|if
condition|(
name|f
operator|==
literal|null
condition|)
block|{
name|f
operator|=
name|createXMLInputFactory
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|f
return|;
block|}
DECL|method|returnXMLInputFactory (XMLInputFactory factory)
specifier|private
name|void
name|returnXMLInputFactory
parameter_list|(
name|XMLInputFactory
name|factory
parameter_list|)
block|{
if|if
condition|(
name|factory
operator|!=
name|inputFactory
condition|)
block|{
name|INPUT_FACTORY_POOL
operator|.
name|offer
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getXMLOutputFactory ()
specifier|private
name|XMLOutputFactory
name|getXMLOutputFactory
parameter_list|()
block|{
name|XMLOutputFactory
name|f
init|=
name|OUTPUT_FACTORY_POOL
operator|.
name|poll
argument_list|()
decl_stmt|;
if|if
condition|(
name|f
operator|==
literal|null
condition|)
block|{
name|f
operator|=
name|XMLOutputFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
return|return
name|f
return|;
block|}
DECL|method|returnXMLOutputFactory (XMLOutputFactory factory)
specifier|private
name|void
name|returnXMLOutputFactory
parameter_list|(
name|XMLOutputFactory
name|factory
parameter_list|)
block|{
if|if
condition|(
name|factory
operator|!=
name|outputFactory
condition|)
block|{
name|OUTPUT_FACTORY_POOL
operator|.
name|offer
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createXMLInputFactory (boolean nsAware)
specifier|public
specifier|static
name|XMLInputFactory
name|createXMLInputFactory
parameter_list|(
name|boolean
name|nsAware
parameter_list|)
block|{
name|XMLInputFactory
name|factory
init|=
name|XMLInputFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|setProperty
argument_list|(
name|factory
argument_list|,
name|XMLInputFactory
operator|.
name|IS_NAMESPACE_AWARE
argument_list|,
name|nsAware
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|factory
argument_list|,
name|XMLInputFactory
operator|.
name|SUPPORT_DTD
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|factory
argument_list|,
name|XMLInputFactory
operator|.
name|IS_REPLACING_ENTITY_REFERENCES
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|factory
argument_list|,
name|XMLInputFactory
operator|.
name|IS_SUPPORTING_EXTERNAL_ENTITIES
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setXMLResolver
argument_list|(
operator|new
name|XMLResolver
argument_list|()
block|{
specifier|public
name|Object
name|resolveEntity
parameter_list|(
name|String
name|publicID
parameter_list|,
name|String
name|systemID
parameter_list|,
name|String
name|baseURI
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|XMLStreamException
block|{
throw|throw
operator|new
name|XMLStreamException
argument_list|(
literal|"Reading external entities is disabled"
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|isWoodstox
argument_list|(
name|factory
argument_list|)
condition|)
block|{
comment|// just log a debug as we are good then
name|LOG
operator|.
name|debug
argument_list|(
literal|"Created Woodstox XMLInputFactory: {}"
argument_list|,
name|factory
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// log a hint that woodstock may be a better factory to use
name|LOG
operator|.
name|info
argument_list|(
literal|"Created XMLInputFactory: {}. DOMSource/DOMResult may have issues with {}. We suggest using Woodstox."
argument_list|,
name|factory
argument_list|,
name|factory
argument_list|)
expr_stmt|;
block|}
return|return
name|factory
return|;
block|}
DECL|method|setProperty (XMLInputFactory f, String p, Object o)
specifier|private
specifier|static
name|void
name|setProperty
parameter_list|(
name|XMLInputFactory
name|f
parameter_list|,
name|String
name|p
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
try|try
block|{
name|f
operator|.
name|setProperty
argument_list|(
name|p
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
block|}
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
return|return
name|getXMLInputFactory
argument_list|()
return|;
block|}
return|return
name|inputFactory
return|;
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
return|return
name|getXMLOutputFactory
argument_list|()
return|;
block|}
return|return
name|outputFactory
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

