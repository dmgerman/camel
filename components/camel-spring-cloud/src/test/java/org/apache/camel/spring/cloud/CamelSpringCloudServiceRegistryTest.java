begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|cloud
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ThreadLocalRandom
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
name|cloud
operator|.
name|ServiceDefinition
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
name|cloud
operator|.
name|DefaultServiceDefinition
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
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|cloud
operator|.
name|CamelCloudAutoConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
name|AutoConfigurations
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
name|test
operator|.
name|context
operator|.
name|runner
operator|.
name|ApplicationContextRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|DefaultServiceInstance
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|serviceregistry
operator|.
name|Registration
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
name|Bean
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
name|convert
operator|.
name|converter
operator|.
name|Converter
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertThat
import|;
end_import

begin_class
DECL|class|CamelSpringCloudServiceRegistryTest
specifier|public
class|class
name|CamelSpringCloudServiceRegistryTest
block|{
comment|// *************************************
comment|// Test Auto Configuration
comment|// *************************************
annotation|@
name|Test
DECL|method|testAutoConfiguration ()
specifier|public
name|void
name|testAutoConfiguration
parameter_list|()
block|{
operator|new
name|ApplicationContextRunner
argument_list|()
operator|.
name|withConfiguration
argument_list|(
name|AutoConfigurations
operator|.
name|of
argument_list|(
name|CamelAutoConfiguration
operator|.
name|class
argument_list|,
name|CamelCloudAutoConfiguration
operator|.
name|class
argument_list|,
name|CamelSpringCloudServiceRegistryAutoConfiguration
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|withUserConfiguration
argument_list|(
name|TestConfiguration
operator|.
name|class
argument_list|)
operator|.
name|withPropertyValues
argument_list|(
literal|"spring.main.banner-mode=off"
argument_list|,
literal|"ribbon.eureka.enabled=false"
argument_list|,
literal|"ribbon.enabled=false"
argument_list|)
operator|.
name|run
argument_list|(
name|context
lambda|->
block|{
comment|// spring cloud registry
name|assertThat
argument_list|(
name|context
argument_list|)
operator|.
name|hasSingleBean
argument_list|(
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|serviceregistry
operator|.
name|ServiceRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|context
argument_list|)
operator|.
name|getBean
argument_list|(
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|serviceregistry
operator|.
name|ServiceRegistry
operator|.
name|class
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|MyServiceRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// camel registry
name|assertThat
argument_list|(
name|context
argument_list|)
operator|.
name|hasSingleBean
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cloud
operator|.
name|ServiceRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|context
argument_list|)
operator|.
name|getBean
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cloud
operator|.
name|ServiceRegistry
operator|.
name|class
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|CamelSpringCloudServiceRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|context
argument_list|)
operator|.
name|getBean
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cloud
operator|.
name|ServiceRegistry
operator|.
name|class
argument_list|)
operator|.
name|hasFieldOrPropertyWithValue
argument_list|(
literal|"nativeServiceRegistry"
argument_list|,
name|context
operator|.
name|getBean
argument_list|(
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|serviceregistry
operator|.
name|ServiceRegistry
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDisabledCamelCloud ()
specifier|public
name|void
name|testDisabledCamelCloud
parameter_list|()
block|{
operator|new
name|ApplicationContextRunner
argument_list|()
operator|.
name|withConfiguration
argument_list|(
name|AutoConfigurations
operator|.
name|of
argument_list|(
name|CamelAutoConfiguration
operator|.
name|class
argument_list|,
name|CamelCloudAutoConfiguration
operator|.
name|class
argument_list|,
name|CamelSpringCloudServiceRegistryAutoConfiguration
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|withUserConfiguration
argument_list|(
name|TestConfiguration
operator|.
name|class
argument_list|)
operator|.
name|withPropertyValues
argument_list|(
literal|"spring.main.banner-mode=off"
argument_list|,
literal|"ribbon.eureka.enabled=false"
argument_list|,
literal|"ribbon.enabled=false"
argument_list|,
literal|"camel.cloud.enabled=false"
argument_list|)
operator|.
name|run
argument_list|(
name|context
lambda|->
block|{
comment|// spring cloud registry
name|assertThat
argument_list|(
name|context
argument_list|)
operator|.
name|hasSingleBean
argument_list|(
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|serviceregistry
operator|.
name|ServiceRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|context
argument_list|)
operator|.
name|getBean
argument_list|(
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|serviceregistry
operator|.
name|ServiceRegistry
operator|.
name|class
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|MyServiceRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// camel registry
name|assertThat
argument_list|(
name|context
argument_list|)
operator|.
name|doesNotHaveBean
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cloud
operator|.
name|ServiceRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDisabledCamelServiceRegistry ()
specifier|public
name|void
name|testDisabledCamelServiceRegistry
parameter_list|()
block|{
operator|new
name|ApplicationContextRunner
argument_list|()
operator|.
name|withConfiguration
argument_list|(
name|AutoConfigurations
operator|.
name|of
argument_list|(
name|CamelAutoConfiguration
operator|.
name|class
argument_list|,
name|CamelCloudAutoConfiguration
operator|.
name|class
argument_list|,
name|CamelSpringCloudServiceRegistryAutoConfiguration
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|withUserConfiguration
argument_list|(
name|TestConfiguration
operator|.
name|class
argument_list|)
operator|.
name|withPropertyValues
argument_list|(
literal|"spring.main.banner-mode=off"
argument_list|,
literal|"ribbon.eureka.enabled=false"
argument_list|,
literal|"ribbon.enabled=false"
argument_list|,
literal|"camel.cloud.enabled=true"
argument_list|,
literal|"camel.cloud.service-registry.enabled=false"
argument_list|)
operator|.
name|run
argument_list|(
name|context
lambda|->
block|{
comment|// spring cloud registry
name|assertThat
argument_list|(
name|context
argument_list|)
operator|.
name|hasSingleBean
argument_list|(
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|serviceregistry
operator|.
name|ServiceRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|context
argument_list|)
operator|.
name|getBean
argument_list|(
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|serviceregistry
operator|.
name|ServiceRegistry
operator|.
name|class
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|MyServiceRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// camel registry
name|assertThat
argument_list|(
name|context
argument_list|)
operator|.
name|doesNotHaveBean
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cloud
operator|.
name|ServiceRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEnabledCamelServiceRegistry ()
specifier|public
name|void
name|testEnabledCamelServiceRegistry
parameter_list|()
block|{
operator|new
name|ApplicationContextRunner
argument_list|()
operator|.
name|withConfiguration
argument_list|(
name|AutoConfigurations
operator|.
name|of
argument_list|(
name|CamelAutoConfiguration
operator|.
name|class
argument_list|,
name|CamelCloudAutoConfiguration
operator|.
name|class
argument_list|,
name|CamelSpringCloudServiceRegistryAutoConfiguration
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|withUserConfiguration
argument_list|(
name|TestConfiguration
operator|.
name|class
argument_list|)
operator|.
name|withPropertyValues
argument_list|(
literal|"spring.main.banner-mode=off"
argument_list|,
literal|"ribbon.eureka.enabled=false"
argument_list|,
literal|"ribbon.enabled=false"
argument_list|,
literal|"camel.cloud.enabled=false"
argument_list|,
literal|"camel.cloud.service-registry.enabled=true"
argument_list|)
operator|.
name|run
argument_list|(
name|context
lambda|->
block|{
comment|// spring cloud registry
name|assertThat
argument_list|(
name|context
argument_list|)
operator|.
name|hasSingleBean
argument_list|(
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|serviceregistry
operator|.
name|ServiceRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|context
argument_list|)
operator|.
name|getBean
argument_list|(
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|serviceregistry
operator|.
name|ServiceRegistry
operator|.
name|class
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|MyServiceRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// camel registry
name|assertThat
argument_list|(
name|context
argument_list|)
operator|.
name|hasSingleBean
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cloud
operator|.
name|ServiceRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|context
argument_list|)
operator|.
name|getBean
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cloud
operator|.
name|ServiceRegistry
operator|.
name|class
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|CamelSpringCloudServiceRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|context
argument_list|)
operator|.
name|getBean
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cloud
operator|.
name|ServiceRegistry
operator|.
name|class
argument_list|)
operator|.
name|hasFieldOrPropertyWithValue
argument_list|(
literal|"nativeServiceRegistry"
argument_list|,
name|context
operator|.
name|getBean
argument_list|(
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|serviceregistry
operator|.
name|ServiceRegistry
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
comment|// *************************************
comment|// Test Registry
comment|// *************************************
annotation|@
name|Test
DECL|method|testServiceRegistry ()
specifier|public
name|void
name|testServiceRegistry
parameter_list|()
block|{
operator|new
name|ApplicationContextRunner
argument_list|()
operator|.
name|withConfiguration
argument_list|(
name|AutoConfigurations
operator|.
name|of
argument_list|(
name|CamelAutoConfiguration
operator|.
name|class
argument_list|,
name|CamelCloudAutoConfiguration
operator|.
name|class
argument_list|,
name|CamelSpringCloudServiceRegistryAutoConfiguration
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|withUserConfiguration
argument_list|(
name|TestConfiguration
operator|.
name|class
argument_list|)
operator|.
name|withPropertyValues
argument_list|(
literal|"spring.main.banner-mode=off"
argument_list|,
literal|"ribbon.eureka.enabled=false"
argument_list|,
literal|"ribbon.enabled=false"
argument_list|)
operator|.
name|run
argument_list|(
name|context
lambda|->
block|{
name|CamelSpringCloudServiceRegistry
name|camelRgistry
init|=
name|context
operator|.
name|getBean
argument_list|(
name|CamelSpringCloudServiceRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|String
name|serviceName
init|=
literal|"my-.service"
decl_stmt|;
specifier|final
name|String
name|serviceId
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
specifier|final
name|int
name|port
init|=
name|ThreadLocalRandom
operator|.
name|current
argument_list|()
operator|.
name|nextInt
argument_list|()
decl_stmt|;
name|camelRgistry
operator|.
name|register
argument_list|(
name|DefaultServiceDefinition
operator|.
name|builder
argument_list|()
operator|.
name|withHost
argument_list|(
literal|"localhost"
argument_list|)
operator|.
name|withPort
argument_list|(
name|port
argument_list|)
operator|.
name|withName
argument_list|(
name|serviceName
argument_list|)
operator|.
name|withId
argument_list|(
name|serviceId
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|MyServiceRegistry
name|cloudRegistry
init|=
name|camelRgistry
operator|.
name|getNativeServiceRegistry
argument_list|(
name|MyServiceRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|cloudRegistry
operator|.
name|registrations
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|cloudRegistry
operator|.
name|registrations
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|hasFieldOrPropertyWithValue
argument_list|(
literal|"serviceId"
argument_list|,
name|serviceName
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|cloudRegistry
operator|.
name|registrations
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|hasFieldOrPropertyWithValue
argument_list|(
literal|"host"
argument_list|,
literal|"localhost"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|cloudRegistry
operator|.
name|registrations
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|hasFieldOrPropertyWithValue
argument_list|(
literal|"port"
argument_list|,
name|port
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
comment|// *************************************
comment|// Config
comment|// *************************************
annotation|@
name|Configuration
DECL|class|TestConfiguration
specifier|public
specifier|static
class|class
name|TestConfiguration
block|{
annotation|@
name|Bean
DECL|method|myServiceRegistry ()
specifier|public
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|serviceregistry
operator|.
name|ServiceRegistry
argument_list|<
name|MyServiceRegistration
argument_list|>
name|myServiceRegistry
parameter_list|()
block|{
return|return
operator|new
name|MyServiceRegistry
argument_list|()
return|;
block|}
annotation|@
name|Bean
DECL|method|definitionToRegistration ()
specifier|public
name|Converter
argument_list|<
name|ServiceDefinition
argument_list|,
name|MyServiceRegistration
argument_list|>
name|definitionToRegistration
parameter_list|()
block|{
return|return
operator|new
name|Converter
argument_list|<
name|ServiceDefinition
argument_list|,
name|MyServiceRegistration
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|MyServiceRegistration
name|convert
parameter_list|(
name|ServiceDefinition
name|source
parameter_list|)
block|{
return|return
operator|new
name|MyServiceRegistration
argument_list|(
name|source
operator|.
name|getName
argument_list|()
argument_list|,
name|source
operator|.
name|getHost
argument_list|()
argument_list|,
name|source
operator|.
name|getPort
argument_list|()
argument_list|)
return|;
block|}
block|}
return|;
block|}
block|}
comment|// *************************************
comment|// Service Registry Impl
comment|// *************************************
DECL|class|MyServiceRegistry
specifier|public
specifier|static
class|class
name|MyServiceRegistry
implements|implements
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|serviceregistry
operator|.
name|ServiceRegistry
argument_list|<
name|MyServiceRegistration
argument_list|>
block|{
DECL|field|registrations
name|List
argument_list|<
name|MyServiceRegistration
argument_list|>
name|registrations
decl_stmt|;
DECL|method|MyServiceRegistry ()
specifier|public
name|MyServiceRegistry
parameter_list|()
block|{
name|this
operator|.
name|registrations
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|register (MyServiceRegistration registration)
specifier|public
name|void
name|register
parameter_list|(
name|MyServiceRegistration
name|registration
parameter_list|)
block|{
name|this
operator|.
name|registrations
operator|.
name|add
argument_list|(
name|registration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|deregister (MyServiceRegistration registration)
specifier|public
name|void
name|deregister
parameter_list|(
name|MyServiceRegistration
name|registration
parameter_list|)
block|{
name|this
operator|.
name|registrations
operator|.
name|remove
argument_list|(
name|registration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
name|this
operator|.
name|registrations
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setStatus (MyServiceRegistration registration, String status)
specifier|public
name|void
name|setStatus
parameter_list|(
name|MyServiceRegistration
name|registration
parameter_list|,
name|String
name|status
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getStatus (MyServiceRegistration registration)
specifier|public
name|Object
name|getStatus
parameter_list|(
name|MyServiceRegistration
name|registration
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
DECL|class|MyServiceRegistration
specifier|public
specifier|static
class|class
name|MyServiceRegistration
extends|extends
name|DefaultServiceInstance
implements|implements
name|Registration
block|{
DECL|method|MyServiceRegistration (String serviceId, String host, int port)
specifier|public
name|MyServiceRegistration
parameter_list|(
name|String
name|serviceId
parameter_list|,
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|)
block|{
name|super
argument_list|(
name|serviceId
argument_list|,
name|host
argument_list|,
name|port
argument_list|,
literal|false
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

