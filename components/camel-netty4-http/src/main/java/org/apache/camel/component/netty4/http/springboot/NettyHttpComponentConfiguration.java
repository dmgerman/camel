begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4.http.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
operator|.
name|http
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|util
operator|.
name|concurrent
operator|.
name|EventExecutorGroup
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
name|netty4
operator|.
name|NettyConfiguration
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
name|netty4
operator|.
name|http
operator|.
name|NettyHttpBinding
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
name|netty4
operator|.
name|http
operator|.
name|NettyHttpSecurityConfiguration
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
comment|/**  * Netty HTTP server and client using the Netty 4.x library.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.netty4-http"
argument_list|)
DECL|class|NettyHttpComponentConfiguration
specifier|public
class|class
name|NettyHttpComponentConfiguration
block|{
comment|/**      * To use a custom org.apache.camel.component.netty4.http.NettyHttpBinding      * for binding to/from Netty and Camel Message API.      */
DECL|field|nettyHttpBinding
specifier|private
name|NettyHttpBinding
name|nettyHttpBinding
decl_stmt|;
comment|/**      * To use a custom org.apache.camel.spi.HeaderFilterStrategy to filter      * headers.      */
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
comment|/**      * Refers to a      * org.apache.camel.component.netty4.http.NettyHttpSecurityConfiguration for      * configuring secure web resources.      */
DECL|field|securityConfiguration
specifier|private
name|NettyHttpSecurityConfiguration
name|securityConfiguration
decl_stmt|;
comment|/**      * The thread pool size for the EventExecutorGroup if its in use. The      * default value is 16.      */
DECL|field|maximumPoolSize
specifier|private
name|Integer
name|maximumPoolSize
decl_stmt|;
comment|/**      * To use the NettyConfiguration as configuration when creating endpoints.      */
DECL|field|configuration
specifier|private
name|NettyConfiguration
name|configuration
decl_stmt|;
comment|/**      * To use the given EventExecutorGroup      */
DECL|field|executorService
specifier|private
name|EventExecutorGroup
name|executorService
decl_stmt|;
DECL|method|getNettyHttpBinding ()
specifier|public
name|NettyHttpBinding
name|getNettyHttpBinding
parameter_list|()
block|{
return|return
name|nettyHttpBinding
return|;
block|}
DECL|method|setNettyHttpBinding (NettyHttpBinding nettyHttpBinding)
specifier|public
name|void
name|setNettyHttpBinding
parameter_list|(
name|NettyHttpBinding
name|nettyHttpBinding
parameter_list|)
block|{
name|this
operator|.
name|nettyHttpBinding
operator|=
name|nettyHttpBinding
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
DECL|method|getSecurityConfiguration ()
specifier|public
name|NettyHttpSecurityConfiguration
name|getSecurityConfiguration
parameter_list|()
block|{
return|return
name|securityConfiguration
return|;
block|}
DECL|method|setSecurityConfiguration ( NettyHttpSecurityConfiguration securityConfiguration)
specifier|public
name|void
name|setSecurityConfiguration
parameter_list|(
name|NettyHttpSecurityConfiguration
name|securityConfiguration
parameter_list|)
block|{
name|this
operator|.
name|securityConfiguration
operator|=
name|securityConfiguration
expr_stmt|;
block|}
DECL|method|getMaximumPoolSize ()
specifier|public
name|Integer
name|getMaximumPoolSize
parameter_list|()
block|{
return|return
name|maximumPoolSize
return|;
block|}
DECL|method|setMaximumPoolSize (Integer maximumPoolSize)
specifier|public
name|void
name|setMaximumPoolSize
parameter_list|(
name|Integer
name|maximumPoolSize
parameter_list|)
block|{
name|this
operator|.
name|maximumPoolSize
operator|=
name|maximumPoolSize
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|NettyConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (NettyConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|NettyConfiguration
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
DECL|method|getExecutorService ()
specifier|public
name|EventExecutorGroup
name|getExecutorService
parameter_list|()
block|{
return|return
name|executorService
return|;
block|}
DECL|method|setExecutorService (EventExecutorGroup executorService)
specifier|public
name|void
name|setExecutorService
parameter_list|(
name|EventExecutorGroup
name|executorService
parameter_list|)
block|{
name|this
operator|.
name|executorService
operator|=
name|executorService
expr_stmt|;
block|}
block|}
end_class

end_unit

