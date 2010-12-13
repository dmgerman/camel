begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|impl
operator|.
name|JndiRegistry
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
name|IdempotentRepository
import|;
end_import

begin_comment
comment|/**  * Unit test for the idempotentRepositoryRef option.  */
end_comment

begin_class
DECL|class|FileConsumerIdempotentRefTest
specifier|public
class|class
name|FileConsumerIdempotentRefTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|invoked
specifier|private
specifier|static
specifier|volatile
name|boolean
name|invoked
decl_stmt|;
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myRepo"
argument_list|,
operator|new
name|MyIdempotentRepository
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
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
name|deleteDirectory
argument_list|(
literal|"target/idempotent"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/idempotent/"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"report.txt"
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"file://target/idempotent/?idempotent=true&idempotentRepository=#myRepo&move=done/${file:name}&delay=10"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
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
DECL|method|testIdempotentRef ()
specifier|public
name|void
name|testIdempotentRef
parameter_list|()
throws|throws
name|Exception
block|{
comment|// consume the file the first time
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
literal|"Hello World"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|oneExchangeDone
operator|.
name|matchesMockWaitTime
argument_list|()
expr_stmt|;
comment|// reset mock and set new expectations
name|mock
operator|.
name|reset
argument_list|()
expr_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// move file back
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/idempotent/done/report.txt"
argument_list|)
decl_stmt|;
name|File
name|renamed
init|=
operator|new
name|File
argument_list|(
literal|"target/idempotent/report.txt"
argument_list|)
decl_stmt|;
name|file
operator|=
name|file
operator|.
name|getAbsoluteFile
argument_list|()
expr_stmt|;
name|file
operator|.
name|renameTo
argument_list|(
name|renamed
operator|.
name|getAbsoluteFile
argument_list|()
argument_list|)
expr_stmt|;
comment|// should NOT consume the file again, let a bit time go
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"MyIdempotentRepository should have been invoked"
argument_list|,
name|invoked
argument_list|)
expr_stmt|;
block|}
DECL|class|MyIdempotentRepository
specifier|public
class|class
name|MyIdempotentRepository
implements|implements
name|IdempotentRepository
argument_list|<
name|String
argument_list|>
block|{
DECL|method|add (String messageId)
specifier|public
name|boolean
name|add
parameter_list|(
name|String
name|messageId
parameter_list|)
block|{
comment|// will return true 1st time, and false 2nd time
name|boolean
name|result
init|=
name|invoked
decl_stmt|;
name|invoked
operator|=
literal|true
expr_stmt|;
name|assertEquals
argument_list|(
literal|"report.txt"
argument_list|,
name|messageId
argument_list|)
expr_stmt|;
return|return
operator|!
name|result
return|;
block|}
DECL|method|contains (String key)
specifier|public
name|boolean
name|contains
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|invoked
return|;
block|}
DECL|method|remove (String key)
specifier|public
name|boolean
name|remove
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
DECL|method|confirm (String key)
specifier|public
name|boolean
name|confirm
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{         }
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{         }
block|}
block|}
end_class

end_unit

