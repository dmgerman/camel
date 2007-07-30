begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Comparator
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

begin_comment
comment|/**  * An implementation of {@link java.util.Comparator} which takes a list of  * {@link org.apache.camel.Expression} objects which is evaluated  * on each exchange to compare them  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|ExpressionListComparator
specifier|public
class|class
name|ExpressionListComparator
implements|implements
name|Comparator
argument_list|<
name|Exchange
argument_list|>
block|{
DECL|field|expressions
specifier|private
specifier|final
name|List
argument_list|<
name|Expression
argument_list|>
name|expressions
decl_stmt|;
DECL|method|ExpressionListComparator (List<Expression> expressions)
specifier|public
name|ExpressionListComparator
parameter_list|(
name|List
argument_list|<
name|Expression
argument_list|>
name|expressions
parameter_list|)
block|{
name|this
operator|.
name|expressions
operator|=
name|expressions
expr_stmt|;
block|}
DECL|method|compare (Exchange e1, Exchange e2)
specifier|public
name|int
name|compare
parameter_list|(
name|Exchange
name|e1
parameter_list|,
name|Exchange
name|e2
parameter_list|)
block|{
for|for
control|(
name|Expression
name|expression
range|:
name|expressions
control|)
block|{
name|Object
name|o1
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|e1
argument_list|)
decl_stmt|;
name|Object
name|o2
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|e2
argument_list|)
decl_stmt|;
name|int
name|answer
init|=
name|ObjectHelper
operator|.
name|compare
argument_list|(
name|o1
argument_list|,
name|o2
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|0
condition|)
block|{
return|return
name|answer
return|;
block|}
block|}
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit

