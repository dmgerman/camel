begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.groovy.extend
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|groovy
operator|.
name|extend
package|;
end_package

begin_import
import|import
name|groovy
operator|.
name|lang
operator|.
name|Closure
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
name|processor
operator|.
name|aggregate
operator|.
name|AggregationStrategy
import|;
end_import

begin_class
DECL|class|ClosureAggregationStrategy
class|class
name|ClosureAggregationStrategy
implements|implements
name|AggregationStrategy
block|{
DECL|field|closure
specifier|private
specifier|final
name|Closure
argument_list|<
name|Exchange
argument_list|>
name|closure
decl_stmt|;
DECL|method|ClosureAggregationStrategy (Closure<Exchange> closure)
name|ClosureAggregationStrategy
parameter_list|(
name|Closure
argument_list|<
name|Exchange
argument_list|>
name|closure
parameter_list|)
block|{
name|this
operator|.
name|closure
operator|=
name|closure
expr_stmt|;
block|}
annotation|@
name|Override
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
return|return
name|ClosureSupport
operator|.
name|call
argument_list|(
name|closure
argument_list|,
name|oldExchange
argument_list|,
name|newExchange
argument_list|)
return|;
block|}
block|}
end_class

end_unit

