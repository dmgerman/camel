begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.facebook
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|facebook
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|*
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|facebook4j
operator|.
name|api
operator|.
name|SearchMethods
import|;
end_import

begin_class
DECL|class|FacebookComponentConsumerTest
specifier|public
class|class
name|FacebookComponentConsumerTest
extends|extends
name|CamelFacebookTestSupport
block|{
DECL|field|searchNames
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|searchNames
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|excludedNames
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|excludedNames
decl_stmt|;
DECL|method|FacebookComponentConsumerTest ()
specifier|public
name|FacebookComponentConsumerTest
parameter_list|()
throws|throws
name|Exception
block|{
comment|// find search methods for consumer tests
for|for
control|(
name|Method
name|method
range|:
name|SearchMethods
operator|.
name|class
operator|.
name|getDeclaredMethods
argument_list|()
control|)
block|{
name|String
name|name
init|=
name|getShortName
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
literal|"locations"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|&&
operator|!
literal|"checkins"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|searchNames
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
name|excludedNames
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"places"
argument_list|,
literal|"users"
argument_list|,
literal|"search"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumers ()
specifier|public
name|void
name|testConsumers
parameter_list|()
throws|throws
name|InterruptedException
block|{
for|for
control|(
name|String
name|name
range|:
name|searchNames
control|)
block|{
name|MockEndpoint
name|mock
decl_stmt|;
if|if
condition|(
operator|!
name|excludedNames
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|mock
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:consumeResult"
operator|+
name|name
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|mock
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:consumeQueryResult"
operator|+
name|name
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
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
block|{
comment|// start with a 7 day window for the first delayed poll
name|String
name|since
init|=
operator|new
name|SimpleDateFormat
argument_list|(
name|FacebookConstants
operator|.
name|FACEBOOK_DATE_FORMAT
argument_list|)
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|TimeUnit
operator|.
name|MILLISECONDS
operator|.
name|convert
argument_list|(
literal|7
argument_list|,
name|TimeUnit
operator|.
name|DAYS
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|searchNames
control|)
block|{
if|if
condition|(
operator|!
name|excludedNames
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
comment|// consumer.sendEmptyMessageWhenIdle is true since user may not have some items like events
name|from
argument_list|(
literal|"facebook://"
operator|+
name|name
operator|+
literal|"?reading.limit=10&reading.locale=en.US&reading.since="
operator|+
name|since
operator|+
literal|"&consumer.initialDelay=1000&consumer.sendEmptyMessageWhenIdle=true&"
operator|+
name|getOauthParams
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:consumeResult"
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
name|from
argument_list|(
literal|"facebook://"
operator|+
name|name
operator|+
literal|"?query=cheese&reading.limit=10&reading.locale=en.US&reading.since="
operator|+
name|since
operator|+
literal|"&consumer.initialDelay=1000&"
operator|+
name|getOauthParams
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:consumeQueryResult"
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
comment|// TODO add tests for the rest of the supported methods
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

