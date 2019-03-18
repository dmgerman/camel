begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hl7
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
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
name|model
operator|.
name|Message
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
name|v24
operator|.
name|message
operator|.
name|ADT_A01
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
name|v24
operator|.
name|segment
operator|.
name|PID
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hl7
operator|.
name|HL7
operator|.
name|hl7terser
import|;
end_import

begin_class
DECL|class|TerserExpressionTest
specifier|public
class|class
name|TerserExpressionTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|PATIENT_ID
specifier|private
specifier|static
specifier|final
name|String
name|PATIENT_ID
init|=
literal|"123456"
decl_stmt|;
annotation|@
name|Test
DECL|method|testTerserExpression ()
specifier|public
name|void
name|testTerserExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:test1"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|PATIENT_ID
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:test1"
argument_list|,
name|createADT01Message
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTerserPredicateValue ()
specifier|public
name|void
name|testTerserPredicateValue
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:test2"
argument_list|)
decl_stmt|;
name|mock
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
literal|"direct:test2"
argument_list|,
name|createADT01Message
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTerserPredicateNull ()
specifier|public
name|void
name|testTerserPredicateNull
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:test3"
argument_list|)
decl_stmt|;
name|mock
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
literal|"direct:test3"
argument_list|,
name|createADT01Message
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CamelExecutionException
operator|.
name|class
argument_list|)
DECL|method|testTerserInvalidExpression ()
specifier|public
name|void
name|testTerserInvalidExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:test4"
argument_list|,
name|createADT01Message
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CamelExecutionException
operator|.
name|class
argument_list|)
DECL|method|testTerserInvalidMessage ()
specifier|public
name|void
name|testTerserInvalidMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:test4"
argument_list|,
literal|"text instead of message"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTerserAnnotatedMethod ()
specifier|public
name|void
name|testTerserAnnotatedMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:test5"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|PATIENT_ID
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:test5"
argument_list|,
name|createADT01Message
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
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
specifier|final
name|TerserBean
name|terserBean
init|=
operator|new
name|TerserBean
argument_list|()
decl_stmt|;
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
literal|"direct:test1"
argument_list|)
operator|.
name|transform
argument_list|(
name|hl7terser
argument_list|(
literal|"PID-3-1"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:test1"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:test2"
argument_list|)
operator|.
name|filter
argument_list|(
name|hl7terser
argument_list|(
literal|"PID-3-1"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|PATIENT_ID
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:test2"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:test3"
argument_list|)
operator|.
name|filter
argument_list|(
name|hl7terser
argument_list|(
literal|"PID-4-1"
argument_list|)
operator|.
name|isNull
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:test3"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:test4"
argument_list|)
operator|.
name|filter
argument_list|(
name|hl7terser
argument_list|(
literal|"blorg gablorg"
argument_list|)
operator|.
name|isNull
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:test3"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:test5"
argument_list|)
operator|.
name|bean
argument_list|(
name|terserBean
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:test5"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|createADT01Message ()
specifier|private
specifier|static
name|Message
name|createADT01Message
parameter_list|()
throws|throws
name|Exception
block|{
name|ADT_A01
name|adt
init|=
operator|new
name|ADT_A01
argument_list|()
decl_stmt|;
name|adt
operator|.
name|initQuickstart
argument_list|(
literal|"ADT"
argument_list|,
literal|"A01"
argument_list|,
literal|"P"
argument_list|)
expr_stmt|;
comment|// Populate the PID Segment
name|PID
name|pid
init|=
name|adt
operator|.
name|getPID
argument_list|()
decl_stmt|;
name|pid
operator|.
name|getPatientName
argument_list|(
literal|0
argument_list|)
operator|.
name|getFamilyName
argument_list|()
operator|.
name|getSurname
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"Doe"
argument_list|)
expr_stmt|;
name|pid
operator|.
name|getPatientName
argument_list|(
literal|0
argument_list|)
operator|.
name|getGivenName
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"John"
argument_list|)
expr_stmt|;
name|pid
operator|.
name|getPatientIdentifierList
argument_list|(
literal|0
argument_list|)
operator|.
name|getID
argument_list|()
operator|.
name|setValue
argument_list|(
name|PATIENT_ID
argument_list|)
expr_stmt|;
return|return
name|adt
return|;
block|}
DECL|class|TerserBean
specifier|public
class|class
name|TerserBean
block|{
DECL|method|patientId (@l7Terservalue = R) String patientId)
specifier|public
name|String
name|patientId
parameter_list|(
annotation|@
name|Hl7Terser
argument_list|(
name|value
operator|=
literal|"PID-3-1"
argument_list|)
name|String
name|patientId
parameter_list|)
block|{
return|return
name|patientId
return|;
block|}
block|}
block|}
end_class

end_unit

