begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.soap
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|soap
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
name|junit
operator|.
name|framework
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|com
operator|.
name|example
operator|.
name|customerservice
operator|.
name|Customer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|example
operator|.
name|customerservice
operator|.
name|CustomerService
import|;
end_import

begin_import
import|import
name|com
operator|.
name|example
operator|.
name|customerservice
operator|.
name|GetCustomersByName
import|;
end_import

begin_import
import|import
name|com
operator|.
name|example
operator|.
name|customerservice
operator|.
name|GetCustomersByNameResponse
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
name|Produce
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
name|dataformat
operator|.
name|soap
operator|.
name|name
operator|.
name|ElementNameStrategy
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
name|dataformat
operator|.
name|soap
operator|.
name|name
operator|.
name|ServiceInterfaceStrategy
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

begin_comment
comment|/**  * Test that uses a dynamic proxy for CustomerService to send a request as SOAP  * and work with a static return SOAP message.  */
end_comment

begin_class
DECL|class|SoapClientTest
specifier|public
class|class
name|SoapClientTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start"
argument_list|)
DECL|field|customerService
name|CustomerService
name|customerService
decl_stmt|;
annotation|@
name|Test
DECL|method|testRoundTripGetCustomersByName ()
specifier|public
name|void
name|testRoundTripGetCustomersByName
parameter_list|()
throws|throws
name|Exception
block|{
name|GetCustomersByNameResponse
name|response
init|=
name|customerService
operator|.
name|getCustomersByName
argument_list|(
operator|new
name|GetCustomersByName
argument_list|()
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|response
operator|.
name|getReturn
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Customer
name|firstCustomer
init|=
name|response
operator|.
name|getReturn
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|100000.0
argument_list|,
name|firstCustomer
operator|.
name|getRevenue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|String
name|jaxbPackage
init|=
name|GetCustomersByName
operator|.
name|class
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|ElementNameStrategy
name|elNameStrat
init|=
operator|new
name|ServiceInterfaceStrategy
argument_list|(
name|CustomerService
operator|.
name|class
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|SoapJaxbDataFormat
name|soapDataFormat
init|=
operator|new
name|SoapJaxbDataFormat
argument_list|(
name|jaxbPackage
argument_list|,
name|elNameStrat
argument_list|)
decl_stmt|;
specifier|final
name|InputStream
name|in
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"response.xml"
argument_list|)
decl_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|(
name|soapDataFormat
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|FileReplyProcessor
argument_list|(
name|in
argument_list|)
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|soapDataFormat
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

