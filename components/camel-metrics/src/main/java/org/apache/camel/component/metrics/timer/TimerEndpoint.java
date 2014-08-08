begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.metrics.timer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|metrics
operator|.
name|timer
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
name|component
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
DECL|class|TimerEndpoint
specifier|public
class|class
name|TimerEndpoint
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
literal|"metrics:timer"
decl_stmt|;
DECL|enum|TimerAction
specifier|public
enum|enum
name|TimerAction
block|{
DECL|enumConstant|start
name|start
block|,
DECL|enumConstant|stop
name|stop
block|;     }
annotation|@
name|UriParam
DECL|field|action
specifier|private
name|TimerAction
name|action
decl_stmt|;
DECL|method|TimerEndpoint (MetricRegistry registry, String metricsName)
specifier|public
name|TimerEndpoint
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
name|TimerProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|getAction ()
specifier|public
name|TimerAction
name|getAction
parameter_list|()
block|{
return|return
name|action
return|;
block|}
DECL|method|setAction (TimerAction action)
specifier|public
name|void
name|setAction
parameter_list|(
name|TimerAction
name|action
parameter_list|)
block|{
name|this
operator|.
name|action
operator|=
name|action
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

