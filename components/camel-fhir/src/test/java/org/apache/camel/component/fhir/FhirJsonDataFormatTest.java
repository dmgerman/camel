begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.fhir
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|fhir
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|context
operator|.
name|FhirContext
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|hl7
operator|.
name|fhir
operator|.
name|dstu3
operator|.
name|model
operator|.
name|Address
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
name|Base
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
name|HumanName
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
name|hl7
operator|.
name|fhir
operator|.
name|instance
operator|.
name|model
operator|.
name|api
operator|.
name|IBaseResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
DECL|class|FhirJsonDataFormatTest
specifier|public
class|class
name|FhirJsonDataFormatTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|PATIENT
specifier|private
specifier|static
specifier|final
name|String
name|PATIENT
init|=
literal|"{\"resourceType\":\"Patient\","
operator|+
literal|"\"name\":[{\"family\":\"Holmes\",\"given\":[\"Sherlock\"]}],"
operator|+
literal|"\"address\":[{\"line\":[\"221b Baker St, Marylebone, London NW1 6XE, UK\"]}]}"
decl_stmt|;
DECL|field|mockEndpoint
specifier|private
name|MockEndpoint
name|mockEndpoint
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|mockEndpoint
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|unmarshal ()
specifier|public
name|void
name|unmarshal
parameter_list|()
throws|throws
name|Exception
block|{
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshal"
argument_list|,
name|PATIENT
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|exchange
init|=
name|mockEndpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Patient
name|patient
init|=
operator|(
name|Patient
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Patients should be equal!"
argument_list|,
name|patient
operator|.
name|equalsDeep
argument_list|(
name|getPatient
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|marshal ()
specifier|public
name|void
name|marshal
parameter_list|()
throws|throws
name|Exception
block|{
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Patient
name|patient
init|=
name|getPatient
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:marshal"
argument_list|,
name|patient
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|exchange
init|=
name|mockEndpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|InputStream
name|inputStream
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|IBaseResource
name|iBaseResource
init|=
name|FhirContext
operator|.
name|forDstu3
argument_list|()
operator|.
name|newJsonParser
argument_list|()
operator|.
name|parseResource
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|inputStream
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Patients should be equal!"
argument_list|,
name|patient
operator|.
name|equalsDeep
argument_list|(
operator|(
name|Base
operator|)
name|iBaseResource
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getPatient ()
specifier|private
name|Patient
name|getPatient
parameter_list|()
block|{
name|Patient
name|patient
init|=
operator|new
name|Patient
argument_list|()
decl_stmt|;
name|patient
operator|.
name|addName
argument_list|(
operator|new
name|HumanName
argument_list|()
operator|.
name|addGiven
argument_list|(
literal|"Sherlock"
argument_list|)
operator|.
name|setFamily
argument_list|(
literal|"Holmes"
argument_list|)
argument_list|)
operator|.
name|addAddress
argument_list|(
operator|new
name|Address
argument_list|()
operator|.
name|addLine
argument_list|(
literal|"221b Baker St, Marylebone, London NW1 6XE, UK"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|patient
return|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:marshal"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|fhirJson
argument_list|(
literal|"DSTU3"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:unmarshal"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|fhirJson
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

