begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.soap.name
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|soap
operator|.
name|name
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|com
operator|.
name|example
operator|.
name|customerservice
operator|.
name|multipart
operator|.
name|MultiPartCustomerService
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
name|RuntimeCamelException
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|ServiceInterfaceStrategyTest
specifier|public
class|class
name|ServiceInterfaceStrategyTest
extends|extends
name|Assert
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ServiceInterfaceStrategyTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testServiceInterfaceStrategyWithClient ()
specifier|public
name|void
name|testServiceInterfaceStrategyWithClient
parameter_list|()
block|{
name|ServiceInterfaceStrategy
name|strategy
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
name|QName
name|elName
init|=
name|strategy
operator|.
name|findQNameForSoapActionOrType
argument_list|(
literal|""
argument_list|,
name|GetCustomersByName
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://customerservice.example.com/"
argument_list|,
name|elName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getCustomersByName"
argument_list|,
name|elName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|QName
name|elName2
init|=
name|strategy
operator|.
name|findQNameForSoapActionOrType
argument_list|(
literal|"getCustomersByName"
argument_list|,
name|GetCustomersByName
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://customerservice.example.com/"
argument_list|,
name|elName2
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getCustomersByName"
argument_list|,
name|elName2
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
comment|// Tests the case where the soap action is found but the in type is null
name|QName
name|elName3
init|=
name|strategy
operator|.
name|findQNameForSoapActionOrType
argument_list|(
literal|"http://customerservice.example.com/getAllCustomers"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|elName3
argument_list|)
expr_stmt|;
try|try
block|{
name|elName
operator|=
name|strategy
operator|.
name|findQNameForSoapActionOrType
argument_list|(
literal|"test"
argument_list|,
name|Class
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Caught expected message: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testServiceInterfaceStrategyWithServer ()
specifier|public
name|void
name|testServiceInterfaceStrategyWithServer
parameter_list|()
block|{
name|ServiceInterfaceStrategy
name|strategy
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
comment|// Tests the case where the action is not found but the type is
name|QName
name|elName
init|=
name|strategy
operator|.
name|findQNameForSoapActionOrType
argument_list|(
literal|""
argument_list|,
name|GetCustomersByNameResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://customerservice.example.com/"
argument_list|,
name|elName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getCustomersByNameResponse"
argument_list|,
name|elName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
comment|// Tests the case where the soap action is found
name|QName
name|elName2
init|=
name|strategy
operator|.
name|findQNameForSoapActionOrType
argument_list|(
literal|"http://customerservice.example.com/getCustomersByName"
argument_list|,
name|GetCustomersByName
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://customerservice.example.com/"
argument_list|,
name|elName2
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getCustomersByNameResponse"
argument_list|,
name|elName2
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
comment|// this tests the case that the soap action as well as the type are not
comment|// found
try|try
block|{
name|elName
operator|=
name|strategy
operator|.
name|findQNameForSoapActionOrType
argument_list|(
literal|"test"
argument_list|,
name|Class
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Caught expected message: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testServiceInterfaceStrategyWithRequestWrapperAndClient ()
specifier|public
name|void
name|testServiceInterfaceStrategyWithRequestWrapperAndClient
parameter_list|()
block|{
name|ServiceInterfaceStrategy
name|strategy
init|=
operator|new
name|ServiceInterfaceStrategy
argument_list|(
name|com
operator|.
name|example
operator|.
name|customerservice2
operator|.
name|CustomerService
operator|.
name|class
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|QName
name|elName
init|=
name|strategy
operator|.
name|findQNameForSoapActionOrType
argument_list|(
literal|""
argument_list|,
name|com
operator|.
name|example
operator|.
name|customerservice2
operator|.
name|GetCustomersByName
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"http://customerservice2.example.com/"
argument_list|,
name|elName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"getCustomersByName"
argument_list|,
name|elName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|elName
operator|=
name|strategy
operator|.
name|findQNameForSoapActionOrType
argument_list|(
literal|"test"
argument_list|,
name|Class
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Caught expected message: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testWithNonWebservice ()
specifier|public
name|void
name|testWithNonWebservice
parameter_list|()
block|{
try|try
block|{
operator|new
name|ServiceInterfaceStrategy
argument_list|(
name|Object
operator|.
name|class
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw an exception for a class that is no webservice"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Caught expected message: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testMultiPart ()
specifier|public
name|void
name|testMultiPart
parameter_list|()
block|{
name|ServiceInterfaceStrategy
name|strategy
init|=
operator|new
name|ServiceInterfaceStrategy
argument_list|(
name|MultiPartCustomerService
operator|.
name|class
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|QName
name|custNameQName
init|=
name|strategy
operator|.
name|findQNameForSoapActionOrType
argument_list|(
literal|"http://multipart.customerservice.example.com/getCustomersByName"
argument_list|,
name|com
operator|.
name|example
operator|.
name|customerservice
operator|.
name|multipart
operator|.
name|GetCustomersByName
operator|.
name|class
argument_list|)
decl_stmt|;
name|QName
name|custTypeQName
init|=
name|strategy
operator|.
name|findQNameForSoapActionOrType
argument_list|(
literal|"http://multipart.customerservice.example.com/getCustomersByName"
argument_list|,
name|com
operator|.
name|example
operator|.
name|customerservice
operator|.
name|multipart
operator|.
name|Product
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://multipart.customerservice.example.com/"
argument_list|,
name|custNameQName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getCustomersByName"
argument_list|,
name|custNameQName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://multipart.customerservice.example.com/"
argument_list|,
name|custTypeQName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"product"
argument_list|,
name|custTypeQName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

