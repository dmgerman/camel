begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gson
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gson
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
DECL|class|SpringGsonFieldNamePolicyTest
specifier|public
class|class
name|SpringGsonFieldNamePolicyTest
extends|extends
name|CamelSpringTestSupport
block|{
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
literal|"org/apache/camel/component/gson/SpringGsonFieldNamePolicyTest.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testUnmarshalPojo ()
specifier|public
name|void
name|testUnmarshalPojo
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|json
init|=
literal|"{\"id\":\"123\",\"first_name\":\"Donald\",\"last_name\":\"Duck\"}"
decl_stmt|;
name|PersonPojo
name|pojo
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:backPojo"
argument_list|,
name|json
argument_list|,
name|PersonPojo
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pojo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|pojo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Donald"
argument_list|,
name|pojo
operator|.
name|getFirstName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Duck"
argument_list|,
name|pojo
operator|.
name|getLastName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshalPojo ()
specifier|public
name|void
name|testMarshalPojo
parameter_list|()
throws|throws
name|Exception
block|{
name|PersonPojo
name|pojo
init|=
operator|new
name|PersonPojo
argument_list|()
decl_stmt|;
name|pojo
operator|.
name|setId
argument_list|(
literal|123
argument_list|)
expr_stmt|;
name|pojo
operator|.
name|setFirstName
argument_list|(
literal|"Donald"
argument_list|)
expr_stmt|;
name|pojo
operator|.
name|setLastName
argument_list|(
literal|"Duck"
argument_list|)
expr_stmt|;
name|String
name|expected
init|=
literal|"{\"id\":123,\"first_name\":\"Donald\",\"last_name\":\"Duck\"}"
decl_stmt|;
name|String
name|json
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:inPojo"
argument_list|,
name|pojo
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|json
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

