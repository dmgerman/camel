begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|ErrorListener
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
name|TransformerException
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
name|ErrorHandler
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
name|xml
operator|.
name|sax
operator|.
name|SAXParseException
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
comment|/**  * {@link ErrorHandler} and {@link ErrorListener} which will ignore warnings,  * and throws error and fatal as exception, which ensures those can be caught by Camel and dealt-with.  *<p/>  * Also any warning, error or fatal error is stored on the {@link Exchange} as a property with the keys  *<tt>CamelXsltWarning</tt>,<tt>CamelXsltError</tt>, and<tt>CamelXsltFatalError</tt> which  * allows end users to access those information form the exchange.  */
end_comment

begin_class
DECL|class|DefaultTransformErrorHandler
specifier|public
class|class
name|DefaultTransformErrorHandler
implements|implements
name|ErrorHandler
implements|,
name|ErrorListener
block|{
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|method|DefaultTransformErrorHandler (Exchange exchange)
specifier|public
name|DefaultTransformErrorHandler
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
block|}
DECL|method|error (SAXParseException exception)
specifier|public
name|void
name|error
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
throws|throws
name|SAXException
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|XSLT_ERROR
argument_list|,
name|exception
argument_list|)
expr_stmt|;
throw|throw
name|exception
throw|;
block|}
DECL|method|fatalError (SAXParseException exception)
specifier|public
name|void
name|fatalError
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
throws|throws
name|SAXException
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|XSLT_FATAL_ERROR
argument_list|,
name|exception
argument_list|)
expr_stmt|;
throw|throw
name|exception
throw|;
block|}
DECL|method|warning (SAXParseException exception)
specifier|public
name|void
name|warning
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
throws|throws
name|SAXException
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|XSLT_WARNING
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
DECL|method|error (TransformerException exception)
specifier|public
name|void
name|error
parameter_list|(
name|TransformerException
name|exception
parameter_list|)
throws|throws
name|TransformerException
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|XSLT_ERROR
argument_list|,
name|exception
argument_list|)
expr_stmt|;
throw|throw
name|exception
throw|;
block|}
DECL|method|fatalError (TransformerException exception)
specifier|public
name|void
name|fatalError
parameter_list|(
name|TransformerException
name|exception
parameter_list|)
throws|throws
name|TransformerException
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|XSLT_FATAL_ERROR
argument_list|,
name|exception
argument_list|)
expr_stmt|;
throw|throw
name|exception
throw|;
block|}
DECL|method|warning (TransformerException exception)
specifier|public
name|void
name|warning
parameter_list|(
name|TransformerException
name|exception
parameter_list|)
throws|throws
name|TransformerException
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|XSLT_WARNING
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

