begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|component
operator|.
name|cxf
operator|.
name|CXFTestSupport
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
name|CustomerService
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
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|JAXRSServerFactoryBean
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
name|jaxrs
operator|.
name|client
operator|.
name|JAXRSClientFactoryBean
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
name|jaxrs
operator|.
name|provider
operator|.
name|json
operator|.
name|JSONProvider
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
DECL|class|CxfRsEndpointTest
specifier|public
class|class
name|CxfRsEndpointTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|CTX
specifier|private
specifier|static
specifier|final
name|String
name|CTX
init|=
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
operator|+
literal|"/CxfRsEndpointTest"
decl_stmt|;
annotation|@
name|Test
DECL|method|testCreateCxfRsEndpoint ()
specifier|public
name|void
name|testCreateCxfRsEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointUri
init|=
literal|"cxfrs://http://localhost:"
operator|+
name|CTX
operator|+
literal|""
operator|+
literal|"?loggingFeatureEnabled=true&loggingSizeLimit=200"
operator|+
literal|"&resourceClasses=org.apache.camel.component.cxf.jaxrs.testbean.CustomerService,"
operator|+
literal|"java.lang.String,org.apache.camel.component.cxf.jaxrs.testbean.Order"
decl_stmt|;
name|CxfRsComponent
name|component
init|=
operator|new
name|CxfRsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|CxfRsEndpoint
name|endpoint
init|=
operator|(
name|CxfRsEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The endpoint should not be null "
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong address "
argument_list|,
name|endpointUri
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong size of resouces classes"
argument_list|,
literal|3
argument_list|,
name|endpoint
operator|.
name|getResourceClasses
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong resources class"
argument_list|,
name|CustomerService
operator|.
name|class
argument_list|,
name|endpoint
operator|.
name|getResourceClasses
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong loggingFeatureEnabled setting"
argument_list|,
literal|true
argument_list|,
name|endpoint
operator|.
name|isLoggingFeatureEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong loggingSizeLimit setting"
argument_list|,
literal|200
argument_list|,
name|endpoint
operator|.
name|getLoggingSizeLimit
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCxfRsEndpointParameters ()
specifier|public
name|void
name|testCxfRsEndpointParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|CxfRsComponent
name|component
init|=
operator|new
name|CxfRsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|String
name|endpointUri
init|=
literal|"cxfrs://http://localhost:"
operator|+
name|CTX
operator|+
literal|"/templatetest/TID/ranges/start=0;end=1?"
operator|+
literal|"continuationTimeout=80000&httpClientAPI=true&loggingFeatureEnabled=true&loggingSizeLimit=200&q1=11&q2=12"
decl_stmt|;
name|CxfRsEndpoint
name|endpoint
init|=
operator|(
name|CxfRsEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong URI "
argument_list|,
name|endpointUri
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong usingClientAPI option"
argument_list|,
literal|true
argument_list|,
name|endpoint
operator|.
name|isHttpClientAPI
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"The Parameter should not be null"
operator|+
name|endpoint
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong parameter map"
argument_list|,
literal|"{q1=11, q2=12}"
argument_list|,
name|endpoint
operator|.
name|getParameters
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong continucationTimeout"
argument_list|,
literal|80000
argument_list|,
name|endpoint
operator|.
name|getContinuationTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCxfRsEndpointResourceClass ()
specifier|public
name|void
name|testCxfRsEndpointResourceClass
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointUri
init|=
literal|"cxfrs://http://localhost:"
operator|+
name|CTX
operator|+
literal|""
operator|+
literal|"?resourceClass=org.apache.camel.component.cxf.jaxrs.testbean.CustomerService"
decl_stmt|;
name|CxfRsComponent
name|component
init|=
operator|new
name|CxfRsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|CxfRsEndpoint
name|endpoint
init|=
operator|(
name|CxfRsEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The endpoint should not be null "
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong address "
argument_list|,
name|endpointUri
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong size of resouces classes"
argument_list|,
literal|1
argument_list|,
name|endpoint
operator|.
name|getResourceClasses
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong resources class"
argument_list|,
name|CustomerService
operator|.
name|class
argument_list|,
name|endpoint
operator|.
name|getResourceClasses
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
comment|// check the default continuation value
name|assertEquals
argument_list|(
literal|"Get a wrong continucationTimeout"
argument_list|,
literal|30000
argument_list|,
name|endpoint
operator|.
name|getContinuationTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCxfRsEndpointSetProvider ()
specifier|public
name|void
name|testCxfRsEndpointSetProvider
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointUri
init|=
literal|"cxfrs://http://localhost:"
operator|+
name|CTX
operator|+
literal|""
operator|+
literal|"?resourceClass=org.apache.camel.component.cxf.jaxrs.testbean.CustomerService"
decl_stmt|;
name|CxfRsComponent
name|component
init|=
operator|new
name|CxfRsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|CxfRsEndpoint
name|endpoint
init|=
operator|(
name|CxfRsEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
name|JSONProvider
argument_list|<
name|?
argument_list|>
name|jsonProvider
init|=
operator|new
name|JSONProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|jsonProvider
operator|.
name|setDropRootElement
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|jsonProvider
operator|.
name|setSupportUnwrapped
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setProvider
argument_list|(
name|jsonProvider
argument_list|)
expr_stmt|;
name|JAXRSServerFactoryBean
name|sfb
init|=
name|endpoint
operator|.
name|createJAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong proider size"
argument_list|,
literal|1
argument_list|,
name|sfb
operator|.
name|getProviders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JAXRSClientFactoryBean
name|cfb
init|=
name|endpoint
operator|.
name|createJAXRSClientFactoryBean
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong proider size"
argument_list|,
literal|1
argument_list|,
name|cfb
operator|.
name|getProviders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCxfRsEndpointCamelContextAware ()
specifier|public
name|void
name|testCxfRsEndpointCamelContextAware
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointUri
init|=
literal|"cxfrs://simple"
decl_stmt|;
name|CxfRsEndpoint
name|endpoint
init|=
operator|new
name|CxfRsEndpoint
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:9000/test"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setResourceClasses
argument_list|(
name|CustomerService
operator|.
name|class
argument_list|)
expr_stmt|;
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|addEndpoint
argument_list|(
name|endpointUri
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong camel context."
argument_list|,
name|context
argument_list|,
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

