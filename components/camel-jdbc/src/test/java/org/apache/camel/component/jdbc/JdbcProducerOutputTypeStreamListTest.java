begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jdbc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jdbc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|*
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
name|EndpointInject
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

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|instanceOf
import|;
end_import

begin_class
DECL|class|JdbcProducerOutputTypeStreamListTest
specifier|public
class|class
name|JdbcProducerOutputTypeStreamListTest
extends|extends
name|AbstractJdbcTestSupport
block|{
DECL|field|QUERY
specifier|private
specifier|static
specifier|final
name|String
name|QUERY
init|=
literal|"select * from customer"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:result"
argument_list|)
DECL|field|result
specifier|private
name|MockEndpoint
name|result
decl_stmt|;
annotation|@
name|Test
DECL|method|shouldReturnAnIterator ()
specifier|public
name|void
name|shouldReturnAnIterator
parameter_list|()
throws|throws
name|Exception
block|{
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|QUERY
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|resultBodyAt
argument_list|(
literal|0
argument_list|)
argument_list|,
name|instanceOf
argument_list|(
name|Iterator
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldStreamResultRows ()
specifier|public
name|void
name|shouldStreamResultRows
parameter_list|()
throws|throws
name|Exception
block|{
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:withSplit"
argument_list|,
name|QUERY
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|resultBodyAt
argument_list|(
literal|0
argument_list|)
argument_list|,
name|instanceOf
argument_list|(
name|Map
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|resultBodyAt
argument_list|(
literal|1
argument_list|)
argument_list|,
name|instanceOf
argument_list|(
name|Map
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|resultBodyAt
argument_list|(
literal|2
argument_list|)
argument_list|,
name|instanceOf
argument_list|(
name|Map
operator|.
name|class
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
throws|throws
name|Exception
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
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jdbc:testdb?outputType=StreamList"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:withSplit"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jdbc:testdb?outputType=StreamList"
argument_list|)
operator|.
name|split
argument_list|(
name|body
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
DECL|method|resultBodyAt (int index)
specifier|private
name|Object
name|resultBodyAt
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|result
operator|.
name|assertExchangeReceived
argument_list|(
name|index
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
return|;
block|}
block|}
end_class

end_unit

