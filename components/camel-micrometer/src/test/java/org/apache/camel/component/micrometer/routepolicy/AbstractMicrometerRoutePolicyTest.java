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
name|Clock
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
name|composite
operator|.
name|CompositeMeterRegistry
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
name|simple
operator|.
name|SimpleMeterRegistry
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
name|util
operator|.
name|HierarchicalNameMapper
import|;
end_import

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|jmx
operator|.
name|JmxMeterRegistry
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
name|BindToRegistry
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
name|CamelContext
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
name|CamelJmxConfig
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
name|MicrometerConstants
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_class
DECL|class|AbstractMicrometerRoutePolicyTest
specifier|public
class|class
name|AbstractMicrometerRoutePolicyTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|meterRegistry
specifier|protected
name|CompositeMeterRegistry
name|meterRegistry
decl_stmt|;
annotation|@
name|Override
DECL|method|useJmx ()
specifier|protected
name|boolean
name|useJmx
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|BindToRegistry
argument_list|(
name|MicrometerConstants
operator|.
name|METRICS_REGISTRY_NAME
argument_list|)
DECL|method|addRegistry ()
specifier|public
name|CompositeMeterRegistry
name|addRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|meterRegistry
operator|=
operator|new
name|CompositeMeterRegistry
argument_list|()
expr_stmt|;
name|meterRegistry
operator|.
name|add
argument_list|(
operator|new
name|SimpleMeterRegistry
argument_list|()
argument_list|)
expr_stmt|;
name|meterRegistry
operator|.
name|add
argument_list|(
operator|new
name|JmxMeterRegistry
argument_list|(
name|CamelJmxConfig
operator|.
name|DEFAULT
argument_list|,
name|Clock
operator|.
name|SYSTEM
argument_list|,
name|HierarchicalNameMapper
operator|.
name|DEFAULT
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|meterRegistry
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|MicrometerRoutePolicyFactory
name|factory
init|=
operator|new
name|MicrometerRoutePolicyFactory
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setMeterRegistry
argument_list|(
name|meterRegistry
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutePolicyFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
block|}
end_class

end_unit

