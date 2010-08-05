begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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

begin_class
DECL|class|SimpleUuidGeneratorTest
specifier|public
class|class
name|SimpleUuidGeneratorTest
extends|extends
name|TestCase
block|{
DECL|field|uuidGenerator
specifier|private
name|SimpleUuidGenerator
name|uuidGenerator
decl_stmt|;
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|uuidGenerator
operator|=
operator|new
name|SimpleUuidGenerator
argument_list|()
expr_stmt|;
block|}
DECL|method|testGenerateUUID ()
specifier|public
name|void
name|testGenerateUUID
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|uuidGenerator
operator|.
name|generateUuid
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2"
argument_list|,
name|uuidGenerator
operator|.
name|generateUuid
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

