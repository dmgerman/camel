begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.cxf.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|cxf
operator|.
name|blueprint
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
name|component
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|CxfRsEndpoint
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
name|blueprint
operator|.
name|CamelBlueprintTestSupport
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|CxfRsEndpointBeansTest
specifier|public
class|class
name|CxfRsEndpointBeansTest
extends|extends
name|CamelBlueprintTestSupport
block|{
annotation|@
name|Override
DECL|method|getBlueprintDescriptor ()
specifier|protected
name|String
name|getBlueprintDescriptor
parameter_list|()
block|{
return|return
literal|"org/apache/camel/test/cxf/blueprint/CxfRsEndpointBeans.xml"
return|;
block|}
annotation|@
name|Override
DECL|method|getBundleDirectives ()
specifier|protected
name|String
name|getBundleDirectives
parameter_list|()
block|{
return|return
literal|"blueprint.aries.xml-validation:=false"
return|;
block|}
annotation|@
name|Test
DECL|method|testCxfBusInjection ()
specifier|public
name|void
name|testCxfBusInjection
parameter_list|()
block|{
name|CxfRsEndpoint
name|serviceEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"cxfrs:bean:serviceEndpoint"
argument_list|,
name|CxfRsEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|CxfRsEndpoint
name|routerEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"cxfrs:bean:routerEndpoint"
argument_list|,
name|CxfRsEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|JAXRSServerFactoryBean
name|server
init|=
name|routerEndpoint
operator|.
name|createJAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|JAXRSClientFactoryBean
name|client
init|=
name|serviceEndpoint
operator|.
name|createJAXRSClientFactoryBean
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"These cxfrs endpoints don't share the same bus"
argument_list|,
name|server
operator|.
name|getBus
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|client
operator|.
name|getBus
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

