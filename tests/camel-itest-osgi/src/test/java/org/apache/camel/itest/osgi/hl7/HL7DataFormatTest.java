begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.hl7
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
name|hl7
package|;
end_package

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|hl7v2
operator|.
name|HL7Exception
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|hl7v2
operator|.
name|model
operator|.
name|DataTypeException
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
name|CamelExecutionException
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
name|itest
operator|.
name|osgi
operator|.
name|OSGiIntegrationTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|testing
operator|.
name|Helper
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
name|junit
operator|.
name|JUnit4TestRunner
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
name|equinox
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
name|felix
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
name|container
operator|.
name|def
operator|.
name|PaxRunnerOptions
operator|.
name|scanFeatures
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
name|container
operator|.
name|def
operator|.
name|PaxRunnerOptions
operator|.
name|workingDirectory
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4TestRunner
operator|.
name|class
argument_list|)
DECL|class|HL7DataFormatTest
specifier|public
class|class
name|HL7DataFormatTest
extends|extends
name|OSGiIntegrationTestSupport
block|{
annotation|@
name|Test
DECL|method|testUnmarshalFailed ()
specifier|public
name|void
name|testUnmarshalFailed
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:unmarshal"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|String
name|body
init|=
name|createHL7AsString
argument_list|()
decl_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshalFailed"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|assertIsInstanceOf
argument_list|(
name|HL7Exception
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|DataTypeException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be a validation error message"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Failed validation rule"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnmarshalOk ()
specifier|public
name|void
name|testUnmarshalOk
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:unmarshal"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|String
name|body
init|=
name|createHL7AsString
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshalOk"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
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
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:unmarshalFailed"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|hl7
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:unmarshal"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:unmarshalOk"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|hl7
argument_list|(
literal|false
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unmarshal"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|createHL7AsString ()
specifier|private
specifier|static
name|String
name|createHL7AsString
parameter_list|()
block|{
name|String
name|line1
init|=
literal|"MSH|^~\\&|REQUESTING|ICE|INHOUSE|RTH00|20080808093202||ORM^O01|0808080932027444985|P|2.4|||AL|NE|||"
decl_stmt|;
name|String
name|line2
init|=
literal|"PID|1||ICE999999^^^ICE^ICE||Testpatient^Testy^^^Mr||19740401|M|||123 Barrel Drive^^^^SW18 4RT|||||2||||||||||||||"
decl_stmt|;
name|String
name|line3
init|=
literal|"NTE|1||Free text for entering clinical details|"
decl_stmt|;
name|String
name|line4
init|=
literal|"PV1|1||^^^^^^^^Admin Location|||||||||||||||NHS|"
decl_stmt|;
name|String
name|line5
init|=
literal|"ORC|NW|213||175|REQ||||20080808093202|ahsl^^Administrator||G999999^TestDoctor^GPtests^^^^^^NAT|^^^^^^^^Admin Location | 819600|200808080932||RTH00||ahsl^^Administrator||"
decl_stmt|;
name|String
name|line6
init|=
literal|"OBR|1|213||CCOR^Serum Cortisol ^ JRH06|||200808080932||0.100||||||^|G999999^TestDoctor^GPtests^^^^^^NAT|819600|ADM162||||||820|||^^^^^R||||||||"
decl_stmt|;
name|String
name|line7
init|=
literal|"OBR|2|213||GCU^Serum Copper ^ JRH06 |||200808080932||0.100||||||^|G999999^TestDoctor^GPtests^^^^^^NAT|819600|ADM162||||||820|||^^^^^R||||||||"
decl_stmt|;
name|String
name|line8
init|=
literal|"OBR|3|213||THYG^Serum Thyroglobulin ^JRH06|||200808080932||0.100||||||^|G999999^TestDoctor^GPtests^^^^^^NAT|819600|ADM162||||||820|||^^^^^R||||||||"
decl_stmt|;
name|StringBuilder
name|body
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|body
operator|.
name|append
argument_list|(
name|line1
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
name|line2
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
name|line3
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
name|line4
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
name|line5
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
name|line6
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
name|line7
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
name|line8
argument_list|)
expr_stmt|;
return|return
name|body
operator|.
name|toString
argument_list|()
return|;
block|}
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
comment|// Default karaf environment
name|Helper
operator|.
name|getDefaultOptions
argument_list|(
comment|// this is how you set the default log level when using pax logging (logProfile)
name|Helper
operator|.
name|setLogLevel
argument_list|(
literal|"WARN"
argument_list|)
argument_list|)
argument_list|,
comment|// using the features to install the camel components
name|scanFeatures
argument_list|(
name|getCamelKarafFeatureUrl
argument_list|()
argument_list|,
literal|"camel-core"
argument_list|,
literal|"camel-spring"
argument_list|,
literal|"camel-test"
argument_list|,
literal|"camel-hl7"
argument_list|)
argument_list|,
name|workingDirectory
argument_list|(
literal|"target/paxrunner/"
argument_list|)
argument_list|,
name|felix
argument_list|()
argument_list|,
name|equinox
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|options
return|;
block|}
block|}
end_class

end_unit

