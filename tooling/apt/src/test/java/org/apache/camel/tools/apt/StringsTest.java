begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.tools.apt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
operator|.
name|helper
operator|.
name|Strings
operator|.
name|asTitle
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
name|tools
operator|.
name|apt
operator|.
name|helper
operator|.
name|Strings
operator|.
name|between
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
name|assertEquals
import|;
end_import

begin_class
DECL|class|StringsTest
specifier|public
class|class
name|StringsTest
block|{
annotation|@
name|Test
DECL|method|testBetween ()
specifier|public
name|void
name|testBetween
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"org.apache.camel.model.OnCompletionDefinition"
argument_list|,
name|between
argument_list|(
literal|"java.util.List<org.apache.camel.model.OnCompletionDefinition>"
argument_list|,
literal|"<"
argument_list|,
literal|">"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAsTitle ()
specifier|public
name|void
name|testAsTitle
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"Broker URL"
argument_list|,
name|asTitle
argument_list|(
literal|"brokerURL"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expose All Queues"
argument_list|,
name|asTitle
argument_list|(
literal|"exposeAllQueues"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Reply To Concurrent Consumers"
argument_list|,
name|asTitle
argument_list|(
literal|"replyToConcurrentConsumers"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

