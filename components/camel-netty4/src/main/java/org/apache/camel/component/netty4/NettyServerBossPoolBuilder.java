begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4
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
package|;
end_package

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|EventLoopGroup
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|epoll
operator|.
name|EpollEventLoopGroup
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|nio
operator|.
name|NioEventLoopGroup
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
name|util
operator|.
name|concurrent
operator|.
name|CamelThreadFactory
import|;
end_import

begin_comment
comment|/**  * A builder to create Netty {@link io.netty.channel.EventLoopGroup} which can be used for executor boss events  * with multiple Netty {@link org.apache.camel.component.netty4.NettyServerBootstrapFactory} server bootstrap configurations.  */
end_comment

begin_class
DECL|class|NettyServerBossPoolBuilder
specifier|public
specifier|final
class|class
name|NettyServerBossPoolBuilder
block|{
DECL|field|name
specifier|private
name|String
name|name
init|=
literal|"NettyServerBoss"
decl_stmt|;
DECL|field|pattern
specifier|private
name|String
name|pattern
decl_stmt|;
DECL|field|bossCount
specifier|private
name|int
name|bossCount
init|=
literal|1
decl_stmt|;
DECL|field|nativeTransport
specifier|private
name|boolean
name|nativeTransport
decl_stmt|;
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|setPattern (String pattern)
specifier|public
name|void
name|setPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
block|}
DECL|method|setBossCount (int bossCount)
specifier|public
name|void
name|setBossCount
parameter_list|(
name|int
name|bossCount
parameter_list|)
block|{
name|this
operator|.
name|bossCount
operator|=
name|bossCount
expr_stmt|;
block|}
DECL|method|setNativeTransport (boolean nativeTransport)
specifier|public
name|void
name|setNativeTransport
parameter_list|(
name|boolean
name|nativeTransport
parameter_list|)
block|{
name|this
operator|.
name|nativeTransport
operator|=
name|nativeTransport
expr_stmt|;
block|}
DECL|method|withName (String name)
specifier|public
name|NettyServerBossPoolBuilder
name|withName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withPattern (String pattern)
specifier|public
name|NettyServerBossPoolBuilder
name|withPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|setPattern
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withBossCount (int bossCount)
specifier|public
name|NettyServerBossPoolBuilder
name|withBossCount
parameter_list|(
name|int
name|bossCount
parameter_list|)
block|{
name|setBossCount
argument_list|(
name|bossCount
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withNativeTransport (boolean nativeTransport)
specifier|public
name|NettyServerBossPoolBuilder
name|withNativeTransport
parameter_list|(
name|boolean
name|nativeTransport
parameter_list|)
block|{
name|setNativeTransport
argument_list|(
name|nativeTransport
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Creates a new boss pool.      */
DECL|method|build ()
specifier|public
name|EventLoopGroup
name|build
parameter_list|()
block|{
if|if
condition|(
name|nativeTransport
condition|)
block|{
return|return
operator|new
name|EpollEventLoopGroup
argument_list|(
name|bossCount
argument_list|,
operator|new
name|CamelThreadFactory
argument_list|(
name|pattern
argument_list|,
name|name
argument_list|,
literal|false
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|NioEventLoopGroup
argument_list|(
name|bossCount
argument_list|,
operator|new
name|CamelThreadFactory
argument_list|(
name|pattern
argument_list|,
name|name
argument_list|,
literal|false
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

