begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
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
name|Processor
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
name|ExpressionComparator
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
name|ExpressionListComparator
import|;
end_import

begin_comment
comment|/**  * An implementation of the<a href="http://camel.apache.org/resequencer.html">Resequencer</a>  * which can reorder messages within a batch.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|Resequencer
specifier|public
class|class
name|Resequencer
extends|extends
name|BatchProcessor
block|{
DECL|method|Resequencer (Processor processor, Expression expression)
specifier|public
name|Resequencer
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
name|this
argument_list|(
name|processor
argument_list|,
name|createSet
argument_list|(
name|expression
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|Resequencer (Processor processor, List<Expression> expressions)
specifier|public
name|Resequencer
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|List
argument_list|<
name|Expression
argument_list|>
name|expressions
parameter_list|)
block|{
name|this
argument_list|(
name|processor
argument_list|,
name|createSet
argument_list|(
name|expressions
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|Resequencer (Processor processor, Set<Exchange> collection)
specifier|public
name|Resequencer
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|Set
argument_list|<
name|Exchange
argument_list|>
name|collection
parameter_list|)
block|{
name|super
argument_list|(
name|processor
argument_list|,
name|collection
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Resequencer[to: "
operator|+
name|getProcessor
argument_list|()
operator|+
literal|"]"
return|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|createSet (Expression expression)
specifier|protected
specifier|static
name|Set
argument_list|<
name|Exchange
argument_list|>
name|createSet
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
return|return
name|createSet
argument_list|(
operator|new
name|ExpressionComparator
argument_list|(
name|expression
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createSet (List<Expression> expressions)
specifier|protected
specifier|static
name|Set
argument_list|<
name|Exchange
argument_list|>
name|createSet
parameter_list|(
name|List
argument_list|<
name|Expression
argument_list|>
name|expressions
parameter_list|)
block|{
if|if
condition|(
name|expressions
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|createSet
argument_list|(
name|expressions
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
return|return
name|createSet
argument_list|(
operator|new
name|ExpressionListComparator
argument_list|(
name|expressions
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createSet (Comparator<? super Exchange> comparator)
specifier|protected
specifier|static
name|Set
argument_list|<
name|Exchange
argument_list|>
name|createSet
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|Exchange
argument_list|>
name|comparator
parameter_list|)
block|{
return|return
operator|new
name|TreeSet
argument_list|<
name|Exchange
argument_list|>
argument_list|(
name|comparator
argument_list|)
return|;
block|}
block|}
end_class

end_unit

