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
name|converter
operator|.
name|jaxp
operator|.
name|StaxConverter
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
name|DataFormat
import|;
end_import

begin_comment
comment|/**  * A<a href="http://activemq.apache.org/camel/data-format.html">data format</a>  * ({@link DataFormat}) using XmlBeans to marshal to and from XML  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|XStreamDataFormat
specifier|public
class|class
name|XStreamDataFormat
implements|implements
name|DataFormat
block|{
DECL|field|xstream
specifier|private
name|XStream
name|xstream
decl_stmt|;
DECL|field|staxConverter
specifier|private
name|StaxConverter
name|staxConverter
decl_stmt|;
comment|/**      * A factory method which takes a collection of types to be annotated      */
DECL|method|processAnnotations (Iterable<Class<?>> types)
specifier|public
specifier|static
name|XStreamDataFormat
name|processAnnotations
parameter_list|(
name|Iterable
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|types
parameter_list|)
block|{
name|XStreamDataFormat
name|answer
init|=
operator|new
name|XStreamDataFormat
argument_list|()
decl_stmt|;
name|XStream
name|xstream
init|=
name|answer
operator|.
name|getXStream
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
range|:
name|types
control|)
block|{
name|xstream
operator|.
name|processAnnotations
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * A factory method which takes a number of types to be annotated      */
DECL|method|processAnnotations (Class<?>.... types)
specifier|public
specifier|static
name|XStreamDataFormat
name|processAnnotations
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|types
parameter_list|)
block|{
name|XStreamDataFormat
name|answer
init|=
operator|new
name|XStreamDataFormat
argument_list|()
decl_stmt|;
name|XStream
name|xstream
init|=
name|answer
operator|.
name|getXStream
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
range|:
name|types
control|)
block|{
name|xstream
operator|.
name|processAnnotations
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|XStreamDataFormat ()
specifier|public
name|XStreamDataFormat
parameter_list|()
block|{     }
DECL|method|XStreamDataFormat (XStream xstream)
specifier|public
name|XStreamDataFormat
parameter_list|(
name|XStream
name|xstream
parameter_list|)
block|{
name|setXStream
argument_list|(
name|xstream
argument_list|)
expr_stmt|;
block|}
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
name|HierarchicalStreamWriter
name|writer
init|=
name|createHierarchicalStreamWriter
argument_list|(
name|exchange
argument_list|,
name|body
argument_list|,
name|stream
argument_list|)
decl_stmt|;
try|try
block|{
name|getXStream
argument_list|()
operator|.
name|marshal
argument_list|(
name|body
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|unmarshal (Exchange exchange, InputStream stream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
name|HierarchicalStreamReader
name|reader
init|=
name|createHierarchicalStreamReader
argument_list|(
name|exchange
argument_list|,
name|stream
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|getXStream
argument_list|()
operator|.
name|unmarshal
argument_list|(
name|reader
argument_list|)
return|;
block|}
finally|finally
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getXStream ()
specifier|public
name|XStream
name|getXStream
parameter_list|()
block|{
if|if
condition|(
name|xstream
operator|==
literal|null
condition|)
block|{
name|xstream
operator|=
name|createXStream
argument_list|()
expr_stmt|;
block|}
return|return
name|xstream
return|;
block|}
DECL|method|setXStream (XStream xstream)
specifier|public
name|void
name|setXStream
parameter_list|(
name|XStream
name|xstream
parameter_list|)
block|{
name|this
operator|.
name|xstream
operator|=
name|xstream
expr_stmt|;
block|}
DECL|method|getStaxConverter ()
specifier|public
name|StaxConverter
name|getStaxConverter
parameter_list|()
block|{
if|if
condition|(
name|staxConverter
operator|==
literal|null
condition|)
block|{
name|staxConverter
operator|=
operator|new
name|StaxConverter
argument_list|()
expr_stmt|;
block|}
return|return
name|staxConverter
return|;
block|}
DECL|method|setStaxConverter (StaxConverter staxConverter)
specifier|public
name|void
name|setStaxConverter
parameter_list|(
name|StaxConverter
name|staxConverter
parameter_list|)
block|{
name|this
operator|.
name|staxConverter
operator|=
name|staxConverter
expr_stmt|;
block|}
DECL|method|createXStream ()
specifier|protected
name|XStream
name|createXStream
parameter_list|()
block|{
return|return
operator|new
name|XStream
argument_list|()
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
name|XMLStreamWriter
name|xmlWriter
init|=
name|getStaxConverter
argument_list|()
operator|.
name|createXMLStreamWriter
argument_list|(
name|stream
argument_list|)
decl_stmt|;
return|return
operator|new
name|StaxWriter
argument_list|(
operator|new
name|QNameMap
argument_list|()
argument_list|,
name|xmlWriter
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
name|XMLStreamReader
name|xmlReader
init|=
name|getStaxConverter
argument_list|()
operator|.
name|createXMLStreamReader
argument_list|(
name|stream
argument_list|)
decl_stmt|;
return|return
operator|new
name|StaxReader
argument_list|(
operator|new
name|QNameMap
argument_list|()
argument_list|,
name|xmlReader
argument_list|)
return|;
block|}
block|}
end_class

end_unit

