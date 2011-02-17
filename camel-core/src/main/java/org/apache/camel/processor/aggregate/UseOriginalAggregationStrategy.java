begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * An {@link org.apache.camel.processor.aggregate.AggregationStrategy} which just uses the original exchange  * which can be needed when you want to preserve the original Exchange. For example when splitting an {@link Exchange}  * and then you may want to keep routing using the original {@link Exchange}.  *  * @see org.apache.camel.processor.Splitter  * @version   */
end_comment

begin_class
DECL|class|UseOriginalAggregationStrategy
specifier|public
class|class
name|UseOriginalAggregationStrategy
implements|implements
name|AggregationStrategy
block|{
DECL|field|original
specifier|private
specifier|final
name|Exchange
name|original
decl_stmt|;
DECL|field|propagateException
specifier|private
specifier|final
name|boolean
name|propagateException
decl_stmt|;
DECL|method|UseOriginalAggregationStrategy (Exchange original, boolean propagateException)
specifier|public
name|UseOriginalAggregationStrategy
parameter_list|(
name|Exchange
name|original
parameter_list|,
name|boolean
name|propagateException
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|original
argument_list|,
literal|"Original Exchange"
argument_list|)
expr_stmt|;
name|this
operator|.
name|original
operator|=
name|original
expr_stmt|;
name|this
operator|.
name|propagateException
operator|=
name|propagateException
expr_stmt|;
block|}
DECL|method|aggregate (Exchange oldExchange, Exchange newExchange)
specifier|public
name|Exchange
name|aggregate
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
if|if
condition|(
name|propagateException
condition|)
block|{
name|Exception
name|exception
init|=
name|checkException
argument_list|(
name|oldExchange
argument_list|,
name|newExchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|exception
operator|!=
literal|null
condition|)
block|{
name|original
operator|.
name|setException
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|original
return|;
block|}
DECL|method|checkException (Exchange oldExchange, Exchange newExchange)
specifier|protected
name|Exception
name|checkException
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
if|if
condition|(
name|oldExchange
operator|==
literal|null
condition|)
block|{
return|return
name|newExchange
operator|.
name|getException
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|(
name|newExchange
operator|!=
literal|null
operator|&&
name|newExchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
operator|)
condition|?
name|newExchange
operator|.
name|getException
argument_list|()
else|:
name|oldExchange
operator|.
name|getException
argument_list|()
return|;
block|}
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
literal|"UseOriginalAggregationStrategy"
return|;
block|}
block|}
end_class

end_unit

