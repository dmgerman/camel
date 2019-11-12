begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Exchange
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
name|component
operator|.
name|log
operator|.
name|LogComponent
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
name|component
operator|.
name|log
operator|.
name|LogEndpoint
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|support
operator|.
name|DefaultEndpoint
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
name|support
operator|.
name|LazyStartProducer
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
name|support
operator|.
name|service
operator|.
name|ServiceHelper
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
DECL|class|LazyStartProducerTest
specifier|public
class|class
name|LazyStartProducerTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|testLazyStartProducer ()
specifier|public
name|void
name|testLazyStartProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Lazy Producer"
argument_list|,
literal|"Hello Again Lazy Producer"
argument_list|)
expr_stmt|;
name|LazyStartProducer
name|lazy
init|=
operator|new
name|LazyStartProducer
argument_list|(
name|mock
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|ServiceHelper
operator|.
name|isStarted
argument_list|(
name|lazy
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceHelper
operator|.
name|isStarted
argument_list|(
name|lazy
operator|.
name|getProcessor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|mock
operator|.
name|isSingleton
argument_list|()
argument_list|,
name|lazy
operator|.
name|isSingleton
argument_list|()
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|lazy
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ServiceHelper
operator|.
name|isStarted
argument_list|(
name|lazy
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceHelper
operator|.
name|isStarted
argument_list|(
name|lazy
operator|.
name|getProcessor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|mock
operator|.
name|isSingleton
argument_list|()
argument_list|,
name|lazy
operator|.
name|isSingleton
argument_list|()
argument_list|)
expr_stmt|;
comment|// process a message which should start the delegate
name|Exchange
name|exchange
init|=
name|mock
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello Lazy Producer"
argument_list|)
expr_stmt|;
name|lazy
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ServiceHelper
operator|.
name|isStarted
argument_list|(
name|lazy
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ServiceHelper
operator|.
name|isStarted
argument_list|(
name|lazy
operator|.
name|getProcessor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|mock
operator|.
name|isSingleton
argument_list|()
argument_list|,
name|lazy
operator|.
name|isSingleton
argument_list|()
argument_list|)
expr_stmt|;
comment|// process a message which should start the delegate
name|exchange
operator|=
name|mock
operator|.
name|createExchange
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello Again Lazy Producer"
argument_list|)
expr_stmt|;
name|lazy
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ServiceHelper
operator|.
name|isStarted
argument_list|(
name|lazy
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ServiceHelper
operator|.
name|isStarted
argument_list|(
name|lazy
operator|.
name|getProcessor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|mock
operator|.
name|isSingleton
argument_list|()
argument_list|,
name|lazy
operator|.
name|isSingleton
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|lazyStartProducerGlobal ()
specifier|public
name|void
name|lazyStartProducerGlobal
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getGlobalEndpointConfiguration
argument_list|()
operator|.
name|setLazyStartProducer
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|mock
operator|.
name|isLazyStartProducer
argument_list|()
argument_list|)
expr_stmt|;
name|LogEndpoint
name|log
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"log:foo"
argument_list|,
name|LogEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|log
operator|.
name|isLazyStartProducer
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|lazyStartProducerComponent ()
specifier|public
name|void
name|lazyStartProducerComponent
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getComponent
argument_list|(
literal|"log"
argument_list|,
name|LogComponent
operator|.
name|class
argument_list|)
operator|.
name|setLazyStartProducer
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|LogEndpoint
name|log
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"log:foo"
argument_list|,
name|LogEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|log
operator|.
name|isLazyStartProducer
argument_list|()
argument_list|)
expr_stmt|;
comment|// but mock is false
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|mock
operator|.
name|isLazyStartProducer
argument_list|()
argument_list|)
expr_stmt|;
comment|// but we can override this via parameter
name|MockEndpoint
name|mock2
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:foo?lazyStartProducer=true"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|mock2
operator|.
name|isLazyStartProducer
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

