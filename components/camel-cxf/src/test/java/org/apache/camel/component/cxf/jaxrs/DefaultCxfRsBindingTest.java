begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|List
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
name|Message
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
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|DefaultHeaderFilterStrategy
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
name|DefaultMessage
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
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|MessageImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|DefaultCxfRsBindingTest
specifier|public
class|class
name|DefaultCxfRsBindingTest
extends|extends
name|Assert
block|{
DECL|field|context
specifier|private
name|DefaultCamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testSetCharsetWithContentType ()
specifier|public
name|void
name|testSetCharsetWithContentType
parameter_list|()
block|{
name|DefaultCxfRsBinding
name|cxfRsBinding
init|=
operator|new
name|DefaultCxfRsBinding
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
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
literal|"text/xml;charset=ISO-8859-1"
argument_list|)
expr_stmt|;
name|cxfRsBinding
operator|.
name|setCharsetWithContentType
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|String
name|charset
init|=
name|IOHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong charset"
argument_list|,
literal|"ISO-8859-1"
argument_list|,
name|charset
argument_list|)
expr_stmt|;
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
literal|"text/xml"
argument_list|)
expr_stmt|;
name|cxfRsBinding
operator|.
name|setCharsetWithContentType
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|charset
operator|=
name|IOHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a worng charset name"
argument_list|,
literal|"UTF-8"
argument_list|,
name|charset
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCopyProtocolHeader ()
specifier|public
name|void
name|testCopyProtocolHeader
parameter_list|()
block|{
name|DefaultCxfRsBinding
name|cxfRsBinding
init|=
operator|new
name|DefaultCxfRsBinding
argument_list|()
decl_stmt|;
name|cxfRsBinding
operator|.
name|setHeaderFilterStrategy
argument_list|(
operator|new
name|DefaultHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Message
name|camelMessage
init|=
operator|new
name|DefaultMessage
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
name|cxfMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"emptyList"
argument_list|,
name|Collections
operator|.
name|EMPTY_LIST
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"zeroSizeList"
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|cxfMessage
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|cxfRsBinding
operator|.
name|copyProtocolHeader
argument_list|(
name|cxfMessage
argument_list|,
name|camelMessage
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"We should get nothing here"
argument_list|,
name|camelMessage
operator|.
name|getHeader
argument_list|(
literal|"emptyList"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"We should get nothing here"
argument_list|,
name|camelMessage
operator|.
name|getHeader
argument_list|(
literal|"zeroSizeList"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

