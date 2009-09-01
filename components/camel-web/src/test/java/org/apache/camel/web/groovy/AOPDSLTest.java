begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.groovy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|groovy
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
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

begin_comment
comment|/**  *   */
end_comment

begin_class
DECL|class|AOPDSLTest
specifier|public
class|class
name|AOPDSLTest
extends|extends
name|GroovyRendererTestSupport
block|{
annotation|@
name|Test
DECL|method|testAOPAfter ()
specifier|public
name|void
name|testAOPAfter
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"from(\"direct:start\").aop().after(\"mock:after\").transform(constant(\"Bye World\")).to(\"mock:result\")"
decl_stmt|;
name|assertEquals
argument_list|(
name|dsl
argument_list|,
name|render
argument_list|(
name|dsl
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
argument_list|(
literal|"Need to fix this test"
argument_list|)
annotation|@
name|Test
comment|// TODO: fix this test!
DECL|method|fixmeTestAOPAfterFinally ()
specifier|public
name|void
name|fixmeTestAOPAfterFinally
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"from(\"direct:start\").aop().afterFinally(\"mock:after\").choice()"
operator|+
literal|".when(body().isEqualTo(\"Hello World\")).transform(constant(\"Bye World\"))"
operator|+
literal|".otherwise().transform(constant(\"Kabom the World\")).throwException(new IllegalArgumentException(\"Damn\"))"
operator|+
literal|".end().to(\"mock:result\")"
decl_stmt|;
name|assertEquals
argument_list|(
name|dsl
argument_list|,
name|render
argument_list|(
name|dsl
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAOPAround ()
specifier|public
name|void
name|testAOPAround
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"from(\"direct:start\").aop().around(\"mock:before\", \"mock:after\").transform(constant(\"Bye World\")).to(\"mock:result\")"
decl_stmt|;
name|assertEquals
argument_list|(
name|dsl
argument_list|,
name|render
argument_list|(
name|dsl
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
argument_list|(
literal|"Need to fix this test"
argument_list|)
annotation|@
name|Test
comment|// TODO: fix this test!
DECL|method|fixmeTestAOPAroundFinally ()
specifier|public
name|void
name|fixmeTestAOPAroundFinally
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"from(\"direct:start\").aop().aroundFinally(\"mock:before\", \"mock:after\").choice()"
operator|+
literal|".when(body().isEqualTo(\"Hello World\")).transform(constant(\"Bye World\"))"
operator|+
literal|".otherwise().transform(constant(\"Kabom the World\")).throwException(new IllegalArgumentException(\"Damn\"))"
operator|+
literal|".end()to(\"mock:result\")"
decl_stmt|;
name|assertEquals
argument_list|(
name|dsl
argument_list|,
name|render
argument_list|(
name|dsl
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAOPBefore ()
specifier|public
name|void
name|testAOPBefore
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"from(\"direct:start\").aop().before(\"mock:before\").transform(constant(\"Bye World\")).to(\"mock:result\")"
decl_stmt|;
name|assertEquals
argument_list|(
name|dsl
argument_list|,
name|render
argument_list|(
name|dsl
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAOPNestedRoute ()
specifier|public
name|void
name|testAOPNestedRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"from(\"direct:start\").to(\"mock:start\").aop().around(\"mock:before\", \"mock:after\")"
operator|+
literal|".transform(constant(\"Bye\")).to(\"mock:middle\").transform(body().append(\" World\")).end().transform(body().prepend(\"Bye \")).to(\"mock:result\")"
decl_stmt|;
name|String
name|expected
init|=
literal|"from(\"direct:start\").to(\"mock:start\").aop().around(\"mock:before\", \"mock:after\")"
operator|+
literal|".transform(constant(\"Bye\")).to(\"mock:middle\").transform(body().append(\" World\")).transform(body().prepend(\"Bye \")).to(\"mock:result\")"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|render
argument_list|(
name|dsl
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

