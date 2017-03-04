begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.vertx.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|vertx
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|io
operator|.
name|vertx
operator|.
name|core
operator|.
name|Vertx
import|;
end_import

begin_import
import|import
name|io
operator|.
name|vertx
operator|.
name|core
operator|.
name|VertxOptions
import|;
end_import

begin_import
import|import
name|io
operator|.
name|vertx
operator|.
name|core
operator|.
name|spi
operator|.
name|VertxFactory
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
name|NestedConfigurationProperty
import|;
end_import

begin_comment
comment|/**  * The vertx component is used for sending and receive messages from a vertx  * event bus.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.vertx"
argument_list|)
DECL|class|VertxComponentConfiguration
specifier|public
class|class
name|VertxComponentConfiguration
block|{
comment|/**      * To use a custom VertxFactory implementation      */
annotation|@
name|NestedConfigurationProperty
DECL|field|vertxFactory
specifier|private
name|VertxFactory
name|vertxFactory
decl_stmt|;
comment|/**      * Hostname for creating an embedded clustered EventBus      */
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
comment|/**      * Port for creating an embedded clustered EventBus      */
DECL|field|port
specifier|private
name|Integer
name|port
decl_stmt|;
comment|/**      * Options to use for creating vertx      */
annotation|@
name|NestedConfigurationProperty
DECL|field|vertxOptions
specifier|private
name|VertxOptions
name|vertxOptions
decl_stmt|;
comment|/**      * To use the given vertx EventBus instead of creating a new embedded      * EventBus      */
annotation|@
name|NestedConfigurationProperty
DECL|field|vertx
specifier|private
name|Vertx
name|vertx
decl_stmt|;
comment|/**      * Timeout in seconds to wait for clustered Vertx EventBus to be ready. The      * default value is 60.      */
DECL|field|timeout
specifier|private
name|Integer
name|timeout
init|=
literal|60
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getVertxFactory ()
specifier|public
name|VertxFactory
name|getVertxFactory
parameter_list|()
block|{
return|return
name|vertxFactory
return|;
block|}
DECL|method|setVertxFactory (VertxFactory vertxFactory)
specifier|public
name|void
name|setVertxFactory
parameter_list|(
name|VertxFactory
name|vertxFactory
parameter_list|)
block|{
name|this
operator|.
name|vertxFactory
operator|=
name|vertxFactory
expr_stmt|;
block|}
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|Integer
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (Integer port)
specifier|public
name|void
name|setPort
parameter_list|(
name|Integer
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
DECL|method|getVertxOptions ()
specifier|public
name|VertxOptions
name|getVertxOptions
parameter_list|()
block|{
return|return
name|vertxOptions
return|;
block|}
DECL|method|setVertxOptions (VertxOptions vertxOptions)
specifier|public
name|void
name|setVertxOptions
parameter_list|(
name|VertxOptions
name|vertxOptions
parameter_list|)
block|{
name|this
operator|.
name|vertxOptions
operator|=
name|vertxOptions
expr_stmt|;
block|}
DECL|method|getVertx ()
specifier|public
name|Vertx
name|getVertx
parameter_list|()
block|{
return|return
name|vertx
return|;
block|}
DECL|method|setVertx (Vertx vertx)
specifier|public
name|void
name|setVertx
parameter_list|(
name|Vertx
name|vertx
parameter_list|)
block|{
name|this
operator|.
name|vertx
operator|=
name|vertx
expr_stmt|;
block|}
DECL|method|getTimeout ()
specifier|public
name|Integer
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|method|setTimeout (Integer timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|Integer
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
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

