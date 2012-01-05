begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|spring
package|;
end_package

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
name|component
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|BeanIdAware
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|BusFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|bus
operator|.
name|spring
operator|.
name|BusWiringBeanFactoryPostProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|feature
operator|.
name|AbstractFeature
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|feature
operator|.
name|LoggingFeature
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|client
operator|.
name|JAXRSClientFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|version
operator|.
name|Version
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
name|ApplicationContextAware
import|;
end_import

begin_class
DECL|class|SpringJAXRSClientFactoryBean
specifier|public
class|class
name|SpringJAXRSClientFactoryBean
extends|extends
name|JAXRSClientFactoryBean
implements|implements
name|ApplicationContextAware
implements|,
name|BeanIdAware
block|{
DECL|field|beanId
specifier|private
name|String
name|beanId
decl_stmt|;
DECL|field|loggingFeatureEnabled
specifier|private
name|boolean
name|loggingFeatureEnabled
decl_stmt|;
DECL|field|loggingSizeLimit
specifier|private
name|int
name|loggingSizeLimit
decl_stmt|;
DECL|method|SpringJAXRSClientFactoryBean ()
specifier|public
name|SpringJAXRSClientFactoryBean
parameter_list|()
block|{     }
DECL|method|isLoggingFeatureEnabled ()
specifier|public
name|boolean
name|isLoggingFeatureEnabled
parameter_list|()
block|{
return|return
name|loggingFeatureEnabled
return|;
block|}
DECL|method|setLoggingFeatureEnabled (boolean loggingFeatureEnabled)
specifier|public
name|void
name|setLoggingFeatureEnabled
parameter_list|(
name|boolean
name|loggingFeatureEnabled
parameter_list|)
block|{
name|this
operator|.
name|loggingFeatureEnabled
operator|=
name|loggingFeatureEnabled
expr_stmt|;
block|}
DECL|method|getLoggingSizeLimit ()
specifier|public
name|int
name|getLoggingSizeLimit
parameter_list|()
block|{
return|return
name|loggingSizeLimit
return|;
block|}
DECL|method|setLoggingSizeLimit (int loggingSizeLimit)
specifier|public
name|void
name|setLoggingSizeLimit
parameter_list|(
name|int
name|loggingSizeLimit
parameter_list|)
block|{
name|this
operator|.
name|loggingSizeLimit
operator|=
name|loggingSizeLimit
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
annotation|@
name|Override
DECL|method|setApplicationContext (ApplicationContext ctx)
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|ctx
parameter_list|)
throws|throws
name|BeansException
block|{
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|Version
operator|.
name|getCurrentVersion
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"2.3"
argument_list|)
condition|)
block|{
comment|// Don't relate on the DefaultBus
name|BusFactory
name|factory
init|=
operator|new
name|SpringBusFactory
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
name|bus
operator|=
name|factory
operator|.
name|createBus
argument_list|()
expr_stmt|;
name|BusWiringBeanFactoryPostProcessor
operator|.
name|updateBusReferencesInContext
argument_list|(
name|bus
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setBus
argument_list|(
name|BusWiringBeanFactoryPostProcessor
operator|.
name|addDefaultBus
argument_list|(
name|ctx
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getBeanId ()
specifier|public
name|String
name|getBeanId
parameter_list|()
block|{
return|return
name|beanId
return|;
block|}
DECL|method|setBeanId (String id)
specifier|public
name|void
name|setBeanId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|beanId
operator|=
name|id
expr_stmt|;
block|}
comment|// add this mothod for testing
DECL|method|getSchemaLocations ()
name|List
argument_list|<
name|String
argument_list|>
name|getSchemaLocations
parameter_list|()
block|{
return|return
name|schemaLocations
return|;
block|}
DECL|method|getFeatures ()
specifier|public
name|List
argument_list|<
name|AbstractFeature
argument_list|>
name|getFeatures
parameter_list|()
block|{
name|List
argument_list|<
name|AbstractFeature
argument_list|>
name|answer
init|=
name|super
operator|.
name|getFeatures
argument_list|()
decl_stmt|;
if|if
condition|(
name|isLoggingFeatureEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|getLoggingSizeLimit
argument_list|()
operator|>
literal|0
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
operator|new
name|LoggingFeature
argument_list|(
name|getLoggingSizeLimit
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|.
name|add
argument_list|(
operator|new
name|LoggingFeature
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

