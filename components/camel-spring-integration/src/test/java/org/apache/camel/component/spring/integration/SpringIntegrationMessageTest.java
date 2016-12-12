begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.integration
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|integration
package|;
end_package

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
name|messaging
operator|.
name|support
operator|.
name|MessageBuilder
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
DECL|class|SpringIntegrationMessageTest
specifier|public
class|class
name|SpringIntegrationMessageTest
block|{
annotation|@
name|Test
DECL|method|testCopyFrom ()
specifier|public
name|void
name|testCopyFrom
parameter_list|()
block|{
name|org
operator|.
name|springframework
operator|.
name|messaging
operator|.
name|Message
name|testSpringMessage
init|=
name|MessageBuilder
operator|.
name|withPayload
argument_list|(
literal|"Test"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"header1"
argument_list|,
literal|"value1"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"header2"
argument_list|,
literal|"value2"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|SpringIntegrationMessage
name|original
init|=
operator|new
name|SpringIntegrationMessage
argument_list|(
name|testSpringMessage
argument_list|)
decl_stmt|;
name|SpringIntegrationMessage
name|copy
init|=
operator|new
name|SpringIntegrationMessage
argument_list|(
name|testSpringMessage
argument_list|)
decl_stmt|;
name|copy
operator|.
name|copyFrom
argument_list|(
name|original
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|copy
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"header1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|copy
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"header2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

