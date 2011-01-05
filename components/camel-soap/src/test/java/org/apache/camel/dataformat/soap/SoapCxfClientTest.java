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
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
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
name|GetAllCustomersResponse
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
name|com
operator|.
name|example
operator|.
name|customerservice
operator|.
name|NoSuchCustomer
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
name|NoSuchCustomerException
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
name|SaveCustomer
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
name|processor
operator|.
name|interceptor
operator|.
name|Tracer
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
name|BeforeClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
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
name|junit
operator|.
name|runner
operator|.
name|RunWith
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
name|SpringJUnit4ClassRunner
import|;
end_import

begin_comment
comment|/**  * Checks for interoperability between a CXF client that is attached using the  * Camel transport for CXF and the SOAP data format  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|SpringJUnit4ClassRunner
operator|.
name|class
argument_list|)
annotation|@
name|ContextConfiguration
DECL|class|SoapCxfClientTest
specifier|public
class|class
name|SoapCxfClientTest
extends|extends
name|RouteBuilder
block|{
DECL|field|serverBean
specifier|private
specifier|static
name|CustomerServiceImpl
name|serverBean
decl_stmt|;
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"customerServiceCxfProxy"
argument_list|)
DECL|field|customerService
specifier|protected
name|CustomerService
name|customerService
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|initServerBean ()
specifier|public
specifier|static
name|void
name|initServerBean
parameter_list|()
block|{
name|serverBean
operator|=
operator|new
name|CustomerServiceImpl
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
DECL|method|testSuccess ()
specifier|public
name|void
name|testSuccess
parameter_list|()
throws|throws
name|NoSuchCustomerException
block|{
name|GetCustomersByName
name|request
init|=
operator|new
name|GetCustomersByName
argument_list|()
decl_stmt|;
name|request
operator|.
name|setName
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|GetCustomersByNameResponse
name|response
init|=
name|customerService
operator|.
name|getCustomersByName
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Customer
argument_list|>
name|customers
init|=
name|response
operator|.
name|getReturn
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|customers
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|customers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRoundTripGetAllCustomers ()
specifier|public
name|void
name|testRoundTripGetAllCustomers
parameter_list|()
throws|throws
name|Exception
block|{
name|GetAllCustomersResponse
name|response
init|=
name|customerService
operator|.
name|getAllCustomers
argument_list|()
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
argument_list|,
literal|0.00001
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRoundTripSaveCustomer ()
specifier|public
name|void
name|testRoundTripSaveCustomer
parameter_list|()
throws|throws
name|Exception
block|{
name|Customer
name|testCustomer
init|=
operator|new
name|Customer
argument_list|()
decl_stmt|;
name|testCustomer
operator|.
name|setName
argument_list|(
literal|"testName"
argument_list|)
expr_stmt|;
name|SaveCustomer
name|request
init|=
operator|new
name|SaveCustomer
argument_list|()
decl_stmt|;
name|request
operator|.
name|setCustomer
argument_list|(
name|testCustomer
argument_list|)
expr_stmt|;
name|customerService
operator|.
name|saveCustomer
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|Customer
name|customer2
init|=
name|serverBean
operator|.
name|getLastSavedCustomer
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"testName"
argument_list|,
name|customer2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
DECL|method|testFault ()
specifier|public
name|void
name|testFault
parameter_list|()
block|{
name|GetCustomersByName
name|request
init|=
operator|new
name|GetCustomersByName
argument_list|()
decl_stmt|;
name|request
operator|.
name|setName
argument_list|(
literal|"none"
argument_list|)
expr_stmt|;
try|try
block|{
name|customerService
operator|.
name|getCustomersByName
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|(
literal|"NoSuchCustomerException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchCustomerException
name|e
parameter_list|)
block|{
name|NoSuchCustomer
name|info
init|=
name|e
operator|.
name|getFaultInfo
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"none"
argument_list|,
name|info
operator|.
name|getCustomerId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
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
literal|false
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
name|getContext
argument_list|()
operator|.
name|addInterceptStrategy
argument_list|(
operator|new
name|Tracer
argument_list|()
argument_list|)
expr_stmt|;
name|getContext
argument_list|()
operator|.
name|setTracing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:cxfclient"
argument_list|)
comment|//
operator|.
name|onException
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|marshal
argument_list|(
name|soapDataFormat
argument_list|)
operator|.
name|end
argument_list|()
comment|//
operator|.
name|unmarshal
argument_list|(
name|soapDataFormat
argument_list|)
comment|//
operator|.
name|bean
argument_list|(
name|serverBean
argument_list|)
comment|//
operator|.
name|marshal
argument_list|(
name|soapDataFormat
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

