begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|notNull
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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|FileSystemResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|UrlResource
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
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

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
name|InputStreamReader
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

begin_comment
comment|/**  * A builder class for creating {@link Processor}, {@link Expression} and {@link Predicate} objects using  * the JSR 223 scripting engine.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ScriptBuilder
specifier|public
class|class
name|ScriptBuilder
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
implements|implements
name|Expression
argument_list|<
name|E
argument_list|>
implements|,
name|Predicate
argument_list|<
name|E
argument_list|>
implements|,
name|Processor
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
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
name|Resource
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
name|this
operator|.
name|scriptText
operator|=
name|scriptText
expr_stmt|;
block|}
DECL|method|ScriptBuilder (String scriptEngineName, Resource scriptResource)
specifier|public
name|ScriptBuilder
parameter_list|(
name|String
name|scriptEngineName
parameter_list|,
name|Resource
name|scriptResource
parameter_list|)
block|{
name|this
argument_list|(
name|scriptEngineName
argument_list|)
expr_stmt|;
name|this
operator|.
name|scriptResource
operator|=
name|scriptResource
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
DECL|method|evaluate (E exchange)
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
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
DECL|method|matches (E exchange)
specifier|public
name|boolean
name|matches
parameter_list|(
name|E
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
DECL|method|assertMatches (String text, E exchange)
specifier|public
name|void
name|assertMatches
parameter_list|(
name|String
name|text
parameter_list|,
name|E
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
comment|//-------------------------------------------------------------------------
comment|/**      * Sets the attribute on the context so that it is available to the script as a variable      * in the {@link ScriptContext#ENGINE_SCOPE}      *      * @param name the name of the attribute      * @param value the attribute value      * @return this builder      */
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
comment|// Create any scripting language builder recognised by JSR 223
comment|//-------------------------------------------------------------------------
comment|/**      * Creates a script builder for the named language and script contents      *      * @param language the language to use for the script      * @param scriptText the script text to be evaluted      * @return the builder      */
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
comment|/**      * Creates a script builder for the named language and script @{link Resource}      *      * @param language the language to use for the script      * @param scriptResource the resource used to load the script      * @return the builder      */
DECL|method|script (String language, Resource scriptResource)
specifier|public
specifier|static
name|ScriptBuilder
name|script
parameter_list|(
name|String
name|language
parameter_list|,
name|Resource
name|scriptResource
parameter_list|)
block|{
return|return
operator|new
name|ScriptBuilder
argument_list|(
name|language
argument_list|,
name|scriptResource
argument_list|)
return|;
block|}
comment|/**      * Creates a script builder for the named language and script @{link File}      *      * @param language the language to use for the script      * @param scriptFile the file used to load the script      * @return the builder      */
DECL|method|script (String language, File scriptFile)
specifier|public
specifier|static
name|ScriptBuilder
name|script
parameter_list|(
name|String
name|language
parameter_list|,
name|File
name|scriptFile
parameter_list|)
block|{
return|return
operator|new
name|ScriptBuilder
argument_list|(
name|language
argument_list|,
operator|new
name|FileSystemResource
argument_list|(
name|scriptFile
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates a script builder for the named language and script @{link URL}      *      * @param language the language to use for the script      * @param scriptURL the URL used to load the script      * @return the builder      */
DECL|method|script (String language, URL scriptURL)
specifier|public
specifier|static
name|ScriptBuilder
name|script
parameter_list|(
name|String
name|language
parameter_list|,
name|URL
name|scriptURL
parameter_list|)
block|{
return|return
operator|new
name|ScriptBuilder
argument_list|(
name|language
argument_list|,
operator|new
name|UrlResource
argument_list|(
name|scriptURL
argument_list|)
argument_list|)
return|;
block|}
comment|// Groovy
comment|//-------------------------------------------------------------------------
comment|/**      * Creates a script builder for the groovy script contents      *      * @param scriptText the script text to be evaluted      * @return the builder      */
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
comment|/**      * Creates a script builder for the groovy script @{link Resource}      *      * @param scriptResource the resource used to load the script      * @return the builder      */
DECL|method|groovy (Resource scriptResource)
specifier|public
specifier|static
name|ScriptBuilder
name|groovy
parameter_list|(
name|Resource
name|scriptResource
parameter_list|)
block|{
return|return
operator|new
name|ScriptBuilder
argument_list|(
literal|"groovy"
argument_list|,
name|scriptResource
argument_list|)
return|;
block|}
comment|/**      * Creates a script builder for the groovy script @{link File}      *      * @param scriptFile the file used to load the script      * @return the builder      */
DECL|method|groovy (File scriptFile)
specifier|public
specifier|static
name|ScriptBuilder
name|groovy
parameter_list|(
name|File
name|scriptFile
parameter_list|)
block|{
return|return
operator|new
name|ScriptBuilder
argument_list|(
literal|"groovy"
argument_list|,
operator|new
name|FileSystemResource
argument_list|(
name|scriptFile
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates a script builder for the groovy script @{link URL}      *      * @param scriptURL the URL used to load the script      * @return the builder      */
DECL|method|groovy (URL scriptURL)
specifier|public
specifier|static
name|ScriptBuilder
name|groovy
parameter_list|(
name|URL
name|scriptURL
parameter_list|)
block|{
return|return
operator|new
name|ScriptBuilder
argument_list|(
literal|"groovy"
argument_list|,
operator|new
name|UrlResource
argument_list|(
name|scriptURL
argument_list|)
argument_list|)
return|;
block|}
comment|// JavaScript
comment|//-------------------------------------------------------------------------
comment|/**      * Creates a script builder for the JavaScript/ECMAScript script contents      *      * @param scriptText the script text to be evaluted      * @return the builder      */
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
comment|/**      * Creates a script builder for the JavaScript/ECMAScript script @{link Resource}      *      * @param scriptResource the resource used to load the script      * @return the builder      */
DECL|method|javaScript (Resource scriptResource)
specifier|public
specifier|static
name|ScriptBuilder
name|javaScript
parameter_list|(
name|Resource
name|scriptResource
parameter_list|)
block|{
return|return
operator|new
name|ScriptBuilder
argument_list|(
literal|"js"
argument_list|,
name|scriptResource
argument_list|)
return|;
block|}
comment|/**      * Creates a script builder for the JavaScript/ECMAScript script @{link File}      *      * @param scriptFile the file used to load the script      * @return the builder      */
DECL|method|javaScript (File scriptFile)
specifier|public
specifier|static
name|ScriptBuilder
name|javaScript
parameter_list|(
name|File
name|scriptFile
parameter_list|)
block|{
return|return
operator|new
name|ScriptBuilder
argument_list|(
literal|"js"
argument_list|,
operator|new
name|FileSystemResource
argument_list|(
name|scriptFile
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates a script builder for the JavaScript/ECMAScript script @{link URL}      *      * @param scriptURL the URL used to load the script      * @return the builder      */
DECL|method|javaScript (URL scriptURL)
specifier|public
specifier|static
name|ScriptBuilder
name|javaScript
parameter_list|(
name|URL
name|scriptURL
parameter_list|)
block|{
return|return
operator|new
name|ScriptBuilder
argument_list|(
literal|"js"
argument_list|,
operator|new
name|UrlResource
argument_list|(
name|scriptURL
argument_list|)
argument_list|)
return|;
block|}
comment|// PHP
comment|//-------------------------------------------------------------------------
comment|/**      * Creates a script builder for the PHP script contents      *      * @param scriptText the script text to be evaluted      * @return the builder      */
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
comment|/**      * Creates a script builder for the PHP script @{link Resource}      *      * @param scriptResource the resource used to load the script      * @return the builder      */
DECL|method|php (Resource scriptResource)
specifier|public
specifier|static
name|ScriptBuilder
name|php
parameter_list|(
name|Resource
name|scriptResource
parameter_list|)
block|{
return|return
operator|new
name|ScriptBuilder
argument_list|(
literal|"php"
argument_list|,
name|scriptResource
argument_list|)
return|;
block|}
comment|/**      * Creates a script builder for the PHP script @{link File}      *      * @param scriptFile the file used to load the script      * @return the builder      */
DECL|method|php (File scriptFile)
specifier|public
specifier|static
name|ScriptBuilder
name|php
parameter_list|(
name|File
name|scriptFile
parameter_list|)
block|{
return|return
operator|new
name|ScriptBuilder
argument_list|(
literal|"php"
argument_list|,
operator|new
name|FileSystemResource
argument_list|(
name|scriptFile
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates a script builder for the PHP script @{link URL}      *      * @param scriptURL the URL used to load the script      * @return the builder      */
DECL|method|php (URL scriptURL)
specifier|public
specifier|static
name|ScriptBuilder
name|php
parameter_list|(
name|URL
name|scriptURL
parameter_list|)
block|{
return|return
operator|new
name|ScriptBuilder
argument_list|(
literal|"php"
argument_list|,
operator|new
name|UrlResource
argument_list|(
name|scriptURL
argument_list|)
argument_list|)
return|;
block|}
comment|// JRuby
comment|//-------------------------------------------------------------------------
comment|/**      * Creates a script builder for the Ruby/JRuby script contents      *      * @param scriptText the script text to be evaluted      * @return the builder      */
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
comment|/**      * Creates a script builder for the Ruby/JRuby script @{link Resource}      *      * @param scriptResource the resource used to load the script      * @return the builder      */
DECL|method|ruby (Resource scriptResource)
specifier|public
specifier|static
name|ScriptBuilder
name|ruby
parameter_list|(
name|Resource
name|scriptResource
parameter_list|)
block|{
return|return
operator|new
name|ScriptBuilder
argument_list|(
literal|"jruby"
argument_list|,
name|scriptResource
argument_list|)
return|;
block|}
comment|/**      * Creates a script builder for the Ruby/JRuby script @{link File}      *      * @param scriptFile the file used to load the script      * @return the builder      */
DECL|method|ruby (File scriptFile)
specifier|public
specifier|static
name|ScriptBuilder
name|ruby
parameter_list|(
name|File
name|scriptFile
parameter_list|)
block|{
return|return
operator|new
name|ScriptBuilder
argument_list|(
literal|"jruby"
argument_list|,
operator|new
name|FileSystemResource
argument_list|(
name|scriptFile
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates a script builder for the Ruby/JRuby script @{link URL}      *      * @param scriptURL the URL used to load the script      * @return the builder      */
DECL|method|ruby (URL scriptURL)
specifier|public
specifier|static
name|ScriptBuilder
name|ruby
parameter_list|(
name|URL
name|scriptURL
parameter_list|)
block|{
return|return
operator|new
name|ScriptBuilder
argument_list|(
literal|"jruby"
argument_list|,
operator|new
name|UrlResource
argument_list|(
name|scriptURL
argument_list|)
argument_list|)
return|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getEngine ()
specifier|public
name|ScriptEngine
name|getEngine
parameter_list|()
block|{
name|checkInitialised
argument_list|()
expr_stmt|;
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
name|this
operator|.
name|scriptText
operator|=
name|scriptText
expr_stmt|;
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
operator|.
name|getDescription
argument_list|()
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
DECL|method|getScriptResource ()
specifier|public
name|Resource
name|getScriptResource
parameter_list|()
block|{
return|return
name|scriptResource
return|;
block|}
DECL|method|setScriptResource (Resource scriptResource)
specifier|public
name|void
name|setScriptResource
parameter_list|(
name|Resource
name|scriptResource
parameter_list|)
block|{
name|this
operator|.
name|scriptResource
operator|=
name|scriptResource
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|checkInitialised ()
specifier|protected
name|void
name|checkInitialised
parameter_list|()
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
if|if
condition|(
name|engine
operator|instanceof
name|Compilable
condition|)
block|{
name|compileScript
argument_list|(
operator|(
name|Compilable
operator|)
name|engine
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|matches (E exchange, Object scriptValue)
specifier|protected
name|boolean
name|matches
parameter_list|(
name|E
name|exchange
parameter_list|,
name|Object
name|scriptValue
parameter_list|)
block|{
return|return
name|ObjectConverter
operator|.
name|toBoolean
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
return|return
name|manager
operator|.
name|getEngineByName
argument_list|(
name|scriptEngineName
argument_list|)
return|;
block|}
DECL|method|compileScript (Compilable compilable)
specifier|protected
name|void
name|compileScript
parameter_list|(
name|Compilable
name|compilable
parameter_list|)
block|{
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
name|compiledScript
operator|=
name|compilable
operator|.
name|compile
argument_list|(
name|createScriptReader
argument_list|()
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
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Script compile failed: "
operator|+
name|e
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
return|return
name|runScript
argument_list|()
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
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Script evaluation failed: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
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
DECL|method|runScript ()
specifier|protected
name|Object
name|runScript
parameter_list|()
throws|throws
name|ScriptException
throws|,
name|IOException
block|{
name|checkInitialised
argument_list|()
expr_stmt|;
if|if
condition|(
name|compiledScript
operator|!=
literal|null
condition|)
block|{
return|return
name|compiledScript
operator|.
name|eval
argument_list|()
return|;
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
return|return
name|getEngine
argument_list|()
operator|.
name|eval
argument_list|(
name|scriptText
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|getEngine
argument_list|()
operator|.
name|eval
argument_list|(
name|createScriptReader
argument_list|()
argument_list|)
return|;
block|}
block|}
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
literal|"exchange"
argument_list|,
name|exchange
argument_list|,
name|scope
argument_list|)
expr_stmt|;
name|context
operator|.
name|setAttribute
argument_list|(
literal|"request"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
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
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|,
name|scope
argument_list|)
expr_stmt|;
block|}
DECL|method|createScriptReader ()
specifier|protected
name|InputStreamReader
name|createScriptReader
parameter_list|()
throws|throws
name|IOException
block|{
comment|// TODO consider character sets?
return|return
operator|new
name|InputStreamReader
argument_list|(
name|scriptResource
operator|.
name|getInputStream
argument_list|()
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
block|}
end_class

end_unit

