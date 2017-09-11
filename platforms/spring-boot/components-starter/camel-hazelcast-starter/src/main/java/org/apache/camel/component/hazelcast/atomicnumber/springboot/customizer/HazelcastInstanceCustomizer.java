begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast.atomicnumber.springboot.customizer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
operator|.
name|atomicnumber
operator|.
name|springboot
operator|.
name|customizer
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
name|component
operator|.
name|hazelcast
operator|.
name|atomicnumber
operator|.
name|HazelcastAtomicnumberComponent
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
name|hazelcast
operator|.
name|atomicnumber
operator|.
name|springboot
operator|.
name|HazelcastAtomicnumberComponentAutoConfiguration
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
name|hazelcast
operator|.
name|springboot
operator|.
name|customizer
operator|.
name|AbstractHazelcastInstanceCustomizer
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
name|spring
operator|.
name|boot
operator|.
name|CamelAutoConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|AutoConfigureAfter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|AutoConfigureBefore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|EnableConfigurationProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Conditional
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|Ordered
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|annotation
operator|.
name|Order
import|;
end_import

begin_class
annotation|@
name|Order
argument_list|(
name|Ordered
operator|.
name|LOWEST_PRECEDENCE
argument_list|)
annotation|@
name|Configuration
annotation|@
name|Conditional
argument_list|(
name|HazelcastInstanceCustomizer
operator|.
name|NestedConditions
operator|.
name|class
argument_list|)
annotation|@
name|AutoConfigureAfter
argument_list|(
name|CamelAutoConfiguration
operator|.
name|class
argument_list|)
annotation|@
name|AutoConfigureBefore
argument_list|(
name|HazelcastAtomicnumberComponentAutoConfiguration
operator|.
name|class
argument_list|)
annotation|@
name|EnableConfigurationProperties
argument_list|(
name|HazelcastInstanceCustomizerConfiguration
operator|.
name|class
argument_list|)
DECL|class|HazelcastInstanceCustomizer
specifier|public
class|class
name|HazelcastInstanceCustomizer
extends|extends
name|AbstractHazelcastInstanceCustomizer
argument_list|<
name|HazelcastAtomicnumberComponent
argument_list|,
name|HazelcastInstanceCustomizerConfiguration
argument_list|>
block|{
annotation|@
name|Override
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
literal|"camel.component.hazelcast-atomicvalue.customizer.hazelcast-instance"
return|;
block|}
block|}
end_class

end_unit

