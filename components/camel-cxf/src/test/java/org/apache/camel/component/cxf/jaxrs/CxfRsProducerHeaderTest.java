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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
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
name|CamelContext
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
name|Processor
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
name|component
operator|.
name|cxf
operator|.
name|CxfConstants
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
name|component
operator|.
name|cxf
operator|.
name|util
operator|.
name|CxfUtils
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_comment
comment|/**  * Unit test that verify header propagation functionality for CxfRsProducer  * that uses WebClient API.  *    * @version   */
end_comment

begin_class
annotation|@
name|ContextConfiguration
DECL|class|CxfRsProducerHeaderTest
specifier|public
class|class
name|CxfRsProducerHeaderTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
DECL|field|RESPONSE
specifier|private
specifier|static
specifier|final
name|Object
name|RESPONSE
init|=
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
operator|+
literal|"<Customer><id>123</id><name>John</name></Customer>"
decl_stmt|;
annotation|@
name|Autowired
DECL|field|camelContext
specifier|protected
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|Test
DECL|method|testInvokeThatDoesNotInvolveHeaders ()
specifier|public
name|void
name|testInvokeThatDoesNotInvolveHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|send
argument_list|(
literal|"direct://http"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
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
name|exchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|inMessage
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_USING_HTTP_API
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"GET"
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|,
literal|"/customerservice/customers/123"
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
comment|// verify the out message is a Response object by default
name|Response
name|response
init|=
operator|(
name|Response
operator|)
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The response should not be null "
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
comment|// test converter (from Response to InputStream)
name|assertEquals
argument_list|(
name|RESPONSE
argument_list|,
name|CxfUtils
operator|.
name|getStringFromInputStream
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHeaderFilteringAndPropagation ()
specifier|public
name|void
name|testHeaderFilteringAndPropagation
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|send
argument_list|(
literal|"direct://http2"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
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
name|exchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|inMessage
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_USING_HTTP_API
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"GET"
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|,
literal|"/customerservice/customers/123"
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setHeader
argument_list|(
literal|"my-user-defined-header"
argument_list|,
literal|"my-value"
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
comment|// get the response message
name|Response
name|response
init|=
operator|(
name|Response
operator|)
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
comment|// check the response code on the Response object as set by the "HttpProcess"
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
comment|// get out message
name|Message
name|outMessage
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
comment|// verify the content-type header sent by the "HttpProcess"
name|assertEquals
argument_list|(
literal|"text/xml"
argument_list|,
name|outMessage
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|)
argument_list|)
expr_stmt|;
comment|// check the user defined header echoed by the "HttpProcess"
name|assertEquals
argument_list|(
literal|"my-value"
argument_list|,
name|outMessage
operator|.
name|getHeader
argument_list|(
literal|"echo-my-user-defined-header"
argument_list|)
argument_list|)
expr_stmt|;
comment|// check the Accept header echoed by the "HttpProcess"
name|assertEquals
argument_list|(
literal|"application/json"
argument_list|,
name|outMessage
operator|.
name|getHeader
argument_list|(
literal|"echo-accept"
argument_list|)
argument_list|)
expr_stmt|;
comment|// make sure the HttpProcess have not seen CxfConstants.CAMEL_CXF_RS_USING_HTTP_API
name|assertNull
argument_list|(
name|outMessage
operator|.
name|getHeader
argument_list|(
literal|"failed-header-using-http-api"
argument_list|)
argument_list|)
expr_stmt|;
comment|// make sure response code has been set in out header
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|200
argument_list|)
argument_list|,
name|outMessage
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|class|HttpProcessor
specifier|public
specifier|static
class|class
name|HttpProcessor
implements|implements
name|Processor
block|{
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
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
if|if
condition|(
name|in
operator|.
name|getHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_USING_HTTP_API
argument_list|)
operator|!=
literal|null
condition|)
block|{
comment|// this should have been filtered
name|out
operator|.
name|setHeader
argument_list|(
literal|"failed-header-using-http-api"
argument_list|,
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_USING_HTTP_API
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|setHeader
argument_list|(
literal|"echo-accept"
argument_list|,
name|in
operator|.
name|getHeader
argument_list|(
literal|"Accept"
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|setHeader
argument_list|(
literal|"echo-my-user-defined-header"
argument_list|,
name|in
operator|.
name|getHeader
argument_list|(
literal|"my-user-defined-header"
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
literal|200
argument_list|)
expr_stmt|;
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
literal|"text/xml"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

