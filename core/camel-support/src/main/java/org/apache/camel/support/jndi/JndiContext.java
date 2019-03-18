begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.jndi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|jndi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|javax
operator|.
name|naming
operator|.
name|Binding
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|CompositeName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|LinkRef
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Name
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NameClassPair
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NameNotFoundException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NameParser
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingEnumeration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NotContextException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|OperationNotSupportedException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Reference
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|spi
operator|.
name|NamingManager
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
name|IntrospectionSupport
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
name|CastUtils
import|;
end_import

begin_comment
comment|/**  * A default JNDI context  */
end_comment

begin_class
DECL|class|JndiContext
specifier|public
class|class
name|JndiContext
implements|implements
name|Context
implements|,
name|Serializable
block|{
DECL|field|SEPARATOR
specifier|public
specifier|static
specifier|final
name|String
name|SEPARATOR
init|=
literal|"/"
decl_stmt|;
DECL|field|NAME_PARSER
specifier|protected
specifier|static
specifier|final
name|NameParser
name|NAME_PARSER
init|=
operator|new
name|NameParser
argument_list|()
block|{
specifier|public
name|Name
name|parse
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|NamingException
block|{
return|return
operator|new
name|CompositeName
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
decl_stmt|;
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|5754338187296859149L
decl_stmt|;
DECL|field|environment
specifier|private
specifier|final
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|environment
decl_stmt|;
comment|// environment for this context
DECL|field|bindings
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|bindings
decl_stmt|;
comment|// bindings at my level
DECL|field|treeBindings
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|treeBindings
decl_stmt|;
comment|// all bindings under me
DECL|field|frozen
specifier|private
name|boolean
name|frozen
decl_stmt|;
DECL|field|nameInNamespace
specifier|private
name|String
name|nameInNamespace
init|=
literal|""
decl_stmt|;
DECL|method|JndiContext ()
specifier|public
name|JndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|this
argument_list|(
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|JndiContext (Hashtable<String, Object>env)
specifier|public
name|JndiContext
parameter_list|(
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|env
parameter_list|)
throws|throws
name|Exception
block|{
name|this
argument_list|(
name|env
argument_list|,
name|createBindingsMapFromEnvironment
argument_list|(
name|env
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|JndiContext (Hashtable<String, Object> environment, Map<String, Object> bindings)
specifier|public
name|JndiContext
parameter_list|(
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|environment
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|bindings
parameter_list|)
block|{
name|this
operator|.
name|environment
operator|=
name|environment
operator|==
literal|null
condition|?
operator|new
name|Hashtable
argument_list|<>
argument_list|()
else|:
operator|new
name|Hashtable
argument_list|<>
argument_list|(
name|environment
argument_list|)
expr_stmt|;
name|this
operator|.
name|bindings
operator|=
name|bindings
expr_stmt|;
name|treeBindings
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
DECL|method|JndiContext (Hashtable<String, Object> environment, Map<String, Object> bindings, String nameInNamespace)
specifier|public
name|JndiContext
parameter_list|(
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|environment
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|bindings
parameter_list|,
name|String
name|nameInNamespace
parameter_list|)
block|{
name|this
argument_list|(
name|environment
argument_list|,
name|bindings
argument_list|)
expr_stmt|;
name|this
operator|.
name|nameInNamespace
operator|=
name|nameInNamespace
expr_stmt|;
block|}
DECL|method|JndiContext (JndiContext clone, Hashtable<String, Object> env)
specifier|protected
name|JndiContext
parameter_list|(
name|JndiContext
name|clone
parameter_list|,
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|env
parameter_list|)
block|{
name|this
operator|.
name|bindings
operator|=
name|clone
operator|.
name|bindings
expr_stmt|;
name|this
operator|.
name|treeBindings
operator|=
name|clone
operator|.
name|treeBindings
expr_stmt|;
name|this
operator|.
name|environment
operator|=
operator|new
name|Hashtable
argument_list|<>
argument_list|(
name|env
argument_list|)
expr_stmt|;
block|}
DECL|method|JndiContext (JndiContext clone, Hashtable<String, Object> env, String nameInNamespace)
specifier|protected
name|JndiContext
parameter_list|(
name|JndiContext
name|clone
parameter_list|,
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|env
parameter_list|,
name|String
name|nameInNamespace
parameter_list|)
block|{
name|this
argument_list|(
name|clone
argument_list|,
name|env
argument_list|)
expr_stmt|;
name|this
operator|.
name|nameInNamespace
operator|=
name|nameInNamespace
expr_stmt|;
block|}
comment|/**      * A helper method to create the JNDI bindings from the input environment      * properties using $foo.class to point to a class name with $foo.* being      * properties set on the injected bean      */
DECL|method|createBindingsMapFromEnvironment (Hashtable<String, Object> env)
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|createBindingsMapFromEnvironment
parameter_list|(
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|env
parameter_list|)
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|answer
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|env
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|env
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
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
name|key
operator|!=
literal|null
operator|&&
name|value
operator|instanceof
name|String
condition|)
block|{
name|String
name|valueText
init|=
operator|(
name|String
operator|)
name|value
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|endsWith
argument_list|(
literal|".class"
argument_list|)
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
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
name|loadClass
argument_list|(
name|valueText
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|String
name|newEntry
init|=
name|key
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|key
operator|.
name|length
argument_list|()
operator|-
literal|".class"
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|bean
init|=
name|createBean
argument_list|(
name|type
argument_list|,
name|answer
argument_list|,
name|newEntry
operator|+
literal|"."
argument_list|)
decl_stmt|;
if|if
condition|(
name|bean
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|put
argument_list|(
name|newEntry
argument_list|,
name|bean
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|freeze ()
specifier|public
name|void
name|freeze
parameter_list|()
block|{
name|frozen
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|isFrozen ()
name|boolean
name|isFrozen
parameter_list|()
block|{
return|return
name|frozen
return|;
block|}
comment|/**      * internalBind is intended for use only during setup or possibly by      * suitably synchronized superclasses. It binds every possible lookup into a      * map in each context. To do this, each context strips off one name segment      * and if necessary creates a new context for it. Then it asks that context      * to bind the remaining name. It returns a map containing all the bindings      * from the next context, plus the context it just created (if it in fact      * created it). (the names are suitably extended by the segment originally      * lopped off).      */
DECL|method|internalBind (String name, Object value)
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|internalBind
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|NamingException
block|{
assert|assert
name|name
operator|!=
literal|null
operator|&&
name|name
operator|.
name|length
argument_list|()
operator|>
literal|0
assert|;
assert|assert
operator|!
name|frozen
assert|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|newBindings
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|int
name|pos
init|=
name|name
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|==
operator|-
literal|1
condition|)
block|{
if|if
condition|(
name|treeBindings
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|NamingException
argument_list|(
literal|"Something already bound at "
operator|+
name|name
argument_list|)
throw|;
block|}
name|bindings
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|newBindings
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|segment
init|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
decl_stmt|;
assert|assert
name|segment
operator|!=
literal|null
assert|;
assert|assert
operator|!
name|segment
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
assert|;
name|Object
name|o
init|=
name|treeBindings
operator|.
name|get
argument_list|(
name|segment
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
name|o
operator|=
name|newContext
argument_list|()
expr_stmt|;
name|treeBindings
operator|.
name|put
argument_list|(
name|segment
argument_list|,
name|o
argument_list|)
expr_stmt|;
name|bindings
operator|.
name|put
argument_list|(
name|segment
argument_list|,
name|o
argument_list|)
expr_stmt|;
name|newBindings
operator|.
name|put
argument_list|(
name|segment
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|JndiContext
operator|)
condition|)
block|{
throw|throw
operator|new
name|NamingException
argument_list|(
literal|"Something already bound where a subcontext should go"
argument_list|)
throw|;
block|}
name|JndiContext
name|defaultContext
init|=
operator|(
name|JndiContext
operator|)
name|o
decl_stmt|;
name|String
name|remainder
init|=
name|name
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|subBindings
init|=
name|defaultContext
operator|.
name|internalBind
argument_list|(
name|remainder
argument_list|,
name|value
argument_list|)
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|subBindings
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|subName
init|=
name|segment
operator|+
literal|"/"
operator|+
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|bound
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|treeBindings
operator|.
name|put
argument_list|(
name|subName
argument_list|,
name|bound
argument_list|)
expr_stmt|;
name|newBindings
operator|.
name|put
argument_list|(
name|subName
argument_list|,
name|bound
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|newBindings
return|;
block|}
DECL|method|newContext ()
specifier|protected
name|JndiContext
name|newContext
parameter_list|()
block|{
try|try
block|{
return|return
operator|new
name|JndiContext
argument_list|()
return|;
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
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|addToEnvironment (String propName, Object propVal)
specifier|public
name|Object
name|addToEnvironment
parameter_list|(
name|String
name|propName
parameter_list|,
name|Object
name|propVal
parameter_list|)
throws|throws
name|NamingException
block|{
return|return
name|environment
operator|.
name|put
argument_list|(
name|propName
argument_list|,
name|propVal
argument_list|)
return|;
block|}
DECL|method|getEnvironment ()
specifier|public
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getEnvironment
parameter_list|()
throws|throws
name|NamingException
block|{
return|return
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Hashtable
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|environment
operator|.
name|clone
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|Object
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|removeFromEnvironment (String propName)
specifier|public
name|Object
name|removeFromEnvironment
parameter_list|(
name|String
name|propName
parameter_list|)
throws|throws
name|NamingException
block|{
return|return
name|environment
operator|.
name|remove
argument_list|(
name|propName
argument_list|)
return|;
block|}
DECL|method|lookup (String name)
specifier|public
name|Object
name|lookup
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|NamingException
block|{
if|if
condition|(
name|name
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
name|Object
name|result
init|=
name|treeBindings
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|result
operator|=
name|bindings
operator|.
name|get
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|int
name|pos
init|=
name|name
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|>
literal|0
condition|)
block|{
name|String
name|scheme
init|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
decl_stmt|;
name|Context
name|ctx
init|=
name|NamingManager
operator|.
name|getURLContext
argument_list|(
name|scheme
argument_list|,
name|environment
argument_list|)
decl_stmt|;
if|if
condition|(
name|ctx
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NamingException
argument_list|(
literal|"scheme "
operator|+
name|scheme
operator|+
literal|" not recognized"
argument_list|)
throw|;
block|}
return|return
name|ctx
operator|.
name|lookup
argument_list|(
name|name
argument_list|)
return|;
block|}
else|else
block|{
comment|// Split out the first name of the path
comment|// and look for it in the bindings map.
name|CompositeName
name|path
init|=
operator|new
name|CompositeName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
else|else
block|{
name|String
name|first
init|=
name|path
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Object
name|value
init|=
name|bindings
operator|.
name|get
argument_list|(
name|first
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NameNotFoundException
argument_list|(
name|name
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Context
operator|&&
name|path
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|Context
name|subContext
init|=
operator|(
name|Context
operator|)
name|value
decl_stmt|;
name|value
operator|=
name|subContext
operator|.
name|lookup
argument_list|(
name|path
operator|.
name|getSuffix
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
block|}
block|}
if|if
condition|(
name|result
operator|instanceof
name|LinkRef
condition|)
block|{
name|LinkRef
name|ref
init|=
operator|(
name|LinkRef
operator|)
name|result
decl_stmt|;
name|result
operator|=
name|lookup
argument_list|(
name|ref
operator|.
name|getLinkName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|instanceof
name|Reference
condition|)
block|{
try|try
block|{
name|result
operator|=
name|NamingManager
operator|.
name|getObjectInstance
argument_list|(
name|result
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|this
operator|.
name|environment
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NamingException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|(
name|NamingException
operator|)
operator|new
name|NamingException
argument_list|(
literal|"could not look up : "
operator|+
name|name
argument_list|)
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|result
operator|instanceof
name|JndiContext
condition|)
block|{
name|String
name|prefix
init|=
name|getNameInNamespace
argument_list|()
decl_stmt|;
if|if
condition|(
name|prefix
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|prefix
operator|=
name|prefix
operator|+
name|SEPARATOR
expr_stmt|;
block|}
name|result
operator|=
operator|new
name|JndiContext
argument_list|(
operator|(
name|JndiContext
operator|)
name|result
argument_list|,
name|environment
argument_list|,
name|prefix
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|lookup (Name name)
specifier|public
name|Object
name|lookup
parameter_list|(
name|Name
name|name
parameter_list|)
throws|throws
name|NamingException
block|{
return|return
name|lookup
argument_list|(
name|name
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
DECL|method|lookupLink (String name)
specifier|public
name|Object
name|lookupLink
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|NamingException
block|{
return|return
name|lookup
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|composeName (Name name, Name prefix)
specifier|public
name|Name
name|composeName
parameter_list|(
name|Name
name|name
parameter_list|,
name|Name
name|prefix
parameter_list|)
throws|throws
name|NamingException
block|{
name|Name
name|result
init|=
operator|(
name|Name
operator|)
name|prefix
operator|.
name|clone
argument_list|()
decl_stmt|;
name|result
operator|.
name|addAll
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|composeName (String name, String prefix)
specifier|public
name|String
name|composeName
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|prefix
parameter_list|)
throws|throws
name|NamingException
block|{
name|CompositeName
name|result
init|=
operator|new
name|CompositeName
argument_list|(
name|prefix
argument_list|)
decl_stmt|;
name|result
operator|.
name|addAll
argument_list|(
operator|new
name|CompositeName
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|result
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|list (String name)
specifier|public
name|NamingEnumeration
argument_list|<
name|NameClassPair
argument_list|>
name|list
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|NamingException
block|{
name|Object
name|o
init|=
name|lookup
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|==
name|this
condition|)
block|{
return|return
name|CastUtils
operator|.
name|cast
argument_list|(
operator|new
name|ListEnumeration
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|Context
condition|)
block|{
return|return
operator|(
operator|(
name|Context
operator|)
name|o
operator|)
operator|.
name|list
argument_list|(
literal|""
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|NotContextException
argument_list|()
throw|;
block|}
block|}
DECL|method|listBindings (String name)
specifier|public
name|NamingEnumeration
argument_list|<
name|Binding
argument_list|>
name|listBindings
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|NamingException
block|{
name|Object
name|o
init|=
name|lookup
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|==
name|this
condition|)
block|{
return|return
name|CastUtils
operator|.
name|cast
argument_list|(
operator|new
name|ListBindingEnumeration
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|Context
condition|)
block|{
return|return
operator|(
operator|(
name|Context
operator|)
name|o
operator|)
operator|.
name|listBindings
argument_list|(
literal|""
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|NotContextException
argument_list|()
throw|;
block|}
block|}
DECL|method|lookupLink (Name name)
specifier|public
name|Object
name|lookupLink
parameter_list|(
name|Name
name|name
parameter_list|)
throws|throws
name|NamingException
block|{
return|return
name|lookupLink
argument_list|(
name|name
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
DECL|method|list (Name name)
specifier|public
name|NamingEnumeration
argument_list|<
name|NameClassPair
argument_list|>
name|list
parameter_list|(
name|Name
name|name
parameter_list|)
throws|throws
name|NamingException
block|{
return|return
name|list
argument_list|(
name|name
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
DECL|method|listBindings (Name name)
specifier|public
name|NamingEnumeration
argument_list|<
name|Binding
argument_list|>
name|listBindings
parameter_list|(
name|Name
name|name
parameter_list|)
throws|throws
name|NamingException
block|{
return|return
name|listBindings
argument_list|(
name|name
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
DECL|method|bind (Name name, Object value)
specifier|public
name|void
name|bind
parameter_list|(
name|Name
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|NamingException
block|{
name|bind
argument_list|(
name|name
operator|.
name|toString
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|bind (String name, Object value)
specifier|public
name|void
name|bind
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|NamingException
block|{
if|if
condition|(
name|isFrozen
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|OperationNotSupportedException
argument_list|()
throw|;
block|}
else|else
block|{
name|internalBind
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|NamingException
block|{
comment|// ignore
block|}
DECL|method|createSubcontext (Name name)
specifier|public
name|Context
name|createSubcontext
parameter_list|(
name|Name
name|name
parameter_list|)
throws|throws
name|NamingException
block|{
throw|throw
operator|new
name|OperationNotSupportedException
argument_list|()
throw|;
block|}
DECL|method|createSubcontext (String name)
specifier|public
name|Context
name|createSubcontext
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|NamingException
block|{
throw|throw
operator|new
name|OperationNotSupportedException
argument_list|()
throw|;
block|}
DECL|method|destroySubcontext (Name name)
specifier|public
name|void
name|destroySubcontext
parameter_list|(
name|Name
name|name
parameter_list|)
throws|throws
name|NamingException
block|{
throw|throw
operator|new
name|OperationNotSupportedException
argument_list|()
throw|;
block|}
DECL|method|destroySubcontext (String name)
specifier|public
name|void
name|destroySubcontext
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|NamingException
block|{
throw|throw
operator|new
name|OperationNotSupportedException
argument_list|()
throw|;
block|}
DECL|method|getNameInNamespace ()
specifier|public
name|String
name|getNameInNamespace
parameter_list|()
throws|throws
name|NamingException
block|{
return|return
name|nameInNamespace
return|;
block|}
DECL|method|getNameParser (Name name)
specifier|public
name|NameParser
name|getNameParser
parameter_list|(
name|Name
name|name
parameter_list|)
throws|throws
name|NamingException
block|{
return|return
name|NAME_PARSER
return|;
block|}
DECL|method|getNameParser (String name)
specifier|public
name|NameParser
name|getNameParser
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|NamingException
block|{
return|return
name|NAME_PARSER
return|;
block|}
DECL|method|rebind (Name name, Object value)
specifier|public
name|void
name|rebind
parameter_list|(
name|Name
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|NamingException
block|{
name|bind
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|rebind (String name, Object value)
specifier|public
name|void
name|rebind
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|NamingException
block|{
name|bind
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|rename (Name oldName, Name newName)
specifier|public
name|void
name|rename
parameter_list|(
name|Name
name|oldName
parameter_list|,
name|Name
name|newName
parameter_list|)
throws|throws
name|NamingException
block|{
throw|throw
operator|new
name|OperationNotSupportedException
argument_list|()
throw|;
block|}
DECL|method|rename (String oldName, String newName)
specifier|public
name|void
name|rename
parameter_list|(
name|String
name|oldName
parameter_list|,
name|String
name|newName
parameter_list|)
throws|throws
name|NamingException
block|{
throw|throw
operator|new
name|OperationNotSupportedException
argument_list|()
throw|;
block|}
DECL|method|unbind (Name name)
specifier|public
name|void
name|unbind
parameter_list|(
name|Name
name|name
parameter_list|)
throws|throws
name|NamingException
block|{
throw|throw
operator|new
name|OperationNotSupportedException
argument_list|()
throw|;
block|}
DECL|method|unbind (String name)
specifier|public
name|void
name|unbind
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|NamingException
block|{
name|bindings
operator|.
name|remove
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|treeBindings
operator|.
name|remove
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
DECL|class|LocalNamingEnumeration
specifier|private
specifier|abstract
class|class
name|LocalNamingEnumeration
implements|implements
name|NamingEnumeration
argument_list|<
name|Object
argument_list|>
block|{
DECL|field|i
specifier|private
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|i
init|=
name|bindings
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
DECL|method|hasMore ()
specifier|public
name|boolean
name|hasMore
parameter_list|()
throws|throws
name|NamingException
block|{
return|return
name|i
operator|.
name|hasNext
argument_list|()
return|;
block|}
DECL|method|hasMoreElements ()
specifier|public
name|boolean
name|hasMoreElements
parameter_list|()
block|{
return|return
name|i
operator|.
name|hasNext
argument_list|()
return|;
block|}
DECL|method|getNext ()
specifier|protected
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getNext
parameter_list|()
block|{
return|return
name|i
operator|.
name|next
argument_list|()
return|;
block|}
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|NamingException
block|{         }
block|}
DECL|class|ListEnumeration
specifier|private
class|class
name|ListEnumeration
extends|extends
name|LocalNamingEnumeration
block|{
DECL|method|ListEnumeration ()
name|ListEnumeration
parameter_list|()
block|{         }
DECL|method|next ()
specifier|public
name|Object
name|next
parameter_list|()
throws|throws
name|NamingException
block|{
return|return
name|nextElement
argument_list|()
return|;
block|}
DECL|method|nextElement ()
specifier|public
name|Object
name|nextElement
parameter_list|()
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
init|=
name|getNext
argument_list|()
decl_stmt|;
return|return
operator|new
name|NameClassPair
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|class|ListBindingEnumeration
specifier|private
class|class
name|ListBindingEnumeration
extends|extends
name|LocalNamingEnumeration
block|{
DECL|method|ListBindingEnumeration ()
name|ListBindingEnumeration
parameter_list|()
block|{         }
DECL|method|next ()
specifier|public
name|Object
name|next
parameter_list|()
throws|throws
name|NamingException
block|{
return|return
name|nextElement
argument_list|()
return|;
block|}
DECL|method|nextElement ()
specifier|public
name|Object
name|nextElement
parameter_list|()
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
init|=
name|getNext
argument_list|()
decl_stmt|;
return|return
operator|new
name|Binding
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|createBean (Class<?> type, Map<String, Object> properties, String prefix)
specifier|protected
specifier|static
name|Object
name|createBean
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|,
name|String
name|prefix
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|value
init|=
name|ObjectHelper
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|value
argument_list|,
name|properties
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
block|}
end_class

end_unit

