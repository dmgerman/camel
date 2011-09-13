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
comment|/**  * An implementation of the<a href="http://camel.apache.org/resequencer.html">Resequencer</a>  * which can reorder messages within a batch.  *  * @version   */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|class|Resequencer
specifier|public
class|class
name|Resequencer
extends|extends
name|BatchProcessor
implements|implements
name|Traceable
block|{
comment|// TODO: Rework to avoid using BatchProcessor
DECL|method|Resequencer (CamelContext camelContext, Processor processor, Expression expression)
specifier|public
name|Resequencer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
name|this
argument_list|(
name|camelContext
argument_list|,
name|processor
argument_list|,
name|createSet
argument_list|(
name|expression
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
argument_list|,
name|expression
argument_list|)
expr_stmt|;
block|}
DECL|method|Resequencer (CamelContext camelContext, Processor processor, Expression expression, boolean allowDuplicates, boolean reverse)
specifier|public
name|Resequencer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|Expression
name|expression
parameter_list|,
name|boolean
name|allowDuplicates
parameter_list|,
name|boolean
name|reverse
parameter_list|)
block|{
name|this
argument_list|(
name|camelContext
argument_list|,
name|processor
argument_list|,
name|createSet
argument_list|(
name|expression
argument_list|,
name|allowDuplicates
argument_list|,
name|reverse
argument_list|)
argument_list|,
name|expression
argument_list|)
expr_stmt|;
block|}
DECL|method|Resequencer (CamelContext camelContext, Processor processor, Set<Exchange> collection, Expression expression)
specifier|public
name|Resequencer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|Set
argument_list|<
name|Exchange
argument_list|>
name|collection
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|,
name|processor
argument_list|,
name|collection
argument_list|,
name|expression
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
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
literal|"resequencer"
return|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|createSet (Expression expression, boolean allowDuplicates, boolean reverse)
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
parameter_list|,
name|boolean
name|allowDuplicates
parameter_list|,
name|boolean
name|reverse
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
argument_list|,
name|allowDuplicates
argument_list|,
name|reverse
argument_list|)
return|;
block|}
DECL|method|createSet (final Comparator<? super Exchange> comparator, boolean allowDuplicates, boolean reverse)
specifier|protected
specifier|static
name|Set
argument_list|<
name|Exchange
argument_list|>
name|createSet
parameter_list|(
specifier|final
name|Comparator
argument_list|<
name|?
super|super
name|Exchange
argument_list|>
name|comparator
parameter_list|,
name|boolean
name|allowDuplicates
parameter_list|,
name|boolean
name|reverse
parameter_list|)
block|{
name|Comparator
argument_list|<
name|?
super|super
name|Exchange
argument_list|>
name|answer
init|=
name|comparator
decl_stmt|;
if|if
condition|(
name|reverse
condition|)
block|{
name|answer
operator|=
operator|new
name|Comparator
argument_list|<
name|Exchange
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Exchange
name|o1
parameter_list|,
name|Exchange
name|o2
parameter_list|)
block|{
name|int
name|answer
init|=
name|comparator
operator|.
name|compare
argument_list|(
name|o1
argument_list|,
name|o2
argument_list|)
decl_stmt|;
comment|// reverse it
return|return
name|answer
operator|*
operator|-
literal|1
return|;
block|}
block|}
expr_stmt|;
block|}
comment|// if we allow duplicates then we need to cater for that in the comparator
specifier|final
name|Comparator
argument_list|<
name|?
super|super
name|Exchange
argument_list|>
name|forAllowDuplicates
init|=
name|answer
decl_stmt|;
if|if
condition|(
name|allowDuplicates
condition|)
block|{
name|answer
operator|=
operator|new
name|Comparator
argument_list|<
name|Exchange
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Exchange
name|o1
parameter_list|,
name|Exchange
name|o2
parameter_list|)
block|{
name|int
name|answer
init|=
name|forAllowDuplicates
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
operator|==
literal|0
condition|)
block|{
comment|// they are equal but we should allow duplicates so say that o2 is higher
comment|// so it will come next
return|return
literal|1
return|;
block|}
return|return
name|answer
return|;
block|}
block|}
expr_stmt|;
block|}
return|return
operator|new
name|TreeSet
argument_list|<
name|Exchange
argument_list|>
argument_list|(
name|answer
argument_list|)
return|;
block|}
block|}
end_class

end_unit

