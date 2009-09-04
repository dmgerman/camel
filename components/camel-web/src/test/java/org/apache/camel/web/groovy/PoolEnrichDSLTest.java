begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.groovy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|groovy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|junit
operator|.
name|Ignore
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
comment|/**  *   */
end_comment

begin_class
DECL|class|PoolEnrichDSLTest
specifier|public
class|class
name|PoolEnrichDSLTest
extends|extends
name|GroovyRendererTestSupport
block|{
comment|/**      * a route involving a external class: aggregationStrategy      *       * TODO: fix this test!      */
annotation|@
name|Ignore
argument_list|(
literal|"Need to fix this test"
argument_list|)
annotation|@
name|Test
DECL|method|fixmeTestPollEnrich ()
specifier|public
name|void
name|fixmeTestPollEnrich
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"from(\"direct:start\").pollEnrich(\"direct:foo\", 1000, aggregationStrategy).to(\"mock:result\")"
decl_stmt|;
name|String
index|[]
name|importClasses
init|=
operator|new
name|String
index|[]
block|{
literal|"import org.apache.camel.processor.enricher.*"
block|}
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|newObjects
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|newObjects
operator|.
name|put
argument_list|(
literal|"aggregationStrategy"
argument_list|,
literal|"SampleAggregator"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|dsl
argument_list|,
name|render
argument_list|(
name|dsl
argument_list|,
name|importClasses
argument_list|,
name|newObjects
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPollEnrichWithoutAggregationStrategy ()
specifier|public
name|void
name|testPollEnrichWithoutAggregationStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"from(\"direct:start\").pollEnrich(\"direct:foo\", 1000).to(\"mock:result\")"
decl_stmt|;
name|assertEquals
argument_list|(
name|dsl
argument_list|,
name|render
argument_list|(
name|dsl
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

