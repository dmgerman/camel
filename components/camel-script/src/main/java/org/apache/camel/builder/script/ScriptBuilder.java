begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.script
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|script
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|io
operator|.
name|Reader
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
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|script
operator|.
name|Compilable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|script
operator|.
name|CompiledScript
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|script
operator|.
name|ScriptContext
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
name|ScriptEngineManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|script
operator|.
name|ScriptException
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
name|Exchange
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
name|Expression
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
name|Message
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
name|Predicate
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
name|Processor
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
name|converter
operator|.
name|ObjectConverter
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
name|IOHelper
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
name|ObjectHelper
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
name|ResourceHelper
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
comment|/**  * A builder class for creating {@link Processor}, {@link Expression} and  * {@link Predicate} objects using the JSR 223 scripting engine.  *  * @version   */
end_comment

begin_class
DECL|class|ScriptBuilder
specifier|public
class|class
name|ScriptBuilder
implements|implements
name|Expression
implements|,
name|Predicate
implements|,
name|Processor
block|{
comment|/**      * Additional arguments to {@link ScriptEngine} provided as a header on the IN {@link org.apache.camel.Message}      * using the key {@link #ARGUMENTS}      */
DECL|field|ARGUMENTS
specifier|public
specifier|static
specifier|final
name|String
name|ARGUMENTS
init|=
literal|"CamelScriptArguments"
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
name|ScriptBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|scriptEngineName
specifier|private
name|String
name|scriptEngineName
decl_stmt|;
DECL|field|scriptResource
specifier|private
name|String
name|scriptResource
decl_stmt|;
DECL|field|scriptText
specifier|private
name|String
name|scriptText
decl_stmt|;
DECL|field|engine
specifier|private
name|ScriptEngine
name|engine
decl_stmt|;
DECL|field|compiledScript
specifier|private
name|CompiledScript
name|compiledScript
decl_stmt|;
comment|/**      * Constructor.      *      * @param scriptEngineName the name of the scripting language      */
DECL|method|ScriptBuilder (String scriptEngineName)
specifier|public
name|ScriptBuilder
parameter_list|(
name|String
name|scriptEngineName
parameter_list|)
block|{
name|this
operator|.
name|scriptEngineName
operator|=
name|scriptEngineName
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param scriptEngineName the name of the scripting language      * @param scriptText the script text to be evaluated, or a reference to a script resource      */
DECL|method|ScriptBuilder (String scriptEngineName, String scriptText)
specifier|public
name|ScriptBuilder
parameter_list|(
name|String
name|scriptEngineName
parameter_list|,
name|String
name|scriptText
parameter_list|)
block|{
name|this
argument_list|(
name|scriptEngineName
argument_list|)
expr_stmt|;
name|setScriptText
argument_list|(
name|scriptText
argument_list|)
expr_stmt|;
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
name|getScriptDescription
argument_list|()
return|;
block|}
DECL|method|evaluate (Exchange exchange)
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|evaluateScript
argument_list|(
name|exchange
argument_list|)
return|;
block|}
DECL|method|evaluate (Exchange exchange, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Object
name|result
init|=
name|evaluate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|result
argument_list|)
return|;
block|}
DECL|method|matches (Exchange exchange)
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|scriptValue
init|=
name|evaluateScript
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
return|return
name|matches
argument_list|(
name|exchange
argument_list|,
name|scriptValue
argument_list|)
return|;
block|}
DECL|method|assertMatches (String text, Exchange exchange)
specifier|public
name|void
name|assertMatches
parameter_list|(
name|String
name|text
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|AssertionError
block|{
name|Object
name|scriptValue
init|=
name|evaluateScript
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|matches
argument_list|(
name|exchange
argument_list|,
name|scriptValue
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|this
operator|+
literal|" failed on "
operator|+
name|exchange
operator|+
literal|" as script returned<"
operator|+
name|scriptValue
operator|+
literal|">"
argument_list|)
throw|;
block|}
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|evaluateScript
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// Builder API
comment|// -------------------------------------------------------------------------
comment|/**      * Sets the attribute on the context so that it is available to the script      * as a variable in the {@link ScriptContext#ENGINE_SCOPE}      *      * @param name the name of the attribute      * @param value the attribute value      * @return this builder      */
DECL|method|attribute (String name, Object value)
specifier|public
name|ScriptBuilder
name|attribute
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|getScriptContext
argument_list|()
operator|.
name|setAttribute
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|ScriptContext
operator|.
name|ENGINE_SCOPE
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Creates a script builder for the named language and script contents      *      * @param language the language to use for the script      * @param scriptText the script text to be evaluated, or a reference to a script resource      * @return the builder      */
DECL|method|script (String language, String scriptText)
specifier|public
specifier|static
name|ScriptBuilder
name|script
parameter_list|(
name|String
name|language
parameter_list|,
name|String
name|scriptText
parameter_list|)
block|{
return|return
operator|new
name|ScriptBuilder
argument_list|(
name|language
argument_list|,
name|scriptText
argument_list|)
return|;
block|}
comment|/**      * Creates a script builder for the groovy script contents      *      * @param scriptText the script text to be evaluated, or a reference to a script resource      * @return the builder      */
DECL|method|groovy (String scriptText)
specifier|public
specifier|static
name|ScriptBuilder
name|groovy
parameter_list|(
name|String
name|scriptText
parameter_list|)
block|{
return|return
operator|new
name|ScriptBuilder
argument_list|(
literal|"groovy"
argument_list|,
name|scriptText
argument_list|)
return|;
block|}
comment|/**      * Creates a script builder for the JavaScript/ECMAScript script contents      *      * @param scriptText the script text to be evaluated, or a reference to a script resource      * @return the builder      */
DECL|method|javaScript (String scriptText)
specifier|public
specifier|static
name|ScriptBuilder
name|javaScript
parameter_list|(
name|String
name|scriptText
parameter_list|)
block|{
return|return
operator|new
name|ScriptBuilder
argument_list|(
literal|"js"
argument_list|,
name|scriptText
argument_list|)
return|;
block|}
comment|/**      * Creates a script builder for the PHP script contents      *      * @param scriptText the script text to be evaluated, or a reference to a script resource      * @return the builder      */
DECL|method|php (String scriptText)
specifier|public
specifier|static
name|ScriptBuilder
name|php
parameter_list|(
name|String
name|scriptText
parameter_list|)
block|{
return|return
operator|new
name|ScriptBuilder
argument_list|(
literal|"php"
argument_list|,
name|scriptText
argument_list|)
return|;
block|}
comment|/**      * Creates a script builder for the Python script contents      *      * @param scriptText the script text to be evaluated, or a reference to a script resource      * @return the builder      */
DECL|method|python (String scriptText)
specifier|public
specifier|static
name|ScriptBuilder
name|python
parameter_list|(
name|String
name|scriptText
parameter_list|)
block|{
return|return
operator|new
name|ScriptBuilder
argument_list|(
literal|"python"
argument_list|,
name|scriptText
argument_list|)
return|;
block|}
comment|/**      * Creates a script builder for the Ruby/JRuby script contents      *      * @param scriptText the script text to be evaluated, or a reference to a script resource      * @return the builder      */
DECL|method|ruby (String scriptText)
specifier|public
specifier|static
name|ScriptBuilder
name|ruby
parameter_list|(
name|String
name|scriptText
parameter_list|)
block|{
return|return
operator|new
name|ScriptBuilder
argument_list|(
literal|"jruby"
argument_list|,
name|scriptText
argument_list|)
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getEngine ()
specifier|public
name|ScriptEngine
name|getEngine
parameter_list|()
block|{
if|if
condition|(
name|engine
operator|==
literal|null
condition|)
block|{
name|engine
operator|=
name|createScriptEngine
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|engine
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No script engine could be created for: "
operator|+
name|getScriptEngineName
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|engine
return|;
block|}
DECL|method|getCompiledScript ()
specifier|public
name|CompiledScript
name|getCompiledScript
parameter_list|()
block|{
return|return
name|compiledScript
return|;
block|}
DECL|method|getScriptText ()
specifier|public
name|String
name|getScriptText
parameter_list|()
block|{
return|return
name|scriptText
return|;
block|}
DECL|method|setScriptText (String scriptText)
specifier|public
name|void
name|setScriptText
parameter_list|(
name|String
name|scriptText
parameter_list|)
block|{
if|if
condition|(
name|ResourceHelper
operator|.
name|hasScheme
argument_list|(
name|scriptText
argument_list|)
condition|)
block|{
name|this
operator|.
name|scriptResource
operator|=
name|scriptText
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|scriptText
operator|=
name|scriptText
expr_stmt|;
block|}
block|}
DECL|method|getScriptEngineName ()
specifier|public
name|String
name|getScriptEngineName
parameter_list|()
block|{
return|return
name|scriptEngineName
return|;
block|}
comment|/**      * Returns a description of the script      *      * @return the script description      */
DECL|method|getScriptDescription ()
specifier|public
name|String
name|getScriptDescription
parameter_list|()
block|{
if|if
condition|(
name|scriptText
operator|!=
literal|null
condition|)
block|{
return|return
name|scriptEngineName
operator|+
literal|": "
operator|+
name|scriptText
return|;
block|}
elseif|else
if|if
condition|(
name|scriptResource
operator|!=
literal|null
condition|)
block|{
return|return
name|scriptEngineName
operator|+
literal|": "
operator|+
name|scriptResource
return|;
block|}
else|else
block|{
return|return
name|scriptEngineName
operator|+
literal|": null script"
return|;
block|}
block|}
comment|/**      * Access the script context so that it can be configured such as adding attributes      */
DECL|method|getScriptContext ()
specifier|public
name|ScriptContext
name|getScriptContext
parameter_list|()
block|{
return|return
name|getEngine
argument_list|()
operator|.
name|getContext
argument_list|()
return|;
block|}
comment|/**      * Sets the context to use by the script      */
DECL|method|setScriptContext (ScriptContext scriptContext)
specifier|public
name|void
name|setScriptContext
parameter_list|(
name|ScriptContext
name|scriptContext
parameter_list|)
block|{
name|getEngine
argument_list|()
operator|.
name|setContext
argument_list|(
name|scriptContext
argument_list|)
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
DECL|method|checkInitialised (Exchange exchange)
specifier|protected
name|void
name|checkInitialised
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|scriptText
operator|==
literal|null
operator|&&
name|scriptResource
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Neither scriptText or scriptResource are specified"
argument_list|)
throw|;
block|}
if|if
condition|(
name|engine
operator|==
literal|null
condition|)
block|{
name|engine
operator|=
name|createScriptEngine
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|compiledScript
operator|==
literal|null
condition|)
block|{
comment|// BeanShell implements Compilable but throws an exception if you call compile
if|if
condition|(
name|engine
operator|instanceof
name|Compilable
operator|&&
operator|!
name|isBeanShell
argument_list|()
condition|)
block|{
name|compileScript
argument_list|(
operator|(
name|Compilable
operator|)
name|engine
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|matches (Exchange exchange, Object scriptValue)
specifier|protected
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|scriptValue
parameter_list|)
block|{
return|return
name|ObjectConverter
operator|.
name|toBool
argument_list|(
name|scriptValue
argument_list|)
return|;
block|}
DECL|method|createScriptEngine ()
specifier|protected
name|ScriptEngine
name|createScriptEngine
parameter_list|()
block|{
name|ScriptEngineManager
name|manager
init|=
operator|new
name|ScriptEngineManager
argument_list|()
decl_stmt|;
try|try
block|{
name|engine
operator|=
name|manager
operator|.
name|getEngineByName
argument_list|(
name|scriptEngineName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoClassDefFoundError
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Cannot load the scriptEngine for "
operator|+
name|scriptEngineName
operator|+
literal|", the exception is "
operator|+
name|ex
operator|+
literal|", please ensure correct JARs is provided on classpath."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|engine
operator|==
literal|null
condition|)
block|{
name|engine
operator|=
name|checkForOSGiEngine
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|engine
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No script engine could be created for: "
operator|+
name|getScriptEngineName
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|isPython
argument_list|()
condition|)
block|{
name|ScriptContext
name|context
init|=
name|engine
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|setAttribute
argument_list|(
literal|"com.sun.script.jython.comp.mode"
argument_list|,
literal|"eval"
argument_list|,
name|ScriptContext
operator|.
name|ENGINE_SCOPE
argument_list|)
expr_stmt|;
block|}
return|return
name|engine
return|;
block|}
DECL|method|checkForOSGiEngine ()
specifier|private
name|ScriptEngine
name|checkForOSGiEngine
parameter_list|()
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No script engine found for "
operator|+
name|scriptEngineName
operator|+
literal|" using standard javax.script auto-registration.  Checking OSGi registry..."
argument_list|)
expr_stmt|;
try|try
block|{
comment|// Test the OSGi environment with the Activator
name|Class
argument_list|<
name|?
argument_list|>
name|c
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"org.apache.camel.script.osgi.Activator"
argument_list|)
decl_stmt|;
name|Method
name|mth
init|=
name|c
operator|.
name|getDeclaredMethod
argument_list|(
literal|"getBundleContext"
argument_list|)
decl_stmt|;
name|Object
name|ctx
init|=
name|mth
operator|.
name|invoke
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found OSGi BundleContext "
operator|+
name|ctx
argument_list|)
expr_stmt|;
if|if
condition|(
name|ctx
operator|!=
literal|null
condition|)
block|{
name|Method
name|resolveScriptEngine
init|=
name|c
operator|.
name|getDeclaredMethod
argument_list|(
literal|"resolveScriptEngine"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
operator|(
name|ScriptEngine
operator|)
name|resolveScriptEngine
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
name|scriptEngineName
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Unable to load OSGi, script engine cannot be found"
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|compileScript (Compilable compilable, Exchange exchange)
specifier|protected
name|void
name|compileScript
parameter_list|(
name|Compilable
name|compilable
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Reader
name|reader
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|scriptText
operator|!=
literal|null
condition|)
block|{
name|compiledScript
operator|=
name|compilable
operator|.
name|compile
argument_list|(
name|scriptText
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|scriptResource
operator|!=
literal|null
condition|)
block|{
name|reader
operator|=
name|createScriptReader
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|compiledScript
operator|=
name|compilable
operator|.
name|compile
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ScriptException
name|e
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Script compile failed: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
throw|throw
name|createScriptCompileException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
name|createScriptCompileException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|evaluateScript (Exchange exchange)
specifier|protected
specifier|synchronized
name|Object
name|evaluateScript
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
name|getScriptContext
argument_list|()
expr_stmt|;
name|populateBindings
argument_list|(
name|getEngine
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|addScriptEngineArguments
argument_list|(
name|getEngine
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|runScript
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"The script evaluation result is: {}"
argument_list|,
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
catch|catch
parameter_list|(
name|ScriptException
name|e
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Script evaluation failed: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
name|createScriptEvaluationException
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
name|createScriptEvaluationException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
name|createScriptEvaluationException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|runScript (Exchange exchange)
specifier|protected
name|Object
name|runScript
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|ScriptException
throws|,
name|IOException
block|{
name|checkInitialised
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|Object
name|result
decl_stmt|;
if|if
condition|(
name|compiledScript
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|compiledScript
operator|.
name|eval
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|scriptText
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|getEngine
argument_list|()
operator|.
name|eval
argument_list|(
name|scriptText
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|getEngine
argument_list|()
operator|.
name|eval
argument_list|(
name|createScriptReader
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|populateBindings (ScriptEngine engine, Exchange exchange)
specifier|protected
name|void
name|populateBindings
parameter_list|(
name|ScriptEngine
name|engine
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|ScriptContext
name|context
init|=
name|engine
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|int
name|scope
init|=
name|ScriptContext
operator|.
name|ENGINE_SCOPE
decl_stmt|;
name|context
operator|.
name|setAttribute
argument_list|(
literal|"context"
argument_list|,
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|scope
argument_list|)
expr_stmt|;
name|context
operator|.
name|setAttribute
argument_list|(
literal|"camelContext"
argument_list|,
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|scope
argument_list|)
expr_stmt|;
name|context
operator|.
name|setAttribute
argument_list|(
literal|"exchange"
argument_list|,
name|exchange
argument_list|,
name|scope
argument_list|)
expr_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|context
operator|.
name|setAttribute
argument_list|(
literal|"request"
argument_list|,
name|in
argument_list|,
name|scope
argument_list|)
expr_stmt|;
name|context
operator|.
name|setAttribute
argument_list|(
literal|"headers"
argument_list|,
name|in
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|scope
argument_list|)
expr_stmt|;
name|context
operator|.
name|setAttribute
argument_list|(
literal|"body"
argument_list|,
name|in
operator|.
name|getBody
argument_list|()
argument_list|,
name|scope
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|context
operator|.
name|setAttribute
argument_list|(
literal|"out"
argument_list|,
name|out
argument_list|,
name|scope
argument_list|)
expr_stmt|;
name|context
operator|.
name|setAttribute
argument_list|(
literal|"response"
argument_list|,
name|out
argument_list|,
name|scope
argument_list|)
expr_stmt|;
block|}
comment|// to make using properties component easier
name|context
operator|.
name|setAttribute
argument_list|(
literal|"properties"
argument_list|,
operator|new
name|ScriptPropertiesFunction
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
argument_list|,
name|scope
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|addScriptEngineArguments (ScriptEngine engine, Exchange exchange)
specifier|protected
name|void
name|addScriptEngineArguments
parameter_list|(
name|ScriptEngine
name|engine
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
operator|!
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|hasHeaders
argument_list|()
condition|)
block|{
return|return;
block|}
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|args
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ARGUMENTS
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|args
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|args
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|key
argument_list|)
operator|&&
name|value
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Putting {} -> {} on ScriptEngine"
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|engine
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|createScriptReader (Exchange exchange)
specifier|protected
name|InputStreamReader
name|createScriptReader
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|scriptResource
argument_list|,
literal|"scriptResource"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|scriptResource
argument_list|)
decl_stmt|;
return|return
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|)
return|;
block|}
DECL|method|createScriptCompileException (Exception e)
specifier|protected
name|ScriptEvaluationException
name|createScriptCompileException
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
operator|new
name|ScriptEvaluationException
argument_list|(
literal|"Failed to compile: "
operator|+
name|getScriptDescription
argument_list|()
operator|+
literal|". Cause: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
return|;
block|}
DECL|method|createScriptEvaluationException (Throwable e)
specifier|protected
name|ScriptEvaluationException
name|createScriptEvaluationException
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"org.jruby.exceptions.RaiseException"
argument_list|)
condition|)
block|{
comment|// Only the nested exception has the specific problem
try|try
block|{
name|Object
name|ex
init|=
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getException"
argument_list|)
operator|.
name|invoke
argument_list|(
name|e
argument_list|)
decl_stmt|;
return|return
operator|new
name|ScriptEvaluationException
argument_list|(
literal|"Failed to evaluate: "
operator|+
name|getScriptDescription
argument_list|()
operator|+
literal|".  Error: "
operator|+
name|ex
operator|+
literal|". Cause: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e1
parameter_list|)
block|{
comment|// do nothing here
block|}
block|}
return|return
operator|new
name|ScriptEvaluationException
argument_list|(
literal|"Failed to evaluate: "
operator|+
name|getScriptDescription
argument_list|()
operator|+
literal|". Cause: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
return|;
block|}
DECL|method|isPython ()
specifier|protected
name|boolean
name|isPython
parameter_list|()
block|{
return|return
literal|"python"
operator|.
name|equals
argument_list|(
name|scriptEngineName
argument_list|)
operator|||
literal|"jython"
operator|.
name|equals
argument_list|(
name|scriptEngineName
argument_list|)
return|;
block|}
DECL|method|isBeanShell ()
specifier|protected
name|boolean
name|isBeanShell
parameter_list|()
block|{
return|return
literal|"beanshell"
operator|.
name|equals
argument_list|(
name|scriptEngineName
argument_list|)
operator|||
literal|"bsh"
operator|.
name|equals
argument_list|(
name|scriptEngineName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

