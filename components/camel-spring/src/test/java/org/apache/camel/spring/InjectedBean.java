begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
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
name|CamelTemplate
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
name|Endpoint
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
name|EndpointInject
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
name|ProducerTemplate
import|;
end_import

begin_comment
comment|/**  * @version $Revision: $  */
end_comment

begin_class
DECL|class|InjectedBean
specifier|public
class|class
name|InjectedBean
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"direct:fieldInjectedEndpoint"
argument_list|)
DECL|field|fieldInjectedEndpoint
specifier|private
name|Endpoint
name|fieldInjectedEndpoint
decl_stmt|;
DECL|field|propertyInjectedEndpoint
specifier|private
name|Endpoint
name|propertyInjectedEndpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"direct:fieldInjectedProducer"
argument_list|)
DECL|field|fieldInjectedProducer
specifier|private
name|Producer
name|fieldInjectedProducer
decl_stmt|;
DECL|field|propertyInjectedProducer
specifier|private
name|Producer
name|propertyInjectedProducer
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"direct:fieldInjectedCamelTemplate"
argument_list|)
DECL|field|fieldInjectedCamelTemplate
specifier|private
name|CamelTemplate
name|fieldInjectedCamelTemplate
decl_stmt|;
DECL|field|propertyInjectedCamelTemplate
specifier|private
name|CamelTemplate
name|propertyInjectedCamelTemplate
decl_stmt|;
annotation|@
name|EndpointInject
DECL|field|injectByFieldName
specifier|private
name|ProducerTemplate
name|injectByFieldName
decl_stmt|;
DECL|field|injectByPropertyName
specifier|private
name|ProducerTemplate
name|injectByPropertyName
decl_stmt|;
comment|// Endpoint
comment|//-----------------------------------------------------------------------
DECL|method|getFieldInjectedEndpoint ()
specifier|public
name|Endpoint
name|getFieldInjectedEndpoint
parameter_list|()
block|{
return|return
name|fieldInjectedEndpoint
return|;
block|}
DECL|method|getPropertyInjectedEndpoint ()
specifier|public
name|Endpoint
name|getPropertyInjectedEndpoint
parameter_list|()
block|{
return|return
name|propertyInjectedEndpoint
return|;
block|}
annotation|@
name|EndpointInject
argument_list|(
name|name
operator|=
literal|"namedEndpoint1"
argument_list|)
DECL|method|setPropertyInjectedEndpoint (Endpoint propertyInjectedEndpoint)
specifier|public
name|void
name|setPropertyInjectedEndpoint
parameter_list|(
name|Endpoint
name|propertyInjectedEndpoint
parameter_list|)
block|{
name|this
operator|.
name|propertyInjectedEndpoint
operator|=
name|propertyInjectedEndpoint
expr_stmt|;
block|}
comment|// Producer
comment|//-----------------------------------------------------------------------
DECL|method|getFieldInjectedProducer ()
specifier|public
name|Producer
name|getFieldInjectedProducer
parameter_list|()
block|{
return|return
name|fieldInjectedProducer
return|;
block|}
DECL|method|getPropertyInjectedProducer ()
specifier|public
name|Producer
name|getPropertyInjectedProducer
parameter_list|()
block|{
return|return
name|propertyInjectedProducer
return|;
block|}
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"direct:propertyInjectedProducer"
argument_list|)
DECL|method|setPropertyInjectedProducer (Producer propertyInjectedProducer)
specifier|public
name|void
name|setPropertyInjectedProducer
parameter_list|(
name|Producer
name|propertyInjectedProducer
parameter_list|)
block|{
name|this
operator|.
name|propertyInjectedProducer
operator|=
name|propertyInjectedProducer
expr_stmt|;
block|}
comment|// CamelTemplate
comment|//-----------------------------------------------------------------------
DECL|method|getFieldInjectedCamelTemplate ()
specifier|public
name|CamelTemplate
name|getFieldInjectedCamelTemplate
parameter_list|()
block|{
return|return
name|fieldInjectedCamelTemplate
return|;
block|}
DECL|method|getPropertyInjectedCamelTemplate ()
specifier|public
name|CamelTemplate
name|getPropertyInjectedCamelTemplate
parameter_list|()
block|{
return|return
name|propertyInjectedCamelTemplate
return|;
block|}
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"direct:propertyInjectedCamelTemplate"
argument_list|)
DECL|method|setPropertyInjectedCamelTemplate (CamelTemplate propertyInjectedCamelTemplate)
specifier|public
name|void
name|setPropertyInjectedCamelTemplate
parameter_list|(
name|CamelTemplate
name|propertyInjectedCamelTemplate
parameter_list|)
block|{
name|this
operator|.
name|propertyInjectedCamelTemplate
operator|=
name|propertyInjectedCamelTemplate
expr_stmt|;
block|}
comment|// ProducerTemplate
comment|//-------------------------------------------------------------------------
DECL|method|getInjectByFieldName ()
specifier|public
name|ProducerTemplate
name|getInjectByFieldName
parameter_list|()
block|{
return|return
name|injectByFieldName
return|;
block|}
DECL|method|setInjectByFieldName (ProducerTemplate injectByFieldName)
specifier|public
name|void
name|setInjectByFieldName
parameter_list|(
name|ProducerTemplate
name|injectByFieldName
parameter_list|)
block|{
name|this
operator|.
name|injectByFieldName
operator|=
name|injectByFieldName
expr_stmt|;
block|}
DECL|method|getInjectByPropertyName ()
specifier|public
name|ProducerTemplate
name|getInjectByPropertyName
parameter_list|()
block|{
return|return
name|injectByPropertyName
return|;
block|}
annotation|@
name|EndpointInject
DECL|method|setInjectByPropertyName (ProducerTemplate injectByPropertyName)
specifier|public
name|void
name|setInjectByPropertyName
parameter_list|(
name|ProducerTemplate
name|injectByPropertyName
parameter_list|)
block|{
name|this
operator|.
name|injectByPropertyName
operator|=
name|injectByPropertyName
expr_stmt|;
block|}
block|}
end_class

end_unit

