begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.reactive.vertx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|reactive
operator|.
name|vertx
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|StaticService
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
name|meta
operator|.
name|Experimental
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
name|ReactiveExecutor
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
name|support
operator|.
name|service
operator|.
name|ServiceSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A VertX based {@link ReactiveExecutor} that uses Vert X event loop.  *<p/>  * NOTE: This is an experimental implementation (use with care)  */
end_comment

begin_class
annotation|@
name|Experimental
DECL|class|VertXReactiveExecutor
specifier|public
class|class
name|VertXReactiveExecutor
extends|extends
name|ServiceSupport
implements|implements
name|ReactiveExecutor
implements|,
name|StaticService
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|VertXReactiveExecutor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|vertx
specifier|private
name|Vertx
name|vertx
decl_stmt|;
DECL|field|shouldClose
specifier|private
name|boolean
name|shouldClose
decl_stmt|;
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
comment|/**      * To use an existing instance of {@link Vertx} instead of creating a default instance.      */
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
annotation|@
name|Override
DECL|method|schedule (Runnable runnable, String description)
specifier|public
name|void
name|schedule
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"schedule: {}"
argument_list|,
name|runnable
argument_list|)
expr_stmt|;
if|if
condition|(
name|description
operator|!=
literal|null
condition|)
block|{
name|runnable
operator|=
name|describe
argument_list|(
name|runnable
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
name|vertx
operator|.
name|nettyEventLoopGroup
argument_list|()
operator|.
name|execute
argument_list|(
name|runnable
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|scheduleMain (Runnable runnable, String description)
specifier|public
name|void
name|scheduleMain
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"scheduleMain: {}"
argument_list|,
name|runnable
argument_list|)
expr_stmt|;
if|if
condition|(
name|description
operator|!=
literal|null
condition|)
block|{
name|runnable
operator|=
name|describe
argument_list|(
name|runnable
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
name|vertx
operator|.
name|nettyEventLoopGroup
argument_list|()
operator|.
name|execute
argument_list|(
name|runnable
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|scheduleSync (Runnable runnable, String description)
specifier|public
name|void
name|scheduleSync
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"scheduleSync: {}"
argument_list|,
name|runnable
argument_list|)
expr_stmt|;
if|if
condition|(
name|description
operator|!=
literal|null
condition|)
block|{
name|runnable
operator|=
name|describe
argument_list|(
name|runnable
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Runnable
name|task
init|=
name|runnable
decl_stmt|;
name|vertx
operator|.
name|executeBlocking
argument_list|(
name|future
lambda|->
block|{
name|task
operator|.
name|run
argument_list|()
expr_stmt|;
name|future
operator|.
name|complete
argument_list|()
expr_stmt|;
block|}
argument_list|,
name|res
lambda|->
block|{}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|executeFromQueue ()
specifier|public
name|boolean
name|executeFromQueue
parameter_list|()
block|{
comment|// not supported so return false
return|return
literal|false
return|;
block|}
DECL|method|describe (Runnable runnable, String description)
specifier|private
specifier|static
name|Runnable
name|describe
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|String
name|description
parameter_list|)
block|{
return|return
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|runnable
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|description
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|vertx
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Starting VertX"
argument_list|)
expr_stmt|;
name|shouldClose
operator|=
literal|true
expr_stmt|;
name|vertx
operator|=
name|Vertx
operator|.
name|vertx
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|vertx
operator|!=
literal|null
operator|&&
name|shouldClose
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Stopping VertX"
argument_list|)
expr_stmt|;
name|vertx
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

