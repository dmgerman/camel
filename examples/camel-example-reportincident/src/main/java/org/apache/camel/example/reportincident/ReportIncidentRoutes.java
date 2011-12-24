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
name|impl
operator|.
name|DefaultCamelContext
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
name|language
operator|.
name|bean
operator|.
name|BeanLanguage
import|;
end_import

begin_comment
comment|/**  * Our routes that we can build using Camel DSL as we extend the RouteBuilder class.  *<p/>  * In the configure method we have all kind of DSL methods we use for expressing our routes.  */
end_comment

begin_class
DECL|class|ReportIncidentRoutes
specifier|public
class|class
name|ReportIncidentRoutes
extends|extends
name|RouteBuilder
block|{
DECL|field|usingServletTransport
specifier|private
name|boolean
name|usingServletTransport
init|=
literal|true
decl_stmt|;
DECL|method|setUsingServletTransport (boolean flag)
specifier|public
name|void
name|setUsingServletTransport
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|usingServletTransport
operator|=
name|flag
expr_stmt|;
block|}
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// webservice response for OK
name|OutputReportIncident
name|ok
init|=
operator|new
name|OutputReportIncident
argument_list|()
decl_stmt|;
name|ok
operator|.
name|setCode
argument_list|(
literal|"0"
argument_list|)
expr_stmt|;
comment|// endpoint to our CXF webservice
comment|// We should use the related path to publish the service, when using the ServletTransport
comment|// so we need to configure set the bus which is configured to use the ServletTransport
name|String
name|cxfEndpointAddress
init|=
literal|"cxf:/incident?bus=#cxf&"
decl_stmt|;
comment|// Using the full http address for stand alone running
if|if
condition|(
operator|!
name|usingServletTransport
condition|)
block|{
name|cxfEndpointAddress
operator|=
literal|"cxf://http://localhost:{{port}}/camel-example-reportincident/webservices/incident?"
expr_stmt|;
block|}
name|String
name|cxfEndpoint
init|=
name|cxfEndpointAddress
operator|+
literal|"serviceClass=org.apache.camel.example.reportincident.ReportIncidentEndpoint"
operator|+
literal|"&wsdlURL=etc/report_incident.wsdl"
decl_stmt|;
comment|// first part from the webservice -> file backup
name|from
argument_list|(
name|cxfEndpoint
argument_list|)
comment|// we need to convert the CXF payload to InputReportIncident that FilenameGenerator and velocity expects
operator|.
name|convertBodyTo
argument_list|(
name|InputReportIncident
operator|.
name|class
argument_list|)
comment|// then set the file name using the FilenameGenerator bean
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|BeanLanguage
operator|.
name|bean
argument_list|(
name|FilenameGenerator
operator|.
name|class
argument_list|,
literal|"generateFilename"
argument_list|)
argument_list|)
comment|// and create the mail body using velocity template
operator|.
name|to
argument_list|(
literal|"velocity:etc/MailBody.vm"
argument_list|)
comment|// and store the file
operator|.
name|to
argument_list|(
literal|"file://target/subfolder"
argument_list|)
comment|// return OK as response
operator|.
name|log
argument_list|(
literal|"Wrote ${file:name} and returning OK response"
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
name|ok
argument_list|)
argument_list|)
expr_stmt|;
comment|// second part from the file backup -> send email
name|from
argument_list|(
literal|"file://target/subfolder"
argument_list|)
comment|// set the subject of the email
operator|.
name|setHeader
argument_list|(
literal|"subject"
argument_list|,
name|constant
argument_list|(
literal|"new incident reported"
argument_list|)
argument_list|)
comment|// send the email
operator|.
name|log
argument_list|(
literal|"Sending email to incident@mycompany.com:\n${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"smtp://someone@localhost?password=secret&to=incident@mycompany.com"
argument_list|)
expr_stmt|;
block|}
DECL|method|main (String args[])
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
name|args
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
name|CamelContext
name|camel
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
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
name|camel
operator|.
name|addRoutes
argument_list|(
name|routes
argument_list|)
expr_stmt|;
name|camel
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

