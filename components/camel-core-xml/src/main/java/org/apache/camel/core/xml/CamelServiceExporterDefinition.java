begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|model
operator|.
name|IdentifiedType
import|;
end_import

begin_comment
comment|/**  * Used for export a service using Spring Remoting to hide the network call using an interface.  *  * @version  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"export"
argument_list|)
DECL|class|CamelServiceExporterDefinition
specifier|public
class|class
name|CamelServiceExporterDefinition
extends|extends
name|IdentifiedType
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|uri
specifier|private
name|String
name|uri
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|serviceRef
specifier|private
name|String
name|serviceRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|serviceInterface
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|serviceInterface
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|camelContextId
specifier|private
name|String
name|camelContextId
decl_stmt|;
DECL|method|getUri ()
specifier|public
name|String
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
comment|/**      * Camel endpoint uri to use a remote transport when calling the service      */
DECL|method|setUri (String uri)
specifier|public
name|void
name|setUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
DECL|method|getServiceRef ()
specifier|public
name|String
name|getServiceRef
parameter_list|()
block|{
return|return
name|serviceRef
return|;
block|}
comment|/**      * Reference to the service name to lookup in the registry.      */
DECL|method|setServiceRef (String serviceRef)
specifier|public
name|void
name|setServiceRef
parameter_list|(
name|String
name|serviceRef
parameter_list|)
block|{
name|this
operator|.
name|serviceRef
operator|=
name|serviceRef
expr_stmt|;
block|}
DECL|method|getServiceInterface ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getServiceInterface
parameter_list|()
block|{
return|return
name|serviceInterface
return|;
block|}
comment|/**      * Java interfaces to use as facade for the service to be exported      */
DECL|method|setServiceInterface (Class<?> serviceInterface)
specifier|public
name|void
name|setServiceInterface
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|serviceInterface
parameter_list|)
block|{
name|this
operator|.
name|serviceInterface
operator|=
name|serviceInterface
expr_stmt|;
block|}
DECL|method|getCamelContextId ()
specifier|public
name|String
name|getCamelContextId
parameter_list|()
block|{
return|return
name|camelContextId
return|;
block|}
comment|/**      * The id of the CamelContext to use, if there is multiple CamelContext in the same JVM.      */
DECL|method|setCamelContextId (String camelContextId)
specifier|public
name|void
name|setCamelContextId
parameter_list|(
name|String
name|camelContextId
parameter_list|)
block|{
name|this
operator|.
name|camelContextId
operator|=
name|camelContextId
expr_stmt|;
block|}
block|}
end_class

end_unit

