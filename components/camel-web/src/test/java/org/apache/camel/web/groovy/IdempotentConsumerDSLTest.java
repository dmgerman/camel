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
name|Test
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
DECL|class|IdempotentConsumerDSLTest
specifier|public
class|class
name|IdempotentConsumerDSLTest
extends|extends
name|GroovyRendererTestSupport
block|{
annotation|@
name|Test
DECL|method|testIdempotentConsumerAsync ()
specifier|public
name|void
name|testIdempotentConsumerAsync
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"from(\"direct:start\").idempotentConsumer(header(\"messageId\"), MemoryIdempotentRepository.memoryIdempotentRepository()).threads().to(\"mock:result\")"
decl_stmt|;
name|String
index|[]
name|importClasses
init|=
operator|new
name|String
index|[]
block|{
literal|"import org.apache.camel.processor.idempotent.*"
block|}
decl_stmt|;
name|assertEquals
argument_list|(
name|dsl
argument_list|,
name|render
argument_list|(
name|dsl
argument_list|,
name|importClasses
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIdempotentConsumerAsyncWithCacheSize ()
specifier|public
name|void
name|testIdempotentConsumerAsyncWithCacheSize
parameter_list|()
throws|throws
name|Exception
block|{
comment|// drop the cache size
name|String
name|dsl
init|=
literal|"from(\"direct:start\").idempotentConsumer(header(\"messageId\"), MemoryIdempotentRepository.memoryIdempotentRepository(200)).threads().to(\"mock:result\")"
decl_stmt|;
name|String
name|expected
init|=
literal|"from(\"direct:start\").idempotentConsumer(header(\"messageId\"), MemoryIdempotentRepository.memoryIdempotentRepository()).threads().to(\"mock:result\")"
decl_stmt|;
name|String
index|[]
name|importClasses
init|=
operator|new
name|String
index|[]
block|{
literal|"import org.apache.camel.processor.idempotent.*"
block|}
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|render
argument_list|(
name|dsl
argument_list|,
name|importClasses
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIdempotentConsumerEager ()
specifier|public
name|void
name|testIdempotentConsumerEager
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"from(\"direct:start\").idempotentConsumer(header(\"messageId\"), MemoryIdempotentRepository.memoryIdempotentRepository(200)).eager(false).to(\"mock:result\")"
decl_stmt|;
name|String
name|expected
init|=
literal|"from(\"direct:start\").idempotentConsumer(header(\"messageId\"), MemoryIdempotentRepository.memoryIdempotentRepository()).eager(false).to(\"mock:result\")"
decl_stmt|;
name|String
index|[]
name|importClasses
init|=
operator|new
name|String
index|[]
block|{
literal|"import org.apache.camel.processor.idempotent.*"
block|}
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|render
argument_list|(
name|dsl
argument_list|,
name|importClasses
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

