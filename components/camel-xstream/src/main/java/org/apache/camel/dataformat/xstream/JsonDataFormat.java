begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * A<a href="http://camel.apache.org/data-format.html">data format</a>  * ({@link DataFormat}) using XStream and Jettison to marshal to and from JSON  *  * @version   */
end_comment

begin_class
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
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
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
DECL|method|createXStream (ClassResolver resolver)
specifier|protected
name|XStream
name|createXStream
parameter_list|(
name|ClassResolver
name|resolver
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
argument_list|)
decl_stmt|;
name|xs
operator|.
name|setMode
argument_list|(
name|XStream
operator|.
name|NO_REFERENCES
argument_list|)
expr_stmt|;
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

