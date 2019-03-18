begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul.springboot.cluster
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|consul
operator|.
name|springboot
operator|.
name|cluster
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
name|cluster
operator|.
name|CamelClusterService
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
name|consul
operator|.
name|cluster
operator|.
name|ConsulClusterService
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
name|cluster
operator|.
name|ClusteredRouteControllerAutoConfiguration
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
name|support
operator|.
name|IntrospectionSupport
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
name|beans
operator|.
name|factory
operator|.
name|config
operator|.
name|ConfigurableBeanFactory
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
name|ConditionalOnProperty
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
name|context
operator|.
name|annotation
operator|.
name|Scope
import|;
end_import

begin_class
annotation|@
name|Configuration
annotation|@
name|AutoConfigureBefore
argument_list|(
block|{
name|ClusteredRouteControllerAutoConfiguration
operator|.
name|class
block|,
name|CamelAutoConfiguration
operator|.
name|class
block|}
argument_list|)
annotation|@
name|ConditionalOnProperty
argument_list|(
name|prefix
operator|=
literal|"camel.component.consul.cluster.service"
argument_list|,
name|name
operator|=
literal|"enabled"
argument_list|)
annotation|@
name|EnableConfigurationProperties
argument_list|(
name|ConsulClusterServiceConfiguration
operator|.
name|class
argument_list|)
DECL|class|ConsulClusterServiceAutoConfiguration
specifier|public
class|class
name|ConsulClusterServiceAutoConfiguration
block|{
annotation|@
name|Autowired
DECL|field|configuration
specifier|private
name|ConsulClusterServiceConfiguration
name|configuration
decl_stmt|;
annotation|@
name|Bean
argument_list|(
name|name
operator|=
literal|"consul-cluster-service"
argument_list|)
annotation|@
name|Scope
argument_list|(
name|ConfigurableBeanFactory
operator|.
name|SCOPE_SINGLETON
argument_list|)
DECL|method|consulClusterService ()
specifier|public
name|CamelClusterService
name|consulClusterService
parameter_list|()
throws|throws
name|Exception
block|{
name|ConsulClusterService
name|service
init|=
operator|new
name|ConsulClusterService
argument_list|()
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|service
argument_list|,
name|IntrospectionSupport
operator|.
name|getNonNullProperties
argument_list|(
name|configuration
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|service
return|;
block|}
block|}
end_class

end_unit

