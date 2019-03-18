begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|UseLatestAggregationStrategy
import|;
end_import

begin_comment
comment|/** */
end_comment

begin_class
DECL|class|MyAggregationStrategy
specifier|public
class|class
name|MyAggregationStrategy
extends|extends
name|UseLatestAggregationStrategy
block|{
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
name|Exchange
name|result
init|=
name|super
operator|.
name|aggregate
argument_list|(
name|oldExchange
argument_list|,
name|newExchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldExchange
operator|==
literal|null
condition|)
block|{
name|result
operator|.
name|setProperty
argument_list|(
literal|"aggregated"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Integer
name|old
init|=
name|oldExchange
operator|.
name|getProperty
argument_list|(
literal|"aggregated"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|result
operator|.
name|setProperty
argument_list|(
literal|"aggregated"
argument_list|,
name|old
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

