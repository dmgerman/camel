begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|DisposableBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|InitializingBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ConfigurableApplicationContext
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
name|Lifecycle
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
name|SmartLifecycle
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
name|annotation
operator|.
name|Bean
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertThat
import|;
end_import

begin_class
DECL|class|StartupShutdownOrderBaseTest
specifier|public
specifier|abstract
class|class
name|StartupShutdownOrderBaseTest
block|{
DECL|class|AutoCloseableBean
specifier|static
class|class
name|AutoCloseableBean
implements|implements
name|AutoCloseable
implements|,
name|TestState
block|{
DECL|field|closed
name|boolean
name|closed
decl_stmt|;
DECL|field|context
specifier|private
specifier|final
name|ApplicationContext
name|context
decl_stmt|;
DECL|method|AutoCloseableBean (final ApplicationContext context)
specifier|public
name|AutoCloseableBean
parameter_list|(
specifier|final
name|ApplicationContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|assertValid ()
specifier|public
name|void
name|assertValid
parameter_list|()
block|{
name|assertThat
argument_list|(
name|closed
argument_list|)
operator|.
name|as
argument_list|(
literal|"AutoCloseable bean should be closed"
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
name|assertThat
argument_list|(
name|camelIsStopped
argument_list|(
name|context
argument_list|)
argument_list|)
operator|.
name|as
argument_list|(
literal|"AutoCloseable bean should be stopped after Camel"
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|closed
operator|=
literal|true
expr_stmt|;
block|}
block|}
DECL|class|Beans
specifier|static
class|class
name|Beans
block|{
annotation|@
name|Bean
DECL|method|autoCloseableBean (final ApplicationContext context)
name|AutoCloseableBean
name|autoCloseableBean
parameter_list|(
specifier|final
name|ApplicationContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|AutoCloseableBean
argument_list|(
name|context
argument_list|)
return|;
block|}
annotation|@
name|Bean
DECL|method|beanWithCloseMethod (final ApplicationContext context)
name|BeanWithShutdownMethod
name|beanWithCloseMethod
parameter_list|(
specifier|final
name|ApplicationContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|BeanWithShutdownMethod
argument_list|(
name|context
argument_list|)
return|;
block|}
annotation|@
name|Bean
DECL|method|disposedBean (final ApplicationContext context)
name|DisposeBean
name|disposedBean
parameter_list|(
specifier|final
name|ApplicationContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|DisposeBean
argument_list|(
name|context
argument_list|)
return|;
block|}
annotation|@
name|Bean
DECL|method|initBean (final ApplicationContext context)
name|InitBean
name|initBean
parameter_list|(
specifier|final
name|ApplicationContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|InitBean
argument_list|(
name|context
argument_list|)
return|;
block|}
annotation|@
name|Bean
DECL|method|lifecycleBean (final ApplicationContext context)
name|Lifecycle
name|lifecycleBean
parameter_list|(
specifier|final
name|ApplicationContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|LifecycleBean
argument_list|(
name|context
argument_list|)
return|;
block|}
block|}
DECL|class|BeanWithShutdownMethod
specifier|static
class|class
name|BeanWithShutdownMethod
implements|implements
name|TestState
block|{
DECL|field|shutdown
name|boolean
name|shutdown
decl_stmt|;
DECL|field|context
specifier|private
specifier|final
name|ApplicationContext
name|context
decl_stmt|;
DECL|method|BeanWithShutdownMethod (final ApplicationContext context)
specifier|public
name|BeanWithShutdownMethod
parameter_list|(
specifier|final
name|ApplicationContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|assertValid ()
specifier|public
name|void
name|assertValid
parameter_list|()
block|{
name|assertThat
argument_list|(
name|shutdown
argument_list|)
operator|.
name|as
argument_list|(
literal|"Bean with shutdown method should be shutdown"
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|assertThat
argument_list|(
name|camelIsStopped
argument_list|(
name|context
argument_list|)
argument_list|)
operator|.
name|as
argument_list|(
literal|"@Bean with close() method should be stopped after Camel"
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|shutdown
operator|=
literal|true
expr_stmt|;
block|}
block|}
DECL|class|DisposeBean
specifier|static
class|class
name|DisposeBean
implements|implements
name|DisposableBean
implements|,
name|TestState
block|{
DECL|field|disposed
name|boolean
name|disposed
decl_stmt|;
DECL|field|context
specifier|private
specifier|final
name|ApplicationContext
name|context
decl_stmt|;
DECL|method|DisposeBean (final ApplicationContext context)
specifier|public
name|DisposeBean
parameter_list|(
specifier|final
name|ApplicationContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|assertValid ()
specifier|public
name|void
name|assertValid
parameter_list|()
block|{
name|assertThat
argument_list|(
name|disposed
argument_list|)
operator|.
name|as
argument_list|(
literal|"DisposableBean should be disposed"
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|camelIsStopped
argument_list|(
name|context
argument_list|)
argument_list|)
operator|.
name|as
argument_list|(
literal|"DisposableBean should be stopped after Camel"
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|disposed
operator|=
literal|true
expr_stmt|;
block|}
block|}
DECL|class|InitBean
specifier|static
class|class
name|InitBean
implements|implements
name|InitializingBean
implements|,
name|TestState
block|{
DECL|field|context
name|ApplicationContext
name|context
decl_stmt|;
DECL|field|initialized
name|boolean
name|initialized
decl_stmt|;
annotation|@
name|Autowired
DECL|method|InitBean (final ApplicationContext context)
specifier|public
name|InitBean
parameter_list|(
specifier|final
name|ApplicationContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterPropertiesSet ()
specifier|public
name|void
name|afterPropertiesSet
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|camelIsStopped
argument_list|(
name|context
argument_list|)
argument_list|)
operator|.
name|as
argument_list|(
literal|"initializing bean should be started before Camel"
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|initialized
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|assertValid ()
specifier|public
name|void
name|assertValid
parameter_list|()
block|{
name|assertThat
argument_list|(
name|initialized
argument_list|)
operator|.
name|as
argument_list|(
literal|"InitializingBean should be initialized"
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|LifecycleBean
specifier|static
class|class
name|LifecycleBean
implements|implements
name|SmartLifecycle
implements|,
name|TestState
block|{
DECL|field|context
name|ApplicationContext
name|context
decl_stmt|;
DECL|field|started
name|boolean
name|started
decl_stmt|;
DECL|field|stopped
name|boolean
name|stopped
decl_stmt|;
annotation|@
name|Autowired
DECL|method|LifecycleBean (final ApplicationContext context)
specifier|public
name|LifecycleBean
parameter_list|(
specifier|final
name|ApplicationContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|assertValid ()
specifier|public
name|void
name|assertValid
parameter_list|()
block|{
name|assertThat
argument_list|(
name|started
argument_list|)
operator|.
name|as
argument_list|(
literal|"Lifecycle should have been started"
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|stopped
argument_list|)
operator|.
name|as
argument_list|(
literal|"Lifecycle should be stopped"
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getPhase ()
specifier|public
name|int
name|getPhase
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|isAutoStartup ()
specifier|public
name|boolean
name|isAutoStartup
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|isRunning ()
specifier|public
name|boolean
name|isRunning
parameter_list|()
block|{
return|return
name|started
return|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
name|assertThat
argument_list|(
name|camelIsStopped
argument_list|(
name|context
argument_list|)
argument_list|)
operator|.
name|as
argument_list|(
literal|"lifecycle bean should be started before Camel"
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|started
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|assertThat
argument_list|(
name|camelIsStopped
argument_list|(
name|context
argument_list|)
argument_list|)
operator|.
name|as
argument_list|(
literal|"lifecycle bean should be stopped after Camel"
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|stopped
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop (final Runnable callback)
specifier|public
name|void
name|stop
parameter_list|(
specifier|final
name|Runnable
name|callback
parameter_list|)
block|{
name|stop
argument_list|()
expr_stmt|;
name|callback
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
DECL|interface|TestState
interface|interface
name|TestState
block|{
DECL|method|assertValid ()
name|void
name|assertValid
parameter_list|()
function_decl|;
block|}
annotation|@
name|Test
DECL|method|camelContextShouldBeStartedLastAndStoppedFirst ()
specifier|public
name|void
name|camelContextShouldBeStartedLastAndStoppedFirst
parameter_list|()
block|{
specifier|final
name|ConfigurableApplicationContext
name|context
init|=
name|createContext
argument_list|()
decl_stmt|;
specifier|final
name|ServiceSupport
name|camelContext
init|=
operator|(
name|ServiceSupport
operator|)
name|context
operator|.
name|getBean
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|TestState
argument_list|>
name|testStates
init|=
name|context
operator|.
name|getBeansOfType
argument_list|(
name|TestState
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|camelContext
operator|.
name|isStarted
argument_list|()
argument_list|)
operator|.
name|as
argument_list|(
literal|"Camel context should be started"
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|context
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|camelContext
operator|.
name|isStopped
argument_list|()
argument_list|)
operator|.
name|as
argument_list|(
literal|"Camel context should be stopped"
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|testStates
operator|.
name|values
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|forEach
argument_list|(
name|TestState
operator|::
name|assertValid
argument_list|)
expr_stmt|;
block|}
DECL|method|createContext ()
specifier|abstract
name|ConfigurableApplicationContext
name|createContext
parameter_list|()
function_decl|;
DECL|method|camel (final ApplicationContext context)
specifier|static
name|ServiceSupport
name|camel
parameter_list|(
specifier|final
name|ApplicationContext
name|context
parameter_list|)
block|{
return|return
operator|(
name|ServiceSupport
operator|)
name|context
operator|.
name|getBean
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|camelIsStarted (final ApplicationContext context)
specifier|static
name|boolean
name|camelIsStarted
parameter_list|(
specifier|final
name|ApplicationContext
name|context
parameter_list|)
block|{
return|return
name|camel
argument_list|(
name|context
argument_list|)
operator|.
name|isStarted
argument_list|()
return|;
block|}
DECL|method|camelIsStopped (final ApplicationContext context)
specifier|static
name|boolean
name|camelIsStopped
parameter_list|(
specifier|final
name|ApplicationContext
name|context
parameter_list|)
block|{
return|return
operator|!
name|camelIsStarted
argument_list|(
name|context
argument_list|)
return|;
block|}
block|}
end_class

end_unit

