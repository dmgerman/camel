begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|spring
operator|.
name|Main
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
name|util
operator|.
name|FileUtil
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
name|Assert
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

begin_comment
comment|/**  * Unit test of our routes  */
end_comment

begin_class
DECL|class|ReportIncidentRoutesTest
specifier|public
class|class
name|ReportIncidentRoutesTest
block|{
comment|// should be the same address as we have in our route
DECL|field|url
specifier|private
specifier|static
name|String
name|url
decl_stmt|;
DECL|field|main
specifier|protected
name|Main
name|main
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
comment|// find a free port number, and write that in the custom.properties file
comment|// which we will use for the unit tests, to avoid port number in use problems
name|int
name|port
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
name|String
name|s
init|=
literal|"proxy.port="
operator|+
name|port
decl_stmt|;
name|int
name|port2
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
name|String
name|s2
init|=
literal|"real.port="
operator|+
name|port2
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
name|write
argument_list|(
literal|"\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|fos
operator|.
name|write
argument_list|(
name|s2
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
name|url
operator|=
literal|"http://localhost:"
operator|+
name|port
operator|+
literal|"/camel-example-cxf-proxy/webservices/incident"
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|cleanup ()
specifier|public
specifier|static
name|void
name|cleanup
parameter_list|()
block|{
name|File
name|custom
init|=
operator|new
name|File
argument_list|(
literal|"target/custom.properties"
argument_list|)
decl_stmt|;
name|FileUtil
operator|.
name|deleteFile
argument_list|(
name|custom
argument_list|)
expr_stmt|;
block|}
DECL|method|startCamel ()
specifier|protected
name|void
name|startCamel
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"skipStartingCamelContext"
argument_list|)
argument_list|)
condition|)
block|{
name|main
operator|=
operator|new
name|Main
argument_list|()
expr_stmt|;
name|main
operator|.
name|setApplicationContextUri
argument_list|(
literal|"META-INF/spring/camel-config.xml"
argument_list|)
expr_stmt|;
name|main
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Skipping starting CamelContext as system property skipStartingCamelContext is set to be true."
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|stopCamel ()
specifier|protected
name|void
name|stopCamel
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|main
operator|!=
literal|null
condition|)
block|{
name|main
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|createCXFClient ()
specifier|protected
specifier|static
name|ReportIncidentEndpoint
name|createCXFClient
parameter_list|()
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
comment|// start camel
name|startCamel
argument_list|()
expr_stmt|;
name|runTest
argument_list|()
expr_stmt|;
comment|// stop camel
name|stopCamel
argument_list|()
expr_stmt|;
block|}
DECL|method|runTest ()
specifier|protected
name|void
name|runTest
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
argument_list|()
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
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"OK;456"
argument_list|,
name|out
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

