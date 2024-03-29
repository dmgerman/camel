begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|Body
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
name|ContextTestSupport
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
DECL|class|ChoiceWhenBeanExpressionWithExceptionTest
specifier|public
class|class
name|ChoiceWhenBeanExpressionWithExceptionTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|gradeA
specifier|private
name|MockEndpoint
name|gradeA
decl_stmt|;
DECL|field|otherGrade
specifier|private
name|MockEndpoint
name|otherGrade
decl_stmt|;
DECL|method|verifyGradeA (String endpointUri)
specifier|protected
name|void
name|verifyGradeA
parameter_list|(
name|String
name|endpointUri
parameter_list|)
throws|throws
name|Exception
block|{
name|gradeA
operator|.
name|reset
argument_list|()
expr_stmt|;
name|otherGrade
operator|.
name|reset
argument_list|()
expr_stmt|;
name|gradeA
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|otherGrade
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|endpointUri
argument_list|,
operator|new
name|Student
argument_list|(
literal|95
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
comment|// expected
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|verifyOtherGrade (String endpointUri)
specifier|public
name|void
name|verifyOtherGrade
parameter_list|(
name|String
name|endpointUri
parameter_list|)
throws|throws
name|Exception
block|{
name|gradeA
operator|.
name|reset
argument_list|()
expr_stmt|;
name|otherGrade
operator|.
name|reset
argument_list|()
expr_stmt|;
name|gradeA
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|otherGrade
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|endpointUri
argument_list|,
operator|new
name|Student
argument_list|(
literal|60
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
comment|// expected
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBeanExpression ()
specifier|public
name|void
name|testBeanExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|verifyGradeA
argument_list|(
literal|"direct:expression"
argument_list|)
expr_stmt|;
name|verifyOtherGrade
argument_list|(
literal|"direct:expression"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMethod ()
specifier|public
name|void
name|testMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|verifyGradeA
argument_list|(
literal|"direct:method"
argument_list|)
expr_stmt|;
name|verifyOtherGrade
argument_list|(
literal|"direct:method"
argument_list|)
expr_stmt|;
block|}
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
name|gradeA
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:gradeA"
argument_list|)
expr_stmt|;
name|otherGrade
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:otherGrade"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
literal|"direct:expression"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|method
argument_list|(
name|MyBean
operator|.
name|class
argument_list|,
literal|"isGradeA"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:gradeA"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:otherGrade"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:method"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|()
operator|.
name|method
argument_list|(
name|MyBean
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:gradeA"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:otherGrade"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyBean
specifier|public
specifier|static
class|class
name|MyBean
block|{
DECL|method|isGradeA (@ody Student student)
specifier|public
name|boolean
name|isGradeA
parameter_list|(
annotation|@
name|Body
name|Student
name|student
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Bean predicated threw exception!"
argument_list|)
throw|;
block|}
block|}
DECL|class|Student
class|class
name|Student
block|{
DECL|field|grade
specifier|private
name|int
name|grade
decl_stmt|;
DECL|method|Student (int grade)
name|Student
parameter_list|(
name|int
name|grade
parameter_list|)
block|{
name|this
operator|.
name|grade
operator|=
name|grade
expr_stmt|;
block|}
DECL|method|getGrade ()
specifier|public
name|int
name|getGrade
parameter_list|()
block|{
return|return
name|grade
return|;
block|}
block|}
block|}
end_class

end_unit

