begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.script
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|script
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
name|junit4
operator|.
name|ExchangeTestSupport
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
comment|/**  *  */
end_comment

begin_class
DECL|class|ScriptBuilderTest
specifier|public
class|class
name|ScriptBuilderTest
extends|extends
name|ExchangeTestSupport
block|{
annotation|@
name|Test
DECL|method|testScriptBuilderClasspath ()
specifier|public
name|void
name|testScriptBuilderClasspath
parameter_list|()
throws|throws
name|Exception
block|{
name|ScriptBuilder
name|builder
init|=
name|ScriptBuilder
operator|.
name|groovy
argument_list|(
literal|"classpath:org/apache/camel/builder/script/example/mygroovy.txt"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|boolean
name|matches
init|=
name|builder
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should match"
argument_list|,
literal|true
argument_list|,
name|matches
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|matches
operator|=
name|builder
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should match"
argument_list|,
literal|false
argument_list|,
name|matches
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testScriptBuilderText ()
specifier|public
name|void
name|testScriptBuilderText
parameter_list|()
throws|throws
name|Exception
block|{
name|ScriptBuilder
name|builder
init|=
name|ScriptBuilder
operator|.
name|groovy
argument_list|(
literal|"request.body == 'foo'"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|boolean
name|matches
init|=
name|builder
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should match"
argument_list|,
literal|true
argument_list|,
name|matches
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|matches
operator|=
name|builder
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should match"
argument_list|,
literal|false
argument_list|,
name|matches
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

