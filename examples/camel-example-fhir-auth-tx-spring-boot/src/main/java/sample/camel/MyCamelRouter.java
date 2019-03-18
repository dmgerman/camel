begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|sample.camel
package|package
name|sample
operator|.
name|camel
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|LoggingLevel
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
name|http
operator|.
name|ProtocolException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hl7
operator|.
name|fhir
operator|.
name|dstu3
operator|.
name|model
operator|.
name|Patient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Component
import|;
end_import

begin_comment
comment|/**  * A simple Camel route that triggers from a file and pushes to a FHIR server.  *<p/>  * Use<tt>@Component</tt> to make Camel auto detect this route when starting.  */
end_comment

begin_class
annotation|@
name|Component
DECL|class|MyCamelRouter
specifier|public
class|class
name|MyCamelRouter
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"file:{{input}}"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"fhir-example"
argument_list|)
operator|.
name|onException
argument_list|(
name|ProtocolException
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|ERROR
argument_list|,
literal|"Error connecting to FHIR server with URL:{{serverUrl}}, please check the application.properties file ${exception.message}"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|log
argument_list|(
literal|"Converting ${file:name}"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|csv
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
lambda|->
block|{
name|List
argument_list|<
name|Patient
argument_list|>
name|bundle
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|patients
init|=
operator|(
name|List
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
for|for
control|(
name|List
argument_list|<
name|String
argument_list|>
name|patient
range|:
name|patients
control|)
block|{
name|Patient
name|fhirPatient
init|=
operator|new
name|Patient
argument_list|()
decl_stmt|;
name|fhirPatient
operator|.
name|setId
argument_list|(
name|patient
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|fhirPatient
operator|.
name|addName
argument_list|()
operator|.
name|addGiven
argument_list|(
name|patient
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|fhirPatient
operator|.
name|getNameFirstRep
argument_list|()
operator|.
name|setFamily
argument_list|(
name|patient
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|bundle
operator|.
name|add
argument_list|(
name|fhirPatient
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
argument_list|)
comment|// create Patient in our FHIR server
operator|.
name|to
argument_list|(
literal|"fhir://transaction/withResources?inBody=resources&serverUrl={{serverUrl}}&username={{serverUser}}&password={{serverPassword}}&fhirVersion={{fhirVersion}}"
argument_list|)
comment|// log the outcome
operator|.
name|log
argument_list|(
literal|"Patients created successfully: ${body}"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

