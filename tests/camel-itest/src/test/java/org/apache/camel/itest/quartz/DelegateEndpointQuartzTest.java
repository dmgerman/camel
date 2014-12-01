begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.quartz
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|quartz
package|;
end_package

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
name|Consumer
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
name|DelegateEndpoint
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
name|Endpoint
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
name|Producer
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
name|component
operator|.
name|quartz
operator|.
name|QuartzConstants
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
name|DefaultComponent
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
name|DefaultEndpoint
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|URISupport
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
name|quartz
operator|.
name|JobDetail
import|;
end_import

begin_class
DECL|class|DelegateEndpointQuartzTest
specifier|public
class|class
name|DelegateEndpointQuartzTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testQuartzCronRoute ()
specifier|public
name|void
name|testQuartzCronRoute
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
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|JobDetail
name|job
init|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"jobDetail"
argument_list|,
name|JobDetail
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|job
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cron"
argument_list|,
name|job
operator|.
name|getJobDataMap
argument_list|()
operator|.
name|get
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_TRIGGER_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"0/2 * * * * ?"
argument_list|,
name|job
operator|.
name|getJobDataMap
argument_list|()
operator|.
name|get
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_TRIGGER_CRON_EXPRESSION
argument_list|)
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
literal|"my:quartz://myGroup/myTimerName?cron=0/2+*+*+*+*+?"
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
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
operator|new
name|JndiRegistry
argument_list|(
name|createJndiContext
argument_list|()
argument_list|)
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"my"
argument_list|,
operator|new
name|MyComponent
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
DECL|class|MyComponent
class|class
name|MyComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|childUri
init|=
name|remaining
decl_stmt|;
comment|// we need to apply the params here
if|if
condition|(
name|parameters
operator|!=
literal|null
operator|&&
name|parameters
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|childUri
operator|=
name|childUri
operator|+
literal|"?"
operator|+
name|URISupport
operator|.
name|createQueryString
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
block|}
comment|// need to clean the parameters to avoid default component verify parameter complain
name|parameters
operator|.
name|clear
argument_list|()
expr_stmt|;
name|Endpoint
name|childEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|childUri
argument_list|)
decl_stmt|;
return|return
operator|new
name|MyEndpoint
argument_list|(
name|uri
argument_list|,
name|childEndpoint
argument_list|)
return|;
block|}
block|}
DECL|class|MyEndpoint
class|class
name|MyEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|DelegateEndpoint
block|{
DECL|field|childEndpoint
specifier|private
specifier|final
name|Endpoint
name|childEndpoint
decl_stmt|;
DECL|method|MyEndpoint (String uri, Endpoint childEndpoint)
name|MyEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Endpoint
name|childEndpoint
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|this
operator|.
name|childEndpoint
operator|=
name|childEndpoint
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|childEndpoint
operator|.
name|createProducer
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|childEndpoint
operator|.
name|createConsumer
argument_list|(
name|processor
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|childEndpoint
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
literal|"my:"
operator|+
name|childEndpoint
operator|.
name|getEndpointUri
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

