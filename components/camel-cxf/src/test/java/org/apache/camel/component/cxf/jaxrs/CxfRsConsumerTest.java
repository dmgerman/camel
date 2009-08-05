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
name|java
operator|.
name|net
operator|.
name|URL
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
name|builder
operator|.
name|RouteBuilder
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
name|jaxrs
operator|.
name|testbean
operator|.
name|Customer
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
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
DECL|class|CxfRsConsumerTest
specifier|public
class|class
name|CxfRsConsumerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|CXF_RS_ENDPOINT_URI
specifier|private
specifier|static
specifier|final
name|String
name|CXF_RS_ENDPOINT_URI
init|=
literal|"cxfrs://http://localhost:9000?resourceClasses=org.apache.camel.component.cxf.jaxrs.testbean.CustomerService"
decl_stmt|;
comment|// START SNIPPET: example
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
name|CXF_RS_ENDPOINT_URI
argument_list|)
operator|.
name|process
argument_list|(
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
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
comment|// Get the operation name from in message
name|String
name|operationName
init|=
name|inMessage
operator|.
name|getHeader
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// The parameter of the invocation is stored in the body of in message
name|String
name|id
init|=
operator|(
name|String
operator|)
name|inMessage
operator|.
name|getBody
argument_list|(
name|Object
index|[]
operator|.
expr|class
argument_list|)
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
literal|"getCustomer"
operator|.
name|equals
argument_list|(
name|operationName
argument_list|)
condition|)
block|{
name|String
name|httpMethod
init|=
name|inMessage
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong http method"
argument_list|,
literal|"GET"
argument_list|,
name|httpMethod
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|inMessage
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_URI
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong http uri"
argument_list|,
literal|"/customerservice/customers/126"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|Customer
name|customer
init|=
operator|new
name|Customer
argument_list|()
decl_stmt|;
name|customer
operator|.
name|setId
argument_list|(
name|Long
operator|.
name|parseLong
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|customer
operator|.
name|setName
argument_list|(
literal|"Willem"
argument_list|)
expr_stmt|;
comment|// We just put the response Object into the out message body
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|customer
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|// END SNIPPET: example
annotation|@
name|Test
DECL|method|testGetCustomer ()
specifier|public
name|void
name|testGetCustomer
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:9000/customerservice/customers/126"
argument_list|)
decl_stmt|;
name|InputStream
name|in
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"{\"Customer\":{\"id\":126,\"name\":\"Willem\"}}"
argument_list|,
name|CxfUtils
operator|.
name|getStringFromInputStream
argument_list|(
name|in
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

