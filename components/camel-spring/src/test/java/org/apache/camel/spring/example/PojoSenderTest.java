begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.example
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|example
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
name|spring
operator|.
name|SpringTestSupport
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
name|AbstractXmlApplicationContext
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
import|import static
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
operator|.
name|assertIsSatisfied
import|;
end_import

begin_class
DECL|class|PojoSenderTest
specifier|public
class|class
name|PojoSenderTest
extends|extends
name|SpringTestSupport
block|{
DECL|field|matchedEndpoint
specifier|protected
name|MockEndpoint
name|matchedEndpoint
decl_stmt|;
DECL|field|notMatchedEndpoint
specifier|protected
name|MockEndpoint
name|notMatchedEndpoint
decl_stmt|;
DECL|field|mySender
specifier|protected
name|MySender
name|mySender
decl_stmt|;
annotation|@
name|Test
DECL|method|testMatchesPredicate ()
specifier|public
name|void
name|testMatchesPredicate
parameter_list|()
throws|throws
name|Exception
block|{
name|matchedEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|notMatchedEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|mySender
operator|.
name|doSomething
argument_list|(
literal|"James"
argument_list|)
expr_stmt|;
name|assertIsSatisfied
argument_list|(
name|matchedEndpoint
argument_list|,
name|notMatchedEndpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDoesNotMatchPredicate ()
specifier|public
name|void
name|testDoesNotMatchPredicate
parameter_list|()
throws|throws
name|Exception
block|{
name|matchedEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|notMatchedEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mySender
operator|.
name|doSomething
argument_list|(
literal|"Rob"
argument_list|)
expr_stmt|;
name|assertIsSatisfied
argument_list|(
name|matchedEndpoint
argument_list|,
name|notMatchedEndpoint
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
name|matchedEndpoint
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
expr_stmt|;
name|notMatchedEndpoint
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
expr_stmt|;
name|mySender
operator|=
name|getMandatoryBean
argument_list|(
name|MySender
operator|.
name|class
argument_list|,
literal|"mySender"
argument_list|)
expr_stmt|;
block|}
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/example/pojoSender.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

