begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.enricher
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|enricher
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|AggregationStrategy
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

begin_class
DECL|class|PollEnrichFileCustomAggregationStrategyTest
specifier|public
class|class
name|PollEnrichFileCustomAggregationStrategyTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
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
name|deleteDirectory
argument_list|(
literal|"target/data/enrich"
argument_list|)
expr_stmt|;
name|deleteDirectory
argument_list|(
literal|"target/data/enrichdata"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPollEnrichCustomAggregationStrategyBody ()
specifier|public
name|void
name|testPollEnrichCustomAggregationStrategyBody
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:start"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Start"
argument_list|)
expr_stmt|;
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
name|expectedBodiesReceived
argument_list|(
literal|"Big file"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
literal|"target/data/enrich/.done/AAA.fin"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
literal|"target/data/enrichdata/.done/AAA.dat"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/data/enrich"
argument_list|,
literal|"Start"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"AAA.fin"
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Sleeping for 0.5 sec before writing enrichdata file"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/data/enrichdata"
argument_list|,
literal|"Big file"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"AAA.dat"
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"... write done"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertFileDoesNotExists
argument_list|(
literal|"target/data/enrichdata/AAA.dat.camelLock"
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
name|from
argument_list|(
literal|"file://target/data/enrich?initialDelay=0&delay=10&move=.done"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:start"
argument_list|)
operator|.
name|pollEnrich
argument_list|(
literal|"file://target/data/enrichdata?initialDelay=0&delay=10&readLock=markerFile&move=.done"
argument_list|,
literal|10000
argument_list|,
operator|new
name|ReplaceAggregationStrategy
argument_list|()
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
DECL|method|assertFileDoesNotExists (String filename)
specifier|private
specifier|static
name|void
name|assertFileDoesNotExists
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|filename
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"File "
operator|+
name|filename
operator|+
literal|" should not exist, it should have been deleted after being processed"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|ReplaceAggregationStrategy
class|class
name|ReplaceAggregationStrategy
implements|implements
name|AggregationStrategy
block|{
DECL|method|aggregate (Exchange original, Exchange resource)
specifier|public
name|Exchange
name|aggregate
parameter_list|(
name|Exchange
name|original
parameter_list|,
name|Exchange
name|resource
parameter_list|)
block|{
name|Object
name|resourceResponse
init|=
name|resource
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|original
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
name|original
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|resourceResponse
argument_list|)
expr_stmt|;
name|original
operator|.
name|getProperties
argument_list|()
operator|.
name|putAll
argument_list|(
name|resource
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|original
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|resourceResponse
argument_list|)
expr_stmt|;
name|original
operator|.
name|getProperties
argument_list|()
operator|.
name|putAll
argument_list|(
name|resource
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|original
return|;
block|}
block|}
block|}
end_class

end_unit

