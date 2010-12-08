begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.hawtdb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
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
name|hawtdb
operator|.
name|HawtDBAggregationRepository
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JmsToHawtDBRoute
specifier|public
class|class
name|JmsToHawtDBRoute
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"activemq:queue:foo"
argument_list|)
operator|.
name|transacted
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:input"
argument_list|)
comment|//.log("Incoming ${header.group} with body ${body}")
operator|.
name|aggregate
argument_list|(
name|header
argument_list|(
literal|"group"
argument_list|)
argument_list|,
operator|new
name|MyConcatAggregatationStrategy
argument_list|()
argument_list|)
operator|.
name|aggregationRepository
argument_list|(
operator|new
name|HawtDBAggregationRepository
argument_list|(
literal|"events"
argument_list|,
literal|"data/hawtdb.dat"
argument_list|)
argument_list|)
operator|.
name|completionSize
argument_list|(
literal|10
argument_list|)
operator|.
name|log
argument_list|(
literal|"Aggregated #${header.counter} ${header.group} with body ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:queue:out"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:out"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:queue:out"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

