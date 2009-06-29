begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.enricher
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|enricher
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
name|AggregationStrategy
import|;
end_import

begin_class
DECL|class|SampleAggregator
specifier|public
class|class
name|SampleAggregator
implements|implements
name|AggregationStrategy
block|{
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
name|newExchange
operator|==
literal|null
condition|)
block|{
return|return
name|oldExchange
return|;
block|}
name|Object
name|oldBody
init|=
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|Object
name|newBody
init|=
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|oldBody
operator|+
literal|":"
operator|+
name|newBody
argument_list|)
expr_stmt|;
return|return
name|oldExchange
return|;
block|}
block|}
end_class

end_unit

