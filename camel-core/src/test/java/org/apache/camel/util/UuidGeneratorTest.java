begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
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
comment|/**  * Unit test for UuidGenerator  */
end_comment

begin_class
DECL|class|UuidGeneratorTest
specifier|public
class|class
name|UuidGeneratorTest
extends|extends
name|TestCase
block|{
DECL|method|testSanitized ()
specifier|public
name|void
name|testSanitized
parameter_list|()
block|{
name|String
name|out
init|=
name|UuidGenerator
operator|.
name|get
argument_list|()
operator|.
name|generateUuid
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should not contain : "
argument_list|,
name|out
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
operator|==
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should not contain . "
argument_list|,
name|out
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
operator|==
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

