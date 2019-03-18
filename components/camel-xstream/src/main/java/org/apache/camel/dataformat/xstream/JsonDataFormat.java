begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.xstream
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|xstream
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
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|XStream
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|HierarchicalStreamReader
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|HierarchicalStreamWriter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|json
operator|.
name|JsonWriter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|xml
operator|.
name|QNameMap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|xml
operator|.
name|StaxReader
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|xml
operator|.
name|StaxWriter
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
name|spi
operator|.
name|ClassResolver
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
name|spi
operator|.
name|annotations
operator|.
name|Dataformat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jettison
operator|.
name|mapped
operator|.
name|MappedXMLInputFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jettison
operator|.
name|mapped
operator|.
name|MappedXMLOutputFactory
import|;
end_import

begin_comment
comment|/**  * A<a href="http://camel.apache.org/data-format.html">data format</a>  * ({@link org.apache.camel.spi.DataFormat}) using XStream and Jettison to marshal to and from JSON  */
end_comment

begin_class
annotation|@
name|Dataformat
argument_list|(
literal|"json-xstream"
argument_list|)
DECL|class|JsonDataFormat
specifier|public
class|class
name|JsonDataFormat
extends|extends
name|AbstractXStreamWrapper
block|{
DECL|field|mof
specifier|private
specifier|final
name|MappedXMLOutputFactory
name|mof
decl_stmt|;
DECL|field|mif
specifier|private
specifier|final
name|MappedXMLInputFactory
name|mif
decl_stmt|;
DECL|field|prettyPrint
specifier|private
name|boolean
name|prettyPrint
decl_stmt|;
DECL|method|JsonDataFormat ()
specifier|public
name|JsonDataFormat
parameter_list|()
block|{
specifier|final
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|nstjsons
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|mof
operator|=
operator|new
name|MappedXMLOutputFactory
argument_list|(
name|nstjsons
argument_list|)
expr_stmt|;
name|mif
operator|=
operator|new
name|MappedXMLInputFactory
argument_list|(
name|nstjsons
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDataFormatName ()
specifier|public
name|String
name|getDataFormatName
parameter_list|()
block|{
return|return
literal|"json-xstream"
return|;
block|}
DECL|method|isPrettyPrint ()
specifier|public
name|boolean
name|isPrettyPrint
parameter_list|()
block|{
return|return
name|prettyPrint
return|;
block|}
DECL|method|setPrettyPrint (boolean prettyPrint)
specifier|public
name|void
name|setPrettyPrint
parameter_list|(
name|boolean
name|prettyPrint
parameter_list|)
block|{
name|this
operator|.
name|prettyPrint
operator|=
name|prettyPrint
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|marshal (Exchange exchange, Object body, OutputStream stream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|body
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
name|super
operator|.
name|marshal
argument_list|(
name|exchange
argument_list|,
name|body
argument_list|,
name|stream
argument_list|)
expr_stmt|;
if|if
condition|(
name|isContentTypeHeader
argument_list|()
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|createXStream (ClassResolver resolver, ClassLoader classLoader)
specifier|protected
name|XStream
name|createXStream
parameter_list|(
name|ClassResolver
name|resolver
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|)
block|{
name|XStream
name|xs
init|=
name|super
operator|.
name|createXStream
argument_list|(
name|resolver
argument_list|,
name|classLoader
argument_list|)
decl_stmt|;
if|if
condition|(
name|getMode
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|xs
operator|.
name|setMode
argument_list|(
name|getModeFromString
argument_list|(
name|getMode
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|xs
operator|.
name|setMode
argument_list|(
name|XStream
operator|.
name|NO_REFERENCES
argument_list|)
expr_stmt|;
block|}
return|return
name|xs
return|;
block|}
DECL|method|createHierarchicalStreamWriter (Exchange exchange, Object body, OutputStream stream)
specifier|protected
name|HierarchicalStreamWriter
name|createHierarchicalStreamWriter
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|body
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|isPrettyPrint
argument_list|()
condition|)
block|{
try|try
block|{
comment|// the json spec. expects UTF-8 as the default encoding
return|return
operator|new
name|JsonWriter
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|stream
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|uee
parameter_list|)
block|{
throw|throw
operator|new
name|XMLStreamException
argument_list|(
name|uee
argument_list|)
throw|;
block|}
block|}
return|return
operator|new
name|StaxWriter
argument_list|(
operator|new
name|QNameMap
argument_list|()
argument_list|,
name|mof
operator|.
name|createXMLStreamWriter
argument_list|(
name|stream
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createHierarchicalStreamReader (Exchange exchange, InputStream stream)
specifier|protected
name|HierarchicalStreamReader
name|createHierarchicalStreamReader
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|stream
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
operator|new
name|StaxReader
argument_list|(
operator|new
name|QNameMap
argument_list|()
argument_list|,
name|mif
operator|.
name|createXMLStreamReader
argument_list|(
name|stream
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

