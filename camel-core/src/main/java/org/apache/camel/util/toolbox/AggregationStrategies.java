begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.toolbox
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|toolbox
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
name|processor
operator|.
name|aggregate
operator|.
name|AggregationStrategy
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
name|processor
operator|.
name|aggregate
operator|.
name|AggregationStrategyBeanAdapter
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
name|processor
operator|.
name|aggregate
operator|.
name|GroupedExchangeAggregationStrategy
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
name|processor
operator|.
name|aggregate
operator|.
name|UseLatestAggregationStrategy
import|;
end_import

begin_comment
comment|/**  * Toolbox class to create commonly used Aggregation Strategies in a fluent manner.  * For more information about the supported {@link AggregationStrategy}, see links to the Javadocs of the relevant class below.  *   * @since 2.12  */
end_comment

begin_class
DECL|class|AggregationStrategies
specifier|public
specifier|final
class|class
name|AggregationStrategies
block|{
DECL|method|AggregationStrategies ()
specifier|private
name|AggregationStrategies
parameter_list|()
block|{ }
comment|/**      * Creates a {@link FlexibleAggregationStrategy} pivoting around a particular type, e.g. it casts all<tt>pick expression</tt>       * results to the desired type.      *       * @param type The type the {@link FlexibleAggregationStrategy} deals with.      */
DECL|method|flexible (Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|FlexibleAggregationStrategy
argument_list|<
name|T
argument_list|>
name|flexible
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|new
name|FlexibleAggregationStrategy
argument_list|<
name|T
argument_list|>
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|/**      * Creates a {@link FlexibleAggregationStrategy} with no particular type, i.e. performing no casts or type conversion of       *<tt>pick expression</tt> results.      */
DECL|method|flexible ()
specifier|public
specifier|static
name|FlexibleAggregationStrategy
argument_list|<
name|Object
argument_list|>
name|flexible
parameter_list|()
block|{
return|return
operator|new
name|FlexibleAggregationStrategy
argument_list|<
name|Object
argument_list|>
argument_list|()
return|;
block|}
comment|/**      * Use the latest incoming exchange.      * @see UseLatestAggregationStrategy      */
DECL|method|useLatest ()
specifier|public
specifier|static
name|AggregationStrategy
name|useLatest
parameter_list|()
block|{
return|return
operator|new
name|UseLatestAggregationStrategy
argument_list|()
return|;
block|}
comment|/**      * Creates a {@link GroupedExchangeAggregationStrategy} aggregation strategy.      */
DECL|method|groupedExchange ()
specifier|public
specifier|static
name|AggregationStrategy
name|groupedExchange
parameter_list|()
block|{
return|return
operator|new
name|GroupedExchangeAggregationStrategy
argument_list|()
return|;
block|}
comment|/**      * Creates a {@link AggregationStrategyBeanAdapter} for using a POJO as the aggregration strategy.      */
DECL|method|bean (Object bean)
specifier|public
specifier|static
name|AggregationStrategy
name|bean
parameter_list|(
name|Object
name|bean
parameter_list|)
block|{
return|return
operator|new
name|AggregationStrategyBeanAdapter
argument_list|(
name|bean
argument_list|)
return|;
block|}
comment|/**      * Creates a {@link AggregationStrategyBeanAdapter} for using a POJO as the aggregration strategy.      */
DECL|method|bean (Object bean, String methodName)
specifier|public
specifier|static
name|AggregationStrategy
name|bean
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|methodName
parameter_list|)
block|{
return|return
operator|new
name|AggregationStrategyBeanAdapter
argument_list|(
name|bean
argument_list|,
name|methodName
argument_list|)
return|;
block|}
comment|/**      * Creates a {@link AggregationStrategyBeanAdapter} for using a POJO as the aggregration strategy.      */
DECL|method|bean (Class<?> type)
specifier|public
specifier|static
name|AggregationStrategy
name|bean
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|new
name|AggregationStrategyBeanAdapter
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|/**      * Creates a {@link AggregationStrategyBeanAdapter} for using a POJO as the aggregration strategy.      */
DECL|method|bean (Class<?> type, String methodName)
specifier|public
specifier|static
name|AggregationStrategy
name|bean
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|String
name|methodName
parameter_list|)
block|{
return|return
operator|new
name|AggregationStrategyBeanAdapter
argument_list|(
name|type
argument_list|,
name|methodName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

