begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.ibatis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|ibatis
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
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|CamelExecutionException
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
name|EndpointInject
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
name|ProducerTemplate
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
name|test
operator|.
name|junit4
operator|.
name|CamelSpringTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|AsyncEndpointTransacted2Test
specifier|public
class|class
name|AsyncEndpointTransacted2Test
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"direct:start"
argument_list|)
DECL|field|producer
name|ProducerTemplate
name|producer
decl_stmt|;
DECL|field|table
specifier|private
name|DummyTable
name|table
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:end"
argument_list|)
DECL|field|end
specifier|private
name|MockEndpoint
name|end
decl_stmt|;
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/itest/ibatis/AsyncEndpointTransacted2Test.xml"
argument_list|)
return|;
block|}
annotation|@
name|Before
DECL|method|createTable ()
specifier|public
name|void
name|createTable
parameter_list|()
block|{
name|table
operator|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
literal|"table"
argument_list|,
name|DummyTable
operator|.
name|class
argument_list|)
expr_stmt|;
name|table
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|dropTable ()
specifier|public
name|void
name|dropTable
parameter_list|()
block|{
name|table
operator|.
name|drop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAsyncEndpointOK ()
specifier|public
name|void
name|testAsyncEndpointOK
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|end
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye Camel"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|sendBody
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Integer
argument_list|>
name|values
init|=
name|table
operator|.
name|values
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|values
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|it
init|=
name|values
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|3
argument_list|)
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|4
argument_list|)
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|end
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAsyncEndpointRollback ()
specifier|public
name|void
name|testAsyncEndpointRollback
parameter_list|()
block|{
name|end
operator|.
name|whenAnyExchangeReceived
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
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
try|try
block|{
name|producer
operator|.
name|sendBody
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|assertIsInstanceOf
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|table
operator|.
name|values
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

