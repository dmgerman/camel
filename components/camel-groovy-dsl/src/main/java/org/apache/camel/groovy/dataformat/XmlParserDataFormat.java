begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.groovy.dataformat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|groovy
operator|.
name|dataformat
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
name|PrintWriter
import|;
end_import

begin_import
import|import
name|groovy
operator|.
name|util
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|groovy
operator|.
name|util
operator|.
name|XmlNodePrinter
import|;
end_import

begin_import
import|import
name|groovy
operator|.
name|util
operator|.
name|XmlParser
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

begin_comment
comment|/**  * DataFormat for using groovy.util.XmlParser as parser and renderer for XML  * data  */
end_comment

begin_class
DECL|class|XmlParserDataFormat
specifier|public
class|class
name|XmlParserDataFormat
extends|extends
name|AbstractXmlDataFormat
block|{
DECL|method|XmlParserDataFormat ()
specifier|public
name|XmlParserDataFormat
parameter_list|()
block|{
name|this
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|XmlParserDataFormat (boolean namespaceAware)
specifier|public
name|XmlParserDataFormat
parameter_list|(
name|boolean
name|namespaceAware
parameter_list|)
block|{
name|super
argument_list|(
name|namespaceAware
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|marshal (Exchange exchange, Object graph, OutputStream stream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|graph
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
name|newPrinter
argument_list|(
name|stream
argument_list|)
operator|.
name|print
argument_list|(
operator|(
name|Node
operator|)
name|graph
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
return|return
name|newParser
argument_list|()
operator|.
name|parse
argument_list|(
name|stream
argument_list|)
return|;
block|}
DECL|method|newParser ()
specifier|private
name|XmlParser
name|newParser
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlParser
name|xmlParser
init|=
operator|new
name|XmlParser
argument_list|(
name|newSaxParser
argument_list|()
argument_list|)
decl_stmt|;
name|xmlParser
operator|.
name|setErrorHandler
argument_list|(
name|getErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
name|xmlParser
operator|.
name|setTrimWhitespace
argument_list|(
operator|!
name|isKeepWhitespace
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|xmlParser
return|;
block|}
DECL|method|newPrinter (OutputStream stream)
specifier|private
name|XmlNodePrinter
name|newPrinter
parameter_list|(
name|OutputStream
name|stream
parameter_list|)
block|{
name|XmlNodePrinter
name|xmlNodePrinter
init|=
operator|new
name|XmlNodePrinter
argument_list|(
operator|new
name|PrintWriter
argument_list|(
name|stream
argument_list|)
argument_list|)
decl_stmt|;
name|xmlNodePrinter
operator|.
name|setNamespaceAware
argument_list|(
name|isNamespaceAware
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|xmlNodePrinter
return|;
block|}
block|}
end_class

end_unit

