begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.micrometer.messagehistory
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
operator|.
name|messagehistory
package|;
end_package

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
name|Timer
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
name|MessageHistory
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
name|NamedNode
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
name|Route
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
name|impl
operator|.
name|DefaultMessageHistory
import|;
end_import

begin_comment
comment|/**  * A micrometer metrics based {@link MessageHistory}. This could also use {@link #elapsed}  * provided by the super class, but Micrometer can potentially use other {@link io.micrometer.core.instrument.Clock clocks}  * and measures in nano-second precision.  */
end_comment

begin_class
DECL|class|MicrometerMessageHistory
specifier|public
class|class
name|MicrometerMessageHistory
extends|extends
name|DefaultMessageHistory
block|{
DECL|field|route
specifier|private
specifier|final
name|Route
name|route
decl_stmt|;
DECL|field|sample
specifier|private
specifier|final
name|Timer
operator|.
name|Sample
name|sample
decl_stmt|;
DECL|field|meterRegistry
specifier|private
specifier|final
name|MeterRegistry
name|meterRegistry
decl_stmt|;
DECL|field|namingStrategy
specifier|private
specifier|final
name|MicrometerMessageHistoryNamingStrategy
name|namingStrategy
decl_stmt|;
DECL|method|MicrometerMessageHistory (MeterRegistry meterRegistry, Route route, NamedNode namedNode, MicrometerMessageHistoryNamingStrategy namingStrategy, long timestamp)
specifier|public
name|MicrometerMessageHistory
parameter_list|(
name|MeterRegistry
name|meterRegistry
parameter_list|,
name|Route
name|route
parameter_list|,
name|NamedNode
name|namedNode
parameter_list|,
name|MicrometerMessageHistoryNamingStrategy
name|namingStrategy
parameter_list|,
name|long
name|timestamp
parameter_list|)
block|{
name|super
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|namedNode
argument_list|,
name|timestamp
argument_list|)
expr_stmt|;
name|this
operator|.
name|meterRegistry
operator|=
name|meterRegistry
expr_stmt|;
name|this
operator|.
name|route
operator|=
name|route
expr_stmt|;
name|this
operator|.
name|namingStrategy
operator|=
name|namingStrategy
expr_stmt|;
name|this
operator|.
name|sample
operator|=
name|Timer
operator|.
name|start
argument_list|(
name|meterRegistry
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|nodeProcessingDone ()
specifier|public
name|void
name|nodeProcessingDone
parameter_list|()
block|{
name|super
operator|.
name|nodeProcessingDone
argument_list|()
expr_stmt|;
name|Timer
name|timer
init|=
name|Timer
operator|.
name|builder
argument_list|(
name|namingStrategy
operator|.
name|getName
argument_list|(
name|route
argument_list|,
name|getNode
argument_list|()
argument_list|)
argument_list|)
operator|.
name|tags
argument_list|(
name|namingStrategy
operator|.
name|getTags
argument_list|(
name|route
argument_list|,
name|getNode
argument_list|()
argument_list|)
argument_list|)
operator|.
name|description
argument_list|(
name|getNode
argument_list|()
operator|.
name|getDescriptionText
argument_list|()
argument_list|)
operator|.
name|register
argument_list|(
name|meterRegistry
argument_list|)
decl_stmt|;
name|sample
operator|.
name|stop
argument_list|(
name|timer
argument_list|)
expr_stmt|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"MicrometerMessageHistory[routeId="
operator|+
name|getRouteId
argument_list|()
operator|+
literal|", node="
operator|+
name|getNode
argument_list|()
operator|.
name|getId
argument_list|()
operator|+
literal|']'
return|;
block|}
block|}
end_class

end_unit

