begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast.springboot.customizer
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
name|springboot
operator|.
name|customizer
package|;
end_package

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|HazelcastInstance
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
name|HazelcastDefaultComponent
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
name|ComponentCustomizer
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
name|HasId
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
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
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
name|condition
operator|.
name|AllNestedConditions
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
name|condition
operator|.
name|ConditionalOnBean
import|;
end_import

begin_class
DECL|class|AbstractHazelcastInstanceCustomizer
specifier|public
specifier|abstract
class|class
name|AbstractHazelcastInstanceCustomizer
parameter_list|<
name|T
extends|extends
name|HazelcastDefaultComponent
parameter_list|,
name|C
extends|extends
name|AbstractHazelcastInstanceCustomizerConfiguration
parameter_list|>
implements|implements
name|HasId
implements|,
name|ComponentCustomizer
argument_list|<
name|T
argument_list|>
block|{
annotation|@
name|Autowired
DECL|field|hazelcastInstance
specifier|private
name|HazelcastInstance
name|hazelcastInstance
decl_stmt|;
annotation|@
name|Autowired
DECL|field|configuration
specifier|private
name|C
name|configuration
decl_stmt|;
annotation|@
name|Override
DECL|method|customize (T component)
specifier|public
name|void
name|customize
parameter_list|(
name|T
name|component
parameter_list|)
block|{
comment|// Set the cache manager only if the customizer is configured to always
comment|// set it or if no cache manager is already configured on component
if|if
condition|(
name|configuration
operator|.
name|isOverride
argument_list|()
operator|||
name|component
operator|.
name|getHazelcastInstance
argument_list|()
operator|==
literal|null
condition|)
block|{
name|component
operator|.
name|setHazelcastInstance
argument_list|(
name|hazelcastInstance
argument_list|)
expr_stmt|;
block|}
block|}
comment|// *************************************************************************
comment|// By default ConditionalOnBean works using an OR operation so if you list
comment|// a number of classes, the condition succeeds if a single instance of any
comment|// class is found.
comment|//
comment|// A workaround is to use AllNestedConditions and creates some dummy classes
comment|// annotated with @ConditionalOnBean
comment|//
comment|// This should be fixed in spring-boot 2.0 where ConditionalOnBean uses and
comment|// AND operation instead of the OR as it does today.
comment|// *************************************************************************
DECL|class|NestedConditions
specifier|public
specifier|static
class|class
name|NestedConditions
extends|extends
name|AllNestedConditions
block|{
DECL|method|NestedConditions ()
specifier|public
name|NestedConditions
parameter_list|()
block|{
name|super
argument_list|(
name|ConfigurationPhase
operator|.
name|REGISTER_BEAN
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ConditionalOnBean
argument_list|(
name|HazelcastInstance
operator|.
name|class
argument_list|)
DECL|class|OnHazelcastInstance
specifier|static
class|class
name|OnHazelcastInstance
block|{         }
annotation|@
name|ConditionalOnBean
argument_list|(
name|CamelAutoConfiguration
operator|.
name|class
argument_list|)
DECL|class|OnCamelAutoConfiguration
specifier|static
class|class
name|OnCamelAutoConfiguration
block|{         }
block|}
block|}
end_class

end_unit

