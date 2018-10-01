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
name|CamelContext
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
name|ConsumerTemplate
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
name|Exchange
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
DECL|class|DefaultConsumerTemplateWithCustomCacheMaxSizeTest
specifier|public
class|class
name|DefaultConsumerTemplateWithCustomCacheMaxSizeTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|getGlobalOptions
argument_list|()
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|MAXIMUM_CACHE_POOL_SIZE
argument_list|,
literal|"200"
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testCacheConsumers ()
specifier|public
name|void
name|testCacheConsumers
parameter_list|()
throws|throws
name|Exception
block|{
name|ConsumerTemplate
name|template
init|=
name|context
operator|.
name|createConsumerTemplate
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Size should be 0"
argument_list|,
literal|0
argument_list|,
name|template
operator|.
name|getCurrentCacheSize
argument_list|()
argument_list|)
expr_stmt|;
comment|// test that we cache at most 500 producers to avoid it eating to much memory
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|203
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
name|template
operator|.
name|receiveNoWait
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|// the eviction is async so force cleanup
name|template
operator|.
name|cleanUp
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Size should be 200"
argument_list|,
literal|200
argument_list|,
name|template
operator|.
name|getCurrentCacheSize
argument_list|()
argument_list|)
expr_stmt|;
name|template
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// should be 0
name|assertEquals
argument_list|(
literal|"Size should be 0"
argument_list|,
literal|0
argument_list|,
name|template
operator|.
name|getCurrentCacheSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvalidSizeABC ()
specifier|public
name|void
name|testInvalidSizeABC
parameter_list|()
block|{
name|context
operator|.
name|getGlobalOptions
argument_list|()
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|MAXIMUM_CACHE_POOL_SIZE
argument_list|,
literal|"ABC"
argument_list|)
expr_stmt|;
try|try
block|{
name|context
operator|.
name|createConsumerTemplate
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Property CamelMaximumCachePoolSize must be a positive number, was: ABC"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testInvalidSizeZero ()
specifier|public
name|void
name|testInvalidSizeZero
parameter_list|()
block|{
name|context
operator|.
name|getGlobalOptions
argument_list|()
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|MAXIMUM_CACHE_POOL_SIZE
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
try|try
block|{
name|context
operator|.
name|createConsumerTemplate
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Property CamelMaximumCachePoolSize must be a positive number, was: 0"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

