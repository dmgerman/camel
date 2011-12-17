begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs
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
name|jaxrs
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
name|Component
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
name|AbstractJAXRSFactoryBean
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
name|JAXRSServerFactoryBean
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

begin_class
DECL|class|CxfRsSpringEndpoint
specifier|public
class|class
name|CxfRsSpringEndpoint
extends|extends
name|CxfRsEndpoint
implements|implements
name|BeanIdAware
block|{
DECL|field|bean
specifier|private
name|AbstractJAXRSFactoryBean
name|bean
decl_stmt|;
DECL|field|beanId
specifier|private
name|String
name|beanId
decl_stmt|;
annotation|@
name|Deprecated
comment|/**      * It will be removed in Camel 3.0      * @param comp      * @param bean      */
DECL|method|CxfRsSpringEndpoint (Component component, AbstractJAXRSFactoryBean bean)
specifier|public
name|CxfRsSpringEndpoint
parameter_list|(
name|Component
name|component
parameter_list|,
name|AbstractJAXRSFactoryBean
name|bean
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|bean
operator|.
name|getAddress
argument_list|()
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|init
argument_list|(
name|bean
argument_list|)
expr_stmt|;
block|}
DECL|method|CxfRsSpringEndpoint (Component component, String uri, AbstractJAXRSFactoryBean bean)
specifier|public
name|CxfRsSpringEndpoint
parameter_list|(
name|Component
name|component
parameter_list|,
name|String
name|uri
parameter_list|,
name|AbstractJAXRSFactoryBean
name|bean
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|setAddress
argument_list|(
name|bean
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|init
argument_list|(
name|bean
argument_list|)
expr_stmt|;
block|}
DECL|method|init (AbstractJAXRSFactoryBean bean)
specifier|private
name|void
name|init
parameter_list|(
name|AbstractJAXRSFactoryBean
name|bean
parameter_list|)
block|{
name|this
operator|.
name|bean
operator|=
name|bean
expr_stmt|;
if|if
condition|(
name|bean
operator|instanceof
name|BeanIdAware
condition|)
block|{
name|setBeanId
argument_list|(
operator|(
operator|(
name|BeanIdAware
operator|)
name|bean
operator|)
operator|.
name|getBeanId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|setupJAXRSServerFactoryBean (JAXRSServerFactoryBean sfb)
specifier|protected
name|void
name|setupJAXRSServerFactoryBean
parameter_list|(
name|JAXRSServerFactoryBean
name|sfb
parameter_list|)
block|{
comment|// Do nothing here
block|}
annotation|@
name|Override
DECL|method|setupJAXRSClientFactoryBean (JAXRSClientFactoryBean cfb, String address)
specifier|protected
name|void
name|setupJAXRSClientFactoryBean
parameter_list|(
name|JAXRSClientFactoryBean
name|cfb
parameter_list|,
name|String
name|address
parameter_list|)
block|{
name|cfb
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
comment|// Need to enable the option of ThreadSafe
name|cfb
operator|.
name|setThreadSafe
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newJAXRSServerFactoryBean ()
specifier|protected
name|JAXRSServerFactoryBean
name|newJAXRSServerFactoryBean
parameter_list|()
block|{
name|checkBeanType
argument_list|(
name|bean
argument_list|,
name|JAXRSServerFactoryBean
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
operator|(
name|JAXRSServerFactoryBean
operator|)
name|bean
return|;
block|}
annotation|@
name|Override
DECL|method|newJAXRSClientFactoryBean ()
specifier|protected
name|JAXRSClientFactoryBean
name|newJAXRSClientFactoryBean
parameter_list|()
block|{
name|checkBeanType
argument_list|(
name|bean
argument_list|,
name|JAXRSClientFactoryBean
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
operator|(
name|JAXRSClientFactoryBean
operator|)
name|bean
return|;
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
name|this
operator|.
name|beanId
operator|=
name|id
expr_stmt|;
block|}
block|}
end_class

end_unit

