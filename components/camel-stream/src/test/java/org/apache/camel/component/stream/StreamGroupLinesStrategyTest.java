begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stream
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stream
package|;
end_package

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
name|BindToRegistry
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

begin_class
DECL|class|StreamGroupLinesStrategyTest
specifier|public
class|class
name|StreamGroupLinesStrategyTest
extends|extends
name|StreamGroupLinesTest
block|{
annotation|@
name|BindToRegistry
argument_list|(
literal|"myGroupStrategy"
argument_list|)
DECL|field|strat
specifier|private
name|MyGroupStrategy
name|strat
init|=
operator|new
name|MyGroupStrategy
argument_list|()
decl_stmt|;
DECL|class|MyGroupStrategy
class|class
name|MyGroupStrategy
implements|implements
name|GroupStrategy
block|{
annotation|@
name|Override
DECL|method|groupLines (List<String> lines)
specifier|public
name|Object
name|groupLines
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|lines
parameter_list|)
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|line
range|:
name|lines
control|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|LS
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|Test
DECL|method|testGroupLines ()
specifier|public
name|void
name|testGroupLines
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
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|mock
operator|.
name|setAssertPeriod
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|StreamConstants
operator|.
name|STREAM_INDEX
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|StreamConstants
operator|.
name|STREAM_COMPLETE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|1
argument_list|)
operator|.
name|header
argument_list|(
name|StreamConstants
operator|.
name|STREAM_INDEX
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|1
argument_list|)
operator|.
name|header
argument_list|(
name|StreamConstants
operator|.
name|STREAM_COMPLETE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Object
name|result
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
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong result."
argument_list|,
literal|"A"
operator|+
name|LS
operator|+
literal|"B"
operator|+
name|LS
operator|+
literal|"C"
operator|+
name|LS
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|Object
name|result2
init|=
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong result."
argument_list|,
literal|"D"
operator|+
name|LS
operator|+
literal|"E"
operator|+
name|LS
operator|+
literal|"F"
operator|+
name|LS
argument_list|,
name|result2
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
literal|"stream:file?fileName=target/stream/streamfile.txt&groupLines=3&groupStrategy=#myGroupStrategy"
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

