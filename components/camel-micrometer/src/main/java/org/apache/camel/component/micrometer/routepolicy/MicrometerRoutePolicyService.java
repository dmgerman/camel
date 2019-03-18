begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.micrometer.routepolicy
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
name|routepolicy
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContextAware
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
name|StaticService
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|micrometer
operator|.
name|json
operator|.
name|AbstractMicrometerService
import|;
end_import

begin_comment
comment|/**  * Service holding the {@link MeterRegistry} which registers all metrics.  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"MicrometerRoutePolicy"
argument_list|)
DECL|class|MicrometerRoutePolicyService
specifier|public
specifier|final
class|class
name|MicrometerRoutePolicyService
extends|extends
name|AbstractMicrometerService
implements|implements
name|CamelContextAware
implements|,
name|StaticService
implements|,
name|MicrometerRoutePolicyMBean
block|{ }
end_class

end_unit

