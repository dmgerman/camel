begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.krati
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|krati
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
DECL|class|KratiProducerSpringTest
specifier|public
class|class
name|KratiProducerSpringTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|Test
DECL|method|testPut ()
specifier|public
name|void
name|testPut
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:results"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:put"
argument_list|,
literal|"TEST1"
argument_list|,
name|KratiConstants
operator|.
name|KEY
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:put"
argument_list|,
literal|"TEST2"
argument_list|,
name|KratiConstants
operator|.
name|KEY
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:put"
argument_list|,
literal|"TEST3"
argument_list|,
name|KratiConstants
operator|.
name|KEY
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPutAndGet ()
specifier|public
name|void
name|testPutAndGet
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:results"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:put"
argument_list|,
literal|"TEST1"
argument_list|,
name|KratiConstants
operator|.
name|KEY
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:put"
argument_list|,
literal|"TEST2"
argument_list|,
name|KratiConstants
operator|.
name|KEY
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:put"
argument_list|,
literal|"TEST3"
argument_list|,
name|KratiConstants
operator|.
name|KEY
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:get"
argument_list|,
literal|null
argument_list|,
name|KratiConstants
operator|.
name|KEY
argument_list|,
literal|"3"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"TEST3"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPutDeleteAndGet ()
specifier|public
name|void
name|testPutDeleteAndGet
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:put"
argument_list|,
literal|"TEST1"
argument_list|,
name|KratiConstants
operator|.
name|KEY
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:put"
argument_list|,
literal|"TEST2"
argument_list|,
name|KratiConstants
operator|.
name|KEY
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:put"
argument_list|,
literal|"TEST3"
argument_list|,
name|KratiConstants
operator|.
name|KEY
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:delete"
argument_list|,
literal|null
argument_list|,
name|KratiConstants
operator|.
name|KEY
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:get"
argument_list|,
literal|null
argument_list|,
name|KratiConstants
operator|.
name|KEY
argument_list|,
literal|"3"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPutDeleteAllAndGet ()
specifier|public
name|void
name|testPutDeleteAllAndGet
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:put"
argument_list|,
literal|"TEST1"
argument_list|,
name|KratiConstants
operator|.
name|KEY
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:put"
argument_list|,
literal|"TEST2"
argument_list|,
name|KratiConstants
operator|.
name|KEY
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:put"
argument_list|,
literal|"TEST3"
argument_list|,
name|KratiConstants
operator|.
name|KEY
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:deleteall"
argument_list|,
literal|null
argument_list|,
name|KratiConstants
operator|.
name|KEY
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:get"
argument_list|,
literal|null
argument_list|,
name|KratiConstants
operator|.
name|KEY
argument_list|,
literal|"1"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|result
operator|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:get"
argument_list|,
literal|null
argument_list|,
name|KratiConstants
operator|.
name|KEY
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|result
operator|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:get"
argument_list|,
literal|null
argument_list|,
name|KratiConstants
operator|.
name|KEY
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
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
literal|"classpath:producer-test.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

