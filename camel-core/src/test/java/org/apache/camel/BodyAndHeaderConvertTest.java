begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|URLDataSource
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
name|DefaultCamelContext
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
name|DefaultExchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|BodyAndHeaderConvertTest
specifier|public
class|class
name|BodyAndHeaderConvertTest
extends|extends
name|TestCase
block|{
DECL|field|exchange
specifier|protected
name|Exchange
name|exchange
decl_stmt|;
DECL|method|testConversionOfBody ()
specifier|public
name|void
name|testConversionOfBody
parameter_list|()
throws|throws
name|Exception
block|{
name|Document
name|document
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Document
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|Element
name|element
init|=
name|document
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Root element name"
argument_list|,
literal|"hello"
argument_list|,
name|element
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testConversionOfExchangeProperties ()
specifier|public
name|void
name|testConversionOfExchangeProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|text
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
literal|"foo"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo property"
argument_list|,
literal|"1234"
argument_list|,
name|text
argument_list|)
expr_stmt|;
comment|// TODO better conversion example when the property editor support is added
block|}
DECL|method|testConversionOfMessageHeaders ()
specifier|public
name|void
name|testConversionOfMessageHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|text
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"bar"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bar header"
argument_list|,
literal|"567"
argument_list|,
name|text
argument_list|)
expr_stmt|;
comment|// TODO better conversion example when the property editor support is added
block|}
DECL|method|testConversionOfMessageAttachments ()
specifier|public
name|void
name|testConversionOfMessageAttachments
parameter_list|()
throws|throws
name|Exception
block|{
name|DataHandler
name|handler
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getAttachment
argument_list|(
literal|"att"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"attachment got lost"
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|exchange
operator|=
operator|new
name|DefaultExchange
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
literal|"foo"
argument_list|,
literal|1234
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"bar"
argument_list|,
literal|567
argument_list|)
expr_stmt|;
name|message
operator|.
name|addAttachment
argument_list|(
literal|"att"
argument_list|,
operator|new
name|DataHandler
argument_list|(
operator|new
name|URLDataSource
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://activemq.apache.org/camel/message.html"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

