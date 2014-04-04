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
name|component
operator|.
name|cxf
operator|.
name|spring
operator|.
name|SpringJAXRSClientFactoryBean
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
name|spring
operator|.
name|SpringJAXRSServerFactoryBean
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
name|spring
operator|.
name|CamelSpringTestSupport
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
name|version
operator|.
name|Version
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
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|CxfRsSpringEndpointTest
specifier|public
class|class
name|CxfRsSpringEndpointTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|Test
DECL|method|testCreateCxfRsServerFactoryBean ()
specifier|public
name|void
name|testCreateCxfRsServerFactoryBean
parameter_list|()
block|{
name|CxfRsEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"cxfrs://bean://rsServer"
argument_list|,
name|CxfRsEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|SpringJAXRSServerFactoryBean
name|sfb
init|=
operator|(
name|SpringJAXRSServerFactoryBean
operator|)
name|endpoint
operator|.
name|createJAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong provider size"
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
name|assertEquals
argument_list|(
literal|"Get a wrong beanId"
argument_list|,
name|sfb
operator|.
name|getBeanId
argument_list|()
argument_list|,
literal|"rsServer"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong address"
argument_list|,
name|sfb
operator|.
name|getAddress
argument_list|()
argument_list|,
literal|"http://localhost:9000/router"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong size of resource classes"
argument_list|,
name|sfb
operator|.
name|getResourceClasses
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong resource class"
argument_list|,
name|sfb
operator|.
name|getResourceClasses
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|CustomerService
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Got the wrong loggingFeatureEnabled"
argument_list|,
literal|true
argument_list|,
name|sfb
operator|.
name|isLoggingFeatureEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Got the wrong loggingSizeLimit"
argument_list|,
literal|200
argument_list|,
name|sfb
operator|.
name|getLoggingSizeLimit
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong size of interceptors"
argument_list|,
literal|1
argument_list|,
name|sfb
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|endpointProps
init|=
name|sfb
operator|.
name|getProperties
argument_list|()
decl_stmt|;
comment|// The beanId key is put by the AbstractCxfBeanDefinitionParser, so the size is 2
name|assertEquals
argument_list|(
literal|"Single endpoint property is expected"
argument_list|,
literal|2
argument_list|,
name|endpointProps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong property value"
argument_list|,
literal|"aValue"
argument_list|,
name|endpointProps
operator|.
name|get
argument_list|(
literal|"aKey"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreateCxfRsClientFactoryBean ()
specifier|public
name|void
name|testCreateCxfRsClientFactoryBean
parameter_list|()
block|{
name|CxfRsEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"cxfrs://bean://rsClient"
argument_list|,
name|CxfRsEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|SpringJAXRSClientFactoryBean
name|cfb
init|=
operator|(
name|SpringJAXRSClientFactoryBean
operator|)
name|endpoint
operator|.
name|createJAXRSClientFactoryBean
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong beanId"
argument_list|,
name|cfb
operator|.
name|getBeanId
argument_list|()
argument_list|,
literal|"rsClient"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong address"
argument_list|,
name|cfb
operator|.
name|getAddress
argument_list|()
argument_list|,
literal|"http://localhost:9002/helloworld"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Get a wrong resource class instance"
argument_list|,
name|cfb
operator|.
name|create
argument_list|()
operator|instanceof
name|CustomerService
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Got the wrong loggingFeatureEnabled"
argument_list|,
literal|false
argument_list|,
name|cfb
operator|.
name|isLoggingFeatureEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Got the wrong loggingSizeLimit"
argument_list|,
literal|0
argument_list|,
name|cfb
operator|.
name|getLoggingSizeLimit
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong size of interceptors"
argument_list|,
literal|1
argument_list|,
name|cfb
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
name|String
name|version
init|=
name|Version
operator|.
name|getCurrentVersion
argument_list|()
decl_stmt|;
if|if
condition|(
name|version
operator|.
name|contains
argument_list|(
literal|"2.5"
argument_list|)
operator|||
name|version
operator|.
name|contains
argument_list|(
literal|"2.4"
argument_list|)
condition|)
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
argument_list|(
literal|"org/apache/camel/component/cxf/jaxrs/CxfRsSpringEndpointBeans.xml"
argument_list|)
argument_list|)
return|;
block|}
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
argument_list|(
literal|"org/apache/camel/component/cxf/jaxrs/CxfRsSpringEndpointBeans-2.6.xml"
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

