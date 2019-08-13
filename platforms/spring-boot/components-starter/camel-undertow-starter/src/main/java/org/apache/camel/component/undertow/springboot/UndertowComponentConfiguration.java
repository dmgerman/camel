begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.undertow.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
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
comment|/**  * The undertow component provides HTTP and WebSocket based endpoints for  * consuming and producing HTTP/WebSocket requests.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.undertow"
argument_list|)
DECL|class|UndertowComponentConfiguration
specifier|public
class|class
name|UndertowComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the undertow component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * To use a custom HttpBinding to control the mapping between Camel message      * and HttpClient. The option is a      * org.apache.camel.component.undertow.UndertowHttpBinding type.      */
DECL|field|undertowHttpBinding
specifier|private
name|String
name|undertowHttpBinding
decl_stmt|;
comment|/**      * To configure security using SSLContextParameters. The option is a      * org.apache.camel.support.jsse.SSLContextParameters type.      */
DECL|field|sslContextParameters
specifier|private
name|String
name|sslContextParameters
decl_stmt|;
comment|/**      * Enable usage of global SSL context parameters.      */
DECL|field|useGlobalSslContextParameters
specifier|private
name|Boolean
name|useGlobalSslContextParameters
init|=
literal|false
decl_stmt|;
comment|/**      * To configure common options, such as thread pools      */
DECL|field|hostOptions
specifier|private
name|UndertowHostOptionsNestedConfiguration
name|hostOptions
decl_stmt|;
comment|/**      * Whether the component should use basic property binding (Camel 2.x) or      * the newer property binding with additional capabilities      */
DECL|field|basicPropertyBinding
specifier|private
name|Boolean
name|basicPropertyBinding
init|=
literal|false
decl_stmt|;
DECL|method|getUndertowHttpBinding ()
specifier|public
name|String
name|getUndertowHttpBinding
parameter_list|()
block|{
return|return
name|undertowHttpBinding
return|;
block|}
DECL|method|setUndertowHttpBinding (String undertowHttpBinding)
specifier|public
name|void
name|setUndertowHttpBinding
parameter_list|(
name|String
name|undertowHttpBinding
parameter_list|)
block|{
name|this
operator|.
name|undertowHttpBinding
operator|=
name|undertowHttpBinding
expr_stmt|;
block|}
DECL|method|getSslContextParameters ()
specifier|public
name|String
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
DECL|method|setSslContextParameters (String sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|String
name|sslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|sslContextParameters
operator|=
name|sslContextParameters
expr_stmt|;
block|}
DECL|method|getUseGlobalSslContextParameters ()
specifier|public
name|Boolean
name|getUseGlobalSslContextParameters
parameter_list|()
block|{
return|return
name|useGlobalSslContextParameters
return|;
block|}
DECL|method|setUseGlobalSslContextParameters ( Boolean useGlobalSslContextParameters)
specifier|public
name|void
name|setUseGlobalSslContextParameters
parameter_list|(
name|Boolean
name|useGlobalSslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|useGlobalSslContextParameters
operator|=
name|useGlobalSslContextParameters
expr_stmt|;
block|}
DECL|method|getHostOptions ()
specifier|public
name|UndertowHostOptionsNestedConfiguration
name|getHostOptions
parameter_list|()
block|{
return|return
name|hostOptions
return|;
block|}
DECL|method|setHostOptions ( UndertowHostOptionsNestedConfiguration hostOptions)
specifier|public
name|void
name|setHostOptions
parameter_list|(
name|UndertowHostOptionsNestedConfiguration
name|hostOptions
parameter_list|)
block|{
name|this
operator|.
name|hostOptions
operator|=
name|hostOptions
expr_stmt|;
block|}
DECL|method|getBasicPropertyBinding ()
specifier|public
name|Boolean
name|getBasicPropertyBinding
parameter_list|()
block|{
return|return
name|basicPropertyBinding
return|;
block|}
DECL|method|setBasicPropertyBinding (Boolean basicPropertyBinding)
specifier|public
name|void
name|setBasicPropertyBinding
parameter_list|(
name|Boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|this
operator|.
name|basicPropertyBinding
operator|=
name|basicPropertyBinding
expr_stmt|;
block|}
DECL|class|UndertowHostOptionsNestedConfiguration
specifier|public
specifier|static
class|class
name|UndertowHostOptionsNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
operator|.
name|UndertowHostOptions
operator|.
name|class
decl_stmt|;
comment|/**          * Set if the Undertow host should use http2 protocol.          */
DECL|field|workerThreads
specifier|private
name|Integer
name|workerThreads
decl_stmt|;
comment|/**          * Set if the Undertow host should use http2 protocol.          */
DECL|field|ioThreads
specifier|private
name|Integer
name|ioThreads
decl_stmt|;
comment|/**          * Set if the Undertow host should use http2 protocol.          */
DECL|field|bufferSize
specifier|private
name|Integer
name|bufferSize
decl_stmt|;
comment|/**          * Set if the Undertow host should use http2 protocol.          */
DECL|field|directBuffers
specifier|private
name|Boolean
name|directBuffers
decl_stmt|;
comment|/**          * Set if the Undertow host should use http2 protocol.          */
DECL|field|http2Enabled
specifier|private
name|Boolean
name|http2Enabled
decl_stmt|;
DECL|method|getWorkerThreads ()
specifier|public
name|Integer
name|getWorkerThreads
parameter_list|()
block|{
return|return
name|workerThreads
return|;
block|}
DECL|method|setWorkerThreads (Integer workerThreads)
specifier|public
name|void
name|setWorkerThreads
parameter_list|(
name|Integer
name|workerThreads
parameter_list|)
block|{
name|this
operator|.
name|workerThreads
operator|=
name|workerThreads
expr_stmt|;
block|}
DECL|method|getIoThreads ()
specifier|public
name|Integer
name|getIoThreads
parameter_list|()
block|{
return|return
name|ioThreads
return|;
block|}
DECL|method|setIoThreads (Integer ioThreads)
specifier|public
name|void
name|setIoThreads
parameter_list|(
name|Integer
name|ioThreads
parameter_list|)
block|{
name|this
operator|.
name|ioThreads
operator|=
name|ioThreads
expr_stmt|;
block|}
DECL|method|getBufferSize ()
specifier|public
name|Integer
name|getBufferSize
parameter_list|()
block|{
return|return
name|bufferSize
return|;
block|}
DECL|method|setBufferSize (Integer bufferSize)
specifier|public
name|void
name|setBufferSize
parameter_list|(
name|Integer
name|bufferSize
parameter_list|)
block|{
name|this
operator|.
name|bufferSize
operator|=
name|bufferSize
expr_stmt|;
block|}
DECL|method|getDirectBuffers ()
specifier|public
name|Boolean
name|getDirectBuffers
parameter_list|()
block|{
return|return
name|directBuffers
return|;
block|}
DECL|method|setDirectBuffers (Boolean directBuffers)
specifier|public
name|void
name|setDirectBuffers
parameter_list|(
name|Boolean
name|directBuffers
parameter_list|)
block|{
name|this
operator|.
name|directBuffers
operator|=
name|directBuffers
expr_stmt|;
block|}
DECL|method|getHttp2Enabled ()
specifier|public
name|Boolean
name|getHttp2Enabled
parameter_list|()
block|{
return|return
name|http2Enabled
return|;
block|}
DECL|method|setHttp2Enabled (Boolean http2Enabled)
specifier|public
name|void
name|setHttp2Enabled
parameter_list|(
name|Boolean
name|http2Enabled
parameter_list|)
block|{
name|this
operator|.
name|http2Enabled
operator|=
name|http2Enabled
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

