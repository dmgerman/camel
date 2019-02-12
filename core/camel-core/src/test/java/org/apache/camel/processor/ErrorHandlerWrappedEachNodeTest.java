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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Unit test for verifying that error handler is wrapped each individual node in a pipeline.  * Based on CAMEL-1548.  */
end_comment

begin_class
DECL|class|ErrorHandlerWrappedEachNodeTest
specifier|public
class|class
name|ErrorHandlerWrappedEachNodeTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|kabom
specifier|private
specifier|static
name|int
name|kabom
decl_stmt|;
DECL|field|hi
specifier|private
specifier|static
name|int
name|hi
decl_stmt|;
annotation|@
name|Test
DECL|method|testKabom ()
specifier|public
name|void
name|testKabom
parameter_list|()
throws|throws
name|Exception
block|{
name|kabom
operator|=
literal|0
expr_stmt|;
name|hi
operator|=
literal|0
expr_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hi Kabom"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Kabom"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// we invoke kabom 3 times
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|kabom
argument_list|)
expr_stmt|;
comment|// but hi is only invoke 1 time
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|hi
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
comment|// use dead letter channel that supports redeliveries
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|logStackTrace
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|pipeline
argument_list|(
literal|"bean:foo?method=hi"
argument_list|,
literal|"bean:foo?method=kabom"
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
literal|"foo"
argument_list|,
operator|new
name|MyFooBean
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
DECL|class|MyFooBean
specifier|public
specifier|static
specifier|final
class|class
name|MyFooBean
block|{
DECL|method|kabom ()
specifier|public
name|void
name|kabom
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|kabom
operator|++
operator|<
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Kabom"
argument_list|)
throw|;
block|}
block|}
DECL|method|hi (String payload)
specifier|public
name|String
name|hi
parameter_list|(
name|String
name|payload
parameter_list|)
throws|throws
name|Exception
block|{
name|hi
operator|++
expr_stmt|;
return|return
literal|"Hi "
operator|+
name|payload
return|;
block|}
block|}
block|}
end_class

end_unit
