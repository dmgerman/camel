begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|xml
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
name|builder
operator|.
name|RouteBuilderTest
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
name|processor
operator|.
name|DelegateProcessor
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
comment|/**  * TODO: re-implement the route building logic using spring and   * then test it by overriding the buildXXX methods in the RouteBuilderTest  *   * @version $Revision: 520164 $  */
end_comment

begin_class
DECL|class|XmlRouteBuilderTest
specifier|public
class|class
name|XmlRouteBuilderTest
extends|extends
name|RouteBuilderTest
block|{
DECL|field|ctx
specifier|private
name|ClassPathXmlApplicationContext
name|ctx
decl_stmt|;
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|ctx
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/builder/spring_route_builder_test.xml"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|ctx
operator|.
name|close
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|buildSimpleRoute ()
specifier|protected
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|buildSimpleRoute
parameter_list|()
block|{
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|builder
init|=
operator|(
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"buildSimpleRoute"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|builder
argument_list|)
expr_stmt|;
return|return
name|builder
return|;
block|}
annotation|@
name|Override
DECL|method|buildCustomProcessor ()
specifier|protected
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|buildCustomProcessor
parameter_list|()
block|{
name|myProcessor
operator|=
operator|(
name|Processor
argument_list|<
name|Exchange
argument_list|>
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"myProcessor"
argument_list|)
expr_stmt|;
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|builder
init|=
operator|(
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"buildCustomProcessor"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|builder
argument_list|)
expr_stmt|;
return|return
name|builder
return|;
block|}
annotation|@
name|Override
DECL|method|buildCustomProcessorWithFilter ()
specifier|protected
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|buildCustomProcessorWithFilter
parameter_list|()
block|{
name|myProcessor
operator|=
operator|(
name|Processor
argument_list|<
name|Exchange
argument_list|>
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"myProcessor"
argument_list|)
expr_stmt|;
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|builder
init|=
operator|(
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"buildCustomProcessorWithFilter"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|builder
argument_list|)
expr_stmt|;
return|return
name|builder
return|;
block|}
annotation|@
name|Override
DECL|method|buildRouteWithInterceptor ()
specifier|protected
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|buildRouteWithInterceptor
parameter_list|()
block|{
name|interceptor1
operator|=
operator|(
name|DelegateProcessor
argument_list|<
name|Exchange
argument_list|>
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"interceptor1"
argument_list|)
expr_stmt|;
name|interceptor2
operator|=
operator|(
name|DelegateProcessor
argument_list|<
name|Exchange
argument_list|>
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"interceptor2"
argument_list|)
expr_stmt|;
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|builder
init|=
operator|(
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"buildRouteWithInterceptor"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|builder
argument_list|)
expr_stmt|;
return|return
name|builder
return|;
block|}
annotation|@
name|Override
DECL|method|buildSimpleRouteWithHeaderPredicate ()
specifier|protected
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|buildSimpleRouteWithHeaderPredicate
parameter_list|()
block|{
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|builder
init|=
operator|(
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"buildSimpleRouteWithHeaderPredicate"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|builder
argument_list|)
expr_stmt|;
return|return
name|builder
return|;
block|}
annotation|@
name|Override
DECL|method|buildSimpleRouteWithChoice ()
specifier|protected
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|buildSimpleRouteWithChoice
parameter_list|()
block|{
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|builder
init|=
operator|(
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"buildSimpleRouteWithChoice"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|builder
argument_list|)
expr_stmt|;
return|return
name|builder
return|;
block|}
annotation|@
name|Override
DECL|method|buildWireTap ()
specifier|protected
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|buildWireTap
parameter_list|()
block|{
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|builder
init|=
operator|(
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"buildWireTap"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|builder
argument_list|)
expr_stmt|;
return|return
name|builder
return|;
block|}
annotation|@
name|Override
DECL|method|buildDynamicRecipientList ()
specifier|protected
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|buildDynamicRecipientList
parameter_list|()
block|{
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|builder
init|=
operator|(
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"buildDynamicRecipientList"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|builder
argument_list|)
expr_stmt|;
return|return
name|builder
return|;
block|}
annotation|@
name|Override
DECL|method|buildStaticRecipientList ()
specifier|protected
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|buildStaticRecipientList
parameter_list|()
block|{
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|builder
init|=
operator|(
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"buildStaticRecipientList"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|builder
argument_list|)
expr_stmt|;
return|return
name|builder
return|;
block|}
annotation|@
name|Override
DECL|method|buildSplitter ()
specifier|protected
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|buildSplitter
parameter_list|()
block|{
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|builder
init|=
operator|(
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"buildSplitter"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|builder
argument_list|)
expr_stmt|;
return|return
name|builder
return|;
block|}
annotation|@
name|Override
DECL|method|buildIdempotentConsumer ()
specifier|protected
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|buildIdempotentConsumer
parameter_list|()
block|{
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|builder
init|=
operator|(
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"buildIdempotentConsumer"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|builder
argument_list|)
expr_stmt|;
return|return
name|builder
return|;
block|}
annotation|@
name|Override
DECL|method|testIdempotentConsumer ()
specifier|public
name|void
name|testIdempotentConsumer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO
block|}
block|}
end_class

end_unit

