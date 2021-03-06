begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|List
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|CxfRsServerFactoryBeanTest
specifier|public
class|class
name|CxfRsServerFactoryBeanTest
extends|extends
name|AbstractSpringBeanTestSupport
block|{
DECL|field|port
specifier|static
name|int
name|port
init|=
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|getApplicationContextFiles ()
specifier|protected
name|String
index|[]
name|getApplicationContextFiles
parameter_list|()
block|{
return|return
operator|new
name|String
index|[]
block|{
literal|"org/apache/camel/component/cxf/spring/CxfRsServerFactoryBeans.xml"
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testCxfRsServerFactoryBean ()
specifier|public
name|void
name|testCxfRsServerFactoryBean
parameter_list|()
block|{
name|SpringJAXRSServerFactoryBean
name|sfb1
init|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"rsServer1"
argument_list|,
name|SpringJAXRSServerFactoryBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong address"
argument_list|,
name|sfb1
operator|.
name|getAddress
argument_list|()
argument_list|,
literal|"http://localhost:"
operator|+
name|port
operator|+
literal|"/CxfRsServerFactoryBeanTest/server1"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|resource1Classes
init|=
name|sfb1
operator|.
name|getResourceClasses
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong size of resouceClasses"
argument_list|,
name|resource1Classes
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
name|resource1Classes
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
name|SpringJAXRSServerFactoryBean
name|sfb2
init|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"rsServer2"
argument_list|,
name|SpringJAXRSServerFactoryBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong address"
argument_list|,
name|sfb2
operator|.
name|getAddress
argument_list|()
argument_list|,
literal|"http://localhost:"
operator|+
name|port
operator|+
literal|"/CxfRsServerFactoryBeanTest/server2"
argument_list|)
expr_stmt|;
name|sfb2
operator|.
name|getResourceClasses
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|resource2Classes
init|=
name|sfb2
operator|.
name|getResourceClasses
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong size of resouceClasses"
argument_list|,
name|resource2Classes
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
name|resource2Classes
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
literal|"Got the wrong schemalocations size"
argument_list|,
literal|1
argument_list|,
name|sfb2
operator|.
name|getSchemaLocations
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Got the wrong schemalocation"
argument_list|,
literal|"classpath:wsdl/Message.xsd"
argument_list|,
name|sfb2
operator|.
name|getSchemaLocations
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

