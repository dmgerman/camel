begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jbpm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jbpm
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Consumer
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
name|Processor
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
name|Producer
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
name|UriEndpoint
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
name|UriParam
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
name|DefaultEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kie
operator|.
name|server
operator|.
name|client
operator|.
name|KieServicesClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kie
operator|.
name|server
operator|.
name|client
operator|.
name|KieServicesConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kie
operator|.
name|server
operator|.
name|client
operator|.
name|KieServicesFactory
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

begin_comment
comment|/**  * The jbpm component provides integration with jBPM (Business Process  * Management).  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.6.0"
argument_list|,
name|scheme
operator|=
literal|"jbpm"
argument_list|,
name|title
operator|=
literal|"JBPM"
argument_list|,
name|syntax
operator|=
literal|"jbpm:connectionURL"
argument_list|,
name|label
operator|=
literal|"process"
argument_list|)
DECL|class|JBPMEndpoint
specifier|public
class|class
name|JBPMEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JBPMEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|JBPMConfiguration
name|configuration
decl_stmt|;
DECL|method|JBPMEndpoint (String uri, JBPMComponent component, JBPMConfiguration configuration)
specifier|public
name|JBPMEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|JBPMComponent
name|component
parameter_list|,
name|JBPMConfiguration
name|configuration
parameter_list|)
throws|throws
name|URISyntaxException
throws|,
name|MalformedURLException
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|KieServicesConfiguration
name|kieConfiguration
init|=
name|KieServicesFactory
operator|.
name|newRestConfiguration
argument_list|(
name|configuration
operator|.
name|getConnectionURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
argument_list|,
name|configuration
operator|.
name|getUserName
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPassword
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getTimeout
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|kieConfiguration
operator|.
name|setTimeout
argument_list|(
name|configuration
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getExtraJaxbClasses
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|configuration
operator|.
name|getExtraJaxbClasses
argument_list|()
argument_list|)
decl_stmt|;
name|kieConfiguration
operator|.
name|addExtraClasses
argument_list|(
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|(
name|classes
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|KieServicesClient
name|kieServerClient
init|=
name|KieServicesFactory
operator|.
name|newKieServicesClient
argument_list|(
name|kieConfiguration
argument_list|)
decl_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"JBPM Producer created with KieServerClient configured for {}"
argument_list|,
name|configuration
operator|.
name|getConnectionURL
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|JBPMProducer
argument_list|(
name|this
argument_list|,
name|kieServerClient
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"JBPM Consumer created and configured for deployment {}"
argument_list|,
name|configuration
operator|.
name|getDeploymentId
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|JBPMConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|setConfiguration (JBPMConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|JBPMConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|JBPMConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
block|}
end_class

end_unit

