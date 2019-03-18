begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.opentracing
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|opentracing
package|;
end_package

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|Span
import|;
end_import

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|mock
operator|.
name|MockTracer
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
name|test
operator|.
name|junit4
operator|.
name|ExchangeTestSupport
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
DECL|class|ActiveSpanManagerTest
specifier|public
class|class
name|ActiveSpanManagerTest
extends|extends
name|ExchangeTestSupport
block|{
DECL|field|tracer
specifier|private
name|MockTracer
name|tracer
init|=
operator|new
name|MockTracer
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testNoSpan ()
specifier|public
name|void
name|testNoSpan
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|ActiveSpanManager
operator|.
name|getSpan
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCurrentSpan ()
specifier|public
name|void
name|testCurrentSpan
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|()
decl_stmt|;
name|Span
name|span
init|=
name|tracer
operator|.
name|buildSpan
argument_list|(
literal|"test"
argument_list|)
operator|.
name|start
argument_list|()
decl_stmt|;
name|ActiveSpanManager
operator|.
name|activate
argument_list|(
name|exchange
argument_list|,
name|span
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|span
argument_list|,
name|ActiveSpanManager
operator|.
name|getSpan
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|ActiveSpanManager
operator|.
name|deactivate
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|ActiveSpanManager
operator|.
name|getSpan
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreateChild ()
specifier|public
name|void
name|testCreateChild
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|()
decl_stmt|;
name|Span
name|parent
init|=
name|tracer
operator|.
name|buildSpan
argument_list|(
literal|"parent"
argument_list|)
operator|.
name|start
argument_list|()
decl_stmt|;
name|ActiveSpanManager
operator|.
name|activate
argument_list|(
name|exchange
argument_list|,
name|parent
argument_list|)
expr_stmt|;
name|Span
name|child
init|=
name|tracer
operator|.
name|buildSpan
argument_list|(
literal|"child"
argument_list|)
operator|.
name|start
argument_list|()
decl_stmt|;
name|ActiveSpanManager
operator|.
name|activate
argument_list|(
name|exchange
argument_list|,
name|child
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|child
argument_list|,
name|ActiveSpanManager
operator|.
name|getSpan
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|ActiveSpanManager
operator|.
name|deactivate
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|parent
argument_list|,
name|ActiveSpanManager
operator|.
name|getSpan
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIsolatedConcurrentExchanges ()
specifier|public
name|void
name|testIsolatedConcurrentExchanges
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|()
decl_stmt|;
name|Span
name|parent
init|=
name|tracer
operator|.
name|buildSpan
argument_list|(
literal|"parent"
argument_list|)
operator|.
name|start
argument_list|()
decl_stmt|;
name|ActiveSpanManager
operator|.
name|activate
argument_list|(
name|exchange
argument_list|,
name|parent
argument_list|)
expr_stmt|;
name|Exchange
name|path1
init|=
name|exchange
operator|.
name|copy
argument_list|()
decl_stmt|;
name|Exchange
name|path2
init|=
name|exchange
operator|.
name|copy
argument_list|()
decl_stmt|;
comment|// Check the parent span is available in the new exchanges
name|assertEquals
argument_list|(
name|parent
argument_list|,
name|ActiveSpanManager
operator|.
name|getSpan
argument_list|(
name|path1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|parent
argument_list|,
name|ActiveSpanManager
operator|.
name|getSpan
argument_list|(
name|path2
argument_list|)
argument_list|)
expr_stmt|;
name|Span
name|child1
init|=
name|tracer
operator|.
name|buildSpan
argument_list|(
literal|"child1"
argument_list|)
operator|.
name|start
argument_list|()
decl_stmt|;
name|ActiveSpanManager
operator|.
name|activate
argument_list|(
name|path1
argument_list|,
name|child1
argument_list|)
expr_stmt|;
name|Span
name|child2
init|=
name|tracer
operator|.
name|buildSpan
argument_list|(
literal|"child2"
argument_list|)
operator|.
name|start
argument_list|()
decl_stmt|;
name|ActiveSpanManager
operator|.
name|activate
argument_list|(
name|path2
argument_list|,
name|child2
argument_list|)
expr_stmt|;
name|ActiveSpanManager
operator|.
name|deactivate
argument_list|(
name|path2
argument_list|)
expr_stmt|;
comment|// Check that the current span in path2 is back to parent
comment|// and hasn't been affected by path1 creating its own child
name|ActiveSpanManager
operator|.
name|activate
argument_list|(
name|path2
argument_list|,
name|parent
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

