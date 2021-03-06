begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|TypeConverterRegistryStatsPerformanceTest
specifier|public
class|class
name|TypeConverterRegistryStatsPerformanceTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|size
specifier|private
name|int
name|size
init|=
literal|1000
decl_stmt|;
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|setTypeConverterStatisticsEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testTransform ()
specifier|public
name|void
name|testTransform
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|noop
init|=
name|context
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|getNoopCounter
argument_list|()
decl_stmt|;
name|long
name|attempt
init|=
name|context
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|getAttemptCounter
argument_list|()
decl_stmt|;
name|long
name|failed
init|=
name|context
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|getFailedCounter
argument_list|()
decl_stmt|;
name|long
name|hit
init|=
name|context
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|getHitCounter
argument_list|()
decl_stmt|;
name|long
name|miss
init|=
name|context
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|getMissCounter
argument_list|()
decl_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
name|size
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|long
name|noop2
init|=
name|context
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|getNoopCounter
argument_list|()
decl_stmt|;
name|long
name|attempt2
init|=
name|context
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|getAttemptCounter
argument_list|()
decl_stmt|;
name|long
name|failed2
init|=
name|context
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|getFailedCounter
argument_list|()
decl_stmt|;
name|long
name|hit2
init|=
name|context
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|getHitCounter
argument_list|()
decl_stmt|;
name|long
name|miss2
init|=
name|context
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|getMissCounter
argument_list|()
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Noop: before={}, after={}, delta={}"
argument_list|,
name|noop
argument_list|,
name|noop2
argument_list|,
name|noop2
operator|-
name|noop
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Attempt: before={}, after={}, delta={}"
argument_list|,
name|attempt
argument_list|,
name|attempt2
argument_list|,
name|attempt2
operator|-
name|attempt
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Failed: before={}, after={}, delta={}"
argument_list|,
name|failed
argument_list|,
name|failed2
argument_list|,
name|failed2
operator|-
name|failed
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Hit: before={}, after={}, delta={}"
argument_list|,
name|hit
argument_list|,
name|hit2
argument_list|,
name|hit2
operator|-
name|hit
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Miss: before={}, after={}, delta={}"
argument_list|,
name|miss
argument_list|,
name|miss2
argument_list|,
name|miss2
operator|-
name|miss
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
literal|"direct:start"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|method
argument_list|(
name|TypeConverterRegistryStatsPerformanceTest
operator|.
name|class
argument_list|,
literal|"transformMe"
argument_list|)
operator|.
name|bean
argument_list|(
name|TypeConverterRegistryStatsPerformanceTest
operator|.
name|class
argument_list|,
literal|"transformMeAlso"
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
DECL|method|transformMe (String in)
specifier|public
name|String
name|transformMe
parameter_list|(
name|String
name|in
parameter_list|)
block|{
return|return
literal|"Hello "
operator|+
name|in
return|;
block|}
DECL|method|transformMeAlso (String in)
specifier|public
name|String
name|transformMeAlso
parameter_list|(
name|String
name|in
parameter_list|)
block|{
return|return
literal|"Bye "
operator|+
name|in
return|;
block|}
block|}
end_class

end_unit

