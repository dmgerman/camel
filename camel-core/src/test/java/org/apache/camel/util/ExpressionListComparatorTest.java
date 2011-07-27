begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|impl
operator|.
name|DefaultExchange
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|ExpressionListComparatorTest
specifier|public
class|class
name|ExpressionListComparatorTest
extends|extends
name|ContextTestSupport
block|{
DECL|class|MyFooExpression
specifier|private
specifier|static
class|class
name|MyFooExpression
implements|implements
name|Expression
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|evaluate (Exchange exchange, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|(
name|T
operator|)
literal|"foo"
return|;
block|}
block|}
DECL|class|MyBarExpression
specifier|private
specifier|static
class|class
name|MyBarExpression
implements|implements
name|Expression
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|evaluate (Exchange exchange, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|(
name|T
operator|)
literal|"bar"
return|;
block|}
block|}
DECL|method|testExpressionListComparator ()
specifier|public
name|void
name|testExpressionListComparator
parameter_list|()
block|{
name|List
argument_list|<
name|Expression
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Expression
argument_list|>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|MyFooExpression
argument_list|()
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|MyBarExpression
argument_list|()
argument_list|)
expr_stmt|;
name|ExpressionListComparator
name|comp
init|=
operator|new
name|ExpressionListComparator
argument_list|(
name|list
argument_list|)
decl_stmt|;
name|Exchange
name|e1
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Exchange
name|e2
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|int
name|out
init|=
name|comp
operator|.
name|compare
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

