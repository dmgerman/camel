begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.cxfbean
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
name|cxfbean
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
name|Processor
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
name|CxfConsumer
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
name|CxfProducer
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
name|transport
operator|.
name|CamelConduit
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
name|transport
operator|.
name|CamelDestination
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
DECL|class|CamelEndpointSpringConfigureTest
specifier|public
class|class
name|CamelEndpointSpringConfigureTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|Test
DECL|method|testCreateDestinationFromSpring ()
specifier|public
name|void
name|testCreateDestinationFromSpring
parameter_list|()
throws|throws
name|Exception
block|{
name|CxfEndpoint
name|cxfEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"cxf:bean:serviceEndpoint"
argument_list|,
name|CxfEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|CxfProducer
name|producer
init|=
operator|(
name|CxfProducer
operator|)
name|cxfEndpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The producer should not be null"
argument_list|,
name|producer
argument_list|)
expr_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|CamelConduit
name|conduit
init|=
operator|(
name|CamelConduit
operator|)
name|producer
operator|.
name|getClient
argument_list|()
operator|.
name|getConduit
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"we should get SpringCamelContext here"
argument_list|,
name|conduit
operator|.
name|getCamelContext
argument_list|()
operator|instanceof
name|SpringCamelContext
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The context id should be camel_conduit"
argument_list|,
literal|"camel_conduit"
argument_list|,
name|conduit
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|cxfEndpoint
operator|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"cxf:bean:routerEndpoint"
argument_list|,
name|CxfEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|CxfConsumer
name|consumer
init|=
operator|(
name|CxfConsumer
operator|)
name|cxfEndpoint
operator|.
name|createConsumer
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// do nothing here
block|}
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The consumer should not be null"
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
name|CamelDestination
name|destination
init|=
operator|(
name|CamelDestination
operator|)
name|consumer
operator|.
name|getServer
argument_list|()
operator|.
name|getDestination
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"we should get SpringCamelContext here"
argument_list|,
name|destination
operator|.
name|getCamelContext
argument_list|()
operator|instanceof
name|SpringCamelContext
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The context id should be camel_destination"
argument_list|,
literal|"camel_destination"
argument_list|,
name|destination
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
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
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"/org/apache/camel/component/cxf/transport/CamelEndpointSpringConfigure.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

