begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ehcache.springboot.customizer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ehcache
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
name|ehcache
operator|.
name|EhcacheComponent
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
name|ehcache
operator|.
name|springboot
operator|.
name|EhcacheComponentAutoConfiguration
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
name|ehcache
operator|.
name|CacheManager
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
name|ConditionalOnProperty
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

begin_comment
comment|/**  * A simple implementation of {@link ComponentCustomizer} that auto discovers a  * {@link CacheManager} instance and bind it to the {@link EhcacheComponent}  * component.  *  * This configurer can be disabled either by disable all the  */
end_comment

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
name|ConditionalOnProperty
argument_list|(
name|name
operator|=
literal|"camel.component.ehcache.configurer.cache-manager.enabled"
argument_list|,
name|matchIfMissing
operator|=
literal|true
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
name|EhcacheComponentAutoConfiguration
operator|.
name|class
argument_list|)
DECL|class|CacheManagerCustomizer
specifier|public
class|class
name|CacheManagerCustomizer
extends|extends
name|AllNestedConditions
implements|implements
name|ComponentCustomizer
argument_list|<
name|EhcacheComponent
argument_list|>
block|{
annotation|@
name|Autowired
DECL|field|cacheManager
specifier|private
name|CacheManager
name|cacheManager
decl_stmt|;
DECL|method|CacheManagerCustomizer ()
specifier|public
name|CacheManagerCustomizer
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
name|Override
DECL|method|customize (EhcacheComponent component)
specifier|public
name|void
name|customize
parameter_list|(
name|EhcacheComponent
name|component
parameter_list|)
block|{
name|component
operator|.
name|setCacheManager
argument_list|(
name|cacheManager
argument_list|)
expr_stmt|;
block|}
comment|// *************************************************************************
comment|// By default ConditionalOnBean works using an OR operation so if you list
comment|// a number of classes, the condition succeeds if a single instance of the
comment|// classes is found.
comment|//
comment|// A workaround is to use AllNestedConditions and creates some dummy classes
comment|// annotated @ConditionalOnBean
comment|//
comment|// This should be fixed in spring-boot 2.0 where ConditionalOnBean uses and
comment|// AND operation instead of the OR as it does today.
comment|// *************************************************************************
annotation|@
name|ConditionalOnBean
argument_list|(
name|CacheManager
operator|.
name|class
argument_list|)
DECL|class|OnCacheManager
specifier|static
class|class
name|OnCacheManager
block|{     }
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
block|{     }
block|}
end_class

end_unit

