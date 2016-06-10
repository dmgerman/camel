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
name|component
operator|.
name|http
operator|.
name|HttpClientConfigurer
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
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|HttpConnectionManager
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
comment|/**  * For calling out to external HTTP servers using Apache HTTP Client 3.x.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.https"
argument_list|)
DECL|class|HttpComponentConfiguration
specifier|public
class|class
name|HttpComponentConfiguration
block|{
comment|/**      * To use the custom HttpClientConfigurer to perform configuration of the      * HttpClient that will be used.      */
DECL|field|httpClientConfigurer
specifier|private
name|HttpClientConfigurer
name|httpClientConfigurer
decl_stmt|;
comment|/**      * To use a custom HttpConnectionManager to manage connections      */
DECL|field|httpConnectionManager
specifier|private
name|HttpConnectionManager
name|httpConnectionManager
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
DECL|method|getHttpClientConfigurer ()
specifier|public
name|HttpClientConfigurer
name|getHttpClientConfigurer
parameter_list|()
block|{
return|return
name|httpClientConfigurer
return|;
block|}
DECL|method|setHttpClientConfigurer ( HttpClientConfigurer httpClientConfigurer)
specifier|public
name|void
name|setHttpClientConfigurer
parameter_list|(
name|HttpClientConfigurer
name|httpClientConfigurer
parameter_list|)
block|{
name|this
operator|.
name|httpClientConfigurer
operator|=
name|httpClientConfigurer
expr_stmt|;
block|}
DECL|method|getHttpConnectionManager ()
specifier|public
name|HttpConnectionManager
name|getHttpConnectionManager
parameter_list|()
block|{
return|return
name|httpConnectionManager
return|;
block|}
DECL|method|setHttpConnectionManager ( HttpConnectionManager httpConnectionManager)
specifier|public
name|void
name|setHttpConnectionManager
parameter_list|(
name|HttpConnectionManager
name|httpConnectionManager
parameter_list|)
block|{
name|this
operator|.
name|httpConnectionManager
operator|=
name|httpConnectionManager
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

