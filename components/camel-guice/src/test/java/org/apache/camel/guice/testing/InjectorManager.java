begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.guice.testing
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|guice
operator|.
name|testing
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Modifier
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|ConcurrentHashMap
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
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|AbstractModule
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Guice
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Injector
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Module
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
name|guice
operator|.
name|inject
operator|.
name|Injectors
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
name|guice
operator|.
name|support
operator|.
name|CloseErrors
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
name|guice
operator|.
name|support
operator|.
name|CloseFailedException
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
name|guice
operator|.
name|support
operator|.
name|internal
operator|.
name|CloseErrorsImpl
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
name|guice
operator|.
name|util
operator|.
name|CloseableScope
import|;
end_import

begin_comment
comment|/**  * Used to manage the injectors for the various injection points  */
end_comment

begin_class
DECL|class|InjectorManager
specifier|public
class|class
name|InjectorManager
block|{
DECL|field|NESTED_MODULE_CLASS
specifier|private
specifier|static
specifier|final
name|String
name|NESTED_MODULE_CLASS
init|=
literal|"TestModule"
decl_stmt|;
DECL|field|injectors
specifier|private
name|Map
argument_list|<
name|Object
argument_list|,
name|Injector
argument_list|>
name|injectors
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|initializeCounter
specifier|private
name|AtomicInteger
name|initializeCounter
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
DECL|field|testScope
specifier|private
name|CloseableScope
name|testScope
init|=
operator|new
name|CloseableScope
argument_list|(
name|TestScoped
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|classScope
specifier|private
name|CloseableScope
name|classScope
init|=
operator|new
name|CloseableScope
argument_list|(
name|ClassScoped
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|closeSingletonsAfterClasses
specifier|private
name|boolean
name|closeSingletonsAfterClasses
decl_stmt|;
DECL|field|runFinalizer
specifier|private
name|boolean
name|runFinalizer
init|=
literal|true
decl_stmt|;
DECL|field|moduleType
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|Module
argument_list|>
name|moduleType
decl_stmt|;
DECL|method|beforeClasses ()
specifier|public
name|void
name|beforeClasses
parameter_list|()
block|{
name|int
name|counter
init|=
name|initializeCounter
operator|.
name|incrementAndGet
argument_list|()
decl_stmt|;
if|if
condition|(
name|counter
operator|>
literal|1
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"WARNING! Initialised more than once! Counter: "
operator|+
name|counter
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|runFinalizer
condition|)
block|{
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|addShutdownHook
argument_list|(
operator|new
name|Thread
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|closeSingletons
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Failed to shut down Guice Singletons: "
operator|+
name|e
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/** Lets close all of the injectors we have created so far */
DECL|method|afterClasses ()
specifier|public
name|void
name|afterClasses
parameter_list|()
throws|throws
name|CloseFailedException
block|{
name|Injector
name|injector
init|=
name|injectors
operator|.
name|get
argument_list|(
name|moduleType
argument_list|)
decl_stmt|;
if|if
condition|(
name|injector
operator|!=
literal|null
condition|)
block|{
name|classScope
operator|.
name|close
argument_list|(
name|injector
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Could not close Class scope as there is no Injector for module type"
argument_list|)
expr_stmt|;
block|}
comment|// NOTE that we don't have any good hooks yet to call complete()
comment|// when the JVM is completed to ensure real singletons shut down
comment|// correctly
comment|//
if|if
condition|(
name|isCloseSingletonsAfterClasses
argument_list|()
condition|)
block|{
name|closeInjectors
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|beforeTest (Object test)
specifier|public
name|void
name|beforeTest
parameter_list|(
name|Object
name|test
parameter_list|)
throws|throws
name|Exception
block|{
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|test
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
name|testType
init|=
name|test
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|moduleType
operator|=
name|getModuleForTestClass
argument_list|(
name|testType
argument_list|)
expr_stmt|;
name|Injector
name|classInjector
init|=
name|injectors
operator|.
name|get
argument_list|(
name|moduleType
argument_list|)
decl_stmt|;
if|if
condition|(
name|classInjector
operator|==
literal|null
condition|)
block|{
name|classInjector
operator|=
name|createInjector
argument_list|(
name|moduleType
argument_list|)
expr_stmt|;
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|classInjector
argument_list|,
literal|"classInjector"
argument_list|)
expr_stmt|;
name|injectors
operator|.
name|put
argument_list|(
name|moduleType
argument_list|,
name|classInjector
argument_list|)
expr_stmt|;
block|}
name|injectors
operator|.
name|put
argument_list|(
name|testType
argument_list|,
name|classInjector
argument_list|)
expr_stmt|;
name|classInjector
operator|.
name|injectMembers
argument_list|(
name|test
argument_list|)
expr_stmt|;
block|}
DECL|method|afterTest (Object test)
specifier|public
name|void
name|afterTest
parameter_list|(
name|Object
name|test
parameter_list|)
throws|throws
name|Exception
block|{
name|Injector
name|injector
init|=
name|injectors
operator|.
name|get
argument_list|(
name|test
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|injector
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Warning - no injector available for: "
operator|+
name|test
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|testScope
operator|.
name|close
argument_list|(
name|injector
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Closes down any JVM level singletons used in this testing JVM      */
DECL|method|closeSingletons ()
specifier|public
name|void
name|closeSingletons
parameter_list|()
throws|throws
name|CloseFailedException
block|{
name|closeInjectors
argument_list|()
expr_stmt|;
block|}
DECL|method|isCloseSingletonsAfterClasses ()
specifier|public
name|boolean
name|isCloseSingletonsAfterClasses
parameter_list|()
block|{
return|return
name|closeSingletonsAfterClasses
return|;
block|}
DECL|method|setCloseSingletonsAfterClasses (boolean closeSingletonsAfterClasses)
specifier|public
name|void
name|setCloseSingletonsAfterClasses
parameter_list|(
name|boolean
name|closeSingletonsAfterClasses
parameter_list|)
block|{
name|this
operator|.
name|closeSingletonsAfterClasses
operator|=
name|closeSingletonsAfterClasses
expr_stmt|;
block|}
DECL|class|TestModule
specifier|protected
class|class
name|TestModule
extends|extends
name|AbstractModule
block|{
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bindScope
argument_list|(
name|ClassScoped
operator|.
name|class
argument_list|,
name|classScope
argument_list|)
expr_stmt|;
name|bindScope
argument_list|(
name|TestScoped
operator|.
name|class
argument_list|,
name|testScope
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|closeInjectors ()
specifier|protected
name|void
name|closeInjectors
parameter_list|()
throws|throws
name|CloseFailedException
block|{
name|CloseErrors
name|errors
init|=
operator|new
name|CloseErrorsImpl
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Entry
argument_list|<
name|Object
argument_list|,
name|Injector
argument_list|>
argument_list|>
name|entries
init|=
name|injectors
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|Object
argument_list|,
name|Injector
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|Injector
name|injector
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|Injectors
operator|.
name|close
argument_list|(
name|injector
argument_list|,
name|errors
argument_list|)
expr_stmt|;
block|}
name|injectors
operator|.
name|clear
argument_list|()
expr_stmt|;
name|errors
operator|.
name|throwIfNecessary
argument_list|()
expr_stmt|;
block|}
comment|/**      * Factory method to return the module type that will be used to create an      * injector.      *       * The default implementation will use the system property      *<code>org.guiceyfruit.modules</code> (see      * {@link Injectors#MODULE_CLASS_NAMES} otherwise if that is not set it will      * look for the {@link UseModule} annotation and use the module defined on      * that otherwise it will try look for the inner public static class      * "TestModule"      *       * @see org.apache.camel.guice.testing.UseModule      * @see #NESTED_MODULE_CLASS      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getModuleForTestClass (Class<?> objectType)
specifier|protected
name|Class
argument_list|<
name|?
extends|extends
name|Module
argument_list|>
name|getModuleForTestClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|objectType
parameter_list|)
throws|throws
name|IllegalAccessException
throws|,
name|InstantiationException
throws|,
name|ClassNotFoundException
block|{
name|String
name|modules
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|Injectors
operator|.
name|MODULE_CLASS_NAMES
argument_list|)
decl_stmt|;
if|if
condition|(
name|modules
operator|!=
literal|null
condition|)
block|{
name|modules
operator|=
name|modules
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|modules
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Overloading Guice Modules: "
operator|+
name|modules
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
name|Class
argument_list|<
name|?
extends|extends
name|Module
argument_list|>
name|moduleType
decl_stmt|;
name|UseModule
name|config
init|=
name|objectType
operator|.
name|getAnnotation
argument_list|(
name|UseModule
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|config
operator|!=
literal|null
condition|)
block|{
name|moduleType
operator|=
name|config
operator|.
name|value
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|String
name|name
init|=
name|objectType
operator|.
name|getName
argument_list|()
operator|+
literal|"$"
operator|+
name|NESTED_MODULE_CLASS
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|type
decl_stmt|;
try|try
block|{
name|type
operator|=
name|objectType
operator|.
name|getClassLoader
argument_list|()
operator|.
name|loadClass
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
try|try
block|{
name|type
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|loadClass
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e2
parameter_list|)
block|{
throw|throw
operator|new
name|ClassNotFoundException
argument_list|(
literal|"Class "
operator|+
name|objectType
operator|.
name|getName
argument_list|()
operator|+
literal|" does not have a @UseModule annotation nor does it have a nested class called "
operator|+
name|NESTED_MODULE_CLASS
operator|+
literal|" available on the classpath. Please see: http://code.google.com/p/guiceyfruit/wiki/Testing"
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
try|try
block|{
name|moduleType
operator|=
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|Module
argument_list|>
operator|)
name|type
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
name|IllegalArgumentException
argument_list|(
literal|"Class "
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|" is not a Guice Module!"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
name|int
name|modifiers
init|=
name|moduleType
operator|.
name|getModifiers
argument_list|()
decl_stmt|;
if|if
condition|(
name|Modifier
operator|.
name|isAbstract
argument_list|(
name|modifiers
argument_list|)
operator|||
operator|!
name|Modifier
operator|.
name|isPublic
argument_list|(
name|modifiers
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Class "
operator|+
name|moduleType
operator|.
name|getName
argument_list|()
operator|+
literal|" must be a public class which is non abstract"
argument_list|)
throw|;
block|}
try|try
block|{
name|moduleType
operator|.
name|getConstructor
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Class "
operator|+
name|moduleType
operator|.
name|getName
argument_list|()
operator|+
literal|" must have a zero argument constructor"
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|moduleType
return|;
block|}
comment|/**      * Creates the injector for the given key      */
DECL|method|createInjector (Class<? extends Module> moduleType)
specifier|protected
name|Injector
name|createInjector
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Module
argument_list|>
name|moduleType
parameter_list|)
throws|throws
name|InstantiationException
throws|,
name|IllegalAccessException
throws|,
name|ClassNotFoundException
block|{
if|if
condition|(
name|moduleType
operator|==
literal|null
condition|)
block|{
return|return
name|Injectors
operator|.
name|createInjector
argument_list|(
name|System
operator|.
name|getProperties
argument_list|()
argument_list|,
operator|new
name|TestModule
argument_list|()
argument_list|)
return|;
block|}
comment|// System.out.println("Creating Guice Injector from module: " +
comment|// moduleType.getName());
name|Module
name|module
init|=
name|moduleType
operator|.
name|newInstance
argument_list|()
decl_stmt|;
return|return
name|Guice
operator|.
name|createInjector
argument_list|(
name|module
argument_list|,
operator|new
name|TestModule
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

