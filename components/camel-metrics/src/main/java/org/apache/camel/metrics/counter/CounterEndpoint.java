begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.metrics.counter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|metrics
operator|.
name|counter
package|;
end_package

begin_import
import|import
name|com
operator|.
name|codahale
operator|.
name|metrics
operator|.
name|MetricRegistry
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
name|Producer
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
name|metrics
operator|.
name|AbstractMetricsEndpoint
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
import|;
end_import

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"metrics"
argument_list|)
DECL|class|CounterEndpoint
specifier|public
class|class
name|CounterEndpoint
extends|extends
name|AbstractMetricsEndpoint
block|{
DECL|field|ENDPOINT_URI
specifier|public
specifier|static
specifier|final
name|String
name|ENDPOINT_URI
init|=
literal|"metrics:counter"
decl_stmt|;
annotation|@
name|UriParam
DECL|field|increment
specifier|private
name|Long
name|increment
decl_stmt|;
annotation|@
name|UriParam
DECL|field|decrement
specifier|private
name|Long
name|decrement
decl_stmt|;
DECL|method|CounterEndpoint (MetricRegistry registry, String metricsName)
specifier|public
name|CounterEndpoint
parameter_list|(
name|MetricRegistry
name|registry
parameter_list|,
name|String
name|metricsName
parameter_list|)
block|{
name|super
argument_list|(
name|registry
argument_list|,
name|metricsName
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|CounterProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|getIncrement ()
specifier|public
name|Long
name|getIncrement
parameter_list|()
block|{
return|return
name|increment
return|;
block|}
DECL|method|setIncrement (Long increment)
specifier|public
name|void
name|setIncrement
parameter_list|(
name|Long
name|increment
parameter_list|)
block|{
name|this
operator|.
name|increment
operator|=
name|increment
expr_stmt|;
block|}
DECL|method|getDecrement ()
specifier|public
name|Long
name|getDecrement
parameter_list|()
block|{
return|return
name|decrement
return|;
block|}
DECL|method|setDecrement (Long decrement)
specifier|public
name|void
name|setDecrement
parameter_list|(
name|Long
name|decrement
parameter_list|)
block|{
name|this
operator|.
name|decrement
operator|=
name|decrement
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
name|ENDPOINT_URI
return|;
block|}
block|}
end_class

end_unit

