begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
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
name|cxf
operator|.
name|incident
operator|.
name|IncidentService
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
name|cxf
operator|.
name|incident
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
name|cxf
operator|.
name|incident
operator|.
name|InputStatusIncident
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
name|cxf
operator|.
name|incident
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
name|cxf
operator|.
name|incident
operator|.
name|OutputStatusIncident
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
name|frontend
operator|.
name|ClientProxyFactoryBean
import|;
end_import

begin_class
DECL|class|CamelRouteClient
specifier|public
class|class
name|CamelRouteClient
block|{
DECL|field|URL
specifier|private
specifier|static
specifier|final
name|String
name|URL
init|=
literal|"http://localhost:8080/camel-example-cxf-tomcat/webservices/incident"
decl_stmt|;
DECL|method|createCXFClient ()
specifier|protected
specifier|static
name|IncidentService
name|createCXFClient
parameter_list|()
block|{
comment|// we use CXF to create a client for us as its easier than JAXWS and works
name|ClientProxyFactoryBean
name|factory
init|=
operator|new
name|ClientProxyFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|IncidentService
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
name|URL
argument_list|)
expr_stmt|;
return|return
operator|(
name|IncidentService
operator|)
name|factory
operator|.
name|create
argument_list|()
return|;
block|}
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|CamelRouteClient
name|client
init|=
operator|new
name|CamelRouteClient
argument_list|()
decl_stmt|;
name|client
operator|.
name|runTest
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
name|IncidentService
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|out
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|InputStatusIncident
name|inStatus
init|=
operator|new
name|InputStatusIncident
argument_list|()
decl_stmt|;
name|inStatus
operator|.
name|setIncidentId
argument_list|(
literal|"456"
argument_list|)
expr_stmt|;
name|OutputStatusIncident
name|outStatus
init|=
name|client
operator|.
name|statusIncident
argument_list|(
name|inStatus
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|outStatus
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

