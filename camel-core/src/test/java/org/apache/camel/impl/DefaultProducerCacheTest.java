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
name|Producer
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultProducerCacheTest
specifier|public
class|class
name|DefaultProducerCacheTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testCacheProducerAcquireAndRelease ()
specifier|public
name|void
name|testCacheProducerAcquireAndRelease
parameter_list|()
throws|throws
name|Exception
block|{
name|ProducerCache
name|cache
init|=
operator|new
name|ProducerCache
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|cache
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Size should be 0"
argument_list|,
literal|0
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// test that we cache at most 1000 producers to avoid it eating to much memory
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|1003
condition|;
name|i
operator|++
control|)
block|{
name|Endpoint
name|e
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:queue:"
operator|+
name|i
argument_list|)
decl_stmt|;
name|Producer
name|p
init|=
name|cache
operator|.
name|acquireProducer
argument_list|(
name|e
argument_list|)
decl_stmt|;
name|cache
operator|.
name|releaseProducer
argument_list|(
name|e
argument_list|,
name|p
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"Size should be 1000"
argument_list|,
literal|1000
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

