begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.seda
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|seda
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
name|builder
operator|.
name|NotifyBuilder
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
name|builder
operator|.
name|RouteBuilder
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
name|spi
operator|.
name|Synchronization
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

begin_comment
comment|/**  * Unit test to verify unit of work with seda. That the UnitOfWork is able to route using seda  * but keeping the same UoW.  */
end_comment

begin_class
DECL|class|SedaUnitOfWorkTest
specifier|public
class|class
name|SedaUnitOfWorkTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|foo
specifier|private
specifier|volatile
name|Object
name|foo
decl_stmt|;
DECL|field|kaboom
specifier|private
specifier|volatile
name|Object
name|kaboom
decl_stmt|;
DECL|field|sync
specifier|private
specifier|volatile
name|String
name|sync
decl_stmt|;
DECL|field|lastOne
specifier|private
specifier|volatile
name|String
name|lastOne
decl_stmt|;
annotation|@
name|Test
DECL|method|testSedaUOW ()
specifier|public
name|void
name|testSedaUOW
parameter_list|()
throws|throws
name|Exception
block|{
name|NotifyBuilder
name|notify
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|whenDone
argument_list|(
literal|2
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
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
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|notify
operator|.
name|matchesMockWaitTime
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"onCompleteA"
argument_list|,
name|sync
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"onCompleteA"
argument_list|,
name|lastOne
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should have propagated the header inside the Synchronization.onComplete() callback"
argument_list|,
literal|"bar"
argument_list|,
name|foo
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSedaUOWWithException ()
specifier|public
name|void
name|testSedaUOWWithException
parameter_list|()
throws|throws
name|Exception
block|{
name|NotifyBuilder
name|notify
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|whenDone
argument_list|(
literal|2
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"kaboom"
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|notify
operator|.
name|matchesMockWaitTime
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"onFailureA"
argument_list|,
name|sync
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"onFailureA"
argument_list|,
name|lastOne
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should have propagated the header inside the Synchronization.onFailure() callback"
argument_list|,
literal|"yes"
argument_list|,
name|kaboom
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|setTracing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyUOWProcessor
argument_list|(
name|SedaUnitOfWorkTest
operator|.
name|this
argument_list|,
literal|"A"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:foo"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|sync
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|lastOne
operator|=
literal|"processor"
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
literal|"yes"
operator|.
name|equals
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"kaboom"
argument_list|)
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"kaboom done!"
argument_list|)
throw|;
block|}
name|lastOne
operator|=
literal|"processor2"
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyUOWProcessor
specifier|private
specifier|static
specifier|final
class|class
name|MyUOWProcessor
implements|implements
name|Processor
block|{
DECL|field|test
specifier|private
name|SedaUnitOfWorkTest
name|test
decl_stmt|;
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|method|MyUOWProcessor (SedaUnitOfWorkTest test, String id)
specifier|private
name|MyUOWProcessor
parameter_list|(
name|SedaUnitOfWorkTest
name|test
parameter_list|,
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|test
operator|=
name|test
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|addSynchronization
argument_list|(
operator|new
name|Synchronization
argument_list|()
block|{
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|test
operator|.
name|sync
operator|=
literal|"onComplete"
operator|+
name|id
expr_stmt|;
name|test
operator|.
name|lastOne
operator|=
name|test
operator|.
name|sync
expr_stmt|;
name|test
operator|.
name|foo
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|test
operator|.
name|sync
operator|=
literal|"onFailure"
operator|+
name|id
expr_stmt|;
name|test
operator|.
name|lastOne
operator|=
name|test
operator|.
name|sync
expr_stmt|;
name|test
operator|.
name|kaboom
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"kaboom"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
