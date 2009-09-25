begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
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
name|ContextTestSupport
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
name|ExchangePattern
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
name|NoSuchBeanException
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
name|NoSuchHeaderException
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
name|NoSuchPropertyException
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|ExchangeHelperTest
specifier|public
class|class
name|ExchangeHelperTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|exchange
specifier|protected
name|Exchange
name|exchange
decl_stmt|;
DECL|method|testValidProperty ()
specifier|public
name|void
name|testValidProperty
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|value
init|=
name|ExchangeHelper
operator|.
name|getMandatoryProperty
argument_list|(
name|exchange
argument_list|,
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
literal|"123"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|testMissingProperty ()
specifier|public
name|void
name|testMissingProperty
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|String
name|value
init|=
name|ExchangeHelper
operator|.
name|getMandatoryProperty
argument_list|(
name|exchange
argument_list|,
literal|"bar"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|fail
argument_list|(
literal|"Should have failed but got: "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchPropertyException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|e
operator|.
name|getPropertyName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testPropertyOfIncompatibleType ()
specifier|public
name|void
name|testPropertyOfIncompatibleType
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|List
name|value
init|=
name|ExchangeHelper
operator|.
name|getMandatoryProperty
argument_list|(
name|exchange
argument_list|,
literal|"foo"
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|fail
argument_list|(
literal|"Should have failed but got: "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchPropertyException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|e
operator|.
name|getPropertyName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testMissingHeader ()
specifier|public
name|void
name|testMissingHeader
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|String
name|value
init|=
name|ExchangeHelper
operator|.
name|getMandatoryHeader
argument_list|(
name|exchange
argument_list|,
literal|"unknown"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|fail
argument_list|(
literal|"Should have failed but got: "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchHeaderException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"unknown"
argument_list|,
name|e
operator|.
name|getHeaderName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testHeaderOfIncompatibleType ()
specifier|public
name|void
name|testHeaderOfIncompatibleType
parameter_list|()
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
try|try
block|{
name|List
name|value
init|=
name|ExchangeHelper
operator|.
name|getMandatoryHeader
argument_list|(
name|exchange
argument_list|,
literal|"foo"
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|fail
argument_list|(
literal|"Should have failed but got: "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchHeaderException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|e
operator|.
name|getHeaderName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testNoSuchBean ()
specifier|public
name|void
name|testNoSuchBean
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|ExchangeHelper
operator|.
name|lookupMandatoryBean
argument_list|(
name|exchange
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchBeanException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"No bean could be found in the registry for: foo"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|e
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testNoSuchBeanType ()
specifier|public
name|void
name|testNoSuchBeanType
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|ExchangeHelper
operator|.
name|lookupMandatoryBean
argument_list|(
name|exchange
argument_list|,
literal|"foo"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchBeanException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"No bean could be found in the registry for: foo"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|e
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testGetExchangeById ()
specifier|public
name|void
name|testGetExchangeById
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Exchange
argument_list|>
argument_list|()
decl_stmt|;
name|Exchange
name|e1
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|Exchange
name|e2
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|e1
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|e2
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|ExchangeHelper
operator|.
name|getExchangeById
argument_list|(
name|list
argument_list|,
literal|"unknown"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|e1
argument_list|,
name|ExchangeHelper
operator|.
name|getExchangeById
argument_list|(
name|list
argument_list|,
name|e1
operator|.
name|getExchangeId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|e2
argument_list|,
name|ExchangeHelper
operator|.
name|getExchangeById
argument_list|(
name|list
argument_list|,
name|e2
operator|.
name|getExchangeId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testPopulateVariableMap ()
specifier|public
name|void
name|testPopulateVariableMap
parameter_list|()
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"quote"
argument_list|,
literal|"Camel rocks"
argument_list|)
expr_stmt|;
name|Map
name|map
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|ExchangeHelper
operator|.
name|populateVariableMap
argument_list|(
name|exchange
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"exchange"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"in"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"request"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"out"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"response"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"headers"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"body"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"camelContext"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateVariableMap ()
specifier|public
name|void
name|testCreateVariableMap
parameter_list|()
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"quote"
argument_list|,
literal|"Camel rocks"
argument_list|)
expr_stmt|;
name|Map
name|map
init|=
name|ExchangeHelper
operator|.
name|createVariableMap
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"exchange"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"in"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"request"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"out"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"response"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"headers"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"body"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"camelContext"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetContentType ()
specifier|public
name|void
name|testGetContentType
parameter_list|()
throws|throws
name|Exception
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
literal|"text/xml"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/xml"
argument_list|,
name|ExchangeHelper
operator|.
name|getContentType
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetContentEncpding ()
specifier|public
name|void
name|testGetContentEncpding
parameter_list|()
throws|throws
name|Exception
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
name|CONTENT_ENCODING
argument_list|,
literal|"iso-8859-1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"iso-8859-1"
argument_list|,
name|ExchangeHelper
operator|.
name|getContentEncoding
argument_list|(
name|exchange
argument_list|)
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
literal|123
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

