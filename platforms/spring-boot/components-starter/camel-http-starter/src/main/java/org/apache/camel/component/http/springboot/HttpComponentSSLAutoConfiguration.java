begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
operator|.
name|springboot
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
name|http
operator|.
name|SSLContextParametersSecureProtocolSocketFactory
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
name|jsse
operator|.
name|GlobalSSLContextParametersSupplier
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|protocol
operator|.
name|Protocol
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|protocol
operator|.
name|ProtocolSocketFactory
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
name|BeansException
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
name|NoSuchBeanDefinitionException
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
name|NoUniqueBeanDefinitionException
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
name|BeanFactoryPostProcessor
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
name|ConfigurableListableBeanFactory
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
name|ConditionalOnBean
argument_list|(
name|type
operator|=
block|{
literal|"org.apache.camel.spring.boot.CamelAutoConfiguration"
block|,
literal|"org.apache.camel.util.jsse.GlobalSSLContextParametersSupplier"
block|}
argument_list|)
annotation|@
name|AutoConfigureAfter
argument_list|(
name|name
operator|=
block|{
literal|"org.apache.camel.spring.boot.CamelAutoConfiguration"
block|,
literal|"org.apache.camel.spring.boot.security.CamelSSLAutoConfiguration"
block|}
argument_list|)
annotation|@
name|ConditionalOnProperty
argument_list|(
name|value
operator|=
literal|"camel.component.http.ssl.auto-configure"
argument_list|,
name|matchIfMissing
operator|=
literal|true
argument_list|)
DECL|class|HttpComponentSSLAutoConfiguration
specifier|public
class|class
name|HttpComponentSSLAutoConfiguration
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|HttpComponentSSLAutoConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Bean
DECL|method|cacheAutoConfigurationValidatorPostProcessor (CamelContext context)
specifier|public
name|HttpSSLPostProcessor
name|cacheAutoConfigurationValidatorPostProcessor
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|HttpSSLPostProcessor
argument_list|(
name|context
argument_list|)
return|;
block|}
DECL|class|HttpSSLPostProcessor
specifier|static
class|class
name|HttpSSLPostProcessor
implements|implements
name|BeanFactoryPostProcessor
block|{
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
DECL|method|HttpSSLPostProcessor (CamelContext context)
name|HttpSSLPostProcessor
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|postProcessBeanFactory (ConfigurableListableBeanFactory beanFactory)
specifier|public
name|void
name|postProcessBeanFactory
parameter_list|(
name|ConfigurableListableBeanFactory
name|beanFactory
parameter_list|)
throws|throws
name|BeansException
block|{
try|try
block|{
name|GlobalSSLContextParametersSupplier
name|sslContextParameters
init|=
name|beanFactory
operator|.
name|getBean
argument_list|(
name|GlobalSSLContextParametersSupplier
operator|.
name|class
argument_list|)
decl_stmt|;
name|ProtocolSocketFactory
name|factory
init|=
operator|new
name|SSLContextParametersSecureProtocolSocketFactory
argument_list|(
name|sslContextParameters
operator|.
name|get
argument_list|()
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|Protocol
operator|.
name|registerProtocol
argument_list|(
literal|"https"
argument_list|,
operator|new
name|Protocol
argument_list|(
literal|"https"
argument_list|,
name|factory
argument_list|,
literal|443
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoUniqueBeanDefinitionException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Multiple instance of SSLContextParameters found, skipping configuration"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchBeanDefinitionException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No instance of SSLContextParameters found"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BeansException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Cannot create SSLContextParameters"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

