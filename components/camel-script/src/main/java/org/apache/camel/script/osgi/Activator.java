begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.script.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|script
operator|.
name|osgi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|script
operator|.
name|ScriptEngine
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|script
operator|.
name|ScriptEngineFactory
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
name|impl
operator|.
name|osgi
operator|.
name|tracker
operator|.
name|BundleTracker
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
name|impl
operator|.
name|osgi
operator|.
name|tracker
operator|.
name|BundleTrackerCustomizer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleActivator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|InvalidSyntaxException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceRegistration
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

begin_class
DECL|class|Activator
specifier|public
class|class
name|Activator
implements|implements
name|BundleActivator
implements|,
name|BundleTrackerCustomizer
block|{
DECL|field|META_INF_SERVICES_DIR
specifier|public
specifier|static
specifier|final
name|String
name|META_INF_SERVICES_DIR
init|=
literal|"META-INF/services"
decl_stmt|;
DECL|field|SCRIPT_ENGINE_SERVICE_FILE
specifier|public
specifier|static
specifier|final
name|String
name|SCRIPT_ENGINE_SERVICE_FILE
init|=
literal|"javax.script.ScriptEngineFactory"
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Activator
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|context
specifier|private
specifier|static
name|BundleContext
name|context
decl_stmt|;
DECL|field|tracker
specifier|private
name|BundleTracker
name|tracker
decl_stmt|;
DECL|field|resolvers
specifier|private
name|Map
argument_list|<
name|Long
argument_list|,
name|List
argument_list|<
name|BundleScriptEngineResolver
argument_list|>
argument_list|>
name|resolvers
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|Long
argument_list|,
name|List
argument_list|<
name|BundleScriptEngineResolver
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|getBundleContext ()
specifier|public
specifier|static
name|BundleContext
name|getBundleContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
DECL|method|start (BundleContext context)
specifier|public
name|void
name|start
parameter_list|(
name|BundleContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|Activator
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Camel-Script activator starting"
argument_list|)
expr_stmt|;
name|tracker
operator|=
operator|new
name|BundleTracker
argument_list|(
name|context
argument_list|,
name|Bundle
operator|.
name|ACTIVE
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|tracker
operator|.
name|open
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Camel-Script activator started"
argument_list|)
expr_stmt|;
block|}
DECL|method|stop (BundleContext context)
specifier|public
name|void
name|stop
parameter_list|(
name|BundleContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Camel-Script activator stopping"
argument_list|)
expr_stmt|;
name|tracker
operator|.
name|close
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Camel-Script activator stopped"
argument_list|)
expr_stmt|;
name|Activator
operator|.
name|context
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|addingBundle (Bundle bundle, BundleEvent event)
specifier|public
name|Object
name|addingBundle
parameter_list|(
name|Bundle
name|bundle
parameter_list|,
name|BundleEvent
name|event
parameter_list|)
block|{
name|List
argument_list|<
name|BundleScriptEngineResolver
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<
name|BundleScriptEngineResolver
argument_list|>
argument_list|()
decl_stmt|;
name|registerScriptEngines
argument_list|(
name|bundle
argument_list|,
name|r
argument_list|)
expr_stmt|;
for|for
control|(
name|BundleScriptEngineResolver
name|service
range|:
name|r
control|)
block|{
name|service
operator|.
name|register
argument_list|()
expr_stmt|;
block|}
name|resolvers
operator|.
name|put
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|,
name|r
argument_list|)
expr_stmt|;
return|return
name|bundle
return|;
block|}
DECL|method|modifiedBundle (Bundle bundle, BundleEvent event, Object object)
specifier|public
name|void
name|modifiedBundle
parameter_list|(
name|Bundle
name|bundle
parameter_list|,
name|BundleEvent
name|event
parameter_list|,
name|Object
name|object
parameter_list|)
block|{     }
DECL|method|removedBundle (Bundle bundle, BundleEvent event, Object object)
specifier|public
name|void
name|removedBundle
parameter_list|(
name|Bundle
name|bundle
parameter_list|,
name|BundleEvent
name|event
parameter_list|,
name|Object
name|object
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Bundle stopped: {}"
argument_list|,
name|bundle
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|BundleScriptEngineResolver
argument_list|>
name|r
init|=
name|resolvers
operator|.
name|remove
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|BundleScriptEngineResolver
name|service
range|:
name|r
control|)
block|{
name|service
operator|.
name|unregister
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|resolveScriptEngine (String scriptEngineName)
specifier|public
specifier|static
name|ScriptEngine
name|resolveScriptEngine
parameter_list|(
name|String
name|scriptEngineName
parameter_list|)
throws|throws
name|InvalidSyntaxException
block|{
name|ServiceReference
index|[]
name|refs
init|=
name|context
operator|.
name|getServiceReferences
argument_list|(
name|ScriptEngineResolver
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|refs
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"No OSGi script engine resolvers available!"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found "
operator|+
name|refs
operator|.
name|length
operator|+
literal|" OSGi ScriptEngineResolver services"
argument_list|)
expr_stmt|;
for|for
control|(
name|ServiceReference
name|ref
range|:
name|refs
control|)
block|{
name|ScriptEngineResolver
name|resolver
init|=
operator|(
name|ScriptEngineResolver
operator|)
name|context
operator|.
name|getService
argument_list|(
name|ref
argument_list|)
decl_stmt|;
name|ScriptEngine
name|engine
init|=
name|resolver
operator|.
name|resolveScriptEngine
argument_list|(
name|scriptEngineName
argument_list|)
decl_stmt|;
name|context
operator|.
name|ungetService
argument_list|(
name|ref
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"OSGi resolver "
operator|+
name|resolver
operator|+
literal|" produced "
operator|+
name|scriptEngineName
operator|+
literal|" engine "
operator|+
name|engine
argument_list|)
expr_stmt|;
if|if
condition|(
name|engine
operator|!=
literal|null
condition|)
block|{
return|return
name|engine
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|registerScriptEngines (Bundle bundle, List<BundleScriptEngineResolver> resolvers)
specifier|protected
name|void
name|registerScriptEngines
parameter_list|(
name|Bundle
name|bundle
parameter_list|,
name|List
argument_list|<
name|BundleScriptEngineResolver
argument_list|>
name|resolvers
parameter_list|)
block|{
name|URL
name|configURL
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Enumeration
name|e
init|=
name|bundle
operator|.
name|findEntries
argument_list|(
name|META_INF_SERVICES_DIR
argument_list|,
name|SCRIPT_ENGINE_SERVICE_FILE
argument_list|,
literal|false
argument_list|)
init|;
name|e
operator|!=
literal|null
operator|&&
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|configURL
operator|=
operator|(
name|URL
operator|)
name|e
operator|.
name|nextElement
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|configURL
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Found ScriptEngineFactory in "
operator|+
name|bundle
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
expr_stmt|;
name|resolvers
operator|.
name|add
argument_list|(
operator|new
name|BundleScriptEngineResolver
argument_list|(
name|bundle
argument_list|,
name|configURL
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|interface|ScriptEngineResolver
specifier|public
specifier|static
interface|interface
name|ScriptEngineResolver
block|{
DECL|method|resolveScriptEngine (String name)
name|ScriptEngine
name|resolveScriptEngine
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
block|}
DECL|class|BundleScriptEngineResolver
specifier|protected
specifier|static
class|class
name|BundleScriptEngineResolver
implements|implements
name|ScriptEngineResolver
block|{
DECL|field|bundle
specifier|protected
specifier|final
name|Bundle
name|bundle
decl_stmt|;
DECL|field|reg
specifier|private
name|ServiceRegistration
name|reg
decl_stmt|;
DECL|field|configFile
specifier|private
specifier|final
name|URL
name|configFile
decl_stmt|;
DECL|method|BundleScriptEngineResolver (Bundle bundle, URL configFile)
specifier|public
name|BundleScriptEngineResolver
parameter_list|(
name|Bundle
name|bundle
parameter_list|,
name|URL
name|configFile
parameter_list|)
block|{
name|this
operator|.
name|bundle
operator|=
name|bundle
expr_stmt|;
name|this
operator|.
name|configFile
operator|=
name|configFile
expr_stmt|;
block|}
DECL|method|register ()
specifier|public
name|void
name|register
parameter_list|()
block|{
name|reg
operator|=
name|bundle
operator|.
name|getBundleContext
argument_list|()
operator|.
name|registerService
argument_list|(
name|ScriptEngineResolver
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|this
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|unregister ()
specifier|public
name|void
name|unregister
parameter_list|()
block|{
name|reg
operator|.
name|unregister
argument_list|()
expr_stmt|;
block|}
DECL|method|resolveScriptEngine (String name)
specifier|public
name|ScriptEngine
name|resolveScriptEngine
parameter_list|(
name|String
name|name
parameter_list|)
block|{
try|try
block|{
name|BufferedReader
name|in
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|configFile
operator|.
name|openStream
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|className
init|=
name|in
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
name|Class
name|cls
init|=
name|bundle
operator|.
name|loadClass
argument_list|(
name|className
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ScriptEngineFactory
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|cls
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Invalid ScriptEngineFactory: "
operator|+
name|cls
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|ScriptEngineFactory
name|factory
init|=
operator|(
name|ScriptEngineFactory
operator|)
name|cls
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|factory
operator|.
name|getNames
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|test
range|:
name|names
control|)
block|{
if|if
condition|(
name|test
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|ClassLoader
name|old
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
name|ScriptEngine
name|engine
decl_stmt|;
try|try
block|{
comment|// JRuby seems to require the correct TCCL to call getScriptEngine
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|factory
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|engine
operator|=
name|factory
operator|.
name|getScriptEngine
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|old
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Resolved ScriptEngineFactory: {} for expected name: {}"
argument_list|,
name|engine
argument_list|,
name|name
argument_list|)
expr_stmt|;
return|return
name|engine
return|;
block|}
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"ScriptEngineFactory: {} does not match expected name: {}"
argument_list|,
name|factory
operator|.
name|getEngineName
argument_list|()
argument_list|,
name|name
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Cannot create ScriptEngineFactory: "
operator|+
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"OSGi script engine resolver for "
operator|+
name|bundle
operator|.
name|getSymbolicName
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

