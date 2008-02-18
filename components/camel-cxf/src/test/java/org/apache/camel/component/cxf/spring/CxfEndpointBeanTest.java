begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.spring
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
name|spring
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
comment|/**  *  */
end_comment

begin_class
DECL|class|CxfEndpointBeanTest
specifier|public
class|class
name|CxfEndpointBeanTest
extends|extends
name|TestCase
block|{
DECL|method|testCxfEndpointBeanDefinitionParser ()
specifier|public
name|void
name|testCxfEndpointBeanDefinitionParser
parameter_list|()
block|{
name|ClassPathXmlApplicationContext
name|ctx
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"org/apache/camel/component/cxf/spring/CxfEndpointBeans.xml"
block|}
argument_list|)
decl_stmt|;
name|CxfEndpointBean
name|routerEndpoint
init|=
operator|(
name|CxfEndpointBean
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"routerEndpoint"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Got the wrong endpoint address"
argument_list|,
name|routerEndpoint
operator|.
name|getAddress
argument_list|()
argument_list|,
literal|"http://localhost:9000/router"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Got the wrong endpont service class"
argument_list|,
name|routerEndpoint
operator|.
name|getServiceClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|,
literal|"org.apache.camel.component.cxf.HelloService"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

