begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.tika
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|tika
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
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
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
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|OutputKeys
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
name|TransformerConfigurationException
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
name|sax
operator|.
name|SAXTransformerFactory
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
name|sax
operator|.
name|TransformerHandler
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
name|StreamResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|ContentHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
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
name|impl
operator|.
name|DefaultProducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tika
operator|.
name|config
operator|.
name|TikaConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tika
operator|.
name|detect
operator|.
name|Detector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tika
operator|.
name|exception
operator|.
name|TikaException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tika
operator|.
name|metadata
operator|.
name|Metadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tika
operator|.
name|mime
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tika
operator|.
name|parser
operator|.
name|AutoDetectParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tika
operator|.
name|parser
operator|.
name|ParseContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tika
operator|.
name|parser
operator|.
name|Parser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tika
operator|.
name|parser
operator|.
name|html
operator|.
name|BoilerpipeContentHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tika
operator|.
name|sax
operator|.
name|BodyContentHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tika
operator|.
name|sax
operator|.
name|ExpandedTitleContentHandler
import|;
end_import

begin_class
DECL|class|TikaProducer
specifier|public
class|class
name|TikaProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|tikaConfiguration
specifier|private
specifier|final
name|TikaConfiguration
name|tikaConfiguration
decl_stmt|;
DECL|field|parser
specifier|private
specifier|final
name|Parser
name|parser
decl_stmt|;
DECL|field|detector
specifier|private
specifier|final
name|Detector
name|detector
decl_stmt|;
DECL|method|TikaProducer (TikaEndpoint endpoint)
specifier|public
name|TikaProducer
parameter_list|(
name|TikaEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|tikaConfiguration
operator|=
name|endpoint
operator|.
name|getTikaConfiguration
argument_list|()
expr_stmt|;
name|TikaConfig
name|config
init|=
name|this
operator|.
name|tikaConfiguration
operator|.
name|getTikaConfig
argument_list|()
decl_stmt|;
name|this
operator|.
name|parser
operator|=
operator|new
name|AutoDetectParser
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|this
operator|.
name|detector
operator|=
name|config
operator|.
name|getDetector
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|TikaOperation
name|operation
init|=
name|this
operator|.
name|tikaConfiguration
operator|.
name|getOperation
argument_list|()
decl_stmt|;
name|Object
name|result
decl_stmt|;
switch|switch
condition|(
name|operation
condition|)
block|{
case|case
name|detect
case|:
name|result
operator|=
name|doDetect
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|parse
case|:
name|result
operator|=
name|doParse
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Unknown operation %s"
argument_list|,
name|tikaConfiguration
operator|.
name|getOperation
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
comment|// propagate headers
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setAttachments
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getAttachments
argument_list|()
argument_list|)
expr_stmt|;
comment|// and set result
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|doDetect (Exchange exchange)
specifier|private
name|Object
name|doDetect
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|inputStream
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|Metadata
name|metadata
init|=
operator|new
name|Metadata
argument_list|()
decl_stmt|;
name|MediaType
name|result
init|=
name|this
operator|.
name|detector
operator|.
name|detect
argument_list|(
name|inputStream
argument_list|,
name|metadata
argument_list|)
decl_stmt|;
name|convertMetadataToHeaders
argument_list|(
name|metadata
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
return|return
name|result
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|doParse (Exchange exchange)
specifier|private
name|Object
name|doParse
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|TikaException
throws|,
name|IOException
throws|,
name|SAXException
throws|,
name|TransformerConfigurationException
block|{
name|InputStream
name|inputStream
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|OutputStream
name|result
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ContentHandler
name|contentHandler
init|=
name|getContentHandler
argument_list|(
name|this
operator|.
name|tikaConfiguration
argument_list|,
name|result
argument_list|)
decl_stmt|;
name|ParseContext
name|context
init|=
operator|new
name|ParseContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|set
argument_list|(
name|Parser
operator|.
name|class
argument_list|,
name|this
operator|.
name|parser
argument_list|)
expr_stmt|;
name|Metadata
name|metadata
init|=
operator|new
name|Metadata
argument_list|()
decl_stmt|;
name|this
operator|.
name|parser
operator|.
name|parse
argument_list|(
name|inputStream
argument_list|,
name|contentHandler
argument_list|,
name|metadata
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|convertMetadataToHeaders
argument_list|(
name|metadata
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|convertMetadataToHeaders (Metadata metadata, Exchange exchange)
specifier|private
name|void
name|convertMetadataToHeaders
parameter_list|(
name|Metadata
name|metadata
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|metadata
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|metaname
range|:
name|metadata
operator|.
name|names
argument_list|()
control|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"Tika"
operator|+
name|metaname
argument_list|,
name|metadata
operator|.
name|get
argument_list|(
name|metaname
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getContentHandler (TikaConfiguration configuration, OutputStream outputStream)
specifier|private
name|ContentHandler
name|getContentHandler
parameter_list|(
name|TikaConfiguration
name|configuration
parameter_list|,
name|OutputStream
name|outputStream
parameter_list|)
throws|throws
name|TransformerConfigurationException
throws|,
name|UnsupportedEncodingException
block|{
name|ContentHandler
name|result
init|=
literal|null
decl_stmt|;
name|TikaParseOutputFormat
name|outputFormat
init|=
name|configuration
operator|.
name|getTikaParseOutputFormat
argument_list|()
decl_stmt|;
name|String
name|encoding
init|=
name|Charset
operator|.
name|defaultCharset
argument_list|()
operator|.
name|name
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|outputFormat
condition|)
block|{
case|case
name|xml
case|:
name|result
operator|=
name|getTransformerHandler
argument_list|(
name|outputStream
argument_list|,
literal|"xml"
argument_list|,
name|encoding
argument_list|,
literal|true
argument_list|)
expr_stmt|;
break|break;
case|case
name|text
case|:
name|result
operator|=
operator|new
name|BodyContentHandler
argument_list|(
name|outputStream
argument_list|)
expr_stmt|;
break|break;
case|case
name|textMain
case|:
name|result
operator|=
operator|new
name|BoilerpipeContentHandler
argument_list|(
name|getOutputWriter
argument_list|(
name|outputStream
argument_list|,
name|encoding
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|html
case|:
name|result
operator|=
operator|new
name|ExpandedTitleContentHandler
argument_list|(
name|getTransformerHandler
argument_list|(
name|outputStream
argument_list|,
literal|"html"
argument_list|,
name|encoding
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Unknown format %s"
argument_list|,
name|tikaConfiguration
operator|.
name|getTikaParseOutputFormat
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
DECL|method|getTransformerHandler (OutputStream output, String method, String encoding, boolean prettyPrint)
specifier|private
name|TransformerHandler
name|getTransformerHandler
parameter_list|(
name|OutputStream
name|output
parameter_list|,
name|String
name|method
parameter_list|,
name|String
name|encoding
parameter_list|,
name|boolean
name|prettyPrint
parameter_list|)
throws|throws
name|TransformerConfigurationException
block|{
name|SAXTransformerFactory
name|factory
init|=
operator|(
name|SAXTransformerFactory
operator|)
name|SAXTransformerFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|TransformerHandler
name|handler
init|=
name|factory
operator|.
name|newTransformerHandler
argument_list|()
decl_stmt|;
name|handler
operator|.
name|getTransformer
argument_list|()
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|METHOD
argument_list|,
name|method
argument_list|)
expr_stmt|;
name|handler
operator|.
name|getTransformer
argument_list|()
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|INDENT
argument_list|,
name|prettyPrint
condition|?
literal|"yes"
else|:
literal|"no"
argument_list|)
expr_stmt|;
if|if
condition|(
name|encoding
operator|!=
literal|null
condition|)
block|{
name|handler
operator|.
name|getTransformer
argument_list|()
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|ENCODING
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
name|handler
operator|.
name|setResult
argument_list|(
operator|new
name|StreamResult
argument_list|(
name|output
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|handler
return|;
block|}
DECL|method|getOutputWriter (OutputStream output, String encoding)
specifier|private
name|Writer
name|getOutputWriter
parameter_list|(
name|OutputStream
name|output
parameter_list|,
name|String
name|encoding
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
if|if
condition|(
name|encoding
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|OutputStreamWriter
argument_list|(
name|output
argument_list|,
name|encoding
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ROOT
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"mac os x"
argument_list|)
condition|)
block|{
return|return
operator|new
name|OutputStreamWriter
argument_list|(
name|output
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|OutputStreamWriter
argument_list|(
name|output
argument_list|,
name|Charset
operator|.
name|defaultCharset
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

