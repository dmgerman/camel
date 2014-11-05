begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
operator|.
name|cxf
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
name|example
operator|.
name|reportincident
operator|.
name|InputReportIncident
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
name|example
operator|.
name|reportincident
operator|.
name|OutputReportIncident
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
name|example
operator|.
name|reportincident
operator|.
name|ReportIncidentEndpoint
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
name|itest
operator|.
name|osgi
operator|.
name|OSGiIntegrationSpringTestSupport
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
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|PaxExam
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|tinybundles
operator|.
name|core
operator|.
name|TinyBundles
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|osgi
operator|.
name|context
operator|.
name|support
operator|.
name|OsgiBundleXmlApplicationContext
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|provision
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|OptionUtils
operator|.
name|combine
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|PaxExam
operator|.
name|class
argument_list|)
DECL|class|CxfProxyExampleTest
specifier|public
class|class
name|CxfProxyExampleTest
extends|extends
name|OSGiIntegrationSpringTestSupport
block|{
annotation|@
name|Test
DECL|method|testCxfProxy ()
specifier|public
name|void
name|testCxfProxy
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
literal|"2010-09-28"
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
literal|"12345678"
argument_list|)
expr_stmt|;
comment|// create the webservice client and send the request
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
literal|"http://localhost:9080/camel-itest-osgi/webservices/incident"
argument_list|)
expr_stmt|;
name|ReportIncidentEndpoint
name|client
init|=
name|factory
operator|.
name|create
argument_list|(
name|ReportIncidentEndpoint
operator|.
name|class
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
literal|"OK;456"
argument_list|,
name|out
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
literal|"Finish the testing"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|OsgiBundleXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|OsgiBundleXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"org/apache/camel/itest/osgi/cxf/camel-config.xml"
block|}
argument_list|)
return|;
block|}
comment|// TODO: CxfConsumer should use OSGi http service (no embedded Jetty)
comment|// TODO: Make this test work with OSGi
annotation|@
name|Configuration
DECL|method|configure ()
specifier|public
specifier|static
name|Option
index|[]
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|Option
index|[]
name|options
init|=
name|combine
argument_list|(
name|getDefaultCamelKarafOptions
argument_list|()
argument_list|,
comment|// using the features to install the camel components
name|loadCamelFeatures
argument_list|(
literal|"camel-http"
argument_list|,
literal|"cxf"
argument_list|,
literal|"camel-cxf"
argument_list|)
argument_list|,
comment|// need to install the generated src as the pax-exam doesn't wrap this bundles
name|provision
argument_list|(
name|TinyBundles
operator|.
name|bundle
argument_list|()
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|reportincident
operator|.
name|InputReportIncident
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|reportincident
operator|.
name|OutputReportIncident
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|reportincident
operator|.
name|ReportIncidentEndpoint
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|reportincident
operator|.
name|ReportIncidentEndpointService
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|reportincident
operator|.
name|ObjectFactory
operator|.
name|class
argument_list|)
operator|.
name|set
argument_list|(
literal|"Export-Package"
argument_list|,
literal|"org.apache.camel.example.reportincident"
argument_list|)
operator|.
name|build
argument_list|(
name|TinyBundles
operator|.
name|withBnd
argument_list|()
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|options
return|;
block|}
block|}
end_class

end_unit

