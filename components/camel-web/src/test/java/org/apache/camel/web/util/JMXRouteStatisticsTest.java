begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|util
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
name|Map
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
name|model
operator|.
name|RouteDefinition
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
name|model
operator|.
name|RoutesDefinition
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
name|util
operator|.
name|CastUtils
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
name|web
operator|.
name|resources
operator|.
name|CamelContextResource
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
name|Assert
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
name|FileSystemXmlApplicationContext
import|;
end_import

begin_class
DECL|class|JMXRouteStatisticsTest
specifier|public
class|class
name|JMXRouteStatisticsTest
extends|extends
name|Assert
block|{
DECL|field|applicationContext
specifier|private
name|AbstractXmlApplicationContext
name|applicationContext
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|statistics
specifier|private
name|RouteStatistics
name|statistics
init|=
operator|new
name|JMXRouteStatistics
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testRouteStats ()
specifier|public
name|void
name|testRouteStats
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContextResource
name|resource
init|=
operator|new
name|CamelContextResource
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|RoutesDefinition
name|routes
init|=
name|resource
operator|.
name|getRoutesResource
argument_list|()
operator|.
name|getRouteDefinitions
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|list
init|=
name|routes
operator|.
name|getRoutes
argument_list|()
decl_stmt|;
name|Object
name|exchangesCompleted
init|=
name|statistics
operator|.
name|getRouteStatistic
argument_list|(
name|camelContext
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|,
literal|"ExchangesCompleted"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"JMX value incorrect, should be 0"
argument_list|,
operator|new
name|Long
argument_list|(
literal|0
argument_list|)
argument_list|,
name|exchangesCompleted
argument_list|)
expr_stmt|;
name|NotifyBuilder
name|notify
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|camelContext
argument_list|)
operator|.
name|whenDone
argument_list|(
literal|1
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|ProducerTemplate
name|template
init|=
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|notify
operator|.
name|matchesMockWaitTime
argument_list|()
expr_stmt|;
name|exchangesCompleted
operator|=
name|statistics
operator|.
name|getRouteStatistic
argument_list|(
name|camelContext
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|,
literal|"ExchangesCompleted"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"JMX value incorrect, should be 1"
argument_list|,
operator|new
name|Long
argument_list|(
literal|1
argument_list|)
argument_list|,
name|exchangesCompleted
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|applicationContext
operator|=
operator|new
name|FileSystemXmlApplicationContext
argument_list|(
literal|"src/main/webapp/WEB-INF/applicationContext.xml"
argument_list|)
expr_stmt|;
name|applicationContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|CamelContext
argument_list|>
name|beansOfType
init|=
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|camelContext
operator|=
name|beansOfType
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|beansOfType
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"camelContext"
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|applicationContext
operator|!=
literal|null
condition|)
block|{
name|applicationContext
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

