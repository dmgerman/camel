begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mybatis.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mybatis
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|mybatis
operator|.
name|MyBatisComponent
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
name|util
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
name|boot
operator|.
name|autoconfigure
operator|.
name|condition
operator|.
name|ConditionalOnClass
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
name|ConditionalOnMissingBean
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

begin_comment
comment|/**  * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Configuration
annotation|@
name|EnableConfigurationProperties
argument_list|(
name|MyBatisComponentConfiguration
operator|.
name|class
argument_list|)
DECL|class|MyBatisComponentAutoConfiguration
specifier|public
class|class
name|MyBatisComponentAutoConfiguration
block|{
annotation|@
name|Bean
annotation|@
name|ConditionalOnClass
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
annotation|@
name|ConditionalOnMissingBean
argument_list|(
name|MyBatisComponent
operator|.
name|class
argument_list|)
DECL|method|configureMyBatisComponent ( CamelContext camelContext, MyBatisComponentConfiguration configuration)
specifier|public
name|MyBatisComponent
name|configureMyBatisComponent
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|MyBatisComponentConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
block|{
name|MyBatisComponent
name|component
init|=
operator|new
name|MyBatisComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|IntrospectionSupport
operator|.
name|getProperties
argument_list|(
name|configuration
argument_list|,
name|parameters
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|camelContext
argument_list|,
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|component
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|component
return|;
block|}
block|}
end_class

end_unit

