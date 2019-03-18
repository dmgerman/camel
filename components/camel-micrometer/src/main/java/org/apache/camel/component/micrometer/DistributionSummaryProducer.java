begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.micrometer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|micrometer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|DistributionSummary
import|;
end_import

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|MeterRegistry
import|;
end_import

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|Tag
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|micrometer
operator|.
name|MicrometerConstants
operator|.
name|HEADER_HISTOGRAM_VALUE
import|;
end_import

begin_class
DECL|class|DistributionSummaryProducer
specifier|public
class|class
name|DistributionSummaryProducer
extends|extends
name|AbstractMicrometerProducer
argument_list|<
name|DistributionSummary
argument_list|>
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DistributionSummaryProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|DistributionSummaryProducer (MicrometerEndpoint endpoint)
specifier|public
name|DistributionSummaryProducer
parameter_list|(
name|MicrometerEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|registrar (String name, Iterable<Tag> tags)
specifier|protected
name|Function
argument_list|<
name|MeterRegistry
argument_list|,
name|DistributionSummary
argument_list|>
name|registrar
parameter_list|(
name|String
name|name
parameter_list|,
name|Iterable
argument_list|<
name|Tag
argument_list|>
name|tags
parameter_list|)
block|{
return|return
name|meterRegistry
lambda|->
name|meterRegistry
operator|.
name|summary
argument_list|(
name|name
argument_list|,
name|tags
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doProcess (Exchange exchange, MicrometerEndpoint endpoint, DistributionSummary summary)
specifier|protected
name|void
name|doProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|MicrometerEndpoint
name|endpoint
parameter_list|,
name|DistributionSummary
name|summary
parameter_list|)
block|{
name|Double
name|value
init|=
name|simple
argument_list|(
name|exchange
argument_list|,
name|endpoint
operator|.
name|getValue
argument_list|()
argument_list|,
name|Double
operator|.
name|class
argument_list|)
decl_stmt|;
name|Double
name|finalValue
init|=
name|getDoubleHeader
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|HEADER_HISTOGRAM_VALUE
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|finalValue
operator|!=
literal|null
condition|)
block|{
name|summary
operator|.
name|record
argument_list|(
name|finalValue
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Cannot update histogram \"{}\" with null value"
argument_list|,
name|summary
operator|.
name|getId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

