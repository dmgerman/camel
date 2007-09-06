begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.bam
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|bam
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
name|builder
operator|.
name|RouteBuilder
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
name|builder
operator|.
name|xml
operator|.
name|XPathBuilder
operator|.
name|xpath
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
name|spring
operator|.
name|SpringTestSupport
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
name|util
operator|.
name|Time
operator|.
name|seconds
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|orm
operator|.
name|jpa
operator|.
name|JpaTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|support
operator|.
name|TransactionTemplate
import|;
end_import

begin_comment
comment|/**  * @version $Revision: $  */
end_comment

begin_class
DECL|class|BamRouteTest
specifier|public
class|class
name|BamRouteTest
extends|extends
name|SpringTestSupport
block|{
DECL|field|overdueEndpoint
specifier|protected
name|MockEndpoint
name|overdueEndpoint
decl_stmt|;
DECL|field|errorTimeout
specifier|protected
name|int
name|errorTimeout
init|=
literal|2
decl_stmt|;
DECL|method|testBam ()
specifier|public
name|void
name|testBam
parameter_list|()
throws|throws
name|Exception
block|{
name|overdueEndpoint
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
literal|"direct:a"
argument_list|,
literal|"<hello id='123'>world!</hello>"
argument_list|)
expr_stmt|;
name|overdueEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|createApplicationContext ()
specifier|protected
name|ClassPathXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/bam/spring.xml"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
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
name|overdueEndpoint
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:overdue"
argument_list|)
expr_stmt|;
name|overdueEndpoint
operator|.
name|setDefaulResultWaitMillis
argument_list|(
literal|8000
argument_list|)
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
name|JpaTemplate
name|jpaTemplate
init|=
name|getMandatoryBean
argument_list|(
name|JpaTemplate
operator|.
name|class
argument_list|,
literal|"jpaTemplate"
argument_list|)
decl_stmt|;
name|TransactionTemplate
name|transactionTemplate
init|=
name|getMandatoryBean
argument_list|(
name|TransactionTemplate
operator|.
name|class
argument_list|,
literal|"transactionTemplate"
argument_list|)
decl_stmt|;
comment|// START SNIPPET: example
return|return
operator|new
name|ProcessBuilder
argument_list|(
name|jpaTemplate
argument_list|,
name|transactionTemplate
argument_list|)
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// lets define some activities, correlating on an XPath on the message bodies
name|ActivityBuilder
name|a
init|=
name|activity
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|name
argument_list|(
literal|"a"
argument_list|)
operator|.
name|correlate
argument_list|(
name|xpath
argument_list|(
literal|"/hello/@id"
argument_list|)
argument_list|)
decl_stmt|;
name|ActivityBuilder
name|b
init|=
name|activity
argument_list|(
literal|"direct:b"
argument_list|)
operator|.
name|name
argument_list|(
literal|"b"
argument_list|)
operator|.
name|correlate
argument_list|(
name|xpath
argument_list|(
literal|"/hello/@id"
argument_list|)
argument_list|)
decl_stmt|;
comment|// now lets add some rules
name|b
operator|.
name|starts
argument_list|()
operator|.
name|after
argument_list|(
name|a
operator|.
name|completes
argument_list|()
argument_list|)
operator|.
name|expectWithin
argument_list|(
name|seconds
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|errorIfOver
argument_list|(
name|seconds
argument_list|(
name|errorTimeout
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:overdue"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
comment|// END SNIPPET: example
block|}
annotation|@
name|Override
DECL|method|getExpectedRouteCount ()
specifier|protected
name|int
name|getExpectedRouteCount
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit

