begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dataset
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dataset
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|Expression
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
name|ExpressionBuilder
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
name|PredicateBuilder
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
name|builder
operator|.
name|xml
operator|.
name|XPathBuilder
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
name|util
operator|.
name|PredicateAssertHelper
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|CustomDataSetTest
specifier|public
class|class
name|CustomDataSetTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|dataSet
specifier|protected
name|DataSet
name|dataSet
init|=
operator|new
name|DataSetSupport
argument_list|()
block|{
name|Expression
name|expression
init|=
operator|new
name|XPathBuilder
argument_list|(
literal|"/message/@index"
argument_list|)
operator|.
name|resultType
argument_list|(
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|assertMessageExpected
parameter_list|(
name|DataSetEndpoint
name|dataSetEndpoint
parameter_list|,
name|Exchange
name|expected
parameter_list|,
name|Exchange
name|actual
parameter_list|,
name|long
name|index
parameter_list|)
throws|throws
name|Exception
block|{
comment|// lets compare the XPath result
name|Predicate
name|predicate
init|=
name|PredicateBuilder
operator|.
name|isEqualTo
argument_list|(
name|expression
argument_list|,
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
name|index
argument_list|)
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"evaluating predicate: "
operator|+
name|predicate
argument_list|)
expr_stmt|;
name|PredicateAssertHelper
operator|.
name|assertMatches
argument_list|(
name|predicate
argument_list|,
literal|"Actual: "
operator|+
name|actual
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Object
name|createMessageBody
parameter_list|(
name|long
name|messageIndex
parameter_list|)
block|{
return|return
literal|"<message index='"
operator|+
name|messageIndex
operator|+
literal|"'>someBody"
operator|+
name|messageIndex
operator|+
literal|"</message>"
return|;
block|}
block|}
decl_stmt|;
annotation|@
name|Test
DECL|method|testUsingCustomDataSet ()
specifier|public
name|void
name|testUsingCustomDataSet
parameter_list|()
throws|throws
name|Exception
block|{
comment|// data set will itself set its assertions so we should just
comment|// assert that all mocks is ok
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|Context
name|context
init|=
name|super
operator|.
name|createJndiContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|bind
argument_list|(
literal|"foo"
argument_list|,
name|dataSet
argument_list|)
expr_stmt|;
return|return
name|context
return|;
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
literal|"dataset:foo?initialDelay=0"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"dataset:foo?initialDelay=0"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

