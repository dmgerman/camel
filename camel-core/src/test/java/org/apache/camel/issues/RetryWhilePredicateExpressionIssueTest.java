begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|issues
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
name|Predicate
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|PredicateBuilder
operator|.
name|and
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|PredicateBuilder
operator|.
name|isNotNull
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|RetryWhilePredicateExpressionIssueTest
specifier|public
class|class
name|RetryWhilePredicateExpressionIssueTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testRetryWhilePredicate ()
specifier|public
name|void
name|testRetryWhilePredicate
parameter_list|()
throws|throws
name|Exception
block|{
name|MyCoolDude
name|dude
init|=
operator|new
name|MyCoolDude
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
name|dude
argument_list|,
literal|"foo"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
operator|+
literal|1
argument_list|,
name|dude
operator|.
name|getCounter
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
name|onException
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|retryWhile
argument_list|(
operator|new
name|Predicate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Predicate
name|predicate
init|=
name|and
argument_list|(
name|simple
argument_list|(
literal|"${body.areWeCool} == 'no'"
argument_list|)
argument_list|,
name|isNotNull
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|boolean
name|answer
init|=
name|predicate
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
return|return
name|answer
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Forced"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyCoolDude
specifier|public
specifier|static
class|class
name|MyCoolDude
block|{
DECL|field|counter
specifier|private
name|int
name|counter
decl_stmt|;
DECL|method|areWeCool ()
specifier|public
name|String
name|areWeCool
parameter_list|()
block|{
if|if
condition|(
name|counter
operator|++
operator|<
literal|3
condition|)
block|{
return|return
literal|"no"
return|;
block|}
else|else
block|{
return|return
literal|"yes"
return|;
block|}
block|}
DECL|method|getCounter ()
specifier|public
name|int
name|getCounter
parameter_list|()
block|{
return|return
name|counter
return|;
block|}
block|}
block|}
end_class

end_unit

