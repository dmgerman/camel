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
name|java
operator|.
name|io
operator|.
name|FileWriter
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|PollEnrichFileDefaultAggregationStrategyTest
specifier|public
class|class
name|PollEnrichFileDefaultAggregationStrategyTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testPollEnrichDefaultAggregationStrategyBody ()
specifier|public
name|void
name|testPollEnrichDefaultAggregationStrategyBody
parameter_list|()
throws|throws
name|Exception
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|String
name|enrichFilename
init|=
literal|"target/pollEnrich/enrich.txt"
decl_stmt|;
name|String
name|msgText
init|=
literal|"Hello Camel"
decl_stmt|;
name|FileWriter
name|enrichFile
init|=
operator|new
name|FileWriter
argument_list|(
name|enrichFilename
argument_list|)
decl_stmt|;
name|enrichFile
operator|.
name|write
argument_list|(
name|msgText
argument_list|)
expr_stmt|;
name|enrichFile
operator|.
name|close
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|exchanges
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|ex
init|=
operator|(
name|Exchange
operator|)
name|exchanges
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|msgText
argument_list|,
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// assert Camel markerFile got deleted
name|Thread
operator|.
name|sleep
argument_list|(
literal|300
argument_list|)
expr_stmt|;
name|File
name|markerFile
init|=
operator|new
name|File
argument_list|(
name|enrichFilename
operator|+
literal|".camelLock"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Camel markerFile "
operator|+
name|enrichFilename
operator|+
literal|".camelLock did not get deleted after file consumption."
argument_list|,
name|markerFile
operator|.
name|exists
argument_list|()
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
literal|"timer:foo?period=1000&repeatCount=1"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Hello from Camel."
argument_list|)
operator|.
name|pollEnrich
argument_list|(
literal|"file:target/pollEnrich?fileName=enrich.txt&readLock=markerFile"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|log
argument_list|(
literal|"The body is ${body}"
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
block|}
end_class

end_unit

