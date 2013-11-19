begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|osgi
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
name|atomic
operator|.
name|AtomicInteger
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
name|spi
operator|.
name|CamelContextNameStrategy
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|osgi
operator|.
name|OsgiCamelContextPublisher
operator|.
name|CONTEXT_NAME_PROPERTY
import|;
end_import

begin_comment
comment|/**  * In OSGi we want to use a {@link CamelContextNameStrategy} that finds a free name in the  * OSGi Service Registry to be used for auto assigned names.  *<p/>  * If there is a name clash in the OSGi registry, then a new candidate name is used by appending  * a unique counter.  */
end_comment

begin_class
DECL|class|OsgiCamelContextNameStrategy
specifier|public
class|class
name|OsgiCamelContextNameStrategy
implements|implements
name|CamelContextNameStrategy
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
name|OsgiCamelContextNameStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|CONTEXT_COUNTER
specifier|private
specifier|static
specifier|final
name|AtomicInteger
name|CONTEXT_COUNTER
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
DECL|field|context
specifier|private
specifier|final
name|BundleContext
name|context
decl_stmt|;
DECL|field|prefix
specifier|private
specifier|final
name|String
name|prefix
init|=
literal|"camel"
decl_stmt|;
DECL|field|name
specifier|private
specifier|volatile
name|String
name|name
decl_stmt|;
DECL|method|OsgiCamelContextNameStrategy (BundleContext context)
specifier|public
name|OsgiCamelContextNameStrategy
parameter_list|(
name|BundleContext
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
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
name|getNextName
argument_list|()
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
annotation|@
name|Override
DECL|method|getNextName ()
specifier|public
specifier|synchronized
name|String
name|getNextName
parameter_list|()
block|{
comment|// generate new candidate
name|String
name|candidate
init|=
name|prefix
operator|+
literal|"-"
operator|+
name|getNextCounter
argument_list|()
decl_stmt|;
name|boolean
name|clash
init|=
literal|false
decl_stmt|;
do|do
block|{
try|try
block|{
name|clash
operator|=
literal|false
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Checking OSGi Service Registry for existence of existing CamelContext with name: {}"
argument_list|,
name|candidate
argument_list|)
expr_stmt|;
name|ServiceReference
index|[]
name|refs
init|=
name|context
operator|.
name|getServiceReferences
argument_list|(
name|CamelContext
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"("
operator|+
name|CONTEXT_NAME_PROPERTY
operator|+
literal|"="
operator|+
name|candidate
operator|+
literal|")"
argument_list|)
decl_stmt|;
if|if
condition|(
name|refs
operator|!=
literal|null
operator|&&
name|refs
operator|.
name|length
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|ServiceReference
name|ref
range|:
name|refs
control|)
block|{
name|Object
name|id
init|=
name|ref
operator|.
name|getProperty
argument_list|(
name|CONTEXT_NAME_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
operator|&&
name|candidate
operator|.
name|equals
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|clash
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|InvalidSyntaxException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Error finding free Camel name in OSGi Service Registry due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
do|while
condition|(
name|clash
condition|)
do|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Generated CamelContext name for bundle id: {}, clash: {} -> {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|context
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
block|,
name|clash
block|,
name|name
block|}
argument_list|)
expr_stmt|;
return|return
name|candidate
return|;
block|}
annotation|@
name|Override
DECL|method|isFixedName ()
specifier|public
name|boolean
name|isFixedName
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|getNextCounter ()
specifier|public
specifier|static
name|int
name|getNextCounter
parameter_list|()
block|{
comment|// we want to start counting from 1, so increment first
return|return
name|CONTEXT_COUNTER
operator|.
name|incrementAndGet
argument_list|()
return|;
block|}
block|}
end_class

end_unit

