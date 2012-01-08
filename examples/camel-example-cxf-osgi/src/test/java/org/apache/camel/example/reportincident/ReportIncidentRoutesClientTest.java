begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.reportincident
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|reportincident
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
name|camel
operator|.
name|test
operator|.
name|junit4
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
name|jaxws
operator|.
name|JaxWsProxyFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
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
name|AbstractApplicationContext
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
comment|/**  * Unit test of our routes  */
end_comment

begin_class
DECL|class|ReportIncidentRoutesClientTest
specifier|public
class|class
name|ReportIncidentRoutesClientTest
extends|extends
name|CamelSpringTestSupport
block|{
comment|// should be the same address as we have in our route
DECL|field|URL
specifier|private
specifier|static
specifier|final
name|String
name|URL
init|=
literal|"http://localhost:%s/cxf/camel-example-cxf-osgi/webservices/incident"
decl_stmt|;
DECL|field|port
specifier|private
specifier|static
specifier|final
name|int
name|port
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|9100
argument_list|)
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|setUpBeforeClass ()
specifier|public
specifier|static
name|void
name|setUpBeforeClass
parameter_list|()
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"port"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|tearDownBeforeClass ()
specifier|public
specifier|static
name|void
name|tearDownBeforeClass
parameter_list|()
block|{
name|System
operator|.
name|clearProperty
argument_list|(
literal|"port"
argument_list|)
expr_stmt|;
block|}
DECL|method|createCXFClient (String url)
specifier|protected
specifier|static
name|ReportIncidentEndpoint
name|createCXFClient
parameter_list|(
name|String
name|url
parameter_list|)
block|{
comment|// we use CXF to create a client for us as its easier than JAXWS and works
name|JaxWsProxyFactoryBean
name|factory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|ReportIncidentEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
name|url
argument_list|)
expr_stmt|;
return|return
operator|(
name|ReportIncidentEndpoint
operator|)
name|factory
operator|.
name|create
argument_list|()
return|;
block|}
annotation|@
name|Test
DECL|method|testReportIncident ()
specifier|public
name|void
name|testReportIncident
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create input parameter
name|InputReportIncident
name|input
init|=
operator|new
name|InputReportIncident
argument_list|()
decl_stmt|;
name|input
operator|.
name|setIncidentId
argument_list|(
literal|"123"
argument_list|)
expr_stmt|;
name|input
operator|.
name|setIncidentDate
argument_list|(
literal|"2008-08-18"
argument_list|)
expr_stmt|;
name|input
operator|.
name|setGivenName
argument_list|(
literal|"Claus"
argument_list|)
expr_stmt|;
name|input
operator|.
name|setFamilyName
argument_list|(
literal|"Ibsen"
argument_list|)
expr_stmt|;
name|input
operator|.
name|setSummary
argument_list|(
literal|"Bla"
argument_list|)
expr_stmt|;
name|input
operator|.
name|setDetails
argument_list|(
literal|"Bla bla"
argument_list|)
expr_stmt|;
name|input
operator|.
name|setEmail
argument_list|(
literal|"davsclaus@apache.org"
argument_list|)
expr_stmt|;
name|input
operator|.
name|setPhone
argument_list|(
literal|"0045 2962 7576"
argument_list|)
expr_stmt|;
comment|// create the webservice client and send the request
name|ReportIncidentEndpoint
name|client
init|=
name|createCXFClient
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|URL
argument_list|,
name|port
argument_list|)
argument_list|)
decl_stmt|;
name|OutputReportIncident
name|out
init|=
name|client
operator|.
name|reportIncident
argument_list|(
name|input
argument_list|)
decl_stmt|;
comment|// assert we got a OK back
name|assertEquals
argument_list|(
literal|"OK"
argument_list|,
name|out
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
comment|// test the input with other users
name|input
operator|.
name|setGivenName
argument_list|(
literal|"Guest"
argument_list|)
expr_stmt|;
name|out
operator|=
name|client
operator|.
name|reportIncident
argument_list|(
name|input
argument_list|)
expr_stmt|;
comment|// assert we got a Accept back
name|assertEquals
argument_list|(
literal|"Accepted"
argument_list|,
name|out
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"camel-context.xml"
block|}
argument_list|)
return|;
block|}
block|}
end_class

end_unit

