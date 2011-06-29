begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
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
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Endpoint
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
name|AvailablePortFinder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|GreeterImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  * A unit test for testing a CXF client invoking a CXF server via route   * in PAYLOAD mode and with CXF features specified in the Spring config.  *   * @version   */
end_comment

begin_class
DECL|class|CxfGreeterPayLoadWithFeatureRouterTest
specifier|public
class|class
name|CxfGreeterPayLoadWithFeatureRouterTest
extends|extends
name|AbstractCXFGreeterRouterTest
block|{
DECL|field|port1
specifier|private
specifier|static
name|int
name|port1
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|port2
specifier|private
specifier|static
name|int
name|port2
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
static|static
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"CxfGreeterPayLoadWithFeatureRouterTest.port1"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|port1
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"CxfGreeterPayLoadWithFeatureRouterTest.port2"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|port2
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getPort1 ()
specifier|public
name|String
name|getPort1
parameter_list|()
block|{
return|return
name|Integer
operator|.
name|toString
argument_list|(
name|port1
argument_list|)
return|;
block|}
DECL|method|getPort2 ()
specifier|public
name|String
name|getPort2
parameter_list|()
block|{
return|return
name|Integer
operator|.
name|toString
argument_list|(
name|port2
argument_list|)
return|;
block|}
annotation|@
name|BeforeClass
DECL|method|startService ()
specifier|public
specifier|static
name|void
name|startService
parameter_list|()
block|{
name|Object
name|implementor
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|port1
operator|+
literal|"/SoapContext/SoapPort"
decl_stmt|;
name|endpoint
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|setUseRouteBuilder
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|CxfEndpoint
name|endpoint
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"cxf:bean:serviceEndpoint?dataFormat=PAYLOAD"
argument_list|,
name|CxfEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|TestCxfFeature
operator|.
name|class
argument_list|,
operator|(
operator|(
name|CxfSpringEndpoint
operator|)
name|endpoint
operator|)
operator|.
name|getFeatures
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
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
annotation|@
name|Override
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
literal|"org/apache/camel/component/cxf/GreeterEndpointWithFeatureBeans.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

