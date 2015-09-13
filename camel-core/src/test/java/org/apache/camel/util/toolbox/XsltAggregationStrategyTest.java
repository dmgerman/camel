begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.toolbox
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|toolbox
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Unit test for the {@link XsltAggregationStrategy}.  *<p>  * Need to use Saxon to get a predictable result: we cannot rely on the JDK's XSLT processor as it can vary across  * platforms and JDK versions. Also, Xalan does not handle node-set properties well.  */
end_comment

begin_class
DECL|class|XsltAggregationStrategyTest
specifier|public
class|class
name|XsltAggregationStrategyTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testXsltAggregationDefaultProperty ()
specifier|public
name|void
name|testXsltAggregationDefaultProperty
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|startRoute
argument_list|(
literal|"route1"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:transformed"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><item>ABC</item>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testXsltAggregationUserProperty ()
specifier|public
name|void
name|testXsltAggregationUserProperty
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|startRoute
argument_list|(
literal|"route2"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:transformed"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><item>ABC</item>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
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
name|from
argument_list|(
literal|"file:src/test/resources/org/apache/camel/util/toolbox?noop=true&antInclude=*.xml"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"route1"
argument_list|)
operator|.
name|noAutoStartup
argument_list|()
operator|.
name|aggregate
argument_list|(
operator|new
name|XsltAggregationStrategy
argument_list|(
literal|"org/apache/camel/util/toolbox/aggregate.xsl"
argument_list|)
operator|.
name|withSaxon
argument_list|()
argument_list|)
operator|.
name|constant
argument_list|(
literal|true
argument_list|)
operator|.
name|completionFromBatchConsumer
argument_list|()
operator|.
name|log
argument_list|(
literal|"after aggregate body: ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:transformed"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"file:src/test/resources/org/apache/camel/util/toolbox?noop=true&antInclude=*.xml"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"route2"
argument_list|)
operator|.
name|noAutoStartup
argument_list|()
operator|.
name|aggregate
argument_list|(
operator|new
name|XsltAggregationStrategy
argument_list|(
literal|"org/apache/camel/util/toolbox/aggregate-user-property.xsl"
argument_list|)
operator|.
name|withSaxon
argument_list|()
operator|.
name|withPropertyName
argument_list|(
literal|"user-property"
argument_list|)
argument_list|)
operator|.
name|constant
argument_list|(
literal|true
argument_list|)
operator|.
name|completionFromBatchConsumer
argument_list|()
operator|.
name|log
argument_list|(
literal|"after aggregate body: ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:transformed"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

