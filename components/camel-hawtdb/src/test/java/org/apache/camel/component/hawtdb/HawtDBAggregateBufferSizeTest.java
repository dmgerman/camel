begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hawtdb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hawtdb
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_class
DECL|class|HawtDBAggregateBufferSizeTest
specifier|public
class|class
name|HawtDBAggregateBufferSizeTest
extends|extends
name|HawtDBAggregateTest
block|{
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
name|HawtDBAggregationRepository
name|repo
init|=
operator|new
name|HawtDBAggregationRepository
argument_list|(
literal|"repo1"
argument_list|,
literal|"target/data/hawtdb.dat"
argument_list|)
decl_stmt|;
comment|// use 2mb file
name|repo
operator|.
name|setBufferSize
argument_list|(
literal|1024
operator|*
literal|1024
operator|*
literal|2
argument_list|)
expr_stmt|;
comment|// here is the Camel route where we aggregate
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|header
argument_list|(
literal|"id"
argument_list|)
argument_list|,
operator|new
name|MyAggregationStrategy
argument_list|()
argument_list|)
operator|.
name|completionSize
argument_list|(
literal|5
argument_list|)
operator|.
name|aggregationRepository
argument_list|(
name|repo
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:aggregated"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

