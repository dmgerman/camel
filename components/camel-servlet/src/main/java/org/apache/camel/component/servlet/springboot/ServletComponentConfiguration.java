begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servlet.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servlet
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
name|component
operator|.
name|servlet
operator|.
name|HttpRegistry
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
name|http
operator|.
name|common
operator|.
name|HttpBinding
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
name|http
operator|.
name|common
operator|.
name|HttpConfiguration
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
name|HeaderFilterStrategy
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
comment|/**  * To use a HTTP Servlet as entry for Camel routes when running in a servlet  * container.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.servlet"
argument_list|)
DECL|class|ServletComponentConfiguration
specifier|public
class|class
name|ServletComponentConfiguration
block|{
comment|/**      * Default name of servlet to use. The default name is CamelServlet.      */
DECL|field|servletName
specifier|private
name|String
name|servletName
decl_stmt|;
comment|/**      * To use a custom org.apache.camel.component.servlet.HttpRegistry.      */
DECL|field|httpRegistry
specifier|private
name|HttpRegistry
name|httpRegistry
decl_stmt|;
comment|/**      * Whether to automatic bind multipart/form-data as attachments on the Camel      * Exchange. This is turn off by default as this may require servet specific      * configuration to enable this when using Servlet's.      */
DECL|field|attachmentMultipartBinding
specifier|private
name|Boolean
name|attachmentMultipartBinding
init|=
literal|false
decl_stmt|;
comment|/**      * To use a custom HttpBinding to control the mapping between Camel message      * and HttpClient.      */
DECL|field|httpBinding
specifier|private
name|HttpBinding
name|httpBinding
decl_stmt|;
comment|/**      * To use the shared HttpConfiguration as base configuration.      */
DECL|field|httpConfiguration
specifier|private
name|HttpConfiguration
name|httpConfiguration
decl_stmt|;
comment|/**      * Whether to allow java serialization when a request uses      * context-type=application/x-java-serialized-object This is by default      * turned off. If you enable this then be aware that Java will deserialize      * the incoming data from the request to Java and that can be a potential      * security risk.      */
DECL|field|allowJavaSerializedObject
specifier|private
name|Boolean
name|allowJavaSerializedObject
init|=
literal|false
decl_stmt|;
comment|/**      * To use a custom HeaderFilterStrategy to filter header to and from Camel      * message.      */
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
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
name|HttpRegistry
name|getHttpRegistry
parameter_list|()
block|{
return|return
name|httpRegistry
return|;
block|}
DECL|method|setHttpRegistry (HttpRegistry httpRegistry)
specifier|public
name|void
name|setHttpRegistry
parameter_list|(
name|HttpRegistry
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
name|HttpBinding
name|getHttpBinding
parameter_list|()
block|{
return|return
name|httpBinding
return|;
block|}
DECL|method|setHttpBinding (HttpBinding httpBinding)
specifier|public
name|void
name|setHttpBinding
parameter_list|(
name|HttpBinding
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
name|HttpConfiguration
name|getHttpConfiguration
parameter_list|()
block|{
return|return
name|httpConfiguration
return|;
block|}
DECL|method|setHttpConfiguration (HttpConfiguration httpConfiguration)
specifier|public
name|void
name|setHttpConfiguration
parameter_list|(
name|HttpConfiguration
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
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
DECL|method|setHeaderFilterStrategy ( HeaderFilterStrategy headerFilterStrategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
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
block|}
end_class

end_unit

