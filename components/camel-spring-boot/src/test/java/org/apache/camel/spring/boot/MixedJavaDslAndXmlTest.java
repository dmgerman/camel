begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|CamelContext
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
name|impl
operator|.
name|DefaultRoute
import|;
end_import

begin_import
import|import
name|org
operator|.
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Condition
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
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|EnableAutoConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|test
operator|.
name|context
operator|.
name|SpringBootTest
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
name|annotation
operator|.
name|Bean
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
name|annotation
operator|.
name|Configuration
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
name|annotation
operator|.
name|ImportResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|SpringRunner
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertThat
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|SpringRunner
operator|.
name|class
argument_list|)
annotation|@
name|SpringBootTest
DECL|class|MixedJavaDslAndXmlTest
specifier|public
class|class
name|MixedJavaDslAndXmlTest
block|{
annotation|@
name|Configuration
annotation|@
name|EnableAutoConfiguration
annotation|@
name|ImportResource
argument_list|(
literal|"classpath:test-camel-context.xml"
argument_list|)
DECL|class|JavaDslConfiguration
specifier|public
specifier|static
class|class
name|JavaDslConfiguration
block|{
annotation|@
name|Bean
DECL|method|javaDsl ()
specifier|public
name|RouteBuilder
name|javaDsl
parameter_list|()
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
name|from
argument_list|(
literal|"timer:project?period=1s"
argument_list|)
operator|.
name|id
argument_list|(
literal|"java"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Hello World from Java Route"
argument_list|)
operator|.
name|log
argument_list|(
literal|">>> ${body}"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
annotation|@
name|Autowired
DECL|field|camel
specifier|private
name|CamelContext
name|camel
decl_stmt|;
annotation|@
name|Test
DECL|method|thereShouldBeTwoRoutesConfigured ()
specifier|public
name|void
name|thereShouldBeTwoRoutesConfigured
parameter_list|()
block|{
specifier|final
name|List
argument_list|<
name|Route
argument_list|>
name|routes
init|=
name|camel
operator|.
name|getRoutes
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|routes
argument_list|)
operator|.
name|as
argument_list|(
literal|"There should be two routes configured, one from Java DSL and one from XML"
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|routeIds
init|=
name|routes
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|Route
operator|::
name|getId
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|routeIds
argument_list|)
operator|.
name|as
argument_list|(
literal|"Should contain routes from Java DSL, XML and auto-loaded XML"
argument_list|)
operator|.
name|containsOnly
argument_list|(
literal|"java"
argument_list|,
literal|"xml"
argument_list|,
literal|"xmlAutoLoading"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|routes
argument_list|)
operator|.
name|as
argument_list|(
literal|"All routes should be started"
argument_list|)
operator|.
name|are
argument_list|(
operator|new
name|Condition
argument_list|<
name|Route
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|matches
parameter_list|(
specifier|final
name|Route
name|route
parameter_list|)
block|{
return|return
operator|(
operator|(
name|DefaultRoute
operator|)
name|route
operator|)
operator|.
name|isStarted
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

