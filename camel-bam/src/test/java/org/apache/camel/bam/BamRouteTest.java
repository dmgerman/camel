begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|SpringCamelContext
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
name|ApplicationContext
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
DECL|field|body
specifier|protected
name|Object
name|body
init|=
literal|"<hello>world!</hello>"
decl_stmt|;
DECL|field|jpaTemplate
specifier|protected
name|JpaTemplate
name|jpaTemplate
decl_stmt|;
DECL|field|overdueEndpoint
specifier|protected
name|MockEndpoint
name|overdueEndpoint
decl_stmt|;
DECL|field|transactionTemplate
specifier|protected
name|TransactionTemplate
name|transactionTemplate
decl_stmt|;
DECL|method|testRoute ()
specifier|public
name|void
name|testRoute
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
name|body
argument_list|,
literal|"foo"
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
name|overdueEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
literal|5000
argument_list|)
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
name|camelContext
operator|.
name|addRoutes
argument_list|(
name|createRouteBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|overdueEndpoint
operator|=
operator|(
name|MockEndpoint
operator|)
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:overdue"
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
name|jpaTemplate
operator|=
name|getMandatoryBean
argument_list|(
name|JpaTemplate
operator|.
name|class
argument_list|,
literal|"jpaTemplate"
argument_list|)
expr_stmt|;
name|transactionTemplate
operator|=
name|getMandatoryBean
argument_list|(
name|TransactionTemplate
operator|.
name|class
argument_list|,
literal|"transactionTemplate"
argument_list|)
expr_stmt|;
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
name|header
argument_list|(
literal|"foo"
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
name|header
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
decl_stmt|;
name|ActivityBuilder
name|c
init|=
name|activity
argument_list|(
literal|"direct:c"
argument_list|)
operator|.
name|name
argument_list|(
literal|"c"
argument_list|)
operator|.
name|correlate
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
decl_stmt|;
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
literal|2
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:overdue"
argument_list|)
expr_stmt|;
comment|/*         expect(b.starts().after(10).minutes().from(a.starts());             process.activity("direct:a").name("a")                 .correlate(header("foo"))                 .expect(seconds(10)).afterProcess().starts();                 .expectedAfter(10).minutes();                 .errorAfter(30).minutes();           process.activity("direct:b").name("b")                 .correlate(header("foo"))                 .expect(minutes(10)).after("a").completes();           BamBuilder bam = BamBuilder.monitor(this, "direct:a", "direct:b", "direct:c");          bam.process("direct:b",).expectedMesageCount(1)                 .expectedAfter().minutes(10)                 .errorAfter().minutes(30);          bam.expects("direct:c").expectedMesageCount(1)                 .expectedAfter().minutes(10)                 .errorAfter().minutes(30);                  */
block|}
block|}
return|;
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

