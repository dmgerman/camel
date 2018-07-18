begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atmosphere.websocket.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atmosphere
operator|.
name|websocket
operator|.
name|springboot
package|;
end_package

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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
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
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * To exchange data with external Websocket clients using Atmosphere.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.atmosphere-websocket"
argument_list|)
DECL|class|WebsocketComponentConfiguration
specifier|public
class|class
name|WebsocketComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the atmosphere-websocket      * component. This is enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * Default name of servlet to use. The default name is CamelServlet.      */
DECL|field|servletName
specifier|private
name|String
name|servletName
decl_stmt|;
comment|/**      * To use a custom org.apache.camel.component.servlet.HttpRegistry. The      * option is a org.apache.camel.component.servlet.HttpRegistry type.      */
DECL|field|httpRegistry
specifier|private
name|String
name|httpRegistry
decl_stmt|;
comment|/**      * Whether to automatic bind multipart/form-data as attachments on the Camel      * Exchange. The options attachmentMultipartBinding=true and      * disableStreamCache=false cannot work together. Remove disableStreamCache      * to use AttachmentMultipartBinding. This is turn off by default as this      * may require servlet specific configuration to enable this when using      * Servlet's.      */
DECL|field|attachmentMultipartBinding
specifier|private
name|Boolean
name|attachmentMultipartBinding
init|=
literal|false
decl_stmt|;
comment|/**      * To use a custom HttpBinding to control the mapping between Camel message      * and HttpClient. The option is a org.apache.camel.http.common.HttpBinding      * type.      */
DECL|field|httpBinding
specifier|private
name|String
name|httpBinding
decl_stmt|;
comment|/**      * To use the shared HttpConfiguration as base configuration. The option is      * a org.apache.camel.http.common.HttpConfiguration type.      */
DECL|field|httpConfiguration
specifier|private
name|String
name|httpConfiguration
decl_stmt|;
comment|/**      * Whether to allow java serialization when a request uses      * context-type=application/x-java-serialized-object. This is by default      * turned off. If you enable this then be aware that Java will deserialize      * the incoming data from the request to Java and that can be a potential      * security risk.      */
DECL|field|allowJavaSerializedObject
specifier|private
name|Boolean
name|allowJavaSerializedObject
init|=
literal|false
decl_stmt|;
comment|/**      * To use a custom org.apache.camel.spi.HeaderFilterStrategy to filter      * header to and from Camel message. The option is a      * org.apache.camel.spi.HeaderFilterStrategy type.      */
DECL|field|headerFilterStrategy
specifier|private
name|String
name|headerFilterStrategy
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getServletName ()
specifier|public
name|String
name|getServletName
parameter_list|()
block|{
return|return
name|servletName
return|;
block|}
DECL|method|setServletName (String servletName)
specifier|public
name|void
name|setServletName
parameter_list|(
name|String
name|servletName
parameter_list|)
block|{
name|this
operator|.
name|servletName
operator|=
name|servletName
expr_stmt|;
block|}
DECL|method|getHttpRegistry ()
specifier|public
name|String
name|getHttpRegistry
parameter_list|()
block|{
return|return
name|httpRegistry
return|;
block|}
DECL|method|setHttpRegistry (String httpRegistry)
specifier|public
name|void
name|setHttpRegistry
parameter_list|(
name|String
name|httpRegistry
parameter_list|)
block|{
name|this
operator|.
name|httpRegistry
operator|=
name|httpRegistry
expr_stmt|;
block|}
DECL|method|getAttachmentMultipartBinding ()
specifier|public
name|Boolean
name|getAttachmentMultipartBinding
parameter_list|()
block|{
return|return
name|attachmentMultipartBinding
return|;
block|}
DECL|method|setAttachmentMultipartBinding (Boolean attachmentMultipartBinding)
specifier|public
name|void
name|setAttachmentMultipartBinding
parameter_list|(
name|Boolean
name|attachmentMultipartBinding
parameter_list|)
block|{
name|this
operator|.
name|attachmentMultipartBinding
operator|=
name|attachmentMultipartBinding
expr_stmt|;
block|}
DECL|method|getHttpBinding ()
specifier|public
name|String
name|getHttpBinding
parameter_list|()
block|{
return|return
name|httpBinding
return|;
block|}
DECL|method|setHttpBinding (String httpBinding)
specifier|public
name|void
name|setHttpBinding
parameter_list|(
name|String
name|httpBinding
parameter_list|)
block|{
name|this
operator|.
name|httpBinding
operator|=
name|httpBinding
expr_stmt|;
block|}
DECL|method|getHttpConfiguration ()
specifier|public
name|String
name|getHttpConfiguration
parameter_list|()
block|{
return|return
name|httpConfiguration
return|;
block|}
DECL|method|setHttpConfiguration (String httpConfiguration)
specifier|public
name|void
name|setHttpConfiguration
parameter_list|(
name|String
name|httpConfiguration
parameter_list|)
block|{
name|this
operator|.
name|httpConfiguration
operator|=
name|httpConfiguration
expr_stmt|;
block|}
DECL|method|getAllowJavaSerializedObject ()
specifier|public
name|Boolean
name|getAllowJavaSerializedObject
parameter_list|()
block|{
return|return
name|allowJavaSerializedObject
return|;
block|}
DECL|method|setAllowJavaSerializedObject (Boolean allowJavaSerializedObject)
specifier|public
name|void
name|setAllowJavaSerializedObject
parameter_list|(
name|Boolean
name|allowJavaSerializedObject
parameter_list|)
block|{
name|this
operator|.
name|allowJavaSerializedObject
operator|=
name|allowJavaSerializedObject
expr_stmt|;
block|}
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|String
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
DECL|method|setHeaderFilterStrategy (String headerFilterStrategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|String
name|headerFilterStrategy
parameter_list|)
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
name|headerFilterStrategy
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
block|}
end_class

end_unit

