begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|spi
operator|.
name|Synchronization
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|UnitOfWorkTest
specifier|public
class|class
name|UnitOfWorkTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|synchronization
specifier|protected
name|Synchronization
name|synchronization
decl_stmt|;
DECL|field|completed
specifier|protected
name|Exchange
name|completed
decl_stmt|;
DECL|field|failed
specifier|protected
name|Exchange
name|failed
decl_stmt|;
DECL|field|uri
specifier|protected
name|String
name|uri
init|=
literal|"direct:foo"
decl_stmt|;
DECL|method|testSuccess ()
specifier|public
name|void
name|testSuccess
parameter_list|()
throws|throws
name|Exception
block|{
name|sendMessage
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
literal|"Should not have failed"
argument_list|,
name|failed
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have received completed notification"
argument_list|,
name|completed
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Received completed: "
operator|+
name|completed
argument_list|)
expr_stmt|;
block|}
DECL|method|testFail ()
specifier|public
name|void
name|testFail
parameter_list|()
throws|throws
name|Exception
block|{
name|sendMessage
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
literal|"Should not have completed"
argument_list|,
name|completed
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have received failed notification"
argument_list|,
name|failed
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Received fail: "
operator|+
name|failed
argument_list|)
expr_stmt|;
block|}
DECL|method|testException ()
specifier|public
name|void
name|testException
parameter_list|()
throws|throws
name|Exception
block|{
name|sendMessage
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
literal|"Should not have completed"
argument_list|,
name|completed
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have received failed notification"
argument_list|,
name|failed
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Received fail: "
operator|+
name|failed
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|synchronization
operator|=
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
name|completed
operator|=
name|exchange
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
name|failed
operator|=
name|exchange
expr_stmt|;
block|}
block|}
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
DECL|method|sendMessage ()
specifier|protected
name|void
name|sendMessage
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|template
operator|.
name|send
argument_list|(
name|uri
argument_list|,
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|addSynchronization
argument_list|(
name|synchronization
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:async"
argument_list|)
operator|.
name|thread
argument_list|(
literal|1
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
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
name|log
operator|.
name|info
argument_list|(
literal|"Received: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
name|String
name|name
init|=
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
literal|"testFail"
argument_list|)
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Failing test!"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getFault
argument_list|(
literal|true
argument_list|)
operator|.
name|setBody
argument_list|(
literal|"testFail() should always fail with a fault!"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
literal|"testException"
argument_list|)
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Throwing exception!"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Failing test!"
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

