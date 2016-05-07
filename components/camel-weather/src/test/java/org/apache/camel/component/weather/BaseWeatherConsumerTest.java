begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.weather
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|weather
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
name|Message
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
name|CamelTestSupport
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

begin_class
DECL|class|BaseWeatherConsumerTest
specifier|public
specifier|abstract
class|class
name|BaseWeatherConsumerTest
extends|extends
name|CamelTestSupport
block|{
DECL|method|checkWeatherContent (String weather)
specifier|protected
name|void
name|checkWeatherContent
parameter_list|(
name|String
name|weather
parameter_list|)
block|{
comment|// the default mode is json
name|log
operator|.
name|debug
argument_list|(
literal|"The weather in {} format is {}{}"
argument_list|,
name|WeatherMode
operator|.
name|JSON
argument_list|,
name|LS
argument_list|,
name|weather
argument_list|)
expr_stmt|;
name|assertStringContains
argument_list|(
name|weather
argument_list|,
literal|"\"coord\":{"
argument_list|)
expr_stmt|;
name|assertStringContains
argument_list|(
name|weather
argument_list|,
literal|"temp"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGrabbingListOfEntries ()
specifier|public
name|void
name|testGrabbingListOfEntries
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
comment|// as the default delay option is one hour long, we expect exactly one message exchange
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// give the route a bit time to start and fetch the weather info
name|assertMockEndpointsSatisfied
argument_list|(
literal|20
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|String
name|weather
init|=
name|assertIsInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|in
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
name|checkWeatherContent
argument_list|(
name|weather
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

