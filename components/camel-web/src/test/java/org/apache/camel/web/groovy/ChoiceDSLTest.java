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

begin_comment
comment|/**  * a test case for choice DSL  */
end_comment

begin_class
DECL|class|ChoiceDSLTest
specifier|public
class|class
name|ChoiceDSLTest
extends|extends
name|GroovyRendererTestSupport
block|{
DECL|method|testChoiceWithMethod ()
specifier|public
name|void
name|testChoiceWithMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"from(\"direct:start\").choice()"
operator|+
literal|".when().method(\"controlBean\", \"isDetour\").to(\"mock:detour\")"
operator|+
literal|".end()"
operator|+
literal|".to(\"mock:result\")"
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
DECL|method|testChoiceWithPredication ()
specifier|public
name|void
name|testChoiceWithPredication
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"from(\"direct:start\").choice()"
operator|+
literal|".when(header(\"username\").isNull()).to(\"mock:god\")"
operator|+
literal|".when(header(\"admin\").isEqualTo(\"true\")).to(\"mock:admin\")"
operator|+
literal|".otherwise().to(\"mock:guest\")"
operator|+
literal|".end()"
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
DECL|method|testChoiceWithoutEnd ()
specifier|public
name|void
name|testChoiceWithoutEnd
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"from(\"direct:start\").split().body().choice()"
operator|+
literal|".when().method(\"orderItemHelper\", \"isWidget\").to(\"bean:widgetInventory\", \"seda:aggregate\")"
operator|+
literal|".otherwise().to(\"bean:gadgetInventory\", \"seda:aggregate\")"
decl_stmt|;
name|String
name|expected
init|=
literal|"from(\"direct:start\").split(body()).choice()"
operator|+
literal|".when().method(\"orderItemHelper\", \"isWidget\").to(\"bean:widgetInventory\").to(\"seda:aggregate\")"
operator|+
literal|".otherwise().to(\"bean:gadgetInventory\").to(\"seda:aggregate\")"
operator|+
literal|".end()"
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

