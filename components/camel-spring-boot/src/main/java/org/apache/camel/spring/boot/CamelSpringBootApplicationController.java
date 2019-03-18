begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PreDestroy
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
name|CamelContext
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|main
operator|.
name|Main
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_class
DECL|class|CamelSpringBootApplicationController
specifier|public
class|class
name|CamelSpringBootApplicationController
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
name|CamelSpringBootApplicationController
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|main
specifier|private
specifier|final
name|Main
name|main
decl_stmt|;
DECL|field|latch
specifier|private
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|completed
specifier|private
specifier|final
name|AtomicBoolean
name|completed
init|=
operator|new
name|AtomicBoolean
argument_list|()
decl_stmt|;
DECL|method|CamelSpringBootApplicationController (final ApplicationContext applicationContext, final CamelContext camelContext)
specifier|public
name|CamelSpringBootApplicationController
parameter_list|(
specifier|final
name|ApplicationContext
name|applicationContext
parameter_list|,
specifier|final
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|main
operator|=
operator|new
name|Main
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|ProducerTemplate
name|findOrCreateCamelTemplate
parameter_list|()
block|{
return|return
name|applicationContext
operator|.
name|getBean
argument_list|(
name|ProducerTemplate
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Controller is shutting down CamelContext"
argument_list|)
expr_stmt|;
try|try
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|completed
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// should use the latch on this instance
name|CamelSpringBootApplicationController
operator|.
name|this
operator|.
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
expr_stmt|;
block|}
DECL|method|getLatch ()
specifier|public
name|CountDownLatch
name|getLatch
parameter_list|()
block|{
return|return
name|this
operator|.
name|latch
return|;
block|}
DECL|method|getCompleted ()
specifier|public
name|AtomicBoolean
name|getCompleted
parameter_list|()
block|{
return|return
name|completed
return|;
block|}
comment|/**      * Runs the application and blocks the main thread and shutdown Camel graceful when the JVM is stopping.      */
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Controller is starting and waiting for Spring-Boot to stop or JVM to terminate"
argument_list|)
expr_stmt|;
try|try
block|{
name|main
operator|.
name|run
argument_list|()
expr_stmt|;
comment|// keep the daemon thread running
name|LOG
operator|.
name|debug
argument_list|(
literal|"Waiting for CamelContext to complete shutdown"
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"CamelContext shutdown complete."
argument_list|)
expr_stmt|;
block|}
comment|/**      * @deprecated use {@link #run()}      */
annotation|@
name|Deprecated
DECL|method|blockMainThread ()
specifier|public
name|void
name|blockMainThread
parameter_list|()
block|{
name|run
argument_list|()
expr_stmt|;
block|}
annotation|@
name|PreDestroy
DECL|method|destroy ()
specifier|private
name|void
name|destroy
parameter_list|()
block|{
name|main
operator|.
name|completed
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

