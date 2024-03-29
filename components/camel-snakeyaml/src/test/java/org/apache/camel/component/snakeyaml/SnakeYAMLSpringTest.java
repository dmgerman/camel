begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.snakeyaml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|snakeyaml
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|annotation
operator|.
name|DirtiesContext
import|;
end_import

begin_class
annotation|@
name|DirtiesContext
DECL|class|SnakeYAMLSpringTest
specifier|public
class|class
name|SnakeYAMLSpringTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|Test
DECL|method|testMarshalAndUnmarshalMap ()
specifier|public
name|void
name|testMarshalAndUnmarshalMap
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|in
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|in
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"Camel"
argument_list|)
expr_stmt|;
name|SnakeYAMLTestHelper
operator|.
name|marshalAndUnmarshal
argument_list|(
name|context
argument_list|()
argument_list|,
name|SnakeYAMLTestHelper
operator|.
name|createTestMap
argument_list|()
argument_list|,
literal|"mock:reverse"
argument_list|,
literal|"direct:in"
argument_list|,
literal|"direct:back"
argument_list|,
literal|"{name: Camel}"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshalAndUnmarshalPojo ()
specifier|public
name|void
name|testMarshalAndUnmarshalPojo
parameter_list|()
throws|throws
name|Exception
block|{
name|SnakeYAMLTestHelper
operator|.
name|marshalAndUnmarshal
argument_list|(
name|context
argument_list|()
argument_list|,
name|SnakeYAMLTestHelper
operator|.
name|createTestPojo
argument_list|()
argument_list|,
literal|"mock:reversePojo"
argument_list|,
literal|"direct:inPojo"
argument_list|,
literal|"direct:backPojo"
argument_list|,
literal|"!!org.apache.camel.component.snakeyaml.model.TestPojo {name: Camel}"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshalAndUnmarshalPojoWithPrettyFlow ()
specifier|public
name|void
name|testMarshalAndUnmarshalPojoWithPrettyFlow
parameter_list|()
throws|throws
name|Exception
block|{
name|SnakeYAMLTestHelper
operator|.
name|marshalAndUnmarshal
argument_list|(
name|context
argument_list|()
argument_list|,
name|SnakeYAMLTestHelper
operator|.
name|createTestPojo
argument_list|()
argument_list|,
literal|"mock:reversePojoWithPrettyFlow"
argument_list|,
literal|"direct:inPojoWithPrettyFlow"
argument_list|,
literal|"direct:backPojoWithPrettyFlow"
argument_list|,
literal|"!!org.apache.camel.component.snakeyaml.model.TestPojo {\n  name: Camel\n}"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
literal|"org/apache/camel/component/snakeyaml/SnakeYAMLSpringTest.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

