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
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|builder
operator|.
name|RouteBuilder
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
name|properties
operator|.
name|PropertiesComponent
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
name|jvnet
operator|.
name|mock_javamail
operator|.
name|Mailbox
import|;
end_import

begin_comment
comment|/**  * Unit test of our routes  */
end_comment

begin_class
DECL|class|ReportIncidentRoutesTest
specifier|public
class|class
name|ReportIncidentRoutesTest
extends|extends
name|CamelTestSupport
block|{
comment|// should be the same address as we have in our route
DECL|field|URL
specifier|private
specifier|static
specifier|final
name|String
name|URL
init|=
literal|"http://localhost:{{port}}/camel-example-reportincident/webservices/incident"
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|setupFreePort ()
specifier|public
specifier|static
name|void
name|setupFreePort
parameter_list|()
throws|throws
name|Exception
block|{
comment|// find a free port number from 9200 onwards, and write that in the custom.properties file
comment|// which we will use for the unit tests, to avoid port number in use problems
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
name|String
name|s
init|=
literal|"port="
operator|+
name|port
decl_stmt|;
name|File
name|custom
init|=
operator|new
name|File
argument_list|(
literal|"target/custom.properties"
argument_list|)
decl_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|custom
argument_list|)
decl_stmt|;
name|fos
operator|.
name|write
argument_list|(
name|s
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camel
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|camel
operator|.
name|addComponent
argument_list|(
literal|"properties"
argument_list|,
operator|new
name|PropertiesComponent
argument_list|(
literal|"classpath:incident.properties,file:target/custom.properties"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|camel
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
name|ReportIncidentRoutes
name|routes
init|=
operator|new
name|ReportIncidentRoutes
argument_list|()
decl_stmt|;
name|routes
operator|.
name|setUsingServletTransport
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|routes
return|;
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
comment|// assert mailbox is empty before starting
name|Mailbox
name|inbox
init|=
name|Mailbox
operator|.
name|get
argument_list|(
literal|"incident@mycompany.com"
argument_list|)
decl_stmt|;
name|inbox
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should not have mails"
argument_list|,
literal|0
argument_list|,
name|inbox
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
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
name|String
name|url
init|=
name|context
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|URL
argument_list|)
decl_stmt|;
name|ReportIncidentEndpoint
name|client
init|=
name|createCXFClient
argument_list|(
name|url
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
literal|"0"
argument_list|,
name|out
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
comment|// let some time pass to allow Camel to pickup the file and send it as an email
name|Thread
operator|.
name|sleep
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
comment|// assert mail box
name|assertEquals
argument_list|(
literal|"Should have got 1 mail"
argument_list|,
literal|1
argument_list|,
name|inbox
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

