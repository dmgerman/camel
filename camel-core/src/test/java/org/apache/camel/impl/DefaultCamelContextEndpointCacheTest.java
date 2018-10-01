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
name|java
operator|.
name|util
operator|.
name|Collection
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
name|Consumer
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
name|ContextTestSupport
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
name|Endpoint
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
name|Processor
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
name|Producer
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

begin_class
DECL|class|DefaultCamelContextEndpointCacheTest
specifier|public
class|class
name|DefaultCamelContextEndpointCacheTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testCacheEndpoints ()
specifier|public
name|void
name|testCacheEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
comment|// test that we cache at most 1000 endpoints in camel context to avoid it eating to much memory
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|1234
condition|;
name|i
operator|++
control|)
block|{
name|String
name|uri
init|=
literal|"my:endpoint?id="
operator|+
name|i
decl_stmt|;
name|DefaultEndpoint
name|e
init|=
operator|new
name|DefaultEndpoint
argument_list|()
block|{
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
name|e
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|e
operator|.
name|setEndpointUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|context
operator|.
name|addEndpoint
argument_list|(
name|uri
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
comment|// the eviction is async so force cleanup
name|context
operator|.
name|getEndpointRegistry
argument_list|()
operator|.
name|cleanUp
argument_list|()
expr_stmt|;
name|Collection
argument_list|<
name|Endpoint
argument_list|>
name|col
init|=
name|context
operator|.
name|getEndpoints
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Size should be 1000"
argument_list|,
literal|1000
argument_list|,
name|col
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

