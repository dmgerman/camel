begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.view
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|view
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|RouteDotGeneratorTest
specifier|public
class|class
name|RouteDotGeneratorTest
extends|extends
name|TestCase
block|{
DECL|method|testInsertSpacesBetweenCamelCase ()
specifier|public
name|void
name|testInsertSpacesBetweenCamelCase
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|value
init|=
name|NodeData
operator|.
name|insertSpacesBetweenCamelCase
argument_list|(
literal|"FooBarType"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Converted value"
argument_list|,
literal|"Foo Bar Type"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

