begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

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
name|NoFactoryAvailableException
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
name|NoSuchLanguageException
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
name|FactoryFinder
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
name|Language
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
name|LanguageResolver
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
comment|/**  * Default language resolver that looks for language factories in<b>META-INF/services/org/apache/camel/language/</b> and  * language resolvers in<b>META-INF/services/org/apache/camel/language/resolver/</b>.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultLanguageResolver
specifier|public
class|class
name|DefaultLanguageResolver
implements|implements
name|LanguageResolver
block|{
DECL|field|LANGUAGE_RESOURCE_PATH
specifier|public
specifier|static
specifier|final
name|String
name|LANGUAGE_RESOURCE_PATH
init|=
literal|"META-INF/services/org/apache/camel/language/"
decl_stmt|;
DECL|field|LANGUAGE_RESOLVER_RESOURCE_PATH
specifier|public
specifier|static
specifier|final
name|String
name|LANGUAGE_RESOLVER_RESOURCE_PATH
init|=
name|LANGUAGE_RESOURCE_PATH
operator|+
literal|"resolver/"
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
name|DefaultLanguageResolver
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|languageFactory
specifier|protected
name|FactoryFinder
name|languageFactory
decl_stmt|;
DECL|field|languageResolver
specifier|protected
name|FactoryFinder
name|languageResolver
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|resolveLanguage (String name, CamelContext context)
specifier|public
name|Language
name|resolveLanguage
parameter_list|(
name|String
name|name
parameter_list|,
name|CamelContext
name|context
parameter_list|)
block|{
comment|// lookup in registry first
name|Object
name|bean
init|=
literal|null
decl_stmt|;
try|try
block|{
name|bean
operator|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|bean
operator|!=
literal|null
operator|&&
name|getLog
argument_list|()
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Found language: "
operator|+
name|name
operator|+
literal|" in registry: "
operator|+
name|bean
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|getLog
argument_list|()
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Ignored error looking up bean: "
operator|+
name|name
operator|+
literal|". Error: "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|bean
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|bean
operator|instanceof
name|Language
condition|)
block|{
return|return
operator|(
name|Language
operator|)
name|bean
return|;
block|}
comment|// we do not throw the exception here and try to auto create a Language from META-INF
block|}
name|Class
name|type
init|=
literal|null
decl_stmt|;
try|try
block|{
name|type
operator|=
name|findLanguage
argument_list|(
name|name
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoFactoryAvailableException
name|e
parameter_list|)
block|{
comment|// ignore
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
literal|"Invalid URI, no Language registered for scheme: "
operator|+
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|Language
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
operator|(
name|Language
operator|)
name|context
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Resolving language: "
operator|+
name|name
operator|+
literal|" detected type conflict: Not a Language implementation. Found: "
operator|+
name|type
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
else|else
block|{
comment|// no specific language found then try fallback
return|return
name|noSpecificLanguageFound
argument_list|(
name|name
argument_list|,
name|context
argument_list|)
return|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|noSpecificLanguageFound (String name, CamelContext context)
specifier|protected
name|Language
name|noSpecificLanguageFound
parameter_list|(
name|String
name|name
parameter_list|,
name|CamelContext
name|context
parameter_list|)
block|{
name|Class
name|type
init|=
literal|null
decl_stmt|;
try|try
block|{
name|type
operator|=
name|findLanguageResolver
argument_list|(
literal|"default"
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoFactoryAvailableException
name|e
parameter_list|)
block|{
comment|// ignore
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
literal|"Invalid URI, no LanguageResolver registered for scheme: "
operator|+
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|LanguageResolver
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|LanguageResolver
name|resolver
init|=
operator|(
name|LanguageResolver
operator|)
name|context
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
decl_stmt|;
return|return
name|resolver
operator|.
name|resolveLanguage
argument_list|(
name|name
argument_list|,
name|context
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Resolving language: "
operator|+
name|name
operator|+
literal|" detected type conflict: Not a LanguageResolver implementation. Found: "
operator|+
name|type
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
throw|throw
operator|new
name|NoSuchLanguageException
argument_list|(
name|name
argument_list|)
throw|;
block|}
DECL|method|findLanguage (String name, CamelContext context)
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|findLanguage
parameter_list|(
name|String
name|name
parameter_list|,
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|languageFactory
operator|==
literal|null
condition|)
block|{
name|languageFactory
operator|=
name|context
operator|.
name|getFactoryFinder
argument_list|(
name|LANGUAGE_RESOURCE_PATH
argument_list|)
expr_stmt|;
block|}
return|return
name|languageFactory
operator|.
name|findClass
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|findLanguageResolver (String name, CamelContext context)
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|findLanguageResolver
parameter_list|(
name|String
name|name
parameter_list|,
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|languageResolver
operator|==
literal|null
condition|)
block|{
name|languageResolver
operator|=
name|context
operator|.
name|getFactoryFinder
argument_list|(
name|LANGUAGE_RESOLVER_RESOURCE_PATH
argument_list|)
expr_stmt|;
block|}
return|return
name|languageResolver
operator|.
name|findClass
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|getLog ()
specifier|protected
name|Logger
name|getLog
parameter_list|()
block|{
return|return
name|LOG
return|;
block|}
block|}
end_class

end_unit

