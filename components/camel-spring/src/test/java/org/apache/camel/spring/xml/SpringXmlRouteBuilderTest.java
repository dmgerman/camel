begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Route
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
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|SpringCamelContext
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * A test case of the builder using Spring 2.0 to load the rules  *  * @version $Revision: 520164 $  */
end_comment

begin_class
DECL|class|SpringXmlRouteBuilderTest
specifier|public
class|class
name|SpringXmlRouteBuilderTest
extends|extends
name|RouteBuilderTest
block|{
DECL|field|applicationContext
specifier|protected
name|AbstractXmlApplicationContext
name|applicationContext
decl_stmt|;
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|buildSimpleRoute ()
specifier|protected
name|List
argument_list|<
name|Route
argument_list|>
name|buildSimpleRoute
parameter_list|()
block|{
return|return
name|getRoutesFromContext
argument_list|(
literal|"org/apache/camel/spring/xml/buildSimpleRoute.xml"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|buildCustomProcessor ()
specifier|protected
name|List
argument_list|<
name|Route
argument_list|>
name|buildCustomProcessor
parameter_list|()
block|{
name|List
argument_list|<
name|Route
argument_list|>
name|answer
init|=
name|getRoutesFromContext
argument_list|(
literal|"org/apache/camel/spring/xml/buildCustomProcessor.xml"
argument_list|)
decl_stmt|;
name|myProcessor
operator|=
operator|(
name|Processor
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"myProcessor"
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|buildCustomProcessorWithFilter ()
specifier|protected
name|List
argument_list|<
name|Route
argument_list|>
name|buildCustomProcessorWithFilter
parameter_list|()
block|{
name|List
argument_list|<
name|Route
argument_list|>
name|answer
init|=
name|getRoutesFromContext
argument_list|(
literal|"org/apache/camel/spring/xml/buildCustomProcessorWithFilter.xml"
argument_list|)
decl_stmt|;
name|myProcessor
operator|=
operator|(
name|Processor
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"myProcessor"
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|buildRouteWithInterceptor ()
specifier|protected
name|List
argument_list|<
name|Route
argument_list|>
name|buildRouteWithInterceptor
parameter_list|()
block|{
name|List
argument_list|<
name|Route
argument_list|>
name|answer
init|=
name|getRoutesFromContext
argument_list|(
literal|"org/apache/camel/spring/xml/buildRouteWithInterceptor.xml"
argument_list|)
decl_stmt|;
name|interceptor1
operator|=
operator|(
name|DelegateProcessor
operator|)
name|applicationContext
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
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"interceptor2"
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|buildSimpleRouteWithHeaderPredicate ()
specifier|protected
name|List
argument_list|<
name|Route
argument_list|>
name|buildSimpleRouteWithHeaderPredicate
parameter_list|()
block|{
return|return
name|getRoutesFromContext
argument_list|(
literal|"org/apache/camel/spring/xml/buildSimpleRouteWithHeaderPredicate.xml"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|buildSimpleRouteWithChoice ()
specifier|protected
name|List
argument_list|<
name|Route
argument_list|>
name|buildSimpleRouteWithChoice
parameter_list|()
block|{
return|return
name|getRoutesFromContext
argument_list|(
literal|"org/apache/camel/spring/xml/buildSimpleRouteWithChoice.xml"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|buildWireTap ()
specifier|protected
name|List
argument_list|<
name|Route
argument_list|>
name|buildWireTap
parameter_list|()
block|{
return|return
name|getRoutesFromContext
argument_list|(
literal|"org/apache/camel/spring/xml/buildWireTap.xml"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|buildDynamicRecipientList ()
specifier|protected
name|List
argument_list|<
name|Route
argument_list|>
name|buildDynamicRecipientList
parameter_list|()
block|{
return|return
name|getRoutesFromContext
argument_list|(
literal|"org/apache/camel/spring/xml/buildDynamicRecipientList.xml"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|buildStaticRecipientList ()
specifier|protected
name|List
argument_list|<
name|Route
argument_list|>
name|buildStaticRecipientList
parameter_list|()
block|{
return|return
name|getRoutesFromContext
argument_list|(
literal|"org/apache/camel/spring/xml/buildStaticRecipientList.xml"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|buildSplitter ()
specifier|protected
name|List
argument_list|<
name|Route
argument_list|>
name|buildSplitter
parameter_list|()
block|{
return|return
name|getRoutesFromContext
argument_list|(
literal|"org/apache/camel/spring/xml/buildSplitter.xml"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|buildIdempotentConsumer ()
specifier|protected
name|List
argument_list|<
name|Route
argument_list|>
name|buildIdempotentConsumer
parameter_list|()
block|{
return|return
name|getRoutesFromContext
argument_list|(
literal|"org/apache/camel/spring/xml/buildIdempotentConsumer.xml"
argument_list|)
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
annotation|@
name|Override
DECL|method|testWireTap ()
specifier|public
name|void
name|testWireTap
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO
block|}
annotation|@
name|Override
DECL|method|testRouteDynamicReceipentList ()
specifier|public
name|void
name|testRouteDynamicReceipentList
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO
block|}
annotation|@
name|Override
DECL|method|testSplitter ()
specifier|public
name|void
name|testSplitter
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO
block|}
DECL|method|getRoutesFromContext (String classpathConfigFile)
specifier|protected
name|List
argument_list|<
name|Route
argument_list|>
name|getRoutesFromContext
parameter_list|(
name|String
name|classpathConfigFile
parameter_list|)
block|{
name|applicationContext
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
name|classpathConfigFile
argument_list|)
expr_stmt|;
name|String
name|name
init|=
literal|"camel"
decl_stmt|;
name|SpringCamelContext
name|context
init|=
operator|(
name|SpringCamelContext
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No Camel Context for name: "
operator|+
name|name
operator|+
literal|" in file: "
operator|+
name|classpathConfigFile
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Route
argument_list|>
name|routes
init|=
name|context
operator|.
name|getRoutes
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No routes available for context: "
operator|+
name|name
operator|+
literal|" in file: "
operator|+
name|classpathConfigFile
argument_list|,
name|routes
argument_list|)
expr_stmt|;
return|return
name|routes
return|;
block|}
block|}
end_class

end_unit

