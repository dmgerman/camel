begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.util
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
name|util
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
name|CxfEndpoint
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
name|CxfSpringEndpoint
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
name|DataFormat
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
name|spring
operator|.
name|SpringCamelContext
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
name|BusFactory
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
DECL|class|CxfEndpointUtilsWithSpringTest
specifier|public
class|class
name|CxfEndpointUtilsWithSpringTest
extends|extends
name|CxfEndpointUtilsTest
block|{
DECL|field|applicationContext
specifier|protected
name|AbstractXmlApplicationContext
name|applicationContext
decl_stmt|;
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
name|applicationContext
operator|=
name|createApplicationContext
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have created a valid spring context"
argument_list|,
name|applicationContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|applicationContext
operator|!=
literal|null
condition|)
block|{
name|applicationContext
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|protected
name|CamelContext
name|getCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|SpringCamelContext
operator|.
name|springCamelContext
argument_list|(
name|applicationContext
argument_list|)
return|;
block|}
DECL|method|createApplicationContext ()
specifier|protected
name|ClassPathXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/cxf/util/CxfEndpointBeans.xml"
argument_list|)
return|;
block|}
DECL|method|getEndpointURI ()
specifier|protected
name|String
name|getEndpointURI
parameter_list|()
block|{
return|return
literal|"cxf:bean:testEndpoint"
return|;
block|}
DECL|method|getNoServiceClassURI ()
specifier|protected
name|String
name|getNoServiceClassURI
parameter_list|()
block|{
return|return
literal|"cxf:bean:noServiceClassEndpoint"
return|;
block|}
DECL|method|testGetServiceClass ()
specifier|public
name|void
name|testGetServiceClass
parameter_list|()
throws|throws
name|Exception
block|{
name|CxfEndpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
literal|"cxf:bean:helloServiceEndpoint?serviceClass=#helloServiceImpl"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.camel.component.cxf.HelloServiceImpl"
argument_list|,
name|endpoint
operator|.
name|getServiceClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetDataFormat ()
specifier|public
name|void
name|testGetDataFormat
parameter_list|()
throws|throws
name|Exception
block|{
name|CxfEndpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
name|getEndpointURI
argument_list|()
operator|+
literal|"?dataFormat=MESSAGE"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"We should get the Message DataFormat"
argument_list|,
name|DataFormat
operator|.
name|MESSAGE
argument_list|,
name|endpoint
operator|.
name|getDataFormat
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetProperties ()
specifier|public
name|void
name|testGetProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|CxfSpringEndpoint
name|endpoint
init|=
operator|(
name|CxfSpringEndpoint
operator|)
name|createEndpoint
argument_list|(
name|getEndpointURI
argument_list|()
argument_list|)
decl_stmt|;
name|QName
name|service
init|=
name|endpoint
operator|.
name|getBean
argument_list|()
operator|.
name|getServiceName
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"We should get the right service name"
argument_list|,
name|service
argument_list|,
name|SERVICE_NAME
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The cxf endpoint's DataFromat should be MESSAGE"
argument_list|,
name|DataFormat
operator|.
name|MESSAGE
argument_list|,
name|endpoint
operator|.
name|getDataFormat
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|=
operator|(
name|CxfSpringEndpoint
operator|)
name|createEndpoint
argument_list|(
literal|"cxf:bean:testPropertiesEndpoint"
argument_list|)
expr_stmt|;
name|service
operator|=
name|CxfEndpointUtils
operator|.
name|getServiceName
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"We should get the right service name"
argument_list|,
name|service
argument_list|,
name|SERVICE_NAME
argument_list|)
expr_stmt|;
name|QName
name|port
init|=
name|CxfEndpointUtils
operator|.
name|getPortName
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"We should get the right endpoint name"
argument_list|,
name|port
argument_list|,
name|PORT_NAME
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetDataFormatFromCxfEndpontProperties ()
specifier|public
name|void
name|testGetDataFormatFromCxfEndpontProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|CxfEndpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
name|getEndpointURI
argument_list|()
operator|+
literal|"?dataFormat=PAYLOAD"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"We should get the PAYLOAD DataFormat"
argument_list|,
name|DataFormat
operator|.
name|PAYLOAD
argument_list|,
name|endpoint
operator|.
name|getDataFormat
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

