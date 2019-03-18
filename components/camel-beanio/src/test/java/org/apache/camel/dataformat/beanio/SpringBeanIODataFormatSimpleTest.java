begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.beanio
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|beanio
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
name|test
operator|.
name|spring
operator|.
name|CamelSpringTestSupport
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
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|SpringBeanIODataFormatSimpleTest
specifier|public
class|class
name|SpringBeanIODataFormatSimpleTest
extends|extends
name|CamelSpringTestSupport
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
name|LS
operator|+
literal|"Jane,Doe,Architect,80000,01152008"
operator|+
name|LS
operator|+
literal|"Jon,Anderson,Manager,85000,03182007"
operator|+
name|LS
decl_stmt|;
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/dataformat/beanio/SpringBeanIODataFormatSimpleTest.xml"
argument_list|)
return|;
block|}
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
literal|"mock:beanio-marshal"
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
literal|"mock:beanio-unmarshal"
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
argument_list|<>
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
block|}
end_class

end_unit

