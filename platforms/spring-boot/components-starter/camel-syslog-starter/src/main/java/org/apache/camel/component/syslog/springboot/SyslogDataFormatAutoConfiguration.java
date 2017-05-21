begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.syslog.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|syslog
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
name|List
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
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|RuntimeCamelException
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
name|syslog
operator|.
name|SyslogDataFormat
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
name|DataFormat
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
name|DataFormatCustomizer
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
name|DataFormatFactory
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
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|DataFormatConfigurationProperties
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
name|util
operator|.
name|ConditionalOnCamelContextAndAutoConfigurationBeans
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
name|util
operator|.
name|GroupCondition
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
name|util
operator|.
name|HierarchicalPropertiesEvaluator
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|ApplicationContext
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

begin_comment
comment|/**  * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|Configuration
annotation|@
name|Conditional
argument_list|(
block|{
name|ConditionalOnCamelContextAndAutoConfigurationBeans
operator|.
name|class
block|,
name|SyslogDataFormatAutoConfiguration
operator|.
name|GroupConditions
operator|.
name|class
block|}
argument_list|)
annotation|@
name|AutoConfigureAfter
argument_list|(
name|name
operator|=
literal|"org.apache.camel.spring.boot.CamelAutoConfiguration"
argument_list|)
annotation|@
name|EnableConfigurationProperties
argument_list|(
block|{
name|DataFormatConfigurationProperties
operator|.
name|class
block|,
name|SyslogDataFormatConfiguration
operator|.
name|class
block|}
argument_list|)
DECL|class|SyslogDataFormatAutoConfiguration
specifier|public
class|class
name|SyslogDataFormatAutoConfiguration
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SyslogDataFormatAutoConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Autowired
DECL|field|applicationContext
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
annotation|@
name|Autowired
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|Autowired
DECL|field|configuration
specifier|private
name|SyslogDataFormatConfiguration
name|configuration
decl_stmt|;
annotation|@
name|Autowired
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|customizers
specifier|private
name|List
argument_list|<
name|DataFormatCustomizer
argument_list|<
name|SyslogDataFormat
argument_list|>
argument_list|>
name|customizers
decl_stmt|;
DECL|class|GroupConditions
specifier|static
class|class
name|GroupConditions
extends|extends
name|GroupCondition
block|{
DECL|method|GroupConditions ()
specifier|public
name|GroupConditions
parameter_list|()
block|{
name|super
argument_list|(
literal|"camel.dataformat"
argument_list|,
literal|"camel.dataformat.syslog"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Bean
argument_list|(
name|name
operator|=
literal|"syslog-dataformat-factory"
argument_list|)
annotation|@
name|ConditionalOnMissingBean
argument_list|(
name|SyslogDataFormat
operator|.
name|class
argument_list|)
DECL|method|configureSyslogDataFormatFactory ()
specifier|public
name|DataFormatFactory
name|configureSyslogDataFormatFactory
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|DataFormatFactory
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|DataFormat
name|newInstance
parameter_list|()
block|{
name|SyslogDataFormat
name|dataformat
init|=
operator|new
name|SyslogDataFormat
argument_list|()
decl_stmt|;
if|if
condition|(
name|CamelContextAware
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|SyslogDataFormat
operator|.
name|class
argument_list|)
condition|)
block|{
name|CamelContextAware
name|contextAware
init|=
name|CamelContextAware
operator|.
name|class
operator|.
name|cast
argument_list|(
name|dataformat
argument_list|)
decl_stmt|;
if|if
condition|(
name|contextAware
operator|!=
literal|null
condition|)
block|{
name|contextAware
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
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
name|dataformat
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|customizers
argument_list|)
condition|)
block|{
for|for
control|(
name|DataFormatCustomizer
argument_list|<
name|SyslogDataFormat
argument_list|>
name|customizer
range|:
name|customizers
control|)
block|{
name|boolean
name|useCustomizer
init|=
operator|(
name|customizer
operator|instanceof
name|HasId
operator|)
condition|?
name|HierarchicalPropertiesEvaluator
operator|.
name|evaluate
argument_list|(
name|applicationContext
operator|.
name|getEnvironment
argument_list|()
argument_list|,
literal|"camel.dataformat.customizer"
argument_list|,
literal|"camel.dataformat.syslog.customizer"
argument_list|,
operator|(
operator|(
name|HasId
operator|)
name|customizer
operator|)
operator|.
name|getId
argument_list|()
argument_list|)
else|:
name|HierarchicalPropertiesEvaluator
operator|.
name|evaluate
argument_list|(
name|applicationContext
operator|.
name|getEnvironment
argument_list|()
argument_list|,
literal|"camel.dataformat.customizer"
argument_list|,
literal|"camel.dataformat.syslog.customizer"
argument_list|)
decl_stmt|;
if|if
condition|(
name|useCustomizer
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Configure dataformat {}, with customizer {}"
argument_list|,
name|dataformat
argument_list|,
name|customizer
argument_list|)
expr_stmt|;
name|customizer
operator|.
name|customize
argument_list|(
name|dataformat
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|dataformat
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

