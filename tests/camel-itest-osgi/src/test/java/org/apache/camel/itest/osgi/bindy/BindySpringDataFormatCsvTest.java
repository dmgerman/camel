begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.bindy
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
name|bindy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

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
name|OSGiIntegrationSpringTestSupport
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
name|OptionUtils
operator|.
name|combine
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4TestRunner
operator|.
name|class
argument_list|)
DECL|class|BindySpringDataFormatCsvTest
specifier|public
class|class
name|BindySpringDataFormatCsvTest
extends|extends
name|OSGiIntegrationSpringTestSupport
block|{
DECL|field|FIXED_DATA
specifier|private
specifier|static
specifier|final
name|String
name|FIXED_DATA
init|=
literal|"Joe,Smith,Developer,75000,10012009"
operator|+
literal|"\n"
operator|+
literal|"Jane,Doe,Architect,80000,01152008"
operator|+
literal|"\n"
operator|+
literal|"Jon,Anderson,Manager,85000,03182007"
operator|+
literal|"\n"
decl_stmt|;
annotation|@
name|Test
DECL|method|testMarshal ()
specifier|public
name|void
name|testMarshal
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Employee
argument_list|>
name|employees
init|=
name|getEmployees
argument_list|()
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:bindy-marshal"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|FIXED_DATA
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:marshal"
argument_list|,
name|employees
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnmarshal ()
specifier|public
name|void
name|testUnmarshal
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Employee
argument_list|>
name|employees
init|=
name|getEmployees
argument_list|()
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:bindy-unmarshal"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|employees
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshal"
argument_list|,
name|FIXED_DATA
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
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
literal|"org/apache/camel/itest/osgi/bindy/BindySpringDataFormatCsvTest.xml"
block|}
argument_list|)
return|;
block|}
DECL|method|getEmployees ()
specifier|private
name|List
argument_list|<
name|Employee
argument_list|>
name|getEmployees
parameter_list|()
throws|throws
name|ParseException
block|{
name|List
argument_list|<
name|Employee
argument_list|>
name|employees
init|=
operator|new
name|ArrayList
argument_list|<
name|Employee
argument_list|>
argument_list|()
decl_stmt|;
name|Employee
name|one
init|=
operator|new
name|Employee
argument_list|()
decl_stmt|;
name|one
operator|.
name|setFirstName
argument_list|(
literal|"Joe"
argument_list|)
expr_stmt|;
name|one
operator|.
name|setLastName
argument_list|(
literal|"Smith"
argument_list|)
expr_stmt|;
name|one
operator|.
name|setTitle
argument_list|(
literal|"Developer"
argument_list|)
expr_stmt|;
name|one
operator|.
name|setSalary
argument_list|(
literal|75000
argument_list|)
expr_stmt|;
name|one
operator|.
name|setHireDate
argument_list|(
operator|new
name|SimpleDateFormat
argument_list|(
literal|"MMddyyyy"
argument_list|)
operator|.
name|parse
argument_list|(
literal|"10012009"
argument_list|)
argument_list|)
expr_stmt|;
name|employees
operator|.
name|add
argument_list|(
name|one
argument_list|)
expr_stmt|;
name|Employee
name|two
init|=
operator|new
name|Employee
argument_list|()
decl_stmt|;
name|two
operator|.
name|setFirstName
argument_list|(
literal|"Jane"
argument_list|)
expr_stmt|;
name|two
operator|.
name|setLastName
argument_list|(
literal|"Doe"
argument_list|)
expr_stmt|;
name|two
operator|.
name|setTitle
argument_list|(
literal|"Architect"
argument_list|)
expr_stmt|;
name|two
operator|.
name|setSalary
argument_list|(
literal|80000
argument_list|)
expr_stmt|;
name|two
operator|.
name|setHireDate
argument_list|(
operator|new
name|SimpleDateFormat
argument_list|(
literal|"MMddyyyy"
argument_list|)
operator|.
name|parse
argument_list|(
literal|"01152008"
argument_list|)
argument_list|)
expr_stmt|;
name|employees
operator|.
name|add
argument_list|(
name|two
argument_list|)
expr_stmt|;
name|Employee
name|three
init|=
operator|new
name|Employee
argument_list|()
decl_stmt|;
name|three
operator|.
name|setFirstName
argument_list|(
literal|"Jon"
argument_list|)
expr_stmt|;
name|three
operator|.
name|setLastName
argument_list|(
literal|"Anderson"
argument_list|)
expr_stmt|;
name|three
operator|.
name|setTitle
argument_list|(
literal|"Manager"
argument_list|)
expr_stmt|;
name|three
operator|.
name|setSalary
argument_list|(
literal|85000
argument_list|)
expr_stmt|;
name|three
operator|.
name|setHireDate
argument_list|(
operator|new
name|SimpleDateFormat
argument_list|(
literal|"MMddyyyy"
argument_list|)
operator|.
name|parse
argument_list|(
literal|"03182007"
argument_list|)
argument_list|)
expr_stmt|;
name|employees
operator|.
name|add
argument_list|(
name|three
argument_list|)
expr_stmt|;
return|return
name|employees
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
comment|// using the features to install the other camel components
name|loadCamelFeatures
argument_list|(
literal|"camel-bindy"
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

