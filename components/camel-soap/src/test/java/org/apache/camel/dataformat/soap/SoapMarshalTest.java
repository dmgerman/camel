begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|IOException
import|;
end_import

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|EndpointInject
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
name|ProducerTemplate
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
name|mock
operator|.
name|MockEndpoint
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
name|TypeNameStrategy
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
DECL|class|SoapMarshalTest
specifier|public
class|class
name|SoapMarshalTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:result"
argument_list|)
DECL|field|resultEndpoint
specifier|protected
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
annotation|@
name|Produce
argument_list|(
literal|"direct:start"
argument_list|)
DECL|field|producer
specifier|protected
name|ProducerTemplate
name|producer
decl_stmt|;
comment|/**      * Test Soap marshalling by sending a GetCustomerByName object and checking      * against a xml file.      *       * @throws IOException      * @throws InterruptedException      */
annotation|@
name|Test
DECL|method|testMarshalNormalObject ()
specifier|public
name|void
name|testMarshalNormalObject
parameter_list|()
throws|throws
name|IOException
throws|,
name|InterruptedException
block|{
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
literal|"SoapMarshalTestExpectedResult.xml"
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|TestUtil
operator|.
name|readStream
argument_list|(
name|in
argument_list|)
argument_list|)
expr_stmt|;
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
literal|"Smith"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|sendBody
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test Soap marshalling by sending a NoSuchCustomerException object and      * checking against a xml file. We expect to receive a SOAP fault here that      * contains a NoSuchCustomer object as detail.      *       * @throws IOException      * @throws InterruptedException      */
annotation|@
name|Test
DECL|method|testMarshalException ()
specifier|public
name|void
name|testMarshalException
parameter_list|()
throws|throws
name|IOException
throws|,
name|InterruptedException
block|{
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
literal|"SoapMarshalTestExpectedFault.xml"
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|TestUtil
operator|.
name|readStream
argument_list|(
name|in
argument_list|)
argument_list|)
expr_stmt|;
name|NoSuchCustomer
name|noSuchCustomer
init|=
operator|new
name|NoSuchCustomer
argument_list|()
decl_stmt|;
name|noSuchCustomer
operator|.
name|setCustomerId
argument_list|(
literal|"None"
argument_list|)
expr_stmt|;
name|NoSuchCustomerException
name|exception
init|=
operator|new
name|NoSuchCustomerException
argument_list|(
literal|"No customer found"
argument_list|,
name|noSuchCustomer
argument_list|)
decl_stmt|;
name|producer
operator|.
name|sendBodyAndHeader
argument_list|(
literal|null
argument_list|,
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|exception
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
comment|/**      * Create data format by using the constructor      */
DECL|method|createDataFormat ()
specifier|protected
name|SoapJaxbDataFormat
name|createDataFormat
parameter_list|()
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
name|elStrat
init|=
operator|new
name|TypeNameStrategy
argument_list|()
decl_stmt|;
return|return
operator|new
name|SoapJaxbDataFormat
argument_list|(
name|jaxbPackage
argument_list|,
name|elStrat
argument_list|)
return|;
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|SoapJaxbDataFormat
name|df
init|=
name|createDataFormat
argument_list|()
decl_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
comment|//
operator|.
name|marshal
argument_list|(
name|df
argument_list|)
comment|//
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

