begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.batch.support
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
name|batch
operator|.
name|support
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
name|CamelTestSupport
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|junit
operator|.
name|MockitoJUnitRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|batch
operator|.
name|core
operator|.
name|JobExecution
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|CamelJobExecutionListenerTest
specifier|public
class|class
name|CamelJobExecutionListenerTest
extends|extends
name|CamelTestSupport
block|{
comment|// Fixtures
annotation|@
name|Mock
DECL|field|jobExecution
name|JobExecution
name|jobExecution
decl_stmt|;
DECL|field|jobExecutionListener
name|CamelJobExecutionListener
name|jobExecutionListener
decl_stmt|;
comment|// Camel fixtures
annotation|@
name|Override
DECL|method|doPostSetup ()
specifier|protected
name|void
name|doPostSetup
parameter_list|()
throws|throws
name|Exception
block|{
name|jobExecutionListener
operator|=
operator|new
name|CamelJobExecutionListener
argument_list|(
name|template
argument_list|()
argument_list|,
literal|"seda:eventQueue"
argument_list|)
expr_stmt|;
block|}
comment|// Tests
annotation|@
name|Test
DECL|method|shouldSendBeforeJobEvent ()
specifier|public
name|void
name|shouldSendBeforeJobEvent
parameter_list|()
throws|throws
name|Exception
block|{
comment|// When
name|jobExecutionListener
operator|.
name|beforeJob
argument_list|(
name|jobExecution
argument_list|)
expr_stmt|;
comment|// Then
name|assertEquals
argument_list|(
name|jobExecution
argument_list|,
name|consumer
argument_list|()
operator|.
name|receiveBody
argument_list|(
literal|"seda:eventQueue"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSetBeforeJobEventHeader ()
specifier|public
name|void
name|shouldSetBeforeJobEventHeader
parameter_list|()
throws|throws
name|Exception
block|{
comment|// When
name|jobExecutionListener
operator|.
name|beforeJob
argument_list|(
name|jobExecution
argument_list|)
expr_stmt|;
comment|// Then
name|Exchange
name|beforeJobEvent
init|=
name|consumer
argument_list|()
operator|.
name|receive
argument_list|(
literal|"seda:eventQueue"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|CamelJobExecutionListener
operator|.
name|EventType
operator|.
name|BEFORE
operator|.
name|name
argument_list|()
argument_list|,
name|beforeJobEvent
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CamelJobExecutionListener
operator|.
name|EventType
operator|.
name|HEADER_KEY
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSendAfterJobEvent ()
specifier|public
name|void
name|shouldSendAfterJobEvent
parameter_list|()
throws|throws
name|Exception
block|{
comment|// When
name|jobExecutionListener
operator|.
name|afterJob
argument_list|(
name|jobExecution
argument_list|)
expr_stmt|;
comment|// Then
name|assertEquals
argument_list|(
name|jobExecution
argument_list|,
name|consumer
argument_list|()
operator|.
name|receiveBody
argument_list|(
literal|"seda:eventQueue"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSetAfterJobEventHeader ()
specifier|public
name|void
name|shouldSetAfterJobEventHeader
parameter_list|()
throws|throws
name|Exception
block|{
comment|// When
name|jobExecutionListener
operator|.
name|afterJob
argument_list|(
name|jobExecution
argument_list|)
expr_stmt|;
comment|// Then
name|Exchange
name|beforeJobEvent
init|=
name|consumer
argument_list|()
operator|.
name|receive
argument_list|(
literal|"seda:eventQueue"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|CamelJobExecutionListener
operator|.
name|EventType
operator|.
name|AFTER
operator|.
name|name
argument_list|()
argument_list|,
name|beforeJobEvent
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CamelJobExecutionListener
operator|.
name|EventType
operator|.
name|HEADER_KEY
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

